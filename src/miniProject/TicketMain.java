package miniProject;

import java.sql.*;
import java.util.Scanner;

import static miniProject.TicketUtils.*;
import static miniProject.UserUtils.*;
import static miniProject.UserUtils.userInfo;

public class TicketMain {
    static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    static final String USER = "adam";
    static final String PASS = "1234";

    // 티켓 예약을 위한 최대 티켓 예약 수량 변수들
    static final int MAX_ManUTickets = 10;
    static final int MAX_AsnTickets = 10;
    static final int MAX_CheTickets = 10;
    static final int MAX_LivTickets = 10;

    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Statement stmt = null;
        Scanner sc = new Scanner(System.in);
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            boolean loggedIn = false;
            String userId = null;

            System.out.println("==============23/24 잉글리쉬 프리미어리그 티켓 구매 및 조회 프로그램==============");

            // 메인 메뉴 루프
            while (true) {
                System.out.println("==============메뉴==============");
                System.out.println("1. 회원가입");
                System.out.println("2. 티켓 조회");
                System.out.println("3. 티켓 예매");
                int menu = sc.nextInt();
                sc.nextLine();
                switch (menu) {
                    case 1:
                        String userPass;
                        String userName;
                        String email;
                        String phoneNumber;
                        while (true) {
                            userId = inPutId(sc, stmt); // 아이디 입력
                            if (isUserIdExists(stmt, userId)) {
                                System.out.println("이미 사용 중인 아이디입니다. 다른 아이디를 입력해주세요.");
                            } else {
                                break; // 아이디가 유효한 경우 루프를 종료하고 비밀번호 입력 단계로 이동
                            }
                        }

                        while (true) {
                            userPass = inputPassword(sc); // 비밀번호 입력
                            if (userPass.length() < 6) {
                                System.out.println("비밀번호 길이가 올바르지 않습니다. (최소 6자 이상)");
                            } else {
                                System.out.println("비밀번호 입력이 완료되었습니다.");
                                break; // 올바른 비밀번호를 입력한 경우 루프를 종료
                            }
                        }


                        System.out.print("이름을 입력하세요 >> ");
                        userName = sc.nextLine();
                        while (true) {
                            System.out.print("이메일을 입력하세요 (형식 email@address.com) >> ");
                            email = sc.nextLine();

                            // 입력된 이메일의 유효성 검사
                            if (!isValidEmail(email)) {
                                System.out.println("유효하지않은 이메일 형식입니다. 다시 입력해주세요");
                            } else {
                                break; // 올바른 이메일을 입력한 경우 루프를 종료
                            }
                        }

                        while (true) {
                            System.out.print("전화번호를 입력하세요 >> ");
                            phoneNumber = sc.nextLine();

                            // 입력된 전화번호의 유효성 검사
                            if (!isValidNumber(phoneNumber)) {
                                System.out.println("유효하지 않은 전화번호 형식입니다. 다시 입력해주세요.");
                            } else {
                                System.out.println("회원가입이 완료되었습니다.");
                                break; // 회원가입이 완료되면 루프를 종료하고 메뉴 선택으로 돌아감
                            }
                            // 모든 정보가 유효한 경우 회원가입 완료
                            break;
                        }

                        // SQL 쿼리를 사용하여 userinfo 테이블에 회원 정보 삽입
                        String sql = "INSERT INTO USERINFO (UserID, Password, Name, Email, PhoneNumber) " +
                                "VALUES ('" + userId + "', '" + userPass + "', '" + userName + "', '" + email + "', '" + phoneNumber + "')";
                        stmt.executeUpdate(sql);

                        // 회원가입된 정보 확인
                        userInfo(userId, userName, email, phoneNumber);

                        if (!continueBooking(sc)) break;


                    case 2:
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


                        // PreparedStatement를 사용하여 SQL Injection을 방지하고 사용자 입력을 처리
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
                    case 3:
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

                        // 사용자로부터 회원 정보 입력 받기
                        while (true) {
                            System.out.println("===== 티켓 예약 =====");
                            // 경기정보 불러오기
                            int match = getMatchSelection(sc);
                            String teamName = "";
                            String matchDate = "";
                            int maxTickets = 0; // 각 팀별 최대 티켓 예약 수량 변수

                            switch (match) {
                                case 1:
                                    teamName = "맨체스터 유나이티드";
                                    maxTickets = MAX_ManUTickets;
                                    if (!checkTicketAvailability(stmt, matchDate, maxTickets)) continue;

                                    // 맨체스터 유나이티드의 경기 정보 조회 메서드 호출
                                    getManUTDMatchInfo(stmt);
                                    break;
                                case 2:
                                    teamName = "아스날";
                                    maxTickets = MAX_AsnTickets;
                                    if (!checkTicketAvailability(stmt, matchDate, maxTickets)) continue;
                                    break;
                                case 3:
                                    teamName = "첼시";
                                    maxTickets = MAX_CheTickets;
                                    if (!checkTicketAvailability(stmt, matchDate, maxTickets)) continue;
                                    break;
                                case 4:
                                    teamName = "리버풀";
                                    maxTickets = MAX_LivTickets;
                                    if (!checkTicketAvailability(stmt, matchDate, maxTickets)) continue;
                                    break;

                            }
                            String reservDate = getValidDate(sc);
                            if (reservDate == null) continue;


                            // 데이터베이스에 티켓 예약 정보 저장
                            addTicket(conn, stmt, Integer.parseInt(userId), teamName, reservDate, matchDate);
                            printTicketInfo(stmt, reservDate);

                            if (!continueBooking(sc)) break;

                        }
                }
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 리소스 해제
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            sc.close();
        }
    }
}
