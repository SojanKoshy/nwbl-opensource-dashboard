package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by root on 14/11/16.
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findByName(String name);

    List<Company> findByOrderByName();

    @Query("select distinct co" +
            " from Company co, GerritAccount ga" +
            " where ga.company = co.id" +
            " order by co.name")
    List<Company> getDistinctHasAccountsOrderByName();

    @Query("select co" +
            " from Company co, CompanyEmailDomain ed" +
            " where ed.company = co.id" +
            " and ed.domain = ?1")
    Company getByEmailDomain(String emailDomain);
}
