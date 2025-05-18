import React from 'react';
import Header from '../../Components/Header/Header';
import Footer from '../../Components/Footer/Footer';
import './LatestNews.css';

const LatestNews = (props) => {
  return (
    <div>
      <Header {...props} />
      <main className="news-container">
        <div className="news-header">
          <h1>Latest News & Updates</h1>
          <p className="news-subtitle">Stay informed with the latest from AgriTestPro</p>
        </div>
        
        <div className="news-sections">
          <div className="news-card research">
            <div className="news-icon">
              <span>🔬</span>
            </div>
            <h2>Latest Research</h2>
            <p>Updates on agricultural product testing.</p>
            <a href="#" className="news-link">View Research</a>
          </div>
          
          <div className="news-card stories">
            <div className="news-icon">
              <span>🌾</span>
            </div>
            <h2>Success Stories</h2>
            <p>Testimonials from farmers and companies benefiting from AgriTestPro.</p>
            <a href="#" className="news-link">Read Stories</a>
          </div>
          
          <div className="news-card events">
            <div className="news-icon">
              <span>📅</span>
            </div>
            <h2>Upcoming Events</h2>
            <p>Conferences, training sessions, and webinars.</p>
            <a href="#" className="news-link">View Calendar</a>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default LatestNews;