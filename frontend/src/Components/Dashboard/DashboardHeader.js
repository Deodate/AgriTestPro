import React, { useState } from 'react';
import { FaUserCircle, FaHome, FaSignOutAlt } from 'react-icons/fa';

const DashboardHeader = ({ user, handleLogout }) => {
  const [userDropdownOpen, setUserDropdownOpen] = useState(false);

  return (
    <div className="twilio-header">
      <div className="header-left">
        <button className="home-button">
          <FaHome /> AgriTest Pro Home
        </button>
      </div>
      
      <div className="header-right">
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
                      {user?.username || user?.name || 'User'}
                    </span>
                    <span className="user-role">
                      {user?.roles?.[0] || user?.role || 'Role'}
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
  );
};

export default DashboardHeader; 