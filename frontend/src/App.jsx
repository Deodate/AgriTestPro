import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import SignupPage from './components/pages/auth/SignupPage';
import ProtectedRoute from './components/auth/ProtectedRoute';
import DashboardLayout from './components/layout/DashboardLayout';
import DashboardHome from './components/pages/dashboard/DashboardHome';
import TestCases from './components/pages/dashboard/TestCases';
import Settings from './components/pages/dashboard/Settings';
import HomePage from './components/pages/HomePage';

const App = () => {
  return (
    <Routes>
      {/* Public Routes */}
      <Route path="/" element={<HomePage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route path="/login" element={<SignupPage />} />

      {/* Protected Routes */}
      <Route path="/dashboard" element={<ProtectedRoute />}>
        <Route element={<DashboardLayout />}>
          <Route index element={<DashboardHome />} />
          <Route path="test-cases" element={<TestCases />} />
          <Route path="settings" element={<Settings />} />
        </Route>
      </Route>

      {/* Catch all route - redirect to home */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
};

export default App;