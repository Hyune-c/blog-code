package com.example.signupsecurity.common.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Converter
public class PasswordEncryptConverter implements AttributeConverter<String, String> {

  private final PasswordEncoder passwordEncoder;

  @Override
  public String convertToDatabaseColumn(String entityAttribute) {
    return passwordEncoder.encode(entityAttribute);
  }

  @Override
  public String convertToEntityAttribute(String databaseColumn) {
    // DB 에서 가져올 때는 복호화하지 않습니다.
    return databaseColumn;
  }
}
