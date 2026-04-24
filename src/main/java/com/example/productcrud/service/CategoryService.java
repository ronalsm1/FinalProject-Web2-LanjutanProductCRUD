package com.example.productcrud.service;

import com.example.productcrud.model.Category;
import com.example.productcrud.model.User;
import com.example.productcrud.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Import pageable ~Brandon David
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// josef -> create categoryService
@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAllByUser(User user){
        return categoryRepository.findByUser(user);
    }

    public Optional<Category> findByIdAndUser(Long id, User user){
        return categoryRepository.findByIdAndUser(id, user);
    }

    // Menggunakan Page dan menambahkan method findCategories() ~Brandon David
    public Page<Category> findCategories(User user, String keyword, Pageable pageable) {
        if (keyword == null) keyword = "";

        return categoryRepository.findByUserAndNameContainingIgnoreCase(user, keyword, pageable);
    }

    public Category save(Category category){
        return categoryRepository.save(category);
    }

    public void deleteByIdAndUser(Long id, User user){
        categoryRepository.findByIdAndUser(id, user).ifPresent(categoryRepository::delete);
    }

    public boolean isNameUniqueForUser(String name, User user, Long categoryId){
        if (categoryId == null){
            return !categoryRepository.existsByNameAndUser(name, user);
        }
        return !categoryRepository.existsByNameAndUserAndIdNot(name, user, categoryId);
    }
}
