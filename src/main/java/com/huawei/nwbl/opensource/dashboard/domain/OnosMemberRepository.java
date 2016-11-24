package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root1 on 23/11/16.
 */
public interface OnosMemberRepository extends JpaRepository<OnosMember, Long> {

    OnosMember findByEmail(String email);
}
