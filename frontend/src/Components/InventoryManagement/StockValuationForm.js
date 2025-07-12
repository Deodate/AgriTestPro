import React, { useState } from 'react';
import './StockValuationForm.css';

const StockValuationForm = () => {
    const [product, setProduct] = useState('');
    const [quantity, setQuantity] = useState('');
    const [unitCost, setUnitCost] = useState('');
    const [totalValue, setTotalValue] = useState(''); // This might be calculated
    const [stockDate, setStockDate] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            product,
            quantity,
            unitCost,
            totalValue,
            stockDate
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="stock-valuation-form-container">
            <form className="stock-valuation-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Product"
                        value={product}
                        onChange={(e) => setProduct(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="number"
                        placeholder="Quantity"
                        value={quantity}
                        onChange={(e) => setQuantity(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="number"
                        placeholder="Unit Cost"
                        value={unitCost}
                        onChange={(e) => setUnitCost(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="number"
                        placeholder="Total Value"
                        value={totalValue}
                        onChange={(e) => setTotalValue(e.target.value)} // This might be calculated or read-only later
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="date"
                        placeholder="Stock Date"
                        value={stockDate}
                        onChange={(e) => setStockDate(e.target.value)}
                        required
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

export default StockValuationForm; 