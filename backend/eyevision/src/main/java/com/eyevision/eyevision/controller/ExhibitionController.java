package com.eyevision.eyevision.controller;

import com.eyevision.eyevision.dto.ExhibitionRegistrationRequest;
import com.eyevision.eyevision.dto.ExhibitionRegistrationResponse;
import com.eyevision.eyevision.dto.TokenValidationResponse;
import com.eyevision.eyevision.serviceimplement.ExhibitionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exhibition")
@CrossOrigin(origins = {"http://localhost:4200", "https://evoptical.in"})
public class ExhibitionController {

    private final ExhibitionService exhibitionService;


    public ExhibitionController(
            ExhibitionService exhibitionService,
            com.eyevision.eyevision.repository.ExhibitionRepository exhibitionRepository
    ) {
        this.exhibitionService = exhibitionService;    }

    // =========================================================
    // REGISTER
    // =========================================================

    @PostMapping("/register")
    public ResponseEntity<ExhibitionRegistrationResponse>
    register(
            @RequestBody ExhibitionRegistrationRequest request
    ) {

        ExhibitionRegistrationResponse response =
                exhibitionService.registerCustomer(request);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // VALIDATE TOKEN
    // =========================================================

    @GetMapping("/token/{token}")
    public ResponseEntity<TokenValidationResponse>
    validateToken(
            @PathVariable String token,
            @RequestParam(required = false) Long exhibitionId
    ) {

        TokenValidationResponse response =
                exhibitionService.validateToken(
                        token,
                        exhibitionId
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // COMPLETE EYE TEST
    // =========================================================

    @PostMapping("/token/{token}/eye-test")
    public ResponseEntity<TokenValidationResponse>
    completeEyeTest(
            @PathVariable String token,
            @RequestParam Long exhibitionId
    ) {

        TokenValidationResponse response =
                exhibitionService.completeEyeTest(
                        token,
                        exhibitionId
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // CLAIM FREE SPECS
    // =========================================================

    @PostMapping("/token/{token}/free-specs")
    public ResponseEntity<TokenValidationResponse>
    claimFreeSpecs(
            @PathVariable String token,
            @RequestParam Long exhibitionId
    ) {

        TokenValidationResponse response =
                exhibitionService.claimFreeSpecs(
                        token,
                        exhibitionId
                );

        return ResponseEntity.ok(response);
    }
}
