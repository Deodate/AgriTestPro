import React, { useState, useEffect } from 'react';
import './RealTimeStockTrackingForm.css';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const RealTimeStockTrackingForm = () => {
  const [formData, setFormData] = useState({
    productName: '',
    batchNumber: '',
    transactionType: '',
    quantity: '',
    unitOfMeasure: '',
    date: '',
    sourceOrDestination: '',
    filledBy: '',
    comment: '',
  });

  const [stockEntries, setStockEntries] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  const API_BASE_URL = 'http://localhost:8089/api/real-time-stock-tracking'; // Backend API URL

  useEffect(() => {
    fetchStockEntries();
  }, []);

  const fetchStockEntries = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await axios.get(API_BASE_URL);
      setStockEntries(response.data);
    } catch (err) {
      console.error('Error fetching stock entries:', err);
      setError('Failed to fetch stock entries.');
      toast.error('Failed to load stock entries.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(API_BASE_URL, formData);
      toast.success('Stock entry created successfully!');
      console.log('Stock entry created:', response.data);
      setFormData({
        productName: '',
        batchNumber: '',
        transactionType: '',
        quantity: '',
        unitOfMeasure: '',
        date: '',
        sourceOrDestination: '',
        filledBy: '',
        comment: '',
      });
      fetchStockEntries(); // Refresh the list after submission
    } catch (err) {
      console.error('Error creating stock entry:', err);
      toast.error('Failed to create stock entry.');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this stock entry?')) {
      try {
        await axios.delete(`${API_BASE_URL}/${id}`);
        toast.success('Stock entry deleted successfully!');
        fetchStockEntries(); // Refresh the list after deletion
      } catch (err) {
        console.error('Error deleting stock entry:', err);
        toast.error('Failed to delete stock entry.');
      }
    }
  };

  const handleCancel = () => {
    console.log('Form cancelled');
    setFormData({
      productName: '',
      batchNumber: '',
      transactionType: '',
      quantity: '',
      unitOfMeasure: '',
      date: '',
      sourceOrDestination: '',
      filledBy: '',
      comment: '',
    });
  };

  return (
    <div className="realtimestocktracking-form-container">
      <form className="realtimestocktracking-form" onSubmit={handleSubmit}>
        <h2>Real-Time Stock Tracking</h2>
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
          <label htmlFor="transactionType">Transaction Type:</label>
          <select
            id="transactionType"
            name="transactionType"
            value={formData.transactionType}
            onChange={handleChange}
            required
          >
            <option value="">Select Type</option>
            <option value="Add">Add</option>
            <option value="Remove">Remove</option>
          </select>
        </div>

        <div className="form-row">
          <label htmlFor="quantity">Quantity:</label>
          <input
            type="number"
            id="quantity"
            name="quantity"
            value={formData.quantity}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="unitOfMeasure">Unit of Measure:</label>
          <input
            type="text"
            id="unitOfMeasure"
            name="unitOfMeasure"
            value={formData.unitOfMeasure}
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
          <label htmlFor="sourceOrDestination">Source or Destination:</label>
          <input
            type="text"
            id="sourceOrDestination"
            name="sourceOrDestination"
            value={formData.sourceOrDestination}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="filledBy">Filled By:</label>
          <input
            type="text"
            id="filledBy"
            name="filledBy"
            value={formData.filledBy}
            onChange={handleChange}
            required
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

      <div className="stock-entries-list-container">
        <h2>Existing Stock Entries</h2>
        {isLoading ? (
          <p>Loading entries...</p>
        ) : error ? (
          <p className="error-message">{error}</p>
        ) : stockEntries.length === 0 ? (
          <p>No stock entries found.</p>
        ) : (
          <table className="stock-entries-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Product Name</th>
                <th>Batch Number</th>
                <th>Type</th>
                <th>Quantity</th>
                <th>Unit</th>
                <th>Date</th>
                <th>Source/Dest</th>
                <th>Filled By</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {stockEntries.map((entry) => (
                <tr key={entry.id}>
                  <td>{entry.id}</td>
                  <td>{entry.productName}</td>
                  <td>{entry.batchNumber}</td>
                  <td>{entry.transactionType}</td>
                  <td>{entry.quantity}</td>
                  <td>{entry.unitOfMeasure}</td>
                  <td>{entry.date}</td>
                  <td>{entry.sourceOrDestination}</td>
                  <td>{entry.filledBy}</td>
                  <td>
                    <button onClick={() => handleDelete(entry.id)} className="delete-button">Delete</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default RealTimeStockTrackingForm; 