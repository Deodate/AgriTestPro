import React from 'react';
import Header from '../Components/Header/Header';
import Footer from '../Components/Footer/Footer';
import './Weather.css';

const Weather = (props) => {
  return (
    <div>
      <Header {...props} />
      <main className="container mx-auto py-8 px-4">
        {/* Circular header section */}
        <div className="circular-header-container mb-8">
          <div className="circular-background">
            <h1>Agricultural Weather Services</h1>
            <p>
              Our comprehensive weather service provides farmers with crucial climate information to support 
              agricultural decision-making and improve crop outcomes.
            </p>
          </div>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8">
          <div className="weather-card">
            <div className="weather-card-icon">🌦️</div>
            <h3 className="weather-card-title">Live Weather Updates</h3>
            <p className="weather-card-text">Real-time weather forecasts for farmers to plan daily operations and prepare for changing conditions.</p>
          </div>
          
          <div className="weather-card">
            <div className="weather-card-icon">📊</div>
            <h3 className="weather-card-title">Seasonal Trends</h3>
            <p className="weather-card-text">Insights on climate changes affecting farming, helping with long-term planning and risk management.</p>
          </div>
          
          <div className="weather-card">
            <div className="weather-card-icon">🌱</div>
            <h3 className="weather-card-title">Agronomic Recommendations</h3>
            <p className="weather-card-text">Advice on when to plant, fertilize, or harvest based on current and forecasted weather conditions.</p>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default Weather;