import React from "react";
import { Link } from "react-router-dom";
import "./ProductCard.css";

// datas to get from props
// productID, name, categoryID, image_url, createdAt

const ProductCard = ({ product }) => {
    return (
        <div className="product-card">
<<<<<<< HEAD
        <Link to={`/product/${product.productID}`}>
=======
        <Link to={`/markets/products?BatchNumber=${product.batchNumber}`}>
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
            <img src={product.image_url} alt={product.name} />
            <div className="product-card-details">
            <h3>{product.name}</h3>
            <p>{product.categoryID}</p>
            <p>{product.createdAt}</p>
            </div>
        </Link>
        </div>
    );
    }

export default ProductCard;


