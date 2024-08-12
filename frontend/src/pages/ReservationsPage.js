// src/pages/ReservationsPage.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './styles.css';

const ReservationsPage = () => {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const navigate = useNavigate();

  const handleGoBack = () => {
    navigate('/menu');
  }

  useEffect(() => {
    const fetchReservations = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/reservations/user');
        const sortedReservations = response.data.sort((a, b) => new Date(a.startTime) - new Date(b.startTime));
        setReservations(sortedReservations);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch reservations');
        setLoading(false);
      }
    };
    fetchReservations();
  }, []);

  const getStatusClass  = (startTime, endTime, reservationStatus) => {
    const now = new Date();
    const start = new Date(startTime);
    const end = new Date(endTime);
    
    if (reservationStatus === 'CANCELLED') return 'reservation-cancelled';
    if (end < now) return 'reservation-past';
    if (start <= now && end >= now) return 'reservation-ongoing';
    return 'reservation-future';
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;
  
  return (
    <div className="reservations-page">
      <h2>My Reservations</h2>
      <table className="reservation-table">
        <thead>
          <tr>
            <th>User</th>
            <th>Item Name</th>
            <th>Item Type</th>
            <th>Description</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {reservations.map((reservation) => (
            <tr key={reservation.id} className={getStatusClass(reservation.startTime, reservation.endTime, reservation.reservationStatus)}>
              <td>{reservation.user?.username}</td>
              <td>{reservation.item?.name}</td>
              <td>{reservation.item?.itemType}</td>
              <td>{reservation.item?.description}</td>
              <td>{new Date(reservation.startTime).toLocaleString()}</td>
              <td>{new Date(reservation.endTime).toLocaleString()}</td>
              <td>{reservation.reservationStatus}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <button type="button" onClick={handleGoBack} className="back-button">
        Back to Previous Menu
      </button>
    </div>
  );
};

export default ReservationsPage;