package com.waleed.capstone1.Controller;

import com.waleed.capstone1.Api.ApiResponse;
import com.waleed.capstone1.Entity.Product;
import com.waleed.capstone1.Entity.User;
import com.waleed.capstone1.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<ApiResponse> getUsers() {
        return ResponseEntity.status(200).body(new ApiResponse("Success", userService.getAllUsers()));
    }

    @PostMapping("/add-user")
    public ResponseEntity<ApiResponse> addUser(@Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage(), null));
        }
        userService.addUser(user);
        return ResponseEntity.status(201).body(new ApiResponse("User added successfully", user));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable String id, @Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage(), null));
        }
        boolean isUpdated = userService.updateUser(id, user);
        if (!isUpdated) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("User updated successfully", user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String id) {
        boolean isDeleted = userService.deleteUser(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully", null));
    }

    @PostMapping("/buy/{userId}/{productId}/{merchantId}")
    public ResponseEntity<ApiResponse> buyProduct(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId) {
        int result = userService.buyProduct(userId, productId, merchantId);

        switch (result) {
            case 1:
                return ResponseEntity.status(400).body(new ApiResponse("Invalid user ID, product ID, or merchant stock ID", null));
            case 2:
                return ResponseEntity.status(400).body(new ApiResponse("Product is out of stock", null));
            case 3:
                return ResponseEntity.status(400).body(new ApiResponse("User balance is less than the product price", null));
            default:
                return ResponseEntity.status(200).body(new ApiResponse("Product bought successfully", null));
        }
    }

    @PutMapping("/deposit/{userId}/{amount}")
    public ResponseEntity<ApiResponse> depositBalance(@PathVariable String userId, @PathVariable double amount) {
        boolean isDeposited = userService.depositBalance(userId, amount);
        if (!isDeposited) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid user ID or amount", null));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Balance deposited successfully", null));
    }

    /*
    Extra Endpoint give a gift card to a customer
     */
    @PutMapping("/gift-card/{adminId}/{targetUserId}/{amount}")
    public ResponseEntity<ApiResponse> adminGiveGiftCard(@PathVariable String adminId,  @PathVariable String targetUserId, @PathVariable double amount) {

        int result = userService.adminGiveGiftCard(adminId, targetUserId, amount);

        if (result == 1) {
            return ResponseEntity.status(404).body(new ApiResponse("Admin or Target user not found", null));
        }
        if (result == 2) {
            return ResponseEntity.status(403).body(new ApiResponse("Access denied: Only admins can issue gift cards", null));
        }
        if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid gift amount", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Gift card applied successfully to user balance", null));
    }

    /*
    Extra Endpoint recommend products based on the user's balance
     */
    @GetMapping("/recommend/{userId}")
    public ResponseEntity<ApiResponse> recommendProducts(@PathVariable String userId) {
        List<Product> recommendations = userService.recommendProductsByBalance(userId);
        if (recommendations == null) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found", null));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Recommended products based on your balance", recommendations));
    }

    /*
    Extra Endpoint for refund
     */
    @PostMapping("/refund/{userId}/{productId}/{merchantId}")
    public ResponseEntity<ApiResponse> refundProduct(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId) {
        int result = userService.refundProduct(userId, productId, merchantId);

        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid user ID, product ID, or merchant stock ID", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Product refunded successfully, balance and stock updated", null));
    }

    /*
    Extra Endpoint give the admin the ability to delete a regular customer but admin can not delete another admin
     */
    @DeleteMapping("/admin-delete/{adminId}/{targetUserId}")
    public ResponseEntity<ApiResponse> adminDeleteCustomer(@PathVariable String adminId, @PathVariable String targetUserId) {
        int result = userService.adminDeleteCustomer(adminId, targetUserId);

        if (result == 1) {
            return ResponseEntity.status(404).body(new ApiResponse("Admin or Target user not found", null));
        }
        if (result == 2) {
            return ResponseEntity.status(403).body(new ApiResponse("Access denied: Only admins can perform this action", null));
        }
        if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("Action not allowed: Cannot delete another admin", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Customer deleted successfully by admin", null));
    }
}