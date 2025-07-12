import React, { useState } from 'react';
import './StatusUpdateForm.css';

const StatusUpdateForm = () => {
  const [formData, setFormData] = useState({
    entityType: '',
    entityName: '',
    newStatus: '',
    updatedBy: '',
    date: '',
    comments: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic here
    console.log('Status Update Form submitted:', formData);
    // You would typically send this data to an API
    // Clear form or show success message
    setFormData({
      entityType: '',
      entityName: '',
      newStatus: '',
      updatedBy: '',
      date: '',
      comments: '',
    });
  };

  const handleCancel = () => {
    // Handle cancel logic here, maybe close the form or clear fields
    console.log('Status Update Form cancelled');
    setFormData({
      entityType: '',
      entityName: '',
      newStatus: '',
      updatedBy: '',
      date: '',
      comments: '',
    });
  };

  return (
    <div className="status-update-form-container">
      <form className="status-update-form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="entityType">Entity Type:</label>
          <input
            type="text"
            id="entityType"
            name="entityType"
            value={formData.entityType}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="entityName">Entity Name:</label>
          <input
            type="text"
            id="entityName"
            name="entityName"
            value={formData.entityName}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="newStatus">New Status:</label>
          <input
            type="text"
            id="newStatus"
            name="newStatus"
            value={formData.newStatus}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="updatedBy">Updated By:</label>
          <input
            type="text"
            id="updatedBy"
            name="updatedBy"
            value={formData.updatedBy}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="date">Date:</label>
          <input
            type="date"
            id="date"
            name="date"
            value={formData.date}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="comments">Comments:</label>
          <textarea
            id="comments"
            name="comments"
            value={formData.comments}
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

export default StatusUpdateForm; 