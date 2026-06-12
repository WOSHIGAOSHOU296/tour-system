package com.tour.dao;

import com.tour.model.User;
import com.tour.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问层
 */
public class UserDao {

    /**
     * 根据用户名查询用户
     */
    public User findByUsername(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();//获取数据库连接
            String sql = "SELECT * FROM users WHERE username = ?";//SQL语句
            stmt = conn.prepareStatement(sql);//预编译sql语句
            stmt.setString(1, username);//将username赋值给sql语句中第一个占位符
            rs = stmt.executeQuery();//调用executeQuery方法，执行sql语句，返回结果集
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return null;
    }

    /**
     * 根据ID查询用户
     */
    public User findById(Long userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM users WHERE user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            rs = stmt.executeQuery();
            if (rs.next()) {//rs.next()判断结果集是否还有下一行数据，如果有则返回true
                return mapUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return null;
    }

    /**
     * 新增用户，返回自增ID
     */
    public Long insert(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO users (username, password, nickname, email, phone, gender, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);//statement对象返回自增主键
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getNickname());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhone());
            stmt.setInt(6, user.getGender() != null ? user.getGender() : 0);//三元运算，如果gender为null，则默认为0，否则使用user.getGender()的值
            stmt.setInt(7, 1);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();//获取自增主键，通常在INSERT操作后使用，用于获取新插入记录的ID，便于后续操作或返回给调用者
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
     * 更新用户信息
     */
    public boolean update(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE users SET nickname=?, email=?, phone=?, gender=? WHERE user_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getNickname());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setInt(4, user.getGender() != null ? user.getGender() : 0);
            stmt.setLong(5, user.getUserId());
            return stmt.executeUpdate() > 0;//executeUpdate()返回受影响的行数，如果大于0则表示更新成功，否则更新失败
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    /**
     * 更新用户状态（禁用/启用）
     */
    public boolean updateStatus(Long userId, int status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE users SET status=? WHERE user_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, status);
            stmt.setLong(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    /**
     * 查询所有用户（分页）
     */
    public List<User> findAll(int page, int pageSize) {
        List<User> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM users ORDER BY created_at DESC LIMIT ?, ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, (page - 1) * pageSize);
            stmt.setInt(2, pageSize);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * 统计用户总数
     */
    public int countAll() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM users";
            stmt = conn.prepareStatement(sql);
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
     * 查询用户的角色ID
     */
    public Long findRoleIdByUserId(Long userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT role_id FROM user_role WHERE user_id = ? LIMIT 1";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("role_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return null;
    }

    /**
     * 为用户分配角色
     */
    public boolean assignRole(Long userId, Long roleId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            stmt.setLong(2, roleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    /**
     * 修改用户角色
     */
    public boolean updateRole(Long userId, Long roleId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE user_role SET role_id = ? WHERE user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, roleId);
            stmt.setLong(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    /**
     * ResultSet → User 映射
     * 这段代码将ResultSet转换为User对象：
     * 从结果集中读取各字段值，设置到User实体的对应属性中，包括用户ID、用户名、密码、昵称、邮箱、手机、头像、性别、状态及时间戳，最后返回完整的User对象。
     */
    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getLong("user_id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setNickname(rs.getString("nickname"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        u.setAvatarUrl(rs.getString("avatar_url"));
        u.setGender(rs.getInt("gender"));
        u.setStatus(rs.getInt("status"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        u.setUpdatedAt(rs.getTimestamp("updated_at"));
        return u;
    }
}
