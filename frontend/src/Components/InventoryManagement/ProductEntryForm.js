<<<<<<< HEAD
import React, { useState } from 'react';
import './ProductEntryForm.css';
=======
import React, { useState, useEffect } from 'react';
import './ProductEntryForm.css';
import axiosInstance from '../../utils/axiosConfig'; // Import the configured axios instance
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589

const ProductEntryForm = () => {
    const [formData, setFormData] = useState({
        productName: '',
<<<<<<< HEAD
        productCategory: '',
        manufacturer: '',
        expiryDate: '',
        quantityReceived: '',
        storageLocation: '',
        supplierName: '',
        purchaseOrderNumber: '',
        dateOfEntry: '',
        enteredBy: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Product Entry form data submitted:', formData);
        // TODO: Implement form submission logic
=======
        category: '',
        date: '',
        quantity: '',
        location: '',
        supplier: '',
        poNumber: '',
        expiryDate: '',
        enteredBy: '',
    });

    const [products, setProducts] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    const API_BASE_URL = '/api/products'; // Use relative path as axiosInstance has baseURL configured

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await axiosInstance.get(API_BASE_URL);
            console.log('Product API response:', response.data);
            setProducts(response.data);
        } catch (err) {
            console.error('Error fetching products:', err);
            setError('Failed to fetch products.');
            toast.error('Failed to load products.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const productRequest = {
            name: formData.productName,
            productType: formData.category,
            dateOfRegistration: formData.date,
            quantity: parseInt(formData.quantity), // Ensure quantity is an integer
            location: formData.location,
            manufacturer: formData.supplier,
            batchNumber: formData.poNumber,
            expiryDate: formData.expiryDate,
            registeredBy: formData.enteredBy,
            // Other fields like description, activeIngredients, etc., are not in this form
            // You might need to add default values or update the form to include them
        };

        const form = new FormData();
        for (const key in productRequest) {
            if (productRequest[key] !== null && productRequest[key] !== undefined) {
                form.append(key, productRequest[key]);
            }
        }

        try {
            const response = await axiosInstance.post(API_BASE_URL, form, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            toast.success('Product entry created successfully!');
            console.log('Product entry created:', response.data);
            setFormData({
                productName: '',
                category: '',
                date: '',
                quantity: '',
                location: '',
                supplier: '',
                poNumber: '',
                expiryDate: '',
                enteredBy: '',
            });
            fetchProducts(); // Refresh the list after submission
        } catch (err) {
            console.error('Error creating product entry:', err);
            toast.error('Failed to create product entry.');
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this product entry?')) {
            try {
                await axiosInstance.delete(`${API_BASE_URL}/${id}`);
                toast.success('Product entry deleted successfully!');
                fetchProducts(); // Refresh the list after deletion
            } catch (err) {
                console.error('Error deleting product entry:', err);
                toast.error('Failed to delete product entry.');
            }
        }
    };

    const handleCancel = () => {
        console.log('Form cancelled');
        setFormData({
            productName: '',
            category: '',
            date: '',
            quantity: '',
            location: '',
            supplier: '',
            poNumber: '',
            expiryDate: '',
            enteredBy: '',
        });
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
    };

    return (
        <div className="product-entry-form-container">
            <form className="product-entry-form" onSubmit={handleSubmit}>
<<<<<<< HEAD
                <div className="form-group">
                    <input type="text" id="productName" name="productName" value={formData.productName} onChange={handleChange} required placeholder="Enter Product Name" />
                </div>
                <div className="form-group">
                    <input type="text" id="productCategory" name="productCategory" value={formData.productCategory} onChange={handleChange} required placeholder="Enter Product Category" />
                </div>
                <div className="form-group">
                    <input type="text" id="manufacturer" name="manufacturer" value={formData.manufacturer} onChange={handleChange} required placeholder="Enter Manufacturer" />
                </div>
                <div className="form-group">
                    <input type="date" id="expiryDate" name="expiryDate" value={formData.expiryDate} onChange={handleChange} required placeholder="Select Expiry Date" />
                </div>
                 <div className="form-group">
                    <input type="number" id="quantityReceived" name="quantityReceived" value={formData.quantityReceived} onChange={handleChange} required placeholder="Enter Quantity" />
                </div>
                <div className="form-group">
                    <input type="text" id="storageLocation" name="storageLocation" value={formData.storageLocation} onChange={handleChange} required placeholder="Enter Storage Location" />
                </div>
                <div className="form-group">
                    <input type="text" id="supplierName" name="supplierName" value={formData.supplierName} onChange={handleChange} required placeholder="Enter Supplier Name" />
                </div>
                <div className="form-group">
                    <input type="text" id="purchaseOrderNumber" name="purchaseOrderNumber" value={formData.purchaseOrderNumber} onChange={handleChange} required placeholder="Enter PO Number" />
                </div>
                <div className="form-group">
                    <input type="date" id="dateOfEntry" name="dateOfEntry" value={formData.dateOfEntry} onChange={handleChange} required placeholder="Select Date of Entry" />
                </div>
                <div className="form-group">
                    <input type="text" id="enteredBy" name="enteredBy" value={formData.enteredBy} onChange={handleChange} required placeholder="Enter Your Name" />
                </div>
                <div className="form-buttons">
                    <button type="submit">Submit</button>
                    <button type="button" className="cancel-button">Cancel</button>
                </div>
            </form>
=======
                <h2>Product Entry</h2>
                <div className="form-group">
                    <input type="text" id="productName" name="productName" value={formData.productName} onChange={handleChange} required placeholder="Product Name" />
                </div>
                <div className="form-group">
                    <input type="text" id="category" name="category" value={formData.category} onChange={handleChange} required placeholder="Category" />
                </div>
                <div className="form-group">
                    <input type="date" id="date" name="date" value={formData.date} onChange={handleChange} required placeholder="Date" />
                </div>
                 <div className="form-group">
                    <input type="number" id="quantity" name="quantity" value={formData.quantity} onChange={handleChange} required placeholder="Quantity" />
                </div>
                <div className="form-group">
                    <input type="text" id="location" name="location" value={formData.location} onChange={handleChange} required placeholder="Location" />
                </div>
                <div className="form-group">
                    <input type="text" id="supplier" name="supplier" value={formData.supplier} onChange={handleChange} required placeholder="Supplier" />
                </div>
                <div className="form-group">
                    <input type="text" id="poNumber" name="poNumber" value={formData.poNumber} onChange={handleChange} required placeholder="PO Number" />
                </div>
                <div className="form-group">
                    <input type="date" id="expiryDate" name="expiryDate" value={formData.expiryDate} onChange={handleChange} required placeholder="Expiry Date" />
                </div>
                <div className="form-group">
                    <input type="text" id="enteredBy" name="enteredBy" value={formData.enteredBy} onChange={handleChange} required placeholder="Entered By" />
                </div>
                <div className="form-buttons">
                    <button type="submit">Submit</button>
                    <button type="button" className="cancel-button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>
            
            <div className="product-list-container">
                <h2>Existing Product Entries</h2>
                {isLoading ? (
                    <p>Loading products...</p>
                ) : error ? (
                    <p className="error-message">{error}</p>
                ) : products.length === 0 ? (
                    <p>No product entries found.</p>
                ) : (
                    <table className="product-list-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Product Name</th>
                                <th>Category</th>
                                <th>Date</th>
                                <th>Quantity</th>
                                <th>Location</th>
                                <th>Supplier</th>
                                <th>PO Number</th>
                                <th>Expiry Date</th>
                                <th>Entered By</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {products.map((product) => (
                                <tr key={product.id}>
                                    <td>{product.id}</td>
                                    <td>{product.name}</td>
                                    <td>{product.productType}</td>
                                    <td>{product.dateOfRegistration}</td>
                                    <td>{product.stockQuantity}</td>
                                    <td>{product.location}</td>
                                    <td>{product.manufacturer}</td>
                                    <td>{product.batchNumber}</td>
                                    <td>{product.expiryDate}</td>
                                    <td>{product.registeredBy}</td>
                                    <td>
                                        <button onClick={() => handleDelete(product.id)} className="delete-button">Delete</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </div>
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
        </div>
    );
};

export default ProductEntryForm; 