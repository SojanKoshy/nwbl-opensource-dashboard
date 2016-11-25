package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by root1 on 23/11/16.
 */
public interface GerritReviewRepository extends JpaRepository<GerritReview, Long> {

    GerritReview findByReviewId(String reviewId);

    @Query("select op.name, sum(gr.commentCount)" +
            " from GerritChange gc, GerritAccount ga, OnosMember om, OnosProject op, GerritReview gr" +
            " where gc.account = ga.id" +
            " and ga.onosMember = om.id" +
            " and gc.onosProject = op.id" +
            " and gr.gerritChange = gc.id" +
            " and op.id in (?3)" +
            " and om.id in (?4)" +
            " and gc.status = 'Merged'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by op.name")
    List<Object[]> getSumCommentsGroupByProjectAndMerged(Date startDate, Date endDate, ArrayList<Long> projectsId,
                                                         ArrayList<Long> accountsId);


    @Query("select op.name, sum(gr.commentCount)" +
            " from GerritChange gc, GerritAccount ga, OnosMember om, OnosProject op, GerritReview gr" +
            " where gc.account = ga.id" +
            " and ga.onosMember = om.id" +
            " and gc.onosProject = op.id" +
            " and gr.gerritChange = gc.id" +
            " and op.id in (?3)" +
            " and om.id in (?4)" +
            " and gc.status != 'Merged'" +
            " and gc.status != 'Abandoned'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by op.name")

    List<Object[]> getSumCommentsSizeGroupByProjectAndOpen(Date startDate, Date endDate, ArrayList<Long> projectsId, ArrayList<Long> accountsId);

    @Query("select om.name, sum(gr.commentCount)" +
            " from GerritChange gc, GerritAccount ga, OnosMember om, OnosProject op, GerritReview gr" +
            " where gr.gerritAccount = ga.id" +
            " and ga.onosMember = om.id" +
            " and gr.gerritChange = gc.id" +
            " and gc.onosProject = op.id" +
            " and op.id in (?3)" +
            " and om.id in (?4)" +
            " and gc.status = 'Merged'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by om.name")
    List<Object[]> getSumCommentsGroupByMemberAndMerged(Date startDate, Date endDate, ArrayList<Long> projectsId,  ArrayList<Long> accountsId);

    @Query("select om.name, sum(gr.commentCount)" +
            " from GerritChange gc, GerritAccount ga, OnosMember om, OnosProject op, GerritReview gr" +
            " where gr.gerritAccount = ga.id" +
            " and ga.onosMember = om.id" +
            " and gr.gerritChange = gc.id" +
            " and gc.onosProject = op.id" +
            " and op.id in (?3)" +
            " and om.id in (?4)" +
            " and gc.status != 'Merged'" +
            " and gc.status != 'Abandoned'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by om.name")
    List<Object[]> getSumActualSizeGroupByMemberAndOpen(Date startDate, Date endDate, ArrayList<Long> projectsId,  ArrayList<Long> accountsId);

    @Query("select co.name,sum(gr.commentCount) as total" +
            " from GerritChange gc,  GerritAccount ga, OnosMember om, OnosProject op, Company co, GerritReview gr" +
            " where gr.gerritAccount = ga.id" +
            " and ga.onosMember = om.id" +
            " and gr.gerritChange = gc.id" +
            " and gc.onosProject = op.id" +
            " and op.id in (?3)" +
            " and om.id in (?4)" +
            " and ga.company = co.id" +
            " and gc.status != 'Abandoned'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by co.name  order by total" )
    List<Object[]> getSumCommentsGroupByCompany(Date startDate, Date endDate, ArrayList<Long> projectsId, ArrayList<Long> accountsId);

    @Query("select gc.updatedOn,sum(gr.commentCount)" +
            " from GerritChange gc,  GerritAccount ga, OnosMember om, OnosProject op, GerritReview gr" +
            " where gc.account = ga.id" +
            " and gr.gerritChange = gc.id" +
            " and ga.onosMember = om.id" +
            " and gc.onosProject = op.id" +
            " and op.id in (?3)" +
            " and om.id in (?4)" +
            " and gc.status != 'Abandoned'" +
            " and gc.updatedOn between ?1 and ?2" +
            " group by gc.updatedOn")
    List<Object[]> getSumCommentsGroupByUpdatedOn(Date startDate, Date endDate, ArrayList<Long> projectsId, ArrayList<Long> accountsId);
}
