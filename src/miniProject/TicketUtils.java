package miniProject;

import java.sql.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TicketUtils {

    // 로그인 메소드
    static boolean performLogin(Scanner sc, Statement stmt) throws SQLException {
        while (true) {
            System.out.print("아이디를 입력하세요: ");
            String inputId = sc.nextLine();
            System.out.print("비밀번호를 입력하세요: ");
            String inputPassword = sc.nextLine();

            if (isLoginValid(stmt, inputId, inputPassword)) {
                System.out.println("로그인에 성공했습니다.");
                return true;
            } else {
                System.out.println("아이디 또는 비밀번호가 올바르지 않습니다. 다시 시도해주세요.");
            }
        }
    }

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
        String query = "SELECT * FROM HOMETEAM WHERE HOMESTADIUM = '올드 트레포드'";
        try {
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("맨체스터 유나이티드 홈 경기 정보:");
            while (rs.next()) {
                String matchInfo = rs.getString("TEAMNAME");
                String location = rs.getString("HOMESTADIUM");
                String matchDate = rs.getString("MATCH_DATE");
                System.out.println("경기: " + matchInfo + ", 장소: " + location + ", 일자: " + matchDate);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 아스날 티켓정보를 가져오는 메소드
    public static void getAsrMatchInfo(Statement stmt) {
        String query = "SELECT * FROM HOMETEAM WHERE HOMESTADIUM = '에미레이츠 스타디움'";
        try {
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("아스날 홈 경기 정보:");
            while (rs.next()) {
                String matchInfo = rs.getString("TEAMNAME");
                String location = rs.getString("HOMESTADIUM");
                String matchDate = rs.getString("MATCH_DATE");
                System.out.println("경기: " + matchInfo + ", 장소: " + location + ", 일자: " + matchDate);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 첼시 티켓정보를 가져오는 메소드
    public static void getCheMatchInfo(Statement stmt) {
        String query = "SELECT * FROM HOMETEAM WHERE HOMESTADIUM = '스탬포드 브릿지'";
        try {
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("첼시 홈 경기 정보:");
            while (rs.next()) {
                String matchInfo = rs.getString("TEAMNAME");
                String location = rs.getString("HOMESTADIUM");
                String matchDate = rs.getString("MATCH_DATE");
                System.out.println("경기: " + matchInfo + ", 장소: " + location + ", 일자: " + matchDate);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 아스날 티켓정보를 가져오는 메소드
    public static void getLivMatchInfo(Statement stmt) {
        String query = "SELECT * FROM HOMETEAM WHERE HOMESTADIUM = '안필드'";
        try {
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("리버풀 홈 경기 정보:");
            while (rs.next()) {
                String matchInfo = rs.getString("TEAMNAME");
                String location = rs.getString("HOMESTADIUM");
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


//    // 티켓수량을 데이터베이스에 추가하는 메서드
//    static int addTicket(Connection conn, Statement stmt, String userId, String team, String date, String location) throws SQLException {
//        int ticketCount = -1;
//
//        // TEAM과 MATCH_DATE를 조건으로 ID값 조회
//        String checkQuery = "SELECT ID, MAX_TICKETS FROM VIPTICKET WHERE TEAM = ? AND MATCH_DATE = ?";
//        PreparedStatement pstmt = conn.prepareStatement(checkQuery);
//        pstmt = conn.prepareStatement(checkQuery);
//        pstmt.setString(1, team);
//        pstmt.setString(2, date);
//        ResultSet rs = pstmt.executeQuery();
//        if (rs.next()) {
//            ticketCount = rs.getInt("ID");
//            int maxTickets = rs.getInt("MAX_TICKETS");
//
//            if (maxTickets <= 0) {
//                // 티켓의 최대 수량이 0 이하이면 예매 불가
//                System.out.println("구매할 수 없습니다. 모든 티켓이 판매되었습니다.");
//            } else {
//                // 티켓 수량 줄이기
//                String updateQuery = "UPDATE VIPTICKET SET MAX_TICKETS = MAX_TICKETS - 1 WHERE ID = ?";
//                pstmt = conn.prepareStatement(updateQuery);
//                pstmt.setInt(1, ticketCount);
//                pstmt.executeUpdate();
//                System.out.println("티켓 예약이 완료되었습니다.");
//            }
//        }
//        rs.close();
//        return ticketCount;
//    }
static int addTicket(Connection conn, Statement stmt, String userId, String team, String date, String location) throws SQLException {
    int remainingTickets = -1; // 남은 티켓 수 초기화

    // 팀과 경기 날짜를 조건으로 하여 티켓 ID와 최대 티켓 수를 조회
    String checkQuery = "SELECT ID, MAX_TICKETS FROM VIPTICKET WHERE TEAM = ? AND MATCH_DATE = ?";
    PreparedStatement pstmt = conn.prepareStatement(checkQuery);
    pstmt.setString(1, team);
    pstmt.setString(2, date);
    ResultSet rs = pstmt.executeQuery();

    if (rs.next()) {
        int ticketId = rs.getInt("ID"); // 티켓 ID
        remainingTickets = rs.getInt("MAX_TICKETS"); // 현재 남은 티켓 수

        // 남은 티켓 수가 0보다 크면 티켓 수량을 하나 줄임
        if (remainingTickets > 0) {
            String updateQuery = "UPDATE VIPTICKET SET MAX_TICKETS = MAX_TICKETS - 1 WHERE ID = ?";
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, ticketId);
            pstmt.executeUpdate();
            remainingTickets--; // 티켓 수량 감소
        }
    } else {
        // 해당 경기의 티켓 정보가 없으면 새로운 티켓 레코드를 추가
        String insertQuery = "INSERT INTO VIPTICKET (ID, TEAM, MATCH_DATE, MAX_TICKETS) VALUES (ticket_seq.NEXTVAL, ?, ?, ?)";
        pstmt = conn.prepareStatement(insertQuery);
        pstmt.setString(1, team);
        pstmt.setString(2, date);
        pstmt.setInt(3, 9); // 새 티켓은 10장 중 1장이 예매된 상태로 시작
        pstmt.executeUpdate();
        remainingTickets = 9; // 새 티켓은 10장 중 1장이 예매된 상태로 시작
    }
    rs.close();
    return remainingTickets; // 남은 티켓 수 반환
}



    // 입력된 날짜가 존재하는지 확인하는 메서드
    static boolean isMatchDateValid(Connection conn, String date) throws SQLException {
        // 입력된 날짜가 존재하는지 확인하는 쿼리
        String dateCheckQuery = "SELECT * FROM HOMETEAM WHERE MATCH_DATE = ?";
        PreparedStatement pstmt = conn.prepareStatement(dateCheckQuery);
        pstmt.setString(1, date);
        ResultSet dateCheckResult = pstmt.executeQuery();

        // 입력된 날짜가 존재하지 않으면 false 반환
        return dateCheckResult.next();
    }


    // 경기 일자 정보 가져오는 메소드
    static ResultSet getTicket(Statement stmt, String date) throws SQLException {
        String sql = "SELECT * FROM HOMETEAM WHERE MATCH_DATE='" + date + "'";
        return stmt.executeQuery(sql);
    }


    // 날짜별 티켓 정보를 출력하는 메서드
    static void printTicketInfo(Statement stmt, String date) throws SQLException {
        ResultSet rs = getTicket(stmt, date);
        System.out.println("[" + date + ", 티켓 예매 정보]");
        while (rs.next()) {
            String team = rs.getString("TEAMNAME");
            String location = rs.getString("HOMESTADIUM");
            String matchDate = rs.getString("MATCH_DATE");
            System.out.println("팀 >> " + team);
            System.out.println("경기장 >> " + location);
            System.out.println("날짜 >> " + matchDate);
            System.out.println();
        }
        rs.close();
    }

    // 반복을 진행하는지 묻는 메소드
    static boolean continueBooking(Scanner sc) {
        System.out.print("메뉴로 가시려면 아무키나 입력하시고, 종료하려면 'N'을 입력하세요 >> ");
        String choice = sc.nextLine().trim(); // 공백 제거를 위해 trim() 추가
        return !choice.equalsIgnoreCase("n"); // "n"이 아니면 계속, "n"이면 종료
    }



    // 경기 정보 조회 메소드
    public static void getMatchInfo(Connection conn, String matchInfo, String matchDate) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String selectQuery = "SELECT HOMESTADIUM, MATCH_DATE FROM HOMETEAM WHERE TEAMNAME = ? AND MATCH_DATE = ?";
            pstmt = conn.prepareStatement(selectQuery);
            pstmt.setString(1, matchInfo);
            pstmt.setString(2, matchDate);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                String stadium = rs.getString("HOMESTADIUM");
                String matchDateResult = rs.getString("MATCH_DATE");
                System.out.println("경기 정보: " + stadium + ", " + matchDateResult);
            } else {
                System.out.println("해당 날짜에는 경기가 없습니다.");
            }
        } finally {
            // 리소스 해제
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
}

