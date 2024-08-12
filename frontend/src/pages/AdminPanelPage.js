// src/pages/AdminPanelPage.js

import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './styles.css'; // Assuming this is where your styles are

const AdminPanelPage = () => {
  const navigate = useNavigate();
  const { userRole } = useAuth();

  console.log('Current User Role in AdminPanelPage:', userRole);

  const handleUserManagement = () => {
    navigate('/admin/users');
  };

  const handleItemManagement = () => {
    navigate('/admin/items');
  };

  const handleReservationManagement = () => {
    navigate('/admin/reservations');
  };

  const handleGoBack = () => {
    navigate('/menu');
  }

  return (
    <div className="create-reservation-page">
      <h2>Admin Panel</h2>
      <div className="create-reservation-options">
        <button onClick={handleUserManagement}>Manage Users</button>
        <button onClick={handleItemManagement}>Manage Items</button>
        <button onClick={handleReservationManagement}>Manage Reservations</button>
      </div>
      <button onClick={handleGoBack} className="back-button">
        Back to Previous Menu
      </button>
    </div>
  );
};

export default AdminPanelPage;