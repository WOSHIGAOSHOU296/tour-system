package com.tour.model;

import java.util.Date;

/**
 * 周围美食实体类
 */
public class Food {
    private Long foodId;
    private Long scenicId;
    private String foodName;
    private String address;
    private String introduce;
    private Date createdAt;

    public Food() {}

    public Long getFoodId() { return foodId; }
    public void setFoodId(Long foodId) { this.foodId = foodId; }
    public Long getScenicId() { return scenicId; }
    public void setScenicId(Long scenicId) { this.scenicId = scenicId; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getIntroduce() { return introduce; }
    public void setIntroduce(String introduce) { this.introduce = introduce; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
