import React, { useState, useEffect } from 'react';
import { FaArrowLeft, FaCalendarAlt, FaPlus, FaTrash, FaSave } from 'react-icons/fa';
import './ComplianceForm.css';

const ComplianceForm = ({ onBack, onSave, initialData }) => {
  const [formData, setFormData] = useState({
    productId: '',
    reviewerName: '',
    reviewDate: new Date().toISOString().split('T')[0],
    comments: '',
    checklistItems: {}
  });

  // For adding new items
  const [newItem, setNewItem] = useState('');

  // Initialize with default checklist items or from initialData
  useEffect(() => {
    if (initialData) {
      setFormData(initialData);
    } else {
      // Default checklist items
      setFormData(prev => ({
        ...prev,
        checklistItems: {
          "Regulatory Documentation Complete": false,
          "Safety Data Sheet Available": false,
          "Ingredient List Compliant": false,
          "Warning Labels Adequate": false,
          "Packaging Meets Standards": false,
          "Environmental Impact Assessment": false
        }
      }));
    }
  }, [initialData]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleCheckboxChange = (itemName) => {
    setFormData(prev => ({
      ...prev,
      checklistItems: {
        ...prev.checklistItems,
        [itemName]: !prev.checklistItems[itemName]
      }
    }));
  };

  const addChecklistItem = () => {
    if (!newItem.trim()) return;
    
    // Don't add if it already exists
    if (formData.checklistItems.hasOwnProperty(newItem)) {
      alert("This item already exists in the checklist");
      return;
    }
    
    setFormData(prev => ({
      ...prev,
      checklistItems: {
        ...prev.checklistItems,
        [newItem]: false
      }
    }));
    
    setNewItem('');
  };

  const removeChecklistItem = (itemName) => {
    const updatedChecklistItems = { ...formData.checklistItems };
    delete updatedChecklistItems[itemName];
    
    setFormData(prev => ({
      ...prev,
      checklistItems: updatedChecklistItems
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (onSave) {
      onSave(formData);
    }
  };

  return (
    <div className="compliance-form-container">
      <div className="compliance-form-header">
        <h1>Create Compliance Checklist</h1>
        <button className="back-button" onClick={onBack}>
          Back to List
        </button>
      </div>

      <div className="compliance-form-content">
        <form onSubmit={handleSubmit}>
          <div className="form-field">
            <label htmlFor="productId">
              Product <span className="required">*</span>
            </label>
            <select
              id="productId"
              name="productId"
              className="form-select"
              value={formData.productId}
              onChange={handleInputChange}
              required
            >
              <option value="">Select a product</option>
              <option value="1">Product A</option>
              <option value="2">Product B</option>
              <option value="101">Fungicide X-500</option>
              <option value="102">Organic Fertilizer B-200</option>
              <option value="103">Insecticide Z-100</option>
              <option value="104">Growth Enhancer G-300</option>
            </select>
          </div>

          <div className="form-field">
            <label htmlFor="reviewerName">
              Reviewer Name <span className="required">*</span>
            </label>
            <input
              type="text"
              id="reviewerName"
              name="reviewerName"
              className="form-input"
              placeholder="Enter reviewer name"
              value={formData.reviewerName}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="form-field">
            <label htmlFor="reviewDate">
              Review Date <span className="required">*</span>
            </label>
            <div className="date-input-wrapper">
              <input
                type="date"
                id="reviewDate"
                name="reviewDate"
                className="form-input date-input"
                value={formData.reviewDate}
                onChange={handleInputChange}
                required
              />
              <FaCalendarAlt className="date-icon" />
            </div>
          </div>

          <div className="form-field">
            <label htmlFor="comments">Comments</label>
            <textarea
              id="comments"
              name="comments"
              className="form-textarea"
              placeholder="Enter any comments about this compliance check"
              value={formData.comments}
              onChange={handleInputChange}
              rows="4"
            />
          </div>

          <div className="checklist-section">
            <h2>Checklist Items <span className="required">*</span></h2>
            
            <div className="add-item">
              <input
                type="text"
                className="form-input"
                placeholder="Enter a checklist item"
                value={newItem}
                onChange={(e) => setNewItem(e.target.value)}
              />
              <button 
                type="button" 
                className="add-item-btn"
                onClick={addChecklistItem}
              >
                <FaPlus /> Add
              </button>
            </div>

            <div className="checklist-items">
              {Object.entries(formData.checklistItems).map(([itemName, isChecked]) => (
                <div key={itemName} className="checklist-item">
                  <div className="item-content">
                    <input
                      type="checkbox"
                      id={`item-${itemName}`}
                      checked={isChecked}
                      onChange={() => handleCheckboxChange(itemName)}
                      className="item-checkbox"
                    />
                    <label htmlFor={`item-${itemName}`} className="item-label">
                      {itemName}
                    </label>
                  </div>
                  <button
                    type="button"
                    className="remove-item-btn"
                    onClick={() => removeChecklistItem(itemName)}
                  >
                    <FaTrash />
                  </button>
                </div>
              ))}
              {Object.keys(formData.checklistItems).length === 0 && (
                <div className="no-items">No checklist items added yet</div>
              )}
            </div>
          </div>

          <div className="form-actions">
            <button 
              type="button" 
              className="cancel-btn"
              onClick={onBack}
            >
              Cancel
            </button>
            <button 
              type="submit" 
              className="save-btn"
            >
              <FaSave /> Save Checklist
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ComplianceForm; 