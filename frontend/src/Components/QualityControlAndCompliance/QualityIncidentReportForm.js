import React, { useState } from 'react';
import './QualityIncidentReportForm.css';

const QualityIncidentReportForm = () => {
    const [formData, setFormData] = useState({
        incidentId: '',
        productId: '',
        dateOfIncident: '',
        descriptionOfIssue: '',
        // photosVideos: null, // File upload will require more complex handling
        correctiveActionsTaken: '',
        status: 'Open' // Default status
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };
    
    // File upload handler would be added here later
    const handleFileChange = (e) => {
        setFormData(prevState => ({
            ...prevState,
            photosVideos: e.target.files[0]
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Quality Incident Report form data submitted:', formData);
        // TODO: Implement form submission logic, including file upload
    };

    return (
        <div className="quality-incident-report-form-container">
            <form className="quality-incident-report-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <input type="text" id="incidentId" name="incidentId" value={formData.incidentId} onChange={handleChange} required placeholder="Incident ID" />
                </div>
                <div className="form-group">
                    <input type="text" id="productId" name="productId" value={formData.productId} onChange={handleChange} required placeholder="Product ID" />
                </div>
                 <div className="form-group">
                    <input type="date" id="dateOfIncident" name="dateOfIncident" value={formData.dateOfIncident} onChange={handleChange} required placeholder="Date of Incident" />
                </div>
                <div className="form-group">
                    <textarea id="descriptionOfIssue" name="descriptionOfIssue" value={formData.descriptionOfIssue} onChange={handleChange} required placeholder="Description of Issue" />
                </div>
                {/* File upload input would be added here later */}
                
                <div className="form-group">
                    
                    <input type="file" id="photosVideos" name="photosVideos" onChange={handleFileChange} />
                </div>
                
                <div className="form-group">
                    <textarea id="correctiveActionsTaken" name="correctiveActionsTaken" value={formData.correctiveActionsTaken} onChange={handleChange} placeholder="Corrective Actions Taken" />
                </div>
                <div className="form-group">
                    <select id="status" name="status" value={formData.status} onChange={handleChange} required>
                        <option value="Open">Open</option>
                        <option value="Resolved">Resolved</option>
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

export default QualityIncidentReportForm; 