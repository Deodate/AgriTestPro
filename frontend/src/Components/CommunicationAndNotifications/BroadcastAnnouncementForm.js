import React, { useState } from 'react';
import './BroadcastAnnouncementForm.css';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { API_CONFIG, AUTH_SETTINGS } from '../../config';

const BroadcastAnnouncementForm = () => {
    const [formData, setFormData] = useState({
        title: '',
        messageBody: '',
        targetAudience: '',
        priorityLevel: 'NORMAL',
        attachments: []
    });

    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState(null);
    const [isSubmitted, setIsSubmitted] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };
    
    const handleFileChange = (e) => {
        const files = Array.from(e.target.files);
        setFormData(prevState => ({
            ...prevState,
            attachments: files
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setError(null);

        try {
            const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
            if (!token) {
                throw new Error('Authentication required to create announcement');
            }

            // Create form data for multipart request
            const formDataToSend = new FormData();
            formDataToSend.append('title', formData.title);
            formDataToSend.append('messageBody', formData.messageBody);
            formDataToSend.append('targetAudience', formData.targetAudience);
            formDataToSend.append('priorityLevel', formData.priorityLevel);

            // Append each file to the form data
            formData.attachments.forEach(file => {
                formDataToSend.append('attachments', file);
            });

            const response = await axios.post(
                `${API_CONFIG.BASE_URL}/api/announcements`,
                formDataToSend,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'multipart/form-data'
                    }
                }
            );

            setIsSubmitted(true);
            toast.success('Announcement created successfully!');

            // Reset form
            setFormData({
                title: '',
                messageBody: '',
                targetAudience: '',
                priorityLevel: 'NORMAL',
                attachments: []
            });

            // Reset file input
            const fileInput = document.getElementById('attachments');
            if (fileInput) {
                fileInput.value = '';
            }

        } catch (err) {
            console.error('Error creating announcement:', err);
            const errorMessage = err.response?.data?.message || err.message || 'Failed to create announcement';
            setError(errorMessage);
            toast.error(errorMessage);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="broadcast-announcement-form-container">
            <form className="broadcast-announcement-form" onSubmit={handleSubmit}>
                {isSubmitted ? (
                    <h2 className="success-message">Successful submission!</h2>
                ) : (
                    <h2>Create Broadcast Announcement</h2>
                )}
                
                {error && <div className="error-message">{error}</div>}
                
                <div className="form-group">
                    <label htmlFor="title">Announcement Title</label>
                    <input 
                        type="text" 
                        id="title" 
                        name="title" 
                        value={formData.title} 
                        onChange={handleChange} 
                        required 
                        placeholder="Enter announcement title"
                        maxLength={255} 
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="messageBody">Message Body</label>
                    <textarea 
                        id="messageBody" 
                        name="messageBody" 
                        value={formData.messageBody} 
                        onChange={handleChange} 
                        required 
                        placeholder="Enter announcement message"
                        maxLength={5000}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="targetAudience">Target Audience</label>
                    <select 
                        id="targetAudience" 
                        name="targetAudience" 
                        value={formData.targetAudience} 
                        onChange={handleChange} 
                        required
                    >
                        <option value="">Select Target Audience</option>
                        <option value="ALL_USERS">All Users</option>
                        <option value="ADMIN">Administrators</option>
                        <option value="TESTER">Testers</option>
                        <option value="MANAGER">Managers</option>
                        <option value="RESEARCHER">Researchers</option>
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="priorityLevel">Priority Level</label>
                    <select 
                        id="priorityLevel" 
                        name="priorityLevel" 
                        value={formData.priorityLevel} 
                        onChange={handleChange} 
                        required
                    >
                        <option value="NORMAL">Normal</option>
                        <option value="URGENT">Urgent</option>
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="attachments">Attachments</label>
                    <input 
                        type="file" 
                        id="attachments" 
                        name="attachments" 
                        onChange={handleFileChange}
                        multiple
                        accept=".pdf,.doc,.docx,.jpg,.jpeg,.png"
                    />
                    <small>Supported file types: PDF, DOC, DOCX, JPG, JPEG, PNG</small>
                </div>

                <div className="form-buttons">
                    <button 
                        type="submit" 
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? 'Creating...' : 'Create Announcement'}
                    </button>
                    <button 
                        type="button" 
                        className="cancel-button" 
                        onClick={() => {
                            setFormData({
                                title: '',
                                messageBody: '',
                                targetAudience: '',
                                priorityLevel: 'NORMAL',
                                attachments: []
                            });
                            setIsSubmitted(false);
                            const fileInput = document.getElementById('attachments');
                            if (fileInput) {
                                fileInput.value = '';
                            }
                        }}
                        disabled={isSubmitting}
                    >
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
};

export default BroadcastAnnouncementForm; 