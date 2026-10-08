package com.eyevision.eyevision.dto;

public class ExhibitionRegistrationResponse {

    private boolean success;
    private String message;

    private Long customerId;
    private String name;
    private String membershipToken;

    private boolean alreadyRegistered;

    public ExhibitionRegistrationResponse() {
    }

    public ExhibitionRegistrationResponse(
            boolean success,
            String message,
            Long customerId,
            String name,
            String membershipToken,
            boolean alreadyRegistered
    ) {
        this.success = success;
        this.message = message;
        this.customerId = customerId;
        this.name = name;
        this.membershipToken = membershipToken;
        this.alreadyRegistered = alreadyRegistered;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMembershipToken() {
        return membershipToken;
    }

    public void setMembershipToken(String membershipToken) {
        this.membershipToken = membershipToken;
    }

    public boolean isAlreadyRegistered() {
        return alreadyRegistered;
    }

    public void setAlreadyRegistered(boolean alreadyRegistered) {
        this.alreadyRegistered = alreadyRegistered;
    }
}