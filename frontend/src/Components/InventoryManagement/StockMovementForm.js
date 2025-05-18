import React, { useState } from 'react';
import './StockMovementForm.css';

const StockMovementForm = () => {
    const [productName, setProductName] = useState('');
    const [movementType, setMovementType] = useState('');
    const [quantity, setQuantity] = useState('');
    const [date, setDate] = useState('');
    const [reason, setReason] = useState('');
    const [authorizedBy, setAuthorizedBy] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            productName,
            movementType,
            quantity,
            date,
            reason,
            authorizedBy
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="stock-movement-form-container">
            <form className="stock-movement-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Product Name"
                        value={productName}
                        onChange={(e) => setProductName(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                     <select
                        placeholder="Type of Movement"
                        value={movementType}
                        onChange={(e) => setMovementType(e.target.value)}
                        required
                    >
                        <option value="">Select Type of Movement</option>
                        <option value="In">In</option>
                        <option value="Out">Out</option>
                    </select>
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
                        type="date"
                        placeholder="Date"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Reason for Movement"
                        value={reason}
                        onChange={(e) => setReason(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Authorized By"
                        value={authorizedBy}
                        onChange={(e) => setAuthorizedBy(e.target.value)}
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

export default StockMovementForm; 