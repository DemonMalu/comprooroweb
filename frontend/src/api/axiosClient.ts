import axios from "axios";

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_BACKEND_URL,
  withCredentials: true, // Permette l'uso dei cookie/sessioni
});

export default apiClient;
