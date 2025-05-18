import React, { useState } from 'react';
import './FieldActivityTrackingForm.css';

const FieldActivityTrackingForm = () => {
    const [observations, setObservations] = useState('');
    const [attachments, setAttachments] = useState(null);

    const [fieldSection, setFieldSection] = useState('');
    const [activityType, setActivityType] = useState('');
    const [date, setDate] = useState('');
    const [staffInvolved, setStaffInvolved] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            fieldSection,
            activityType,
            date,
            staffInvolved,
            observations,
            attachments
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    const handleFileChange = (e) => {
        setAttachments(e.target.files[0]);
    };

    return (
        <div className="field-activity-tracking-form-container">
            <form className="field-activity-tracking-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <label htmlFor="fieldSection">Field Section:</label>
                    <input
                        type="text"
                        id="fieldSection"
                        value={fieldSection}
                        onChange={(e) => setFieldSection(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="activityType">Activity Type:</label>
                    <input
                        type="text"
                        id="activityType"
                        value={activityType}
                        onChange={(e) => setActivityType(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="date">Date:</label>
                    <input
                        type="date"
                        id="date"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="staffInvolved">Staff Involved:</label>
                    <input
                        type="text"
                        id="staffInvolved"
                        value={staffInvolved}
                        onChange={(e) => setStaffInvolved(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="observations">Observations:</label>
                    <textarea
                        id="observations"
                        value={observations}
                        onChange={(e) => setObservations(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="attachments">Attachments:</label>
                    <input
                        type="file"
                        id="attachments"
                        onChange={handleFileChange}
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

export default FieldActivityTrackingForm; 