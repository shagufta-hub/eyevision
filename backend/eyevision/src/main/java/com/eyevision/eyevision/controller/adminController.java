package com.eyevision.eyevision.controller;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.eyevision.eyevision.entity.AdminProductAttachment;
import com.eyevision.eyevision.entity.Exhibition;
import com.eyevision.eyevision.entity.PlaceOrder;
import com.eyevision.eyevision.entity.adminProductVariant;
import com.eyevision.eyevision.entity.adminProductVariantAttachment;
import com.eyevision.eyevision.entity.adminaddproduct;
import com.eyevision.eyevision.repository.AdminProductAttachmentRepository;
import com.eyevision.eyevision.repository.adminproductRepository;


import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
@CrossOrigin(origins = {"http://localhost:4200", "https://evoptical.in"})
@RestController
@RequestMapping("/api/admin")
public class adminController {

    @Autowired
    private adminproductRepository productRepository;

    @Autowired
    private com.eyevision.eyevision.service.Adminproductservice adminProductService;

    @Autowired
private AdminProductAttachmentRepository productAttachmentRepository;

    // @PostMapping("/addproduct")
    // public ResponseEntity<adminaddproduct> createProductWithVariants(@RequestBody adminaddproduct product) {
    //       System.out.println("Title = " + product.getTitle());
    // System.out.println("Description = " + product.getDescription());
    // System.out.println("Base Price = " + product.getBasePrice());
    // System.out.println("Status = " + product.getStatus());
    // System.out.println("Variants = " + product.getVariants());
    //     // CascadeType.ALL guarantees that saving the parent object automatically commits child matrix rows
    //     adminaddproduct savedProduct = productRepository.save(product);
    //     return ResponseEntity.ok(savedProduct);
    // }

