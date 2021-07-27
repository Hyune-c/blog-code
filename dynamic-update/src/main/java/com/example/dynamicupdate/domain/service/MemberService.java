package com.example.dynamicupdate.domain.service;

import com.example.dynamicupdate.domain.entity.Member;
import com.example.dynamicupdate.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public void updateIntroduce(long memberId, String introduce) {
        Member member = memberRepository.findById(memberId).get();
        member.updateIntroduce(introduce);
    }
}
