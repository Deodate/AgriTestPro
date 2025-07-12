<<<<<<< HEAD
import React, { useState } from 'react';
import './FieldActivityTrackingForm.css';

const FieldActivityTrackingForm = () => {
    const [observations, setObservations] = useState('');
    const [attachments, setAttachments] = useState(null);

    const [fieldSection, setFieldSection] = useState('');
    const [activityType, setActivityType] = useState('');
    const [date, setDate] = useState('');
    const [staffInvolved, setStaffInvolved] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            fieldSection,
            activityType,
            date,
            staffInvolved,
            observations,
            attachments
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
=======
import React, { useState, useEffect } from 'react';
import './FieldActivityTrackingForm.css';
import axiosInstance from '../../utils/axiosConfig';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const FieldActivityTrackingForm = () => {
    const [activityName, setActivityName] = useState('');
    const [location, setLocation] = useState('');
    const [responsiblePersonId, setResponsiblePersonId] = useState(''); // Will need to be populated dynamically
    const [activityDateTime, setActivityDateTime] = useState('');
    const [observations, setObservations] = useState('');
    const [attachments, setAttachments] = useState(null);
    const [status, setStatus] = useState('PLANNED'); // Default status

    const [activities, setActivities] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    const API_BASE_URL = '/api/field-activities';

    useEffect(() => {
        fetchActivities();
    }, []);

    const fetchActivities = async () => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await axiosInstance.get(API_BASE_URL);
            setActivities(response.data);
            toast.success('Field activities loaded successfully!');
        } catch (err) {
            console.error('Error fetching field activities:', err);
            setError('Failed to fetch field activities.');
            toast.error('Failed to load field activities.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('activityName', activityName);
        formData.append('location', location);
        // IMPORTANT: In a real application, responsiblePersonId should be selected from a list of users.
        // For now, we are using a placeholder. Replace with actual user ID handling.
        formData.append('responsiblePersonId', responsiblePersonId || '1'); // Placeholder, assume ID 1 exists
        formData.append('activityDateTime', `${activityDateTime}T00:00:00`); // Assuming date input is YYYY-MM-DD
        formData.append('observations', observations);
        formData.append('status', status);

        if (attachments) {
            formData.append('attachments', attachments);
        }

        try {
            const response = await axiosInstance.post(API_BASE_URL, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            toast.success('Field activity created successfully!');
            console.log('Field activity created:', response.data);
            // Clear form fields
            setActivityName('');
            setLocation('');
            setResponsiblePersonId('');
            setActivityDateTime('');
            setObservations('');
            setAttachments(null);
            setStatus('PLANNED');
            fetchActivities(); // Refresh the list
        } catch (err) {
            console.error('Error creating field activity:', err);
            toast.error(`Failed to create field activity: ${err.response?.data || err.message}`);
        }
    };

    const handleCancel = () => {
        setActivityName('');
        setLocation('');
        setResponsiblePersonId('');
        setActivityDateTime('');
        setObservations('');
        setAttachments(null);
        setStatus('PLANNED');
        toast.info('Form cleared.');
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
    };

    const handleFileChange = (e) => {
        setAttachments(e.target.files[0]);
    };

    return (
        <div className="field-activity-tracking-form-container">
            <form className="field-activity-tracking-form" onSubmit={handleSubmit}>
<<<<<<< HEAD
                <div className="form-row">
                    <label htmlFor="fieldSection">Field Section:</label>
                    <input
                        type="text"
                        id="fieldSection"
                        value={fieldSection}
                        onChange={(e) => setFieldSection(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="activityType">Activity Type:</label>
                    <input
                        type="text"
                        id="activityType"
                        value={activityType}
                        onChange={(e) => setActivityType(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="date">Date:</label>
                    <input
                        type="date"
                        id="date"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <label htmlFor="staffInvolved">Staff Involved:</label>
                    <input
                        type="text"
                        id="staffInvolved"
                        value={staffInvolved}
                        onChange={(e) => setStaffInvolved(e.target.value)}
                        required
                    />
                </div>
=======
                <h2>Field Activity Tracking Form</h2>

                <div className="form-row">
                    <label htmlFor="activityName">Activity Name:</label>
                    <input
                        type="text"
                        id="activityName"
                        placeholder="Enter Activity Name"
                        value={activityName}
                        onChange={(e) => setActivityName(e.target.value)}
                        required
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="location">Location:</label>
                    <input
                        type="text"
                        id="location"
                        placeholder="Enter Location (e.g., Field Section)"
                        value={location}
                        onChange={(e) => setLocation(e.target.value)}
                        required
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="responsiblePersonId">Responsible Person ID:</label>
                    <input
                        type="number"
                        id="responsiblePersonId"
                        placeholder="Enter Responsible Person ID"
                        value={responsiblePersonId}
                        onChange={(e) => setResponsiblePersonId(e.target.value)}
                        required
                    />
                </div>

                <div className="form-row">
                    <label htmlFor="activityDateTime">Date & Time:</label>
                    <input
                        type="date"
                        id="activityDateTime"
                        value={activityDateTime}
                        onChange={(e) => setActivityDateTime(e.target.value)}
                        required
                    />
                </div>

>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                <div className="form-row">
                    <label htmlFor="observations">Observations:</label>
                    <textarea
                        id="observations"
<<<<<<< HEAD
                        value={observations}
                        onChange={(e) => setObservations(e.target.value)}
                        required
                    />
                </div>
=======
                        placeholder="Provide detailed observations"
                        value={observations}
                        onChange={(e) => setObservations(e.target.value)}
                        rows="4"
                        required
                    ></textarea>
                </div>

>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                <div className="form-row">
                    <label htmlFor="attachments">Attachments:</label>
                    <input
                        type="file"
                        id="attachments"
                        onChange={handleFileChange}
                    />
                </div>

<<<<<<< HEAD
                <div className="button-row">
                    <button type="submit">Submit</button>
                    <button type="button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>
=======
                <div className="form-row">
                    <label htmlFor="status">Status:</label>
                    <select
                        id="status"
                        value={status}
                        onChange={(e) => setStatus(e.target.value)}
                        required
                    >
                        <option value="PLANNED">Planned</option>
                        <option value="IN_PROGRESS">In Progress</option>
                        <option value="COMPLETED">Completed</option>
                        <option value="CANCELLED">Cancelled</option>
                    </select>
                </div>

                <div className="button-row">
                    <button type="submit" className="submit-button">Submit</button>
                    <button type="button" className="cancel-button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>

            <div className="activities-list-container">
                <h2>Existing Field Activities</h2>
                {isLoading ? (
                    <p>Loading activities...</p>
                ) : error ? (
                    <p className="error-message">{error}</p>
                ) : activities.length === 0 ? (
                    <p>No field activities found.</p>
                ) : (
                    <table className="activities-table">
                        <thead>
                            <tr>
                                <th>Activity Name</th>
                                <th>Location</th>
                                <th>Responsible Person</th>
                                <th>Date & Time</th>
                                <th>Observations</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {activities.map((activity) => (
                                <tr key={activity.id}>
                                    <td>{activity.activityName}</td>
                                    <td>{activity.location}</td>
                                    <td>{activity.responsiblePerson?.fullName || activity.responsiblePerson?.username || 'N/A'}</td>
                                    <td>{new Date(activity.activityDateTime).toLocaleString()}</td>
                                    <td>{activity.observations}</td>
                                    <td>{activity.status}</td>
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
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
        </div>
    );
};

export default FieldActivityTrackingForm; 