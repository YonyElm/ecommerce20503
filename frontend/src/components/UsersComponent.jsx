import React from "react";
import {
  Typography, Box, Table, TableBody, TableCell, TableContainer, TableHead,
  TableRow, Paper, Button, Chip,
} from "@mui/material";
import { UsersComponentContext } from "../context/UsersComponentContext";
import PageContainer from "./PageContainer";

export default function UsersComponent() {
  const {
    users, loading, error, handleActivateUser,
  } = UsersComponentContext();

  return (
    <PageContainer title="Manage Users" loading={loading} error={error && "Could not load users."}>
      <TableContainer component={Paper} variant="outlined">
        <Table aria-label="users table">
          <TableHead>
            <TableRow sx={{ backgroundColor: "grey.50" }}>
              <TableCell>User ID</TableCell>
              <TableCell>Name</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Role</TableCell>
              <TableCell>Status</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {users.length === 0 ? (
              <TableRow>
                <TableCell colSpan={6} align="center" sx={{ py: 4 }}>
                  <Typography variant="body1" color="text.secondary">
                    No users found.
                  </Typography>
                </TableCell>
              </TableRow>
            ) : (
              users.map(user => (
                <TableRow key={user.id}>
                  <TableCell>{user.id}</TableCell>
                  <TableCell>
                    <Typography variant="body2" fontWeight={500}>{user.fullName}</Typography>
                  </TableCell>
                  <TableCell>{user.email}</TableCell>
                  <TableCell>
                    <Typography variant="body2">{user.roleName}</Typography>
                  </TableCell>
                  <TableCell>
                    <Chip
                      label={user.isActive ? "Active" : "Inactive"}
                      color={user.isActive ? "success" : "error"}
                      size="small"
                      sx={{ fontWeight: 600 }}
                    />
                  </TableCell>
                  <TableCell align="center">
                    <Box display="flex" justifyContent="center" gap={1}>
                      {user.isActive ? (
                        <Button
                          variant="outlined" color="error" size="small"
                          onClick={() => handleActivateUser(user.id, false)}
                          sx={{ minWidth: 90, textTransform: "none" }}
                        >
                          Block
                        </Button>
                      ) : (
                        <Button
                          variant="outlined" color="success" size="small"
                          onClick={() => handleActivateUser(user.id, true)}
                          sx={{ minWidth: 90, textTransform: "none" }}
                        >
                          Activate
                        </Button>
                      )}
                    </Box>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </PageContainer>
  );
}