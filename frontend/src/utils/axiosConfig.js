import axios from 'axios';
import { API_CONFIG } from '../config';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8888',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

export default axiosInstance; 