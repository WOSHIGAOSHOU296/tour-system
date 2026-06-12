package com.tour.dao;

import com.tour.model.Trip;
import com.tour.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 行程数据访问层
 */
public class TripDao {

    /**
     * 创建行程，返回自增ID
     */
    public Long insert(Trip trip) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO trips (user_id, trip_name, start_date, end_date, trip_content) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, trip.getUserId());
            stmt.setString(2, trip.getTripName());
            stmt.setDate(3, trip.getStartDate() != null ? new java.sql.Date(trip.getStartDate().getTime()) : null);
            stmt.setDate(4, trip.getEndDate() != null ? new java.sql.Date(trip.getEndDate().getTime()) : null);
            stmt.setString(5, trip.getTripContent());
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
     * 根据ID查询行程
     */
    public Trip findById(Long tripId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT t.*, u.nickname, u.username FROM trips t " +
                         "LEFT JOIN users u ON t.user_id = u.user_id WHERE t.trip_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, tripId);
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
     * 查询某用户的所有行程（按创建时间倒序）
     */
    public List<Trip> findByUserId(Long userId, int page, int pageSize) {
        List<Trip> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM trips WHERE user_id = ? ORDER BY created_at DESC LIMIT ?, ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            stmt.setInt(2, (page - 1) * pageSize);
            stmt.setInt(3, pageSize);
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
     * 统计用户行程数
     */
    public int countByUserId(Long userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM trips WHERE user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
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
     * 更新行程
     */
    public boolean update(Trip trip) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE trips SET trip_name=?, start_date=?, end_date=?, trip_content=? WHERE trip_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, trip.getTripName());
            stmt.setDate(2, trip.getStartDate() != null ? new java.sql.Date(trip.getStartDate().getTime()) : null);
            stmt.setDate(3, trip.getEndDate() != null ? new java.sql.Date(trip.getEndDate().getTime()) : null);
            stmt.setString(4, trip.getTripContent());
            stmt.setLong(5, trip.getTripId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    /**
     * 删除行程
     */
    public boolean delete(Long tripId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM trips WHERE trip_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, tripId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    private Trip map(ResultSet rs) throws SQLException {
        Trip t = new Trip();
        t.setTripId(rs.getLong("trip_id"));
        t.setUserId(rs.getLong("user_id"));
        t.setTripName(rs.getString("trip_name"));
        t.setStartDate(rs.getDate("start_date"));
        t.setEndDate(rs.getDate("end_date"));
        t.setTripContent(rs.getString("trip_content"));
        t.setCreatedAt(rs.getTimestamp("created_at"));
        return t;
    }
}
