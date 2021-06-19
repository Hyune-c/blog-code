package com.example.signupsecurity.web.auth.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginMemberRequest {

  @Email
  @NotEmpty
  private String loginId;

  @NotEmpty
  private String password;
}
