// src/contexts/AuthContext.jsx
import React, { createContext, useContext, useState } from 'react';

// Create context
const AuthContext = createContext();

// Auth provider component
export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(false);

  // Register function
  const register = async (userData) => {
    setLoading(true);
    try {
      // In a real app, you would call your API here
      console.log("Registering user:", userData);
      
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // Success - in a real app you would store the returned user
      return { success: true };
    } catch (error) {
      console.error("Registration error:", error);
      throw new Error(error.message || "Failed to create account");
    } finally {
      setLoading(false);
    }
  };

  // Login function
  const login = async (email, password) => {
    setLoading(true);
    try {
      // In a real app, you would call your API here
      console.log("Logging in user:", { email, password });
      
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // For demo purposes, simulate a user response
      const user = { 
        id: "user123", 
        email: email,
        firstName: "Demo",
        lastName: "User" 
      };
      
      setCurrentUser(user);
      return { success: true };
      
      // Uncomment to simulate 2FA
      // return { 
      //   requiresVerification: true,
      //   userId: "user123"
      // };
    } catch (error) {
      console.error("Login error:", error);
      throw new Error(error.message || "Invalid email or password");
    } finally {
      setLoading(false);
    }
  };

  // Two-factor verification
  const verifyTwoFactor = async (userId, code) => {
    setLoading(true);
    try {
      // In a real app, you would call your API here
      console.log("Verifying 2FA:", { userId, code });
      
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // For demo, simulate verification success
      if (code === "123456") {
        const user = { 
          id: userId, 
          email: "user@example.com",
          firstName: "Demo",
          lastName: "User" 
        };
        
        setCurrentUser(user);
        return { success: true };
      } else {
        throw new Error("Invalid verification code");
      }
    } catch (error) {
      console.error("2FA error:", error);
      throw new Error(error.message || "Verification failed");
    } finally {
      setLoading(false);
    }
  };

  // Logout function
  const logout = () => {
    setCurrentUser(null);
  };

  const value = {
    currentUser,
    loading,
    register,
    login,
    verifyTwoFactor,
    logout
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

// Custom hook to use the auth context
export const useAuth = () => {
  return useContext(AuthContext);
};

export default AuthContext;