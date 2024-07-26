import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './styles.css';
import { useAuth } from '../context/AuthContext';

function LoginPage() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const { login } = useAuth();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();


  const { username, password } = formData;
    
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        username,
        password
      });

      const role = response.data.userRole;
      console.log('User Role after login:', role);
      login(role);  

      // Handle successful login, e.g., redirect to dashboard
      navigate('/menu');

    } catch (error) {
      setError('Login failed. Please try again.');
    }
  };

  const handleCreateAccount = () => {
    navigate('/register');
  };

  return (
    <div className="login-page">
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Username: <span className="required">*</span></label>
          <input 
            type="username" 
            name="username"
            value={formData.email} 
            onChange={handleChange} 
            required 
          />
        </div>
        <div className="form-group">
          <label>Password: <span className="required">*</span></label>
          <input 
            type="password" 
            name="password"
            value={formData.password} 
            onChange={handleChange} 
            required 
          />
        </div>
        {error && <div className="error">{error}</div>}
        <button type="submit">Log in</button>
        <button type="button" className="create-account-button" onClick={handleCreateAccount}>Create Account</button>
      </form>
    </div>
  );
}

export default LoginPage;