import React from "react";
import {
  Typography, Box, Table, TableBody, TableCell, TableContainer, TableHead,
  TableRow, Paper, Button, TextField,
} from "@mui/material";
import { CategoryComponentContext } from "../context/CategoryComponentContext";
import PageContainer from "./PageContainer";

export default function CategoriesComponent() {
  const {
    categories,
    loading,
    error,
    newCategoryName,
    handleChangeNewCategoryName,
    handleAddCategory,
    editingId,
    editedCategoryName,
    handleEditCategory,
    handleChangeEditedCategoryName,
    handleUpdateCategory,
    handleCancelEdit,
    handleDeleteCategory,
  } = CategoryComponentContext();

  return (
    <PageContainer title="Manage Categories" loading={loading}
      error={error && "Could not load categories!"}>
      {/* Add New Category Form */}
      <Typography variant="h6" fontWeight={600} gutterBottom>
        Add New Category
      </Typography>
      <Box component="form" onSubmit={handleAddCategory} sx={{ display: "flex", gap: 2, mb: 4 }}>
        <TextField id="new-category-name" label="Category Name" variant="outlined"
          value={newCategoryName} onChange={handleChangeNewCategoryName} required sx={{ flex: 1 }} size="small" />
        <Button type="submit" variant="contained" color="primary" sx={{ minWidth: 160 }} disableElevation>
          Add Category
        </Button>
      </Box>

      {/* Categories Table */}
      <Typography variant="h6" fontWeight={600} gutterBottom>
        Existing Categories
      </Typography>
      <TableContainer component={Paper} variant="outlined">
        <Table aria-label="categories table">
          <TableHead>
            <TableRow>
              <TableCell>Category ID</TableCell>
              <TableCell>Category Name</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {categories.length === 0 ? (
              <TableRow>
                <TableCell colSpan={3} align="center">
                  <Typography variant="body1" color="text.secondary">
                    No categories found.
                  </Typography>
                </TableCell>
              </TableRow>
            ) : (
              categories.map((category) => (
                <TableRow key={category.id}>
                  <TableCell>{category.id}</TableCell>
                  <TableCell>
                    {editingId === category.id ? (
                      <TextField value={editedCategoryName} onChange={handleChangeEditedCategoryName}
                        size="small" variant="standard" autoFocus sx={{ minWidth: 200 }} />
                    ) : (
                      category.name
                    )}
                  </TableCell>
                  <TableCell align="center">
                    {editingId === category.id ? (
                      <Box display="flex" gap={1} justifyContent="center">
                        <Button color="primary" variant="contained" size="small" onClick={handleUpdateCategory}>
                          Save
                        </Button>
                        <Button color="inherit" variant="outlined" size="small" onClick={handleCancelEdit}>
                          Cancel
                        </Button>
                      </Box>
                    ) : (
                      <Box display="flex" gap={1} justifyContent="center">
                        <Button color="primary" variant="outlined" size="small" onClick={() => handleEditCategory(category)}>
                          Edit
                        </Button>
                        <Button color="error" variant="outlined" size="small" onClick={() => handleDeleteCategory(category.id)}>
                          Delete
                        </Button>
                      </Box>
                    )}
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </PageContainer>
  );
}