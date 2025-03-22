import React, { useState, useEffect } from "react";
import StatisticsContainer from "./StatisticsContainer";
import { menuItems } from "./sidebar"; // Import the menu items from sidebar

const Container = ({ children }) => {
    const [activeMenu, setActiveMenu] = useState("");
    
    useEffect(() => {
        // Get the active menu from localStorage (set by Sidebar component)
        const currentActive = localStorage.getItem('activeMenu') || "/dashboard";
        
        // Find the corresponding menu item
        const menuItem = menuItems.find(item => item.to === currentActive);
        setActiveMenu(menuItem ? menuItem.label : "");
    }, []);
    
    // Listen for changes to localStorage
    useEffect(() => {
        const handleStorageChange = () => {
            const currentActive = localStorage.getItem('activeMenu') || "/dashboard";
            const menuItem = menuItems.find(item => item.to === currentActive);
            setActiveMenu(menuItem ? menuItem.label : "");
        };
        
        window.addEventListener('storage', handleStorageChange);
        return () => {
            window.removeEventListener('storage', handleStorageChange);
        };
    }, []);

    return (
        <div className="w-full px-6 py-4 ml-64" style={{ backgroundColor: "#c5e4fa" }}>
            {/* Display the active menu */}
            {activeMenu && (
                <div className="mb-4 p-3 bg-white rounded-lg shadow-sm">
                    <h2 className="text-lg font-semibold">
                        Currently Active: <span className="text-blue-600">{activeMenu}</span>
                    </h2>
                </div>
            )}
            
            <StatisticsContainer />
            {children}
        </div>
    );
};

export default Container;