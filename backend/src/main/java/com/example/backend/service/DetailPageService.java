package com.example.backend.service;

import com.example.backend.dao.ProductDAO;
import com.example.backend.dao.InventoryDAO;
import com.example.backend.viewModel.DetailPageViewModel;
import com.example.backend.model.Product;
import com.example.backend.model.Category;
import com.example.backend.model.Inventory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Transactional
    public DetailPageViewModel getProductDetailById(int id) {
        Optional<Product> productOptional = productDAO.findById(id);
        if (productOptional.isEmpty()) {
            return null;
        }
        Product product = productOptional.get();

        // Fetch additional related data
        Category category = product.getCategory();
        Inventory inventory = inventoryDAO.findByProductId(product.getId());

        // Assemble view model
        DetailPageViewModel viewModel = new DetailPageViewModel();
        viewModel.setId(product.getId());
        viewModel.setName(product.getName());
        viewModel.setDescription(product.getDescription());
        viewModel.setPrice(product.getPrice());
        viewModel.setCategoryName(category.getName());
        viewModel.setMaxQuantity(inventory.getQuantity());

        return viewModel;
    }
}
