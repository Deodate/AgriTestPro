import React, { useState, useEffect, useRef, useMemo } from 'react';
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
// Import with correct capitalization 
import ComplianceForm from '../Components/ComplianceChecklist/ComplianceForm';
// Import TestCaseForm
import TestCaseForm from '../Components/TestCase/TestCaseForm';
import TrialPhaseForm from '../Components/TrialPhase/TrialPhaseForm';
import TestDocumentationForm from '../Components/TestDocumentation/TestDocumentationForm';
import ProductEntryForm from '../Components/InventoryManagement/ProductEntryForm';
import StockMovementForm from '../Components/InventoryManagement/StockMovementForm';
import StockMonitoringForm from '../Components/InventoryManagement/StockMonitoringForm';
import ReportGenerationForm from '../Components/DataAnalyticsAndReporting/ReportGenerationForm';
import PerformanceAnalysisForm from '../Components/DataAnalyticsAndReporting/PerformanceAnalysisForm';
import QualityIncidentReportForm from '../Components/QualityControlAndCompliance/QualityIncidentReportForm';
import BroadcastAnnouncementForm from '../Components/CommunicationAndNotifications/BroadcastAnnouncementForm';
import TaskAssignmentForm from '../Components/CommunicationAndNotifications/TaskAssignmentForm';
import AutomatedAlertForm from '../Components/CommunicationAndNotifications/AutomatedAlertForm';
import CalendarManagementForm from '../Components/OperationalTools/CalendarManagementForm';
import UserActivityLogForm from '../Components/UserManagement/UserActivityLogForm';
import AuditTrailForm from '../Components/UserManagement/AuditTrailForm';
import PasswordPoliciesForm from '../Components/UserManagement/PasswordPoliciesForm';
import CostTrackingForm from '../Components/OperationalTools/CostTrackingForm';
import FieldActivityTrackingForm from '../Components/OperationalTools/FieldActivityTrackingForm';
import ProductRegistrationForm from '../Components/ProductTesting/ProductRegistrationForm';
import EvidenceUploadForm from '../Components/ProductTesting/EvidenceUploadForm';
import TestSchedulingForm from '../Components/ProductTesting/TestSchedulingForm';
import HistoricalDataForm from '../Components/ProductTesting/HistoricalDataForm';
import DataVisualizationForm from '../Components/DataAnalyticsAndReporting/DataVisualizationForm';
import ResultsComparisonForm from '../Components/ProductTesting/ResultsComparisonForm';
import AddInventoryItemForm from '../Components/InventoryManagement/AddInventoryItemForm';
import StockValuationForm from '../Components/InventoryManagement/StockValuationForm';
import ExpiryAlertSetupForm from '../Components/InventoryManagement/ExpiryAlertSetupForm';
import CustomReportBuilderForm from '../Components/DataAnalyticsAndReporting/CustomReportBuilderForm';
import ForecastingForm from '../Components/DataAnalyticsAndReporting/ForecastingForm';
import ProtocolRegistrationForm from '../Components/ProtocolRegistrationForm/ProtocolRegistrationForm';
import AlertConfigurationForm from '../Components/AlertConfigurationForm/AlertConfigurationForm';
import FeedbackCollectionForm from '../Components/FeedbackCollectionForm/FeedbackCollectionForm';
import BroadcastMessageForm from '../Components/CommunicationAndNotifications/BroadcastMessageForm';
import ResourceAllocationForm from '../Components/OperationalTools/ResourceAllocationForm';
import TimeTrackingForm from '../Components/OperationalTools/TimeTrackingForm';
import RealTimeStockTrackingForm from '../Components/InventoryManagement/RealTimeStockTrackingForm';
import ProductManagementForm from '../Components/InventoryManagement/ProductManagementForm';
import QualityControlForm from '../Components/InventoryManagement/QualityControlForm';
import EffectivenessEvaluationForm from '../Components/InventoryManagement/EffectivenessEvaluationForm';
import ReportSchedulerForm from '../Components/InventoryManagement/ReportSchedulerForm';
import RoleManagementForm from '../Components/UserManagement/RoleManagementForm';
import SMSNotificationForm from '../Components/CommunicationAndNotifications/SMSNotificationForm';
import ReminderSystemForm from '../Components/CommunicationAndNotifications/ReminderSystemForm';
import StatusUpdateForm from '../Components/CommunicationAndNotifications/StatusUpdateForm';
import EmergencyAlertForm from '../Components/CommunicationAndNotifications/EmergencyAlertForm';
import EquipmentMaintenanceSchedulingForm from '../Components/OperationalTools/EquipmentMaintenanceSchedulingForm';
import TaskSchedulingForm from '../Components/OperationalTools/TaskSchedulingForm';
import axios from 'axios';
import EvidenceListTable from '../Components/ProductTesting/EvidenceListTable';
import HistoricalDataList from '../Components/ProductTesting/HistoricalDataList'; // Import HistoricalDataList
import ComplianceChecklistTable from '../Pages/ComplianceChecklist/ComplianceChecklistTable';
import AllDataTable from '../Components/AllDataTable/AllDataTable'; // Import the new AllDataTable component

