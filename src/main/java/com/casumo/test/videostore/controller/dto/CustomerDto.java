package com.casumo.test.videostore.controller.dto;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class CustomerDto {

    protected String fullName;

    protected Integer phoneNumber;

    public CustomerDto() {
    }

    public CustomerDto(String fullName, Integer phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
