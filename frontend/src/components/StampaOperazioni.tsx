import React, { useState } from "react";

const StampaOperazioni = () => {
  const [id, setId] = useState("");
  const [operazioni, setOperazioni] = useState<any[]>([]);
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
        `http://localhost:8080/api/report/operazione/${id}`,
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
          setError("Errore: Operazione non trovata.");
        } else {
          setError(`Errore: ${response.status} - ${response.statusText}`);
        }
        setOperazioni([]);
        return;
      }

      const data = await response.json();
      setOperazioni([data]);
      setColumns(["ID Operazione", "Descrizione", "Tipo", "Importo", "Data"]);
      setError(null);
    } catch (error) {
      setError("Errore nella richiesta.");
      console.error("Errore nella richiesta:", error);
      setOperazioni([]);
    }
  };

  const handleReportAll = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSearched(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/report/all-operazioni`,
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
        setOperazioni([]);
        return;
      }

      const data = await response.json();
      setOperazioni(data);
      setColumns(["ID Operazione", "Descrizione", "Tipo", "Importo", "Data"]);
      setError(null);
    } catch (error) {
      setError("Errore nella richiesta.");
      console.error("Errore nella richiesta:", error);
      setOperazioni([]);
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
          style={{ marginRight: 10, width: 30 }}
        />
        <button type="button" onClick={handleReport} style={{ margin: 10 }}>
          Stampa Operazione
        </button>
        <button type="button" onClick={handleReportAll}>
          Stampa tutte le Operazioni
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
                {operazioni.length > 0 ? (
                  operazioni.map((operazione, index) => (
                    <tr key={index}>
                      {columns.includes("ID Operazione") && (
                        <td>{operazione.idOperazione}</td>
                      )}
                      {columns.includes("Descrizione") && (
                        <td>{operazione.descrizione}</td>
                      )}
                      {columns.includes("Tipo") && <td>{operazione.tipo}</td>}
                      {columns.includes("Importo") && (
                        <td>{operazione.importo}</td>
                      )}
                      {columns.includes("Data") && <td>{operazione.data}</td>}
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

export default StampaOperazioni;
