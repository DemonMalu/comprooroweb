import React, { useState } from "react";

const StampaUtenti = () => {
  const [username, setUsername] = useState("");
  const [utente, setUtente] = useState<any[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [columns, setColumns] = useState<string[]>([]);
  const [isSearched, setIsSearched] = useState(false);

  const handleUsername = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUsername(e.target.value);
  };

  const handleReport = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSearched(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/report/ricerca-utente/${username}`,
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
          setError("Errore: Utente non trovato.");
        } else {
          setError(`Errore: ${response.status} - ${response.statusText}`);
        }
        setUtente([]);
        return;
      }

      const data = await response.json();
      setUtente([data]);
      setColumns([
        "Username",
        "Nome",
        "Cognome",
        "Data di Nascita",
        "Città",
        "Email",
        "Codice Fiscale",
        "Indirizzo",
        "Ruolo",
        "Documento Scannerizzato",
      ]);
      setError(null);
    } catch (error) {
      setError("Errore nella richiesta.");
      console.error("Errore nella richiesta:", error);
      setUtente([]);
    }
  };

  const handleReportAll = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSearched(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/report/all-utenti`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
        }
      );

      if (!response.ok) {
        setError(`Errore: ${response.status} - ${response.statusText}`);
        setUtente([]);
        return;
      }

      const data = await response.json();
      setUtente(data);
      setColumns([
        "Username",
        "Nome",
        "Cognome",
        "Data di Nascita",
        "Città",
        "Email",
        "Codice Fiscale",
        "Indirizzo",
        "Ruolo",
        "Documento Scannerizzato",
      ]);
      setError(null);
    } catch (error) {
      setError("Errore nella richiesta.");
      console.error("Errore nella richiesta:", error);
      setUtente([]);
    }
  };

  return (
    <>
      <div onSubmit={handleReport} className="report-form">
        <label style={{ margin: 10 }}>Username:</label>
        <input
          type="text"
          value={username}
          onChange={handleUsername}
          style={{ margin: 10 }}
        />
        <button type="button" onClick={handleReport} style={{ margin: 10 }}>
          Stampa Utente
        </button>
        <button type="button" onClick={handleReportAll}>
          Stampa tutti gli Utenti
        </button>
        <br /> <br />
        {isSearched && (
          <div className="results">
            <table style={{ width: "100%", borderCollapse: "collapse" }}>
              <thead>
                <tr>
                  {columns.map((col, index) => (
                    <th
                      key={index}
                      style={{
                        border: "1px solid #ddd",
                        padding: "8px",
                        textAlign: "center",
                      }}
                    >
                      {col}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {utente.length > 0 ? (
                  utente.map((user, index) => (
                    <tr key={index}>
                      {columns.includes("Username") && <td>{user.username}</td>}
                      {columns.includes("Nome") && <td>{user.nome}</td>}
                      {columns.includes("Cognome") && <td>{user.cognome}</td>}
                      {columns.includes("Data di Nascita") && (
                        <td>{user.dataNascita}</td>
                      )}
                      {columns.includes("Città") && <td>{user.citta}</td>}
                      {columns.includes("Email") && <td>{user.email}</td>}
                      {columns.includes("Codice Fiscale") && (
                        <td>{user.codiceFiscale}</td>
                      )}
                      {columns.includes("Indirizzo") && (
                        <td>{user.indirizzo}</td>
                      )}
                      {columns.includes("Ruolo") && <td>{user.ruolo}</td>}
                      {columns.includes("Documento Scannerizzato") && (
                        <td>
                          {user.documentoScannerizzato ? (
                            <a
                              href={`data:${user.contentType};base64,${user.documentoScannerizzato}`}
                              download="documento_scannerizzato"
                            >
                              Scarica Documento Scan.
                            </a>
                          ) : (
                            "Documento non disponibile"
                          )}
                        </td>
                      )}
                    </tr>
                  ))
                ) : (
                  <tr>
                    {error && (
                      <td colSpan={columns.length} style={{ color: "red" }}>
                        {error}
                      </td>
                    )}
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </>
  );
};

export default StampaUtenti;
