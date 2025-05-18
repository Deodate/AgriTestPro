import React, { useState } from 'react';
import './ComplianceChecklistForm.css';

const ComplianceChecklistForm = () => {
    const [formData, setFormData] = useState({
        productId: '',
        checklistItems: '', // This might need a more complex structure later
        reviewerName: '',
        reviewDate: '',
        comments: ''
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
        console.log('Compliance Checklist form data submitted:', formData);
        // TODO: Implement form submission logic
    };

    return (
        <div className="compliance-checklist-form-container">
            <form className="compliance-checklist-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <input type="text" id="productId" name="productId" value={formData.productId} onChange={handleChange} required placeholder="Enter Product ID" />
                </div>
                <div className="form-group">
                    <textarea id="checklistItems" name="checklistItems" value={formData.checklistItems} onChange={handleChange} placeholder="Enter Checklist Items and Status (e.g., Toxicity Test: Pass, Environmental Impact: Fail)" />
                </div>
                 <div className="form-group">
                    <input type="text" id="reviewerName" name="reviewerName" value={formData.reviewerName} onChange={handleChange} required placeholder="Enter Reviewer Name" />
                </div>
                <div className="form-group">
                    <input type="date" id="reviewDate" name="reviewDate" value={formData.reviewDate} onChange={handleChange} required placeholder="Select Review Date" />
                </div>
                <div className="form-group">
                    <textarea id="comments" name="comments" value={formData.comments} onChange={handleChange} placeholder="Enter Comments" />
                </div>
                <div className="form-buttons">
                    <button type="submit">Submit</button>
                    <button type="button" className="cancel-button">Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default ComplianceChecklistForm; 