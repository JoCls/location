// src/pages/ItemAdminPage.js

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../styles.css';
import { useNavigate } from 'react-router-dom';

const ItemAdminPage = () => {
  const [items, setItems] = useState([]);
  const [editingItem, setEditingItem] = useState(null);
  const [creatingNewItem, setCreatingNewItem] = useState(false);
  const [showConfirmation, setShowConfirmation] = useState(false);
  const [actionType, setActionType] = useState(''); // 'delete' or 'update'
  const [currentItemId, setCurrentItemId] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    itemType: '', // 'EQUIPMENT' or 'CLASSROOM'
    description: '',
  });

  const navigate = useNavigate();

  useEffect(() => {
    fetchItems();
  }, []);

  const fetchItems = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/items/all');
      setItems(response.data);
    } catch (error) {
      console.error('Error fetching items:', error);
    }
  };

  const handleEdit = (item) => {
    setEditingItem(item);
    setFormData({
      name: item.name,
      itemType: item.itemType,
      description: item.description,
    });
  };

  const handleDelete = (itemId) => {
    setActionType('delete');
    setCurrentItemId(itemId);
    setShowConfirmation(true);
  };

  const handleUpdate = (itemId) => {
    setActionType('update');
    setCurrentItemId(itemId);
    setShowConfirmation(true);
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value,
    }));
  };

  const confirmAction = async () => {
    if (actionType === 'delete') {
      try {
        await axios.delete(`http://localhost:8080/api/items/${currentItemId}`);
        setItems((prevItems) => prevItems.filter((item) => item.id !== currentItemId));
        setShowConfirmation(false);
      } catch (error) {
        console.error('Error deleting item:', error);
      }
    } else if (actionType === 'update' && editingItem) {
      try {
        const updatedItem = {
          ...editingItem,
          ...formData,
        };
        await axios.put(`http://localhost:8080/api/items/${currentItemId}`, updatedItem);
        setItems((prevItems) => prevItems.map((item) => (item.id === currentItemId ? updatedItem : item)));
        setEditingItem(null);
        setShowConfirmation(false);
      } catch (error) {
        console.error('Error updating item:', error);
      }
    }
  };

  const cancelAction = () => {
    setShowConfirmation(false);
    setEditingItem(null);
    setCreatingNewItem(false);
    setFormData({
      name: '',
      itemType: '',
      description: '',
    });
  };

  const handleCreateNewItem = () => {
    setCreatingNewItem(true);
    setFormData({
      name: '',
      itemType: '',
      description: '',
    });
  };

  const confirmNewItem = async () => {
    try {
      const response = await axios.post(
        'http://localhost:8080/api/items',
        formData
      );
      const newItem = response.data;

      setItems((prevItems) => [...prevItems, newItem]);
      setCreatingNewItem(false);
      setFormData({
        name: '',
        itemType: '',
        description: '',
      });
    } catch (error) {
      console.error('Error creating new item:', error);
    }
  };

  return (
    <div className="item-admin-page">
      <h2>Item Management</h2>
      <table className="item-admin-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Type</th>
            <th>Description</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {items.map((item) => (
            <tr key={item.id}>
              <td>{item.id}</td>
              <td>
                {editingItem && editingItem.id === item.id ? (
                  <input
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleInputChange}
                  />
                ) : (
                  item.name
                )}
              </td>
              <td>
                {editingItem && editingItem.id === item.id ? (
                  <select name="type" value={formData.itemType} onChange={handleInputChange}>
                    <option value="EQUIPMENT">Equipment</option>
                    <option value="CLASSROOM">Classroom</option>
                  </select>
                ) : (
                  item.itemType
                )}
              </td>
              <td>
                {editingItem && editingItem.id === item.id ? (
                  <textarea
                    name="description"
                    value={formData.description}
                    onChange={handleInputChange}
                  />
                ) : (
                  item.description
                )}
              </td>
              <td>
                {editingItem && editingItem.id === item.id ? (
                  <>
                    <button class="item-save-button" onClick={() => handleUpdate(item.id)}>Save</button>
                    <button class="item-cancel-button" onClick={cancelAction}>Cancel</button>
                  </>
                ) : (
                  <>
                    <button class="item-edit-button" onClick={() => handleEdit(item)}>Edit</button>
                    <button class="item-delete-button" onClick={() => handleDelete(item.id)}>Delete</button>
                  </>
                )}
              </td>
            </tr>
          ))}

          {/* New Item Row */}
          {creatingNewItem && (
            <tr>
              <td>New</td>
              <td>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleInputChange}
                  placeholder="Item Name"
                />
              </td>
              <td>
                <select
                  name="itemType"
                  value={formData.itemType}
                  onChange={handleInputChange}
                >
                  <option value="">Select Type</option>
                  <option value="EQUIPMENT">Equipment</option>
                  <option value="CLASSROOM">Classroom</option>
                </select>
              </td>
              <td>
                <textarea
                  name="description"
                  value={formData.description}
                  onChange={handleInputChange}
                  placeholder="Description"
                />
              </td>
              <td>
                <button
                  className="item-confirm-button"
                  onClick={confirmNewItem}
                >
                  Confirm
                </button>
                <button
                  className="item-cancel-button"
                  onClick={cancelAction}
                >
                  Cancel
                </button>
              </td>
            </tr>
          )}
        </tbody>
      </table>
      <button onClick={handleCreateNewItem} className="create-button">
        Create New Item
      </button>

      {/* Confirmation Dialog */}
      {showConfirmation && (
        <div className="confirmation-dialog">
          <div className="confirmation-content">
            <h3>{actionType === 'delete' ? 'Delete Confirmation' : 'Update Confirmation'}</h3>
            <p>
              Are you sure you want to {actionType} this item?
            </p>
            <button onClick={confirmAction}>Yes</button>
            <button onClick={cancelAction}>No</button>
          </div>
        </div>
      )}

      <button onClick={() => navigate('/admin')} className="back-button">
        Back to Admin Panel
      </button>
    </div>
  );
};

export default ItemAdminPage;