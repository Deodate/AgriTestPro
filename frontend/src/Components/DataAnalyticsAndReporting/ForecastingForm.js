import React, { useState } from 'react';
import './ForecastingForm.css';

const ForecastingForm = () => {
    const [productParameter, setProductParameter] = useState('');
    const [timeHorizon, setTimeHorizon] = useState('');
    const [modelType, setModelType] = useState(''); // Consider a select dropdown later
    const [keyFactors, setKeyFactors] = useState(''); // Consider multi-select or text area later
    const [outputFormat, setOutputFormat] = useState(''); // Consider a select dropdown later

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            productParameter,
            timeHorizon,
            modelType,
            keyFactors,
            outputFormat
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="forecasting-form-container">
            <form className="forecasting-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Product/Parameter to Forecast"
                        value={productParameter}
                        onChange={(e) => setProductParameter(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Time Horizon"
                        value={timeHorizon}
                        onChange={(e) => setTimeHorizon(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Model Type (Time Series, Regression, etc.)"
                        value={modelType}
                        onChange={(e) => setModelType(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Key Factors/Inputs"
                        value={keyFactors}
                        onChange={(e) => setKeyFactors(e.target.value)}
                    />
                </div>
                 <div className="form-row">
                    <select
                        placeholder="Output Format"
                        value={outputFormat}
                        onChange={(e) => setOutputFormat(e.target.value)}
                        required
                    >
                        <option value="">Select Output Format</option>
                        <option value="Graph">Graph</option>
                        <option value="Table">Table</option>
                    </select>
                </div>

                <div className="button-row">
                    <button type="submit">Generate Forecast</button>
                    <button type="button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default ForecastingForm; 