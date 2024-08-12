// src/pages/CreateReservationPage.js

import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './styles.css';

const CreateReservationPage = () => {
  const navigate = useNavigate();
  const { userRole } = useAuth();

  console.log('Current User Role in CreateReservationPage:', userRole);

  const handleEquipmentReservation = () => {
    navigate('/reserve-equipment');
  };

  const handleClassroomReservation = () => {
    navigate('/reserve-classroom');
  };

  const handleGoBack = () => {
    navigate('/menu');
  }

  return (
    <div className="create-reservation-page">
      <h2>Create a Reservation</h2>
      <div className="create-reservation-options">
        <button onClick={handleEquipmentReservation}>Reserve an equipment</button>
        {userRole !== 'STUDENT' && (
          <button onClick={handleClassroomReservation}>Reserve a classroom</button>
        )}
      </div>
      <button onClick={handleGoBack} className="back-button">
          Back to Previous Menu
        </button>
    </div>
  );
};

export default CreateReservationPage;