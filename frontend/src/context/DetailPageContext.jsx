import { useCallback, useContext, useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getProductDetailsById } from "../api/products";
import { AuthContext } from "./AuthContext";
import { CartContext } from "./CartContext";

const DetailPageContext = () => {
    const { productId } = useParams();
    const [product, setProduct] = useState(null);
    const [isLoading, setLoading] = useState(true);

    // Quantity state and handler
    const [chosenQuantity, setChosenQuantity] = useState(1);
    const handleChosenQuantityChange = useCallback((e) => {
        const val = Math.max(1, parseInt(e.target.value, 10) || 1);
        setChosenQuantity(val);
    }, []);

    // Auth and cart context
    const { user: isSignedIn } = useContext(AuthContext);
    const { cartItems, addItem, updateQuantity } = useContext(CartContext);
    const navigate = useNavigate();

    // Fetch product info
    useEffect(() => {
        setLoading(true);
        getProductDetailsById(productId)
          .then((res) => setProduct(res.data))
          .catch((err) => {
              console.error("Error loading product details", err);
              setProduct(null);
          })
          .finally(() => setLoading(false));
    }, [productId]);

    // Computed fields and logic
    const inStock = useMemo(() => product?.maxQuantity > 0, [product]);
    const categoryName = useMemo(() => product?.categoryName || "N/A", [product]);

    const addToCart = useCallback(() => {
        if (!product) return;
        if (cartItems.some((item) => item.productId === product.id)) {
            updateQuantity(product.id, chosenQuantity, true);
        } else {
            let item = {
                productId: product.id,
                name: product.name,
                price: product.price,
                quantity: chosenQuantity,
            };
            addItem(item);
        }
        alert(`Added ${chosenQuantity} ${product.name}(s) to cart!`);
    }, [cartItems, product, chosenQuantity, addItem, updateQuantity]);

    const buyNow = useCallback(() => {
        if (!product) return;
        navigate(`/checkout/${product.id}?quantity=${chosenQuantity}&price=${product.price}`);
    }, [product, chosenQuantity, navigate]);

    return {
        product,
        isLoading,
        chosenQuantity,
        handleChosenQuantityChange,
        isSignedIn,
        inStock,
        categoryName,
        addToCart,
        buyNow,
    };
};

export {DetailPageContext};