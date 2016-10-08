package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Gerrit account repository
 */
public interface GerritAccountRepository extends JpaRepository<GerritAccount, Long> {
}