      @PostMapping(
            value = "/addproduct",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<adminaddproduct> createProductWithVariants(

            @RequestPart("product")
            adminaddproduct product,

            @RequestPart(value = "files", required = false)
            List<MultipartFile> files

    ) {

        try {

            // =================================================
            // PRINT PRODUCT DATA
            // =================================================

            System.out.println("======================================");
            System.out.println("PRODUCT DETAILS");
            System.out.println("======================================");

            System.out.println(
                    "Title       = " + product.getTitle()
            );

            System.out.println(
                    "Description = " + product.getDescription()
            );

            System.out.println(
                    "Base Price  = " + product.getBasePrice()
            );

            System.out.println(
                    "Status      = " + product.getStatus()
            );

            System.out.println(
                    "Audience    = " + product.getAudience()
            );

            System.out.println(
                    "Category    = " + product.getCategory()
            );

            System.out.println(
                    "Brand       = " + product.getBrand()
            );

            System.out.println(
                    "Variants    = " + product.getVariants()
            );


            // =================================================
            // CONNECT VARIANTS WITH PRODUCT
            // =================================================

            if (product.getVariants() != null) {

                for (adminProductVariant variant
                        : product.getVariants()) {

                    variant.setProduct(product);
                }
            }


            // =================================================
            // SAVE PRODUCT + VARIANTS
            // =================================================

            adminaddproduct savedProduct =
                    productRepository.save(product);


            System.out.println("======================================");
            System.out.println("PRODUCT SAVED");
            System.out.println("======================================");

            System.out.println(
                    "Product ID = " + savedProduct.getId()
            );


            // =================================================
            // HANDLE MULTIPLE PRODUCT IMAGES
            // =================================================

            if (files != null && !files.isEmpty()) {

                System.out.println("======================================");
                System.out.println("PRODUCT IMAGES");
                System.out.println("======================================");

                System.out.println(
                        "Total files = " + files.size()
                );


                // ---------------------------------------------
                // Upload directory
                // ---------------------------------------------

                String uploadDir =
                        "uploads/products/";

                File directory =
                        new File(uploadDir);


                if (!directory.exists()) {

                    if (!directory.mkdirs()) {

                        System.out.println(
                                "Unable to create upload directory"
                        );

                        return ResponseEntity
                                .status(
                                        HttpStatus.INTERNAL_SERVER_ERROR
                                )
                                .build();
                    }
                }


                // ---------------------------------------------
                // Maximum file size = 1 MB
                // ---------------------------------------------

                long maxSize =
                        1 * 1024 * 1024;


                // ---------------------------------------------
                // Process each image
                // ---------------------------------------------

                for (int i = 0;
                     i < files.size();
                     i++) {


                    MultipartFile file =
                            files.get(i);


                    // -----------------------------------------
                    // Skip empty file
                    // -----------------------------------------

                    if (file == null ||
                            file.isEmpty()) {

                        continue;
                    }


                    System.out.println(
                            "--------------------------------------"
                    );

                    System.out.println(
                            "Processing Image = "
                                    + (i + 1)
                    );


                    System.out.println(
                            "Original Name = "
                                    + file.getOriginalFilename()
                    );

                    System.out.println(
                            "Content Type = "
                                    + file.getContentType()
                    );

                    System.out.println(
                            "File Size = "
                                    + file.getSize()
                    );


                    // -----------------------------------------
                    // File size validation
                    // -----------------------------------------

                    if (file.getSize() > maxSize) {

                        System.out.println(
                                "File size exceeds 1 MB"
                        );

                        return ResponseEntity
                                .status(
                                        HttpStatus.PAYLOAD_TOO_LARGE
                                )
                                .build();
                    }


                    // -----------------------------------------
                    // File type validation
                    // -----------------------------------------

                    String contentType =
                            file.getContentType();


                    if (contentType == null ||
                            !(
                                    contentType.equals(
                                            "image/jpeg"
                                    )
                                    ||
                                    contentType.equals(
                                            "image/png"
                                    )
                                    ||
                                    contentType.equals(
                                            "image/webp"
                                    )
                            )
                    ) {

                        System.out.println(
                                "Invalid image type"
                        );

                        return ResponseEntity
                                .status(
                                        HttpStatus.BAD_REQUEST
                                )
                                .build();
                    }


                    // -----------------------------------------
                    // Original file name
                    // -----------------------------------------

                    String originalFileName =
                            file.getOriginalFilename();


                    // -----------------------------------------
                    // Get extension
                    // -----------------------------------------

                    String extension = "";


                    if (originalFileName != null &&
                            originalFileName.contains(".")) {

                        extension =
                                originalFileName
                                        .substring(
                                                originalFileName
                                                        .lastIndexOf(".")
                                        )
                                        .toLowerCase();
                    }


                    // -----------------------------------------
                    // Generate unique file name
                    // -----------------------------------------

                    String fileName =
                            UUID.randomUUID()
                                    .toString()
                                    + extension;


                    // -----------------------------------------
                    // Create file path
                    // -----------------------------------------

                    Path filePath =
                            Paths.get(
                                    uploadDir,
                                    fileName
                            );


                    // -----------------------------------------
                    // Save physical file
                    // -----------------------------------------

                    Files.copy(
                            file.getInputStream(),
                            filePath,
                            StandardCopyOption.REPLACE_EXISTING
                    );


                    // =================================================
                    // SAVE IMAGE DETAILS IN product_attachments TABLE
                    // =================================================

                    AdminProductAttachment attachment =
                            new AdminProductAttachment();


                    attachment.setProduct(
                            savedProduct
                    );


                    attachment.setFileName(
                            originalFileName
                    );


                    attachment.setFilePath(
                            "/uploads/products/"
                                    + fileName
                    );


                    attachment.setFileType(
                            contentType
                    );


                    // First uploaded image = primary image

                    attachment.setIsPrimary(
                            i == 0
                    );


                    // Image order

                    attachment.setSortOrder(
                            i
                    );


                    // Save DB record

                    productAttachmentRepository.save(
                            attachment
                    );


                    // -----------------------------------------
                    // Print file information
                    // -----------------------------------------

                    System.out.println(
                            "File saved at = "
                                    + filePath.toAbsolutePath()
                    );

                    System.out.println(
                            "Database path = "
                                    + attachment.getFilePath()
                    );

                    System.out.println(
                            "Primary = "
                                    + attachment.getIsPrimary()
                    );

                    System.out.println(
                            "Sort Order = "
                                    + attachment.getSortOrder()
                    );
                }
            }


            // =================================================
            // SUCCESS
            // =================================================

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "PRODUCT CREATED SUCCESSFULLY"
            );

            System.out.println(
                    "Product ID = "
                            + savedProduct.getId()
            );

            System.out.println(
                    "======================================"
            );


            return ResponseEntity.ok(
                    savedProduct
            );


        } catch (IOException e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .build();


        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .build();
        }
    }
    
    @GetMapping("/getproduclistt")
    public ResponseEntity<List<adminaddproduct>> getProducts() {
         adminaddproduct product=new adminaddproduct();
        // CascadeType.ALL guarantees that saving the parent object automatically commits child matrix rows
        List<adminaddproduct> savedProduct = adminProductService.getallAdminaddproducts(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PostMapping(
        value = "/variant/{variantId}/attachments",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
)
public ResponseEntity<?> uploadVariantAttachment(
        @PathVariable Long variantId,

        @RequestPart("file")
        MultipartFile file,

        @RequestParam(
                value = "isPrimary",
                required = false,
                defaultValue = "false"
        )
        Boolean isPrimary
) {

    try {

        adminProductVariantAttachment attachment =
                adminProductService.uploadVariantAttachment(
                        variantId,
                        file,
                        isPrimary
                );

        return ResponseEntity.ok(attachment);

    } catch (RuntimeException e) {

        return ResponseEntity
                .badRequest()
                .body(e.getMessage());

    } catch (IOException e) {

        e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unable to upload file");
    }
}

 @PutMapping("/modifyproductstatus")
    public ResponseEntity<Integer> updateOrder( @RequestBody Integer prdid) {
        try {
            Integer Prodid = adminProductService.Updateproductstatus(prdid);
            return new ResponseEntity<>(Prodid, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
 


}

