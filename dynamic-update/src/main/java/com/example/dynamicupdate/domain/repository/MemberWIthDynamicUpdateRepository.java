package com.example.dynamicupdate.domain.repository;

import com.example.dynamicupdate.domain.entity.MemberWIthDynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberWIthDynamicUpdateRepository extends JpaRepository<MemberWIthDynamicUpdate, Long> {

}
