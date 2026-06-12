package com.tour.dao;

import com.tour.model.ForumPost;
import com.tour.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 论坛帖子数据访问层
 */
public class ForumPostDao {

    /**
     * 发布帖子
     */
    public Long insert(ForumPost post) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO forum_posts (user_id, category, title, content, status) VALUES (?, ?, ?, ?, 1)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, post.getUserId());
            stmt.setString(2, post.getCategory());
            stmt.setString(3, post.getTitle());
            stmt.setString(4, post.getContent());
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
     * 根据ID查询帖子
     */
    public ForumPost findById(Long postId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT p.*, u.nickname, u.username FROM forum_posts p " +
                         "LEFT JOIN users u ON p.user_id = u.user_id WHERE p.post_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, postId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapPost(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return null;
    }

    /**
     * 分页查询帖子列表（支持分类筛选）
     */
    public List<ForumPost> findAll(String category, int page, int pageSize) {
        List<ForumPost> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            StringBuilder sql = new StringBuilder(
                "SELECT p.*, u.nickname, u.username FROM forum_posts p " +
                "LEFT JOIN users u ON p.user_id = u.user_id WHERE p.status = 1");
            List<Object> params = new ArrayList<>();

            if (category != null && !category.trim().isEmpty()) {
                sql.append(" AND p.category = ?");
                params.add(category);
            }

            sql.append(" ORDER BY p.created_at DESC LIMIT ?, ?");
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
                list.add(mapPost(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * 统计帖子总数
     */
    public int countAll(String category) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM forum_posts WHERE status = 1");
            if (category != null && !category.trim().isEmpty()) {
                sql.append(" AND category = ?");
            }
            stmt = conn.prepareStatement(sql.toString());
            if (category != null && !category.trim().isEmpty()) {
                stmt.setString(1, category);
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
     * 删除帖子
     */
    public boolean delete(Long postId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            // 先删评论
            stmt = conn.prepareStatement("DELETE FROM post_comments WHERE post_id = ?");
            stmt.setLong(1, postId);
            stmt.executeUpdate();
            stmt.close();
            // 再删帖子
            stmt = conn.prepareStatement("DELETE FROM forum_posts WHERE post_id = ?");
            stmt.setLong(1, postId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    /**
     * 更新帖子状态（审核/下架）
     */
    public boolean updateStatus(Long postId, int status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE forum_posts SET status = ? WHERE post_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, status);
            stmt.setLong(2, postId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    /**
     * 查询所有帖子（管理员用，含违规帖）
     */
    public List<ForumPost> findAllForAdmin(String category, String keyword, int page, int pageSize) {
        List<ForumPost> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            StringBuilder sql = new StringBuilder(
                "SELECT p.*, u.nickname, u.username FROM forum_posts p " +
                "LEFT JOIN users u ON p.user_id = u.user_id WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (category != null && !category.trim().isEmpty()) {
                sql.append(" AND p.category = ?");
                params.add(category);
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND (p.title LIKE ? OR p.content LIKE ?)");
                String kw = "%" + keyword.trim() + "%";
                params.add(kw);
                params.add(kw);
            }

            sql.append(" ORDER BY p.created_at DESC LIMIT ?, ?");
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
                list.add(mapPost(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return list;
    }

    private ForumPost mapPost(ResultSet rs) throws SQLException {
        ForumPost p = new ForumPost();
        p.setPostId(rs.getLong("post_id"));
        p.setUserId(rs.getLong("user_id"));
        p.setCategory(rs.getString("category"));
        p.setTitle(rs.getString("title"));
        p.setContent(rs.getString("content"));
        p.setStatus(rs.getInt("status"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        try { p.setUsername(rs.getString("nickname") != null ? rs.getString("nickname") : rs.getString("username")); } catch (SQLException ignored) {}
        return p;
    }
}
