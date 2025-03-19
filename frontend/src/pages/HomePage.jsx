import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import MENU_ITEMS, { APP_CONSTANTS } from '../constants/menuItems'

function HomePage() {
  const [activeDropdown, setActiveDropdown] = useState(null)
  const navigate = useNavigate()

  const handleDropdownToggle = (label) => {
    setActiveDropdown(activeDropdown === label ? null : label)
  }

  const { companyName, companySubtitle, mainHeading, companyTagline } = APP_CONSTANTS;

  // Function to get the button background color based on the item's color property
  const getButtonBgColor = (color) => {
    switch(color) {
      case 'info':
        return 'bg-blue-500';
      default:
        return 'bg-white';
    }
  }

  // Function to get the button text color based on the item's color property
  const getButtonTextColor = (color) => {
    switch(color) {
      case 'info':
        return 'text-white';
      default:
        return 'text-gray-800';
    }
  }

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
                <span className="text-xl font-bold">{companyName}</span>
                <span className="text-sm">{companySubtitle}</span>
              </div>
            </div>
          </div>

          {/* Navigation Menu - Starting from left with 5px margin */}
          <div className="w-1/2 hidden md:flex space-x-4 items-center" style={{ marginLeft: '5px' }}>
            {MENU_ITEMS.map((item) => (
              <div 
                key={item.label} 
                className="relative group"
              >
                <button 
                  className={`${getButtonBgColor(item.color)} ${getButtonTextColor(item.color)} hover:text-blue-700 flex items-center py-2 px-4 rounded-md transition`}
                  onMouseEnter={() => item.hasDropdown && handleDropdownToggle(item.label)}
                  onMouseLeave={() => item.hasDropdown && handleDropdownToggle(item.label)}
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
                    onMouseEnter={() => handleDropdownToggle(item.label)}
                    onMouseLeave={() => handleDropdownToggle(null)}
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
            
          {/* Cash Bids Button - Fixed position */}
          <div className="w-1/4 flex justify-end">
            <button 
              className="bg-blue-700 text-white px-4 py-2 rounded-md hover:bg-blue-800 transition flex items-center"
              onClick={() => navigate('/login')}
              style={{ position: 'relative', left: '123px' }}
            >
              <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
              </svg>
              CASH BIDS
            </button>
          </div>
        </div>
      </nav>

      {/* Hero Section - Full Width with Gray Background */}
      <div 
        className="relative flex items-center justify-center w-full bg-gray-300 flex-grow"
        style={{
          height: "calc(100vh - 6rem)",
        }}
      >
        <div className="absolute inset-0 flex flex-col justify-center w-full">
          <div className="container mx-auto px-4">
            {/* Changed main heading to black */}
            <h1 className="text-5xl md:text-6xl font-bold mb-4 text-black">
              {mainHeading}
            </h1>
            <div className="w-32 h-1 bg-yellow-400 my-8"></div>
            {/* Black company tagline */}
            <p className="text-2xl mb-8 text-black font-medium">
              {companyTagline}
            </p>
          </div>
        </div>
        
        {/* Curved white section at bottom */}
        <div className="absolute bottom-0 left-0 right-0 w-full overflow-hidden">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320" className="w-full">
            <path fill="#ffffff" d="M0,96L80,106.7C160,117,320,139,480,149.3C640,160,800,160,960,138.7C1120,117,1280,75,1360,53.3L1440,32L1440,320L1360,320C1280,320,1120,320,960,320C800,320,640,320,480,320C320,320,160,320,80,320L0,320Z"></path>
          </svg>
        </div>
      </div>

      {/* Footer */}
      <footer className="bg-gray-800 text-white py-6 w-full">
        <div className="container mx-auto px-4 text-center">
          <p>&copy; {new Date().getFullYear()} {companyName} {companySubtitle}. All Rights Reserved.</p>
        </div>
      </footer>
    </div>
  )
}

export default HomePage