import React from "react";
import {Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button as MuiButton, Box} from "@mui/material";

const DEFAULT_FORM = {
  fullName: "",
  addressLine1: "",
  addressLine2: "",
  city: "",
  postalCode: "",
  country: "",
  phoneNumber: "",
};

const REQUIRED_FIELDS = [
  "fullName",
  "addressLine1",
  "city",
  "postalCode",
  "country",
];

export default function AddressModal({ open, onClose, onSubmit, address }) {
  const [form, setForm] = React.useState(address || DEFAULT_FORM);
  const [errors, setErrors] = React.useState({});

  React.useEffect(() => {
    setForm(address || DEFAULT_FORM);
    setErrors({});
  }, [address, open]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setErrors({ ...errors, [e.target.name]: false });
  };

  const handleSubmit = (e) => {
    e?.preventDefault();

    // Validate required fields
    const newErrors = {};
    REQUIRED_FIELDS.forEach((field) => {
      if (!form[field] || form[field].trim() === "") {
        newErrors[field] = true;
      }
    });
    setErrors(newErrors);

    // Submit a form only where there are no errors
    if (Object.keys(newErrors).length === 0) {
      onSubmit(form);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>{address ? "Edit Address" : "Add New Address"}</DialogTitle>
      <DialogContent>
        <Box component="form" onSubmit={handleSubmit}>
          <TextField
            label="Full Name"
            name="fullName"
            value={form.fullName || ""}
            onChange={handleChange}
            fullWidth
            margin="normal"
            size="small"
            required
            error={errors.fullName}
            helperText={errors.fullName ? "Full Name is required" : ""}
          />
          <TextField
            label="Address Line 1"
            name="addressLine1"
            value={form.addressLine1 || ""}
            onChange={handleChange}
            fullWidth
            margin="normal"
            size="small"
            required
            error={errors.addressLine1}
            helperText={errors.addressLine1 ? "Address Line 1 is required" : ""}
          />
          <TextField
            label="Address Line 2"
            name="addressLine2"
            value={form.addressLine2 || ""}
            onChange={handleChange}
            fullWidth
            margin="normal"
            size="small"
          />
          <TextField
            label="City"
            name="city"
            value={form.city || ""}
            onChange={handleChange}
            fullWidth
            margin="normal"
            size="small"
            required
            error={errors.city}
            helperText={errors.city ? "City is required" : ""}
          />
          <TextField
            label="Postal Code"
            name="postalCode"
            value={form.postalCode || ""}
            onChange={handleChange}
            fullWidth
            margin="normal"
            size="small"
            required
            error={errors.postalCode}
            helperText={errors.postalCode ? "Postal Code is required" : ""}
          />
          <TextField
            label="Country"
            name="country"
            value={form.country || ""}
            onChange={handleChange}
            fullWidth
            margin="normal"
            size="small"
            required
            error={errors.country}
            helperText={errors.country ? "Country is required" : ""}
          />
          <TextField
            label="Phone Number"
            name="phoneNumber"
            value={form.phoneNumber || ""}
            onChange={handleChange}
            fullWidth
            size="small"
            margin="normal"
          />
        </Box>
      </DialogContent>
      <DialogActions>
        <MuiButton onClick={onClose}>Cancel</MuiButton>
        <MuiButton variant="contained" color="primary" onClick={handleSubmit}>
          {address ? "Save" : "Add"}
        </MuiButton>
      </DialogActions>
    </Dialog>
  );
}