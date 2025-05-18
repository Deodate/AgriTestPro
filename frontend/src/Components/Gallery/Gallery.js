import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Gallery.css';
import KinigiImage from '../../Assets/potatoes.jpg';
import Cornwheat from '../../Assets/corn-wheat.jpg';
import FarmView from '../../Assets/FarmView.JPG';
import FarmsView from '../../Assets/FarmsView.JPG';
import Viewimg from '../../Assets/Viewimg.jpg';
import HomeImg from '../../Assets/wheatFarm.JPG';
import Viewimgs from '../../Assets/Viwies.jpg';


const images = [
    Viewimgs,
    HomeImg,
    FarmView,
    KinigiImage,
    Cornwheat,
    FarmsView,
    Viewimg,
  
];

const Gallery = () => {
    const [currentIndex, setCurrentIndex] = useState(0);
    const navigate = useNavigate();

    // Auto-advance images every 5 seconds
    useEffect(() => {
        const interval = setInterval(() => {
            setCurrentIndex((prevIndex) => (prevIndex === images.length - 1 ? 0 : prevIndex + 1));
        }, 5000);
        return () => clearInterval(interval);
    }, []);

    const handleSignUp = () => {
        // Navigate to the login page instead of signup
        navigate('/login');
    };

    const handleSignIn = () => {
        // Handle sign in action or navigation
        navigate('/login');
        // You could add: navigate('/login'); once the login route is uncommented
    };

    return (
        <div className="gallery">
            <div className="gallery__container">
                <div className="gallery__container__image">
                    <img src={images[currentIndex]} alt="gallery" />
                </div>
                <div className="gallery__container__buttons">
                    <button className="gallery__button gallery__button--signup" onClick={handleSignUp}>
                        Sign Up
                    </button>
                    <button className="gallery__button gallery__button--signin" onClick={handleSignIn}>
                        Sign In
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Gallery;