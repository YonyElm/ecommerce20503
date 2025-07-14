import React from "react";

const CartItem = ({ item, updateQuantity, removeItem }) => {
    if (!item?.price || !item?.name || !item?.itemId || !item?.productId || !item?.quantity) {
        console.error(
            "CartItem: Missing required prop(s).",
            {
                price: item?.price,
                name: item?.name,
                itemId: item?.itemId,
                productId: item?.productId,
                quantity: item?.quantity,
            }
        );
        return null;
    }

    return (
        <div className="cart-item flex items-center gap-4 mb-6 bg-white rounded shadow p-3">
            <div className="cart-item-image w-24 h-24 bg-gray-200 flex items-center justify-center rounded">
                {item.image ? (
                    <img src={item.image} alt={item.name} className="object-contain w-full h-full rounded" />
                ) : (
                    <span className="text-gray-400">[Image]</span>
                )}
            </div>
            <div className="cart-item-details flex-1">
                <div className="cart-item-title font-semibold text-lg">{item.name}</div>
                <div className="cart-item-price text-cyan-600 font-bold mt-2">${item.price.toFixed(2)}</div>
            </div>
            <div className="cart-item-actions flex items-center gap-2">
                <label htmlFor={`qty-item-${item.itemId}`} className="text-gray-700 text-sm mr-1">Qty:</label>
                <input
                    type="number"
                    id={`qty-item-${item.itemId}`}
                    name={`qty-item-${item.itemId}`}
                    value={item.quantity}
                    min={1}
                    onChange={(e) => updateQuantity(item.itemId, Math.max(1, Number(e.target.value)), false)}
                    className="w-16 border rounded py-1 px-2 text-center"
                />
                <button
                    className="remove-item-btn text-red-500 hover:underline ml-2"
                    onClick={() => removeItem(item.itemId)}
                >
                    Remove
                </button>
            </div>
        </div>
    );
};

export default CartItem;