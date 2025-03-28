import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const Sidebar = () => {
  const location = useLocation();

  const isActive = (path) => {
    return location.pathname === path;
  };

  return (
    <div className="bg-gray-800 text-white w-64 space-y-6 py-7 px-2 absolute inset-y-0 left-0 transform -translate-x-full md:relative md:translate-x-0 transition duration-200 ease-in-out">
      <nav>
        <Link
          to="/dashboard"
          className={`block py-2.5 px-4 rounded transition duration-200 ${
            isActive('/dashboard') ? 'bg-gray-900 text-white' : 'text-gray-400 hover:bg-gray-700 hover:text-white'
          }`}
        >
          Dashboard
        </Link>
        <Link
          to="/dashboard/test-cases"
          className={`block py-2.5 px-4 rounded transition duration-200 ${
            isActive('/dashboard/test-cases') ? 'bg-gray-900 text-white' : 'text-gray-400 hover:bg-gray-700 hover:text-white'
          }`}
        >
          Test Cases
        </Link>
        <Link
          to="/dashboard/settings"
          className={`block py-2.5 px-4 rounded transition duration-200 ${
            isActive('/dashboard/settings') ? 'bg-gray-900 text-white' : 'text-gray-400 hover:bg-gray-700 hover:text-white'
          }`}
        >
          Settings
        </Link>
      </nav>
    </div>
  );
};

export default Sidebar; 