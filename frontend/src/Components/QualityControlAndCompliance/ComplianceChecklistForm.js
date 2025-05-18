import React, { useState } from 'react';
import './ComplianceChecklistForm.css';

const ComplianceChecklistForm = () => {
    const [productId, setProductId] = useState('');
    const [checklistItems, setChecklistItems] = useState(''); // Consider a more complex state for pass/fail items later
    const [reviewerName, setReviewerName] = useState('');
    const [reviewDate, setReviewDate] = useState('');
    const [comments, setComments] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            productId,
            checklistItems,
            reviewerName,
            reviewDate,
            comments
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="compliance-checklist-form-container">
            <form className="compliance-checklist-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Product ID"
                        value={productId}
                        onChange={(e) => setProductId(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                     <textarea
                        placeholder="Checklist Items (Pass/Fail)"
                        value={checklistItems}
                        onChange={(e) => setChecklistItems(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Reviewer Name"
                        value={reviewerName}
                        onChange={(e) => setReviewerName(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                     <input
                        type="date"
                        placeholder="Review Date"
                        value={reviewDate}
                        onChange={(e) => setReviewDate(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                    <textarea
                        placeholder="Comments"
                        value={comments}
                        onChange={(e) => setComments(e.target.value)}
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

export default ComplianceChecklistForm; 