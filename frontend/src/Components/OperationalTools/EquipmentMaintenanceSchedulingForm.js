import React, { useState } from 'react';
import './EquipmentMaintenanceSchedulingForm.css';

const EquipmentMaintenanceSchedulingForm = () => {
  const [formData, setFormData] = useState({
    equipmentName: '',
    equipmentID: '',
    lastMaintenanceDate: '',
    nextMaintenanceDate: '',
    maintenanceFrequency: '',
    assignedTechnician: '',
    notes: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic here
    console.log('Equipment Maintenance Scheduling Form submitted:', formData);
    // You would typically send this data to an API
    // Clear form or show success message
    setFormData({
      equipmentName: '',
      equipmentID: '',
      lastMaintenanceDate: '',
      nextMaintenanceDate: '',
      maintenanceFrequency: '',
      assignedTechnician: '',
      notes: '',
    });
  };

  const handleCancel = () => {
    // Handle cancel logic here, maybe close the form or clear fields
    console.log('Equipment Maintenance Scheduling Form cancelled');
    setFormData({
      equipmentName: '',
      equipmentID: '',
      lastMaintenanceDate: '',
      nextMaintenanceDate: '',
      maintenanceFrequency: '',
      assignedTechnician: '',
      notes: '',
    });
  };

  return (
    <div className="equipment-maintenance-scheduling-form-container">
     
      <form className="equipment-maintenance-scheduling-form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="equipmentName">Equipment Name:</label>
          <input
            type="text"
            id="equipmentName"
            name="equipmentName"
            value={formData.equipmentName}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="equipmentID">Equipment ID:</label>
          <input
            type="text"
            id="equipmentID"
            name="equipmentID"
            value={formData.equipmentID}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="lastMaintenanceDate">Last Maintenance Date:</label>
          <input
            type="date"
            id="lastMaintenanceDate"
            name="lastMaintenanceDate"
            value={formData.lastMaintenanceDate}
            onChange={handleChange}
          />
        </div>

        <div className="form-row">
          <label htmlFor="nextMaintenanceDate">Next Maintenance Date:</label>
          <input
            type="date"
            id="nextMaintenanceDate"
            name="nextMaintenanceDate"
            value={formData.nextMaintenanceDate}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="maintenanceFrequency">Maintenance Frequency:</label>
          <input
            type="text"
            id="maintenanceFrequency"
            name="maintenanceFrequency"
            value={formData.maintenanceFrequency}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="assignedTechnician">Assigned Technician:</label>
          <input
            type="text"
            id="assignedTechnician"
            name="assignedTechnician"
            value={formData.assignedTechnician}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-row">
          <label htmlFor="notes">Notes:</label>
          <textarea
            id="notes"
            name="notes"
            value={formData.notes}
            onChange={handleChange}
          />
        </div>

        <div className="button-row">
          <button type="submit">Schedule</button>
          <button type="button" onClick={handleCancel}>Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default EquipmentMaintenanceSchedulingForm; 