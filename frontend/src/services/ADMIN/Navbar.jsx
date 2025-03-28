import React, { Component } from "react";

class Navbar extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isDropdownOpen: false
    };

    this.dropdownRef = React.createRef();
    this.handleClickOutside = this.handleClickOutside.bind(this);
    this.toggleDropdown = this.toggleDropdown.bind(this);
    this.handleLogout = this.handleLogout.bind(this);
  }

  componentDidMount() {
    document.addEventListener('mousedown', this.handleClickOutside);
  }

  componentWillUnmount() {
    document.removeEventListener('mousedown', this.handleClickOutside);
  }

  handleClickOutside(event) {
    if (this.dropdownRef.current && !this.dropdownRef.current.contains(event.target)) {
      this.setState({ isDropdownOpen: false });
    }
  }

  toggleDropdown() {
    this.setState(prevState => ({
      isDropdownOpen: !prevState.isDropdownOpen
    }));
  }

  async handleLogout() {
    try {
      console.log('Attempting to logout...');

      try {
        // First attempt the API call
        const response = await fetch('http://localhost:8088/api/auth/signout', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token') || ''}`
          }
        });

        console.log('Logout API response:', response);

        if (response.ok) {
          console.log('Logout successful via API');
        } else {
          console.warn('API responded with error:', response.status);
          const errorText = await response.text();
          console.warn('Error details:', errorText);
        }
      } catch (apiError) {
        // Log the API error but continue with local logout
        console.warn('API call failed, proceeding with local logout:', apiError);
      }

      // Regardless of API success, perform local logout actions
      console.log('Performing local logout actions');
      localStorage.removeItem('token');
      localStorage.removeItem('user');

      console.log('Redirecting to login page');
      window.location.href = '/login';

    } catch (error) {
      console.error('Critical logout error:', error);
      alert('Logout failed. Please try again or refresh the page.');
    }
  }

  render() {
    return (
      <nav className="bg-slate-600 fixed w-full top-0 left-0 right-0 z-10 h-16 flex items-center justify-between px-4 shadow-md">
        {/* Left side - Logo/Brand */}
        <div className="flex items-center">
          <span className="text-red-500 mr-2 font-bold">AGRI TEST PRO</span>
          <span className="text-blue-400 mr-2">›</span>
          <span className="text-blue-400">Dashboard</span>
        </div>

        {/* Center - Navigation Menu */}
        <div className="flex items-center space-x-6">
          <a href="/gallery" className="text-yellow-400 underline hover:no-underline text-xs transition-all">Gallery</a>
          <span className="text-gray-500">|</span>
          <a href="/contact" className="text-yellow-400 underline hover:no-underline text-xs transition-all">News</a>
          <span className="text-gray-500">|</span>
          <a href="/customers" className="text-yellow-400 underline hover:no-underline text-xs transition-all">Users</a>
        </div>

        {/* Right side - User Info */}
        <div className="flex items-center">
       
          <div className="relative" ref={this.dropdownRef}>
            <div
              className="flex items-center cursor-pointer"
              onClick={this.toggleDropdown}
            >
              <div className="h-5 w-5 bg-gray-300 rounded-full flex items-center justify-center">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="text-gray-500">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                  <circle cx="12" cy="7" r="4"></circle>
                </svg>
              </div>&nbsp;
              
              <span className="text-white mr-2">David</span>
              <span className="text-white ml-1">▼</span>
            </div>

            {/* Dropdown Menu */}
            {this.state.isDropdownOpen && (
              <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-20">
                <a href="#profile" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                  My Profile
                </a>
                <div className="border-t border-gray-200"></div>
                <button
                  onClick={this.handleLogout}
                  className="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-gray-100"
                >
                  Log out
                </button>
              </div>
            )}
          </div>
        </div>
      </nav>
    );
  }
}

export default Navbar;