import React, { useState } from 'react';
import './TaskAssignmentForm.css';

const TaskAssignmentForm = () => {
    const [taskName, setTaskName] = useState('');
    const [assignedTo, setAssignedTo] = useState('');
    const [dueDate, setDueDate] = useState('');
    const [priorityLevel, setPriorityLevel] = useState('Normal');
    const [taskDescription, setTaskDescription] = useState('');
    const [status, setStatus] = useState('Pending');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            taskName,
            assignedTo,
            dueDate,
            priorityLevel,
            taskDescription,
            status
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="task-assignment-form-container">
            <form className="task-assignment-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Task Name"
                        value={taskName}
                        onChange={(e) => setTaskName(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                     <input
                        type="text"
                        placeholder="Assigned To"
                        value={assignedTo}
                        onChange={(e) => setAssignedTo(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="date"
                        placeholder="Due Date"
                        value={dueDate}
                        onChange={(e) => setDueDate(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <select
                        placeholder="Priority Level"
                        value={priorityLevel}
                        onChange={(e) => setPriorityLevel(e.target.value)}
                        required
                    >
                        <option value="Normal">Normal</option>
                        <option value="High">High</option>
                        <option value="Urgent">Urgent</option>
                    </select>
                </div>
                 <div className="form-row">
                    <textarea
                        placeholder="Task Description"
                        value={taskDescription}
                        onChange={(e) => setTaskDescription(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                    <select
                        placeholder="Status"
                        value={status}
                        onChange={(e) => setStatus(e.target.value)}
                        required
                    >
                        <option value="Pending">Pending</option>
                        <option value="In Progress">In Progress</option>
                        <option value="Completed">Completed</option>
                    </select>
                </div>

                <div className="button-row">
                    <button type="submit">Submit</button>
                    <button type="button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default TaskAssignmentForm; 