package com.hyune.exceptionlogging.web;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/api/test")
    public void test() {
        System.out.println("system out");
        log.info("### log info");
        log.info("### MDC transactionId={}", MDC.get("transactionId"));
        throw new RuntimeException("test!! " + MDC.get("transactionId"));
    }
}
