package com.tour.model;

import java.util.Date;

public class Trip {
    private Long tripId;
    private Long userId;
    private String tripName;
    private Date startDate;
    private Date endDate;
    private String tripContent;
    private Date createdAt;

    public Trip() {}

    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTripName() { return tripName; }
    public void setTripName(String tripName) { this.tripName = tripName; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getTripContent() { return tripContent; }
    public void setTripContent(String tripContent) { this.tripContent = tripContent; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
