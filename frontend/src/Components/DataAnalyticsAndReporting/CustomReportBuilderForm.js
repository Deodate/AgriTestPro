import React, { useState } from 'react';
import './CustomReportBuilderForm.css';

const CustomReportBuilderForm = () => {
    const [reportTitle, setReportTitle] = useState('');
    const [dataSource, setDataSource] = useState(''); // Consider a select dropdown later
    const [fieldsToInclude, setFieldsToInclude] = useState(''); // Consider multi-select later
    const [filters, setFilters] = useState(''); // Consider a more complex input later
    const [format, setFormat] = useState(''); // Consider a select dropdown later
    const [recurrence, setRecurrence] = useState(''); // Consider a select dropdown later

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            reportTitle,
            dataSource,
            fieldsToInclude,
            filters,
            format,
            recurrence
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="custom-report-builder-form-container">
            <form className="custom-report-builder-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Report Title"
                        value={reportTitle}
                        onChange={(e) => setReportTitle(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Data Source (Test Results, Inventory, etc.)"
                        value={dataSource}
                        onChange={(e) => setDataSource(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Fields to Include"
                        value={fieldsToInclude}
                        onChange={(e) => setFieldsToInclude(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Filters"
                        value={filters}
                        onChange={(e) => setFilters(e.target.value)}
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Format (PDF, Excel, etc.)"
                        value={format}
                        onChange={(e) => setFormat(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Recurrence (One-time, Weekly, Monthly)"
                        value={recurrence}
                        onChange={(e) => setRecurrence(e.target.value)}
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

export default CustomReportBuilderForm; 