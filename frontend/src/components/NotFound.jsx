import React from "react";
import { Link as RouterLink } from "react-router-dom";
import { Box, Typography, Link } from "@mui/material";

const NotFound = ({ message }) => (
  <Box>
    <Typography variant="body1">{message}</Typography>
    <Link component={RouterLink} to="/" color="primary">
      Start Shopping
    </Link>
  </Box>
);

export default NotFound;
