import React, { useState, useEffect } from 'react';
import './ProductManagementForm.css';

const ProductManagementForm = () => {
  const [formData, setFormData] = useState({
    productName: '',
    category: '',
    supplierName: '',
    batchNumber: '',
    manufactureDate: '',
    expiryDate: '',
    initialStock: '',
    description: '',
    registeredBy: '',
    activeSize: '',
  });
  const [registeredByFullName, setRegisteredByFullName] = useState('');

  // Fetch next batch number from backend API
  useEffect(() => {
    const fetchNextBatchNumber = async () => {
      try {
        // Replace with your actual API endpoint
        const response = await fetch('/api/products/next-batch-number');
        if (response.ok) {
          const data = await response.text();
          setFormData(prev => ({ ...prev, batchNumber: data }));
        } else {
          console.error('Failed to fetch next batch number');
        }
      } catch (error) {
        console.error('Error fetching next batch number:', error);
      }
    };
    fetchNextBatchNumber();
  }, []);

  // Fetch user full name whenever registeredBy changes
  useEffect(() => {
    const fetchFullName = async () => {
      const userId = formData.registeredBy;
      if (!userId || isNaN(userId)) {
        setRegisteredByFullName('');
        return;
      }
      try {
        const response = await fetch(`/api/users/${userId}`);
        if (response.ok) {
          const user = await response.json();
          setRegisteredByFullName(user.fullName || '');
        } else {
          setRegisteredByFullName('');
        }
      } catch (error) {
        setRegisteredByFullName('');
      }
    };
    fetchFullName();
  }, [formData.registeredBy]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    let fullName = registeredByFullName;
    // If full name is not set, fetch it before submitting
    if (!fullName && formData.registeredBy && !isNaN(formData.registeredBy)) {
      try {
        const response = await fetch(`/api/users/${formData.registeredBy}`);
        if (response.ok) {
          const user = await response.json();
          fullName = user.fullName || formData.registeredBy;
        } else {
          fullName = formData.registeredBy;
        }
      } catch (error) {
        fullName = formData.registeredBy;
      }
    }
    // Prepare data to send: use full name for registeredBy
    const payload = {
      ...formData,
      registeredBy: fullName || formData.registeredBy,
      activeSize: '',
    };
    // Handle form submission logic here
    console.log('Product Management Form submitted:', payload);
    // You would typically send this data to an API
    // Clear form or show success message
    setFormData({
      productName: '',
      category: '',
      supplierName: '',
      batchNumber: '',
      manufactureDate: '',
      expiryDate: '',
      initialStock: '',
      description: '',
      registeredBy: '',
      activeSize: '',
    });
  };

  const handleCancel = () => {
    // Handle cancel logic here, maybe close the form or clear fields
    console.log('Product Management Form cancelled');
    setFormData({
      productName: '',
      category: '',
      supplierName: '',
      batchNumber: '',
      manufactureDate: '',
      expiryDate: '',
      initialStock: '',
      description: '',
      registeredBy: '',
      activeSize: '',
    });
  };

  return (
    <div className="productmanagement-form-container">
  
      <form className="productmanagement-form" onSubmit={handleSubmit}>
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
          <label htmlFor="category">Category:</label>
          <input
            type="text"
            id="category"
            name="category"
            value={formData.category}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="supplierName">Supplier Name:</label>
          <input
            type="text"
            id="supplierName"
            name="supplierName"
            value={formData.supplierName}
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
            readOnly
          />
        </div>

        <div className="form-row">
          <label htmlFor="manufactureDate">Manufacture Date:</label>
          <input
            type="date"
            id="manufactureDate"
            name="manufactureDate"
            value={formData.manufactureDate}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="expiryDate">Expiry Date:</label>
          <input
            type="date"
            id="expiryDate"
            name="expiryDate"
            value={formData.expiryDate}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="initialStock">Initial Stock:</label>
          <input
            type="number"
            id="initialStock"
            name="initialStock"
            value={formData.initialStock}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="description">Description:</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
          />
        </div>

        <div className="form-row">
          <label htmlFor="activeSize">Active Size:</label>
          <input
            type="text"
            id="activeSize"
            name="activeSize"
            value={formData.activeSize}
            onChange={handleChange}
          />
        </div>

        <div className="form-row">
          <label htmlFor="registeredBy">Registered By (User ID):</label>
          <input
            type="text"
            id="registeredBy"
            name="registeredBy"
            value={formData.registeredBy || ''}
            onChange={handleChange}
          />
          {registeredByFullName && (
            <div style={{ fontSize: '6px', color: 'red', marginTop: '2px', marginLeft: 0 }}>
              {registeredByFullName}
            </div>
          )}
        </div>

        <div className="button-row">
          <button type="submit" className="submit-btn">Submit</button>
          <button type="button" className="cancel-btn" onClick={handleCancel}>Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default ProductManagementForm; 