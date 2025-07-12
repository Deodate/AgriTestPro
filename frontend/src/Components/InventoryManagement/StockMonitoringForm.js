import React, { useState } from 'react';
import './StockMonitoringForm.css';

const StockMonitoringForm = () => {
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

    return (
        <div className="stock-monitoring-form-container">
            <form className="stock-monitoring-form" onSubmit={handleSubmit}>
                <div className="form-group">
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
                </div>
            </form>
        </div>
    );
};

export default StockMonitoringForm; 