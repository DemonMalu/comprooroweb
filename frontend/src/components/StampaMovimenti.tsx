import React, { useState } from "react";

const StampaMovimenti = () => {
  const [id, setId] = useState("");
  const [movimenti, setMovimenti] = useState<any[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [columns, setColumns] = useState<string[]>([]);
  const [isSearched, setIsSearched] = useState(false);

  const handleIdChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setId(e.target.value);
  };

  const handleReport = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSearched(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/report/movimento/${id}`,
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
          setError("Errore: Movimento non trovato.");
        } else {
          setError(`Errore: ${response.status} - ${response.statusText}`);
        }
        setMovimenti([]);
        return;
      }

      const data = await response.json();
      setMovimenti([data]);
      setColumns([
        "ID Movimento",
        "Data Movimento",
        "Modalità Pagamento",
        "Importo",
        "Username Cliente",
        "Assegno Scannerizzato",
      ]);
      setError(null);
    } catch (error) {
      setError("Errore nella richiesta.");
      console.error("Errore nella richiesta:", error);
      setMovimenti([]);
    }
  };

  const handleReportAll = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSearched(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/report/all-movimenti`,
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
        setMovimenti([]);
        return;
      }

      const data = await response.json();
      setMovimenti(data);
      setColumns([
        "ID Movimento",
        "Data Movimento",
        "Modalità Pagamento",
        "Importo",
        "Username Cliente",
        "Assegno Scannerizzato",
      ]);
      setError(null);
    } catch (error) {
      setError("Errore nella richiesta.");
      console.error("Errore nella richiesta:", error);
      setMovimenti([]);
    }
  };

  return (
    <>
      <div onSubmit={handleReport} className="report-form">
        <label style={{ margin: 10 }}>ID:</label>
        <input
          type="number"
          value={id}
          onChange={handleIdChange}
          style={{ margin: 10, width: 30 }}
        />
        <button type="button" onClick={handleReport} style={{ margin: 10 }}>
          Stampa Movimento
        </button>
        <button type="button" onClick={handleReportAll} style={{ margin: 10 }}>
          Stampa tutti i Movimenti
        </button>
        <br /> <br />
        {isSearched && (
          <div className="results">
            <table>
              <thead>
                <tr>
                  {columns.map((col, index) => (
                    <th key={index}>{col}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {movimenti.length > 0 ? (
                  movimenti.map((movimento, index) => (
                    <tr key={index}>
                      {columns.includes("ID Movimento") && (
                        <td>{movimento.idMovimento}</td>
                      )}
                      {columns.includes("Data Movimento") && (
                        <td>{movimento.data}</td>
                      )}
                      {columns.includes("Importo") && (
                        <td>{movimento.importo}</td>
                      )}
                      {columns.includes("Modalità Pagamento") && (
                        <td>{movimento.modalitaPagamento}</td>
                      )}
                      {columns.includes("Username Cliente") && (
                        <td>{movimento.username}</td>
                      )}
                      {columns.includes("Assegno Scannerizzato") && (
                        <td>
                          {movimento.assegnoScannerizzato ? (
                            <a
                              href={`data:${movimento.contentType};base64,${movimento.assegnoScannerizzato}`}
                              download="assegno_scannerizzato"
                            >
                              Scarica Assegno Scan.
                            </a>
                          ) : (
                            "Assegno non disponibile"
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

export default StampaMovimenti;
