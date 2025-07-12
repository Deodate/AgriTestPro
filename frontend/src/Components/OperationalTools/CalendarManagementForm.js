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
        </div>
    );
};

export default CalendarManagementForm; 