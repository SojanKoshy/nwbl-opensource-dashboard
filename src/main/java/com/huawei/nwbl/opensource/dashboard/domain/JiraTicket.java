package com.huawei.nwbl.opensource.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * Created by sojan on 18/11/16.
 */
@Entity
@Table
public class JiraTicket {

    @Id
    private Long id;

    private String name;
    private String type;
    private String status;
    private String project;
    private String severity;
    private String resolution;
    private String summary;
    private Date createdOn;
    private Date updatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId")
    private JiraAccount creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporterId")
    private JiraAccount reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigneeId")
    private JiraAccount assignee;;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public JiraAccount getCreator() {
        return creator;
    }

    public void setCreator(JiraAccount creator) {
        this.creator = creator;
    }

    public JiraAccount getReporter() {
        return reporter;
    }

    public void setReporter(JiraAccount reporter) {
        this.reporter = reporter;
    }

    public JiraAccount getAssignee() {
        return assignee;
    }

    public void setAssignee(JiraAccount assignee) {
        this.assignee = assignee;
    }

}
