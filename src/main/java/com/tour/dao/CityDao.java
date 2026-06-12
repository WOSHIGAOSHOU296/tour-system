package com.tour.dao;

import com.tour.model.City;
import com.tour.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 城市数据访问层
 */
public class CityDao {

    /**
     * 查询所有城市
     */
    public List<City> findAll() {
        List<City> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM cities ORDER BY city_id";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapCity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * 根据ID查询城市
     */
    public City findById(Long cityId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM cities WHERE city_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, cityId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapCity(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return null;
    }

    private City mapCity(ResultSet rs) throws SQLException {
        City c = new City();
        c.setCityId(rs.getLong("city_id"));
        c.setCityName(rs.getString("city_name"));
        c.setProvince(rs.getString("province"));
        c.setCityType(rs.getString("city_type"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        c.setUpdatedAt(rs.getTimestamp("updated_at"));
        return c;
    }
}
