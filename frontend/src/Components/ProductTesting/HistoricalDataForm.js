import React, { useState, useEffect } from 'react';
import './HistoricalDataForm.css';
import { AUTH_SETTINGS } from '../../config'; // Import AUTH_SETTINGS
import { toast, ToastContainer } from 'react-toastify'; // Import toast and ToastContainer
import 'react-toastify/dist/ReactToastify.css'; // Import toastify CSS
import axios from 'axios'; // Import axios
import { API_CONFIG } from '../../config'; // Import API_CONFIG

const HistoricalDataForm = () => {
    const [testName, setTestName] = useState('');
    const [selectedTestId, setSelectedTestId] = useState('');
    const [trialPhase, setTrialPhase] = useState('');
    const [dateRange, setDateRange] = useState(''); // Consider date range picker later
    const [productType, setProductType] = useState('');
    const [resultStatus, setResultStatus] = useState('');
    const [keywords, setKeywords] = useState('');
    const [testCases, setTestCases] = useState([]); // Add state for test cases
    const [isLoadingTestCases, setIsLoadingTestCases] = useState(false); // Add loading state for test cases

    // Effect to fetch test cases on component mount
    useEffect(() => {
        const fetchTestCases = async () => {
            setIsLoadingTestCases(true);
            try {
                const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
                if (!token) {
                    console.error('Authentication token not found. Cannot fetch test cases.');
                    // Optionally show a toast or handle authentication flow
                    setIsLoadingTestCases(false);
                    return;
                }

                const api = axios.create({
                    baseURL: AUTH_SETTINGS.API_BASE_URL || 'http://localhost:8888', // Use base URL from AUTH_SETTINGS or default
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                const response = await api.get('/api/testcases'); // Assuming this endpoint exists
                if (response.data && Array.isArray(response.data)) {
                    setTestCases(response.data);
                } else {
                    console.error('Failed to fetch test cases: Invalid data format', response.data);
                    setTestCases([]);
                }
            } catch (error) {
                console.error('Error fetching test cases:', error);
                toast.error('Failed to load test cases.');
                setTestCases([]);
            } finally {
                setIsLoadingTestCases(false);
            }
        };

        fetchTestCases();
    }, []); // Empty dependency array means this runs once on mount

    // Handle change in Test Name dropdown
    const handleTestNameChange = (e) => {
        const id = e.target.value;
        setSelectedTestId(id);

        // Find the selected test case from the fetched list by ID
        const selectedTestCase = testCases.find(tc => tc.id === parseInt(id));

        if (selectedTestCase) {
            setTestName(selectedTestCase.testName || '');
            setProductType(selectedTestCase.productType || '');
        } else {
            setTestName('');
            setProductType('');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        console.log('Handling form submission...');

        const formData = {
            testName: testName,
            trialPhase,
            dateRange,
            productType,
            resultStatus,
            keywords
        };

        console.log('Form data:', formData);

        // Get authentication token from local storage
        const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);

        if (!token) {
            console.error('Authentication token not found. Please log in.');
            toast.error('Authentication required to submit historical data.'); // Use toast.error
            return; // Stop the submission if no token is found
        }

        try {
            console.log('Sending POST request to historical data endpoint...');
            const response = await axios.post(`${API_CONFIG.BASE_URL}/api/historical-data`, formData, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });

            console.log('API Response:', response);

            if (response.data && response.data.success) {
                console.log('Historical data submitted successfully!');
                toast.success('Historical data submitted successfully!'); // Use toast.success
                // alert('Historical data submitted successfully!'); // Removed alert
                // Clear the form after successful submission
                setTestName('');
                setTrialPhase('');
                setDateRange('');
                setProductType('');
                setResultStatus('');
                setKeywords('');
            } else {
                const errorData = response.data;
                console.error('Failed to submit historical data:', response.status, errorData);
                console.log('API Error Response Body:', errorData);
                toast.error(`Failed to submit historical data: ${errorData.message || response.statusText}`); // Use toast.error
                // alert(`Failed to submit historical data: ${errorData.message || response.statusText}`); // Removed alert
            }
        } catch (error) {
            console.error('Error submitting historical data:', error);
            console.log('API Fetch Error:', error);
            toast.error('An error occurred while submitting historical data.'); // Use toast.error
            // alert('An error occurred while submitting historical data.'); // Removed alert
        }
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
        setTestName('');
        setTrialPhase('');
        setDateRange('');
        setProductType('');
        setResultStatus('');
        setKeywords('');
    };

    return (
        <div className="historical-data-form-container">
            <form className="historical-data-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <select
                        value={selectedTestId}
                        onChange={handleTestNameChange}
                        required
                    >
                        <option value="">Select Test Name</option>
                        {isLoadingTestCases ? (
                            <option value="" disabled>Loading Test Cases...</option>
                        ) : (
                            testCases.map((testCase) => (
                                <option key={testCase.id} value={testCase.id}>
                                    {testCase.testName}
                                </option>
                            ))
                        )}
                    </select>
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Trial Phase"
                        value={trialPhase}
                        onChange={(e) => setTrialPhase(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Date Range (e.g., YYYY-MM-DD to YYYY-MM-DD)" // Updated placeholder for clarity
                        value={dateRange}
                        onChange={(e) => setDateRange(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Product Type"
                        value={productType}
                        onChange={(e) => setProductType(e.target.value)}
                        readOnly
                        required
                    />
                </div>
                 <div className="form-row">
                    <select
                        placeholder="Result Status"
                        value={resultStatus}
                        onChange={(e) => setResultStatus(e.target.value)}
                        required
                    >
                        <option value="">Select Result Status</option>
                        <option value="Successful">Successful</option>
                        <option value="Failed">Failed</option>
                        <option value="Inconclusive">Inconclusive</option>
                    </select>
                </div>
                 <div className="form-row">
                     <textarea
                        placeholder="Keywords"
                        value={keywords}
                        onChange={(e) => setKeywords(e.target.value)}
                        required
                    />
                </div>

                <div className="button-row">
                    <button type="submit" onClick={handleSubmit}>Submit</button>
                    <button type="button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>
            <ToastContainer /> {/* Add ToastContainer */}
        </div>
    );
};

export default HistoricalDataForm; 