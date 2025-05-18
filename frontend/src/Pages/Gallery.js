import React, { useState } from 'react';
import Header from '../Components/Header/Header';
import Footer from '../Components/Footer/Footer';
import HomeImg from '../Assets/About.jpg';
import './Gallery.css';

const Gallery = (props) => {
  const [activeCategory, setActiveCategory] = useState('All');
  
  // Sample gallery data with image paths
  const galleryItems = [
    { 
      id: 1, 
      title: 'Agricultural Technology Exhibition', 
      category: 'Events', 
      date: 'March 2025',
      image: HomeImg
    },
    { 
      id: 2, 
      title: 'Harvest Season Highlights', 
      category: 'Seasonal', 
      date: 'February 2025',
      image: HomeImg
    },
    { 
      id: 3, 
      title: 'Precision Farming Demonstration', 
      category: 'Technology', 
      date: 'January 2025',
      image: HomeImg
    },
    { 
      id: 4, 
      title: 'Sustainable Farming Practices', 
      category: 'Education', 
      date: 'December 2024',
      image: HomeImg
    },
    { 
      id: 5, 
      title: 'Agri Test Annual Conference', 
      category: 'Events', 
      date: 'November 2024',
      image: HomeImg
    },
    { 
      id: 6, 
      title: 'Modern Irrigation Systems', 
      category: 'Technology', 
      date: 'October 2024',
      image: HomeImg
    }
  ];

  // Get unique categories for filter buttons
  const categories = ['All', ...new Set(galleryItems.map(item => item.category))];
  
  // Filter items based on active category
  const filteredItems = activeCategory === 'All' 
    ? galleryItems 
    : galleryItems.filter(item => item.category === activeCategory);
  
  return (
    <div>
      <Header {...props} />
      
      <div className="gallery-hero">
        <div className="gallery-hero-content">
          <h1>Photo Gallery</h1>
          <p>Explore our collection of agricultural innovations and events</p>
        </div>
      </div>
      
      <main className="gallery-container">
        <div className="gallery-filter">
          {categories.map(category => (
            <button 
              key={category}
              className={`filter-button ${activeCategory === category ? 'active' : ''}`}
              onClick={() => setActiveCategory(category)}
            >
              {category}
            </button>
          ))}
        </div>
        
        <div className="gallery-grid">
          {filteredItems.map(item => (
            <div key={item.id} className="gallery-item">
              <div className="gallery-image-container">
                <img src={item.image} alt={item.title} className="gallery-image" />
                <div className="gallery-overlay">
                  <button className="view-button">View</button>
                </div>
              </div>
              <div className="gallery-info">
                <div className="gallery-meta">
                  <span className="gallery-category">{item.category}</span>
                  <span className="gallery-date">{item.date}</span>
                </div>
                <h3 className="gallery-title">{item.title}</h3>
              </div>
            </div>
          ))}
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default Gallery;