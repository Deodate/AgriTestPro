import { API_CONFIG, getAuthHeaders, AUTH_STORAGE_KEYS } from './config';

class UserAccount {
    // 1. Create Account API
    static async createAccount(accountData) {
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}/api/auth/signup`, {
                method: 'POST',
                headers: getAuthHeaders(),
                body: JSON.stringify(accountData)
            });

            const data = await response.json();
            
            if (!response.ok) {
                throw new Error(data.message || 'Failed to create account');
            }

            return data;
        } catch (error) {
            throw error;
        }
    }

    // 2. Enable Account API
    static async enableAccount(userId) {
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}/api/auth/enable/${userId}`, {
                method: 'PUT',
                headers: getAuthHeaders(this.getStoredToken())
            });

            const data = await response.json();
            
            if (!response.ok) {
                throw new Error(data.message || 'Failed to enable account');
            }

            return data;
        } catch (error) {
            throw error;
        }
    }

    // 3. Enable 2FA API
    static async toggleTwoFactor(username, enabled) {
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}/api/auth/2fa/${username}?enabled=${enabled}`, {
                method: 'PUT',
                headers: getAuthHeaders(this.getStoredToken())
            });

            const data = await response.json();
            
            if (!response.ok) {
                throw new Error(data.message || 'Failed to toggle 2FA');
            }

            return data;
        } catch (error) {
            throw error;
        }
    }

    // 4. Request 2FA Code API
    static async requestTwoFactorCode(phoneNumber) {
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}/api/auth/2fa/request-code`, {
                method: 'POST',
                headers: getAuthHeaders(),
                body: JSON.stringify({ phoneNumber })
            });

            const data = await response.json();
            
            if (!response.ok) {
                throw new Error(data.message || 'Failed to request 2FA code');
            }

            return data;
        } catch (error) {
            throw error;
        }
    }

    // 5. Verify 2FA Code API
    static async verifyTwoFactorCode(code, username) {
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}/api/auth/2fa/verify`, {
                method: 'POST',
                headers: getAuthHeaders(),
                body: JSON.stringify({ code, username })
            });

            const data = await response.json();
            
            if (!response.ok) {
                throw new Error(data.message || 'Failed to verify 2FA code');
            }

            // Store user data after successful verification
            if (data.token) {
                this.storeUserData(data);
            }

            return data;
        } catch (error) {
            throw error;
        }
    }

    // 6. Sign In API
    static async login(username, password) {
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}/api/auth/signin`, {
                method: 'POST',
                headers: getAuthHeaders(),
                body: JSON.stringify({ username, password })
            });

            const data = await response.json();
            
            if (!response.ok) {
                throw new Error(data.message || 'Login failed');
            }

            // If 2FA is not required, store the user data
            if (!data.requiresVerification) {
                this.storeUserData(data);
            }

            return data;
        } catch (error) {
            throw error;
        }
    }

    // Helper methods
    static storeUserData(userData) {
        localStorage.setItem(AUTH_STORAGE_KEYS.USER_DATA, JSON.stringify(userData));
    }

    static getStoredToken() {
        const userData = localStorage.getItem(AUTH_STORAGE_KEYS.USER_DATA);
        return userData ? JSON.parse(userData).token : null;
    }

    static isAuthenticated() {
        return !!this.getStoredToken();
    }

    static logout() {
        localStorage.removeItem(AUTH_STORAGE_KEYS.USER_DATA);
    }

    static getUserData() {
        const userData = localStorage.getItem(AUTH_STORAGE_KEYS.USER_DATA);
        return userData ? JSON.parse(userData) : null;
    }
}

export default UserAccount;