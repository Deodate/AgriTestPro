import React from "react";
import TestCaseCreationForm from "./components/TestCaseCreationForm";

const TestCasePage = () => {
  return (
    <div className="ml-64 p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Test Case Management</h1>
      </div>
      
      <div className="mb-8">
        <TestCaseCreationForm />
      </div>
    </div>
  );
};

export default TestCasePage;