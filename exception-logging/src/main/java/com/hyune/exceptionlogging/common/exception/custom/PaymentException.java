package com.hyune.exceptionlogging.common.exception.custom;

import com.hyune.exceptionlogging.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String orderId;

  public PaymentException(ErrorCode errorCode) {
    super(errorCode.getReason());
    this.errorCode = errorCode;
    this.orderId = "";
  }

  public PaymentException(ErrorCode errorCode, String orderId) {
    super(errorCode.getReason());
    this.errorCode = errorCode;
    this.orderId = orderId;
  }
}
