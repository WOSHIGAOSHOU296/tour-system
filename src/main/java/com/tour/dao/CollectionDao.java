package com.tour.dao;

import com.tour.model.Collection;
import com.tour.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏数据访问层
 */
public class CollectionDao {

    /**
     * 添加收藏
     */
    public boolean insert(Collection c) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            // 防重复
            String checkSql = "SELECT COUNT(*) FROM collections WHERE user_id=? AND collect_type=? AND target_id=?";
            stmt = conn.prepareStatement(checkSql);
            stmt.setLong(1, c.getUserId());
            stmt.setInt(2, c.getCollectType());
            stmt.setLong(3, c.getTargetId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                rs.close();
                stmt.close();
                return false; // 已收藏
            }
            rs.close();
            stmt.close();

            String sql = "INSERT INTO collections (user_id, collect_type, target_id) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, c.getUserId());
            stmt.setInt(2, c.getCollectType());
            stmt.setLong(3, c.getTargetId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    /**
     * 取消收藏
     */
    public boolean delete(Long userId, Integer collectType, Long targetId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM collections WHERE user_id=? AND collect_type=? AND target_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            stmt.setInt(2, collectType);
            stmt.setLong(3, targetId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    /**
     * 查询用户是否已收藏某资源
     */
    public boolean isCollected(Long userId, Integer collectType, Long targetId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM collections WHERE user_id=? AND collect_type=? AND target_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            stmt.setInt(2, collectType);
            stmt.setLong(3, targetId);
            rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return false;
    }

    /**
     * 查询用户收藏列表（分页，按类型筛选）
     */
    public List<Collection> findByUser(Long userId, Integer collectType, int page, int pageSize) {
        List<Collection> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM collections WHERE user_id=?");
            if (collectType != null && collectType > 0) {
                sql.append(" AND collect_type=?");
            }
            sql.append(" ORDER BY created_at DESC LIMIT ?, ?");

            stmt = conn.prepareStatement(sql.toString());
            int idx = 1;
            stmt.setLong(idx++, userId);
            if (collectType != null && collectType > 0) {
                stmt.setInt(idx++, collectType);
            }
            stmt.setInt(idx++, (page - 1) * pageSize);
            stmt.setInt(idx++, pageSize);
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

    public int countByUser(Long userId, Integer collectType) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM collections WHERE user_id=?");
            if (collectType != null && collectType > 0) {
                sql.append(" AND collect_type=?");
            }
            stmt = conn.prepareStatement(sql.toString());
            stmt.setLong(1, userId);
            if (collectType != null && collectType > 0) {
                stmt.setInt(2, collectType);
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

    private Collection map(ResultSet rs) throws SQLException {
        Collection c = new Collection();
        c.setCollectId(rs.getLong("collect_id"));
        c.setUserId(rs.getLong("user_id"));
        c.setCollectType(rs.getInt("collect_type"));
        c.setTargetId(rs.getLong("target_id"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }
}
