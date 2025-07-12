import React, { useState } from 'react';
import './TimeTrackingForm.css'; // Optional: for specific form styling
<<<<<<< HEAD
=======
import axios from 'axios'; // Import axios for API calls
import { AUTH_SETTINGS, API_CONFIG } from '../../config'; // Import auth settings and API config
import { toast } from 'react-toastify'; // Import toast for notifications
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589

const TimeTrackingForm = () => {
  const [staffName, setStaffName] = useState('');
  const [activity, setActivity] = useState('');
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');
  const [totalHours, setTotalHours] = useState('');
<<<<<<< HEAD

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic here
    console.log({
      staffName,
      activity,
      startTime,
      endTime,
      totalHours,
    });
    // Clear form or show success message
=======
  const [isLoading, setIsLoading] = useState(false); // Add loading state

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true); // Set loading to true on submission

    // Prepare data to match backend model
    const timeTrackingData = {
      staffName,
      activity,
      startTime: startTime ? new Date(startTime).toISOString() : null, // Convert to ISO string
      endTime: endTime ? new Date(endTime).toISOString() : null, // Convert to ISO string
      totalHours: totalHours ? parseFloat(totalHours) : null, // Convert to number
    };

    console.log('Submitting time tracking data:', timeTrackingData);

    const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY); // Get auth token

    if (!token) {
      console.error('Authentication token not found. Please log in.');
      toast.error('Authentication required to submit time tracking data.');
      setIsLoading(false); // Stop loading
      return; // Stop submission if no token
    }

    const apiBaseUrl = API_CONFIG.BASE_URL || 'http://localhost:8089'; // Get API base URL

    try {
      const response = await axios.post(`${apiBaseUrl}/api/time-tracking`, timeTrackingData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      console.log('Time tracking data saved successfully:', response.data);
      toast.success('Time tracking data saved successfully!');
      
      // Clear the form after successful submission
      setStaffName('');
      setActivity('');
      setStartTime('');
      setEndTime('');
      setTotalHours(''); // totalHours will be recalculated by useEffect

    } catch (error) {
      console.error('Error saving time tracking data:', error);
      if (error.response) {
        console.error('Error response data:', error.response.data);
        console.error('Error response status:', error.response.status);
        alert(`Failed to save time tracking data: ${error.response.data.message || error.response.statusText}`);
      } else if (error.request) {
        console.error('Error request:', error.request);
        alert('No response received from the server. Please try again.');
      } else {
        console.error('Error message:', error.message);
        alert(`An error occurred: ${error.message}`);
      }
      toast.error('Failed to save time tracking data.');

    } finally {
      setIsLoading(false); // Stop loading regardless of success or failure
    }
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
  };

  const handleCancel = () => {
    // Handle cancel logic here, maybe close the form or clear fields
    console.log('Form cancelled');
<<<<<<< HEAD
=======
    // Clear the form fields
    setStaffName('');
    setActivity('');
    setStartTime('');
    setEndTime('');
    setTotalHours('');
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
  };

  // Helper to calculate total hours (basic implementation, can be refined)
  const calculateTotalHours = (start, end) => {
    if (!start || !end) return '';
<<<<<<< HEAD
    const startDate = new Date(start);
    const endDate = new Date(end);
=======
    try {
    const startDate = new Date(start);
    const endDate = new Date(end);
      // Check for valid dates
      if (isNaN(startDate.getTime()) || isNaN(endDate.getTime())) {
          return 'Invalid Date(s)';
      }
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
    const diffMs = endDate - startDate;
    if (diffMs < 0) return 'Invalid time range';
    const diffHours = diffMs / (1000 * 60 * 60);
    return diffHours.toFixed(2);
<<<<<<< HEAD
=======
    } catch (error) {
        console.error('Error calculating total hours:', error);
        return 'Calculation Error';
    }
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
  };

  React.useEffect(() => {
    setTotalHours(calculateTotalHours(startTime, endTime));
  }, [startTime, endTime]);

  return (
    <div className="time-tracking-form-container">
<<<<<<< HEAD
=======
      {isLoading && <div className="loading-overlay">Saving...</div>} {/* Loading overlay */}
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
      <form className="time-tracking-form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="staffName">Staff Name:</label>
          <input
            type="text"
            id="staffName"
            value={staffName}
            onChange={(e) => setStaffName(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <label htmlFor="activity">Activity:</label>
          <input
            type="text"
            id="activity"
            value={activity}
            onChange={(e) => setActivity(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <label htmlFor="startTime">Start Time:</label>
          <input
            type="datetime-local"
            id="startTime"
            value={startTime}
            onChange={(e) => setStartTime(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <label htmlFor="endTime">End Time:</label>
          <input
            type="datetime-local"
            id="endTime"
            value={endTime}
            onChange={(e) => setEndTime(e.target.value)}
            required
          />
        </div>
         <div className="form-row">
          <label htmlFor="totalHours">Total Hours:</label>
          <input
            type="text"
            id="totalHours"
            value={totalHours}
            readOnly // Calculated field
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

export default TimeTrackingForm; 