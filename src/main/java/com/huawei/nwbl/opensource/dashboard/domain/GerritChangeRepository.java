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

    @Query("select op.name, sum(gc.actualSize)" +
            " from GerritChange gc, GerritAccount ga, OnosMember om, OnosProject op" +
            " where gc.account = ga.id" +
            " and ga.onosMember = om.id" +
            " and gc.onosProject = op.id" +
            " and op.id in (?3)" +
            " and om.id in (?4)" +
            " and gc.status = 'Merged'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by op.name")
    List<Object[]> getSumActualSizeGroupByProjectAndMerged(Date startDate, Date endDate, ArrayList<Long> projectsId,
    ArrayList<Long> accountsId);

    @Query("select op.name, sum(gc.actualSize)" +
            " from GerritChange gc, GerritAccount ga, OnosMember om, OnosProject op" +
            " where gc.account = ga.id" +
            " and ga.onosMember = om.id" +
            " and gc.onosProject = op.id" +
            " and op.id in (?3)" +
            " and om.id in (?4)" +
            " and gc.status != 'Merged'" +
            " and gc.status != 'Abandoned'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by op.name")
    List<Object[]> getSumActualSizeGroupByProjectAndOpen(Date startDate, Date endDate, ArrayList<Long> projectsId,
    ArrayList<Long> accountsId);

    @Query("select om.name, sum(gc.actualSize)" +
            " from GerritChange gc, GerritAccount ga, OnosMember om, OnosProject op" +
            " where gc.account = ga.id" +
            " and ga.onosMember = om.id" +
            " and gc.onosProject = op.id" +
            " and op.id in (?3)" +
            " and om.id in (?4)" +
            " and gc.status = 'Merged'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by om.name")
    List<Object[]> getSumActualSizeGroupByMemberAndMerged(Date startDate, Date endDate, ArrayList<Long> projectsId,
    ArrayList<Long> accountsId);

    @Query("select om.name, sum(gc.actualSize)" +
            " from GerritChange gc,  GerritAccount ga, OnosMember om, OnosProject op" +
            " where gc.account = ga.id" +
            " and ga.onosMember = om.id" +
            " and gc.onosProject = op.id" +
            " and op.id in (?3)" +
            " and om.id in (?4)" +
            " and gc.status != 'Merged'" +
            " and gc.status != 'Abandoned'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by om.name")
    List<Object[]> getSumActualSizeGroupByMemberAndOpen(Date startDate, Date endDate, ArrayList<Long> projectsId,
    ArrayList<Long> accountsId);

    @Query("select gc.updatedOn,sum(gc.actualSize)" +
            " from GerritChange gc,  GerritAccount ga, OnosMember om, OnosProject op" +
            " where gc.account = ga.id" +
            " and ga.onosMember = om.id" +
            " and gc.onosProject = op.id" +
            " and op.id in (?3)" +
            " and om.id in (?4)" +
            " and gc.status != 'Abandoned'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by gc.updatedOn")
    List<Object[]> getSumActualSizeGroupByUpdatedOn(Date startDate, Date endDate, ArrayList<Long> projectsId,
    ArrayList<Long> accountsId);


    @Query("select co.name,sum(gc.actualSize) as total" +
            " from GerritChange gc,  GerritAccount ga, OnosMember om, OnosProject op, Company co" +
            " where gc.account = ga.id" +
            " and ga.onosMember = om.id" +
            " and gc.onosProject = op.id" +
            " and op.id in (?3)" +
            " and om.id in (?4)" +
            " and ga.company = co.id" +
            " and gc.status != 'Abandoned'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by co.name  order by total" )
    List<Object[]> getSumActualSizeGroupByCompany(Date startDate, Date endDate, ArrayList<Long> projectsId,
                                                    ArrayList<Long> accountsId);

}
