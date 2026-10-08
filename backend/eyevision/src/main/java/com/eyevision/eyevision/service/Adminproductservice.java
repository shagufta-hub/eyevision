package com.eyevision.eyevision.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.eyevision.eyevision.entity.adminProductVariant;
import com.eyevision.eyevision.entity.adminProductVariantAttachment;
import com.eyevision.eyevision.entity.adminaddproduct;

/**
 * Adminproductservice
 */
public interface Adminproductservice {

        public List<adminaddproduct> getallAdminaddproducts(adminaddproduct product);
        public List<adminProductVariant> getVariantbyProduct(Integer productid);
        adminProductVariantAttachment uploadVariantAttachment(Long variantId, MultipartFile file, Boolean isPrimary)
                        throws IOException;
        int Updateproductstatus(Integer productid);


}
