import React, { useState, useEffect, useCallback } from 'react';
import { FaArrowLeft, FaCalendarAlt, FaSave, FaMapMarkerAlt } from 'react-icons/fa';
import './TestCaseForm.css';
import { API_CONFIG, AUTH_SETTINGS } from '../../config';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

// Custom notification function to ensure notifications are displayed properly
const showNotification = (type, message) => {
  // Force a small delay to ensure the toast container is mounted
  setTimeout(() => {
    if (type === 'success') {
      toast.success(message, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        theme: "colored",
      });
    } else if (type === 'error') {
      toast.error(message, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        theme: "colored",
      });
    }
  }, 100);
};

const TestCaseForm = ({ onBack, onSave, initialData }) => {
  // Get the API URL from config or use default
  const apiBaseUrl = API_CONFIG.BASE_URL || 'http://localhost:8089';
  console.log('Using API base URL:', apiBaseUrl);

  const [formData, setFormData] = useState({
    testName: '',
    testDescription: '',
    testObjectives: '',
    productType: '',
    testingLocation: '',
    assignedWorkerId: '',
    startDate: new Date().toISOString().split('T')[0],
    endDate: '',
    notes: '',
    receiveUpdates: false
  });

  // For available assigned workers and product types
  const [availableWorkers, setAvailableWorkers] = useState([]);
  const [productTypes, setProductTypes] = useState([]);
  const [availableProductBatchNumbers, setAvailableProductBatchNumbers] = useState([]);
  const [fetchingProductBatchNumbers, setFetchingProductBatchNumbers] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  // Effect to log state changes for debugging batch number population
  useEffect(() => {
    console.log('availableProductBatchNumbers state updated:', availableProductBatchNumbers);
    console.log('formData.productBatchNumber state updated:', formData.productBatchNumber);
  }, [availableProductBatchNumbers, formData.productBatchNumber]);

  // Effect to log formData state changes, particularly productBatchNumber
  useEffect(() => {
    console.log('formData state updated, productBatchNumber:', formData.productBatchNumber);
  }, [formData.productBatchNumber]);

  // Function to reset the form fields
  const resetForm = () => {
    setFormData({
      testName: '',
      testDescription: '',
      testObjectives: '',
      productType: '',
      testingLocation: '',
      assignedWorkerId: '',
      startDate: new Date().toISOString().split('T')[0],
      endDate: '',
      notes: '',
      receiveUpdates: false
    });
    // Assuming validation error states exist, clear them here if needed
    // setTestNameError(''); // Example
  };

  // Fetch distinct product types
  useEffect(() => {
    const fetchProductTypes = async () => {
      const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
      if (!token) {
        console.error('No authentication token found. Cannot fetch product types.');
        toast.error('Authentication required to fetch product types.');
        return;
      }

      try {
      const api = axios.create({
        baseURL: apiBaseUrl,
        headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        // Assuming there's an endpoint to get all products or product types
        const response = await api.get('/api/products');
        if (response.data && Array.isArray(response.data)) {
          // Extract unique product types
          const types = [...new Set(response.data.map(product => product.productType).filter(type => type))];
          setProductTypes(types);
        }
      } catch (err) {
        console.error('Error fetching product types:', err);
        toast.error('Failed to load product types.');
      } finally {
         // Fetch products for the first type or a default type if needed on load
         // For now, we'll rely on the user selecting a type to trigger fetching batch numbers
      }
    };

    fetchProductTypes();
  }, [apiBaseUrl]);

  // Fetch products by type and populate batch numbers
  const fetchProductsByType = useCallback(async (type) => {
    console.log('Fetching products for type:', type);
    setFetchingProductBatchNumbers(true);
    setError(null);
    const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
    if (!token) {
      console.error('No authentication token found. Cannot fetch products by type.');
      toast.error('Authentication required to fetch products.');
      setFetchingProductBatchNumbers(false);
      return;
    }

    try {
      const api = axios.create({
        baseURL: apiBaseUrl,
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      // Use the new backend endpoint to fetch products by type
      const url = `/api/products/type?productType=${encodeURIComponent(type)}`;
      console.log('Fetching products with URL:', url); // Log the URL being used
      const response = await api.get(url);

      console.log('Backend response for products by type:', response); // Log the full response object

      if (response.data && Array.isArray(response.data)) {
        // Extract batch numbers from the products
        const batchNumbers = response.data.map(product => product.batchNumber).filter(batchNum => batchNum);
        console.log('Extracted batch numbers:', batchNumbers);
        setAvailableProductBatchNumbers(batchNumbers);
        console.log('Updated availableProductBatchNumbers state.');
      } else {
        console.log('No data or unexpected data format for products by type:', response.data);
        setAvailableProductBatchNumbers([]); // No products found for this type
      }
    } catch (err) {
      console.error('Error fetching products by type:', err);
      setError('Failed to load products for selected type.');
      toast.error('Failed to load products for selected type.');
      setAvailableProductBatchNumbers([]);
    } finally {
      setFetchingProductBatchNumbers(false);
    }
  }, [apiBaseUrl]);

  // Fetch workers from API
  useEffect(() => {
    const fetchWorkers = async () => {
      setIsLoading(true);
      setError(null);
      
      // Create axios instance with auth headers
      const api = axios.create({
        baseURL: apiBaseUrl,
        headers: {
          'Authorization': `Bearer ${localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY)}`
        }
      });
      
      try {
        // First try to get users with ROLE_FIELD_WORKER
        const response = await api.get('/api/users/role/ROLE_FIELD_WORKER');
        
        // Check if we got field workers
        if (response.data && response.data.length > 0) {
          const workers = response.data.map(user => ({
            id: user.id,
            name: user.fullName || user.username
          }));
          setAvailableWorkers(workers);
          return;
        }
        
        // If no field workers found, try agronomists as they can also do field work
        console.log('No field workers found, fetching agronomists instead');
        const agronomistResponse = await api.get('/api/users/role/ROLE_AGRONOMIST');
        
        if (agronomistResponse.data && agronomistResponse.length > 0) {
          const workers = agronomistResponse.data.map(user => ({
            id: user.id,
            name: user.fullName || user.username
          }));
          setAvailableWorkers(workers);
          return;
        }
        
        // If still no workers found, throw an error
        throw new Error('No field workers or agronomists found in the system');
      } catch (err) {
        console.error('Error fetching workers:', err);
        
        // If there's an authentication error, provide a specific message
        if (err.response && err.response.status === 401) {
          setError('Authentication error: Please log in again to access worker data');
        } else if (err.response && err.response.status === 403) {
          setError('Permission denied: You do not have access to view worker data');
        } else {
          setError('Failed to load workers: ' + (err.message || 'Unknown error'));
        }
        
        // Fallback to sample data
        setAvailableWorkers([
          { id: 2, name: 'John Smith (Field Worker)' },
          { id: 15, name: 'Jane Doe (Field Worker)' },
          { id: 24, name: 'Michael Johnson (Field Worker)' },
          { id: 32, name: 'Alice Johnson (Field Worker)' },
          { id: 41, name: 'Robert Brown (Field Worker)' }
        ]);
      } finally {
        setIsLoading(false);
      }
    };

    fetchWorkers();
  }, []);

  // Initialize with provided data if any
  useEffect(() => {
    const fetchTestCase = async (id) => {
      setIsLoading(true);
      setError(null);
      const api = axios.create({
        baseURL: apiBaseUrl,
        headers: {
          'Authorization': `Bearer ${localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY)}`
        }
      });
      try {
        const response = await api.get(`/api/testcases/${id}`);
        if (response.data) {
          setFormData(response.data);
        } else {
          setError('Test case not found.');
          showNotification('error', 'Test case not found for editing.');
        }
      } catch (err) {
        console.error('Error fetching test case for editing:', err);
         if (err.response && err.response.status === 404) {
           setError('Test case not found.');
           showNotification('error', 'Test case not found for editing.');
         } else if (err.response && err.response.status === 401) {
           setError('Authentication error: Please log in again to access test case data.');
           showNotification('error', 'Authentication required to fetch test case for editing.');
         } else if (err.response && err.response.status === 403) {
           setError('Permission denied: You do not have access to view this test case.');
           showNotification('error', 'Permission denied to fetch test case for editing.');
         } else {
           setError('Failed to load test case: ' + (err.message || 'Unknown error'));
           showNotification('error', 'Failed to load test case for editing.');
         }
      } finally {
        setIsLoading(false);
      }
    };

    if (initialData) {
      // If initialData has an ID, fetch full details from the backend
      if (initialData.id) {
         fetchTestCase(initialData.id);
      } else {
         // Otherwise, just pre-fill with provided data (e.g., for initial creation form with defaults)
      setFormData(initialData);
      }
    } else {
      resetForm(); // Reset form if no initialData
    }
  }, [initialData, apiBaseUrl]);

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
    
    // If the product type changes, fetch available product batch numbers
    if (name === 'productType' && value) {
      fetchProductsByType(value);
      // Reset the selected batch number when type changes
      setFormData(prev => ({ ...prev, productBatchNumber: '' }));
      setAvailableProductBatchNumbers([]); // Clear previous batch numbers
    } else if (name === 'productType' && !value) {
        // If product type is cleared, clear available batch numbers and the selected one
        setAvailableProductBatchNumbers([]);
        setFormData(prev => ({ ...prev, productBatchNumber: '' }));
    }
  };

  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Ensure convert assignedWorkerId to a number before sending to API
    const formDataToSubmit = {
      ...formData,
      assignedWorkerId: formData.assignedWorkerId ? Number(formData.assignedWorkerId) : null
    };
    
    setIsSubmitting(true);
    
    try {
      const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
      if (!token) {
        console.error('No authentication token found.');
        setIsSubmitting(false);
        showNotification('error', 'Authentication required to submit form.');
        return;
      }
      
      const api = axios.create({
        baseURL: apiBaseUrl,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });
      
      let url = '';
      let method = '';
      let successMessage = '';
      
      if (initialData && initialData.id) {
        // Update existing test case
        url = `/api/testcases/${initialData.id}`;
        method = 'PUT';
        successMessage = 'Test case updated successfully!';
        // For update, include the ID in the body as well if the backend expects it
        formDataToSubmit.id = initialData.id;
      } else {
        // Create new test case
        url = '/api/testcases';
        method = 'POST';
        successMessage = 'Test case created successfully!';
      }
      
      console.log(`${method}ing test case at ${url} with data:`, formDataToSubmit);
      
      const response = await api({ url, method, data: formDataToSubmit });
      
      console.log('API response:', response.data);
      
      if (response.status === 200 || response.status === 201) {
        showNotification('success', `Test Case ${formData.id ? 'updated' : 'registered'} successfully!`);
        // Call the onSave prop passed from the parent component (Dashboard.js)
        if (onSave) {
          onSave(response.data); // Pass the saved test case data back to the parent
        }
        resetForm(); // Reset the form after successful save
          } else {
        console.error(`${initialData ? 'Error updating test case' : 'Error creating test case'}:`, response.data);
        
        let errorMessage = `Failed to ${initialData ? 'update' : 'create'} test case.`;
        if (response.data?.message) {
            errorMessage = `Error: ${response.data.message}`;
        }
        
        showNotification('error', errorMessage);
      }
      
    } catch (error) {
      console.error(`${initialData ? 'Error updating test case' : 'Error creating test case'}:`, error);
      
      let errorMessage = `Failed to ${initialData ? 'update' : 'create'} test case.`;
      if (error.response?.data?.message) {
          errorMessage = `Error: ${error.response.data.message}`;
      } else if (error.message) {
          errorMessage = `Error: ${error.message}`;
      }
      
      showNotification('error', errorMessage);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="test-case-form-container">
      {/* Toast notifications container - positioned at the top of the component */}
      <ToastContainer 
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop
        closeOnClick
        pauseOnHover
        draggable
        theme="colored"
      />
      <div className="test-case-form-header">
        <div>
          <h1>Creating Test Case</h1>
          <p className="test-case-form-subheader">Please create a test case by completing this form.</p>
        </div>
        <button className="back-button" onClick={onBack}>
          Back to List
        </button>
      </div>

      <div className="test-case-form-content">
        <form onSubmit={handleSubmit} className="form-container">
          <div className="field-group">
            <label htmlFor="testName">
              Test Name<span className="required">*</span>
            </label>
            <input
              type="text"
              id="testName"
              name="testName"
              className="form-input"
              value={formData.testName}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="field-group">
            <label htmlFor="testDescription">
              Test Description<span className="required">*</span>
            </label>
            <input
              type="text"
              id="testDescription"
              name="testDescription"
              className="form-input"
              value={formData.testDescription}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="field-group">
            <label htmlFor="testObjectives">
              Test Objectives<span className="required">*</span>
            </label>
            <input
              type="text"
              id="testObjectives"
              name="testObjectives"
              className="form-input"
              value={formData.testObjectives}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="field-group">
            <label htmlFor="productType">
              Product Type<span className="required">*</span>
            </label>
            <select
              id="productType"
              name="productType"
              className="form-select"
              value={formData.productType}
              onChange={handleInputChange}
              required
            >
              <option value="">Please Select</option>
              {productTypes.map(type => (
                <option key={type} value={type}>{type}</option>
              ))}
            </select>
          </div>

          <div className="field-group">
            <label htmlFor="productBatchNumber">
              Product Batch Number<span className="required">*</span>
            </label>
            <select
              id="productBatchNumber"
              name="productBatchNumber"
              className="form-select"
              value={formData.productBatchNumber}
              onChange={handleInputChange}
              required
              disabled={!formData.productType || fetchingProductBatchNumbers}
            >
              <option value="">
                {fetchingProductBatchNumbers ? "Loading batch numbers..." : "Please Select Product Batch Number"}
              </option>
              {availableProductBatchNumbers.map(batchNumber => (
                <option key={batchNumber} value={batchNumber}>{batchNumber}</option>
              ))}
            </select>
            {!formData.productType && <div className="info-message">Please select a Product Type first.</div>}
            {formData.productType && fetchingProductBatchNumbers && <div className="info-message">Loading available batch numbers...</div>}
            {formData.productType && !fetchingProductBatchNumbers && availableProductBatchNumbers.length === 0 && <div className="info-message">No products found for this type.</div>}
          </div>

          <div className="field-group">
            <label htmlFor="testingLocation">
              Testing Location<span className="required">*</span>
            </label>
            <select
              id="testingLocation"
              name="testingLocation"
              className="form-select"
              value={formData.testingLocation}
              onChange={handleInputChange}
              required
            >
              <option value="">Please Select Testing Location</option>
              {[...Array(11).keys()].map(i => {
                const fieldNumber = i + 1;
                const fieldValue = `Field ${fieldNumber}`;
                return (
                  <option key={fieldValue} value={fieldValue}>{fieldValue}</option>
                );
              })}
            </select>
          </div>

          <div className="field-group">
            <label htmlFor="assignedWorkerId">
              Assigned Worker<span className="required">*</span>
            </label>
            <select
              id="assignedWorkerId"
              name="assignedWorkerId"
              className="form-select"
              value={formData.assignedWorkerId}
              onChange={handleInputChange}
              required
              disabled={isLoading}
            >
              <option value="">
                {isLoading ? "Loading workers..." : "Please Select"}
              </option>
              {availableWorkers.map(worker => (
                <option key={worker.id} value={worker.id}>{worker.name}</option>
              ))}
            </select>
            {error && <div className="error-message">{error}</div>}
          </div>

          <div className="field-group">
            <label htmlFor="startDate">
              Start Date<span className="required">*</span>
            </label>
            <div className="date-input-container">
              <input
                type="date"
                id="startDate"
                name="startDate"
                className="form-input date-input"
                value={formData.startDate}
                onChange={handleInputChange}
                required
              />
              <FaCalendarAlt className="calendar-icon" />
            </div>
          </div>

          <div className="field-group">
            <label htmlFor="endDate">
              End Date<span className="required">*</span>
            </label>
            <div className="date-input-container">
              <input
                type="date"
                id="endDate"
                name="endDate"
                className="form-input date-input"
                value={formData.endDate}
                onChange={handleInputChange}
                required
              />
              <FaCalendarAlt className="calendar-icon" />
            </div>
          </div>

          <div className="field-group full-width">
            <label htmlFor="notes">
              Notes
            </label>
            <textarea
              id="notes"
              name="notes"
              className="form-textarea"
              value={formData.notes}
              onChange={handleInputChange}
              rows="4"
            />
          </div>


          <div className="form-actions">
            <button 
              type="submit" 
              className="save-btn"
              disabled={isSubmitting}
            >
              {isSubmitting ? (initialData ? 'Updating...' : 'Submitting...') : (initialData ? 'Update' : 'Submit')}
            </button>
            <button 
              type="button" 
              className="cancel-btn"
              onClick={onBack}
              disabled={isSubmitting}
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default TestCaseForm; 