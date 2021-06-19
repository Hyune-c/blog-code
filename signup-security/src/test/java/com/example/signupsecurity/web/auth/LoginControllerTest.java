package com.example.signupsecurity.web.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.signupsecurity.domain.member.entity.Member;
import com.example.signupsecurity.domain.member.repository.MemberRepository;
import com.example.signupsecurity.web.auth.request.LoginMemberRequest;
import com.example.signupsecurity.web.member.request.CreateMemberRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LoginControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private MemberRepository memberRepository;

  public static String jwt;

  @DisplayName("[web] 로그인")
  @TestFactory
  public Stream<DynamicTest> login() {
    String email = "hyune@gmail.com";
    String password = "1q2w3e4r*";

    return Stream.of(
        dynamicTest("[성공] 회원 가입", () -> {
          // given
          CreateMemberRequest request = new CreateMemberRequest(email, password);

          // when
          MvcResult mvcResult = mockMvc.perform(post("/api/v1/members")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
              .andDo(print())
              .andExpect(status().isCreated())
              .andReturn();

          // then
          Long memberId = Long.valueOf(mvcResult.getResponse().getContentAsString());
          assertThat(memberRepository.existsById(memberId)).isTrue();

          Member member = memberRepository.findById(memberId).get();
          assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();

          System.out.println("### member.getId()=" + member.getId()
              + ", member.getEmail()=" + member.getEmail()
              + ", member.getPassword()=" + member.getPassword());
        }),
        dynamicTest("[성공] 로그인", () -> {
          // given
          LoginMemberRequest request = new LoginMemberRequest(email, password);

          // when
          MvcResult mvcResult = mockMvc.perform(post("/api/v1/login")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
              .andDo(print())
              .andExpect(status().isOk())
              .andReturn();

          // then
          String token = mvcResult.getResponse().getContentAsString();
          assertThat(token).isNotBlank();

          jwt = token;
          System.out.println("### token=" + mvcResult.getResponse().getContentAsString());
        }),
        dynamicTest("[성공] jwt 로 내 정보 조회", () -> {
          // given

          // when
          mockMvc.perform(get("/api/v1/my")
              .header(HttpHeaders.AUTHORIZATION, "bearer " + jwt))
              .andDo(print())
              .andExpect(status().isOk());

          // then
        })
    );
  }
}
