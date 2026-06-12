package com.tour.model;

import java.util.Date;

public class Strategy {
    private Long strategyId;
    private Long scenicId;
    private Long userId;
    private String title;
    private String content;
    private Date createdAt;

    // 辅助字段
    private String username;
    private String scenicName;

    public Strategy() {}

    public Long getStrategyId() { return strategyId; }
    public void setStrategyId(Long strategyId) { this.strategyId = strategyId; }
    public Long getScenicId() { return scenicId; }
    public void setScenicId(Long scenicId) { this.scenicId = scenicId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getScenicName() { return scenicName; }
    public void setScenicName(String scenicName) { this.scenicName = scenicName; }
}
