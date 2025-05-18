import React, { useState } from 'react';
import './ProductRegistrationForm.css';

const ProductRegistrationForm = () => {
    const [productName, setProductName] = useState('');
    const [productType, setProductType] = useState('');
    const [manufacturerName, setManufacturerName] = useState('');
    const [batchNumber, setBatchNumber] = useState('');
    const [activeIngredients, setActiveIngredients] = useState('');
    const [dateOfRegistration, setDateOfRegistration] = useState('');
    const [registeredBy, setRegisteredBy] = useState('');
    const [intendedUse, setIntendedUse] = useState('');
    const [cropTarget, setCropTarget] = useState('');
    const [comments, setComments] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            productName,
            productType,
            manufacturerName,
            batchNumber,
            activeIngredients,
            dateOfRegistration,
            registeredBy,
            intendedUse,
            cropTarget,
            comments
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="product-registration-form-container">
            <form className="product-registration-form" onSubmit={handleSubmit}>
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
                        placeholder="Product Type"
                        value={productType}
                        onChange={(e) => setProductType(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Manufacturer Name"
                        value={manufacturerName}
                        onChange={(e) => setManufacturerName(e.target.value)}
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
                        type="text"
                        placeholder="Active Ingredients"
                        value={activeIngredients}
                        onChange={(e) => setActiveIngredients(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                     <input
                        type="date"
                        placeholder="Date of Registration"
                        value={dateOfRegistration}
                        onChange={(e) => setDateOfRegistration(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                     <input
                        type="text"
                        placeholder="Registered By (User ID)"
                        value={registeredBy}
                        onChange={(e) => setRegisteredBy(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                     <textarea
                        placeholder="Intended Use"
                        value={intendedUse}
                        onChange={(e) => setIntendedUse(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                     <input
                        type="text"
                        placeholder="Crop Target (potato, maize, etc.)"
                        value={cropTarget}
                        onChange={(e) => setCropTarget(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                     <textarea
                        placeholder="Comments"
                        value={comments}
                        onChange={(e) => setComments(e.target.value)}
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

export default ProductRegistrationForm; 