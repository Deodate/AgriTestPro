import React, { useState } from 'react';
import './UserManagementForm.css';

const UserManagementForm = () => {
  const [userId, setUserId] = useState('');
  const [username, setUsername] = useState('');
  const [role, setRole] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState(''); // Note: Password should be handled securely, this is a placeholder
  const [accountStatus, setAccountStatus] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic here
    console.log({
      userId,
      username,
      role,
      email,
      password,
      accountStatus,
    });
    // Clear form or show success message
  };

  const handleCancel = () => {
    // Handle cancel logic here, maybe close the form or clear fields
    console.log('Form cancelled');
  };

  return (
    <div className="user-management-form-container">
      <form onSubmit={handleSubmit}>
        <div className="form-row">
          <input
            type="text"
            placeholder="User ID"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <select
            placeholder="Role"
            value={role}
            onChange={(e) => setRole(e.target.value)}
            required
          >
            <option value="">Select Role</option>
            <option value="Admin">Admin</option>
            <option value="User">User</option>
          </select>
        </div>
        <div className="form-row">
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <input
            type="password"
            placeholder="Password (encrypted)"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
          <select
            placeholder="Account Status"
            value={accountStatus}
            onChange={(e) => setAccountStatus(e.target.value)}
            required
          >
            <option value="">Select Status</option>
            <option value="Active">Active</option>
            <option value="Inactive">Inactive</option>
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

export default UserManagementForm; 