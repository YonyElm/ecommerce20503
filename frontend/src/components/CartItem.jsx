import React from "react";
import { useNavigate } from "react-router-dom";
import {Card, CardContent, CardMedia, Box, Typography, TextField, Button} from "@mui/material";

const CartItem = ({ item, updateQuantity, removeItem }) => {
  const navigate = useNavigate();

  if (!item?.price || !item?.name || !item?.itemId || !item?.productId || !item?.quantity) {
    console.error(
      "CartItem: Missing required prop(s).",
      {
        price: item?.price,
        name: item?.name,
        itemId: item?.itemId,
        productId: item?.productId,
        quantity: item?.quantity,
      }
    );
    return null;
  }

  const handleItemClick = (e) => {
    if (e.target.closest("input") || e.target.closest("button") || e.target.closest("a")) {
      return;
    }
    navigate(`/details/${item.productId}`);
  };

  return (
    <Card onClick={handleItemClick} variant="outlined"
      sx={{display: "flex", alignItems: "center", mb: 2, p: 2, bgcolor: "background.paper", cursor: "pointer",
        "&:hover": { bgcolor: "grey.50" }
      }}>
      <CardMedia component="div"
        sx={{width: 96, height: 96, display: "flex", alignItems: "center", justifyContent: "center",}}>
        {item.imageURL ? (
          <img src={item.imageURL} alt={item.name}
               style={{objectFit: "contain", width: "100%", height: "100%", borderRadius: 8,}}/>
        ) : (
          <Typography color="text.secondary" variant="body2">
            [Image]
          </Typography>
        )}
      </CardMedia>
      <CardContent sx={{ flex: 1}}>
        <Typography variant="h6" fontWeight={600}>
          {item.name}
        </Typography>
        <Typography variant="subtitle1" color="text.secondary" fontWeight={700}>
          ${item.price.toFixed(2)}
        </Typography>
      </CardContent>
      <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
        <TextField type="number" size="small" label="Qty"
          inputProps={{ min: 1, style: { textAlign: "center", width: 60 } }}
          value={item.quantity} onClick={e => e.stopPropagation()}
          onChange={e =>
            updateQuantity(item.productId, Math.max(1, Number(e.target.value)), false)
          }
        />
        <Button color="error" variant="outlined" size="small"
          onClick={e => { e.stopPropagation(); removeItem(item.itemId); }}>
          Remove
        </Button>
      </Box>
    </Card>
  );
};

export default CartItem;