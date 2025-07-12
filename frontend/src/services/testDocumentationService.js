import axios from 'axios';
import { API_CONFIG } from '../config';
import { AUTH_SETTINGS } from '../config';

const API_URL = `${API_CONFIG.BASE_URL}/api/test-documentation`;

// Helper function to get the auth token
const getAuthToken = () => {
    return localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
};

const testDocumentationService = {
    createTestDocumentation: async (data) => {
        try {
            const formData = new FormData();
            
            // Add all text fields to formData
            Object.keys(data).forEach(key => {
                if (key !== 'attachments') {
                    formData.append(key, data[key]);
                }
            });

            // Add attachments if present
            if (data.attachments && data.attachments.length > 0) {
                Array.from(data.attachments).forEach(file => {
                    formData.append('attachments', file);
                });
            }

            const response = await axios.post(API_URL, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': `Bearer ${getAuthToken()}`
                }
            });
            return response.data;
        } catch (error) {
            throw error.response?.data || error.message;
        }
    },

    updateTestDocumentation: async (id, data) => {
        try {
            const response = await axios.put(`${API_URL}/${id}`, data, {
                headers: {
                    'Authorization': `Bearer ${getAuthToken()}`
                }
            });
            return response.data;
        } catch (error) {
            throw error.response?.data || error.message;
        }
    },

    getTestDocumentation: async (id) => {
        try {
            const response = await axios.get(`${API_URL}/${id}`, {
                headers: {
                    'Authorization': `Bearer ${getAuthToken()}`
                }
            });
            return response.data;
        } catch (error) {
            throw error.response?.data || error.message;
        }
    },

    getAllTestDocumentations: async () => {
        try {
            const response = await axios.get(API_URL, {
                headers: {
                    'Authorization': `Bearer ${getAuthToken()}`
                }
            });
            return response.data;
        } catch (error) {
            throw error.response?.data || error.message;
        }
    },

    deleteTestDocumentation: async (id) => {
        try {
            const response = await axios.delete(`${API_URL}/${id}`, {
                headers: {
                    'Authorization': `Bearer ${getAuthToken()}`
                }
            });
            return response.data;
        } catch (error) {
            throw error.response?.data || error.message;
        }
    }
};

export default testDocumentationService; 