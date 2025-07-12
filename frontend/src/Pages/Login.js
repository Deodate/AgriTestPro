import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Header from '../Components/Header/Header';
import Footer from '../Components/Footer/Footer';
import './login.css';
import { useAuth } from '../contexts/AuthContext';
import authService from '../services/authService';
import { FaUser, FaLock, FaUsers, FaEnvelope, FaPhone, FaExclamationTriangle, FaUserPlus, FaArrowLeft } from 'react-icons/fa';
import { setPersistentError, clearErrorTimeouts } from '../utils/errorUtils';

/**
 * Login component with updated UI and circle title
 * @param {Object} props - Component props
 * @returns {JSX.Element} - The rendered Login page
 */
const Login = (props) => {
  const navigate = useNavigate();
  const { login, verify2FA, error: authError } = useAuth();
  
  // Form state
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    verificationCode: "",
    email: "",
    rememberMe: false,
    confirmPassword: "",
    phoneNumber: "",
  });
  
  // UI state
  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [requires2FA, setRequires2FA] = useState(false);
  const [userId, setUserId] = useState(null);
  const [showForgotPassword, setShowForgotPassword] = useState(false);
  const [resetCodeSent, setResetCodeSent] = useState(false);
  const [resetCode, setResetCode] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmNewPassword, setConfirmNewPassword] = useState("");
  const [maskedPhone, setMaskedPhone] = useState("");
  const [isSignup, setIsSignup] = useState(false);
  const [errorType, setErrorType] = useState(null);
  
  // Clean up error timeouts when component unmounts
  useEffect(() => {
    return () => {
      clearErrorTimeouts();
    };
  }, []);
  
  // Handle input changes without clearing errors
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
    // Errors will not be cleared on typing - they will persist for 1 minute
  };
  
  // Handle forgot password submit
  const handleForgotPassword = async (e) => {
    e.preventDefault();
    if (!formData.email) {
      setPersistentError(setErrors, { email: "Email is required" }, 'forgotPassword');
      return;
    }
    
    setIsLoading(true);
    try {
      const response = await authService.forgotPassword(formData.email);
      setSuccessMessage(response.message);
      // Store masked phone number for display
      if (response.debug) {
        setMaskedPhone(response.debug.phoneNumber ? 
          `****${response.debug.phoneNumber.slice(-3)}` : "***");
        // For development purposes, auto-fill the reset code
        if (process.env.NODE_ENV === 'development') {
          setResetCode(response.debug.resetCode);
        }
      }
      setResetCodeSent(true);
    } catch (error) {
      setPersistentError(setErrors, {
        form: error.message || "Failed to process forgot password request. Please verify your email."
      }, 'forgotPassword');
    } finally {
      setIsLoading(false);
    }
  };

  // Handle reset password submit
  const handleResetPassword = async (e) => {
    e.preventDefault();
    if (!resetCode || !newPassword || !confirmNewPassword) {
      setPersistentError(setErrors, {
        form: "Please fill in all fields"
      }, 'resetPassword');
      return;
    }
    
    if (newPassword !== confirmNewPassword) {
      setPersistentError(setErrors, {
        form: "Passwords do not match"
      }, 'resetPassword');
      return;
    }
    
    if (newPassword.length < 6) {
      setPersistentError(setErrors, {
        form: "Password must be at least 6 characters long"
      }, 'resetPassword');
      return;
    }
    
    setIsLoading(true);
    try {
      // Try the reset-password endpoint first (uses token parameter)
      let response;
      try {
        response = await authService.resetPassword(resetCode, newPassword, confirmNewPassword);
      } catch (err) {
        // If that fails, try the verify-reset-code endpoint (uses code parameter)
        response = await authService.verifyResetCode(resetCode, newPassword, confirmNewPassword);
      }
      
      setSuccessMessage(response.message || "Password reset successful! Please login with your new password.");
      setTimeout(() => {
        setShowForgotPassword(false);
        setResetCodeSent(false);
        setSuccessMessage("");
        setNewPassword("");
        setConfirmNewPassword("");
        setResetCode("");
      }, 3000);
    } catch (error) {
      setPersistentError(setErrors, {
        form: error.message || "Failed to reset password. Code may be invalid or expired."
      }, 'resetPassword');
    } finally {
      setIsLoading(false);
    }
  };
  
  // Validate signup form
  const validateSignupForm = () => {
    const newErrors = {};
    
    if (!formData.username) {
      newErrors.username = "Username is required";
    }
    
    if (!formData.email) {
      newErrors.email = "Email is required";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "Invalid email format";
    }
    
    if (!formData.password) {
      newErrors.password = "Password is required";
    }
    
    if (!formData.confirmPassword) {
      newErrors.confirmPassword = "Please confirm your password";
    } else if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match";
    }
    
    if (!formData.phoneNumber) {
      newErrors.phoneNumber = "Phone number is required";
    }
    
    if (Object.keys(newErrors).length > 0) {
      setPersistentError(setErrors, newErrors, 'validateSignup');
      return false;
    }
    
    return true;
  };

  // Handle signup submit
  const handleSignup = async (e) => {
    e.preventDefault();
    
    if (!validateSignupForm()) {
      return;
    }
    
    setIsLoading(true);
    setErrors({});
    
    try {
      const response = await authService.signup({
        username: formData.username,
        email: formData.email,
        password: formData.password,
        confirmPassword: formData.confirmPassword,
        phoneNumber: formData.phoneNumber,
        fullName: formData.username, // Use username as default full name
        role: "USER" // Default role
      });
      
      setSuccessMessage("Registration successful! Please login.");
      setIsSignup(false);
      setFormData({
        ...formData,
        password: "",
        confirmPassword: "",
        phoneNumber: ""
      });
    } catch (error) {
      // Handle validation errors if they're returned from the backend
      if (error.errors) {
        const fieldErrors = {};
        
        // Map backend errors to form fields
        Object.entries(error.errors).forEach(([field, message]) => {
          fieldErrors[field] = message;
        });
        
        // Add a general error message
        fieldErrors.form = "Please fix the errors in the form";
        
        setPersistentError(setErrors, fieldErrors, 'signupValidation');
      } else {
        setPersistentError(setErrors, {
        form: error.message || "Registration failed. Please try again."
        }, 'signup');
      }
    } finally {
      setIsLoading(false);
    }
  };
  
  // Validate form
  const validateForm = () => {
    const newErrors = {};
    
    if (!requires2FA) {
      // Username validation
      if (!formData.username) {
        newErrors.username = "Username is required";
      } else if (formData.username.includes('@')) {
        newErrors.username = "Please use your username, not email address";
      }
      
      // Password validation
      if (!formData.password) {
        newErrors.password = "Password is required";
      }
    } else {
      // Verification code validation
      if (!formData.verificationCode) {
        newErrors.verificationCode = "Verification code is required";
      }
    }
    
    if (Object.keys(newErrors).length > 0) {
      setPersistentError(setErrors, newErrors, 'validateForm');
      return false;
    }
    
    return true;
  };
  
  // Handle login form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log("Login form submitted.");

    if (!validateForm()) {
      console.log("Form validation failed.");
      return;
    }

    setIsLoading(true);
    setErrors({});

    try {
      console.log("Attempting login with username:", formData.username);
      const response = await login(formData.username, formData.password);
      console.log("Login successful. Response:", response);

      if (response.requiresVerification) {
        // Show 2FA form
        setRequires2FA(true);
        setUserId(response.userId);
        setMaskedPhone(response.phoneNumber ? response.phoneNumber.slice(-3) : "xxx");
        setSuccessMessage(`Code sent to ****${response.phoneNumber ? response.phoneNumber.slice(-3) : "xxx"}`);
      } else {
        // Normal login success
        setSuccessMessage("Login successful! Redirecting...");
        setTimeout(() => {
          navigate('/dashboard');
        }, 1500);
      }
    } catch (error) {
      console.error("Login error:", error);
      // Handle specific error types from the backend with 1-minute persistence
      if (error.error) {
        switch (error.error) {
          case 'user_not_found':
            setPersistentError(setErrors, {
              username: error.message || "Username not found",
              form: "Account not found. Please check your username or register a new account."
            }, 'login');
            break;
          case 'invalid_password':
            setPersistentError(setErrors, {
              password: "Incorrect password",
              form: error.message || "Incorrect password. Please try again."
            }, 'login');
            break;
          case 'account_disabled':
            setPersistentError(setErrors, {
              form: error.message || "This account has been disabled. Please contact administration."
            }, 'login');
            break;
          default:
            setPersistentError(setErrors, {
              form: error.message || "Login failed. Please try again."
            }, 'login');
        }
      } else {
        setPersistentError(setErrors, {
        form: error.message || "Login failed. Please try again."
        }, 'login');
      }
    } finally {
      console.log("Login process finished. Setting isLoading to false.");
      setIsLoading(false);
    }
  };

  // Handle 2FA verification
  const handleVerify2FA = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }
    
    setIsLoading(true);
    setErrors({});
    
    try {
      await verify2FA(userId, formData.verificationCode);
      setSuccessMessage("Verification successful!");
      // Immediately navigate to dashboard
      navigate('/dashboard');
    } catch (err) {
      setPersistentError(setErrors, {
        form: err.message || "Verification failed. Please try again."
      }, '2fa');
      setIsLoading(false);
    }
  };

  // Handle cancel 2FA
  const handleCancel2FA = () => {
    setRequires2FA(false);
    setFormData({
      ...formData,
      verificationCode: ""
    });
    setSuccessMessage("");
  };

  // Handle remember me change
  const handleRememberMe = (e) => {
    setFormData({
      ...formData,
      rememberMe: e.target.checked
    });
  };
  
  return (
    <div className="workshop-registration">
      <Header />
      
      <div className="login-container">
        <div className="login-form-container">
          <div className="login-title">
            {showForgotPassword ? (
              resetCodeSent ? (
                <h2>Reset Password</h2>
              ) : (
                <h2>Forgot Password</h2>
              )
            ) : (
              <>
            <FaUsers className="login-title-icon" />
            <h2>{requires2FA ? 'Verify Code' : isSignup ? 'Create Account' : 'Login'}</h2>
              </>
            )}
          </div>

          {requires2FA ? (
            // 2FA Verification Form
            <form onSubmit={handleVerify2FA} className="verification-form">
              {successMessage && (
                <div className="success-message">{successMessage}</div>
              )}
              {(errors.form || authError) && (
                <div className="error-message">{errors.form || authError}</div>
              )}

              <div className="verification-info">
                <FaPhone className="info-icon" />
                <span>Code sent to ****{maskedPhone}</span>
              </div>

              <div className="input-container">
                <FaLock className="input-icon" />
                <input
                  type="text"
                  name="verificationCode"
                  placeholder="Enter verification code"
                  value={formData.verificationCode}
                  onChange={handleChange}
                  className={errors.verificationCode ? 'error' : ''}
                  maxLength="6"
                />
                {errors.verificationCode && (
                  <div className="error-text">{errors.verificationCode}</div>
                )}
              </div>

              <div className="login-actions">
                <button
                  type="submit"
                  className="login-button"
                  disabled={isLoading}
                >
                  {isLoading ? 'Verifying...' : 'Verify'}
                </button>
                <button
                  type="button"
                  className="cancel-button"
                  onClick={() => {
                    setRequires2FA(false);
                    setFormData({
                      ...formData,
                      verificationCode: ""
                    });
                    setSuccessMessage("");
                  }}
                  disabled={isLoading}
                >
                  Cancel
                </button>
              </div>
            </form>
          ) : showForgotPassword ? (
            // Forgot Password Inline Form
            resetCodeSent ? (
              // Reset Password Form
              <form onSubmit={handleResetPassword} className="workshop-form">
                {successMessage && (
                  <div className="success-message">{successMessage}</div>
                )}
                {errors.form && (
                  <div className="error-message">
                    <FaExclamationTriangle style={{ marginRight: '0.5rem' }} />
                    {errors.form}
                  </div>
                )}
                
                <div className="form-group">
                  <label htmlFor="resetCode">Reset Code:</label>
                  <div className="input-container">
                    <input
                      type="text"
                      id="resetCode"
                      value={resetCode}
                      onChange={(e) => setResetCode(e.target.value)}
                      placeholder="Enter reset code"
                      maxLength="4"
                      required
                    />
                  </div>
                  {process.env.NODE_ENV === 'development' && resetCode && (
                    <p className="dev-info">Using code: {resetCode}</p>
                  )}
                  {maskedPhone && (
                    <p className="masked-phone">Code sent to {maskedPhone}</p>
                  )}
                </div>
                
                <div className="form-group">
                  <label htmlFor="newPassword">New Password:</label>
                  <div className="input-container">
                    <input
                      type="password"
                      id="newPassword"
                      value={newPassword}
                      onChange={(e) => setNewPassword(e.target.value)}
                      placeholder="Enter new password"
                      required
                    />
                  </div>
                </div>
                
                <div className="form-group">
                  <label htmlFor="confirmNewPassword">Confirm Password:</label>
                  <div className="input-container">
                    <input
                      type="password"
                      id="confirmNewPassword"
                      value={confirmNewPassword}
                      onChange={(e) => setConfirmNewPassword(e.target.value)}
                      placeholder="Confirm new password"
                      required
                    />
                  </div>
                </div>
                
                <div className="form-group">
                  <div className="button-group">
                    <button
                      type="submit"
                      className={`login-button ${isLoading ? "loading" : ""}`}
                      disabled={isLoading}
                    >
                      {isLoading ? "Resetting..." : "Reset Password"}
                    </button>
                    <button
                      type="button"
                      className="cancel-button"
                      onClick={() => {
                        setShowForgotPassword(false);
                        setResetCodeSent(false);
                      }}
                    >
                      Back to Login
                    </button>
                  </div>
                </div>
              </form>
            ) : (
              // Forgot Password Form
              <form onSubmit={handleForgotPassword} className="workshop-form">
                {successMessage && (
                  <div className="success-message">{successMessage}</div>
                )}
                {errors.form && (
                  <div className="error-message">
                    <FaExclamationTriangle style={{ marginRight: '0.5rem' }} />
                    {errors.form}
                  </div>
                )}
                
                <div className="back-to-login">
                  <button 
                    type="button" 
                    className="back-button" 
                    onClick={() => setShowForgotPassword(false)}
                  >
                    <FaArrowLeft /> Back to Login
                  </button>
                </div>
                
                <div className="form-group">
                  <label htmlFor="email">Email Address:</label>
                  <div className="input-container">
                    <input
                      type="email"
                      id="email"
                      name="email"
                      value={formData.email}
                      onChange={handleChange}
                      className={errors.email ? "error" : ""}
                      placeholder="Enter your email"
                      required
                    />
                    {errors.email && (
                      <p className="error-text">{errors.email}</p>
                    )}
                  </div>
                </div>
                
                <div className="form-group">
                  <button
                    type="submit"
                    className={`login-button ${isLoading ? "loading" : ""}`}
                    disabled={isLoading}
                  >
                    {isLoading ? "Sending..." : "Send Reset Code"}
                  </button>
                </div>
              </form>
            )
          ) : (
            // Login/Signup Form
            <form onSubmit={isSignup ? handleSignup : handleSubmit}>
              {successMessage && (
                <div className="success-message">{successMessage}</div>
              )}
              {(errors.form || authError) && (
                <div className="error-message">
                  <FaExclamationTriangle style={{ marginRight: '0.5rem' }} />
                  {errors.form || authError}
                </div>
              )}
              
              {/* User not found suggestion */}
              {errorType === 'user_not_found' && !isSignup && (
                <div className="account-suggestion">
                  <FaUserPlus className="suggestion-icon" />
                  <p>
                    No account found with this username. Would you like to 
                    <button 
                      type="button" 
                      className="inline-button"
                      onClick={() => {
                        setIsSignup(true);
                        setErrors({});
                        setErrorType(null);
                      }}
                    >
                      create a new account
                    </button>
                    ?
                  </p>
                </div>
              )}

              <div className="input-container">
                <FaUser className="input-icon" />
                <input
                  type="text"
                  name="username"
                  placeholder="Username (not email)"
                  value={formData.username}
                  onChange={handleChange}
                  className={errors.username ? 'error' : ''}
                />
                {errors.username && (
                  <div className="error-text">{errors.username}</div>
                )}
              </div>

              {isSignup && (
                <div className="input-container">
                  <FaEnvelope className="input-icon" />
                  <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={formData.email}
                    onChange={handleChange}
                    className={errors.email ? 'error' : ''}
                  />
                  {errors.email && (
                    <div className="error-text">{errors.email}</div>
                  )}
                </div>
              )}

              <div className="input-container">
                <FaLock className="input-icon" />
                <input
                  type="password"
                  name="password"
                  placeholder="Password"
                  value={formData.password}
                  onChange={handleChange}
                  className={errors.password ? 'error' : ''}
                />
                {errors.password && (
                  <div className="error-text">{errors.password}</div>
                )}
              </div>

              {isSignup && (
                <>
                  <div className="input-container">
                    <FaLock className="input-icon" />
                    <input
                      type="password"
                      name="confirmPassword"
                      placeholder="Confirm Password"
                      value={formData.confirmPassword}
                      onChange={handleChange}
                      className={errors.confirmPassword ? 'error' : ''}
                    />
                    {errors.confirmPassword && (
                      <div className="error-text">{errors.confirmPassword}</div>
                    )}
                  </div>

                  <div className="input-container">
                    <FaPhone className="input-icon" />
                    <input
                      type="tel"
                      name="phoneNumber"
                      placeholder="Phone Number"
                      value={formData.phoneNumber}
                      onChange={handleChange}
                      className={errors.phoneNumber ? 'error' : ''}
                    />
                    {errors.phoneNumber && (
                      <div className="error-text">{errors.phoneNumber}</div>
                    )}
                  </div>
                </>
              )}

              {!isSignup && (
                <div className="remember-forgot">
                  <label className="remember-me">
                    <input
                      type="checkbox"
                      checked={formData.rememberMe}
                      onChange={handleRememberMe}
                    />
                    <span>Remember me</span>
                  </label>
                  <Link
                    to="/reset-password"
                    className="forgot-password"
                  >
                    Forgot Password?
                  </Link>
                </div>
              )}

              <div className="login-actions">
                <button
                  type="submit"
                  className="login-button"
                  disabled={isLoading}
                >
                  {isLoading 
                    ? (isSignup ? 'Creating Account...' : 'Logging in...') 
                    : (isSignup ? 'Create Account' : 'LOGIN')}
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

              <div className="divider">
                <span>OR</span>
              </div>

              <div className="signup-section">
                <button
                  type="button"
                  className="switch-form-button"
                  onClick={() => {
                    setIsSignup(!isSignup);
                    // Don't clear errors - they will persist for the minimum time
                    setSuccessMessage("");
                  }}
                >
                  {isSignup ? 'Already have an account? Login' : 'Create an Account'}
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

export default Login;