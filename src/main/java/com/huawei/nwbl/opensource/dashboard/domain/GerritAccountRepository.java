package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Gerrit account repository
 */
public interface GerritAccountRepository extends JpaRepository<GerritAccount, Long> {
    List<GerritAccount> findAllByMemberIsNotNull();

    List<GerritAccount> findByEmail(String email);

    List<GerritAccount> findAllByMemberIsNullOrderByName();

    @Query("select ga" +
            " from GerritAccount ga, Member mb" +
            " where ga.member = mb" +
            " order by mb.name")
    List<GerritAccount> getAllOrderByMemberName();
}
