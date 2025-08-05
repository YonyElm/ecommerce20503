package com.example.backend.service;

import com.example.backend.dao.*;
import com.example.backend.model.*;
import com.example.backend.viewModel.DetailPageViewModel;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ResourceLoader;

import java.math.BigDecimal;
import java.util.*;
import java.io.File;
import java.io.IOException;

@Service
public class StoreService {

    private final ProductDAO productDAO;
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final InventoryDAO inventoryDAO;
    private final CategoryDAO categoryDAO;
    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);
    private final ResourceLoader resourceLoader;
    private String imageUploadDir = "file:../frontend/public/product_assets/";
    private static final String IMAGE_URL_DIR = "/product_assets/";

    @Autowired
    public StoreService(ProductDAO productDAO, UserDAO userDAO,
                        RoleDAO roleDAO, InventoryDAO inventoryDAO,
                        CategoryDAO categoryDAO, ResourceLoader resourceLoader,
                        @Value("${image.upload.dir}") String imageUploadDir) {
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.inventoryDAO = inventoryDAO;
        this.categoryDAO = categoryDAO;
        this.resourceLoader = resourceLoader;
        this.imageUploadDir = imageUploadDir;
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
    public DetailPageViewModel addStoreProduct(int userId, Map<String, String> productData, MultipartFile image) {
        User seller = userDAO.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = new Product();
        setProductValues(productData, product, image);
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
    public DetailPageViewModel updateStoreProduct(int userId, int productId, Map<String, String> productData, MultipartFile image) {
        Product existing = productDAO.findByIdAndIsActiveTrue(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!isAdminOrSeller(userId, existing)) {
            throw new RuntimeException("Unauthorized operation");
        }

        setProductValues(productData, existing, image);
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

    private void setProductValues(Map<String, String> productData, Product existing, MultipartFile image) {
        if (productData.containsKey("name")) {
            existing.setName(productData.get("name"));
        }
        if (productData.containsKey("description")) {
            existing.setDescription(productData.get("description"));
        }
        if (productData.containsKey("price")) {
            existing.setPrice(new BigDecimal(productData.get("price")));
        }

        if (productData.containsKey("categoryName")) {
            String categoryName = productData.get("categoryName");
            Category category = categoryDAO.findFirstByNameIs(categoryName)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existing.setCategory(category);
        }

        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImageFile(image);
            existing.setImageURL(imageUrl);
        }
    }

    /**
     * Saves the uploaded image to disk and returns the public path (for your product's imageURL).
     */
    private String saveImageFile(MultipartFile image) {
        File uploadDir;
        try {
            String path = resourceLoader.getResource(imageUploadDir).getFile().getAbsolutePath();
            uploadDir = new File(path);
        } catch (Exception e) {
            throw new RuntimeException("Could not resolve uploads directory path", e);
        }
        try {
            String original = image.getOriginalFilename();
            String ext = "";
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf('.'));
            }
            String uniqueName = UUID.randomUUID() + "-" + System.currentTimeMillis() + ext;
            File dest = new File(uploadDir, uniqueName);
            image.transferTo(dest);

            return IMAGE_URL_DIR  + uniqueName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image file: " + e.getMessage(), e);
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
        viewModel.setImageURL(product.getImageURL());

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
