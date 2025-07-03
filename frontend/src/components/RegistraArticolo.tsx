import { useState, useEffect } from "react";

interface Articolo {
  nome: string;
  descrizione: string;
  caratura: string;
  grammi: number;
  idMovimento: number;
}

interface RegistraArticoloProps {
  idMovimento: number;
  maxArticoli: number;
  onArticoloInserito: (articolo: Articolo) => void;
}

const RegistraArticolo = ({
  idMovimento,
  onArticoloInserito,
}: RegistraArticoloProps) => {
  const [articolo, setArticolo] = useState<Articolo>({
    nome: "",
    descrizione: "",
    caratura: "18",
    grammi: 0,
    idMovimento: idMovimento,
  });

  const [file1, setFile1] = useState<File | null>(null);
  const [file2, setFile2] = useState<File | null>(null);

  useEffect(() => {
    setArticolo((prev) => ({ ...prev, idMovimento }));
  }, [idMovimento]);

  const isCapitalized = (str: string) => /^[A-ZÀ-Ý]/.test(str);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setArticolo((prev) => ({
      ...prev,
      [name]: name === "grammi" ? parseFloat(value) || 0 : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!articolo.nome || !articolo.caratura || articolo.grammi <= 0) {
      console.error("Compila tutti i campi richiesti.");
      return;
    }

    if (
      !isCapitalized(articolo.nome) ||
      (articolo.descrizione && !isCapitalized(articolo.descrizione))
    ) {
      console.error(
        "Nome e Descrizione devono iniziare con una lettera maiuscola."
      );
      return;
    }

    const formData = new FormData();
    formData.append("nome", articolo.nome);
    formData.append("descrizione", articolo.descrizione);
    formData.append("caratura", articolo.caratura);
    formData.append("grammi", articolo.grammi.toString());
    formData.append("idMovimento", articolo.idMovimento.toString());
    formData.append(
      "articolo",
      new Blob([JSON.stringify(articolo)], { type: "application/json" })
    );

    if (file1) formData.append("foto1", file1);
    if (file2) formData.append("foto2", file2);

    try {
      const response = await fetch(
        "http://localhost:8080/api/home/operatore/register-articolo",
        {
          method: "POST",
          body: formData,
          credentials: "include",
        }
      );

      if (response.ok) {
        console.info("Articolo registrato con successo.");
        alert("Articolo registrato con successo.");
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `cartellino_articolo.pdf`);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);

        setArticolo({
          nome: "",
          descrizione: "",
          caratura: "18",
          grammi: 0,
          idMovimento: idMovimento,
        });
        setFile1(null);
        setFile2(null);
        onArticoloInserito(articolo);
      } else {
        const errorData = await response.json();
        console.error(
          "Errore: " +
            (errorData.message || "Impossibile completare l'operazione")
        );
      }
    } catch (error) {
      console.error("Errore nella richiesta:", error);
      console.error("Errore durante la registrazione dell'articolo.");
    }
  };

  return (
    <div className="articolo-register-container">
      <h1 className="articolo-register-title">Registra Articolo</h1>
      <form onSubmit={handleSubmit} className="articolo-form">
        <label>Nome Articolo:</label>
        <input
          type="text"
          name="nome"
          value={articolo.nome}
          onChange={handleChange}
          required
          className={`input ${
            articolo.nome.length > 0 && !isCapitalized(articolo.nome)
              ? "input-error"
              : ""
          }`}
        />
        {!articolo.nome ||
          (isCapitalized(articolo.nome) === false && (
            <>
              <br />
              <p className="error-message">
                Il nome deve iniziare con una lettera maiuscola.
              </p>
            </>
          ))}
        <br /> <br />
        <label>Descrizione Articolo:</label>
        <input
          type="text"
          name="descrizione"
          value={articolo.descrizione}
          onChange={handleChange}
          className={`input ${
            articolo.descrizione.length > 0 &&
            !isCapitalized(articolo.descrizione)
              ? "input-error"
              : ""
          }`}
        />
        {!articolo.descrizione ||
          (isCapitalized(articolo.descrizione) === false && (
            <>
              <br />
              <p className="error-message">
                La descrizione deve iniziare con una lettera maiuscola.
              </p>
            </>
          ))}
        <br /> <br />
        <label>Caratura Articolo:</label>
        <select
          name="caratura"
          value={articolo.caratura}
          onChange={handleChange}
          required
        >
          {["18", "8", "9", "14", "21", "21.6", "22", "23.6", "24"].map(
            (caratura) => (
              <option key={caratura} value={caratura}>
                {caratura}KT
              </option>
            )
          )}
          <option value="argento">Argento</option>
        </select>
        <br /> <br />
        <label>Grammi Articolo:</label>
        <input
          type="number"
          name="grammi"
          min={0}
          value={articolo.grammi}
          onChange={handleChange}
          required
        />
        <br /> <br />
        <input
          type="file"
          id="file-upload-1"
          onChange={(e) => setFile1(e.target.files?.[0] || null)}
          style={{ display: "none" }}
        />
        <label
          htmlFor="file-upload-1"
          className="file-upload-button"
          style={{ float: "none" }}
        >
          {file1 ? file1.name : "Carica una foto"}
        </label>
        <br /> <br />
        <input
          type="file"
          id="file-upload-2"
          onChange={(e) => setFile2(e.target.files?.[0] || null)}
          style={{ display: "none" }}
        />
        <label
          htmlFor="file-upload-2"
          className="file-upload-button"
          style={{ float: "none" }}
        >
          {file2 ? file2.name : "Carica una foto"}
        </label>
        <br /> <br />
        <button
          type="submit"
          disabled={
            !articolo.nome ||
            articolo.grammi <= 0 ||
            !articolo.caratura ||
            !articolo.grammi ||
            !isCapitalized(articolo.nome) ||
            (!articolo.descrizione
              ? false
              : !isCapitalized(articolo.descrizione))
          }
          className="save-button"
        >
          Salva Articolo
        </button>
      </form>
    </div>
  );
};

export default RegistraArticolo;
