import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
  FaArrowRight, FaLeaf, FaWater, FaTools, FaCalendarAlt,
  FaPlus, FaFilter, FaSearch
} from 'react-icons/fa';

const DashboardMain = ({
  activeTab,
  formStates,
  modeStates,
  dataStates,
  productStates,
  scheduleStates,
  handleProductUpdateSuccess,
  handleCancelEdit,
  handleDeleteProduct,
  handleDeleteTrialPhase,
  renderStatusBadge,
  renderPagination
}) => {
  const navigate = useNavigate();
  const { productSearchTerm, setProductSearchTerm } = productStates;

  const renderStats = () => (
    <div className="dashboard-stats">
      <div className="stat-card">
        <div className="stat-icon soil-icon">
          <FaLeaf />
        </div>
        <div className="stat-content">
          <h3>Completed Tests</h3>
          <p className="stat-number">{dataStates.totalTestCases}</p>
          <p className="stat-label">All Phases Passed</p>
        </div>
      </div>

      <div className="stat-card">
        <div className="stat-icon water-icon">
          <FaWater />
        </div>
        <div className="stat-content">
          <h3>Successful Tests</h3>
          <p className="stat-number">{dataStates.successfulTestCount}</p>
          <p className="stat-label"></p>
        </div>
      </div>

      <div className="stat-card">
        <div className="stat-icon equipment-icon">
          <FaTools />
        </div>
        <div className="stat-content">
          <h3>Failed Tests</h3>
          <p className="stat-number">{dataStates.failedTestCount}</p>
          <p className="stat-label"></p>
        </div>
      </div>

      <div className="stat-card">
        <div className="stat-icon scheduled-icon">
          <FaCalendarAlt />
        </div>
        <div className="stat-content">
          <h3>Scheduled</h3>
          <p className="stat-number">{dataStates.totalCalendarEvents}</p>
          <p className="stat-footer">Total events</p>
        </div>
      </div>
    </div>
  );

  const renderTabs = () => (
    <div className="dashboard-tabs">
      <div 
        className={`dashboard-tab ${activeTab === 'soil' ? 'active' : ''}`}
        onClick={() => navigate('/dashboard')}
      >
        <FaLeaf /> Product List
      </div>
      {/* Add more tabs */}
    </div>
  );

  const renderContent = () => {
    switch (activeTab) {
      case 'soil':
        return (
          <div className="table-container">
            <div className="table-header-actions">
              <h2>Product List</h2>
              <div className="table-actions">
                <div className="table-search">
                  <input
                    type="text"
                    placeholder="Search products..."
                    className="search-input"
                    value={productSearchTerm}
                    onChange={(e) => setProductSearchTerm(e.target.value)}
                  />
                  <FaSearch className="search-icon" />
                </div>
                <button className="table-action-btn">
                  <FaPlus /> Create New
                </button>
                <button className="table-action-btn hide-button">Export CSV</button>
                <div className="table-filter hide-button">
                  <FaFilter /> Filter
                </div>
              </div>
            </div>
            {/* Add table content */}
          </div>
        );
      // Add more cases for other tabs
      default:
        return null;
    }
  };

  return (
    <div className="dashboard-main">
      <div className="dashboard-page-header">
        <div className="header-left">
          <h1 className="page-title">Test Results & Equipment</h1>
        </div>
        <div className="header-right">
          <button className="primary-button" onClick={() => navigate('/dashboard/new-test')}>
            New Test Request <FaArrowRight />
          </button>
        </div>
      </div>

      {renderStats()}
      {renderTabs()}
      {renderContent()}
    </div>
  );
};

export default DashboardMain; 