package com.waleed.capstone1.Controller;

import com.waleed.capstone1.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final MerchantService merchantService;
    private final MerchantStockService merchantStockService;
    private final UserService userService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("merchants", merchantService.getAllMerchants());
        model.addAttribute("users", userService.getAllUsers());
        return "index";
    }

    @GetMapping("/shop/products")
    public String products(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @GetMapping("/shop/merchants")
    public String merchants(Model model) {
        model.addAttribute("merchants", merchantService.getAllMerchants());
        model.addAttribute("stocks", merchantStockService.getAllMerchantStocks());
        return "merchants";
    }
}
