package com.eyevision.eyevision.entity;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "product_variant_attachments")
public class adminProductVariantAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private adminProductVariant variant;
    

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    public Long getId() {
        return id;
    }

    public adminProductVariant getVariant() {
        return variant;
    }

      public void setVariant(adminProductVariant variant) {
        this.variant = variant;
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



}