import React from "react";
import { Box, Card, CardContent, Typography, CircularProgress, Alert } from "@mui/material";

export default function PageContainer({
  title,
  loading,
  error,
  children,
  maxWidth = "lg",
  cardProps = {},
  titleProps = {},
  errorSeverity = "error",
  sx = {},
}) {
  return (
    <Box width="100%" maxWidth={maxWidth} mx="auto" sx={{ mt: 4, px: 2, ...sx }}>
      {title && (
        <Typography
          variant="h4"
          fontWeight={700}
          gutterBottom
          {...titleProps}
        >
          {title}
        </Typography>
      )}
      <Card sx={{ width: "100%" }} variant="outlined" {...cardProps}>
        <CardContent>
          {loading ? (
            <Box display="flex" justifyContent="center" alignItems="center" sx={{ py: 4 }}>
              <CircularProgress />
            </Box>
          ) : error ? (
            <Alert severity={errorSeverity} sx={{ mb: 2 }}>
              {typeof error === "string" ? error : "An error occurred."}
            </Alert>
          ) : (
            children
          )}
        </CardContent>
      </Card>
    </Box>
  );
}