import React, { useState } from 'react';
import './ResourceAllocationForm.css';

const ResourceAllocationForm = () => {
    const [resourceName, setResourceName] = useState('');
    const [resourceType, setResourceType] = useState('');
    const [quantity, setQuantity] = useState('');
    const [allocatedTo, setAllocatedTo] = useState('');
    const [allocationDate, setAllocationDate] = useState('');
    const [returnDate, setReturnDate] = useState('');
    const [notes, setNotes] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            resourceName,
            resourceType,
            quantity,
            allocatedTo,
            allocationDate,
            returnDate,
            notes
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="resource-allocation-form-container">
            <form className="resource-allocation-form" onSubmit={handleSubmit}>
                <h2>Resource Allocation Form</h2>
                <div className="form-row">
                    <label htmlFor="resourceName">Resource Name:</label>
                    <input
                        type="text"
                        id="resourceName"
                        placeholder="Enter resource name"
                        value={resourceName}
                        onChange={(e) => setResourceName(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="resourceType">Resource Type:</label>
                    <input
                        type="text"
                        id="resourceType"
                        placeholder="Enter resource type (e.g., Equipment, Personnel)"
                        value={resourceType}
                        onChange={(e) => setResourceType(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="quantity">Quantity:</label>
                    <input
                        type="number"
                        id="quantity"
                        placeholder="Enter quantity"
                        value={quantity}
                        onChange={(e) => setQuantity(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="allocatedTo">Allocated To:</label>
                    <input
                        type="text"
                        id="allocatedTo"
                        placeholder="Enter recipient of resource"
                        value={allocatedTo}
                        onChange={(e) => setAllocatedTo(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="allocationDate">Allocation Date:</label>
                    <input
                        type="date"
                        id="allocationDate"
                        value={allocationDate}
                        onChange={(e) => setAllocationDate(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="returnDate">Return Date:</label>
                    <input
                        type="date"
                        id="returnDate"
                        value={returnDate}
                        onChange={(e) => setReturnDate(e.target.value)}
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="notes">Notes:</label>
                    <textarea
                        id="notes"
                        placeholder="Add any additional notes about the allocation"
                        value={notes}
                        onChange={(e) => setNotes(e.target.value)}
                        rows="4"
                    ></textarea>
                </div>

                <div className="button-row">
                    <button type="submit" className="submit-button">Submit</button>
                    <button type="button" className="cancel-button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default ResourceAllocationForm; 