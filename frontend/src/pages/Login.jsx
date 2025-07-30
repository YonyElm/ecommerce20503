import React, { useState, useContext, useEffect } from "react";
import {Box, Card, CardContent, Typography, TextField,
    Button as MuiButton, Link as MUILink, Alert,} from "@mui/material";
import { login } from "../api/auth";
import { useNavigate, Link } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

function Login() {
    const [form, setForm] = useState({ email: "", password: "" });
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
            const res = await login(form);
            authContext.login(res.data.token);
            navigate("/");
        } catch (err) {
            setError("Invalid credentials");
        }
    };

    return (
      <Box
        maxWidth="sm"
        mx="auto"
        sx={{mt: 8, px: 2, display: "flex",
            justifyContent: "center", alignItems: "center",}}>
          <Card sx={{ width: "70%" }}>
              <CardContent>
                  <Typography variant="h5" fontWeight="bold" gutterBottom>
                      Login
                  </Typography>
                  <Box component="form" onSubmit={handleSubmit}>
                      <TextField type="email" name="email" label="Email" placeholder="Email" size="small"
                        fullWidth variant="outlined" margin="normal" value={form.email} onChange={handleChange} required/>
                      <TextField type="password" name="password" label="Password" placeholder="Password" size="small"
                        fullWidth variant="outlined" margin="normal" value={form.password} onChange={handleChange} required/>
                      {error && (
                        <Alert severity="error" sx={{ mt: 1, mb: 1 }}>
                            {error}
                        </Alert>
                      )}
                      <MuiButton type="submit" variant="contained" color="primary" size="medium" fullWidth sx={{ mt: 2 }}>
                          Login
                      </MuiButton>
                  </Box>
                  <Box sx={{ mt: 2, textAlign: "center" }}>
                      <Typography variant="body2" component="span" color="text.primary">
                          Don't have an account?{" "}
                      </Typography>
                      <MUILink component={Link} to="/register" color="secondary" underline="hover" fontWeight="medium">
                          Sign up
                      </MUILink>
                  </Box>
              </CardContent>
          </Card>
      </Box>
    );
}

export default Login;