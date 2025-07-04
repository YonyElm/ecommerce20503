import React, { useContext, useState } from "react";
import { CartContext } from "../context/CartContext";
import CheckoutContext from "../context/CheckoutContext";

// Adjust this as needed
const SHIPPING_COST = 0;

const CheckoutPage = () => {
    const { cartItems } = useContext(CartContext); // Make sure userId is in CartContext
    const {
        shippingAddressList,
        paymentMethodList,
        isLoading,
    } = CheckoutContext();

    const [selectedAddressId, setSelectedAddressId] = useState("");
    const [selectedPaymentId, setSelectedPaymentId] = useState("");

    const subtotal = cartItems?.reduce((sum, item) => sum + item.price * item.quantity, 0) || 0;
    const totalItems = cartItems?.reduce((sum, item) => sum + item.quantity, 0) || 0;
    const total = subtotal + (cartItems.length > 0 ? SHIPPING_COST : 0);

    const handleSubmit = (e) => {
        e.preventDefault();
        // Submit order using selectedAddressId and selectedPaymentId
        alert(`Order placed with Address ID ${selectedAddressId} and Payment ID ${selectedPaymentId}`);
    };

    if (isLoading) return <p className="p-6 text-center text-gray-500">Loading checkout data...</p>;

    const isButtonDisabled = !selectedAddressId || !selectedPaymentId;

    return (
        <main className="container mx-auto mt-8 px-4">
            <h2 className="text-3xl font-bold mb-6 text-gray-800">Checkout</h2>

            <div className="checkout-container grid grid-cols-1 md:grid-cols-3 gap-8">
                <div className="md:col-span-2">
                    <form onSubmit={handleSubmit}>
                        {/* Shipping Address */}
                        <div className="checkout-section mb-8">
                            <h3 className="text-lg font-semibold mb-2">Shipping Address</h3>
                            <div className="form-group mb-4">
                                <label htmlFor="shipping-address" className="block mb-1 font-medium">
                                    Select Shipping Address
                                </label>
                                <select
                                    id="shipping-address"
                                    name="shipping_address"
                                    value={selectedAddressId}
                                    onChange={(e) => setSelectedAddressId(e.target.value)}
                                    className="border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                    required
                                >
                                    <option value="">-- Select --</option>
                                    {shippingAddressList.map((address) => (
                                        <option key={address.id} value={address.id}>
                                            {address.fullName}, {address.addressLine1}, {address.city}
                                        </option>
                                    ))}
                                </select>
                            </div>
                        </div>

                        {/* Payment Method */}
                        <div className="checkout-section mb-8">
                            <h3 className="text-lg font-semibold mb-2">Payment Method</h3>
                            <div className="form-group mb-4">
                                <label htmlFor="payment-method" className="block mb-1 font-medium">
                                    Select Payment Method
                                </label>
                                <select
                                    id="payment-method"
                                    name="payment_method"
                                    value={selectedPaymentId}
                                    onChange={(e) => setSelectedPaymentId(e.target.value)}
                                    className="border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                    required
                                >
                                    <option value="">-- Select --</option>
                                    {paymentMethodList.map((payment) => (
                                        <option key={payment.id} value={payment.id}>
                                            {payment.name}
                                        </option>
                                    ))}
                                </select>
                            </div>
                        </div>

                        {/* Order Summary */}
                        <div className="checkout-section mb-8">
                            <h3 className="text-lg font-semibold mb-2">Order Review</h3>
                            <div className="order-summary-line flex justify-between mb-2">
                                <span>Items:</span>
                                <span>${subtotal.toFixed(2)}</span>
                            </div>
                            <div className="order-summary-line flex justify-between mb-2">
                                <span>Shipping:</span>
                                <span>
                                    {SHIPPING_COST === 0 ? (
                                        <span className="text-green-600 font-semibold">FREE</span>
                                    ) : (
                                        `$${SHIPPING_COST.toFixed(2)}`
                                    )}
                                </span>
                            </div>
                            <div className="order-summary-total flex justify-between mt-4 font-bold text-lg">
                                <span>Order Total:</span>
                                <span>${total.toFixed(2)}</span>
                            </div>
                        </div>

                        <button
                            type="submit"
                            className={`w-full py-2 rounded font-semibold
                            ${!isButtonDisabled ?
                                "bg-cyan-500 text-white hover:bg-cyan-700"
                                    : "bg-gray-300 text-gray-500 cursor-not-allowed"}`}
                            disabled={isButtonDisabled}
                        >
                            Place Your Order
                        </button>
                    </form>
                </div>

                {/* Sidebar Summary */}
                <div className="md:col-span-1 order-summary-sidebar h-fit bg-white rounded shadow p-6">
                    <h3 className="text-xl font-semibold mb-4">Order Summary</h3>
                    <div className="order-summary-line flex justify-between mb-2">
                        <span>Items ({totalItems}):</span>
                        <span>${subtotal.toFixed(2)}</span>
                    </div>
                    <div className="order-summary-line flex justify-between mb-2">
                        <span>Shipping:</span>
                        <span>
                            {SHIPPING_COST === 0 ? (
                                <span className="text-green-600 font-semibold">FREE</span>
                            ) : (
                                `$${SHIPPING_COST.toFixed(2)}`
                            )}
                        </span>
                    </div>
                    <div className="order-summary-total flex justify-between mt-4 font-bold text-lg">
                        <span>Order Total:</span>
                        <span>${total.toFixed(2)}</span>
                    </div>
                </div>
            </div>
        </main>
    );
};

export default CheckoutPage;