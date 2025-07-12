import React, { useState } from 'react';
import './AutomatedAlertForm.css';

const AutomatedAlertForm = () => {
    const [alertType, setAlertType] = useState('');
    const [alertRecipient, setAlertRecipient] = useState('');
    const [notificationMethod, setNotificationMethod] = useState('');
    const [customMessage, setCustomMessage] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            alertType,
            alertRecipient,
            notificationMethod,
            customMessage
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="automated-alert-form-container">
            <form className="automated-alert-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <select
                        placeholder="Alert Type"
                        value={alertType}
                        onChange={(e) => setAlertType(e.target.value)}
                        required
                    >
                        <option value="">Select Alert Type</option>
                        <option value="Stock Expiry">Stock Expiry</option>
                        <option value="Testing Deadline">Testing Deadline</option>
                        <option value="Compliance Review">Compliance Review</option>
                    </select>
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Alert Recipient"
                        value={alertRecipient}
                        onChange={(e) => setAlertRecipient(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                    <select
                        placeholder="Notification Method"
                        value={notificationMethod}
                        onChange={(e) => setNotificationMethod(e.target.value)}
                        required
                    >
                        <option value="">Select Notification Method</option>
                        <option value="SMS">SMS</option>
                        <option value="Email">Email</option>
                        <option value="Dashboard">Dashboard</option>
                    </select>
                </div>
                 <div className="form-row">
                    <textarea
                        placeholder="Custom Message"
                        value={customMessage}
                        onChange={(e) => setCustomMessage(e.target.value)}
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

export default AutomatedAlertForm; 