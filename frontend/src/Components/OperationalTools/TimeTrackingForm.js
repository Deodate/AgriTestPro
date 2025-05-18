import React, { useState } from 'react';
import './TimeTrackingForm.css'; // Optional: for specific form styling

const TimeTrackingForm = () => {
  const [staffName, setStaffName] = useState('');
  const [activity, setActivity] = useState('');
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');
  const [totalHours, setTotalHours] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic here
    console.log({
      staffName,
      activity,
      startTime,
      endTime,
      totalHours,
    });
    // Clear form or show success message
  };

  const handleCancel = () => {
    // Handle cancel logic here, maybe close the form or clear fields
    console.log('Form cancelled');
  };

  // Helper to calculate total hours (basic implementation, can be refined)
  const calculateTotalHours = (start, end) => {
    if (!start || !end) return '';
    const startDate = new Date(start);
    const endDate = new Date(end);
    const diffMs = endDate - startDate;
    if (diffMs < 0) return 'Invalid time range';
    const diffHours = diffMs / (1000 * 60 * 60);
    return diffHours.toFixed(2);
  };

  React.useEffect(() => {
    setTotalHours(calculateTotalHours(startTime, endTime));
  }, [startTime, endTime]);

  return (
    <div className="time-tracking-form-container">
      <form className="time-tracking-form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="staffName">Staff Name:</label>
          <input
            type="text"
            id="staffName"
            value={staffName}
            onChange={(e) => setStaffName(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <label htmlFor="activity">Activity:</label>
          <input
            type="text"
            id="activity"
            value={activity}
            onChange={(e) => setActivity(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <label htmlFor="startTime">Start Time:</label>
          <input
            type="datetime-local"
            id="startTime"
            value={startTime}
            onChange={(e) => setStartTime(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <label htmlFor="endTime">End Time:</label>
          <input
            type="datetime-local"
            id="endTime"
            value={endTime}
            onChange={(e) => setEndTime(e.target.value)}
            required
          />
        </div>
         <div className="form-row">
          <label htmlFor="totalHours">Total Hours:</label>
          <input
            type="text"
            id="totalHours"
            value={totalHours}
            readOnly // Calculated field
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

export default TimeTrackingForm; 