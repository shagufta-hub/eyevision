package com.eyevision.eyevision.controller;

import com.eyevision.eyevision.entity.Exhibition;
import com.eyevision.eyevision.repository.ExhibitionRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/exhibitions")
@CrossOrigin(origins = {"http://localhost:4200", "https://evoptical.in"})
public class AdminExhibitionController {

    private final ExhibitionRepository exhibitionRepository;

    public AdminExhibitionController(
            ExhibitionRepository exhibitionRepository
    ) {
        this.exhibitionRepository = exhibitionRepository;
    }

    @PostMapping
    public ResponseEntity<Exhibition> create(
            @RequestBody Exhibition exhibition
    ) {

        exhibition.setId(null);
        exhibition.setActive(true);

        return ResponseEntity.ok(
                exhibitionRepository.save(exhibition)
        );
    }
         @GetMapping("/{id}")
public ResponseEntity<Exhibition> getById(@PathVariable Long id) {
    return exhibitionRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
}
