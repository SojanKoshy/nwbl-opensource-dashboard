package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Gerrit account repository
 */
public interface GerritAccountRepository extends JpaRepository<GerritAccount, Long> {
    List<GerritAccount> findByMemberIsNotNull();
}
