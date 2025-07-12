import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaPlus, FaSearch, FaFilter, FaCheck, FaTimesCircle, FaExclamationTriangle } from 'react-icons/fa';
import './ComplianceChecklist.css'; // Assuming you'll have a CSS file for this component

const ComplianceChecklist = () => {
  const [checklists, setChecklists] = useState([]);
  const [filteredChecklists, setFilteredChecklists] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('all');

  // Pagination state
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(3); // Display 3 rows per page
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    // Simulate fetching data from API
    const fetchChecklists = async () => {
      setLoading(true);
      try {
        // Mock data for now - replace with actual API call later
        const mockData = [
          {
            id: 1,
            productId: 101,
            productName: "Fungicide X-500",
            checklistItems: {
              "Toxicity Test": true,
              "Environmental Impact": true,
              "Application Safety": true,
              "Storage Requirements": true,
              "Disposal Guidelines": true
            },
            reviewerName: "John Mwarimu",
            reviewDate: "2023-06-05",
            comments: "All requirements met. Product is safe for use when guidelines are followed.",
            overallStatus: "COMPLIANT",
            createdAt: "2023-06-05T10:30:00",
            updatedAt: "2023-06-05T10:30:00"
          },
          {
            id: 2,
            productId: 102,
            productName: "Organic Fertilizer B-200",
            checklistItems: {
              "Toxicity Test": true,
              "Environmental Impact": true,
              "Application Safety": true,
              "Storage Requirements": false,
              "Disposal Guidelines": true
            },
            reviewerName: "Emily Johnson",
            reviewDate: "2023-06-10",
            comments: "Storage requirements not met. Need proper containment solutions.",
            overallStatus: "PARTIALLY_COMPLIANT",
            createdAt: "2023-06-10T14:20:00",
            updatedAt: "2023-06-10T14:20:00"
          },
          {
            id: 3,
            productId: 103,
            productName: "Insecticide Z-100",
            checklistItems: {
              "Toxicity Test": false,
              "Environmental Impact": false,
              "Application Safety": true,
              "Storage Requirements": true,
              "Disposal Guidelines": false
            },
            reviewerName: "Michael Brown",
            reviewDate: "2023-06-12",
            comments: "Multiple compliance issues. Product needs reformulation.",
            overallStatus: "NON_COMPLIANT",
            createdAt: "2023-06-12T09:15:00",
            updatedAt: "2023-06-12T09:15:00"
          },
          {
            id: 4,
            productId: 104,
            productName: "Herbicide Y-300",
            checklistItems: {
              "Toxicity Test": true,
              "Environmental Impact": true,
              "Application Safety": true,
              "Storage Requirements": true,
              "Disposal Guidelines": true
            },
            reviewerName: "Sarah Davis",
            reviewDate: "2023-06-15",
            comments: "Fully compliant product.",
            overallStatus: "COMPLIANT",
            createdAt: "2023-06-15T11:00:00",
            updatedAt: "2023-06-15T11:00:00"
          },
          {
            id: 5,
            productId: 105,
            productName: "Seed Treatment S-10",
            checklistItems: {
              "Toxicity Test": true,
              "Environmental Impact": false,
              "Application Safety": true,
              "Storage Requirements": true,
              "Disposal Guidelines": true
            },
            reviewerName: "David Lee",
            reviewDate: "2023-06-18",
            comments: "Environmental impact assessment incomplete.",
            overallStatus: "PARTIALLY_COMPLIANT",
            createdAt: "2023-06-18T13:45:00",
            updatedAt: "2023-06-18T13:45:00"
          },
        ];

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
      <div className="compliance-checklist-content">
        <div className="compliance-checklist-header">
          <h1>Compliance lChecklistu</h1>
          <Link to="/dashboard?ComplianceChecklist=create" className="add-new-button">
            <FaPlus /> Add New Checklist
          </Link>
        </div>

        <div className="search-filter-controls">
          <div className="search-bar">
            <input
              type="text"
              placeholder="Search by product or reviewer name..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <FaSearch className="search-icon" />
          </div>

          <div className="filter-dropdown">
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
            >
              <option value="all">All Statuses</option>
              <option value="COMPLIANT">Compliant</option>
              <option value="PARTIALLY_COMPLIANT">Partially Compliant</option>
              <option value="NON_COMPLIANT">Non-Compliant</option>
            </select>
            <FaFilter className="filter-icon" />
          </div>
        </div>

        {loading && <p>Loading compliance checklists...</p>}
        {error && <p className="error-message">{error}</p>}

        {!loading && !error && currentItems.length === 0 && (
          <div className="no-checklists">
            <p>No compliance checklists found.</p>
            <Link to="/dashboard?ComplianceChecklist=create" className="add-first-button">
              <FaPlus /> Create Your First Checklist
            </Link>
          </div>
        )}

        {!loading && !error && currentItems.length > 0 && (
          <div className="compliance-checklist-table">
            <table>
              <thead>
                <tr>
                  <th>Product Name</th>
                  <th>Reviewer Name</th>
                  <th>Review Date</th>
                </tr>
              </thead>
              <tbody>
                {currentItems.map(checklist => (
                  <tr key={checklist.id}>
                    <td>{checklist.productName}</td>
                    <td>{checklist.reviewerName}</td>
                    <td>{new Date(checklist.reviewDate).toLocaleDateString()}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {!loading && !error && totalPages > 1 && renderPagination()}
      </div>
    </div>
  );
};

export default ComplianceChecklist; 