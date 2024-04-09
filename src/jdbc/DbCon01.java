package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbCon01 {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");  // Oracle 드라이버 클래스명
            System.out.println("JDBC DRIVER 로드 성공 ");
            String url = "jdbc:oracle:thin:@localhost:1521:xe";
            String user = "ADAM";
            String pw = "1234";
            String sql = "SELCET * FROM EMP";
            connection = DriverManager.getConnection(url, user, pw);    // DB 연결하기
            System.out.println("DB 연결 성공");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  // 예외 메시지 출력을 위해 e.printStackTrace()를 사용합니다.
            throw new RuntimeException("JDBC 드라이버를 로드할 수 없습니다.", e);
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 메시지 출력을 위해 e.printStackTrace()를 사용합니다.
            throw new RuntimeException("데이터베이스 연결에 실패했습니다.", e);
        } finally {
            try {
                connection.close();
                System.out.println("데이터베이스 연결 해제 \n");
            } catch (SQLException e) {
                e.printStackTrace();  // 예외 메시지 출력을 위해 e.printStackTrace()를 사용합니다.
            }
        }
    }
}
