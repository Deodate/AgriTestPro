import React, { useState } from 'react';
import './ResultsComparisonForm.css';
<<<<<<< HEAD

const ResultsComparisonForm = () => {
=======
import axios from 'axios';
import { API_CONFIG, AUTH_SETTINGS } from '../../config'; // Import API_CONFIG and AUTH_SETTINGS

const ResultsComparisonForm = () => {
    const apiBaseUrl = API_CONFIG.BASE_URL || 'http://localhost:8089';

>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
    const [productsTrials, setProductsTrials] = useState('');
    const [parameterToCompare, setParameterToCompare] = useState('');
    const [timeFrame, setTimeFrame] = useState('');
    const [comparisonType, setComparisonType] = useState('');
    const [resultSummary, setResultSummary] = useState('Auto-generated summary will appear here');
    const [downloadFormat, setDownloadFormat] = useState('');
<<<<<<< HEAD

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            productsTrials,
            parameterToCompare,
            timeFrame,
            comparisonType,
            downloadFormat
        });
        // Clear form or show success message
=======
    const [isLoading, setIsLoading] = useState(false); // Add loading state
    const [error, setError] = useState(null); // Add error state

    const handleSubmit = async (e) => { // Make handleSubmit async
        e.preventDefault();

        setIsLoading(true); // Set loading to true on submission
        setError(null); // Clear previous errors

        // Prepare data for the backend request
        const requestData = {
            // Assuming comma-separated IDs in productsTrials and sending them as productIds
            productIds: productsTrials.split(',').map(id => parseInt(id.trim())).filter(id => !isNaN(id)),
            trialIds: [], // Assuming no trial IDs from current input, or needs frontend update
            parameterToCompare: parameterToCompare,
            timeFrame: timeFrame,
            comparisonType: comparisonType,
            downloadFormat: downloadFormat
        };

        // Get token from local storage
        const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);

        if (!token) {
            setError('Authentication token not found. Please log in.');
            setIsLoading(false);
            return;
        }

        // Add Authorization header
        const headers = {
            'Authorization': `Bearer ${token}`,
        };

        try {
            const response = await axios.post(
                `${apiBaseUrl}/api/comparison/generate-report`,
                requestData,
                {
                    responseType: 'blob', // Important: to handle file download response
                    headers: headers, // Add headers to the request
                }
            );

            // Handle successful response (file download)
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            
            // Determine filename from response headers or use a default
            const contentDisposition = response.headers['content-disposition'];
            let filename = 'report';
            if (contentDisposition) {
                const filenameMatch = contentDisposition.match(/filename="(.+)"/);
                if (filenameMatch && filenameMatch[1]) {
                    filename = filenameMatch[1];
                }
            } else {
                 filename += downloadFormat === 'PDF' ? '.pdf' : '.csv';
            }

            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url); // Clean up the URL object

            // Optionally clear form or show success message
            // Clear form fields
            setProductsTrials('');
            setParameterToCompare('');
            setTimeFrame('');
            setComparisonType('');
            setDownloadFormat('');
            setResultSummary('Auto-generated summary will appear here');

        } catch (err) {
            console.error('Error generating report:', err);
            
             // Attempt to read error message from response if available
             if (err.response) {
                 if (err.response.status === 401 || err.response.status === 403) {
                    setError('Authentication failed or you do not have permission.');
                 } else if (err.response.data) {
                    const reader = new FileReader();
                    reader.onload = function() {
                        try {
                             const errorData = JSON.parse(reader.result);
                             setError(`Failed to generate report: ${errorData.message || err.message}`);
                        } catch (parseError) {
                             // If response is not JSON, use generic error
                             setError(`Failed to generate report: ${err.message}`);
                        }
                    };
                     reader.readAsText(err.response.data);
                 } else {
                     setError(`Failed to generate report: ${err.message}`);
                 }
            } else {
                 setError(`Failed to generate report: ${err.message}`);
            }

        } finally {
            setIsLoading(false); // Set loading to false after request completes
        }
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
<<<<<<< HEAD
=======
         // Example: Clear form fields on cancel
        setProductsTrials('');
        setParameterToCompare('');
        setTimeFrame('');
        setComparisonType('');
        setDownloadFormat('');
        setResultSummary('Auto-generated summary will appear here');
        setError(null);
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
    };

    return (
        <div className="results-comparison-form-container">
            <form className="results-comparison-form" onSubmit={handleSubmit}>
                <div className="form-row">
<<<<<<< HEAD
                    <input
                        type="text"
                        placeholder="Select Products/Trials to Compare"
=======
                    <label htmlFor="productsTrials">Select Products/Trials (comma-separated IDs):</label>
                    <input
                        type="text"
                        id="productsTrials"
                        placeholder="Enter Product or Trial IDs (e.g., 1,2,3)"
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                        value={productsTrials}
                        onChange={(e) => setProductsTrials(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
<<<<<<< HEAD
                    <input
                        type="text"
                        placeholder="Parameter to Compare (Yield, Disease Resistance, etc.)"
=======
                     <label htmlFor="parameterToCompare">Parameter to Compare:</label>
                    <input
                        type="text"
                        id="parameterToCompare"
                        placeholder="Yield, Disease Resistance, etc."
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                        value={parameterToCompare}
                        onChange={(e) => setParameterToCompare(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
<<<<<<< HEAD
                    <input
                        type="text"
                        placeholder="Time Frame"
=======
                     <label htmlFor="timeFrame">Time Frame (YYYY-MM-DD,YYYY-MM-DD):</label>
                    <input
                        type="text"
                        id="timeFrame"
                        placeholder="e.g., 2023-01-01,2023-12-31"
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                        value={timeFrame}
                        onChange={(e) => setTimeFrame(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
<<<<<<< HEAD
                    <select
                        placeholder="Comparison Type"
=======
                     <label htmlFor="comparisonType">Comparison Type:</label>
                    <select
                        id="comparisonType"
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                        value={comparisonType}
                        onChange={(e) => setComparisonType(e.target.value)}
                        required
                    >
                        <option value="">Select Comparison Type</option>
                        <option value="Bar chart">Bar chart</option>
                        <option value="Table">Table</option>
                        <option value="Line graph">Line graph</option>
                    </select>
                </div>
                 <div className="form-row">
<<<<<<< HEAD
                    <textarea
                        placeholder="Result Summary (Auto-generated)"
=======
                     <label htmlFor="resultSummary">Result Summary:</label>
                    <textarea
                        id="resultSummary"
                        placeholder="Auto-generated summary will appear here"
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                        value={resultSummary}
                        onChange={(e) => setResultSummary(e.target.value)} // This might be read-only later
                        readOnly
                    />
                </div>
                 <div className="form-row">
<<<<<<< HEAD
                    <select
                        placeholder="Download as PDF/CSV"
=======
                     <label htmlFor="downloadFormat">Download Format:</label>
                    <select
                        id="downloadFormat"
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                        value={downloadFormat}
                        onChange={(e) => setDownloadFormat(e.target.value)}
                        required
                    >
                        <option value="">Select Download Format</option>
                        <option value="PDF">PDF</option>
                        <option value="CSV">CSV</option>
                    </select>
                </div>

<<<<<<< HEAD
                <div className="button-row">
                    <button type="submit">Submit</button>
                    <button type="button" onClick={handleCancel}>Cancel</button>
=======
                {error && <div className="error-message">{error}</div>} {/* Display error message */}

                <div className="button-row">
                    <button type="submit" disabled={isLoading}>
                         {isLoading ? 'Generating...' : 'Generate Report'}
                        </button>
                    <button type="button" onClick={handleCancel} disabled={isLoading}>Cancel</button>
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                </div>
            </form>
        </div>
    );
};

export default ResultsComparisonForm; 