import axios from 'axios';
import { API_CONFIG, AUTH_SETTINGS } from '../config';

const api = axios.create({
    baseURL: API_CONFIG.BASE_URL
});

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

const eventService = {
    createEvent: async (eventData) => {
        try {
            const response = await api.post('/api/events', eventData);
            return response.data;
        } catch (error) {
            throw error.response?.data || { message: 'Failed to create event' };
        }
    },
    getAllEvents: async () => {
        try {
            const response = await api.get('/api/events');
            return response.data;
        } catch (error) {
            throw error.response?.data || { message: 'Failed to fetch events' };
        }
    }
};

export default eventService; 