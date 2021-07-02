package com.hyune.exceptionlogging.common.exception;

import com.hyune.exceptionlogging.common.exception.custom.BusinessException;
import com.hyune.exceptionlogging.common.exception.custom.PaymentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionAdvice extends BaseExceptionAdvice {

    @ExceptionHandler(PaymentException.class)
    protected ResponseEntity<ErrorResponse> handlePaymentException(PaymentException ex) {
        return new ResponseEntity<>(ErrorResponse.of(ex, generateLogId(ex)),
            ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        return new ResponseEntity<>(ErrorResponse.of(ex, generateLogId(ex)),
            ex.getErrorCode().getStatus());
    }
}
