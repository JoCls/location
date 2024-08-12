import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './styles.css';

const ReserveClassroomPage = () => {
  const [startDate, setStartDate] = useState('');
  const [startTime, setStartTime] = useState('');
  const [endDate, setEndDate] = useState('');
  const [endTime, setEndTime] = useState('');
  const [availableClassroom, setAvailableClassroom] = useState([]);
  const [selectedClassroom, setSelectedClassroom] = useState(null);
  const [reservationSuccess, setReservationSuccess] = useState(false);

  // Separate error states for each input
  const [startDateError, setStartDateError] = useState('');
  const [startTimeError, setStartTimeError] = useState('');
  const [endDateError, setEndDateError] = useState('');
  const [endTimeError, setEndTimeError] = useState('');

  // Debounce timeout reference
  const debounceTimeoutRef = React.useRef(null);

  const handleStartDateChange = (e) => setStartDate(e.target.value);
  const handleStartTimeChange = (e) => setStartTime(e.target.value);
  const handleEndDateChange = (e) => setEndDate(e.target.value);
  const handleEndTimeChange = (e) => setEndTime(e.target.value);


  const navigate = useNavigate();

  const handleGoBack = () => {
    navigate('/create-reservation');
  }

  const handleGoBackToMain = () => {
    navigate('/menu');
  }

  const handleCreateAnother = () => {
    setReservationSuccess(false);
 };

  // Validate inputs and set respective error states
  const validateDates = () => {
    let isValid = true;

    const currentDateTime = new Date();
    const startDateTime = new Date(`${startDate}T${startTime}`);
    const endDateTime = new Date(`${endDate}T${endTime}`);

    // Split date and time components for comparison
    const currentDate = new Date(currentDateTime.toDateString());
    const startDateOnly = new Date(startDateTime.toDateString());
    const endDateOnly = new Date(endDateTime.toDateString());

    const currentTime = currentDateTime.getHours() * 60 + currentDateTime.getMinutes();
    const startTimeOnly = startDateTime.getHours() * 60 + startDateTime.getMinutes();
    const endTimeOnly = endDateTime.getHours() * 60 + endDateTime.getMinutes();

    // Validate start date
    if (startDateOnly < currentDate) {
      setStartDateError('Start date cannot be in the past.');
      isValid = false;
    } else {
      setStartDateError('');
    }

    // Validate start time only if the start date is today
    if (startDateOnly.getTime() === currentDate.getTime() && startTimeOnly < currentTime) {
      setStartTimeError('Start time cannot be in the past.');
      isValid = false;
    } else {
      setStartTimeError('');
    }

    // Check if start date and time are provided
    if (!startDate) {
      setStartDateError('Start date is required.');
      isValid = false;
    }

    if (!startTime) {
      setStartTimeError('Start time is required.');
      isValid = false;
    }

    // Validate end date
    if (endDateOnly < startDateOnly) {
      setEndDateError('End date cannot be earlier than start date.');
      isValid = false;
    } else {
      setEndDateError('');
    }

    // Validate end time only if the end date is the same as the start date
    if (endDateOnly.getTime() === startDateOnly.getTime() && endTimeOnly <= startTimeOnly) {
      setEndTimeError('End time cannot be earlier than start time.');
      isValid = false;
    } else {
      setEndTimeError('');
    }

    // Check if end date and time are provided
    if (!endDate) {
      setEndDateError('End date is required.');
      isValid = false;
    }

    if (!endTime) {
      setEndTimeError('End time is required.');
      isValid = false;
    }

    return isValid;
  };
  
  
  const fetchAvailableClassroom = async () => {
    // Check if all fields are filled
    if (!startDate || !startTime || !endDate || !endTime) {
      setAvailableClassroom([]);
      return;
    }

    // Validate date and time inputs
    if (!validateDates()) {
      setAvailableClassroom([]);
      return;
    }

    try {
      setSelectedClassroom(null);
      const response = await axios.get('http://localhost:8080/api/items/available', {
        params: {
          startTime: `${startDate}T${startTime}`,
          endTime: `${endDate}T${endTime}`,
          type: 'CLASSROOM', 
        },
      });

      setAvailableClassroom(response.data);
    } catch (err) {
      console.error('Failed to fetch available classroom:', err);
      setAvailableClassroom([]);
    }
  };

  // useEffect to trigger the API call whenever inputs change
  useEffect(() => {
    if (debounceTimeoutRef.current) {
      clearTimeout(debounceTimeoutRef.current);
    }

    debounceTimeoutRef.current = setTimeout(() => {
      if (startDate && startTime && endDate && endTime && validateDates()) {
        fetchAvailableClassroom();
      } else {
        setAvailableClassroom([]);
      }
    }, 500);

    return () => {
      if (debounceTimeoutRef.current) {
        clearTimeout(debounceTimeoutRef.current);
      }
    };
  }, [startDate, startTime, endDate, endTime]);

  const handleClassroomSelect = (classroomId) => {
    setSelectedClassroom(classroomId);
  };

  const createReservation = async () => {
    if (!selectedClassroom) {
      alert('Please select an classroom to reserve.');
      return;
    }

    if (!validateDates()) {
      return;
    }

    try {
      await axios.post(`http://localhost:8080/api/reservations/create`, {
        itemId: selectedClassroom,
        startTime: `${startDate}T${startTime}`,
        endTime: `${endDate}T${endTime}`,
      });

      setReservationSuccess(true);

      //alert('Reservation created successfully!');
      setStartDate('');
      setStartTime('');
      setEndDate('');
      setEndTime('');
      setAvailableClassroom([]);
      setSelectedClassroom(null);
    } catch (err) {
      console.error('Failed to create reservation:', err);
      alert('Failed to create reservation.');
    }
  };

  return (
    <div className="create-classroom-page">
      <h2>Reserve Classroom</h2>

      {reservationSuccess ? (
        <div className="succes-message">
          <p>Reservation created successfully !</p>
          <button onClick={handleCreateAnother} className="create-another-button">
            Create Another Reservation
          </button>
          <button onClick={handleGoBackToMain} className="back-button">
            Back to Menu
          </button>
          </div>
      ) : (      
      <>
      <div className="date-time-picker">
        <div className="form-group">
          <label>Start Date:</label>
          <input
            type="date"
            value={startDate}
            onChange={handleStartDateChange}
            className={startDateError ? 'input-error' : ''}
            required
          />
          {startDateError && <span className="error">{startDateError}</span>}
        </div>

        <div className="form-group">
          <label>Start Time:</label>
          <input
            type="time"
            value={startTime}
            onChange={handleStartTimeChange}
            className={startTimeError ? 'input-error' : ''}
            required
          />
          {startTimeError && <span className="error">{startTimeError}</span>}
        </div>

        <div className="form-group">
          <label>End Date:</label>
          <input
            type="date"
            value={endDate}
            onChange={handleEndDateChange}
            className={endDateError ? 'input-error' : ''}
            required
          />
          {endDateError && <span className="error">{endDateError}</span>}
        </div>

        <div className="form-group">
          <label>End Time:</label>
          <input
            type="time"
            value={endTime}
            onChange={handleEndTimeChange}
            className={endTimeError ? 'input-error' : ''}
            required
          />
          {endTimeError && <span className="error">{endTimeError}</span>}
        </div>
      </div>

      {availableClassroom.length > 0 && (
        <div className="classroom-list">
          <h3>Available Classroom</h3>
          {availableClassroom.map((classroom) => (
            <div key={classroom.id} className="classroom-item">
              <input
                type="radio"
                name="classroom"
                value={classroom.id}
                checked={selectedClassroom === classroom.id}
                onChange={() => handleClassroomSelect(classroom.id)}
              />
              <label>{classroom.name}</label>
            </div>
          ))}
          <button
          type="button"
          className="create-reservation-primary-button"
          onClick={createReservation}
          disabled={!selectedClassroom}
          >Create Reservation</button>
        </div>
      )}

      <button type="button" onClick={handleGoBack} className="back-button">
        Back to Previous Menu
      </button>
      </>
    )}  
    </div>
  );
};

export default ReserveClassroomPage;