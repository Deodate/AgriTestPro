import React, { useState } from 'react';
import './HistoricalDataForm.css';

const HistoricalDataForm = () => {
    const [productName, setProductName] = useState('');
    const [trialPhase, setTrialPhase] = useState('');
    const [dateRange, setDateRange] = useState(''); // Consider date range picker later
    const [cropType, setCropType] = useState('');
    const [resultStatus, setResultStatus] = useState('');
    const [keywords, setKeywords] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            productName,
            trialPhase,
            dateRange,
            cropType,
            resultStatus,
            keywords
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="historical-data-form-container">
            <form className="historical-data-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Product Name"
                        value={productName}
                        onChange={(e) => setProductName(e.target.value)}
                        required
                    />
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
                        placeholder="Date Range (e.g., 2023-01-01 to 2023-12-31)"
                        value={dateRange}
                        onChange={(e) => setDateRange(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Crop Type"
                        value={cropType}
                        onChange={(e) => setCropType(e.target.value)}
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
                    <button type="submit">Submit</button>
                    <button type="button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default HistoricalDataForm; 