package com.casumo.test.videostore.controller;

import com.casumo.test.videostore.utils.TimeProvider;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.casumo.test.videostore.constants.VideoStoreConstants.SECURITY_TOKEN_TESTING;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
public class TestUtilsController {

    private TimeProvider timeProvider;

    public TestUtilsController(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }


    @PostMapping("/testutils/fixdate/{date}/{securitytoken}")
    public @ResponseBody
    ResponseEntity<Date> fixDate(@PathVariable @DateTimeFormat(iso = DATE) Date date, @PathVariable String securitytoken) {
        if (!securitytoken.equals(SECURITY_TOKEN_TESTING)) return new ResponseEntity<>(date, HttpStatus.FORBIDDEN);
        timeProvider.setFixedDate(date);
        return new ResponseEntity<>(date, HttpStatus.OK);
    }
}
