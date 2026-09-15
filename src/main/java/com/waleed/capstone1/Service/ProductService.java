package com.waleed.capstone1.Service;

import com.waleed.capstone1.Entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private List<Product> products = new ArrayList<>();
    private final CategoryService categoryService;

    public List<Product> getAllProducts() {
        return products;
    }

    public int addProduct(Product product) {
        if (!categoryService.checkCategoryExists(product.getCategoryID())) {
            return 1; // Category not found
        }
        products.add(product);
        return 0; // Success
    }

    public int updateProduct(String id, Product updated) {
        Product existing = null;
        for (Product p : products) {
            if (p.getId().equals(id)) {
                existing = p;
                break;
            }
        }
        if (existing == null) {
            return 1; // Product not found
        }

        if (!categoryService.checkCategoryExists(updated.getCategoryID())) {
            return 2; // Category not found
        }

        existing.setName(updated.getName());
        existing.setPrice(updated.getPrice());
        existing.setCategoryID(updated.getCategoryID());
        return 0; // Success
    }

    public boolean deleteProduct(String id) {
        return products.removeIf(p -> p.getId().equals(id));
    }

    public Product getProductById(String productId) {
        for (Product p : products) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    public List<Product> getProductsByCategory(String categoryId) {
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategoryID().equals(categoryId)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Product> searchProductsByName(String name) {
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }

    /*
    Extra Endpoint apply a discount for a specific category
     */
    public int applyDiscountToCategory(String categoryId, double discountPercentage) {
        if (!categoryService.checkCategoryExists(categoryId)) {
            return 1; // Category not found
        }
        if (discountPercentage <= 0 || discountPercentage >= 100) {
            return 2; // Invalid percentage
        }

        boolean updatedAny = false;
        for (Product p : products) {
            if (p.getCategoryID().equals(categoryId)) {
                double newPrice = p.getPrice() - (p.getPrice() * (discountPercentage / 100.0));
                p.setPrice(newPrice);
                updatedAny = true;
            }
        }
        return updatedAny ? 0 : 3; // 0 = Success, 3 = No products found in this category
    }
}