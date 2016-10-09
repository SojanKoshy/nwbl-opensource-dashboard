package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Calendar;
import java.util.List;

/**
 * Gerrit change repository
 */
public interface GerritChangeRepository extends JpaRepository<GerritChange, Long> {
    List<GerritChange> findByStatus(String status);

    List<GerritChange> findByFolderIsNull();

    List<GerritChange> findByAccountIsNull();

    List<GerritChange> findByUpdatedOnBetween(Calendar fromDate, Calendar toDate);

    List<GerritChange> findByFirstFilePathContaining(String folderName);

    List<GerritChange> findByOwner(String owner);
}
