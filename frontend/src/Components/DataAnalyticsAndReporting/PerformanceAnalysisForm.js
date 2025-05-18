import React, { useState } from 'react';
import './PerformanceAnalysisForm.css';

const PerformanceAnalysisForm = () => {
    const [formData, setFormData] = useState({
        productId: '',
        testCaseId: '',
        trialResults: '',
        seasonalPerformanceMetrics: '',
        effectivenessRating: '',
        comparisonWithPreviousSeasons: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Performance Analysis form data submitted:', formData);
        // TODO: Implement form submission logic
    };

    return (
        <div className="performance-analysis-form-container">
            <form className="performance-analysis-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <input type="text" id="productId" name="productId" value={formData.productId} onChange={handleChange} required placeholder="Enter Product ID" />
                </div>
                <div className="form-group">
                    <input type="text" id="testCaseId" name="testCaseId" value={formData.testCaseId} onChange={handleChange} required placeholder="Enter Test Case ID" />
                </div>
                 <div className="form-group">
                    <select id="trialResults" name="trialResults" value={formData.trialResults} onChange={handleChange} required>
                        <option value="">Select Result Type</option>
                        <option value="Graph">Graph</option>
                        <option value="DataTable">Data Table</option>
                    </select>
                </div>
                <div className="form-group">
                    <textarea id="seasonalPerformanceMetrics" name="seasonalPerformanceMetrics" value={formData.seasonalPerformanceMetrics} onChange={handleChange} placeholder="Enter Seasonal Performance Metrics" />
                </div>
                <div className="form-group">
                    <input type="number" id="effectivenessRating" name="effectivenessRating" value={formData.effectivenessRating} onChange={handleChange} required placeholder="Enter Effectiveness Rating (1-10)" min="1" max="10" />
                </div>
                <div className="form-group">
                    <textarea id="comparisonWithPreviousSeasons" name="comparisonWithPreviousSeasons" value={formData.comparisonWithPreviousSeasons} onChange={handleChange} placeholder="Enter Comparison Details" />
                </div>
                <div className="form-buttons">
                    <button type="submit">Submit</button>
                    <button type="button" className="cancel-button">Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default PerformanceAnalysisForm;
 