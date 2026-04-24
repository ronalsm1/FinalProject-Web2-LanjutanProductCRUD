package com.example.productcrud.controller;

import com.example.productcrud.model.Category;
import com.example.productcrud.model.User;
import com.example.productcrud.repository.UserRepository;
import com.example.productcrud.service.CategoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Import pageable ~Brandon David
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// membuat categoryController -> josef
@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    public CategoryController(CategoryService categoryService, UserRepository userRepository){
        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(UserDetails userDetails){
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }

    // Menambahkan method-method yang diperlukan untuk pagination dan search & filter ~Brandon David
    @GetMapping
    public String listCategories(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model){
        User currentUser = getCurrentUser(userDetails);

        Pageable pageable = PageRequest.of(page, size); // Ini logika LIMIT OFFSET (Contoh: LIMIT 5 OFFSET 5) ~Brandon David
        Page<Category> categoryPage = categoryService.findCategories(currentUser, keyword, pageable);

        // Bind ke attributes ~Brandon David
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("totalItems", categoryPage.getTotalElements());
        model.addAttribute("keyword", keyword);

        return "category/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("category", new Category());
        return "category/form";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes){
        User currentUser = getCurrentUser(userDetails);

        if (!categoryService.isNameUniqueForUser(category.getName(), currentUser, category.getId())){
            redirectAttributes.addFlashAttribute("errrorMessage", "Nama kategori sudah ada!");
            return "redirect:/categories";
        }

        if (category.getId() != null){
            boolean isOwner = categoryService.findByIdAndUser(category.getId(), currentUser).isPresent();
            if (!isOwner){
                redirectAttributes.addFlashAttribute("errorMessage", "Kategori tidak ditemukan.");
            }
        }

        category.setUser(currentUser);
        categoryService.save(category);
        redirectAttributes.addFlashAttribute("successMessage", "Kategori berhasil ditambahkan dan disimpan!");
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model, RedirectAttributes redirectAttributes){
        User currentUser = getCurrentUser(userDetails);
        return categoryService.findByIdAndUser(id,currentUser).map(category -> {
            model.addAttribute("category", category);
            return "category/form"; }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Kategori tidak ditemukan.");
            return "redirect:/categories";
        });
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes){
        User currentUser = getCurrentUser(userDetails);
        boolean isOwner = categoryService.findByIdAndUser(id, currentUser).isPresent();

        if (isOwner) {
            try {
                categoryService.deleteByIdAndUser(id, currentUser);
                redirectAttributes.addFlashAttribute("successMessage", "Kategori berhasil dihapus!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Kategori tidak bisa dihapus karena masih digunakan oleh produk.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Kategori tidak ditemukan.");
        }

        return "redirect:/categories";
    }

}