package miniProject;

import java.sql.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserUtils {

    // ID를 입력받는 메소드

    public static String inPutId(Scanner sc, Statement stmt) throws SQLException {
        String inputId;
        while (true) {
            System.out.print("아이디를 입력하세요 (형식: 숫자 4자리) >> ");
            inputId = sc.nextLine();
            if (!makeId_4(inputId)) {
                System.out.println("잘못된 입력 형식입니다. 형식에 맞게 다시 입력해주세요.");
            } else {
                break;
            }
        }
        return inputId;
    }

    // ID가 4자리인지 확인하는 메소드
    static boolean makeId_4(String id) {
        String regex = "\\d{4}";
        return Pattern.matches(regex, id);
    }

    // 입력된 아이디가 중복이 있는지 확인하는 메서드
    public static boolean isUserIdExists(Statement stmt, String userId) throws SQLException {
        String sql = "SELECT COUNT(*) AS COUNT FROM USERINFO WHERE UserID='" + userId + "'";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        int count = rs.getInt("COUNT");
        rs.close();
        return count > 0;
    }

    // 비밀번호를 입력받는 메소드
    public static String inputPassword(Scanner sc) {
        System.out.print("비밀번호를 입력하세요 >> ");
        return sc.nextLine();
    }

    // 전화번호의 유효성을 검사하는 메소드
    public static boolean isValidNumber(String phoneNumber) {
        String regex = "^\\d{2,3}-\\d{3,4}-\\d{4}$";
        return Pattern.matches(regex, phoneNumber);
    }

    // 이메일 유효성을 검사하는 메소드
    public static boolean isValidEmail(String eMail) {
        String regex = "^[a-zA-Z0-9]([-_.]?[a-zA-Z0-9]+)*@[a-zA-Z0-9]([-_.]?[a-zA-Z0-9]+)*\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, eMail);
    }

    // 회원가입 정보를 확인하는 메소드
    public static void userInfo(String userId, String userName, String email, String phoneNumber) {
        System.out.println("ID >> : " + userId);
        System.out.println("이름 >> " + userName);
        System.out.println("이메일 >> " + email);
        System.out.println("전화번호 >> " + phoneNumber);
    }

    static void withdrawMember(Connection conn, Statement stmt, String userId) throws SQLException {
        // 사용자 아이디로 회원 정보를 삭제하는 SQL 쿼리
        String deleteQuery = "DELETE FROM USERINFO WHERE UserID = ?";
        PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
        pstmt.setString(1, userId);

        // SQL 쿼리 실행
        int rowsAffected = pstmt.executeUpdate();

        // 삭제된 행의 수를 확인하여 회원 탈퇴가 성공적으로 이루어졌는지 확인
        if (rowsAffected > 0) {
            System.out.println("회원 탈퇴가 완료되었습니다.");
        } else {
            System.out.println("회원 탈퇴에 실패했습니다. 다시 시도해주세요.");
        }
    }
}

