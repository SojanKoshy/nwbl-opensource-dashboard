package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Gerrit change repository
 */
public interface GerritChangeRepository extends JpaRepository<GerritChange, Long> {
    List<GerritChange> findAllByFolderIsNullOrderByIdDesc();

    List<GerritChange> findAllByAccountIsNullOrderByIdDesc();

    List<GerritChange> findAllByFirstFilePathContaining(String folderName);

    List<GerritChange> findAllByOwner(String owner);

    List<GerritChange> findAllByFirstFilePathIsNotContaining(String folder);

    @Query("select gc.id, gc.link, pj.name, mb.name, gc.actualSize, gc.status, gc.updatedOn" +
            " from GerritChange gc, Folder fd, Project pj, GerritAccount ga, Member mb" +
            " where gc.folder = fd.id" +
            " and fd.project = pj.id" +
            " and pj.id in (?3)" +
            " and gc.account = ga.id" +
            " and ga.member = mb.id" +
            " and gc.updatedOn between ?1 and ?2")
    List<Object[]> getAllByUpdatedOnBetween(Date startDate, Date endDate, ArrayList<Long> projectsId);

    @Query("select gc" +
            " from GerritChange gc, GerritAccount ga, Member mb" +
            " where gc.account = ga.id" +
            " and ga.member = mb.id" +
            " and mb.id = ?1" +
            " order by gc.id desc")
    List<GerritChange> getAllByMember(Long memberId);

    @Query("select gc" +
            " from GerritChange gc, Folder fd, Project pj" +
            " where gc.folder = fd.id" +
            " and fd.project = pj.id" +
            " and pj.id = ?1" +
            " order by gc.id desc")
    List<GerritChange> getAllByProject(Long id);

    @Query("select pj.name, sum(gc.actualSize)" +
            " from GerritChange gc, Folder fd, Project pj" +
            " where gc.folder = fd.id" +
            " and fd.project = pj.id" +
            " and pj.visible = '1'" +
            " and pj.id in (?3)" +
            " and gc.status != 'Abandoned'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by pj.name")
    List<Object[]> getSumActualSizeGroupByProject(Date startDate, Date endDate, ArrayList<Long> projectsId);

    @Query("select pj.name, sum(gc.actualSize)" +
            " from GerritChange gc, Folder fd, Project pj" +
            " where gc.folder = fd.id" +
            " and fd.project = pj.id" +
            " and pj.visible = '1'" +
            " and pj.id in (?3)" +
            " and gc.status = 'Merged'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by pj.name")
    List<Object[]> getSumActualSizeGroupByProjectAndMerged(Date startDate, Date endDate, ArrayList<Long> projectsId);

    @Query("select pj.name, sum(gc.actualSize)" +
            " from GerritChange gc, Folder fd, Project pj" +
            " where gc.folder = fd.id" +
            " and fd.project = pj.id" +
            " and pj.visible = '1'" +
            " and pj.id in (?3)" +
            " and gc.status != 'Merged'" +
            " and gc.status != 'Abandoned'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by pj.name")
    List<Object[]> getSumActualSizeGroupByProjectAndOpen(Date startDate, Date endDate, ArrayList<Long> projectsId);

    @Query("select mb.name, sum(gc.actualSize)" +
            " from GerritChange gc, GerritAccount ga, Member mb, Folder fd, Project pj" +
            " where gc.account = ga.id" +
            " and gc.folder = fd.id" +
            " and fd.project = pj.id" +
            " and ga.member = mb.id" +
            " and mb.visible = '1'" +
            " and pj.id in (?3)" +
            " and gc.status != 'Abandoned'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by mb.name")
    List<Object[]> getSumActualSizeGroupByMember(Date startDate, Date endDate, ArrayList<Long> projectsId);

    @Query("select mb.name, sum(gc.actualSize)" +
            " from GerritChange gc, GerritAccount ga, Member mb, Folder fd, Project pj" +
            " where gc.account = ga.id" +
            " and gc.folder = fd.id" +
            " and fd.project = pj.id" +
            " and ga.member = mb.id" +
            " and mb.visible = '1'" +
            " and pj.id in (?3)" +
            " and gc.status = 'Merged'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by mb.name")
    List<Object[]> getSumActualSizeGroupByMemberAndMerged(Date startDate, Date endDate, ArrayList<Long> projectsId);

    @Query("select mb.name, sum(gc.actualSize)" +
            " from GerritChange gc, GerritAccount ga, Member mb, Folder fd, Project pj" +
            " where gc.account = ga.id" +
            " and gc.folder = fd.id" +
            " and fd.project = pj.id" +
            " and ga.member = mb.id" +
            " and mb.visible = '1'" +
            " and pj.id in (?3)" +
            " and gc.status != 'Merged'" +
            " and gc.status != 'Abandoned'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by mb.name")
    List<Object[]> getSumActualSizeGroupByMemberAndOpen(Date startDate, Date endDate, ArrayList<Long> projectsId);

    @Query("select sum(gc.actualSize)" +
            " from GerritChange gc, Folder fd, Project pj" +
            " where gc.folder = fd.id" +
            " and fd.project = pj.id" +
            " and gc.status = 'Merged'" +
            " and pj.id in (?3)" +
            " and gc.updatedOn between ?1 and ?2")
    Integer getSumActualSizeByStatusIsMerged(Date startDate, Date endDate, ArrayList<Long> projectsId);

    @Query("select sum(gc.actualSize)" +
            " from GerritChange gc , Folder fd, Project pj" +
            " where gc.folder = fd.id" +
            " and fd.project = pj.id" +
            " and (gc.status = '' or gc.status = 'Merge Conflict')" +
            " and pj.id in (?3)" +
            " and gc.updatedOn between ?1 and ?2")
    Integer getSumActualSizeByStatusIsOpen(Date startDate, Date endDate, ArrayList<Long> projectsId);

    @Query("select gc.updatedOn,sum(gc.actualSize)" +
            " from GerritChange gc, Folder fd, Project pj" +
            " where gc.folder = fd.id" +
            " and fd.project = pj.id" +
            " and pj.id in (?3)" +
            " and gc.status != 'Abandoned'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by gc.updatedOn")
    List<Object[]> getSumActualSizeGroupByUpdatedOn(Date startDate, Date endDate, ArrayList<Long> projectsId);

}
