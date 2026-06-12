package com.tour.model;

import java.util.Date;

public class Collection {
    private Long collectId;
    private Long userId;
    private Integer collectType;
    private Long targetId;
    private Date createdAt;

    public Collection() {}

    public Long getCollectId() { return collectId; }
    public void setCollectId(Long collectId) { this.collectId = collectId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getCollectType() { return collectType; }
    public void setCollectType(Integer collectType) { this.collectType = collectType; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
