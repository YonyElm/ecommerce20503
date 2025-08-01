import React, { useState } from "react";
import { MdImageNotSupported } from "react-icons/md";
import { useNavigate } from "react-router-dom";
import {Alert, Card, CardContent, Typography, Box, Grid, Chip, Stack, Tooltip,} from "@mui/material";

const OrderItem = ({ order }) => (
  <Card variant="outlined" sx={{backgroundColor: "background.paper", width: "100%",}}>
    <CardContent>
      <Box display="flex" alignItems="center" justifyContent="space-between">
        <Typography variant="h6" fontWeight={600}>
          Order #{order.order.id}
        </Typography>
        <Typography variant="subtitle2" color="text.secondary">
          Order Date: {order.order.orderDate}
        </Typography>
      </Box>
      <Typography variant="subtitle1" fontWeight={500} gutterBottom>
        Items in this order:
      </Typography>
      <Stack direction="column" spacing={2}>
        {order.orderItemList.map((orderItemObj) => (
          <OrderProduct
            key={orderItemObj.orderItem.id}
            item={orderItemObj.orderItem}
            status={orderItemObj.statusList}
          />
        ))}
      </Stack>
      <Box display="flex" justifyContent="flex-end" mt={1}>
        <Box textAlign="right">
          <Typography variant="subtitle1" fontWeight={600} display="inline">
            Order Total:
          </Typography>
          <Typography component="span" variant="h6" fontWeight={700} color="success.main">
            ${order.order.totalAmount.toFixed(2)}
          </Typography>
        </Box>
      </Box>
    </CardContent>
  </Card>
);

const OrderProduct = ({ item, status }) => {
  const [imageError, setImageError] = useState(false);
  const navigate = useNavigate();
  const handleItemClick = (e) => {
    e.stopPropagation();
    navigate(`/details/${item.product.id}`);
  };

// Product fallback
  if (!item || !item.product) {
    return (
      <Alert severity="error" sx={{ mb: 1 }}>
        <Typography variant="body1" fontWeight={500} gutterBottom>
          Product details unavailable
        </Typography>
      </Alert>
    );
  }

  return (
    <Card variant="outlined"
      sx={{cursor: "pointer", "&:hover": {bgcolor: "grey.100"}}}
      onClick={handleItemClick}>
      <CardContent>
        <Grid container alignItems="center" spacing={2}>
          <Grid>
            <Box
              sx={{width: 56, height: 56, overflow: "hidden", display: "flex",
                alignItems: "center", justifyContent: "center",}}>
              {!item.product.image || imageError ? (
                <MdImageNotSupported size={36} data-testid="broken-img-icon" />
              ) : (
                <img
                  alt={item.product.name}
                  src={item.product.image}
                  style={{ width: "100%", height: "100%", objectFit: "cover" }}
                  onError={() => setImageError(true)}
                />
              )}
            </Box>
          </Grid>
          <Grid flex={1}>
            <Typography variant="body1" fontWeight={500} noWrap>
              {item.product.name}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Qty: {item.quantity} | ${item.product.price.toFixed(2)}
              {item.quantity > 1 && " each"}
            </Typography>
          </Grid>
          <Grid>
            <Stack spacing={1} alignItems="flex-end">
              <Chip size="small" color="info" label={`Status: ${status?.[0]?.status ?? "N/A"}`}/>
              <Tooltip title="Line Total" arrow>
                <Typography variant="body1" fontWeight={600}>
                  Total: ${(item.quantity * item.product.price).toFixed(2)}
                </Typography>
              </Tooltip>
            </Stack>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  );
};

export default OrderItem;