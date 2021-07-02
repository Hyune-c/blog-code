package com.hyune.exceptionlogging.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncService {

    @Async
    public void async(Integer tid) throws InterruptedException {
        Thread.sleep((long) (Math.random() * 1000 + 1));
        log.info("### mdc.tid={}, inputValue={}", MDC.get("tid"), tid);
    }
}
