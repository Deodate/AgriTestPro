import React, { createContext, useState, useContext, useEffect } from 'react';
import { userAuth } from '../services/AUTH/userAuth'; // Adjust import path as needed

// Create the AuthContext
const AuthContext = createContext({
  user: null,
  isLoggedIn: false,
  login: async () => {},
  logout: () => {},
  signup: async () => {},
});

// AuthProvider component
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  // Check authentication on initial load
  useEffect(() => {
    const checkAuthStatus = async () => {
      try {
        const storedToken = localStorage.getItem('token');
        if (storedToken) {
          // Verify token and get user info
          const userData = await userAuth.verifyToken(storedToken);
          if (userData) {
            setUser(userData);
            setIsLoggedIn(true);
          }
        }
      } catch (error) {
        // Token is invalid or expired
        localStorage.removeItem('token');
        setUser(null);
        setIsLoggedIn(false);
      } finally {
        setIsLoading(false);
      }
    };

    checkAuthStatus();
  }, []);

  // Login function
  const login = async (credentials) => {
    try {
      const response = await userAuth.login(credentials);
      
      // Assuming the response contains user info and a token
      const { token, user: userData } = response;
      
      // Store token in localStorage
      localStorage.setItem('token', token);
      
      // Update state
      setUser(userData);
      setIsLoggedIn(true);
      
      return { success: true, user: userData };
    } catch (error) {
      console.error('Login failed:', error);
      return { 
        success: false, 
        error: error.message || 'Login failed. Please try again.'
      };
    }
  };

  // Logout function
  const logout = () => {
    // Clear token from localStorage
    localStorage.removeItem('token');
    
    // Reset state
    setUser(null);
    setIsLoggedIn(false);
  };

  // Signup function
  const signup = async (signupData) => {
    try {
      const response = await userAuth.signup(signupData);
      
      // Assuming signup returns token and user info
      const { token, user: userData } = response;
      
      // Store token in localStorage
      localStorage.setItem('token', token);
      
      // Update state
      setUser(userData);
      setIsLoggedIn(true);
      
      return { success: true, user: userData };
    } catch (error) {
      console.error('Signup failed:', error);
      return { 
        success: false, 
        error: error.message || 'Signup failed. Please try again.'
      };
    }
  };

  // Prevent rendering children until auth status is determined
  if (isLoading) {
    return <div>Loading...</div>; // Or a more sophisticated loading component
  }

  return (
    <AuthContext.Provider value={{ 
      user, 
      isLoggedIn, 
      login, 
      logout, 
      signup 
    }}>
      {children}
    </AuthContext.Provider>
  );
};

// Custom hook to use the AuthContext
export const useAuth = () => {
  const context = useContext(AuthContext);
  
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  
  return context;
};