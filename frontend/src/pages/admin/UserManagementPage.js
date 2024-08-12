// src/pages/admin/UserManagementPage.js

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const UserManagementPage = () => {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [actionType, setActionType] = useState(null);
  const [newRole, setNewRole] = useState('');
  const [originalRole, setOriginalRole] = useState('');
  const navigate = useNavigate();

  const handleGoBack = () => {
    navigate('/admin'); // Adjust the path based on your routing setup
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/users/all');
      setUsers(response.data);
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };

  const handleRoleChangeClick = (user, role) => {
    setSelectedUser(user);
    setOriginalRole(user.userRole);
    setNewRole(role);
    setActionType('update');
  };

  const handleDeleteClick = (user) => {
    setSelectedUser(user);
    setActionType('delete');
  };

  const handleConfirmAction = async () => {
    if (!selectedUser) return;

    if (actionType === 'update') {
      try {
        await axios.put(`http://localhost:8080/api/users/${selectedUser.id}/role`, { userRole: newRole });
        setUsers((prevUsers) =>
          prevUsers.map((user) =>
            user.id === selectedUser.id ? { ...user, role: newRole } : user
          )
        );
        alert('Role updated successfully!');
        fetchUsers();
      } catch (error) {
        console.error('Error updating role:', error);
        alert('Failed to update role.');
      }
    } else if (actionType === 'delete') {
      try {
        await axios.delete(`http://localhost:8080/api/users/${selectedUser.id}`);
        setUsers((prevUsers) => prevUsers.filter((user) => user.id !== selectedUser.id));
        alert('User deleted successfully!');
      } catch (error) {
        console.error('Error deleting user:', error);
        alert('Failed to delete user.');
      }
    }

    setActionType(null);
    setSelectedUser(null);
    setOriginalRole('');
    setNewRole('');
  };

  const handleCancelAction = () => {
    if (selectedUser) {
      setUsers((prevUsers) =>
        prevUsers.map((user) =>
          user.id === selectedUser.id ? { ...user, userRole: originalRole } : user
        )
      );
    }
    setActionType(null);
    setSelectedUser(null);
    setOriginalRole('');
    setNewRole('');
  };

  return (
    <div className="user-management-page">
      <h2>User Management</h2>
      <table className="user-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Role</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.username}</td>
              <td>
                <select
                  value={user.id === selectedUser?.id ? newRole : user.userRole}
                  onChange={(e) => handleRoleChangeClick(user, e.target.value)}
                >
                  <option value="STUDENT">Student</option>
                  <option value="TEACHER">Teacher</option>
                  <option value="ADMIN">Admin</option>
                </select>
              </td>
              <td>
                <button onClick={() => handleDeleteClick(user)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {actionType && selectedUser && (
        <div className="modal-overlay">
          <div className="modal-content">
            <p>
              {actionType === 'update'
                ? `Are you sure you want to change ${selectedUser.username}'s role to ${newRole}?`
                : `Are you sure you want to delete ${selectedUser.username}?`}
            </p>
            <button onClick={handleConfirmAction}>Confirm</button>
            <button onClick={handleCancelAction}>Cancel</button>
          </div>
        </div>
      )}
      <button onClick={handleGoBack} className="back-button">
        Back to Admin Panel
      </button>
    </div>
  );
};

export default UserManagementPage;