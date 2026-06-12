package com.tour.model;

import java.util.Date;

public class BrowseRecord {
    private Long recordId;
    private Long userId;
    private Integer browseType;
    private Long targetId;
    private Date browseTime;

    public BrowseRecord() {}

    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getBrowseType() { return browseType; }
    public void setBrowseType(Integer browseType) { this.browseType = browseType; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public Date getBrowseTime() { return browseTime; }
    public void setBrowseTime(Date browseTime) { this.browseTime = browseTime; }
}
