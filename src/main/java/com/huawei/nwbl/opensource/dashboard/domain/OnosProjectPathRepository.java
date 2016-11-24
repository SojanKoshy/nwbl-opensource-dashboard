package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Project repository
 */
public interface OnosProjectPathRepository extends JpaRepository<OnosProjectPath, Long> {
    OnosProjectPath findByPath(String path);

}
