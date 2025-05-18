import axios from 'axios';
import { API_CONFIG, AUTH_SETTINGS } from '../config';

// Create axios instance with base URL
const api = axios.create({
    baseURL: API_CONFIG.BASE_URL
});

// Add request interceptor for auth token
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Add response interceptor for error handling
api.interceptors.response.use(
    (response) => response,
    (error) => {
        // For password reset endpoints, just pass through the error
        if (error.config && (
            error.config.url.includes('/auth/forgot-password') ||
            error.config.url.includes('/auth/reset-password') ||
            error.config.url.includes('/auth/verify-reset-code')
        )) {
            return Promise.reject(error);
        }
        
        // For other endpoints, handle 401 unauthorized
        if (error.response?.status === 401) {
            // Clear auth data on unauthorized
            localStorage.removeItem(AUTH_SETTINGS.TOKEN_KEY);
            localStorage.removeItem(AUTH_SETTINGS.USER_KEY);
            window.location.href = '/login';
        }
        return Promise.reject(error.response?.data || error);
    }
);

const authService = {
    // Login with username and password
    login: async (username, password) => {
        try {
            const response = await api.post(API_CONFIG.AUTH.SIGNIN, {
                username,
                password
            });
            
            // Store token if not requiring 2FA
            if (!response.data.requiresVerification) {
                localStorage.setItem(AUTH_SETTINGS.TOKEN_KEY, response.data.token);
                localStorage.setItem(AUTH_SETTINGS.USER_KEY, JSON.stringify(response.data));
            }
            
            return response.data;
        } catch (error) {
            throw error.response?.data || { message: 'Login failed' };
        }
    },

    // Verify 2FA code
    verify2FA: async (userId, code) => {
        try {
            const response = await api.post(API_CONFIG.AUTH.TWO_FACTOR.VERIFY, {
                userId,
                code
            });
            
            // Store token after successful 2FA
            if (response.data.token) {
                localStorage.setItem(AUTH_SETTINGS.TOKEN_KEY, response.data.token);
                localStorage.setItem(AUTH_SETTINGS.USER_KEY, JSON.stringify(response.data));
            }
            
            return response.data;
        } catch (error) {
            throw error.response?.data || { message: 'Verification failed' };
        }
    },

    // Register new user
    signup: async (userData) => {
        try {
            const response = await api.post(API_CONFIG.AUTH.SIGNUP, userData);
            return response.data;
        } catch (error) {
            // If the error response contains validation errors, pass them through
            if (error.response?.data?.errors) {
                throw {
                    message: error.response.data.message || 'Registration failed',
                    errors: error.response.data.errors
                };
            }
            throw error.response?.data || { message: 'Registration failed' };
        }
    },

    // Logout user
    logout: async () => {
        try {
            const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
            if (token) {
                await api.post(API_CONFIG.AUTH.SIGNOUT);
            }
        } catch (error) {
            console.error('Logout error:', error);
        } finally {
            localStorage.removeItem(AUTH_SETTINGS.TOKEN_KEY);
            localStorage.removeItem(AUTH_SETTINGS.USER_KEY);
        }
    },

    // Get current authenticated user
    getCurrentUser: () => {
        const userStr = localStorage.getItem(AUTH_SETTINGS.USER_KEY);
        return userStr ? JSON.parse(userStr) : null;
    },

    // Check if user is authenticated
    isAuthenticated: () => {
        const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
        if (!token) return false;

        // Get user data to check expiration
        const userStr = localStorage.getItem(AUTH_SETTINGS.USER_KEY);
        if (!userStr) {
            localStorage.removeItem(AUTH_SETTINGS.TOKEN_KEY);
            return false;
        }

        try {
            const user = JSON.parse(userStr);
            
            // Check if token is expired
            if (user.exp && user.exp * 1000 < Date.now()) {
                localStorage.removeItem(AUTH_SETTINGS.TOKEN_KEY);
                localStorage.removeItem(AUTH_SETTINGS.USER_KEY);
                return false;
            }

            return true;
        } catch (error) {
            localStorage.removeItem(AUTH_SETTINGS.TOKEN_KEY);
            localStorage.removeItem(AUTH_SETTINGS.USER_KEY);
            return false;
        }
    },

    // Get authentication token
    getToken: () => {
        return localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
    },

    // Toggle 2FA for a user
    toggleTwoFactor: async (userIdentifier, enabled) => {
        try {
            const response = await api.put(
                API_CONFIG.AUTH.TWO_FACTOR.TOGGLE(userIdentifier),
                null,
                { params: { enabled } }
            );
            return response.data;
        } catch (error) {
            throw error.response?.data || { message: 'Failed to toggle 2FA' };
        }
    },

    // Generate 2FA code
    generateTwoFactorCode: async (userId) => {
        try {
            const response = await api.post(API_CONFIG.AUTH.TWO_FACTOR.GENERATE(userId));
            return response.data;
        } catch (error) {
            throw error.response?.data || { message: 'Failed to generate 2FA code' };
        }
    },

    /**
     * Send a forgot password request to initiate password reset process
     * @param {string} email - The user's email address
     * @returns {Promise<Object>} Response containing status and message
     */
    forgotPassword: async (email) => {
        try {
            const response = await api.post(API_CONFIG.AUTH.FORGOT_PASSWORD, { email });
            return response.data;
        } catch (error) {
            if (error.response) {
                // The server responded with a status code outside the 2xx range
                // Create an error object that includes both message and error code
                const errorObj = {
                    message: error.response.data.message || "Failed to process request. Please try again.",
                    error: error.response.data.error || null,
                    status: error.response.status
                };
                throw errorObj;
            } else if (error.request) {
                // The request was made but no response was received
                throw new Error("No response from server. Please check your internet connection.");
            } else {
                // Something happened in setting up the request
                throw new Error("Error making request. Please try again.");
            }
        }
    },

    // Reset password
    resetPassword: async (resetCode, newPassword, confirmPassword) => {
        try {
            const response = await api.post(API_CONFIG.AUTH.RESET_PASSWORD, {
                token: resetCode,
                newPassword: newPassword,
                confirmPassword: confirmPassword
            });
            return response.data;
        } catch (error) {
            throw error.response?.data || { message: 'Failed to reset password' };
        }
    },

    // Verify reset code and reset password (alternative endpoint)
    verifyResetCode: async (resetCode, newPassword, confirmPassword) => {
        try {
            const response = await api.post(API_CONFIG.AUTH.VERIFY_RESET_CODE, {
                code: resetCode,
                newPassword: newPassword,
                confirmPassword: confirmPassword
            });
            return response.data;
        } catch (error) {
            throw error.response?.data || { message: 'Failed to verify reset code' };
        }
    },

    async verifyTwoFactor(username, code) {
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}/auth/verify-2fa`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, code }),
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || 'Failed to verify 2FA code');
            }

            return await response.json();
        } catch (error) {
            throw new Error(error.message || 'Failed to verify 2FA code');
        }
    }
};

export default authService; 