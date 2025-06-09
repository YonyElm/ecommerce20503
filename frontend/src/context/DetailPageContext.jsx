import {useCallback, useEffect, useState} from 'react';
import {getProductDetailsById} from "../api/products";
import {useParams} from "react-router-dom";

const DetailPageContext = () => {
    const { productID } = useParams();
    const [product, setProduct] = useState(null);
    const [isLoading, setLoading] = useState(true);

    useEffect(() => {
        getProductDetailsById(productID)
            .then((res) => {
                setProduct(res.data);
                setLoading(false);
            })
            .catch((err) => {
                console.error('Error loading categories', err);
                setLoading(false);
            });
    }, [productID]);

    return { product, isLoading};
};

const ChosenQuantityContext = () => {
    const [quantity, setQuantity] = useState(1);

    const handleQuantityChange = useCallback((e) => {
        const val = Math.max(1, parseInt(e.target.value) || 1);
        setQuantity(val);
    }, []);

    return [quantity, handleQuantityChange];
};


export {DetailPageContext, ChosenQuantityContext};