package jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcModel {
    // Statement 객체 사용
    // 1. EMP 테이블 전체 회원정보 조회

    public void empSelectAll() {

        // 객체사용후 close하기위해 지역변수로 선언
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            // 오라클 드라이버를 사용하겠다는 의미
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // 자신의 주소값에 scott 라는 아이디와 tiger 의 비밀번호로 접속함
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "adam", "1234");

            stmt = conn.createStatement();
            // EMP 테이블 전체조회하기위한 쿼리문
            String query = "SELECT * FROM EMP";

            //select문이기때문에 executeQuery으로사용
            rs = stmt.executeQuery(query);
            System.out.println("==================DB 연결 성공==================");
            while (rs.next()) {
                int empNo = rs.getInt("EMPNO");
                String empName = rs.getString("ENAME");
                String job = rs.getString("JOB");
                int mgr = rs.getInt("MGR");
                // Date import 대상은 util 패키지가아닌 sql 패키지 해야함
                Date hireDate = rs.getDate("HIREDATE");
                int sal = rs.getInt("SAL");
                int comm = rs.getInt("COMM");
                int deptNo = rs.getInt("DEPTNO");

                // 출력
                System.out.println(empNo + ", " + empName + ", " + job + ", " + mgr + ", " + hireDate + ", " + sal + ", " + comm
                        + ", " + deptNo);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 항상 사용후 무조건 닫아주자!
            try {
                rs.close();
                stmt.close();
                conn.close();
                System.out.println("==================DB 연결 종료==================");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
