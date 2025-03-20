// src/components/auth/SignupPage.jsx
import React, { Component } from "react";
import { useNavigate } from "react-router-dom";
import MENU_ITEMS, { APP_CONSTANTS } from "../../constants/MenuItems";
import Footer from "../Footer";
import UserAccount from "../../services/AUTH/userAccount";

class SignupPage extends Component {
    constructor(props) {
        super(props);

        this.state = {
            // Form fields
            fullName: '',
            phoneNumber: '',
            email: '',
            username: '',
            password: '',
            role: "ROLE_USER",
            confirmPassword: '',

            // Login modal fields
            loginEmail: "",
            loginPassword: "",

            // Two-factor fields
            twoFactorCode: "",
            twoFactorUserId: null,

            // UI state
            activeDropdown: null,
            showLoginModal: false,
            showTwoFactorModal: false,
            isLoading: false,
            errorMsg: "",
            successMsg: "",

            // Form validation state
            errors: {
                fullName: "",
                phoneNumber: "",
                email: "",
                username: "",
                password: "",
                confirmPassword: ""
            },
            validFields: {
                fullName: false,
                phoneNumber: false,
                email: false,
                username: false,
                password: false,
                confirmPassword: false
            }
        };

        // Binding all methods to 'this' context
        this.changeFullNameHandler = this.changeFullNameHandler.bind(this);
        this.changePhoneNumberHandler = this.changePhoneNumberHandler.bind(this);
        this.changeEmailHandler = this.changeEmailHandler.bind(this);
        this.changeUsernameHandler = this.changeUsernameHandler.bind(this);
        this.changePasswordHandler = this.changePasswordHandler.bind(this);
        this.changeConfirmPasswordHandler = this.changeConfirmPasswordHandler.bind(this);
        this.changeLoginEmailHandler = this.changeLoginEmailHandler.bind(this);
        this.changeLoginPasswordHandler = this.changeLoginPasswordHandler.bind(this);
        this.changeTwoFactorCodeHandler = this.changeTwoFactorCodeHandler.bind(this);
        this.validateField = this.validateField.bind(this);
        this.handleDropdownToggle = this.handleDropdownToggle.bind(this);
        this.toggleLoginModal = this.toggleLoginModal.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleLoginSubmit = this.handleLoginSubmit.bind(this);
        this.handleTwoFactorSubmit = this.handleTwoFactorSubmit.bind(this);
        this.createAccount = this.createAccount.bind(this);
    }

    createAccount = (e) => {
        e.preventDefault();
        this.setState({ isLoading: true, errorMsg: "", successMsg: "" });

        // Create account object from state
        let account = {
            fullName: this.state.fullName,
            phoneNumber: this.state.phoneNumber,
            email: this.state.email,
            username: this.state.username,
            password: this.state.password,
            role: this.state.role
        };

        console.log('account => ' + JSON.stringify(account));

        // Make API call to backend service
        UserAccount.createAccount(account).then(response => {
            console.log('User registered successfully', response.data);

            // Reset form fields
            this.setState({
                successMsg: "Account created successfully! You can now sign in.",
                fullName: '',
                phoneNumber: '',
                email: '',
                username: '',
                password: '',
                role: 'ROLE_USER',
                confirmPassword: '',
                isLoading: false
            });

            // Auto-open login modal after successful signup
            setTimeout(() => {
                this.setState({ showLoginModal: true });
            }, 2000);

        }).catch(error => {
            // Error handling
            let errorMessage = "Registration failed. Please try again.";

            // Extract more specific error message if available
            if (error.response && error.response.data) {
                if (error.response.data.message) {
                    errorMessage = error.response.data.message;
                } else if (typeof error.response.data === 'string') {
                    errorMessage = error.response.data;
                }
            }

            this.setState({
                errorMsg: errorMessage,
                isLoading: false
            });

            console.error("Registration error:", error);
        });
    }


    // Input handlers
    changeFullNameHandler = (event) => {
        this.setState({ fullName: event.target.value });
        this.validateField('fullName', event.target.value);
    }

    changePhoneNumberHandler = (event) => {
        this.setState({ phoneNumber: event.target.value });
        this.validateField('phoneNumber', event.target.value);
    }

    changeEmailHandler = (event) => {
        this.setState({ email: event.target.value });
        this.validateField('email', event.target.value);
    }

    changeUsernameHandler = (event) => {
        this.setState({ username: event.target.value });
        this.validateField('username', event.target.value);
    }

