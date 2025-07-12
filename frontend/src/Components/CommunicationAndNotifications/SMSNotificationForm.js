import React, { useState } from 'react';
import './SMSNotificationForm.css';

const SMSNotificationForm = () => {
  const [formData, setFormData] = useState({
    templateName: '',
    messageType: '',
    smsBody: '',
    triggerEvent: '',
    recipients: '',
    sendImmediately: false,
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({ ...formData, [name]: type === 'checkbox' ? checked : value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic here
    console.log('SMS Notification Form submitted:', formData);
    // You would typically send this data to an API
    // Clear form or show success message
    setFormData({
      templateName: '',
      messageType: '',
      smsBody: '',
      triggerEvent: '',
      recipients: '',
      sendImmediately: false,
    });
  };

  const handleCancel = () => {
    // Handle cancel logic here, maybe close the form or clear fields
    console.log('SMS Notification Form cancelled');
    setFormData({
      templateName: '',
      messageType: '',
      smsBody: '',
      triggerEvent: '',
      recipients: '',
      sendImmediately: false,
    });
  };

  return (
    <div className="sms-notification-form-container">
      <form className="sms-notification-form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="templateName">Template Name:</label>
          <input
            type="text"
            id="templateName"
            name="templateName"
            value={formData.templateName}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="messageType">Message Type:</label>
          <input
            type="text"
            id="messageType"
            name="messageType"
            value={formData.messageType}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="smsBody">SMS Body:</label>
          <textarea
            id="smsBody"
            name="smsBody"
            value={formData.smsBody}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="triggerEvent">Trigger Event:</label>
          <input
            type="text"
            id="triggerEvent"
            name="triggerEvent"
            value={formData.triggerEvent}
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
          <label htmlFor="sendImmediately">Send Immediately?:</label>
          <input
            type="checkbox"
            id="sendImmediately"
            name="sendImmediately"
            checked={formData.sendImmediately}
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

export default SMSNotificationForm; 