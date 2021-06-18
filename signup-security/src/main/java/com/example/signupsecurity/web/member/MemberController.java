package com.example.signupsecurity.web.member;

import com.example.signupsecurity.domain.member.entity.Member;
import com.example.signupsecurity.domain.member.repository.MemberRepository;
import com.example.signupsecurity.web.member.request.CreateMemberRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

  private final MemberRepository memberRepository;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/api/v1/members")
  public Long create(@RequestBody @Valid CreateMemberRequest request) {
    Member member = new Member(request.getEmail(), request.getPassword());

    return memberRepository.save(member).getId();
  }
}
