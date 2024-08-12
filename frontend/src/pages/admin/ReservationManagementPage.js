import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../styles.css';
import { useNavigate } from 'react-router-dom';

const ReservationAdminPage = () => {
  const [reservations, setReservations] = useState([]);
  const [editingReservation, setEditingReservation] = useState(null);
  const [formData, setFormData] = useState({
    reservationStatus: '',
  });
  const navigate = useNavigate();

  useEffect(() => {
    fetchReservations();
  }, []);

  const fetchReservations = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/reservations/all');
      setReservations(response.data);
    } catch (error) {
      console.error('Error fetching reservations:', error);
    }
  };

  const handleEdit = (reservation) => {
    setEditingReservation(reservation);
    setFormData({
      reservationStatus: reservation.reservationStatus,
    });
  };

  const handleUpdate = async (reservationId) => {
    try {
      const updatedReservation = {
        ...editingReservation,
        ...formData,
      };
      await axios.put(`http://localhost:8080/api/reservations/${reservationId}/role`, {reservationStatus: formData.reservationStatus});
      setReservations((prevReservations) =>
        prevReservations.map((reservation) =>
          reservation.id === reservationId ? updatedReservation : reservation
        )
      );
      setEditingReservation(null);
    } catch (error) {
      console.error('Error updating reservation:', error);
    }
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value,
    }));
  };

  const cancelEdit = () => {
    setEditingReservation(null);
  };

  return (
    <div className="reservation-admin-page">
      <h2>Reservation Management</h2>
      <table className="reservation-admin-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>User</th>
            <th>Item</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {reservations.map((reservation) => (
            <tr key={reservation.id}>
              <td>{reservation.id}</td>
              <td>{reservation.user.username}</td>
              <td>{reservation.item.name}</td>
              <td>{new Date(reservation.startTime).toLocaleDateString()}</td>
              <td>{new Date(reservation.endTime).toLocaleDateString()}</td>
              <td>
                {editingReservation && editingReservation.id === reservation.id ? (
                  <select
                    name="reservationStatus"
                    value={formData.reservationStatus}
                    onChange={handleInputChange}
                  >
                    <option value="APPROVED">Approved</option>
                    <option value="CANCELLED">Cancelled</option>
                  </select>
                ) : (
                  reservation.reservationStatus
                )}
              </td>
              <td>
                {editingReservation && editingReservation.id === reservation.id ? (
                  <>
                    <button
                      className="item-save-button"
                      onClick={() => handleUpdate(reservation.id)}
                    >
                      Save
                    </button>
                    <button className="item-cancel-button" onClick={cancelEdit}>
                      Cancel
                    </button>
                  </>
                ) : (
                  <button
                    className="item-edit-button"
                    onClick={() => handleEdit(reservation)}
                  >
                    Edit
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <button onClick={() => navigate('/admin')} className="back-button">
        Back to Admin Panel
      </button>
    </div>
  );
};

export default ReservationAdminPage;