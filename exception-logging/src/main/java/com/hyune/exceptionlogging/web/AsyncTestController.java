package com.hyune.exceptionlogging.web;

import com.hyune.exceptionlogging.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AsyncTestController {

    private final AsyncService asyncService;

    @GetMapping("/api/test/async")
    public void asyncTest() throws InterruptedException {
        for (int i = 1; i <= 100; i++) {
            MDC.put("tid", String.valueOf(i));
            asyncService.async(i);
        }

        Thread.sleep(3000L);
    }
}
