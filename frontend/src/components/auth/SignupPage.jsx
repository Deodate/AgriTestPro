// src/components/auth/SignupPage.jsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import MENU_ITEMS, { APP_CONSTANTS } from "../../constants/MenuItems";
import Footer from "../Footer";
import { useAuth } from "../../contexts/AuthContext.jsx"; // Note the .jsx extension

function SignupPage() {
  const [activeDropdown, setActiveDropdown] = useState(null);
  const navigate = useNavigate();
  const [showLoginModal, setShowLoginModal] = useState(false);
  const { register, login, verifyTwoFactor } = useAuth(); // Use the hook

  // Rest of the component remains the same
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    confirmPassword: '',
    phoneNumber: ''
  });

  const [loginData, setLoginData] = useState({
    email: '',
    password: ''
  });

  const [isLoading, setIsLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");
  const [showTwoFactorModal, setShowTwoFactorModal] = useState(false);
  const [twoFactorData, setTwoFactorData] = useState({
    userId: null,
    code: ''
  });

  const handleDropdownToggle = (label) => {
    setActiveDropdown(activeDropdown === label ? null : label);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }));
    setErrorMsg(""); // Clear error message on input change
  };

  const handleLoginInputChange = (e) => {
    const { name, value } = e.target;
    setLoginData(prevState => ({
      ...prevState,
      [name]: value
    }));
    setErrorMsg(""); // Clear error message on input change
  };

  const handleTwoFactorInputChange = (e) => {
    setTwoFactorData(prevState => ({
      ...prevState,
      code: e.target.value
    }));
  };

  // Handle signup form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setErrorMsg("");
    setSuccessMsg("");
    
    // Validate passwords match
    if (formData.password !== formData.confirmPassword) {
      setErrorMsg("Passwords do not match");
      setIsLoading(false);
      return;
    }
    
    try {
      // Call the register function from auth context
      await register(formData);
      setSuccessMsg("Account created successfully! You can now sign in.");
      
      // Reset form after successful registration
      setFormData({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        confirmPassword: '',
        phoneNumber: ''
      });
      
      // Auto-open login modal after successful signup
      setTimeout(() => {
        setShowLoginModal(true);
      }, 2000);
      
    } catch (error) {
      setErrorMsg(error.message || "Registration failed. Please try again.");
      console.error("Registration error:", error);
    } finally {
      setIsLoading(false);
    }
  };

  // Handle login form submission
  const handleLoginSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setErrorMsg("");
    
    try {
      // Call the login function from auth context
      const response = await login(loginData.email, loginData.password);
      
      // Handle 2FA if required
      if (response && response.requiresVerification) {
        setTwoFactorData({
          userId: response.userId,
          code: ''
        });
        setShowLoginModal(false);
        setShowTwoFactorModal(true);
        return;
      }
      
      // On successful login
      setSuccessMsg("Login successful!");
      setShowLoginModal(false);
      
      // Redirect to dashboard
      setTimeout(() => {
        navigate('/dashboard');
      }, 1000);
      
    } catch (error) {
      setErrorMsg(error.message || "Login failed. Please check your credentials.");
      console.error("Login error:", error);
    } finally {
      setIsLoading(false);
    }
  };

  // Handle 2FA verification
  const handleTwoFactorSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setErrorMsg("");
    
    try {
      await verifyTwoFactor(twoFactorData.userId, twoFactorData.code);
      
      setSuccessMsg("Verification successful!");
      setShowTwoFactorModal(false);
      
      // Redirect to dashboard
      setTimeout(() => {
        navigate('/dashboard');
      }, 1000);
      
    } catch (error) {
      setErrorMsg("Verification failed. Please try again.");
      console.error("2FA verification error:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const toggleLoginModal = () => {
    setShowLoginModal(!showLoginModal);
    setErrorMsg(""); // Clear error messages when toggling modal
  };

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

          {/* Navigation Menu - Starting from left with 5px margin */}
          <div className="w-1/2 hidden md:flex space-x-4 items-center" style={{ marginLeft: '100px' }}>
            {MENU_ITEMS.map((item) => (
              <div 
                key={item.label} 
                className="relative group"
              >
                <button 
                  className={`hover:text-blue-700 flex items-center py-2 px-2 rounded-md transition ${item.isHighlighted ? 'bg-blue-500 text-white' : 'bg-white text-gray-800'}`}
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
            
          {/* Contact us Button - Fixed position */}
          <div className="w-1/4 flex justify-end">
            <button 
              className="bg-blue-700 text-white px-4 py-2 rounded-md hover:bg-blue-800 transition flex items-center"
              onClick={() => navigate('/contact')}
              style={{ position: 'relative', left: '11px' }}
            >
              <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
              </svg>
              Contact us
            </button>
          </div>
        </div>
      </nav>

      {/* Signup Form Section */}
      <div className="flex-grow bg-gray-100 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-md w-full space-y-8 bg-white p-10 rounded-xl shadow-lg">
          <div>
            <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
              Create your account
            </h2>
            <p className="mt-2 text-center text-sm text-gray-600">
              Join Agri Test Pro and enhance your agricultural management
            </p>
          </div>
          
          {/* Success Message */}
          {successMsg && (
            <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative">
              {successMsg}
            </div>
          )}
          
          {/* Error Message */}
          {errorMsg && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative">
              {errorMsg}
            </div>
          )}
          
          <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label htmlFor="firstName" className="sr-only">First Name</label>
                <input 
                  id="firstName"
                  name="firstName"
                  type="text"
                  required
                  className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  placeholder="First Name"
                  value={formData.firstName}
                  onChange={handleInputChange}
                  disabled={isLoading}
                />
              </div>
              <div>
                <label htmlFor="lastName" className="sr-only">Last Name</label>
                <input 
                  id="lastName"
                  name="lastName"
                  type="text"
                  required
                  className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  placeholder="Last Name"
                  value={formData.lastName}
                  onChange={handleInputChange}
                  disabled={isLoading}
                />
              </div>
            </div>
            <div>
              <label htmlFor="email" className="sr-only">Email address</label>
              <input 
                id="email"
                name="email"
                type="email"
                autoComplete="email"
                required
                className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="Email address"
                value={formData.email}
                onChange={handleInputChange}
                disabled={isLoading}
              />
            </div>
            <div>
              <label htmlFor="phoneNumber" className="sr-only">Phone Number</label>
              <input 
                id="phoneNumber"
                name="phoneNumber"
                type="tel"
                required
                className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="Phone Number"
                value={formData.phoneNumber}
                onChange={handleInputChange}
                disabled={isLoading}
              />
            </div>
            <div>
              <label htmlFor="password" className="sr-only">Password</label>
              <input 
                id="password"
                name="password"
                type="password"
                required
                className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="Password"
                value={formData.password}
                onChange={handleInputChange}
                disabled={isLoading}
              />
            </div>
            <div>
              <label htmlFor="confirmPassword" className="sr-only">Confirm Password</label>
              <input 
                id="confirmPassword"
                name="confirmPassword"
                type="password"
                required
                className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="Confirm Password"
                value={formData.confirmPassword}
                onChange={handleInputChange}
                disabled={isLoading}
              />
            </div>
            <div>
              <button
                type="submit"
                className={`group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 ${isLoading ? 'opacity-70 cursor-not-allowed' : ''}`}
                disabled={isLoading}
              >
                {isLoading ? 'Creating Account...' : 'Create Account'}
              </button>
            </div>
          </form>
          <div className="text-center">
            <p className="mt-2 text-sm text-gray-600">
              Already have an account? 
              <button 
                className="font-medium text-blue-600 hover:text-blue-500 ml-1"
                onClick={toggleLoginModal}
                disabled={isLoading}
              >
                Sign in
              </button>
            </p>
          </div>
        </div>
      </div>

      {/* Login Modal */}
      {showLoginModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-8 rounded-lg shadow-xl max-w-md w-full relative">
            {/* Close Button */}
            <button 
              onClick={toggleLoginModal}
              className="absolute top-4 right-4 text-gray-500 hover:text-gray-700"
              aria-label="Close"
              disabled={isLoading}
            >
              <svg className="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>

            <h2 className="text-2xl font-bold text-center mb-6">Login to your account</h2>
            
            {/* Error Message in Login Modal */}
            {errorMsg && (
              <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4">
                {errorMsg}
              </div>
            )}
            
            <form onSubmit={handleLoginSubmit} className="space-y-6">
              <div>
                <label htmlFor="loginEmail" className="block text-sm font-medium text-gray-700">Email address</label>
                <input
                  id="loginEmail"
                  name="email"
                  type="email"
                  autoComplete="email"
                  required
                  className="mt-1 appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  placeholder="Email address"
                  value={loginData.email}
                  onChange={handleLoginInputChange}
                  disabled={isLoading}
                />
              </div>
              <div>
                <label htmlFor="loginPassword" className="block text-sm font-medium text-gray-700">Password</label>
                <input
                  id="loginPassword"
                  name="password"
                  type="password"
                  autoComplete="current-password"
                  required
                  className="mt-1 appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  placeholder="Password"
                  value={loginData.password}
                  onChange={handleLoginInputChange}
                  disabled={isLoading}
                />
              </div>
              <div className="flex items-center justify-between">
                <div className="flex items-center">
                  <input
                    id="remember-me"
                    name="remember-me"
                    type="checkbox"
                    className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                  />
                  <label htmlFor="remember-me" className="ml-2 block text-sm text-gray-900">
                    Remember me
                  </label>
                </div>
                <div className="text-sm">
                  <a href="#" className="font-medium text-blue-600 hover:text-blue-500">
                    Forgot your password?
                  </a>
                </div>
              </div>
              <div>
                <button
                  type="submit"
                  className={`w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 ${isLoading ? 'opacity-70 cursor-not-allowed' : ''}`}
                  disabled={isLoading}
                >
                  {isLoading ? 'Signing in...' : 'Sign in'}
                </button>
              </div>
            </form>
            <div className="mt-4 text-center">
              <p className="text-sm text-gray-600">
                Don't have an account? 
                <button 
                  className="font-medium text-blue-600 hover:text-blue-500 ml-1"
                  onClick={toggleLoginModal}
                  disabled={isLoading}
                >
                  Sign up
                </button>
              </p>
            </div>
          </div>
        </div>
      )}

      {/* Two-Factor Authentication Modal */}
      {showTwoFactorModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-8 rounded-lg shadow-xl max-w-md w-full relative">
            <h2 className="text-2xl font-bold text-center mb-6">Two-Factor Authentication</h2>
            
            <p className="mb-4 text-center text-gray-600">
              Please enter the verification code sent to your device.
            </p>
            
            {/* Error Message in 2FA Modal */}
            {errorMsg && (
              <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4">
                {errorMsg}
              </div>
            )}
            
            <form onSubmit={handleTwoFactorSubmit} className="space-y-6">
              <div>
                <label htmlFor="verificationCode" className="block text-sm font-medium text-gray-700">Verification Code</label>
                <input
                  id="verificationCode"
                  name="code"
                  type="text"
                  required
                  className="mt-1 appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  placeholder="Enter code"
                  value={twoFactorData.code}
                  onChange={handleTwoFactorInputChange}
                  disabled={isLoading}
                />
              </div>
              <div>
                <button
                  type="submit"
                  className={`w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 ${isLoading ? 'opacity-70 cursor-not-allowed' : ''}`}
                  disabled={isLoading}
                >
                  {isLoading ? 'Verifying...' : 'Verify'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Footer */}
      <Footer />
    </div>
  );
}

export default SignupPage;