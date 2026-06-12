package com.tour.dao;

import com.tour.model.Announcement;
import com.tour.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDao {

    public boolean insert(Announcement a) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO announcements (scenic_id, user_id, title, content) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, a.getScenicId());
            stmt.setLong(2, a.getUserId());
            stmt.setString(3, a.getTitle());
            stmt.setString(4, a.getContent());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    public List<Announcement> findByScenicId(Long scenicId) {
        List<Announcement> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, u.nickname, u.username, s.scenic_name FROM announcements a " +
                         "LEFT JOIN users u ON a.user_id = u.user_id " +
                         "LEFT JOIN attractions s ON a.scenic_id = s.scenic_id " +
                         "WHERE a.scenic_id = ? ORDER BY a.created_at DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, scenicId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return list;
    }

    public boolean delete(Long annId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM announcements WHERE ann_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, annId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    private Announcement map(ResultSet rs) throws SQLException {
        Announcement a = new Announcement();
        a.setAnnId(rs.getLong("ann_id"));
        a.setScenicId(rs.getLong("scenic_id"));
        a.setUserId(rs.getLong("user_id"));
        a.setTitle(rs.getString("title"));
        a.setContent(rs.getString("content"));
        a.setCreatedAt(rs.getTimestamp("created_at"));
        try { a.setUsername(rs.getString("nickname") != null ? rs.getString("nickname") : rs.getString("username")); } catch (SQLException ignored) {}
        try { a.setScenicName(rs.getString("scenic_name")); } catch (SQLException ignored) {}
        return a;
    }
}
