package com.casumo.test.videostore.callback;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class LoggingCallback<C, R> implements CommandCallback<C, R> {

    private static final Logger logger = LoggerFactory.getLogger(LoggingCallback.class);

    public static final LoggingCallback INSTANCE = new LoggingCallback();

    private LoggingCallback() {
    }

    @Override
    public void onSuccess(CommandMessage<? extends C> commandMessage, R result) {
        logger.info("Command successful: {} -> {}", commandMessage.getPayloadType().getSimpleName(), Objects.toString(result));
    }

    @Override
    public void onFailure(CommandMessage<? extends C> commandMessage, Throwable cause) {
        cause.printStackTrace();
        logger.info("Command failed: {} -> {}", commandMessage.getPayloadType().getSimpleName(),
                cause.getClass().getSimpleName());
    }
}
