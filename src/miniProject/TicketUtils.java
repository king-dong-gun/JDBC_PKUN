package miniProject;

import java.sql.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TicketUtils {

    // 아이디를 입력받는 메소드
    static String getValidTicketId(Scanner sc, Statement stmt) throws SQLException {
        while (true) {
            System.out.print("아이디를 입력하세요 (형식: 숫자 4자리) >> ");
            String ticketId = sc.nextLine();
            if (!isValidTicketId(ticketId)) {
                System.out.println("잘못된 입력 형식입니다. 형식에 맞게 다시 입력해주세요.");
                continue;
            }

            int ticketIdInt = Integer.parseInt(ticketId);
            if (isTicketIdExists(stmt, ticketIdInt)) {
                System.out.println("이미 예약된 아이디입니다.");
                continue;
            } else {
                System.out.println("아이디 입력이 완료되었습니다.");
                return ticketId;
            }
        }
    }

    // 입력된 아이디가 숫자 4자리 형식에 맞는지 확인하는 메서드
    static boolean isValidTicketId(String ticketId) {
        String regex = "\\d{4}";
        return Pattern.matches(regex, ticketId);
    }

    // 입력된 아이디가 중복이 있는지 확인하는 메서드
    static boolean isTicketIdExists(Statement stmt, int ticketId) throws SQLException {
        String sql = "SELECT COUNT(*) AS COUNT FROM VIPTICKET WHERE ID=" + ticketId;
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        int count = rs.getInt("COUNT");
        rs.close();
        return count > 0;
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
        String sql = "SELECT COUNT(*) AS COUNT FROM VIPTICKET WHERE MATCHDATE = '" + date + "'";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        int count = rs.getInt("count");
        rs.close();
        return count;
    }


    // 티켓수량을 데이터베이스에 추가하는 메서드
    static int addTicket(Statement stmt, int id, String team, String date, String location) throws SQLException {
        String sql = "INSERT INTO VIPTICKET (TICKET_ID, id, TEAM, MATCHDATE, LOCATION) VALUES (ticket_seq.NEXTVAL," + id + ", '" + team + "', '" + date + "', '" + location + "')";

        int result = stmt.executeUpdate(sql);
        return result;
    }

    // 사용자에게 티켓 정보를 제공하는 메서드
    static ResultSet getTicket(Statement stmt, String team) throws SQLException {
        String sql = "SELECT * FROM VIPTICKET WHERE TEAM='" + team + "'";
        return stmt.executeQuery(sql);
    }

    // 예약정보를 제공하는 메소드
    static void printTicketInfo(Statement stmt, String team, String date) throws SQLException {
        String dateData = date;
        ResultSet rs = getTicket(stmt, team);
        System.out.println("[" + dateData + ", 티켓 예매 정보]");
        while (rs.next()) {
            String id = rs.getString("id");
            String titleData = rs.getString("team");
            String contentData = rs.getString("location");
            System.out.println("예약 ID >> " + id);
            System.out.println("팀 >> " + titleData);
            System.out.println("경기장 >> " + contentData);
            System.out.println();
        }
        rs.close();
    }

    // 반복을 진행하는지 묻는 메소드
    static boolean continueBooking(Scanner sc) {
        System.out.print("계속하려면 'Y'를 종료하려면 'N'을 입력하세요 >> ");
        String choice = sc.nextLine();
        return !choice.equalsIgnoreCase("N");
    }
}

