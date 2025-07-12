import React, { useState } from 'react';
import './RoleManagementForm.css';

const RoleManagementForm = () => {
  const [formData, setFormData] = useState({
    userName: '',
    email: '', // This would typically be auto-filled or selected
    role: '',
    permissions: '',
    effectiveDate: '',
    comment: '',
  });

  // In a real application, you'd likely fetch user data to auto-fill email based on userName
  // useEffect(() => {
  //   if (formData.userName) {
  //     // Fetch user data and set email
  //     // setFormData(prevData => ({ ...prevData, email: fetchedEmail }));
  //   }
  // }, [formData.userName]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic here
    console.log('Role Management Form submitted:', formData);
    // You would typically send this data to an API
    // Clear form or show success message
    setFormData({
      userName: '',
      email: '',
      role: '',
      permissions: '',
      effectiveDate: '',
      comment: '',
    });
  };

  const handleCancel = () => {
    // Handle cancel logic here, maybe close the form or clear fields
    console.log('Role Management Form cancelled');
    setFormData({
      userName: '',
      email: '',
      role: '',
      permissions: '',
      effectiveDate: '',
      comment: '',
    });
  };

  return (
    <div className="role-management-form-container">
      <form className="role-management-form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="userName">User Name:</label>
          <input
            type="text"
            id="userName"
            name="userName"
            value={formData.userName}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="email">Email (Auto-filled):</label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            // In a real app, make this readonly and auto-fill
            readOnly
            disabled
          />
        </div>

        <div className="form-row">
          <label htmlFor="role">Role:</label>
          <input
            type="text"
            id="role"
            name="role"
            value={formData.role}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="permissions">Permissions:</label>
          <textarea
            id="permissions"
            name="permissions"
            value={formData.permissions}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="effectiveDate">Effective Date:</label>
          <input
            type="date"
            id="effectiveDate"
            name="effectiveDate"
            value={formData.effectiveDate}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="comment">Comment:</label>
          <textarea
            id="comment"
            name="comment"
            value={formData.comment}
            onChange={handleChange}
          />
        </div>

        <div className="button-row">
          <button type="submit">Save Role</button>
          <button type="button" onClick={handleCancel}>Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default RoleManagementForm; 