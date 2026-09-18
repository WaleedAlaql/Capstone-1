package com.waleed.capstone1.Service;

import com.waleed.capstone1.Entity.MerchantStock;
import com.waleed.capstone1.Entity.Product;
import com.waleed.capstone1.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private List<User> users = new ArrayList<>();
    private final ProductService productService;
    private final MerchantStockService merchantStockService;

    public List<User> getAllUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public boolean updateUser(String id, User updated) {
        for (User u : users) {
            if (u.getId().equals(id)) {
                u.setUsername(updated.getUsername());
                u.setPassword(updated.getPassword());
                u.setEmail(updated.getEmail());
                u.setRole(updated.getRole());
                u.setBalance(updated.getBalance());
                return true;
            }
        }
        return false;
    }

    public boolean deleteUser(String id) {
        return users.removeIf(u -> u.getId().equals(id));
    }

    public User getUserById(String userId) {
        for (User u : users) {
            if (u.getId().equals(userId)) return u;
        }
        return null;
    }

    // --- Buy Product Logic ---
    public int buyProduct(String userId, String productId, String merchantId) {
        User user = getUserById(userId);
        Product product = productService.getProductById(productId);
        MerchantStock targetStock = merchantStockService.getStockByProductAndMerchant(productId, merchantId);

        if (user == null || product == null || targetStock == null) {
            return 1; // Invalid IDs
        }
        if (targetStock.getStock() <= 0) {
            return 2; // Out of stock
        }
        if (user.getBalance() < product.getPrice()) {
            return 3; // Insufficient balance
        }

        targetStock.setStock(targetStock.getStock() - 1);
        user.setBalance(user.getBalance() - product.getPrice());

        return 0; // Success
    }

    /*
    Extra Endpoint Deposit balance
     */
    public boolean depositBalance(String userId, double amount) {
        User user = getUserById(userId);
        if (user == null || amount <= 0) {
            return false;
        }
        user.setBalance(user.getBalance() + amount);
        return true;
    }

    /*
    Extra Endpoint give a gift card to a customer
     */
    public int adminGiveGiftCard(String adminId, String targetUserId, double amount) {
        User admin = getUserById(adminId);
        User targetUser = getUserById(targetUserId);

        if (admin == null || targetUser == null) {
            return 1; // Admin or User not found
        }
        if (!admin.getRole().equalsIgnoreCase("admin")) {
            return 2; // Not an admin
        }
        if (amount <= 0) {
            return 3; // Invalid gift amount
        }

        // Apply gift card balance to the target customer
        targetUser.setBalance(targetUser.getBalance() + amount);
        return 0; // Success
    }

    /*
    Extra Endpoint recommend products by balance
     */
    public List<Product> recommendProductsByBalance(String userId) {
        User user = getUserById(userId);
        if (user == null) {
            return null; // User not found
        }

        List<Product> recommendedProducts = new ArrayList<>();
        List<Product> allProducts = productService.getAllProducts();

        for (Product p : allProducts) {
            // Recommend products that the user can afford with their current balance
            if (p.getPrice() <= user.getBalance()) {
                recommendedProducts.add(p);
            }
        }
        return recommendedProducts;
    }

    /*
    Extra Endpoint give the admin the ability to delete a regular customer but admin can not delete another admin
     */
    public int adminDeleteCustomer(String adminId, String targetUserId) {
        User admin = getUserById(adminId);
        User targetUser = getUserById(targetUserId);

        if (admin == null || targetUser == null) {
            return 1; // One or both users not found
        }
        if (!admin.getRole().equalsIgnoreCase("admin")) {
            return 2; // The requester is not an admin
        }
        if (targetUser.getRole().equalsIgnoreCase("admin")) {
            return 3; // Cannot delete another admin
        }

        // Remove the customer from the users list
        users.remove(targetUser);
        return 0; // Success
    }
}