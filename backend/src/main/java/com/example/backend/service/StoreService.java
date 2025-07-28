package com.example.backend.service;

import com.example.backend.dao.*;
import com.example.backend.model.*;
import com.example.backend.viewModel.DetailPageViewModel;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class StoreService {

    private final ProductDAO productDAO;
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final InventoryDAO inventoryDAO;
    private final CategoryDAO categoryDAO;
    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);


    @Autowired
    public StoreService(ProductDAO productDAO, UserDAO userDAO,
                        RoleDAO roleDAO, InventoryDAO inventoryDAO,
                        CategoryDAO categoryDAO) {
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.inventoryDAO = inventoryDAO;
        this.categoryDAO = categoryDAO;
    }

    @Transactional
    public Map<String, Object> getStore(int userId) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Category> categories = categoryDAO.findAll().stream()
                .map( c -> Category.builder()
                        .name(c.getName())
                        .id(c.getId())
                        .build()
                ).toList();
        Optional<User> userOptional = userDAO.findById(userId);
        if (userOptional.isEmpty()) {
            return resultMap;
        }

        List<Role> roles = roleDAO.getUserRoles(userId);
        List<Product> products;

        if (roleMatch(roles, Role.RoleName.ADMIN)) {
            products = productDAO.findByIsActiveTrue();
        } else if (roleMatch(roles, Role.RoleName.SELLER)) {
            products = productDAO.findBySeller_IdAndIsActiveTrue(userId);
        } else {
            products =  Collections.emptyList();
        }


        List<DetailPageViewModel> viewModels = products.stream()
            .map(product -> {
                try {
                    return toViewModel(product);
                } catch (Exception e) {
                    logger.error("Failed to build viewModel for productId={}: {}", product.getId(), e.getMessage());
                    return null; // Skip this product
                }
            })
            .filter(Objects::nonNull)
            .toList();
        resultMap.put("products", viewModels);
        resultMap.put("categories", categories);

        return resultMap;
    }


    @Transactional
    public DetailPageViewModel addStoreProduct(int userId, Map<String, String> productData) {
        User seller = userDAO.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = new Product();
        setProductValues(productData, product);
        product.setSeller(seller);

        Product savedProduct = productDAO.save(product);

        if (productData.containsKey("maxQuantity")) {
            Integer maxQuantity = Integer.parseInt(productData.get("maxQuantity"));
            Inventory inventory = Inventory.builder()
                    .quantity(maxQuantity)
                    .productId(savedProduct.getId()).build();
            inventoryDAO.save(inventory);
        }
        return toViewModel(savedProduct);
    }

    @Transactional
    public DetailPageViewModel updateStoreProduct(int userId, int productId, Map<String, String> productData) {
        Product existing = productDAO.findByIdAndIsActiveTrue(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!isAdminOrSeller(userId, existing)) {
            throw new RuntimeException("Unauthorized operation");
        }

        // Only update allowed fields
        setProductValues(productData, existing);
        Product savedProduct = productDAO.save(existing);

        if (productData.containsKey("maxQuantity")) {
            Integer maxQuantity = Integer.parseInt(productData.get("maxQuantity"));
            Inventory inventory = inventoryDAO.findByProductId(productId)
                    .orElseThrow(() -> new RuntimeException("Inventory not found for product id: " + productId));
            inventory.setQuantity(maxQuantity);
            inventoryDAO.save(inventory);
        }
        return toViewModel(savedProduct);
    }

    private boolean isAdminOrSeller(int userId, Product product) {
        List<Role> roles = roleDAO.getUserRoles(userId);
        boolean isAdmin = roles.stream()
                .anyMatch(role -> Role.RoleName.ADMIN.equals(role.getRoleName()));

        return isAdmin || (product.getSeller() != null && product.getSeller().getId() == userId);
    }

    private void setProductValues(Map<String, String> productData, Product existing) {
        if (productData.containsKey("name")) {
            existing.setName(productData.get("name"));
        }
        if (productData.containsKey("description")) {
            existing.setDescription(productData.get("description"));
        }
        if (productData.containsKey("price")) {
            existing.setPrice(new BigDecimal(productData.get("price")));
        }

        // Optionally set category if your Product entity supports it and category DAO is available
        if(productData.containsKey("categoryName")) {
            String categoryName = productData.get("categoryName");
            Category category = categoryDAO.findFirstByNameIs(categoryName)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existing.setCategory(category);
        }
    }

    private DetailPageViewModel toViewModel(Product product) {
        DetailPageViewModel viewModel = new DetailPageViewModel();
        Inventory inventory = inventoryDAO.findByProductId(product.getId())
                .orElseThrow(() -> new RuntimeException("Inventory not found for product id: " + product.getId()));

        viewModel.setId(product.getId());
        viewModel.setName(product.getName());
        viewModel.setDescription(product.getDescription());
        viewModel.setPrice(product.getPrice());
        if (product.getCategory() != null) {
            viewModel.setCategoryName(product.getCategory().getName());
        }
        viewModel.setMaxQuantity(inventory.getQuantity());

        return viewModel;
    }

    private boolean roleMatch(List<Role> roles, Role.RoleName roleName) {
        return roles.stream().anyMatch(role -> roleName.equals(role.getRoleName()));
    }

    @Transactional
    public ResponseEntity<Void> deleteProduct(int userId, int productId) {

        Optional<Product> optionalProduct = productDAO.findByIdAndIsActiveTrue(productId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Product product = optionalProduct.get();

        if (!isAdminOrSeller(userId, product)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // Delete product
        product.setIsActive(false);
        productDAO.save(product);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
