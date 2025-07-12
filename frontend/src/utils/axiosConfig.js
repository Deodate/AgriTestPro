import axios from 'axios';

// Create axios instance with default config
const axiosInstance = axios.create({
<<<<<<< HEAD
  baseURL: 'http://localhost:8089',
=======
  baseURL: 'http://localhost:8888',
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true // Important for CORS
});

// Add request interceptor
axiosInstance.interceptors.request.use(
  (config) => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.token) {
      config.headers.Authorization = `Bearer ${user.token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // Handle 401 Unauthorized errors
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      // Clear user data and redirect to login
      localStorage.removeItem('user');
      window.location.href = '/login';
    }

    return Promise.reject(error);
  }
);

export default axiosInstance; 