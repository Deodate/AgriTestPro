import React from 'react';
import { Link } from 'react-router-dom';

function HomePage() {
  // State for managing dropdown menus
  const [activeDropdown, setActiveDropdown] = useState(null)

  // Navigation hook
  const navigate = useNavigate()

  // Dropdown toggle handler
  const handleDropdownToggle = (label) => {
    setActiveDropdown(activeDropdown === label ? null : label)
  }

  // Destructure constants for easy access
  const {
    companyName,
    companySubtitle,
    mainHeading,
    companyTagline
  } = APP_CONSTANTS

  return (
    <div className="min-h-screen flex flex-col w-screen overflow-hidden">
      {/* Navbar Section */}
      <nav className="bg-teal-800 shadow-md w-full">
        <div className="container mx-auto px-4 flex items-center h-24">
          {/* Logo Section */}
          <div className="w-1/4 flex items-start">
            <div className="flex items-center">
              {/* Logo with Colored Dots */}
              <div className="h-16 w-16 relative">
                <div className="rounded-full h-full w-full border-2 border-transparent relative flex items-center justify-center">
                  <div className="absolute w-4 h-4 rounded-full bg-green-500"
                    style={{
                      top: '0px',
                      left: '50%',
                      transform: 'translateX(-50%)'
                    }}
                  ></div>
                  <div className="absolute w-4 h-4 rounded-full bg-blue-500"
                    style={{
                      top: '25%',
                      left: '0px'
                    }}
                  ></div>
                  <div className="absolute w-4 h-4 rounded-full bg-yellow-500"
                    style={{
                      bottom: '25%',
                      left: '0px'
                    }}
                  ></div>
                  <div className="absolute w-4 h-4 rounded-full bg-yellow-500"
                    style={{
                      bottom: '0px',
                      left: '50%',
                      transform: 'translateX(-50%)'
                    }}
                  ></div>
                </div>
              </div>

              {/* Company Name */}
              <div className="ml-2 text-white flex flex-col">
                <span className="text-xl font-bold">{companyName}</span>
                <span className="text-sm">{companySubtitle}</span>
              </div>
            </div>
          </div>

          {/* Navigation Menu */}
          <div
            className="w-1/2 hidden md:flex space-x-4 items-center"
            style={{ marginLeft: '120px' }}
          >
            {MENU_ITEMS.map((item) => (
              <div
                key={item.label}
                className="relative group"
              >
                <button
                  className={`
                    ${item.isHighlighted ? 'bg-blue-500 text-white' : 'bg-white text-gray-800'}
                    hover:text-blue-700 
                    flex items-center 
                    py-2 px-2 
                    rounded-md 
                    transition
                  `}
                  onMouseEnter={() => item.hasDropdown && handleDropdownToggle(item.label)}
                  onMouseLeave={() => item.hasDropdown && handleDropdownToggle(item.label)}
                >
                  {item.label}
                  {item.hasDropdown && (
                    <svg
                      className="ml-1 w-4 h-4"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                      xmlns="http://www.w3.org/2000/svg"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M19 9l-7 7-7-7"
                      />
                    </svg>
                  )}
                </button>

                {/* Dropdown Submenu */}
                {item.hasDropdown && activeDropdown === item.label && (
                  <div
                    className="absolute z-10 mt-2 w-48 bg-white shadow-lg rounded-md"
                    onMouseEnter={() => handleDropdownToggle(item.label)}
                    onMouseLeave={() => handleDropdownToggle(null)}
                  >
                    {item.subItems.map((subItem) => (
                      <Link
                        key={subItem}
                        to={`/${subItem.toLowerCase().replace(/\s+/g, '-')}`}
                        className="block px-4 py-2 text-gray-700 hover:bg-gray-100"
                      >
                        {subItem}
                      </Link>
                    ))}
                  </div>
                )}
              </div>
            ))}
          </div>

          {/* Contact Button */}
          <div className="w-1/4 flex justify-end">
            <button
              className="bg-blue-700 text-white px-4 py-2 rounded-md hover:bg-blue-800 transition flex items-center"
              onClick={() => navigate('/contact')}
              style={{ position: 'relative', left: '11px' }}
            >
              <svg
                className="w-5 h-5 mr-2"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"
                />
              </svg>
              Contact us
            </button>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <div
        className="relative flex items-center justify-center w-full bg-gray-300 flex-grow"
        style={{
          height: "calc(100vh - 6rem)",
        }}
      >
        <div className="absolute inset-0 flex flex-col justify-center w-full">
          <div className="container mx-auto px-4 text-center">
            {/* Main Heading */}
            <h1 className="text-5xl md:text-6xl font-bold mb-4 text-black">
              {mainHeading}
            </h1>

            {/* Divider */}
            <div className="w-32 h-1 bg-yellow-400 my-8 mx-auto"></div>

            {/* Tagline */}
            <p className="text-2xl mb-8 text-black font-medium">
              {companyTagline}
            </p>
          </div>
        </div>

        {/* Curved Bottom Section */}
        <div className="absolute bottom-0 left-0 right-0 w-full overflow-hidden">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 1440 320"
            className="w-full relative"
          >
            <path
              fill="#ffffff"
              d="M0,96L80,106.7C160,117,320,139,480,149.3C640,160,800,160,960,138.7C1120,117,1280,75,1360,53.3L1440,32L1440,320L1360,320C1280,320,1120,320,960,320C800,320,640,320,480,320C320,320,160,320,80,320L0,320Z"
            ></path>
            
            {/* Buttons Container */}
            <foreignObject x="20%" y="50%" width="60%" height="100">
              <div xmlns="http://www.w3.org/1999/xhtml" className="flex justify-center space-x-6">
                <Link
                  to="/signup"
                  className="bg-blue-600 text-white text-xl font-bold py-3 px-10 rounded-md 
                    hover:bg-blue-700 transition-colors duration-300 
                    transform hover:scale-105 hover:translate-y-[-5px]
                    shadow-md hover:shadow-lg"
                >
                  Sign Up
                </Link>
                
                <Link
                  to="/login"
                  className="bg-green-600 text-white text-xl font-bold py-3 px-10 rounded-md 
                    hover:bg-green-700 transition-colors duration-300 
                    transform hover:scale-105 hover:translate-y-[-5px]
                    shadow-md hover:shadow-lg"
                >
                  Sign In
                </Link>
              </div>
            </foreignObject>
          </svg>
        </div>
      </div>

      {/* Footer Component */}
      <Footer />
    </div>
  )
}

export default HomePage