import React, { useState, useEffect, useRef } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './Dashboard.css';
import { useAuth } from '../contexts/AuthContext';
import authService from '../services/authService';
import { toast } from 'react-toastify';
import {
  FaArrowRight, FaFilter, FaChevronDown, FaSearch, FaSignOutAlt,
  FaBell, FaHome, FaChevronRight, FaChevronLeft, FaLeaf, FaWater,
  FaTools, FaCalendarAlt, FaCheck, FaExclamationTriangle, FaThermometerHalf,
  FaTint, FaCogs, FaUserCircle, FaFlask, FaWarehouse, FaChartLine,
  FaUserShield, FaComments, FaTasks, FaClipboardCheck, FaPlus, FaMinus, FaSave, FaTimesCircle,
  FaEdit, FaTrash, FaFileAlt
} from 'react-icons/fa';
import Header from '../Components/Header/Header'; // Assuming Header is used globally
import Footer from '../Components/Footer/Footer'; // Assuming Footer is used globally

// Import compliance components
import ComplianceForm from '../Components/ComplianceChecklist/ComplianceForm';
import ComplianceChecklistTable from '../Pages/ComplianceChecklist/ComplianceChecklistTable';
import ComplianceChecklistList from '../Pages/ComplianceChecklist/ComplianceChecklistList'; // Keeping this for now, but its content will be reverted
import AllDataTable from '../Components/AllDataTable/AllDataTable';

