package com.casumo.test.videostore.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReturnFilmsDto {

    private String customerId;

    private List<String> filmsIds;

    public ReturnFilmsDto() {
    }

    public ReturnFilmsDto(String customerId, List<String> filmsIds) {
        this.customerId = customerId;
        this.filmsIds = filmsIds;
    }

}
