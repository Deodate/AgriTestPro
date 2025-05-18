import React, { useState } from 'react';
import './DataTable.css';
import { FaSearch, FaDownload, FaPlus, FaTrash, FaAngleDown, FaAngleUp, FaChevronDown, FaChevronRight } from 'react-icons/fa';

const DataTable = () => {
  const [selectedRows, setSelectedRows] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [sortColumn, setSortColumn] = useState(null);
  const [sortDirection, setSortDirection] = useState('asc');
  const [searchTerm, setSearchTerm] = useState('');
  const [editingCell, setEditingCell] = useState(null);
  const [editValue, setEditValue] = useState('');

  // Sample data
  const [data, setData] = useState([
    { id: 0, productName: 'Item name 0', price: 2100, coupon: 'no', inStock: 'yes', customerName: 'Customer 0', order: 0 },
    { id: 1, productName: 'Item name 1', price: 2101, coupon: 'yes', inStock: 'no', customerName: 'Customer 1', order: 1 },
    { id: 2, productName: 'Item name 2', price: 2102, coupon: 'no', inStock: 'no', customerName: 'Customer 2', order: 2 },
    { id: 3, productName: 'Item name 3', price: 2103, coupon: 'no', inStock: 'no', customerName: 'Customer 3', order: 3 },
    { id: 4, productName: 'Item name 4', price: 2104, coupon: 'no', inStock: 'no', customerName: 'Customer 4', order: 4 },
    { id: 5, productName: 'Item name 5', price: 2105, coupon: 'no', inStock: 'no', customerName: 'Customer 5', order: 5 },
    { id: 6, productName: 'Item name 6', price: 2106, coupon: 'no', inStock: 'no', customerName: 'Customer 6', order: 6 },
    { id: 7, productName: 'Item name 7', price: 2107, coupon: 'yes', inStock: 'yes', customerName: 'Customer 7', order: 7 },
    { id: 8, productName: 'Item name 8', price: 2108, coupon: 'no', inStock: 'no', customerName: 'Customer 8', order: 8 },
    { id: 9, productName: 'Item name 9', price: 2109, coupon: 'yes', inStock: 'no', customerName: 'Customer 9', order: 9 },
  ]);

  // Handle row selection
  const toggleRowSelection = (id) => {
    if (selectedRows.includes(id)) {
      setSelectedRows(selectedRows.filter(rowId => rowId !== id));
    } else {
      setSelectedRows([...selectedRows, id]);
    }
  };

  // Handle select all rows
  const toggleSelectAll = () => {
    if (selectedRows.length === data.length) {
      setSelectedRows([]);
    } else {
      setSelectedRows(data.map(row => row.id));
    }
  };

  // Handle sorting
  const handleSort = (column) => {
    if (sortColumn === column) {
      setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
    } else {
      setSortColumn(column);
      setSortDirection('asc');
    }
  };

  // Handle export to CSV
  const handleExportCSV = () => {
    const headers = ['ID', 'Product Name', 'Price', 'Customer Name', 'Order'];
    const selectedData = selectedRows.length > 0 
      ? data.filter(row => selectedRows.includes(row.id)) 
      : data;
      
    const csvContent = [
      headers.join(','),
      ...selectedData.map(row => [
        row.id,
        row.productName,
        row.price,
        row.customerName,
        row.order
      ].join(','))
    ].join('\n');
    
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.setAttribute('href', url);
    link.setAttribute('download', 'data_export.csv');
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  // Handle adding a new row
  const handleAddRow = () => {
    const newId = data.length > 0 ? Math.max(...data.map(row => row.id)) + 1 : 0;
    const newRow = {
      id: newId,
      productName: `Item name ${newId}`,
      price: 2100 + newId,
      coupon: 'no',
      inStock: 'no',
      customerName: `Customer ${newId}`,
      order: newId
    };
    setData([...data, newRow]);
  };

  // Handle deleting selected rows
  const handleDeleteRows = () => {
    if (selectedRows.length === 0) return;
    
    setData(data.filter(row => !selectedRows.includes(row.id)));
    setSelectedRows([]);
  };

  // Handle cell edit
  const handleCellEdit = (rowId, field, value) => {
    const updatedData = data.map(row => {
      if (row.id === rowId) {
        return { ...row, [field]: value };
      }
      return row;
    });
    setData(updatedData);
    setEditingCell(null);
  };

  // Filter data based on search term
  const filteredData = data.filter(row => {
    if (!searchTerm) return true;
    const searchLower = searchTerm.toLowerCase();
    return (
      row.id.toString().includes(searchLower) ||
      row.productName.toLowerCase().includes(searchLower) ||
      row.price.toString().includes(searchLower) ||
      row.coupon.toLowerCase().includes(searchLower) ||
      row.inStock.toLowerCase().includes(searchLower) ||
      row.customerName.toLowerCase().includes(searchLower) ||
      row.order.toString().includes(searchLower)
    );
  });

  // Sort data
  const sortedData = sortColumn 
    ? [...filteredData].sort((a, b) => {
        const aValue = a[sortColumn];
        const bValue = b[sortColumn];
        
        if (typeof aValue === 'string') {
          return sortDirection === 'asc' 
            ? aValue.localeCompare(bValue)
            : bValue.localeCompare(aValue);
        }
        
        return sortDirection === 'asc' 
          ? aValue - bValue
          : bValue - aValue;
      })
    : filteredData;

  // Pagination
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = sortedData.slice(indexOfFirstItem, indexOfLastItem);
  const totalPages = Math.ceil(sortedData.length / itemsPerPage);

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  // Render sort icon for table headers
  const renderSortIcon = (column) => {
    if (sortColumn !== column) return <FaChevronDown className="sort-icon faded" />;
    return sortDirection === 'asc' ? <FaAngleUp className="sort-icon" /> : <FaAngleDown className="sort-icon" />;
  };

  return (
    <div className="data-table-container">
      <div className="data-table-toolbar">
        <div className="toolbar-left">
          <button className="toolbar-button export-button" onClick={handleExportCSV}>
            <FaDownload /> Export to CSV
          </button>
          <button className="toolbar-button add-button" onClick={handleAddRow}>
            <FaPlus /> New
          </button>
          <button className="toolbar-button delete-button" onClick={handleDeleteRows} disabled={selectedRows.length === 0}>
            <FaTrash /> Delete
          </button>
        </div>
        <div className="toolbar-right">
          <div className="search-container">
            <FaSearch className="search-icon" />
            <input
              type="text"
              placeholder="Search"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
            />
            {searchTerm && (
              <button 
                className="clear-search" 
                onClick={() => setSearchTerm('')}
              >
                &times;
              </button>
            )}
          </div>
        </div>
      </div>

      <div className="data-table-wrapper">
        <table className="data-table">
          <thead>
            <tr>
              <th className="checkbox-column">
                <input
                  type="checkbox"
                  checked={selectedRows.length === data.length && data.length > 0}
                  onChange={toggleSelectAll}
                />
              </th>
              <th onClick={() => handleSort('id')}>
                ID {renderSortIcon('id')}
              </th>
              <th colSpan="1" className="product-header">
                Product
              </th>
              <th colSpan="2" className="customer-header">
                Customer
              </th>
            </tr>
            <tr>
              <th></th>
              <th></th>
              <th onClick={() => handleSort('productName')}>
                name {renderSortIcon('productName')}
              </th>
              <th onClick={() => handleSort('customerName')}>
                name {renderSortIcon('customerName')}
              </th>
              <th onClick={() => handleSort('order')}>
                order {renderSortIcon('order')}
              </th>
            </tr>
          </thead>
          <tbody>
            <tr className="filter-row">
              <td></td>
              <td></td>
              <td>
                <input type="text" placeholder="Enter name..." className="filter-input" />
              </td>
              <td></td>
              <td></td>
            </tr>
            {currentItems.map(row => (
              <tr 
                key={row.id} 
                className={selectedRows.includes(row.id) ? 'selected-row' : ''}
              >
                <td>
                  <input
                    type="checkbox"
                    checked={selectedRows.includes(row.id)}
                    onChange={() => toggleRowSelection(row.id)}
                  />
                </td>
                <td>{row.id}</td>
                <td
                  onClick={() => {
                    setEditingCell(`${row.id}-productName`);
                    setEditValue(row.productName);
                  }}
                >
                  {editingCell === `${row.id}-productName` ? (
                    <input
                      type="text"
                      value={editValue}
                      onChange={(e) => setEditValue(e.target.value)}
                      onBlur={() => handleCellEdit(row.id, 'productName', editValue)}
                      onKeyPress={(e) => {
                        if (e.key === 'Enter') {
                          handleCellEdit(row.id, 'productName', editValue);
                        }
                      }}
                      autoFocus
                    />
                  ) : (
                    row.productName
                  )}
                </td>
                <td>{row.customerName}</td>
                <td>{row.order}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="data-table-footer">
        <div className="items-per-page">
          <span>Rows per page:</span>
          <select 
            value={itemsPerPage} 
            onChange={(e) => setItemsPerPage(Number(e.target.value))}
            className="page-size-select"
          >
            <option value={5}>5</option>
            <option value={10}>10</option>
            <option value={20}>20</option>
            <option value={50}>50</option>
          </select>
        </div>
        
        <div className="pagination-info">
          Showing {indexOfFirstItem + 1}-{Math.min(indexOfLastItem, sortedData.length)} of {sortedData.length} items
        </div>
        
        <div className="pagination">
          <button
            className="pagination-button"
            onClick={() => paginate(1)}
            disabled={currentPage === 1}
            title="First page"
          >
            &laquo;
          </button>
          <button
            className="pagination-button"
            onClick={() => paginate(currentPage - 1)}
            disabled={currentPage === 1}
            title="Previous page"
          >
            &lt;
          </button>
          
          {Array.from({ length: Math.min(5, totalPages) }).map((_, index) => {
            let pageNumber;
            
            if (totalPages <= 5) {
              pageNumber = index + 1;
            } else {
              // Complex pagination logic to show correct page numbers
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
                onClick={() => paginate(pageNumber)}
              >
                {pageNumber}
              </button>
            );
          })}
          
          <button
            className="pagination-button"
            onClick={() => paginate(currentPage + 1)}
            disabled={currentPage === totalPages}
            title="Next page"
          >
            &gt;
          </button>
          <button
            className="pagination-button"
            onClick={() => paginate(totalPages)}
            disabled={currentPage === totalPages}
            title="Last page"
          >
            &raquo;
          </button>
        </div>
      </div>
    </div>
  );
};

export default DataTable; 