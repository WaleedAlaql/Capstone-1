package com.waleed.capstone1.Service;

import com.waleed.capstone1.Entity.MerchantStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantStockService {

    private List<MerchantStock> merchantStocks = new ArrayList<>();
    private final ProductService productService;
    private final MerchantService merchantService;


    public List<MerchantStock> getAllMerchantStocks() {
        return merchantStocks;
    }

    public int addMerchantStock(MerchantStock stock) {
        if (productService.getProductById(stock.getProductId()) == null) {
            return 1; // Product ID not found
        }
        if (!merchantService.checkMerchantExists(stock.getMerchantId())) {
            return 2; // Merchant ID not found
        }
        merchantStocks.add(stock);
        return 0; // Success
    }

    public int updateMerchantStock(String id, MerchantStock updated) {
        MerchantStock existing = null;
        for (MerchantStock s : merchantStocks) {
            if (s.getId().equals(id)) {
                existing = s;
                break;
            }
        }
        if (existing == null) {
            return 1; // MerchantStock not found
        }

        if (productService.getProductById(updated.getProductId()) == null) {
            return 2; // Product ID not found
        }
        if (!merchantService.checkMerchantExists(updated.getMerchantId())) {
            return 3; // Merchant ID not found
        }

        existing.setProductId(updated.getProductId());
        existing.setMerchantId(updated.getMerchantId());
        existing.setStock(updated.getStock());
        return 0; // Success
    }

    public boolean deleteMerchantStock(String id) {
        return merchantStocks.removeIf(s -> s.getId().equals(id));
    }

    public int addMoreStock(String productId, String merchantId, int additionalStock) {
        for (MerchantStock stock : merchantStocks) {
            if (stock.getProductId().equals(productId) && stock.getMerchantId().equals(merchantId)) {
                stock.setStock(stock.getStock() + additionalStock);
                return 1; // Success
            }
        }
        return 0; // Not found
    }

    public MerchantStock getStockByProductAndMerchant(String productId, String merchantId) {
        for (MerchantStock stock : merchantStocks) {
            if (stock.getProductId().equals(productId) && stock.getMerchantId().equals(merchantId)) {
                return stock;
            }
        }
        return null;
    }
}