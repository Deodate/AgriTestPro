import React, { useState } from 'react';
import './TestSchedulingForm.css';

const TestSchedulingForm = () => {
    const [productId, setProductId] = useState('');
    const [trialPhase, setTrialPhase] = useState('');
    const [scheduledDate, setScheduledDate] = useState('');
    const [assignedPersonnel, setAssignedPersonnel] = useState('');
    const [location, setLocation] = useState('');
    const [testObjective, setTestObjective] = useState('');
    const [equipmentRequired, setEquipmentRequired] = useState('');
    const [notificationPreference, setNotificationPreference] = useState('');
    const [notes, setNotes] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            productId,
            trialPhase,
            scheduledDate,
            assignedPersonnel,
            location,
            testObjective,
            equipmentRequired,
            notificationPreference,
            notes
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="test-scheduling-form-container">
            <form className="test-scheduling-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Test Name"
                        value={productId}
                        onChange={(e) => setProductId(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Trial Phase"
                        value={trialPhase}
                        onChange={(e) => setTrialPhase(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="date"
                        placeholder="Scheduled Date"
                        value={scheduledDate}
                        onChange={(e) => setScheduledDate(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Assigned Personnel"
                        value={assignedPersonnel}
                        onChange={(e) => setAssignedPersonnel(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Location"
                        value={location}
                        onChange={(e) => setLocation(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                    <textarea
                        placeholder="Test Objective"
                        value={testObjective}
                        onChange={(e) => setTestObjective(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <textarea
                        placeholder="Equipment Required"
                        value={equipmentRequired}
                        onChange={(e) => setEquipmentRequired(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                    <select
                        placeholder="Notification Preference"
                        value={notificationPreference}
                        onChange={(e) => setNotificationPreference(e.target.value)}
                        required
                    >
                        <option value="">Select Preference</option>
                        <option value="SMS">SMS</option>
                        <option value="Email">Email</option>
                    </select>
                </div>
                 <div className="form-row">
                    <textarea
                        placeholder="Notes"
                        value={notes}
                        onChange={(e) => setNotes(e.target.value)}
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

export default TestSchedulingForm; 