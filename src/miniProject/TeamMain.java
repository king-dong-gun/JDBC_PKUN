import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TeamMain {
    static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    static final String USER = "adam";
    static final String PASS = "1234";

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Scanner sc = new Scanner(System.in);
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            System.out.println("경기를 선택해주세요:");
            System.out.println("1. 맨체스터 유나이티드");
            System.out.println("2. 아스날");
            System.out.println("3. 첼시");
            System.out.println("4. 리버풀");
            System.out.print("선택: ");
            int teamChoice = sc.nextInt();

            String team;
            switch (teamChoice) {
                case 1:
                    team = "맨체스터 유나이티드";
                    break;
                case 2:
                    team = "아스날";
                    break;
                case 3:
                    team = "첼시";
                    break;
                case 4:
                    team = "리버풀";
                    break;
                default:
                    System.out.println("잘못된 선택입니다.");
                    return;
            }

            System.out.print("날짜를 입력해주세요(예: 2024/MM/dd): ");
            String date = sc.next();

            // PreparedStatement를 사용하여 SQL Injection을 방지하고 사용자 입력을 처리합니다.
            String selectQuery = "SELECT MAX_TICKETS FROM VIPTICKET WHERE TEAM = ? AND MATCH_DATE = ?";
            pstmt = conn.prepareStatement(selectQuery);
            pstmt.setString(1, team);
            pstmt.setString(2, date);

            // 쿼리 실행
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int maxTickets = rs.getInt("MAX_TICKETS");
                System.out.println("티켓 구매 가능 수량: " + maxTickets);
            } else {
                System.out.println("구매된 티켓이 없습니다.");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
