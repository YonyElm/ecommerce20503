import React, {useContext} from "react";
import { CartContext } from "../context/CartContext";
import CartItem from "../components/CartItem";
import {Link} from "react-router-dom";

const SHIPPING_COST = 5.99;

const CartPage = () => {
    const { cartItems, updateQuantity, removeItem } = useContext(CartContext);

    const subtotal = cartItems?.reduce((sum, item) => sum + item.price * item.quantity, 0);
    const totalItems = cartItems?.reduce((sum, item) => sum + item.quantity, 0);
    const total = subtotal + (cartItems?.length > 0 ? SHIPPING_COST : 0);

    return (
        <main className="container mx-auto mt-8 px-4">
            <h2 className="text-3xl font-bold mb-6 text-gray-800">Shopping Cart</h2>
            <div className="cart-container grid grid-cols-1 md:grid-cols-3 gap-8">
                <div className="md:col-span-2">
                    {!cartItems?.length ? (
                        <div className="text-gray-500 flex flex-col items-start gap-2 mb-6">
                            <span>Your cart is empty.</span>
                            <Link
                                to="/"
                                className="text-cyan-500 hover:underline font-semibold"
                            >
                                Start Shopping
                            </Link>
                        </div>
                    ) : (
                        cartItems.map((item) => (
                            <CartItem
                                key={item.id}
                                item={item}
                                updateQuantity={updateQuantity}
                                removeItem={removeItem}
                            />
                        ))
                    )}
                </div>
                <div className="cart-summary md:col-span-1 bg-white rounded shadow p-6">
                    <h3 className="text-xl font-semibold mb-4">Order Summary</h3>
                    <div className="cart-summary-line flex justify-between mb-2">
                        <span>Subtotal ({totalItems} items):</span>
                        <span>${subtotal?.toFixed(2)}</span>
                    </div>
                    <div className="cart-summary-line flex justify-between mb-2">
                        <span>Shipping:</span>
                        <span>${cartItems.length > 0 ? SHIPPING_COST.toFixed(2) : "0.00"}</span>
                    </div>
                    <div className="cart-summary-total flex justify-between mt-4 font-bold text-lg">
                        <span>Order Total:</span>
                        <span>${cartItems.length > 0 ? total?.toFixed(2) : "0.00"}</span>
                    </div>
                    <button
                        className="checkout-btn mt-6 w-full bg-cyan-500 text-white py-2 rounded font-semibold
                            hover:bg-cyan-700 disabled:opacity-50 disabled:bg-gray-300"
                        disabled={cartItems.length === 0}
                    >
                        Proceed to Checkout
                    </button>
                </div>
            </div>
        </main>
    );
};

export default CartPage;