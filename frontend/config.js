// API Configuration
export const API_CONFIG = {
    BASE_URL: 'http://localhost:8888',
    AUTH: {
        SIGNIN: '/api/auth/signin',
        SIGNUP: '/api/auth/signup',
        SIGNOUT: '/api/auth/signout',
        FORGOT_PASSWORD: '/api/auth/forgot-password',
        RESET_PASSWORD: '/api/auth/reset-password',
        VERIFY_RESET_CODE: '/api/auth/verify-reset-code'
    },
    ENDPOINTS: {
        TEST_CASES: '/api/testcases',
        USERS: '/api/users',
        ROLES: '/api/roles'
    }
};

// Authentication Settings
export const AUTH_SETTINGS = {
    TOKEN_KEY: 'auth_token',
    USER_KEY: 'user_data',
    ROLES: {
        ADMIN: 'ROLE_ADMIN',
        USER: 'ROLE_USER',
        FIELD_WORKER: 'ROLE_FIELD_WORKER',
        AGRONOMIST: 'ROLE_AGRONOMIST'
    }
};

// Route Settings
export const ROUTE_SETTINGS = {
    PUBLIC: ['/login', '/signup', '/forgot-password'],
    DEFAULT_AUTH_REDIRECT: '/dashboard',
    DEFAULT_PUBLIC_REDIRECT: '/login'
};
