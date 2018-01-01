package com.casumo.test.videostore.controller.dto;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class CustomerDto {

    protected String fullName;

    protected Integer phoneNumber;

    public CustomerDto() {
    }

    public CustomerDto(String fullName, Integer phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

}
