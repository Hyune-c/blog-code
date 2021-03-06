package com.hyune.exceptionlogging.common.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseExceptionAdvice {

    protected void preHandle(Exception ex) {
        log.error("### message={}, cause={}", ex.getMessage(), ex.getCause(), ex);
    }
}
