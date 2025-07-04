import {useState, useEffect, useContext} from "react";
import { getCheckoutDetailsByUserId } from "../api/checkout";
import {AuthContext} from "./AuthContext";

/**
 * Custom hook to fetch checkout data (addresses and payment methods) for a user.
 *
 * @param {number} userId
 * @returns {object}
 */
const CheckoutContext = () => {
  const { user } = useContext(AuthContext);
  const [shippingAddressList, setShippingAddressList] = useState([]);
  const [paymentMethodList, setPaymentMethodList] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (user && user.sub) {
      getCheckoutDetailsByUserId(user.sub)
        .then(res => {
          setShippingAddressList(res.data.shipingAddressList || []);
          setPaymentMethodList(res.data.paymentMethodList || []);
          setIsLoading(false);
        })
        .catch(err => {
          console.error('Error loading checkout data', err);
          setIsLoading(false);
        });
    } else {
      setShippingAddressList([]);
      setPaymentMethodList([]);
      setIsLoading(false);
    }
  }, [user]);

  return {
    shippingAddressList,
    paymentMethodList,
    isLoading,
  };
};

export default CheckoutContext;