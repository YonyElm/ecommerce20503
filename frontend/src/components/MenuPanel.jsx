import React from "react";
import Spinner from "./Spinner";
import CategoryContext from "../context/CategoryContext";
import CategoryCard from "./CategoryCard";
import {Box, Paper, Typography, List, ListItem} from "@mui/material";

/**
 * Sidebar panel using CategoryContext for category data.
 * Each category is shown with a CategoryCard.
 */
function CategoryPanel({ selectedCategoryIds, onSelectionChange }) {
    const { categories, loading } = CategoryContext();

    function handleCategoryClick(categoryId) {
        const next = selectedCategoryIds.includes(categoryId)
            ? [] // Deselect if clicking the same category
            : [categoryId]; // Select only this category, deselecting all others
        onSelectionChange(next);
    }

    if (!loading && !categories?.length) {
        return null;
    }

    return (
      <Box sx={{maxWidth: 250}}>
          <Paper sx={{p: 2}}>
              <Typography variant="h5" fontWeight="bold">
                  Shop by Category
              </Typography>
              {loading ? (
                <Box display="flex" justifyContent="center" alignItems="center">
                    <Spinner />
                </Box>
              ) : (
                <List>
                    {categories.map((category, i) => (
                      <React.Fragment key={category.id}>
                          <ListItem disableGutters>
                              <CategoryCard category={category}
                                selected={selectedCategoryIds.includes(category.id)}
                                onClick={() => handleCategoryClick(category.id)}/>
                          </ListItem>
                      </React.Fragment>
                    ))}
                </List>
              )}
          </Paper>
      </Box>
    );
}

export default CategoryPanel;