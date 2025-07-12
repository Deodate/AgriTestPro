import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FaPlus, FaSearch, FaFilter, FaCheck, FaTimesCircle, FaExclamationTriangle } from 'react-icons/fa';
import './ComplianceChecklist.css';
import Header from '../../Components/Header/Header';
import Footer from '../../Components/Footer/Footer';

/**
 * Component for displaying a list of compliance checklists
 * @returns {JSX.Element} Rendered component
 */
const ComplianceChecklistList = () => {
  const navigate = useNavigate();
  const [checklists, setChecklists] = useState([]);
  const [filteredChecklists, setFilteredChecklists] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('all');
  
  // Pagination state
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10); // Reverted to default 10 items per page
  const [totalPages, setTotalPages] = useState(0);
  
  useEffect(() => {
    // Original mock data for ComplianceChecklistList.js (if any, otherwise empty array)
    // This ensures this component is clean if it's not the primary display for the table
    const fetchChecklists = async () => {
      setLoading(true);
      try {
        const mockData = []; // Start with empty data as the table will be in ComplianceChecklistTable.js
        setChecklists(mockData);
        setFilteredChecklists(mockData);
        setTotalPages(Math.ceil(mockData.length / itemsPerPage));
        setLoading(false);
      } catch (err) {
        setError("Failed to fetch compliance checklists. Please try again later.");
        setLoading(false);
      }
    };
    
    fetchChecklists();
  }, [itemsPerPage]);
  
  useEffect(() => {
    // Filter checklists based on search term and status filter
    let filtered = checklists;
    
    // Apply search filter
    if (searchTerm) {
      filtered = filtered.filter(checklist => 
        checklist.productName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        checklist.reviewerName.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
    
    // Apply status filter
    if (statusFilter !== 'all') {
      filtered = filtered.filter(checklist => checklist.overallStatus === statusFilter);
    }
    
    setFilteredChecklists(filtered);
    setTotalPages(Math.ceil(filtered.length / itemsPerPage));
    setCurrentPage(1); // Reset to first page when filters change
  }, [searchTerm, statusFilter, checklists, itemsPerPage]);
  
  // Get current items for pagination
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = filteredChecklists.slice(indexOfFirstItem, indexOfLastItem);
  
  // Change page
  const paginate = (pageNumber) => setCurrentPage(pageNumber);
  
  // Render status badge
  const renderStatusBadge = (status) => {
    switch (status) {
      case 'COMPLIANT':
        return <span className="status-badge compliant"><FaCheck /> Compliant</span>;
      case 'PARTIALLY_COMPLIANT':
        return <span className="status-badge partially-compliant"><FaExclamationTriangle /> Partially Compliant</span>;
      case 'NON_COMPLIANT':
        return <span className="status-badge non-compliant"><FaTimesCircle /> Non-Compliant</span>;
      default:
        return <span className="status-badge">{status}</span>;
    }
  };
  
  // Calculate compliance percentage
  const calculateCompliancePercentage = (checklistItems) => {
    if (!checklistItems || Object.keys(checklistItems).length === 0) return 0;
    
    const total = Object.keys(checklistItems).length;
    const passed = Object.values(checklistItems).filter(Boolean).length;
    
    return Math.round((passed / total) * 100);
  };
  
  // Render pagination
  const renderPagination = () => {
    const pageNumbers = [];
    
    for (let i = 1; i <= totalPages; i++) {
      pageNumbers.push(
        <button
          key={i}
          className={`pagination-button ${currentPage === i ? 'active' : ''}`}
          onClick={() => paginate(i)}
        >
          {i}
        </button>
      );
    }
    
    return (
      <div className="pagination">
        <button 
          className="pagination-button"
          onClick={() => paginate(Math.max(1, currentPage - 1))}
          disabled={currentPage === 1}
        >
          &laquo;
        </button>
        {pageNumbers}
        <button 
          className="pagination-button"
          onClick={() => paginate(Math.min(totalPages, currentPage + 1))}
          disabled={currentPage === totalPages}
        >
          &raquo;
        </button>
      </div>
    );
  };
  
  return (
    <div className="compliance-checklist-container">
      <Header />
      
      <div className="compliance-checklist-content">
        <div className="compliance-checklist-header">
          <h1>Compliance Checklists</h1>
          <button 
            className="add-button"
            onClick={() => navigate('/compliance-checklist/create')}
          >
            <FaPlus /> Create New Checklist
          </button>
        </div>
        
        <div className="filters-container">
          <div className="search-container">
            <FaSearch className="search-icon" />
            <input
              type="text"
              placeholder="Search by product or reviewer..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
            />
          </div>
          
          <div className="status-filter">
            <FaFilter className="filter-icon" />
            <select 
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="filter-select"
            >
              <option value="all">All Statuses</option>
              <option value="COMPLIANT">Compliant</option>
              <option value="PARTIALLY_COMPLIANT">Partially Compliant</option>
              <option value="NON_COMPLIANT">Non-Compliant</option>
            </select>
          </div>
        </div>
        
        {loading ? (
          <div className="loading-container">
            <p>Loading compliance checklists...</p>
          </div>
        ) : error ? (
          <div className="error-container">
            <p>{error}</p>
          </div>
        ) : filteredChecklists.length === 0 ? (
          <div className="no-results">
            <p>No compliance checklists found. Please try a different search term or filter.</p>
          </div>
        ) : (
          <>
            <div className="compliance-checklist-table">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Product</th>
                    <th>Reviewer</th>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {currentItems.map(checklist => (
                    <tr key={checklist.id}>
                      <td>{`CL-${checklist.id < 1000 ? `100${checklist.id}` : checklist.id}`}</td>
                      <td>{checklist.productName}</td>
                      <td>{checklist.reviewerName}</td>
                      <td>{new Date(checklist.reviewDate).toLocaleDateString()}</td>
                      <td>{renderStatusBadge(checklist.overallStatus)}</td>
                      <td>
                        <Link to={`/dashboard?ComplianceChecklist=view&id=${checklist.id}`} className="view-button">
                          View
                        </Link>
                        <button className="delete-button" onClick={() => { /* handleDelete(checklist.id) */ }}>
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            
            {renderPagination()}
          </>
        )}
      </div>
      
      <Footer />
    </div>
  );
};

export default ComplianceChecklistList; 