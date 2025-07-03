import OperazioneForm from "../components/OperazioneForm";
import BilancioCalculator from "../components/BilancioCalculator";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const HomeAdmin = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
  const navigate = useNavigate();

  const checkAuthStatus = async () => {
    try {
      const response = await fetch(
        "http://localhost:8080/api/home/amministratore/profilo",
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
    document.title = "Home Amministratore - Compro Oro";
  }, []);

  useEffect(() => {
    if (isAuthenticated === false) {
      navigate("/");
    }
  }, [isAuthenticated, navigate]);

  return (
    <div className="home-admin-container">
      <h1 className="home-admin-title">👑 Home Amministratore</h1>

      <div className="admin-content-container">
        <div className="box">
          <h2>Inserimento Operazione di Cassa</h2>
          <OperazioneForm />
        </div>

        <div className="box">
          <h2>Calcolo Bilancio</h2>
          <BilancioCalculator />
        </div>
      </div>

      <div className="footer">
        <p>
          Per stampare le informazioni relative ad un utente, un movimento, un
          articolo o un operazione:{" "}
          <button
            onClick={(e) => {
              if (e.ctrlKey || e.metaKey) {
                window.open("/api/report", "_blank"); // Apre in una nuova scheda
              } else {
                window.location.href = "/api/report"; // Stessa scheda
              }
            }}
          >
            Stampa
          </button>
        </p>
      </div>
    </div>
  );
};

export default HomeAdmin;
