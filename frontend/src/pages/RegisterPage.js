import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';
import './styles.css';

const RegisterPage = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        confirmPassword: '',
        email: '',
        firstName: '',
        lastName: '',
        userRole: 'STUDENT',
        teacherCertification: ''
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        if (formData.password !== formData.confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        // Prepare data for backend
        const { confirmPassword, ...userRegistrationData } = formData;

        try {
            const response = await axios.post('http://localhost:8080/api/users/register', userRegistrationData);
            setSuccess('Registration successful! Redirecting to login...');
            setTimeout(() => navigate('/login'), 2000);
        } catch (err) {
            setError(err.response?.data || 'Registration failed. Please try again.');
        }
    };

    return (
        <div className="register-page">
            <h2>Register</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Username <span className="required">*</span></label>
                    <input type="text" name="username" value={formData.username} onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label>Password <span className="required">*</span></label>
                    <input type="password" name="password" value={formData.password} onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label>Confirm Password <span className="required">*</span></label>
                    <input type="password" name="confirmPassword" value={formData.confirmPassword} onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label>Email <span className="required">*</span></label>
                    <input type="email" name="email" value={formData.email} onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label>First Name <span className="required">*</span></label>
                    <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label>Last Name <span className="required">*</span></label>
                    <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label>User Role <span className="required">*</span></label>
                    <select name="userRole" value={formData.userRole} onChange={handleChange} required>
                        <option value="STUDENT">STUDENT</option>
                        <option value="TEACHER">TEACHER</option>
                    </select>
                </div>
                {formData.userRole === 'TEACHER' && (
                    <div className="form-group">
                        <label>Teacher Code Validation <span className="required">*</span></label>
                        <input type="text" name="teacherCertification" value={formData.teacherCertification} onChange={handleChange} required />
                    </div>
                )}
                <button type="submit">Register</button>
            </form>
            {error && <div className="error">{error}</div>}
            {success && <div className="success">{success}</div>}
            <Link to="/login" className="back-to-login">Back to Login</Link> {}
        </div>
    );
};

export default RegisterPage;