import React, { useState } from 'react';
import { toast } from 'react-toastify';
import testDocumentationService from '../../services/testDocumentationService';
import './TestDocumentationForm.css';

const TestDocumentationForm = () => {
    const [formData, setFormData] = useState({
        testName: '',
        testType: '',
        description: '',
        testProcedure: '',
        expectedResults: '',
        actualResults: '',
        testStatus: 'PENDING',
        attachments: ''
    });

    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);

        try {
            await testDocumentationService.createTestDocumentation(formData);
            toast.success('Test documentation created successfully!');
            // Reset form
            setFormData({
                testName: '',
                testType: '',
                description: '',
                testProcedure: '',
                expectedResults: '',
                actualResults: '',
                testStatus: 'PENDING',
                attachments: ''
            });
        } catch (error) {
            toast.error(error.message || 'Failed to create test documentation');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="test-documentation-form">
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