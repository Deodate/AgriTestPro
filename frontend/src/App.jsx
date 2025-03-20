import { Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import SignupPage from './components/auth/SignupPage';
import { AuthProvider } from './contexts/AuthContext.jsx'; // Note the .jsx extension

function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/login" element={<SignupPage />} />
      </Routes>
    </AuthProvider>
  );
}

export default App;