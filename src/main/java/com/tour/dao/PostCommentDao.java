package com.tour.dao;

import com.tour.model.PostComment;
import com.tour.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 帖子评论数据访问层
 */
public class PostCommentDao {

    /**
     * 添加评论
     */
    public boolean insert(PostComment comment) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO post_comments (post_id, user_id, content) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, comment.getPostId());
            stmt.setLong(2, comment.getUserId());
            stmt.setString(3, comment.getContent());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }

    /**
     * 查询某帖子的所有评论（按时间正序）
     */
    public List<PostComment> findByPostId(Long postId) {
        List<PostComment> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT c.*, u.nickname, u.username FROM post_comments c " +
                         "LEFT JOIN users u ON c.user_id = u.user_id " +
                         "WHERE c.post_id = ? ORDER BY c.created_at ASC";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, postId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                PostComment pc = new PostComment();
                pc.setCommentId(rs.getLong("comment_id"));
                pc.setPostId(rs.getLong("post_id"));
                pc.setUserId(rs.getLong("user_id"));
                pc.setContent(rs.getString("content"));
                pc.setCreatedAt(rs.getTimestamp("created_at"));
                String name = rs.getString("nickname");
                pc.setUsername(name != null && !name.isEmpty() ? name : rs.getString("username"));
                list.add(pc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * 统计帖子的评论数
     */
    public int countByPostId(Long postId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM post_comments WHERE post_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, postId);
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
     * 删除评论
     */
    public boolean delete(Long commentId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM post_comments WHERE comment_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, commentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(null, stmt, conn);
        }
        return false;
    }
}
