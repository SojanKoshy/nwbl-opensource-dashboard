package com.huawei.nwbl.opensource.dashboard.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by root on 14/11/16.
 */
@Entity
@Table
public class Company {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String emailDomain;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    List<GerritAccount> gerritAccounts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }

    public List<GerritAccount> getGerritAccounts() {
        return gerritAccounts;
    }

    public void setGerritAccounts(List<GerritAccount> gerritAccounts) {
        this.gerritAccounts = gerritAccounts;
    }

    public String toString() {
        return String.format("%s", getName());
    }

}