<<<<<<< HEAD
import React from 'react';
import './CalendarManagementForm.css';

const CalendarManagementForm = () => {
    // Sample data for the table
    const tableData = [
        { invoice: '1001', customer: 'Mark Otto', ship: 'Japan', price: '$3000', purchasedPrice: '$1200', status: 'progress' },
        { invoice: '1001', customer: 'Mark Otto', ship: 'Japan', price: '$3000', purchasedPrice: '$1200', status: 'open' },
        { invoice: '1001', customer: 'Mark Otto', ship: 'Japan', price: '$3000', purchasedPrice: '$1200', status: 'on-hold' },
        { invoice: '1001', customer: 'Mark Otto', ship: 'Japan', price: '$3000', purchasedPrice: '$1200', status: 'progress' },
        { invoice: '1001', customer: 'Mark Otto', ship: 'Japan', price: '$3000', purchasedPrice: '$1200', status: 'on-hold' },
        { invoice: '1001', customer: 'Mark Otto', ship: 'Japan', price: '$3000', purchasedPrice: '$1200', status: 'open' },
        { invoice: '1001', customer: 'Mark Otto', ship: 'Japan', price: '$3000', purchasedPrice: '$1200', status: 'open' },
        { invoice: '1001', customer: 'Mark Otto', ship: 'Japan', price: '$3000', purchasedPrice: '$1200', status: 'progress' },
    ];

    return (
        <div className="calendar-management-table-container">
            <h2 style={{ textAlign: 'center', marginBottom: '20px', color: '#333' }}>Table #09</h2>
            <table className="calendar-management-table">
                <thead>
                    <tr>
                        <th>INVOICE</th>
                        <th>CUSTOMER</th>
                        <th>SHIP</th>
                        <th>PRICE</th>
                        <th>PURCHASED PRICE</th>
                        <th>STATUS</th>
                    </tr>
                </thead>
                <tbody>
                    {tableData.map((row, index) => (
                        <tr key={index}>
                            <td>{row.invoice}</td>
                            <td>{row.customer}</td>
                            <td>{row.ship}</td>
                            <td>{row.price}</td>
                            <td>{row.purchasedPrice}</td>
                            <td><span className={`status-badge ${row.status}`}>{row.status}</span></td>
                        </tr>
                    ))}
                </tbody>
            </table>
=======
import React, { useState } from 'react';
import './CalendarManagementForm.css';

const CalendarManagementForm = () => {
    const [eventTitle, setEventTitle] = useState('');
    const [description, setDescription] = useState('');
    const [dateTime, setDateTime] = useState('');
    const [eventType, setEventType] = useState('');
    const [assignedParticipants, setAssignedParticipants] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            eventTitle,
            description,
            dateTime,
            eventType,
            assignedParticipants
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="calendar-management-form-container">
            <form className="calendar-management-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Event Title"
                        value={eventTitle}
                        onChange={(e) => setEventTitle(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <textarea
                        placeholder="Description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="datetime-local"
                        placeholder="Date & Time"
                        value={dateTime}
                        onChange={(e) => setDateTime(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <select
                        placeholder="Event Type"
                        value={eventType}
                        onChange={(e) => setEventType(e.target.value)}
                        required
                    >
                        <option value="">Select Event Type</option>
                        <option value="Meeting">Meeting</option>
                        <option value="Task Deadline">Task Deadline</option>
                        <option value="Testing Schedule">Testing Schedule</option>
                    </select>
                </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Assigned Participants"
                        value={assignedParticipants}
                        onChange={(e) => setAssignedParticipants(e.target.value)}
                        required
                    />
                </div>

                <div className="button-row">
                    <button type="submit">Submit</button>
                    <button type="button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
        </div>
    );
};

export default CalendarManagementForm; 