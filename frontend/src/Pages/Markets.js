import React from 'react';
import Header from '../Components/Header/Header';
import Footer from '../Components/Footer/Footer';
import HomeImg from '../Assets/Kinigi.png';
import HomeImgs from '../Assets/potatoes.jpg';
import HomeImgss from '../Assets/wheatFarm.JPG';
import './Markets.css';

const Markets = (props) => {
    return (
        <div>
            <Header {...props} />
            <main className="markets-container">
                <section className="markets-header">
                    {/* Left Column - Images (30%) */}
                    <div className="markets-images-column">
                        <div className="markets-image-container">
                            <img
                                src={HomeImg}
                                alt="Agricultural Marketplace"
                                className="markets-image"
                            />
                        </div>
                        <div className="markets-image-container second-image">
                            <img
                                src={HomeImgs}
                                alt="Agricultural Marketplace"
                                className="markets-image"
                            />
                        </div>
                        <div className="markets-image-container second-image">
                            <img
                                src={HomeImgss}
                                alt="Agricultural Marketplace"
                                className="markets-image"
                            />
                        </div>
                    </div>

                    {/* Right Column - Content (70%) */}
                    <div className="markets-content">
                        <h1 className="markets-title text-3xl">Agricultural Marketplace</h1>
                        <p className="markets-section-text mb-4">
                            Our comprehensive agricultural marketplace connects farmers, suppliers, and distributors 
                            with cutting-edge agricultural solutions and resources.
                        </p>

                        <div className="space-y-4">
                            <div className="markets-section">
                                <h2 className="markets-section-title text-2xl">Our Services</h2>
                                <ul className="markets-services-list">
                                    <li className="markets-service-item">
                                        <h3 className="markets-service-title">Available Products</h3>
                                        <p className="markets-section-text">Products that have passed testing and are ready for sale. Our platform ensures only the highest quality inputs reach farmers, supporting improved agricultural productivity and sustainability.</p>
                                    </li>
                                    <li className="markets-service-item">
                                        <h3 className="markets-service-title">Price Listings</h3>
                                        <p className="markets-section-text">Current market prices for agricultural inputs. Our dynamic pricing system helps farmers and suppliers make informed purchasing decisions with transparent and up-to-date information.</p>
                                    </li>
                                    <li className="markets-service-item">
                                        <h3 className="markets-service-title">Suppliers & Distributors</h3>
                                        <p className="markets-section-text">Verified suppliers of farming products. Each supplier undergoes a comprehensive vetting process to ensure reliability, quality, and commitment to agricultural innovation.</p>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </section>
            </main>
            <Footer />
        </div>
    );
};

export default Markets;