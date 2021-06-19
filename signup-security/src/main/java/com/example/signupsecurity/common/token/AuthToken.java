package com.example.signupsecurity.common.token;

public interface AuthToken<T> {

  boolean validate();

  T getClaims();
}
