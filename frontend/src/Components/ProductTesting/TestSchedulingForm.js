import React, { useState, useEffect } from 'react';
import { API_CONFIG } from '../../config';
import { useAuth } from '../../contexts/AuthContext';
import { toast } from 'react-toastify';
import './TestSchedulingForm.css';

const TestSchedulingForm = () => {
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    testId: '',
    workerId: '',
    scheduledDate: '',
    priority: 'MEDIUM',
    notes: ''
  });
  const [testCases, setTestCases] = useState([]);
  const [availableWorkers, setAvailableWorkers] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Fetch test cases and workers when component mounts
    fetchTestCases();
    fetchWorkers();
  }, []);

  const fetchTestCases = async () => {
    try {
      const response = await fetch(`${API_CONFIG.BASE_URL}/api/test-cases`, {
        headers: {
          'Authorization': `Bearer ${user.token}`
        }
      });
      if (response.ok) {
        const data = await response.json();
        setTestCases(data);
      } else {
        throw new Error('Failed to fetch test cases');
      }
    } catch (error) {
      setError('Error fetching test cases');
      console.error(error);
    }
  };

  const fetchWorkers = async () => {
    try {
      const response = await fetch(`${API_CONFIG.BASE_URL}/api/workers`, {
        headers: {
          'Authorization': `Bearer ${user.token}`
        }
      });
      if (response.ok) {
        const data = await response.json();
        setAvailableWorkers(data);
      } else {
        throw new Error('Failed to fetch workers');
      }
    } catch (error) {
      setError('Error fetching workers');
      console.error(error);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);

    try {
      const response = await fetch(`${API_CONFIG.BASE_URL}/api/schedules`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${user.token}`
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        toast.success('Test scheduled successfully!');
        // Reset form
        setFormData({
          testId: '',
          workerId: '',
          scheduledDate: '',
          priority: 'MEDIUM',
          notes: ''
        });
      } else {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to schedule test');
      }
    } catch (error) {
      setError(error.message);
      toast.error(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="test-scheduling-form">
      <h2>Schedule Test</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="testId">Test Case:</label>
          <select
            id="testId"
            name="testId"
            value={formData.testId}
            onChange={handleChange}
            required
          >
            <option value="">Select a test case</option>
            {testCases.map(test => (
              <option key={test.id} value={test.id}>
                {test.name}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="workerId">Assign To:</label>
          <select
            id="workerId"
            name="workerId"
            value={formData.workerId}
            onChange={handleChange}
            required
          >
            <option value="">Select a worker</option>
            {availableWorkers.map(worker => (
              <option key={worker.id} value={worker.id}>
                {worker.name}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="scheduledDate">Schedule Date:</label>
          <input
            type="datetime-local"
            id="scheduledDate"
            name="scheduledDate"
            value={formData.scheduledDate}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
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
            <option value="URGENT">Urgent</option>
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="notes">Notes:</label>
          <textarea
            id="notes"
            name="notes"
            value={formData.notes}
            onChange={handleChange}
            rows="4"
          />
        </div>

        <div className="form-actions">
          <button type="submit" disabled={isLoading}>
            {isLoading ? 'Scheduling...' : 'Schedule Test'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default TestSchedulingForm; 