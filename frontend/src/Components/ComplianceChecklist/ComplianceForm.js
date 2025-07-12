<<<<<<< HEAD
import React, { useState, useEffect } from 'react';
import { FaArrowLeft, FaCalendarAlt, FaPlus, FaTrash, FaSave } from 'react-icons/fa';
import './ComplianceForm.css';

const ComplianceForm = ({ onBack, onSave, initialData }) => {
=======
import React, { useState, useEffect, useRef } from 'react';
import { FaArrowLeft, FaCalendarAlt, FaPlus, FaTrash, FaSave } from 'react-icons/fa';
import './ComplianceForm.css';
import axios from 'axios';
import { AUTH_SETTINGS, API_CONFIG } from '../../config';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const ComplianceForm = ({ onBack, onSave, initialData }) => {
  const newItemInputRef = useRef(null); // Create a ref for the new item input

>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
  const [formData, setFormData] = useState({
    productId: '',
    reviewerName: '',
    reviewDate: new Date().toISOString().split('T')[0],
    comments: '',
<<<<<<< HEAD
    checklistItems: {}
  });

  // For adding new items
  const [newItem, setNewItem] = useState('');
=======
    checklistItems: [], // Initialize as an array
    productName: '' // Add productName to state
  });

  const [products, setProducts] = useState([]);
  const [isLoadingProducts, setIsLoadingProducts] = useState(false);
  const [productError, setProductError] = useState(null);

  // For adding new items
  const [newItem, setNewItem] = useState('Regulatory documentations completes'); // Default value
  const [newItemError, setNewItemError] = useState(''); // New state for validation error
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589

  // Initialize with default checklist items or from initialData
  useEffect(() => {
    if (initialData) {
<<<<<<< HEAD
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
=======
      // Transform initialData.checklistItems (which is a Map/Object) into an array of {name, status} objects
      const transformedChecklistItems = initialData.checklistItems
        ? Object.entries(initialData.checklistItems).map(([name, status]) => ({ name, status }))
        : [];
      setFormData({
        ...initialData,
        checklistItems: transformedChecklistItems,
        productName: initialData.product?.name || '' // Set productName from initialData if available
      });
    } else {
      // Default checklist items as an array of objects
      setFormData(prev => ({
        ...prev,
        checklistItems: [
          { name: 'Regulatory documentations completes', status: false },
          { name: 'Safety Data Sheet Available', status: false },
          { name: 'Packaging Meets Standards', status: false },
        ],
        productName: '' // Ensure productName is empty for new forms
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
      }));
    }
  }, [initialData]);

<<<<<<< HEAD
=======
  // Effect to fetch products on component mount
  useEffect(() => {
    const fetchProducts = async () => {
      setIsLoadingProducts(true);
      setProductError(null);
      const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);

      if (!token) {
        setProductError('Authentication token not found. Cannot fetch products.');
        setIsLoadingProducts(false);
        return;
      }

      const apiBaseUrl = API_CONFIG.BASE_URL || 'http://localhost:8888';

      try {
        const response = await axios.get(`${apiBaseUrl}/api/products`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        console.log('Fetched products:', response.data);
        setProducts(response.data);
      } catch (error) {
        console.error('Error fetching products:', error);
        setProductError('Failed to load products.');
      } finally {
        setIsLoadingProducts(false);
      }
    };

    fetchProducts();
  }, []);

>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

