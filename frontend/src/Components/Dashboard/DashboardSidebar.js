import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
  FaChevronDown, FaChevronRight, FaChevronLeft,
  FaFlask, FaClipboardCheck, FaWarehouse, FaChartLine,
  FaUserShield, FaComments, FaTasks
} from 'react-icons/fa';

const DashboardSidebar = ({
  menuStates,
  subMenuStates,
  toggleMenu,
  toggleSubMenu,
  setFormVisibility,
  setMode,
  sidebarCollapsed,
  toggleSidebar
}) => {
  const navigate = useNavigate();

  const handleMenuClick = (menuName) => {
    toggleMenu(menuName);
  };

  const handleSubMenuClick = (subMenuName) => {
    toggleSubMenu(subMenuName);
  };

  const handleFormClick = (formName, mode = 'create') => {
    setFormVisibility(formName, true);
    setMode(formName.replace('show', '').toLowerCase() + 'Mode', mode);
    navigate(`/dashboard?${formName.replace('show', '')}=${mode}`, { replace: true });
  };

  return (
    <div className={`dashboard-sidebar ${sidebarCollapsed ? 'collapsed' : ''}`}>
      {/* Product Testing Section */}
      <div className="sidebar-section">
        <div className="section-header" onClick={() => handleMenuClick('productTestingOpen')}>
          {menuStates.productTestingOpen ? <FaChevronDown className="toggle-icon" /> : <FaChevronRight className="toggle-icon" />}
          <FaFlask className="sidebar-icon" />
          <span className="section-title">Product Testing</span>
        </div>

        {menuStates.productTestingOpen && (
          <div className="section-content">
            <div className="sub-section">
              <div className="sub-header" onClick={() => handleSubMenuClick('testCaseSubMenuOpen')}>
                {subMenuStates.testCaseSubMenuOpen ? <FaChevronDown className="toggle-icon-sub" /> : <FaChevronRight className="toggle-icon-sub" />}
                <span className="sub-title">Test Management</span>
              </div>

              {subMenuStates.testCaseSubMenuOpen && (
                <div className="sub-content">
                  <div className="menu-item" onClick={() => handleFormClick('showTestCaseForm')}>
                    Creating Test Cases
                  </div>
                  <div className="menu-item" onClick={() => handleFormClick('showTrialPhaseForm')}>
                    Trial Phase Tracking
                  </div>
                  <div className="menu-item" onClick={() => handleFormClick('showTestDocumentationForm')}>
                    Test Documentation
                  </div>
                  {/* Add more menu items */}
                </div>
              )}
            </div>
          </div>
        )}
      </div>

      {/* Compliance Checklist Section */}
      <div className="sidebar-section">
        <div className="section-header" onClick={() => handleMenuClick('complianceChecklistOpen')}>
          {menuStates.complianceChecklistOpen ? <FaChevronDown className="toggle-icon" /> : <FaChevronRight className="toggle-icon" />}
          <FaClipboardCheck className="sidebar-icon" />
          <span className="section-title">Compliance Checklist</span>
        </div>

        {menuStates.complianceChecklistOpen && (
          <div className="section-content">
            {/* Add compliance checklist content */}
          </div>
        )}
      </div>

      {/* Add more sections */}

      <button className="collapse-btn" onClick={toggleSidebar}>
        {sidebarCollapsed ? <FaChevronRight /> : <FaChevronLeft />}
      </button>
    </div>
  );
};

export default DashboardSidebar; 