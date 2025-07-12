import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import testDocumentationService from '../../services/testDocumentationService';
import './TestDocumentationForm.css';

const TestDocumentationForm = () => {
    const initialFormState = {
        testName: '',
        testType: '',
        description: '',
        testProcedure: '',
        expectedResults: '',
        actualResults: '',
        testStatus: 'PENDING',
        attachments: null
    };

    const [formData, setFormData] = useState(initialFormState);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [showSuccess, setShowSuccess] = useState(false);

    useEffect(() => {
        let timer;
        if (showSuccess) {
            timer = setTimeout(() => {
                setShowSuccess(false);
            }, 5000);
        }
        return () => clearTimeout(timer);
    }, [showSuccess]);

    const handleChange = (e) => {
        const { name, value, files } = e.target;
        if (name === 'attachments') {
            setFormData(prevState => ({
                ...prevState,
                attachments: files
            }));
        } else {
            setFormData(prevState => ({
                ...prevState,
                [name]: value
            }));
        }
    };

    const resetForm = () => {
        setFormData(initialFormState);
        // Reset file input
        const fileInput = document.querySelector('input[type="file"]');
        if (fileInput) {
            fileInput.value = '';
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);

        try {
            await testDocumentationService.createTestDocumentation(formData);
            setShowSuccess(true);
            resetForm();
            toast.success('Test documentation created successfully!');
        } catch (error) {
            console.error('Error creating test documentation:', error);
            toast.error(error.message || 'Failed to create test documentation');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="test-documentation-form">
            {showSuccess && (
                <div className="success-message">
                    Test Submitted!
                </div>
            )}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="testName">Test Name *</label>
                    <input
                        type="text"
                        id="testName"
                        name="testName"
                        value={formData.testName}
                        onChange={handleChange}
                        required
                        placeholder="Enter test name"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="testType">Test Type *</label>
                    <select
                        id="testType"
                        name="testType"
                        value={formData.testType}
                        onChange={handleChange}
                        required
                    >
                        <option value="">Select test type</option>
                        <option value="UNIT_TEST">Unit Test</option>
                        <option value="INTEGRATION_TEST">Integration Test</option>
                        <option value="SYSTEM_TEST">System Test</option>
                        <option value="ACCEPTANCE_TEST">Acceptance Test</option>
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="description">Description</label>
                    <textarea
                        id="description"
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        placeholder="Enter test description"
                        rows="4"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="testProcedure">Test Procedure *</label>
                    <textarea
                        id="testProcedure"
                        name="testProcedure"
                        value={formData.testProcedure}
                        onChange={handleChange}
                        required
                        placeholder="Enter test procedure steps"
                        rows="4"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="expectedResults">Expected Results *</label>
                    <textarea
                        id="expectedResults"
                        name="expectedResults"
                        value={formData.expectedResults}
                        onChange={handleChange}
                        required
                        placeholder="Enter expected results"
                        rows="3"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="actualResults">Actual Results</label>
                    <textarea
                        id="actualResults"
                        name="actualResults"
                        value={formData.actualResults}
                        onChange={handleChange}
                        placeholder="Enter actual results"
                        rows="3"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="testStatus">Test Status</label>
                    <select
                        id="testStatus"
                        name="testStatus"
                        value={formData.testStatus}
                        onChange={handleChange}
                    >
                        <option value="PENDING">Pending</option>
                        <option value="IN_PROGRESS">In Progress</option>
                        <option value="COMPLETED">Completed</option>
                        <option value="FAILED">Failed</option>
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="attachments">Attachments</label>
                    <input
                        type="file"
                        id="attachments"
                        name="attachments"
                        onChange={handleChange}
                        multiple
                        accept=".pdf,.doc,.docx,.txt,.jpg,.jpeg,.png"
                    />
                </div>

                <div className="form-actions">
                    <button 
                        type="submit" 
                        className="submit-button"
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? 'Creating...' : 'Create Test Documentation'}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default TestDocumentationForm; 