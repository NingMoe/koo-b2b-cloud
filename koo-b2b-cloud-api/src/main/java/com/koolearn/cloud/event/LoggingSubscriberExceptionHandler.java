package com.koolearn.cloud.event;


import org.apache.log4j.Logger;


class LoggingSubscriberExceptionHandler implements SubscriberExceptionHandler {
    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context) {
        Logger log = Logger.getLogger(this.getClass());
        log.error("Could not dispatch event: " + context.getSubscriber() + " to " + context.getSubscriberMethod(),
                     exception.getCause());
    }
}
