// src/components/AdminRoute.js

import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const AdminRoute = ({ children }) => {
  const { isAuthenticated, userRole } = useAuth();

  if (isAuthenticated === undefined) {
    return null;
  }

  console.log('AdminRoute : isAuthenticated : ', isAuthenticated);
  console.log('AdminRoute : userRole : ', userRole);

  return isAuthenticated && userRole === 'ADMIN' ? children : <Navigate to="/login" replace />;
};

export default AdminRoute;