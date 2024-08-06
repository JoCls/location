import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import AdminRoute from './components/AdminRoute';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import MenuPage from './pages/MenuPage';
import ReservationsPage from './pages/ReservationsPage'; 
import CreateReservationPage from './pages/CreateReservationPage'; 
import AdminPanelPage from './pages/AdminPanelPage';
import ReserveEquipmentPage from './pages/ReserveEquipmentPage';
import ReserveClassroomPage from './pages/ReserveClassroomPage';
import UserManagementPage from './pages/admin/UserManagementPage';
import ItemManagementPage from './pages/admin/ItemManagementPage';
import ReservationManagementPage from './pages/admin/ReservationManagementPage';


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
        {/* Admin section routes */}
        <Route path="/admin" element={<AdminRoute><AdminPanelPage /></AdminRoute>}/>
        <Route path="/admin/users" element={<AdminRoute><UserManagementPage /></AdminRoute>} />
        <Route path="/admin/items" element={<AdminRoute><ItemManagementPage /></AdminRoute>} />
        <Route path="/admin/reservations" element={<AdminRoute><ReservationManagementPage /></AdminRoute>} />

        {/* Fallback route: Redirects to login if the path doesn't match */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  </AuthProvider>

  );
}

export default App;