import React, { createContext, useState, useContext, useEffect } from 'react';
import authService from '../services/authService';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Check if user is logged in on app load
    const user = authService.getCurrentUser();
    if (user) {
      setUser(user);
    }
    setLoading(false);
  }, []);

  const login = async (username, password) => {
    try {
      setError(null);
      const response = await authService.login(username, password);
      
      if (!response.requiresVerification) {
        setUser(response);
      }
      
      return response;
    } catch (err) {
      setError(err.message || 'Login failed');
      throw err;
    }
  };

  const verify2FA = async (userId, code) => {
    try {
      setError(null);
      const response = await authService.verify2FA(userId, code);
      setUser(response);
      return response;
    } catch (err) {
      setError(err.message || 'Verification failed');
      throw err;
    }
  };

  const logout = async () => {
    try {
      await authService.logout();
      setUser(null);
      setError(null);
    } catch (err) {
      setError(err.message || 'Logout failed');
      throw err;
    }
  };

  const signup = async (userData) => {
    try {
      setError(null);
      return await authService.signup(userData);
    } catch (err) {
      setError(err.message || 'Registration failed');
      throw err;
    }
  };

  const forgotPassword = async (email) => {
    try {
      setError(null);
      return await authService.forgotPassword(email);
    } catch (err) {
      setError(err.message || 'Failed to process forgot password request');
      throw err;
    }
  };

  const resetPassword = async (resetCode, newPassword, confirmPassword) => {
    try {
      setError(null);
      return await authService.resetPassword(resetCode, newPassword, confirmPassword);
    } catch (err) {
      setError(err.message || 'Failed to reset password');
      throw err;
    }
  };

  const value = {
    user,
    loading,
    error,
    login,
    logout,
    verify2FA,
    signup,
    forgotPassword,
    resetPassword,
    isAuthenticated: () => !!user
  };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export default AuthContext; 