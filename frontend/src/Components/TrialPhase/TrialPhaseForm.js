import React, { useState, useEffect } from 'react';
import { FaArrowRight, FaUpload } from 'react-icons/fa';
import './TrialPhaseForm.css';
import { API_CONFIG, AUTH_SETTINGS } from '../../config';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const TrialPhaseForm = ({ onBack, onSave, initialData }) => {
  const apiBaseUrl = API_CONFIG.BASE_URL || 'http://localhost:8888';

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

<<<<<<< HEAD
=======
  // Effect to populate form data when initialData prop changes
  useEffect(() => {
    if (initialData) {
      setFormData({
        testCaseId: initialData.testCaseId || '',
        phaseName: initialData.phaseName || '',
        phaseDate: initialData.phaseDate || new Date().toISOString().split('T')[0],
        observations: initialData.observations || '',
        testData: initialData.testDataEntry || '', // Note: backend uses testDataEntry
        mediaFiles: [], // Media files are typically not pre-filled for security/complexity
        weatherData: {
          temperature: initialData.weatherData?.temperature || '',
          humidity: initialData.weatherData?.humidity || '',
          rainfall: initialData.weatherData?.rainfall || ''
        },
        additionalComments: initialData.additionalComments || ''
      });
    } else {
      // Reset form if initialData is not provided (e.g., switching to create mode)
      resetForm();
    }
  }, [initialData]);

>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
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
<<<<<<< HEAD
=======
        console.log('Frontend received test cases:', response.data);
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
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
    
<<<<<<< HEAD
=======
    // Only allow input changes if not in view mode (i.e., initialData is null)
    if (initialData) return; 

>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
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

<<<<<<< HEAD
  const handleSubmit = async (e) => {
    e.preventDefault();
=======
  const resetForm = () => {
    setFormData({
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
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Prevent submission if in view mode
    if (initialData) return; 

>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
    setIsLoading(true);
    setError(null);

    try {
      const formDataToSend = new FormData();
      
      // Append all form fields
      Object.keys(formData).forEach(key => {
        if (key === 'mediaFiles') {
          formData[key].forEach(file => {
<<<<<<< HEAD
            formDataToSend.append('mediaFiles', file);
          });
        } else if (key === 'weatherData') {
          formDataToSend.append(key, JSON.stringify(formData[key]));
=======
            formDataToSend.append('attachments', file);
          });
        } else if (key === 'weatherData') {
          // Append individual weather data fields instead of the stringified object
          Object.keys(formData[key]).forEach(weatherKey => {
            formDataToSend.append(`weatherData.${weatherKey}`, formData[key][weatherKey]);
          });
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
        } else {
          formDataToSend.append(key, formData[key]);
        }
      });

      const api = axios.create({
        baseURL: apiBaseUrl,
        headers: {
          'Authorization': `Bearer ${localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY)}`,
<<<<<<< HEAD
          'Content-Type': 'multipart/form-data'
        }
      });

      const response = await api.post('/api/trial-phases', formDataToSend);
=======
          // 'Content-Type': 'multipart/form-data' // Axios and FormData handle this header
        }
      });

      const response = await api.post('/api/test-case-trial-phases', formDataToSend);
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
      
      toast.success('Trial phase data saved successfully!');
      if (onSave) {
        onSave(response.data);
      }
<<<<<<< HEAD
=======
      resetForm();
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
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

<<<<<<< HEAD
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
=======
  // Options for Trial Phase Name
  const phaseOptions = Array.from({ length: 10 }, (_, i) => `Phase ${i + 1}`);

  return (
    <div className="trial-phase-form">
      {/* Conditionally render form or just display data */}
      {initialData ? (
        // Display Mode - Using Card-like Layout
        <div className="trial-phase-details-view">
          <h2>Trial Phase Details (ID: {initialData.id})</h2>
          <div className="details-grid">
            <div className="detail-card">
              <div className="card-label">Test Name:</div>
              <div className="card-value">{initialData.testName || 'N/A'}</div>
            </div>
            <div className="detail-card">
              <div className="card-label">Trial Phase Name:</div>
              <div className="card-value">{initialData.phaseName || 'N/A'}</div>
            </div>

            <div className="detail-card">
              <div className="card-label">Date of Phase:</div>
              <div className="card-value">{initialData.phaseDate || 'N/A'}</div>
            </div>
            <div className="detail-card">
              <div className="card-label">Observations:</div>
              <div className="card-value">{initialData.observations || 'Null'}</div>
            </div>

            <div className="detail-card">
              <div className="card-label">Test Data Entry:</div>
              <div className="card-value">{initialData.testDataEntry || 'N/A'}</div>
            </div>
            <div className="detail-card">
              <div className="card-label">Photos/Videos:</div>
              <div className="card-value">
                {initialData.attachments && initialData.attachments.length > 0 ? (
                  initialData.attachments.map(attachment => (
                    <div key={attachment.id}>{attachment.fileName} ({attachment.fileType})</div>
                  ))
                ) : (
                  <span>No attachments</span>
                )}
              </div>
            </div>

            {initialData.weatherData && (
              <div className="detail-card">
                <div className="card-label">Temperature:</div>
                <div className="card-value">{initialData.weatherData.temperature ?? 'N/A'}</div>
              </div>
            )}
            {initialData.weatherData && (
              <div className="detail-card">
                <div className="card-label">Humidity:</div>
                <div className="card-value">{initialData.weatherData.humidity ?? 'N/A'}</div>
              </div>
            )}
            {initialData.weatherData && (
              <div className="detail-card">
                <div className="card-label">Rainfall:</div>
                <div className="card-value">{initialData.weatherData.rainfall ?? 'N/A'}</div>
              </div>
            )}

            <div className="detail-card full-width">
              <div className="card-label">Additional Comments:</div>
              <div className="card-value">{initialData.additionalComments || 'N/A'}</div>
            </div>
          </div>

          <div className="form-actions">
            <button type="button" className="action-button" onClick={handleCancel}>
              Back
            </button>
          </div>
        </div>
      ) : (
        // Edit/Create Mode
        <form onSubmit={handleSubmit}>
          {/* Existing form content for create/edit */}
          <div className="form-row">
            {/* 1. Test Case ID */}
            <div className="form-group">
              <label htmlFor="testCaseId">Test Name</label>
              <div className="input-container">
                <select
                  id="testCaseId"
                  name="testCaseId"
                  value={formData.testCaseId}
                  onChange={handleInputChange}
                  required
                >
                  <option value="">Select Test Case</option>
                  {testCases
                    .filter(test => test.testName)
                    .map(test => (
                    <option key={test.id} value={test.id}>
                      {test.testName}
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
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                </div>
              </div>
            </div>
          </div>
<<<<<<< HEAD
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
=======

          {/* Weather Data - will be pre-filled if available in initialData */}
           <div className="form-row">
              <div className="form-group">
                  <label htmlFor="weather.temperature">Temperature (&deg;C)</label>
                  <div className="input-container">
                      <input
                          type="number"
                          id="weather.temperature"
                          name="weather.temperature"
                          value={formData.weatherData.temperature}
                          onChange={handleInputChange}
                          placeholder="e.g., 25"
                          step="0.1"
                      />
                  </div>
              </div>
              <div className="form-group">
                  <label htmlFor="weather.humidity">Humidity (%)</label>
                  <div className="input-container">
                      <input
                          type="number"
                          id="weather.humidity"
                          name="weather.humidity"
                          value={formData.weatherData.humidity}
                          onChange={handleInputChange}
                          placeholder="e.g., 60"
                          step="0.1"
                      />
                  </div>
              </div>
              <div className="form-group">
                  <label htmlFor="weather.rainfall">Rainfall (mm)</label>
                  <div className="input-container">
                      <input
                          type="number"
                          id="weather.rainfall"
                          name="weather.rainfall"
                          value={formData.weatherData.rainfall}
                          onChange={handleInputChange}
                          placeholder="e.g., 5"
                          step="0.1"
                      />
                  </div>
              </div>
          </div>

          {/* 7. Additional Comments */}
          <div className="form-row">
            <div className="form-group full-width">
              <label htmlFor="additionalComments">Additional Comments</label>
              <div className="input-container">
                <textarea
                  id="additionalComments"
                  name="additionalComments"
                  value={formData.additionalComments}
                  onChange={handleInputChange}
                  placeholder="Enter any additional comments"
                  rows="4"
                ></textarea>
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
              </div>
            </div>
          </div>

<<<<<<< HEAD
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
=======
          <div className="form-actions">
            <button type="submit" className="action-button" disabled={isLoading}>
              {isLoading ? 'Saving...' : 'Save Trial Phase'}
            </button>
            <button type="button" className="action-button cancel" onClick={handleCancel}>
              Cancel
            </button>
          </div>
        </form>
      )}
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
    </div>
  );
};

export default TrialPhaseForm; 