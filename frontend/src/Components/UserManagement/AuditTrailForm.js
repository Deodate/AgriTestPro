import React, { useState } from 'react';
import './AuditTrailForm.css';

const AuditTrailForm = () => {
    const [userId, setUserId] = useState('');
    const [actionType, setActionType] = useState('');
    const [timestamp, setTimestamp] = useState('');
    const [affectedRecordId, setAffectedRecordId] = useState('');
    const [details, setDetails] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            userId,
            actionType,
            timestamp,
            affectedRecordId,
            details
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="audit-trail-form-container">
            <form className="audit-trail-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="User ID"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Action Type"
                        value={actionType}
                        onChange={(e) => setActionType(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="datetime-local"
                        placeholder="Timestamp"
                        value={timestamp}
                        onChange={(e) => setTimestamp(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Affected Record ID"
                        value={affectedRecordId}
                        onChange={(e) => setAffectedRecordId(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                    <textarea
                        placeholder="Details"
                        value={details}
                        onChange={(e) => setDetails(e.target.value)}
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

export default AuditTrailForm; 