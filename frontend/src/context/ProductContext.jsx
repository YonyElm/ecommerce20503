import { useEffect, useState } from 'react';
import {getProducts} from "../api/products";

const ProductContext = () => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);


    useEffect(() => {
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
    }, []);

    return { products, loading };
};

export default ProductContext;
