import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import { AUTH_SETTINGS, API_CONFIG } from '../../config';
import './HistoricalDataList.css'; // We will create this CSS file next

const HistoricalDataList = () => {
    const [historicalData, setHistoricalData] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const apiBaseUrl = API_CONFIG.BASE_URL || 'http://localhost:8888';

    useEffect(() => {
        const fetchHistoricalData = async () => {
            setIsLoading(true);
            setError(null);
            try {
                const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
                if (!token) {
                    console.error('Authentication token not found. Cannot fetch historical data.');
                    setError('Authentication required to view historical data.');
                    setIsLoading(false);
                    return;
                }

                const api = axios.create({
                    baseURL: apiBaseUrl,
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                const response = await api.get('/api/historical-data'); // Call the new backend endpoint

                if (response.data && Array.isArray(response.data)) {
                    setHistoricalData(response.data);
                } else {
                    console.error('Failed to fetch historical data: Invalid data format', response.data);
                    setHistoricalData([]);
                    setError('Received unexpected data format from server.');
                }
            } catch (err) {
                console.error('Error fetching historical data:', err);
                if (err.response && err.response.status === 401) {
                    setError('Authentication error: Please log in again to access historical data.');
                    toast.error('Authentication required to view historical data.');
                } else if (err.response && err.response.status === 403) {
                    setError('Permission denied: You do not have access to view historical data.');
                    toast.error('Permission denied to view historical data.');
                } else {
                    setError(`Failed to load historical data: ${err.message || 'Unknown error'}`);
                    toast.error('Failed to load historical data.');
                }
                setHistoricalData([]);
            } finally {
                setIsLoading(false);
            }
        };

        fetchHistoricalData();
    }, [apiBaseUrl]); // Refetch if apiBaseUrl changes

    return (
        <div className="historical-data-list-container">
            <h2>Historical Data List</h2>
            {isLoading && <p>Loading historical data...</p>}
            {error && <p className="error-message">{error}</p>}
            {!isLoading && !error && historicalData.length === 0 && <p>No historical data found.</p>}

            {!isLoading && !error && historicalData.length > 0 && (
                <div className="data-table">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Test Name</th>
                                <th>Trial Phase</th>
                                <th>Date Range</th> {/* Or Start Date/End Date if separate */}
                                <th>Product Type</th>
                                <th>Result Status</th>
                                <th>Keywords</th>
                                {/* Add other relevant columns from your historical data model */}
                            </tr>
                        </thead>
                        <tbody>
                            {historicalData.map((data) => (
                                <tr key={data.id}> {/* Assuming each historical data record has a unique 'id' */}
                                    <td>{data.id}</td>
                                    <td>{data.testName || 'N/A'}</td>
                                    <td>{data.trialPhase || 'N/A'}</td>
                                    <td>{data.dateRange || `${data.startDate || 'N/A'} to ${data.endDate || 'N/A'}`}</td> {/* Adjust based on your model */}
                                    <td>{data.productType || 'N/A'}</td>
                                    <td>{data.resultStatus || 'N/A'}</td>
                                    <td>{data.keywords || 'N/A'}</td>
                                    {/* Map other data fields to table cells */}
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default HistoricalDataList; 