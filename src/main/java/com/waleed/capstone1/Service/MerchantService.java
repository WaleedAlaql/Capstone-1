package com.waleed.capstone1.Service;

import com.waleed.capstone1.Entity.Merchant;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MerchantService {

    private List<Merchant> merchants = new ArrayList<>();

    public List<Merchant> getAllMerchants() {
        return merchants;
    }

    public void addMerchant(Merchant merchant) {
        merchants.add(merchant);
    }

    public boolean updateMerchant(String id, Merchant updated) {
        for (Merchant m : merchants) {
            if (m.getId().equals(id)) {
                m.setName(updated.getName());
                return true;
            }
        }
        return false;
    }

    public boolean deleteMerchant(String id) {
        return merchants.removeIf(m -> m.getId().equals(id));
    }

    public boolean checkMerchantExists(String merchantId) {
        for (Merchant m : merchants) {
            if (m.getId().equals(merchantId)) {
                return true;
            }
        }
        return false;
    }
}