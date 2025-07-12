import React, { useState, useEffect } from 'react';
import './TestSchedulingForm.css';
import { AUTH_SETTINGS } from '../../config'; // Import AUTH_SETTINGS
import axios from 'axios'; // Import axios for making HTTP requests
import { API_CONFIG } from '../../config'; // Import API_CONFIG
import { toast } from 'react-toastify'; // Import toast for notifications

const TestSchedulingForm = () => {
    const [productId, setProductId] = useState('');
    const [trialPhase, setTrialPhase] = useState('');
    const [scheduledDate, setScheduledDate] = useState('');
    const [assignedPersonnel, setAssignedPersonnel] = useState('');
    const [location, setLocation] = useState('');
    const [testObjective, setTestObjective] = useState('');
    const [equipmentRequired, setEquipmentRequired] = useState('');
    const [notificationPreference, setNotificationPreference] = useState('');
    const [notes, setNotes] = useState('');
    const [scheduleName, setScheduleName] = useState('');
    const [frequency, setFrequency] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [testCases, setTestCases] = useState([]); // State to hold fetched test cases
    const [availableWorkers, setAvailableWorkers] = useState([]); // State to hold fetched workers
    const apiBaseUrl = API_CONFIG.BASE_URL || 'http://localhost:8089'; // Define apiBaseUrl

    // Fetch test cases on component mount
    useEffect(() => {
        const fetchTestCases = async () => {
          setIsLoading(true);

          const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
          if (!token) {
              console.error('Authentication token not found. Cannot fetch test cases.');
              setIsLoading(false);
              return;
          }

          const api = axios.create({
            baseURL: apiBaseUrl,
            headers: {
              'Authorization': `Bearer ${token}`
            }
          });

          try {
            const response = await api.get('/api/testcases');
            console.log('Frontend received test cases:', response.data);
            // Filter out test cases with empty or null testName before setting state
            const validTestCases = (response.data || []).filter(test => test.testName && test.testName.trim() !== '');
            setTestCases(validTestCases);
          } catch (err) {
            console.error('Error fetching test cases:', err);
             // Optionally set an error state to display a message to the user
          } finally {
            setIsLoading(false);
          }
        };

        fetchTestCases();
    }, [apiBaseUrl]); // Re-run if apiBaseUrl changes

    // Fetch workers on component mount
    useEffect(() => {
        const fetchWorkers = async () => {
          setIsLoading(true);
          // No explicit error state for workers for now, using console.error

          const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
          if (!token) {
              console.error('Authentication token not found. Cannot fetch workers.');
              setIsLoading(false);
              return;
          }

          const api = axios.create({
            baseURL: apiBaseUrl,
            headers: {
              'Authorization': `Bearer ${token}`
            }
          });

          try {
            // First try to get users with ROLE_FIELD_WORKER
            const fieldWorkerResponse = await api.get('/api/users/role/ROLE_FIELD_WORKER');
            let workers = [];
            if (fieldWorkerResponse.data && Array.isArray(fieldWorkerResponse.data)) {
              workers = fieldWorkerResponse.data.map(user => ({
                id: user.id,
                name: user.fullName || user.username // Use fullName or username
              }));
            }

            // Then try agronomists and combine the lists, avoiding duplicates if any
            const agronomistResponse = await api.get('/api/users/role/ROLE_AGRONOMIST');
            if (agronomistResponse.data && Array.isArray(agronomistResponse.data)) {
                 const agronomistWorkers = agronomistResponse.data.map(user => ({
                    id: user.id,
                    name: user.fullName || user.username // Use fullName or username
                 }));
                 // Combine and remove potential duplicates based on id
                 const combinedWorkers = [...workers, ...agronomistWorkers];
                 const uniqueWorkers = Array.from(new Map(combinedWorkers.map(item => [item['id'], item])).values());
                 setAvailableWorkers(uniqueWorkers);
            } else {
                // If no agronomists, just use field workers (or empty if none)
                 setAvailableWorkers(workers);
            }

            console.log('Frontend received workers:', availableWorkers);

          } catch (err) {
            console.error('Error fetching workers:', err);
            // Keep availableWorkers as empty or set a default/error state
             setAvailableWorkers([]); // Set to empty array on error
          } finally {
            setIsLoading(false);
          }
        };

        fetchWorkers();
    }, [apiBaseUrl]); // Re-run if apiBaseUrl changes

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log('Submit button clicked. Submitting form...');
        // Handle form submission logic here
        const formData = {
            testCaseId: productId,
            trialPhase: trialPhase,
            scheduledDate: scheduledDate,
            assignedPersonnel: assignedPersonnel,
            location: location,
            testObjective: testObjective,
            equipmentRequired: equipmentRequired,
            notificationPreference: notificationPreference,
            notes: notes,
            scheduleName: scheduleName,
            startDate: scheduledDate,
            frequency: frequency,
            trialPhase: trialPhase,
            assignedPersonnel: assignedPersonnel,
            location: location,
            testObjective: testObjective,
            equipmentRequired: equipmentRequired,
            notificationPreference: notificationPreference,
            notes: notes
        };

        try {
            const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY); // Get token from local storage
            if (!token) {
                console.error('Authentication token not found.');
                toast.error('You are not authenticated. Please log in.');
                setIsLoading(false); // Assuming you have an isLoading state
                return; // Stop the submission process if no token
            }

            const response = await fetch('http://localhost:8089/api/schedules', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`, // Add Authorization header
                },
                body: JSON.stringify(formData),
            });

            if (!response.ok) {
                // Handle HTTP errors
                const errorData = await response.json();
                console.error('Submission failed:', response.status, errorData);
                toast.error(`Failed to schedule test: ${errorData.message || response.statusText}`);
            } else {
                // Handle successful submission
                const result = await response.json();
                console.log('Submission successful:', result);
                toast.success('Test scheduled successfully!');
                // Optionally clear the form
                // setProductId('');
                // setTrialPhase('');
                // setScheduledDate('');
                // setAssignedPersonnel('');
                // setLocation('');
                // setTestObjective('');
                // setEquipmentRequired('');
                // setNotificationPreference('');
                // setNotes('');
            }
        } catch (error) {
            console.error('Error submitting form:', error);
            toast.error('An error occurred while scheduling the test.');
        } finally {
            setIsLoading(false); // Ensure isLoading is set to false after the submission attempt
        }
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="test-scheduling-form-container">
            <form className="test-scheduling-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <label htmlFor="productId">Test Name:</label>
                    <select
                        id="productId"
                        value={productId}
                        onChange={(e) => setProductId(e.target.value)}
                        required
                        disabled={isLoading} // Disable while loading test cases
                    >
                        <option value="">{isLoading ? 'Loading Test Cases...' : 'Select Test Name'}</option>
                        {testCases.map(test => (
                            <option key={test.id} value={test.id}>
                                {test.testName}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="form-row">
                    <label htmlFor="scheduleName">Schedule Name:</label>
                    <input
                        type="text"
                        id="scheduleName"
                        placeholder="Schedule Name"
                        value={scheduleName}
                        onChange={(e) => setScheduleName(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="trialPhase">Trial Phase:</label>
                    <input
                        type="text"
                        id="trialPhase"
                        placeholder="Trial Phase"
                        value={trialPhase}
                        onChange={(e) => setTrialPhase(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="scheduledDate">Scheduled Date:</label>
                    <input
                        type="date"
                        id="scheduledDate"
                        placeholder="Scheduled Date"
                        value={scheduledDate}
                        onChange={(e) => setScheduledDate(e.target.value)}
                        required
                    />
                </div>
                 {/* Assigned Personnel Dropdown */}
                 <div className="form-row">
                     <label htmlFor="assignedPersonnel">Assigned Personnel:</label>
                     <select
                        id="assignedPersonnel"
                        value={assignedPersonnel}
                        onChange={(e) => setAssignedPersonnel(e.target.value)}
                        required
                        disabled={isLoading} // Disable while loading workers
                    >
                        <option value="">{isLoading ? 'Loading Workers...' : 'Select Personnel'}</option>
                        {availableWorkers.map(worker => (
                            <option key={worker.id} value={worker.name}>
                                {worker.name}
                            </option>
                        ))}
                    </select>
                </div>
                 <div className="form-row">
                     <label htmlFor="location">Location:</label>
                     <input
                        type="text"
                        id="location"
                        placeholder="Location"
                        value={location}
                        onChange={(e) => setLocation(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                    <label htmlFor="testObjective">Test Objective:</label>
                    <textarea
                        id="testObjective"
                        placeholder="Test Objective"
                        value={testObjective}
                        onChange={(e) => setTestObjective(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="equipmentRequired">Equipment Required:</label>
                    <textarea
                        id="equipmentRequired"
                        placeholder="Equipment Required"
                        value={equipmentRequired}
                        onChange={(e) => setEquipmentRequired(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                    <label htmlFor="notificationPreference">Notification Preference:</label>
                    <select
                        id="notificationPreference"
                        placeholder="Notification Preference"
                        value={notificationPreference}
                        onChange={(e) => setNotificationPreference(e.target.value)}
                        required
                    >
                        <option value="">Select Preference</option>
                        <option value="SMS">SMS</option>
                        <option value="Email">Email</option>
                    </select>
                </div>
                 <div className="form-row">
                    <label htmlFor="notes">Notes:</label>
                    <textarea
                        id="notes"
                        placeholder="Notes"
                        value={notes}
                        onChange={(e) => setNotes(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="frequency">Frequency:</label>
                    <select
                        id="frequency"
                        placeholder="Frequency"
                        value={frequency}
                        onChange={(e) => setFrequency(e.target.value)}
                        required
                    >
                        <option value="">Select Frequency</option>
                        <option value="DAILY">Daily</option>
                        <option value="WEEKLY">Weekly</option>
                        <option value="BIWEEKLY">Bi-Weekly</option>
                        <option value="MONTHLY">Monthly</option>
                    </select>
                </div>

                <div className="button-row">
                    <button type="submit">Submit</button>
                    <button type="button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default TestSchedulingForm; 