package com.test.day03.util;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JDBCUtils {
    public static Connection getConnection() {
        String url = "jdbc:mysql://8.129.91.152:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
        String user = "future";
        String password = "123456";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args) throws Exception {
        //1、插入数据
//        String insertSQL = "INSERT into member VALUE(1187999,'周大神2','952D9AFCE978A5C2F2883395AF3AC98E','15336566666',1,2000.0,'2020-1-11 22:22:22');";
//        update(insertSQL);
//        //2、更改数据
//        String updateSQL = "UPDATE member SET reg_name = 'Java22' WHERE id = 1187999";
//        update(updateSQL);
        //3、删除数据 -- -tongshang
        //4、查询数据
        //4.1查询全部数据
//        String sqlALL = "SELECT * FROM member limit 10;";
//        queryALL(sqlALL);
        //4.2查询单个数据
//        String sqlSingle = "SELECT count(*) FROM member where reg_name = 'Java22'";
//        System.out.println(querySingle(sqlSingle));
        //4.3查询一条完整数据
//        String sqlOne = "SELECT * FROM member where reg_name = 'Java22'";
//        System.out.println(queryOne(sqlOne));
        //如何解决每一次注册运行的时候，手机号码需要手动更改的问题
        //1、随机生成手机号码，（可能已经被注册）
        //2、去查询数据库是否存在，如果有注册再重新生成一次
        //3、1-2循环 知道生成一个可以用的手机号
    }

    public static void update(String sql) throws Exception {
        Connection connection = getConnection();
        QueryRunner query = new QueryRunner();
        query.update(connection, sql);
    }
    public  static List<Map<String, Object>> queryALL(String sql){
        Connection connection = getConnection();
        QueryRunner query = new QueryRunner();
        //数据库连接对象、执行sql语句、接收查询结果的
        MapListHandler mapListHandler = new MapListHandler();
        try {
            List<Map<String, Object>> result = query.query(connection, sql, mapListHandler);
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public static Map<String,Object> queryOne(String sql){
        Connection connection = getConnection();
        QueryRunner query = new QueryRunner();
        //数据库连接对象、执行sql语句、接收查询结果的
        try {
            MapHandler mapHandler = new MapHandler();
            Map<String, Object> result = query.query(connection, sql, mapHandler);
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public static Object querySingle(String sql){
        Connection connection = getConnection();
        QueryRunner query = new QueryRunner();
        //数据库连接对象、执行sql语句、接收查询结果的
        try {
            Object result =query.query(connection, sql, new ScalarHandler<Object>());
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
