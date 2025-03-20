import React from 'react';
import { Link } from 'react-router-dom';

const Footer = () => {
  return (
    <footer className="bg-white relative">
      {/* Curved top edge */}
      <div className="absolute top-0 left-0 right-0 w-full h-24 overflow-hidden transform -translate-y-full">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320" className="w-full h-full">
          <path fill="#ffffff" d="M0,160L1440,96L1440,320L0,320Z" />
        </svg>
      </div>
      
      <div className="container mx-auto px-4 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {/* Logo and Company Name */}
          <div className="col-span-1">
            <div className="flex items-start">
              {/* Circle logo with dots */}
              <div className="relative w-24 h-24">
                <div className="rounded-full border-white w-full h-full relative">
                  {/* Green dots */}
                  <div className="absolute w-2 h-2 rounded-full bg-green-600" style={{ top: '0%', left: '50%', transform: 'translateX(-50%)' }}></div>
                  <div className="absolute w-2 h-2 rounded-full bg-green-600" style={{ top: '5%', left: '65%' }}></div>
                  <div className="absolute w-2 h-2 rounded-full bg-green-600" style={{ top: '15%', left: '80%' }}></div>
                  <div className="absolute w-2 h-2 rounded-full bg-green-600" style={{ top: '30%', left: '90%' }}></div>
                  
                  {/* Blue dots */}
                  <div className="absolute w-2 h-2 rounded-full bg-blue-700" style={{ top: '45%', left: '90%' }}></div>
                  <div className="absolute w-2 h-2 rounded-full bg-blue-700" style={{ top: '60%', left: '85%' }}></div>
                  <div className="absolute w-2 h-2 rounded-full bg-blue-700" style={{ top: '75%', left: '75%' }}></div>
                  <div className="absolute w-2 h-2 rounded-full bg-blue-700" style={{ top: '85%', left: '65%' }}></div>
                  <div className="absolute w-2 h-2 rounded-full bg-blue-700" style={{ top: '95%', left: '50%', transform: 'translateX(-50%)' }}></div>
                  
                  {/* Yellow dots */}
                  <div className="absolute w-2 h-2 rounded-full bg-yellow-400" style={{ top: '95%', left: '35%' }}></div>
                  <div className="absolute w-2 h-2 rounded-full bg-yellow-400" style={{ top: '85%', left: '25%' }}></div>
                  <div className="absolute w-2 h-2 rounded-full bg-yellow-400" style={{ top: '75%', left: '15%' }}></div>
                  <div className="absolute w-2 h-2 rounded-full bg-yellow-400" style={{ top: '60%', left: '10%' }}></div>
                  <div className="absolute w-2 h-2 rounded-full bg-yellow-400" style={{ top: '45%', left: '5%' }}></div>
                </div>
                
                <div className="absolute inset-0 flex flex-col justify-center items-center pl-6 pt-2">
                  <div className="text-blue-700 font-bold text-xs">
                    <span className="block">RAB, Rwanda</span>
                    <span className="block">AGRI TEST PRODUCTS</span>
                    <span className="block text-gray-600 text-xs font-normal">RAB</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          {/* MAAPW Plant */}
          <div className="col-span-1">
            <h3 className="text-xl font-bold text-gray-800 mb-4">Agri Test Pro</h3>
            <div className="flex items-start mb-4">
              <svg className="w-5 h-5 text-blue-700 mt-1 mr-2" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd" />
              </svg>
              <div>
                <p className="text-gray-700">(250) 723-322-098</p>
                <p className="text-gray-700">Musanze, Kigali</p>
              </div>
            </div>
            <div className="flex items-center">
              <svg className="w-5 h-5 text-blue-700 mr-2" fill="currentColor" viewBox="0 0 20 20">
                <path d="M2 3a1 1 0 011-1h2.153a1 1 0 01.986.836l.74 4.435a1 1 0 01-.54 1.06l-1.548.773a11.037 11.037 0 006.105 6.105l.774-1.548a1 1 0 011.059-.54l4.435.74a1 1 0 01.836.986V17a1 1 0 01-1 1h-2C7.82 18 2 12.18 2 5V3z" />
              </svg>
              <span className="text-gray-700">(250) 734-457-000</span>
            </div>
          </div>
          
          {/* MAAPW Corporate Office */}
          <div className="col-span-1">
            <h3 className="text-xl font-bold text-gray-800 mb-4">Agri Test Pro</h3>
            <div className="flex items-start mb-4">
              <svg className="w-5 h-5 text-blue-700 mt-1 mr-2" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd" />
              </svg>
              <div>
                <p className="text-gray-700">Rwanda, Musanze</p>
                <p className="text-gray-700">Musanze, North Province</p>
              </div>
            </div>
            <div className="flex items-center">
              <svg className="w-5 h-5 text-blue-700 mr-2" fill="currentColor" viewBox="0 0 20 20">
                <path d="M2 3a1 1 0 011-1h2.153a1 1 0 01.986.836l.74 4.435a1 1 0 01-.54 1.06l-1.548.773a11.037 11.037 0 006.105 6.105l.774-1.548a1 1 0 011.059-.54l4.435.74a1 1 0 01.836.986V17a1 1 0 01-1 1h-2C7.82 18 2 12.18 2 5V3z" />
              </svg>
              <span className="text-gray-700">(250) 783-092-298</span>
            </div>
          </div>
          
          {/* Quick Links */}
          <div className="col-span-1">
            <h3 className="text-xl font-bold text-gray-800 mb-4">Quick Links</h3>
            <ul className="space-y-2">
              <li className="flex items-center">
                <svg className="w-5 h-5 text-blue-700 mr-2" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                </svg>
                <Link to="/privacy-policy" className="text-gray-700 hover:text-blue-700">
                  Privacy Policy
                </Link>
              </li>
              <li className="flex items-center">
                <svg className="w-5 h-5 text-blue-700 mr-2" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M6.267 3.455a3.066 3.066 0 001.745-.723 3.066 3.066 0 013.976 0 3.066 3.066 0 001.745.723 3.066 3.066 0 012.812 2.812c.051.643.304 1.254.723 1.745a3.066 3.066 0 010 3.976 3.066 3.066 0 00-.723 1.745 3.066 3.066 0 01-2.812 2.812 3.066 3.066 0 00-1.745.723 3.066 3.066 0 01-3.976 0 3.066 3.066 0 00-1.745-.723 3.066 3.066 0 01-2.812-2.812 3.066 3.066 0 00-.723-1.745 3.066 3.066 0 010-3.976 3.066 3.066 0 00.723-1.745 3.066 3.066 0 012.812-2.812zm7.44 5.252a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                </svg>
                <Link to="/accessibility" className="text-gray-700 hover:text-blue-700">
                  Accessibility Statement
                </Link>
              </li>
              <li className="flex items-center">
                <svg className="w-5 h-5 text-blue-700 mr-2" fill="currentColor" viewBox="0 0 20 20">
                  <path d="M5 3a2 2 0 00-2 2v2a2 2 0 002 2h2a2 2 0 002-2V5a2 2 0 00-2-2H5zM5 11a2 2 0 00-2 2v2a2 2 0 002 2h2a2 2 0 002-2v-2a2 2 0 00-2-2H5zM11 5a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V5zM11 13a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z" />
                </svg>
                <Link to="/sitemap" className="text-gray-700 hover:text-blue-700">
                  Sitemap
                </Link>
              </li>
            </ul>
          </div>
        </div>
      </div>
      
      {/* Footer bottom with design credit */}
      <div className="border-t border-gray-200 py-6">
        <div className="container mx-auto px-4 flex flex-col md:flex-row justify-between items-center">
          <p className="text-gray-600 text-sm mb-2 md:mb-0">
            &copy; {new Date().getFullYear()} Agri Test Pro. All Rights Reserved.
          </p>
          <div className="text-center">
            <a 
              href="https://cybernautic.com" 
              target="_blank" 
              rel="noopener noreferrer" 
              className="text-gray-400 text-xs"
            >
              <span className="block uppercase tracking-widest"></span>
              <span className="block text-xs uppercase">AUCA FINAL YEAR PROJECT</span>
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;