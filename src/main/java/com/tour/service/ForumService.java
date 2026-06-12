package com.tour.service;

import com.tour.dao.ForumPostDao;
import com.tour.dao.PostCommentDao;
import com.tour.model.ForumPost;
import com.tour.model.PostComment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 论坛业务逻辑层
 */
public class ForumService {

    private final ForumPostDao forumPostDao = new ForumPostDao();
    private final PostCommentDao postCommentDao = new PostCommentDao();

    /**
     * 发布帖子
     */
    public boolean createPost(Long userId, String category, String title, String content) {
        if (title == null || title.trim().isEmpty()) return false;
        if (content == null || content.trim().isEmpty()) return false;

        ForumPost post = new ForumPost();
        post.setUserId(userId);
        post.setCategory(category);
        post.setTitle(title.trim());
        post.setContent(content.trim());
        return forumPostDao.insert(post) != null;
    }

    /**
     * 获取帖子列表（分页+分类筛选）
     */
    public Map<String, Object> listPosts(String category, int page, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        List<ForumPost> list = forumPostDao.findAll(category, page, pageSize);
        int total = forumPostDao.countAll(category);
        int totalPages = (int) Math.ceil((double) total / pageSize);

        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("totalPages", totalPages);
        return result;
    }

    /**
     * 获取帖子详情（含评论）
     */
    public Map<String, Object> getPostDetail(Long postId) {
        Map<String, Object> result = new HashMap<>();
        ForumPost post = forumPostDao.findById(postId);
        if (post == null) return null;

        List<PostComment> comments = postCommentDao.findByPostId(postId);
        result.put("post", post);
        result.put("comments", comments);
        return result;
    }

    /**
     * 添加评论
     */
    public boolean addComment(Long postId, Long userId, String content) {
        if (content == null || content.trim().isEmpty()) return false;

        PostComment comment = new PostComment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(content.trim());
        return postCommentDao.insert(comment);
    }

    /**
     * 删除评论
     */
    public boolean deleteComment(Long commentId) {
        return postCommentDao.delete(commentId);
    }

    /**
     * 审核帖子（管理员）
     */
    public boolean updatePostStatus(Long postId, int status) {
        return forumPostDao.updateStatus(postId, status);
    }
}
