package com.tour.service;

import com.tour.dao.ScenicDao;
import com.tour.dao.TripDao;
import com.tour.model.Scenic;
import com.tour.model.Trip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行程业务逻辑层
 */
public class TripService {

    private final TripDao tripDao = new TripDao();
    private final ScenicDao scenicDao = new ScenicDao();

    /**
     * 创建行程
     */
    public Long create(Long userId, String tripName, String startDateStr, String endDateStr, String tripContent) {
        if (tripName == null || tripName.trim().isEmpty()) return null;
        if (startDateStr == null || endDateStr == null) return null;

        Trip trip = new Trip();
        trip.setUserId(userId);
        trip.setTripName(tripName.trim());
        trip.setTripContent(tripContent != null ? tripContent.trim() : "");

        try {
            trip.setStartDate(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(startDateStr));
            trip.setEndDate(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(endDateStr));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return tripDao.insert(trip);
    }

    /**
     * 获取用户行程列表（分页）
     */
    public Map<String, Object> listByUser(Long userId, int page, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        List<Trip> list = tripDao.findByUserId(userId, page, pageSize);
        int total = tripDao.countByUserId(userId);
        int totalPages = (int) Math.ceil((double) total / pageSize);

        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("totalPages", totalPages);
        return result;
    }

    /**
     * 获取行程详情
     */
    public Trip getDetail(Long tripId) {
        return tripDao.findById(tripId);
    }

    /**
     * 更新行程
     */
    public boolean update(Long tripId, String tripName, String startDateStr, String endDateStr, String tripContent) {
        Trip trip = new Trip();
        trip.setTripId(tripId);
        trip.setTripName(tripName);
        trip.setTripContent(tripContent != null ? tripContent : "");

        try {
            trip.setStartDate(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(startDateStr));
            trip.setEndDate(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(endDateStr));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return tripDao.update(trip);
    }

    /**
     * 删除行程
     */
    public boolean delete(Long tripId) {
        return tripDao.delete(tripId);
    }

    /**
     * 获取所有景点（创建行程时参考）
     */
    public List<Scenic> getAllScenics() {
        return scenicDao.findAll(null, null, 1, 1000);
    }
}
