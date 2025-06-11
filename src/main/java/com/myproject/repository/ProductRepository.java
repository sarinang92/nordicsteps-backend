// src/main/java/com/myproject/repository/ProductRepository.java
package com.myproject.repository;

import com.myproject.model.Products; // <--- This MUST be 'Products' (plural)
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Ensure this import is present for findById

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> { // <--- THIS LINE MUST BE <Products, Long>

    List<Products> findByName(String name); // Consider updating return type here too
    List<Products> findByPriceGreaterThan(double price); // And here

    // findById is inherited from JpaRepository and will now work with Products
}