package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Project repository
 */
public interface OnosProjectRepository extends JpaRepository<OnosProject, Long> {

    OnosProject findByName(String name);

    List<OnosProject> findByOrderByName();
}
