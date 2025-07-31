import React, { useState } from "react";
import ProductGrid from "../components/ProductGrid";
import CategoryContext from "../context/CategoryContext";
import MenuPanel from "../components/MenuPanel";
import { Box, Grid, Container } from "@mui/material";

const Home = () => {
  const [selectedMenuIds, setSelectedMenuIds] = useState([]);
  const { categories, loading } = CategoryContext();

    return (
      <Container maxWidth="xl" sx={{ py: 4 }}>
          <Grid container spacing={4}>
              <Box sx={{ border: 0 }}>
                <MenuPanel items={categories} loading={loading}
                  selectedItemIds={selectedMenuIds} onSelectionChange={setSelectedMenuIds} title="Shop by Category"/>
              </Box>
              <Box sx={{ border: 0, flex: 1 }}>
                <ProductGrid selectedCategories={selectedMenuIds} />
              </Box>
          </Grid>
      </Container>
    );
};

export default Home;