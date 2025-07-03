import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import StampaUtenti from "../components/StampaUtenti";
import StampaMovimenti from "../components/StampaMovimenti";
import StampaArticoli from "../components/StampaArticoli";
import StampaOperazioni from "../components/StampaOperazioni";

const Stampa = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
  const navigate = useNavigate();

  const checkAuthStatus = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/report/profilo", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      });

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
    document.title = "Report - Compro Oro";
  }, []);

  useEffect(() => {
    if (isAuthenticated === false) {
      navigate("/");
    }
  }, [isAuthenticated, navigate]);

  return (
    <div className="report-container">
      <h1 className="report-title">🖨️ Stampa</h1>
      <div className="content-container">
        <StampaUtenti />
        <StampaMovimenti />
        <StampaArticoli />
        <StampaOperazioni />
      </div>
    </div>
  );
};

export default Stampa;
