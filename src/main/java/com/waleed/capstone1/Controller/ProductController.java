package com.waleed.capstone1.Controller;

import com.waleed.capstone1.Api.ApiResponse;
import com.waleed.capstone1.Entity.Product;
import com.waleed.capstone1.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all-products")
    public ResponseEntity<ApiResponse> getProducts() {
        return ResponseEntity.status(200).body(new ApiResponse("Success", productService.getAllProducts()));
    }

    @PostMapping("/add-product")
    public ResponseEntity<ApiResponse> addProduct(@Valid @RequestBody Product product, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage(), null));
        }
        int result = productService.addProduct(product);
        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Category ID not found", null));
        }

        return ResponseEntity.status(201).body(new ApiResponse("Product added successfully", product));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable String id, @Valid @RequestBody Product product, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage(), null));
        }
        int result = productService.updateProduct(id, product);
        if (result == 1) {
            return ResponseEntity.status(404).body(new ApiResponse("Product not found", null));
        }
        if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Category ID not found", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Product updated successfully", product));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String id) {
        boolean isDeleted = productService.deleteProduct(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse("Product not found", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Product deleted successfully", null));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String categoryId) {
        
        if (productService.getProductsByCategory(categoryId) == null) {
            return ResponseEntity.status(404).body(new ApiResponse("Category not found", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Success", productService.getProductsByCategory(categoryId)));
    }
    
    @GetMapping("/search/{name}")
    public ResponseEntity<ApiResponse> searchProducts(@PathVariable String name) {
        return ResponseEntity.status(200).body(new ApiResponse("Success", productService.searchProductsByName(name)));
    }

    /*
    Extra Endpoint for discount
     */
    @PutMapping("/discount/{categoryId}/{percentage}")
    public ResponseEntity<ApiResponse> applyDiscount(@PathVariable String categoryId, @PathVariable double percentage) {
        int result = productService.applyDiscountToCategory(categoryId, percentage);
        if (result == 1) {
            return ResponseEntity.status(404).body(new ApiResponse("Category not found", null));
        }
        if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid discount percentage (must be between 0 and 100)", null));
        }
        if (result == 3) {
            return ResponseEntity.status(404).body(new ApiResponse("No products found for this category", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Discount applied successfully", null));
    }
}