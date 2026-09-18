package com.waleed.capstone1.Controller;

import com.waleed.capstone1.Api.ApiResponse;
import com.waleed.capstone1.Entity.MerchantStock;
import com.waleed.capstone1.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchantstock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;

    @GetMapping("/all-merchantstock")
    public ResponseEntity<ApiResponse> getMerchantStocks() {
        return ResponseEntity.status(200).body(new ApiResponse("Success", merchantStockService.getAllMerchantStocks()));
    }

    @PostMapping("/add-merchantstock")
    public ResponseEntity<ApiResponse> addMerchantStock(@Valid @RequestBody MerchantStock stock, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage(), null));
        }

        int result = merchantStockService.addMerchantStock(stock);
        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Product ID not found", null));
        }
        if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant ID not found", null));
        }

        return ResponseEntity.status(201).body(new ApiResponse("Merchant Stock added successfully", stock));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateMerchantStock(@PathVariable String id, @Valid @RequestBody MerchantStock stock, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage(), null));
        }

        int result = merchantStockService.updateMerchantStock(id, stock);
        if (result == 1) {
            return ResponseEntity.status(404).body(new ApiResponse("Merchant Stock not found", null));
        }
        if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Product ID not found", null));
        }
        if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant ID not found", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock updated successfully", stock));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMerchantStock(@PathVariable String id) {
        boolean isDeleted = merchantStockService.deleteMerchantStock(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse("Merchant Stock not found", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock deleted successfully", null));
    }

    @PutMapping("/addstock/{productId}/{merchantId}/{amount}")
    public ResponseEntity<ApiResponse> addMoreStock(@PathVariable String productId, @PathVariable String merchantId, @PathVariable int amount) {

        int result = merchantStockService.addMoreStock(merchantId, productId, amount);

        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Stock amount must be greater than 0", null));
        }

        if (result == 2) {
            return ResponseEntity.status(404).body(new ApiResponse("Product or Merchant Stock combination not found", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Stock added successfully", null));
    }
}