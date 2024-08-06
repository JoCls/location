// src/components/ProtectedRoute.js
import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

// ProtectedRoute component
const ProtectedRoute = ({ children }) => {
    const { isAuthenticated } = useAuth();

    console.log('isAuthenticated ProtectedRoute :', isAuthenticated);

  // If not authenticated, redirect to login page
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // If authenticated, render the children components
  return children;
};

export default ProtectedRoute;