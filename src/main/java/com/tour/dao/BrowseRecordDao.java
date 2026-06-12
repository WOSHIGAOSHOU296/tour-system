package com.tour.dao;

import com.tour.model.BrowseRecord;
import com.tour.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 浏览记录数据访问层
 */
public class BrowseRecordDao {

    /**
     * 记录浏览
     */
    public boolean insert(BrowseRecord br) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO browse_records (user_id, browse_type, target_id, browse_time) VALUES (?, ?, ?, NOW())";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, br.getUserId());
            stmt.setInt(2, br.getBrowseType());
            stmt.setLong(3, br.getTargetId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    /**
     * 查询用户浏览历史（分页，带目标名称）
     */
    public List<BrowseRecord> findByUser(Long userId, int page, int pageSize) {
        List<BrowseRecord> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT b.*, " +
                         "  CASE b.browse_type " +
                         "    WHEN 1 THEN a.scenic_name " +
                         "    WHEN 2 THEN s.title " +
                         "    WHEN 3 THEN f.title " +
                         "    ELSE '未知' END AS target_name " +
                         "FROM browse_records b " +
                         "LEFT JOIN attractions a ON b.browse_type=1 AND b.target_id=a.scenic_id " +
                         "LEFT JOIN strategies s ON b.browse_type=2 AND b.target_id=s.strategy_id " +
                         "LEFT JOIN forum_posts f ON b.browse_type=3 AND b.target_id=f.post_id " +
                         "WHERE b.user_id=? ORDER BY b.browse_time DESC LIMIT ?, ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            stmt.setInt(2, (page - 1) * pageSize);
            stmt.setInt(3, pageSize);
            rs = stmt.executeQuery();
            while (rs.next()) {
                BrowseRecord br = new BrowseRecord();
                br.setRecordId(rs.getLong("record_id"));
                br.setUserId(rs.getLong("user_id"));
                br.setBrowseType(rs.getInt("browse_type"));
                br.setTargetId(rs.getLong("target_id"));
                br.setBrowseTime(rs.getTimestamp("browse_time"));
                br.setTargetName(rs.getString("target_name"));
                list.add(br);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return list;
    }

    public int countByUser(Long userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM browse_records WHERE user_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return 0;
    }
}
