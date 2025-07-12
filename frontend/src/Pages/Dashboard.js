import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './Dashboard.css';
import { useAuth } from '../contexts/AuthContext';
import authService from '../services/authService';
import { 
  FaArrowRight, FaFilter, FaChevronDown, FaSearch, FaSignOutAlt, 
  FaBell, FaHome, FaChevronRight, FaChevronLeft, FaLeaf, FaWater, 
  FaTools, FaCalendarAlt, FaCheck, FaExclamationTriangle, FaThermometerHalf, 
  FaTint, FaCog, FaUserCircle, FaFlask, FaWarehouse, FaChartLine, 
  FaUserShield, FaComments, FaTasks, FaClipboardCheck, FaPlus, FaMinus, FaSave, FaTimesCircle
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
import ComplianceChecklistForm from '../Components/ComplianceChecklist/ComplianceChecklistForm';
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
import axios from 'axios';

const Dashboard = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuth();
  const [isLoading, setIsLoading] = useState(true);
  const [userInfo, setUserInfo] = useState(null);
  
  // Menu state variables
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const [adminOpen, setAdminOpen] = useState(false);
  const [userDropdownOpen, setUserDropdownOpen] = useState(false);
  
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
  
  // Test Case form state
  const [showTestCaseForm, setShowTestCaseForm] = useState(false);
  const [testCaseMode, setTestCaseMode] = useState('list'); // list, create, view
  
  // Submenu open/closed states
  const [testCaseSubMenuOpen, setTestCaseSubMenuOpen] = useState(true);
  const [complianceSubMenuOpen, setComplianceSubMenuOpen] = useState(false);
  const [inventorySubMenuOpen, setInventorySubMenuOpen] = useState(false);
  const [reportSubMenuOpen, setReportSubMenuOpen] = useState(false);
  const [userSecuritySubMenuOpen, setUserSecuritySubMenuOpen] = useState(false);
  const [notificationsSubMenuOpen, setNotificationsSubMenuOpen] = useState(false);
  const [calendarSubMenuOpen, setCalendarSubMenuOpen] = useState(false);

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
  
  // State variables for fetched counts
  const [totalTestCases, setTotalTestCases] = useState(0);
  const [failedTestResults, setFailedTestResults] = useState(0);
  
  // Track when numbers reach their maximum (only for remaining animated counters)
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
  
  // Compliance Checklist form state
  const [showComplianceChecklistForm, setShowComplianceChecklistForm] = useState(false);
  
  // Quality Incident Report form state
  const [showQualityIncidentReportForm, setShowQualityIncidentReportForm] = useState(false);
  
  // Broadcast Announcement form state
  const [showBroadcastAnnouncementForm, setShowBroadcastAnnouncementForm] = useState(false);
  
  // Task Assignment form state
  const [showTaskAssignmentForm, setShowTaskAssignmentForm] = useState(false);
  
  // Automated Alert form state
  const [showAutomatedAlertForm, setShowAutomatedAlertForm] = useState(false);
  
  // Calendar Management form state
  const [showCalendarManagementForm, setShowCalendarManagementForm] = useState(false);
  
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
  
  useEffect(() => {
    if (equipment.length > 0) {
      // Delay for the third card
      const startDelay = setTimeout(() => {
        let count = 1;
        const finalValue = equipment.length;
        const speed = 800; // Medium speed
        const pauseAtMax = 10000; // pause for 10 seconds at max value
        
        const animateCounter = () => {
          // If at final value, set max flag and schedule the next increment with delay
          if (count === finalValue) {
            setIsEquipmentMax(true);
            setTimeout(() => {
              count = 1;
              setIsEquipmentMax(false);
              setAnimatedEquipmentCount(count);
              timeoutRef.current = setTimeout(animateCounter, speed);
            }, pauseAtMax);
          } else {
            // Regular increment
            count++;
            setAnimatedEquipmentCount(count);
            timeoutRef.current = setTimeout(animateCounter, speed);
          }
        };
        
        setAnimatedEquipmentCount(count);
        const timeoutRef = { current: setTimeout(animateCounter, speed) };
        
        return () => {
          if (timeoutRef.current) {
            clearTimeout(timeoutRef.current);
          }
        };
      }, 400);
      
      return () => clearTimeout(startDelay);
    }
  }, [equipment]);
  
  useEffect(() => {
    if (scheduledTests.length > 0) {
      // Delay for the fourth card
      const startDelay = setTimeout(() => {
        let count = 1;
        const finalValue = scheduledTests.length;
        const speed = 650; // Faster speed
        const pauseAtMax = 10000; // pause for 10 seconds at max value
        
        const animateCounter = () => {
          // If at final value, set max flag and schedule the next increment with delay
          if (count === finalValue) {
            setIsScheduledMax(true);
            setTimeout(() => {
              count = 1;
              setIsScheduledMax(false);
              setAnimatedScheduledCount(count);
              timeoutRef.current = setTimeout(animateCounter, speed);
            }, pauseAtMax);
          } else {
            // Regular increment
            count++;
            setAnimatedScheduledCount(count);
            timeoutRef.current = setTimeout(animateCounter, speed);
          }
        };
        
        setAnimatedScheduledCount(count);
        const timeoutRef = { current: setTimeout(animateCounter, speed) };
        
        return () => {
          if (timeoutRef.current) {
            clearTimeout(timeoutRef.current);
          }
        };
      }, 600);
      
      return () => clearTimeout(startDelay);
    }
  }, [scheduledTests]);

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    const showCompliance = queryParams.get('ComplianceChecklist');
    const showTestCase = queryParams.get('CreatingTestCases');
    const showTrialPhase = queryParams.get('TrialPhaseTrackingForm');
    const showTestDocumentation = queryParams.get('CreatingTestDocumentation');
    const showProductEntry = queryParams.get('ProductEntry');
    const showStockMovement = queryParams.get('StockMovement');
    const showStockMonitoring = queryParams.get('StockMonitoringForm');
    const showReportGeneration = queryParams.get('ReportGeneration');
    const showPerformanceAnalysis = queryParams.get('PerformanceAnalysis');
    const showComplianceChecklist = queryParams.get('ComplianceChecklistForm');
    const showQualityIncidentReport = queryParams.get('QualityIncidentReportForm');
    const showBroadcastAnnouncement = queryParams.get('BroadcastAnnouncementForm');
    const showTaskAssignment = queryParams.get('TaskAssignmentForm');
    const showAutomatedAlert = queryParams.get('AutomatedAlertForm');
    const showCalendarManagement = queryParams.get('CalendarManagementForm');
    const showUserActivityLog = queryParams.get('UserActivityLogForm');
    const showAuditTrail = queryParams.get('AuditTrailForm');
    const showCostTracking = queryParams.get('CostTrackingForm');
    const showFieldActivityTracking = queryParams.get('FieldActivityTrackingForm');
    const showProductRegistration = queryParams.get('ProductRegistrationForm');
    const showEvidenceUpload = queryParams.get('EvidenceUploadForm');
    const showTestScheduling = queryParams.get('TestSchedulingForm');
    const showHistoricalData = queryParams.get('HistoricalDataForm');
    const showDataVisualization = queryParams.get('DataVisualizationForm');
    const showResultsComparison = queryParams.get('ResultsComparisonForm');
    const showAddInventoryItem = queryParams.get('AddInventoryItemForm');
    const showStockValuation = queryParams.get('StockValuationForm');
    const showExpiryAlertSetup = queryParams.get('ExpiryAlertSetupForm');
    const showCustomReportBuilder = queryParams.get('CustomReportBuilderForm');
    const showForecasting = queryParams.get('ForecastingForm');
    const showProtocolRegistration = queryParams.get('ProtocolRegistrationForm');
    const showAlertConfiguration = queryParams.get('AlertConfigurationForm');
    const showFeedbackCollection = queryParams.get('FeedbackCollection');
    const showResourceAllocation = queryParams.get('ResourceAllocationForm');
    const showTimeTracking = queryParams.get('TimeTrackingForm');

    // Reset all form visibility states first
    setShowComplianceForm(false);
    setShowTestCaseForm(false);
    setShowTrialPhaseForm(false);
    setShowTestDocumentationForm(false);
    setShowProductEntryForm(false);
    setShowStockMovementForm(false);
    setShowStockMonitoringForm(false);
    setShowReportGenerationForm(false);
    setShowPerformanceAnalysisForm(false);
    setShowComplianceChecklistForm(false);
    setShowQualityIncidentReportForm(false);
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

    // Set state for Broadcast Message form based on URL parameter
    if (queryParams.get('BroadcastMessageForm') === 'create') {
      setShowBroadcastMessageForm(true);
    } else {
      setShowBroadcastMessageForm(false);
    }

    if (showBroadcastAnnouncement === 'create') {
      setShowBroadcastAnnouncementForm(true);
      setActiveTab('broadcastannouncementform');
    } else if (showTaskAssignment === 'create') {
      setShowTaskAssignmentForm(true);
      setActiveTab('taskassignmentform');
    } else if (showAutomatedAlert === 'create') {
      setShowAutomatedAlertForm(true);
      setActiveTab('automatedalertform');
    } else if (showCalendarManagement === 'create') {
      setShowCalendarManagementForm(true);
      setActiveTab('calendarmanagementform');
    } else if (showUserActivityLog === 'create') {
      setShowUserActivityLogForm(true);
      setActiveTab('useractivitylogform');
    } else if (showAuditTrail === 'create') {
      setShowAuditTrailForm(true);
      setActiveTab('audittrailform');
    } else if (showTestDocumentation === 'create') {
      setShowTestDocumentationForm(true);
      setActiveTab('testdocumentation');
    } else if (showReportGeneration === 'create') {
      setShowReportGenerationForm(true);
      setActiveTab('reportgeneration');
    } else if (showPerformanceAnalysis === 'create') {
      setShowPerformanceAnalysisForm(true);
      setActiveTab('performanceanalysis');
    } else if (showComplianceChecklist === 'create') {
      setShowComplianceChecklistForm(true);
      setActiveTab('compliancechecklistform'); // Set a unique activeTab
    } else if (showQualityIncidentReport === 'create') {
      setShowQualityIncidentReportForm(true);
      setActiveTab('qualityincidentreport'); // Set a unique activeTab
    } else if (showCompliance !== null) {
      setShowComplianceForm(true);
      setComplianceMode(showCompliance || 'list');
      setComplianceChecklistOpen(true); // Keep sidebar section open
      setComplianceSubMenuOpen(true); // Keep sidebar sub-section open
      setActiveTab('compliance');
    } else if (showTestCase !== null) {
      setShowTestCaseForm(true);
      setTestCaseMode(showTestCase || 'list');
      setProductTestingOpen(true); // Keep sidebar section open
      setTestCaseSubMenuOpen(true); // Keep sidebar sub-section open
      setActiveTab('testcase');
    } else if (showTrialPhase !== null) {
      setShowTrialPhaseForm(true);
      setTrialPhaseMode(showTrialPhase || 'list');
      setProductTestingOpen(true); // Keep sidebar section open
      setTestCaseSubMenuOpen(true); // Keep sidebar sub-section open
      setActiveTab('trialphase');
    } else if (showProductEntry === 'create') {
      setShowProductEntryForm(true);
      setActiveTab('productentry'); // Set a unique activeTab for product entry
    } else if (showStockMovement === 'create') {
      setShowStockMovementForm(true);
      setActiveTab('stockmovement'); // Set a unique activeTab for stock movement
    } else if (showStockMonitoring === 'create') {
      setShowStockMonitoringForm(true);
      setActiveTab('stockmonitoring'); // Set a unique activeTab for stock monitoring
    } else if (showCostTracking === 'create') {
      setShowCostTrackingForm(true);
      setActiveTab('costtracking'); // Set a unique activeTab for cost tracking
    } else if (showFieldActivityTracking === 'create') {
      setShowFieldActivityTrackingForm(true);
      setActiveTab('fieldactivitytracking'); // Set a unique activeTab
    } else if (showProductRegistration === 'create') {
      setShowProductRegistrationForm(true);
      setActiveTab('productregistration'); // Set a unique activeTab
    } else if (showEvidenceUpload === 'create') {
      setShowEvidenceUploadForm(true);
      setActiveTab('evidenceupload'); // Set a unique activeTab
    } else if (showTestScheduling === 'create') {
      setShowTestSchedulingForm(true);
      setActiveTab('testschedulingform'); // Set a unique activeTab
    } else if (showHistoricalData === 'create') {
      setShowHistoricalDataForm(true);
      setActiveTab('historicaldataform'); // Set a unique activeTab
    } else if (showDataVisualization === 'create') {
      setShowDataVisualizationForm(true);
      setActiveTab('datavisualizationform'); // Set a unique activeTab
    } else if (showResultsComparison === 'create') {
      setShowResultsComparisonForm(true);
      setActiveTab('resultscomparisonform'); // Set a unique activeTab
    } else if (showAddInventoryItem === 'create') {
      setShowAddInventoryItemForm(true);
      setActiveTab('addinventoryitemform'); // Set a unique activeTab
    } else if (showStockValuation === 'create') {
      setShowStockValuationForm(true);
      setActiveTab('stockvaluationform'); // Set a unique activeTab
    } else if (showExpiryAlertSetup === 'create') {
      setShowExpiryAlertSetupForm(true);
      setActiveTab('expiryalertsetupform'); // Set a unique activeTab
    } else if (showCustomReportBuilder === 'create') {
      setShowCustomReportBuilderForm(true);
      setActiveTab('customreportbuilderform'); // Set a unique activeTab
    } else if (showForecasting === 'create') {
      setShowForecastingForm(true);
      setActiveTab('forecastingform'); // Set a unique activeTab
    } else if (showProtocolRegistration === 'create') {
      setShowProtocolRegistrationForm(true);
      setActiveTab('protocolregistrationform'); // Set a unique activeTab
    } else if (showAlertConfiguration === 'create') {
      setShowAlertConfigurationForm(true);
      setActiveTab('alertconfigurationform'); // Set a unique activeTab
    } else if (showFeedbackCollection === 'create') {
      setShowFeedbackCollectionForm(true);
      setActiveTab('feedbackcollection'); // Set a unique activeTab
    } else if (showResourceAllocation === 'create') {
      setShowResourceAllocationForm(true);
      setActiveTab('resourceallocationform'); // Set a unique activeTab
    } else if (showTimeTracking === 'create') {
      setShowTimeTrackingForm(true);
      setActiveTab('timetrackingform'); // Set a unique activeTab
    } else {
      // If no specific form is requested via URL, show default tab (e.g., soil)
      setActiveTab('soil');
      // Ensure default sidebar sections are open if needed
      setProductTestingOpen(true);
      setTestCaseSubMenuOpen(true);
    }

  }, [location.search, navigate]); // Depend on location.search to re-run on URL change

  useEffect(() => {
    // Fetch user data or verify authentication
    const checkAuth = async () => {
      setIsLoading(true);
      try {
        // Verify authentication and get current user
        const currentUser = authService.getCurrentUser();
        if (!currentUser) {
          navigate('/login');
          return;
        }
        
        // Set user info for display in the profile dropdown
        setUserInfo(currentUser);
        
        // Log user data for debugging purposes
        console.log('Current user:', currentUser);
        
        // Fetch total test case count
        const testCaseCountResponse = await axios.get('/api/testcases/count');
        setTotalTestCases(testCaseCountResponse.data);

        // Fetch failed test results count
        const failedTestResultCountResponse = await axios.get('/api/testresults/failed/count');
        setFailedTestResults(failedTestResultCountResponse.data);

        // Mock data for dashboard tables (KEEP FOR TABLES)
        setSoilTests([
          { id: 'ST-1001', farmName: 'Green Valley Farm', date: '2023-06-15', pH: 6.8, status: 'Completed' },
          { id: 'ST-1002', farmName: 'Sunshine Acres', date: '2023-06-17', pH: 7.2, status: 'Completed' },
          { id: 'ST-1003', farmName: 'Hillside Orchard', date: '2023-06-18', pH: 6.5, status: 'Pending' },
          { id: 'ST-1004', farmName: 'Riverside Plantation', date: '2023-06-20', pH: 5.9, status: 'Processing' },
          { id: 'ST-1005', farmName: 'Mountain View Farm', date: '2023-06-21', pH: 7.4, status: 'Completed' },
        ]);
        
        setWaterTests([
          { id: 'WT-501', source: 'Irrigation Well 1', farmName: 'Green Valley Farm', date: '2023-06-14', pH: 7.1, turbidity: '12 NTU', ecoli: 'Negative', nitrate: '4.2 mg/L', hardness: '120 mg/L', status: 'Completed' },
          { id: 'WT-502', source: 'Reservoir', farmName: 'Sunshine Acres', date: '2023-06-16', pH: 6.9, turbidity: '8 NTU', ecoli: 'Negative', nitrate: '2.8 mg/L', hardness: '95 mg/L', status: 'Completed' },
          { id: 'WT-503', source: 'Creek', farmName: 'Hillside Orchard', date: '2023-06-18', pH: 7.3, turbidity: '15 NTU', ecoli: 'Positive', nitrate: '5.6 mg/L', hardness: '150 mg/L', status: 'Attention' },
        ]);
        
        setEquipment([
          { id: 'EQ-101', name: 'Soil pH Meter', model: 'SoilPro X5', lastCalibration: '2023-05-01', nextCalibration: '2023-08-01', status: 'Available' },
          { id: 'EQ-102', name: 'Moisture Analyzer', model: 'HydroSense 2000', lastCalibration: '2023-05-15', nextCalibration: '2023-08-15', status: 'In Use' },
          { id: 'EQ-103', name: 'Nutrient Test Kit', model: 'NutriTest Plus', lastCalibration: '2023-04-20', nextCalibration: '2023-07-20', status: 'Available' },
          { id: 'EQ-104', name: 'Conductivity Meter', model: 'EC-500', lastCalibration: '2023-06-01', nextCalibration: '2023-09-01', status: 'Maintenance' },
          { id: 'EQ-105', name: 'Spectrophotometer', model: 'SpectraScan Pro', lastCalibration: '2023-05-20', nextCalibration: '2023-08-20', status: 'Available' },
        ]);
        
        setScheduledTests([
          { id: 'SCH-201', testType: 'Soil Analysis', farmName: 'Sunrise Organic Farm', location: 'Field 1', scheduledDate: '2023-06-25', technician: 'John Smith', priority: 'Normal' },
          { id: 'SCH-202', testType: 'Water Quality', farmName: 'Green Valley Farm', location: 'Irrigation Pond', scheduledDate: '2023-06-26', technician: 'Maria Rodriguez', priority: 'High' },
          { id: 'SCH-203', testType: 'Pesticide Residue', farmName: 'Hillside Orchard', location: 'Apple Section', scheduledDate: '2023-06-27', technician: 'David Chen', priority: 'Normal' },
          { id: 'SCH-204', testType: 'Soil Microbial Analysis', farmName: 'Meadowbrook Farm', location: 'North Field', scheduledDate: '2023-06-28', technician: 'Sarah Johnson', priority: 'Normal' },
          { id: 'SCH-205', testType: 'Heavy Metal Screening', farmName: 'Riverside Plantation', location: 'Vegetable Plots', scheduledDate: '2023-06-28', technician: 'James Wilson', priority: 'Urgent' },
        ]);

        setIsLoading(false);
      } catch (error) {
        console.error('Authentication error:', error);
        navigate('/login');
      }
    };

    checkAuth();
  }, [navigate]);

  const handleLogout = async () => {
    try {
      // Call the logout API
      await logout();
      
      // Reset user info
      setUserInfo(null);
      
      // Close dropdown menu
      setUserDropdownOpen(false);
      
      // Navigate to login
      navigate('/login');
    } catch (error) {
      console.error('Logout error:', error);
    }
  };

  const handleFilter = () => {
    // Implement filtering logic
    alert(`Filtering with: ${activeFilter}=${filterValue}, ${configFilter}=${configValue}`);
  };

  const resetFilters = () => {
    setFilterValue('');
    setConfigValue('');
  };

  const toggleSidebar = () => {
    setSidebarCollapsed(!sidebarCollapsed);
  };

  // Function to render the status badge
  const renderStatusBadge = (status) => {
    switch (status.toLowerCase()) {
      case 'completed':
        return <span className="status-badge completed"><FaCheck /> Completed</span>;
      case 'pending':
        return <span className="status-badge pending">Pending</span>;
      case 'processing':
        return <span className="status-badge processing">Processing</span>;
      case 'attention':
        return <span className="status-badge attention"><FaExclamationTriangle /> Attention Required</span>;
      case 'available':
        return <span className="status-badge available">Available</span>;
      case 'in use':
        return <span className="status-badge in-use">In Use</span>;
      case 'maintenance':
        return <span className="status-badge maintenance">Maintenance</span>;
      case 'high':
        return <span className="status-badge high">High</span>;
      case 'normal':
        return <span className="status-badge normal">Normal</span>;
      case 'urgent':
        return <span className="status-badge urgent">Urgent</span>;
      default:
        return <span className="status-badge">{status}</span>;
    }
  };

  // Close dropdowns when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (adminOpen && !event.target.closest('.user-profile-container')) {
        setAdminOpen(false);
      }
      if (userDropdownOpen && !event.target.closest('.user-profile-container')) {
        setUserDropdownOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [adminOpen, userDropdownOpen]);

  // Add search and pagination state
  const [soilSearchTerm, setSoilSearchTerm] = useState('');
  const [waterSearchTerm, setWaterSearchTerm] = useState('');
  const [equipmentSearchTerm, setEquipmentSearchTerm] = useState('');
  const [scheduledSearchTerm, setScheduledSearchTerm] = useState('');
  
  const [soilPage, setSoilPage] = useState(1);
  const [waterPage, setWaterPage] = useState(1);
  const [equipmentPage, setEquipmentPage] = useState(1);
  const [scheduledPage, setScheduledPage] = useState(1);
  
  const [itemsPerPage] = useState(5);

  // Filter soil tests based on search term
  const filteredSoilTests = soilTests.filter(test => {
    if (!soilSearchTerm) return true;
    const searchLower = soilSearchTerm.toLowerCase();
    return (
      test.id.toLowerCase().includes(searchLower) ||
      test.farmName.toLowerCase().includes(searchLower) ||
      test.date.includes(searchLower) ||
      test.status.toLowerCase().includes(searchLower) ||
      test.pH.toString().includes(searchLower)
    );
  });

  // Filter water tests based on search term
  const filteredWaterTests = waterTests.filter(test => {
    if (!waterSearchTerm) return true;
    const searchLower = waterSearchTerm.toLowerCase();
    return (
      test.id.toLowerCase().includes(searchLower) ||
      test.source.toLowerCase().includes(searchLower) ||
      test.farmName.toLowerCase().includes(searchLower) ||
      test.date.includes(searchLower) ||
      test.status.toLowerCase().includes(searchLower)
    );
  });

  // Filter equipment based on search term
  const filteredEquipment = equipment.filter(item => {
    if (!equipmentSearchTerm) return true;
    const searchLower = equipmentSearchTerm.toLowerCase();
    return (
      item.id.toLowerCase().includes(searchLower) ||
      item.name.toLowerCase().includes(searchLower) ||
      item.model.toLowerCase().includes(searchLower) ||
      item.status.toLowerCase().includes(searchLower)
    );
  });

  // Filter scheduled tests based on search term
  const filteredScheduledTests = scheduledTests.filter(schedule => {
    if (!scheduledSearchTerm) return true;
    const searchLower = scheduledSearchTerm.toLowerCase();
    return (
      schedule.id.toLowerCase().includes(searchLower) ||
      schedule.testType.toLowerCase().includes(searchLower) ||
      schedule.farmName.toLowerCase().includes(searchLower) ||
      schedule.location.toLowerCase().includes(searchLower) ||
      schedule.technician.toLowerCase().includes(searchLower)
    );
  });

  // Pagination logic for soil tests
  const soilIndexOfLastItem = soilPage * itemsPerPage;
  const soilIndexOfFirstItem = soilIndexOfLastItem - itemsPerPage;
  const currentSoilTests = filteredSoilTests.slice(soilIndexOfFirstItem, soilIndexOfLastItem);
  const totalSoilPages = Math.ceil(filteredSoilTests.length / itemsPerPage);

  // Pagination logic for water tests
  const waterIndexOfLastItem = waterPage * itemsPerPage;
  const waterIndexOfFirstItem = waterIndexOfLastItem - itemsPerPage;
  const currentWaterTests = filteredWaterTests.slice(waterIndexOfFirstItem, waterIndexOfLastItem);
  const totalWaterPages = Math.ceil(filteredWaterTests.length / itemsPerPage);

  // Pagination logic for equipment
  const equipmentIndexOfLastItem = equipmentPage * itemsPerPage;
  const equipmentIndexOfFirstItem = equipmentIndexOfLastItem - itemsPerPage;
  const currentEquipment = filteredEquipment.slice(equipmentIndexOfFirstItem, equipmentIndexOfLastItem);
  const totalEquipmentPages = Math.ceil(filteredEquipment.length / itemsPerPage);

  // Pagination logic for scheduled tests
  const scheduledIndexOfLastItem = scheduledPage * itemsPerPage;
  const scheduledIndexOfFirstItem = scheduledIndexOfLastItem - itemsPerPage;
  const currentScheduledTests = filteredScheduledTests.slice(scheduledIndexOfFirstItem, scheduledIndexOfLastItem);
  const totalScheduledPages = Math.ceil(filteredScheduledTests.length / itemsPerPage);

  // Handle pagination for soil tests
  const paginateSoil = (pageNumber) => setSoilPage(pageNumber);
  
  // Handle pagination for water tests
  const paginateWater = (pageNumber) => setWaterPage(pageNumber);
  
  // Handle pagination for equipment
  const paginateEquipment = (pageNumber) => setEquipmentPage(pageNumber);
  
  // Handle pagination for scheduled tests
  const paginateScheduled = (pageNumber) => setScheduledPage(pageNumber);

  // Render pagination controls
  const renderPagination = (currentPage, totalPages, paginateFunction) => {
    if (totalPages <= 1) return null;

    return (
      <div className="table-pagination">
        <button 
          className="pagination-button" 
          onClick={() => paginateFunction(1)} 
          disabled={currentPage === 1}
        >
          &laquo;
        </button>
        <button 
          className="pagination-button" 
          onClick={() => paginateFunction(currentPage - 1)} 
          disabled={currentPage === 1}
        >
          &lt;
        </button>
        
        {Array.from({ length: Math.min(5, totalPages) }).map((_, index) => {
          let pageNumber;
          
          if (totalPages <= 5) {
            pageNumber = index + 1;
          } else {
            if (currentPage <= 3) {
              pageNumber = index + 1;
            } else if (currentPage >= totalPages - 2) {
              pageNumber = totalPages - 4 + index;
            } else {
              pageNumber = currentPage - 2 + index;
            }
          }
          
          return (
            <button
              key={pageNumber}
              className={`pagination-button ${currentPage === pageNumber ? 'active' : ''}`}
              onClick={() => paginateFunction(pageNumber)}
            >
              {pageNumber}
            </button>
          );
        })}
        
        <button 
          className="pagination-button" 
          onClick={() => paginateFunction(currentPage + 1)} 
          disabled={currentPage === totalPages}
        >
          &gt;
        </button>
        <button 
          className="pagination-button" 
          onClick={() => paginateFunction(totalPages)} 
          disabled={currentPage === totalPages}
        >
          &raquo;
        </button>
      </div>
    );
  };

  // Render the appropriate form based on the query parameters
  const renderForm = () => {
    if (showComplianceForm) {
      return <ComplianceForm />;
    } else if (showTestCaseForm) {
      return <TestCaseForm />;
    } else if (showTrialPhaseForm) {
      return <TrialPhaseForm />;
    } else if (showTestDocumentationForm) {
      return <TestDocumentationForm />;
    } else if (showProductEntryForm) {
      return <ProductEntryForm />;
    } else if (showStockMovementForm) {
      return <StockMovementForm />;
    } else if (showStockMonitoringForm) {
      return <StockMonitoringForm />;
    } else if (showReportGenerationForm) {
      return <ReportGenerationForm />;
    } else if (showPerformanceAnalysisForm) {
      return <PerformanceAnalysisForm />;
    } else if (showComplianceChecklistForm) {
      return <ComplianceChecklistForm />;
    } else if (showQualityIncidentReportForm) {
      return <QualityIncidentReportForm />;
    } else if (showBroadcastAnnouncementForm) {
      return <BroadcastAnnouncementForm />;
    } else if (showTaskAssignmentForm) {
      return <TaskAssignmentForm />;
    } else if (showAutomatedAlertForm) {
      return <AutomatedAlertForm />;
    } else if (showCalendarManagementForm) {
      return <CalendarManagementForm />;
    } else if (showUserActivityLogForm) {
      return <UserActivityLogForm />;
    } else if (showAuditTrailForm) {
      return <AuditTrailForm />;
    } else if (showCostTrackingForm) {
      return <CostTrackingForm />;
    } else if (showFieldActivityTrackingForm) {
      return <FieldActivityTrackingForm />;
    } else if (showProductRegistrationForm) {
      return <ProductRegistrationForm />;
    } else if (showEvidenceUploadForm) {
      return <EvidenceUploadForm />;
    } else if (showTestSchedulingForm) {
      return <TestSchedulingForm />;
    } else if (showHistoricalDataForm) {
      return <HistoricalDataForm />;
    } else if (showDataVisualizationForm) {
      return <DataVisualizationForm />;
    } else if (showResultsComparisonForm) {
      return <ResultsComparisonForm />;
    } else if (showAddInventoryItemForm) {
      return <AddInventoryItemForm />;
    } else if (showStockValuationForm) {
      return <StockValuationForm />;
    } else if (showExpiryAlertSetupForm) {
      return <ExpiryAlertSetupForm />;
    } else if (showCustomReportBuilderForm) {
      return <CustomReportBuilderForm />;
    } else if (showForecastingForm) {
      return <ForecastingForm />;
    } else if (showProtocolRegistrationForm) {
      return <ProtocolRegistrationForm />;
    } else if (showAlertConfigurationForm) {
      return <AlertConfigurationForm />;
    } else if (showFeedbackCollectionForm) {
      return <FeedbackCollectionForm />;
    } else if (showBroadcastMessageForm) {
      return <BroadcastMessageForm />;
    } else if (showResourceAllocationForm) {
      return <ResourceAllocationForm />;
    } else if (showTimeTrackingForm) {
      return <TimeTrackingForm />;
    }
    return null; // Or a default dashboard view
  };

  // Add cleanup effect when activeTab changes
  useEffect(() => {
    // Reset all form states when switching tabs
    if (activeTab !== 'trialphase') {
      setShowTrialPhaseForm(false);
      setTrialPhaseMode('list');
    }
    if (activeTab !== 'broadcastannouncementform') {
      setShowBroadcastAnnouncementForm(false);
    }
  }, [activeTab]);

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
                      <div className="menu-item" onClick={() => {
                        setShowTestCaseForm(true);
                        setTestCaseMode('create');
                        setActiveTab('testcase');
                        navigate('/dashboard?CreatingTestCases=create', { replace: true });
                      }}>Creating Test Cases</div>
                      <div className="menu-item" onClick={() => {
                        setShowTrialPhaseForm(true);
                        setTrialPhaseMode('create');
                        setActiveTab('trialphase');
                        setShowBroadcastAnnouncementForm(false); // Reset broadcast form
                        navigate('/dashboard?TrialPhaseTrackingForm=create', { replace: true });
                      }}>Trial Phase Tracking</div>
                      <div className="menu-item" onClick={() => {
                        setShowTestDocumentationForm(true);
                        setActiveTab('testdocumentation');
                        navigate('/dashboard?CreatingTestDocumentation=create', { replace: true });
                      }}>Test Documentation</div>
                      <div className="menu-item" onClick={() => {
                        setShowProductRegistrationForm(true);
                        setActiveTab('productregistration');
                        navigate('/dashboard?ProductRegistrationForm=create', { replace: true });
                      }}>Product Registration</div>
                      <div className="menu-item" onClick={() => {
                        setShowEvidenceUploadForm(true);
                        setActiveTab('evidenceupload');
                        navigate('/dashboard?EvidenceUploadForm=create', { replace: true });
                      }}>Evidence Upload</div>
                      <div className="menu-item" onClick={() => {
                        setShowTestSchedulingForm(true);
                        setActiveTab('testschedulingform');
                        navigate('/dashboard?TestSchedulingForm=create', { replace: true });
                      }}>Total Product</div>
                      <div className="menu-item" onClick={() => {
                        setShowHistoricalDataForm(true);
                        setActiveTab('historicaldataform');
                        navigate('/dashboard?HistoricalDataForm=create', { replace: true });
                      }}>Historical Data</div>
                      <div className="menu-item" onClick={() => {
                        setShowDataVisualizationForm(true);
                        setActiveTab('datavisualizationform');
                        navigate('/dashboard?DataVisualizationForm=create', { replace: true });
                      }}>Data Visualization</div>
                      <div className="menu-item" onClick={() => {
                        setShowResultsComparisonForm(true);
                        setActiveTab('resultscomparisonform');
                        navigate('/dashboard?ResultsComparisonForm=create', { replace: true });
                      }}>Results Comparison</div>
                      <div className="menu-item">Weather Integration</div>
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
                  <div className="sub-header" onClick={() => setComplianceSubMenuOpen(!complianceSubMenuOpen)}>
                    {complianceSubMenuOpen ? <FaChevronDown className="toggle-icon-sub" /> : <FaChevronRight className="toggle-icon-sub" />}
                    <span className="sub-title">Compliance Management</span>
                  </div>
                  
                  {complianceSubMenuOpen && (
                    <div className="sub-content">
                      <div className="menu-item" onClick={() => {
                        setShowComplianceForm(true);
                        setComplianceMode('create');
                        setActiveTab('compliance');
                        navigate('/dashboard?ComplianceChecklist=create', { replace: true });
                      }}>Create Checklist</div>
                      <div className="menu-item" onClick={() => {
                        setShowComplianceForm(true);
                        setComplianceMode('list');
                        setActiveTab('compliance');
                        navigate('/dashboard?ComplianceChecklist=list', { replace: true });
                      }}>View All Checklists</div>
                      <div className="menu-item" onClick={() => {
                        setShowComplianceForm(true);
                        setComplianceMode('list');
                        setActiveTab('compliance');
                        navigate('/dashboard?ComplianceChecklist=recent', { replace: true });
                      }}>Recent Compliance</div>
                      <div className="menu-item" onClick={() => {
                        setShowComplianceForm(true);
                        setComplianceMode('list');
                        setActiveTab('compliance');
                        navigate('/dashboard?ComplianceChecklist=by-product', { replace: true });
                      }}>Compliance by Product</div>
                      <div className="menu-item" onClick={() => {
                        setShowComplianceForm(true);
                        setComplianceMode('list');
                        setActiveTab('compliance');
                        navigate('/dashboard?ComplianceChecklist=reports', { replace: true });
                      }}>Compliance Reports</div>
                      <div className="menu-item" onClick={() => {
                        setShowComplianceForm(true);
                        setComplianceMode('list');
                        setActiveTab('compliance');
                        navigate('/dashboard?ComplianceChecklist=status', { replace: true });
                      }}>Compliance Status</div>
                      <div className="menu-item" onClick={() => {
                        setShowComplianceForm(true);
                        setComplianceMode('list');
                        setActiveTab('compliance');
                        navigate('/dashboard?ComplianceChecklist=trends', { replace: true });
                      }}>Compliance Trends</div>
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
                      <div className="menu-item" onClick={() => {
                        setShowProductEntryForm(true);
                        setActiveTab('productentry');
                        navigate('/dashboard?ProductEntry=create', { replace: true });
                      }}>Product Entry</div>
                      <div className="menu-item" onClick={() => {
                        setShowStockMovementForm(true);
                        setActiveTab('stockmovement');
                        navigate('/dashboard?StockMovement=create', { replace: true });
                      }}>Stock Movement</div>
                      <div className="menu-item" onClick={() => {
                        setShowStockMonitoringForm(true);
                        setActiveTab('stockmonitoring');
                        navigate('/dashboard?StockMonitoringForm=create', { replace: true });
                      }}>Stock Monitoring Form</div>
                      <div className="menu-item" onClick={() => {
                        setShowAddInventoryItemForm(true);
                        setActiveTab('addinventoryitemform');
                        navigate('/dashboard?AddInventoryItemForm=create', { replace: true });
                      }}>Add Inventory Item</div>
                      <div className="menu-item" onClick={() => {
                        setShowExpiryAlertSetupForm(true);
                        setActiveTab('expiryalertsetupform');
                        navigate('/dashboard?ExpiryAlertSetupForm=create', { replace: true });
                      }}>Expiration Alerts</div>
                      <div className="menu-item">Real-time Tracking</div>
                      <div className="menu-item">Valuation Reports</div>
                      <div className="menu-item" onClick={() => {
                        setShowStockValuationForm(true);
                        setActiveTab('stockvaluationform');
                        navigate('/dashboard?StockValuationForm=create', { replace: true });
                      }}>Stock Valuation</div>
                      <div className="menu-item" onClick={() => navigate('/products-management')}>Product Management</div>
                      <div className="menu-item">Quality Control</div>
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
                      <div className="menu-item" onClick={() => {
                        setShowReportGenerationForm(true);
                        setActiveTab('reportgeneration');
                        navigate('/dashboard?ReportGeneration=create', { replace: true });
                      }}>Report Generation Form</div>
                      <div className="menu-item" onClick={() => {
                        setShowPerformanceAnalysisForm(true);
                        setActiveTab('performanceanalysis');
                        navigate('/dashboard?PerformanceAnalysis=create', { replace: true });
                      }}>Performance Analysis Form</div>
                      <div className="menu-item" onClick={() => {
                        setShowCustomReportBuilderForm(true);
                        setActiveTab('customreportbuilderform');
                        navigate('/dashboard?CustomReportBuilderForm=create', { replace: true });
                      }}>Custom Report Builder</div>
                      <div className="menu-item">Data Visualization</div>
                      <div className="menu-item">Seasonal Analytics</div>
                      <div className="menu-item">Success Rate Analysis</div>
                      <div className="menu-item">Product Effectiveness</div>
                      <div className="menu-item">Export Tools</div>
                      <div className="menu-item">Report Scheduling</div>
                      <div className="menu-item">Trend Analysis</div>
                      <div className="menu-item" onClick={() => {
                        setShowForecastingForm(true);
                        setActiveTab('forecastingform');
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
                      <div className="menu-item">Role Management</div>
                      <div className="menu-item">Multi-factor Authentication</div>
                      <div className="menu-item" onClick={() => {
                        setShowUserActivityLogForm(true);
                        setActiveTab('useractivitylogform');
                        navigate('/dashboard?UserActivityLogForm=create', { replace: true });
                      }}>User Activity Log</div>
                      <div className="menu-item" onClick={() => {
                        setShowAuditTrailForm(true);
                        setActiveTab('audittrailform');
                        navigate('/dashboard?AuditTrailForm=create', { replace: true });
                      }}>Audit Trails</div>
                      <div className="menu-item">Password Policies</div>
                      <div className="menu-item">Session Management</div>
                      <div className="menu-item">Data Encryption</div>
                      <div className="menu-item">Backup & Recovery</div>
                      <div className="menu-item">API Security</div>
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
                      <div className="menu-item" onClick={() => {
                        setShowAutomatedAlertForm(true);
                        setActiveTab('automatedalertform');
                        navigate('/dashboard?AutomatedAlertForm=create', { replace: true });
                      }}>Automated Alerts</div>
                      <div className="menu-item">SMS Notifications</div>
                      <div className="menu-item">Announcements</div>
                      <div className="menu-item">Task Assignments</div>
                      <div className="menu-item">Reminders</div>
                      <div className="menu-item">Status Updates</div>
                      <div className="menu-item">Emergency Alerts</div>
                      <div className="menu-item" onClick={() => {
                        setShowFeedbackCollectionForm(true);
                        setActiveTab('feedbackcollection');
                        navigate('/dashboard?FeedbackCollection=create', { replace: true });
                      }}>Feedback Collection</div>
                      <div className="menu-item" onClick={() => {
                        setShowBroadcastAnnouncementForm(true);
                        setActiveTab('broadcastannouncementform');
                        navigate('/dashboard?BroadcastAnnouncementForm=create', { replace: true });
                      }}>Broadcast Announcement</div>
                      <div className="menu-item" onClick={() => {
                        setShowTaskAssignmentForm(true);
                        setActiveTab('taskassignmentform');
                        navigate('/dashboard?TaskAssignmentForm=create', { replace: true });
                      }}>Task Assignment Form</div>
                      {/* Add Alert Configuration Menu Item */}
                      <div className="menu-item" onClick={() => {
                        setShowAlertConfigurationForm(true);
                        setActiveTab('alertconfigurationform');
                        navigate('/dashboard?AlertConfigurationForm=create', { replace: true });
                      }}>Alert Configuration</div>
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
                      <div className="menu-item" onClick={() => {
                        setShowCalendarManagementForm(true);
                        setActiveTab('calendarmanagementform');
                        navigate('/dashboard?CalendarManagementForm=create', { replace: true });
                      }}>Calendar Management</div>
                      <div className="menu-item">Task Scheduling</div>
                      <div className="menu-item" onClick={() => {
                        setShowFieldActivityTrackingForm(true);
                        setActiveTab('fieldactivitytracking');
                        navigate('/dashboard?FieldActivityTrackingForm=create', { replace: true });
                      }}>Field Activity Tracking</div>
                      <div className="menu-item" onClick={() => {
                        setShowCostTrackingForm(true);
                        setActiveTab('costtracking');
                        navigate('/dashboard?CostTrackingForm=create', { replace: true });
                      }}>Cost Tracking</div>
                      <div className="menu-item" onClick={() => {
                        setShowTimeTrackingForm(true);
                        setActiveTab('timetrackingform');
                        navigate('/dashboard?TimeTrackingForm=create', { replace: true });
                      }}>Time Tracking</div>
                      <div className="menu-item" onClick={() => {
                        setShowResourceAllocationForm(true);
                        // setActiveTab might not be needed if rendering is solely based on showResourceAllocationForm
                        navigate('/dashboard?ResourceAllocationForm=create', { replace: true });
                      }}>Resource Allocation</div>
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>

          {/* 7. QUALITY CONTROL AND COMPLIANCE */}
          <div className="sidebar-section">
            <div className="section-header" onClick={() => setQualityControlOpen(!qualityControlOpen)}>
              {qualityControlOpen ? <FaChevronDown className="toggle-icon" /> : <FaChevronRight className="toggle-icon" />}
              <FaClipboardCheck className="sidebar-icon" /> 
              <span className="section-title">Quality Control and Compliance</span>
            </div>
            
            {qualityControlOpen && (
              <div className="section-content">
                <div className="sub-section">
                  <div className="sub-header" onClick={() => setComplianceSubMenuOpenNew(!complianceSubMenuOpenNew)}>
                    {complianceSubMenuOpenNew ? <FaChevronDown className="toggle-icon-sub" /> : <FaChevronRight className="toggle-icon-sub" />}
                    <span className="sub-title">Compliance</span>
                  </div>
                  
                  {complianceSubMenuOpenNew && (
                    <div className="sub-content">
                      <div className="menu-item" onClick={() => {
                        setShowComplianceChecklistForm(true);
                        setActiveTab('compliancechecklistform');
                        navigate('/dashboard?ComplianceChecklistForm=create', { replace: true });
                      }}>Compliance Checklist</div>
                      <div className="menu-item" onClick={() => {
                        setShowQualityIncidentReportForm(true);
                        setActiveTab('qualityincidentreportform');
                        navigate('/dashboard?QualityIncidentReportForm=create', { replace: true });
                      }}>Quality Incident Report</div>
                      {/* Add Protocol Registration Menu Item */}
                      <div className="menu-item" onClick={() => {
                        setShowProtocolRegistrationForm(true);
                        setActiveTab('protocolregistrationform');
                        navigate('/dashboard?ProtocolRegistrationForm=create', { replace: true });
                      }}>Protocol Registration</div>
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>
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
                <h3>Soil Tests</h3>
                <p className={`stat-number ${isSoilMax ? 'max-value' : ''}`}>{animatedSoilCount}</p>
                <p className="stat-label">in the last 30 days</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon water-icon">
                <FaWater />
              </div>
              <div className="stat-content">
                <h3>Water Tests</h3>
                <p className={`stat-number ${isWaterMax ? 'max-value' : ''}`}>{animatedWaterCount}</p>
                <p className="stat-label">in the last 30 days</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon equipment-icon">
                <FaTools />
              </div>
              <div className="stat-content">
                <h3>Equipment</h3>
                <p className={`stat-number ${isEquipmentMax ? 'max-value' : ''}`}>{animatedEquipmentCount}</p>
                <p className="stat-label">total devices</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon scheduled-icon">
                <FaCalendarAlt />
              </div>
              <div className="stat-content">
                <h3>Scheduled</h3>
                <p className={`stat-number ${isScheduledMax ? 'max-value' : ''}`}>{animatedScheduledCount}</p>
                <p className="stat-label">upcoming tests</p>
              </div>
            </div>
          </div>

          <div className="dashboard-tabs">
            <div 
              className={`dashboard-tab ${activeTab === 'soil' ? 'active' : ''}`}
              onClick={() => {
                setActiveTab('soil');
                setShowComplianceForm(false);
                setShowTestDocumentationForm(false);
              }}
            >
              <FaLeaf /> Soil Tests
            </div>
            <div 
              className={`dashboard-tab ${activeTab === 'water' ? 'active' : ''}`}
              onClick={() => {
                setActiveTab('water');
                setShowComplianceForm(false);
                setShowTestDocumentationForm(false);
              }}
            >
              <FaWater /> Water Tests
            </div>
            <div 
              className={`dashboard-tab ${activeTab === 'equipment' ? 'active' : ''}`}
              onClick={() => {
                setActiveTab('equipment');
                setShowComplianceForm(false);
                setShowTestDocumentationForm(false);
              }}
            >
              <FaTools /> Equipment
            </div>
            <div 
              className={`dashboard-tab ${activeTab === 'scheduled' ? 'active' : ''}`}
              onClick={() => {
                setActiveTab('scheduled');
                setShowComplianceForm(false);
                setShowTestDocumentationForm(false);
              }}
            >
              <FaCalendarAlt /> Scheduled Tests
            </div>
            <div 
              className={`dashboard-tab ${activeTab === 'compliance' ? 'active' : ''}`}
              onClick={() => {
                setActiveTab('compliance');
                setShowComplianceForm(true);
                setShowTestDocumentationForm(false);
                setComplianceMode(complianceMode || 'list');
                navigate('/dashboard?ComplianceChecklist=' + (complianceMode || 'list'), { replace: true });
              }}
            >
              <FaClipboardCheck /> Compliance Checklist
            </div>
            <div 
              className={`dashboard-tab ${activeTab === 'testcase' ? 'active' : ''}`}
              onClick={() => {
                setActiveTab('testcase');
                setShowTestCaseForm(true);
                setShowTestDocumentationForm(false);
                setTestCaseMode(testCaseMode || 'list');
                navigate('/dashboard?CreatingTestCases=' + (testCaseMode || 'list'), { replace: true });
              }}
            >
              <FaFlask /> Product Registration
            </div>
            <div 
              className={`dashboard-tab ${activeTab === 'trialphase' ? 'active' : ''}`}
              onClick={() => {
                setActiveTab('trialphase');
                setShowTrialPhaseForm(true);
                setShowTestDocumentationForm(false);
                setTrialPhaseMode(trialPhaseMode || 'list');
                navigate('/dashboard?TrialPhaseTrackingForm=' + (trialPhaseMode || 'list'), { replace: true });
              }}
            >
              <FaFlask /> Trial Phase Tracking
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
                    <button className="table-action-btn">Export CSV</button>
                    <div className="table-filter">
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
                          <th>Compliance Rate</th>
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
                          <td>100%</td>
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
                          <td>80%</td>
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
                          <td>40%</td>
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
                   'Product Registrations'}
                </h2>
                {testCaseMode === 'list' && (
                  <div className="table-actions">
                    <div className="table-search">
                      <input
                        type="text"
                        placeholder="Search test cases..."
                        className="search-input"
                      />
                      <FaSearch className="search-icon" />
                    </div>
                    <button 
                      className="table-action-btn" 
                      onClick={() => {
                        setTestCaseMode('create');
                        navigate('/dashboard?CreatingTestCases=create', { replace: true });
                      }}
                    >
                      <FaPlus /> Register Product
                    </button>
                    <button className="table-action-btn">Export CSV</button>
                    <div className="table-filter">
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
                  <div className="data-table">
                    <table>
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Test Name</th>
                          <th>Product Type</th>
                          <th>Batch Number</th>
                          <th>Location</th>
                          <th>Start Date</th>
                          <th>Actions</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td>PR-1001</td>
                          <td>Fungicide Efficacy Test</td>
                          <td>Fungicide</td>
                          <td>FNG-2023-0342</td>
                          <td>Field Station Alpha</td>
                          <td>2023-10-15</td>
                          <td>
                            <button 
                              className="action-button" 
                              onClick={() => {
                                setTestCaseMode('view');
                                navigate('/dashboard?CreatingTestCases=view&id=1', { replace: true });
                              }}
                            >
                              View
                            </button>
                            <button className="action-button delete">Delete</button>
                          </td>
                        </tr>
                        <tr>
                          <td>PR-1002</td>
                          <td>Herbicide Performance Assessment</td>
                          <td>Herbicide</td>
                          <td>HRB-2023-0187</td>
                          <td>Field Station Beta</td>
                          <td>2023-09-20</td>
                          <td>
                            <button 
                              className="action-button"
                              onClick={() => {
                                setTestCaseMode('view');
                                navigate('/dashboard?CreatingTestCases=view&id=2', { replace: true });
                              }}
                            >
                              View
                            </button>
                            <button className="action-button delete">Delete</button>
                          </td>
                        </tr>
                        <tr>
                          <td>PR-1003</td>
                          <td>Insecticide Longevity Study</td>
                          <td>Insecticide</td>
                          <td>INS-2023-0489</td>
                          <td>Greenhouse C</td>
                          <td>2023-11-05</td>
                          <td>
                            <button 
                              className="action-button"
                              onClick={() => {
                                setTestCaseMode('view');
                                navigate('/dashboard?CreatingTestCases=view&id=3', { replace: true });
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
                    <button className="table-action-btn">Export CSV</button>
                    <div className="table-filter">
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
                      alert('Trial phase data saved successfully!');
                      setTrialPhaseMode('list');
                      navigate('/dashboard?TrialPhaseTrackingForm=list', { replace: true });
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
                          <th>Test Case ID</th>
                          <th>Phase Name</th>
                          <th>Date</th>
                          <th>Weather</th>
                          <th>Status</th>
                          <th>Actions</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td>TC-001</td>
                          <td>Initial</td>
                          <td>2024-03-15</td>
                          <td>25°C, 65%</td>
                          <td>{renderStatusBadge('Completed')}</td>
                          <td>
                            <button 
                              className="action-button"
                              onClick={() => {
                                setTrialPhaseMode('view');
                                navigate('/dashboard?TrialPhaseTrackingForm=view&id=1', { replace: true });
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
                      Showing 1-1 of 1 results
                    </div>
                  </div>
                </>
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
          {activeTab === 'compliancechecklistform' && showComplianceChecklistForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Compliance Checklist Form</h2>
              </div>
              <div className="dashboard-compliancechecklistform-wrapper">
                <ComplianceChecklistForm />
              </div>
            </div>
          )}
          {activeTab === 'qualityincidentreport' && showQualityIncidentReportForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Quality Incident Report Form</h2>
              </div>
              <div className="dashboard-qualityincidentreport-wrapper">
                <QualityIncidentReportForm />
              </div>
            </div>
          )}
          {activeTab === 'broadcastannouncementform' && showBroadcastAnnouncementForm && (
            <div className="table-container">
              <div className="table-header-actions">
                <h2>Broadcast Announcement Form</h2>
              </div>
              <div className="dashboard-broadcastannouncementform-wrapper">
                <BroadcastAnnouncementForm />
              </div>
            </div>
          )}
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
              <div className="table-header-actions">
                <h2>Product Registration Form</h2>
              </div>
              <div className="dashboard-productregistrationform-wrapper">
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
        </div>
      </div>
        </div>
    );
};

export default Dashboard; 