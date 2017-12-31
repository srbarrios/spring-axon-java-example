package com.casumo.test.videostore.controller.dto;

import java.util.List;

public class ReturnFilmsDto {

    private String customerId;

    private List<String> filmsIds;

    public ReturnFilmsDto() {
    }

    public ReturnFilmsDto(String customerId, List<String> filmsIds) {
        this.customerId = customerId;
        this.filmsIds = filmsIds;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<String> getFilmsIds() {
        return filmsIds;
    }

    public void setFilmsIds(List<String> filmsIds) {
        this.filmsIds = filmsIds;
    }
}
