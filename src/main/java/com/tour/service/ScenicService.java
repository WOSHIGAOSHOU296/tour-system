package com.tour.service;

import com.tour.dao.CityDao;
import com.tour.dao.ScenicDao;
import com.tour.model.City;
import com.tour.model.Food;
import com.tour.model.Hotel;
import com.tour.model.Scenic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 景点业务逻辑层
 */
public class ScenicService {

    private final ScenicDao scenicDao = new ScenicDao();
    private final CityDao cityDao = new CityDao();

    /**
     * 获取景点详情
     */
    public Scenic getDetail(Long scenicId) {
        return scenicDao.findById(scenicId);
    }

    /**
     * 获取景点周边的美食和酒店
     */
    public Map<String, Object> getSurroundings(Long scenicId) {
        Map<String, Object> result = new HashMap<>();
        List<Food> foods = scenicDao.findFoodsByScenicId(scenicId);
        List<Hotel> hotels = scenicDao.findHotelsByScenicId(scenicId);
        result.put("foods", foods);
        result.put("hotels", hotels);
        return result;
    }

    /**
     * 分页查询景点列表
     */
    public Map<String, Object> listScenics(Long cityId, String keyword, int page, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        List<Scenic> list = scenicDao.findAll(cityId, keyword, page, pageSize);
        int total = scenicDao.countAll(cityId, keyword);
        int totalPages = (int) Math.ceil((double) total / pageSize);

        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("totalPages", totalPages);
        return result;
    }

    /**
     * 获取热度 Top N 景点（首页可视化）
     */
    public List<Scenic> getHotTop(int topN) {
        return scenicDao.findTopByViewCount(topN);
    }

    /**
     * 获取所有城市（下拉筛选用）
     */
    public List<City> getAllCities() {
        return cityDao.findAll();
    }
}
