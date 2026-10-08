package com.eyevision.eyevision.repository;

import com.eyevision.eyevision.entity.OrderTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderTrackingRepository extends JpaRepository<OrderTracking, Long> {
    
    List<OrderTracking> findByOrderIdOrderByCreatedAtDesc(Long orderId);
}
