import React, { useState, useEffect } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { FaCheck, FaTimesCircle, FaExclamationTriangle, FaArrowLeft, FaEdit, FaTrash, FaPrint, FaFilePdf } from 'react-icons/fa';
import './ComplianceChecklist.css';
import Header from '../../Components/Header/Header';
import Footer from '../../Components/Footer/Footer';

/**
 * Component for displaying detailed information about a compliance checklist
 * @returns {JSX.Element} Rendered component
 */
const ComplianceChecklistDetail = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [checklist, setChecklist] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  
  useEffect(() => {
    // Fetch checklist data
    const fetchChecklist = async () => {
      setLoading(true);
      try {
        // Mock data for now - replace with API call
        const mockChecklist = {
          id: parseInt(id),
          productId: 101,
          productName: "Fungicide X-500",
          checklistItems: {
            "Toxicity Test": true,
            "Environmental Impact": true,
            "Application Safety": true,
            "Storage Requirements": true,
            "Disposal Guidelines": true
          },
          reviewerName: "John Smith",
          reviewDate: "2023-06-05",
          comments: "All requirements met. Product is safe for use when guidelines are followed.",
          overallStatus: "COMPLIANT",
          createdAt: "2023-06-05T10:30:00",
          updatedAt: "2023-06-05T10:30:00"
        };
        
        // Simulate API delay
        await new Promise(resolve => setTimeout(resolve, 500));
        
        setChecklist(mockChecklist);
        setLoading(false);
      } catch (err) {
        setError("Failed to fetch compliance checklist. Please try again later.");
        setLoading(false);
      }
    };
    
    fetchChecklist();
  }, [id]);
  
  // Handle checklist deletion
  const handleDelete = async () => {
    try {
      // Mock API call - replace with actual API call
      console.log("Deleting checklist:", id);
      
      // Simulate API delay
      await new Promise(resolve => setTimeout(resolve, 500));
      
      navigate('/compliance-checklist');
    } catch (err) {
      setError("Failed to delete compliance checklist. Please try again.");
    }
  };
  
  // Handle print function
  const handlePrint = () => {
    window.print();
  };
  
  // Export as PDF (mock function)
  const handleExportPdf = () => {
    alert("PDF export functionality would be implemented here");
  };
  
  // Render status badge
  const renderStatusBadge = (status) => {
    switch (status) {
      case 'COMPLIANT':
        return <span className="status-badge compliant"><FaCheck /> Compliant</span>;
      case 'PARTIALLY_COMPLIANT':
        return <span className="status-badge partially-compliant"><FaExclamationTriangle /> Partially Compliant</span>;
      case 'NON_COMPLIANT':
        return <span className="status-badge non-compliant"><FaTimesCircle /> Non-Compliant</span>;
      default:
        return <span className="status-badge">{status}</span>;
    }
  };
  
  // Calculate compliance percentage
  const calculateCompliancePercentage = (checklistItems) => {
    if (!checklistItems || Object.keys(checklistItems).length === 0) return 0;
    
    const total = Object.keys(checklistItems).length;
    const passed = Object.values(checklistItems).filter(Boolean).length;
    
    return Math.round((passed / total) * 100);
  };
  
  // Format date
  const formatDate = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString();
  };
  
  // Format datetime
  const formatDateTime = (dateTimeString) => {
    if (!dateTimeString) return '';
    const date = new Date(dateTimeString);
    return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
  };
  
  return (
    <div className="compliance-checklist-container">
      <Header />
      
      <div className="compliance-checklist-content">
        <div className="back-link">
          <Link to="/compliance-checklist">
            <FaArrowLeft /> Back to Compliance Checklists
          </Link>
        </div>
        
        {loading ? (
          <div className="loading-container">
            <p>Loading compliance checklist...</p>
          </div>
        ) : error ? (
          <div className="error-container">
            <p>{error}</p>
          </div>
        ) : !checklist ? (
          <div className="not-found-container">
            <p>Compliance checklist not found.</p>
          </div>
        ) : (
          <div className="checklist-detail">
            <div className="checklist-header">
              <div className="checklist-title">
                <h1>{checklist.productName}</h1>
                {renderStatusBadge(checklist.overallStatus)}
              </div>
              
              <div className="checklist-actions">
                <button className="action-button print" onClick={handlePrint}>
                  <FaPrint /> Print
                </button>
                <button className="action-button export" onClick={handleExportPdf}>
                  <FaFilePdf /> Export
                </button>
                <button className="action-button edit" onClick={() => navigate(`/compliance-checklist/edit/${checklist.id}`)}>
                  <FaEdit /> Edit
                </button>
                <button className="action-button delete" onClick={() => setDeleteModalOpen(true)}>
                  <FaTrash /> Delete
                </button>
              </div>
            </div>
            
            <div className="compliance-summary">
              <div className="compliance-percentage">
                <svg viewBox="0 0 36 36" className="circular-chart">
                  <path
                    className="circle-bg"
                    d="M18 2.0845
                      a 15.9155 15.9155 0 0 1 0 31.831
                      a 15.9155 15.9155 0 0 1 0 -31.831"
                  />
                  <path
                    className={`circle ${
                      checklist.overallStatus === 'COMPLIANT'
                        ? 'compliant'
                        : checklist.overallStatus === 'PARTIALLY_COMPLIANT'
                        ? 'partial'
                        : 'non-compliant'
                    }`}
                    strokeDasharray={`${calculateCompliancePercentage(checklist.checklistItems)}, 100`}
                    d="M18 2.0845
                      a 15.9155 15.9155 0 0 1 0 31.831
                      a 15.9155 15.9155 0 0 1 0 -31.831"
                  />
                  <text x="18" y="20.35" className="percentage">
                    {calculateCompliancePercentage(checklist.checklistItems)}%
                  </text>
                </svg>
                <div className="compliance-text">Compliance Rate</div>
              </div>
              
              <div className="checklist-info">
                <div className="info-group">
                  <div className="info-label">Product:</div>
                  <div className="info-value">{checklist.productName}</div>
                </div>
                <div className="info-group">
                  <div className="info-label">Reviewer:</div>
                  <div className="info-value">{checklist.reviewerName}</div>
                </div>
                <div className="info-group">
                  <div className="info-label">Review Date:</div>
                  <div className="info-value">{formatDate(checklist.reviewDate)}</div>
                </div>
                <div className="info-group">
                  <div className="info-label">Created:</div>
                  <div className="info-value">{formatDateTime(checklist.createdAt)}</div>
                </div>
                <div className="info-group">
                  <div className="info-label">Last Updated:</div>
                  <div className="info-value">{formatDateTime(checklist.updatedAt)}</div>
                </div>
                <div className="info-group full-width">
                  <div className="info-label">Comments:</div>
                  <div className="info-value comments">{checklist.comments || "No comments provided."}</div>
                </div>
              </div>
            </div>
            
            <div className="checklist-items-detail">
              <h2>Checklist Items</h2>
              <div className="item-list">
                {Object.entries(checklist.checklistItems).map(([itemName, status]) => (
                  <div key={itemName} className={`checklist-item-detail ${status ? 'passed' : 'failed'}`}>
                    <div className="item-icon">
                      {status ? <FaCheck className="check-icon" /> : <FaTimesCircle className="x-icon" />}
                    </div>
                    <div className="item-name">{itemName}</div>
                    <div className="item-status">{status ? 'Pass' : 'Fail'}</div>
                  </div>
                ))}
              </div>
            </div>
            
            {/* Delete Confirmation Modal */}
            {deleteModalOpen && (
              <div className="modal-backdrop">
                <div className="modal-content">
                  <h2>Confirm Deletion</h2>
                  <p>Are you sure you want to delete this compliance checklist?</p>
                  <p>This action cannot be undone.</p>
                  <div className="modal-actions">
                    <button className="cancel-button" onClick={() => setDeleteModalOpen(false)}>
                      Cancel
                    </button>
                    <button className="delete-button" onClick={handleDelete}>
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
      
      <Footer />
    </div>
  );
};

export default ComplianceChecklistDetail; 