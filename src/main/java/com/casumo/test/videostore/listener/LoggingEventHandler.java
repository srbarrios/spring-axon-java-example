package com.casumo.test.videostore.listener;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingEventHandler {

    private Logger logger = LoggerFactory.getLogger(LoggingEventHandler.class);

    @EventHandler
    public void on(Object event) {
        logger.debug("Event received: " + event.toString());
    }
}
