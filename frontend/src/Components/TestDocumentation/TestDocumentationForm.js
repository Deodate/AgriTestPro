<<<<<<< HEAD
import React, { useState } from 'react';
import './TestDocumentationForm.css';

const TestDocumentationForm = () => {
    const [formData, setFormData] = useState({
        testCaseId: '',
        productId: '',
        trialPhase: '',
        testMetrics: '',
        finalVerdict: '',
        recommendations: '',
        approvedBy: '',
        dateOfApproval: ''
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
        console.log('Form data submitted:', formData);
        // TODO: Implement form submission logic
    };

    return (
        <div className="test-documentation-form-container">
            <form className="test-documentation-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <input type="text" id="testCaseId" name="testCaseId" value={formData.testCaseId} onChange={handleChange} required placeholder="Enter Test Case ID" />
                </div>
                <div className="form-group">
                    <input type="text" id="productId" name="productId" value={formData.productId} onChange={handleChange} required placeholder="Enter Product ID" />
                </div>
                <div className="form-group">
                    <input type="text" id="trialPhase" name="trialPhase" value={formData.trialPhase} onChange={handleChange} required placeholder="Enter Trial Phase" />
                </div>
                <div className="form-group">
                    <textarea id="testMetrics" name="testMetrics" value={formData.testMetrics} onChange={handleChange} required placeholder="Enter Test Metrics" />
                </div>
                <div className="form-group">
                    <select id="finalVerdict" name="finalVerdict" value={formData.finalVerdict} onChange={handleChange} required>
                        <option value="">Select Verdict</option>
                        <option value="Pass">Pass</option>
                        <option value="Fail">Fail</option>
                    </select>
                </div>
                <div className="form-group">
                    <textarea id="recommendations" name="recommendations" value={formData.recommendations} onChange={handleChange} placeholder="Enter Recommendations" />
                </div>
                <div className="form-group">
                    <input type="text" id="approvedBy" name="approvedBy" value={formData.approvedBy} onChange={handleChange} required placeholder="Enter Approver Name" />
                </div>
                <div className="form-group">
                    <input type="date" id="dateOfApproval" name="dateOfApproval" value={formData.dateOfApproval} onChange={handleChange} required placeholder="Select Date" />
                </div>
                <div className="form-buttons">
                    <button type="submit">Submit</button>
                    <button type="button" className="cancel-button">Cancel</button>
=======
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import { API_CONFIG, AUTH_SETTINGS } from '../../config';
import './TestDocumentationForm.css';

const TestDocumentationForm = ({ onBack, onSave, initialData }) => {
    const apiBaseUrl = API_CONFIG.BASE_URL || 'http://localhost:8089';

    const [formData, setFormData] = useState({
        testCaseId: '',
        parameterName: '',
        value: '',
        unit: '',
        notes: '',
        mediaFiles: [],
        productId: '',
        trialPhase: '',
        finalVerdict: '',
        approvedBy: '',
        dateOfApproval: '',
    });

    const [testCases, setTestCases] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchTestCases = async () => {
            setIsLoading(true);
            setError(null);
            
            const api = axios.create({
                baseURL: apiBaseUrl,
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY)}`
                }
            });
            
            try {
                const response = await api.get('/api/testcases');
                setTestCases(response.data || []);
            } catch (err) {
                console.error('Error fetching test cases:', err);
                setError('Failed to load test cases');
            } finally {
                setIsLoading(false);
            }
        };
        
        fetchTestCases();
    }, [apiBaseUrl]);

    useEffect(() => {
        if (initialData) {
            setFormData({
                id: initialData.id || '',
                testCaseId: initialData.testCaseId || '',
                parameterName: initialData.parameterName || '',
                value: initialData.value || '',
                unit: initialData.unit || '',
                notes: initialData.notes || '',
                mediaFiles: [],
                productId: initialData.productId || '',
                trialPhase: initialData.trialPhase || '',
                finalVerdict: initialData.finalVerdict || '',
                approvedBy: initialData.approvedBy || '',
                dateOfApproval: initialData.dateOfApproval || '',
            });
        }
    }, [initialData]);

    const handleInputChange = (e) => {
        const { name, value, type, files } = e.target;
        
        if (type === 'file') {
            setFormData(prev => ({
                ...prev,
                mediaFiles: Array.from(files)
            }));
        } else {
            setFormData(prev => ({
                ...prev,
            [name]: value
        }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Basic validation
        if (!formData.testCaseId || !formData.parameterName || !formData.value || !formData.productId || !formData.trialPhase || !formData.finalVerdict || !formData.approvedBy || !formData.dateOfApproval) {
            toast.error('Please fill out all required fields.');
            return;
        }

        setIsLoading(true);
        setError(null);

        const formDataToSend = new FormData();

        // Append non-file fields
        Object.keys(formData).forEach(key => {
            if (key !== 'mediaFiles') {
                formDataToSend.append(key, formData[key]);
            }
        });

        // Append file fields
        formData.mediaFiles.forEach(file => {
            formDataToSend.append('attachments', file); // Use 'attachments' to match backend DTO field name
        });

        const api = axios.create({
            baseURL: apiBaseUrl,
            headers: {
                'Authorization': `Bearer ${localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY)}`,
            }
        });

        try {
            let response;
            if (formData.id) {
                response = await api.put(`/api/test-results/${formData.id}`, formDataToSend);
                toast.success('Test Documentation updated successfully!');
            } else {
                response = await api.post('/api/test-results', formDataToSend);
                toast.success('Test Documentation saved successfully!');
            }
            
            if (onSave) {
                onSave(response.data);
            }
            if (!formData.id) {
                resetForm();
            }

        } catch (err) {
            console.error('Error saving test documentation:', err);
            setError('Failed to save test documentation');
            toast.error('Failed to save test documentation');
        } finally {
            setIsLoading(false);
        }
    };

    const handleCancel = () => {
        if (onBack) {
            onBack();
        }
    };

    const resetForm = () => {
        setFormData({
            testCaseId: '',
            parameterName: '',
            value: '',
            unit: '',
            notes: '',
            mediaFiles: [],
            productId: '',
            trialPhase: '',
            finalVerdict: '',
            approvedBy: '',
            dateOfApproval: '',
        });
    };

    return (
        <div className="test-documentation-form">
            <h2>{formData.id ? 'Edit Test Documentation' : 'Create Test Documentation'}</h2>
            {isLoading && <p>Loading...</p>}
            {error && <div className="error-message">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="testCaseId">Test Case</label>
                    <select
                        id="testCaseId"
                        name="testCaseId"
                        value={formData.testCaseId}
                        onChange={handleInputChange}
                        required
                        disabled={isLoading}
                    >
                        <option value="">Select Test Case</option>
                        {testCases.map(testCase => (
                            <option key={testCase.id} value={testCase.id}>
                                {testCase.id} - {testCase.testName}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="productId">Product ID</label>
                    <input
                        type="number"
                        id="productId"
                        name="productId"
                        value={formData.productId}
                        onChange={handleInputChange}
                        required
                        disabled={isLoading}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="trialPhase">Trial Phase</label>
                    <input
                        type="text"
                        id="trialPhase"
                        name="trialPhase"
                        value={formData.trialPhase}
                        onChange={handleInputChange}
                        required
                        disabled={isLoading}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="parameterName">Parameter Name</label>
                    <input
                        type="text"
                        id="parameterName"
                        name="parameterName"
                        value={formData.parameterName}
                        onChange={handleInputChange}
                        placeholder="e.g., pH Level"
                        required
                        disabled={isLoading}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="value">Value</label>
                    <input
                        type="text"
                        id="value"
                        name="value"
                        value={formData.value}
                        onChange={handleInputChange}
                        placeholder="e.g., 7.2"
                        required
                        disabled={isLoading}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="unit">Unit</label>
                    <input
                        type="text"
                        id="unit"
                        name="unit"
                        value={formData.unit}
                        onChange={handleInputChange}
                        placeholder="e.g., pH units, mg/L, etc."
                        disabled={isLoading}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="notes">Notes</label>
                    <textarea
                        id="notes"
                        name="notes"
                        value={formData.notes}
                        onChange={handleInputChange}
                        placeholder="Enter any relevant notes or observations"
                        rows="4"
                        disabled={isLoading}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="finalVerdict">Final Verdict</label>
                    <select
                        id="finalVerdict"
                        name="finalVerdict"
                        value={formData.finalVerdict}
                        onChange={handleInputChange}
                        required
                        disabled={isLoading}
                    >
                        <option value="">Select Verdict</option>
                        <option value="PASS">PASS</option>
                        <option value="FAIL">FAIL</option>
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="approvedBy">Approved By</label>
                    <input
                        type="text"
                        id="approvedBy"
                        name="approvedBy"
                        value={formData.approvedBy}
                        onChange={handleInputChange}
                        required
                        disabled={isLoading}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="dateOfApproval">Date of Approval</label>
                    <input
                        type="datetime-local"
                        id="dateOfApproval"
                        name="dateOfApproval"
                        value={formData.dateOfApproval}
                        onChange={handleInputChange}
                        required
                        disabled={isLoading}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="mediaFiles">Attach Files</label>
                    <input
                        type="file"
                        id="mediaFiles"
                        name="mediaFiles"
                        onChange={handleInputChange}
                        multiple
                        disabled={isLoading}
                    />
                    {formData.mediaFiles.length > 0 && (
                        <span>{formData.mediaFiles.length} file(s) selected</span>
                    )}
                </div>

                <div className="form-buttons">
                    <button type="button" onClick={handleCancel} disabled={isLoading}>Cancel</button>
                    <button type="submit" disabled={isLoading}>{formData.id ? 'Update Documentation' : 'Save Documentation'}</button>
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                </div>
            </form>
        </div>
    );
};

export default TestDocumentationForm; 