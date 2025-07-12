import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { toast } from 'react-toastify';
import axios from 'axios';

// Services
import authService from '../services/authService';
import { API_CONFIG } from '../config';

// Components
import DashboardHeader from '../Components/Dashboard/DashboardHeader';
import DashboardSidebar from '../Components/Dashboard/DashboardSidebar';
import DashboardMain from '../Components/Dashboard/DashboardMain';

// Constants
const ITEMS_PER_PAGE = 5;

const Dashboard = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuth();
  const [isLoading, setIsLoading] = useState(true);
  const [userInfo, setUserInfo] = useState(null);
  
  // State for active menu and form visibility
  const [activeMenuItem, setActiveMenuItem] = useState('');
  const [activeTab, setActiveTab] = useState('soil');
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  
  // Menu states
  const [menuStates, setMenuStates] = useState({
    adminOpen: false,
    userDropdownOpen: false,
    productTestingOpen: true,
    complianceChecklistOpen: false,
    inventoryManagementOpen: false,
    dataAnalyticsOpen: false,
    userManagementOpen: false,
    communicationOpen: false,
    operationalToolsOpen: false,
    qualityControlOpen: false
  });

  // Submenu states
  const [subMenuStates, setSubMenuStates] = useState({
    testCaseSubMenuOpen: true,
    complianceSubMenuOpen: false,
    inventorySubMenuOpen: false,
    reportSubMenuOpen: false,
    userSecuritySubMenuOpen: false,
    notificationsSubMenuOpen: false,
    calendarSubMenuOpen: false
  });

  // Form visibility states
  const [formStates, setFormStates] = useState({
    showComplianceForm: false,
    showTestCaseForm: false,
    showTrialPhaseForm: false,
    showTestDocumentationForm: false,
    showProductRegistrationForm: false,
    showEvidenceUploadForm: false,
    showTestSchedulingForm: false,
    showHistoricalDataForm: false,
    showDataVisualizationForm: false,
    showResultsComparisonForm: false,
    showReportGenerationForm: false,
    showPerformanceAnalysisForm: false,
    showBroadcastAnnouncementForm: false,
    showTaskAssignmentForm: false,
    showAutomatedAlertForm: false,
    showCalendarManagementForm: false,
    showUserActivityLogForm: false,
    showAuditTrailForm: false,
    showPasswordPoliciesForm: false,
    showCostTrackingForm: false,
    showFieldActivityTrackingForm: false,
    showResourceAllocationForm: false,
    showTimeTrackingForm: false,
    showRealTimeStockTrackingForm: false,
    showProductManagementForm: false,
    showQualityControlForm: false,
    showEffectivenessEvaluationForm: false,
    showReportSchedulerForm: false,
    showRoleManagementForm: false,
    showSMSNotificationForm: false,
    showReminderSystemForm: false,
    showStatusUpdateForm: false,
    showEmergencyAlertForm: false,
    showEquipmentMaintenanceSchedulingForm: false,
    showTaskSchedulingForm: false,
    showEvidenceList: false,
    showTestSchedulingList: false,
    showComplianceChecklistTable: false,
    showAllDataTable: false,
    showTestCasesTable: false
  });

  // Form mode states
  const [modeStates, setModeStates] = useState({
    complianceMode: 'list',
    testCaseMode: 'list',
    trialPhaseMode: 'list'
  });

  // Data states
  const [dataStates, setDataStates] = useState({
    testCases: [],
    trialPhases: [],
    viewedTrialPhaseData: null,
    totalTestCases: 0,
    failedTestCount: 0,
    successfulTestCount: 0,
    totalCalendarEvents: 0
  });

  // Animation states
  const [animationStates, setAnimationStates] = useState({
    animatedSoilCount: 0,
    animatedWaterCount: 0,
    animatedEquipmentCount: 0,
    animatedScheduledCount: 0,
    isSoilMax: false,
    isWaterMax: false,
    isEquipmentMax: false,
    isScheduledMax: false
  });

  // Notification states
  const [notificationStates, setNotificationStates] = useState({
    notificationCount: 3,
    notificationsOpen: false,
    notifications: [
      { id: 1, message: "New soil test results are available", time: "10 min ago", read: false },
      { id: 2, message: "Equipment calibration due tomorrow", time: "1 hour ago", read: false },
      { id: 3, message: "Water test report has been updated", time: "2 hours ago", read: false }
    ]
  });

  // Product states
  const [productStates, setProductStates] = useState({
    products: [],
    editingProduct: null,
    productSearchTerm: '',
    productPage: 1,
    userFullNames: {},
    productImages: {}
  });

  // Test schedule states
  const [scheduleStates, setScheduleStates] = useState({
    testSchedules: [],
    loadingSchedules: false,
    schedulesError: null,
    schedulePage: 1,
    searchQuery: '',
    filteredSchedules: []
  });

  // Handlers
  const handleLogout = async () => {
    try {
      await logout();
      toast.success('Logged out successfully!');
      navigate('/');
    } catch (error) {
      console.error('Error during logout:', error);
      toast.error('Failed to log out.');
    }
  };

  const toggleSidebar = () => setSidebarCollapsed(!sidebarCollapsed);

  const toggleMenu = (menuName) => {
    setMenuStates(prev => ({
      ...prev,
      [menuName]: !prev[menuName]
    }));
  };

  const toggleSubMenu = (subMenuName) => {
    setSubMenuStates(prev => ({
      ...prev,
      [subMenuName]: !prev[subMenuName]
    }));
  };

  const setFormVisibility = (formName, visible) => {
    setFormStates(prev => ({
      ...prev,
      [formName]: visible
    }));
  };

  const setMode = (modeName, value) => {
    setModeStates(prev => ({
      ...prev,
      [modeName]: value
    }));
  };

  const handleProductUpdateSuccess = (updatedProduct) => {
    setProductStates(prev => ({
      ...prev,
      products: prev.products.map(p => (p.id === updatedProduct.id ? updatedProduct : p)),
      editingProduct: null
    }));
    toast.success('Product updated successfully!');
  };

  const handleCancelEdit = () => {
    setProductStates(prev => ({
      ...prev,
      editingProduct: null
    }));
  };

  const handleDeleteProduct = async (productId) => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      setProductStates(prev => ({
        ...prev,
        products: prev.products.filter(p => p.id !== productId)
      }));
      toast.success('Product deleted successfully!');
    }
  };

  const handleDeleteTrialPhase = async (id) => {
    if (window.confirm('Are you sure you want to delete this trial phase?')) {
      const token = authService.getToken();
      if (!token) {
        toast.error('Authentication token not found. Cannot delete.');
        return;
      }
      try {
        const response = await axios.delete(`${API_CONFIG.BASE_URL}/api/test-case-trial-phases/${id}`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        if (response.status === 204) {
          toast.success('Trial phase deleted successfully!');
          setDataStates(prev => ({
            ...prev,
            trialPhases: prev.trialPhases.filter(phase => phase.id !== id)
          }));
        } else {
          toast.error('Failed to delete trial phase.');
        }
      } catch (error) {
        console.error('Error deleting trial phase:', error);
        toast.error('Error deleting trial phase.');
      }
    }
  };

  // Effects
  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    const formMode = queryParams.get('TrialPhaseTrackingForm');
    const allDataMode = queryParams.get('AllData');
    const trialPhaseId = queryParams.get('id');
    const showResourceAllocation = queryParams.get('ResourceAllocationForm');

    // Reset all form states
    setFormStates(prev => {
      const newStates = {};
      Object.keys(prev).forEach(key => {
        newStates[key] = false;
      });
      return newStates;
    });

    if (formMode) {
      setMode('trialPhaseMode', formMode);
      setFormVisibility('showTrialPhaseForm', true);
      
      if (formMode === 'view' && trialPhaseId) {
        fetchTrialPhaseData(trialPhaseId);
      }
    } else if (allDataMode === 'list') {
      setFormVisibility('showAllDataTable', true);
    } else if (showResourceAllocation === 'create') {
      setFormVisibility('showResourceAllocationForm', true);
      setActiveTab('resourceallocationform');
    }
  }, [location.search]);

  // Fetch trial phase data
  const fetchTrialPhaseData = async (id) => {
    const token = authService.getToken();
    if (!token) {
      toast.error('Authentication token not found. Cannot fetch trial phase data.');
      return;
    }
    try {
      const response = await axios.get(`${API_CONFIG.BASE_URL}/api/test-case-trial-phases/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      if (response.data) {
        setDataStates(prev => ({
          ...prev,
          viewedTrialPhaseData: response.data
        }));
      }
    } catch (error) {
      console.error('Error fetching trial phase data:', error);
      toast.error('Failed to fetch trial phase data.');
    }
  };

  // Render helpers
  const renderStatusBadge = (status) => {
    let className = 'status-badge';
    if (status === 'Completed') className += ' status-completed';
    if (status === 'Pending') className += ' status-pending';
    if (status === 'Attention') className += ' status-attention';
    return <span className={className}>{status}</span>;
  };

  const renderPagination = (currentPage, totalPages, setPage) => {
    if (totalPages <= 1) return null;

    return (
      <div className="pagination-controls">
        <button onClick={() => setPage(1)} disabled={currentPage === 1}>&laquo;</button>
        <button onClick={() => setPage(currentPage - 1)} disabled={currentPage === 1}>&lt;</button>
        {Array.from({ length: totalPages }).map(number => (
          <button
            key={number}
            onClick={() => setPage(number)}
            className={`pagination-button ${currentPage === number ? 'active' : ''}`}
          >
            {number}
          </button>
        ))}
        <button onClick={() => setPage(currentPage + 1)} disabled={currentPage === totalPages}>&gt;</button>
        <button onClick={() => setPage(totalPages)} disabled={currentPage === totalPages}>&raquo;</button>
      </div>
    );
  };

  // JSX
  return (
    <div className="dashboard-container">
      <DashboardHeader 
        userInfo={userInfo}
        notificationStates={notificationStates}
        handleLogout={handleLogout}
      />

      <div className="dashboard-content">
        <DashboardSidebar 
          menuStates={menuStates}
          subMenuStates={subMenuStates}
          toggleMenu={toggleMenu}
          toggleSubMenu={toggleSubMenu}
          setFormVisibility={setFormVisibility}
          setMode={setMode}
          sidebarCollapsed={sidebarCollapsed}
          toggleSidebar={toggleSidebar}
        />

        <DashboardMain 
          activeTab={activeTab}
          formStates={formStates}
          modeStates={modeStates}
          dataStates={dataStates}
          productStates={productStates}
          scheduleStates={scheduleStates}
          handleProductUpdateSuccess={handleProductUpdateSuccess}
          handleCancelEdit={handleCancelEdit}
          handleDeleteProduct={handleDeleteProduct}
          handleDeleteTrialPhase={handleDeleteTrialPhase}
          renderStatusBadge={renderStatusBadge}
          renderPagination={renderPagination}
        />
      </div>
    </div>
  );
};

export default Dashboard; 