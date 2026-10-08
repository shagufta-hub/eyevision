package com.eyevision.eyevision.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eyevision.eyevision.entity.adminaddproduct;

@Repository
public interface adminproductRepository extends JpaRepository<adminaddproduct, Long> {
    // Basic CRUD features (save, findById, delete) are automatically included here.
    
    // Optional: Add a custom helper method to check if a specific SKU exists in the master database
    boolean existsByVariantsSku(String sku);
    
}

