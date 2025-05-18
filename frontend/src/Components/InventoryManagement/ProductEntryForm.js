import React, { useState } from 'react';
import './ProductEntryForm.css';

const ProductEntryForm = () => {
    const [formData, setFormData] = useState({
        productName: '',
        productCategory: '',
        manufacturer: '',
        expiryDate: '',
        quantityReceived: '',
        storageLocation: '',
        supplierName: '',
        purchaseOrderNumber: '',
        dateOfEntry: '',
        enteredBy: ''
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
        console.log('Product Entry form data submitted:', formData);
        // TODO: Implement form submission logic
    };

    return (
        <div className="product-entry-form-container">
            <form className="product-entry-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <input type="text" id="productName" name="productName" value={formData.productName} onChange={handleChange} required placeholder="Enter Product Name" />
                </div>
                <div className="form-group">
                    <input type="text" id="productCategory" name="productCategory" value={formData.productCategory} onChange={handleChange} required placeholder="Enter Product Category" />
                </div>
                <div className="form-group">
                    <input type="text" id="manufacturer" name="manufacturer" value={formData.manufacturer} onChange={handleChange} required placeholder="Enter Manufacturer" />
                </div>
                <div className="form-group">
                    <input type="date" id="expiryDate" name="expiryDate" value={formData.expiryDate} onChange={handleChange} required placeholder="Select Expiry Date" />
                </div>
                 <div className="form-group">
                    <input type="number" id="quantityReceived" name="quantityReceived" value={formData.quantityReceived} onChange={handleChange} required placeholder="Enter Quantity" />
                </div>
                <div className="form-group">
                    <input type="text" id="storageLocation" name="storageLocation" value={formData.storageLocation} onChange={handleChange} required placeholder="Enter Storage Location" />
                </div>
                <div className="form-group">
                    <input type="text" id="supplierName" name="supplierName" value={formData.supplierName} onChange={handleChange} required placeholder="Enter Supplier Name" />
                </div>
                <div className="form-group">
                    <input type="text" id="purchaseOrderNumber" name="purchaseOrderNumber" value={formData.purchaseOrderNumber} onChange={handleChange} required placeholder="Enter PO Number" />
                </div>
                <div className="form-group">
                    <input type="date" id="dateOfEntry" name="dateOfEntry" value={formData.dateOfEntry} onChange={handleChange} required placeholder="Select Date of Entry" />
                </div>
                <div className="form-group">
                    <input type="text" id="enteredBy" name="enteredBy" value={formData.enteredBy} onChange={handleChange} required placeholder="Enter Your Name" />
                </div>
                <div className="form-buttons">
                    <button type="submit">Submit</button>
                    <button type="button" className="cancel-button">Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default ProductEntryForm; 