package com.waleed.capstone1.Controller;

import com.waleed.capstone1.Api.ApiResponse;
import com.waleed.capstone1.Entity.Merchant;
import com.waleed.capstone1.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/all-merchants")
    public ResponseEntity<ApiResponse> getMerchants() {
        return ResponseEntity.status(200).body(new ApiResponse("Success", merchantService.getAllMerchants()));
    }

    @PostMapping("/add-merchant")
    public ResponseEntity<ApiResponse> addMerchant(@Valid @RequestBody Merchant merchant, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage(), null));
        }
        merchantService.addMerchant(merchant);
        return ResponseEntity.status(201).body(new ApiResponse("Merchant added successfully", merchant));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateMerchant(@PathVariable String id, @Valid @RequestBody Merchant merchant, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage(), null));
        }
        boolean isUpdated = merchantService.updateMerchant(id, merchant);
        if (!isUpdated) {
            return ResponseEntity.status(404).body(new ApiResponse("Merchant not found", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Merchant updated successfully", merchant));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMerchant(@PathVariable String id) {
        boolean isDeleted = merchantService.deleteMerchant(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse("Merchant not found", null));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Merchant deleted successfully", null));
    }
}