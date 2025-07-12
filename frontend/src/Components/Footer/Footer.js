import React from "react";
import "./Footer.css";

/**
 * Footer component based on Rwanda Agriculture Board design.
 * Includes three columns: logo/company, location/contact, and quick links.
 */
const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer__container">
        {/* First Column - Logo and Company */}
        <div className="footer__section">
          <div className="footer__logo">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" width="120" height="120">
              {/* Outer circle with dots */}
              <circle cx="50" cy="50" r="3" fill="#4CAF50" />
              <circle cx="45" cy="35" r="3" fill="#4CAF50" />
              <circle cx="35" cy="40" r="3" fill="#4CAF50" />
              <circle cx="30" cy="50" r="3" fill="#4CAF50" />
              <circle cx="35" cy="60" r="3" fill="#4CAF50" />
              <circle cx="45" cy="65" r="3" fill="#2196F3" />
              <circle cx="55" cy="65" r="3" fill="#2196F3" />
              <circle cx="65" cy="60" r="3" fill="#2196F3" />
              <circle cx="70" cy="50" r="3" fill="#FFC107" />
              <circle cx="65" cy="40" r="3" fill="#FFC107" />
              <circle cx="55" cy="35" r="3" fill="#FFC107" />
              
              {/* Text in the middle */}
              <text x="42" y="46" fontFamily="Arial" fontSize="8" fontWeight="bold" fill="#1565C0">AGRI</text>
              <text x="42" y="54" fontFamily="Arial" fontSize="8" fontWeight="bold" fill="#1565C0">TEST</text>
              <text x="42" y="62" fontFamily="Arial" fontSize="6" fill="#1565C0">Pro.</text>
            </svg>
          </div>
          <h2 className="footer__title">Rwanda Agriculture Board</h2>
        </div>

        {/* Second Column - Location and Contact */}
        <div className="footer__section">
          <h2 className="footer__title">Agro Test Pro</h2>
          <div className="footer__contact-item">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#2196F3" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
              <circle cx="12" cy="10" r="3"></circle>
            </svg>
            <div>
              <p>Musanze</p>
              <p>North Province</p>
            </div>
          </div>
          <div className="footer__contact-item">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#2196F3" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
            </svg>
            <p>(250) 789-000-873</p>
          </div>
        </div>

        {/* Third Column - Phone and Quick Links */}
        <div className="footer__section">
          <h2 className="footer__title">Quick Links</h2>
          <div className="footer__link-item">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#4169E1" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M12 22s-8-4.5-8-11.8A8 8 0 0 1 12 2a8 8 0 0 1 8 8.2c0 7.3-8 11.8-8 11.8z"></path>
              <path d="M12 8v8"></path>
              <path d="M8 12h8"></path>
            </svg>
            <a href="/privacy">Privacy Policy</a>
          </div>
          <div className="footer__link-item">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#4169E1" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M17 3a2.828 2.828 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5L17 3z"></path>
            </svg>
            <a href="/accessibility">Accessibility Statement</a>
          </div>
          <div className="footer__link-item">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#4169E1" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <rect x="3" y="3" width="7" height="7"></rect>
              <rect x="14" y="3" width="7" height="7"></rect>
              <rect x="14" y="14" width="7" height="7"></rect>
              <rect x="3" y="14" width="7" height="7"></rect>
            </svg>
            <a href="/sitemap">Sitemap</a>
          </div>
        </div>
      </div>

      <div className="footer__divider"></div>
      
      <div className="footer__bottom">
        <p>© 2025 Agri Test Products. All Rights Reserved.</p>
        <p className="footer__rwandan-product">RWANDAN<br />AGRICULTURE PRODUCT</p>
      </div>
    </footer>
  );
};

export default Footer;