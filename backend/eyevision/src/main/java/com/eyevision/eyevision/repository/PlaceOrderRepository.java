package com.eyevision.eyevision.repository;

import com.eyevision.eyevision.entity.PlaceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceOrderRepository extends JpaRepository<PlaceOrder, Long> {
    
    List<PlaceOrder> findByCustomerName(String customerName);
    
    List<PlaceOrder> findByEmail(String email);
    
    List<PlaceOrder> findByPhoneNumber(String phoneNumber);
    
    Optional<PlaceOrder> findByOrderId(Long orderId);
}
