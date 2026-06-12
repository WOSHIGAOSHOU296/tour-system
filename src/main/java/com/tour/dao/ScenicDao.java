package com.tour.dao;

import com.tour.model.Food;
import com.tour.model.Hotel;
import com.tour.model.Scenic;
import com.tour.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 景点数据访问层
 */
public class ScenicDao {

    /**
     * 根据ID查询景点详情（含浏览量+1）
     */
    public Scenic findById(Long scenicId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            // 浏览量+1
            String updateSql = "UPDATE attractions SET view_count = view_count + 1 WHERE scenic_id = ?";
            stmt = conn.prepareStatement(updateSql);
            stmt.setLong(1, scenicId);
            stmt.executeUpdate();
            stmt.close();

            // 连表查询城市名
            String sql = "SELECT a.*, c.city_name FROM attractions a " +
                         "LEFT JOIN cities c ON a.city_id = c.city_id " +
                         "WHERE a.scenic_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, scenicId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapScenic(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return null;
    }

    /**
     * 分页查询景点（支持城市筛选和关键词搜索）
     */
    public List<Scenic> findAll(Long cityId, String keyword, int page, int pageSize) {
        List<Scenic> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            StringBuilder sql = new StringBuilder(
                "SELECT a.*, c.city_name FROM attractions a " +
                "LEFT JOIN cities c ON a.city_id = c.city_id WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (cityId != null && cityId > 0) {
                sql.append(" AND a.city_id = ?");
                params.add(cityId);
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND (a.scenic_name LIKE ? OR a.introduce LIKE ?)");
                String kw = "%" + keyword.trim() + "%";
                params.add(kw);
                params.add(kw);
            }

            sql.append(" ORDER BY a.view_count DESC LIMIT ?, ?");
            params.add((page - 1) * pageSize);
            params.add(pageSize);

            stmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof Long) {
                    stmt.setLong(i + 1, (Long) params.get(i));
                } else if (params.get(i) instanceof String) {
                    stmt.setString(i + 1, (String) params.get(i));
                } else if (params.get(i) instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) params.get(i));
                }
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapScenic(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * 统计景点总数（支持筛选）
     */
    public int countAll(Long cityId, String keyword) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM attractions WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (cityId != null && cityId > 0) {
                sql.append(" AND city_id = ?");
                params.add(cityId);
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND (scenic_name LIKE ? OR introduce LIKE ?)");
                String kw = "%" + keyword.trim() + "%";
                params.add(kw);
                params.add(kw);
            }

            stmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof Long) {
                    stmt.setLong(i + 1, (Long) params.get(i));
                } else {
                    stmt.setString(i + 1, (String) params.get(i));
                }
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
     * 查询浏览量最高的 Top N 景点（数据可视化用）
     */
    public List<Scenic> findTopByViewCount(int topN) {
        List<Scenic> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, c.city_name FROM attractions a " +
                         "LEFT JOIN cities c ON a.city_id = c.city_id " +
                         "ORDER BY a.view_count DESC LIMIT ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, topN);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapScenic(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * 查询某景点周边的美食
     */
    public List<Food> findFoodsByScenicId(Long scenicId) {
        List<Food> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM foods WHERE scenic_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, scenicId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Food f = new Food();
                f.setFoodId(rs.getLong("food_id"));
                f.setScenicId(rs.getLong("scenic_id"));
                f.setFoodName(rs.getString("food_name"));
                f.setAddress(rs.getString("address"));
                f.setIntroduce(rs.getString("introduce"));
                f.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * 查询某景点周边的酒店
     */
    public List<Hotel> findHotelsByScenicId(Long scenicId) {
        List<Hotel> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM hotels WHERE scenic_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, scenicId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Hotel h = new Hotel();
                h.setHotelId(rs.getLong("hotel_id"));
                h.setScenicId(rs.getLong("scenic_id"));
                h.setHotelName(rs.getString("hotel_name"));
                h.setAddress(rs.getString("address"));
                h.setPriceRange(rs.getString("price_range"));
                h.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return list;
    }

    private Scenic mapScenic(ResultSet rs) throws SQLException {
        Scenic s = new Scenic();
        s.setScenicId(rs.getLong("scenic_id"));
        s.setCityId(rs.getLong("city_id"));
        s.setScenicName(rs.getString("scenic_name"));
        s.setScenicType(rs.getString("scenic_type"));
        s.setAddress(rs.getString("address"));
        s.setTicketPrice(rs.getBigDecimal("ticket_price"));
        s.setOpenTime(rs.getString("open_time"));
        s.setIntroduce(rs.getString("introduce"));
        s.setImgUrl(rs.getString("img_url"));
        s.setAverageScore(rs.getBigDecimal("average_score"));
        s.setViewCount(rs.getInt("view_count"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        s.setUpdatedAt(rs.getTimestamp("updated_at"));
        try { s.setCityName(rs.getString("city_name")); } catch (SQLException ignored) {}
        return s;
    }
}
