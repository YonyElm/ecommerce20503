import { useEffect, useState, useContext } from "react";
import * as api from "../api/userSettings";
import { AuthContext } from "./AuthContext";
import { useNavigate } from "react-router-dom";

/**
 * Fetches and manages the user's profile, addresses, payment methods,
 * and all UI-related state/handlers for the User Settings page.
 */
export function UserSettingsPageContext() {
  const authContext = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    if (!authContext.loading && !authContext.user) {
      navigate("/");
    }
  }, [authContext.user, authContext.loading, navigate]);

  const [profile, setProfile] = useState(null);
  const [addresses, setAddresses] = useState([]);
  const [payments, setPayments] = useState([]);
  const [roleName, setRoleName] = useState("CUSTOMER");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!authContext.user) return;
    setLoading(true);
    api
      .getUserSettings(authContext.user.sub)
      .then((res) => {
        if (res && res.data) {
          setProfile(res.data.user);
          setAddresses(res.data.addresses || []);
          setPayments(res.data.payments || []);
          setRoleName(res.data.roleName || "CUSTOMER");
        }
        setLoading(false);
      })
      .catch((err) => {
        setError(err);
        setLoading(false);
      });
  }, [authContext.user]);

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

  // MODAL state and handlers (logic now all here)
  // Address Modal
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
    if (editingAddress && editingAddress.id) {
      await editAddress(editingAddress.id, address);
    } else {
      await addAddress(address);
    }
    handleCloseAddressModal();
  };

  // Payment Modal
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
    if (editingPayment && editingPayment.id) {
      await editPayment(editingPayment.id, payment);
    } else {
      await addPayment(payment);
    }
    handleClosePaymentModal();
  };

  const storeLink = () => {
    navigate("/store");
  }

  // Add becomeSeller functionality
  const becomeSeller = async () => {
    try {
      await api.updateUserRole(authContext.user.sub,
        { targetUserId: authContext.user.sub,  roleName: "SELLER" });
      setRoleName("SELLER");
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
    roleName,
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