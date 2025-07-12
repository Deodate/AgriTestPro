import React, { useState } from 'react';
import Header from '../Components/Header/Header';
import Footer from '../Components/Footer/Footer';
import './Contact.css';

const Contact = (props) => {
    const [formData, setFormData] = useState({
        fullName: '',
        email: '',
        phone: '',
        message: ''
    });
    const [formSubmitted, setFormSubmitted] = useState(false);
    const [errors, setErrors] = useState({
        fullName: '',
        email: '',
        phone: '',
        message: ''
    });

    const validateField = (name, value) => {
        let error = '';
        
        switch (name) {
            case 'fullName':
                if (value.trim().length < 3) {
                    error = 'Name must be at least 3 characters';
                }
                break;
            case 'email':
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailRegex.test(value)) {
                    error = 'Please enter a valid email address';
                }
                break;
            case 'phone':
                if (value && !/^[+]?[\d\s()-]{7,15}$/.test(value)) {
                    error = 'Please enter a valid phone number';
                }
                break;
            case 'message':
                if (value.trim().length < 10) {
                    error = 'Message must be at least 10 characters';
                }
                break;
            default:
                break;
        }
        
        return error;
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        
        // Validate the field
        const error = validateField(name, value);
        
        // Update errors state
        setErrors(prevErrors => ({
            ...prevErrors,
            [name]: error
        }));
        
        // Update form data
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        
        // Validate all fields before submission
        let isValid = true;
        const newErrors = {};
        
        Object.keys(formData).forEach(key => {
            const error = validateField(key, formData[key]);
            newErrors[key] = error;
            if (error) isValid = false;
        });
        
        setErrors(newErrors);
        
        if (isValid) {
            console.log('Form submitted:', formData);
            setFormSubmitted(true);
            setFormData({ fullName: '', email: '', phone: '', message: '' });
            setErrors({ fullName: '', email: '', phone: '', message: '' });
            setTimeout(() => setFormSubmitted(false), 5000);
        }
    };
    
    const handleCancel = () => {
        setFormData({ fullName: '', email: '', phone: '', message: '' });
        setErrors({ fullName: '', email: '', phone: '', message: '' });
    };

    return (
        <div>
            <Header {...props} />
            <main className="contact-container">
                <h1 className="contact-page-title">Contact Us</h1>
                <p className="contact-page-subtitle">Questions about our agricultural testing services? Reach out to us.</p>
                
                <div className="contact-content">
                    {/* Contact Info Card - Streamlined */}
                    <div className="contact-info-card">
                        <h2 className="contact-section-title">Contact Information</h2>
                        <div className="contact-quick-info">
                            <div className="contact-info-row">
                                <div className="contact-icon"><i className="location-icon"></i></div>
                                <div><strong>Address:</strong> RAB Headquarters, KK 30 Ave, Kigali, Rwanda</div>
                            </div>
                            <div className="contact-info-row">
                                <div className="contact-icon"><i className="phone-icon"></i></div>
                                <div><strong>Phone:</strong> +250 784809876</div>
                            </div>
                            <div className="contact-info-row">
                                <div className="contact-icon"><i className="email-icon"></i></div>
                                <div><strong>Email:</strong> agritestpro@gmail.com</div>
                            </div>
                            <div className="contact-info-row">
                                <div className="contact-icon"><i className="hours-icon"></i></div>
                                <div><strong>Hours:</strong> Monday-Friday: 8:00 AM - 5:00 PM</div>
                            </div>
                        </div>
                    </div>
                    
                    {/* Contact Form Card - Streamlined */}
                    <div className="contact-form-card">
                        <h2 className="contact-section-title">Send Us a Message</h2>
                        
                        {formSubmitted ? (
                            <div className="form-success-message">
                                Thank you for your message! We'll get back to you soon.
                            </div>
                        ) : (
                            <form onSubmit={handleSubmit} className="contact-form">
                                <div className="form-row">
                                    <div className="form-group">
                                        <label htmlFor="fullName">Full Name</label>
                                        <input
                                            type="text"
                                            id="fullName"
                                            name="fullName"
                                            value={formData.fullName}
                                            onChange={handleChange}
                                            className={errors.fullName ? "input-error" : ""}
                                            required
                                        />
                                        {errors.fullName && <span className="error-message">{errors.fullName}</span>}
                                    </div>
                                    <div className="form-group">
                                        <label htmlFor="email">Email Address</label>
                                        <input
                                            type="email"
                                            id="email"
                                            name="email"
                                            value={formData.email}
                                            onChange={handleChange}
                                            className={errors.email ? "input-error" : ""}
                                            required
                                        />
                                        {errors.email && <span className="error-message">{errors.email}</span>}
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="phone">Phone Number</label>
                                    <input
                                        type="tel"
                                        id="phone"
                                        name="phone"
                                        value={formData.phone}
                                        onChange={handleChange}
                                        className={errors.phone ? "input-error" : ""}
                                    />
                                    {errors.phone && <span className="error-message">{errors.phone}</span>}
                                </div>
                                <div className="form-group">
                                    <label htmlFor="message">Your Message</label>
                                    <textarea
                                        id="message"
                                        name="message"
                                        rows="3"
                                        value={formData.message}
                                        onChange={handleChange}
                                        className={errors.message ? "input-error" : ""}
                                        required
                                    ></textarea>
                                    {errors.message && <span className="error-message">{errors.message}</span>}
                                </div>
                                <div className="button-row">
                                    <button type="submit" className="submit-button">Send Message</button>
                                    <button type="button" className="cancel-button" onClick={handleCancel}>Cancel</button>
                                </div>
                            </form>
                        )}
                    </div>
                </div>
            </main>
            <Footer /> <hr/>
        </div>
    );
};

export default Contact;