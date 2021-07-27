package com.example.dynamicupdate.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.dynamicupdate.domain.entity.Member;
import com.example.dynamicupdate.domain.repository.MemberRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("@DynamicUpdate 미사용")
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void update_withoutDynamicUpdate() {
        // 1. member 생성
        String password = "1234";
        Member member = new Member("choi8608@gmail.com", password, "변경전 - withoutDynamicUpdate");
        memberRepository.save(member);

        long memberId = member.getId();

        // 2. 영속성 컨텍스트 초기화 후 member 조회
        em.clear();
        member = memberRepository.findById(memberId).get();

        // 3. 암호화된 비밀번호 확인
        System.out.println("### " + member.getPassword());
        assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();

        // 4. '자기소개' 만 수정 (dirtyCheck)
        // 4-1. 모든 컬럼이 업데이트되면서 password 가 또 암호화됨
        memberService.updateIntroduce(member.getId(), "변경후 - withoutDynamicUpdate");

        // 5. 영속성 컨텍스트 초기화 후 member 조회
        em.clear();
        member = memberRepository.findById(memberId).get();

        // 6. 비밀번호가 틀려짐
        System.out.println("### " + member.getPassword());
        assertThat(passwordEncoder.matches(password, member.getPassword())).isFalse();
    }
}
