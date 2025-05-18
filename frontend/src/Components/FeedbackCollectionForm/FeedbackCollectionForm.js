import React, { useState } from 'react';
import './FeedbackCollectionForm.css';

const FeedbackCollectionForm = () => {
  const [formData, setFormData] = useState({
    submittedBy: '',
    topicModule: '',
    message: '',
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
    <div className="feedback-collection-form-container">
      <form onSubmit={handleSubmit} className="feedback-collection-form">
        <div className="form-group">
          <input
            type="text"
            id="submittedBy"
            name="submittedBy"
            value={formData.submittedBy}
            onChange={handleChange}
            placeholder="Submitted By"
            required
          />
        </div>
        <div className="form-group">
          <input
            type="text"
            id="topicModule"
            name="topicModule"
            value={formData.topicModule}
            onChange={handleChange}
            placeholder="Topic/Module"
            required
          />
        </div>
        <div className="form-group">
          <textarea
            id="message"
            name="message"
            value={formData.message}
            onChange={handleChange}
            placeholder="Message"
            required
          ></textarea>
        </div>

        <div className="button-group">
          <button type="submit" className="submit-button">Submit</button>
          <button type="button" onClick={handleCancel} className="cancel-button">Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default FeedbackCollectionForm; 