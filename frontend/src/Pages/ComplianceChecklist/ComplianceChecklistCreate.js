import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaPlus, FaMinus, FaCalendarAlt, FaSave } from 'react-icons/fa';
import './ComplianceChecklist.css';
import Header from '../../Components/Header/Header';
import Footer from '../../Components/Footer/Footer';

/**
 * Component for creating a new compliance checklist
 * @returns {JSX.Element} Rendered component
 */
const ComplianceChecklistCreate = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [products, setProducts] = useState([]);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);
  
  // Form state
  const [formData, setFormData] = useState({
    productId: '',
    reviewerName: '',
    reviewDate: new Date().toISOString().split('T')[0], // Today's date
    comments: '',
    checklistItems: {}
  });
  
  // Manage checklist items
  const [checklistItemName, setChecklistItemName] = useState('');
  
  useEffect(() => {
    // Fetch products for dropdown
    const fetchProducts = async () => {
      try {
        // Mock data for now - replace with API call
        const mockProducts = [
          { id: 101, name: "Fungicide X-500" },
          { id: 102, name: "Organic Fertilizer B-200" },
          { id: 103, name: "Insecticide Z-100" },
          { id: 104, name: "Growth Enhancer G-300" }
        ];
        
        setProducts(mockProducts);
      } catch (err) {
        setError("Failed to fetch products. Please try again later.");
      }
    };
    
    fetchProducts();
  }, []);
  
  // Handle input change
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };
  
  // Add checklist item
  const addChecklistItem = () => {
    if (!checklistItemName.trim()) return;
    
    setFormData({
      ...formData,
      checklistItems: {
        ...formData.checklistItems,
        [checklistItemName]: false
      }
    });
    
    setChecklistItemName('');
  };
  
  // Remove checklist item
  const removeChecklistItem = (itemName) => {
    const updatedChecklistItems = { ...formData.checklistItems };
    delete updatedChecklistItems[itemName];
    
    setFormData({
      ...formData,
      checklistItems: updatedChecklistItems
    });
  };
  
  // Toggle checklist item status
  const toggleChecklistItemStatus = (itemName) => {
    setFormData({
      ...formData,
      checklistItems: {
        ...formData.checklistItems,
        [itemName]: !formData.checklistItems[itemName]
      }
    });
  };
  
  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    
    // Validation
    if (!formData.productId) {
      setError("Please select a product");
      setLoading(false);
      return;
    }
    
    if (!formData.reviewerName) {
      setError("Please enter a reviewer name");
      setLoading(false);
      return;
    }
    
    if (Object.keys(formData.checklistItems).length === 0) {
      setError("Please add at least one checklist item");
      setLoading(false);
      return;
    }
    
    try {
      // Mock API call - replace with actual API call
      console.log("Submitting checklist:", formData);
      
      // Simulate API delay
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      setSuccessMessage("Compliance checklist created successfully!");
      setTimeout(() => {
        navigate('/compliance-checklist');
      }, 2000);
    } catch (err) {
      setError("Failed to create compliance checklist. Please try again.");
    } finally {
      setLoading(false);
    }
  };
  
  return (
    <div className="compliance-checklist-container">
      <Header />
      
      <div className="compliance-checklist-content">
        <div className="compliance-checklist-header">
          <h1>Create Compliance Checklist</h1>
        </div>
        
        {successMessage && (
          <div className="success-message">
            {successMessage}
          </div>
        )}
        
        {error && (
          <div className="error-message">
            {error}
          </div>
        )}
        
        <form onSubmit={handleSubmit} className="compliance-form">
          <div className="form-group">
            <label htmlFor="productId">Product *</label>
            <select
              id="productId"
              name="productId"
              value={formData.productId}
              onChange={handleInputChange}
              required
              className="form-control"
            >
              <option value="">Select a product</option>
              {products.map(product => (
                <option key={product.id} value={product.id}>
                  {product.name}
                </option>
              ))}
            </select>
          </div>
          
          <div className="form-group">
            <label htmlFor="reviewerName">Reviewer Name *</label>
            <input
              type="text"
              id="reviewerName"
              name="reviewerName"
              value={formData.reviewerName}
              onChange={handleInputChange}
              required
              className="form-control"
              placeholder="Enter reviewer name"
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="reviewDate">Review Date *</label>
            <div className="date-input-container">
              <FaCalendarAlt className="date-icon" />
              <input
                type="date"
                id="reviewDate"
                name="reviewDate"
                value={formData.reviewDate}
                onChange={handleInputChange}
                required
                className="form-control"
              />
            </div>
          </div>
          
          <div className="form-group">
            <label htmlFor="comments">Comments</label>
            <textarea
              id="comments"
              name="comments"
              value={formData.comments}
              onChange={handleInputChange}
              className="form-control"
              placeholder="Enter any comments about this compliance check"
              rows="4"
            />
          </div>
          
          <div className="checklist-items-section">
            <h3>Checklist Items *</h3>
            
            <div className="add-checklist-item">
              <input
                type="text"
                value={checklistItemName}
                onChange={(e) => setChecklistItemName(e.target.value)}
                placeholder="Enter a checklist item"
                className="form-control"
              />
              <button
                type="button"
                onClick={addChecklistItem}
                className="add-item-button"
                disabled={!checklistItemName.trim()}
              >
                <FaPlus /> Add
              </button>
            </div>
            
            {Object.keys(formData.checklistItems).length > 0 ? (
              <div className="checklist-items-list">
                {Object.entries(formData.checklistItems).map(([itemName, status]) => (
                  <div key={itemName} className="checklist-item">
                    <div className="checklist-item-content">
                      <input
                        type="checkbox"
                        id={`item-${itemName}`}
                        checked={status}
                        onChange={() => toggleChecklistItemStatus(itemName)}
                        className="checklist-checkbox"
                      />
                      <label htmlFor={`item-${itemName}`} className="checklist-label">
                        {itemName}
                      </label>
                    </div>
                    <button
                      type="button"
                      onClick={() => removeChecklistItem(itemName)}
                      className="remove-item-button"
                    >
                      <FaMinus />
                    </button>
                  </div>
                ))}
              </div>
            ) : (
              <p className="no-items-message">No checklist items added yet.</p>
            )}
          </div>
          
          <div className="form-buttons">
            <button
              type="button"
              onClick={() => navigate('/compliance-checklist')}
              className="cancel-button"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="submit-button"
              disabled={loading}
            >
              <FaSave /> {loading ? 'Saving...' : 'Save Checklist'}
            </button>
          </div>
        </form>
      </div>
      
      <Footer />
    </div>
  );
};

export default ComplianceChecklistCreate; 