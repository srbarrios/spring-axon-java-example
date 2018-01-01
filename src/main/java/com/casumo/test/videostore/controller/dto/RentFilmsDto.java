package com.casumo.test.videostore.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class RentFilmsDto {

    private String customerId;

    private List<String> filmsIds;

    private int days;

    public RentFilmsDto() {
    }

    public RentFilmsDto(String customerId, List<String> filmsIds, int days) {
        this.customerId = customerId;
        this.filmsIds = filmsIds;
        this.days = days;
    }

}
