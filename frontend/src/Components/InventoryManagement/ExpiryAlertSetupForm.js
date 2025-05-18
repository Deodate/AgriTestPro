import React, { useState } from 'react';
import './ExpiryAlertSetupForm.css';

const ExpiryAlertSetupForm = () => {
    const [product, setProduct] = useState('');
    const [alertThreshold, setAlertThreshold] = useState('');
    const [notificationMethod, setNotificationMethod] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            product,
            alertThreshold,
            notificationMethod
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="expiry-alert-setup-form-container">
            <form className="expiry-alert-setup-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Product"
                        value={product}
                        onChange={(e) => setProduct(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="number"
                        placeholder="Alert Threshold (Days before expiry)"
                        value={alertThreshold}
                        onChange={(e) => setAlertThreshold(e.target.value)}
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
                        <option value="Email">Email</option>
                        <option value="SMS">SMS</option>
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

export default ExpiryAlertSetupForm; 