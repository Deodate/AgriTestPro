import React, { useState, useEffect } from 'react';
import { FaChevronLeft, FaChevronRight, FaSearch } from 'react-icons/fa';
import './AllDataTable.css';
import axios from 'axios'; // Import axios for API calls
import { API_CONFIG, AUTH_SETTINGS } from '../../config';

const AllDataTable = ({ apiEndpoint }) => {
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 3;
  const [data, setData] = useState([]); // State to store fetched data
  const [isLoading, setIsLoading] = useState(true); // Loading state
  const [error, setError] = useState(null); // Error state
  const [searchTerm, setSearchTerm] = useState(''); // New state for search term
  const [originalData, setOriginalData] = useState([]); // Store original fetched data
  const [filteredData, setFilteredData] = useState([]); // State to store filtered data

  useEffect(() => {
    const fetchAllData = async () => {
      setIsLoading(true);
      setError(null);
      const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
      if (!token) {
        setError('Authentication required to fetch data.');
        setIsLoading(false);
        return;
      }

      try {
        const axiosInstance = axios.create({
          baseURL: API_CONFIG.BASE_URL || 'http://localhost:8888',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });
        // Use the passed apiEndpoint, or fallback to a default if not provided
        const response = await axiosInstance.get(apiEndpoint || '/api/all-data'); 
        setData(response.data);
        setOriginalData(response.data);
      } catch (err) {
        setError('Failed to fetch data. Please try again later.');
        console.error('Error fetching all data:', err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchAllData();
  }, [apiEndpoint]);

  // Effect to filter data based on search term
  useEffect(() => {
    console.log('Search term changed:', searchTerm); // Log search term
    const lowerCaseSearchTerm = searchTerm.toLowerCase();
    const currentFilteredData = originalData.filter(item => {
      const testNameMatch = item.testName && item.testName.toLowerCase().includes(lowerCaseSearchTerm);
      const productTypeMatch = item.productType && item.productType.toLowerCase().includes(lowerCaseSearchTerm);
      const statusMatch = item.status && item.status.toLowerCase().includes(lowerCaseSearchTerm);
      const assignedWorkerMatch = item.assignedWorker && 
                                  ((item.assignedWorker.fullName && item.assignedWorker.fullName.toLowerCase().includes(lowerCaseSearchTerm)) || 
                                   (item.assignedWorker.username && item.assignedWorker.username.toLowerCase().includes(lowerCaseSearchTerm)));
      
      return testNameMatch || productTypeMatch || statusMatch || assignedWorkerMatch;
    });
    setFilteredData(currentFilteredData);
    console.log('Filtered data length:', currentFilteredData.length); // Log filtered data length
    setCurrentPage(1); // Reset to first page on search
  }, [searchTerm, originalData]);

  const totalPages = Math.ceil(filteredData.length / itemsPerPage); // Use filteredData for pagination
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = filteredData.slice(indexOfFirstItem, indexOfLastItem); // Use filteredData for slicing

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  if (isLoading) {
    return <div className="all-data-table-container">Loading data...</div>;
  }

  if (error) {
    return <div className="all-data-table-container error-message">{error}</div>;
  }

  if (filteredData.length === 0 && searchTerm !== '') {
    return <div className="all-data-table-container">No matching data found.</div>;
  }

  if (originalData.length === 0 && searchTerm === '') {
    return <div className="all-data-table-container">No data available.</div>;
  }

  return (
    <div className="all-data-table-container">
      <div className="search-bar-container">
        <div className="search-input-wrapper">
          <input
            type="text"
            placeholder="Search by Test Name, Product Type, Status, Assigned..."
            className="search-input"
            value={searchTerm}
            onChange={(e) => {
              console.log('Input value:', e.target.value); // Log input value
              setSearchTerm(e.target.value);
            }}
          />
          <FaSearch className="search-icon" />
        </div>
      </div>
      <table className="all-data-table">
        <thead>
          <tr>
            {/* Dynamically render table headers based on the first item's keys, excluding specified columns */}
            {filteredData.length > 0 && Object.keys(filteredData[0])
              .filter(key => ![
                'id',
                'testDescription',
                'notes',
                'createdBy',
                'createdAt',
                'phases',
                'updatedAt',
                'productBatchNumber'
              ].includes(key))
              .map((key) => (
                <th key={key}>{
                  key === 'assignedWorker' ? 'Assigned' : 
                  key.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase())
                }</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {currentItems.map((item, index) => (
            <tr key={item.id || index}>
              {/* Dynamically render table cells, excluding specified columns */}
              {Object.keys(item)
                .filter(key => ![
                  'id',
                  'testDescription',
                  'notes',
                  'createdBy',
                  'createdAt',
                  'phases',
                  'updatedAt',
                  'productBatchNumber'
                ].includes(key))
                .map((key, i) => (
                  <td key={i}>{
                    key === 'assignedWorker' ? 
                      (item[key] ? item[key].fullName || item[key].username : '') : 
                    key === 'startDate' || key === 'endDate' || key === 'createdAt' || key === 'updatedAt' ?
                      (item[key] ? new Date(item[key]).toLocaleDateString() : '') : 
                    key === 'testObjectives' ? 
                      (item[key] ? (item[key].length > 11 ? item[key].substring(0, 11) + '...' : item[key]) : '') : 
                      item[key]
                  }</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      <div className="pagination-controls">
        <button onClick={() => paginate(currentPage - 1)} disabled={currentPage === 1}>
          <FaChevronLeft />
        </button>
        {Array.from({ length: totalPages }, (_, i) => i + 1).map(pageNumber => (
          <button 
            key={pageNumber} 
            onClick={() => paginate(pageNumber)}
            className={`page-number-button ${pageNumber === currentPage ? 'active' : ''}`}
          >
            {pageNumber}
          </button>
        ))}
        <button onClick={() => paginate(currentPage + 1)} disabled={currentPage === totalPages}>
          <FaChevronRight />
        </button>
      </div>
    </div>
  );
};

export default AllDataTable; 