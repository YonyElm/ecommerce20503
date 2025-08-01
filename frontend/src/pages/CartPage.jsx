import React, { useContext, useEffect } from "react";
import { CartContext } from "../context/CartContext";
import CartItem from "../components/CartItem";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import NotFound from "../components/NotFound";
import PageContainer from "../components/PageContainer";
import {Box, Typography, Grid, Paper, Divider, Button,
} from "@mui/material";

const SHIPPING_COST = 0; // or any other value

const CartPage = () => {
    const authContext = useContext(AuthContext);
    const navigate = useNavigate();

    useEffect(() => {
        if (!authContext.loading && !authContext.user) {
            navigate("/");
        }
    }, [authContext.user, authContext.loading, navigate]);

    const { cartItems, updateQuantity, removeItem } = useContext(CartContext);
    const subtotal = cartItems?.reduce((sum, item) => sum + item.price * item.quantity, 0);
    const totalItems = cartItems?.reduce((sum, item) => sum + item.quantity, 0);
    const shippingAmount = cartItems.length > 0 ? SHIPPING_COST : 0;
    const total = subtotal + shippingAmount;

    return (
      <PageContainer title="Shopping Cart">
          <Grid container spacing={2}>
              <Grid flex={1}>
                  {!cartItems?.length ? (
                    <NotFound message="Your cart is empty." />
                  ) : (
                    cartItems.map((item) => (
                      <Box key={item.itemId}>
                          <CartItem item={item} updateQuantity={updateQuantity} removeItem={removeItem}/>
                      </Box>
                    ))
                  )}
              </Grid>
              <Grid>
                  <Paper variant="outlined" sx={{ p: 2}}>
                      <Typography variant="h6" fontWeight={600} gutterBottom>
                          Order Summary
                      </Typography>
                      <Box display="flex" justifyContent="space-between">
                          <Typography variant="body1">
                              Subtotal ({totalItems} items):
                          </Typography>
                          <Typography variant="body1">
                              ${subtotal?.toFixed(2)}
                          </Typography>
                      </Box>
                      <Box display="flex" justifyContent="space-between">
                          <Typography variant="body1">Shipping:</Typography>
                          {SHIPPING_COST === 0 ? (
                            <Typography color="success.main" fontWeight={600}>
                                FREE
                            </Typography>
                          ) : (
                            <Typography>${shippingAmount.toFixed(2)}</Typography>
                          )}
                      </Box>
                      <Divider sx={{ my: 2 }} />
                      <Box display="flex" justifyContent="space-between" mb={2}>
                          <Typography variant="subtitle1" fontWeight={700}>
                              Order Total:
                          </Typography>
                          <Typography variant="subtitle1" fontWeight={700}>
                              ${cartItems.length > 0 ? total?.toFixed(2) : "0.00"}
                          </Typography>
                      </Box>
                      <Button variant="contained" size="large" fullWidth disabled={cartItems.length === 0}
                        onClick={() => navigate("/checkout")}>
                          Proceed to Checkout
                      </Button>
                  </Paper>
              </Grid>
          </Grid>
      </PageContainer>
    );
};

export default CartPage;