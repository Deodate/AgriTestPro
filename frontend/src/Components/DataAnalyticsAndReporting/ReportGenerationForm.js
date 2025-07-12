import React, { useState } from 'react';
import './ReportGenerationForm.css';

const ReportGenerationForm = () => {
    const [formData, setFormData] = useState({
        reportType: '',
        dateRangeStart: '',
        dateRangeEnd: '',
        filters: '',
        reportFormat: '',
        emailReport: ''
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
        console.log('Report Generation form data submitted:', formData);
        // TODO: Implement form submission logic
    };

    return (
        <div className="report-generation-form-container">
            <form className="report-generation-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <select id="reportType" name="reportType" value={formData.reportType} onChange={handleChange} required>
                        <option value="">Select Report Type</option>
                        <option value="Inventory">Inventory</option>
                        <option value="Testing Results">Testing Results</option>
                        <option value="Compliance">Compliance</option>
                    </select>
                </div>
                <div className="form-group">
                    <input type="date" id="dateRangeStart" name="dateRangeStart" value={formData.dateRangeStart} onChange={handleChange} required placeholder="Date Range Start" />
                </div>
                 <div className="form-group">
                    <input type="date" id="dateRangeEnd" name="dateRangeEnd" value={formData.dateRangeEnd} onChange={handleChange} required placeholder="Date Range End" />
                </div>
                <div className="form-group">
                    <input type="text" id="filters" name="filters" value={formData.filters} onChange={handleChange} placeholder="Filters (Product Type, Location, Status)" />
                </div>
                <div className="form-group">
                    <select id="reportFormat" name="reportFormat" value={formData.reportFormat} onChange={handleChange} required>
                        <option value="">Select Format</option>
                        <option value="PDF">PDF</option>
                        <option value="Excel">Excel</option>
                        <option value="CSV">CSV</option>
                    </select>
                </div>
                <div className="form-group">
                    <select id="emailReport" name="emailReport" value={formData.emailReport} onChange={handleChange} required>
                        <option value="">Email Report (Yes/No)</option>
                        <option value="Yes">Yes</option>
                        <option value="No">No</option>
                    </select>
                </div>
                <div className="form-buttons">
                    <button type="submit">Submit</button>
                    <button type="button" className="cancel-button">Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default ReportGenerationForm; 