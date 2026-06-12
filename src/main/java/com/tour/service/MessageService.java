package com.tour.service;

import com.tour.dao.MessageDao;
import com.tour.model.Message;

import java.util.List;

/**
 * 景点留言业务逻辑层
 */
public class MessageService {

    private final MessageDao messageDao = new MessageDao();

    /**
     * 发表留言
     */
    public boolean addMessage(Long scenicId, Long userId, String content) {
        if (content == null || content.trim().isEmpty()) return false;
        Message msg = new Message();
        msg.setScenicId(scenicId);
        msg.setUserId(userId);
        msg.setContent(content.trim());
        return messageDao.insert(msg);
    }

    /**
     * 获取景点留言列表
     */
    public List<Message> getMessages(Long scenicId) {
        return messageDao.findByScenicId(scenicId);
    }

    /**
     * 删除留言（管理员）
     */
    public boolean delete(Long messageId) {
        return messageDao.delete(messageId);
    }
}
