package com.eyevision.eyevision.repository;

import com.eyevision.eyevision.entity.ExhibitionVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExhibitionVisitRepository
        extends JpaRepository<ExhibitionVisit, Long> {

    Optional<ExhibitionVisit> findByCustomerIdAndExhibitionId(
            Long customerId,
            Long exhibitionId
    );
}