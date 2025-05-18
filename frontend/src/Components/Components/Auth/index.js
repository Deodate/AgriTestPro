import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../../contexts/AuthContext';
import { ROUTE_SETTINGS } from '../../../config';

const ProtectedRoute = ({ children }) => {
    const { isAuthenticated } = useAuth();
    const location = useLocation();

    // Check if the current route is public
    const isPublicRoute = ROUTE_SETTINGS.PUBLIC.includes(location.pathname);

    if (!isAuthenticated() && !isPublicRoute) {
        // Redirect to login if trying to access protected route while not authenticated
        return <Navigate to={ROUTE_SETTINGS.DEFAULT_PUBLIC_REDIRECT} state={{ from: location }} replace />;
    }

    if (isAuthenticated() && isPublicRoute) {
        // Redirect to dashboard if trying to access public route while authenticated
        return <Navigate to={ROUTE_SETTINGS.DEFAULT_AUTH_REDIRECT} replace />;
    }

    return children;
};

export default ProtectedRoute; 