import React, { useState } from 'react';
import './ReportSchedulerForm.css';

const ReportSchedulerForm = () => {
  const [formData, setFormData] = useState({
    reportType: '',
    scheduleFrequency: '',
    startDate: '',
    time: '',
    additionalNotes: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic here
    console.log('Report Scheduler Form submitted:', formData);
    // You would typically send this data to an API
    // Clear form or show success message
    setFormData({
      reportType: '',
      scheduleFrequency: '',
      startDate: '',
      time: '',
      additionalNotes: '',
    });
  };

  const handleCancel = () => {
    // Handle cancel logic here, maybe close the form or clear fields
    console.log('Report Scheduler Form cancelled');
    setFormData({
      reportType: '',
      scheduleFrequency: '',
      startDate: '',
      time: '',
      additionalNotes: '',
    });
  };

  return (
    <div className="report-scheduler-form-container">
      <form className="report-scheduler-form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="reportType">Report Type:</label>
          <input
            type="text"
            id="reportType"
            name="reportType"
            value={formData.reportType}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="scheduleFrequency">Schedule Frequency:</label>
          <select
            id="scheduleFrequency"
            name="scheduleFrequency"
            value={formData.scheduleFrequency}
            onChange={handleChange}
            required
          >
            <option value="">Select Frequency</option>
            <option value="Daily">Daily</option>
            <option value="Weekly">Weekly</option>
            <option value="Monthly">Monthly</option>
          </select>
        </div>

        <div className="form-row">
          <label htmlFor="startDate">Start Date:</label>
          <input
            type="date"
            id="startDate"
            name="startDate"
            value={formData.startDate}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="time">Time:</label>
          <input
            type="time"
            id="time"
            name="time"
            value={formData.time}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="additionalNotes">Additional Notes:</label>
          <textarea
            id="additionalNotes"
            name="additionalNotes"
            value={formData.additionalNotes}
            onChange={handleChange}
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

export default ReportSchedulerForm; 