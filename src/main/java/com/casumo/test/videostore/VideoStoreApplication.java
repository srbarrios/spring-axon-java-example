package com.casumo.test.videostore;

import org.axonframework.config.EventHandlingConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VideoStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoStoreApplication.class, args);
    }

    @Autowired
    public void configure(EventHandlingConfiguration config) {
        config.registerTrackingProcessor("com.casumo.test.videostore.listener");
    }

}
