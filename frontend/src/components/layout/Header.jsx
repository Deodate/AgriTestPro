import React from 'react';
import Button from '../common/Button';
import { useAuth } from '../../hooks/useAuth';

const Header = () => {
  const { logout } = useAuth();

  return (
    <header className="bg-white shadow">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <div className="flex justify-between items-center">
          <h1 className="text-2xl font-semibold text-gray-900">Dashboard</h1>
          <Button
            variant="danger"
            size="small"
            onClick={logout}
          >
            Logout
          </Button>
        </div>
      </div>
    </header>
  );
};

export default Header; 