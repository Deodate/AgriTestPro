import React from 'react';
import DataTable from '../Components/DataTable/DataTable';
import './ProductsPage.css';

const ProductsPage = () => {
  return (
    <div className="products-page">
      <div className="page-header">
        <h1>Products Management</h1>
        <p>Manage your agricultural products inventory</p>
      </div>
      
      <div className="content-section">
        <DataTable />
      </div>
    </div>
  );
};

export default ProductsPage; 