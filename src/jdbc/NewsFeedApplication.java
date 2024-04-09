package jdbc;

import java.sql.*;
import java.util.Scanner;

public class NewsFeedApplication {
    static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe"; // Oracle 서버 주소와 포트, SID에 맞게 수정

    static final String USER = "adam";
    static final String PASS = "1234";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // JDBC 드라이버를 등록합니다.
            Class.forName(JDBC_DRIVER);

            // 데이터베이스와 연결합니다.
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            while (true) {
                System.out.println("==========================DB 연결 성공==========================");
                stmt = conn.createStatement();

                Scanner sc = new Scanner(System.in);
                System.out.println("기사 제목을 입력하세요 >> ");
                String title = sc.nextLine();

                System.out.println("기사 내용을 입력하세요 >> ");
                String content = sc.nextLine();

                System.out.println("기사 카테고리를 입력하세요 >> ");
                String category = sc.nextLine();

                addNewsArticle(stmt, title, content, category);

                String categoryData = category;
                ResultSet rs = getNewsFeed(stmt, categoryData);
                System.out.println("[" + categoryData + " 카테고리 뉴스]");
                while (rs.next()) {
                    String titleData = rs.getString("title");
                    String contentData = rs.getString("content");
                    System.out.println("제목: " + titleData);
                    System.out.println("내용: " + contentData);
                    System.out.println();
                }
                rs.close();
                stmt.close();

                System.out.println("계속하려면 'Y'를 종료하려면 'N'을 입력하세요 >> ");
                String choice = sc.nextLine();
                if (choice.equalsIgnoreCase("N")) {
                    break;
                }
            }


            // ResultSet, Statement 및 Connection을 닫습니다.
            conn.close();
            System.out.println("==========================DB 연결 종료==========================");
        } catch (SQLException e) {
            // JDBC 관련 오류를 처리합니다.
            e.printStackTrace();
        } catch (Exception e) {
            // 기타 오류를 처리합니다.
            e.printStackTrace();
        } finally {
            // 자원을 정리합니다.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    // 뉴스 기사를 데이터베이스에 추가하는 메서드
    static void addNewsArticle(Statement stmt, String title, String content, String category) throws SQLException {
        String sql = "INSERT INTO news (id, title, content, category) VALUES (news_seq.nextval, '" + title + "', '" + content + "', '" + category + "')";
        stmt.executeUpdate(sql);
    }

    // 사용자에게 뉴스 피드를 제공하는 메서드
    static ResultSet getNewsFeed(Statement stmt, String category) throws SQLException {
        String sql = "SELECT * FROM news WHERE category='" + category + "'";
        return stmt.executeQuery(sql);
    }
}


