import React, { useState } from 'react';
import './AlertConfigurationForm.css';

const AlertConfigurationForm = () => {
  const [formData, setFormData] = useState({
    triggerEvent: '',
    notificationType: '',
    recipients: '',
    frequency: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Form submitted:', formData);
    // Add form submission logic here
  };

  const handleCancel = () => {
    console.log('Form cancelled');
    // Add form cancellation logic here, likely navigate away or close form
  };

  return (
    <div className="alert-configuration-form-container">
      <form onSubmit={handleSubmit} className="alert-configuration-form">
        <div className="form-group">
          <input
            type="text"
            id="triggerEvent"
            name="triggerEvent"
            value={formData.triggerEvent}
            onChange={handleChange}
            placeholder="Trigger Event (e.g., Low Stock, Test Deadline)"
            required
          />
        </div>
        <div className="form-group">
          <input
            type="text"
            id="notificationType"
            name="notificationType"
            value={formData.notificationType}
            onChange={handleChange}
            placeholder="Notification Type (Email/SMS)"
            required
          />
        </div>
        <div className="form-group">
          <input
            type="text"
            id="recipients"
            name="recipients"
            value={formData.recipients}
            onChange={handleChange}
            placeholder="Recipients"
          />
        </div>
        <div className="form-group">
          <input
            type="text"
            id="frequency"
            name="frequency"
            value={formData.frequency}
            onChange={handleChange}
            placeholder="Frequency"
          />
        </div>

        <div className="button-group">
          <button type="submit" className="submit-button">Submit</button>
          <button type="button" onClick={handleCancel} className="cancel-button">Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default AlertConfigurationForm; 