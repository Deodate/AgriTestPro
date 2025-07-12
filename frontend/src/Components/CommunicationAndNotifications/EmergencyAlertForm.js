import React, { useState } from 'react';
import './EmergencyAlertForm.css';

const EmergencyAlertForm = () => {
  const [formData, setFormData] = useState({
    alertTitle: '',
    messageBody: '',
    recipients: '',
    sendTime: '',
    attachment: null, // For file attachment
  });

  const handleChange = (e) => {
    const { name, value, type, files } = e.target;
    // Handle file input separately
    if (type === 'file') {
      setFormData({ ...formData, [name]: files[0] });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic here
    console.log('Emergency Alert Form submitted:', formData);
    // You would typically send this data (including the file) to an API
    // Clear form or show success message
    setFormData({
      alertTitle: '',
      messageBody: '',
      recipients: '',
      sendTime: '',
      attachment: null,
    });
  };

  const handleCancel = () => {
    // Handle cancel logic here, maybe close the form or clear fields
    console.log('Emergency Alert Form cancelled');
    setFormData({
      alertTitle: '',
      messageBody: '',
      recipients: '',
      sendTime: '',
      attachment: null,
    });
  };

  return (
    <div className="emergency-alert-form-container">
    
      <form className="emergency-alert-form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="alertTitle">Alert Title:</label>
          <input
            type="text"
            id="alertTitle"
            name="alertTitle"
            value={formData.alertTitle}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="messageBody">Message Body:</label>
          <textarea
            id="messageBody"
            name="messageBody"
            value={formData.messageBody}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="recipients">Recipients:</label>
          <input
            type="text"
            id="recipients"
            name="recipients"
            value={formData.recipients}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="sendTime">Send Time:</label>
          <input
            type="time"
            id="sendTime"
            name="sendTime"
            value={formData.sendTime}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="attachment">Attach File:</label>
          <input
            type="file"
            id="attachment"
            name="attachment"
            onChange={handleChange}
          />
        </div>

        <div className="button-row">
          <button type="submit">Send</button>
          <button type="button" onClick={handleCancel}>Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default EmergencyAlertForm; 