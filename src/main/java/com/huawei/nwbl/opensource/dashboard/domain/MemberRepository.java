package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Member repository
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByOrderByName();
}
