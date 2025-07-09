import React, { useState } from "react";
import { MdImageNotSupported } from "react-icons/md";
import {FaExclamationCircle} from "react-icons/fa";

/**
 * OrderItem component represents a single order with header, products, and summary.
 * Props:
 * - order: {
 *     id: string|number,
 *     date: string,
 *     total: number,
 *     products: Array<{
 *       id: string|number,
 *       name: string,
 *       image: string,
 *       qty: number,
 *       price: number,
 *       status: string
 *     }>
 *   }
 */
const OrderItem = ({ order }) => (
  <div className="order-item border rounded-lg p-4 shadow bg-white">
    <div className="order-header flex justify-between items-center mb-3">
      <h3 className="font-semibold text-lg">Order #{order.order.id}</h3>
      <span className="text-sm text-gray-500">Order Date: {order.order.orderDate}</span>
    </div>
    <div className="order-details flex flex-col">
      <h4 className="font-semibold mb-2">Items in this order:</h4>
      <div>
        {order.orderItemList.map((orderItemObj) => (
          <OrderProduct key={orderItemObj.orderItem.id} item={orderItemObj.orderItem} status={orderItemObj.statusList} />
        ))}
      </div>
      <div className="flex justify-end mt-4">
        <div className="order-summary-small flex items-center gap-2 text-right">
          <span className="text-lg font-bold">Order Total:</span>
          <span className="total text-xl font-bold text-green-700">${order.order.totalAmount.toFixed(2)}</span>
        </div>
      </div>
    </div>
  </div>
);

const OrderProduct = ({ item, status }) => {
  const [imageError, setImageError] = useState(false);

  // fallback when product cant be loaded, preventing null-pointer-exception
  if (!item || !item.product) {
    return (
        <div className="flex flex-col sm:flex-row sm:items-center mb-2 p-2 border rounded-md bg-red-50 text-red-700">
          <div className="flex items-center flex-1">
            <FaExclamationCircle size={24} className="mr-3 text-red-600 flex-shrink-0" />
            <div className="order-product-info flex-1">
              <div className="font-medium">Product details unavailable</div>
              <div className="text-sm">We couldn't load the information for this item.</div>
            </div>
          </div>
        </div>
    );
  }

  return (
    <div className="order-product-item flex flex-col sm:flex-row sm:items-center justify-between mb-2 p-2 border rounded-md bg-gray-50">
      <div className="flex items-center flex-1">
        <div className="order-product-image w-16 h-16 flex-shrink-0 rounded overflow-hidden bg-gray-100 mr-3 flex items-center justify-center">
          {!item.product.image || imageError ? (
            <MdImageNotSupported size={40} className="text-gray-400" data-testid="broken-img-icon" />
          ) : (
            <img
              alt={item.product.name}
              src={item.product.image}
              className="object-cover w-full h-full"
              onError={() => setImageError(true)}
            />
          )}
        </div>
        <div className="order-product-info flex-1">
          <div className="order-product-title font-medium">{item.product.name}</div>
          <div className="order-product-qty-price text-sm text-gray-600">
            Qty: {item.quantity} | ${item.product.price.toFixed(2)}
            {item.quantity > 1 && <> each</>}
          </div>
        </div>
      </div>
      <div className="mt-2 md:mt-0 md:ml-4 flex flex-col md:items-end text-right">
        <span className="order-product-status text-xs text-cyan-700 font-bold">
          Status: {status?.[0]?.status ?? "N/A"}
        </span>
        <span className="order-product-total text-md text-gray-800 font-semibold mt-1">
          Total: ${(item.quantity * item.product.price).toFixed(2)}
        </span>
      </div>
    </div>
  );
};

export default OrderItem;