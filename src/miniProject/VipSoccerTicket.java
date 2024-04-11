package miniProject;

import java.sql.*;
import java.util.Scanner;

import static miniProject.TicketUtils.*;

public class VipSoccerTicket {
    static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe"; // Oracle 서버 주소와 포트, SID에 맞게 수정

    static final String USER = "adam";
    static final String PASS = "1234";
    static final int MAX_ManUTICKETS = 10; // 맨유 경기당 최대 티켓 예매 수량
    static final int MAX_AsnUTICKETS = 10; // 아스날 경기당 최대 티켓 예매 수량
    static final int MAX_CheTICKETS = 10; // 첼시 경기당 최대 티켓 예매 수량
    static final int MAX_LivUTICKETS = 10; // 리버풀 경기당 최대 티켓 예매 수량

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

                String ticketId = getValidTicketId(sc, stmt);
                if (ticketId == null) continue;

                String date = getValidDate(sc);
                if (date == null) continue;

                int match = getMatchSelection(sc);

                String team = "";
                String location = "";
                switch (match) {
                    case 1:
                        team = "맨체스터 유나이티드";
                        location = "올드 트레포드";
                        if (!checkTicketAvailability(stmt, date, MAX_ManUTICKETS)) continue;
                        break;
                    case 2:
                        team = "아스날";
                        location = "에미레이트 스타디움";
                        if (!checkTicketAvailability(stmt, date, MAX_AsnUTICKETS)) continue;
                        break;
                    case 3:
                        team = "첼시";
                        location = "스탬포드 브릿지";
                        if (!checkTicketAvailability(stmt, date, MAX_CheTICKETS)) continue;
                        break;
                    case 4:
                        team = "리버풀";
                        location = "안필드";
                        if (!checkTicketAvailability(stmt, date, MAX_LivUTICKETS)) continue;
                        break;
                }

                int result = addTicket(stmt, Integer.parseInt(ticketId), team, date, location);
                System.out.println("구매티켓 수량 >>  " + result);

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
            System.out.println("==========================DB 연결 종료==========================");
        }
    }
}
