package com.eyevision.eyevision.repository;


import com.eyevision.eyevision.entity.ExhibitionCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExhibitionCustomerRepository
        extends JpaRepository<ExhibitionCustomer, Long> {

    Optional<ExhibitionCustomer> findByMobile(String mobile);

    Optional<ExhibitionCustomer> findByMembershipToken(String membershipToken);

    boolean existsByMobile(String mobile);

    boolean existsByMembershipToken(String membershipToken);
}