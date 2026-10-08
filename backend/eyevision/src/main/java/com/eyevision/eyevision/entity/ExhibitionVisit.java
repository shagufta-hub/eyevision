package com.eyevision.eyevision.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "exhibition_visits",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_customer_exhibition",
            columnNames = {"customer_id", "exhibition_id"}
        )
    }
)
public class ExhibitionVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private ExhibitionCustomer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id", nullable = false)
    private Exhibition exhibition;

    private Boolean eyeTestCompleted = false;

    private LocalDateTime eyeTestCompletedAt;

    private Boolean freeSpecsClaimed = false;

    private LocalDateTime freeSpecsClaimedAt;

    private Integer discountPercentage = 10;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();

        if (eyeTestCompleted == null) {
            eyeTestCompleted = false;
        }

        if (freeSpecsClaimed == null) {
            freeSpecsClaimed = false;
        }

        if (discountPercentage == null) {
            discountPercentage = 10;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExhibitionCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(ExhibitionCustomer customer) {
        this.customer = customer;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public Boolean getEyeTestCompleted() {
        return eyeTestCompleted;
    }

    public void setEyeTestCompleted(Boolean eyeTestCompleted) {
        this.eyeTestCompleted = eyeTestCompleted;
    }

    public LocalDateTime getEyeTestCompletedAt() {
        return eyeTestCompletedAt;
    }

    public void setEyeTestCompletedAt(LocalDateTime eyeTestCompletedAt) {
        this.eyeTestCompletedAt = eyeTestCompletedAt;
    }

    public Boolean getFreeSpecsClaimed() {
        return freeSpecsClaimed;
    }

    public void setFreeSpecsClaimed(Boolean freeSpecsClaimed) {
        this.freeSpecsClaimed = freeSpecsClaimed;
    }

    public LocalDateTime getFreeSpecsClaimedAt() {
        return freeSpecsClaimedAt;
    }

    public void setFreeSpecsClaimedAt(LocalDateTime freeSpecsClaimedAt) {
        this.freeSpecsClaimedAt = freeSpecsClaimedAt;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
