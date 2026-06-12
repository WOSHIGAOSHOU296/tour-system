package com.tour.dao;

import com.tour.model.Strategy;
import com.tour.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 旅友圈攻略数据访问层
 */
public class StrategyDao {

    /**
     * 发布攻略
     */
    public Long insert(Strategy s) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO strategies (scenic_id, user_id, title, content) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, s.getScenicId());
            stmt.setLong(2, s.getUserId());
            stmt.setString(3, s.getTitle());
            stmt.setString(4, s.getContent());
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return null;
    }

    /**
     * 根据ID查询攻略
     */
    public Strategy findById(Long strategyId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT s.*, u.nickname, u.username, a.scenic_name FROM strategies s " +
                         "LEFT JOIN users u ON s.user_id = u.user_id " +
                         "LEFT JOIN attractions a ON s.scenic_id = a.scenic_id " +
                         "WHERE s.strategy_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, strategyId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return null;
    }

    /**
     * 分页查询攻略列表（可按景点筛选）
     */
    public List<Strategy> findAll(Long scenicId, int page, int pageSize) {
        List<Strategy> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            StringBuilder sql = new StringBuilder(
                "SELECT s.*, u.nickname, u.username, a.scenic_name FROM strategies s " +
                "LEFT JOIN users u ON s.user_id = u.user_id " +
                "LEFT JOIN attractions a ON s.scenic_id = a.scenic_id WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (scenicId != null && scenicId > 0) {
                sql.append(" AND s.scenic_id = ?");
                params.add(scenicId);
            }

            sql.append(" ORDER BY s.created_at DESC LIMIT ?, ?");
            params.add((page - 1) * pageSize);
            params.add(pageSize);

            stmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof Long) {
                    stmt.setLong(i + 1, (Long) params.get(i));
                } else if (params.get(i) instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) params.get(i));
                }
            }
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

    /**
     * 统计攻略总数
     */
    public int countAll(Long scenicId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM strategies WHERE 1=1");
            if (scenicId != null && scenicId > 0) {
                sql.append(" AND scenic_id = ?");
            }
            stmt = conn.prepareStatement(sql.toString());
            if (scenicId != null && scenicId > 0) {
                stmt.setLong(1, scenicId);
            }
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return 0;
    }

    /**
     * 删除攻略
     */
    public boolean delete(Long strategyId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM strategies WHERE strategy_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, strategyId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    private Strategy map(ResultSet rs) throws SQLException {
        Strategy s = new Strategy();
        s.setStrategyId(rs.getLong("strategy_id"));
        s.setScenicId(rs.getLong("scenic_id"));
        s.setUserId(rs.getLong("user_id"));
        s.setTitle(rs.getString("title"));
        s.setContent(rs.getString("content"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        try { s.setUsername(rs.getString("nickname") != null ? rs.getString("nickname") : rs.getString("username")); } catch (SQLException ignored) {}
        try { s.setScenicName(rs.getString("scenic_name")); } catch (SQLException ignored) {}
        return s;
    }
}
