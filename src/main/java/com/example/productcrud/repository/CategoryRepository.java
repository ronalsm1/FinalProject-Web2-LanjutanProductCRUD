package com.example.productcrud.repository;

import com.example.productcrud.model.Category;
import com.example.productcrud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Import pageable ~Brandon David
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// create category repository -> josef
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    //Ambil semua category milik user tertentu
    List<Category> findByUser(User user);
    Optional<Category> findByIdAndUser(Long id, User user);
    boolean existsByNameAndUser(String name, User user);
    boolean existsByNameAndUserAndIdNot(String name, User user, Long id);

    // Menggunakan Page dan membuat function: ~Brandon David
    // findByOwnerAndNameContainingIgnoreCase
    Page<Category> findByUserAndNameContainingIgnoreCase(User user, String keyword, Pageable pageable);
}
