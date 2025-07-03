import { useState } from "react";

const BilancioCalculator = () => {
  const [bilancioData, setBilancioData] = useState("");
  const [bilancioResult, setBilancioResult] = useState<{
    entrate: number;
    uscite: number;
    rimanenze: number;
  } | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleDataChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setBilancioData(e.target.value);
  };

  const handleCalcolaBilancio = async () => {
    setError(null); // Reset dell'errore precedente
    try {
      const response = await fetch(
        `http://localhost:8080/api/home/amministratore/bilancio/${bilancioData}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
        }
      );

      const responseText = await response.text();
      console.log(responseText);

      if (!response.ok) {
        setError(`Errore: ${response.status} - ${response.statusText}`);
        return;
      }

      try {
        const data = JSON.parse(responseText);

        setBilancioResult({
          entrate:
            data.totaleEntrate && !isNaN(data.totaleEntrate)
              ? data.totaleEntrate
              : 0,
          uscite:
            data.totaleUscite && !isNaN(data.totaleUscite)
              ? data.totaleUscite
              : 0,
          rimanenze:
            data.totaleRimanenze && !isNaN(data.totaleRimanenze)
              ? data.totaleRimanenze
              : 0,
        });
      } catch (e) {
        setError("La risposta non è in formato JSON valido.");
      }
    } catch (error) {
      setError("Errore nella richiesta.");
      console.error("Errore nella richiesta:", error);
    }
  };

  return (
    <div className="bilancio-container">
      <label style={{ marginRight: 10 }}>Data:</label>
      <input
        type="date"
        value={bilancioData}
        onChange={handleDataChange}
        required
      />{" "}
      <br /> <br />
      <button type="button" onClick={handleCalcolaBilancio}>
        Calcola Bilancio
      </button>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {bilancioResult && (
        <div className="bilancio-results" style={{ fontSize: "1.2rem" }}>
          <p>Bilancio in data {bilancioData}:</p>
          <p>Entrate: {bilancioResult.entrate} €</p>
          <p>Uscite: {bilancioResult.uscite} €</p>
          <p>Rimanenze: {bilancioResult.rimanenze} €</p>
        </div>
      )}
    </div>
  );
};

export default BilancioCalculator;
