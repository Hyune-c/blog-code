package com.hyune.exceptionlogging.common.exception;

import static com.hyune.exceptionlogging.common.exception.ErrorCode.FORBIDDEN;
import static com.hyune.exceptionlogging.common.exception.ErrorCode.UNAUTHORIZED;

import java.nio.file.AccessDeniedException;
import javax.security.sasl.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionAdvice extends BaseExceptionAdvice {

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합니다.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponse handleAuthenticationException(AuthenticationException ex) {
        preHandle(ex);
        return ErrorResponse.of(UNAUTHORIZED);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합니다.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
        preHandle(ex);
        return ErrorResponse.of(FORBIDDEN);
    }
}
