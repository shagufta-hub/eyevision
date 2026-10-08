package com.eyevision.eyevision.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
// import lombok.Getter;
// import lombok.Setter;

// @Getter
// @Setter
@Entity
@Table(name = "product_attachments")
public class AdminProductAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================================================
    // PRODUCT
    // =========================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            nullable = false
    )
    @JsonIgnore
    private adminaddproduct product;


    // =========================================================
    // FILE NAME
    // =========================================================

    @Column(name = "file_name")
    private String fileName;


    // =========================================================
    // FILE PATH
    // =========================================================

    @Column(name = "file_path")
    private String filePath;


    // =========================================================
    // FILE TYPE
    // =========================================================

    @Column(name = "file_type")
    private String fileType;


    // =========================================================
    // PRIMARY IMAGE
    // =========================================================

    @Column(name = "is_primary")
    private Boolean isPrimary = false;


    // =========================================================
    // SORT ORDER
    // =========================================================

    @Column(name = "sort_order")
    private Integer sortOrder = 0;


    // =========================================================
    // GETTERS AND SETTERS
    // =========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public adminaddproduct getProduct() {
        return product;
    }

    public void setProduct(adminaddproduct product) {
        this.product = product;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }


    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    // getters and setters
}
