import React, { useState } from 'react';
import './EffectivenessEvaluationForm.css';

const EffectivenessEvaluationForm = () => {
  const [formData, setFormData] = useState({
    productName: '',
    cropType: '',
    applicationDate: '',
    observedEffects: '',
    effectivenessRating: '',
    weatherConditions: '',
    evaluationDate: '',
    evaluatorName: '',
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
    console.log('Effectiveness Evaluation Form submitted:', formData);
    // You would typically send this data (including the file) to an API
    // Clear form or show success message
    setFormData({
      productName: '',
      cropType: '',
      applicationDate: '',
      observedEffects: '',
      effectivenessRating: '',
      weatherConditions: '',
      evaluationDate: '',
      evaluatorName: '',
      attachment: null,
    });
  };

  const handleCancel = () => {
    // Handle cancel logic here, maybe close the form or clear fields
    console.log('Effectiveness Evaluation Form cancelled');
    setFormData({
      productName: '',
      cropType: '',
      applicationDate: '',
      observedEffects: '',
      effectivenessRating: '',
      weatherConditions: '',
      evaluationDate: '',
      evaluatorName: '',
      attachment: null,
    });
  };

  return (
    <div className="effectiveness-evaluation-form-container">
      <form className="effectiveness-evaluation-form" onSubmit={handleSubmit}>
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
          <label htmlFor="cropType">Crop Type:</label>
          <input
            type="text"
            id="cropType"
            name="cropType"
            value={formData.cropType}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="applicationDate">Application Date:</label>
          <input
            type="date"
            id="applicationDate"
            name="applicationDate"
            value={formData.applicationDate}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="observedEffects">Observed Effects:</label>
          <textarea
            id="observedEffects"
            name="observedEffects"
            value={formData.observedEffects}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="effectivenessRating">Effectiveness Rating:</label>
          <input
            type="text"
            id="effectivenessRating"
            name="effectivenessRating"
            value={formData.effectivenessRating}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="weatherConditions">Weather Conditions:</label>
          <input
            type="text"
            id="weatherConditions"
            name="weatherConditions"
            value={formData.weatherConditions}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="evaluationDate">Evaluation Date:</label>
          <input
            type="date"
            id="evaluationDate"
            name="evaluationDate"
            value={formData.evaluationDate}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="evaluatorName">Evaluator Name:</label>
          <input
            type="text"
            id="evaluatorName"
            name="evaluatorName"
            value={formData.evaluatorName}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="attachment">Attach Photos or File:</label>
          <input
            type="file"
            id="attachment"
            name="attachment"
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

export default EffectivenessEvaluationForm; 