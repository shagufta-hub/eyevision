package com.eyevision.eyevision.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "product_variants")
public class adminProductVariant {
      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku;

    private BigDecimal price;
    private Integer stock;
    
    // Explicit attribute tracking values
    private String color;
    private String size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore // Prevents infinite recursion processing loops during serialization output stages
    private adminaddproduct product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public adminaddproduct getProduct() {
        return product;
    }

    public void setProduct(adminaddproduct product) {
        this.product = product;
    }
    @OneToMany(
    mappedBy = "variant",
    cascade = CascadeType.ALL,
    orphanRemoval = true
)
private List<adminProductVariantAttachment> attachments = new ArrayList<>();
    
}
