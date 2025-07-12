import React, { useState } from 'react';
import './StockMonitoringForm.css';

const StockMonitoringForm = () => {
<<<<<<< HEAD
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

=======
    const [productName, setProductName] = useState('');
    const [stockLevel, setStockLevel] = useState('');
    const [expiryDate, setExpiryDate] = useState('');
    const [alerts, setAlerts] = useState('');
    const [lastUpdated, setLastUpdated] = useState('');
    const [responsible, setResponsible] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log({
            productName,
            stockLevel,
            expiryDate,
            alerts,
            lastUpdated,
            responsible
        });
        // TODO: Implement form submission logic
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
    return (
        <div className="stock-monitoring-form-container">
            <form className="stock-monitoring-form" onSubmit={handleSubmit}>
                <div className="form-group">
<<<<<<< HEAD
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
=======
                    <label htmlFor="productName">Product Name:</label>
                    <input type="text" id="productName" name="productName" value={productName} onChange={(e) => setProductName(e.target.value)} required placeholder="Product Name" />
                </div>
                <div className="form-group">
                    <label htmlFor="stockLevel">Stock Level:</label>
                    <input type="text" id="stockLevel" name="stockLevel" value={stockLevel} onChange={(e) => setStockLevel(e.target.value)} required placeholder="Stock Level" />
                </div>
                 <div className="form-group">
                    <label htmlFor="expiryDate">Expiry Date:</label>
                    <input type="date" id="expiryDate" name="expiryDate" value={expiryDate} onChange={(e) => setExpiryDate(e.target.value)} required placeholder="Expiry Date" />
                </div>
                <div className="form-group">
                    <label htmlFor="alerts">Alerts:</label>
                    <input type="text" id="alerts" name="alerts" value={alerts} onChange={(e) => setAlerts(e.target.value)} required placeholder="Alerts" />
                </div>
                <div className="form-group">
                    <label htmlFor="lastUpdated">Last Updated Date:</label>
                    <input type="date" id="lastUpdated" name="lastUpdated" value={lastUpdated} onChange={(e) => setLastUpdated(e.target.value)} required placeholder="Last Updated Date" />
                </div>
                <div className="form-group">
                    <label htmlFor="responsible">Responsible Officer:</label>
                    <input type="text" id="responsible" name="responsible" value={responsible} onChange={(e) => setResponsible(e.target.value)} required placeholder="Responsible Officer" />
                </div>
                <div className="form-buttons">
                    <button type="submit">Submit</button>
                    <button type="button" className="cancel-button" onClick={handleCancel}>Cancel</button>
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                </div>
            </form>
        </div>
    );
};

export default StockMonitoringForm; 