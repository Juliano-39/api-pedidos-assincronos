package com.juliano.pedidos.category.repository;

import com.juliano.pedidos.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long id);
    Optional<Category>  findCategoryByName(String name);
}
