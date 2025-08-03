import React from "react";
import { MdImageNotSupported } from "react-icons/md";
import {Alert, Card, CardContent, Typography, Box, Grid, Chip, Stack, Tooltip, Button, Menu, MenuItem,} from "@mui/material";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";
import {OrderItemContext} from "../context/OrdersContext";

const OrderItem = ({ order, triggerOrdersRefresh}) => (
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
            triggerOrdersRefresh={triggerOrdersRefresh}
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

const OrderProduct = ({ item, status, triggerOrdersRefresh }) => {
  const {anchorMenuElement, handleMenuOpen, handleMenuClose, handleMenuAction, handleLinkToDetailPage,} = OrderItemContext(item, triggerOrdersRefresh);

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
      onClick={handleLinkToDetailPage}>
      <CardContent>
        <Grid container alignItems="center" spacing={2}>
          <Grid>
            <Box
              sx={{width: 56, height: 56, overflow: "hidden", display: "flex",
                alignItems: "center", justifyContent: "center",}}>
              {!item.product.imageURL ? (
                <MdImageNotSupported size={36} data-testid="broken-img-icon" />
              ) : (
                <img
                  alt={item.product.name} src={item.product.imageURL}
                  style={{ width: "100%", height: "100%", objectFit: "cover" }}/>
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
            <Stack direction="row" alignItems="center" spacing={1}>
              {status?.[0]?.nextSteps.length > 0 && (
                <Box>
                  <Button variant="outlined" size="small" endIcon={<ArrowDropDownIcon />} onClick={handleMenuOpen}>
                    Actions
                  </Button>
                  <Menu id="item-actions-menu" anchorEl={anchorMenuElement} open={Boolean(anchorMenuElement)}
                    onClose={handleMenuClose} onClick={(e) => e.stopPropagation()}>
                    {status?.[0]?.nextSteps.map((action) => (
                      <MenuItem key={action} onClick={handleMenuAction(item.id, action)}>
                        {action}
                      </MenuItem>
                    ))}
                  </Menu>
                </Box>
              )}
              <Chip size="small" color="info" label={`Status: ${status?.[0]?.status ?? "N/A"}`}/>
            </Stack>
            <Stack spacing={1} alignItems="flex-end" mt={1}>
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