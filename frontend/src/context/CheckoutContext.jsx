import { useState, useEffect, useContext } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import { getCheckoutDetailsByUserId } from "../api/checkout";
import { AuthContext } from "./AuthContext";
import { CartContext } from "./CartContext";

const SHIPPING_COST = 0;

const CheckoutContext = () => {
  const authContext = useContext(AuthContext);
  const { productId } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const { cartItems, submitPlaceOrderForm } = useContext(CartContext);

  // --- Buy now/URL queries ---
  const query = new URLSearchParams(location.search);
  const buyNowPrice = Number(query.get("price") || -1);
  const buyNowQuantity = Number(query.get("quantity") || -1);

  // --- Auth protection and redirect ---
  useEffect(() => {
    if (!authContext.loading &&
      (!authContext.user || (productId 
        && (buyNowPrice < 0 || buyNowQuantity < 0)))
    ) {
      navigate("/");
    }
  }, [authContext.user, authContext.loading, navigate, productId, buyNowPrice, buyNowQuantity]);

  // --- Checkout lists fetch ---
  const [shippingAddressList, setShippingAddressList] = useState([]);
  const [paymentMethodList, setPaymentMethodList] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setIsLoading(true);
    if (!authContext.loading && (!authContext.user || !authContext.user.sub)) {
      setShippingAddressList([]);
      setPaymentMethodList([]);
      setIsLoading(false);
      return;
    }

    if (authContext.user?.sub) {
      getCheckoutDetailsByUserId(authContext.user.sub)
        .then(res => {
          const data = res?.data ?? {};
          setShippingAddressList(data.shippingAddressList || []);
          setPaymentMethodList(data.paymentMethodList ?? []);
        })
        .catch(err => {
          console.error("Error loading checkout data", err);
          setShippingAddressList([]);
          setPaymentMethodList([]);
        }).finally(() => setIsLoading(false));
    }
  }, [authContext.user, authContext.loading]);

  // --- Price and items calculations ---
  const subtotalPrice =
    !productId
      ? cartItems?.reduce((sum, item) => sum + item.price * item.quantity, 0) || 0
      : buyNowPrice * buyNowQuantity;
  const totalItems =
    !productId
      ? cartItems?.reduce((sum, item) => sum + item.quantity, 0) || 0
      : buyNowQuantity;
  const totalPrice =
      subtotalPrice + (cartItems && cartItems.length > 0 ? SHIPPING_COST : 0);

  return {
    shippingAddressList,
    paymentMethodList,
    isLoading: isLoading || authContext.loading,
    subtotalPrice,
    shippingCost: SHIPPING_COST,
    totalItems,
    totalPrice,
    productId,
    submitPlaceOrderForm
  };
};

export default CheckoutContext;