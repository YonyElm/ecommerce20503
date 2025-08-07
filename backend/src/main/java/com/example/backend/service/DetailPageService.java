package com.example.backend.service;

import com.example.backend.dao.ProductDAO;
import com.example.backend.dao.InventoryDAO;
import com.example.backend.viewModel.DetailPageViewModel;
import com.example.backend.model.Product;
import com.example.backend.model.Category;
import com.example.backend.model.Inventory;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DetailPageService {

    private final ProductDAO productDAO;
    private final InventoryDAO inventoryDAO;
    private static final Logger logger = LoggerFactory.getLogger(DetailPageService.class);


    /**
     * Constructor for DetailPageService.
     * @param productDAO DAO for product data access
     * @param inventoryDAO DAO for inventory data access
     */
    @Autowired
    public DetailPageService(ProductDAO productDAO,
                          InventoryDAO inventoryDAO) {
        this.productDAO = productDAO;
        this.inventoryDAO = inventoryDAO;
    }

    /**
     * Retrieves detailed product information by product ID, including category and inventory data.
     * @param id The ID of the product
     * @return DetailPageViewModel containing product details, or null if not found
     */
    @Transactional
    public DetailPageViewModel getProductDetailById(int id) {
        Optional<Product> productOptional = productDAO.findByIdAndIsActiveTrue(id);
        if (productOptional.isEmpty()) {
            return null;
        }
        Product product = productOptional.get();

        // Fetch additional related data
        Category category = product.getCategory();
        if (category == null) {
            logger.warn("Category not found for product id: {}", product.getId());
        }
        Inventory inventory = inventoryDAO.findByProductId(product.getId()).orElse(null);
        if (inventory == null) {
            logger.error("Inventory not found for product id: {}", product.getId());
        }

        // Assemble view model
        DetailPageViewModel viewModel = new DetailPageViewModel();
        viewModel.setId(product.getId());
        viewModel.setName(product.getName());
        viewModel.setDescription(product.getDescription());
        viewModel.setPrice(product.getPrice());
        if (category != null && category.getName() != null) {
            viewModel.setCategoryName(category.getName());
        }
        if (inventory != null && inventory.getQuantity() != null) {
            viewModel.setMaxQuantity(inventory.getQuantity());
        }
        viewModel.setImageURL(product.getImageURL());

        return viewModel;
    }
}
