// API Configuration
export const API_CONFIG = {
    BASE_URL: 'http://localhost:8089',
    ENDPOINTS: {
        SIGNIN: '/api/auth/signin',
        SIGNUP: '/api/auth/signup',
        TWO_FACTOR: {
            REQUEST_CODE: '/api/auth/2fa/request-code',
            VERIFY: '/api/auth/2fa/verify',
            TOGGLE: (username) => `/api/auth/2fa/${username}`
        }
    },
    VERIFICATION_KEY: process.env.REACT_APP_VERIFICATION_KEY || 'W23U' // Should be moved to environment variables
};

// Authentication Storage Keys
export const AUTH_STORAGE_KEYS = {
    USER_DATA: 'user',
    TOKEN: 'token'
};

// HTTP Headers
export const getAuthHeaders = (token) => ({
    'Content-Type': 'application/json',
    ...(token && { 'Authorization': `Bearer ${token}` })
}); 