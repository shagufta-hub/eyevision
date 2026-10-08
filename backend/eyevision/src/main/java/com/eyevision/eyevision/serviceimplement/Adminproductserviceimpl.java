package com.eyevision.eyevision.serviceimplement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eyevision.eyevision.entity.PlaceOrder;
import com.eyevision.eyevision.entity.adminProductVariant;
import com.eyevision.eyevision.entity.adminProductVariantAttachment;
import com.eyevision.eyevision.entity.adminaddproduct;
import com.eyevision.eyevision.repository.adminProductVariantAttachmentRepository;
import com.eyevision.eyevision.repository.adminProductVariantRepository;
import com.eyevision.eyevision.service.Adminproductservice;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class Adminproductserviceimpl implements Adminproductservice {

@Autowired
private JdbcTemplate jdbcTemplate;

  @Autowired
    private adminProductVariantRepository variantRepository;

    @Autowired
    private adminProductVariantAttachmentRepository attachmentRepository;
    @Override
    public List<adminaddproduct> getallAdminaddproducts(adminaddproduct product) {

        String sql = "SELECT * FROM products"; // Adjust the table name as necessary
        List<adminaddproduct> products = jdbcTemplate.query(sql, (rs, rowNum) -> {
            adminaddproduct p = new adminaddproduct();
            p.setId(rs.getLong("id"));
            p.setTitle(rs.getString("title"));
            p.setDescription(rs.getString("description"));
            // p.setBasePrice(rs.getDouble("base_price"));
            p.setStatus(rs.getString("status"));
            // Add other fields as necessary
            return p;
        });
        return products;
    }
    // Implement service methods here if needed
    @Deprecated
    public List<adminProductVariant> getVariantbyProduct(Integer productId) {
        String sql = "SELECT * FROM product_variants where product_id = ?"; // Adjust the table name as necessary
        List<adminProductVariant> variants = jdbcTemplate.query(sql, new Object[]{productId}, (rs, rowNum) -> {
            adminProductVariant v = new adminProductVariant();
            v.setId(rs.getLong("id"));
            v.setSku(rs.getString("sku"));
            v.setPrice(rs.getBigDecimal("price"));
            // v.setPrice(rs.getDouble("price"));
            v.setStock(rs.getInt("stock"));
            v.setColor(rs.getString("color"));
            v.setSize(rs.getString("size"));
            return v;
        });
        return variants;
    }

    @Override 
        public adminProductVariantAttachment uploadVariantAttachment(
            Long variantId,
            MultipartFile file,
            Boolean isPrimary
    ) throws IOException {

        // 1. Check variant
        adminProductVariant variant =
                variantRepository.findById(variantId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Variant not found with id: "
                                                + variantId
                                )
                        );


        // 2. Validate file
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is required");
        }


        // 3. Allowed file types
        String contentType = file.getContentType();

        if (contentType == null ||
                !(contentType.equals("image/jpeg")
                        || contentType.equals("image/png")
                        || contentType.equals("image/webp"))) {

            throw new RuntimeException(
                    "Only JPG, PNG and WEBP files are allowed"
            );
        }


        // 4. File size - 1 MB
        long maxSize = 1 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new RuntimeException(
                    "File size must not exceed 1 MB"
            );
        }


        // 5. Create upload directory
        String uploadDir =
                "uploads/products/variants/" + variantId + "/";

        Path directory = Paths.get(uploadDir);

        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }


        // 6. Original filename
        String originalFileName =
                file.getOriginalFilename();

        if (originalFileName == null ||
                originalFileName.trim().isEmpty()) {

            originalFileName = "image";
        }


        // 7. Extension
        String extension = "";

        int lastDot =
                originalFileName.lastIndexOf(".");

        if (lastDot > 0) {
            extension =
                    originalFileName
                            .substring(lastDot)
                            .toLowerCase();
        }


        // 8. Unique filename
        String newFileName =
                UUID.randomUUID()
                        .toString()
                        + extension;


        // 9. Save file
        Path filePath =
                directory.resolve(newFileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );


        // 10. Existing attachment count
        long existingCount =
                attachmentRepository
                        .countByVariantId(variantId);


        // 11. Primary image logic
        boolean primary =
                Boolean.TRUE.equals(isPrimary);

        if (existingCount == 0) {
            primary = true;
        }


        // If new image is primary,
        // remove primary from old images
        if (primary) {

            List<adminProductVariantAttachment> existingAttachments =
                    attachmentRepository
                            .findByVariantIdOrderBySortOrderAsc(
                                    variantId
                            );

            for (adminProductVariantAttachment attachment
                    : existingAttachments) {

                attachment.setIsPrimary(false);
            }

            attachmentRepository.saveAll(existingAttachments);
        }


        // 12. Create DB record
        adminProductVariantAttachment attachment =
                new adminProductVariantAttachment();

        attachment.setVariant(variant);
        attachment.setFileName(originalFileName);

        attachment.setFilePath(
                "/uploads/products/variants/"
                        + variantId
                        + "/"
                        + newFileName
        );

        attachment.setFileType(contentType);
        attachment.setIsPrimary(primary);
        attachment.setSortOrder((int) existingCount);


        // 13. Save DB
        return attachmentRepository.save(attachment);
    }

     @Override
    public int Updateproductstatus(Integer productid) {
        if (productid == null) {
            throw new IllegalArgumentException("Product Id cannot be null");
        }
        String sql="";
        sql="UPDATE products SET STATUS=? WHERE id=?";
        int prd=jdbcTemplate.update(sql,"active",productid);

        
        // Send confirmation email
        // emailService.sendOrderConfirmation(savedOrder);
        
        return prd;
    }
}

