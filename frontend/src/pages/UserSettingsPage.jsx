import React, {useEffect, useState} from "react";
import { UserSettingsPageContext } from "../context/UserSettingsPageContext";
import Spinner from "../components/Spinner";
import NotFound from "../components/NotFound";
import AddressModal from "../components/modals/AddressModal";
import PaymentMethodModal from "../components/modals/PaymentMethodModal";
import {
  Typography,
  TextField,
  Button as MuiButton,
  Card,
  CardContent,
  CardActions,
  Grid,
  Box,
} from "@mui/material";

export default function UserSettingsPage() {
  const {
    loading,
    error,
    profile,
    addresses,
    payments,
    updateProfile,
    deleteAddress,
    deletePayment,
    logout,
    // modals state & handlers
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
  } = UserSettingsPageContext();

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="300px">
        <Spinner />
      </Box>
    );
  }

  return (
    <Box maxWidth="lg" mx="auto" sx={{ mt: 4, px: 2 }}>
      <Typography variant="h4" fontWeight={700} gutterBottom>
        Manage Your Profile
      </Typography>
      {error ? (
        <NotFound message="Could not find user settings. Please try again later." />
      ) : (
        <Grid container direction="column" spacing={4}>
          <Grid item>
            <ProfileForm profile={profile} updateProfile={updateProfile} />
          </Grid>
          <Grid item>
            <AddressesSection
              addresses={addresses}
              deleteAddress={deleteAddress}
              onEdit={handleOpenEditAddress}
              onAdd={handleOpenAddAddress}
            />
          </Grid>
          <Grid item>
            <PaymentsSection
              payments={payments}
              onEdit={handleOpenEditPayment}
              onAdd={handleOpenAddPayment}
              deletePayment={deletePayment}
            />
          </Grid>
          <Grid item>
            <MuiButton sx={{ width: "100%" }} variant="contained" color="error" size="medium" onClick={logout}>
              Logout
            </MuiButton>
          </Grid>
        </Grid>
      )}
      <AddressModal
        open={isAddressModalOpen}
        onClose={handleCloseAddressModal}
        onSubmit={handleSubmitAddress}
        address={editingAddress}
      />
      <PaymentMethodModal
        open={isPaymentModalOpen}
        onClose={handleClosePaymentModal}
        onSubmit={handleSubmitPayment}
        payment={editingPayment}
      />
    </Box>
  );
}

function ProfileForm({ profile, updateProfile }) {
  const [form, setForm] = useState({
    fullName: profile?.fullName || "",
  });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(false);

  useEffect(() => {
    setForm({
      fullName: profile?.fullName || "",
    });
  }, [profile]);

  const handleChange = (e) => {
    setForm((formVal) => ({ ...formVal, [e.target.name]: e.target.value }));
    setError(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.fullName || !form.fullName.trim()) {
      setError(true);
      return;
    }
    setSaving(true);
    await updateProfile(form);
    setSaving(false);
  };

  return (
    <Card sx={{ width: "100%" }} variant="outlined">
      <CardContent>
        <Typography variant="h6" gutterBottom>
          Personal Information
        </Typography>
        <Box component="form" onSubmit={handleSubmit}>
          <TextField
            fullWidth
            label="Full Name"
            id="profile-name"
            name="fullName"
            value={form.fullName}
            onChange={handleChange}
            placeholder="Enter your name"
            disabled={saving}
            variant="outlined"
            size="small"
            required
            error={error}
            helperText={error ? "Full Name is required" : ""}
          />
          <MuiButton
            sx={{ mt: 2 }}
            type="submit"
            variant="contained"
            size="small"
            disabled={saving}
            color="primary"
          >
            {saving ? "Saving..." : "Update Details"}
          </MuiButton>
        </Box>
      </CardContent>
    </Card>
  );
}

function AddressesSection({ addresses, deleteAddress, onEdit, onAdd }) {
  return (
    <Card sx={{ width: "100%" }} variant="outlined">
      <CardContent>
        <Typography variant="h6" gutterBottom>
          Shipping Addresses
        </Typography>
        <Grid container direction="column" spacing={2}>
          {addresses.map((addr) => (
            <Card key={addr.id} variant="outlined" sx={{ width: "100%", display: "flex" }}>
              <CardContent sx={{ flex: 1 }}>
                <Typography variant="subtitle1" fontWeight={500}>
                  {addr.fullName}
                </Typography>
                <Typography variant="body2">{addr.addressLine1}</Typography>
                <Typography variant="body2">{addr.addressLine2}</Typography>
                <Typography variant="body2">
                  {addr.city}, {addr.postalCode}
                </Typography>
                <Typography variant="body2">{addr.country}</Typography>
                <Typography variant="body2">{addr.phoneNumber}</Typography>
              </CardContent>
              <Box
                sx={{display: "flex", flexDirection: "column", justifyContent: "center",
                  alignItems: "flex-start", gap: 2, py: 1, pr: 1,}}>
                <MuiButton variant="outlined" size="small" sx={{ minWidth: 90 }} onClick={() => onEdit(addr)}>
                  Edit
                </MuiButton>
                <MuiButton variant="outlined" color="error" size="small"
                           onClick={() => deleteAddress(addr.id)} sx={{ minWidth: 90 }}>
                  Delete
                </MuiButton>
              </Box>
            </Card>
          ))}
        </Grid>
      </CardContent>
      <CardActions sx={{ pb: 2, pl: 2 }}>
        <MuiButton variant="contained" size="small" onClick={onAdd}>
          Add New Address
        </MuiButton>
      </CardActions>
    </Card>
  );
}

function PaymentsSection({ payments, onEdit, onAdd, deletePayment }) {
  return (
    <Card sx={{ width: "100%" }} variant="outlined">
      <CardContent>
        <Typography variant="h6" gutterBottom>
          Payment Methods
        </Typography>
        <Grid container direction="column" spacing={2}>
          {payments.map((pm) => (
            <Card key={pm.id} variant="outlined" sx={{ display: "flex", width: "100%" }}>
              <CardContent sx={{ flexGrow: 1 }}>
                <Typography variant="subtitle1" fontWeight={500}>
                  {pm.name}
                </Typography>
              </CardContent>
              <CardActions>
                <MuiButton variant="outlined" size="small" onClick={() => onEdit(pm)}>
                  Edit
                </MuiButton>
                <MuiButton variant="outlined" color="error" size="small" onClick={() => deletePayment(pm.id)}>
                  Delete
                </MuiButton>
              </CardActions>
            </Card>
          ))}
        </Grid>
      </CardContent>
      <CardActions sx={{ pb: 2, pl: 2 }}>
        <MuiButton variant="contained" size="small" onClick={onAdd}>
          Add Payment Method
        </MuiButton>
      </CardActions>
    </Card>
  );
}