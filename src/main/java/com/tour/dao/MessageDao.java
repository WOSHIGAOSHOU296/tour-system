package com.tour.dao;

import com.tour.model.Message;
import com.tour.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 景点留言数据访问层
 */
public class MessageDao {

    /**
     * 根据ID查留言
     */
    public Message findById(Long messageId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM messages WHERE message_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, messageId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Message msg = new Message();
                msg.setMessageId(rs.getLong("message_id"));
                msg.setScenicId(rs.getLong("scenic_id"));
                msg.setUserId(rs.getLong("user_id"));
                msg.setContent(rs.getString("content"));
                return msg;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return null;
    }

    /**
     * 发表留言
     */
    public boolean insert(Message msg) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO messages (scenic_id, user_id, content) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, msg.getScenicId());
            stmt.setLong(2, msg.getUserId());
            stmt.setString(3, msg.getContent());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    /**
     * 查询某景点的留言（带用户名，按时间倒序）
     */
    public List<Message> findByScenicId(Long scenicId) {
        List<Message> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT m.*, u.nickname, u.username FROM messages m " +
                         "LEFT JOIN users u ON m.user_id = u.user_id " +
                         "WHERE m.scenic_id = ? ORDER BY m.created_at DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, scenicId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Message msg = new Message();
                msg.setMessageId(rs.getLong("message_id"));
                msg.setScenicId(rs.getLong("scenic_id"));
                msg.setUserId(rs.getLong("user_id"));
                msg.setContent(rs.getString("content"));
                msg.setCreatedAt(rs.getTimestamp("created_at"));
                String name = rs.getString("nickname");
                msg.setUsername(name != null && !name.isEmpty() ? name : rs.getString("username"));
                list.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * 删除留言
     */
    public boolean delete(Long messageId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM messages WHERE message_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, messageId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }
}
