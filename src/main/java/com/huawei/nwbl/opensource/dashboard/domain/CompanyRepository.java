package com.huawei.nwbl.opensource.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by root on 14/11/16.
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findAllByOrderByName();

    Company findByName(String name);

    Company findByEmailDomain(String emailDomain);
}
