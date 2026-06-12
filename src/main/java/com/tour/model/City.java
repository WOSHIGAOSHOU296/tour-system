package com.tour.model;

import java.util.Date;

/**
 * 城市实体类
 */
public class City {
    private Long cityId;
    private String cityName;
    private String province;
    private String cityType;
    private Date createdAt;
    private Date updatedAt;

    public City() {}

    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getCityType() { return cityType; }
    public void setCityType(String cityType) { this.cityType = cityType; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
