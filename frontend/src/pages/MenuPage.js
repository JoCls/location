// src/pages/MenuPage.js
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './MenuPage.css';
import { useAuth } from '../context/AuthContext';

const MenuPage = () => {
  const navigate = useNavigate();

  const { userRole, logout } = useAuth();

  console.log('Current User Role in ManuPage:', userRole);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="menu-page">
      <header className="menu-header">
        <h1>Welcome to the Reservation System</h1>
      </header>

      <main className="menu-main">
        <Link to="/reservations" className="menu-button">
          See My Reservations
        </Link>
        <Link to="/create-reservation" className="menu-button">
          Create a New Reservation
        </Link>
        {userRole === 'ADMIN' && (
          <Link to="/admin-panel" className="menu-button admin-button">
            Admin Panel
          </Link>
        )}
      </main>

      <button className="logout-button" onClick={handleLogout}>
        Logout
      </button>
    </div>
  );
};

export default MenuPage;