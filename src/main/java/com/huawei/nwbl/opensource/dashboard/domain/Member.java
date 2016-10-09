/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.huawei.nwbl.opensource.dashboard.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.Set;

/**
 * Member entity in member table
 */
@Entity
@Table
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Name is required.")
    @Column(unique = true)
    private String name;

    @NotEmpty(message = "Valid account id is required.")
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "member")
    private Set<GerritAccount> accounts;

    @ManyToMany(mappedBy = "members")
    private Set<Project> projects;

    private boolean visible = true;

    private Calendar created = Calendar.getInstance();

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

    public Set<GerritAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<GerritAccount> accounts) {
        this.accounts = accounts;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public String toString() {
        return String.format("%s", getName());
    }

    @PreRemove
    public void removeMemberFromOthers() {
        for (Project project : projects) {
            project.getMembers().remove(this);
        }
        for (GerritAccount account : accounts) {
            account.setMember(null);
        }
    }
}
