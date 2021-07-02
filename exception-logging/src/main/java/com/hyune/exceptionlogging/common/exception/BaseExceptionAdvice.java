package com.hyune.exceptionlogging.common.exception;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseExceptionAdvice {

    /**
     * UUID 를 통해 서버 로그를 쉽게 검색합니다.
     *
     * @param ex
     * @return
     */
    protected UUID generateLogId(Exception ex) {
        UUID uuid = UUID.randomUUID();
        log.error("### {}, {}", uuid, ex.getClass().getSimpleName(), ex);
        return uuid;
    }
}
