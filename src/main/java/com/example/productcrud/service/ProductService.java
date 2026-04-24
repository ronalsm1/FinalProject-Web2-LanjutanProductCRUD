package com.example.productcrud.service;

import com.example.productcrud.model.Product;
import com.example.productcrud.model.User;
import com.example.productcrud.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Import pageable ~Brandon David
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAllByOwner(User owner) {
        return productRepository.findByOwner(owner);
    }

    public Optional<Product> findByIdAndOwner(Long id, User owner) {
        return productRepository.findByIdAndOwner(id, owner);
    }

    // Menggunakan Page dan menambahkan method findProducts() ~Brandon David
    public Page<Product> findProducts(User user, String keyword, Long categoryId, Pageable pageable) {
        if (keyword == null) keyword = "";

        if (categoryId != null) {
            return productRepository
                    .findByOwnerAndNameContainingIgnoreCaseAndCategory_Id(user, keyword, categoryId, pageable);
        }

        return productRepository
                .findByOwnerAndNameContainingIgnoreCase(user, keyword, pageable);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteByIdAndOwner(Long id, User owner) {
        productRepository.findByIdAndOwner(id, owner)
                .ifPresent(product -> productRepository.delete(product));
    }
}