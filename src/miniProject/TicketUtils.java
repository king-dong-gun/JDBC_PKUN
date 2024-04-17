package miniProject;

import java.sql.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TicketUtils {

    // 입력된 아이디와 비밀번호가 일치하는 사용자가 있는지 확인하는 메소드
    static boolean isLoginValid(Statement stmt, String userId, String password) throws SQLException {
        String sql = "SELECT * FROM USERINFO WHERE UserID = '" + userId + "' AND Password = '" + password + "'";
        ResultSet rs = stmt.executeQuery(sql);
        boolean isValid = rs.next();
        rs.close();
        return isValid;
    }


    // 경기날짜를 입력받는 메소드
    static String getValidDate(Scanner sc) {
        while (true) {
            System.out.print("예매하실 날짜를 선택하세요 (형식: 2024/MM/dd) >> ");
            String date = sc.nextLine();
            if (!isValidDate(date)) {
                System.out.println("잘못된 날짜 형식입니다. 형식에 맞게 다시 입력해주세요.");
                continue;
            }
            return date;
        }
    }

    // 입력된 날짜가 MM/dd 형식에 맞는지 확인하는 메서드
    static boolean isValidDate(String date) {
        String regex = "2024/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])";
        return Pattern.matches(regex, date);
    }

    // 경기정보를 확인하는 메소드
    static int getMatchSelection(Scanner sc) {
        System.out.println("예매하실 경기를 선택하세요");
        System.out.println("1. 맨체스터 유나이티드");
        System.out.println("2. 아스날");
        System.out.println("3. 첼시");
        System.out.println("4. 리버풀");
        int result = sc.nextInt();
        sc.nextLine();
        return result;
    }
    // 맨체스터 유나이티드 티켓정보를 가져오는 메소드
    public static void getManUTDMatchInfo(Statement stmt) {
        String query = "SELECT * FROM TEAM WHERE TEAM LIKE '%맨체스터%'";
        try {
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("맨체스터 유나이티드의 경기 정보:");
            while (rs.next()) {
                String matchInfo = rs.getString("TEAM");
                String location = rs.getString("LOCATION");
                String matchDate = rs.getString("MATCH_DATE");
                System.out.println("경기: " + matchInfo + ", 장소: " + location + ", 일자: " + matchDate);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 최대 티켓 수량을 확인하는 메소드
    static boolean checkTicketAvailability(Statement stmt, String date, int maxTickets) throws SQLException {
        int ticketCount = getTicketCount(stmt, date);
        if (ticketCount >= maxTickets) {
            System.out.println("이 날짜의 티켓 예매 가능 수량을 초과했습니다. 다른 날짜를 선택하세요.");
            return false;
        }
        return true;
    }

    // 티켓수량을 데이터베이스에서 가져오는 메서드
    static int getTicketCount(Statement stmt, String date) throws SQLException {
        String sql = "SELECT COUNT(*) AS COUNT FROM VIPTICKET WHERE MATCH_DATE = '" + date + "'";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        int count = rs.getInt("count");
        rs.close();
        return count;
    }


    // 티켓수량을 데이터베이스에 추가하는 메서드
    static void addTicket(Connection conn, Statement stmt, int userId, String team, String date, String location) throws SQLException {
        int ticketCount = -1;

        // (TEAM, MATCH_DATE) 조회 -> ID값을 뽑는다(변수에 저장) -> UPDATE
        // SELECT ID, TEAM, MATCH_DATE FROM VIPTICKET WHERE (TEAM, MATCH_DATE) IN (SELECT TEAM, MATCH_DATE FROM VIPTICKET);
        String checkQuery = "SELECT ID, TEAM, MATCH_DATE FROM VIPTICKET WHERE TEAM = ? AND MATCH_DATE = ?";
        PreparedStatement pstmt = conn.prepareStatement(checkQuery);
        pstmt.setString(1, team);
        pstmt.setString(2, date);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            ticketCount = rs.getInt("ID");
        }

        if (ticketCount == -1) { // 조회되는 컬럼이 없으면 추가=
            String sql = "INSERT INTO VIPTICKET (ID, TEAM, LOCATION, MATCH_DATE) VALUES (ticket_seq.NEXTVAL, '" + team + "', '" + location + "', '" + date + "')";
            stmt.executeUpdate(sql);
            // 티켓수 줄이는 UPDATE 문 필요
            String checkSeq = "SELECT LAST_NUMBER FROM ALL_SEQUENCES WHERE SEQUENCE_NAME = 'TICKET_SEQ'";
            PreparedStatement preparedStatement2 = stmt.getConnection().prepareStatement(checkSeq);
            ResultSet rs2 = preparedStatement2.executeQuery();
            while (rs2.next()) {
                ticketCount = rs2.getInt("LAST_NUMBER");
            }
            ticketCount -= 1;
            String updateQuery = "UPDATE VIPTICKET SET MAX_TICKETS = MAX_TICKETS - 1 WHERE ID = ?";
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, ticketCount);
            pstmt.executeUpdate();
        } else {
            String updateQuery = "UPDATE VIPTICKET SET MAX_TICKETS = MAX_TICKETS - 1 WHERE ID = ?";
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, ticketCount);
            pstmt.executeUpdate();
        }
    }


    // 경기 일자 정보 가져오는 메소드
    static ResultSet getTicket(Statement stmt, String date) throws SQLException {
        String sql = "SELECT * FROM VIPTICKET WHERE MATCH_DATE='" + date + "'";
        return stmt.executeQuery(sql);
    }


    // 날짜별 티켓 정보를 출력하는 메서드
    static void printTicketInfo(Statement stmt, String date) throws SQLException {
        ResultSet rs = getTicket(stmt, date);
        System.out.println("[" + date + ", 티켓 예매 정보]");
        while (rs.next()) {
            String id = rs.getString("ID");
            String team = rs.getString("TEAM");
            String location = rs.getString("LOCATION");
            System.out.println("ID >> " + id);
            System.out.println("팀 >> " + team);
            System.out.println("경기장 >> " + location);
            System.out.println();
        }
        rs.close();
    }

    // 반복을 진행하는지 묻는 메소드
    static boolean continueBooking(Scanner sc) {
        System.out.print("메뉴로 가시려면 'Y'를 종료하려면 'N'을 입력하세요 >> ");
        String choice = sc.nextLine();
        return !choice.equalsIgnoreCase("N");
    }
}