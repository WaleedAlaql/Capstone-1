package com.waleed.capstone1.Service;

import com.waleed.capstone1.Entity.Category;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private List<Category> categories = new ArrayList<>();

    public List<Category> getAllCategories() {
        return categories;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public boolean updateCategory(String id, Category updated) {
        for (Category c : categories) {
            if (c.getId().equals(id)) {
                c.setName(updated.getName());
                return true;
            }
        }
        return false;
    }

    public boolean deleteCategory(String id) {
        return categories.removeIf(c -> c.getId().equals(id));
    }

    public boolean checkCategoryExists(String categoryId) {
        for (Category c : categories) {
            if (c.getId().equals(categoryId)){
                return true;
            }
        }
        return false;
    }
}