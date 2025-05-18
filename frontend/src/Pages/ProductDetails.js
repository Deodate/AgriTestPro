import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import Header from '../../Components/Header/Header';
import Footer from '../../Components/Footer/Footer';
import './ProductDetails.css';
import potatoesImage from '../Assets/potatoes.jpg'; // Corrected path with double ../
import ProductImg from '../../Assets/About.jpg'; // Corrected path with double ../

/**
 * Product Details component to display detailed information about a specific product
 * @param {Object} props - Component props
 * @returns {JSX.Element} - The rendered ProductDetails page
 */
const ProductDetails = (props) => {
    const { productId } = useParams();
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    
    // Fallback image in case of error
    const fallbackImage = "https://via.placeholder.com/400x300?text=Product+Image";
    
    // Sample products data - in a real app, this would come from an API
    const productsData = [
        {
            id: 1,
            name: 'DELTAMAX',
            type: 'Insecticide',
            available: '1L',
            tested: true,
            image: potatoesImage,
            description: 'DELTAMAX is a powerful insecticide that effectively controls a wide range of pests in various crops. It provides long-lasting protection with minimal environmental impact.',
            activeIngredient: 'Deltamethrin 2.5% EC',
            applications: ['Vegetables', 'Cereals', 'Fruits'],
            dosage: '15-20 ml per 20L of water',
            waitingPeriod: '7 days before harvest',
            safetyInfo: 'Keep out of reach of children. Avoid contact with skin and eyes. Wear protective clothing during application.'
        },
        {
            id: 2,
            name: 'SULPHURMAX',
            type: 'Fungicide',
            available: '1 kg',
            tested: false,
            image: potatoesImage,
            description: 'SULPHURMAX is a versatile fungicide that controls powdery mildew and other fungal diseases. It also has secondary effects against certain mites and insects.',
            activeIngredient: 'Sulfur 80% WP',
            applications: ['Grapevines', 'Vegetables', 'Ornamentals'],
            dosage: '3-5 g per liter of water',
            waitingPeriod: '1 day before harvest',
            safetyInfo: 'Avoid application during hot weather. Do not mix with alkaline materials.'
        },
        {
            id: 3,
            name: 'HIGHTIVAMAX',
            type: 'Fungicide',
            available: '1 L',
            tested: true,
            image: potatoesImage,
            description: 'HIGHTIVAMAX provides broad-spectrum disease control with both preventative and curative properties. Ideal for disease management programs in various crops.',
            activeIngredient: 'Tebuconazole 25% EW',
            applications: ['Wheat', 'Barley', 'Vegetables'],
            dosage: '10-15 ml per 20L of water',
            waitingPeriod: '14 days before harvest',
            safetyInfo: 'Harmful if swallowed. May cause eye irritation. Store in original container.'
        },
        {
            id: 4,
            name: 'IMAX 350 FS',
            type: 'Seed Coating',
            available: '25 L',
            tested: true,
            image: potatoesImage,
            description: 'IMAX 350 FS is a premium seed treatment that protects seeds and seedlings from soil-borne diseases and early-season insect pests. Enhances germination and seedling vigor.',
            activeIngredient: 'Imidacloprid 350 g/L FS',
            applications: ['Maize', 'Wheat', 'Cotton', 'Rice'],
            dosage: '7-10 ml per kg of seeds',
            waitingPeriod: 'Not applicable (seed treatment)',
            safetyInfo: 'Toxic to bees. Do not use treated seed for human or animal consumption.'
        },
        {
            id: 5,
            name: 'JACKMAX',
            type: 'Insecticide',
            available: '1L\n100 ML',
            tested: true,
            image: potatoesImage,
            description: 'JACKMAX is a systemic insecticide with rapid knockdown effect and long residual activity. Controls sucking and chewing insects in various crops.',
            activeIngredient: 'Acetamiprid 20% SP',
            applications: ['Cotton', 'Vegetables', 'Citrus'],
            dosage: '5-8 g per 20L of water',
            waitingPeriod: '7-14 days depending on crop',
            safetyInfo: 'Avoid spray drift to non-target areas. Toxic to aquatic organisms.'
        },
        {
            id: 6,
            name: 'COPPERMAX',
            type: 'Fungicide',
            available: '1kg\n250 gr',
            tested: true,
            image: potatoesImage,
            description: 'COPPERMAX is a protective fungicide with bactericidal properties. Effective against a wide range of fungal and bacterial diseases in horticultural and field crops.',
            activeIngredient: 'Copper Oxychloride 50% WP',
            applications: ['Tomatoes', 'Potatoes', 'Grapes', 'Citrus'],
            dosage: '30-50 g per 20L of water',
            waitingPeriod: '7 days before harvest',
            safetyInfo: 'May cause skin sensitization. Wear protective gloves during mixing and application.'
        },
        {
            id: 7,
            name: 'SUPERFEED PLUS',
            type: 'Fertilizer',
            available: '1KG',
            tested: false,
            image: ProductImg,
            description: 'SUPERFEED PLUS is a complete water-soluble fertilizer with macro and micronutrients. Promotes balanced growth, flowering, and fruit development.',
            activeIngredient: 'NPK 20-20-20 + micronutrients',
            applications: ['All crops'],
            dosage: '2-5 g per liter of water for foliar application, 5-10 g per liter for soil application',
            waitingPeriod: 'None',
            safetyInfo: 'Store in a cool, dry place. Keep out of reach of children and pets.'
        },
        {
            id: 8,
            name: 'MALAXYM 2',
            type: 'Seed Coating',
            available: '1 KG\n500 GR',
            tested: true,
            image: ProductImg,
            description: 'MALAXYM 2 is a systemic fungicide seed treatment that protects against soil-borne and seed-borne diseases. Ensures healthy crop establishment under diverse conditions.',
            activeIngredient: 'Metalaxyl-M 2% WS',
            applications: ['Cereals', 'Pulses', 'Oilseeds'],
            dosage: '2-3 g per kg of seeds',
            waitingPeriod: 'Not applicable (seed treatment)',
            safetyInfo: 'Do not use treated seeds for food, feed, or oil processing. Wash hands after handling.'
        }
    ];
    
    // Fetch product data based on ID
    useEffect(() => {
        // Add some debugging
        console.log("Product ID from URL:", productId);
        console.log("Type of productId:", typeof productId);
        
        // Simulate API fetch with setTimeout
        const timer = setTimeout(() => {
            const foundProduct = productsData.find(p => p.id === parseInt(productId));
            console.log("Found product:", foundProduct);
            setProduct(foundProduct || null);
            setLoading(false);
        }, 300);
        
        return () => clearTimeout(timer);
    }, [productId]);
    
    // Loading state
    if (loading) {
        return (
            <div>
                <Header {...props} />
                <main className="product-details-container">
                    <div className="loading-spinner">Loading product details...</div>
                </main>
                <Footer />
            </div>
        );
    }
    
    // Product not found
    if (!product) {
        return (
            <div>
                <Header {...props} />
                <main className="product-details-container">
                    <div className="product-not-found">
                        <h2>Product Not Found</h2>
                        <p>The product you are looking for does not exist or has been removed.</p>
                        <Link to="/markets/products" className="back-to-products">Back to Products</Link>
                    </div>
                </main>
                <Footer />
            </div>
        );
    }
    
    return (
        <div>
            <Header {...props} />
            
            <main className="product-details-container">
                <div className="product-details-nav">
                    <Link to="/markets/products" className="back-to-products">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                            <line x1="19" y1="12" x2="5" y2="12"></line>
                            <polyline points="12 19 5 12 12 5"></polyline>
                        </svg>
                        Back to Products
                    </Link>
                </div>
                
                <div className="product-details-content">
                    <div className="product-image-container">
                        <img 
                            src={product.image} 
                            alt={product.name} 
                            className="product-detail-image" 
                            onError={(e) => {
                                console.error("Image failed to load", e);
                                e.target.src = fallbackImage;
                            }}
                        />
                        <div className="product-badges">
                            <span className="product-type-badge">{product.type}</span>
                            {product.tested && (
                                <span className="product-tested-badge">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                        <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                                        <polyline points="22 4 12 14.01 9 11.01"></polyline>
                                    </svg>
                                    Tested & Approved
                                </span>
                            )}
                        </div>
                    </div>
                    
                    <div className="product-details-info">
                        <h1 className="product-detail-name">{product.name}</h1>
                        
                        <div className="product-detail-meta">
                            <div className="meta-item">
                                <span className="meta-label">Availability:</span>
                                <span className="meta-value">{product.available}</span>
                            </div>
                            <div className="meta-item">
                                <span className="meta-label">Type:</span>
                                <span className="meta-value">{product.type}</span>
                            </div>
                            <div className="meta-item">
                                <span className="meta-label">Tested:</span>
                                <span className={`meta-value ${product.tested ? 'tested-yes' : 'tested-no'}`}>
                                    {product.tested ? 'YES' : 'NO'}
                                </span>
                            </div>
                        </div>
                        
                        <div className="product-description">
                            <h2>Description</h2>
                            <p>{product.description}</p>
                        </div>
                        
                        <div className="product-details-table">
                            <div className="detail-row">
                                <div className="detail-label">Active Ingredient</div>
                                <div className="detail-value">{product.activeIngredient}</div>
                            </div>
                            <div className="detail-row">
                                <div className="detail-label">Recommended For</div>
                                <div className="detail-value">
                                    <ul className="applications-list">
                                        {product.applications.map((app, index) => (
                                            <li key={index}>{app}</li>
                                        ))}
                                    </ul>
                                </div>
                            </div>
                            <div className="detail-row">
                                <div className="detail-label">Dosage</div>
                                <div className="detail-value">{product.dosage}</div>
                            </div>
                            <div className="detail-row">
                                <div className="detail-label">Waiting Period</div>
                                <div className="detail-value">{product.waitingPeriod}</div>
                            </div>
                        </div>
                        
                        <div className="product-safety">
                            <h2>Safety Information</h2>
                            <div className="safety-box">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                    <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path>
                                    <line x1="12" y1="9" x2="12" y2="13"></line>
                                    <line x1="12" y1="17" x2="12.01" y2="17"></line>
                                </svg>
                                <p>{product.safetyInfo}</p>
                            </div>
                        </div>
                        
                        <div className="product-actions">
                            <button className="inquiry-button">
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                                </svg>
                                Request Quote
                            </button>
                            <button className="download-button">
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                    <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                                    <polyline points="7 10 12 15 17 10"></polyline>
                                    <line x1="12" y1="15" x2="12" y2="3"></line>
                                </svg>
                                Download Datasheet
                            </button>
                        </div>
                    </div>
                </div>
                
                <div className="related-products">
                    <h2>Related Products</h2>
                    <div className="related-products-grid">
                        {productsData
                            .filter(p => p.type === product.type && p.id !== product.id)
                            .slice(0, 3)
                            .map(relatedProduct => (
                                <div key={relatedProduct.id} className="related-product-card">
                                    <div className="related-product-image">
                                        <img 
                                            src={relatedProduct.image} 
                                            alt={relatedProduct.name} 
                                            onError={(e) => {
                                                e.target.src = fallbackImage;
                                            }}
                                        />
                                    </div>
                                    <div className="related-product-info">
                                        <h3>{relatedProduct.name}</h3>
                                        <p>{relatedProduct.type}</p>
                                        <Link to={`/product/${relatedProduct.id}`} className="view-related-btn">
                                            View Details
                                        </Link>
                                    </div>
                                </div>
                            ))}
                    </div>
                </div>
            </main>
            
            <Footer />
        </div>
    );
};

export default ProductDetails;