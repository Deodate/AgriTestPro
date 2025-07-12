import React, { useState } from 'react';
import './CalendarManagementForm.css';

const CalendarManagementForm = () => {
    const [eventTitle, setEventTitle] = useState('');
    const [description, setDescription] = useState('');
    const [dateTime, setDateTime] = useState('');
    const [eventType, setEventType] = useState('');
    const [assignedParticipants, setAssignedParticipants] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            eventTitle,
            description,
            dateTime,
            eventType,
            assignedParticipants
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="calendar-management-form-container">
            <form className="calendar-management-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Event Title"
                        value={eventTitle}
                        onChange={(e) => setEventTitle(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <textarea
                        placeholder="Description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="datetime-local"
                        placeholder="Date & Time"
                        value={dateTime}
                        onChange={(e) => setDateTime(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <select
                        placeholder="Event Type"
                        value={eventType}
                        onChange={(e) => setEventType(e.target.value)}
                        required
                    >
                        <option value="">Select Event Type</option>
                        <option value="Meeting">Meeting</option>
                        <option value="Task Deadline">Task Deadline</option>
                        <option value="Testing Schedule">Testing Schedule</option>
                    </select>
                </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Assigned Participants"
                        value={assignedParticipants}
                        onChange={(e) => setAssignedParticipants(e.target.value)}
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

export default CalendarManagementForm; 