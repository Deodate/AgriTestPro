import React, { useState } from 'react';
import './UserActivityLogForm.css';

const UserActivityLogForm = () => {
    const [userId, setUserId] = useState('');
    const [activityType, setActivityType] = useState('');
    const [timestamp, setTimestamp] = useState('');
    const [ipAddress, setIpAddress] = useState('');
    const [status, setStatus] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            userId,
            activityType,
            timestamp,
            ipAddress,
            status
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="user-activity-log-form-container">
            <form className="user-activity-log-form" onSubmit={handleSubmit}>
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
                    <select
                        placeholder="Activity Type"
                        value={activityType}
                        onChange={(e) => setActivityType(e.target.value)}
                        required
                    >
                        <option value="">Select Activity Type</option>
                        <option value="Login">Login</option>
                        <option value="Logout">Logout</option>
                        <option value="File Access">File Access</option>
                        <option value="Data Modification">Data Modification</option>
                    </select>
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
                        placeholder="IP Address"
                        value={ipAddress}
                        onChange={(e) => setIpAddress(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                    <select
                        placeholder="Status"
                        value={status}
                        onChange={(e) => setStatus(e.target.value)}
                        required
                    >
                        <option value="">Select Status</option>
                        <option value="Success">Success</option>
                        <option value="Failed">Failed</option>
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

export default UserActivityLogForm; 