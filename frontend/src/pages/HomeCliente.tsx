import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUserCircle } from "@fortawesome/free-solid-svg-icons";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const HomeCliente = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [clienteData, setClienteData] = useState<any>(null);
  const [storico, setStorico] = useState<any[]>([]);
  const [isStoricoLoaded, setIsStoricoLoaded] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
  const navigate = useNavigate();
  const checkAuthStatus = async () => {
    try {
      const response = await fetch(
        "http://localhost:8080/api/home/cliente/profilo",
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
    document.title = "Home Cliente - Compro Oro";
  }, []);

  useEffect(() => {
    if (isAuthenticated === false) {
      navigate("/");
    }
  }, [isAuthenticated, navigate]);

  const handleProfileClick = async () => {
    const response = await fetch(
      "http://localhost:8080/api/home/cliente/profilo",
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );
    const data = await response.json();
    setClienteData(data);
    setIsModalOpen(true); // Apre il modal
  };

  const closeModal = () => {
    setIsModalOpen(false); // Chiude il modal
  };

  const handleStoricoClick = async () => {
    setTimeout(async () => {
      if (!isStoricoLoaded) {
        const response = await fetch(
          "http://localhost:8080/api/home/cliente/storico",
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
            },
            credentials: "include",
          }
        );
        const data = await response.json();
        setStorico(data);
        setIsStoricoLoaded(true);
      }
    }, 50); // Attende 50ms per verificare se è stato un double-click
  };

  const handleStoricoDoubleClick = () => {
    setStorico([]);
    setIsStoricoLoaded(false);
  };

  if (isAuthenticated === null) {
    return <div>Loading...</div>;
  }

  return (
    <div className="home-cliente-container">
      <h1 className="home-cliente-title">
        Home Cliente{" "}
        <FontAwesomeIcon
          icon={faUserCircle}
          onClick={handleProfileClick}
          style={{ cursor: "pointer", marginLeft: "10px", fontSize: "3rem" }}
        />
      </h1>

      <div className="storico-container">
        <button
          onClick={handleStoricoClick}
          onDoubleClick={handleStoricoDoubleClick}
        >
          Visualizza Storico Movimenti
        </button>

        {isStoricoLoaded && storico.length === 0 && (
          <p>Nessun movimento trovato.</p>
        )}

        {storico.length > 0 && (
          <>
            <table>
              <thead>
                <tr>
                  <th>Data</th>
                  <th>Modalità Pagamento</th>
                  <th>Importo</th>
                  <th>Assegno Scannerizzato</th>
                </tr>
              </thead>
              <tbody>
                {storico.map((movimento, index) => (
                  <tr key={index}>
                    <td>{movimento.data}</td>
                    <td>{movimento.modalitaPagamento}</td>
                    <td>{movimento.importo}</td>
                    <td>
                      <a
                        href={`data:${movimento.contentType};base64,${movimento.assegnoScannerizzato}`}
                        download="assegno_scannerizzato"
                      >
                        Scarica Assegno Scan.
                      </a>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </>
        )}
      </div>

      {/* Modal */}
      {isModalOpen && clienteData && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <h3>Profilo Cliente</h3>
            <p>
              <strong>Username:</strong> {clienteData.username}
            </p>
            <p>
              <strong>Nome:</strong> {clienteData.nome}
            </p>
            <p>
              <strong>Cognome:</strong> {clienteData.cognome}
            </p>
            <p>
              <strong>Data di Nascita:</strong> {clienteData.dataNascita}
            </p>
            <p>
              <strong>Città:</strong> {clienteData.citta}
            </p>
            <p>
              <strong>Email:</strong> {clienteData.email}
            </p>
            <p>
              <strong>Codice Fiscale:</strong> {clienteData.codiceFiscale}
            </p>
            <p>
              <strong>Indirizzo:</strong> {clienteData.indirizzo}
            </p>
            <p>
              <strong>Assegno Scannerizzato:</strong>{" "}
              <a
                href={`data:${clienteData.contentType};base64,${clienteData.documentoScannerizzato}`}
                download="documento_scannerizzato"
              >
                Scarica Documento.
              </a>
            </p>
          </div>
        </div>
      )}
    </div>
  );
};

export default HomeCliente;
