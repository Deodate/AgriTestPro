import { Outlet, Navigate } from "react-router-dom";
import { useEffect, useState } from "react";

const ProtectedRoutes = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Check for user in localStorage
    const checkAuth = () => {
      try {
        const userJSON = localStorage.getItem('user');
        console.log("User data from localStorage:", userJSON);
        
        if (!userJSON) {
          console.log("No user data found in localStorage");
          setIsAuthenticated(false);
          setIsLoading(false);
          return;
        }
        
        const user = JSON.parse(userJSON);
        console.log("Parsed user data:", user);
        
        // Validate the user object has required properties
        if (user && user.token) {
          console.log("User is authenticated with token:", user.token);
          setIsAuthenticated(true);
        } else {
          console.log("User data doesn't have a valid token");
          setIsAuthenticated(false);
        }
      } catch (error) {
        console.error("Error checking authentication:", error);
        setIsAuthenticated(false);
      } finally {
        setIsLoading(false);
      }
    };

    checkAuth();
  }, []);

  if (isLoading) {
    // Optional: Return a loading indicator while checking auth status
    return <div>Loading...</div>;
  }

  return isAuthenticated ? <Outlet /> : <Navigate to="/signup" />;
};

export default ProtectedRoutes;