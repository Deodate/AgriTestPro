import { Outlet, Navigate } from "react-router-dom";

const ProtectedRoutes = () => {
  // Check for user in localStorage
  const userJSON = localStorage.getItem('user');
  const user = userJSON ? JSON.parse(userJSON) : null;
  
  // If user exists and has a token, allow access to the protected routes
  // Otherwise, redirect to the signup page
  return user && user.token ? <Outlet /> : <Navigate to="/signup" />
}

export default ProtectedRoutes;