    changePasswordHandler = (event) => {
        this.setState({ password: event.target.value });
        this.validateField('password', event.target.value);
    }

    changeConfirmPasswordHandler = (event) => {
        this.setState({ confirmPassword: event.target.value });
        this.validateField('confirmPassword', event.target.value);
    }

    changeLoginEmailHandler = (event) => {
        this.setState({ loginEmail: event.target.value });
    }

    changeLoginPasswordHandler = (event) => {
        this.setState({ loginPassword: event.target.value });
    }

    changeTwoFactorCodeHandler = (event) => {
        this.setState({ twoFactorCode: event.target.value });
    }

    // Validation
    validateField(name, value) {
        let error = "";
        let isValid = false;

        switch (name) {
            case 'fullName':
                if (!value.trim()) {
                    error = "Full name is required";
                } else if (!/^[A-Za-z\s]+$/.test(value)) {
                    error = "Full name should only contain letters and spaces";
                } else if (!value.includes(" ")) {
                    error = "Please provide both first and last name";
                } else {
                    isValid = true;
                }
                break;

            case 'phoneNumber':
                if (!value.trim()) {
                    error = "Phone number is required";
                } else if (!/^\d{10}$/.test(value.replace(/\D/g, ''))) {
                    error = "Phone number must be 10 digits";
                } else {
                    isValid = true;
                }
                break;

            case 'email':
                if (!value.trim()) {
                    error = "Email is required";
                } else if (!/\S+@\S+\.\S+/.test(value)) {
                    error = "Invalid email format";
                } else {
                    isValid = true;
                }
                break;

            case 'username':
                if (!value.trim()) {
                    error = "Username is required";
                } else if (value.length < 4) {
                    error = "Username must be at least 4 characters";
                } else {
                    isValid = true;
                }
                break;

            case 'password':
                if (!value) {
                    error = "Password is required";
                } else if (value.length < 6) {
                    error = "Password must be at least 6 characters";
                } else {
                    isValid = true;
                }
                break;

            case 'confirmPassword':
                if (!value) {
                    error = "Please confirm your password";
                } else if (value !== this.state.password) {
                    error = "Passwords do not match";
                } else {
                    isValid = true;
                }
                break;

            default:
                break;
        }

        this.setState(prevState => ({
            errors: { ...prevState.errors, [name]: error },
            validFields: { ...prevState.validFields, [name]: isValid }
        }));

        return isValid;
    }

    // Dropdown handling
    handleDropdownToggle(label) {
        this.setState(prevState => ({
            activeDropdown: prevState.activeDropdown === label ? null : label
        }));
    }

    // Modal toggling
    toggleLoginModal() {
        this.setState(prevState => ({
            showLoginModal: !prevState.showLoginModal,
            errorMsg: ""
        }));
    }

    // Form submissions
    async handleSubmit(e) {
        e.preventDefault();
        this.setState({ isLoading: true, errorMsg: "", successMsg: "" });

        // Validate passwords match
        if (this.state.password !== this.state.confirmPassword) {
            this.setState({
                errorMsg: "Passwords do not match",
                isLoading: false
            });
            return;
        }

        try {
            // Call the register function from auth context
            const formData = {
                fullName: this.state.fullName,
                phoneNumber: this.state.phoneNumber,
                email: this.state.email,
                username: this.state.username,
                password: this.state.password,
                role: this.state.role
            };

            await this.props.auth.register(formData);
            this.setState({
                successMsg: "Account created successfully! You can now sign in.",
                fullName: '',
                phoneNumber: '',
                email: '',
                username: '',
                password: '',
                role: 'ROLE_USER',
                confirmPassword: ''
            });

            // Auto-open login modal after successful signup
            setTimeout(() => {
                this.setState({ showLoginModal: true });
            }, 2000);

        } catch (error) {
            this.setState({
                errorMsg: error.message || "Registration failed. Please try again."
            });
            console.error("Registration error:", error);
        } finally {
            this.setState({ isLoading: false });
        }
    }

    async handleLoginSubmit(e) {
        e.preventDefault();
        this.setState({ isLoading: true, errorMsg: "" });

        try {
            // Call the login function from auth context
            const response = await this.props.auth.login(
                this.state.loginEmail,
                this.state.loginPassword
            );

            // Handle 2FA if required
            if (response && response.requiresVerification) {
                this.setState({
                    twoFactorUserId: response.userId,
                    twoFactorCode: '',
                    showLoginModal: false,
                    showTwoFactorModal: true
                });
                return;
            }

            // On successful login
            this.setState({
                successMsg: "Login successful!",
                showLoginModal: false
            });

            // Redirect to dashboard
            setTimeout(() => {
                this.props.navigate('/dashboard');
            }, 1000);

        } catch (error) {
            this.setState({
                errorMsg: error.message || "Login failed. Please check your credentials."
            });
            console.error("Login error:", error);
        } finally {
            this.setState({ isLoading: false });
        }
    }

