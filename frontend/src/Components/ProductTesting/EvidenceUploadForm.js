import React, { useState, useEffect, useRef } from 'react';
import './EvidenceUploadForm.css';
import axios from 'axios';
import { toast } from 'react-toastify';
import { API_CONFIG, AUTH_SETTINGS } from '../../config';
import authService from '../../services/authService';

const EvidenceUploadForm = ({ onUploadSuccess, onCancel }) => {
    const apiBaseUrl = API_CONFIG.BASE_URL;

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

          try {
            console.log('Fetching test cases from:', `${apiBaseUrl}/api/testcases`);
            const response = await axios.get(`${apiBaseUrl}/api/testcases`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            console.log('Frontend received test cases:', response.data);
            setTestCases(response.data || []);
          } catch (err) {
            console.error('Error fetching test cases:', err);
            const errorMessage = err.response?.data?.message || err.message || 'Failed to load test cases';
            setError(errorMessage);
            toast.error(errorMessage);
            setTestCases([]); // Set to empty array on error
          } finally {
            setIsLoading(false);
          }
        };

        fetchTestCases();
      }, [apiBaseUrl]); // Re-run if apiBaseUrl changes

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log('Form submitted with values:', {
            entityId,
            mediaType,
            selectedFile,
            descriptionCaption,
            takenBy,
            dateCaptured
        });

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
        }

        if (!isValid) {
            toast.error(errorMessage);
            return;
        }

        setIsLoading(true);

        const formData = new FormData();
        formData.append('file', selectedFile);
        formData.append('testCaseId', entityId);
        formData.append('mediaType', mediaType);
        formData.append('description', descriptionCaption);
        formData.append('takenBy', takenBy);
        formData.append('dateCaptured', dateCaptured);

        const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
        if (!token) {
            toast.error('Authentication token not found. Please log in again.');
            setIsLoading(false);
            return;
        }

        try {
            console.log('Sending evidence upload request to:', `${apiBaseUrl}/api/files/upload-media`);
            const response = await axios.post(
                `${apiBaseUrl}/api/files/upload-media`,
                formData,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        // Let axios set the correct Content-Type for FormData
                    }
                }
            );

            console.log('Upload response:', response);

            if (response.status === 200 || response.status === 201) {
                toast.success('Evidence Uploaded successful!', {
                    position: "top-center",
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    theme: "colored",
                    style: {
                        background: "#4CAF50",
                        color: "white",
                        fontSize: "16px",
                        textAlign: "center",
                        fontWeight: "bold"
                    }
                });

                // Reset form fields
                setEntityId('');
                setEntityType('');
                setSelectedFile(null);
                setDescriptionCaption('');
                setDateCaptured('');
                setMediaType('');

                // Reset the file input
                if (fileInputRef.current) {
                    fileInputRef.current.value = '';
                }

                // Call success handler if provided
                if (onUploadSuccess) {
                    onUploadSuccess(response.data);
                }
            } else {
                throw new Error(`Upload failed with status: ${response.status}`);
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
        if (onCancel) {
            onCancel();
        }
        // Clear form fields
        setEntityId('');
        setEntityType('');
        setSelectedFile(null);
        setDescriptionCaption('');
        setTakenBy('');
        setDateCaptured('');
        setMediaType('');
        // Reset file input
        if (fileInputRef.current) {
            fileInputRef.current.value = '';
        }
    };

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            // Validate file size (10MB limit)
            const maxSize = 10 * 1024 * 1024; // 10MB in bytes
            if (file.size > maxSize) {
                toast.error('File size exceeds 10MB limit');
                e.target.value = ''; // Reset file input
                return;
            }
            setSelectedFile(file);
        }
    };

    if (error) {
        return <div className="error-message">{error}</div>;
    }

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
                            setEntityType('TEST_CASE');
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
                    </select>
                </div>

                {/* File Upload */}
                 <div className="form-row">
                    <label htmlFor="fileUpload">Select File:</label>
                     <input
                        type="file"
                        id="fileUpload"
                        onChange={handleFileChange}
                        required
                        ref={fileInputRef}
                        accept={mediaType === 'PHOTO' ? 'image/*' : mediaType === 'VIDEO' ? 'video/*' : undefined}
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
                        required
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