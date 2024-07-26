import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import MenuPage from './pages/MenuPage';
import ReservationsPage from './pages/ReservationsPage'; 
import CreateReservationPage from './pages/CreateReservationPage'; 
import AdminPanelPage from './pages/AdminPanelPage';
import { AuthProvider } from './context/AuthContext';

function App() {
  const isAuthenticated = true; 
  const userRole = "ADMIN"; 
  return (
    <AuthProvider>
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/menu" element={<MenuPage />} />
      </Routes>
    </Router>
  </AuthProvider>

  );
}

export default App;