import React, { useState } from 'react';
import { FaBell, FaUserCircle, FaHome, FaSignOutAlt } from 'react-icons/fa';

const DashboardHeader = ({ userInfo, notificationStates, handleLogout }) => {
  const { notificationCount, notificationsOpen, notifications, toggleNotifications, markAllAsRead } = notificationStates;
  const [userDropdownOpen, setUserDropdownOpen] = useState(false);

  return (
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
                      {userInfo?.username || userInfo?.name || 'User'}
                    </span>
                    <span className="user-role">
                      {userInfo?.roles?.[0] || userInfo?.role || 'Role'}
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