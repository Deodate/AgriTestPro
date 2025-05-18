import React, { useState } from 'react';
import './ResultsComparisonForm.css';

const ResultsComparisonForm = () => {
    const [productsTrials, setProductsTrials] = useState('');
    const [parameterToCompare, setParameterToCompare] = useState('');
    const [timeFrame, setTimeFrame] = useState('');
    const [comparisonType, setComparisonType] = useState('');
    const [resultSummary, setResultSummary] = useState('Auto-generated summary will appear here');
    const [downloadFormat, setDownloadFormat] = useState('');

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
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="results-comparison-form-container">
            <form className="results-comparison-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Select Products/Trials to Compare"
                        value={productsTrials}
                        onChange={(e) => setProductsTrials(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Parameter to Compare (Yield, Disease Resistance, etc.)"
                        value={parameterToCompare}
                        onChange={(e) => setParameterToCompare(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Time Frame"
                        value={timeFrame}
                        onChange={(e) => setTimeFrame(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                    <select
                        placeholder="Comparison Type"
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
                    <textarea
                        placeholder="Result Summary (Auto-generated)"
                        value={resultSummary}
                        onChange={(e) => setResultSummary(e.target.value)} // This might be read-only later
                        readOnly
                    />
                </div>
                 <div className="form-row">
                    <select
                        placeholder="Download as PDF/CSV"
                        value={downloadFormat}
                        onChange={(e) => setDownloadFormat(e.target.value)}
                        required
                    >
                        <option value="">Select Download Format</option>
                        <option value="PDF">PDF</option>
                        <option value="CSV">CSV</option>
                    </select>
                </div>

                <div className="button-row">
                    <button type="submit">Submit</button>
                    <button type="button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default ResultsComparisonForm; 