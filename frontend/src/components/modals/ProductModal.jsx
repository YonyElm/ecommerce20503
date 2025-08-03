import React from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button as MuiButton,
  Box,
  MenuItem,
  Typography,
} from "@mui/material";

const DEFAULT_FORM = {
  name: "",
  description: "",
  price: "",
  maxQuantity: "",
  imageURL: "",
  categoryName: "",
};

const REQUIRED_FIELDS = ["name", "price", "maxQuantity", "categoryName", "description"];

export default function ProductModal({ open, onClose, onSubmit, product, categories = [] }) {
  const [form, setForm] = React.useState(product || DEFAULT_FORM);
  const [errors, setErrors] = React.useState({});
  const [selectedFile, setSelectedFile] = React.useState(null);
  const [fileError, setFileError] = React.useState("");

  React.useEffect(() => {
    setForm(product || DEFAULT_FORM);
    setErrors({});
    setSelectedFile(null);
    setFileError("");
  }, [product, open]);

  const handleChange = (e) => {
    setForm(prev => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
    setErrors(prev => ({
      ...prev,
      [e.target.name]: false,
    }));
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (!file) {
      setSelectedFile(null);
      setFileError("");
      return;
    }
    if (file.size > 0.5 * 1024 * 1024) {
      setSelectedFile(null);
      setFileError("Image size must be below 0.5MB");
    } else {
      setSelectedFile(file);
      setFileError("");
      setForm((prev) => ({ ...prev, imageURL: "" }));
    }
  };

  const handleSubmit = (e) => {
    e?.preventDefault();
    const newErrors = {};

    REQUIRED_FIELDS.forEach((field) => {
      if (!form[field] || form[field].toString().trim() === "") {
        newErrors[field] = true;
      }
    });

    // Additional type validation
    if (form.price && isNaN(Number(form.price))) {
      newErrors.price = true;
    }
    if (form.maxQuantity && !Number.isInteger(Number(form.maxQuantity))) {
      newErrors.maxQuantity = true;
    }

    setErrors(newErrors);

    // Don't submit if errors or image too large
    if (Object.keys(newErrors).length === 0 && !fileError) {
      const submitData = {
        ...form,
        price: Number(form.price),
        maxQuantity: Number(form.maxQuantity),
      };
      // Pass both data and selectedFile to onSubmit
      onSubmit(submitData, selectedFile);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>{product ? "Edit Product" : "Add New Product"}</DialogTitle>
      <DialogContent>
        <Box component="form" onSubmit={handleSubmit}>
          <TextField
            label="Product Name"
            name="name"
            value={form.name || ""}
            onChange={handleChange}
            fullWidth
            margin="normal"
            size="small"
            required
            error={errors.name}
            helperText={errors.name ? "Product name is required" : ""}
          />
          <TextField
            label="Description"
            name="description"
            value={form.description || ""}
            onChange={handleChange}
            fullWidth
            margin="normal"
            size="small"
            multiline
            minRows={2}
            required
            error={!!errors.description}
            helperText={errors.description ? "Description is required" : ""}
          />
          <TextField
            label="Price"
            name="price"
            type="number"
            value={form.price}
            onChange={handleChange}
            fullWidth
            margin="normal"
            size="small"
            required
            error={errors.price}
            helperText={
              errors.price
                ? isNaN(Number(form.price))
                  ? "Price must be a number"
                  : "Price is required"
                : ""
            }
            inputProps={{ step: "0.01", min: "0" }}
          />
          <TextField
            label="Stock Quantity"
            name="maxQuantity"
            type="number"
            value={form.maxQuantity}
            onChange={handleChange}
            fullWidth
            margin="normal"
            size="small"
            required
            error={errors.maxQuantity}
            helperText={
              errors.maxQuantity
                ? !Number.isInteger(Number(form.maxQuantity))
                  ? "Stock quantity must be an integer"
                  : "Stock is required"
                : ""
            }
            inputProps={{ min: "0" }}
          />
          <TextField
            label="Category"
            name="categoryName"
            select
            value={form.categoryName}
            onChange={handleChange}
            fullWidth
            margin="normal"
            size="small"
            required
            error={errors.categoryName}
            helperText={errors.categoryName ? "Category is required" : ""}
          >
            {categories.length === 0 ? (
              <MenuItem value="" disabled>
                No categories available
              </MenuItem>
            ) : (
              categories.map((cat) => (
                <MenuItem key={cat.id || cat.name} value={cat.name}>
                  {cat.name}
                </MenuItem>
              ))
            )}
          </TextField>

          {/* File input for image */}
          <Box sx={{ mt: 2, mb: 1 }}>
            <input
              accept="image/*"
              type="file"
              id="image-upload"
              style={{ display: "none" }}
              onChange={handleFileChange}
            />
            <label htmlFor="image-upload">
              <MuiButton variant="outlined" component="span">
                Choose Product Image
              </MuiButton>
            </label>
            {selectedFile && (
              <Typography
                variant="body2"
                color="text.secondary"
                sx={{ ml: 2 }}
              >
                {selectedFile.name} (
                {(selectedFile.size / 1024).toFixed(1)} KB)
              </Typography>
            )}
            {fileError && (
              <Typography color="error" variant="body2" sx={{ mt: 1 }}>
                {fileError}
              </Typography>
            )}
          </Box>
        </Box>
      </DialogContent>
      <DialogActions>
        <MuiButton onClick={onClose}>Cancel</MuiButton>
        <MuiButton variant="contained" color="primary" onClick={handleSubmit}>
          {product ? "Save" : "Add"}
        </MuiButton>
      </DialogActions>
    </Dialog>
  );
}