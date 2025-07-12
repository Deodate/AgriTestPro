import React, { useState } from 'react';
import './BroadcastAnnouncementForm.css';

const BroadcastAnnouncementForm = () => {
    const [formData, setFormData] = useState({
        announcementTitle: '',
        messageBody: '',
        targetAudience: '',
        priorityLevel: 'Normal', // Default priority
        attachments: null // File upload
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };
    
    const handleFileChange = (e) => {
        setFormData(prevState => ({
            ...prevState,
            attachments: e.target.files[0]
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Broadcast Announcement form data submitted:', formData);
        // TODO: Implement form submission logic, including file upload
    };

    return (
        <div className="broadcast-announcement-form-container">
            <form className="broadcast-announcement-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <input type="text" id="announcementTitle" name="announcementTitle" value={formData.announcementTitle} onChange={handleChange} required placeholder="Announcement Title" />
                </div>
                <div className="form-group">
                    <textarea id="messageBody" name="messageBody" value={formData.messageBody} onChange={handleChange} required placeholder="Message Body" />
                </div>
                 <div className="form-group">
                    <select id="targetAudience" name="targetAudience" value={formData.targetAudience} onChange={handleChange} required>
                        <option value="">Select Target Audience</option>
                        <option value="All Users">All Users</option>
                        <option value="Specific Role">Specific Role</option>
                    </select>
                </div>
                <div className="form-group">
                    <select id="priorityLevel" name="priorityLevel" value={formData.priorityLevel} onChange={handleChange} required>
                        <option value="Normal">Normal</option>
                        <option value="Urgent">Urgent</option>
                    </select>
                </div>
                <div className="form-group">
                    <input type="file" id="attachments" name="attachments" onChange={handleFileChange} />
                </div>
                <div className="form-buttons">
                    <button type="submit">Submit</button>
                    <button type="button" className="cancel-button">Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default BroadcastAnnouncementForm; 