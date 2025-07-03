import { useState } from "react";

interface Operazione {
  importo: number;
  descrizione: string;
  data: string;
  tipo: number; // 1 per entrata, 0 per uscita
  username: string;
}

const OperazioneForm = () => {
  const [operazione, setOperazione] = useState<Operazione>({
    importo: 1,
    descrizione: "",
    data: "",
    tipo: 0,
    username: "Admin",
  });
  const [isTipoLocked, setIsTipoLocked] = useState(true); // Per bloccare il tipo in base alla descrizione

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setOperazione((prev) => ({
      ...prev,
      [name]: name === "importo" ? parseFloat(value) || 1 : value,
    }));
  };

  const handleDescrizioneChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const value = e.target.value;
    setOperazione((prev) => ({
      ...prev,
      descrizione: value,
      tipo:
        value === "bolletta-energia" ||
        value === "bolletta-gas" ||
        value === "bolletta-acqua" ||
        value === "bolletta-telefonia" ||
        value === "bolletta-internet"
          ? 0 // Tipo = uscita per bollette
          : value === "vendita"
          ? 1 // Tipo = entrata per vendita
          : prev.tipo,
    }));
    setIsTipoLocked(value !== "altro"); // Blocca tipo se non è "altro"
  };

  const handleTipoChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    if (!isTipoLocked) {
      setOperazione((prev) => ({
        ...prev,
        tipo: e.target.value === "entrata" ? 1 : 0,
      }));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      // Formatta la data in yyyy-MM-dd (senza ora)
      const formattedDate = operazione.data.split("T")[0];

      const response = await fetch(
        "http://localhost:8080/api/home/amministratore/register-operazione",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            ...operazione,
            data: formattedDate, // Invia solo la data, senza l'ora
            username: "Admin", // Username fisso
          }),
          credentials: "include",
        }
      );

      if (response.ok) {
        console.info("Operazione salvata con successo!");
        alert("Operazione salvata con successo!");
        setOperazione({
          importo: 1,
          descrizione: "",
          data: "",
          tipo: 0,
          username: "Admin",
        });
      } else {
        console.error("Errore nel salvataggio dell'operazione.");
      }
    } catch (error) {
      console.error("Errore nella richiesta:", error);
    }
  };

  return (
    <div className="operation-form-container">
      <form onSubmit={handleSubmit} className="operation-form">
        <label style={{ marginRight: 10 }}>Importo:</label>
        <input
          type="number"
          name="importo"
          min={1}
          value={operazione.importo}
          onChange={handleInputChange}
          required
        />{" "}
        <br /> <br />
        <label style={{ marginRight: 10 }}>Descrizione:</label>
        <select
          name="descrizione"
          value={operazione.descrizione}
          onChange={handleDescrizioneChange}
          required
        >
          <option value="bolletta-energia">Bolletta Energia</option>
          <option value="bolletta-gas">Bolletta Gas</option>
          <option value="bolletta-acqua">Bolletta Acqua</option>
          <option value="bolletta-telefonia">Bolletta Telefonia</option>
          <option value="bolletta-internet">Bolletta Internet</option>
          <option value="vendita">Vendita</option>
          <option value="altro">Altro</option>
        </select>{" "}
        <br /> <br />
        <label style={{ marginRight: 10 }}>Data:</label>
        <input
          type="date"
          name="data"
          value={operazione.data}
          onChange={handleInputChange}
          required
        />{" "}
        <br /> <br />
        <label style={{ marginRight: 10 }}>Tipo:</label>
        <select
          name="tipo"
          value={operazione.tipo === 1 ? "entrata" : "uscita"}
          onChange={handleTipoChange}
          disabled={isTipoLocked}
          required
        >
          <option value="entrata">Entrata</option>
          <option value="uscita">Uscita</option>
        </select>{" "}
        <br /> <br />
        <button
          type="submit"
          disabled={operazione.importo <= 0 || !operazione.data}
        >
          Salva
        </button>
      </form>{" "}
    </div>
  );
};

export default OperazioneForm;
