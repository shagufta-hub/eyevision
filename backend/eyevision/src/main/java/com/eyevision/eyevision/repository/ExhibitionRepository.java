package com.eyevision.eyevision.repository;

import com.eyevision.eyevision.entity.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExhibitionRepository
        extends JpaRepository<Exhibition, Long> {

    Optional<Exhibition> findByIdAndActiveTrue(Long id);
}