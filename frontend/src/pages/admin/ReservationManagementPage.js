// src/pages/admin/ReservationManagementPage.js

import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ReservationManagementPage = () => {
  const [reservations, setReservations] = useState([]);

  useEffect(() => {
    fetchReservations();
  }, []);

  const fetchReservations = async () => {
    try {
      const response = await axios.get('/api/reservations'); // API to fetch reservations
      setReservations(response.data);
    } catch (error) {
      console.error('Error fetching reservations:', error);
    }
  };

  const updateReservationStatus = async (id, status) => {
    try {
      await axios.put(`/api/reservations/${id}`, { status });
      fetchReservations();
    } catch (error) {
      console.error('Error updating reservation:', error);
    }
  };

  return (
    <div className="reservation-management">
      <h3>Manage Reservations</h3>
      <table className="reservation-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>User</th>
            <th>Item</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {reservations.map((reservation) => (
            <tr key={reservation.id}>
              <td>{reservation.id}</td>
              <td>{reservation.user}</td>
              <td>{reservation.item}</td>
              <td>{reservation.status}</td>
              <td>
                {reservation.status !== 'CANCEL' && (
                  <button onClick={() => updateReservationStatus(reservation.id, 'CANCEL')}>
                    Cancel
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ReservationManagementPage;