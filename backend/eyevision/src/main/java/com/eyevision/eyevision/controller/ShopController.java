package com.eyevision.eyevision.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eyevision.eyevision.entity.adminaddproduct;
import com.eyevision.eyevision.repository.adminproductRepository;
  @RestController
@RequestMapping("/api/shop")
@CrossOrigin(origins = {"http://localhost:4200", "https://evoptical.in"})
public class ShopController {
    @Autowired
    private adminproductRepository productRepository;

    @GetMapping("/products")
    public ResponseEntity<List<adminaddproduct>> getAllProducts() {

        List<adminaddproduct> products =
                productRepository.findAll();

        return ResponseEntity.ok(products);
    }


    
    @GetMapping("/productsbyid/{id}")
    public ResponseEntity<adminaddproduct> getProductById(@PathVariable Long id) {
        adminaddproduct product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    
    
}
