import { useEffect, useState } from 'react';
import {getProducts, getProductsByCategory} from "../api/products";

const ProductContext = (selectedCategories = []) => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setLoading(true);
        // If no categories selected, get all products
        if (selectedCategories.length === 0) {
            getProducts()
                .then(res => {
                    if (res && res.data != null) {
                        setProducts(res.data);
                        setLoading(false);
                    }
                })
                .catch((err) => {
                    console.error('Error loading products', err);
                    setLoading(false);
                });
        } else {
            // If category is selected, get products for that category
            const categoryId = selectedCategories[0]; // Since we only allow one category now
            getProductsByCategory(categoryId)
                .then(res => {
                    if (res && res.data != null) {
                        setProducts(res.data);
                        setLoading(false);
                    }
                })
                .catch((err) => {
                    console.error('Error loading products by category', err);
                    setLoading(false);
                });
        }
    }, [selectedCategories]);

    return { products, loading };
};

export default ProductContext;
