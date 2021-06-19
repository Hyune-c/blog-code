package com.example.signupsecurity.common.token;

import com.example.signupsecurity.common.exception.ErrorCode;
import com.example.signupsecurity.common.exception.customexception.CustomAuthenticationException;
import com.example.signupsecurity.domain.member.entity.Member;
import com.example.signupsecurity.domain.member.repository.MemberRepository;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return memberRepository.findByEmail(email)
        .map(this::createSpringSecurityUser)
        .orElseThrow(() -> new CustomAuthenticationException(ErrorCode.NOT_EXIST_MEMBER));
  }

  private User createSpringSecurityUser(Member member) {
    List<GrantedAuthority> grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole()));
    return new User(member.getEmail(), member.getPassword(), grantedAuthorities);
  }
}
