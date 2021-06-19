package com.example.signupsecurity.common.resolver;

import com.example.signupsecurity.common.annotation.LoginMember;
import com.example.signupsecurity.common.exception.ErrorCode;
import com.example.signupsecurity.common.exception.customexception.CustomAuthrizationException;
import com.example.signupsecurity.common.token.JwtAuthToken;
import com.example.signupsecurity.common.token.JwtAuthTokenProvider;
import com.example.signupsecurity.domain.member.repository.MemberRepository;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

  private static final String AUTHORIZATION_HEADER = "authorization";

  private final JwtAuthTokenProvider jwtAuthTokenProvider;
  private final MemberRepository memberRepository;

  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    return methodParameter.hasParameterAnnotation(LoginMember.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
    LoginMember loginUserAnnotation = parameter.getParameterAnnotation(LoginMember.class);
    if (!loginUserAnnotation.required()) {
      return null;
    }

    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

    return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER))
        .map(authorization -> authorization.split("bearer")[1])
        .map(jwtAuthTokenProvider::convertAuthToken)
        .map(JwtAuthToken::getEmail)
        .map(email -> memberRepository.findByEmail(email)
            .orElseThrow(() -> new CustomAuthrizationException(ErrorCode.NOT_EXIST_MEMBER)))
        .orElseThrow(() -> new CustomAuthrizationException(ErrorCode.FORBIDDEN));
  }
}
