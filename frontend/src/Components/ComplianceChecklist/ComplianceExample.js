import React, { useState } from 'react';
import ComplianceForm from './ComplianceForm';

// This is an example component showing how to use the ComplianceForm
const ComplianceExample = () => {
  const [showForm, setShowForm] = useState(true);
  
  const handleSave = (formData) => {
    console.log('Form data saved:', formData);
    // Here you would typically send the data to your API
    alert('Compliance checklist saved successfully!');
    setShowForm(false);
  };
  
  const handleBack = () => {
    setShowForm(false);
    // In a real application, you might navigate to a list view here
  };
  
  return (
    <div className="compliance-example">
      {showForm ? (
        <ComplianceForm 
          onBack={handleBack}
          onSave={handleSave}
        />
      ) : (
        <div className="compliance-list-placeholder">
          <h2>Compliance Checklists List</h2>
          <p>This is where you would show a list of checklists</p>
          <button 
            onClick={() => setShowForm(true)}
            className="create-button"
          >
            Create New Checklist
          </button>
        </div>
      )}
    </div>
  );
};

export default ComplianceExample;

// Usage in Dashboard.js or another component:
/*
import ComplianceForm from '../components/ComplianceChecklist';
// or
import { ComplianceExample } from '../components/ComplianceChecklist';

// Then, in your Dashboard component:
{showComplianceForm && complianceMode === 'create' && (
  <ComplianceForm 
    onBack={() => {
      setComplianceMode('list');
      navigate('/dashboard?ComplianceChecklist=list', { replace: true });
    }}
    onSave={(data) => {
      // Handle saving compliance data
      console.log('Saving compliance data:', data);
      setComplianceMode('list');
      navigate('/dashboard?ComplianceChecklist=list', { replace: true });
    }}
  />
)}
*/ 