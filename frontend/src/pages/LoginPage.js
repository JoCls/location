import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './styles.css';

function LoginPage() {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        username,
        password
      });

      // Handle successful login, e.g., redirect to dashboard
      navigate('/dashboard');

    } catch (error) {
      setError('Login failed. Please try again.');
    }
  };

  const handleCreateAccount = () => {
    navigate('/register');
  };

  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Username:</label>
          <input 
            type="text" 
            value={username} 
            onChange={(e) => setUsername(e.target.value)} 
            required 
          />
        </div>
        <div>
          <label>Password:</label>
          <input 
            type="password" 
            value={password} 
            onChange={(e) => setPassword(e.target.value)} 
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