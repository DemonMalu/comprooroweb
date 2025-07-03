import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const HomeOperatore = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
  const navigate = useNavigate();

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
    document.title = "Home Operatore - Compro Oro";
  }, []);

  useEffect(() => {
    if (isAuthenticated === false) {
      navigate("/");
    }
  }, [isAuthenticated, navigate]);

  return (
    <div className="home-operatore-container">
      <h1 className="home-operatore-title">🔧 Home Operatore</h1>

      <div className="content-container">
        <button
          style={{ fontSize: "1.3rem" }}
          onClick={(e) => {
            if (e.ctrlKey || e.metaKey) {
              window.open("/api/home/operatore/register-cliente", "_blank");
            } else {
              window.location.href = "/api/home/operatore/register-cliente";
            }
          }}
        >
          Registra nuovo Cliente
        </button>

        <button
          style={{ fontSize: "1.3rem" }}
          onClick={(e) => {
            if (e.ctrlKey || e.metaKey) {
              window.open("/api/report/ricerca-cliente", "_blank");
            } else {
              window.location.href = "/api/report/ricerca-cliente";
            }
          }}
        >
          Ricerca Cliente
        </button>

        <button
          style={{ fontSize: "1.3rem" }}
          onClick={(e) => {
            if (e.ctrlKey || e.metaKey) {
              window.open("/api/home/operatore/register-movimento", "_blank");
            } else {
              window.location.href = "/api/home/operatore/register-movimento";
            }
          }}
        >
          Inserimento movimento d'acquisto Articoli
        </button>

        <button
          style={{ fontSize: "1.3rem" }}
          onClick={(e) => {
            if (e.ctrlKey || e.metaKey) {
              window.open("/api/report", "_blank");
            } else {
              window.location.href = "/api/report";
            }
          }}
        >
          Stampa
        </button>
      </div>
    </div>
  );
};

export default HomeOperatore;
