import { useState, useEffect, useContext } from "react";
import { AuthContext } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import { getOrdersPage } from "../api/orders";

export const OrdersContext = () => {
  const authContext = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    if (!authContext.loading && !authContext.user) {
      navigate("/");
    }
  }, [authContext.user, authContext.loading, navigate]);

  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (authContext.user && authContext.user.sub) {
      getOrdersPage(authContext.user.sub)
        .then((res) => {
          setOrders(res.data)
        })
        .catch((err) => {
            console.error('Error loading orders', err);
            setOrders([])
        })
        .finally(() => {
          setLoading(false)
        });
    } else {
      setOrders([]);
      setLoading(false);
    }
  }, [authContext.user]);

  return { orders, loading };
};