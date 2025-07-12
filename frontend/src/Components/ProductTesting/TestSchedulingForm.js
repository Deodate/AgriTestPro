import React, { useState, useEffect } from 'react';
import './TestSchedulingForm.css';
import { toast } from 'react-toastify';
import { API_CONFIG, AUTH_SETTINGS } from '../../config';
import axios from 'axios';

const TestSchedulingForm = () => {
    const [formData, setFormData] = useState({
        testName: '',
        scheduleName: '',
        trialPhase: '',
        assignedPersonnel: '',
        location: '',
        testObjective: '',
        equipmentRequired: '',
        notificationPreference: '',
        notes: '',
        frequency: 'DAILY',
        dayOfMonth: '',
        dayOfWeek: '',
        startDate: '',
        endDate: '',
        isActive: true,
        testCaseId: '',
        description: '',
        priority: 'MEDIUM',
        status: 'PENDING'
    });

    const [testCases, setTestCases] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        // Fetch test cases when component mounts
        fetchTestCases();
    }, []);

    const fetchTestCases = async () => {
        try {
            const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
            const response = await axios.get(
                `${API_CONFIG.BASE_URL}/api/test-cases`,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                }
            );
            setTestCases(response.data);
        } catch (error) {
            console.error('Error fetching test cases:', error);
            toast.error('Failed to fetch test cases');
        }
    };

    const handleChange = (e) => {
        const { name, value, type } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: type === 'checkbox' ? e.target.checked : value
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

            const response = await axios.post(
                `${API_CONFIG.BASE_URL}/api/test-schedules`,
                formData,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );

            if (response.status === 200 || response.status === 201) {
                toast.success('Test Schedule created successfully!');
                // Reset form
                setFormData({
                    testName: '',
                    scheduleName: '',
                    trialPhase: '',
                    assignedPersonnel: '',
                    location: '',
                    testObjective: '',
                    equipmentRequired: '',
                    notificationPreference: '',
                    notes: '',
                    frequency: 'DAILY',
                    dayOfMonth: '',
                    dayOfWeek: '',
                    startDate: '',
                    endDate: '',
                    isActive: true,
                    testCaseId: '',
                    description: '',
                    priority: 'MEDIUM',
                    status: 'PENDING'
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
                    <label htmlFor="scheduleName">Schedule Name:</label>
                    <input
                        type="text"
                        id="scheduleName"
                        name="scheduleName"
                        value={formData.scheduleName}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="testCaseId">Test Case:</label>
                    <select
                        id="testCaseId"
                        name="testCaseId"
                        value={formData.testCaseId}
                        onChange={handleChange}
                        required
                    >
                        <option value="">Select Test Case</option>
                        {testCases.map(testCase => (
                            <option key={testCase.id} value={testCase.id}>
                                {testCase.testName}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-row">
                    <label htmlFor="trialPhase">Trial Phase:</label>
                    <input
                        type="text"
                        id="trialPhase"
                        name="trialPhase"
                        value={formData.trialPhase}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="assignedPersonnel">Assigned Personnel:</label>
                    <input
                        type="text"
                        id="assignedPersonnel"
                        name="assignedPersonnel"
                        value={formData.assignedPersonnel}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="location">Location:</label>
                    <input
                        type="text"
                        id="location"
                        name="location"
                        value={formData.location}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="testObjective">Test Objective:</label>
                    <textarea
                        id="testObjective"
                        name="testObjective"
                        value={formData.testObjective}
                        onChange={handleChange}
                        rows="3"
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="equipmentRequired">Equipment Required:</label>
                    <textarea
                        id="equipmentRequired"
                        name="equipmentRequired"
                        value={formData.equipmentRequired}
                        onChange={handleChange}
                        rows="3"
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="frequency">Frequency:</label>
                    <select
                        id="frequency"
                        name="frequency"
                        value={formData.frequency}
                        onChange={handleChange}
                        required
                    >
                        <option value="DAILY">Daily</option>
                        <option value="WEEKLY">Weekly</option>
                        <option value="MONTHLY">Monthly</option>
                    </select>
                </div>

                {formData.frequency === 'WEEKLY' && (
                    <div className="form-row">
                        <label htmlFor="dayOfWeek">Day of Week (1-7):</label>
                        <input
                            type="number"
                            id="dayOfWeek"
                            name="dayOfWeek"
                            value={formData.dayOfWeek}
                            onChange={handleChange}
                            min="1"
                            max="7"
                        />
                    </div>
                )}

                {formData.frequency === 'MONTHLY' && (
                    <div className="form-row">
                        <label htmlFor="dayOfMonth">Day of Month (1-31):</label>
                        <input
                            type="number"
                            id="dayOfMonth"
                            name="dayOfMonth"
                            value={formData.dayOfMonth}
                            onChange={handleChange}
                            min="1"
                            max="31"
                        />
                    </div>
                )}

                <div className="form-row">
                    <label htmlFor="startDate">Start Date:</label>
                    <input
                        type="date"
                        id="startDate"
                        name="startDate"
                        value={formData.startDate}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="endDate">End Date:</label>
                    <input
                        type="date"
                        id="endDate"
                        name="endDate"
                        value={formData.endDate}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="notificationPreference">Notification Preference:</label>
                    <input
                        type="text"
                        id="notificationPreference"
                        name="notificationPreference"
                        value={formData.notificationPreference}
                        onChange={handleChange}
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
                    <label htmlFor="notes">Notes:</label>
                    <textarea
                        id="notes"
                        name="notes"
                        value={formData.notes}
                        onChange={handleChange}
                        rows="4"
                    />
                </div>

                <div className="form-row">
                    <label>
                        <input
                            type="checkbox"
                            name="isActive"
                            checked={formData.isActive}
                            onChange={handleChange}
                        />
                        Active
                    </label>
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