    async handleTwoFactorSubmit(e) {
        e.preventDefault();
        this.setState({ isLoading: true, errorMsg: "" });

        try {
            await this.props.auth.verifyTwoFactor(
                this.state.twoFactorUserId,
                this.state.twoFactorCode
            );

            this.setState({
                successMsg: "Verification successful!",
                showTwoFactorModal: false
            });

            // Redirect to dashboard
            setTimeout(() => {
                this.props.navigate('/dashboard');
            }, 1000);

        } catch (error) {
            this.setState({
                errorMsg: "Verification failed. Please try again."
            });
            console.error("2FA verification error:", error);
        } finally {
            this.setState({ isLoading: false });
        }
    }

    render() {
        const {
            fullName, phoneNumber, email, username, password, confirmPassword, role,
            loginEmail, loginPassword, twoFactorCode,
            activeDropdown, showLoginModal, showTwoFactorModal,
            isLoading, errorMsg, successMsg,
            errors, validFields
        } = this.state;

        return (
            <div className="min-h-screen flex flex-col w-screen overflow-hidden">
                {/* Navbar - Full Width */}
                <nav className="bg-teal-800 shadow-md w-full">
                    <div className="container mx-auto px-4 flex items-center h-24">
                        {/* Logo - Fixed on left */}
                        <div className="w-1/4 flex items-start">
                            <div className="flex items-center">
                                <div className="h-16 w-16 relative">
                                    {/* Circle with colored dots (simplified representation) */}
                                    <div className="rounded-full h-full w-full border-2 border-transparent relative flex items-center justify-center">
                                        <div className="absolute w-4 h-4 rounded-full bg-green-500" style={{ top: '0px', left: '50%', transform: 'translateX(-50%)' }}></div>
                                        <div className="absolute w-4 h-4 rounded-full bg-blue-500" style={{ top: '25%', left: '0px' }}></div>
                                        <div className="absolute w-4 h-4 rounded-full bg-yellow-500" style={{ bottom: '25%', left: '0px' }}></div>
                                        <div className="absolute w-4 h-4 rounded-full bg-yellow-500" style={{ bottom: '0px', left: '50%', transform: 'translateX(-50%)' }}></div>
                                    </div>
                                </div>
                                <div className="ml-2 text-white flex flex-col">
                                    <span className="text-xl font-bold">{APP_CONSTANTS.companyName}</span>
                                    <span className="text-sm">{APP_CONSTANTS.companySubtitle}</span>
                                </div>
                            </div>
                        </div>

                        {/* Navigation Menu - Starting from left with 5px margin */}
                        <div className="w-1/2 hidden md:flex space-x-4 items-center" style={{ marginLeft: '100px' }}>
                            {MENU_ITEMS.map((item) => (
                                <div
                                    key={item.label}
                                    className="relative group"
                                >
                                    <button
                                        className={`hover:text-blue-700 flex items-center py-2 px-2 rounded-md transition ${item.isHighlighted ? 'bg-blue-500 text-white' : 'bg-white text-gray-800'}`}
                                        onMouseEnter={() => item.hasDropdown && this.handleDropdownToggle(item.label)}
                                        onMouseLeave={() => item.hasDropdown && this.handleDropdownToggle(item.label)}
                                    >
                                        {item.label}
                                        {item.hasDropdown && (
                                            <svg className="ml-1 w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                                            </svg>
                                        )}
                                    </button>
                                    {item.hasDropdown && activeDropdown === item.label && (
                                        <div className="absolute z-10 mt-2 w-48 bg-white shadow-lg rounded-md"
                                            onMouseEnter={() => this.handleDropdownToggle(item.label)}
                                            onMouseLeave={() => this.handleDropdownToggle(null)}
                                        >
                                            {item.subItems.map((subItem) => (
                                                <a
                                                    key={subItem}
                                                    href="#"
                                                    className="block px-4 py-2 text-gray-700 hover:bg-gray-100"
                                                >
                                                    {subItem}
                                                </a>
                                            ))}
                                        </div>
                                    )}
                                </div>
                            ))}
                        </div>

                        {/* Contact us Button - Fixed position */}
                        <div className="w-1/4 flex justify-end">
                            <button
                                className="bg-blue-700 text-white px-4 py-2 rounded-md hover:bg-blue-800 transition flex items-center"
                                onClick={() => this.props.navigate('/contact')}
                                style={{ position: 'relative', left: '11px' }}
                            >
                                <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                                </svg>
                                Contact us
                            </button>
                        </div>
                    </div>
                </nav>

                {/* Signup Form Section */}
                <div className="flex-grow bg-gray-100 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
                    <div className="max-w-md w-full space-y-8 bg-white p-10 rounded-xl shadow-lg">
                        <div>
                            <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
                                Create your account
                            </h2>
                            <p className="mt-2 text-center text-sm text-gray-600">
                                Join Agri Test Pro and enhance your agricultural management
                            </p>
                        </div>

                        {/* Success Message */}
                        {successMsg && (
                            <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative">
                                {successMsg}
                            </div>
                        )}

                        {/* Error Message */}
                        {errorMsg && (
                            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative">
                                {errorMsg}
                            </div>
                        )}

                        <form className="mt-8 space-y-6" onSubmit={this.handleSubmit}>
                            <div>
                                <label htmlFor="fullName" className="sr-only">Full Name</label>
                                <input
                                    value={this.state.fullName}
                                    onChange={this.changeFullNameHandler}
                                    id="fullName"
                                    name="fullName"
                                    type="text"
                                    required
                                    className={`appearance-none rounded-md relative block w-full px-3 py-2 border ${errors.fullName ? 'border-red-500' : validFields.fullName ? 'border-green-500' : 'border-gray-300'
                                        } placeholder-gray-500 text-gray-900 focus:outline-none ${errors.fullName ? 'focus:ring-red-500 focus:border-red-500' : 'focus:ring-blue-500 focus:border-blue-500'
                                        }`}
                                    placeholder="Full Name (First and Last)"
                                    disabled={isLoading}
                                />
                                {errors.fullName && (
                                    <p className="mt-1 text-xs text-red-500">{errors.fullName}</p>
                                )}
                            </div>

                            <div>
                                <label htmlFor="phoneNumber" className="sr-only">Phone Number</label>
                                <input
                                    value={this.state.phoneNumber}
                                    onChange={this.changePhoneNumberHandler}
                                    id="phoneNumber"
                                    name="phoneNumber"
                                    type="tel"
                                    required
                                    className={`appearance-none rounded-md relative block w-full px-3 py-2 border ${errors.phoneNumber ? 'border-red-500' : validFields.phoneNumber ? 'border-green-500' : 'border-gray-300'
                                        } placeholder-gray-500 text-gray-900 focus:outline-none ${errors.phoneNumber ? 'focus:ring-red-500 focus:border-red-500' : 'focus:ring-blue-500 focus:border-blue-500'
                                        }`}
                                    placeholder="Phone Number (10 digits)"
                                    disabled={isLoading}
                                />
                                {errors.phoneNumber && (
                                    <p className="mt-1 text-xs text-red-500">{errors.phoneNumber}</p>
                                )}
                            </div>
                            <div>
                                <label htmlFor="email" className="sr-only">Email address</label>
                                <input
                                    value={this.state.email}
                                    onChange={this.changeEmailHandler}
                                    id="email"
                                    name="email"
                                    type="email"
                                    autoComplete="email"
                                    required
                                    className={`appearance-none rounded-md relative block w-full px-3 py-2 border ${errors.email ? 'border-red-500' : validFields.email ? 'border-green-500' : 'border-gray-300'
                                        } placeholder-gray-500 text-gray-900 focus:outline-none ${errors.email ? 'focus:ring-red-500 focus:border-red-500' : 'focus:ring-blue-500 focus:border-blue-500'
                                        }`}
                                    placeholder="Email address"
                                    disabled={isLoading}
                                />
                                {errors.email && (
                                    <p className="mt-1 text-xs text-red-500">{errors.email}</p>
                                )}
                            </div>
                            <div>
                                <label htmlFor="username" className="sr-only">Username</label>
                                <input
                                    value={this.state.username}
                                    onChange={this.changeUsernameHandler}
                                    id="username"
                                    name="username"
                                    type="text"
                                    required
                                    className={`appearance-none rounded-md relative block w-full px-3 py-2 border ${errors.username ? 'border-red-500' : validFields.username ? 'border-green-500' : 'border-gray-300'
                                        } placeholder-gray-500 text-gray-900 focus:outline-none ${errors.username ? 'focus:ring-red-500 focus:border-red-500' : 'focus:ring-blue-500 focus:border-blue-500'
                                        }`}
                                    placeholder="Username"
                                    disabled={isLoading}
                                />
                                {errors.username && (
                                    <p className="mt-1 text-xs text-red-500">{errors.username}</p>
                                )}
                            </div>
                            <div>
                                <label htmlFor="password" className="sr-only">Password</label>
                                <input
                                    value={this.state.password}
                                    onChange={this.changePasswordHandler}
                                    id="password"
                                    name="password"
                                    type="password"
                                    required
                                    className={`appearance-none rounded-md relative block w-full px-3 py-2 border ${errors.password ? 'border-red-500' : validFields.password ? 'border-green-500' : 'border-gray-300'
                                        } placeholder-gray-500 text-gray-900 focus:outline-none ${errors.password ? 'focus:ring-red-500 focus:border-red-500' : 'focus:ring-blue-500 focus:border-blue-500'
                                        }`}
                                    placeholder="Password"
                                    disabled={isLoading}
                                />
                                {errors.password && (
                                    <p className="mt-1 text-xs text-red-500">{errors.password}</p>
                                )}
                            </div>
                            <div>
                                <label htmlFor="confirmPassword" className="sr-only">Confirm Password</label>
                                <input
                                    value={this.state.confirmPassword}
                                    onChange={this.changeConfirmPasswordHandler}
                                    id="confirmPassword"
                                    name="confirmPassword"
                                    type="password"
                                    required
                                    className={`appearance-none rounded-md relative block w-full px-3 py-2 border ${errors.confirmPassword ? 'border-red-500' : validFields.confirmPassword ? 'border-green-500' : 'border-gray-300'
                                        } placeholder-gray-500 text-gray-900 focus:outline-none ${errors.confirmPassword ? 'focus:ring-red-500 focus:border-red-500' : 'focus:ring-blue-500 focus:border-blue-500'
                                        }`}
                                    placeholder="Confirm Password"
                                    disabled={isLoading}
                                />
                                {errors.confirmPassword && (
                                    <p className="mt-1 text-xs text-red-500">{errors.confirmPassword}</p>
                                )}
                            </div>
                            <div className="hidden">
                                <input value={role} type="hidden" id="role" name="role" />
                            </div>
                            <div className="flex justify-center gap-4">
                                <button
                                    type="submit"
                                    className={`w-1/3 flex justify-center py-2 px-3 border border-transparent text-sm font-medium rounded-md text-white bg-green-500 hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-400 ${isLoading ? 'opacity-70 cursor-not-allowed' : ''}`}
                                    disabled={isLoading}
                                    name="action"
                                    value="register"
                                    onClick={this.createAccount}
                                >
                                    {isLoading ? 'Processing...' : 'Register'}
                                </button>
                                <button
                                    type="button"
                                    onClick={() => this.props.navigate('/')}
                                    className={`w-1/3 flex justify-center py-2 px-3 border border-transparent text-sm font-medium rounded-md text-white bg-red-500 hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-400 ${isLoading ? 'opacity-70 cursor-not-allowed' : ''}`}
                                    disabled={isLoading}

                                >
                                    Cancel
                                </button>
                            </div>
                        </form>
                        <div className="text-center bg-transparent">
                            <p className="mt-2 text-sm text-gray-600">
                                Already have an account?
                                <button
                                    className="font-medium text-blue-600 hover:text-blue-500 ml-1 bg-transparent"
                                    onClick={this.toggleLoginModal}
                                    disabled={isLoading}
                                >
                                    Sign in
                                </button>
                            </p>
                        </div>
                    </div>
                </div>

                {/* Login Modal */}
                {showLoginModal && (
                    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                        <div className="bg-white p-8 rounded-lg shadow-xl max-w-md w-full relative">
                            {/* Close Button */}
                            <button
                                onClick={this.toggleLoginModal}
                                className="absolute top-4 right-4 text-gray-500 hover:text-gray-700"
                                aria-label="Close"
                                disabled={isLoading}
                            >
                                <svg className="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                                </svg>
                            </button>

                            <h2 className="text-2xl font-bold text-center mb-6 text-green-600">Login</h2>

                            {/* Error Message in Login Modal */}
                            {errorMsg && (
                                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4">
                                    {errorMsg}
                                </div>
                            )}
                            <form onSubmit={this.handleLoginSubmit} className="space-y-6">
                                <div>
                                    <label htmlFor="loginEmail" className="block text-sm font-medium text-gray-700">Email address</label>
                                    <input
                                        value={this.state.loginEmail}
                                        onChange={this.changeLoginEmailHandler}
                                        id="loginEmail"
                                        name="email"
                                        type="email"
                                        autoComplete="email"
                                        required
                                        className="mt-1 appearance-none rounded-md relative block w-full px-3 py-2 border border-blue-500 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                                        placeholder="Email address"
                                        disabled={isLoading}
                                    />
                                </div>
                                <div>
                                    <label htmlFor="loginPassword" className="block text-sm font-medium text-gray-700">Password</label>
                                    <input
                                        value={this.state.loginPassword}
                                        onChange={this.changeLoginPasswordHandler}
                                        id="loginPassword"
                                        name="password"
                                        type="password"
                                        autoComplete="current-password"
                                        required
                                        className="mt-1 appearance-none rounded-md relative block w-full px-3 py-2 border border-blue-500 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                                        placeholder="Password"
                                        disabled={isLoading}
                                    />
                                </div>
                                <div className="flex items-center justify-between">
                                    <div className="flex items-center">
                                        <input
                                            id="remember-me"
                                            name="remember-me"
                                            type="checkbox"
                                            className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                                        />
                                        <label htmlFor="remember-me" className="ml-2 block text-sm text-gray-900">
                                            Remember me
                                        </label>
                                    </div>
                                    <div className="text-sm">
                                        <a href="#" className="font-medium text-blue-600 hover:text-blue-500">
                                            Forgot your password?
                                        </a>
                                    </div>
                                </div>
                                <div className="flex justify-center gap-4">
                                    <button
                                        type="submit"
                                        className={`w-2/5 flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 ${isLoading ? 'opacity-70 cursor-not-allowed' : ''}`}
                                        disabled={isLoading}
                                    >
                                        {isLoading ? 'Signing in...' : 'Login'}
                                    </button>
                                    <button
                                        type="button"
                                        onClick={this.toggleLoginModal}
                                        className={`w-2/5 flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-red-500 hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-400 ${isLoading ? 'opacity-70 cursor-not-allowed' : ''}`}
                                        disabled={isLoading}
                                    >
                                        Cancel
                                    </button>
                                </div>
                            </form>
                            <div className="mt-4 text-center">
                                <p className="text-sm text-gray-600">
                                    Don't have an account?
                                    <button
                                        className="font-medium text-blue-600 hover:text-blue-500 ml-1"
                                        onClick={this.toggleLoginModal}
                                        disabled={isLoading}
                                    >
                                        Sign up
                                    </button>
                                </p>
                            </div>
                        </div>
                    </div>
                )}

                {/* Two-Factor Authentication Modal */}
                {showTwoFactorModal && (
                    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                        <div className="bg-white p-8 rounded-lg shadow-xl max-w-md w-full relative">
                            <h2 className="text-2xl font-bold text-center mb-6">Two-Factor Authentication</h2>

                            <p className="mb-4 text-center text-gray-600">
                                Please enter the verification code sent to your device.
                            </p>

                            {/* Error Message in 2FA Modal */}
                            {errorMsg && (
                                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4">
                                    {errorMsg}
                                </div>
                            )}

                            <form onSubmit={this.handleTwoFactorSubmit} className="space-y-6">
                                <div>
                                    <label htmlFor="verificationCode" className="block text-sm font-medium text-gray-700">Verification Code</label>
                                    <input
                                        value={this.state.twoFactorCode}
                                        onChange={this.changeTwoFactorCodeHandler}
                                        id="verificationCode"
                                        name="code"
                                        type="text"
                                        required
                                        className="mt-1 appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                                        placeholder="Enter code"
                                        disabled={isLoading}
                                    />
                                </div>
                                <div>
                                    <button
                                        type="submit"
                                        className={`w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 ${isLoading ? 'opacity-70 cursor-not-allowed' : ''}`}
                                        disabled={isLoading}
                                    >
                                        {isLoading ? 'Verifying...' : 'Verify'}
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}

                {/* Footer */}
                <Footer />
            </div>
        );
    }
}

// Wrapper component to use hooks with class component
const SignupPageWithHooks = (props) => {
    const navigate = useNavigate();

    return <SignupPage {...props} navigate={navigate} />;
};

export default SignupPageWithHooks;