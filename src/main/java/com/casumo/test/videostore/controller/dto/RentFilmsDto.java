package com.casumo.test.videostore.controller.dto;

import java.util.List;

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

    public String getCustomerId() {
        return customerId;
    }

    public List<String> getFilmsIds() {
        return filmsIds;
    }

    public void setFilmsIds(List<String> filmsIds) {
        this.filmsIds = filmsIds;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
