import React from "react";
import { Link } from "react-router-dom";

const Navbar = () => {
  return (
    <nav className="bg-slate-700 fixed w-full top-0 left-0 right-0 z-10 h-16 flex items-center justify-between px-4 shadow-md">
      {/* Left side - Logo/Brand */}
      <div className="flex items-center">
        <span className="text-white mr-2">Farm</span>
        <span className="text-blue-400 mr-2">›</span>
        <span className="text-blue-400">Dashboard</span>
      </div>
      
      {/* Middle - Search Box */}
      <div className="flex-1 max-w-xl mx-4 relative">
        <div className="absolute inset-y-0 left-3 flex items-center pointer-events-none">
          <span className="text-gray-400">🔍</span>
        </div>
        <input 
          type="text" 
          placeholder="Search here" 
          className="bg-white text-black h-10 pl-10 pr-4 rounded-md w-full"
        />
      </div>
      
      {/* Right side - User Info */}
      <div className="flex items-center">
        <span className="text-white mr-4">🔔</span>
        <span className="text-white mr-2">David</span>
        <div className="flex items-center">
          <div className="h-8 w-8 bg-gray-300 rounded-full flex items-center justify-center">
            <span className="text-gray-700">👤</span>
          </div>
          <span className="text-white ml-1">▼</span>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;