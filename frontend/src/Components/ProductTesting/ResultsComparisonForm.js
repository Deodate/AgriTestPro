import React, { useState } from 'react';
import './ResultsComparisonForm.css';
import axios from 'axios';
import { API_CONFIG, AUTH_SETTINGS } from '../../config';
import { toast } from 'react-toastify';

const ResultsComparisonForm = () => {
  const apiBaseUrl = API_CONFIG.BASE_URL || 'http://localhost:8888';
  const [productsTrials, setProductsTrials] = useState('');
  const [parameterToCompare, setParameterToCompare] = useState('');
  const [timeFrame, setTimeFrame] = useState('');
  const [comparisonType, setComparisonType] = useState('');
  const [resultSummary, setResultSummary] = useState('Auto-generated summary will appear here');
  const [downloadFormat, setDownloadFormat] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);

    try {
      const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
      if (!token) {
        throw new Error('Authentication token not found');
      }

      const response = await axios.post(`${apiBaseUrl}/api/results-comparison`, {
        productsTrials: productsTrials.split(',').map(id => id.trim()),
        parameterToCompare,
        timeFrame: timeFrame.split(',').map(date => date.trim()),
        comparisonType,
        downloadFormat
      }, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.data) {
        setResultSummary(response.data.summary);
        toast.success('Comparison completed successfully!');
      }
    } catch (error) {
      console.error('Error during comparison:', error);
      setError(error.response?.data?.message || error.message || 'Failed to perform comparison');
      toast.error('Failed to perform comparison');
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancel = () => {
    setProductsTrials('');
    setParameterToCompare('');
    setTimeFrame('');
    setComparisonType('');
    setDownloadFormat('');
    setResultSummary('Auto-generated summary will appear here');
    setError(null);
  };

  return (
    <div className="results-comparison-container">
      <form className="results-comparison-form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="productsTrials">Select Products/Trials (comma-separated IDs):</label>
          <input
            type="text"
            id="productsTrials"
            placeholder="Enter Product or Trial IDs (e.g., 1,2,3)"
            value={productsTrials}
            onChange={(e) => setProductsTrials(e.target.value)}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="parameterToCompare">Parameter to Compare:</label>
          <input
            type="text"
            id="parameterToCompare"
            placeholder="Yield, Disease Resistance, etc."
            value={parameterToCompare}
            onChange={(e) => setParameterToCompare(e.target.value)}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="timeFrame">Time Frame (YYYY-MM-DD,YYYY-MM-DD):</label>
          <input
            type="text"
            id="timeFrame"
            placeholder="e.g., 2023-01-01,2023-12-31"
            value={timeFrame}
            onChange={(e) => setTimeFrame(e.target.value)}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="comparisonType">Comparison Type:</label>
          <select
            id="comparisonType"
            value={comparisonType}
            onChange={(e) => setComparisonType(e.target.value)}
            required
          >
            <option value="">Select Comparison Type</option>
            <option value="DIRECT">Direct Comparison</option>
            <option value="STATISTICAL">Statistical Analysis</option>
            <option value="TREND">Trend Analysis</option>
          </select>
        </div>

        <div className="form-row">
          <label htmlFor="resultSummary">Result Summary:</label>
          <textarea
            id="resultSummary"
            placeholder="Auto-generated summary will appear here"
            value={resultSummary}
            onChange={(e) => setResultSummary(e.target.value)}
            readOnly
          />
        </div>

        <div className="form-row">
          <label htmlFor="downloadFormat">Download Format:</label>
          <select
            id="downloadFormat"
            value={downloadFormat}
            onChange={(e) => setDownloadFormat(e.target.value)}
            required
          >
            <option value="">Select Format</option>
            <option value="PDF">PDF</option>
            <option value="CSV">CSV</option>
            <option value="EXCEL">Excel</option>
          </select>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="button-row">
          <button type="submit" disabled={isLoading}>
            {isLoading ? 'Processing...' : 'Generate Comparison'}
          </button>
          <button type="button" onClick={handleCancel} disabled={isLoading}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default ResultsComparisonForm; 