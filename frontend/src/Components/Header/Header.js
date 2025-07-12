import React, { useState, useRef, useEffect } from 'react';
import { NavLink, Link } from 'react-router-dom';
import './header.css';
import { useAuth } from '../../contexts/AuthContext';

/**
 * Header component for the application based on Agri Test design.
 * @param {Object} props - The props object.
 * @param {boolean} props.loggedIn - Indicates whether the user is logged in or not.
 * @returns {JSX.Element} - The rendered header component.
 */
const Header = ({ loggedIn }) => {
    const [isNewsDropdownOpen, setIsNewsDropdownOpen] = useState(false);
    const [isProductsDropdownOpen, setIsProductsDropdownOpen] = useState(false);
    const newsDropdownRef = useRef(null);
    const productsDropdownRef = useRef(null);
    const { user } = useAuth();
    
    const toggleNewsDropdown = (e) => {
        e.preventDefault();
        setIsNewsDropdownOpen(!isNewsDropdownOpen);
        setIsProductsDropdownOpen(false);
    };

    const toggleProductsDropdown = (e) => {
        e.preventDefault();
        setIsProductsDropdownOpen(!isProductsDropdownOpen);
        setIsNewsDropdownOpen(false);
    };

    // Close dropdowns when clicking outside
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (newsDropdownRef.current && !newsDropdownRef.current.contains(event.target)) {
                setIsNewsDropdownOpen(false);
            }
            if (productsDropdownRef.current && !productsDropdownRef.current.contains(event.target)) {
                setIsProductsDropdownOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    return (
        <header className='header'>
            <div className='header__container'>
                {/* Logo section */}
                <div className='header__logo-section'>
                    <div className='header__logo'>
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 60 60" width="40" height="40">
                            <circle cx="30" cy="30" r="3" fill="#4CAF50" />
                            <circle cx="20" cy="20" r="3" fill="#4CAF50" />
                            <circle cx="20" cy="40" r="3" fill="#3F51B5" />
                            <circle cx="30" cy="45" r="3" fill="#FFC107" />
                            <circle cx="40" cy="40" r="3" fill="#FFC107" />
                        </svg>
                    </div>
                    <div className='header__title'>
                        <h1>AGRI TEST</h1>
                        <p>Pro.</p>
                    </div>
                </div>
                
                {/* Navigation section */}
                <nav className='header__nav'>
                    <ul className='header__nav-list'>
                        <li className='header__nav-item'>
                            <NavLink 
                                to='/' 
                                className='header__nav-link'
                                style={({ isActive }) => 
                                    isActive ? { backgroundColor: '#2463eb', color: 'white' } : {}
                                }
                            >
                                HOME
                            </NavLink>
                        </li>
                        <li className='header__nav-item'>
                            <NavLink 
                                to='/about' 
                                className='header__nav-link'
                                style={({ isActive }) => 
                                    isActive ? { backgroundColor: '#2463eb', color: 'white' } : {}
                                }
                            >
                                ABOUT
                            </NavLink>
                        </li>
                        
                        {/* Markets Dropdown Menu */}
                        <li className='header__nav-item dropdown' ref={productsDropdownRef}>
                            <NavLink 
                                to='/markets' 
                                className='header__nav-link header__nav-link--dropdown'
                                onClick={toggleProductsDropdown}
                                style={({ isActive }) => 
                                    isActive ? { backgroundColor: '#2463eb', color: 'white' } : {}
                                }
                            >
                                 MARKETS
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                    <polyline points="6 9 12 15 18 9"></polyline>
                                </svg>
                            </NavLink>
                            {isProductsDropdownOpen && (
                                <div className='header__dropdown-menu'>
                                    <NavLink 
                                        to='/markets' 
                                        className='header__dropdown-item'
                                        style={({ isActive }) => 
                                            isActive ? { backgroundColor: '#f0f0f0' } : {}
                                        }
                                    >
                                        Markets Overview
                                    </NavLink>
                                    
                                    <NavLink 
                                        to='/markets/products' 
                                        className='header__dropdown-item'
                                        style={({ isActive }) => 
                                            isActive ? { backgroundColor: '#f0f0f0' } : {}
                                        }
                                    >
                                        Products
                                    </NavLink>
                                </div>
                            )}
                        </li>
                        
                        <li className='header__nav-item dropdown' ref={newsDropdownRef}>
                            <NavLink 
                                to='/news' 
                                className='header__nav-link header__nav-link--dropdown'
                                onClick={toggleNewsDropdown}
                                style={({ isActive }) => 
                                    isActive ? { backgroundColor: '#2463eb', color: 'white' } : {}
                                }
                            >
                                NEWS
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                    <polyline points="6 9 12 15 18 9"></polyline>
                                </svg>
                            </NavLink>
                            {isNewsDropdownOpen && (
                                <div className='header__dropdown-menu'>
                                    <NavLink 
                                        to='/news/latest' 
                                        className='header__dropdown-item'
                                        style={({ isActive }) => 
                                            isActive ? { backgroundColor: '#f0f0f0' } : {}
                                        }
                                    >
                                        Latest News
                                    </NavLink>
                                    
                                    <NavLink 
                                        to='/news/gallery' 
                                        className='header__dropdown-item'
                                        style={({ isActive }) => 
                                            isActive ? { backgroundColor: '#f0f0f0' } : {}
                                        }
                                    >
                                        Gallery
                                    </NavLink>
                                </div>
                            )}
                        </li>
                        <li className='header__nav-item'>
                            <NavLink 
                                to='/weather' 
                                className='header__nav-link'
                                style={({ isActive }) => 
                                    isActive ? { backgroundColor: '#2463eb', color: 'white' } : {}
                                }
                            >
                                WEATHER
                            </NavLink>
                        </li>
                    </ul>
                    
                    <div className='header__right-section'>
                    <div className='header__contact'>
                        <NavLink 
                            to='/contact' 
                            className='header__contact-button'
                            style={({ isActive }) => 
                                isActive ? { backgroundColor: '#1a4fba' } : {}
                            }
                        >
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                <path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"></path>
                            </svg>
                            Contact us
                        </NavLink>
                        </div>
                    </div>
                </nav>
            </div>
        </header>
    );
};

export default Header;