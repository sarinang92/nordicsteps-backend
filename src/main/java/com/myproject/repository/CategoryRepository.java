package com.myproject.repository;

import com.myproject.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Long> {
    // You might want to find a category by its name, for example
    Optional<Categories> findByName(String name);
}