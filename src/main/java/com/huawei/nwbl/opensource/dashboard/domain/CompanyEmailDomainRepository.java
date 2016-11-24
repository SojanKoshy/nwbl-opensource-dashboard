package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by root on 14/11/16.
 */
public interface CompanyEmailDomainRepository extends JpaRepository<CompanyEmailDomain, Long> {

    CompanyEmailDomain findByDomain(String domain);

}
