import { useContext } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { CartContext } from "../context/CartContext";
import {Typography, AppBar, Toolbar, IconButton, Badge, Box, Button, Stack} from "@mui/material";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import StorefrontIcon from "@mui/icons-material/Storefront";

function Navbar() {
    const { user } = useContext(AuthContext);
    const { cartItems } = useContext(CartContext);

    return (
      <AppBar position="sticky" sx={{backgroundColor: 'customColors.headerDark'}}>
          <Toolbar>
              {/* Logo with Shop Icon */}
              <Link to="/">
                  <Box display="flex" alignItems="center">
                      <StorefrontIcon sx={{ fontSize: 34, marginRight: 1, color: 'primary.main'}} />
                      <Typography variant="h5" component="span" sx={{ fontWeight: 'bold' }}>
                          <Box component="span" sx={{ color: 'primary.main'}}>E</Box>
                          <Box component="span" sx={{ display: { xs: 'none', sm: 'inline' } }}>
                              commerce20503
                          </Box>
                          <Box component="span" sx={{ display: { xs: 'inline', sm: 'none' } }}>
                              20503
                          </Box>
                      </Typography>
                  </Box>
              </Link>
              <Box flexGrow={1} />
              {/* Navigation */}
              <Stack direction="row" alignItems="center">
                  {/* Cart */}
                  <Link to={user ? "/cart" : "/login"} style={{ color: "inherit" }}>
                      <IconButton size="large" color="inherit">
                          <Badge badgeContent={user && cartItems?.length ? cartItems.length : 0} color="primary" overlap="circular">
                              <ShoppingCartIcon sx={{ fontSize: 34 }} />
                          </Badge>
                      </IconButton>
                  </Link>
                  {/* Account Info */}
                  <Button color="inherit"
                          component={Link}
                          to={user ? "/settings" : "/login"}
                          sx={{ textTransform: 'none' }}>
                      <Box textAlign="left">
                          <Typography variant="body2">
                              {user && user.email ? `Hello, ${user.email}` : "Hello, Sign In"}
                          </Typography>
                          <Typography variant="subtitle2" sx={{ fontWeight: "bold" }}>
                              Account & Lists
                          </Typography>
                      </Box>
                  </Button>
                  {/* Orders */}
                  <Button color="inherit"
                      component={Link}
                      to={user ? "/orders" : "/login"}
                      sx={{ textTransform: 'none' }}
                  >
                      <Box textAlign="left">
                          <Typography variant="body2">Returns</Typography>
                          <Typography variant="subtitle2" sx={{ fontWeight: "bold" }}>& Orders</Typography>
                      </Box>
                  </Button>
              </Stack>
          </Toolbar>
      </AppBar>
    );
}

export default Navbar;