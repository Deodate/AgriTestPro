import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Header from '../Components/Header/Header';
import Footer from '../Components/Footer/Footer';
import './SignUp.css';

/**
 * SignUp component with updated UI and circle title
 * @param {Object} props - Component props
 * @returns {JSX.Element} - The rendered SignUp page
 */
const SignUp = (props) => {
  const navigate = useNavigate();
  
  // Form state
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
    fullName: "",
    phoneNumber: "",
    jobTitle: "",
    company: "",
    twoFactorEnabled: false,
    role: "USER" // Default role
  });
  
  // Error and success messages
  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  
  // Handle input changes
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value
    });
    
    // Clear error for this field when user starts typing
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: ""
      });
    }
  };
  
  // Validate form
  const validateForm = () => {
    const newErrors = {};
    
    // Username validation
    if (!formData.username) {
      newErrors.username = "Username is required";
    } else if (formData.username.length < 4) {
      newErrors.username = "Username must be at least 4 characters";
    }
    
    // Email validation
    if (!formData.email) {
      newErrors.email = "Email is required";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "Email address is invalid";
    }
    
    // Password validation
    if (!formData.password) {
      newErrors.password = "Password is required";
    } else if (formData.password.length < 8) {
      newErrors.password = "Password must be at least 8 characters";
    } else if (!/(?=.*\d)(?=.*[a-z])(?=.*[A-Z])/.test(formData.password)) {
      newErrors.password = "Password must contain at least one number, one lowercase and one uppercase letter";
    }
    
    // Confirm password validation
    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match";
    }
    
    // Full name validation
    if (!formData.fullName) {
      newErrors.fullName = "Full name is required";
    }
    
    // Phone number validation
    if (!formData.phoneNumber) {
      newErrors.phoneNumber = "Phone number is required";
    } else if (!/^\d{10,12}$/.test(formData.phoneNumber.replace(/[-()\s]/g, ''))) {
      newErrors.phoneNumber = "Please enter a valid phone number";
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };
  
  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }
    
    setIsLoading(true);
    
    try {
      // This would be your API call to register the user
      // For now, we'll simulate it with a timeout
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      setSuccessMessage("Registration successful! Redirecting to login...");
      
      // Clear form
      setFormData({
        username: "",
        email: "",
        password: "",
        confirmPassword: "",
        fullName: "",
        phoneNumber: "",
        jobTitle: "",
        company: "",
        twoFactorEnabled: false,
        role: "USER"
      });
      
      // Redirect after a delay
      setTimeout(() => {
        navigate('/login');
      }, 2000);
      
    } catch (error) {
      setErrors({
        form: error.message || "Registration failed. Please try again."
      });
    } finally {
      setIsLoading(false);
    }
  };
  
  return (
    <div className="workshop-registration">
      {/* Use the same Header component */}
      <Header {...props} />
      
      {/* Main Form Content */}
      <div className="workshop-form-container">
        <div className="workshop-form-wrapper">
          <div className="circle-title">
            <span>SIGNUP FORM</span>
          </div>
          
          {/* Success Message */}
          {successMessage && (
            <div className="success-message">
              {successMessage}
            </div>
          )}
          
          {/* Form Error */}
          {errors.form && (
            <div className="error-message">
              {errors.form}
            </div>
          )}
          
          <form onSubmit={handleSubmit} className="workshop-form">
            {/* Full Name */}
            <div className="form-group">
              <label htmlFor="fullName">Full Name:</label>
              <div className="input-container">
                <input
                  type="text"
                  id="fullName"
                  name="fullName"
                  value={formData.fullName}
                  onChange={handleChange}
                  className={errors.fullName ? "error" : ""}
                />
                {errors.fullName && (
                  <p className="error-text">{errors.fullName}</p>
                )}
              </div>
            </div>
            
            {/* Email */}
            <div className="form-group">
              <label htmlFor="email">Email:</label>
              <div className="input-container">
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  className={errors.email ? "error" : ""}
                />
                {errors.email && (
                  <p className="error-text">{errors.email}</p>
                )}
              </div>
            </div>
          
            {/* Phone Number */}
            <div className="form-group">
              <label htmlFor="phoneNumber">Phone Number:</label>
              <div className="input-container">
                <input
                  type="tel"
                  id="phoneNumber"
                  name="phoneNumber"
                  value={formData.phoneNumber}
                  onChange={handleChange}
                  className={errors.phoneNumber ? "error" : ""}
                />
                {errors.phoneNumber && (
                  <p className="error-text">{errors.phoneNumber}</p>
                )}
              </div>
            </div>
            
            {/* Username */}
            <div className="form-group">
              <label htmlFor="username">Username:</label>
              <div className="input-container">
                <input
                  type="text"
                  id="username"
                  name="username"
                  value={formData.username}
                  onChange={handleChange}
                  className={errors.username ? "error" : ""}
                />
                {errors.username && (
                  <p className="error-text">{errors.username}</p>
                )}
              </div>
            </div>
            
            {/* Password */}
            <div className="form-group">
              <label htmlFor="password">Password:</label>
              <div className="input-container">
                <input
                  type="password"
                  id="password"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  className={errors.password ? "error" : ""}
                />
                {errors.password && (
                  <p className="error-text">{errors.password}</p>
                )}
              </div>
            </div>
            
            {/* Confirm Password */}
            <div className="form-group">
              <label htmlFor="confirmPassword">Confirm Password:</label>
              <div className="input-container">
                <input
                  type="password"
                  id="confirmPassword"
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleChange}
                  className={errors.confirmPassword ? "error" : ""}
                />
                {errors.confirmPassword && (
                  <p className="error-text">{errors.confirmPassword}</p>
                )}
              </div>
            </div>
            
            {/* Two-Factor Authentication */}
            <div className="form-group checkbox">
              <input
                type="checkbox"
                id="twoFactorEnabled"
                name="twoFactorEnabled"
                checked={formData.twoFactorEnabled}
                onChange={handleChange}
              />
              <label htmlFor="twoFactorEnabled">
                Enable Two-Factor Authentication
              </label>
            </div>
            
            {/* Submit Button */}
            <div className="form-group">
              <div className="button-group">
                <button
                  type="submit"
                  className={`register-button ${isLoading ? "loading" : ""}`}
                  disabled={isLoading}
                >
                  {isLoading ? "Processing..." : "Register"}
                </button>
                <button
                  type="button"
                  className="cancel-button"
                  onClick={() => navigate('/')}
                  disabled={isLoading}
                >
                  Cancel
                </button>
              </div>
            </div>
            
            {/* Login Link */}
            <div className="login-link">
              Already have an account?{" "}
              <Link to="/login">
                Sign in
              </Link>
            </div>
          </form>
        </div>
      </div>
      
      {/* Use the same Footer component */}
      <Footer {...props} />
    </div>
  );
};

export default SignUp;