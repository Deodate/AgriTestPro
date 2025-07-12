import React, { useState } from 'react';
import Header from "../Components/Header/Header";
import Footer from "../Components/Footer/Footer";
import Gallery from "../Components/Gallery/Gallery";
import About from "../Components/About/About";
import Features from "../Components/Features/Features";
import { useAuth } from '../contexts/AuthContext';

const HomePage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [verificationCode, setVerificationCode] = useState('');
  const { login, verify2FA, error, requires2FA, tempUserData } = useAuth();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      await login(username, password);
    } catch (err) {
      // Error is handled by the auth context
      console.error('Login failed:', err);
    }
  };

  const handleVerify2FA = async (e) => {
    e.preventDefault();
    try {
      await verify2FA(verificationCode);
    } catch (err) {
      console.error('2FA verification failed:', err);
    }
  };

  // If 2FA is required, show the verification form
  if (requires2FA) {
    return (
      <div>
        <h2>Two-Factor Authentication</h2>
        <p>Please enter the verification code sent to your phone</p>
        {error && <div className="error">{error}</div>}
        <form onSubmit={handleVerify2FA}>
          <input
            type="text"
            value={verificationCode}
            onChange={(e) => setVerificationCode(e.target.value)}
            placeholder="Enter verification code"
            required
          />
          <button type="submit">Verify</button>
        </form>
      </div>
    );
  }

  // Show the login form
  return (
    <div className="homepage">
      <Header/>
      <Gallery/>
      <About/>
      <Features/>
      <Footer/>
    </div>
  );
};

export default HomePage;