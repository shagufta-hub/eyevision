package com.eyevision.eyevision.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "exhibition_customers",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_membership_token",
            columnNames = "membership_token"
        ),
        @UniqueConstraint(
            name = "uk_mobile",
            columnNames = "mobile"
        )
    }
)
public class ExhibitionCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 20)
    private String mobile;

    private String email;

    @Column(name = "membership_token", nullable = false, unique = true)
    private String membershipToken;

    private Boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (active == null) {
            active = true;
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMembershipToken() {
        return membershipToken;
    }

    public void setMembershipToken(String membershipToken) {
        this.membershipToken = membershipToken;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
