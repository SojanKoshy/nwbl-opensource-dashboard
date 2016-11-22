package com.huawei.nwbl.opensource.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * Created by saravana on 21/11/16.
 */
@Entity
@Table
public class GerritDump {
    @Id
    private Long id;

    @Size(max = 1000000)
    private String jsonDetails;

    @Size(max = 1000000)
    private String jsonFiles;

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonFiles() {
        return jsonFiles;
    }

    public void setJsonFiles(String jsonFiles) {
        this.jsonFiles = jsonFiles;
    }

    public String getJsonDetails() {
        return jsonDetails;
    }

    public void setJsonDetails(String jsonDetails) {
        this.jsonDetails = jsonDetails;
    }

  }
