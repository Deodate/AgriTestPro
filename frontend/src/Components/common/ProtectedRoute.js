import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';

const ProtectedRoute = ({ children }) => {
  const { user, loading, isAuthenticated } = useAuth();
  const location = useLocation();

  // Show loading state while checking authentication
  if (loading) {
    return null; // or a loading spinner if you prefer
  }

  // Check if user is authenticated
  if (!isAuthenticated()) {
    // Redirect to login while preserving the attempted URL
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return children;
};

export default ProtectedRoute; 