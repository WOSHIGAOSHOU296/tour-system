package com.tour.service;

import com.tour.dao.ScenicDao;
import com.tour.dao.StrategyDao;
import com.tour.model.Scenic;
import com.tour.model.Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 旅友圈攻略业务逻辑层
 */
public class StrategyService {

    private final StrategyDao strategyDao = new StrategyDao();
    private final ScenicDao scenicDao = new ScenicDao();

    /**
     * 发布攻略
     */
    public boolean create(Long userId, Long scenicId, String title, String content) {
        if (title == null || title.trim().isEmpty()) return false;
        if (content == null || content.trim().isEmpty()) return false;

        Strategy s = new Strategy();
        s.setUserId(userId);
        s.setScenicId(scenicId);
        s.setTitle(title.trim());
        s.setContent(content.trim());
        return strategyDao.insert(s) != null;
    }

    /**
     * 攻略列表（分页+景点筛选）
     */
    public Map<String, Object> list(Long scenicId, int page, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        List<Strategy> list = strategyDao.findAll(scenicId, page, pageSize);
        int total = strategyDao.countAll(scenicId);
        int totalPages = (int) Math.ceil((double) total / pageSize);

        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("totalPages", totalPages);
        return result;
    }

    /**
     * 攻略详情
     */
    public Strategy getDetail(Long strategyId) {
        return strategyDao.findById(strategyId);
    }

    /**
     * 删除攻略
     */
    public boolean delete(Long strategyId) {
        return strategyDao.delete(strategyId);
    }

    /**
     * 获取所有景点（发布攻略时选择）
     */
    public List<Scenic> getAllScenics() {
        return scenicDao.findAll(null, null, 1, 1000);
    }
}
