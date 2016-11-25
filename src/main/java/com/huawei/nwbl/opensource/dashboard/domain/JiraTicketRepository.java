package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sojan on 19/11/16.
 */
public interface JiraTicketRepository extends JpaRepository<JiraTicket, Long> {



    @Query("select jt.project, count(jt)" +
            " from JiraTicket jt, OnosMember om, JiraAccount ja" +
            " where jt.creator = ja.id" +
            " and ja.onosMember = om.id" +
            " and om.id in (?3)" +
            " and jt.type = 'Bug'" +
            " and (jt.status = 'Closed' or jt.status = 'Resolved')" +
            " and jt.updatedOn between ?1 and ?2" +
            " group by jt.project")
    List<Object[]> getSumDefectGroupByProjectAndClosed(Date startDate, Date endDate, ArrayList<Long> accountsId);

    @Query("select jt.project, count(jt)" +
            " from JiraTicket jt, OnosMember om, JiraAccount ja" +
            " where jt.creator = ja.id" +
            " and ja.onosMember = om.id" +
            " and om.id in (?3)" +
            " and jt.type = 'Bug'" +
            " and jt.status != 'Closed' and jt.status != 'Resolved'" +
            " and jt.updatedOn between ?1 and ?2" +
            " group by jt.project")
    List<Object[]> getSumDefectGroupByProjectAndOpen(Date startDate, Date endDate, ArrayList<Long> accountsId);

    @Query("select om.name, count(jt)" +
            " from JiraTicket jt, OnosMember om, JiraAccount ja" +
            " where jt.creator = ja.id" +
            " and ja.onosMember = om.id" +
            " and om.id in (?3)" +
            " and jt.type = 'Bug'" +
            " and (jt.status = 'Closed' or jt.status = 'Resolved')" +
            " and jt.updatedOn between ?1 and ?2" +
            " group by om.name")
    List<Object[]> getSumDefectsGroupByMemberAndClosed(Date startDate, Date endDate, ArrayList<Long> accountsId);

    @Query("select om.name, count(jt)" +
            " from JiraTicket jt, OnosMember om, JiraAccount ja" +
            " where jt.creator = ja.id" +
            " and ja.onosMember = om.id" +
            " and om.id in (?3)" +
            " and jt.type = 'Bug'" +
            " and jt.status != 'Closed' and jt.status != 'Resolved'" +
            " and jt.updatedOn between ?1 and ?2" +
            " group by om.name")
    List<Object[]> getSumDefectsGroupByMemberAndOpen(Date startDate, Date endDate, ArrayList<Long> accountsId);

    @Query("select co.name, count(jt)" +
            " from JiraTicket jt, OnosMember om, Company co, JiraAccount ja" +
            " where jt.creator = ja.id" +
            " and ja.onosMember = om.id" +
            " and om.id in (?3)" +
            " and om.company = co.id" +
            " and jt.type = 'Bug'" +
            " and jt.updatedOn between ?1 and ?2" +
            " group by co.name")
    List<Object[]> getSumDefectGroupByCompany(Date startDate, Date endDate, ArrayList<Long> accountsId);

    @Query("select jt.updatedOn, count(jt)" +
            " from JiraTicket jt, OnosMember om, JiraAccount ja" +
            " where jt.creator = ja.id" +
            " and ja.onosMember = om.id" +
            " and om.id in (?3)" +
            " and jt.type = 'Bug'" +
            " and jt.updatedOn between ?1 and ?2" +
            " group by jt.updatedOn")
    List<Object[]> getSumDefectGroupByUpdatedOn(Date startDate, Date endDate, ArrayList<Long> accountsId);
}
