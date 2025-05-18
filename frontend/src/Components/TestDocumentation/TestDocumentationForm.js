import React, { useState } from 'react';
import './TestDocumentationForm.css';

const TestDocumentationForm = () => {
    const [formData, setFormData] = useState({
        testCaseId: '',
        productId: '',
        trialPhase: '',
        testMetrics: '',
        finalVerdict: '',
        recommendations: '',
        approvedBy: '',
        dateOfApproval: ''
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
        console.log('Form data submitted:', formData);
        // TODO: Implement form submission logic
    };

    return (
        <div className="test-documentation-form-container">
            <form className="test-documentation-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <input type="text" id="testCaseId" name="testCaseId" value={formData.testCaseId} onChange={handleChange} required placeholder="Enter Test Case ID" />
                </div>
                <div className="form-group">
                    <input type="text" id="productId" name="productId" value={formData.productId} onChange={handleChange} required placeholder="Enter Product ID" />
                </div>
                <div className="form-group">
                    <input type="text" id="trialPhase" name="trialPhase" value={formData.trialPhase} onChange={handleChange} required placeholder="Enter Trial Phase" />
                </div>
                <div className="form-group">
                    <textarea id="testMetrics" name="testMetrics" value={formData.testMetrics} onChange={handleChange} required placeholder="Enter Test Metrics" />
                </div>
                <div className="form-group">
                    <select id="finalVerdict" name="finalVerdict" value={formData.finalVerdict} onChange={handleChange} required>
                        <option value="">Select Verdict</option>
                        <option value="Pass">Pass</option>
                        <option value="Fail">Fail</option>
                    </select>
                </div>
                <div className="form-group">
                    <textarea id="recommendations" name="recommendations" value={formData.recommendations} onChange={handleChange} placeholder="Enter Recommendations" />
                </div>
                <div className="form-group">
                    <input type="text" id="approvedBy" name="approvedBy" value={formData.approvedBy} onChange={handleChange} required placeholder="Enter Approver Name" />
                </div>
                <div className="form-group">
                    <input type="date" id="dateOfApproval" name="dateOfApproval" value={formData.dateOfApproval} onChange={handleChange} required placeholder="Select Date" />
                </div>
                <div className="form-buttons">
                    <button type="submit">Submit</button>
                    <button type="button" className="cancel-button">Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default TestDocumentationForm; 