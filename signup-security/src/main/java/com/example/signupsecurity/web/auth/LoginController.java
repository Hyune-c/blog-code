package com.example.signupsecurity.web.auth;

import com.example.signupsecurity.common.token.JwtAuthToken;
import com.example.signupsecurity.domain.auth.service.LoginService;
import com.example.signupsecurity.web.auth.request.LoginMemberRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginController {

  private final LoginService loginService;

  @PostMapping("/api/v1/login")
  public String login(@RequestBody @Valid LoginMemberRequest request) {
    JwtAuthToken jwtAuthToken = loginService.login(request.getLoginId(), request.getPassword());

    return jwtAuthToken.getToken();
  }
}
