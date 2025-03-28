import React from 'react';
import Button from '../../common/Button';

const TestCases = () => {
  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-gray-900">Test Cases</h2>
        <Button>Create New Test Case</Button>
      </div>
      
      <div className="bg-white shadow rounded-lg">
        <div className="p-6">
          <p className="text-gray-500 text-center">No test cases found. Create your first test case to get started.</p>
        </div>
      </div>
    </div>
  );
};

export default TestCases; 