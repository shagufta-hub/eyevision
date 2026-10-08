package com.eyevision.eyevision.repository;

// public class AdminProductAttachmentRepository    {
    
// }
import com.eyevision.eyevision.entity.AdminProductAttachment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductAttachmentRepository   extends JpaRepository<AdminProductAttachment, Long> {

    List<AdminProductAttachment>
    findByProductIdOrderBySortOrderAsc(Long productId);
}