import React, { useState, useEffect } from "react";
import axios from "axios";
// import { toast } from "react-toastify";

const TestCaseCreationForm = () => {
  const [formData, setFormData] = useState({
    testName: "",
    testDescription: "",
    testObjectives: "",
    productType: "",
    productBatchNumber: "",
    testingLocation: "",
    assignedWorkerId: "",
    startDate: "",
    endDate: "",
    notes: "",
  });

  const [workers, setWorkers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Fetch workers for the dropdown
    const fetchWorkers = async () => {
      try {
        const response = await axios.get("/api/users?role=TESTER");
        setWorkers(response.data);
      } catch (err) {
        console.error("Error fetching workers:", err);
      }
    };

    fetchWorkers();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      // Convert assignedWorkerId to number
      const dataToSubmit = {
        ...formData,
        assignedWorkerId: formData.assignedWorkerId 
          ? parseInt(formData.assignedWorkerId, 10) 
          : null,
      };

      const response = await axios.post("/api/testcases", dataToSubmit);
      toast.success("Test case created successfully!");
      
      // Reset form
      setFormData({
        testName: "",
        testDescription: "",
        testObjectives: "",
        productType: "",
        productBatchNumber: "",
        testingLocation: "",
        assignedWorkerId: "",
        startDate: "",
        endDate: "",
        notes: "",
      });
      
      // You could redirect or do something else here
      
    } catch (err) {
      setError(
        err.response?.data?.message || 
        "An error occurred while creating the test case."
      );
      toast.error("Failed to create test case");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 bg-white rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-6">Create New Test Case</h2>
      
      {error && (
        <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
          {error}
        </div>
      )}
      
      <form onSubmit={handleSubmit}>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Test Name */}
          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Test Name
            </label>
            <input
              type="text"
              name="testName"
              value={formData.testName}
              onChange={handleChange}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          
          {/* Product Type */}
          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Product Type
            </label>
            <input
              type="text"
              name="productType"
              value={formData.productType}
              onChange={handleChange}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          
          {/* Product Batch Number */}
          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Product Batch Number
            </label>
            <input
              type="text"
              name="productBatchNumber"
              value={formData.productBatchNumber}
              onChange={handleChange}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          
          {/* Testing Location */}
          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Testing Location
            </label>
            <input
              type="text"
              name="testingLocation"
              value={formData.testingLocation}
              onChange={handleChange}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          
          {/* Assigned Worker */}
          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Assigned Worker
            </label>
            <select
              name="assignedWorkerId"
              value={formData.assignedWorkerId}
              onChange={handleChange}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Select Worker</option>
              {workers.map((worker) => (
                <option key={worker.id} value={worker.id}>
                  {worker.fullName || worker.username}
                </option>
              ))}
            </select>
          </div>
          
          {/* Start Date */}
          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Start Date
            </label>
            <input
              type="date"
              name="startDate"
              value={formData.startDate}
              onChange={handleChange}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          
          {/* End Date */}
          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              End Date
            </label>
            <input
              type="date"
              name="endDate"
              value={formData.endDate}
              onChange={handleChange}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
        </div>
        
        {/* Test Description */}
        <div className="mt-6">
          <label className="block mb-2 text-sm font-medium text-gray-700">
            Test Description
          </label>
          <textarea
            name="testDescription"
            value={formData.testDescription}
            onChange={handleChange}
            rows="3"
            className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          ></textarea>
        </div>
        
        {/* Test Objectives */}
        <div className="mt-6">
          <label className="block mb-2 text-sm font-medium text-gray-700">
            Test Objectives
          </label>
          <textarea
            name="testObjectives"
            value={formData.testObjectives}
            onChange={handleChange}
            rows="3"
            className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          ></textarea>
        </div>
        
        {/* Notes */}
        <div className="mt-6">
          <label className="block mb-2 text-sm font-medium text-gray-700">
            Notes
          </label>
          <textarea
            name="notes"
            value={formData.notes}
            onChange={handleChange}
            rows="3"
            className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          ></textarea>
        </div>
        
        {/* Submit Button */}
        <div className="mt-8 flex justify-end">
          <button
            type="submit"
            disabled={loading}
            className={`px-6 py-2 bg-slate-700 text-white rounded-lg hover:bg-slate-600 transition-colors ${
              loading ? "opacity-70 cursor-not-allowed" : ""
            }`}
          >
            {loading ? "Creating..." : "Create Test Case"}
          </button>
        </div>
      </form>
    </div>
  );
};

export default TestCaseCreationForm;