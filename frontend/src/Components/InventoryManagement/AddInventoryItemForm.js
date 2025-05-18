import React, { useState } from 'react';
import './AddInventoryItemForm.css';

const AddInventoryItemForm = () => {
    const [productName, setProductName] = useState('');
    const [batchNumber, setBatchNumber] = useState('');
    const [quantity, setQuantity] = useState('');
    const [supplier, setSupplier] = useState('');
    const [purchaseDate, setPurchaseDate] = useState('');
    const [expiryDate, setExpiryDate] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            productName,
            batchNumber,
            quantity,
            supplier,
            purchaseDate,
            expiryDate
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="add-inventory-item-form-container">
            <form className="add-inventory-item-form" onSubmit={handleSubmit}>
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
                    <input
                        type="text"
                        placeholder="Batch Number"
                        value={batchNumber}
                        onChange={(e) => setBatchNumber(e.target.value)}
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
                        type="text"
                        placeholder="Supplier"
                        value={supplier}
                        onChange={(e) => setSupplier(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="date"
                        placeholder="Purchase Date"
                        value={purchaseDate}
                        onChange={(e) => setPurchaseDate(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="date"
                        placeholder="Expiry Date"
                        value={expiryDate}
                        onChange={(e) => setExpiryDate(e.target.value)}
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

export default AddInventoryItemForm; 