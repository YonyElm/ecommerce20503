import React from "react";
import { Link } from "react-router-dom";
import {DetailPageContext} from "../context/DetailPageContext";
import PageContainer from "../components/PageContainer";
import NotFound from "../components/NotFound";
import { MdImageNotSupported } from "react-icons/md";
import {Box, Typography, TextField, Button as MuiButton, Grid, Alert, Stack, Link as MUILink} from "@mui/material";

export default function DetailPage() {
    const {product, isLoading, chosenQuantity, handleChosenQuantityChange, isSignedIn, inStock,
        categoryName, addToCart, buyNow,} = DetailPageContext();

    if (isLoading) {
        return <PageContainer loading title="Product Details" />;
    }

    if (!product) {
        return (
          <PageContainer title="Product Details">
              <NotFound message="Can't find product, Perhaps it is no longer listed for sale." />
          </PageContainer>
        );
    }

    return (
      <PageContainer title="Product Details">
          <Grid container spacing={4} alignItems="stretch">
              <Grid item xs={12} sm={6}>
                  {product.imageURL ? (
                    <Box component="img" src={product.imageURL} alt={product.name} sx={{height: "100%", width: "100%", objectFit: "contain",}}/>
                  ) : (
                    <Box sx={{height: "100%", width: "100%", display: "flex", alignItems: "center", justifyContent: "center", px: 10}}>
                        <MdImageNotSupported size={64} data-testid="broken-img-icon"/>
                    </Box>
                  )}
              </Grid>
              <Grid item xs={12} sm={6}>
                  <Box sx={{display: "flex", flexDirection: "column",}}>
                      <Typography variant="h4" fontWeight={700} gutterBottom>
                          {product.name}
                      </Typography>
                      <Typography color="text.secondary" variant="subtitle2" gutterBottom>
                          Category: {categoryName}
                      </Typography>
                      <Typography color="primary" variant="h5" fontWeight={600} gutterBottom>
                          ${product.price}
                      </Typography>
                      <Typography variant="body1" color="text.secondary" gutterBottom>
                          {product.description}
                      </Typography>
                      <Box mb={2}>
                          <Typography component="span" variant="subtitle1" fontWeight={600} sx={{ color: "text.secondary" }}>
                              Availability:
                          </Typography>
                          {inStock ? (
                            <Typography component="span" fontWeight={600} color="success.main" sx={{ ml: 1 }}>
                                In Stock ({product.maxQuantity} left)
                            </Typography>
                          ) : (
                            <Typography component="span" fontWeight={600} color="error.main" sx={{ ml: 1 }}>
                                Out of Stock
                            </Typography>
                          )}
                      </Box>
                      <Box display="flex" alignItems="center" gap={2} mb={4}>
                          <Typography fontWeight={600} color={!inStock ? "text.disabled" : "text.primary"}
                            sx={{ minWidth: 75 }}>
                              Quantity:
                          </Typography>
                          <TextField id="quantity" name="quantity" type="number" size="small" value={chosenQuantity}
                            onChange={handleChosenQuantityChange}
                            inputProps={{
                                min: 1,
                                max: product.maxQuantity,
                            }}
                            disabled={!inStock || !isSignedIn} sx={{ width: 80 }}/>
                      </Box>
                      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
                          <MuiButton fullWidth variant="contained" color="primary" size="large"
                            sx={{ textTransform: "none", width: 150 }} disabled={!inStock || !isSignedIn} onClick={addToCart}>
                              Add to Cart
                          </MuiButton>
                          <MuiButton fullWidth variant="contained" color="secondary" size="large"
                                     sx={{ textTransform: "none", width: 150  }} disabled={!inStock || !isSignedIn} onClick={buyNow}>
                              Buy Now
                          </MuiButton>
                      </Stack>
                      {!isSignedIn && (
                        <Alert severity="info" sx={{ mt: 3 }}>
                            Please{" "}
                            <MUILink component={Link} to="/login" color="secondary" underline="hover" fontWeight="medium">
                                Sign in
                            </MUILink>
                            {" "}to purchase or add items to your cart.
                        </Alert>
                      )}
                  </Box>
              </Grid>
          </Grid>
      </PageContainer>
    );
}