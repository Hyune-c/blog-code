package com.example.signupsecurity.common.token;

import com.example.signupsecurity.common.exception.ErrorCode;
import com.example.signupsecurity.common.exception.customexception.CustomAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class JwtAuthTokenProvider implements AuthTokenProvider<JwtAuthToken> {

  private static final String AUTHORITIES_KEY = "role";
  private final Key secreatKey;

  // secreat 을 통해 secreatKey 를 생성합니다.
  public JwtAuthTokenProvider(String secreatKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secreatKey);
    this.secreatKey = Keys.hmacShaKeyFor(keyBytes);
  }

  @Override
  public JwtAuthToken createAuthToken(String email, String role, Date expiredDate) {
    return new JwtAuthToken(email, role, expiredDate, secreatKey);
  }

  @Override
  public JwtAuthToken convertAuthToken(String token) {
    return new JwtAuthToken(token, secreatKey);
  }

  @Override
  public Authentication getAuthentication(JwtAuthToken authToken) {
    if (authToken.validate()) {
      Claims claims = authToken.getClaims();
      Collection<? extends GrantedAuthority> authorities = Collections.singleton(
          new SimpleGrantedAuthority(claims.get(AUTHORITIES_KEY).toString()));
      User principal = new User(claims.get("email").toString(), "", authorities);

      return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
    } else {
      throw new CustomAuthenticationException(ErrorCode.FAILED_GENERATE_TOKEN);
    }
  }
}
