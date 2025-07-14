import React, {createContext, useContext, useEffect, useState} from "react";
import {getCart, removeCartItem, updateCartItem} from "../api/cart";
import {AuthContext} from "./AuthContext";
import {placeCartOrder, placeBuyItNowOrder} from "../api/checkout";

export const CartContext = createContext();

export const CartProvider = ({ children }) => {
    const { user } = useContext(AuthContext);

    // Initialize from localStorage if available (for guests), otherwise empty array
    const [cartItems, setCartItems] = useState(() => {
        const stored = localStorage.getItem("cartItems");
        return stored ? JSON.parse(stored) : [];
    });
    const [isLoading, setIsLoading] = useState(true);

    // Sync to localStorage whenever cartItems changes, and user is not logged in
    useEffect(() => {
        if (!user || !user.sub) {
            localStorage.setItem("cartItems", JSON.stringify(cartItems));
        }
    }, [cartItems, user]);

    const submitPlaceOrderForm = async (e, selectedAddressId, selectedPaymentId, productId, totalItems, navigate) => {
        e.preventDefault();
        setIsLoading(true);
        try {
            // If productId is provided, it means this is a "Buy Now" action
            if (productId) {
                await placeBuyItNowOrder(user.sub, selectedAddressId, selectedPaymentId, productId, totalItems);
            } else {
                await placeCartOrder(user.sub, selectedAddressId, selectedPaymentId);
            }
            navigate("/orders");
        } catch (err) {
            console.error("Failed placing cart order", err);
        } finally {
            setIsLoading(false);
        }
    };

    // Load cart from API or local storage at (re)mount or user change
    useEffect(() => {

        const shouldLoadCart =
        (user && user.sub) || !isLoading;

        if (shouldLoadCart) {
            if (user && user.sub) {
                getCart(user.sub)
                    .then((res) => setCartItems(res.data))
                    .catch((err) => {
                        console.error("Failed to load cart", err);
                        setCartItems([]);
                    });
            } else {
                setCartItems( []);
            }
        }
    }, [user, isLoading]);

    const removeItem = (cartItemId) => {
        setCartItems((items) => {
            if (user && user.sub) {
                removeCartItem(cartItemId)
                    .then((res) => console.log("Updated cart", res))
                    .catch((err) => {
                        console.error("Failed to update cart in DB", err);
                    });

                return items.filter((item) => item.itemId !== cartItemId)
            } else {
                return [];
            }
        });
    }

    const updateQuantity = (productId, qty, addition) => {
        setCartItems((items) => {

            if (user && user.sub) {
                return items.map((item) => {
                    if (item.productId === productId) {
                        // TBD: Cap it from the top with MaxInventory
                        let quantity = addition? item.quantity + qty : qty;
                        updateCartItem(user.sub, productId, quantity)
                            .then((res) => console.log("Updated cart", res))
                            .catch((err) => {
                                console.error("Failed to update cart in DB", err);
                            });
                        return { ...item, quantity: quantity};
                    } else {
                        return item;
                    }
                })

            } else {
                return [];
            }
        });
    };

    // Add item to cart, or update quantity if it already exists
    // At this point itemId might not exist yet, it is important to read from DB before going to cart-page
    const addItem = (item) => {
        setCartItems((prevItems) => {
            if (user && user.sub) {
                setIsLoading(true);
                updateCartItem(user.sub, item.productId, item.quantity)
                    .then((res) => console.log("Updated cart", res))
                    .catch((err) => {
                        console.error("Failed to update cart in DB", err);
                    }).finally(() => {
                        setIsLoading(false);
                    });

                // Replace cartItem with a new one to trigger render
                return prevItems.concat([item]);
            } else {
                return [];
            }
        });
    };

    return (
        <CartContext.Provider value={{ cartItems, removeItem, updateQuantity, addItem, submitPlaceOrderForm }}>
            {children}
        </CartContext.Provider>
    );
};