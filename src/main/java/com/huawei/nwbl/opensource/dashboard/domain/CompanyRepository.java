package com.huawei.nwbl.opensource.dashboard.domain;

/**
 * Created by root on 14/11/16.
 */
public interface CompanyRepository {

    Company findByName(String name);

    Company findByEmailDomain(String emailDomain);
}