<<<<<<< HEAD
  const handleCheckboxChange = (itemName) => {
    setFormData(prev => ({
      ...prev,
      checklistItems: {
        ...prev.checklistItems,
        [itemName]: !prev.checklistItems[itemName]
      }
=======
  const handleChecklistItemChange = (index) => {
    const newChecklistItems = formData.checklistItems.map((item, i) => {
      if (i === index) {
        // Set newItem state to the item's name when checkbox is toggled
        setNewItem(item.name);
        setNewItemError(''); // Clear any previous error on new item input
        return { ...item, status: !item.status }; // Toggle status directly
      }
      return item;
    });
    setFormData(prevState => ({
      ...prevState,
      checklistItems: newChecklistItems
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
    }));
  };

  const addChecklistItem = () => {
<<<<<<< HEAD
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
=======
    if (!newItem.trim()) {
      setNewItemError('Checklist item cannot be empty.');
      return;
    }
    
    const itemsToAdd = newItem.split(',').map(item => item.trim()).filter(item => item !== '');
    const existingItemNames = formData.checklistItems.map(item => item.name.toLowerCase());
    let addedCount = 0;
    let skippedCount = 0;

    const updatedChecklistItems = [...formData.checklistItems];

    itemsToAdd.forEach(itemText => {
      if (existingItemNames.includes(itemText.toLowerCase())) {
        skippedCount++;
      } else {
        updatedChecklistItems.push({ name: itemText, status: false });
        addedCount++;
      }
    });

    if (addedCount > 0) {
        setFormData(prev => ({
            ...prev,
            checklistItems: updatedChecklistItems
        }));
        setNewItem(''); // Clear input only if something was added
        setNewItemError(''); // Clear error on successful add
        newItemInputRef.current.focus(); // Set focus back to the input
    } else if (skippedCount > 0) {
        setNewItemError('All entered items already exist or were empty.');
    } else {
        setNewItemError('No valid items entered.');
    }
  };

  const removeChecklistItem = (index) => {
    setFormData(prev => ({
      ...prev,
      checklistItems: prev.checklistItems.filter((_, i) => i !== index)
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    console.log('Submitting compliance form data:', formData);

    const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);

    if (!token) {
      console.error('Authentication token not found. Please log in.');
      alert('Authentication required to submit compliance checklist.');
      return;
    }

    const apiBaseUrl = API_CONFIG.BASE_URL || 'http://localhost:8888';

    // Transform checklistItems array back into a Map<String, Boolean> for the backend
    const transformedChecklistItems = formData.checklistItems.reduce((acc, item) => {
        acc[item.name] = item.status;
        return acc;
    }, {});

    const payload = {
        productName: formData.productName, // Send productName instead of productId
        checklistItems: transformedChecklistItems,
        reviewerName: formData.reviewerName,
        reviewDate: formData.reviewDate, // YYYY-MM-DD format already from input type="date"
        comments: formData.comments
    };

    try {
      const response = await axios.post(`${apiBaseUrl}/api/compliance-checklist`, payload, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      console.log('Compliance checklist created successfully:', response.data);

      if (onSave) {
        onSave(response.data);
      }
      toast.success('Compliance Checklist submitted successfully!');
      // Optionally clear form or redirect
      setFormData({
        productId: '',
        reviewerName: '',
        reviewDate: new Date().toISOString().split('T')[0],
        comments: '',
        checklistItems: [
          { name: 'Regulatory documentations completes', status: false },
          { name: 'Safety Data Sheet Available', status: false },
          { name: 'Packaging Meets Standards', status: false },
        ],
        productName: '' // Clear productName after submission
      });

    } catch (error) {
      console.error('Error creating compliance checklist:', error);
      if (error.response) {
        console.error('Error response data:', error.response.data);
        console.error('Error response status:', error.response.status);
        console.error('Error response headers:', error.response.headers);
        toast.error(`Failed to create compliance checklist: ${error.response.data.message || error.response.statusText}`);
      } else if (error.request) {
        console.error('Error request:', error.request);
        toast.error('No response received from the server. Please check your internet connection.');
      } else {
        console.error('Error message:', error.message);
        toast.error(`An error occurred: ${error.message}`);
      }
      
    if (onSave) {
          onSave(null, error);
      }
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
    }
  };

  return (
    <div className="compliance-form-container">
<<<<<<< HEAD
=======
      <ToastContainer />
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
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
<<<<<<< HEAD
              name="productId"
              className="form-select"
              value={formData.productId}
=======
              name="productName" // Change name to productName
              className="form-select"
              value={formData.productName} // Bind to productName
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
              onChange={handleInputChange}
              required
            >
              <option value="">Select a product</option>
<<<<<<< HEAD
              <option value="1">Product A</option>
              <option value="2">Product B</option>
              <option value="101">Fungicide X-500</option>
              <option value="102">Organic Fertilizer B-200</option>
              <option value="103">Insecticide Z-100</option>
              <option value="104">Growth Enhancer G-300</option>
            </select>
=======
              {products.map(product => (
                <option key={product.id} value={product.name}>{product.name}</option> // Send product.name as value
              ))}
            </select>
            {isLoadingProducts && <p>Loading products...</p>}
            {productError && <p className="error-message">{productError}</p>}
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
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
<<<<<<< HEAD
              <input
                type="text"
                className="form-input"
                placeholder="Enter a checklist item"
                value={newItem}
                onChange={(e) => setNewItem(e.target.value)}
=======
              {newItemError && <p className="error-text">{newItemError}</p>}
              <input
                type="text"
                ref={newItemInputRef} // Attach the ref here
                className={`form-input ${newItemError ? 'input-error' : ''}`}
                placeholder="Enter a checklist item"
                value={newItem}
                onChange={(e) => {
                  setNewItem(e.target.value);
                  if (newItemError) setNewItemError(''); // Clear error on typing
                }}
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
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
<<<<<<< HEAD
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
=======
              {formData.checklistItems.map((item, index) => (
                <div key={item.name} className="checklist-item">
                  <div className="item-content">
                    <input
                      type="checkbox"
                      id={`item-${item.name}`}
                      checked={item.status}
                      onChange={() => handleChecklistItemChange(index)}
                      className="item-checkbox"
                    />
                    <label htmlFor={`item-${item.name}`} className="item-label">
                      {item.name}
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                    </label>
                  </div>
                  <button
                    type="button"
                    className="remove-item-btn"
<<<<<<< HEAD
                    onClick={() => removeChecklistItem(itemName)}
=======
                    onClick={() => removeChecklistItem(index)}
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                  >
                    <FaTrash />
                  </button>
                </div>
              ))}
<<<<<<< HEAD
              {Object.keys(formData.checklistItems).length === 0 && (
=======
              {formData.checklistItems.length === 0 && (
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
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