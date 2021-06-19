package com.example.signupsecurity.domain.member.entity;

import com.example.signupsecurity.common.converter.PasswordEncryptConverter;
import com.example.signupsecurity.common.token.Role;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String email;

  @Convert(converter = PasswordEncryptConverter.class)
  private String password;

  private String role;

  public Member(String email, String password) {
    this.email = email;
    this.password = password;
    this.role = Role.MEMBER.getCode();
  }
}
