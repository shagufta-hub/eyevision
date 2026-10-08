package com.eyevision.eyevision.repository;
import com.eyevision.eyevision.entity.adminProductVariantAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository 
public interface adminProductVariantAttachmentRepository  extends JpaRepository<adminProductVariantAttachment, Long> {

    List<adminProductVariantAttachment> findByVariantIdOrderBySortOrderAsc(Long variantId );

    long countByVariantId(Long variantId);
}