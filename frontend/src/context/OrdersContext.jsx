import {useState, useEffect, useContext, useCallback} from "react";
import { AuthContext } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import {getOrdersPage, updateOrderItemStatus} from "../api/orders";

// Hook that encapsulates menu and product navigation logic for an order product
export function OrderItemContext(item, triggerOrdersRefresh) {
  const authContext = useContext(AuthContext);
  const [anchorMenuElement, setAnchorMenuElement] = useState(null);
  const navigate = useNavigate();

  const handleLinkToDetailPage = (e) => {
    e.stopPropagation();
    navigate(`/details/${item.product.id}`);
  };

  const handleMenuOpen = (event) => {
    event.stopPropagation();
    setAnchorMenuElement(event.currentTarget);
  };

  const handleMenuClose = (event) => {
    event?.stopPropagation();
    setAnchorMenuElement(null);
  };

  const handleMenuAction = (itemId, action) => (event) => {
    event.stopPropagation();
    let statusObject = {status: action};
    updateOrderItemStatus(authContext.user.sub, itemId, statusObject)
      .then((res) => {
        if (res.data.success) {
          triggerOrdersRefresh();
        }
      })
      .catch((err) => {
        console.error("Error updating order item status", err);
      })
    setAnchorMenuElement(null);
  };

  return {
    anchorMenuElement,
    handleMenuOpen,
    handleMenuClose,
    handleMenuAction,
    handleLinkToDetailPage,
  };
}

// OrdersContext provides order data loading and state
export const OrdersContext = () => {
  const authContext = useContext(AuthContext);
  const navigate = useNavigate();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [refreshFlag, setRefreshFlag] = useState(0);

  // Call this function after successful status update
  const triggerOrdersRefresh = useCallback(() => {
    setRefreshFlag((flag) => flag + 1);
  }, []);


  useEffect(() => {
    setLoading(true);
    if (authContext.loading) {
      return; // wait until auth finishes
    }

    if (!authContext.user) {
      setLoading(false);
      navigate("/");
      return;
    }

    if (!authContext.user.sub) {
      setOrders([]);
      setLoading(false);
      return;
    }

    setLoading(true);
    getOrdersPage(authContext.user.sub)
      .then((res) => {
        if (res.data.success) {
          setOrders(res.data.data);
        } else {
          setOrders([]);
        }
      })
      .catch((err) => {
        console.error("Error loading orders", err);
        setOrders([]);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [authContext.loading, authContext.user, navigate, refreshFlag]);

  return { orders, loading, triggerOrdersRefresh};
};