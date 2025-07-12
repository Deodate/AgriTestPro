import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { toast } from 'react-toastify';
import {
  FaArrowRight, FaPlus, FaList, FaCalendar, FaClipboard,
  FaFileAlt, FaCheckCircle, FaTimesCircle, FaClock
} from 'react-icons/fa';

// Components
import DashboardHeader from '../Components/Dashboard/DashboardHeader';
import DashboardSidebar from '../Components/Dashboard/DashboardSidebar';

const Dashboard = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  
  // Menu states for sidebar
  const [menuStates, setMenuStates] = useState({
    operationalTools: true,
    dailyOperations: true,
    qualityControl: false
  });

  // Stats for the dashboard
  const [stats, setStats] = useState({
    completedTests: 0,
    successfulTests: 0,
    failedTests: 0,
    scheduledTests: 0
  });

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

  // Action buttons configuration
  const actionButtons = [
    {
      title: 'Product List',
      icon: <FaList />,
      onClick: () => navigate('/dashboard/products')
    },
    {
      title: 'List Test Scheduling',
      icon: <FaCalendar />,
      onClick: () => navigate('/dashboard/test-scheduling')
    },
    {
      title: 'List Test Cases',
      icon: <FaClipboard />,
      onClick: () => navigate('/dashboard/test-cases')
    },
    {
      title: 'Create Test Documentation',
      icon: <FaFileAlt />,
      onClick: () => navigate('/dashboard/test-documentation/create')
    },
    {
      title: 'List of Evidence',
      icon: <FaList />,
      onClick: () => navigate('/dashboard/evidence')
    }
  ];

  return (
    <div className="dashboard-container">
      <DashboardHeader 
        user={user}
        handleLogout={handleLogout}
      />

      <div className="dashboard-content">
        <DashboardSidebar 
          menuStates={menuStates}
          toggleMenu={toggleMenu}
          sidebarCollapsed={sidebarCollapsed}
          toggleSidebar={toggleSidebar}
        />

        <main className="dashboard-main" style={{ backgroundColor: '#1976d2' }}>
          {/* Header with title and action button */}
          <div className="dashboard-page-header">
            <h1 className="page-title" style={{ color: '#fff' }}>Test Results & Equipment</h1>
            <button className="new-test-btn" onClick={() => navigate('/dashboard/new-test')}>
              New Test Request <FaArrowRight />
            </button>
          </div>

          {/* Stats Cards */}
          <div className="dashboard-stats">
            <div className="stat-card">
              <div className="stat-icon completed">
                <FaCheckCircle />
              </div>
              <div className="stat-content">
                <h3>Completed Tests</h3>
                <p className="stat-number">{stats.completedTests}</p>
                <p className="stat-label">All Phases Passed</p>
              </div>
            </div>

            <div className="stat-card">
              <div className="stat-icon successful">
                <FaCheckCircle />
              </div>
              <div className="stat-content">
                <h3>Successful Tests</h3>
                <p className="stat-number">{stats.successfulTests}</p>
              </div>
            </div>

            <div className="stat-card">
              <div className="stat-icon failed">
                <FaTimesCircle />
              </div>
              <div className="stat-content">
                <h3>Failed Tests</h3>
                <p className="stat-number">{stats.failedTests}</p>
              </div>
            </div>

            <div className="stat-card">
              <div className="stat-icon scheduled">
                <FaClock />
              </div>
              <div className="stat-content">
                <h3>Scheduled</h3>
                <p className="stat-number">{stats.scheduledTests}</p>
                <p className="stat-label">Upcoming Tests</p>
              </div>
            </div>
          </div>

          {/* Action Buttons */}
          <div className="dashboard-actions">
            {actionButtons.map((button, index) => (
              <button 
                key={index}
                className="action-button"
                onClick={button.onClick}
              >
                {button.icon}
                <span>{button.title}</span>
              </button>
            ))}
          </div>
        </main>
      </div>
    </div>
  );
};

export default Dashboard; 