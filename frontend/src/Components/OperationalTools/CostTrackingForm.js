import React, { useState } from 'react';
import './CostTrackingForm.css';

const CostTrackingForm = () => {
<<<<<<< HEAD
    const [expenseId, setExpenseId] = useState('');
=======
    const [expenseName, setExpenseName] = useState('');
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
    const [date, setDate] = useState('');
    const [expenseType, setExpenseType] = useState('');
    const [amount, setAmount] = useState('');
    const [paidBy, setPaidBy] = useState('');
    const [receiptUpload, setReceiptUpload] = useState(null);

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
<<<<<<< HEAD
            expenseId,
=======
            expenseName,
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
            date,
            expenseType,
            amount,
            paidBy,
            receiptUpload
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    const handleFileChange = (e) => {
        setReceiptUpload(e.target.files[0]);
    };

    return (
        <div className="cost-tracking-form-container">
            <form className="cost-tracking-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
<<<<<<< HEAD
                        placeholder="Expense ID"
                        value={expenseId}
                        onChange={(e) => setExpenseId(e.target.value)}
=======
                        placeholder="Expense Name"
                        value={expenseName}
                        onChange={(e) => setExpenseName(e.target.value)}
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
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
                    <select
                        placeholder="Expense Type"
                        value={expenseType}
                        onChange={(e) => setExpenseType(e.target.value)}
                        required
                    >
                        <option value="">Select Expense Type</option>
                        <option value="Labor">Labor</option>
                        <option value="Materials">Materials</option>
                        <option value="Testing Fees">Testing Fees</option>
                    </select>
                </div>
                 <div className="form-row">
                     <input
                        type="number"
                        placeholder="Amount"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Paid By"
                        value={paidBy}
                        onChange={(e) => setPaidBy(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="file"
                        onChange={handleFileChange}
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

export default CostTrackingForm; 