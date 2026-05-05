import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_HOST;

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true, // セッションCookieを毎回送受信する
});

export default axiosInstance;
