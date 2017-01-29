package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by mian on 2017/1/29.
 * mysql连接类
 */
public class MysqlDao {
    private static Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/webmagic";
        String username = "root";
        String password = "root";
        Connection conn = null;
        try {
            Class.forName(driver); // classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static int insertList(String url, String detailUrl) {
        Connection conn = getConn();
        int i = 0;
        String sql = "insert into baozou_list (URL, DETAIL_URL) values(?,?)";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, url);
            stmt.setString(2, detailUrl);
            i = stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static int insertDetail(String url, String author, String content, String time) {
        Connection conn = getConn();
        int i = 0;
        String sql = "insert into baozou (URL, AUTHOR, CONTENT , TIME) values(?,?,?,?)";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, url);
            stmt.setString(2, author);
            stmt.setString(3, content);
            stmt.setString(4, time);
            i = stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }
}
