// src/services/AuthService.js

class AuthService {
    isAuthenticated() {
      try {
        const userData = localStorage.getItem('user');
        if (!userData) return false;
        
        const user = JSON.parse(userData);
        return !!user.token; // Return true if token exists
      } catch (error) {
        console.error('Error checking authentication:', error);
        return false;
      }
    }
  
    getToken() {
      try {
        const userData = localStorage.getItem('user');
        if (!userData) return null;
        
        const user = JSON.parse(userData);
        return user.token;
      } catch (error) {
        console.error('Error getting token:', error);
        return null;
      }
    }
  
    getUserData() {
      try {
        const userData = localStorage.getItem('user');
        if (!userData) return null;
        
        return JSON.parse(userData);
      } catch (error) {
        console.error('Error getting user data:', error);
        return null;
      }
    }
  
    logout() {
      return new Promise(async (resolve, reject) => {
        try {
          const token = this.getToken();
          
          // Call the logout API if token exists
          if (token) {
            try {
              await fetch('http://localhost:8088/api/auth/signout', {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json',
                  'Authorization': `Bearer ${token}`
                }
              });
            } catch (apiError) {
              console.warn('Logout API call failed:', apiError);
              // Continue with local logout even if API call fails
            }
          }
          
          // Clear local storage
          localStorage.removeItem('user');
          localStorage.removeItem('token');
          resolve(true);
        } catch (error) {
          console.error('Logout error:', error);
          reject(error);
        }
      });
    }
  
    async validateToken() {
      try {
        const token = this.getToken();
        if (!token) return false;
        
        const response = await fetch('http://localhost:8088/api/auth/validate-token', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        
        const data = await response.json();
        return response.ok && data.valid === true;
      } catch (error) {
        console.error('Token validation error:', error);
        return false;
      }
    }
  }
  
  export default new AuthService();