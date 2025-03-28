import React from 'react';
import Input from '../../common/Input';
import Button from '../../common/Button';

const Settings = () => {
  return (
    <div>
      <h2 className="text-2xl font-bold text-gray-900 mb-6">Settings</h2>
      
      <div className="bg-white shadow rounded-lg p-6">
        <form className="space-y-6">
          <Input
            label="Username"
            name="username"
            placeholder="Enter your username"
          />
          
          <Input
            label="Email"
            type="email"
            name="email"
            placeholder="Enter your email"
          />
          
          <Input
            label="Phone Number"
            type="tel"
            name="phone"
            placeholder="Enter your phone number"
          />
          
          <div className="flex justify-end">
            <Button type="submit">Save Changes</Button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Settings; 