import React, { useContext, useEffect, useState } from "react";
import {Box, Card, CardContent, Typography, TextField,
    Button as MuiButton, Link as MUILink, Alert} from "@mui/material";
import { register } from "../api/auth";
import { useNavigate, Link } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

function Register() {
    const [form, setForm] = useState({ email: "", password: "", fullName: "" });
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const authContext = useContext(AuthContext);

    useEffect(() => {
        if (authContext.user) {
            navigate("/");
        }
    }, [authContext.user, navigate]);

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
        setError(null);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await register(form);
            navigate("/login");
        } catch (err) {
            setError("Registration failed");
        }
    };

    return (
      <Box maxWidth="sm" mx="auto"
        sx={{mt: 4, px: 2, display: "flex", justifyContent: "center", alignItems: "center",}}>
          <Card sx={{ width: "70%"}}>
              <CardContent>
                  <Typography variant="h5" fontWeight="bold" gutterBottom>
                      Register
                  </Typography>
                  <Box component="form" onSubmit={handleSubmit}>
                      <TextField type="email" name="email" label="Email" placeholder="Email" size="small"
                        fullWidth variant="outlined" margin="normal" value={form.email}
                        onChange={handleChange} required/>
                      <TextField type="password" name="password" label="Password" placeholder="Password" size="small"
                        fullWidth variant="outlined" margin="normal" value={form.password} onChange={handleChange}
                        required/>
                      <TextField type="text" name="fullName" label="Full Name" placeholder="Full Name" fullWidth size="small"
                        variant="outlined" margin="normal" value={form.fullName} onChange={handleChange} required/>
                      {error && (
                        <Alert severity="error" sx={{ mt: 1, mb: 1 }}>
                            {error}
                        </Alert>
                      )}
                      <MuiButton type="submit" variant="contained" color="primary" size="medium" fullWidth sx={{ mt: 2 }}>
                          Sign Up
                      </MuiButton>
                  </Box>
                  <Box sx={{ mt: 2, textAlign: "center" }}>
                      <Typography variant="body2" component="span" color="text.primary">
                          Already have an account?{" "}
                      </Typography>
                      <MUILink component={Link} to="/login" color="secondary" underline="hover" fontWeight="medium">
                          Sign in
                      </MUILink>
                  </Box>
              </CardContent>
          </Card>
      </Box>
    );
}

export default Register;