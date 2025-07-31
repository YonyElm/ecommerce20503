import React from "react";
import { Box, Container, Grid, Typography, Link as MUILink, Divider } from "@mui/material";

const linkSections = [
    {
        title: "Get to Know Us",
        links: [
            { label: "About Us", href: "#" },
            { label: "Careers", href: "#" },
            { label: "Press Releases", href: "#" },
            { label: "Blog", href: "#" },
        ],
    },
    {
        title: "Connect with Us",
        links: [
            { label: "Github", href: "#" },
            { label: "LinkedIn", href: "#" },
        ],
    },
    {
        title: "Make Money with Us",
        links: [
            { label: "Sell on Our Platform", href: "#" },
            { label: "Affiliate Program", href: "#" },
            { label: "Advertise Your Products", href: "#" },
        ],
    },
    {
        title: "Let Us Help You",
        links: [
            { label: "Your Account", href: "#" },
            { label: "Returns Centre", href: "#" },
            { label: "Help", href: "#" },
            { label: "Contact Us", href: "#" },
        ],
    },
];

const Footer = () => {
    return (
      <Box component="footer" sx={{backgroundColor: "customColors.headerDark", mt: 4, pt: 4, pb: 4,}}>
          <Container maxWidth="lg">
              <Grid container spacing={2} justifyContent="space-between" alignItems="flex-start">
                  {linkSections.map((section) => (
                    <Grid  key={section.title}>
                        <Typography variant="subtitle1" sx={{ fontWeight: "bold", color: "common.white" }} gutterBottom>
                            {section.title}
                        </Typography>
                        <Box component="ul">
                            {section.links.map((link) => (
                              <li key={link.label}>
                                  <MUILink href={link.href} underline="hover" variant="body2"
                                    color="text.secondary" sx={{display: "inline-block"}}>
                                      {link.label}
                                  </MUILink>
                              </li>
                            ))}
                        </Box>
                    </Grid>
                  ))}
              </Grid>
              <Divider sx={{ mt: 4, bgcolor: "background.default" }} />
              <Typography variant="body2" align="center" sx={{ pt: 4, color: "text.secondary" }}>
                  &copy; 2025 Ecommerce, Inc. All rights reserved.
              </Typography>
          </Container>
      </Box>
    );
};

export default Footer;