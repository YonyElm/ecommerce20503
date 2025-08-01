import { OrdersContext } from "../context/OrdersContext";
import PageContainer from "../components/PageContainer";
import OrderItem from "../components/OrderItem";
import NotFound from "../components/NotFound";
import { Stack } from "@mui/material";

function OrdersPage() {
  const { orders, loading } = OrdersContext();

  return (
    <PageContainer title="Your Orders" loading={loading}>
      {!orders.length ? (
        <NotFound message="No orders found." />
      ) : (
        <Stack spacing={3}>
          {orders.map((order) => (
            <OrderItem key={order.order.id} order={order} />
          ))}
        </Stack>
      )}
    </PageContainer>
  );
}

export default OrdersPage;