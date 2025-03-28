import React from 'react';
import { Link } from 'react-router-dom';

const Footer = () => {
  return (
    <footer className="bg-gray-800 text-white py-8">
      <div className="container mx-auto px-4">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {/* Company Info */}
          <div>
            <h3 className="text-xl font-bold mb-4">Agri Test Pro</h3>
            <p className="text-gray-400">
              Your trusted partner in agricultural testing solutions.
            </p>
          </div>

          {/* Quick Links */}
          <div>
            <h3 className="text-xl font-bold mb-4">Quick Links</h3>
            <ul className="space-y-2">
              <li>
                <Link to="/" className="text-gray-400 hover:text-white">
                  Home
                </Link>
              </li>
              <li>
                <Link to="/services" className="text-gray-400 hover:text-white">
                  Services
                </Link>
              </li>
              <li>
                <Link to="/about" className="text-gray-400 hover:text-white">
                  About
                </Link>
              </li>
              <li>
                <Link to="/contact" className="text-gray-400 hover:text-white">
                  Contact
                </Link>
              </li>
            </ul>
          </div>

          {/* Services */}
          <div>
            <h3 className="text-xl font-bold mb-4">Services</h3>
            <ul className="space-y-2">
              <li>
                <Link to="/services/soil-testing" className="text-gray-400 hover:text-white">
                  Soil Testing
                </Link>
              </li>
              <li>
                <Link to="/services/crop-analysis" className="text-gray-400 hover:text-white">
                  Crop Analysis
                </Link>
              </li>
              <li>
                <Link to="/services/water-quality" className="text-gray-400 hover:text-white">
                  Water Quality
                </Link>
              </li>
              <li>
                <Link to="/services/pest-control" className="text-gray-400 hover:text-white">
                  Pest Control
                </Link>
              </li>
            </ul>
          </div>

          {/* Contact Info */}
          <div>
            <h3 className="text-xl font-bold mb-4">Contact Us</h3>
            <ul className="space-y-2 text-gray-400">
              <li>123 Agriculture Street</li>
              <li>Farm City, FC 12345</li>
              <li>Phone: (555) 123-4567</li>
              <li>Email: info@agritestpro.com</li>
            </ul>
          </div>
        </div>

        {/* Copyright */}
        <div className="mt-8 pt-8 border-t border-gray-700 text-center text-gray-400">
          <p>&copy; {new Date().getFullYear()} Agri Test Pro. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer; 