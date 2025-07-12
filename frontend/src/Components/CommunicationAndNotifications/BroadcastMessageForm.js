import React, { useState } from 'react';

const BroadcastMessageForm = () => {
  const [messageTitle, setMessageTitle] = useState('');
  const [content, setContent] = useState('');
  const [recipientsGroup, setRecipientsGroup] = useState('');
  const [scheduledTime, setScheduledTime] = useState('');

  return (
    <div className="broadcast-message-form-container">
      <h2>Broadcast Message Form</h2>
      <form className="broadcast-message-form">
        <div className="form-row">
          <label htmlFor="messageTitle">Message Title:</label>
          <input
            type="text"
            id="messageTitle"
            value={messageTitle}
            onChange={(e) => setMessageTitle(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <label htmlFor="content">Content:</label>
          <textarea
            id="content"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <label htmlFor="recipientsGroup">Recipients Group:</label>
          <input
            type="text"
            id="recipientsGroup"
            value={recipientsGroup}
            onChange={(e) => setRecipientsGroup(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <label htmlFor="scheduledTime">Scheduled Time:</label>
          <input
            type="datetime-local"
            id="scheduledTime"
            value={scheduledTime}
            onChange={(e) => setScheduledTime(e.target.value)}
            required
          />
        </div>
        {/* Buttons will be added here */}
      </form>
    </div>
  );
};

export default BroadcastMessageForm; 