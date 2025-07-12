import React, { useState, useEffect } from 'react';
import './QualityIncidentReportForm.css';
import axiosInstance from '../../utils/axiosConfig';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const QualityIncidentReportForm = () => {
    const [incidentId, setIncidentId] = useState('');
    const [productId, setProductId] = useState('');
    const [incidentDate, setIncidentDate] = useState('');
    const [description, setDescription] = useState('');
    const [file, setFile] = useState(null);
    const [correctiveActions, setCorrectiveActions] = useState('');
    const [status, setStatus] = useState('OPEN'); // Default status to OPEN
    const [reports, setReports] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    const API_BASE_URL = '/api/quality-incident-reports';

    useEffect(() => {
        fetchReports();
    }, []);

    const fetchReports = async () => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await axiosInstance.get(API_BASE_URL);
            console.log('Quality Incident Reports API response:', response.data);
            setReports(response.data);
            toast.success('Quality incident reports loaded successfully!');
        } catch (err) {
            console.error('Error fetching quality incident reports:', err);
            setError('Failed to fetch quality incident reports.');
            toast.error('Failed to load quality incident reports.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('incidentId', incidentId);
        formData.append('productId', productId);
        formData.append('incidentDate', `${incidentDate}T00:00:00`); // Assuming date input is YYYY-MM-DD
        formData.append('description', description);
        formData.append('correctiveActions', correctiveActions);
        formData.append('status', status);

        if (file) {
            formData.append('mediaFiles', file);
        }

        try {
            const response = await axiosInstance.post(API_BASE_URL, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            toast.success('Quality incident report created successfully!');
            console.log('Report created:', response.data);
            // Clear form fields
            setIncidentId('');
            setProductId('');
            setIncidentDate('');
            setDescription('');
            setFile(null);
            setCorrectiveActions('');
            setStatus('OPEN');
            fetchReports(); // Refresh the list
        } catch (err) {
            console.error('Error creating report:', err);
            toast.error(`Failed to create report: ${err.response?.data || err.message}`);
        }
    };

    const handleCancel = () => {
        setIncidentId('');
        setProductId('');
        setIncidentDate('');
        setDescription('');
        setFile(null);
        setCorrectiveActions('');
        setStatus('OPEN');
        toast.info('Form cleared.');
    };

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    return (
        <div className="quality-incident-report-form-container">
            <form className="quality-incident-report-form" onSubmit={handleSubmit}>
                <h2>Quality Incident Report Form</h2>

                <div className="form-row">
                    <label htmlFor="incidentId">Incident ID (Name):</label>
                    <input
                        type="text"
                        id="incidentId"
                        placeholder="Enter Incident ID or Name"
                        value={incidentId}
                        onChange={(e) => setIncidentId(e.target.value)}
                        required
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="productId">Product ID (Name):</label>
                    <input
                        type="text"
                        id="productId"
                        placeholder="Enter Product ID or Name"
                        value={productId}
                        onChange={(e) => setProductId(e.target.value)}
                        required
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="incidentDate">Date:</label>
                    <input
                        type="date"
                        id="incidentDate"
                        value={incidentDate}
                        onChange={(e) => setIncidentDate(e.target.value)}
                        required
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="description">Description:</label>
                    <textarea
                        id="description"
                        placeholder="Provide a detailed description of the incident"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        rows="4"
                        required
                    ></textarea>
                </div>

                <div className="form-row">
                    <label htmlFor="file">Upload File (PDF):</label>
                    <input
                        type="file"
                        id="file"
                        accept=".pdf"
                        onChange={handleFileChange}
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="correctiveActions">Corrective Actions Taken:</label>
                    <textarea
                        id="correctiveActions"
                        placeholder="Describe the corrective actions taken"
                        value={correctiveActions}
                        onChange={(e) => setCorrectiveActions(e.target.value)}
                        rows="4"
                    ></textarea>
                </div>

                <div className="form-row">
                    <label htmlFor="status">Status:</label>
                    <select
                        id="status"
                        value={status}
                        onChange={(e) => setStatus(e.target.value)}
                        required
                    >
                        <option value="OPEN">Open</option>
                        <option value="IN_PROGRESS">In Progress</option>
                        <option value="RESOLVED">Resolved</option>
                        <option value="CLOSED">Closed</option>
                    </select>
                </div>

                <div className="button-row">
                    <button type="submit" className="submit-button">Submit</button>
                    <button type="button" className="cancel-button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>

            <div className="reports-list-container">
                <h2>Existing Quality Incident Reports</h2>
                {isLoading ? (
                    <p>Loading reports...</p>
                ) : error ? (
                    <p className="error-message">{error}</p>
                ) : reports.length === 0 ? (
                    <p>No quality incident reports found.</p>
                ) : (
                    <table className="reports-table">
                        <thead>
                            <tr>
                                <th>Incident ID</th>
                                <th>Product ID</th>
                                <th>Incident Date</th>
                                <th>Description</th>
                                <th>Corrective Actions</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {reports.map((report) => (
                                <tr key={report.id}>
                                    <td>{report.incidentId}</td>
                                    <td>{report.productId}</td>
                                    <td>{new Date(report.incidentDate).toLocaleDateString()}</td>
                                    <td>{report.description}</td>
                                    <td>{report.correctiveActions}</td>
                                    <td>{report.status}</td>
                                    <td>
                                        <button className="view-button">View</button>
                                        <button className="edit-button">Edit</button>
                                        <button className="delete-button">Delete</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    );
};

export default QualityIncidentReportForm; 