<<<<<<< HEAD
import React, { useState } from 'react';
import './ProductRegistrationForm.css';
=======
import React, { useState, useEffect, useCallback, useRef } from 'react';
import './ProductRegistrationForm.css';
import authService from '../../services/authService';
import { API_CONFIG } from '../../config';
import { toast } from 'react-toastify';
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589

const ProductRegistrationForm = () => {
    const [productName, setProductName] = useState('');
    const [productType, setProductType] = useState('');
    const [manufacturerName, setManufacturerName] = useState('');
    const [batchNumber, setBatchNumber] = useState('');
    const [activeIngredients, setActiveIngredients] = useState('');
    const [dateOfRegistration, setDateOfRegistration] = useState('');
    const [registeredBy, setRegisteredBy] = useState('');
    const [intendedUse, setIntendedUse] = useState('');
    const [cropTarget, setCropTarget] = useState('');
    const [comments, setComments] = useState('');
<<<<<<< HEAD

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            productName,
            productType,
            manufacturerName,
            batchNumber,
            activeIngredients,
            dateOfRegistration,
            registeredBy,
            intendedUse,
            cropTarget,
            comments
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
=======
    const [productImage, setProductImage] = useState(null);

    // State for validation errors
    const [productNameError, setProductNameError] = useState('');
    const [productTypeError, setProductTypeError] = useState('');
    const [manufacturerNameError, setManufacturerNameError] = useState('');
    const [intendedUseError, setIntendedUseError] = useState('');
    const [cropTargetError, setCropTargetError] = useState('');
    const [registeredByError, setRegisteredByError] = useState(''); // Error for Registered By

    // Ref for the file input to clear its value
    const fileInputRef = useRef(null);

    // State for managing the submit button's loading state
    const [isSubmitting, setIsSubmitting] = useState(false);
    // State for managing the 'None' checkbox for batch number
    const [isBatchNumberNone, setIsBatchNumberNone] = useState(false);

    // State for registeredByFullName
    const [registeredByFullName, setRegisteredByFullName] = useState('');

    // Function to reset the form fields
    const resetForm = () => {
        setProductName('');
        setProductType('');
        setManufacturerName('');
        setBatchNumber(''); // Batch number will be refetched by useEffect on re-render
        setActiveIngredients('');
        setDateOfRegistration('');
        setRegisteredBy('');
        setIntendedUse('');
        setCropTarget('');
        setComments('');
        setProductImage(null); // Clear the file input state
        // Also clear the file input element's value
        if (fileInputRef.current) {
            fileInputRef.current.value = '';
        }
        setIsBatchNumberNone(false); // Reset batch number none state

        // Clear validation errors
        setProductNameError('');
        setProductTypeError('');
        setManufacturerNameError('');
        setIntendedUseError('');
        setCropTargetError('');
        setRegisteredByError('');
    };

    // Validation function (only characters and spaces)
    const validateCharactersOnly = (value) => {
        // Allows letters, spaces, hyphens, and apostrophes
        return /^[a-zA-Z\s-']*$/.test(value);
    };

    // Validation function (only numbers)
    const validateNumbersOnly = (value) => {
        // Allows only digits (0-9)
        return /^\d+$/.test(value);
    };

    // Fetch and set the next batch number using useCallback
    const fetchNextBatchNumber = useCallback(async () => {
        console.log('Attempting to fetch next batch number...'); // Log to terminal
        const token = authService.getToken();
        if (!token) {
            console.error('No authentication token found. Cannot fetch batch number.'); // Log to terminal
            // Optionally show a toast for missing token
            toast.error('Authentication required to fetch batch number.');
            return;
        }

        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}/api/products/next-batch-number`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                cache: 'no-cache',
            });

            if (response.ok) {
                const nextBatchNumResponse = await response.text(); // Get as text
                console.log('Successfully fetched batch number response:', nextBatchNumResponse); // Log the raw response

                // Use the batch number as returned by the backend, do not increment or parse
                setBatchNumber(nextBatchNumResponse);
                console.log('Batch number state updated to:', nextBatchNumResponse); // Confirm state update
            } else {
                // Log detailed error response to terminal
                console.error(`Error fetching next batch number: Status - ${response.status}, StatusText - ${response.statusText}`);
                const errorBody = await response.text(); // Get error body as text for more info
                console.error('Error response body:', errorBody); // Log error body to terminal
                // Show an error toast
                toast.error(`Failed to fetch next batch number: ${response.statusText || 'Unknown Error'}`);
            }
        } catch (error) {
            // Log network error details to terminal
            console.error('Network error fetching next batch number:', error);
            // Show a network error toast
            toast.error('Network error fetching next batch number.');
        }
    }, [setBatchNumber]); // Add setBatchNumber to dependency array

    // Fetch and set the next batch number on component mount
    useEffect(() => {
        fetchNextBatchNumber();
    }, [fetchNextBatchNumber]); // Empty dependency array means this effect runs once on mount

    // Fetch user full name when registeredBy changes and is a valid number
    useEffect(() => {
        const fetchUserFullName = async () => {
            if (!registeredBy || isNaN(registeredBy)) {
                setRegisteredByFullName('');
                return;
            }
            try {
                const token = authService.getToken();
                const response = await fetch(`${API_CONFIG.BASE_URL}/api/users/${registeredBy}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (response.ok) {
                    const user = await response.json();
                    setRegisteredByFullName(user.fullName || '');
                } else {
                    setRegisteredByFullName('');
                }
            } catch (error) {
                setRegisteredByFullName('');
            }
        };
        fetchUserFullName();
    }, [registeredBy]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Perform validation before submitting
        let isValid = true;

        // Validation logic here is now redundant with onChange validation, but keeping for final check on submit
        if (!validateCharactersOnly(productName)) {
            setProductNameError('Product Name must contain only characters.');
            isValid = false;
        } else {
            setProductNameError('');
        }

        if (!validateCharactersOnly(productType)) {
            setProductTypeError('Product Type must contain only characters.');
            isValid = false;
        } else {
            setProductTypeError('');
        }

        if (!validateCharactersOnly(manufacturerName)) {
            setManufacturerNameError('Manufacturer Name must contain only characters.');
            isValid = false;
        } else {
            setManufacturerNameError('');
        }

        if (!validateCharactersOnly(intendedUse)) {
             setIntendedUseError('Intended Use must contain only characters.');
             isValid = false;
        } else {
             setIntendedUseError('');
        }

        if (!validateCharactersOnly(cropTarget)) {
             setCropTargetError('Crop Target must contain only characters.');
             isValid = false;
        } else {
             setCropTargetError('');
        }
        
        // Validation for Registered By (User ID) - only numbers
         if (!validateNumbersOnly(registeredBy)) {
             setRegisteredByError('Registered By (User ID) must contain only numbers.'); // Updated error message
             isValid = false;
         } else {
             setRegisteredByError('');
         }

        if (!isValid) {
            toast.error('Please fix the errors before submitting.');
            return; // Prevent submission if validation fails
        }

        setIsSubmitting(true); // Start submission animation/state
        
        const formData = new FormData();
        formData.append('name', productName);
        formData.append('productType', productType);
        formData.append('manufacturer', manufacturerName);

        // Append batch number based on the checkbox state
        if (isBatchNumberNone) {
            formData.append('batchNumber', 'None');
        } else {
            formData.append('batchNumber', batchNumber);
        }

        formData.append('activeIngredients', activeIngredients);
        formData.append('dateOfRegistration', dateOfRegistration);
        formData.append('registeredBy', registeredBy);
        formData.append('intendedUse', intendedUse);
        formData.append('cropTarget', cropTarget);
        formData.append('comments', comments);

        console.log('Product image state before appending:', productImage);
        
        if (productImage) {
            formData.append('productImage', productImage);
        } else {
            // This should ideally not happen if the 'required' attribute is enforced
            console.error('Product image is missing.');
            toast.error('Please select a product image.');
            setIsSubmitting(false); // Stop submission
            return; // Prevent sending the request
        }

        const token = authService.getToken();
        if (!token) {
            console.error('No authentication token found.');
            return;
        }

        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}/api/products`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                body: formData,
            });

            if (response.ok) {
                console.log('Product registration successful. Response received:', response);
                const result = await response.json();
                console.log('Product registered successfully:', result);
                toast.success('Product registered successfully!');
                resetForm(); // Reset the form after successful submission
                // Fetch the next batch number after successful registration
                fetchNextBatchNumber();
            } else {
                const error = await response.json();
                console.error('Error registering product:', error);
                
                // Check for duplicate batch number error specifically
                if (error.message && error.message.includes('Checking for existing product with batch number') && error.message.includes('Found: true')) {
                    toast.error('Error: Duplicate Batch number. Please try again.');
                } else {
                toast.error(`Error: ${error.message || 'Failed to register product'}`);
                }
            }
        } catch (error) {
            console.error('Network or other error:', error);
            toast.error('Network error. Please try again.');
        } finally {
            setIsSubmitting(false); // End submission animation/state
        }
    };

    const handleCancel = () => {
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
        console.log('Form cancelled');
    };

    return (
        <div className="product-registration-form-container">
            <form className="product-registration-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Product Name"
                        value={productName}
<<<<<<< HEAD
                        onChange={(e) => setProductName(e.target.value)}
                        required
                    />
=======
                        onChange={(e) => {
                            setProductName(e.target.value);
                            setProductNameError(validateCharactersOnly(e.target.value) ? '' : 'Product Name must contain only characters.');
                        }}
                        className={productNameError ? 'is-invalid' : (productName ? 'is-valid' : '')}
                        required
                    />
                     {productNameError && <div className="error-message">{productNameError}</div>}
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Product Type"
                        value={productType}
<<<<<<< HEAD
                        onChange={(e) => setProductType(e.target.value)}
                        required
                    />
=======
                        onChange={(e) => {
                            setProductType(e.target.value);
                            setProductTypeError(validateCharactersOnly(e.target.value) ? '' : 'Product Type must contain only characters.');
                        }}
                        className={productTypeError ? 'is-invalid' : (productType ? 'is-valid' : '')}
                        required
                    />
                     {productTypeError && <div className="error-message">{productTypeError}</div>}
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Manufacturer Name"
                        value={manufacturerName}
<<<<<<< HEAD
                        onChange={(e) => setManufacturerName(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Batch Number"
                        value={batchNumber}
                        onChange={(e) => setBatchNumber(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Active Ingredients"
                        value={activeIngredients}
                        onChange={(e) => setActiveIngredients(e.target.value)}
=======
                        onChange={(e) => {
                            setManufacturerName(e.target.value);
                            setManufacturerNameError(validateCharactersOnly(e.target.value) ? '' : 'Manufacturer Name must contain only characters.');
                        }}
                        className={manufacturerNameError ? 'is-invalid' : (manufacturerName ? 'is-valid' : '')}
                        required
                    />
                    {manufacturerNameError && <div className="error-message">{manufacturerNameError}</div>}
                </div>
                 <div className="form-row">
                     {/* Wrapper for Batch Number input and None checkbox */}
                     <label>Batch Number:</label>
                     <div className="batch-number-control-group">
                         {/* Conditionally render the batch number display or handle the input */}
                         {!isBatchNumberNone ? (
                             // Display batch number as text when 'None' is not checked
                             <div className="batch-number-display">{batchNumber}</div>
                         ) : (
                             // The input is technically not needed if we display as text, but keeping for clarity if future changes require it.
                             // Based on the user request to NOT display 'inside input', replacing with a div is better.
                             // We will not render an input here when 'None' is checked either, as the 'None' checkbox handles the input concept.
                             null // Do not render input when 'None' is checked
                         )}
                         <div className="batch-number-none-checkbox">
                             <input
                                 type="checkbox"
                                 id="batchNumberNone"
                                 checked={isBatchNumberNone}
                                 onChange={(e) => setIsBatchNumberNone(e.target.checked)}
                             />
                             <label htmlFor="batchNumberNone">None</label>
                         </div>
                     </div>
                 </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Active Size"
                        value={activeIngredients}
                        onChange={(e) => {
                            setActiveIngredients(e.target.value);
                        }}
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                        required
                    />
                </div>
                <div className="form-row">
                     <input
                        type="date"
                        placeholder="Date of Registration"
                        value={dateOfRegistration}
                        onChange={(e) => setDateOfRegistration(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                     <input
                        type="text"
                        placeholder="Registered By (User ID)"
                        value={registeredBy}
<<<<<<< HEAD
                        onChange={(e) => setRegisteredBy(e.target.value)}
                        required
                    />
=======
                        onChange={(e) => {
                            setRegisteredBy(e.target.value);
                            setRegisteredByError(validateNumbersOnly(e.target.value) ? '' : 'Registered By (User ID) must contain only numbers.');
                        }}
                        className={registeredByError ? 'is-invalid' : (registeredBy ? 'is-valid' : '')}
                        required
                    />
                     {registeredByError && <div className="error-message">{registeredByError}</div>}
                     {registeredByFullName && (
                        <div style={{ fontSize: '6px', color: '#333', marginTop: '2px' }}>{registeredByFullName}</div>
                     )}
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                </div>
                <div className="form-row">
                     <textarea
                        placeholder="Intended Use"
                        value={intendedUse}
<<<<<<< HEAD
                        onChange={(e) => setIntendedUse(e.target.value)}
                        required
                    />
=======
                        onChange={(e) => {
                            setIntendedUse(e.target.value);
                            setIntendedUseError(validateCharactersOnly(e.target.value) ? '' : 'Intended Use must contain only characters.');
                        }}
                        className={intendedUseError ? 'is-invalid' : (intendedUse ? 'is-valid' : '')}
                        required
                    />
                    {intendedUseError && <div className="error-message">{intendedUseError}</div>}
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                </div>
                <div className="form-row">
                     <input
                        type="text"
                        placeholder="Crop Target (potato, maize, etc.)"
                        value={cropTarget}
<<<<<<< HEAD
                        onChange={(e) => setCropTarget(e.target.value)}
                        required
                    />
=======
                        onChange={(e) => {
                            setCropTarget(e.target.value);
                            setCropTargetError(validateCharactersOnly(e.target.value) ? '' : 'Crop Target must contain only characters.');
                        }}
                        className={cropTargetError ? 'is-invalid' : (cropTarget ? 'is-valid' : '')}
                        required
                    />
                    {cropTargetError && <div className="error-message">{cropTargetError}</div>}
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                </div>
                <div className="form-row">
                     <textarea
                        placeholder="Comments"
                        value={comments}
                        onChange={(e) => setComments(e.target.value)}
                        required
                    />
                </div>
<<<<<<< HEAD

                <div className="button-row">
                    <button type="submit">Submit</button>
                    <button type="button" onClick={handleCancel}>Cancel</button>
=======
                <div className="form-row">
                    <label htmlFor="productImage">Product Image (JPG/PNG):</label>
                     <input
                        type="file"
                        id="productImage"
                        accept=".jpg,.png"
                        onChange={(e) => setProductImage(e.target.files[0])}
                        required
                        ref={fileInputRef} // Attach the ref to the file input
                    />
                </div>

                <div className="button-row">
                    <button type="submit" disabled={isSubmitting}>
                        {isSubmitting ? 'Submitting...' : 'Submit'}
                    </button>
                    <button type="button" onClick={handleCancel} disabled={isSubmitting}>
                        Cancel
                    </button>
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
                </div>
            </form>
        </div>
    );
};

export default ProductRegistrationForm; 