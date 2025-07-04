import React, {createContext, useContext, useEffect, useState} from "react";
import {getCart, removeCartItem, updateCartItem} from "../api/cart";
import {AuthContext} from "./AuthContext";

export const CartContext = createContext();

export const CartProvider = ({ children }) => {
    const { user } = useContext(AuthContext);

    // Initialize from localStorage if available (for guests), otherwise empty array
    const [cartItems, setCartItems] = useState(() => {
        const stored = localStorage.getItem("cartItems");
        return stored ? JSON.parse(stored) : [];
    });

    // Sync to localStorage whenever cartItems changes, and user is not logged in
    useEffect(() => {
        if (!user || !user.sub) {
            localStorage.setItem("cartItems", JSON.stringify(cartItems));
        }
    }, [cartItems, user]);

    // Load cart from API or local storage at (re)mount or user change
    useEffect(() => {
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
    }, [user]);

    const removeItem = (productId) => {
        setCartItems((items) => {
            if (user && user.sub) {
                removeCartItem(user.sub, productId)
                    .then((res) => console.log("Updated cart", res))
                    .catch((err) => {
                        console.error("Failed to update cart in DB", err);
                    });

                return items.filter((item) => item.id !== productId)
            } else {
                return [];
            }
        });
    }

    const updateQuantity = (id, qty, addition) => {
        setCartItems((items) => {

            if (user && user.sub) {
                updateCartItem(user.sub, id, qty)
                    .then((res) => console.log("Updated cart", res))
                    .catch((err) => {
                        console.error("Failed to update cart in DB", err);
                    });

                return items.map((item) => {
                    if (item.id === id) {
                        // TBD: Cap it from the top with MaxInventory
                        return { ...item, quantity: addition? item.quantity + qty : qty };
                    } else {
                        return item;
                    }
                })

            } else {
                return [];
            }
        });
    };

    const addItem = (item) => {
        setCartItems((prevItems) => {

            if (user && user.sub) {
                updateCartItem(user.sub, item.id, item.quantity)
                    .then((res) => console.log("Updated cart", res))
                    .catch((err) => {
                        console.error("Failed to update cart in DB", err);
                    });

                // Replace cartItem with a new one to trigger render
                return prevItems.concat([item]);
            } else {
                return [];
            }
        });
    };

    return (
        <CartContext.Provider value={{ cartItems, removeItem, updateQuantity, addItem }}>
            {children}
        </CartContext.Provider>
    );
};