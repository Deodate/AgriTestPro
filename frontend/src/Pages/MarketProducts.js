import React, { useState, useEffect } from 'react';
import Header from '../Components/Header/Header';
import Footer from '../Components/Footer/Footer';
import './MarketProducts.css';

// Static sample product data
const staticProducts = [
  {
    id: 1,
    name: 'DELTAMAX',
    productType: 'Insecticide',
    activeIngredients: '1L',
    imageUrl: 'https://via.placeholder.com/300x200?text=Product+Image+1', // Placeholder image
    tested: 'YES',
  },
  {
    id: 2,
    name: 'SULPHURMAX',
    productType: 'Fungicide',
    activeIngredients: '1 kg',
    imageUrl: 'https://via.placeholder.com/300x200?text=Product+Image+2', // Placeholder image
    tested: 'NO',
  },
  {
    id: 3,
    name: 'HIGHTIVAMAX',
    productType: 'Fungicide',
    activeIngredients: '1 L',
    imageUrl: 'https://via.placeholder.com/300x200?text=Product+Image+3', // Placeholder image
    tested: 'YES',
  },
  {
    id: 4,
    name: 'IMAX 350 FS',
    productType: 'Seed Coating',
    activeIngredients: '25 L',
    imageUrl: 'https://via.placeholder.com/300x200?text=Product+Image+4', // Placeholder image
    tested: 'YES',
  },
];

const MarketProducts = () => {
  const [products, setProducts] = useState(staticProducts);

  return (
    <div>
      <Header />
      <main className="market-products-container">
        <h1 className="page-title">Available Products</h1>
        <div className="product-list">
          {products.length === 0 ? (
            <p>No products found.</p>
          ) : (
            products.map(product => (
              <div key={product.id} className="product-card">
                <div className="product-image-container">
                  {product.imageUrl ? (
                    <img src={product.imageUrl} alt={product.name} className="product-image" />
                  ) : (
                    <div className="no-image">No Image</div>
                  )}
                </div>
                <div className="product-details">
                  <h3 className="product-name">{product.name}</h3>
                  <p>Type: {product.productType}</p>
                  <p>Available: {product.activeIngredients || 'N/A'}</p>
                  <p>Tested: <span style={{ color: product.tested === 'YES' ? 'green' : 'red' }}>{product.tested}</span></p>
                  <button className="view-details-button">View Details</button>
                </div>
              </div>
            ))
          )}
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default MarketProducts; 