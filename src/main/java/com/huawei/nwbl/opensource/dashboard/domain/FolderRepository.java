package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project repository
 */
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Folder findByName(String name);
}
