package com.waleed.capstone1.Controller;

import com.waleed.capstone1.Api.ApiResponse;
import com.waleed.capstone1.Entity.Category;
import com.waleed.capstone1.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all-categories")
    public ResponseEntity<ApiResponse> getCategories() {
        return ResponseEntity.status(200).body(new ApiResponse("Success", categoryService.getAllCategories()));
    }

    @PostMapping("/add-category")
    public ResponseEntity<ApiResponse> addCategory(@Valid @RequestBody Category category, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage(), null));
        }
        categoryService.addCategory(category);
        return ResponseEntity.status(201).body(new ApiResponse("Category added successfully", category));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable String id, @Valid @RequestBody Category category, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage(), null));
        }
        boolean isUpdated = categoryService.updateCategory(id, category);
        if (!isUpdated) {
            return ResponseEntity.status(404).body(new ApiResponse("Category not found", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Category updated successfully", category));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String id) {
        boolean isDeleted = categoryService.deleteCategory(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse("Category not found", null));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Category deleted successfully", null));
    }
}