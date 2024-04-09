package miniProject;

import java.sql.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class VipSoccerTicket {
    static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe"; // Oracle 서버 주소와 포트, SID에 맞게 수정

    static final String USER = "adam";
    static final String PASS = "1234";
    static final int MAX_TICKETS = 10; // 경기당 최대 티켓 예매 수량
    static final int MAX_ID = 1;    // 아이디당 최대 티켓 예매 수량

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println("==========================DB 연결 성공==========================");

                // 아이디 입력
                System.out.println("아이디를 입력하세요 (형식: 숫자 4자리)");
                String ticketId = sc.nextLine();

                // 입력된 아이디가 숫자 4자리 형식에 맞는지 확인
                if (!isValidTicketId(String.valueOf(ticketId))) {
                    System.out.println("잘못된 입력 형식입니다. 형식에 맞게 다시 입력해주세요.");
                    continue;
                }

                System.out.println("아이디 입력이 완료되었습니다.");
                int ticketIdInt = Integer.parseInt(ticketId);
                // 동일한 아이디가 있는지 확인
                if (isTicketIdExists(stmt, ticketIdInt)) {
                    System.out.println("이미 예약된 아이디입니다.");
                    continue;
                }

                // 날짜 선택
                System.out.println("예매하실 날짜를 선택하세요 (형식: 2024/MM/dd)");
                String date = sc.nextLine();

                // 입력된 날짜가 MM/dd 형식에 맞는지 확인
                if (!isValidDate(date)) {
                    System.out.println("잘못된 날짜 형식입니다. 형식에 맞게 다시 입력해주세요.");
                    continue;
                }

                // 경기 선택
                System.out.println("예매하실 경기를 선택하세요");
                System.out.println("1. 맨체스터 유나이티드");
                System.out.println("2. 아스날");
                System.out.println("3. 첼시");
                System.out.println("4. 리버풀");
                int match = sc.nextInt();
                sc.nextLine();
                String team = ""; // 예약된 팀 이름
                String location = ""; // 예약된 경기장
                switch (match) {
                    case 1:
                        team = "맨체스터 유나이티드";
                        location = "올드 트레포드"; // 맨체스터 유나이티드의 경기장 추가
                        // 티켓 예매 수량 확인
                        int ticketCount1 = getTicketCount(stmt, date);
                        if (ticketCount1 >= MAX_TICKETS) {
                            System.out.println("이 날짜의 티켓 예매 가능 수량을 초과했습니다. 다른 날짜를 선택하세요.");
                            continue;
                        }
                        break;
                    case 2:
                        team = "아스날";
                        location = "에미레이트 스타디움"; // 아스날의 경기장 추가
                        // 티켓 예매 수량 확인
                        int ticketCount2 = getTicketCount(stmt, date);
                        if (ticketCount2 >= MAX_TICKETS) {
                            System.out.println("이 날짜의 티켓 예매 가능 수량을 초과했습니다. 다른 날짜를 선택하세요.");
                            continue;
                        }
                        break;
                    case 3:
                        team = "첼시";
                        location = "스탬포드 브릿지"; // 첼시의 경기장 추가
                        // 티켓 예매 수량 확인
                        int ticketCount3 = getTicketCount(stmt, date);
                        if (ticketCount3 >= MAX_TICKETS) {
                            System.out.println("이 날짜의 티켓 예매 가능 수량을 초과했습니다. 다른 날짜를 선택하세요.");
                            continue;
                        }
                        break;
                    case 4:
                        team = "리버풀";
                        location = "안필드"; // 리버풀의 경기장 추가
                        // 티켓 예매 수량 확인
                        int ticketCount4 = getTicketCount(stmt, date);
                        if (ticketCount4 >= MAX_TICKETS) {
                            System.out.println("이 날짜의 티켓 예매 가능 수량을 초과했습니다. 다른 날짜를 선택하세요.");
                            continue;
                        }
                        break;
                }

                int result = addTicket(stmt, ticketIdInt, team, date, location);
                System.out.println("result = " + result);


                // 티켓정보 확인
                String dateData = date;
                ResultSet rs = getTicket(stmt, team);
                System.out.println("[" + dateData + " 티켓 예매 정보]");
                while (rs.next()) {
                    String id = rs.getString("id");
                    String titleData = rs.getString("team");
                    String contentData = rs.getString("location");
                    System.out.println("예약 ID: " + id);
                    System.out.println("팀: " + titleData);
                    System.out.println("위치: " + contentData);
                    System.out.println();
                }
                rs.close();

                System.out.println("계속하려면 'Y'를 종료하려면 'N'을 입력하세요 >> ");
                String choice = sc.next();
                if (choice.equalsIgnoreCase("N")) {
                    break;
                }
            }

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close(); // Statement 객체를 닫음
                if (conn != null) conn.close(); // Connection 객체를 닫음
            } catch (SQLException se) {
                se.printStackTrace();
            }
            System.out.println("==========================DB 연결 종료==========================");
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

    // 티켓수량을 데이터베이스에서 가져오는 메서드
    static int getTicketCount(Statement stmt, String date) throws SQLException {
        String sql = "SELECT COUNT(*) AS COUNT FROM VIPTICKET WHERE MATCHDATE = '" + date + "'";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        int count = rs.getInt("count");
        rs.close();
        return count;
    }

    // 입력된 날짜가 MM/dd 형식에 맞는지 확인하는 메서드
    static boolean isValidDate(String date) {
        String regex = "2024/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])";
        return Pattern.matches(regex, date);
    }

    // 티켓수량을 데이터베이스에 추가하는 메서드
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
}
