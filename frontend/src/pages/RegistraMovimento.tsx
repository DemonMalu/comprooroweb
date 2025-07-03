import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import RegistraArticolo from "../components/RegistraArticolo";

interface Movimento {
  data: string;
  modalitaPagamento: string;
  importo: number;
  username: string;
}

const RegistraMovimento = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
  const navigate = useNavigate();
  const [idMovimento, setIdMovimento] = useState<number | null>(null);
  const [showArticoliForm, setShowArticoliForm] = useState<boolean>(false);
  const [showMovimentiForm, setShowMovimentiForm] = useState<boolean>(true);
  const [maxArticoli, setMaxArticoli] = useState<number>(1);
  const [articoli, setArticoli] = useState<any[]>([]);

  const handleArticoloInserito = (articolo: any) => {
    setArticoli((prevArticoli) => [...prevArticoli, articolo]); // Aggiungi l'articolo alla lista
  };

  const [movimento, setMovimento] = useState<Movimento>({
    data: "",
    modalitaPagamento: "",
    importo: 1,
    username: "",
  });

  const [usernameValido, setUsernameValido] = useState<boolean | null>(null);
  const [file, setFile] = useState<File | null>(null);

  useEffect(() => {
    const checkAuthStatus = async () => {
      try {
        const response = await fetch(
          "http://localhost:8080/api/home/operatore/profilo",
          {
            method: "GET",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
          }
        );
        setIsAuthenticated(response.status === 200);
      } catch {
        setIsAuthenticated(false);
      }
    };

    checkAuthStatus();
    document.title = "Registra Movimento - Compro Oro";
  }, []);

  useEffect(() => {
    if (isAuthenticated === false) navigate("/");
  }, [isAuthenticated, navigate]);

  const verificaUsername = async (username: string) => {
    if (!username) return;
    try {
      const response = await fetch(
        `http://localhost:8080/api/report/exist-username/${username}`,
        {
          method: "GET",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
        }
      );

      if (!response.ok) {
        throw new Error("Errore nella richiesta al backend");
      }

      const usernameEsistente = await response.json();

      if (usernameEsistente) {
        setUsernameValido(true);
        console.info("Username presente!");
      } else {
        setUsernameValido(false);
        console.error("Username inesistente o non valido!");
      }
    } catch (error) {
      console.error("Errore nella verifica dello username:", error);
      setUsernameValido(false);
    }
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setMovimento((prev) => ({
      ...prev,
      [name]: name === "importo" ? parseFloat(value) || 1 : value,
    }));

    if (name === "maxArticoli") {
      setMaxArticoli(parseFloat(value) || 1);
    }

    if (name === "username") verificaUsername(value);
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setFile(e.target.files[0]);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!usernameValido) {
      console.error("Errore: Username non valido o inesistente.");
      return;
    }

    const formData = new FormData();
    formData.append("data", movimento.data);
    formData.append("modalitaPagamento", movimento.modalitaPagamento);
    formData.append("importo", movimento.importo.toString());
    formData.append("username", movimento.username);
    formData.append(
      "movimento",
      new Blob([JSON.stringify(movimento)], { type: "application/json" })
    );

    if (file) formData.append("assegno", file);

    try {
      const response = await fetch(
        "http://localhost:8080/api/home/operatore/register-movimento",
        {
          method: "POST",
          body: formData,
          credentials: "include",
        }
      );

      if (response.ok) {
        const responseData = await response.json();
        console.info("Movimento registrato con successo.");
        alert("Movimento registrato con successo.");
        setIdMovimento(responseData.idMovimento);
        setShowArticoliForm(true);
        setShowMovimentiForm(false);
        setMovimento({
          data: "",
          modalitaPagamento: "",
          importo: 1,
          username: "",
        });
        setFile(null);
      } else {
        const errorData = await response.json();
        console.error(
          "Errore: " +
            (errorData.message || "Impossibile completare l'operazione")
        );
      }
    } catch (error) {
      console.error("Errore nella richiesta: " + error);
    }
  };

  useEffect(() => {
    if (maxArticoli === articoli.length) {
      generaDichiarazioneVendita();
    }
  }, [articoli, maxArticoli]);

  const generaDichiarazioneVendita = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/report/dichiarazione/${idMovimento}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            movimentoId: idMovimento,
            articoli: articoli,
          }),
          credentials: "include",
        }
      );

      if (response.ok) {
        const contentType = response.headers.get("Content-Type");

        if (contentType && contentType.includes("application/pdf")) {
          const blob = await response.blob();
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement("a");
          link.href = url;
          link.download = "dichiarazione-vendita.pdf";
          link.click();
          console.info("Dichiarazione di vendita generata con successo.");
          navigate("/api/home/operatore");
        } else {
          const responseData = await response.json();
          console.error(
            "Errore nella generazione della dichiarazione: " +
              responseData.message
          );
        }
      } else {
        const errorData = await response.json();
        console.error(
          "Errore nella generazione della dichiarazione: " + errorData.message
        );
      }
    } catch (error) {
      console.error("Errore nella richiesta: " + error);
    }
  };

  return (
    <div className="movimento-register-container">
      {showMovimentiForm && (
        <>
          <h1 className="register-movimento-title">Registra Movimento</h1>
          <form onSubmit={handleSubmit} className="movimento-form">
            <label>Data: </label>
            <input
              type="date"
              name="data"
              value={movimento.data}
              onChange={handleChange}
              required
            />
            <br />
            <br />
            <label>Modalità di Pagamento: </label>
            <select
              name="modalitaPagamento"
              value={movimento.modalitaPagamento}
              onChange={handleChange}
              required
            >
              <option value="">Seleziona una modalità</option>
              <option value="Contanti">Contanti</option>
              <option value="Bonifico">Bonifico</option>
              <option value="Carta">Carta</option>
            </select>
            <br />
            <br />
            <label>Importo: </label>
            <input
              type="number"
              name="importo"
              min={1}
              value={movimento.importo}
              onChange={handleChange}
              required
            />
            <br />
            <br />
            <label>Username associato: </label>
            <input
              type="text"
              name="username"
              value={movimento.username}
              onChange={handleChange}
              required
              className={`input ${
                movimento.username.length > 0 && !usernameValido
                  ? "input-error"
                  : ""
              }`}
            />
            {!movimento.username ||
              (usernameValido === false && (
                <>
                  <br />
                  <p className="error-message">
                    Username inesistente o non valido.
                  </p>
                </>
              ))}
            <br />
            <br />
            <label>Numero massimo Articoli associati:</label>
            <input
              type="number"
              name="maxArticoli"
              min={1}
              value={maxArticoli}
              onChange={(e) => setMaxArticoli(parseInt(e.target.value) || 1)}
            />
            <br />
            <br />
            <input
              type="file"
              id="file-upload"
              onChange={handleFileChange}
              style={{ display: "none" }}
            />
            <label
              htmlFor="file-upload"
              className="file-upload-button"
              style={{ float: "none" }}
            >
              {file ? file.name : "Carica un file"}
            </label>
            <br />
            <br />
            <button
              type="submit"
              className="save-button"
              disabled={
                !usernameValido ||
                !movimento.username ||
                !movimento.data ||
                !movimento.modalitaPagamento ||
                !movimento.importo ||
                !file
              }
            >
              Salva Movimento
            </button>
          </form>
          <br />
          <br />
        </>
      )}
      {showArticoliForm && idMovimento && maxArticoli > articoli.length && (
        <RegistraArticolo
          idMovimento={idMovimento}
          maxArticoli={maxArticoli}
          onArticoloInserito={handleArticoloInserito}
        />
      )}
    </div>
  );
};

export default RegistraMovimento;
