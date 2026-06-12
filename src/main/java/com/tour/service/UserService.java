package com.tour.service;

import com.tour.dao.UserDao;
import com.tour.model.User;
import com.tour.util.PasswordUtil;

/**
 * 用户业务逻辑层
 */
public class UserService {

    private final UserDao userDao = new UserDao();

    /**
     * 用户注册
     */
    public String register(String username, String password, String nickname,
                           String email, String phone, Long roleId) {
        // 校验用户名
        if (username == null || username.trim().isEmpty()) {
            return "用户名不能为空";
        }
        if (userDao.findByUsername(username) != null) {
            return "该用户名已被注册";
        }
        // 密码校验
        if (password == null || password.length() < 6) {
            return "密码长度不能少于6位";
        }
        // 加密密码
        String encrypted = PasswordUtil.encrypt(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encrypted);
        user.setNickname(nickname != null ? nickname : username);
        user.setEmail(email != null ? email : "");
        user.setPhone(phone != null ? phone : "");
        user.setGender(0);

        Long userId = userDao.insert(user);
        if (userId == null) {
            return "注册失败，请重试";
        }

        // 分配角色: 2=普通用户, 3=景区管理员，不允许注册为游客或平台管理员
        Long finalRole = (roleId != null && roleId >= 2 && roleId <= 3) ? roleId : 2L;
        userDao.assignRole(userId, finalRole);
        return null; // null 表示成功
    }

    /**
     * 用户登录，成功返回 User 对象，失败返回 null
     */
    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            return null;
        }
        if (user.getStatus() == 0) {
            return null; // 账号已禁用
        }
        // 密码校验，密码是加密后存储在数据库中的，所以这里要调用 PasswordUtil 的 verify 方法进行校验
        if (!PasswordUtil.verify(password, user.getPassword())) {
            return null;
        }
        // 加载角色ID
        /**Long roleId = userDao.findRoleIdByUserId(user.getUserId());
        user.setPassword(null); // 不暴露密码
        // 用 gender 临时传递 roleId（已在User类中复用，实际通过session存） */
        return user;
    }

    /**
     * 获取用户角色ID
     */
    public Long getUserRoleId(Long userId) {
        return userDao.findRoleIdByUserId(userId);
    }

    /**
     * 获取用户信息
     */
    public User getProfile(Long userId) {
        User user = userDao.findById(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    /**
     * 更新用户信息
     */
    public boolean updateProfile(User user) {
        return userDao.update(user);
    }
}
