import React, { useState } from 'react';
import './TestSchedulingForm.css';
import { toast } from 'react-toastify';
import { API_CONFIG, AUTH_SETTINGS } from '../../config';
import axios from 'axios';

const TestSchedulingForm = () => {
    const [formData, setFormData] = useState({
        testName: '',
        testType: '',
        scheduledDate: '',
        assignedTo: '',
        priority: 'MEDIUM',
        status: 'PENDING',
        description: ''
    });

    const [isLoading, setIsLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsLoading(true);

        try {
            const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
            if (!token) {
                toast.error('Authentication token not found. Please log in again.');
                return;
            }

            // Convert the date string to a proper datetime format
            const scheduledDateTime = new Date(formData.scheduledDate).toISOString();

            const response = await axios.post(
                `${API_CONFIG.BASE_URL}/api/test-schedules`,
                {
                    ...formData,
                    scheduledDate: scheduledDateTime
                },
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );

            if (response.status === 200) {
                toast.success('Test Schedule created successfully!', {
                    position: "top-center",
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    theme: "colored",
                    style: {
                        background: "#4CAF50",
                        color: "white",
                        fontSize: "16px",
                        textAlign: "center",
                        fontWeight: "bold"
                    }
                });

                // Reset form
                setFormData({
                    testName: '',
                    testType: '',
                    scheduledDate: '',
                    assignedTo: '',
                    priority: 'MEDIUM',
                    status: 'PENDING',
                    description: ''
                });
            }
        } catch (error) {
            console.error('Error creating test schedule:', error);
            const errorMessage = error.response?.data?.message || error.message || 'An error occurred while creating the test schedule.';
            toast.error(`Failed to create test schedule: ${errorMessage}`);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="test-scheduling-form-container">
            <form className="test-scheduling-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <label htmlFor="testName">Test Name:</label>
                    <input
                        type="text"
                        id="testName"
                        name="testName"
                        value={formData.testName}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="testType">Test Type:</label>
                    <select
                        id="testType"
                        name="testType"
                        value={formData.testType}
                        onChange={handleChange}
                        required
                    >
                        <option value="">Select Test Type</option>
                        <option value="FUNCTIONAL">Functional Test</option>
                        <option value="PERFORMANCE">Performance Test</option>
                        <option value="SECURITY">Security Test</option>
                        <option value="USABILITY">Usability Test</option>
                        <option value="INTEGRATION">Integration Test</option>
                    </select>
                </div>

                <div className="form-row">
                    <label htmlFor="scheduledDate">Scheduled Date:</label>
                    <input
                        type="datetime-local"
                        id="scheduledDate"
                        name="scheduledDate"
                        value={formData.scheduledDate}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="assignedTo">Assigned To:</label>
                    <input
                        type="text"
                        id="assignedTo"
                        name="assignedTo"
                        value={formData.assignedTo}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="priority">Priority:</label>
                    <select
                        id="priority"
                        name="priority"
                        value={formData.priority}
                        onChange={handleChange}
                    >
                        <option value="LOW">Low</option>
                        <option value="MEDIUM">Medium</option>
                        <option value="HIGH">High</option>
                        <option value="CRITICAL">Critical</option>
                    </select>
                </div>

                <div className="form-row">
                    <label htmlFor="status">Status:</label>
                    <select
                        id="status"
                        name="status"
                        value={formData.status}
                        onChange={handleChange}
                    >
                        <option value="PENDING">Pending</option>
                        <option value="IN_PROGRESS">In Progress</option>
                        <option value="COMPLETED">Completed</option>
                        <option value="CANCELLED">Cancelled</option>
                    </select>
                </div>

                <div className="form-row">
                    <label htmlFor="description">Description:</label>
                    <textarea
                        id="description"
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        rows="4"
                    />
                </div>

                <div className="button-row">
                    <button type="submit" disabled={isLoading}>
                        {isLoading ? 'Creating...' : 'Create Schedule'}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default TestSchedulingForm; 