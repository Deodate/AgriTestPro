import React, { useState } from 'react';
import './TimeTrackingForm.css';
import axios from 'axios';
import { AUTH_SETTINGS, API_CONFIG } from '../../config';
import { toast } from 'react-toastify';

const TimeTrackingForm = () => {
  const [staffName, setStaffName] = useState('');
  const [activity, setActivity] = useState('');
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');
  const [totalHours, setTotalHours] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);

    // Validate time range
    const calculatedHours = calculateTotalHours(startTime, endTime);
    if (calculatedHours === 'Invalid time range' || calculatedHours === 'Invalid Date(s)') {
      setError('Please enter a valid time range');
      setIsLoading(false);
      return;
    }

    const apiBaseUrl = API_CONFIG.BASE_URL || 'http://localhost:8888';

    try {
      const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
      if (!token) {
        throw new Error('Authentication token not found');
      }

      const response = await axios.post(`${apiBaseUrl}/api/time-tracking`, {
        staffName,
        activity,
        startTime,
        endTime,
        totalHours: calculatedHours
      }, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.data) {
        toast.success('Time tracking entry saved successfully!');
        handleCancel(); // Clear form after successful submission
      }
    } catch (error) {
      console.error('Error saving time tracking:', error);
      setError(error.response?.data?.message || error.message || 'Failed to save time tracking entry');
      toast.error('Failed to save time tracking entry');
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancel = () => {
    setStaffName('');
    setActivity('');
    setStartTime('');
    setEndTime('');
    setTotalHours('');
    setError(null);
  };

  const calculateTotalHours = (start, end) => {
    if (!start || !end) return '';
    try {
      const startDate = new Date(start);
      const endDate = new Date(end);
      
      if (isNaN(startDate.getTime()) || isNaN(endDate.getTime())) {
        return 'Invalid Date(s)';
      }
      
      const diffMs = endDate - startDate;
      if (diffMs < 0) return 'Invalid time range';
      const diffHours = diffMs / (1000 * 60 * 60);
      return diffHours.toFixed(2);
    } catch (error) {
      console.error('Error calculating total hours:', error);
      return 'Calculation Error';
    }
  };

  // Update total hours whenever start or end time changes
  React.useEffect(() => {
    const hours = calculateTotalHours(startTime, endTime);
    setTotalHours(hours);
  }, [startTime, endTime]);

  return (
    <div className="time-tracking-form-container">
      {isLoading && <div className="loading-overlay">Saving...</div>}
      <form className="time-tracking-form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="staffName">Staff Name:</label>
          <input
            type="text"
            id="staffName"
            value={staffName}
            onChange={(e) => setStaffName(e.target.value)}
            required
            disabled={isLoading}
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
            disabled={isLoading}
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
            disabled={isLoading}
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
            disabled={isLoading}
          />
        </div>

        <div className="form-row">
          <label htmlFor="totalHours">Total Hours:</label>
          <input
            type="text"
            id="totalHours"
            value={totalHours}
            readOnly
            disabled
          />
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="button-row">
          <button type="submit" disabled={isLoading}>
            {isLoading ? 'Saving...' : 'Save Time Entry'}
          </button>
          <button type="button" onClick={handleCancel} disabled={isLoading}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default TimeTrackingForm; 