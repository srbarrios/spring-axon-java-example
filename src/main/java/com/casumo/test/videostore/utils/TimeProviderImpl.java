package com.casumo.test.videostore.utils;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TimeProviderImpl implements TimeProvider {

    Date fixedDate;

    @Override
    public Date getCurrentDate() {
        return fixedDate == null ? new Date() : fixedDate;
    }

    @Override
    public void setFixedDate(Date date) {
        this.fixedDate = date;
    }

}
