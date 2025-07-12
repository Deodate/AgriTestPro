import React, { useState } from 'react';
import './ReminderSystemForm.css';

const ReminderSystemForm = () => {
  const [formData, setFormData] = useState({
    reminderTitle: '',
    reminderDate: '',
    reminderTime: '',
    frequency: '',
    assignTo: '',
    notes: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic here
    console.log('Reminder System Form submitted:', formData);
    // You would typically send this data to an API
    // Clear form or show success message
    setFormData({
      reminderTitle: '',
      reminderDate: '',
      reminderTime: '',
      frequency: '',
      assignTo: '',
      notes: '',
    });
  };

  const handleCancel = () => {
    // Handle cancel logic here, maybe close the form or clear fields
    console.log('Reminder System Form cancelled');
    setFormData({
      reminderTitle: '',
      reminderDate: '',
      reminderTime: '',
      frequency: '',
      assignTo: '',
      notes: '',
    });
  };

  return (
    <div className="reminder-system-form-container">
      <form className="reminder-system-form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="reminderTitle">Reminder Title:</label>
          <input
            type="text"
            id="reminderTitle"
            name="reminderTitle"
            value={formData.reminderTitle}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="reminderDate">Reminder Date:</label>
          <input
            type="date"
            id="reminderDate"
            name="reminderDate"
            value={formData.reminderDate}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="reminderTime">Reminder Time:</label>
          <input
            type="time"
            id="reminderTime"
            name="reminderTime"
            value={formData.reminderTime}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="frequency">Frequency:</label>
          <select
            id="frequency"
            name="frequency"
            value={formData.frequency}
            onChange={handleChange}
            required
          >
            <option value="">Select Frequency</option>
            <option value="Once">Once</option>
            <option value="Daily">Daily</option>
            <option value="Weekly">Weekly</option>
            <option value="Custom">Custom</option>
          </select>
        </div>

        <div className="form-row">
          <label htmlFor="assignTo">Assign To:</label>
          <input
            type="text"
            id="assignTo"
            name="assignTo"
            value={formData.assignTo}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="notes">Notes:</label>
          <textarea
            id="notes"
            name="notes"
            value={formData.notes}
            onChange={handleChange}
          />
        </div>

        <div className="button-row">
          <button type="submit">Save</button>
          <button type="button" onClick={handleCancel}>Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default ReminderSystemForm; 