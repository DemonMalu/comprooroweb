import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const RicercaCliente = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
  const navigate = useNavigate();
  const [username, setUsername] = useState<string>("");
  const [cliente, setCliente] = useState<any | null>(null);
  const [error, setError] = useState<string | null>(null);

  const checkAuthStatus = async () => {
    try {
      const response = await fetch(
        "http://localhost:8080/api/home/operatore/profilo",
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
        }
      );

      if (response.status === 200) {
        setIsAuthenticated(true);
      } else {
        setIsAuthenticated(false);
      }
    } catch (error) {
      setIsAuthenticated(false);
    }
  };

  useEffect(() => {
    checkAuthStatus();
    document.title = "Ricerca Cliente - Compro Oro";
  }, []);

  useEffect(() => {
    if (isAuthenticated === false) {
      navigate("/");
    }
  }, [isAuthenticated, navigate]);

  const handleUsername = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUsername(e.target.value);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch(
        `http://localhost:8080/api/report/ricerca-cliente/${username}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
        }
      );

      if (!response.ok) {
        if (response.status === 404) {
          setError("Errore: Cliente non trovato.");
        } else {
          setError(`Errore: ${response.status} - ${response.statusText}`);
        }
        setCliente(null);
        return;
      }

      const data = await response.json();
      setCliente(data);
      setError(null);
    } catch (error) {
      setError("Errore nella richiesta.");
      console.error("Errore nella richiesta:", error);
      setCliente(null);
    }
  };

  return (
    <div className="client-ricerca-container">
      <h1 className="ricerca-client-title">Ricerca Cliente</h1>
      <form onSubmit={handleSubmit} className="client-form">
        <label style={{ marginRight: 10 }}>Username: </label>
        <input
          type="text"
          name="username"
          value={username}
          onChange={handleUsername}
          required
        />{" "}
        <br /> <br />
        <button type="submit" disabled={!username}>
          Cerca
        </button>
      </form>

      {error && <p style={{ color: "red" }}>{error}</p>}

      {cliente && (
        <div className="cliente-details">
          <p>
            <strong>Username:</strong> {cliente.username}
          </p>
          <p>
            <strong>Nome:</strong> {cliente.nome}
          </p>
          <p>
            <strong>Cognome:</strong> {cliente.cognome}
          </p>
          <p>
            <strong>Data di Nascita:</strong> {cliente.dataNascita}
          </p>
          <p>
            <strong>Città:</strong> {cliente.citta}
          </p>
          <p>
            <strong>Email:</strong> {cliente.email}
          </p>
          <p>
            <strong>Codice Fiscale:</strong> {cliente.codiceFiscale}
          </p>
          <p>
            <strong>Indirizzo:</strong> {cliente.indirizzo}
          </p>
          {cliente.documentoScannerizzato && (
            <div>
              <p>
                <strong>Documento Scannerizzato:</strong>
              </p>
              <img
                src={`data:${cliente.contentType};base64,${cliente.documentoScannerizzato}`}
                alt="Documento Scannerizzato"
                style={{ maxWidth: "500px" }}
              />
              <br />
              <a
                href={`data:${cliente.contentType};base64,${cliente.documentoScannerizzato}`}
                download="documento_scannerizzato"
              >
                Scarica Documento
              </a>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default RicercaCliente;
