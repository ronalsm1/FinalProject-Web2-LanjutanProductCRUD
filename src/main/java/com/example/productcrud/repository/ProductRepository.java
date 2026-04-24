package com.example.productcrud.repository;

import com.example.productcrud.model.Product;
import com.example.productcrud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Import pageable ~Brandon David
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByOwner(User owner);

    Optional<Product> findByIdAndOwner(Long id, User owner);

    // Menggunakan Page dan membuat function: ~Brandon David
    // findByOwnerAndNameContainingIgnoreCase & findByOwnerAndNameContainingIgnoreCaseAndCategory_Id
    Page<Product> findByOwnerAndNameContainingIgnoreCase(User owner, String keyword, Pageable pageable);
    Page<Product> findByOwnerAndNameContainingIgnoreCaseAndCategory_Id(User owner, String keyword, Long categoryId, Pageable pageable);
}
