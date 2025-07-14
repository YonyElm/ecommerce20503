import React, {useState} from "react";
import CheckoutContext from "../context/CheckoutContext";
import {useNavigate} from "react-router-dom";
import Spinner from "../components/Spinner";

const CheckoutPage = () => {
    const {
        shippingAddressList,
        paymentMethodList,
        isLoading,
        subtotalPrice,
        shippingCost,
        totalItems,
        totalPrice,
        productId,
        submitPlaceOrderForm
    } = CheckoutContext();
    const navigate = useNavigate();

    const [selectedAddressId, setSelectedAddressId] = useState("");
    const [selectedPaymentId, setSelectedPaymentId] = useState("");

    if (isLoading) {
        return (
            <div className="flex justify-center items-center h-64">
                <Spinner />
            </div>
        );
    }

    const isButtonDisabled = !selectedAddressId || !selectedPaymentId;

    return (
        <main className="container mx-auto mt-8 px-4">
            <h2 className="text-3xl font-bold mb-6 text-gray-800">Checkout</h2>

            <div className="checkout-container grid grid-cols-1 md:grid-cols-3 gap-8">
                <div className="md:col-span-2">
                    <form onSubmit={(e) => submitPlaceOrderForm(e, selectedAddressId, selectedPaymentId, productId, totalItems, navigate)}>
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
                                <span>Items ({totalItems}):</span>
                                <span>${subtotalPrice.toFixed(2)}</span>
                            </div>
                            <div className="order-summary-line flex justify-between mb-2">
                                <span>Shipping:</span>
                                <span>
                                    {shippingCost === 0 ? (
                                        <span className="text-green-600 font-semibold">FREE</span>
                                    ) : (
                                        `$${shippingCost.toFixed(2)}`
                                    )}
                                </span>
                            </div>
                            <div className="order-summary-total flex justify-between mt-4 font-bold text-lg">
                                <span>Order Total:</span>
                                <span>${totalPrice.toFixed(2)}</span>
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
                        <span>${subtotalPrice.toFixed(2)}</span>
                    </div>
                    <div className="order-summary-line flex justify-between mb-2">
                        <span>Shipping:</span>
                        <span>
                            {shippingCost === 0 ? (
                                <span className="text-green-600 font-semibold">FREE</span>
                            ) : (
                                `$${shippingCost.toFixed(2)}`
                            )}
                        </span>
                    </div>
                    <div className="order-summary-total flex justify-between mt-4 font-bold text-lg">
                        <span>Order Total:</span>
                        <span>${totalPrice.toFixed(2)}</span>
                    </div>
                </div>
            </div>
        </main>
    );
};

export default CheckoutPage;