// src/context/AuthContext.js
import React, { createContext, useState, useContext, useEffect } from 'react';
import axios from 'axios';
import { jwtDecode } from "jwt-decode";

const AuthContext = createContext();

export const useAuth = () => {
  return useContext(AuthContext);
};

export const AuthProvider = ({ children }) => {
  const [authData, setAuthData] = useState({
    isAuthenticated: false,
    userRole: null,
  });

  const [waitAuth, setWaitAuth] = useState(false);

  useEffect(() => {
    const storedAuthData = JSON.parse(localStorage.getItem('authData'));
    const token = localStorage.getItem('token');

    if (storedAuthData && token) {
      setAuthData(storedAuthData);
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    setWaitAuth(true);
  }, []);

  // Function to log in a user and set authentication data
  const login = (token) => {
    console.log("Login method in AuthContext.js");
    const decodedToken = jwtDecode(token);
    const role = decodedToken.userRole;

    setAuthData({ isAuthenticated: true, userRole: role });
    localStorage.setItem('authData', JSON.stringify({ isAuthenticated: true, userRole: role }));
    localStorage.setItem('token', token);

    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  };

  // Function to log out a user and clear authentication data
  const logout = () => {
    setAuthData({ isAuthenticated: false, userRole: null });
    localStorage.removeItem('authData');
    localStorage.removeItem('token');
    delete axios.defaults.headers.common['Authorization'];
  };

  return (
    <AuthContext.Provider value={{ ...authData, login, logout }}>
      {waitAuth && children}
    </AuthContext.Provider>
  );
};