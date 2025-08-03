
import React, { useState } from "react";
import MenuPanel from '../components/MenuPanel';
import StorePage from './StorePage';
import OrdersPage from './OrdersPage';
import { Box, Grid, Container } from "@mui/material";
import UsersComponent from "../components/UsersComponent";
import CategoriesPage from "../components/CategoriesComponent";

const AdminPage = () => {
  const [selectedMenuIds, setSelectedMenuIds] = useState([1]);

  const categories = [{id: 1, name: "Users"}, {id: 2, name: "Products"}, {id: 3, name: "Orders"}, {id: 4, name: "Categories"}];
    return (
        <Container maxWidth="xl">
            <Grid container spacing={4}>
                <Box sx={{py: 4 }}>
                <MenuPanel items={categories} selectedItemIds={selectedMenuIds} onSelectionChange={setSelectedMenuIds} title="Admin Menu" oneAlwaysOn={true}/>
                </Box>
                <Box sx={{flex: 1}}>
                    {selectedMenuIds.includes(1) && <UsersComponent />}
                    {selectedMenuIds.includes(2) && <StorePage />}
                    {selectedMenuIds.includes(3) && <OrdersPage adminFlag={true}/>}
                    {selectedMenuIds.includes(4) && <CategoriesPage />}
                </Box>
            </Grid>
        </Container>
    );
};

export default AdminPage;
