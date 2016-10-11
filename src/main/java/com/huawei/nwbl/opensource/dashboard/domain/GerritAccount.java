package com.huawei.nwbl.opensource.dashboard.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.List;

/**
 * DB entity for gerrit_account table
 */
@Entity
@Table
public class GerritAccount {
    @Id
    private Long id;
    private String name;
    private String email;
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
    private List<GerritChange> gerritChanges;

    private Calendar lastUpdatedOn;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        if (member == null) {
            for (GerritChange gerritChange : gerritChanges) {
                gerritChange.setAccount(null);
            }
        }
        this.member = member;
    }

    public Calendar getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Calendar lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public List<GerritChange> getGerritChanges() {
        return gerritChanges;
    }

    public void setGerritChanges(List<GerritChange> gerritChanges) {
        this.gerritChanges = gerritChanges;
    }

    public String toString() {
        return String.format("%s \"%s &lt;%s&gt;\" - %s (%d found)", id, name, email,
                (username != null ? username : ""), getGerritChanges().size());
    }

}
