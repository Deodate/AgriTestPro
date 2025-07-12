import React, { useState } from 'react';
import './TaskAssignmentForm.css';

const TaskAssignmentForm = () => {
  const [formData, setFormData] = useState({
    taskTitle: '',
    assignedTo: '',
    deadline: '',
    linkedTestCaseOrActivity: '',
    priority: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Form submitted:', formData);
    // Add form submission logic here
  };

  const handleCancel = () => {
    console.log('Form cancelled');
    // Add form cancellation logic here, likely navigate away or close form
  };

  return (
    <div className="task-assignment-form-container">
      <form onSubmit={handleSubmit} className="task-assignment-form">
        <div className="form-group">
          <input
            type="text"
            id="taskTitle"
            name="taskTitle"
            value={formData.taskTitle}
            onChange={handleChange}
            placeholder="Task Title"
            required
          />
        </div>
        <div className="form-group">
          <input
            type="text"
            id="assignedTo"
            name="assignedTo"
            value={formData.assignedTo}
            onChange={handleChange}
            placeholder="Assigned To"
            required
          />
        </div>
        <div className="form-group">
          <input
            type="date"
            id="deadline"
            name="deadline"
            value={formData.deadline}
            onChange={handleChange}
            placeholder="Deadline"
            required
          />
        </div>
        <div className="form-group">
          <input
            type="text"
            id="linkedTestCaseOrActivity"
            name="linkedTestCaseOrActivity"
            value={formData.linkedTestCaseOrActivity}
            onChange={handleChange}
            placeholder="Linked Test Case or Activity"
          />
        </div>
        <div className="form-group">
          <input
            type="text"
            id="priority"
            name="priority"
            value={formData.priority}
            onChange={handleChange}
            placeholder="Priority"
          />
        </div>

        <div className="button-group">
          <button type="submit" className="submit-button">Submit</button>
          <button type="button" onClick={handleCancel} className="cancel-button">Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default TaskAssignmentForm; 