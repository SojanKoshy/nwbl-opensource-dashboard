package com.huawei.nwbl.opensource.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    //private GerritChange gerritChange;
    private Long ChangeId;

    public Long getChangeId() {
        return ChangeId;
    }

    public void setChangeId(Long changeId) {
        ChangeId = changeId;
    }

    private String reviewId;
    //private GerritAccount gerritAccount;
    private int commentCount;

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
}
