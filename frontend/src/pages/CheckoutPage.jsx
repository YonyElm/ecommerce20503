import React, { useState } from "react";
import CheckoutContext from "../context/CheckoutContext";
import { useNavigate } from "react-router-dom";
import {Box, Typography, Grid, Card, CardContent, MenuItem, Button, Alert, TextField,} from "@mui/material";
import PageContainer from "../components/PageContainer";

const CheckoutPage = () => {
    const {
        shippingAddressList,
        paymentMethodList,
        isLoading,
        subtotalPrice,
        shippingCost,
        totalItems,
        totalPrice,
        productId,
        submitPlaceOrderForm,
        checkoutError
    } = CheckoutContext();
    const navigate = useNavigate();

    const [selectedAddressId, setSelectedAddressId] = useState("");
    const [selectedPaymentId, setSelectedPaymentId] = useState("");
    const isButtonDisabled = !selectedAddressId || !selectedPaymentId;

    return (
      <PageContainer title="Checkout" loading={isLoading}>
          <Grid container spacing={2}>
              <Grid sx={{flex: 1}}>
              <Box component="form" fullWidth
                  onSubmit={e => submitPlaceOrderForm(e, selectedAddressId, selectedPaymentId, productId, totalItems, navigate)}>
                  {/* Shipping Address */}
                  <Card variant="outlined" sx={{ mb: 2 }}>
                      <CardContent>
                          <Typography variant="h6" sx={{ mb: 2 }} >
                              Shipping Address
                          </Typography>
                          <TextField label="Select Shipping Address" name="shipping_address" select
                            value={selectedAddressId} onChange={e => setSelectedAddressId(e.target.value)}
                            fullWidth size="small">
                              {shippingAddressList.length === 0 ? (
                                <MenuItem value="" disabled>
                                    No shipping addresses available
                                </MenuItem>
                              ) : (
                                shippingAddressList.map(address => (
                                  <MenuItem key={address.id} value={address.id}>
                                      {address.fullName}, {address.addressLine1}, {address.city}
                                  </MenuItem>
                                ))
                              )}
                          </TextField>
                      </CardContent>
                  </Card>

                  {/* Payment Method */}
                  <Card variant="outlined" sx={{ mb: 2 }}>
                      <CardContent>
                          <Typography variant="h6" sx={{ mb: 2 }}>
                              Payment Method
                          </Typography>
                          <TextField label="Select Payment Method" name="payment_method" select
                            value={selectedPaymentId} onChange={e => setSelectedPaymentId(e.target.value)}
                            fullWidth size="small">
                              {paymentMethodList.length === 0 ? (
                                <MenuItem value="" disabled>
                                    No payment methods available
                                </MenuItem>
                              ) : (
                                paymentMethodList.map(payment => (
                                  <MenuItem key={payment.id} value={payment.id}>
                                      {payment.name}
                                  </MenuItem>
                                ))
                              )}
                          </TextField>
                      </CardContent>
                  </Card>

                  {/* Order Review */}
                  <Card variant="outlined" sx={{ mb: 2 }}>
                      <CardContent>
                          <Typography variant="h6" sx={{ mb: 2 }}>
                              Order Review
                          </Typography>
                          <Box sx={{ display: "flex", justifyContent: "space-between", mb: 1 }}>
                              <Typography>Items ({totalItems}):</Typography>
                              <Typography>${subtotalPrice?.toFixed(2)}</Typography>
                          </Box>
                          <Box sx={{ display: "flex", justifyContent: "space-between", mb: 1 }}>
                              <Typography>Shipping:</Typography>
                              <Typography color={shippingCost === 0 ? "success.main" : "inherit"} fontWeight={shippingCost === 0 ? 600 : "normal"}>
                                  {shippingCost === 0 ? "FREE" : `$${shippingCost?.toFixed(2)}`}
                              </Typography>
                          </Box>
                          <Box sx={{ display: "flex", justifyContent: "space-between", mt: 2, fontWeight: 700 }}>
                              <Typography>Order Total:</Typography>
                              <Typography>${totalPrice?.toFixed(2)}</Typography>
                          </Box>
                      </CardContent>
                  </Card>

                  {checkoutError && (
                    <Alert severity="error" sx={{ mb: 2 }}>
                        {checkoutError}
                    </Alert>
                  )}

                  <Button type="submit" variant="contained" color="primary" fullWidth size="large" disabled={isButtonDisabled}>
                      Place Your Order
                  </Button>
              </Box>
              </Grid>

              {/* Sidebar Summary */}
              <Grid>
              <Card variant="outlined">
                  <CardContent>
                      <Typography variant="h6" sx={{ mb: 2 }}>
                          Order Summary
                      </Typography>
                      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 1, gap: 10 }}>
                          <Typography variant="body2">Items ({totalItems}):</Typography>
                          <Typography variant="body2">${subtotalPrice?.toFixed(2)}</Typography>
                      </Box>
                      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 1, gap: 10  }}>
                          <Typography variant="body2">Shipping:</Typography>
                          <Typography variant="body2" color={shippingCost === 0 ? "success.main" : "inherit"} fontWeight={shippingCost === 0 ? 600 : "normal"}>
                              {shippingCost === 0 ? "FREE" : `$${shippingCost?.toFixed(2)}`}
                          </Typography>
                      </Box>
                      <Box sx={{ display: "flex", justifyContent: "space-between", mt: 2, fontWeight: 700, gap: 10  }}>
                          <Typography variant="body1" fontWeight={700}>Order Total:</Typography>
                          <Typography variant="body1" fontWeight={700}>${totalPrice?.toFixed(2)}</Typography>
                      </Box>
                  </CardContent>
              </Card>
              </Grid>
          </Grid>
      </PageContainer>
    );
};

export default CheckoutPage;