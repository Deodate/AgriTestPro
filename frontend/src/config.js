// API Configuration
export const API_CONFIG = {
    BASE_URL: 'http://localhost:8888',
    AUTH: {
        SIGNIN: '/api/auth/signin',
        SIGNUP: '/api/auth/signup',
        SIGNOUT: '/api/auth/signout',
        FORGOT_PASSWORD: '/api/auth/forgot-password',
        RESET_PASSWORD: '/api/auth/reset-password',
        VERIFY_RESET_CODE: '/api/auth/verify-reset-code',
        TWO_FACTOR: {
            VERIFY: '/api/auth/2fa/verify',
            GENERATE: (userId) => `/api/auth/2fa/generate/${userId}`,
            TOGGLE: (userIdentifier) => `/api/auth/2fa/${userIdentifier}`
        }
    }
};

// Authentication Settings
export const AUTH_SETTINGS = {
    TOKEN_KEY: 'auth_token',
    USER_KEY: 'user_data',
    ROLES: {
        ADMIN: 'ROLE_ADMIN',
        USER: 'ROLE_USER',
        FARMER: 'ROLE_FARMER',
        TESTER: 'ROLE_TESTER'
    }
};

// Route Settings
export const ROUTE_SETTINGS = {
    PUBLIC: ['/login', '/signup', '/forgot-password'],
    DEFAULT_AUTH_REDIRECT: '/dashboard',
    DEFAULT_PUBLIC_REDIRECT: '/login'
}; 