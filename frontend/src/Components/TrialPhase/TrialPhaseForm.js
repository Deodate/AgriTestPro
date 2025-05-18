import React, { useState, useEffect } from 'react';
import { FaArrowRight, FaUpload } from 'react-icons/fa';
import './TrialPhaseForm.css';
import { API_CONFIG, AUTH_SETTINGS } from '../../config';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const TrialPhaseForm = ({ onBack, onSave, initialData }) => {
  const apiBaseUrl = API_CONFIG.BASE_URL || 'http://localhost:8089';

  const [formData, setFormData] = useState({
    testCaseId: '',
    phaseName: '',
    phaseDate: new Date().toISOString().split('T')[0],
    observations: '',
    testData: '',
    mediaFiles: [],
    weatherData: {
      temperature: '',
      humidity: '',
      rainfall: ''
    },
    additionalComments: ''
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
        setTestCases([
          { id: 'TC-001', name: 'Fungicide Test A' },
          { id: 'TC-002', name: 'Herbicide Test B' },
          { id: 'TC-003', name: 'Pesticide Test C' }
        ]);
      } finally {
        setIsLoading(false);
      }
    };
    
    fetchTestCases();
  }, [apiBaseUrl]);

  const handleInputChange = (e) => {
    const { name, value, type, files } = e.target;
    
    if (type === 'file') {
      setFormData(prev => ({
        ...prev,
        mediaFiles: Array.from(files)
      }));
    } else if (name.startsWith('weather.')) {
      const weatherField = name.split('.')[1];
      setFormData(prev => ({
        ...prev,
        weatherData: {
          ...prev.weatherData,
          [weatherField]: value
        }
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
    setIsLoading(true);
    setError(null);

    try {
      const formDataToSend = new FormData();
      
      // Append all form fields
      Object.keys(formData).forEach(key => {
        if (key === 'mediaFiles') {
          formData[key].forEach(file => {
            formDataToSend.append('mediaFiles', file);
          });
        } else if (key === 'weatherData') {
          formDataToSend.append(key, JSON.stringify(formData[key]));
        } else {
          formDataToSend.append(key, formData[key]);
        }
      });

      const api = axios.create({
        baseURL: apiBaseUrl,
        headers: {
          'Authorization': `Bearer ${localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY)}`,
          'Content-Type': 'multipart/form-data'
        }
      });

      const response = await api.post('/api/trial-phases', formDataToSend);
      
      toast.success('Trial phase data saved successfully!');
      if (onSave) {
        onSave(response.data);
      }
    } catch (err) {
      console.error('Error saving trial phase:', err);
      setError('Failed to save trial phase data');
      toast.error('Failed to save trial phase data');
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancel = () => {
    if (onBack) {
      onBack();
    }
  };

  const phaseOptions = [
    'Initial',
    'Mid-Growth',
    'Harvest',
    'Post-Harvest',
    'Analysis'
  ];

  return (
    <div className="trial-phase-form">
      <form onSubmit={handleSubmit}>
        <div className="form-row">
          {/* 1. Test Case ID */}
          <div className="form-group">
            <label htmlFor="testCaseId">Test Case ID</label>
            <div className="input-container">
              <select
                id="testCaseId"
                name="testCaseId"
                value={formData.testCaseId}
                onChange={handleInputChange}
                required
              >
                <option value="">Select Test Case</option>
                {testCases.map(test => (
                  <option key={test.id} value={test.id}>
                    {test.id} - {test.name}
                  </option>
                ))}
              </select>
            </div>
          </div>

          {/* 2. Trial Phase Name */}
          <div className="form-group">
            <label htmlFor="phaseName">Trial Phase Name</label>
            <div className="input-container">
              <select
                id="phaseName"
                name="phaseName"
                value={formData.phaseName}
                onChange={handleInputChange}
                required
              >
                <option value="">Select Phase</option>
                {phaseOptions.map(phase => (
                  <option key={phase} value={phase}>{phase}</option>
                ))}
              </select>
            </div>
          </div>
        </div>

        <div className="form-row">
          {/* 3. Date of Phase */}
          <div className="form-group">
            <label htmlFor="phaseDate">Date of Phase</label>
            <div className="input-container">
              <input
                type="date"
                id="phaseDate"
                name="phaseDate"
                value={formData.phaseDate}
                onChange={handleInputChange}
                required
              />
            </div>
          </div>

          {/* 4. Observations */}
          <div className="form-group">
            <label htmlFor="observations">Observations</label>
            <div className="input-container">
              <input
                type="text"
                id="observations"
                name="observations"
                value={formData.observations}
                onChange={handleInputChange}
                placeholder="Enter observations"
                required
              />
            </div>
          </div>
        </div>

        <div className="form-row">
          {/* 5. Test Data Entry */}
          <div className="form-group">
            <label htmlFor="testData">Test Data Entry</label>
            <div className="input-container">
              <input
                type="text"
                id="testData"
                name="testData"
                value={formData.testData}
                onChange={handleInputChange}
                placeholder="Enter test data"
              />
            </div>
          </div>

          {/* 6. Photos/Videos */}
          <div className="form-group">
            <label htmlFor="mediaFiles">Photos/Videos</label>
            <div className="input-container">
              <div className="file-upload">
                <input
                  type="file"
                  id="mediaFiles"
                  name="mediaFiles"
                  onChange={handleInputChange}
                  multiple
                  accept="image/*,video/*"
                />
                <div className="file-upload-label">
                  <FaUpload />
                  <span>{formData.mediaFiles.length > 0 ? `${formData.mediaFiles.length} file(s) selected` : 'Choose files'}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="form-row">
          {/* 7. Weather Data */}
          <div className="form-group">
            <label htmlFor="weather">Weather Data</label>
            <div className="input-container">
              <div className="weather-data-section">
                <input
                  type="number"
                  id="weather.temperature"
                  name="weather.temperature"
                  value={formData.weatherData.temperature}
                  onChange={handleInputChange}
                  placeholder="Temperature (°C)"
                  step="0.1"
                />
                <input
                  type="number"
                  id="weather.humidity"
                  name="weather.humidity"
                  value={formData.weatherData.humidity}
                  onChange={handleInputChange}
                  placeholder="Humidity (%)"
                  min="0"
                  max="100"
                />
                <input
                  type="number"
                  id="weather.rainfall"
                  name="weather.rainfall"
                  value={formData.weatherData.rainfall}
                  onChange={handleInputChange}
                  placeholder="Rainfall (mm)"
                  min="0"
                  step="0.1"
                />
              </div>
            </div>
          </div>

          {/* 8. Additional Comments */}
          <div className="form-group">
            <label htmlFor="additionalComments">Additional Comments</label>
            <div className="input-container">
              <input
                type="text"
                id="additionalComments"
                name="additionalComments"
                value={formData.additionalComments}
                onChange={handleInputChange}
                placeholder="Enter additional comments"
              />
            </div>
          </div>
        </div>

        {/* Form Buttons */}
        <div className="form-buttons">
          <button 
            type="button" 
            className="cancel-btn" 
            onClick={handleCancel}
            disabled={isLoading}
          >
            Cancel
          </button>
          <button 
            type="submit" 
            className="submit-button" 
            disabled={isLoading}
          >
            {isLoading ? 'Submitting...' : 'Submit'}
          </button>
        </div>

        {error && <div className="error-message">{error}</div>}
      </form>
    </div>
  );
};

export default TrialPhaseForm; 