import { useEffect } from "react";

function LoginRedirect() {
  useEffect(() => {
    window.location.href = import.meta.env.VITE_BACKEND_URL + "/login";
  }, []);

  return <p>Reindirizzamento in corso...</p>;
}

export default LoginRedirect;
