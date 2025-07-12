import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import Header from '../Components/Header/Header';
import Footer from '../Components/Footer/Footer';
import './login.css';
import { FaLock, FaKey, FaExclamationTriangle, FaArrowLeft, FaEnvelope } from 'react-icons/fa';
import authService from '../services/authService';

/**
 * Reset Password component with UI that matches the login form
 * @returns {JSX.Element} - The rendered Reset Password page
 */
const ResetPassword = () => {
  const navigate = useNavigate();
  const location = useLocation();
  
  // Extract email from query params if available
  const queryParams = new URLSearchParams(location.search);
  const emailFromQuery = queryParams.get('email');
  
  // Form state
  const [resetCode, setResetCode] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [email, setEmail] = useState(emailFromQuery || "");
  
  // UI state
  const [isLoading, setIsLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState("");
  const [showCodeInput, setShowCodeInput] = useState(false);
  const [maskedPhone, setMaskedPhone] = useState("");
  
  // Handle requesting reset code
  const handleRequestCode = async (e) => {
    e.preventDefault();
    
    if (!email) {
      setErrors({ email: "Email is required" });
      return;
    }
    
    setIsLoading(true);
    setErrors({});
    
    try {
      const response = await authService.forgotPassword(email);
      setSuccessMessage(response.message);
      setShowCodeInput(true);
      
      // Store masked phone number for display
      if (response.debug) {
        setMaskedPhone(response.debug.phoneNumber ? 
          `****${response.debug.phoneNumber.slice(-3)}` : "***");
      }
    } catch (error) {
      console.error('Reset password error:', error);
      
      // Handle different error formats
      if (error.response) {
        // Error from axios with response
        const responseData = error.response.data;
        
        if (responseData.error === 'email_not_found' || responseData.status === 404) {
          setErrors({
            form: "We couldn't find an account with that email address. Please check your email or create a new account."
          });
        } else {
          setErrors({
            form: responseData.message || "Failed to send reset code. Please try again later."
          });
        }
      } else if (error.error === 'email_not_found') {
        // Direct error object with error property
        setErrors({
          form: "We couldn't find an account with that email address. Please check your email or create a new account."
        });
      } else if (error.message) {
        // Error with message property (could be Error object or custom error)
        const errorMsg = error.message;
        if (errorMsg.includes('not registered') || errorMsg.includes('not found')) {
          setErrors({
            form: "We couldn't find an account with that email address. Please check your email or create a new account."
          });
        } else {
          setErrors({
            form: errorMsg
          });
        }
      } else {
        // Fallback for unknown error format
        setErrors({
          form: "An unexpected error occurred. Please try again later."
        });
      }
    } finally {
      setIsLoading(false);
    }
  };
  
  // Handle password reset
  const handleResetPassword = async (e) => {
    e.preventDefault();
    
    // Form validation
    const newErrors = {};
    
    if (!resetCode) {
      newErrors.resetCode = "Reset code is required";
    }
    
    if (!newPassword) {
      newErrors.newPassword = "New password is required";
    } else if (newPassword.length < 6) {
      newErrors.newPassword = "Password must be at least 6 characters";
    }
    
    if (!confirmPassword) {
      newErrors.confirmPassword = "Please confirm your password";
    } else if (newPassword !== confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match";
    }
    
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }
    
    setIsLoading(true);
    setErrors({});
    
    try {
      // Try the reset-password endpoint first (uses token parameter)
      let response;
      try {
        response = await authService.resetPassword(resetCode, newPassword, confirmPassword);
      } catch (err) {
        // If that fails, try the verify-reset-code endpoint (uses code parameter)
        response = await authService.verifyResetCode(resetCode, newPassword, confirmPassword);
      }
      
      setSuccessMessage(response.message || "Password has been reset successfully! You can now login with your new password.");
      
      // Redirect to login after 3 seconds
      setTimeout(() => {
        navigate('/login');
      }, 3000);
    } catch (error) {
      setErrors({
        form: error.message || "Failed to reset password. Code may be invalid or expired."
      });
    } finally {
      setIsLoading(false);
    }
  };
  
  // Function to render error messages
  const renderErrorMessages = () => {
    if (!errors || Object.keys(errors).length === 0) return null;
    
    return (
      <div className="error-message">
        <FaExclamationTriangle style={{ marginRight: '0.5rem' }} />
        {errors.form || 
         (errors.email && `Email Error: ${errors.email}`) || 
         (errors.resetCode && `Code Error: ${errors.resetCode}`) || 
         (errors.newPassword && `Password Error: ${errors.newPassword}`) || 
         (errors.confirmPassword && `Confirm Password Error: ${errors.confirmPassword}`)}
      </div>
    );
  };
  
  return (
    <div className="workshop-registration">
      <Header />
      
      <div className="login-container">
        <div className="login-form-container">
          <div className="login-title">
            <h2>{showCodeInput ? 'Reset Password' : 'Forgot Password'}</h2>
          </div>
          
          {/* Success message notification */}
          {successMessage && (
            <div className="success-message">{successMessage}</div>
          )}
          
          {/* Back to login button */}
          <div className="back-to-login">
            <button 
              type="button" 
              className="back-button" 
              onClick={() => navigate('/login')}
            >
              <FaArrowLeft /> Back to Login
            </button>
          </div>
          
          {!showCodeInput ? (
            // Email input form to request reset code
            <form onSubmit={handleRequestCode} className="compact-form">
              <div className="input-container">
                <input
                  type="email"
                  id="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="Enter your email"
                  className={errors.email ? 'error' : ''}
                  required
                />
              </div>
              
              {/* Error messages displayed below the form */}
              {renderErrorMessages()}
              
              <button
                type="submit"
                className="login-button"
                disabled={isLoading}
              >
                {isLoading ? 'Sending...' : 'Send Reset Code'}
              </button>
            </form>
          ) : (
            // Reset password form
            <form onSubmit={handleResetPassword} className="compact-form">
              <div className="input-container">
                <input
                  type="text"
                  id="resetCode"
                  value={resetCode}
                  onChange={(e) => setResetCode(e.target.value)}
                  placeholder={`Please enter code sent on ${maskedPhone}`}
                  className={errors.resetCode ? 'error' : ''}
                  maxLength="4"
                  required
                />
              </div>
              
              <div className="input-container">
                <input
                  type="password"
                  id="newPassword"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  placeholder="Create password"
                  className={errors.newPassword ? 'error' : ''}
                  required
                />
              </div>
              
              <div className="input-container">
                <input
                  type="password"
                  id="confirmPassword"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  placeholder="Confirm password"
                  className={errors.confirmPassword ? 'error' : ''}
                  required
                />
              </div>
              
              {/* Error messages displayed below the form */}
              {renderErrorMessages()}
              
              {(process.env.NODE_ENV === 'development' && resetCode) && (
                <p className="dev-info">Using code: {resetCode}</p>
              )}
              {maskedPhone && (
                <p className="masked-phone">Code sent to {maskedPhone}</p>
              )}
              
              <div className="button-container">
                <button
                  type="submit"
                  className="login-button"
                  disabled={isLoading}
                >
                  {isLoading ? 'Resetting...' : 'Reset Password'}
                </button>
                <button
                  type="button"
                  className="cancel-button"
                  onClick={() => setShowCodeInput(false)}
                >
                  Back
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
      
      <Footer />
    </div>
  );
};

export default ResetPassword; 