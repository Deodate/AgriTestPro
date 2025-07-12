import React, { useState, useEffect, useRef } from 'react';
import './EvidenceUploadForm.css';
import axios from 'axios';
import { toast } from 'react-toastify';
import { API_CONFIG, AUTH_SETTINGS } from '../../config';
import authService from '../../services/authService';

const EvidenceUploadForm = ({ onUploadSuccess, onCancel }) => {
    const apiBaseUrl = API_CONFIG.BASE_URL || 'http://localhost:8089';

    const [entityId, setEntityId] = useState('');
    const [entityType, setEntityType] = useState(''); // e.g., 'TEST_CASE', 'TRIAL_PHASE'
    const [selectedFile, setSelectedFile] = useState(null);
    const [descriptionCaption, setDescriptionCaption] = useState('');
    const [takenBy, setTakenBy] = useState('');
    const [dateCaptured, setDateCaptured] = useState('');
    const [mediaType, setMediaType] = useState(''); // Add state for Media Type
    const [isLoading, setIsLoading] = useState(false);
    const [testCases, setTestCases] = useState([]); // Add state for test cases
    const [error, setError] = useState(null); // Add state for errors

    const fileInputRef = useRef(null); // Create a ref for the file input

    useEffect(() => {
        const currentUser = authService.getCurrentUser();
        if (currentUser) {
            // Prioritize user ID if available, otherwise use username or name
            setTakenBy(currentUser.fullName || currentUser.username || currentUser.name || '');
        }
    }, []); // Empty dependency array ensures this runs once on mount

    useEffect(() => {
        const fetchTestCases = async () => {
          setIsLoading(true);
          setError(null);

          const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
          if (!token) {
              setError('Authentication token not found. Cannot fetch test cases.');
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
            setTestCases(response.data || []);
          } catch (err) {
            console.error('Error fetching test cases:', err);
            setError('Failed to load test cases');
            setTestCases([]); // Set to empty array on error
          } finally {
            setIsLoading(false);
          }
        };

        fetchTestCases();
      }, [apiBaseUrl]); // Re-run if apiBaseUrl changes

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!selectedFile || !entityId || !mediaType) {
            toast.error('Please select a test name, media type, and a file.');
            return;
        }

        // Validate file type based on selected media type
        const fileType = selectedFile.type;
        let isValid = true;
        let errorMessage = '';

        if (mediaType === 'PHOTO' && !fileType.startsWith('image/')) {
            isValid = false;
            errorMessage = 'Please select an image file for Photo media type.';
        } else if (mediaType === 'VIDEO' && !fileType.startsWith('video/')) {
            isValid = false;
            errorMessage = 'Please select a video file for Video media type.';
        } // Add more checks here for other media types if needed

        if (!isValid) {
            toast.error(errorMessage);
            return;
        }

        setIsLoading(true);

        const formData = new FormData();
        formData.append('file', selectedFile); // Use 'file' as the parameter name
        formData.append('testCaseId', entityId); // Use entityId (which is now testCaseId) as testCaseId
        formData.append('mediaType', mediaType);
        formData.append('description', descriptionCaption);
        formData.append('takenBy', takenBy);
        formData.append('dateCaptured', dateCaptured); // Assuming dateCaptured is in a format the backend can parse

        const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
        const headers = {
            'Authorization': `Bearer ${token}`,
            // 'Content-Type': 'multipart/form-data' // Axios handles this automatically with FormData
        };

        try {
            const response = await axios.post(
                `${apiBaseUrl}/api/evidence/upload`, // New endpoint
                formData,
                { headers }
            );

            if (response.status === 200) {
                toast.success('Evidence uploaded successfully!');
                // Optionally call a success handler passed from parent component
                if (onUploadSuccess) {
                    onUploadSuccess();
                }
                // Reset form fields
                setEntityId('');
                setEntityType('');
                setSelectedFile(null);
                setDescriptionCaption('');
                setTakenBy('');
                setDateCaptured('');
                setMediaType(''); // Reset media type

                // Reset the file input element's value
                if (fileInputRef.current) {
                    fileInputRef.current.value = '';
                }
            } else {
                 // Handle non-200 responses
                 toast.error(`Upload failed: ${response.statusText}`);
                 console.error('Upload failed:', response.status, response.statusText, response.data);
            }
        } catch (error) {
            console.error('Error uploading file:', error);
             const errorMessage = error.response?.data?.message || error.message || 'An unexpected error occurred during upload.';
            toast.error(`Upload failed: ${errorMessage}`);
        } finally {
            setIsLoading(false);
        }
    };

    const handleCancelClick = () => {
        // Call the onCancel handler passed from parent component
        if (onCancel) {
            onCancel();
        }
        // Clear form fields on cancel
        setEntityId('');
        setEntityType('');
        setSelectedFile(null);
        setDescriptionCaption('');
        setTakenBy('');
        setDateCaptured('');
        setMediaType(''); // Clear media type
    };

    const handleFileChange = (e) => {
        setSelectedFile(e.target.files[0]);
    };

    return (
        <div className="evidence-upload-form-container">
            <form className="evidence-upload-form" onSubmit={handleSubmit}>
                {/* Test Name Input */}
                <div className="form-row">
                     <label htmlFor="entityId">Test Name:</label>
                    <select
                        id="entityId"
                        value={entityId}
                        onChange={(e) => {
                            setEntityId(e.target.value);
                            setEntityType('TEST_CASE'); // Set entityType to TEST_CASE when a test is selected
                        }}
                        required
                    >
                        <option value="">Select Test Name</option>
                        {testCases.map(test => (
                            <option key={test.id} value={test.id}>
                                {test.testName}
                            </option>
                        ))}
                    </select>
                </div>

                {/* Media Type */}
                <div className="form-row">
                    <label htmlFor="mediaType">Media Type:</label>
                    <select
                        id="mediaType"
                        value={mediaType}
                        onChange={(e) => setMediaType(e.target.value)}
                        required
                    >
                        <option value="">Select Media Type</option>
                        <option value="PHOTO">Photo</option>
                        <option value="VIDEO">Video</option>
                         {/* Add other relevant media types as needed */}
                    </select>
                </div>

                {/* File Upload */}
                 <div className="form-row">
                    <label htmlFor="fileUpload">Select File:</label>
                     <input
                        type="file"
                        id="fileUpload"
                        onChange={handleFileChange}
                         required // Make file selection required
                        ref={fileInputRef} // Attach the ref to the input
                    />
                </div>

                 {/* Description/Caption */}
                 <div className="form-row">
                    <label htmlFor="descriptionCaption">Description:</label>
                    <textarea
                        id="descriptionCaption"
                        placeholder="Enter Description"
                        value={descriptionCaption}
                        onChange={(e) => setDescriptionCaption(e.target.value)}
                    />
                </div>
                 {/* Taken By (User ID) */}
                 <div className="form-row">
                     <label htmlFor="takenBy">Taken By:</label>
                     <input
                        type="text"
                        id="takenBy"
                        placeholder="Enter User ID"
                        value={takenBy}
                        readOnly
                    />
                </div>
                {/* Date Captured */}
                <div className="form-row">
                     <label htmlFor="dateCaptured">Date Captured:</label>
                     <input
                        type="date"
                        id="dateCaptured"
                        placeholder="Date Captured"
                        value={dateCaptured}
                        onChange={(e) => setDateCaptured(e.target.value)}
                    />
                </div>

                <div className="button-row">
                    <button type="submit" disabled={isLoading}>
                        {isLoading ? 'Uploading...' : 'Upload'}
                    </button>
                    <button type="button" className="cancel" onClick={handleCancelClick} disabled={isLoading}>Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default EvidenceUploadForm; 