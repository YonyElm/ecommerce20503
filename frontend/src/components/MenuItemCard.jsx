import React from "react";
import { ButtonBase, Box, Typography } from "@mui/material";
import CheckIcon from "@mui/icons-material/Check";

/**
 * Displays a single menu item. Supports selection highlighting and click handling.
 */
function MenuItemCard({ item, selected, onClick }) {
  return (
    <ButtonBase onClick={() => onClick(item.id)} aria-pressed={selected}
      sx={{width: "100%", textAlign: "left", p: 1, alignItems: "center", display: "flex",}}>
      <Box sx={{width: 25, height: 25, display: "flex", alignItems: "center", justifyContent: "center",
          mr: 2, borderRadius: "50%", border: 1, bgcolor: selected ? "primary.main" : "common.white",}}>
        {selected && <CheckIcon sx={{ color: "common.white" }} />}
      </Box>
      <Typography variant="body1" sx={{ flex: 1 }}>
        {item.name}
      </Typography>
    </ButtonBase>
  );
}

export default MenuItemCard;