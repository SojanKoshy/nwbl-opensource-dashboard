package com.huawei.nwbl.opensource.dashboard.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by Saravana on 14/11/16.
 */
@Entity
@Table
public class Company {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "company", orphanRemoval = true)
    private Set<CompanyEmailDomain> emailDomains;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    private List<GerritAccount> gerritAccounts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    private List<Member> members;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    private List<OnosMember> onosMembers;

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

    public Set<CompanyEmailDomain> getEmailDomains() {
        return emailDomains;
    }

    public void setEmailDomains(Set<CompanyEmailDomain> emailDomains) {
        this.emailDomains = emailDomains;
    }

    public List<GerritAccount> getGerritAccounts() {
        return gerritAccounts;
    }

    public void setGerritAccounts(List<GerritAccount> gerritAccounts) {
        this.gerritAccounts = gerritAccounts;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public List<OnosMember> getOnosMembers() {
        return onosMembers;
    }

    public void setOnosMembers(List<OnosMember> onosMembers) {
        this.onosMembers = onosMembers;
    }

    public String toString() {
        return String.format("%s", getName());
    }


}
