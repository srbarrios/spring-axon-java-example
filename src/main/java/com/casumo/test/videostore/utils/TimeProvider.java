package com.casumo.test.videostore.utils;

import java.util.Date;

public interface TimeProvider {

    Date getCurrentDate();

    void setFixedDate(Date date);
}