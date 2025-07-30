import { OrdersContext } from "../context/OrdersContext";
import Spinner from "../components/Spinner";
import React from "react";
import OrderItem from "../components/OrderItem";
import NotFound from "../components/NotFound";

function OrdersPage() {
  const { orders, loading } = OrdersContext();

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <Spinner />
      </div>
    );
  }

  return (
    <main className="container mx-auto mt-8 px-4">
      <h2 className="text-3xl font-bold mb-6 text-gray-800">Your Orders</h2>
      <div className="order-tracking-container space-y-8">
        {!orders.length ?
          <NotFound message="No orders found." />
          :
          orders.map((order) => (
          <OrderItem key={order.order.id} order={order} />
        ))}
      </div>
    </main>
  );
}

export default OrdersPage;