package miniProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static miniProject.TicketUtils.*;
import static miniProject.UserUtils.*;

public class TicketMain {
    static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    static final String USER = "adam";
    static final String PASS = "1234";

    // 티켓 예약을 위한 최대 티켓 예약 수량 변수들
    static final int MAX_ManUTICKETS = 10;
    static final int MAX_AsnTICKETS = 10;
    static final int MAX_CheTICKETS = 10;
    static final int MAX_LivUTICKETS = 10;

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        Scanner sc = new Scanner(System.in);
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            boolean loggedIn = false;
            String userId = null;

            while (!loggedIn) {
                System.out.println("===== 로그인 =====");
                System.out.print("아이디를 입력하세요: ");
                String inputId = sc.nextLine();
                System.out.print("비밀번호를 입력하세요: ");
                String inputPassword = sc.nextLine();

                // 입력된 아이디와 비밀번호가 일치하는 사용자가 있는지 확인
                if (isLoginValid(stmt, inputId, inputPassword)) {
                    loggedIn = true;
                    userId = inputId;
                    System.out.println("로그인에 성공했습니다.");
                } else {
                    System.out.println("아이디 또는 비밀번호가 올바르지 않습니다. 다시 시도해주세요.");
                }
            }

            // 사용자로부터 회원 정보 입력 받기
            String userName;
            String email;
            String phoneNumber;
            while (true) {
                System.out.println("===== 티켓 예약 =====");
                String date = getValidDate(sc);
                if (date == null) continue;

                int match = getMatchSelection(sc);

                String team = "";
                String location = "";
                int maxTickets = 0; // 각 팀별 최대 티켓 예약 수량 변수

                switch (match) {
                    case 1:
                        team = "맨체스터 유나이티드";
                        location = "올드 트레포드";
                        maxTickets = MAX_ManUTICKETS;
                        if (!checkTicketAvailability(stmt, date, maxTickets)) continue;
                        break;
                    case 2:
                        team = "아스날";
                        location = "에미레이트 스타디움";
                        maxTickets = MAX_AsnTICKETS;
                        if (!checkTicketAvailability(stmt, date, maxTickets)) continue;
                        break;
                    case 3:
                        team = "첼시";
                        location = "스탬포드 브릿지";
                        maxTickets = MAX_CheTICKETS;
                        if (!checkTicketAvailability(stmt, date, maxTickets)) continue;
                        break;
                    case 4:
                        team = "리버풀";
                        location = "안필드";
                        maxTickets = MAX_LivUTICKETS;
                        if (!checkTicketAvailability(stmt, date, maxTickets)) continue;
                        break;
                }

                // 데이터베이스에 티켓 예약 정보 저장
                int result = addTicket(stmt, Integer.parseInt(userId), team, date, location);
                System.out.println("구매티켓 수량: " + result);


                System.out.print("구매하신 티켓 ID: ");
                int ticketId = sc.nextInt();
                sc.nextLine(); // 버퍼 비우기

                // TicketUtils 클래스의 메서드를 호출하여 티켓을 구매하고 티켓 수량을 업데이트
                purchaseTicket(conn, stmt, ticketId);

                printTicketInfo(stmt, team, date);

                if (!continueBooking(sc)) break;
            }

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
