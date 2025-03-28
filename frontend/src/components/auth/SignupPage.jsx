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
            twoFactorPhoneNumber: '',

            // Available roles
            roles: [
                { value: "ROLE_ADMIN", label: "Admin" },
                { value: "ROLE_AGRONOMIST", label: "Agronomist (Farm Manager)" },
                { value: "ROLE_STOREKEEPER", label: "Storekeeper" },
                { value: "ROLE_MANUFACTURER", label: "Manufacturer" },
                { value: "ROLE_FIELD_WORKER", label: "Field Worker" }
            ],

            // Login modal fields
            loginUsername: "",
            loginPassword: "",
            loginEmail: "", // Keep this for compatibility if needed
            enableTwoFactor: false,

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
            },

            // Enhanced 2FA state
            twoFactorSetup: {
                step: 'initial', // 'initial', 'phone_verification', 'code_verification', 'complete'
                phoneNumber: '',
                verificationCode: '',
                isVerified: false
            }
        };

        // Binding all methods to 'this' context
        this.changeFullNameHandler = this.changeFullNameHandler.bind(this);
        this.changePhoneNumberHandler = this.changePhoneNumberHandler.bind(this);
        this.changeEmailHandler = this.changeEmailHandler.bind(this);
        this.changeUsernameHandler = this.changeUsernameHandler.bind(this);
        this.changePasswordHandler = this.changePasswordHandler.bind(this);
        this.changeConfirmPasswordHandler = this.changeConfirmPasswordHandler.bind(this);

        // Login and authentication related handlers
        this.changeLoginEmailHandler = this.changeLoginEmailHandler.bind(this);
        this.changeLoginPasswordHandler = this.changeLoginPasswordHandler.bind(this);
        this.changeLoginUsernameHandler = this.changeLoginUsernameHandler.bind(this);
        this.changeTwoFactorCodeHandler = this.changeTwoFactorCodeHandler.bind(this);


        // Other modal and form submission handlers
        this.handleEnableTwoFactorChange = this.handleEnableTwoFactorChange.bind(this);
        this.validateField = this.validateField.bind(this);
        this.handleDropdownToggle = this.handleDropdownToggle.bind(this);
        this.toggleLoginModal = this.toggleLoginModal.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleLoginSubmit = this.handleLoginSubmit.bind(this);
        this.handleTwoFactorSubmit = this.handleTwoFactorSubmit.bind(this);
        this.createAccount = this.createAccount.bind(this);
        this.requestTwoFactorCode = this.requestTwoFactorCode.bind(this);
        this.changeTwoFactorPhoneNumberHandler = this.changeTwoFactorPhoneNumberHandler.bind(this);
        this.changeRoleHandler = this.changeRoleHandler.bind(this);
    }

    // Enhanced 2FA setup method
    setup2FA = async () => {
        const { loginUsername } = this.state;

        if (!loginUsername.trim()) {
            this.setState({
                errorMsg: "Please enter a username to enable 2FA"
            });
            return;
        }

        this.setState({ isLoading: true, errorMsg: "" });

        try {
            // 1. Enable 2FA for user
            await UserAccount.toggleTwoFactor(loginUsername, true);
            
            // 2. Move to phone verification
            this.setState({
                twoFactorSetup: {
                    ...this.state.twoFactorSetup,
                    step: 'phone_verification'
                },
                successMsg: "2FA enabled. Please verify your phone number.",
                isLoading: false
            });
        } catch (error) {
            this.setState({
                errorMsg: error.message || "Error enabling 2FA",
                isLoading: false
            });
        }
    }

    // Handle phone number verification for 2FA
    handlePhoneVerification = async (e) => {
        e.preventDefault();
        const { twoFactorPhoneNumber } = this.state;

        if (!twoFactorPhoneNumber || twoFactorPhoneNumber.length !== 10) {
            this.setState({
                errorMsg: "Please enter a valid 10-digit phone number"
            });
            return;
        }

        this.setState({ isLoading: true, errorMsg: "" });

        try {
            // Request verification code
            const response = await UserAccount.requestTwoFactorCode(twoFactorPhoneNumber);
            
            this.setState(prevState => ({
                twoFactorSetup: {
                    ...prevState.twoFactorSetup,
                    step: 'code_verification',
                    phoneNumber: twoFactorPhoneNumber
                },
                successMsg: "Verification code sent to your phone.",
                isLoading: false
            }));
        } catch (error) {
            this.setState({
                errorMsg: error.message || "Failed to send verification code",
                isLoading: false
            });
        }
    }

    // Handle verification code submission
    handleCodeVerification = async (e) => {
        e.preventDefault();
        const { twoFactorCode, loginUsername } = this.state;

        if (!twoFactorCode) {
            this.setState({
                errorMsg: "Please enter the verification code"
            });
            return;
        }

        this.setState({ isLoading: true, errorMsg: "" });

        try {
            await UserAccount.verifyTwoFactorCode(twoFactorCode, loginUsername);
            
            this.setState(prevState => ({
                twoFactorSetup: {
                    ...prevState.twoFactorSetup,
                    step: 'complete',
                    isVerified: true
                },
                successMsg: "2FA has been successfully set up!",
                show2FASetupForm: false,
                showLoginFields: true,
                isLoading: false
            }));

            // Optionally redirect to login or dashboard
            setTimeout(() => {
                this.props.navigate('/login');
            }, 2000);
        } catch (error) {
            this.setState({
                errorMsg: error.message || "Failed to verify code.",
                isLoading: false
            });
        }
    }

    // Render 2FA setup form based on current step
    render2FASetupForm = () => {
        const { twoFactorSetup, isLoading, twoFactorPhoneNumber, twoFactorCode } = this.state;

        switch (twoFactorSetup.step) {
            case 'phone_verification':
                return (
                    <div className="p-4 bg-white rounded-lg shadow">
                        <h3 className="text-lg font-medium mb-4">Verify Your Phone Number</h3>
                        <form onSubmit={this.handlePhoneVerification}>
                            <div className="mb-4">
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Phone Number
                                </label>
                                <input
                                    type="tel"
                                    value={twoFactorPhoneNumber}
                                    onChange={this.changeTwoFactorPhoneNumberHandler}
                                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-blue-500"
                                    placeholder="Enter 10-digit phone number"
                                    maxLength="10"
                                    required
                                />
                            </div>
                            <button
                                type="submit"
                                disabled={isLoading || twoFactorPhoneNumber.length !== 10}
                                className={`w-full py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white 
                                    ${isLoading || twoFactorPhoneNumber.length !== 10 
                                        ? 'bg-gray-400 cursor-not-allowed' 
                                        : 'bg-blue-600 hover:bg-blue-700'}`}
                            >
                                {isLoading ? 'Sending...' : 'Send Verification Code'}
                            </button>
                        </form>
                    </div>
                );

            case 'code_verification':
                return (
                    <div className="p-4 bg-white rounded-lg shadow">
                        <h3 className="text-lg font-medium mb-4">Enter Verification Code</h3>
                        <form onSubmit={this.handleCodeVerification}>
                            <div className="mb-4">
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Verification Code
                                </label>
                                <input
                                    type="text"
                                    value={twoFactorCode}
                                    onChange={this.changeTwoFactorCodeHandler}
                                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-blue-500"
                                    placeholder="Enter verification code"
                                    required
                                />
                            </div>
                            <button
                                type="submit"
                                disabled={isLoading || !twoFactorCode}
                                className={`w-full py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white 
                                    ${isLoading || !twoFactorCode 
                                        ? 'bg-gray-400 cursor-not-allowed' 
                                        : 'bg-blue-600 hover:bg-blue-700'}`}
                            >
                                {isLoading ? 'Verifying...' : 'Verify Code'}
                            </button>
                        </form>
                    </div>
                );

            default:
                return (
                    <div className="p-4 bg-white rounded-lg shadow">
                        <h3 className="text-lg font-medium mb-4">Enable Two-Factor Authentication</h3>
                        <p className="text-sm text-gray-600 mb-4">
                            Enhance your account security by enabling two-factor authentication.
                        </p>
                        <button
                            onClick={this.setup2FA}
                            disabled={isLoading}
                            className={`w-full py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white 
                                ${isLoading ? 'bg-gray-400 cursor-not-allowed' : 'bg-blue-600 hover:bg-blue-700'}`}
                        >
                            {isLoading ? 'Setting up...' : 'Set up 2FA'}
                        </button>
                    </div>
                );
        }
    }

    // Define these methods outside of the constructor to avoid duplicate method warnings
    changeLoginUsernameHandler(event) {
        this.setState({ loginUsername: event.target.value });
    }


    changeLoginPasswordHandler(event) {
        this.setState({ loginPassword: event.target.value });
    }

    handleEnableTwoFactorChange = (event) => {
        const isChecked = event.target.checked;
        this.setState({
            enableTwoFactor: isChecked,
            // Show the 2FA setup form when checked
            show2FASetupForm: isChecked,
            // Hide the login password field when showing 2FA setup
            showLoginPassword: !isChecked,
            // Clear error message when toggling
            errorMsg: ""
        });
    }

    changeTwoFactorPhoneNumberHandler = (event) => {
        // Allow only numbers
        const phoneNumber = event.target.value.replace(/\D/g, '');
        this.setState({
            twoFactorPhoneNumber: phoneNumber
        });
    }


    createAccount = async (e) => {
        e.preventDefault();
        
        if (this.state.password !== this.state.confirmPassword) {
            this.setState({
                errorMsg: "Passwords do not match"
            });
            return;
        }

        const account = {
            username: this.state.username,
            password: this.state.password,
            confirmPassword: this.state.confirmPassword,
            email: this.state.email,
            fullName: this.state.fullName,
            phoneNumber: this.state.phoneNumber,
            role: this.state.role
        };

        this.setState({ errorMsg: "", isLoading: true });

        try {
            // 1. Create account
            const response = await UserAccount.createAccount(account);
            
            // 2. Enable account if needed
            if (response.userId) {
                await UserAccount.enableAccount(response.userId);
            }

            this.setState({
                successMsg: "Account created successfully! Please login to continue.",
                isLoading: false
            });

            // Show login modal after delay
            setTimeout(() => {
                this.setState({
                    showLoginModal: true,
                    // Reset form
                    fullName: '',
                    phoneNumber: '',
                    email: '',
                    username: '',
                    password: '',
                    confirmPassword: '',
                    role: 'ROLE_USER'
                });
            }, 2000);

        } catch (error) {
            this.setState({
                errorMsg: error.message || "Error creating account",
                isLoading: false
            });
        }
    };


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

    changeTwoFactorCodeHandler = (event) => {
        this.setState({ twoFactorCode: event.target.value });
    }

    validatePhoneNumber = () => {
        const { twoFactorPhoneNumber } = this.state;
        // Remove all non-digit characters and check if something remains
        const cleanedNumber = twoFactorPhoneNumber.replace(/[^\d]/g, '');
        return cleanedNumber.length > 0;
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

    // Add this method to your SignupPage class
    async requestTwoFactorCode(e) {
        e.preventDefault();
        this.setState({ isLoading: true, errorMsg: "", successMsg: "" });

        try {
            const phoneNumber = this.state.twoFactorPhoneNumber.replace(/[^\d]/g, '');
            const response = await UserAccount.requestTwoFactorCode(phoneNumber);

            this.setState({
                twoFactorUserId: response.userId,
                twoFactorCode: response.generatedCode,
                successMsg: `This is your Verification code: ${response.generatedCode}`,
                isLoading: false
            });
        } catch (error) {
            this.setState({
                errorMsg: error.message || "Failed to request verification code.",
                isLoading: false
            });
        }
    }

    async handleLoginSubmit(e) {
        e.preventDefault();
        this.setState({ isLoading: true, errorMsg: "" });

        try {
            // 1. Attempt login
            const response = await UserAccount.login(
                this.state.loginUsername,
                this.state.loginPassword
            );

            // 2. Handle 2FA if required
            if (response.requiresVerification) {
                this.setState({
                    twoFactorUserId: response.userId,
                    showLoginModal: false,
                    showTwoFactorModal: true,
                    isLoading: false
                });
                return;
            }

            // 3. Login successful
            this.setState({
                successMsg: "Login successful!",
                showLoginModal: false,
                loginUsername: '',
                loginPassword: '',
                isLoading: false
            });

            // 4. Redirect to dashboard
            this.props.navigate('/dashboard');

        } catch (error) {
            this.setState({
                errorMsg: error.message || "Login failed",
                isLoading: false
            });
        }
    }

    async handleTwoFactorSubmit(e) {
        e.preventDefault();
        const { twoFactorCode, loginUsername } = this.state;

        if (!twoFactorCode) {
            this.setState({
                errorMsg: "Please enter the verification code"
            });
            return;
        }

        this.setState({ isLoading: true, errorMsg: "" });

        try {
            // Verify 2FA code
            await UserAccount.verifyTwoFactorCode(twoFactorCode, loginUsername);
            
            this.setState({
                successMsg: "Verification successful!",
                showTwoFactorModal: false,
                isLoading: false
            });

            // Redirect to dashboard
            this.props.navigate('/dashboard');
        } catch (error) {
            this.setState({
                errorMsg: error.message || "Verification failed",
                isLoading: false
            });
        }
    }

    // Add role change handler
    changeRoleHandler = (event) => {
        this.setState({ role: event.target.value });
    }

    render() {
        const {
            fullName, phoneNumber, email, username, password, confirmPassword, role, roles,
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
                                <label htmlFor="role" className="block text-sm font-medium text-gray-700 mb-1">Select Role</label>
                                <select
                                    value={role}
                                    onChange={this.changeRoleHandler}
                                    id="role"
                                    name="role"
                                    required
                                    className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                                    disabled={isLoading}
                                >
                                    {roles.map((roleOption) => (
                                        <option key={roleOption.value} value={roleOption.value}>
                                            {roleOption.label}
                                        </option>
                                    ))}
                                </select>
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

                            {/* Login Form Content */}
                            <form onSubmit={this.handleLoginSubmit} className="space-y-6">
                                {/* 2FA Setup Form - shown only when enableTwoFactor is true */}
                                {this.state.show2FASetupForm ? (
                                    <div className="mt-4">
                                        {this.render2FASetupForm()}
                                    </div>
                                ) : (
                                    <>
                                        <div>
                                            <label htmlFor="loginUsername" className="block text-sm font-medium text-gray-700">Username</label>
                                            <input
                                                value={this.state.loginUsername}
                                                onChange={this.changeLoginUsernameHandler}
                                                id="loginUsername"
                                                name="username"
                                                type="text"
                                                required
                                                className="mt-1 appearance-none rounded-md relative block w-full px-3 py-2 border border-blue-500 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                                                placeholder="Username"
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
                                                    id="enable-2fa"
                                                    name="enable-2fa"
                                                    type="checkbox"
                                                    checked={this.state.enableTwoFactor}
                                                    onChange={this.handleEnableTwoFactorChange}
                                                    className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                                                />
                                                <label htmlFor="enable-2fa" className="ml-2 block text-sm text-gray-900">
                                                    Enable 2FA
                                                </label>
                                            </div>
                                            {!this.state.show2FASetupForm && (
                                                <div className="text-sm">
                                                    <a href="#" className="font-medium text-blue-600 hover:text-blue-500">
                                                        Forgot your password?
                                                    </a>
                                                </div>
                                            )}
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
                                    </>
                                )}
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

                            {/* Success Message */}
                            {successMsg && (
                                <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative mb-4">
                                    {successMsg}
                                </div>
                            )}

                            {/* Error Message in 2FA Modal */}
                            {errorMsg && (
                                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4">
                                    {errorMsg}
                                </div>
                            )}

                            {/* Phone Number Input for 2FA */}
                            {!this.state.twoFactorCode && (
                                <form onSubmit={this.requestTwoFactorCode} className="space-y-6">
                                    <div>
                                        <label htmlFor="phoneNumberVerification" className="block text-sm font-medium text-gray-700">
                                            Phone Number for Verification
                                        </label>
                                        <input
                                            value={this.state.twoFactorPhoneNumber}
                                            onChange={this.changeTwoFactorPhoneNumberHandler}
                                            id="phoneNumberVerification"
                                            name="phoneNumber"
                                            type="tel"
                                            maxLength="10"
                                            required
                                            className="mt-1 appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                                            placeholder="Enter 10-digit phone number"
                                        />
                                        {this.state.twoFactorPhoneNumber.length > 0 && this.state.twoFactorPhoneNumber.length < 10 && (
                                            <p className="mt-1 text-xs text-red-500">
                                                Phone number must be 10 digits
                                            </p>
                                        )}
                                    </div>
                                    <div>
                                        <button
                                            type="submit"
                                            className={`w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white ${this.state.twoFactorPhoneNumber.length === 10
                                                ? 'bg-blue-600 hover:bg-blue-700'
                                                : 'bg-gray-400 cursor-not-allowed'
                                                } focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500`}
                                            disabled={this.state.twoFactorPhoneNumber.length !== 10}
                                        >
                                            Request Verification Code
                                        </button>
                                    </div>
                                </form>
                            )}

                            {/* Verification Code Input (shown after code is requested) */}
                            {this.state.twoFactorCode && (
                                <form onSubmit={this.handleTwoFactorSubmit} className="space-y-6 mt-6">
                                    <div className="mb-4">
                                        <label htmlFor="verificationCode" className="block text-sm font-medium text-gray-700">
                                            Verification Code
                                        </label>
                                        <input
                                            value={this.state.twoFactorCode}
                                            onChange={this.changeTwoFactorCodeHandler}
                                            id="verificationCode"
                                            name="code"
                                            type="text"
                                            required
                                            className="mt-1 appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                                            placeholder="Enter verification code"
                                            disabled={isLoading}
                                        />
                                    </div>
                                    <div>
                                        <label htmlFor="verificationUsername" className="block text-sm font-medium text-gray-700">
                                            Username
                                        </label>
                                        <input
                                            value={this.state.loginUsername}
                                            onChange={this.changeLoginUsernameHandler}
                                            id="verificationUsername"
                                            name="username"
                                            type="text"
                                            required
                                            className="mt-1 appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                                            placeholder="Enter your username"
                                            disabled={isLoading}
                                        />
                                    </div>
                                    <div className="mt-4">
                                        <button
                                            type="submit"
                                            className={`w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 ${isLoading ? 'opacity-70 cursor-not-allowed' : ''}`}
                                            disabled={isLoading}
                                        >
                                            {isLoading ? 'Verifying...' : 'Verify Code'}
                                        </button>
                                    </div>
                                </form>
                            )}
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