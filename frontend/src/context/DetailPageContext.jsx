import {useCallback, useEffect, useState} from 'react';
import {getProductById} from "../api/products";
import {useParams} from "react-router-dom";

const DetailPageContext = () => {
    const { productID } = useParams();
    const [product, setProduct] = useState(null);
    const [isLoading, setLoading] = useState(true);
    const [maxQuantity, setMaxQuantity] = useState(1);

    useEffect(() => {
        getProductById(productID)
            .then((res) => {
                setProduct(res.data);
                setLoading(false);
            })
            .catch((err) => {
                console.error('Error loading categories', err);
                setLoading(false);
            });
        //getQuantityByIdd(productID)
        setMaxQuantity(5);
    }, [productID]);

    return { product, isLoading, maxQuantity};
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