import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { API_CONFIG, AUTH_SETTINGS } from '../../config';
import { toast } from 'react-toastify';
import './EvidenceListTable.css';
import { FaSearch, FaPlus, FaFilter, FaEdit, FaTrash } from 'react-icons/fa'; // Import icons
import { useNavigate } from 'react-router-dom'; // Import useNavigate

const EvidenceListTable = () => {
    const navigate = useNavigate(); // Initialize navigate
    const apiBaseUrl = API_CONFIG.BASE_URL || 'http://localhost:8089';
    const [evidenceList, setEvidenceList] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchTerm, setSearchTerm] = useState(''); // Add state for search term
    const [currentPage, setCurrentPage] = useState(1); // Add state for current page
    const [itemsPerPage] = useState(3); // Items per page - Changed from 5 to 3

    // Function to handle deletion
    const handleDelete = async (evidenceId) => {
        const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
        if (!token) {
            toast.error('Authentication token not found. Please log in.');
            return;
        }

        const api = axios.create({
            baseURL: apiBaseUrl,
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        try {
            // Assuming the backend delete endpoint is /api/evidence/{id}
            await api.delete(`/api/evidence/${evidenceId}`);
            // Remove the deleted item from the local state
            setEvidenceList(prevList => prevList.filter(item => item.id !== evidenceId));
            toast.success('Evidence deleted successfully!');
        } catch (err) {
            console.error('Error deleting evidence:', err);
            toast.error('Failed to delete evidence.');
        }
    };

    // Function to handle editing
    const handleEdit = (evidenceId) => {
        // Navigate to the Evidence Upload Form with the evidence ID
        navigate(`/dashboard?EvidenceUploadForm=edit&evidenceId=${evidenceId}`, { replace: true });
    };

    useEffect(() => {
        const fetchEvidence = async () => {
            setIsLoading(true);
            setError(null);
            
            const token = localStorage.getItem(AUTH_SETTINGS.TOKEN_KEY);
            if (!token) {
                setError('Authentication token not found. Please log in.');
                setIsLoading(false);
                toast.error('Authentication error. Please log in.');
                return;
            }

            const api = axios.create({
                baseURL: apiBaseUrl,
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            try {
                const response = await api.get('/api/evidence');
                console.log('Frontend received evidence list:', response.data);
                // Assuming the backend returns an array of evidence objects
                setEvidenceList(response.data || []);
            } catch (err) {
                console.error('Error fetching evidence list:', err);
                setError('Failed to load evidence list');
                toast.error('Failed to load evidence list.');
                setEvidenceList([]); // Set to empty array on error
            } finally {
                setIsLoading(false);
            }
        };

        fetchEvidence();
    }, [apiBaseUrl]); // Re-run if apiBaseUrl changes

    // Filter evidence list based on search term
    const filteredEvidence = evidenceList.filter(evidence => {
        if (!searchTerm) return true;
        const searchLower = searchTerm.toLowerCase();
        return (
            (evidence.testName && evidence.testName.toLowerCase().includes(searchLower)) ||
            (evidence.mediaType && evidence.mediaType.toLowerCase().includes(searchLower)) ||
            (evidence.fileName && evidence.fileName.toLowerCase().includes(searchLower)) ||
            (evidence.takenBy && evidence.takenBy.toLowerCase().includes(searchLower))
            // Add more fields to search if needed
        );
    });

    // Pagination logic
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentEvidence = filteredEvidence.slice(indexOfFirstItem, indexOfLastItem);
    const totalPages = Math.ceil(filteredEvidence.length / itemsPerPage);

    // Handle pagination
    const paginate = (pageNumber) => setCurrentPage(pageNumber);

    // Function to render pagination controls (can be extracted to a separate component)
    const renderPagination = (currentPage, totalPages, paginateFunction) => {
      if (totalPages <= 1) return null;

      return (
        <div className="table-pagination">
          <button 
            className="pagination-button" 
            onClick={() => paginateFunction(1)} 
            disabled={currentPage === 1}
          >
            &laquo;
          </button>
          <button 
            className="pagination-button" 
            onClick={() => paginateFunction(currentPage - 1)} 
            disabled={currentPage === 1}
          >
            &lt;
          </button>
          
          {Array.from({ length: Math.min(5, totalPages) }).map((_, index) => {
            let pageNumber;
            
            if (totalPages <= 5) {
              pageNumber = index + 1;
            } else {
              if (currentPage <= 3) {
                pageNumber = index + 1;
              } else if (currentPage >= totalPages - 2) {
                pageNumber = totalPages - 4 + index;
              } else {
                pageNumber = currentPage - 2 + index;
              }
            }
            
            return (
              <button
                key={pageNumber}
                className={`pagination-button ${currentPage === pageNumber ? 'active' : ''}`}
                onClick={() => paginateFunction(pageNumber)}
              >
                {pageNumber}
              </button>
            );
          })}
          
          <button 
            className="pagination-button" 
            onClick={() => paginateFunction(currentPage + 1)} 
            disabled={currentPage === totalPages}
          >
            &gt;
          </button>
          <button 
            className="pagination-button" 
            onClick={() => paginateFunction(totalPages)} 
            disabled={currentPage === totalPages}
          >
            &raquo;
          </button>
        </div>
      );
    };

    return (
        <div className="evidence-list-table-container">
            <div className="table-header-actions">
                <h3>List of Evidence</h3>
                <div className="table-actions">
                    <div className="table-search">
                        <input
                            type="text"
                            placeholder="Search evidence..."
                            className="search-input"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                        <FaSearch className="search-icon" />
                    </div>
                    <button 
                        className="table-action-btn"
                        onClick={() => navigate('/dashboard?EvidenceUploadForm=create', { replace: true })} // Navigate to upload form
                    >
                        <FaPlus /> Create New
                    </button>
                    <button className="table-action-btn hide-button">Export CSV</button>
                    <button className="table-action-btn hide-button"><FaFilter /> Filter</button>
                </div>
            </div>
            
            {isLoading && <p>Loading evidence...</p>}
            {error && <p className="error-message">{error}</p>}
            {!isLoading && !error && evidenceList.length === 0 && <p>No evidence found.</p>}

            {!isLoading && !error && evidenceList.length > 0 && (
                <div className="data-table">
                    <table>
                        <thead>
                            <tr>
                                <th>Test Name</th>
                                <th>Media Type</th>
                                <th>Taken By</th>
                                <th>Date Taken</th>
                                <th>File Name</th>
                                <th>Created At</th>
                                <th>Actions</th> {/* Added Actions column */}
                            </tr>
                        </thead>
                        <tbody>
                            {currentEvidence.map(evidence => (
                                <tr key={evidence.id}> {/* Assuming each evidence has a unique ID */}
                                    <td>{evidence.testName}</td>
                                    <td>{evidence.mediaType}</td>
                                    <td>{evidence.takenBy}</td>
                                    <td>{new Date(evidence.dateTaken).toLocaleDateString()}</td>
                                    <td>{evidence.fileName}</td>
                                    <td>{evidence.createdAt ? new Date(evidence.createdAt).toLocaleDateString() : 'N/A'}</td>
                                    <td>
                                        {/* Placeholder Action buttons */}
                                        <button className="icon-action-button view-icon" onClick={() => handleEdit(evidence.id)}><FaEdit /></button>
                                        <button className="icon-action-button delete-icon" onClick={() => handleDelete(evidence.id)}><FaTrash /></button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}

            {/* Pagination controls */}
            {!isLoading && !error && filteredEvidence.length > itemsPerPage && (
                 renderPagination(currentPage, totalPages, paginate)
            )}
        </div>
    );
};

export default EvidenceListTable; 