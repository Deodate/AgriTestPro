import React from 'react';
import Header from '../Components/Header/Header';
import Footer from '../Components/Footer/Footer';
import HomeImg from '../Assets/About.jpg';
import './About.css'; // Import the CSS file

const About = (props) => {
    return (
        <div>
            <Header {...props} />
            <div className="about">
                <div className="about-left">
                    <img 
                        src={HomeImg} 
                        alt="Agricultural Technology" 
                        className="about-image" 
                    />
                </div>
                <div className="about-right">
                    <h1 className="about-title">Overview</h1>
                    <p className="about-text">
                        Agri Test is a cutting-edge agricultural technology platform dedicated to transforming agricultural product testing
                        through innovative data-driven solutions. We provide comprehensive testing and analysis services that empower farmers
                        and agricultural businesses to make informed decisions.
                    </p>

                    <h2 className="project-details-title">Mission & Vision</h2>
                    <p className="about-text">
                        Our mission is to revolutionize agricultural product testing by leveraging advanced technologies,
                        comprehensive analysis, and data-driven insights. We aim to:
                    </p>
                    <ul className="about-list">
                        <li className="about-list-item">Enhance agricultural productivity through precise product testing</li>
                        <li className="about-list-item">Provide actionable insights for crop improvement</li>
                        <li className="about-list-item">Support sustainable agricultural practices</li>
                        <li className="about-list-item">Empower farmers with cutting-edge technological solutions</li>
                    </ul>
                </div>
            </div>
            <Footer />
        </div>
    );
};

export default About;