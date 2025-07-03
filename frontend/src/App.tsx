import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomeAdmin from "./pages/HomeAdmin";
import HomeOperatore from "./pages/HomeOperatore";
import HomeCliente from "./pages/HomeCliente";
import Stampa from "./pages/Stampa";
import RegistraCliente from "./pages/RegistraCliente";
import RicercaCliente from "./pages/RicercaCliente";
import RegistraMovimento from "./pages/RegistraMovimento";
import LoginRedirect from "./components/LoginRedirect";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginRedirect />} />
        <Route path="/api/home/amministratore" element={<HomeAdmin />} />
        <Route path="/api/home/operatore" element={<HomeOperatore />} />
        <Route path="/api/home/cliente" element={<HomeCliente />} />
        <Route path="/api/report" element={<Stampa />} />
        <Route
          path="/api/home/operatore/register-cliente"
          element={<RegistraCliente />}
        />
        <Route
          path="/api/report/ricerca-cliente"
          element={<RicercaCliente />}
        />
        <Route
          path="/api/home/operatore/register-movimento"
          element={<RegistraMovimento />}
        />
      </Routes>
    </Router>
  );
}

export default App;
