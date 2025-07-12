import React, { useState } from 'react';
import './SessionManagementForm.css';

const SessionManagementForm = () => {
    const [userId, setUserId] = useState('');
    const [sessionId, setSessionId] = useState('');
    const [loginTime, setLoginTime] = useState('');
    const [lastActivityTime, setLastActivityTime] = useState('');
    const [ipAddress, setIpAddress] = useState('');
    const [status, setStatus] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            userId,
            sessionId,
            loginTime,
            lastActivityTime,
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
        <div className="session-management-form-container">
            <form className="session-management-form" onSubmit={handleSubmit}>
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
                        placeholder="Session ID"
                        value={sessionId}
                        onChange={(e) => setSessionId(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="datetime-local"
                        placeholder="Login Time"
                        value={loginTime}
                        onChange={(e) => setLoginTime(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="datetime-local"
                        placeholder="Last Activity Time"
                        value={lastActivityTime}
                        onChange={(e) => setLastActivityTime(e.target.value)}
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
                        <option value="Active">Active</option>
                        <option value="Inactive">Inactive</option>
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

export default SessionManagementForm; 