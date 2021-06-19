package com.example.signupsecurity.domain.auth.service;

import static com.example.signupsecurity.common.exception.ErrorCode.NOT_EXIST_AUTHORIZATION;
import static com.example.signupsecurity.common.exception.ErrorCode.NOT_EXIST_MEMBER;
import static com.example.signupsecurity.common.exception.ErrorCode.NOT_VALID_PASSWORD;

import com.example.signupsecurity.common.exception.customexception.CustomAuthenticationException;
import com.example.signupsecurity.common.exception.customexception.CustomAuthrizationException;
import com.example.signupsecurity.common.token.JwtAuthToken;
import com.example.signupsecurity.common.token.JwtAuthTokenProvider;
import com.example.signupsecurity.common.token.Role;
import com.example.signupsecurity.domain.member.entity.Member;
import com.example.signupsecurity.domain.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {

  private final static long LOGIN_RETENTION_MINUTES = 300;

  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final JwtAuthTokenProvider jwtAuthTokenProvider;
  private final PasswordEncoder passwordEncoder;

  private final MemberRepository memberRepository;

  public JwtAuthToken login(String loginId, String password) {
    Member member = memberRepository.findByEmail(loginId)
        .orElseThrow(() -> new CustomAuthenticationException(NOT_EXIST_MEMBER));

    if (!passwordEncoder.matches(password, member.getPassword())) {
      throw new CustomAuthenticationException(NOT_VALID_PASSWORD);
    }

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getEmail(), password);
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    Role role = authentication.getAuthorities().stream()
        .findFirst()
        .map(GrantedAuthority::getAuthority)
        .map(Role::of)
        .orElseThrow(() -> new CustomAuthrizationException(NOT_EXIST_AUTHORIZATION));
    Date expiredDate = Date.from(
        LocalDateTime.now().plusMinutes(LOGIN_RETENTION_MINUTES).atZone(ZoneId.systemDefault()).toInstant());

    return jwtAuthTokenProvider.createAuthToken(member.getEmail(), role.getCode(), expiredDate);
  }
}
