package com.example.signupsecurity.common.exception.customexception;

import com.example.signupsecurity.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CustomAuthenticationException extends RuntimeException {

  private final ErrorCode errorCode;

  public CustomAuthenticationException(ErrorCode errorCode) {
    super(errorCode.getReason());
    this.errorCode = errorCode;
  }
}
