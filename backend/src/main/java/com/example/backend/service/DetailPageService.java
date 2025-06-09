package com.example.backend.service;

import com.example.backend.dao.ProductDAO;
import com.example.backend.dao.InventoryDAO;
import com.example.backend.viewModel.DetailPageViewModel;
import com.example.backend.model.Product;
import com.example.backend.model.Category;
import com.example.backend.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetailPageService {

    private final ProductDAO productDAO;
    private final InventoryDAO inventoryDAO;

    @Autowired
    public DetailPageService(ProductDAO productDAO,
                          InventoryDAO inventoryDAO) {
        this.productDAO = productDAO;
        this.inventoryDAO = inventoryDAO;
    }

    public DetailPageViewModel getProductDetailById(int id) {
        Product product = productDAO.getProductById(id);
        if (product == null) return null;

        // Fetch additional related data
        Category category = product.getCategory();
        Inventory inventory = inventoryDAO.findByProductId(product.getId());

        // Assemble view model
        DetailPageViewModel viewModel = new DetailPageViewModel();
        viewModel.setName(product.getName());
        viewModel.setDescription(product.getDescription());
        viewModel.setPrice(product.getPrice());
        viewModel.setCategoryName(category.getName());
        viewModel.setMaxQuantity(inventory.getQuantity());

        return viewModel;
    }
}
