package com.hyune.exceptionlogging.common.exception;

import com.hyune.exceptionlogging.common.exception.custom.BusinessException;
import com.hyune.exceptionlogging.common.exception.custom.PaymentException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.MDC;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private final String code;
    private final String message;
    private final LocalDateTime time = LocalDateTime.now();
    private final String transactionId = MDC.get("transactionId");
    private final List<FieldError> errors;

    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        return ErrorResponse.builder()
            .code(errorCode.name())
            .message(errorCode.getReason())
            .errors(FieldError.of(bindingResult))
            .build();
    }

    public static ErrorResponse of(ErrorCode errorCode, List<FieldError> errors) {
        return ErrorResponse.builder()
            .code(errorCode.name())
            .message(errorCode.getReason())
            .errors(errors)
            .build();
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
            .code(errorCode.name())
            .message(errorCode.getReason())
            .errors(new ArrayList<>())
            .build();
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException ex) {
        String value = (ex.getValue() == null) ? "" : ex.getValue().toString();
        List<FieldError> errors = Collections.singletonList(FieldError.of(ex.getName(), value, ex.getErrorCode()));
        return ErrorResponse.of(ErrorCode.BAD_REQUEST, errors);
    }

    public static ErrorResponse of(MissingServletRequestParameterException ex) {
        List<FieldError> errors = Collections.singletonList(FieldError.of(ex.getParameterName(), null, "Not exist"));
        return ErrorResponse.of(ErrorCode.BAD_REQUEST, errors);
    }

    public static ErrorResponse of(ConstraintViolationException ex) {
        List<FieldError> errors = ex.getConstraintViolations().stream()
            .map(violation -> FieldError.of(extractPropertyName(violation.getPropertyPath()), null, violation.getMessage()))
            .collect(Collectors.toList());
        return ErrorResponse.of(ErrorCode.BAD_REQUEST, errors);
    }

    public static ErrorResponse of(PaymentException ex) {
        List<FieldError> errors = Collections.singletonList(FieldError.of("orderId", ex.getOrderId(), null));
        return ErrorResponse.of(ex.getErrorCode(), errors);
    }

    public static ErrorResponse of(BusinessException ex) {
        return ErrorResponse.of(ex.getErrorCode());
    }

    /**
     * 속성 전체 경로에서 속성 이름만 가져옵니다.
     *
     * @param propertyPath 속성 전체 경로
     * @return
     */
    private static String extractPropertyName(Path propertyPath) {
        String pathString = propertyPath.toString();
        return pathString.substring(pathString.lastIndexOf('.') + 1);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FieldError {

        private final String field;
        private final String value;
        private final String reason;

        public static FieldError of(String field, String value, String reason) {
            return new FieldError(field, value, reason);
        }

        public static FieldError of(org.springframework.validation.FieldError fieldError) {
            return new FieldError(
                fieldError.getField(),
                (fieldError.getRejectedValue() == null) ? "" : fieldError.getRejectedValue().toString(),
                fieldError.getDefaultMessage());
        }

        private static List<FieldError> of(BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                .map(FieldError::of)
                .collect(Collectors.toList());
        }
    }
}
