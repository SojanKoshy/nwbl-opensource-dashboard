package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Member repository
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByOrderByName();

    Member findByName(String name);


    @Query("select distinct mb" +
            " from Member mb, GerritAccount ga, GerritChange gc, Folder fd, Project pj" +
            " where ga.member = mb.id " +
            " and gc.account = ga.id" +
            " and gc.folder = fd.id" +
            " and fd.project = pj.id" +
            " and pj.id = ?1")
    List<Member> getDistinctByProject(Long projectId);
}
