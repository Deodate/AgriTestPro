import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import Input from '../../common/Input';
import Button from '../../common/Button';
import { useAuth } from '../../../hooks/useAuth';

const SignupPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuth();
  const isLogin = location.pathname === '/login';

  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    confirmPassword: '',
  });

  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!isLogin && formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    try {
      // For now, we'll just simulate a successful login
      login({ username: formData.username });
      navigate('/dashboard');
    } catch (err) {
      setError(err.message || 'An error occurred');
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
          {isLogin ? 'Sign in to your account' : 'Create your account'}
        </h2>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
          <form className="space-y-6" onSubmit={handleSubmit}>
            <Input
              label="Username"
              name="username"
              value={formData.username}
              onChange={handleChange}
              required
            />

            {!isLogin && (
              <Input
                label="Email"
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            )}

            <Input
              label="Password"
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
            />

            {!isLogin && (
              <Input
                label="Confirm Password"
                type="password"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                required
              />
            )}

            {error && (
              <div className="text-red-600 text-sm">{error}</div>
            )}

            <div>
              <Button type="submit" className="w-full">
                {isLogin ? 'Sign in' : 'Sign up'}
              </Button>
            </div>
          </form>

          <div className="mt-6">
            <div className="relative">
              <div className="relative flex justify-center text-sm">
                <span className="px-2 bg-white text-gray-500">
                  {isLogin ? "Don't have an account?" : 'Already have an account?'}
                </span>
              </div>
              <div className="mt-6 text-center">
                <Button
                  variant="outline"
                  onClick={() => navigate(isLogin ? '/signup' : '/login')}
                >
                  {isLogin ? 'Sign up' : 'Sign in'}
                </Button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SignupPage; 