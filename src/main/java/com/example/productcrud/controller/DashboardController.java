package com.example.productcrud.controller;

import com.example.productcrud.model.Product;
import com.example.productcrud.model.User;
import com.example.productcrud.repository.UserRepository;
import com.example.productcrud.service.ProductService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    private final ProductService productService;
    private final UserRepository userRepository;

    public DashboardController(ProductService productService, UserRepository userRepository){
        this.productService = productService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(UserDetails userDetails){
        return  userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User tidak ditemukan!"));
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User currentUser = getCurrentUser(userDetails);
        List<Product> products = productService.findAllByOwner(currentUser);

        // Menampilkan Statistik -Billy
        long totalProducts = products.size();
        long totalInventoryValue = products.stream()
                .mapToLong(p -> p.getPrice() * p.getStock())
                .sum();

        long activeProducts = products.stream().filter(Product::isActive).count();
        long inactiveProducts = totalProducts - activeProducts;

        // Menambahkan Produk per Kategori -Billy
        Map<String, Long> productsByCategory = products.stream()
                .filter(p -> p.getCategory() != null)
                .collect(Collectors.groupingBy(p -> p.getCategory().getName(), Collectors.counting()));

        // Menambahkan Low Stock (stock < 5) -Billy
        List<Product> lowStockProducts = products.stream()
                .filter(p -> p.getStock() < 5)
                .collect(Collectors.toList());

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalInventoryValue", totalInventoryValue);
        model.addAttribute("activeProducts", activeProducts);
        model.addAttribute("inactiveProducts", inactiveProducts);
        model.addAttribute("productsByCategory", productsByCategory);
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("hasProducts", !products.isEmpty());

        return "dashboard/index";
    }
}
