package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Project repository
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOrderByName();

    @Query("select pj" +
            " from Project pj" +
            " where pj.visible = '1'" +
            " order by pj.name")
    List<Project> getAllByIsVisibleOrderByName();
}
