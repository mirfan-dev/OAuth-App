import useAuth from "@/auth/store";
import axios from "axios";

const apiClient =axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:9096/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
  timeout: 10000
});

apiClient.interceptors.request.use(
  (config) => {
    const accessToken = useAuth.getState().accessToken;

    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);


export default apiClient;