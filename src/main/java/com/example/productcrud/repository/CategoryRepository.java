package com.example.productcrud.repository;

import com.example.productcrud.model.Category;
import com.example.productcrud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
// create category repository -> josef
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    //Ambil semua category milik user tertentu
    List<Category> findByUser(User user);
    Optional<Category> findByIdAndUser(Long id, User user);
    boolean existsByNameAndUser(String name, User user);
    boolean existsByNameAndUserAndIdNot(String name, User user, Long id);
}
