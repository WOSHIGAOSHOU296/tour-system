package com.tour.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 景点实体类
 */
public class Scenic {
    private Long scenicId;
    private Long cityId;
    private String scenicName;
    private String scenicType;
    private String address;
    private BigDecimal ticketPrice;
    private String openTime;
    private String introduce;
    private String imgUrl;
    private BigDecimal averageScore;
    private Integer viewCount;
    private Date createdAt;
    private Date updatedAt;

    // 查询辅助字段
    private String cityName;

    public Scenic() {}

    public Long getScenicId() { return scenicId; }
    public void setScenicId(Long scenicId) { this.scenicId = scenicId; }
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    public String getScenicName() { return scenicName; }
    public void setScenicName(String scenicName) { this.scenicName = scenicName; }
    public String getScenicType() { return scenicType; }
    public void setScenicType(String scenicType) { this.scenicType = scenicType; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public BigDecimal getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }
    public String getOpenTime() { return openTime; }
    public void setOpenTime(String openTime) { this.openTime = openTime; }
    public String getIntroduce() { return introduce; }
    public void setIntroduce(String introduce) { this.introduce = introduce; }
    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }
    public BigDecimal getAverageScore() { return averageScore; }
    public void setAverageScore(BigDecimal averageScore) { this.averageScore = averageScore; }
    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
}
