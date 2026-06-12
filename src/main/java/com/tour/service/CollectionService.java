package com.tour.service;

import com.tour.dao.CollectionDao;
import com.tour.model.Collection;

import java.util.List;

/**
 * 收藏业务逻辑层
 */
public class CollectionService {

    private final CollectionDao collectionDao = new CollectionDao();

    /** 收藏/取消收藏，返回操作结果描述 */
    public String toggle(Long userId, Integer collectType, Long targetId) {
        if (collectionDao.isCollected(userId, collectType, targetId)) {
            collectionDao.delete(userId, collectType, targetId);
            return "uncollected";
        } else {
            Collection c = new Collection();
            c.setUserId(userId);
            c.setCollectType(collectType);
            c.setTargetId(targetId);
            collectionDao.insert(c);
            return "collected";
        }
    }

    /** 查询是否已收藏 */
    public boolean isCollected(Long userId, Integer collectType, Long targetId) {
        return collectionDao.isCollected(userId, collectType, targetId);
    }

    /** 用户收藏列表（分页+按类型筛选） */
    public List<Collection> listByUser(Long userId, Integer collectType, int page, int pageSize) {
        return collectionDao.findByUser(userId, collectType, page, pageSize);
    }

    public int countByUser(Long userId, Integer collectType) {
        return collectionDao.countByUser(userId, collectType);
    }
}
