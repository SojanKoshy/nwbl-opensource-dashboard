package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.List;

/**
 * Gerrit change repository
 */
public interface GerritChangeRepository extends JpaRepository<GerritChange, Long> {
    List<GerritChange> findAllByStatus(String status);

    List<GerritChange> findAllByFolderIsNullOrderByIdDesc();

    List<GerritChange> findAllByAccountIsNullOrderByIdDesc();

    List<GerritChange> findAllByUpdatedOnBetween(Calendar fromDate, Calendar toDate);

    List<GerritChange> findAllByFirstFilePathContaining(String folderName);

    List<GerritChange> findAllByOwner(String owner);

    @Query("select gc" +
            " from GerritChange gc, GerritAccount ga, Member mb" +
            " where gc.account = ga.id"+
            " and ga.member = mb.id" +
            " and mb.id = ?1" +
            " order by gc.id desc")
    List<GerritChange> getAllByMember(Long memberId);

    @Query("select gc" +
            " from GerritChange gc, Folder fd, Project pj" +
            " where gc.folder = fd.id"+
            " and fd.project = pj.id" +
            " and pj.id = ?1" +
            " order by gc.id desc")
    List<GerritChange> getAllByProject(Long id);

    @Query("select sum(gc.actualSize)" +
            " from GerritChange gc, GerritAccount ga, Member mb" +
            " where gc.account = ga.id"+
            " and ga.member = mb.id" +
            " and mb.id = ?1" +
            " and gc.status = ?2")
    Integer getSumActualSizeByMemberAndStatus(Long memberId, String status);

    @Query("select sum(gc.actualSize)" +
            " from GerritChange gc, Folder fd, Project pj" +
            " where gc.folder = fd.id"+
            " and fd.project = pj.id" +
            " and pj.id = ?1" +
            " and gc.status = ?2")
    Integer getSumActualSizeByProjectAndStatus(Long projectId, String status);

    @Query("select sum(gc.actualSize)" +
            " from GerritChange gc" +
            " where gc.status = ?1")
    Integer getSumActualSizeByStatus(String status);
}
