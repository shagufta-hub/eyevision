package com.eyevision.eyevision.serviceimplement;


import com.eyevision.eyevision.dto.ExhibitionRegistrationRequest;
import com.eyevision.eyevision.dto.ExhibitionRegistrationResponse;
import com.eyevision.eyevision.dto.TokenValidationResponse;
import com.eyevision.eyevision.entity.Exhibition;
import com.eyevision.eyevision.entity.ExhibitionCustomer;
import com.eyevision.eyevision.entity.ExhibitionVisit;
import com.eyevision.eyevision.repository.ExhibitionCustomerRepository;
import com.eyevision.eyevision.repository.ExhibitionRepository;
import com.eyevision.eyevision.repository.ExhibitionVisitRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitionCustomerRepository customerRepository;
    private final ExhibitionVisitRepository visitRepository;

    public ExhibitionService(
            ExhibitionRepository exhibitionRepository,
            ExhibitionCustomerRepository customerRepository,
            ExhibitionVisitRepository visitRepository
    ) {
        this.exhibitionRepository = exhibitionRepository;
        this.customerRepository = customerRepository;
        this.visitRepository = visitRepository;
    }

    // =========================================================
    // REGISTER CUSTOMER
    // =========================================================

    @Transactional
    public ExhibitionRegistrationResponse registerCustomer(
            ExhibitionRegistrationRequest request
    ) {

        System.out.println("Registering customer: " + request.getExhibitionId());
        if (request.getName() == null ||
                request.getName().trim().isEmpty()) {

            return new ExhibitionRegistrationResponse(
                    false,
                    "Name is required",
                    null,
                    null,
                    null,
                    false
            );
        }

        if (request.getMobile() == null ||
                request.getMobile().trim().isEmpty()) {

            return new ExhibitionRegistrationResponse(
                    false,
                    "Mobile number is required",
                    null,
                    null,
                    null,
                    false
            );
        }

        if (request.getExhibitionId() == null) {

            return new ExhibitionRegistrationResponse(
                    false,
                    "Exhibition ID is required",
                    null,
                    null,
                    null,
                    false
            );
        }

        // -----------------------------------------------------
        // Find exhibition
        // -----------------------------------------------------

        Exhibition exhibition =
                exhibitionRepository
                        .findByIdAndActiveTrue(request.getExhibitionId())
                        .orElse(null);

        if (exhibition == null) {

            return new ExhibitionRegistrationResponse(
                    false,
                    "Exhibition not found or inactive",
                    null,
                    null,
                    null,
                    false
            );
        }

        String mobile = request.getMobile().trim();

        // -----------------------------------------------------
        // Check existing customer
        // -----------------------------------------------------

        ExhibitionCustomer customer =
                customerRepository
                        .findByMobile(mobile)
                        .orElse(null);

        boolean alreadyRegistered = false;

        if (customer == null) {

            customer = new ExhibitionCustomer();

            customer.setName(request.getName().trim());
            customer.setMobile(mobile);
            customer.setEmail(
                    request.getEmail() != null
                            ? request.getEmail().trim()
                            : null
            );

            customer.setMembershipToken(
                    generateUniqueToken()
            );

            customer.setActive(true);

            customer = customerRepository.save(customer);

        } else {

            alreadyRegistered = true;

            // Update name/email if supplied
            if (request.getName() != null &&
                    !request.getName().trim().isEmpty()) {

                customer.setName(request.getName().trim());
            }

            if (request.getEmail() != null &&
                    !request.getEmail().trim().isEmpty()) {

                customer.setEmail(request.getEmail().trim());
            }

            customer = customerRepository.save(customer);
        }

        // -----------------------------------------------------
        // Check visit for this exhibition
        // -----------------------------------------------------

        ExhibitionVisit visit =
                visitRepository
                        .findByCustomerIdAndExhibitionId(
                                customer.getId(),
                                exhibition.getId()
                        )
                        .orElse(null);

        if (visit == null) {

            visit = new ExhibitionVisit();

            visit.setCustomer(customer);
            visit.setExhibition(exhibition);

            visit.setEyeTestCompleted(false);
            visit.setFreeSpecsClaimed(false);

            // Exhibition discount
            visit.setDiscountPercentage(10);

            visitRepository.save(visit);
        }

        return new ExhibitionRegistrationResponse(
                true,
                alreadyRegistered
                        ? "Customer already registered"
                        : "Registration successful",
                customer.getId(),
                customer.getName(),
                customer.getMembershipToken(),
                alreadyRegistered
        );
    }

    // =========================================================
    // GENERATE UNIQUE TOKEN
    // =========================================================

    private String generateUniqueToken() {

        String token;

        do {

            String randomPart =
                    UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .substring(0, 8)
                            .toUpperCase();

            token = "EV26-" + randomPart;

        } while (
                customerRepository.existsByMembershipToken(token)
        );

        return token;
    }

    // =========================================================
    // VALIDATE TOKEN
    // =========================================================

    @Transactional(readOnly = true)
    public TokenValidationResponse validateToken(
            String token,
            Long exhibitionId
    ) {

        TokenValidationResponse response =
                new TokenValidationResponse();

        if (token == null || token.trim().isEmpty()) {

            response.setValid(false);
            response.setMessage("Token is required");

            return response;
        }

        ExhibitionCustomer customer =
                customerRepository
                        .findByMembershipToken(
                                token.trim().toUpperCase()
                        )
                        .orElse(null);

        if (customer == null) {

            response.setValid(false);
            response.setMessage("Invalid membership token");

            return response;
        }

        if (!Boolean.TRUE.equals(customer.getActive())) {

            response.setValid(false);
            response.setMessage("Membership is inactive");

            return response;
        }

        ExhibitionVisit visit;

        if (exhibitionId != null) {

            visit =
                    visitRepository
                            .findByCustomerIdAndExhibitionId(
                                    customer.getId(),
                                    exhibitionId
                            )
                            .orElse(null);

            if (visit == null) {

                response.setValid(false);
                response.setMessage(
                        "Customer is not registered for this exhibition"
                );

                return response;
            }

        } else {

            // If exhibition ID is not supplied,
            // token itself is still valid.
            visit = null;
        }

        response.setValid(true);
        response.setMessage("Valid membership token");

        response.setCustomerId(customer.getId());
        response.setCustomerName(customer.getName());
        response.setMobile(customer.getMobile());
        response.setEmail(customer.getEmail());
        response.setMembershipToken(customer.getMembershipToken());

        if (visit != null) {

            response.setExhibitionId(
                    visit.getExhibition().getId()
            );

            response.setExhibitionName(
                    visit.getExhibition().getName()
            );

            response.setEyeTestCompleted(
                    Boolean.TRUE.equals(
                            visit.getEyeTestCompleted()
                    )
            );

            response.setFreeSpecsClaimed(
                    Boolean.TRUE.equals(
                            visit.getFreeSpecsClaimed()
                    )
            );

            response.setDiscountPercentage(
                    visit.getDiscountPercentage()
            );
        }

        return response;
    }

    // =========================================================
    // COMPLETE EYE TEST
    // =========================================================

    @Transactional
    public TokenValidationResponse completeEyeTest(
            String token,
            Long exhibitionId
    ) {

        ExhibitionCustomer customer =
                getCustomerByToken(token);

        ExhibitionVisit visit =
                getVisit(customer, exhibitionId);

        visit.setEyeTestCompleted(true);
        visit.setEyeTestCompletedAt(
                LocalDateTime.now()
        );

        visitRepository.save(visit);

        return buildResponse(customer, visit);
    }

    // =========================================================
    // CLAIM FREE SPECS
    // =========================================================

    @Transactional
    public TokenValidationResponse claimFreeSpecs(
            String token,
            Long exhibitionId
    ) {

        ExhibitionCustomer customer =
                getCustomerByToken(token);

        ExhibitionVisit visit =
                getVisit(customer, exhibitionId);

        // Eye test should be completed first
        if (!Boolean.TRUE.equals(
                visit.getEyeTestCompleted()
        )) {

            throw new RuntimeException(
                    "Eye test must be completed before claiming free spectacles"
            );
        }

        // Already claimed
        if (Boolean.TRUE.equals(
                visit.getFreeSpecsClaimed()
        )) {

            throw new RuntimeException(
                    "Free spectacles have already been claimed"
            );
        }

        visit.setFreeSpecsClaimed(true);

        visit.setFreeSpecsClaimedAt(
                LocalDateTime.now()
        );

        visitRepository.save(visit);

        return buildResponse(customer, visit);
    }

    // =========================================================
    // GET CUSTOMER
    // =========================================================

    private ExhibitionCustomer getCustomerByToken(
            String token
    ) {

        if (token == null ||
                token.trim().isEmpty()) {

            throw new RuntimeException(
                    "Membership token is required"
            );
        }

        return customerRepository
                .findByMembershipToken(
                        token.trim().toUpperCase()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid membership token"
                        )
                );
    }

    // =========================================================
    // GET VISIT
    // =========================================================

    private ExhibitionVisit getVisit(
            ExhibitionCustomer customer,
            Long exhibitionId
    ) {

        if (exhibitionId == null) {

            throw new RuntimeException(
                    "Exhibition ID is required"
            );
        }

        return visitRepository
                .findByCustomerIdAndExhibitionId(
                        customer.getId(),
                        exhibitionId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer is not registered for this exhibition"
                        )
                );
    }

    // =========================================================
    // BUILD RESPONSE
    // =========================================================

    private TokenValidationResponse buildResponse(
            ExhibitionCustomer customer,
            ExhibitionVisit visit
    ) {

        TokenValidationResponse response =
                new TokenValidationResponse();

        response.setValid(true);
        response.setMessage(
                "Membership information retrieved successfully"
        );

        response.setCustomerId(customer.getId());
        response.setCustomerName(customer.getName());
        response.setMobile(customer.getMobile());
        response.setEmail(customer.getEmail());

        response.setMembershipToken(
                customer.getMembershipToken()
        );

        response.setExhibitionId(
                visit.getExhibition().getId()
        );

        response.setExhibitionName(
                visit.getExhibition().getName()
        );

        response.setEyeTestCompleted(
                Boolean.TRUE.equals(
                        visit.getEyeTestCompleted()
                )
        );

        response.setFreeSpecsClaimed(
                Boolean.TRUE.equals(
                        visit.getFreeSpecsClaimed()
                )
        );

        response.setDiscountPercentage(
                visit.getDiscountPercentage()
        );

        return response;
    }
}
