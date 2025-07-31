import React from "react";
import {Typography, Card, CardContent, Box, Table, TableBody, TableCell, TableContainer, TableHead,
  TableRow, Paper, Button, Chip, CircularProgress, Alert,} from "@mui/material";
import { UsersComponentContext } from "../context/UsersComponentContext";
import Spinner from "./Spinner";

export default function UsersComponent() {
  const {users, loading, error, handleBlockUser, handleActivateUser,} = UsersComponentContext();

  return (
    <Box width="100%" maxWidth="lg" mx="auto" sx={{ mt: 4, px: 2 }}>
      <Typography variant="h4" fontWeight={700} gutterBottom>
        Manage Users
      </Typography>
      <Card sx={{ width: "100%" }} variant="outlined">
        <CardContent>
          {loading ? (
            <Box display="flex" justifyContent="center" alignItems="center" sx={{ py: 4 }}>
              <Spinner />
            </Box>
          ) : error ? (
            <Alert severity="error">Could not load users.</Alert>
          ) : (
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
                        <Typography variant="body2" fontWeight={500}>
                          {user.fullName}
                        </Typography>
                      </TableCell>
                      <TableCell>{user.email}</TableCell>
                      <TableCell>
                        <Typography variant="body2">
                          {user.roleName}
                        </Typography>
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
                              variant="outlined"
                              color="error"
                              size="small"
                              onClick={() => handleActivateUser(user.id, false)}
                              sx={{ minWidth: 90, textTransform: "none" }}
                            >
                              Block
                            </Button>
                          ) : (
                            <Button
                              variant="outlined"
                              color="success"
                              size="small"
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
          )}
        </CardContent>
      </Card>
    </Box>
  );
}