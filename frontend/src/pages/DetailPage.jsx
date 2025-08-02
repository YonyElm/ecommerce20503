import React, { useContext } from "react";
import {Link, useNavigate} from "react-router-dom";
import Spinner from "../components/Spinner";
import {ChosenQuantityContext, DetailPageContext} from "../context/DetailPageContext";
import { AuthContext } from "../context/AuthContext";
import {CartContext} from "../context/CartContext";
import NotFound from "../components/NotFound";

const DetailPage = () => {
    const { product, isLoading } = DetailPageContext();
    const [chosenQuantity, handleChosenQuantityChange] = ChosenQuantityContext();
    const { user: isSignedIn} = useContext(AuthContext);
    const {cartItems, addItem, updateQuantity} = useContext(CartContext);
    const navigate = useNavigate();

    if (isLoading) {
        return (
            <div className="flex justify-center items-center h-64">
                <Spinner />
            </div>
        );
    }

    if (!product) {
        return (
            <main className="container mx-auto mt-8 px-4">
                <NotFound message="Can't find product, Perhaps it is no longer listed for sale." />
            </main>
        );
    }

    const addToCart = () => {
        if (cartItems.some(item => item.productId === product.id)) {
            updateQuantity(product.id, chosenQuantity, true);
        } else {
            let item = {productId: product.id, name: product.name,
                price: product.price, quantity: chosenQuantity};
            addItem(item);
        }
        alert(`Added ${chosenQuantity} ${product.name}(s) to cart!`);
    };

    const buyNow = () => {
        navigate(`/checkout/${product.id}?quantity=${chosenQuantity}&price=${product.price}`);
    };

    const inStock = product.maxQuantity > 0;
    const categoryName = product.categoryName ? product.categoryName : "N/A";

    return (
        <main className="container mx-auto mt-24 px-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8 bg-white p-6 rounded-lg shadow border border-gray-200">
                <div>
                    <div className="bg-gray-200 flex justify-center items-center h-[400px] w-full rounded text-4xl text-gray-400 mb-4">
                        {product.imageURL ? (
                            <img
                                src={product.imageURL}
                                alt={product.name}
                                className="object-contain h-full w-full rounded"
                            />
                        ) : (
                            <span>[Main Product Image]</span>
                        )}
                    </div>
                    <div className="flex gap-2 mt-4">
                        {product.images && product.images.length > 0
                            ? product.images.map((img, idx) => (
                                <img
                                    key={idx}
                                    src={img}
                                    alt={`Thumbnail ${idx + 1}`}
                                    className="w-16 h-16 bg-gray-200 rounded cursor-pointer object-cover"
                                />
                            ))
                            : [1, 2, 3].map((n) => (
                                <div
                                    key={n}
                                    className="w-16 h-16 bg-gray-200 rounded cursor-pointer flex items-center justify-center text-gray-400"
                                >
                                    +
                                </div>
                            ))}
                    </div>
                </div>

                <div>
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">{product.name}</h1>
                    <p className="text-gray-600 text-sm mb-4">Category: {categoryName}</p>
                    <p className="text-xl font-bold text-gray-900 mb-4">${product.price}</p>
                    <p className="text-gray-700 mb-6">{product.description}</p>

                    <div className="mb-4">
                        <span className="text-gray-700 font-semibold">Availability:</span>
                        {inStock ? (
                            <span className="text-green-600"> In Stock ({product.maxQuantity} left)</span>
                        ) : (
                            <span className="text-red-600"> Out of Stock</span>
                        )}
                    </div>

                    <div className="flex items-center gap-4 mb-6">
                        <label htmlFor="quantity" className={`font-semibold ${!inStock ? "text-gray-400 line-through" : "text-gray-700"}`}>
                            Quantity:
                        </label>
                        <input
                            type="number"
                            id="quantity"
                            name="quantity"
                            value={chosenQuantity}
                            onChange={handleChosenQuantityChange}
                            min="1"
                            max={product.maxQuantity}
                            className={`border rounded py-1 px-2 w-16 text-center 
                                ${!inStock ? "bg-gray-100 text-gray-400 cursor-not-allowed" : ""}`}
                            disabled={!inStock || !isSignedIn}
                        />
                    </div>

                    <div className="flex gap-4">
                        <button
                            className={`w-full py-2 rounded font-semibold 
                                ${
                                    inStock && isSignedIn
                                        ? "bg-cyan-500 text-white hover:bg-cyan-700"
                                        : "bg-gray-300 text-gray-500 cursor-not-allowed"
                                }`}
                            disabled={!inStock || !isSignedIn}
                            onClick={addToCart}
                        >
                            Add to Cart
                        </button>
                        <button
                            className={`w-full py-2 rounded font-semibold 
                                ${
                                    inStock && isSignedIn
                                        ? "bg-cyan-500 text-white hover:bg-cyan-700"
                                        : "bg-gray-300 text-gray-500 cursor-not-allowed"
                                }`}
                            disabled={!inStock || !isSignedIn}
                            onClick={buyNow}
                        >
                            Buy Now
                        </button>
                    </div>

                    {!isSignedIn ?
                        <p className="mt-6 text-center text-m text-gray-700">
                            Please{" "}
                            <Link to="/login" className="text-cyan-500 underline font-medium hover:text-cyan-700">
                                sign in
                            </Link>{" "}
                            to purchase or add items to your cart.
                        </p>
                        :
                        <div></div>
                    }
                </div>
            </div>
        </main>
    );
};

export default DetailPage;