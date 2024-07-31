import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import MenuPage from './pages/MenuPage';
import ReservationsPage from './pages/ReservationsPage'; 
import CreateReservationPage from './pages/CreateReservationPage'; 
import AdminPanelPage from './pages/AdminPanelPage';
import ReserveEquipmentPage from './pages/ReserveEquipmentPage';
import ReserveClassroomPage from './pages/ReserveClassroomPage';

function App() {
  return (
    <AuthProvider>
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        {/* Protect the following routes */}
        <Route path="/menu" element={<ProtectedRoute><MenuPage /></ProtectedRoute>}/>
        <Route path="/reservations" element={<ProtectedRoute><ReservationsPage /></ProtectedRoute>}/>
        <Route path="/create-reservation" element={<ProtectedRoute><CreateReservationPage /></ProtectedRoute>}/>
        <Route path="/reserve-equipment" element={<ProtectedRoute><ReserveEquipmentPage /></ProtectedRoute>}/>
        <Route path="/reserve-classroom" element={<ProtectedRoute><ReserveClassroomPage /></ProtectedRoute>}/>
        <Route path="/admin" element={<ProtectedRoute><AdminPanelPage /></ProtectedRoute>}/>

        {/* Fallback route: Redirects to login if the path doesn't match */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  </AuthProvider>

  );
}

export default App;