package com.tour.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * JDBC 数据库连接工具类
 */
public class DBUtil {

    private static final String DRIVER  = "com.mysql.jdbc.Driver";
    private static final String URL     = "jdbc:mysql://localhost:3306/tour_db?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    private static final String USER    = "root";
    private static final String PASSWORD = "@Xjt1621857576";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL 驱动加载失败", e);
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据库连接失败", e);
        }
    }

    /**
     * 释放资源
     */
    public static void closeAll(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
