import React, { useState, useEffect } from "react";
import {Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button as MuiButton, Box,} from "@mui/material";

export default function PaymentModal({ open, onClose, onSubmit, payment }) {
  const [name, setName] = useState(payment?.name || "");
  const [error, setError] = useState(false);

  useEffect(() => {
    setName(payment?.name || "");
    setError(false);
  }, [payment, open]);

  const handleChange = (e) => {
    setName(e.target.value);
    setError(false);
  };

  const handleSubmit = (e) => {
    e?.preventDefault();
    if (name.trim()) {
      onSubmit({ name: name.trim() });
    } else {
      setError(true);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
      <DialogTitle>{payment ? "Edit Payment Method" : "Add Payment Method"}</DialogTitle>
      <DialogContent>
        <Box component="form" onSubmit={handleSubmit}>
          <TextField
            label="Payment Method (e.g., Visa, PayPal)"
            value={name}
            onChange={handleChange}
            fullWidth
            margin="normal"
            size="small"
            required
            autoFocus
            error={error}
            helperText={error ? "Payment Method is required" : ""}
          />
        </Box>
      </DialogContent>
      <DialogActions>
        <MuiButton onClick={onClose}>Cancel</MuiButton>
        <MuiButton variant="contained" color="primary" onClick={handleSubmit}>
          {payment ? "Save" : "Add"}
        </MuiButton>
      </DialogActions>
    </Dialog>
  );
}