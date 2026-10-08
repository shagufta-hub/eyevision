package com.eyevision.eyevision.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "products")

public class adminaddproduct 
{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private BigDecimal basePrice;
    private String status;

      // Store uploaded file path/URL
    private String attachment;
    //new ele,ent added for product 
    @Column(nullable = false)
private String category;

@Column(nullable = false)
private String audience;

private String brand;

private BigDecimal originalPrice;

private Double rating;

private Integer reviewCount;

private Boolean featured = false;

    // Orchestrate mapping setup to correctly save all dynamic objects in a single database payload drop
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    
    private List<adminProductVariant> variants = new ArrayList<>();

    // Helper setter strategy to preserve structural foreign key parameters inside relational tables
    public void setVariants(List<adminProductVariant> variants) {
        this.variants = variants;
        if (variants != null) {
            for (adminProductVariant variant : variants) {
                variant.setProduct(this);
            }
        }
    }

    // Standard Getters and Setters...
@OneToMany(
        mappedBy = "product",
        cascade = CascadeType.ALL,
        orphanRemoval = true
)
private List<AdminProductAttachment> attachments =
        new ArrayList<>();
}


