import { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";

interface Cliente {
  username: string;
  password: string;
  nome: string;
  cognome: string;
  dataNascita: string;
  citta: string;
  email: string;
  codiceFiscale: string;
  indirizzo: string;
  ruolo: string;
}

const RegistraCliente = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
  const navigate = useNavigate();
  const [usernameValido, setUsernameValido] = useState<boolean | null>(null);
  const [emailValida, setEmailValida] = useState<boolean | null>(null);
  const [codiceValido, setCodiceValido] = useState<boolean | null>(null);

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
    document.title = "Registra Cliente - Compro Oro";
  }, []);

  useEffect(() => {
    if (isAuthenticated === false) {
      navigate("/");
    }
  }, [isAuthenticated, navigate]);

  const [cliente, setCliente] = useState<Cliente>({
    username: "",
    password: "",
    nome: "",
    cognome: "",
    dataNascita: "",
    citta: "",
    email: "",
    codiceFiscale: "",
    indirizzo: "",
    ruolo: "CLIENT",
  });

  const verificaUsername = async (username: string) => {
    if (!username) return;

    try {
      const response = await fetch(
        `http://localhost:8080/api/report/exist-username/${username}`,
        {
          method: "GET",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
        }
      );

      if (!response.ok) {
        throw new Error("Errore nella richiesta al backend");
      }

      const usernameEsistente = await response.json();

      if (usernameEsistente) {
        setUsernameValido(false);
        console.info("Username già presente!");
      } else {
        setUsernameValido(true);
        console.info("Username non presente");
      }
    } catch (error) {
      console.error("Errore nella verifica dello username:", error);
      setUsernameValido(false);
    }
  };

  const verificaEmail = async (email: string) => {
    if (!email) return;

    if (!isEmailValid(email)) {
      setEmailValida(false);
      console.error("Formato email non valido");
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/report/exist-email/${email}`,
        {
          method: "GET",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
        }
      );

      if (!response.ok) {
        throw new Error("Errore nella richiesta al backend");
      }

      const emailEsistente = await response.json();

      if (emailEsistente) {
        setEmailValida(false);
        console.info("Email già presente!");
      } else {
        setEmailValida(true);
        console.info("Email non presente");
      }
    } catch (error) {
      console.error("Errore nella verifica dell'email:", error);
      setEmailValida(false);
    }
  };

  const isEmailValid = (email: string) => {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
  };

  const verificaCodice = async (codiceFiscale: string) => {
    if (!codiceFiscale) return;

    if (!isCodiceFiscaleValid(codiceFiscale)) {
      setCodiceValido(false);
      console.error("Formato codice fiscale non valido");
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/report/exist-codice/${codiceFiscale}`,
        {
          method: "GET",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
        }
      );

      if (!response.ok) {
        throw new Error("Errore nella richiesta al backend");
      }

      const codiceEsistente = await response.json();

      if (codiceEsistente) {
        setCodiceValido(false);
        console.info("Codice già presente!");
      } else {
        setCodiceValido(true);
        console.info("Codice non presente");
      }
    } catch (error) {
      console.error("Errore nella verifica del codice fiscale:", error);
      setCodiceValido(false);
    }
  };

  // Funzione per validare il codice fiscale (per l'Italia)
  const isCodiceFiscaleValid = (cf: string) => {
    const cfRegex = /^[A-Z0-9]{16}$/;
    return cfRegex.test(cf);
  };

  const isCapitalized = (str: string) => /^[A-ZÀ-Ý]/.test(str);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setCliente((prev) => ({ ...prev, [name]: value }));

    if (name === "username") debouncedVerificaUsername(value);

    if (name === "email") debouncedVerificaEmail(value);

    if (name === "codiceFiscale") debouncedVerificaCodice(value);
  };

  const [file, setFile] = useState<File | null>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setFile(e.target.files[0]);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!usernameValido) {
      console.error("Errore: Username non valido o inesistente.");
      return;
    }

    if (!emailValida) {
      console.error("Errore: Email non valida.");
      return;
    }

    if (!codiceValido) {
      console.error("Errore: Codice Fiscale non valido.");
      return;
    }

    const formData = new FormData();
    formData.append(
      "cliente",
      new Blob([JSON.stringify(cliente)], { type: "application/json" })
    );

    if (file) formData.append("documento", file);

    try {
      const response = await fetch(
        "http://localhost:8080/api/home/operatore/register-cliente",
        {
          method: "POST",
          credentials: "include",
          body: formData,
        }
      );

      if (response.ok) {
        alert("Cliente salvato con successo!");
        console.info("Cliente salvato con successo!");
        setCliente({
          username: "",
          password: "",
          nome: "",
          cognome: "",
          dataNascita: "",
          citta: "",
          email: "",
          codiceFiscale: "",
          indirizzo: "",
          ruolo: "CLIENT",
        });
        setFile(null);
        navigate("/api/home/operatore");
      } else {
        const errorData = await response.json();
        console.error(
          "Errore: " +
            (errorData.message || "Impossibile completare l'operazione")
        );
      }
    } catch (error) {
      console.error("Errore nella richiesta:", error);
      console.error(
        "Si è verificato un errore durante la registrazione del cliente."
      );
    }
  };

  const debounce = (func: Function, delay: number) => {
    let timer: NodeJS.Timeout;
    return (...args: any[]) => {
      clearTimeout(timer);
      timer = setTimeout(() => func(...args), delay);
    };
  };

  const debouncedVerificaUsername = useRef(
    debounce(verificaUsername, 500)
  ).current;

  const debouncedVerificaEmail = useRef(debounce(verificaEmail, 500)).current;

  const debouncedVerificaCodice = useRef(debounce(verificaCodice, 500)).current;

  const isFormValid = () =>
    usernameValido &&
    emailValida &&
    codiceValido &&
    isCapitalized(cliente.nome) &&
    isCapitalized(cliente.cognome) &&
    isCapitalized(cliente.citta) &&
    isCapitalized(cliente.indirizzo) &&
    cliente.username?.trim() &&
    cliente.password?.trim() &&
    cliente.nome?.trim() &&
    cliente.cognome?.trim() &&
    cliente.citta?.trim() &&
    cliente.email?.trim() &&
    cliente.codiceFiscale?.trim() &&
    cliente.dataNascita &&
    cliente.indirizzo?.trim() &&
    file;

  return (
    <div className="client-register-container">
      <h1 className="register-client-title">Registra Cliente</h1>
      <form onSubmit={handleSubmit} className="client-form">
        <label>Username: </label>
        <input
          type="text"
          name="username"
          value={cliente.username}
          onChange={handleChange}
          required
          className={`input ${
            cliente.username.length > 0 && !usernameValido ? "input-error" : ""
          }`}
        />
        {!cliente.username ||
          (usernameValido === false && (
            <>
              <br />
              <p className="error-message">Username già in uso.</p>
            </>
          ))}
        <br /> <br />
        <label>Password: </label>
        <input
          type="password"
          name="password"
          value={cliente.password}
          onChange={handleChange}
          required
          minLength={5}
          className={`input ${
            cliente.password.length > 0 && cliente.password.length < 5
              ? "input-error"
              : ""
          }`}
        />
        {!cliente.password ||
          (cliente.password.length < 5 && (
            <>
              <br />
              <p className="error-message">
                La password deve essere lunga almeno 5 caratteri..
              </p>
            </>
          ))}
        <br /> <br />
        <label>Nome: </label>
        <input
          type="text"
          name="nome"
          value={cliente.nome}
          onChange={handleChange}
          required
          className={`input ${
            cliente.nome && !isCapitalized(cliente.nome) ? "input-error" : ""
          }`}
        />
        {cliente.nome && !isCapitalized(cliente.nome) && (
          <>
            <br />
            <p className="error-message">
              Il nome deve iniziare con una lettera maiuscola.
            </p>
          </>
        )}
        <br /> <br />
        <label>Cognome: </label>
        <input
          type="text"
          name="cognome"
          value={cliente.cognome}
          onChange={handleChange}
          required
          className={`input ${
            cliente.cognome && !isCapitalized(cliente.cognome)
              ? "input-error"
              : ""
          }`}
        />
        {cliente.cognome && !isCapitalized(cliente.cognome) && (
          <>
            <br />
            <p className="error-message">
              Il cognome deve iniziare con una lettera maiuscola.
            </p>
          </>
        )}
        <br /> <br />
        <label>Data di nascita: </label>
        <input
          type="date"
          name="dataNascita"
          value={cliente.dataNascita}
          onChange={handleChange}
          required
        />
        <br /> <br />
        <label>Città: </label>
        <input
          type="text"
          name="citta"
          value={cliente.citta}
          onChange={handleChange}
          required
          className={`input ${
            cliente.citta && !isCapitalized(cliente.citta) ? "input-error" : ""
          }`}
        />
        {cliente.citta && !isCapitalized(cliente.citta) && (
          <>
            <br />
            <p className="error-message">
              La città deve iniziare con una lettera maiuscola.
            </p>
          </>
        )}
        <br /> <br />
        <label>Email: </label>
        <input
          type="email"
          name="email"
          value={cliente.email}
          onChange={handleChange}
          required
          className={`input ${
            cliente.email.length > 0 && !emailValida ? "input-error" : ""
          }`}
        />
        {!cliente.email ||
          (emailValida === false && (
            <>
              <br />
              <p className="error-message">Email già in uso o non valida.</p>
            </>
          ))}
        <br /> <br />
        <label>Codice Fiscale: </label>
        <input
          type="text"
          name="codiceFiscale"
          value={cliente.codiceFiscale}
          onChange={handleChange}
          required
          className={`input ${
            cliente.codiceFiscale.length > 0 && !codiceValido
              ? "input-error"
              : ""
          }`}
        />
        {!cliente.codiceFiscale ||
          (codiceValido === false && (
            <>
              <br />
              <p className="error-message">
                Codice Fiscale già in uso o non valido.
              </p>
            </>
          ))}
        <br /> <br />
        <label>Indirizzo: </label>
        <input
          type="text"
          name="indirizzo"
          value={cliente.indirizzo}
          onChange={handleChange}
          required
          className={`input ${
            cliente.indirizzo && !isCapitalized(cliente.indirizzo)
              ? "input-error"
              : ""
          }`}
        />
        {cliente.indirizzo && !isCapitalized(cliente.indirizzo) && (
          <>
            <br />
            <p className="error-message">
              L'indirizzo deve iniziare con una lettera maiuscola.
            </p>
          </>
        )}
        <br /> <br />
        <input
          type="file"
          id="file-upload"
          onChange={handleFileChange}
          style={{ display: "none" }}
        />
        <label
          htmlFor="file-upload"
          className="file-upload-button"
          style={{ float: "none" }}
        >
          {file ? file.name : "Carica documento scannerizzato"}
        </label>
        <br /> <br />
        <button type="submit" className="save-button" disabled={!isFormValid()}>
          Salva
        </button>
      </form>
    </div>
  );
};

export default RegistraCliente;
