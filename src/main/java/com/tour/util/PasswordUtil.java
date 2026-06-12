package com.tour.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 密码加密工具 — MD5 + 随机盐值
 */
public class PasswordUtil {

    private static final Random RANDOM = new SecureRandom();

    /**
     * 生成16位随机盐值
     */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        StringBuilder sb = new StringBuilder();
        for (byte b : salt) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * MD5 加密
     */
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5 加密失败", e);
        }
    }

    /**
     * 加密密码: 密码+盐值 → MD5，格式: salt#hash
     */
    public static String encrypt(String password) {
        String salt = generateSalt();
        String hash = md5(password + salt);
        return salt + "#" + hash;
    }

    /**
     * 验证密码
     */
    public static boolean verify(String password, String stored) {
        if (stored == null || !stored.contains("#")) {
            // 兼容未加盐的旧密码格式
            return md5(password).equals(stored);
        }
        String[] parts = stored.split("#", 2);
        String salt = parts[0];
        String hash = parts[1];
        return md5(password + salt).equals(hash);
    }
}
