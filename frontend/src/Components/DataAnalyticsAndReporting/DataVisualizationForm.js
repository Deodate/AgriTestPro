import React, { useState } from 'react';
import './DataVisualizationForm.css';

const DataVisualizationForm = () => {
    const [reportId, setReportId] = useState('');
    const [visualizationType, setVisualizationType] = useState('');
    const [dataSource, setDataSource] = useState('');
    const [parametersFilters, setParametersFilters] = useState('');
    const [dateRange, setDateRange] = useState(''); // Consider date range picker later
    const [notes, setNotes] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            reportId,
            visualizationType,
            dataSource,
            parametersFilters,
            dateRange,
            notes
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="data-visualization-form-container">
            <form className="data-visualization-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Report ID (FK)"
                        value={reportId}
                        onChange={(e) => setReportId(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <select
                        placeholder="Visualization Type"
                        value={visualizationType}
                        onChange={(e) => setVisualizationType(e.target.value)}
                        required
                    >
                        <option value="">Select Visualization Type</option>
                        <option value="Chart">Chart</option>
                        <option value="Graph">Graph</option>
                        <option value="Table">Table</option>
                    </select>
                </div>
                <div className="form-row">
                     <input
                        type="text"
                        placeholder="Data Source"
                        value={dataSource}
                        onChange={(e) => setDataSource(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <textarea
                        placeholder="Parameters/Filters"
                        value={parametersFilters}
                        onChange={(e) => setParametersFilters(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Date Range (e.g., 2023-01-01 to 2023-12-31)"
                        value={dateRange}
                        onChange={(e) => setDateRange(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                    <textarea
                        placeholder="Notes"
                        value={notes}
                        onChange={(e) => setNotes(e.target.value)}
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

export default DataVisualizationForm; 