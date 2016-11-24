package com.huawei.nwbl.opensource.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by sojan on 22/11/16.
 */
@Entity
@Table
public class OnosProjectPath {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onosProjectId")
    private OnosProject onosProject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public OnosProject getOnosProject() {
        return onosProject;
    }

    public void setOnosProject(OnosProject onosProject) {
        this.onosProject = onosProject;
    }
}
