// src/services/AUTH/userAuth.js
import axios from "axios";

const API_BASE_URL = "http://localhost:8089/api/auth";

class UserAuth {
    // Register a new user
    createAccount(account) {
        return axios.post(`${API_BASE_URL}/signup`, account);
    }

    // Login user
    login(credentials) {
        return axios.post(`${API_BASE_URL}/signin`, {
            username: credentials.username,
            password: credentials.password
        });
    }

    // Save user data to localStorage
    saveUserData(userData) {
        localStorage.setItem('user', JSON.stringify(userData));
        
        if (userData.token) {
            localStorage.setItem('token', userData.token);
            // Set token for future requests
            this.setAuthHeader(userData.token);
        }
    }

    // Set auth token for all future requests
    setAuthHeader(token) {
        if (token) {
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        } else {
            delete axios.defaults.headers.common['Authorization'];
        }
    }

    // Check if user is logged in
    isLoggedIn() {
        return localStorage.getItem('token') !== null;
    }

    // Get current user data
    getCurrentUser() {
        const userStr = localStorage.getItem('user');
        if (userStr) {
            return JSON.parse(userStr);
        }
        return null;
    }

    // Logout user
    logout() {
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        this.setAuthHeader(null);
    }
}

export default new UserAuth();