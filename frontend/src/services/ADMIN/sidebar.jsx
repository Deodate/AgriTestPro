import React from "react";
import { Link } from "react-router-dom";

const Sidebar = () => {
    return (
        <div className="w-64 bg-slate-700 text-white h-screen fixed left-0 top-0 flex flex-col">
            {/* Header */}
            <div className="p-4 border-b border-slate-600">
                <h1 className="text-2xl font-bold">Admin Panel</h1>
            </div>
            
            {/* Navigation */}
            <nav className="flex-1 overflow-y-auto p-4">
                <ul className="space-y-2 font-medium text-white">
                    <NavItem to="/dashboard" label="Dashboard" />
                    <NavItem to="/dashboard/tests" label="Test Case Creation Form" />
                    <NavItem to="/dashboard/operations" label="Operations" />
                    <NavItem to="/dashboard/users" label="Users" />
                    <NavItem to="/dashboard/inventory" label="Inventory" />
                    <NavItem to="/dashboard/reports" label="Reports" />
                    <NavItem to="/dashboard/marketplace" label="Marketplace" />
                </ul>
            </nav>
            
            {/* Footer */}
            <div className="p-4 border-t border-slate-600">
                <NavItem to="/logout" label="Logout" />
            </div>
        </div>
    );
};

// Navigation Item Component
const NavItem = ({ to, label }) => {
    return (
        <li>
            <Link 
                to={to} 
                className="flex items-center p-2 rounded-lg hover:bg-slate-600 group transition-colors text-white"
            >
                <span>{label}</span>
            </Link>
        </li>
    );
};

export default Sidebar;