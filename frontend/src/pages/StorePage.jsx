import React from "react";
import { StorePageContext } from "../context/StorePageContext";
import ProductModal from "../components/modals/ProductModal";
import {
  Typography, Button as MuiButton, Box,
  Table, TableBody, TableCell, TableContainer, TableHead,
  TableRow, Paper, Avatar,
} from "@mui/material";
import PageContainer from "../components/PageContainer";
import NotFound from "../components/NotFound";

export default function StorePage() {
  const {
    loading,
    error,
    products,
    categories,
    isProductModalOpen,
    editingProduct,
    handleOpenAddProduct,
    handleOpenEditProduct,
    handleCloseProductModal,
    handleSubmitProduct,
    handleDeleteProduct,
    navigate,
  } = StorePageContext();

  if (error) {
    return (
      <Box maxWidth="lg" mx="auto" sx={{ mt: 4, px: 2 }}>
        <NotFound message="Could not load store products. Please try again later." />
      </Box>
    );
  }

  return (
    <PageContainer title="Manage Products" loading={loading} error={null}>
      <Box sx={{ display: "flex", justifyContent: "flex-end", mb: 2 }}>
        <MuiButton
          variant="contained" color="primary" onClick={handleOpenAddProduct}
          sx={{ textTransform: "none" }}
        >
          Add New Product
        </MuiButton>
      </Box>
      <TableContainer component={Paper} variant="outlined">
        <Table sx={{ minWidth: 650 }} aria-label="products table">
          <TableHead>
            <TableRow sx={{ backgroundColor: "grey.50" }}>
              <TableCell>Image</TableCell>
              <TableCell>Product Name</TableCell>
              <TableCell align="right">Price</TableCell>
              <TableCell align="right">Stock</TableCell>
              <TableCell>Category</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {products.length === 0 ? (
              <TableRow>
                <TableCell colSpan={6} align="center" sx={{ py: 4 }}>
                  <Typography variant="body1" color="text.secondary">
                    No products found. Add your first product to get started.
                  </Typography>
                </TableCell>
              </TableRow>
            ) : (
              products.map((product) => (
                <TableRow key={product.id}
                  onClick={() => navigate(`/details/${product.id}`)}
                  sx={{ cursor: 'pointer', '&:hover': { backgroundColor: 'action.hover' } }}
                >
                  <TableCell>
                    <Avatar
                      src={product.imageUrl || "https://placehold.co/50x50/e0e0e0/666?text=Img"}
                      alt={product.name}
                      variant="rounded"
                      sx={{ width: 50, height: 50 }}
                    />
                  </TableCell>
                  <TableCell component="th" scope="row">
                    <Typography variant="body2" fontWeight={500}>
                      {product.name}
                    </Typography>
                  </TableCell>
                  <TableCell align="right">
                    <Typography variant="body2">
                      ${product.price?.toFixed(2)}
                    </Typography>
                  </TableCell>
                  <TableCell align="right">
                    <Typography variant="body2">
                      {product.maxQuantity}
                    </Typography>
                  </TableCell>
                  <TableCell>
                    <Typography variant="body2">
                      {product.categoryName}
                    </Typography>
                  </TableCell>
                  <TableCell align="right" sx={{ width: 200 }}>
                    <Box sx={{ display: "flex", gap: 1, justifyContent: "right" }}>
                      <MuiButton variant="outlined" size="small" sx={{ minWidth: 90 }}
                        onClick={(event) => {
                          event.stopPropagation();
                          handleOpenEditProduct(product)
                        }}>
                        Edit
                      </MuiButton>
                      <MuiButton variant="outlined" color="error" size="small" sx={{ minWidth: 90 }}
                        onClick={async (event) => {
                          event.stopPropagation();
                          await handleDeleteProduct(product.id)
                        }}>
                        Delete
                      </MuiButton>
                    </Box>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </TableContainer>
      <ProductModal
        open={isProductModalOpen}
        onClose={handleCloseProductModal}
        onSubmit={handleSubmitProduct}
        product={editingProduct}
        categories={categories}
      />
    </PageContainer>
  );
}