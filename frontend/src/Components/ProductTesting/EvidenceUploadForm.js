import React, { useState } from 'react';
import './EvidenceUploadForm.css';

const EvidenceUploadForm = () => {
    const [testCaseOrTrialPhaseId, setTestCaseOrTrialPhaseId] = useState('');
    const [mediaType, setMediaType] = useState('');
    const [fileUpload, setFileUpload] = useState(null);
    const [descriptionCaption, setDescriptionCaption] = useState('');
    const [takenBy, setTakenBy] = useState('');
    const [dateCaptured, setDateCaptured] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            testCaseOrTrialPhaseId,
            mediaType,
            fileUpload,
            descriptionCaption,
            takenBy,
            dateCaptured
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    const handleFileChange = (e) => {
        setFileUpload(e.target.files[0]);
    };

    return (
        <div className="evidence-upload-form-container">
            <form className="evidence-upload-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Test Case ID or Trial Phase ID (FK)"
                        value={testCaseOrTrialPhaseId}
                        onChange={(e) => setTestCaseOrTrialPhaseId(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <select
                        placeholder="Media Type"
                        value={mediaType}
                        onChange={(e) => setMediaType(e.target.value)}
                        required
                    >
                        <option value="">Select Media Type</option>
                        <option value="Photo">Photo</option>
                        <option value="Video">Video</option>
                    </select>
                </div>
                 <div className="form-row">
                     <input
                        type="file"
                        onChange={handleFileChange}
                    />
                </div>
                 <div className="form-row">
                    <textarea
                        placeholder="Description/Caption"
                        value={descriptionCaption}
                        onChange={(e) => setDescriptionCaption(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Taken By (User ID)"
                        value={takenBy}
                        onChange={(e) => setTakenBy(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                     <input
                        type="date"
                        placeholder="Date Captured"
                        value={dateCaptured}
                        onChange={(e) => setDateCaptured(e.target.value)}
                        required
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

export default EvidenceUploadForm; 