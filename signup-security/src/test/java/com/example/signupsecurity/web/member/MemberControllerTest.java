package com.example.signupsecurity.web.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.signupsecurity.domain.member.entity.Member;
import com.example.signupsecurity.domain.member.repository.MemberRepository;
import com.example.signupsecurity.web.member.request.CreateMemberRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@DisplayName("[web] 회원")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private MemberRepository memberRepository;

  @DisplayName("[성공] 회원 가입")
  @Test
  public void success_create() throws Exception {
    // given
    String password = "1q2w3e4r*";
    CreateMemberRequest request = new CreateMemberRequest("hyune@gmail.com", password);

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
  }
}