const Dashboard = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuth();
  const [isLoading, setIsLoading] = useState(true);
  const [userInfo, setUserInfo] = useState(null);
  
  // Function to handle user logout
  const handleLogout = async () => {
    try {
      await logout(); // Call the logout function from AuthContext
      toast.success('Logged out successfully!');
      navigate('/'); // Redirect to login page after logout
    } catch (error) {
      console.error('Error during logout:', error);
      toast.error('Failed to log out.');
    }
  };

  // Function to toggle sidebar collapse
  const toggleSidebar = () => {
    setSidebarCollapsed(!sidebarCollapsed);
  };

  // Menu state variables
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const [adminOpen, setAdminOpen] = useState(false);
  const [userDropdownOpen, setUserDropdownOpen] = useState(false);
  
  // State for Evidence List table
  const [showEvidenceList, setShowEvidenceList] = useState(false);

  // Sidebar menu open/closed states
  const [productTestingOpen, setProductTestingOpen] = useState(true);
  const [complianceChecklistOpen, setComplianceChecklistOpen] = useState(false);
  const [inventoryManagementOpen, setInventoryManagementOpen] = useState(false);
  const [dataAnalyticsOpen, setDataAnalyticsOpen] = useState(false);
  const [userManagementOpen, setUserManagementOpen] = useState(false);
  const [communicationOpen, setCommunicationOpen] = useState(false);
  const [operationalToolsOpen, setOperationalToolsOpen] = useState(false);
  
  // Quality Control and Compliance sidebar states
  const [qualityControlOpen, setQualityControlOpen] = useState(false);
  const [complianceSubMenuOpenNew, setComplianceSubMenuOpenNew] = useState(false);
  
  // Compliance Checklist form state
  const [showComplianceForm, setShowComplianceForm] = useState(false);
  const [complianceMode, setComplianceMode] = useState('list'); // list, create, view
  const [showComplianceChecklistTable, setShowComplianceChecklistTable] = useState(false); // New state for ComplianceChecklistTable
  const [showAllDataTable, setShowAllDataTable] = useState(false); // New state for View All Data table
  
  // Test Case form state
  const [showTestCaseForm, setShowTestCaseForm] = useState(false);
  const [testCaseMode, setTestCaseMode] = useState('list'); // list, create, view
  const [showTestCasesTable, setShowTestCasesTable] = useState(false); // New state for Test Cases table
  
  // Submenu open/closed states
  const [testCaseSubMenuOpen, setTestCaseSubMenuOpen] = useState(true);
  const [complianceSubMenuOpen, setComplianceSubMenuOpen] = useState(false);
  const [inventorySubMenuOpen, setInventorySubMenuOpen] = useState(false);
  const [reportSubMenuOpen, setReportSubMenuOpen] = useState(false);
  const [userSecuritySubMenuOpen, setUserSecuritySubMenuOpen] = useState(false);
  const [notificationsSubMenuOpen, setNotificationsSubMenuOpen] = useState(false);
  const [calendarSubMenuOpen, setCalendarSubMenuOpen] = useState(false);

  // State to track the currently active menu item for highlighting
  const [activeMenuItem, setActiveMenuItem] = useState('');

  // Create a ref for the ProductRegistrationForm
  const productRegistrationFormRef = useRef(null);

  // Filter states
  const [activeFilter, setActiveFilter] = useState('all');
  const [filterValue, setFilterValue] = useState('');
  const [configFilter, setConfigFilter] = useState('all');
  const [configValue, setConfigValue] = useState('');
  const [activeTab, setActiveTab] = useState('soil');
  const [soilTests, setSoilTests] = useState([]);
  const [waterTests, setWaterTests] = useState([]);
  const [equipment, setEquipment] = useState([]);
  const [scheduledTests, setScheduledTests] = useState([]);
  
  // State to store test cases
  const [testCases, setTestCases] = useState([]);
  
  // Animated stat counters
  const [animatedSoilCount, setAnimatedSoilCount] = useState(0);
  const [animatedWaterCount, setAnimatedWaterCount] = useState(0);
  const [animatedEquipmentCount, setAnimatedEquipmentCount] = useState(0);
  const [animatedScheduledCount, setAnimatedScheduledCount] = useState(0);
  
  // State to store total test case count
  const [totalTestCases, setTotalTestCases] = useState(0);
  // Add state to store failed tests count
  const [failedTestCount, setFailedTestCount] = useState(0);
  // Add state to store successful tests count
  const [successfulTestCount, setSuccessfulTestCount] = useState(0);
  
  // Track when numbers reach their maximum
  const [isSoilMax, setIsSoilMax] = useState(false);
  const [isWaterMax, setIsWaterMax] = useState(false);
  const [isEquipmentMax, setIsEquipmentMax] = useState(false);
  const [isScheduledMax, setIsScheduledMax] = useState(false);
  
  // Add notification state
  const [notificationCount, setNotificationCount] = useState(3);
  const [notificationsOpen, setNotificationsOpen] = useState(false);
  
  // Sample notifications
  const [notifications, setNotifications] = useState([
    { id: 1, message: "New soil test results are available", time: "10 min ago", read: false },
    { id: 2, message: "Equipment calibration due tomorrow", time: "1 hour ago", read: false },
    { id: 3, message: "Water test report has been updated", time: "2 hours ago", read: false }
  ]);
  
  // Trial Phase form state
  const [showTrialPhaseForm, setShowTrialPhaseForm] = useState(false);
  const [trialPhaseMode, setTrialPhaseMode] = useState('list'); // list, create, view
  const [viewedTrialPhaseData, setViewedTrialPhaseData] = useState(null);
  
  // State to store trial phases
  const [trialPhases, setTrialPhases] = useState([]);
  
  // State to store trial phase images
  const [trialPhaseImages, setTrialPhaseImages] = useState({});
  
  // Pagination state for trial phases
  const [trialPhaseCurrentPage, setTrialPhaseCurrentPage] = useState(1);
  const trialPhaseItemsPerPage = 3;
  
  // Function to fetch trial phases
  const fetchTrialPhases = async () => {
    console.log("Attempting to fetch trial phases...");
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        console.error('Authentication token not found.');
        toast.error('You are not authenticated. Please log in.');
        return;
      }

      const response = await axios.get('http://localhost:8888/api/test-case-trial-phases', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log("API Response data:", response.data);
      setTrialPhases(response.data);
      console.log("Trial phases state after setting:", response.data);
    } catch (error) {
      console.error('Error fetching trial phases:', error);
      toast.error('Failed to fetch trial phases.');
    }
  };

  // Fetch trial phases when the component mounts or trialPhaseMode changes to list
  useEffect(() => {
    if (trialPhaseMode === 'list') {
      fetchTrialPhases();
    }
  }, [trialPhaseMode]);

  // Add a console log for trialPhases whenever it changes
  useEffect(() => {
    console.log("Current trialPhases state:", trialPhases);
    console.log("Number of trial phases:", trialPhases.length);
  }, [trialPhases]);

  // Add a console log for pagination slicing
  useEffect(() => {
    const indexOfLastTrialPhase = trialPhaseCurrentPage * trialPhaseItemsPerPage;
    const indexOfFirstTrialPhase = indexOfLastTrialPhase - trialPhaseItemsPerPage;
    const currentTrialPhases = trialPhases.slice(indexOfFirstTrialPhase, indexOfLastTrialPhase);
    console.log("Pagination slicing - First index:", indexOfFirstTrialPhase, "Last index:", indexOfLastTrialPhase, "Current sliced phases:", currentTrialPhases);
  }, [trialPhases, trialPhaseCurrentPage, trialPhaseItemsPerPage]);
  
  // Test Documentation form state
  const [showTestDocumentationForm, setShowTestDocumentationForm] = useState(false);
  
  // Product Entry form state
  const [showProductEntryForm, setShowProductEntryForm] = useState(false);
  
  // Stock Movement form state
  const [showStockMovementForm, setShowStockMovementForm] = useState(false);
  
  // Stock Monitoring form state
  const [showStockMonitoringForm, setShowStockMonitoringForm] = useState(false);
  
  // Report Generation form state
  const [showReportGenerationForm, setShowReportGenerationForm] = useState(false);
  
  // Performance Analysis form state
  const [showPerformanceAnalysisForm, setShowPerformanceAnalysisForm] = useState(false);
  
  // Quality Incident Report form state
  // const [showQualityIncidentReportForm, setShowQualityIncidentReportForm] = useState(false);
  
  // Broadcast Announcement form state
  const [showBroadcastAnnouncementForm, setShowBroadcastAnnouncementForm] = useState(false);
  
  // Task Assignment form state
  const [showTaskAssignmentForm, setShowTaskAssignmentForm] = useState(false);
  
  // Automated Alert form state
  const [showAutomatedAlertForm, setShowAutomatedAlertForm] = useState(false);
  
  // Calendar Management form state
  const [showCalendarManagementForm, setShowCalendarManagementForm] = useState(false);
  const [showScheduleTasksTable, setShowScheduleTasksTable] = useState(false);
  
  // User Activity Log form state
  const [showUserActivityLogForm, setShowUserActivityLogForm] = useState(false);
  
  // Audit Trail form state
  const [showAuditTrailForm, setShowAuditTrailForm] = useState(false);
  
  // Password Policies form state
  const [showPasswordPoliciesForm, setShowPasswordPoliciesForm] = useState(false);
  
  // Cost Tracking form state
  const [showCostTrackingForm, setShowCostTrackingForm] = useState(false);
  
  // Field Activity Tracking form state
  const [showFieldActivityTrackingForm, setShowFieldActivityTrackingForm] = useState(false);
  
  // Product Registration form state
  const [showProductRegistrationForm, setShowProductRegistrationForm] = useState(false);
  
  // Evidence Upload form state
  const [showEvidenceUploadForm, setShowEvidenceUploadForm] = useState(false);
  
  // Test Scheduling form state
  const [showTestSchedulingForm, setShowTestSchedulingForm] = useState(false);
  
  // Historical Data form state
  const [showHistoricalDataForm, setShowHistoricalDataForm] = useState(false);
  
  // Data Visualization form state
  const [showDataVisualizationForm, setShowDataVisualizationForm] = useState(false);
  
  // Results Comparison form state
  const [showResultsComparisonForm, setShowResultsComparisonForm] = useState(false);
  
  // Add Inventory Item form state
  const [showAddInventoryItemForm, setShowAddInventoryItemForm] = useState(false);
  
  // Stock Valuation form state
  const [showStockValuationForm, setShowStockValuationForm] = useState(false);
  
  // Expiry Alert Setup form state
  const [showExpiryAlertSetupForm, setShowExpiryAlertSetupForm] = useState(false);
  
  // Custom Report Builder form state
  const [showCustomReportBuilderForm, setShowCustomReportBuilderForm] = useState(false);
  
  // Forecasting form state
  const [showForecastingForm, setShowForecastingForm] = useState(false);
  
  // Protocol Registration form state
  const [showProtocolRegistrationForm, setShowProtocolRegistrationForm] = useState(false);
  
  // Alert Configuration form state
  const [showAlertConfigurationForm, setShowAlertConfigurationForm] = useState(false);
  
  // Feedback Collection form state
  const [showFeedbackCollectionForm, setShowFeedbackCollectionForm] = useState(false);
  
  // Broadcast Message form state
  const [showBroadcastMessageForm, setShowBroadcastMessageForm] = useState(false);
  
  // Resource Allocation form state
  const [showResourceAllocationForm, setShowResourceAllocationForm] = useState(false);
  
  // Time Tracking form state
  const [showTimeTrackingForm, setShowTimeTrackingForm] = useState(false);
  
  // Real-time Stock Tracking form state
  const [showRealTimeStockTrackingForm, setShowRealTimeStockTrackingForm] = useState(false);
  
  // Product Management form state
  const [showProductManagementForm, setShowProductManagementForm] = useState(false);
  
  // Quality Control form state
  const [showQualityControlForm, setShowQualityControlForm] = useState(false);
  
  // Effectiveness Evaluation form state
  const [showEffectivenessEvaluationForm, setShowEffectivenessEvaluationForm] = useState(false);
  
  // Report Scheduler form state
  const [showReportSchedulerForm, setShowReportSchedulerForm] = useState(false);
  
  // Role Management form state
  const [showRoleManagementForm, setShowRoleManagementForm] = useState(false);
  
  // SMS Notification form state
  const [showSMSNotificationForm, setShowSMSNotificationForm] = useState(false);
  
  // Reminder System form state
  const [showReminderSystemForm, setShowReminderSystemForm] = useState(false);
  
  // Status Update form state
  const [showStatusUpdateForm, setShowStatusUpdateForm] = useState(false);
  
  // Emergency Alert form state
  const [showEmergencyAlertForm, setShowEmergencyAlertForm] = useState(false);
  
  // Equipment Maintenance Scheduling form state
  const [showEquipmentMaintenanceSchedulingForm, setShowEquipmentMaintenanceSchedulingForm] = useState(false);
  
  // Task Scheduling form state
  const [showTaskSchedulingForm, setShowTaskSchedulingForm] = useState(false);
  
  // State for Test Scheduling List visibility
  const [showTestSchedulingList, setShowTestSchedulingList] = useState(false);
  
  // State to store test schedules
  const [testSchedules, setTestSchedules] = useState([]);
  const [loadingSchedules, setLoadingSchedules] = useState(false);
  const [schedulesError, setSchedulesError] = useState(null);
  
  // Pagination state for test schedules
  const [schedulePage, setSchedulePage] = useState(1);
  const schedulesPerPage = 3; // Number of items per page for test schedules
  
  // State for search query and filtered schedules
  const [searchQuery, setSearchQuery] = useState('');
  const [filteredSchedules, setFilteredSchedules] = useState([]);
  
  // Toggle notifications dropdown
  const toggleNotifications = () => {
    setNotificationsOpen(!notificationsOpen);
    // Close other dropdowns
    if (!notificationsOpen) {
      setAdminOpen(false);
      setUserDropdownOpen(false);
    }
  };
  
  // Mark all notifications as read
  const markAllAsRead = () => {
    const updatedNotifications = notifications.map(notification => ({
      ...notification,
      read: true
    }));
    setNotifications(updatedNotifications);
    setNotificationCount(0);
  };

  // Function to render status badges
  const renderStatusBadge = (status) => {
    let className = 'status-badge';
    if (status === 'Completed') {
      className += ' status-completed';
    } else if (status === 'Pending') {
      className += ' status-pending';
    } else if (status === 'Attention') {
      className += ' status-attention';
    }
    return <span className={className}>{status}</span>;
  };

  // Function to render pagination controls
  const renderPagination = (currentPage, totalPages, setPage) => {
    if (totalPages <= 1) return null;

    const pageNumbers = [];
    for (let i = 1; i <= totalPages; i++) {
      pageNumbers.push(i);
    }

    return (
      <div className="pagination-controls">
        <button onClick={() => setPage(1)} disabled={currentPage === 1}>&laquo;</button>
        <button onClick={() => setPage(currentPage - 1)} disabled={currentPage === 1}>&lt;</button>
        {pageNumbers.map(number => (
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
  
  // Animated stat counters for soilTests (Completed Tests)
  useEffect(() => {
    if (totalTestCases > 0) {
      let count = 0;
      const finalValue = totalTestCases;
      const speed = 900; // Slightly slower

      const animateCounter = () => {
        if (count < finalValue) {
          count++;
          setAnimatedSoilCount(count); // Update for soil tests
          setTimeout(animateCounter, speed);
        } else {
          setIsSoilMax(true);
        }
      };

      const startDelay = setTimeout(animateCounter, 200); // Initial delay

      return () => clearTimeout(startDelay);
    } else {
      setAnimatedSoilCount(0);
      setIsSoilMax(false);
    }
  }, [totalTestCases]); // Depend on totalTestCases

  // Animated stat counters for waterTests (Successful Tests)
  useEffect(() => {
    if (successfulTestCount > 0) {
      let count = 0;
      const finalValue = successfulTestCount;
      const speed = 800; // Medium speed

      const animateCounter = () => {
        if (count < finalValue) {
          count++;
          setAnimatedWaterCount(count); // Update for water tests
          setTimeout(animateCounter, speed);
        } else {
          setIsWaterMax(true);
        }
      };

      const startDelay = setTimeout(animateCounter, 400); // Initial delay

      return () => clearTimeout(startDelay);
    } else {
      setAnimatedWaterCount(0);
      setIsWaterMax(false);
    }
  }, [successfulTestCount]); // Depend on successfulTestCount

  // Animated stat counters for equipment (Failed Tests)
  useEffect(() => {
    if (failedTestCount > 0) {
      let count = 0;
      const finalValue = failedTestCount;
      const speed = 700; // Faster speed

      const animateCounter = () => {
        if (count < finalValue) {
          count++;
          setAnimatedEquipmentCount(count); // Update for equipment
          setTimeout(animateCounter, speed);
        } else {
          setIsEquipmentMax(true);
        }
      };

      const startDelay = setTimeout(animateCounter, 600); // Initial delay

      return () => clearTimeout(startDelay);
    } else {
      setAnimatedEquipmentCount(0);
      setIsEquipmentMax(false);
    }
  }, [failedTestCount]); // Depend on failedTestCount

  // Animated stat counters for scheduledTests
  useEffect(() => {
    if (scheduledTests.length > 0) {
      let count = 0;
      const finalValue = scheduledTests.length;
      const speed = 650; // Fastest speed

      const animateCounter = () => {
        if (count < finalValue) {
          count++;
          setAnimatedScheduledCount(count);
          setTimeout(animateCounter, speed);
        } else {
          setIsScheduledMax(true);
        }
      };

      const startDelay = setTimeout(animateCounter, 800); // Initial delay

      return () => clearTimeout(startDelay);
    } else {
      setAnimatedScheduledCount(0);
      setIsScheduledMax(false);
    }
  }, [scheduledTests]);

  // Effect to handle URL changes and set the trialPhaseMode and fetch data
  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    const formMode = queryParams.get('TrialPhaseTrackingForm');
    const allDataMode = queryParams.get('AllData'); // Get AllData parameter
    const trialPhaseId = queryParams.get('id');
    const showResourceAllocation = queryParams.get('ResourceAllocationForm'); // Resource Allocation Form parameter
    const showQualityIncidentReport = queryParams.get('QualityIncidentReportForm'); // Quality Incident Report Form parameter

    // Reset all show states to false initially to ensure only one form is shown at a time
    setShowComplianceForm(false);
    setShowTestCaseForm(false);
    setShowTrialPhaseForm(false);
    setShowTestDocumentationForm(false);
    setShowProductEntryForm(false);
    setShowStockMovementForm(false);
    setShowStockMonitoringForm(false);
    setShowReportGenerationForm(false);
    setShowPerformanceAnalysisForm(false);
    // setShowQualityIncidentReportForm(false);
    setShowBroadcastAnnouncementForm(false);
    setShowTaskAssignmentForm(false);
    setShowAutomatedAlertForm(false);
    setShowCalendarManagementForm(false);
    setShowUserActivityLogForm(false);
    setShowAuditTrailForm(false);
    setShowPasswordPoliciesForm(false);
    setShowCostTrackingForm(false);
    setShowFieldActivityTrackingForm(false);
    setShowProductRegistrationForm(false);
    setShowEvidenceUploadForm(false);
    setShowTestSchedulingForm(false);
    setShowHistoricalDataForm(false);
    setShowDataVisualizationForm(false);
    setShowResultsComparisonForm(false);
    setShowAddInventoryItemForm(false);
    setShowStockValuationForm(false);
    setShowExpiryAlertSetupForm(false);
    setShowCustomReportBuilderForm(false);
    setShowForecastingForm(false);
    setShowProtocolRegistrationForm(false);
    setShowAlertConfigurationForm(false);
    setShowFeedbackCollectionForm(false);
    setShowBroadcastMessageForm(false);
    setShowResourceAllocationForm(false);
    setShowTimeTrackingForm(false);
    setShowRealTimeStockTrackingForm(false);
    setShowProductManagementForm(false);
    setShowQualityControlForm(false);
    setShowEffectivenessEvaluationForm(false);
    setShowReportSchedulerForm(false);
    setShowRoleManagementForm(false);
    setShowSMSNotificationForm(false);
    setShowReminderSystemForm(false);
    setShowStatusUpdateForm(false);
    setShowEmergencyAlertForm(false);
    setShowEquipmentMaintenanceSchedulingForm(false);
    setShowTaskSchedulingForm(false);
    setShowComplianceChecklistTable(false); // Reset ComplianceChecklistTable
    setShowAllDataTable(false); // Reset AllDataTable
    setShowTestCasesTable(false); // Reset Test Cases table

    if (formMode) {
      setTrialPhaseMode(formMode);
      setShowTrialPhaseForm(true); // Show TrialPhaseForm if formMode exists
      if (formMode === 'view' && trialPhaseId) {
        // Fetch specific trial phase data
        const fetchTrialPhaseData = async () => {
          setIsLoading(true);
          const token = authService.getToken();
          if (token) {
            try {
              const response = await fetch(`http://localhost:8089/api/test-case-trial-phases/${trialPhaseId}`, {
                headers: {
                  'Authorization': `Bearer ${token}`
                }
              });
              if (!response.ok) {
                console.error('Failed to fetch trial phase data:', response.status, response.statusText);
                throw new Error('Failed to fetch trial phase data');
              }
              const data = await response.json();
              console.log('Fetched trial phase data for view:', data);
              setViewedTrialPhaseData(data);
            } catch (error) {
              console.error('Error fetching trial phase data for view:', error);
              toast.error('Failed to load trial phase data.');
              setViewedTrialPhaseData(null); // Clear data on error
            } finally {
              setIsLoading(false);
            }
          }
        };
        fetchTrialPhaseData();
      } else if (showResourceAllocation === 'create') { // Handle ResourceAllocationForm=create parameter
        setShowResourceAllocationForm(true); // Show ResourceAllocationForm
        setActiveTab('resourceallocationform'); // Set activeTab for Resource Allocation Form
      } else if (showQualityIncidentReport === 'create') { // Handle QualityIncidentReportForm=create parameter
        // setShowQualityIncidentReportForm(true); // Show QualityIncidentReportForm
        // setActiveTab('qualityincidentreportform'); // Set activeTab for Quality Incident Report Form
      } else {
        setTrialPhaseMode('list');
        setViewedTrialPhaseData(null); // Clear data if no formMode param
      }
    } else if (allDataMode === 'list') { // Handle AllData=list parameter
      setShowAllDataTable(true); // Show AllDataTable component
    } else if (showResourceAllocation === 'create') { // Handle ResourceAllocationForm=create parameter
      setShowResourceAllocationForm(true); // Show ResourceAllocationForm
      setActiveTab('resourceallocationform'); // Set activeTab for Resource Allocation Form
    } else {
      setTrialPhaseMode('list');
      setViewedTrialPhaseData(null); // Clear data if no formMode param
    }
  }, [location.search, navigate, authService]); // Depend on location.search and navigate to react to URL changes

  // Effect to fetch test schedules when the tab is active
  useEffect(() => {
    const fetchTestSchedules = async () => {
      if (activeTab === 'testschedulinglist') {
        setLoadingSchedules(true);
        setSchedulesError(null);
        const token = authService.getToken(); // Assuming authService.getToken() exists
        if (!token) {
          setSchedulesError('Authentication token not found. Cannot fetch test schedules.');
          setLoadingSchedules(false);
          return;
        }

        try {
          // Replace with your actual backend endpoint for fetching test schedules
          const response = await fetch('http://localhost:8089/api/schedules', {
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json',
            },
          });

          if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Failed to fetch test schedules: ${response.status} ${response.statusText} - ${errorText}`);
          }

          const data = await response.json();
          console.log('Fetched test schedules:', data);
          setTestSchedules(data);
        } catch (error) {
          console.error('Error fetching test schedules:', error);
          setSchedulesError(`Error loading test schedules: ${error.message}`);
          setTestSchedules([]); // Clear previous data on error
        } finally {
          setLoadingSchedules(false);
        }
      }
    };

    fetchTestSchedules();
  }, [activeTab, authService]); // Depend on activeTab to trigger fetch

  // Filter test schedules based on search query
  useEffect(() => {
    const lowerCaseQuery = searchQuery.toLowerCase();
    const filtered = testSchedules.filter(schedule => {
      // Check if schedule or schedule.testCase is null/undefined before accessing properties
      const testName = schedule.testCase?.testName?.toLowerCase() || '';
      const frequency = schedule.frequency?.toLowerCase() || '';
      const location = schedule.location?.toLowerCase() || '';
      const assignedPersonnel = schedule.assignedPersonnel?.toLowerCase() || '';

      return (
        testName.includes(lowerCaseQuery) ||
        frequency.includes(lowerCaseQuery) ||
        location.includes(lowerCaseQuery) ||
        assignedPersonnel.includes(lowerCaseQuery)
      );
    });
    setFilteredSchedules(filtered);
  }, [searchQuery, testSchedules]); // Re-run filter when query or schedules change

  // State and functions for product management in "Product List" tab
  const productsPerPage = 5; // Number of products to display per page
  const [productPage, setProductPage] = useState(1);
  const [productSearchTerm, setProductSearchTerm] = useState('');
  const [editingProduct, setEditingProduct] = useState(null); // State to hold product being edited
  const [userFullNames, setUserFullNames] = useState({}); // To store full names by ID
  const [productImages, setProductImages] = useState({}); // To store image URLs

  const fetchProducts = async () => {
    // This is a placeholder. You'll need to fetch actual product data
    // from your backend API here.
    // Example:
    // const response = await axios.get('/api/products');
    // setProducts(response.data);

    // Mock data for demonstration
    const mockProducts = [
      { id: 'prod1', name: 'AgriFertilizer 100', registeredBy: 'user1', productType: 'Fertilizer', tested: true, imageUrl: '/images/fertilizer.jpg' },
      { id: 'prod2', name: 'PestGuard Spray', registeredBy: 'user2', productType: 'Pesticide', tested: false, imageUrl: '/images/pesticide.jpg' },
      { id: 'prod3', name: 'SeedBoost Pro', registeredBy: 'user1', productType: 'Seed Treatment', tested: true, imageUrl: '/images/seed_treatment.jpg' },
      { id: 'prod4', name: 'HarvestMax Enhancer', registeredBy: 'user3', productType: 'Crop Enhancer', tested: true, imageUrl: '/images/crop_enhancer.jpg' },
      { id: 'prod5', name: 'BioPest Control', registeredBy: 'user2', productType: 'Biopesticide', tested: false, imageUrl: '/images/biopesticide.jpg' },
      { id: 'prod6', name: 'SoilRich Organics', registeredBy: 'user1', productType: 'Organic Fertilizer', tested: true, imageUrl: '/images/organic_fertilizer.jpg' },
      // Add more mock products as needed
    ];
    setProducts(mockProducts);

    // Fetch user full names (mock or actual API call)
    const mockUsers = {
      'user1': 'Alice Wonderland',
      'user2': 'Bob Thebuilder',
      'user3': 'Charlie Chaplin'
    };
    setUserFullNames(mockUsers);

    // Fetch product images (mock or actual API call)
    const mockProductImages = {
      'prod1': 'https://via.placeholder.com/30/FF0000/FFFFFF?text=F1',
      'prod2': 'https://via.placeholder.com/30/00FF00/FFFFFF?text=P1',
      'prod3': 'https://via.placeholder.com/30/0000FF/FFFFFF?text=S1',
      'prod4': 'https://via.placeholder.com/30/FFFF00/000000?text=C1',
      'prod5': 'https://via.placeholder.com/30/FF00FF/FFFFFF?text=B1',
      'prod6': 'https://via.placeholder.com/30/00FFFF/000000?text=O1',
    };
    setProductImages(mockProductImages);
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const [products, setProducts] = useState([]); // State to store all products

  const filteredProducts = products.filter(product =>
    product.name.toLowerCase().includes(productSearchTerm.toLowerCase()) ||
    product.productType.toLowerCase().includes(productSearchTerm.toLowerCase()) ||
    (userFullNames[product.registeredBy] || product.registeredBy).toLowerCase().includes(productSearchTerm.toLowerCase())
  );

  const totalProductPages = Math.ceil(filteredProducts.length / productsPerPage);
  const paginatedProducts = filteredProducts.slice(
    (productPage - 1) * productsPerPage,
    productPage * productsPerPage
  );

  const handleProductUpdateSuccess = (updatedProduct) => {
    // Update the products list with the updated product
    setProducts(prevProducts => prevProducts.map(p => (p.id === updatedProduct.id ? updatedProduct : p)));
    setEditingProduct(null); // Exit editing mode
    toast.success('Product updated successfully!');
  };

  const handleCancelEdit = () => {
    setEditingProduct(null); // Exit editing mode without saving
  };

  const handleDeleteProduct = async (productId) => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      // Implement actual API call for deletion here
      // Example:
      // try {
      //   await axios.delete(`/api/products/${productId}`);
      //   setProducts(prevProducts => prevProducts.filter(p => p.id !== productId));
      //   toast.success('Product deleted successfully!');
      // } catch (error) {
      //   console.error('Error deleting product:', error);
      //   toast.error('Failed to delete product.');
      // }

      // Mock deletion
      setProducts(prevProducts => prevProducts.filter(p => p.id !== productId));
      toast.success('Product deleted successfully (mock)!');
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
        const response = await axios.delete(`http://localhost:8089/api/test-case-trial-phases/${id}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.status === 204) { // No Content for successful deletion
          toast.success('Trial phase deleted successfully!');
          // Remove the deleted phase from the state
          setTrialPhases(prevPhases => prevPhases.filter(phase => phase.id !== id));
        } else {
          toast.error('Failed to delete trial phase.');
        }
      } catch (error) {
        console.error('Error deleting trial phase:', error);
        toast.error('Error deleting trial phase.');
      }
    }
  };

  const [selectedRole, setSelectedRole] = useState(''); // For role management
  const [totalCalendarEvents, setTotalCalendarEvents] = useState(0); // New state for total events from calendar

  // Removed: `useHistory` is deprecated and `AuthService` is already imported as an object
  // const history = useHistory();
  // const authService = useMemo(() => new AuthService(), []);

  // The `location` is correctly declared at the top of the component as `const location = useLocation();`

    return (
    <div className="dashboard-container">
      <div className="twilio-header">
        <div className="header-left">
          <button className="home-button">
            <FaHome /> AgriTest Pro Home
          </button>
        </div>
        
        <div className="header-right">
          <div className="notification-container">
            <button className="icon-button" onClick={toggleNotifications}>
              <FaBell />
              {notificationCount > 0 && (
                <span className="notification-badge">{notificationCount}</span>
              )}
            </button>
            
            {notificationsOpen && (
              <div className="notifications-dropdown">
                <div className="notifications-header">
                  <h3>Notifications</h3>
                  {notificationCount > 0 && (
                    <button className="mark-read-button" onClick={markAllAsRead}>
                      Mark all as read
                    </button>
                  )}
                </div>
                
                <div className="notifications-list">
                  {notifications.length > 0 ? (
                    notifications.map(notification => (
                      <div 
                        key={notification.id} 
                        className={`notification-item ${notification.read ? 'read' : 'unread'}`}
                      >
                        <div className="notification-content">
                          <p className="notification-message">{notification.message}</p>
                          <span className="notification-time">{notification.time}</span>
                        </div>
                      </div>
                    ))
                  ) : (
                    <p className="no-notifications">No new notifications</p>
                  )}
                </div>
                
                <div className="notifications-footer">
                  <a href="#" className="view-all-link">View all notifications</a>
                </div>
              </div>
            )}
          </div>
          
          <div className="user-profile-container">
            <button className="icon-button" onClick={() => setUserDropdownOpen(!userDropdownOpen)}>
              <FaUserCircle />
            </button>
            
            {userDropdownOpen && (
              <div className="user-dropdown">
                <div className="dropdown-header">
                  <div className="user-info">
                    <FaUserCircle className="user-icon" />
                    <div className="user-details">
                      <span className="user-name">
                        {userInfo?.username || userInfo?.name || user?.username || user?.name || 'User'}
                      </span>
                      <span className="user-role">
                        {userInfo?.roles?.[0] || userInfo?.role || user?.roles?.[0] || user?.role || 'Role'}
                      </span>
                    </div>
                  </div>
                </div>
                <div className="dropdown-content">
                  <button className="dropdown-item" onClick={handleLogout}>
                    <FaSignOutAlt className="dropdown-icon" /> Logout
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>

      <div className="dashboard-content">
        <div className={`dashboard-sidebar ${sidebarCollapsed ? 'collapsed' : ''}`}>
          {/* 1. PRODUCT TESTING MANAGEMENT */}
          <div className="sidebar-section">
            <div className="section-header" onClick={() => setProductTestingOpen(!productTestingOpen)}>
              {productTestingOpen ? <FaChevronDown className="toggle-icon" /> : <FaChevronRight className="toggle-icon" />}
              <FaFlask className="sidebar-icon" /> 
              <span className="section-title">Product Testing</span>
            </div>
            
            {productTestingOpen && (
              <div className="section-content">
                <div className="sub-section">
                  <div className="sub-header" onClick={() => setTestCaseSubMenuOpen(!testCaseSubMenuOpen)}>
                    {testCaseSubMenuOpen ? <FaChevronDown className="toggle-icon-sub" /> : <FaChevronRight className="toggle-icon-sub" />}
                    <span className="sub-title">Test Management</span>
                  </div>
                  
                  {testCaseSubMenuOpen && (
                    <div className="sub-content">
                      <div className={`menu-item ${activeMenuItem === 'CreatingTestCases' ? 'active' : ''}`} onClick={() => {
                        setShowTestCaseForm(true);
                        setTestCaseMode('create');
                        setActiveTab('testcase');
                        setActiveMenuItem('CreatingTestCases');
                        navigate('/dashboard?CreatingTestCases=create', { replace: true });
                      }}>Creating Test Cases</div>
                      <div className={`menu-item ${activeMenuItem === 'TrialPhaseTrackingForm' ? 'active' : ''}`} onClick={() => {
                        setShowTrialPhaseForm(true);
                        setTrialPhaseMode('create');
                        setActiveTab('trialphase');
                        setActiveMenuItem('TrialPhaseTrackingForm');
                        navigate('/dashboard?TrialPhaseTrackingForm=create', { replace: true });
                      }}>Trial Phase Tracking</div>
                      <div className={`menu-item ${activeMenuItem === 'CreatingTestDocumentation' ? 'active' : ''}`} onClick={() => {
                        setShowTestDocumentationForm(true);
                        setActiveTab('testdocumentation');
                        setActiveMenuItem('CreatingTestDocumentation');
                        navigate('/dashboard?CreatingTestDocumentation=create', { replace: true });
                      }}>Test Documentation</div>
                      <div className={`menu-item ${activeMenuItem === 'ProductRegistrationForm' ? 'active' : ''}`} onClick={() => {
                        setShowProductRegistrationForm(true);
                        setActiveTab('productregistration');
                        setActiveMenuItem('ProductRegistrationForm');
                        navigate('/dashboard?ProductRegistrationForm=create', { replace: true });
                      }}>Product Registration</div>
                      <div className={`menu-item ${activeMenuItem === 'EvidenceUploadForm' ? 'active' : ''}`} onClick={() => {
                        setShowEvidenceUploadForm(true);
                        setActiveTab('evidenceupload');
                        setActiveMenuItem('EvidenceUploadForm');
                        navigate('/dashboard?EvidenceUploadForm=create', { replace: true });
                      }}>Evidence Upload</div>
                      <div className={`menu-item ${activeMenuItem === 'TestSchedulingForm' ? 'active' : ''}`} onClick={() => {
                        setShowTestSchedulingForm(true);
                        setActiveTab('testschedulingform');
                        setActiveMenuItem('TestSchedulingForm');
                        navigate('/dashboard?TestSchedulingForm=create', { replace: true });
                      }}>Test Scheduling</div>
                      <div className={`menu-item ${activeMenuItem === 'HistoricalDataForm' ? 'active' : ''}`} onClick={() => {
                        setShowHistoricalDataForm(true);
                        setActiveTab('historicaldataform');
                        setActiveMenuItem('HistoricalDataForm');
                        navigate('/dashboard?HistoricalDataForm=create', { replace: true });
                      }}>Historical Data</div>
                      
                      <div className={`menu-item ${activeMenuItem === 'ResultsComparisonForm' ? 'active' : ''}`} onClick={() => {
                        setShowResultsComparisonForm(true);
                        setActiveTab('resultscomparisonform');
                        setActiveMenuItem('ResultsComparisonForm');
                        navigate('/dashboard?ResultsComparisonForm=create', { replace: true });
                      }}>Results Comparison</div>
                    
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>

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
                        setComplianceMode('create');
                        setActiveTab('compliance');
                        setActiveMenuItem('ComplianceChecklist');
                        navigate('/dashboard?ComplianceChecklist=create', { replace: true });
                      }}>Create Checklist</div>
                      <div className={`menu-item ${activeMenuItem === 'ComplianceChecklist' && complianceMode === 'list' ? 'active' : ''}`} onClick={() => {
                        setShowComplianceForm(true);
                        setComplianceMode('list');
                        setActiveTab('compliance');
                        setActiveMenuItem('ComplianceChecklist');
                        navigate('/dashboard?ComplianceChecklist=list', { replace: true });
                      }}>View All Checklists</div>
                      
                      <div className={`menu-item ${activeMenuItem === 'ComplianceChecklist' && location.search.includes('recent') ? 'active' : ''}`} onClick={() => {
                        setShowComplianceForm(true);
                        setComplianceMode('list');
                        setActiveTab('compliance');
                        setActiveMenuItem('ComplianceChecklist');
                        navigate('/dashboard?ComplianceChecklist=recent', { replace: true });
                      }}>Recent Compliance</div>
                      <div className={`menu-item ${activeMenuItem === 'ComplianceChecklist' && location.search.includes('by-product') ? 'active' : ''}`} onClick={() => {
                        setShowComplianceForm(true);
                        setComplianceMode('list');
                        setActiveTab('compliance');
                        setActiveMenuItem('ComplianceChecklist');
                        navigate('/dashboard?ComplianceChecklist=by-product', { replace: true });
                      }}>Compliance by Product</div>
                      
                      <div className={`menu-item ${activeMenuItem === 'ComplianceChecklist' && location.search.includes('status') ? 'active' : ''}`} onClick={() => {
                        setShowComplianceForm(true);
                        setComplianceMode('list');
                        setActiveTab('compliance');
                        setActiveMenuItem('ComplianceChecklist');
                        navigate('/dashboard?ComplianceChecklist=status', { replace: true });
                      }}>Compliance Status</div>
                     
                      <div className={`menu-item ${activeMenuItem === 'ComplianceChecklist' && location.search.includes('ComplianceChecklistTable=list') ? 'active' : ''}`} onClick={() => {
                        setShowComplianceForm(false); // Hide other compliance forms
                        setShowComplianceChecklistTable(true); // Show the new table component
                        setComplianceMode(''); // Clear existing compliance mode
                        setActiveTab('compliance');
                        setActiveMenuItem('ComplianceChecklist');
                        navigate('/dashboard?ComplianceChecklistTable=list', { replace: true });
                      }}>View All Checklists</div>
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>

          {/* 2. INVENTORY MANAGEMENT */}
          <div className="sidebar-section">
            <div className="section-header" onClick={() => setInventoryManagementOpen(!inventoryManagementOpen)}>
              {inventoryManagementOpen ? <FaChevronDown className="toggle-icon" /> : <FaChevronRight className="toggle-icon" />}
              <FaWarehouse className="sidebar-icon" /> 
              <span className="section-title">Inventory Management</span>
            </div>
            
            {inventoryManagementOpen && (
              <div className="section-content">
                <div className="sub-section">
                  <div className="sub-header" onClick={() => setInventorySubMenuOpen(!inventorySubMenuOpen)}>
                    {inventorySubMenuOpen ? <FaChevronDown className="toggle-icon-sub" /> : <FaChevronRight className="toggle-icon-sub" />}
                    <span className="sub-title">Stock Control</span>
                  </div>
                  
                  {inventorySubMenuOpen && (
                    <div className="sub-content">
                   
                      <div className={`menu-item ${activeMenuItem === 'StockMovement' ? 'active' : ''}`} onClick={() => {
                        setShowStockMovementForm(true);
                        setActiveTab('stockmovement');
                        setActiveMenuItem('StockMovement');
                        navigate('/dashboard?StockMovement=create', { replace: true });
                      }}>Stock Movement</div>
                      <div className={`menu-item ${activeMenuItem === 'StockMonitoringForm' ? 'active' : ''}`} onClick={() => {
                        setShowStockMonitoringForm(true);
                        setActiveTab('stockmonitoring');
                        setActiveMenuItem('StockMonitoringForm');
                        navigate('/dashboard?StockMonitoringForm=create', { replace: true });
                      }}>Stock Monitoring</div>
                      <div className={`menu-item ${activeMenuItem === 'AddInventoryItemForm' ? 'active' : ''}`} onClick={() => {
                        setShowAddInventoryItemForm(true);
                        setActiveTab('addinventoryitemform');
                        setActiveMenuItem('AddInventoryItemForm');
                        navigate('/dashboard?AddInventoryItemForm=create', { replace: true });
                      }}>Add Inventory Item</div>
                      <div className={`menu-item ${activeMenuItem === 'ExpiryAlertSetupForm' ? 'active' : ''}`} onClick={() => {
                        setShowExpiryAlertSetupForm(true);
                        setActiveTab('expiryalertsetupform');
                        setActiveMenuItem('ExpiryAlertSetupForm');
                        navigate('/dashboard?ExpiryAlertSetupForm=create', { replace: true });
                      }}>Expiration Alerts</div>
                     
                      <div className={`menu-item ${activeMenuItem === 'EffectivenessEvaluationForm' ? 'active' : ''}`} onClick={() => {
                        setShowEffectivenessEvaluationForm(true);
                        setActiveTab('effectivenesseevaluationform');
                        setActiveMenuItem('EffectivenessEvaluationForm');
                        navigate('/dashboard?EffectivenessEvaluationForm=create', { replace: true });
                      }}>Effectiveness Evaluation</div>
                      <div className={`menu-item ${activeMenuItem === 'StockValuationForm' ? 'active' : ''}`} onClick={() => {
                        setShowStockValuationForm(true);
                        setActiveTab('stockvaluationform');
                        setActiveMenuItem('StockValuationForm');
                        navigate('/dashboard?StockValuationForm=create', { replace: true });
                      }}>Stock Valuation</div>
                      <div className={`menu-item ${activeMenuItem === 'ProductManagementForm' ? 'active' : ''}`} onClick={() => {
                        setShowProductManagementForm(true);
                        setActiveTab('productmanagementform');
                        setActiveMenuItem('ProductManagementForm');
                        navigate('/dashboard?ProductManagementForm=create', { replace: true });
                      }}>Product Management</div>
                      <div className={`menu-item ${activeMenuItem === 'ReportSchedulerForm' ? 'active' : ''}`} onClick={() => {
                        setShowReportSchedulerForm(true);
                        setActiveTab('reportschedulerform');
                        setActiveMenuItem('ReportSchedulerForm');
                        navigate('/dashboard?ReportSchedulerForm=create', { replace: true });
                      }}>Report Scheduler</div>
          
                    
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>

          {/* 3. DATA ANALYTICS & REPORTING */}
          <div className="sidebar-section">
            <div className="section-header" onClick={() => setDataAnalyticsOpen(!dataAnalyticsOpen)}>
              {dataAnalyticsOpen ? <FaChevronDown className="toggle-icon" /> : <FaChevronRight className="toggle-icon" />}
              <FaChartLine className="sidebar-icon" /> 
              <span className="section-title">Data Analytics & Reporting</span>
            </div>
            
            {dataAnalyticsOpen && (
              <div className="section-content">
                <div className="sub-section">
                  <div className="sub-header" onClick={() => setReportSubMenuOpen(!reportSubMenuOpen)}>
                    {reportSubMenuOpen ? <FaChevronDown className="toggle-icon-sub" /> : <FaChevronRight className="toggle-icon-sub" />}
                    <span className="sub-title">Reports & Insights</span>
                  </div>
                  
                  {reportSubMenuOpen && (
                    <div className="sub-content">
                      <div className={`menu-item ${activeMenuItem === 'ReportGeneration' ? 'active' : ''}`} onClick={() => {
                        setShowReportGenerationForm(true);
                        setActiveTab('reportgeneration');
                        setActiveMenuItem('ReportGeneration');
                        navigate('/dashboard?ReportGeneration=create', { replace: true });
                      }}>Report Generation Form</div>
                      <div className={`menu-item ${activeMenuItem === 'PerformanceAnalysis' ? 'active' : ''}`} onClick={() => {
                        setShowPerformanceAnalysisForm(true);
                        setActiveTab('performanceanalysis');
                        setActiveMenuItem('PerformanceAnalysis');
                        navigate('/dashboard?PerformanceAnalysis=create', { replace: true });
                      }}>Performance Analysis</div>
                      <div className={`menu-item ${activeMenuItem === 'CustomReportBuilderForm' ? 'active' : ''}`} onClick={() => {
                        setShowCustomReportBuilderForm(true);
                        setActiveTab('customreportbuilderform');
                        setActiveMenuItem('CustomReportBuilderForm');
                        navigate('/dashboard?CustomReportBuilderForm=create', { replace: true });
                      }}>Custom Report Builder</div>
                  
                      <div className="menu-item" onClick={() => {
                        setShowForecastingForm(true);
                        setActiveTab('forecastingform');
                        setActiveMenuItem('ForecastingForm');
                        navigate('/dashboard?ForecastingForm=create', { replace: true });
                      }}>Forecasting</div>
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>

          {/* 4. USER MANAGEMENT & SECURITY */}
          <div className="sidebar-section">
            <div className="section-header" onClick={() => setUserManagementOpen(!userManagementOpen)}>
              {userManagementOpen ? <FaChevronDown className="toggle-icon" /> : <FaChevronRight className="toggle-icon" />}
              <FaUserShield className="sidebar-icon" /> 
              <span className="section-title">User Management & Security</span>
            </div>
            
            {userManagementOpen && (
              <div className="section-content">
                <div className="sub-section">
                  <div className="sub-header" onClick={() => setUserSecuritySubMenuOpen(!userSecuritySubMenuOpen)}>
                    {userSecuritySubMenuOpen ? <FaChevronDown className="toggle-icon-sub" /> : <FaChevronRight className="toggle-icon-sub" />}
                    <span className="sub-title">System Security</span>
                </div>
                
                  {userSecuritySubMenuOpen && (
                    <div className="sub-content">
                      <div className={`menu-item ${activeMenuItem === 'RoleManagementForm' ? 'active' : ''}`} onClick={() => {
                        setShowRoleManagementForm(true);
                        setActiveTab('rolemanagementform');
                        setActiveMenuItem('RoleManagementForm');
                        navigate('/dashboard?RoleManagementForm=create', { replace: true });
                      }}>Role Management</div>
                     
                      <div className={`menu-item ${activeMenuItem === 'UserActivityLogForm' ? 'active' : ''}`} onClick={() => {
                        setShowUserActivityLogForm(true);
                        setActiveTab('useractivitylogform');
                        setActiveMenuItem('UserActivityLogForm');
                        navigate('/dashboard?UserActivityLogForm=create', { replace: true });
                      }}>User Activity Log</div>
                      <div className={`menu-item ${activeMenuItem === 'AuditTrailForm' ? 'active' : ''}`} onClick={() => {
                        setShowAuditTrailForm(true);
                        setActiveTab('audittrailform');
                        setActiveMenuItem('AuditTrailForm');
                        navigate('/dashboard?AuditTrailForm=create', { replace: true });
                      }}>Audit Trails</div>
                     
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>

          {/* 5. COMMUNICATION & NOTIFICATIONS */}
          <div className="sidebar-section">
            <div className="section-header" onClick={() => setCommunicationOpen(!communicationOpen)}>
              {communicationOpen ? <FaChevronDown className="toggle-icon" /> : <FaChevronRight className="toggle-icon" />}
              <FaComments className="sidebar-icon" /> 
              <span className="section-title">Communication & Notifications</span>
            </div>
            
            {communicationOpen && (
              <div className="section-content">
                <div className="sub-section">
                  <div className="sub-header" onClick={() => setNotificationsSubMenuOpen(!notificationsSubMenuOpen)}>
                    {notificationsSubMenuOpen ? <FaChevronDown className="toggle-icon-sub" /> : <FaChevronRight className="toggle-icon-sub" />}
                    <span className="sub-title">Alert System</span>
                    </div>
                    
                  {notificationsSubMenuOpen && (
                    <div className="sub-content">
                      
                      <div className={`menu-item ${activeMenuItem === 'SMSNotificationForm' ? 'active' : ''}`} onClick={() => {
                        setShowSMSNotificationForm(true);
                        setActiveTab('smsnotificationform');
                        setActiveMenuItem('SMSNotificationForm');
                        navigate('/dashboard?SMSNotificationForm=create', { replace: true });
                      }}>SMS Notifications</div>
                    
                      <div className={`menu-item ${activeMenuItem === 'ReminderSystemForm' ? 'active' : ''}`} onClick={() => {
                        setShowReminderSystemForm(true);
                        setActiveTab('remindersystemform');
                        setActiveMenuItem('ReminderSystemForm');
                        navigate('/dashboard?ReminderSystemForm=create', { replace: true });
                      }}>Reminders</div>
                      
                     
                      <div className={`menu-item ${activeMenuItem === 'BroadcastAnnouncementForm' ? 'active' : ''}`} onClick={() => {
                        setShowBroadcastAnnouncementForm(true);
                        setActiveTab('broadcastannouncementform');
                        setActiveMenuItem('BroadcastAnnouncementForm');
                        navigate('/dashboard?BroadcastAnnouncementForm=create', { replace: true });
                      }}>Broadcast Announcement</div>
                     
                      {/* Add Alert Configuration Menu Item */}
                      
                    
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>

          {/* 6. OPERATIONAL TOOLS */}
          <div className="sidebar-section">
            <div className="section-header" onClick={() => setOperationalToolsOpen(!operationalToolsOpen)}>
              {operationalToolsOpen ? <FaChevronDown className="toggle-icon" /> : <FaChevronRight className="toggle-icon" />}
              <FaTasks className="sidebar-icon" /> 
              <span className="section-title">Operational Tools</span>
            </div>
            
            {operationalToolsOpen && (
              <div className="section-content">
                <div className="sub-section">
                  <div className="sub-header" onClick={() => setCalendarSubMenuOpen(!calendarSubMenuOpen)}>
                    {calendarSubMenuOpen ? <FaChevronDown className="toggle-icon-sub" /> : <FaChevronRight className="toggle-icon-sub" />}
                    <span className="sub-title">Daily Operations</span>
                    </div>
                    
                  {calendarSubMenuOpen && (
                    <div className="sub-content">
                      {/* <div className={`menu-item ${activeMenuItem === 'CalendarManagementForm' ? 'active' : ''}`} onClick={() => {
                        setShowCalendarManagementForm(true);
                        setActiveTab('calendarmanagementform');
                        setActiveMenuItem('CalendarManagementForm');
                        navigate('/dashboard?CalendarManagementForm=create', { replace: true });
                      }}>
                        <FaCalendarAlt className="menu-icon" />
                        <span>Calendar Management</span>
                      </div> */}
                      <div className={`menu-item ${activeMenuItem === 'ScheduleTasksTable' ? 'active' : ''}`} onClick={() => {
                        setShowScheduleTasksTable(true);
                        setActiveTab('scheduletaskstable');
                        setActiveMenuItem('ScheduleTasksTable');
                        navigate('/dashboard?ScheduleTasksTable=create', { replace: true });
                      }}>
                        <FaTasks className="menu-icon" />
                        <span>Schedule Tasks</span>
                      </div>
                     
                      <div className={`menu-item ${activeMenuItem === 'FieldActivityTrackingForm' ? 'active' : ''}`} onClick={() => {
                        setShowFieldActivityTrackingForm(true);
                        setActiveTab('fieldactivitytracking');
                        setActiveMenuItem('FieldActivityTrackingForm');
                        navigate('/dashboard?FieldActivityTrackingForm=create', { replace: true });
                      }}>Field Activity Tracking</div>
                     
                   
                      <div className={`menu-item ${activeMenuItem === 'ResourceAllocationForm' ? 'active' : ''}`} onClick={() => {
                        setShowResourceAllocationForm(true);
                        setActiveTab('resourceallocationform');
                        setActiveMenuItem('ResourceAllocationForm');
                        navigate('/dashboard?ResourceAllocationForm=create', { replace: true });
                      }}>Resource Allocation</div>
                      <div className={`menu-item ${activeMenuItem === 'EquipmentMaintenanceSchedulingForm' ? 'active' : ''}`} onClick={() => {
                        setShowEquipmentMaintenanceSchedulingForm(true);
                        setActiveTab('equipmentmaintenanceschedulingform');
                        setActiveMenuItem('EquipmentMaintenanceSchedulingForm');
                        navigate('/dashboard?EquipmentMaintenanceSchedulingForm=create', { replace: true });
                      }}>Equipment Maintenance Scheduling</div>
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>

          {/* 7. QUALITY CONTROL AND COMPLIANCE */}
         

          {/* 7. QUALITY CONTROL AND COMPLIANCE */}
          
        </div>
        
        <button className="collapse-btn" onClick={toggleSidebar}>
          {sidebarCollapsed ? <FaChevronRight /> : <FaChevronLeft />}
        </button>

        <div className="dashboard-main">
          <div className="dashboard-page-header">
            <div className="header-left">
              <h1 className="page-title">Test Results & Equipment</h1>
            </div>
            <div className="header-right">
              <button className="primary-button" onClick={() => alert('New test request')}>
                New Test Request <FaArrowRight />
              </button>
            </div>
          </div>

          <div className="dashboard-stats">
            <div className="stat-card">
              <div className="stat-icon soil-icon">
                <FaLeaf />
              </div>
              <div className="stat-content">
                <h3>Completed Tests</h3>
                <p className={`stat-number ${isEquipmentMax ? 'max-value' : ''}`}>{animatedSoilCount}</p>
                <p className="stat-label">All Phases Passed</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon water-icon">
                <FaWater />
              </div>
              <div className="stat-content">
                <h3>Successful Tests</h3>
                {/* Use totalTestCases for animation */}
                <p className={`stat-number ${isWaterMax ? 'max-value' : ''}`}>{animatedWaterCount}</p>
                <p className="stat-label"></p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon equipment-icon">
                <FaTools />
              </div>
              <div className="stat-content">
                <h3>Failed Tests</h3>{/* Changed from Failed tests */}
                <p className={`stat-number ${isEquipmentMax ? 'max-value' : ''}`}>{animatedEquipmentCount}</p>
                <p className="stat-label"></p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon scheduled-icon">
                <FaCalendarAlt />
              </div>
              <div className="stat-content">
                <h3>Scheduled</h3>
                {/* <p className={`stat-number ${isScheduledMax ? 'max-value' : ''}`}>{animatedScheduledCount}</p> */}
                <p className="stat-number">{totalCalendarEvents}</p>
                <p className="stat-footer">Total events</p>
              </div>
            </div>
          </div>

          <div className="dashboard-tabs">
            {/* Always show relevant tabs */}
            <div 
              className={`dashboard-tab ${activeTab === 'soil' ? 'active' : ''}`}
              onClick={() => {
                setActiveTab('soil');
                // Reset all form/list visibilities
                setShowComplianceForm(false);
                setShowTestCaseForm(false);
                setShowTrialPhaseForm(false);
                setShowTestDocumentationForm(false);
                setShowProductEntryForm(false);
                setShowStockMovementForm(false);
                setShowStockMonitoringForm(false);
                setShowReportGenerationForm(false);
                setShowPerformanceAnalysisForm(false);
                // setShowQualityIncidentReportForm(false);
                setShowBroadcastAnnouncementForm(false);
                setShowTaskAssignmentForm(false);
                setShowAutomatedAlertForm(false);
                setShowCalendarManagementForm(false);
                setShowUserActivityLogForm(false);
                setShowAuditTrailForm(false);
                setShowCostTrackingForm(false);
                setShowFieldActivityTrackingForm(false);
                setShowProductRegistrationForm(false);
                setShowEvidenceUploadForm(false);
                setShowTestSchedulingForm(false);
                setShowHistoricalDataForm(false);
                setShowDataVisualizationForm(false);
                setShowResultsComparisonForm(false);
                setShowAddInventoryItemForm(false);
                setShowStockValuationForm(false);
                setShowExpiryAlertSetupForm(false);
                setShowCustomReportBuilderForm(false);
                setShowForecastingForm(false);
                setShowProtocolRegistrationForm(false);
                setShowAlertConfigurationForm(false);
                setShowFeedbackCollectionForm(false);
                setShowBroadcastMessageForm(false);
                setShowResourceAllocationForm(false);
                setShowTimeTrackingForm(false);
                setShowRealTimeStockTrackingForm(false);
                setShowProductManagementForm(false);
                setShowQualityControlForm(false);
                setShowEffectivenessEvaluationForm(false);
                setShowReportSchedulerForm(false);
                setShowRoleManagementForm(false);
                setShowSMSNotificationForm(false);
                setShowReminderSystemForm(false);
                setShowStatusUpdateForm(false);
                setShowEmergencyAlertForm(false);
                setShowEquipmentMaintenanceSchedulingForm(false);
                setShowTaskSchedulingForm(false);
                setShowEvidenceList(false); // Hide Evidence List
                setShowTestSchedulingList(false); // Hide Test Scheduling List
                // Navigate to default URL or product list URL
                navigate('/dashboard', { replace: true });
              }}
            >
                  <FaLeaf /> Product List
            </div>
            <div 
                  className={`dashboard-tab ${activeTab === 'testschedulinglist' ? 'active' : ''}`} // Updated active tab check
              onClick={() => {
                    setActiveTab('testschedulinglist'); // Set active tab to testschedulinglist
                    setShowTestSchedulingList(true); // Show the list
                    // Hide all other forms/lists
                setShowComplianceForm(false);
                    setShowTestCaseForm(false);
                    setShowTrialPhaseForm(false);
                setShowTestDocumentationForm(false);
                    setShowProductEntryForm(false);
                    setShowStockMovementForm(false);
                    setShowStockMonitoringForm(false);
                    setShowReportGenerationForm(false);
                    setShowPerformanceAnalysisForm(false);
                    // setShowQualityIncidentReportForm(false);
                    setShowBroadcastAnnouncementForm(false);
                    setShowTaskAssignmentForm(false);
                    setShowAutomatedAlertForm(false);
                    setShowCalendarManagementForm(false);
                    setShowUserActivityLogForm(false);
                    setShowAuditTrailForm(false);
                    setShowCostTrackingForm(false);
                    setShowFieldActivityTrackingForm(false);
                    setShowProductRegistrationForm(false);
                    setShowEvidenceUploadForm(false);
                    setShowTestSchedulingForm(false);
                    setShowHistoricalDataForm(false);
                    setShowDataVisualizationForm(false);
                    setShowResultsComparisonForm(false);
                    setShowAddInventoryItemForm(false);
                    setShowStockValuationForm(false);
                    setShowExpiryAlertSetupForm(false);
                    setShowCustomReportBuilderForm(false);
                    setShowForecastingForm(false);
                    setShowProtocolRegistrationForm(false);
                setShowAlertConfigurationForm(false);
                    setShowFeedbackCollectionForm(false);
                    setShowBroadcastMessageForm(false);
                    setShowResourceAllocationForm(false);
                    setShowTimeTrackingForm(false);
                    setShowRealTimeStockTrackingForm(false);
                    setShowProductManagementForm(false);
                    setShowQualityControlForm(false);
                    setShowEffectivenessEvaluationForm(false);
                    setShowReportSchedulerForm(false);
                    setShowRoleManagementForm(false);
                    setShowSMSNotificationForm(false);
                    setShowReminderSystemForm(false);
                    setShowStatusUpdateForm(false);
                    setShowEmergencyAlertForm(false);
                    setShowEquipmentMaintenanceSchedulingForm(false);
                    setShowTaskSchedulingForm(false);
                    setShowEvidenceList(false); // Hide Evidence List

                    navigate('/dashboard?TestSchedulingForm=list', { replace: true }); // Navigate to the list URL
              }}
            >
                  <FaCalendarAlt /> List Test Scheduling
            </div>
            <div 
                  className={`dashboard-tab ${activeTab === 'testcase' && testCaseMode === 'list' ? 'active' : ''}`}
              onClick={() => {
                    setActiveTab('testcase');
                    setTestCaseMode('list');
                    // Hide all other forms/lists
                    setShowComplianceForm(false);
                    setShowTrialPhaseForm(false);
                    setShowTestDocumentationForm(false);
                    setShowProductEntryForm(false);
                    setShowStockMovementForm(false);
                    setShowStockMonitoringForm(false);
                    setShowReportGenerationForm(false);
                    setShowPerformanceAnalysisForm(false);
                    // setShowQualityIncidentReportForm(false);
                    setShowBroadcastAnnouncementForm(false);
                    setShowTaskAssignmentForm(false);
                    setShowAutomatedAlertForm(false);
                    setShowCalendarManagementForm(false);
                    setShowUserActivityLogForm(false);
                    setShowAuditTrailForm(false);
                    setShowCostTrackingForm(false);
                    setShowFieldActivityTrackingForm(false);
                    setShowProductRegistrationForm(false);
                    setShowEvidenceUploadForm(false);
                    setShowTestSchedulingForm(false);
                    setShowHistoricalDataForm(false);
                    setShowDataVisualizationForm(false);
                    setShowResultsComparisonForm(false);
                    setShowAddInventoryItemForm(false);
                    setShowStockValuationForm(false);
                    setShowExpiryAlertSetupForm(false);
                    setShowCustomReportBuilderForm(false);
                    setShowForecastingForm(false);
                    setShowProtocolRegistrationForm(false);
                    setShowAlertConfigurationForm(false);
                    setShowFeedbackCollectionForm(false);
                    setShowBroadcastMessageForm(false);
                    setShowResourceAllocationForm(false);
                    setShowTimeTrackingForm(false);
                    setShowRealTimeStockTrackingForm(false);
                    setShowProductManagementForm(false);
                    setShowQualityControlForm(false);
                    setShowEffectivenessEvaluationForm(false);
                    setShowReportSchedulerForm(false);
                    setShowRoleManagementForm(false);
                    setShowSMSNotificationForm(false);
                    setShowReminderSystemForm(false);
                    setShowStatusUpdateForm(false);
                    setShowEmergencyAlertForm(false);
                    setShowEquipmentMaintenanceSchedulingForm(false);
                    setShowTaskSchedulingForm(false);
                    setShowEvidenceList(false); // Hide Evidence List
                    setShowTestSchedulingList(false); // Hide Test Scheduling List

                    navigate('/dashboard?CreatingTestCases=list', { replace: true });
                  }}
                >
                  <FaClipboardCheck /> List Test Cases
                </div>
                 {/* Add Test Documentation tab */}
                 <div
                  className={`dashboard-tab ${activeMenuItem === 'testdocumentation' ? 'active' : ''}`}
                  onClick={() => {
                    setActiveTab('testdocumentation');
                    setShowTestDocumentationForm(true); // Show Test Documentation Form
                    // Hide all other forms/lists
                setShowComplianceForm(false);
                    setShowTestCaseForm(false);
                    setShowTrialPhaseForm(false);
                    setShowProductEntryForm(false);
                    setShowStockMovementForm(false);
                    setShowStockMonitoringForm(false);
                    setShowReportGenerationForm(false);
                    setShowPerformanceAnalysisForm(false);
                    // setShowQualityIncidentReportForm(false);
                    setShowBroadcastAnnouncementForm(false);
                    setShowTaskAssignmentForm(false);
                    setShowAutomatedAlertForm(false);
                    setShowCalendarManagementForm(false);
                    setShowUserActivityLogForm(false);
                    setShowAuditTrailForm(false);
                    setShowCostTrackingForm(false);
                    setShowFieldActivityTrackingForm(false);
                    setShowProductRegistrationForm(false);
                    setShowEvidenceUploadForm(false);
                    setShowTestSchedulingForm(false);
                    setShowHistoricalDataForm(false);
                    setShowDataVisualizationForm(false);
                    setShowResultsComparisonForm(false);
                    setShowAddInventoryItemForm(false);
                    setShowStockValuationForm(false);
                    setShowExpiryAlertSetupForm(false);
                    setShowCustomReportBuilderForm(false);
                    setShowForecastingForm(false);
                    setShowProtocolRegistrationForm(false);
                setShowAlertConfigurationForm(false);
                    setShowFeedbackCollectionForm(false);
                    setShowBroadcastMessageForm(false);
                    setShowResourceAllocationForm(false);
                    setShowTimeTrackingForm(false);
                    setShowRealTimeStockTrackingForm(false);
                    setShowProductManagementForm(false);
                    setShowQualityControlForm(false);
                    setShowEffectivenessEvaluationForm(false);
                    setShowReportSchedulerForm(false);
                    setShowRoleManagementForm(false);
                    setShowSMSNotificationForm(false);
                    setShowReminderSystemForm(false);
                    setShowStatusUpdateForm(false);
                    setShowEmergencyAlertForm(false);
                    setShowEquipmentMaintenanceSchedulingForm(false);
                    setShowTaskSchedulingForm(false);
                setShowEvidenceList(false); // Hide Evidence List
                    setShowTestSchedulingList(false); // Hide Test Scheduling List

                    navigate('/dashboard?TestDocumentationForm=create', { replace: true }); // Navigate to form URL
              }}
            >
                  <FaFileAlt /> Create Test Documentation
            </div>
             {/* Add List of Evidence tab */}
             <div
                  className={`dashboard-tab ${activeMenuItem === 'evidencelist' ? 'active' : ''}`}
                  onClick={() => {
                    setActiveTab('evidencelist');
                    setShowEvidenceList(true); // Show the Evidence List Table
                    // Hide all other forms/lists
                    setShowComplianceForm(false);
                    setShowTestCaseForm(false);
                    setShowTrialPhaseForm(false);
                    setShowTestDocumentationForm(false);
                    setShowProductEntryForm(false);
                    setShowStockMovementForm(false);
                    setShowStockMonitoringForm(false);
                    setShowReportGenerationForm(false);
                    setShowPerformanceAnalysisForm(false);
                    // setShowQualityIncidentReportForm(false);
                    setShowBroadcastAnnouncementForm(false);
                    setShowTaskAssignmentForm(false);
                    setShowAutomatedAlertForm(false);
                    setShowCalendarManagementForm(false);
                    setShowUserActivityLogForm(false);
                    setShowAuditTrailForm(false);
                    setShowCostTrackingForm(false);
                    setShowFieldActivityTrackingForm(false);
                    setShowProductRegistrationForm(false);
                    setShowEvidenceUploadForm(false);
                    setShowTestSchedulingForm(false);
                    setShowHistoricalDataForm(false);
                    setShowDataVisualizationForm(false);
                    setShowResultsComparisonForm(false);
                    setShowAddInventoryItemForm(false);
                    setShowStockValuationForm(false);
                    setShowExpiryAlertSetupForm(false);
                    setShowCustomReportBuilderForm(false);
                    setShowForecastingForm(false);
                    setShowProtocolRegistrationForm(false);
                    setShowAlertConfigurationForm(false);
                    setShowFeedbackCollectionForm(false);
                    setShowBroadcastMessageForm(false);
                    setShowResourceAllocationForm(false);
                    setShowTimeTrackingForm(false);
                    setShowRealTimeStockTrackingForm(false);
                    setShowProductManagementForm(false);
                    setShowQualityControlForm(false);
                    setShowEffectivenessEvaluationForm(false);
                    setShowReportSchedulerForm(false);
                    setShowRoleManagementForm(false);
                    setShowSMSNotificationForm(false);
                    setShowReminderSystemForm(false);
                    setShowStatusUpdateForm(false);
                    setShowEmergencyAlertForm(false);
                    setShowEquipmentMaintenanceSchedulingForm(false);
                    setShowTaskSchedulingForm(false);
                    setShowTestSchedulingList(false); // Hide Test Scheduling List

                    navigate('/dashboard?EvidenceList=list', { replace: true }); // Navigate to list URL
                  }}
                >
                  <FaClipboardCheck /> List of Evidence
                </div>
          </div>

          {/* Compliance Checklist Content */}
          {activeTab === 'compliance' && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>
                  {complianceMode === 'create' ? 'Create Compliance Checklist' : 
                   complianceMode === 'view' ? 'Compliance Checklist Details' : 
                   'Compliance Checklists'}
                </h2>
                {complianceMode === 'list' && (
                  <div className="table-actions">
                    <div className="table-search">
                      <input
                        type="text"
                        placeholder="Search compliance checklists..."
                        className="search-input"
                      />
                      <FaSearch className="search-icon" />
                    </div>
                    <button 
                      className="table-action-btn" 
                      onClick={() => {
                        setComplianceMode('create');
                        navigate('/dashboard?ComplianceChecklist=create', { replace: true });
                      }}
                    >
                      <FaPlus /> Create New
                    </button>
                    <button className="table-action-btn hide-button">Export CSV</button>
                    <div className="table-filter hide-button">
                      <FaFilter /> Filter
                    </div>
                  </div>
                )}
                {complianceMode !== 'list' && (
                  <div className="table-actions">
                    <button 
                      className="table-action-btn" 
                      onClick={() => {
                        setComplianceMode('list');
                        navigate('/dashboard?ComplianceChecklist=list', { replace: true });
                      }}
                    >
                      Back to List
                    </button>
                  </div>
                )}
              </div>

              {/* Inline Compliance Form */}
              {complianceMode === 'create' && (
                <div className="dashboard-compliance-wrapper">
                  <ComplianceForm 
                    onBack={() => {
                      setComplianceMode('list');
                      navigate('/dashboard?ComplianceChecklist=list', { replace: true });
                    }}
                    onSave={(data) => {
                      // Handle saving compliance data
                      console.log('Saving compliance data:', data);
                      alert('Compliance checklist saved successfully!');
                      setComplianceMode('list');
                      navigate('/dashboard?ComplianceChecklist=list', { replace: true });
                    }}
                  />
                </div>
              )}

              {/* Compliance Checklist Table */}
              {complianceMode === 'list' && (
                <>
                  <div className="data-table">
                    <table>
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Product</th>
                          <th>Reviewer</th>
                          <th>Date</th>
                          <th>Status</th>
                          <th>Actions</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td>CL-1001</td>
                          <td>Fungicide X-500</td>
                          <td>John Smith</td>
                          <td>2023-06-05</td>
                          <td>{renderStatusBadge('Completed')}</td>
                          <td>
                            <button 
                              className="action-button" 
                              onClick={() => {
                                setComplianceMode('view');
                                navigate('/dashboard?ComplianceChecklist=view&id=1', { replace: true });
                              }}
                            >
                              View
                            </button>
                            <button className="action-button delete">Delete</button>
                          </td>
                        </tr>
                        <tr>
                          <td>CL-1002</td>
                          <td>Organic Fertilizer B-200</td>
                          <td>Emily Johnson</td>
                          <td>2023-06-10</td>
                          <td>{renderStatusBadge('Pending')}</td>
                          <td>
                            <button 
                              className="action-button"
                              onClick={() => {
                                setComplianceMode('view');
                                navigate('/dashboard?ComplianceChecklist=view&id=2', { replace: true });
                              }}
                            >
                              View
                            </button>
                            <button className="action-button delete">Delete</button>
                          </td>
                        </tr>
                        <tr>
                          <td>CL-1003</td>
                          <td>Insecticide Z-100</td>
                          <td>Michael Brown</td>
                          <td>2023-06-12</td>
                          <td>{renderStatusBadge('Attention')}</td>
                          <td>
                            <button 
                              className="action-button"
                              onClick={() => {
                                setComplianceMode('view');
                                navigate('/dashboard?ComplianceChecklist=view&id=3', { replace: true });
                              }}
                            >
                              View
                            </button>
                            <button className="action-button delete">Delete</button>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <div className="table-footer">
                    <div className="page-info">
                      Showing 1-3 of 3 results
                    </div>
                    {/* Pagination would go here */}
                  </div>
                </>
              )}

              {/* Compliance Checklist Detail View */}
              {complianceMode === 'view' && (
                <div className="checklist-detail">
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
                          className="circle compliant"
                          strokeDasharray="100, 100"
                          d="M18 2.0845
                            a 15.9155 15.9155 0 0 1 0 31.831
                            a 15.9155 15.9155 0 0 1 0 -31.831"
                        />
                        <text x="18" y="20.35" className="percentage">
                          100%
                        </text>
                      </svg>
                      <div className="compliance-text">Compliance Rate</div>
                    </div>
                    
                    <div className="checklist-info">
                      <div className="info-group">
                        <div className="info-label">Product:</div>
                        <div className="info-value">Fungicide X-500</div>
                      </div>
                      <div className="info-group">
                        <div className="info-label">Reviewer:</div>
                        <div className="info-value">John Smith</div>
                      </div>
                      <div className="info-group">
                        <div className="info-label">Review Date:</div>
                        <div className="info-value">2023-06-05</div>
                      </div>
                      <div className="info-group">
                        <div className="info-label">Created:</div>
                        <div className="info-value">2023-06-05 10:30:00</div>
                      </div>
                      <div className="info-group">
                        <div className="info-label">Last Updated:</div>
                        <div className="info-value">2023-06-05 10:30:00</div>
                      </div>
                      <div className="info-group full-width">
                        <div className="info-label">Comments:</div>
                        <div className="info-value comments">All requirements met. Product is safe for use when guidelines are followed.</div>
                      </div>
                    </div>
                  </div>
                  
                  <div className="checklist-items-detail">
                    <h2>Checklist Items</h2>
                    <div className="item-list">
                      <div className="checklist-item-detail passed">
                        <div className="item-icon">
                          <FaCheck className="check-icon" />
                        </div>
                        <div className="item-name">Toxicity Test</div>
                        <div className="item-status">Pass</div>
                      </div>
                      <div className="checklist-item-detail passed">
                        <div className="item-icon">
                          <FaCheck className="check-icon" />
                        </div>
                        <div className="item-name">Environmental Impact</div>
                        <div className="item-status">Pass</div>
                      </div>
                      <div className="checklist-item-detail passed">
                        <div className="item-icon">
                          <FaCheck className="check-icon" />
                        </div>
                        <div className="item-name">Application Safety</div>
                        <div className="item-status">Pass</div>
                      </div>
                      <div className="checklist-item-detail passed">
                        <div className="item-icon">
                          <FaCheck className="check-icon" />
                        </div>
                        <div className="item-name">Storage Requirements</div>
                        <div className="item-status">Pass</div>
                      </div>
                      <div className="checklist-item-detail passed">
                        <div className="item-icon">
                          <FaCheck className="check-icon" />
                        </div>
                        <div className="item-name">Disposal Guidelines</div>
                        <div className="item-status">Pass</div>
                      </div>
                    </div>
                    </div>
                </div>
              )}
            </div>
          )}

          {/* Test Case Content */}
          {activeTab === 'testcase' && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>
                  {testCaseMode === 'create' ? 'Creating Test Cases' : 
                   testCaseMode === 'view' ? 'Registration Details' : 
                   ''}
                </h2>
                {testCaseMode === 'list' && (
                  <div className="table-actions">
                    {/* Removed search input and Register Product button as per user request */}
                    <button className="table-action-btn hide-button">Export CSV</button>
                    <div className="table-filter hide-button">
                      <FaFilter /> Filter
                    </div>
                  </div>
                )}
                {testCaseMode !== 'list' && (
                  <div className="table-actions">
                    <button 
                      className="table-action-btn" 
                      onClick={() => {
                        setTestCaseMode('list');
                        navigate('/dashboard?CreatingTestCases=list', { replace: true });
                      }}
                    >
                      Back to List
                    </button>
                  </div>
                )}
              </div>

              {/* Inline Test Case Form */}
              {testCaseMode === 'create' && (
                <div className="dashboard-testcase-wrapper">
                  <TestCaseForm 
                    onBack={() => {
                      setTestCaseMode('list');
                      navigate('/dashboard?CreatingTestCases=list', { replace: true });
                    }}
                    onSave={(data) => {
                      // Handle saving test case data
                      console.log('Saving product registration data:', data);
                      alert('Registration submitted successfully!');
                      setTestCaseMode('list');
                      navigate('/dashboard?CreatingTestCases=list', { replace: true });
                    }}
                  />
                </div>
              )}

              {/* Test Case Table */}
              {testCaseMode === 'list' && (
                <>
                  <div className="table-container">
                    <div className="table-header-actions">
                      <h2>Test Cases</h2>
                      {/* Removed actions: search and Create New button as per user request */}
                  </div>
                    <AllDataTable apiEndpoint="/api/testcases" />
                  </div>
                </>
              )}

              {/* Test Case Detail View */}
              {testCaseMode === 'view' && (
                <div className="testcase-detail">
                  <div className="testcase-info">
                    <div className="info-group">
                      <div className="info-label">Test Name:</div>
                      <div className="info-value">Fungicide Efficacy Test</div>
                    </div>
                    <div className="info-group">
                      <div className="info-label">Test Description:</div>
                      <div className="info-value">Evaluation of fungicide performance on wheat crops</div>
                    </div>
                    <div className="info-group">
                      <div className="info-label">Test Objectives:</div>
                      <div className="info-value">Determine efficacy against powdery mildew</div>
                    </div>
                    <div className="info-group">
                      <div className="info-label">Product Type:</div>
                      <div className="info-value">Fungicide</div>
                    </div>
                    <div className="info-group">
                      <div className="info-label">Product Batch Number:</div>
                      <div className="info-value">FNG-2023-0342</div>
                    </div>
                    <div className="info-group">
                      <div className="info-label">Testing Location:</div>
                      <div className="info-value">Field Station Alpha</div>
                    </div>
                    <div className="info-group">
                      <div className="info-label">Assigned Worker:</div>
                      <div className="info-value">John Smith</div>
                    </div>
                    <div className="info-group">
                      <div className="info-label">Start Date:</div>
                      <div className="info-value">2023-10-15</div>
                    </div>
                    <div className="info-group">
                      <div className="info-label">End Date:</div>
                      <div className="info-value">2023-11-15</div>
                    </div>
                    <div className="info-group">
                      <div className="info-label">Notes:</div>
                      <div className="info-value">Test will be conducted under standard field conditions with three replications.</div>
                    </div>
                    <div className="info-group">
                      <div className="info-label">Receive Updates:</div>
                      <div className="info-value">Yes</div>
                    </div>
                  </div>
                </div>
              )}
            </div>
          )}

          {/* Trial Phase Content */}
          {activeTab === 'trialphase' && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>
                  {trialPhaseMode === 'create' ? 'Trial Phase Tracking' : 
                   trialPhaseMode === 'view' ? 'Trial Phase Details' : 
                   'Trial Phases'}
                </h2>
                {trialPhaseMode === 'list' && (
                  <div className="table-actions">
                    <div className="table-search">
                      <input
                        type="text"
                        placeholder="Search trial phases..."
                        className="search-input"
                      />
                      <FaSearch className="search-icon" />
                    </div>
                    <button 
                      className="table-action-btn" 
                      onClick={() => {
                        setTrialPhaseMode('create');
                        navigate('/dashboard?TrialPhaseTrackingForm=create', { replace: true });
                      }}
                    >
                      <FaPlus /> Create New
                    </button>
                    <button className="table-action-btn hide-button">Export CSV</button>
                    <div className="table-filter hide-button">
                      <FaFilter /> Filter
                    </div>
                  </div>
                )}
                {trialPhaseMode !== 'list' && (
                  <div className="table-actions">
                    <button 
                      className="table-action-btn" 
                      onClick={() => {
                        setTrialPhaseMode('list');
                        navigate('/dashboard?TrialPhaseTrackingForm=list', { replace: true });
                      }}
                    >
                      Back to List
                    </button>
                  </div>
                )}
              </div>

              {/* Inline Trial Phase Form */}
              {trialPhaseMode === 'create' && (
                <div className="dashboard-trialphase-wrapper">
                  <TrialPhaseForm 
                    onBack={() => {
                      setTrialPhaseMode('list');
                      navigate('/dashboard?TrialPhaseTrackingForm=list', { replace: true });
                    }}
                    onSave={(data) => {
                      console.log('Saving trial phase data:', data);
                      toast.success('Trial phase data saved successfully!');
                      setTrialPhaseMode('list');
                      navigate('/dashboard?TrialPhaseTrackingForm=list', { replace: true });
                      // Optionally refresh the list after save
                      // fetchTrialPhases();
                    }}
                  />
                </div>
              )}

              {/* Trial Phase List Table */}
              {trialPhaseMode === 'list' && (
                <>
                  <div className="data-table">
                    <table>
                      <thead>
                        <tr>
                          <th>Observations</th>
                          <th>Phase Name</th>
                          <th>phase_date</th>
                          <th>Temperature</th>
                          <th>Test Name</th>
                          <th>Actions</th>
                        </tr>
                      </thead>
                      <tbody>
                        {/* Calculate pagination indices */}
                        {(() => {
                          const indexOfLastTrialPhase = trialPhaseCurrentPage * trialPhaseItemsPerPage;
                          const indexOfFirstTrialPhase = indexOfLastTrialPhase - trialPhaseItemsPerPage;
                          const currentTrialPhases = trialPhases.slice(indexOfFirstTrialPhase, indexOfLastTrialPhase);

                          if (currentTrialPhases.length === 0) {
                            return (
                              <tr>
                                <td colSpan="6" style={{ textAlign: 'center' }}>No trial phases found.</td>
                              </tr>
                            );
                          }

                          return currentTrialPhases.map(phase => (
                              <tr key={phase.id}>
                                <td>{phase.observations || 'Null'}</td> {/* Display observations or 'Null' */} 
                                <td>{phase.phaseName}</td>
                                <td>{phase.phaseDate}</td>
                                <td>{phase.weatherData?.temperature ?? 'N/A'}</td> {/* Display temperature if available */} 
                                <td>{phase.testName || 'N/A'}</td> {/* Use phase.testName directly, fallback to N/A */} 
                                <td>
                                  <button 
                                    className="action-button"
                                    onClick={() => {
                                      setTrialPhaseMode('view');
                                      navigate(`/dashboard?TrialPhaseTrackingForm=view&id=${phase.id}`, { replace: true });
                                    }}
                                  >
                                    View
                                  </button>
                                  <button 
                                    className="action-button delete"
                                    onClick={() => {
                                      handleDeleteTrialPhase(phase.id);
                                    }}
                                  >
                                    Delete
                                  </button>
                                </td>
                              </tr>
                            ));
                        })()}
                      </tbody>
                    </table>
                  </div>
                  <div className="table-footer">
                    {/* Basic indication of data count - pagination can be added later */}
                    <div className="page-info">
                       Showing {Math.min(trialPhases.length, (trialPhaseCurrentPage - 1) * trialPhaseItemsPerPage + 1)} to {Math.min(trialPhases.length, trialPhaseCurrentPage * trialPhaseItemsPerPage)} of {trialPhases.length} result(s)
                    </div>
                    {/* Pagination controls */}
                    {renderPagination(trialPhaseCurrentPage, Math.ceil(trialPhases.length / trialPhaseItemsPerPage), setTrialPhaseCurrentPage)}
                  </div>
                </>
              )}

              {/* Trial Phase View Form */}
              {trialPhaseMode === 'view' && viewedTrialPhaseData && (
                <div className="dashboard-trialphase-wrapper">
                  <TrialPhaseForm 
                    initialData={viewedTrialPhaseData}
                    onBack={() => {
                      setTrialPhaseMode('list');
                      navigate('/dashboard?TrialPhaseTrackingForm=list', { replace: true });
                      setViewedTrialPhaseData(null); // Clear data when going back
                    }}
                    // Pass null for onSave in view mode or handle updates separately
                    onSave={null}
                  />
                </div>
              )}
            </div>
          )}

          {/* Conditional rendering based on activeTab state */}
          {activeTab === 'testdocumentation' && showTestDocumentationForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Create Test Documentation</h2>
                {/* Optionally add a back button if needed */}
                {/* <button onClick={() => navigate(-1)}>Back</button> */}
              </div>
              <div className="dashboard-testdocumentation-wrapper"> {/* Use a wrapper class if needed for styling */}
                <TestDocumentationForm />
              </div>
            </div>
          )}
          {activeTab === 'productentry' && showProductEntryForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Product Entry Form</h2>
                {/* Optionally add a back button if needed */}
                {/* <button onClick={() => navigate(-1)}>Back</button> */}
              </div>
              {/* Use a wrapper class if needed for styling */}
              <div className="dashboard-productentry-wrapper">
                <ProductEntryForm />
              </div>
            </div>
          )}
          {activeTab === 'stockmovement' && showStockMovementForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Stock Movement Form</h2>
                {/* Optionally add a back button if needed */}
                {/* <button onClick={() => navigate(-1)}>Back</button> */}
              </div>
              {/* Use a wrapper class if needed for styling */}
              <div className="dashboard-stockmovement-wrapper">
                <StockMovementForm />
              </div>
            </div>
          )}
          {activeTab === 'stockmonitoring' && showStockMonitoringForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Stock Monitoring Form</h2>
                {/* Optionally add a back button if needed */}
                {/* <button onClick={() => navigate(-1)}>Back</button> */}
              </div>
              {/* Use a wrapper class if needed for styling */}
              <div className="dashboard-stockmonitoring-wrapper">
                <StockMonitoringForm />
              </div>
            </div>
          )}
          {activeTab === 'reportgeneration' && showReportGenerationForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Report Generation Form</h2>
                {/* Optionally add a back button if needed */}
                {/* <button onClick={() => navigate(-1)}>Back</button> */}
              </div>
              {/* Use a wrapper class if needed for styling */}
              <div className="dashboard-reportgeneration-wrapper">
                <ReportGenerationForm />
              </div>
            </div>
          )}
          {activeTab === 'performanceanalysis' && showPerformanceAnalysisForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Performance Analysis Form</h2>
                {/* Optionally add a back button if needed */}
                {/* <button onClick={() => navigate(-1)}>Back</button> */}
              </div>
              {/* Use a wrapper class if needed for styling */}
              <div className="dashboard-performanceanalysis-wrapper">
                <PerformanceAnalysisForm />
              </div>
            </div>
          )}

            <div className="table-container">
              <div className="table-header-actions">
                <h2>Broadcast Announcement Form</h2>
              </div>
              <div className="dashboard-broadcastannouncementform-wrapper">
                <BroadcastAnnouncementForm />
              </div>
            </div>
          
          {activeTab === 'taskassignmentform' && showTaskAssignmentForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Task Assignment Form</h2>
              </div>
              <div className="dashboard-taskassignmentform-wrapper">
                <TaskAssignmentForm />
              </div>
            </div>
          )}
          {activeTab === 'automatedalertform' && showAutomatedAlertForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Automated Alert Form</h2>
              </div>
              <div className="dashboard-automatedalertform-wrapper">
                <AutomatedAlertForm />
              </div>
            </div>
          )}
          {activeTab === 'calendarmanagementform' && showCalendarManagementForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Calendar Management Form</h2>
              </div>
              <div className="dashboard-calendarmanagementform-wrapper">
                <CalendarManagementForm />
              </div>
            </div>
          )}
          {activeTab === 'useractivitylogform' && showUserActivityLogForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>User Activity Log Form</h2>
              </div>
              <div className="dashboard-useractivitylogform-wrapper">
                <UserActivityLogForm />
              </div>
            </div>
          )}
          {activeTab === 'audittrailform' && showAuditTrailForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Audit Trail Form</h2>
              </div>
              <div className="dashboard-audittrailform-wrapper">
                <AuditTrailForm />
              </div>
            </div>
          )}
          {activeTab === 'costtracking' && showCostTrackingForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Cost Tracking Form</h2>
              </div>
              <div className="dashboard-costtracking-wrapper">
                <CostTrackingForm />
              </div>
            </div>
          )}
          {activeTab === 'fieldactivitytracking' && showFieldActivityTrackingForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Field Activity Tracking Form</h2>
              </div>
              <div className="dashboard-fieldactivitytrackingform-wrapper">
                <FieldActivityTrackingForm />
              </div>
            </div>
          )}
          {activeTab === 'productregistration' && showProductRegistrationForm && (
            <div className="table-container">
              <div className="table-header-actions center-header">
                <h2>Product Registration Form</h2>
              </div>
              <div className="centered-form-wrapper">
                <ProductRegistrationForm />
              </div>
            </div>
          )}
          {activeTab === 'evidenceupload' && showEvidenceUploadForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Evidence Upload Form</h2>
              </div>
              <div className="dashboard-evidenceuploadform-wrapper">
                <EvidenceUploadForm />
              </div>
            </div>
          )}
          {activeTab === 'testschedulingform' && showTestSchedulingForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Test Scheduling Form</h2>
              </div>
              <div className="dashboard-testschedulingform-wrapper">
                <TestSchedulingForm />
              </div>
            </div>
          )}
          {activeTab === 'historicaldataform' && showHistoricalDataForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Historical Data Form</h2>
              </div>
              <div className="dashboard-historicaldataform-wrapper">
                <HistoricalDataForm />
              </div>
            </div>
          )}
          {activeTab === 'datavisualizationform' && showDataVisualizationForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Data Visualization Form</h2>
              </div>
              <div className="dashboard-datavisualizationform-wrapper">
                <DataVisualizationForm />
              </div>
            </div>
          )}
          {activeTab === 'resultscomparisonform' && showResultsComparisonForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Results Comparison Form</h2>
              </div>
              <div className="dashboard-resultscomparisonform-wrapper">
                <ResultsComparisonForm />
              </div>
            </div>
          )}
          {activeTab === 'addinventoryitemform' && showAddInventoryItemForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Add Inventory Item Form</h2>
              </div>
              <div className="dashboard-addinventoryitemform-wrapper">
                <AddInventoryItemForm />
              </div>
            </div>
          )}
          {activeTab === 'stockvaluationform' && showStockValuationForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Stock Valuation Form</h2>
              </div>
              <div className="dashboard-stockvaluationform-wrapper">
                <StockValuationForm />
              </div>
            </div>
          )}
          {activeTab === 'expiryalertsetupform' && showExpiryAlertSetupForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Expiry Alert Setup Form</h2>
              </div>
              <div className="dashboard-expiryalertsetupform-wrapper">
                <ExpiryAlertSetupForm />
              </div>
            </div>
          )}
          {activeTab === 'customreportbuilderform' && showCustomReportBuilderForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Custom Report Builder Form</h2>
              </div>
              <div className="dashboard-customreportbuilderform-wrapper">
                <CustomReportBuilderForm />
              </div>
            </div>
          )}
          {activeTab === 'forecastingform' && showForecastingForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Forecasting Form</h2>
              </div>
              <div className="dashboard-forecastingform-wrapper">
                <ForecastingForm />
              </div>
            </div>
          )}
          {activeTab === 'protocolregistrationform' && showProtocolRegistrationForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Protocol Registration Form</h2>
              </div>
              <div className="dashboard-protocolregistrationform-wrapper">
                <ProtocolRegistrationForm />
              </div>
            </div>
          )}
          {activeTab === 'alertconfigurationform' && showAlertConfigurationForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Alert Configuration Form</h2>
              </div>
              <div className="dashboard-alertconfigurationform-wrapper">
                <AlertConfigurationForm />
              </div>
            </div>
          )}
          {activeTab === 'feedbackcollection' && showFeedbackCollectionForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Feedback Collection Form</h2>
              </div>
              <div className="dashboard-feedbackcollectionform-wrapper">
                <FeedbackCollectionForm />
              </div>
            </div>
          )}
          {activeTab === 'broadcastmessageform' && showBroadcastMessageForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Broadcast Message Form</h2>
              </div>
              <div className="dashboard-broadcastmessageform-wrapper">
                <BroadcastMessageForm />
              </div>
            </div>
          )}
          {activeTab === 'resourceallocationform' && showResourceAllocationForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Resource Allocation Form</h2>
              </div>
              <div className="dashboard-resourceallocationform-wrapper">
                <ResourceAllocationForm />
              </div>
            </div>
          )}
          {activeTab === 'timetrackingform' && showTimeTrackingForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Time Tracking Form</h2>
              </div>
              <div className="dashboard-timetrackingform-wrapper">
                <TimeTrackingForm />
              </div>
            </div>
          )}
          {activeTab === 'realtimestocktrackingform' && showRealTimeStockTrackingForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Real-time Stock Tracking Form</h2>
              </div>
              <div className="dashboard-realtimestocktrackingform-wrapper">
                <RealTimeStockTrackingForm />
              </div>
            </div>
          )}
          {activeTab === 'productmanagementform' && showProductManagementForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Product Management Form</h2>
              </div>
              <div className="dashboard-productmanagementform-wrapper">
                <ProductManagementForm />
              </div>
            </div>
          )}
          {activeTab === 'qualitycontrolform' && showQualityControlForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Quality Control Form</h2>
              </div>
              <div className="dashboard-qualitycontrolform-wrapper">
                <QualityControlForm />
              </div>
            </div>
          )}
          {activeTab === 'effectivenesseevaluationform' && showEffectivenessEvaluationForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Effectiveness Evaluation Form</h2>
              </div>
              <div className="dashboard-effectivenessevaluationform-wrapper">
                <EffectivenessEvaluationForm />
              </div>
            </div>
          )}
          {activeTab === 'reportschedulerform' && showReportSchedulerForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Report Scheduler Form</h2>
              </div>
              <div className="dashboard-reportschedulerform-wrapper">
                <ReportSchedulerForm />
              </div>
            </div>
          )}
          {activeTab === 'rolemanagementform' && showRoleManagementForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Role Management Form</h2>
              </div>
              <div className="dashboard-rolemanagementform-wrapper">
                <RoleManagementForm />
              </div>
            </div>
          )}
          {activeTab === 'smsnotificationform' && showSMSNotificationForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>SMS Notification Form</h2>
              </div>
              <div className="dashboard-smsnotificationform-wrapper">
                <SMSNotificationForm />
              </div>
            </div>
          )}
          {activeTab === 'remindersystemform' && showReminderSystemForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Reminder System Form</h2>
              </div>
              <div className="dashboard-remindersystemform-wrapper">
                <ReminderSystemForm />
              </div>
            </div>
          )}
          {activeTab === 'statusupdateform' && showStatusUpdateForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Status Update Form</h2>
              </div>
              <div className="dashboard-statusupdateform-wrapper">
                <StatusUpdateForm />
              </div>
            </div>
          )}
          {activeTab === 'emergencyalertform' && showEmergencyAlertForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Emergency Alert Form</h2>
              </div>
              <div className="dashboard-emergencyalertform-wrapper">
                <EmergencyAlertForm />
              </div>
            </div>
          )}
          {activeTab === 'equipmentmaintenanceschedulingform' && showEquipmentMaintenanceSchedulingForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Equipment Maintenance Scheduling Form</h2>
              </div>
              <div className="dashboard-equipmentmaintenanceschedulingform-wrapper">
                <EquipmentMaintenanceSchedulingForm />
              </div>
            </div>
          )}
          {activeTab === 'taskschedulingform' && showTaskSchedulingForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Task Scheduling Form</h2>
              </div>
              <div className="dashboard-taskschedulingform-wrapper">
                <TaskSchedulingForm />
              </div>
            </div>
          )}
          {activeTab === 'scheduletaskstable' && showScheduleTasksTable && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Schedule Tasks Table</h2>
              </div>
              <div className="dashboard-scheduletaskstable-wrapper">
                <TaskSchedulingForm setTotalCalendarEvents={setTotalCalendarEvents} />
              </div>
            </div>
          )}

          {/* Show Compliance Checklists table when Product List is active */}
          {activeTab === 'soil' && (activeMenuItem === 'ProductRegistrationForm' || activeTab === 'productregistration') && (
            editingProduct ? (
              <div className="table-container">
                <div className="table-header-actions center-header">
                  <h2>Edit Product</h2>
                </div>
                <div className="centered-form-wrapper">
                  <ProductRegistrationForm
                    productToEdit={editingProduct}
                    onSave={handleProductUpdateSuccess} // Use onSave for updates as well
                    onCancel={handleCancelEdit}
                  />
                </div>
              </div>
            ) : (
              <div className="table-container">
                <div className="table-header-actions">
                  <h2>Product List</h2>
                  <div className="table-actions">
                    <div className="table-search">
                      <input
                        type="text"
                        placeholder="Search products..."
                        className="search-input product-search-input"
                        value={productSearchTerm}
                        onChange={(e) => setProductSearchTerm(e.target.value)}
                      />
                      <FaSearch className="search-icon" />
                    </div>
                    {/* Add other product table actions here if needed */}
                  </div>
                </div>
                <div className="data-table">
                  <table>
                    <thead>
                      <tr>
                        <th>Name</th>
                        <th>Registered By</th>
                        <th>Product Type</th>
                        <th>Tested</th>
                        <th>Photo</th>
                        <th>Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      {paginatedProducts.length === 0 ? (
                        <tr><td colSpan="6" style={{textAlign: 'center'}}>No products found.</td></tr>
                      ) : (
                        paginatedProducts.map((product, idx) => (
                          <tr key={product.id || idx}>
                            <td>{product.name}</td>
                            <td>
                              {/* Display full name if available, otherwise the original value */}
                              {userFullNames[product.registeredBy] || product.registeredBy}
                            </td>
                            <td>{product.productType}</td>
                            <td>
                              <span className={product.tested ? 'tested-yes-bg' : 'tested-no'}>
                                {product.tested ? 'YES' : 'NO'}
                              </span>
                            </td>
                            <td>
                              {product.imageUrl && productImages[product.id] ? (
                                <img src={productImages[product.id]} alt="Product" style={{ width: '30px', height: '30px', objectFit: 'cover' }} />
                              ) : (
                                'No Photo' // Or a placeholder image/icon
                              )}
                            </td>
                            <td>
                              <button className="icon-action-button edit-icon" onClick={() => setEditingProduct(product)}><FaEdit /></button>
                              <button className="icon-action-button delete-icon" onClick={() => handleDeleteProduct(product.id)}><FaTrash /></button>
                            </td>
                          </tr>
                        ))
                      )}
                    </tbody>
                  </table>
                </div>
                {/* Pagination controls */}
                {totalProductPages > 1 && (
                  <div className="table-pagination" style={{marginTop: '10px', justifyContent: 'center'}}>
                    <button className="pagination-button" onClick={() => setProductPage(1)} disabled={productPage === 1}>&laquo;</button>
                    <button className="pagination-button" onClick={() => setProductPage(productPage - 1)} disabled={productPage === 1}>&lt;</button>
                    {Array.from({ length: totalProductPages }).map((_, i) => (
                      <button
                        key={i + 1}
                        className={`pagination-button${productPage === i + 1 ? ' active' : ''}`}
                        onClick={() => setProductPage(i + 1)}
                      >
                        {i + 1}
                      </button>
                    ))}
                    <button className="pagination-button" onClick={() => setProductPage(productPage + 1)} disabled={productPage === totalProductPages}>&gt;</button>
                    <button className="pagination-button" onClick={() => setProductPage(totalProductPages)} disabled={productPage === totalProductPages}>&raquo;</button>
                  </div>
                )}
              </div>
            )
          )}
          {/* Show Evidence List Table when activeTab is 'evidencelist' */}
          {activeTab === 'evidencelist' && (
            <div className="table-container">
              <EvidenceListTable />
            </div>
          )}
          {/* Test Scheduling List Content */}
          {activeTab === 'testschedulinglist' && showTestSchedulingList && (
             <div className="table-container">
              <div className="table-header-actions">
                <h2>Test Schedules List</h2>
                <div className="table-actions">
                    {/* Search Input */}
                    <div className="table-search" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', width: '100%' }}>
                        <input
                            type="text"
                            placeholder="Search schedules..."
                            className="search-input"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            style={{ textAlign: 'center', color: 'black' }} // Inline style for centering and color
                        />
                        <FaSearch className="search-icon" style={{ marginLeft: '-25px', color: '#888' }} /> {/* Position icon */}
                    </div>
                    <button
                        className="table-action-btn"
                        onClick={() => navigate('/dashboard?TestSchedulingForm=create', { replace: true })} // Navigate to upload form
                    >
                        <FaPlus /> Create New
                    </button>
                    {/* Add other actions if needed, e.g., Export */}
                    {/* <button className="table-action-btn">Export CSV</button> */}
                    {/* <button className="table-action-btn"><FaFilter /> Filter</button> */}
                </div>
            </div>


              {loadingSchedules && <p>Loading test schedules...</p>}
              {schedulesError && <p className="error-message">{schedulesError}</p>}
              {!loadingSchedules && !schedulesError && filteredSchedules.length === 0 && <p>No test schedules found.</p>} {/* Use filteredSchedules */}

              {!loadingSchedules && !schedulesError && filteredSchedules.length > 0 && (
                <div className="data-table">
                  <table>
                    <thead>
                      <tr>
                        <th>Test Name</th>
                        <th>Schedule Name</th>
                        <th>Start Date</th>
                        <th>Frequency</th>
                        <th>Location</th>
                        <th>Assigned Personnel</th>
                        <th>Actions</th> {/* Added Actions column header */}
                      </tr>
                    </thead>
                    <tbody>
                      {/* Use filteredSchedules for mapping */}
                      {/* Apply pagination logic to filteredSchedules */}
                      {(() => {
                         const indexOfLastSchedule = schedulePage * schedulesPerPage;
                         const indexOfFirstSchedule = indexOfLastSchedule - schedulesPerPage;
                         const currentSchedules = filteredSchedules.slice(indexOfFirstSchedule, indexOfLastSchedule);

                         return currentSchedules.map(schedule => (
                           <tr key={schedule.id}> {/* Assuming each schedule has a unique 'id' */}
                             <td>{schedule.testCaseTitle || 'N/A'}</td> {/* Use schedule.testCaseTitle */}
                             <td>{schedule.scheduleName || 'N/A'}</td> {/* Use schedule.scheduleName */}
                             <td>{schedule.startDate || 'N/A'}</td> {/* Use schedule.startDate */}
                             <td>{schedule.frequency || 'N/A'}</td> {/* Use schedule.frequency */}
                             <td>{schedule.location || 'N/A'}</td> {/* Display schedule.location */}
                             <td>{schedule.assignedPersonnel || 'N/A'}</td> {/* Use schedule.assignedPersonnel */}
                             <td>
                               {/* Add action buttons as needed (Edit, Delete) */}
                               <button className="action-button edit">Edit</button>
                               <button className="action-button delete">Delete</button>
                             </td>
                           </tr>
                         ));
                       })()}
                    </tbody>
                  </table>
                </div>
              )}

              {/* Pagination controls - need to adapt for filteredSchedules if pagination is desired */}
              {/* For now, pagination is removed as filtering is applied */}
              {/* Pagination Placeholder */}
                <div className="table-footer">
                  <div className="page-info">
                    Showing {Math.min(filteredSchedules.length, (schedulePage - 1) * schedulesPerPage + 1)} to {Math.min(filteredSchedules.length, schedulePage * schedulesPerPage)} of {filteredSchedules.length} result(s) {/* Use filteredSchedules.length */}
                  </div>
                  {/* Pagination controls would go here */}
                  {filteredSchedules.length > schedulesPerPage && renderPagination(schedulePage, Math.ceil(filteredSchedules.length / schedulesPerPage), setSchedulePage)} {/* Use filteredSchedules.length */}
                </div>
            </div>
          )}
        </div>
      </div>
    </div>
    );
};

export default Dashboard; 