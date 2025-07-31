import React from "react";
import { Box, Paper, Typography, List, ListItem } from "@mui/material";
import Spinner from "./Spinner";
import MenuItemCard from "./MenuItemCard";

/**
 * Generic sidebar/menu panel for displaying a list of menu items.
 */
function MenuPanel({items = [], loading = false, selectedItemIds = [],
  onSelectionChange, title = "Menu", oneAlwaysOn = false}) {
  function handleItemClick(itemId) {
    if (oneAlwaysOn) {
      onSelectionChange([itemId]);
    } else {
      const next = selectedItemIds.includes(itemId) ? [] : [itemId];
      onSelectionChange(next);
    }
  }

  if (!loading && (!items || !items.length)) {
    return null;
  }

  return (
    <Box sx={{ maxWidth: 250 }}>
      <Paper sx={{ p: 2 }}>
        <Typography variant="h5" fontWeight="bold">
          {title}
        </Typography>
        {loading ? (
          <Box display="flex" justifyContent="center" alignItems="center">
            <Spinner />
          </Box>
        ) : (
          <List>
            {items.map((item, i) => (
              <ListItem disableGutters key={item.id}>
                <MenuItemCard item={item} selected={selectedItemIds.includes(item.id)} onClick={handleItemClick}/>
              </ListItem>
            ))}
          </List>
        )}
      </Paper>
    </Box>
  );
}

export default MenuPanel;