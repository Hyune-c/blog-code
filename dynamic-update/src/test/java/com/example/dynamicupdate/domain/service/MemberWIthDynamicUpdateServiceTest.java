package com.example.dynamicupdate.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.dynamicupdate.domain.entity.MemberWIthDynamicUpdate;
import com.example.dynamicupdate.domain.repository.MemberWIthDynamicUpdateRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("@DynamicUpdate 사용")
@SpringBootTest
class MemberWIthDynamicUpdateServiceTest {

    @Autowired
    private MemberWIthDynamicUpdateRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MemberWIthDynamicUpdateService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void update_withDynamicUpdate() {
        // 1. member 생성
        String password = "1234";
        MemberWIthDynamicUpdate member = new MemberWIthDynamicUpdate("choi8608@gmail.com", password, "변경전 - withDynamicUpdate");
        memberRepository.save(member);

        long memberId = member.getId();

        // 2. 영속성 컨텍스트 초기화 후 member 조회
        em.clear();
        member = memberRepository.findById(memberId).get();

        // 3. 암호화된 비밀번호 확인
        System.out.println("### " + member.getPassword());
        assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();

        // 4. '자기소개' 만 수정 (dirtyCheck)
        // 4-1. 수정한 컬럼만 update 됨
        memberService.updateIntroduce(member.getId(), "변경후 - withDynamicUpdate");

        // 5. 영속성 컨텍스트 초기화 후 member 조회
        em.clear();
        member = memberRepository.findById(memberId).get();

        // 6. 비밀번호가 그대로 유지됨
        System.out.println("### " + member.getPassword());
        assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();
    }
}
