package com.example.dynamicupdate.domain.service;

import com.example.dynamicupdate.domain.entity.MemberWIthDynamicUpdate;
import com.example.dynamicupdate.domain.repository.MemberWIthDynamicUpdateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberWIthDynamicUpdateService {

    private final MemberWIthDynamicUpdateRepository memberWIthDynamicUpdateRepository;

    public void updateIntroduce(long memberId, String introduce) {
        MemberWIthDynamicUpdate member = memberWIthDynamicUpdateRepository.findById(memberId).get();
        member.updateIntroduce(introduce);
    }
}
