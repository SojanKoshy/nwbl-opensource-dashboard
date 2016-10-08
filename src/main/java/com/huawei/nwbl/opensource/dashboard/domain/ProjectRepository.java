package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Project repository
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOrderByName();
}
