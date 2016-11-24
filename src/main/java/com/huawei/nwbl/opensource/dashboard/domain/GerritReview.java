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
import javax.validation.constraints.Size;

/**
 * Created by root1 on 23/11/16.
 */

@Entity
@Table
public class GerritReview {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String reviewId;
    private int commentCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changeId")
    private GerritChange gerritChange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    private GerritAccount gerritAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public GerritChange getGerritChange() {
        return gerritChange;
    }

    public void setGerritChange(GerritChange gerritChange) {
        this.gerritChange = gerritChange;
    }

    public GerritAccount getGerritAccount() {
        return gerritAccount;
    }

    public void setGerritAccount(GerritAccount gerritAccount) {
        this.gerritAccount = gerritAccount;
    }
}
