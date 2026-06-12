package com.tour.model;

import java.util.Date;

public class Announcement {
    private Long annId;
    private Long scenicId;
    private Long userId;
    private String title;
    private String content;
    private Date createdAt;

    private String scenicName;
    private String username;

    public Announcement() {}

    public Long getAnnId() { return annId; }
    public void setAnnId(Long annId) { this.annId = annId; }
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
    public String getScenicName() { return scenicName; }
    public void setScenicName(String scenicName) { this.scenicName = scenicName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
