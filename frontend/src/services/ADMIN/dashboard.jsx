// src/components/dashboard/Dashboard.jsx
import React, { Component } from "react";
import { useNavigate } from "react-router-dom";
import MENU_ITEMS, { APP_CONSTANTS } from "../../constants/MenuItems";

class Dashboard extends Component {
    constructor(props) {
        super(props);

        this.state = {
            activeDropdown: null,
            userData: null
        };

        this.handleDropdownToggle = this.handleDropdownToggle.bind(this);
        this.handleLogout = this.handleLogout.bind(this);
    }

    componentDidMount() {
        // Get user data from localStorage
        const userData = localStorage.getItem('user');
        if (userData) {
            this.setState({ userData: JSON.parse(userData) });
        } else {
            // Redirect to login if no user data found
            this.props.navigate('/login');
        }
    }

    // Dropdown handling
    handleDropdownToggle(label) {
        this.setState(prevState => ({
            activeDropdown: prevState.activeDropdown === label ? null : label
        }));
    }

    // Handle logout
    handleLogout() {
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        this.props.navigate('/login');
    }

    render() {
        const { activeDropdown, userData } = this.state;

        return (
            <div className="min-h-screen flex flex-col w-screen overflow-hidden">
                {/* Navbar - Full Width */}
                <nav className="bg-teal-800 shadow-md w-full">
                    <div className="container mx-auto px-4 flex items-center h-24">
                        {/* Logo - Fixed on left */}
                        <div className="w-1/4 flex items-start">
                            <div className="flex items-center">
                                <div className="h-16 w-16 relative">
                                    {/* Circle with colored dots (simplified representation) */}
                                    <div className="rounded-full h-full w-full border-2 border-transparent relative flex items-center justify-center">
                                        <div className="absolute w-4 h-4 rounded-full bg-green-500" style={{ top: '0px', left: '50%', transform: 'translateX(-50%)' }}></div>
                                        <div className="absolute w-4 h-4 rounded-full bg-blue-500" style={{ top: '25%', left: '0px' }}></div>
                                        <div className="absolute w-4 h-4 rounded-full bg-yellow-500" style={{ bottom: '25%', left: '0px' }}></div>
                                        <div className="absolute w-4 h-4 rounded-full bg-yellow-500" style={{ bottom: '0px', left: '50%', transform: 'translateX(-50%)' }}></div>
                                    </div>
                                </div>
                                <div className="ml-2 text-white flex flex-col">
                                    <span className="text-xl font-bold">{APP_CONSTANTS.companyName}</span>
                                    <span className="text-sm">{APP_CONSTANTS.companySubtitle}</span>
                                </div>
                            </div>
                        </div>

                        {/* Navigation Menu */}
                        <div className="w-1/2 hidden md:flex space-x-4 items-center" style={{ marginLeft: '100px' }}>
                            {MENU_ITEMS.map((item) => (
                                <div
                                    key={item.label}
                                    className="relative group"
                                >
                                    <button
                                        className={`hover:text-blue-700 flex items-center py-2 px-2 rounded-md transition ${item.isHighlighted ? 'bg-blue-500 text-white' : 'bg-white text-gray-800'}`}
                                        onMouseEnter={() => item.hasDropdown && this.handleDropdownToggle(item.label)}
                                        onMouseLeave={() => item.hasDropdown && this.handleDropdownToggle(item.label)}
                                    >
                                        {item.label}
                                        {item.hasDropdown && (
                                            <svg className="ml-1 w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                                            </svg>
                                        )}
                                    </button>
                                    {item.hasDropdown && activeDropdown === item.label && (
                                        <div className="absolute z-10 mt-2 w-48 bg-white shadow-lg rounded-md"
                                            onMouseEnter={() => this.handleDropdownToggle(item.label)}
                                            onMouseLeave={() => this.handleDropdownToggle(null)}
                                        >
                                            {item.subItems.map((subItem) => (
                                                <a
                                                    key={subItem}
                                                    href="#"
                                                    className="block px-4 py-2 text-gray-700 hover:bg-gray-100"
                                                >
                                                    {subItem}
                                                </a>
                                            ))}
                                        </div>
                                    )}
                                </div>
                            ))}
                        </div>

                        {/* Logout Button */}
                        <div className="w-1/4 flex justify-end">
                            <button
                                className="bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600 transition flex items-center"
                                onClick={this.handleLogout}
                                style={{ position: 'relative', left: '11px' }}
                            >
                                <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                                </svg>
                                Logout
                            </button>
                        </div>
                    </div>
                </nav>

                {/* Dashboard Content */}
                <div className="flex-grow bg-gray-100 p-8">
                    <div className="max-w-7xl mx-auto">
                        {/* Welcome Card */}
                        <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                            <h1 className="text-2xl font-bold text-gray-800">
                                Welcome to your Dashboard{userData ? `, ${userData.username}` : ''}!
                            </h1>
                            <p className="mt-2 text-gray-600">
                                Your account has been successfully created. You can now access all features.
                            </p>
                        </div>

                        {/* Dashboard Cards */}
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                            {/* Quick Stats Card */}
                            <div className="bg-white rounded-lg shadow-md p-6">
                                <h2 className="text-lg font-semibold text-gray-800 mb-4">Account Information</h2>
                                {userData && (
                                    <div className="space-y-2">
                                        <p><span className="font-medium">Username:</span> {userData.username}</p>
                                        <p><span className="font-medium">Email:</span> {userData.email}</p>
                                        <p><span className="font-medium">Role:</span> {userData.roles && userData.roles.join(', ')}</p>
                                    </div>
                                )}
                            </div>

                            {/* Recent Activity Card */}
                            <div className="bg-white rounded-lg shadow-md p-6">
                                <h2 className="text-lg font-semibold text-gray-800 mb-4">Recent Activity</h2>
                                <ul className="space-y-2">
                                    <li className="flex items-center text-sm text-gray-600">
                                        <svg className="w-4 h-4 mr-2 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                                        </svg>
                                        Account created successfully
                                    </li>
                                    <li className="flex items-center text-sm text-gray-600">
                                        <svg className="w-4 h-4 mr-2 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                                        </svg>
                                        First login completed
                                    </li>
                                </ul>
                            </div>

                            {/* Quick Actions Card */}
                            <div className="bg-white rounded-lg shadow-md p-6">
                                <h2 className="text-lg font-semibold text-gray-800 mb-4">Quick Actions</h2>
                                <div className="space-y-3">
                                    <button className="w-full bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded transition">
                                        Update Profile
                                    </button>
                                    <button className="w-full bg-green-500 hover:bg-green-600 text-white py-2 px-4 rounded transition">
                                        View Reports
                                    </button>
                                    <button className="w-full bg-purple-500 hover:bg-purple-600 text-white py-2 px-4 rounded transition">
                                        Manage Settings
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Footer */}
                <Footer />
            </div>
        );
    }
}

// Wrapper component to use hooks with class component
const DashboardWithHooks = (props) => {
    const navigate = useNavigate();
    
    return <Dashboard {...props} navigate={navigate} />;
};

export default DashboardWithHooks;