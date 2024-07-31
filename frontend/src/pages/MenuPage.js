// src/pages/MenuPage.js
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './MenuPage.css';
import { useAuth } from '../context/AuthContext';
import './styles.css';

const MenuPage = () => {
  const navigate = useNavigate();

  const { userRole, logout } = useAuth();

  console.log('Current User Role in MenuPage:', userRole);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="menu-container">
      <div className="menu-header">
        <h2>Main Menu</h2>
      </div>
      <div className="menu-items">
        <button className="menu-button" onClick={() => navigate('/reservations')}>See My Reservations</button>
        <button className="menu-button" onClick={() => navigate('/create-reservation')}>Create a New Reservation</button>

        {userRole === 'ADMIN' && (
          <button className="menu-button admin-button" onClick={() => navigate('/admin')}>Admin Panel</button>
        )}

        <button className="logout-button" onClick={handleLogout}>Logout</button>
      </div>
    </div>
  );
};

export default MenuPage;