const Dashboard = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuth();
  const [isLoading, setIsLoading] = useState(true);
  const [userInfo, setUserInfo] = useState(null);

  // Sidebar menu open/closed states
  const [complianceChecklistOpen, setComplianceChecklistOpen] = useState(true); // Keeping open for focus
  const [complianceSubMenuOpenNew, setComplianceSubMenuOpenNew] = useState(true); // Keeping open for focus

  // State for compliance forms/tables
  const [showComplianceForm, setShowComplianceForm] = useState(false);
  const [complianceMode, setComplianceMode] = useState(''); // create, view
  const [showComplianceChecklistTable, setShowComplianceChecklistTable] = useState(false);
  const [showAllDataTable, setShowAllDataTable] = useState(false);

  // State to track the currently active menu item for highlighting
  const [activeMenuItem, setActiveMenuItem] = useState('');
  const [activeTab, setActiveTab] = useState('compliance'); // Default to compliance tab for testing

  // State for sidebar collapse
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);

  // Define handleLogout and toggleSidebar
  const handleLogout = async () => {
    try {
      await logout();
      navigate('/login');
      toast.info('You have been logged out.');
    } catch (error) {
      console.error('Logout failed:', error);
      toast.error('Logout failed.');
    }
  };

  const toggleSidebar = () => {
    setSidebarCollapsed(!sidebarCollapsed);
  };

  // Placeholder for renderStatusBadge and renderPagination
  const renderStatusBadge = (status) => {
    // This function will render a status badge based on the status string
    // This will likely be implemented fully in AllDataTable or ComplianceChecklistTable
    // For now, return a simple span
    return <span>{status}</span>;
  };

  const renderPagination = (currentPage, totalPages, onPageChange) => {
    // This function will render pagination controls
    // This will likely be implemented fully in AllDataTable or ComplianceChecklistTable
    // For now, return a simple div
    return <div>Pagination controls here</div>;
  };

  // Placeholder for handleDeleteTrialPhase - if it's not defined elsewhere
  const handleDeleteTrialPhase = (id) => {
    console.log(`Deleting trial phase with ID: ${id}`);
    // Implement actual deletion logic here
    toast.info(`Trial phase ${id} deleted.`);
  };

  useEffect(() => {
    // Parse URL parameters to determine which component to show
    const params = new URLSearchParams(location.search);
    if (params.has('ComplianceChecklistTable') && params.get('ComplianceChecklistTable') === 'list') {
      setShowComplianceChecklistTable(true);
      setShowComplianceForm(false);
      setComplianceMode('');
      setActiveMenuItem('ComplianceChecklist');
      setActiveTab('compliance');
    } else if (params.has('ComplianceChecklist') && params.get('ComplianceChecklist') === 'create') {
      setShowComplianceForm(true);
      setShowComplianceChecklistTable(false);
      setComplianceMode('create');
      setActiveMenuItem('ComplianceChecklist');
      setActiveTab('compliance');
    } else if (params.has('ComplianceChecklist') && params.get('ComplianceChecklist') === 'view') {
      setShowComplianceForm(true); // Assuming view uses ComplianceForm or a similar detail form
      setShowComplianceChecklistTable(false);
      setComplianceMode('view');
      setActiveMenuItem('ComplianceChecklist');
      setActiveTab('compliance');
    } else if (params.has('AllData') && params.get('AllData') === 'list') {
      setShowAllDataTable(true);
      setShowComplianceChecklistTable(false);
      setShowComplianceForm(false);
      setComplianceMode('');
      setActiveMenuItem('AllData'); // A new menu item for AllData
      setActiveTab('allData'); // A new tab for AllData
    } else {
      // Default view if no specific param is set, e.g., show the table
      setShowComplianceChecklistTable(true);
      setShowComplianceForm(false);
      setComplianceMode('');
      setActiveMenuItem('ComplianceChecklist');
      setActiveTab('compliance');
    }
    setIsLoading(false); // Set loading to false once initial state is determined
  }, [location.search]);

  // Render content based on state and URL parameters
  const renderContent = () => {
    if (isLoading) {
      return <p>Loading dashboard...</p>;
    }

    if (activeTab === 'compliance') {
      if (showComplianceChecklistTable) {
        return <ComplianceChecklistTable />;
      } else if (showComplianceForm && complianceMode === 'create') {
        return (
          <ComplianceForm
            onBack={() => navigate('/dashboard?ComplianceChecklistTable=list', { replace: true })}
            onSave={(data) => {
              console.log('Saving compliance data:', data);
              toast.success('Compliance checklist saved successfully!');
              navigate('/dashboard?ComplianceChecklistTable=list', { replace: true });
            }}
          />
        );
      } else if (showComplianceForm && complianceMode === 'view') {
        // You would likely pass an ID here for viewing a specific item
        return (
          <ComplianceForm
            onBack={() => navigate('/dashboard?ComplianceChecklistTable=list', { replace: true })}
            initialData={{ /* load data based on ID from URL */}}
          />
        );
      }
    } else if (activeTab === 'allData') {
      if (showAllDataTable) {
        return <AllDataTable />;
      }
    }
    return <p>Select a section from the sidebar.</p>;
  };

  return (
    <div className="dashboard-container">
      <Header />
      <div className="main-content">
        {/* Sidebar */}
        <div className="sidebar">
          {/* COMPLIANCE CHECKLIST SECTION */}
          <div className="sidebar-section">
            <div className="section-header" onClick={() => setComplianceChecklistOpen(!complianceChecklistOpen)}>
              {complianceChecklistOpen ? <FaChevronDown className="toggle-icon" /> : <FaChevronRight className="toggle-icon" />}
              <FaClipboardCheck className="sidebar-icon" />
              <span className="section-title">Compliance Checklist</span>
            </div>

            {complianceChecklistOpen && (
              <div className="section-content">
                <div className="sub-section">
                  <div className="sub-header" onClick={() => setComplianceSubMenuOpenNew(!complianceSubMenuOpenNew)}>
                    {complianceSubMenuOpenNew ? <FaChevronDown className="toggle-icon-sub" /> : <FaChevronRight className="toggle-icon-sub" />}
                    <span className="sub-title">Compliance Management</span>
                  </div>

                  {complianceSubMenuOpenNew && (
                    <div className="sub-content">
                      <div className={`menu-item ${activeMenuItem === 'ComplianceChecklist' && complianceMode === 'create' ? 'active' : ''}`} onClick={() => {
                        setShowComplianceForm(true);
                        setShowComplianceChecklistTable(false); // Hide table when creating
                        setComplianceMode('create');
                        setActiveTab('compliance');
                        setActiveMenuItem('ComplianceChecklist');
                        navigate('/dashboard?ComplianceChecklist=create', { replace: true });
                      }}>Create Checklist</div>
                      <div className={`menu-item ${activeMenuItem === 'ComplianceChecklist' && location.search.includes('ComplianceChecklistTable=list') ? 'active' : ''}`} onClick={() => {
                        setShowComplianceForm(false); // Hide other compliance forms
                        setShowComplianceChecklistTable(true); // Show the new table component
                        setComplianceMode(''); // Clear existing compliance mode
                        setActiveTab('compliance');
                        setActiveMenuItem('ComplianceChecklist');
                        navigate('/dashboard?ComplianceChecklistTable=list', { replace: true });
                      }}>View All Checklists</div>
                      <div className={`menu-item ${activeMenuItem === 'AllData' ? 'active' : ''}`} onClick={() => {
                        setShowAllDataTable(true);
                        setShowComplianceForm(false);
                        setShowComplianceChecklistTable(false);
                        setComplianceMode('');
                        setActiveTab('allData');
                        setActiveMenuItem('AllData');
                        navigate('/dashboard?AllData=list', { replace: true });
                      }}>View All Data</div>
                      {/* Placeholder for other compliance menu items */}
                      <div className="menu-item">Recent Compliance</div>
                      <div className="menu-item">Compliance by Product</div>
                      <div className="menu-item">Compliance Reports</div>
                      <div className="menu-item">Compliance Status</div>
                      <div className="menu-item">Compliance Trends</div>
                    </div>
                  )}
                </div>
              </div>
            )}
            {/* Other sidebar sections would go here */}
          </div>
        </div>

        {/* Main Content Area */}
        <div className="dashboard-content-area">
          {renderContent()}
        </div>
      </div>
      <button className="collapse-btn" onClick={toggleSidebar}>
        {sidebarCollapsed ? <FaChevronRight /> : <FaChevronLeft />}
      </button>
      <Footer />
    </div>
  );
};

export default Dashboard; 