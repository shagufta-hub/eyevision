package com.eyevision.eyevision.dto;

public class TokenValidationResponse {

    private boolean valid;

    private String message;

    private Long customerId;

    private String customerName;

    private String mobile;

    private String email;

    private String membershipToken;

    private Long exhibitionId;

    private String exhibitionName;

    private boolean eyeTestCompleted;

    private boolean freeSpecsClaimed;

    private Integer discountPercentage;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMembershipToken() {
        return membershipToken;
    }

    public void setMembershipToken(String membershipToken) {
        this.membershipToken = membershipToken;
    }

    public Long getExhibitionId() {
        return exhibitionId;
    }

    public void setExhibitionId(Long exhibitionId) {
        this.exhibitionId = exhibitionId;
    }

    public String getExhibitionName() {
        return exhibitionName;
    }

    public void setExhibitionName(String exhibitionName) {
        this.exhibitionName = exhibitionName;
    }

    public boolean isEyeTestCompleted() {
        return eyeTestCompleted;
    }

    public void setEyeTestCompleted(boolean eyeTestCompleted) {
        this.eyeTestCompleted = eyeTestCompleted;
    }

    public boolean isFreeSpecsClaimed() {
        return freeSpecsClaimed;
    }

    public void setFreeSpecsClaimed(boolean freeSpecsClaimed) {
        this.freeSpecsClaimed = freeSpecsClaimed;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}