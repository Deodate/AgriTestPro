import React, { useState } from 'react';
import './QualityControlForm.css';

const QualityControlForm = () => {
  const [formData, setFormData] = useState({
    productName: '',
    batchNumber: '',
    inspectionDate: '',
    testParameters: '',
    result: '',
    inspectorName: '[Auto-filled Inspector Name]', // Placeholder for auto-filled value
    comment: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic here
    console.log('Quality Control Form submitted:', formData);
    // You would typically send this data to an API
    // Clear form or show success message
    setFormData({
      productName: '',
      batchNumber: '',
      inspectionDate: '',
      testParameters: '',
      result: '',
      inspectorName: '[Auto-filled Inspector Name]', // Reset to placeholder
      comment: '',
    });
  };

  const handleCancel = () => {
    // Handle cancel logic here, maybe close the form or clear fields
    console.log('Quality Control Form cancelled');
    setFormData({
      productName: '',
      batchNumber: '',
      inspectionDate: '',
      testParameters: '',
      result: '',
      inspectorName: '[Auto-filled Inspector Name]', // Reset to placeholder
      comment: '',
    });
  };

  return (
    <div className="qualitycontrol-form-container">
      <form className="qualitycontrol-form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="productName">Product Name:</label>
          <input
            type="text"
            id="productName"
            name="productName"
            value={formData.productName}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="batchNumber">Batch Number:</label>
          <input
            type="text"
            id="batchNumber"
            name="batchNumber"
            value={formData.batchNumber}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="inspectionDate">Inspection Date:</label>
          <input
            type="date"
            id="inspectionDate"
            name="inspectionDate"
            value={formData.inspectionDate}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="testParameters">Test Parameters:</label>
          <input
            type="text"
            id="testParameters"
            name="testParameters"
            value={formData.testParameters}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="result">Result:</label>
          <input
            type="text"
            id="result"
            name="result"
            value={formData.result}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="inspectorName">Inspector Name:</label>
          <input
            type="text"
            id="inspectorName"
            name="inspectorName"
            value={formData.inspectorName}
            readOnly // Make it read-only as it's auto-filled
          />
        </div>

        <div className="form-row">
          <label htmlFor="comment">Comment:</label>
          <textarea
            id="comment"
            name="comment"
            value={formData.comment}
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

export default QualityControlForm; 