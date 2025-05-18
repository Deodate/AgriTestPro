import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import Header from '../Components/Header/Header';
import Footer from '../Components/Footer/Footer';
import './Products.css';
import potatoesImage from '../Assets/potatoes.jpg';
// Import the image directly if using webpack/create-react-app
// import potatoesImage from '../Assets/potatoes.jpg'; 

/**
 * Products component displaying a catalog of agricultural products with filtering
 * @param {Object} props - Component props
 * @returns {JSX.Element} - The rendered Products page
 */
const Products = (props) => {
    // Filter categories
    const categories = ['All', 'Insecticide', 'Fungicide', 'Fertilizer', 'Seed Coating', 'Water Treatment', 'Others'];
    
    // State for active filter
    const [activeFilter, setActiveFilter] = useState('All');
    // State for view mode (list or grid)
    const [viewMode, setViewMode] = useState('grid');
    
    // Image path - try multiple options depending on your project structure
    // Option 1: Using process.env.PUBLIC_URL for public folder (Create React App)
    // const imagePath = `${process.env.PUBLIC_URL}/Assets/potatoes.jpg`;
    
    // Option 2: Direct path (if images are in public folder)
    const imagePath = '../Assets/potatoes.jpg';
    
    // Option 3: Relative path
    // const imagePath = '../Assets/potatoes.jpg';
    
    // Option 4: Full path if needed
    // const imagePath = 'http://localhost:3000/Assets/potatoes.jpg';
    
    // Sample products data
    const products = [
        {
            id: 1,
            name: 'DELTAMAX',
            type: 'Insecticide',
            available: '1L',
            tested: true,
            image: potatoesImage 
        },
        {
            id: 2,
            name: 'SULPHURMAX',
            type: 'Fungicide',
            available: '1 kg',
            tested: false,
            image: potatoesImage 
        },
        {
            id: 3,
            name: 'HIGHTIVAMAX',
            type: 'Fungicide',
            available: '1 L',
            tested: true,
            image: potatoesImage 
        },
        {
            id: 4,
            name: 'IMAX 350 FS',
            type: 'Seed Coating',
            available: '25 L',
            tested: true,
            image: potatoesImage 
        },
        {
            id: 5,
            name: 'JACKMAX',
            type: 'Insecticide',
            available: '1L\n100 ML',
            tested: true,
            image: potatoesImage 
        },
        {
            id: 6,
            name: 'COPPERMAX',
            type: 'Fungicide',
            available: '1kg\n250 gr',
            tested: true,
            image: potatoesImage 
        },
        {
            id: 7,
            name: 'SUPERFEED PLUS',
            type: 'Fertilizer',
            available: '1KG',
            tested: false,
            image: potatoesImage 
        },
        {
            id: 8,
            name: 'MALAXYM 2',
            type: 'Seed Coating',
            available: '1 KG\n500 GR',
            tested: true,
            image: potatoesImage 
        }
    ];
    
    // Filter products based on active filter
    const filteredProducts = activeFilter === 'All' 
        ? products 
        : products.filter(product => product.type === activeFilter);
    
    // Handler for filter click
    const handleFilterClick = (category) => {
        setActiveFilter(category);
    };
    
    // Get products count for pagination
    const totalProducts = filteredProducts.length;
    const currentPage = 1;
    const totalPages = Math.ceil(totalProducts / 8); // Assuming 8 products per page
    
    return (
        <div>
            <Header {...props} />
            
            <main className="products-container">
                <div className="products-header">
                    <h1>Agricultural Products</h1>
                    <p>Browse our selection of tested and approved agricultural products</p>
                </div>
                
                <div className="filter-container">
                    <div className="category-filters">
                        {categories.map(category => (
                            <button 
                                key={category} 
                                className={`filter-btn ${activeFilter === category ? 'active' : ''}`}
                                onClick={() => handleFilterClick(category)}
                            >
                                {category}
                            </button>
                        ))}
                    </div>
                    
                    <div className="view-options">
                        <button 
                            className={`view-btn ${viewMode === 'list' ? 'active' : ''}`}
                            onClick={() => setViewMode('list')}
                            aria-label="List view"
                        >
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                <line x1="8" y1="6" x2="21" y2="6"></line>
                                <line x1="8" y1="12" x2="21" y2="12"></line>
                                <line x1="8" y1="18" x2="21" y2="18"></line>
                                <line x1="3" y1="6" x2="3.01" y2="6"></line>
                                <line x1="3" y1="12" x2="3.01" y2="12"></line>
                                <line x1="3" y1="18" x2="3.01" y2="18"></line>
                            </svg>
                        </button>
                        <button 
                            className={`view-btn ${viewMode === 'grid' ? 'active' : ''}`}
                            onClick={() => setViewMode('grid')}
                            aria-label="Grid view"
                        >
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                <rect x="3" y="3" width="7" height="7"></rect>
                                <rect x="14" y="3" width="7" height="7"></rect>
                                <rect x="14" y="14" width="7" height="7"></rect>
                                <rect x="3" y="14" width="7" height="7"></rect>
                            </svg>
                        </button>
                    </div>
                </div>
                
                <div className={`products-grid ${viewMode === 'list' ? 'list-view' : ''}`}>
                    {filteredProducts.map(product => (
                        <div key={product.id} className="product-card">
                            <div className="product-image">
                                <img 
                                    src={product.image} 
                                    alt={product.name} 
                                    onError={(e) => {
                                        console.error("Image failed to load", e);
                                        e.target.src = "https://via.placeholder.com/200x150?text=Product+Image";
                                    }}
                                />
                            </div>
                            <div className="product-info">
                                <h2 className="product-name">{product.name}</h2>
                                <div className="product-details">
                                    <p>Type: {product.type}</p>
                                    <p>Available: {product.available}</p>
                                    <p>Tested: <span className={product.tested ? 'tested-yes' : 'tested-no'}>
                                        {product.tested ? 'YES' : 'NO'}
                                    </span></p>
                                </div>
                                <Link to={`/product/${product.id}`} className="view-details-btn">
                                    View Details
                                </Link>
                            </div>
                        </div>
                    ))}
                </div>
                
                <div className="pagination">
                    <p className="showing-info">Showing {filteredProducts.length} of {totalProducts}</p>
                    <div className="pagination-controls">
                        <button className="pagination-btn" disabled={currentPage === 1}>
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                <polyline points="15 18 9 12 15 6"></polyline>
                            </svg>
                        </button>
                        <button className="pagination-btn" disabled={currentPage === totalPages}>
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                <polyline points="9 18 15 12 9 6"></polyline>
                            </svg>
                        </button>
                    </div>
                </div>
            </main>
            
            <Footer />
        </div>
    );
};

export default Products;