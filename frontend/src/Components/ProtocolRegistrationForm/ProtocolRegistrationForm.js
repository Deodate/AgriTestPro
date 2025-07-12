import React, { useState } from 'react';
import './ProtocolRegistrationForm.css';

const ProtocolRegistrationForm = () => {
  const [formData, setFormData] = useState({
    protocolName: '',
    description: '',
    linkedTestTypes: '',
    approvalAuthority: '',
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
    <div className="protocol-registration-form-container">
      <form onSubmit={handleSubmit} className="protocol-registration-form">
        <div className="form-group">
          <input
            type="text"
            id="protocolName"
            name="protocolName"
            value={formData.protocolName}
            onChange={handleChange}
            placeholder="Protocol Name"
            required
          />
        </div>
        <div className="form-group">
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            placeholder="Description"
            required
          ></textarea>
        </div>
        <div className="form-group">
          <input
            type="text"
            id="linkedTestTypes"
            name="linkedTestTypes"
            value={formData.linkedTestTypes}
            onChange={handleChange}
            placeholder="Linked Test Types"
          />
        </div>
        <div className="form-group">
          <input
            type="text"
            id="approvalAuthority"
            name="approvalAuthority"
            value={formData.approvalAuthority}
            onChange={handleChange}
            placeholder="Approval Authority"
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

export default ProtocolRegistrationForm; 