import React, { useState } from "react";

const StampaArticoli = () => {
  const [id, setId] = useState("");
  const [articoli, setArticoli] = useState<any[]>([]);
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
        `http://localhost:8080/api/report/articolo/${id}`,
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
          setError("Errore: Articolo non trovato.");
        } else {
          setError(`Errore: ${response.status} - ${response.statusText}`);
        }
        setArticoli([]);
        return;
      }

      const data = await response.json();
      setArticoli([data]);
      setColumns([
        "ID Articolo",
        "Nome Articolo",
        "Descrizione",
        "Grammi",
        "Caratura",
        "ID Movimento",
        "Foto 1",
        "Foto 2",
      ]);
      setError(null);
    } catch (error) {
      setError("Errore nella richiesta.");
      console.error("Errore nella richiesta:", error);
      setArticoli([]);
    }
  };

  const handleReportAll = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSearched(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/report/all-articoli`,
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
        setArticoli([]);
        return;
      }

      const data = await response.json();
      setArticoli(data);
      setColumns([
        "ID Articolo",
        "Nome Articolo",
        "Descrizione",
        "Grammi",
        "Caratura",
        "ID Movimento",
        "Foto 1",
        "Foto 2",
      ]);
      setError(null);
    } catch (error) {
      setError("Errore nella richiesta.");
      console.error("Errore nella richiesta:", error);
      setArticoli([]);
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
          Stampa Articolo
        </button>
        <button type="button" onClick={handleReportAll}>
          Stampa tutti gli Articoli
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
                {articoli.length > 0 ? (
                  articoli.map((articolo, index) => (
                    <tr key={index}>
                      {columns.includes("ID Articolo") && (
                        <td>{articolo.idArticolo}</td>
                      )}
                      {columns.includes("Nome Articolo") && (
                        <td>{articolo.nome}</td>
                      )}
                      {columns.includes("Descrizione") && (
                        <td>{articolo.descrizione}</td>
                      )}
                      {columns.includes("Grammi") && <td>{articolo.grammi}</td>}
                      {columns.includes("Caratura") && (
                        <td>{articolo.caratura}</td>
                      )}
                      {columns.includes("ID Movimento") && (
                        <td>{articolo.idMovimento}</td>
                      )}
                      {columns.includes("Foto 1") && (
                        <td>
                          {articolo.foto1 ? (
                            <a
                              href={`data:${articolo.contentType};base64,${articolo.foto1}`}
                              download="foto_1"
                            >
                              Scarica Foto 1
                            </a>
                          ) : (
                            "Foto non disponibile"
                          )}
                        </td>
                      )}
                      {columns.includes("Foto 2") && (
                        <td>
                          {articolo.foto2 ? (
                            <a
                              href={`data:${articolo.contentType};base64,${articolo.foto2}`}
                              download="foto_2"
                            >
                              Scarica Foto 2
                            </a>
                          ) : (
                            "Foto non disponibile"
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

export default StampaArticoli;
