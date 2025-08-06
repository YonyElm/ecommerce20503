import { useEffect, useState, useContext } from "react";
import * as api from "../api/userSettings";
import { AuthContext } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

/**
 * Fetches and manages the user's profile, addresses, payment methods,
 * and all UI-related state/handlers for the User Settings page.
 */
export function UserSettingsPageContext() {
  const authContext = useContext(AuthContext);
  const navigate = useNavigate();
  const [profile, setProfile] = useState(null);
  const [addresses, setAddresses] = useState([]);
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);

    if (authContext.loading) return;

    if (!authContext.user) {
      navigate("/");
      setLoading(false);
      return;
    }

    const userContext = authContext.user;

    if (!userContext.sub) {
      setProfile(null);
      setAddresses([]);
      setPayments([]);
      setLoading(false);
      return;
    }

    api.getUserSettings(userContext.sub)
      .then((res) => {
        if (res.data.success && res.data.data) {
          setProfile(res.data.data.user);
          setAddresses(res.data.data.addresses || []);
          setPayments(res.data.data.payments || []);
        } else {
          authContext.logout();
        }
      })
      .catch((err) => {
        authContext.logout();
        setError(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [authContext, navigate]);

  const updateProfile = async (data) => {
    const res = await api.updateUserProfile(authContext.user.sub, data);
    setProfile(res.data);
  };

  const addAddress = async (address) => {
    const res = await api.addUserAddress(authContext.user.sub, address);
    setAddresses((prev) => [...prev, res.data]);
  };

  const editAddress = async (addressId, address) => {
    const res = await api.updateUserAddress(authContext.user.sub, addressId, address);
    setAddresses((prev) =>
      prev.map((addressItem) =>
        addressItem.id === addressId ? res.data : addressItem
      )
    );
  };

  const deleteAddress = async (addressId) => {
    await api.deleteUserAddress(authContext.user.sub, addressId);
    setAddresses((prev) =>
      prev.filter((addressItem) => addressItem.id !== addressId)
    );
  };

  const addPayment = async (payment) => {
    const res = await api.addUserPaymentMethod(authContext.user.sub, payment);
    setPayments((prev) => [...prev, res.data]);
  };

  const editPayment = async (paymentId, payment) => {
    const res = await api.updateUserPaymentMethod(authContext.user.sub, paymentId, payment);
    setPayments((prev) =>
      prev.map((p) => (p.id === paymentId ? res.data : p))
    );
  };

  const deletePayment = async (paymentId) => {
    await api.deleteUserPaymentMethod(authContext.user.sub, paymentId);
    setPayments((prev) => prev.filter((p) => p.id !== paymentId));
  };

  // Address Modal State & Handlers
  const [isAddressModalOpen, setIsAddressModalOpen] = useState(false);
  const [editingAddress, setEditingAddress] = useState(null);

  const handleOpenAddAddress = () => {
    setEditingAddress(null);
    setIsAddressModalOpen(true);
  };

  const handleOpenEditAddress = (address) => {
    setEditingAddress(address);
    setIsAddressModalOpen(true);
  };

  const handleCloseAddressModal = () => {
    setIsAddressModalOpen(false);
    setEditingAddress(null);
  };

  const handleSubmitAddress = async (address) => {
    if (editingAddress?.id) {
      await editAddress(editingAddress.id, address);
    } else {
      await addAddress(address);
    }
    handleCloseAddressModal();
  };

  // Payment Modal State & Handlers
  const [isPaymentModalOpen, setIsPaymentModalOpen] = useState(false);
  const [editingPayment, setEditingPayment] = useState(null);

  const handleOpenAddPayment = () => {
    setEditingPayment(null);
    setIsPaymentModalOpen(true);
  };

  const handleOpenEditPayment = (payment) => {
    setEditingPayment(payment);
    setIsPaymentModalOpen(true);
  };

  const handleClosePaymentModal = () => {
    setIsPaymentModalOpen(false);
    setEditingPayment(null);
  };

  const handleSubmitPayment = async (payment) => {
    if (editingPayment?.id) {
      await editPayment(editingPayment.id, payment);
    } else {
      await addPayment(payment);
    }
    handleClosePaymentModal();
  };

  const storeLink = () => {
    if (authContext?.user?.roleName === "ADMIN") {
      navigate("/admin");
    } else if (authContext?.user?.roleName === "SELLER") {
      navigate("/store");
    }
  };

  const becomeSeller = async () => {
    try {
      const result = await api.updateUserRole(authContext.user.sub, {
        targetUserId: authContext.user.sub,
        roleName: "SELLER",
      });
      if (result?.data?.token) {
        authContext.login(result.data.token);
      }
    } catch (err) {
      console.error("Failed to update role", err);
    }
  };

  return {
    loading,
    error,
    profile,
    addresses,
    payments,
    roleName: authContext?.user?.roleName,
    updateProfile,
    addAddress,
    editAddress,
    deleteAddress,
    addPayment,
    editPayment,
    deletePayment,
    logout: () => authContext.logout(),
    storeLink,
    becomeSeller,
    // Modals state & handlers
    isAddressModalOpen,
    editingAddress,
    handleOpenAddAddress,
    handleOpenEditAddress,
    handleCloseAddressModal,
    handleSubmitAddress,
    isPaymentModalOpen,
    editingPayment,
    handleOpenAddPayment,
    handleOpenEditPayment,
    handleClosePaymentModal,
    handleSubmitPayment,
  };
}
