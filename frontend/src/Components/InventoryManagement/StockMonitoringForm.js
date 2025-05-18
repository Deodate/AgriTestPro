import React, { useState } from 'react';
import './StockMonitoringForm.css';

const StockMonitoringForm = () => {
    const [formData, setFormData] = useState({
        productId: '',
        currentStockLevel: '',
        expiryDate: '',
        stockAlerts: '',
        lastUpdatedDate: '',
        responsibleOfficer: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Stock Monitoring form data submitted:', formData);
        // TODO: Implement form submission logic
    };

    return (
        <div className="stock-monitoring-form-container">
            <form className="stock-monitoring-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="productId">Product ID:</label>
                    <input type="text" id="productId" name="productId" value={formData.productId} onChange={handleChange} required placeholder="Product ID" />
                </div>
                <div className="form-group">
                    <label htmlFor="currentStockLevel">Current Stock Level:</label>
                    <input type="number" id="currentStockLevel" name="currentStockLevel" value={formData.currentStockLevel} onChange={handleChange} required placeholder="Current Stock Level" />
                </div>
                 <div className="form-group">
                    <label htmlFor="expiryDate">Expiry Date:</label>
                    <input type="date" id="expiryDate" name="expiryDate" value={formData.expiryDate} onChange={handleChange} required placeholder="Expiry Date" />
                </div>
                <div className="form-group">
                    <label htmlFor="stockAlerts">Stock Alerts:</label>
                    <select id="stockAlerts" name="stockAlerts" value={formData.stockAlerts} onChange={handleChange} required>
                        <option value="">Stock Alerts (Yes/No)</option>
                        <option value="Yes">Yes</option>
                        <option value="No">No</option>
                    </select>
                </div>
                <div className="form-group">
                    <label htmlFor="lastUpdatedDate">Last Updated Date:</label>
                    <input type="date" id="lastUpdatedDate" name="lastUpdatedDate" value={formData.lastUpdatedDate} onChange={handleChange} required placeholder="Last Updated Date" />
                </div>
                <div className="form-group">
                    <label htmlFor="responsibleOfficer">Responsible Officer:</label>
                    <input type="text" id="responsibleOfficer" name="responsibleOfficer" value={formData.responsibleOfficer} onChange={handleChange} required placeholder="Responsible Officer" />
                </div>
                <div className="form-buttons">
                    <button type="submit">Submit</button>
                    <button type="button" className="cancel-button">Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default StockMonitoringForm; 