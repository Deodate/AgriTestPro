import React, { useState } from 'react';
// import './ResourceAllocationForm.css'; // Assuming a CSS file will be created later

const ResourceAllocationForm = () => {
  const [eventName, setEventName] = useState('');
  const [type, setType] = useState('');
  const [dateTime, setDateTime] = useState('');
  const [linkedProjectTest, setLinkedProjectTest] = useState('');
  const [participants, setParticipants] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic here
    console.log({
      eventName,
      type,
      dateTime,
      linkedProjectTest,
      participants,
    });
    // Clear form or show success message
  };

  const handleCancel = () => {
    // Handle cancel logic here, maybe close the form or clear fields
    console.log('Form cancelled');
  };

  return (
    <div className="resource-allocation-form-container">
      <form className="resource-allocation-form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="eventName">Event Name:</label>
          <input
            type="text"
            id="eventName"
            value={eventName}
            onChange={(e) => setEventName(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <label htmlFor="type">Type:</label>
          <select
            id="type"
            value={type}
            onChange={(e) => setType(e.target.value)}
            required
          >
            <option value="">Select Type</option>
            <option value="Test Phase">Test Phase</option>
            <option value="Meeting">Meeting</option>
            <option value="Reminder">Reminder</option>
          </select>
        </div>
        <div className="form-row">
          <label htmlFor="dateTime">Date & Time:</label>
          <input
            type="datetime-local"
            id="dateTime"
            value={dateTime}
            onChange={(e) => setDateTime(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <label htmlFor="linkedProjectTest">Linked Project/Test:</label>
          <input
            type="text"
            id="linkedProjectTest"
            value={linkedProjectTest}
            onChange={(e) => setLinkedProjectTest(e.target.value)}
          />
        </div>
        <div className="form-row">
          <label htmlFor="participants">Participants:</label>
          <textarea
            id="participants"
            value={participants}
            onChange={(e) => setParticipants(e.target.value)}
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

export default ResourceAllocationForm; 