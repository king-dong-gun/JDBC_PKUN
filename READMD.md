## PKUN_JDBC 미니 프로젝트

### 목차
- 1. 개요
- 2. 주요기능
- 구동 설명

#### 1. 개요

- 프로젝트 이름: 잉글랜드 프리미어리그 빅4팀 VIP 티켓 예메 서비스
- 프로젝트 설명: VIP 고객이 좀 더 수월하게 예약할 수 있도록 아이디와 날짜, 관람경기만 입력하면 티켓이 발권되는 프로그램

#### 2. 주요 기능
- 

#### 구동 설명

##### 코드 실행 순서

> 1. DataBase 테이블 생성

```oracle
-- 티켓 테이블 생성
CREATE TABLE VIPTICKET
(
    TICKET_ID NUMBER PRIMARY KEY, -- 기본키로 중복 설정 불가
    ID        NUMBER(4),
    TEAM      VARCHAR2(50),
    MATCHDATE VARCHAR2(50),
    LOCATION  VARCHAR2(50)
);
```

> 2. 시퀀스 생성

```oracle
CREATE SEQUENCE ticket_seq
    START WITH 1 -- 시작번호 1번 설정
    INCREMENT BY 1 -- 1씩 증가
    NOCACHE; -- 반복 없음
```

> 3. 데이터베이스 연동

```java
public class VipSoccerTicket {
    static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe"; // Oracle 서버 주소와 포트, SID에 맞게 수정

    static final String USER = "adam";
    static final String PASS = "1234";
```

```java
    public static void main(String[] args) {
    Connection conn = null;
    Statement stmt = null;
    try {
        Class.forName(JDBC_DRIVER);                 // 드라이버 로드
        conn = DriverManager.getConnection(DB_URL, USER, PASS);     // 데이터베이스 연결 설정
        stmt = conn.createStatement();              // SQL문 실행

```

![DB저장](https://github.com/king-dong-gun/PKUN_DB/assets/160683545/3988721f-5a92-43de-845e-9928e0b6b1fe)




> 4. 메소드문 작성

- 아이디 입력을 받는 메소드
    - 입력 4자리가 아니면 재입력
      ```java
      // 입력된 아이디가 숫자 4자리 형식에 맞는지 확인하는 메서드
      static boolean isValidTicketId(String ticketId) {
          String regex = "\\d{4}";      // 정규표현식 사용
          return Pattern.matches(regex, ticketId);  // 두값이 일치하면 true 반환
      ```
        - 아이디 중복을 허용하지 않음
        ```java
      // 입력된 아이디가 중복이 있는지 확인하는 메서드
      static boolean isTicketIdExists(Statement stmt, int ticketId) throws SQLException {
      String sql = "SELECT COUNT(*) AS COUNT FROM VIPTICKET WHERE ID=" + ticketId;  // vip테이블에서 id와 일치하는 행의 수를 세고 결과 반환 
      ResultSet rs = stmt.executeQuery(sql);
      rs.next();
      int count = rs.getInt("COUNT");       // 현재 행의 count 열값을 가져와 중복이 있는지 확인
      rs.close();
      return count > 0; // 존재하면 true로 반환
      }
      ```

![아이디입력](https://github.com/king-dong-gun/PKUN_DB/assets/160683545/0e8989d6-296a-4020-a626-5c6b267fc6c6)



- 경기날짜를 입력 받는 메소드
    - 입력된 날짜 MM/dd 형식이 아니면 재입력
    ```java
    // 입력된 날짜가 MM/dd 형식에 맞는지 확인하는 메서드
    static boolean isValidDate(String date) {
    String regex = "2024/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])"; // 정규표현식 사용
    return Pattern.matches(regex, date);        // 값이 일치하면 true 반환
    ```

![날짜입력](https://github.com/king-dong-gun/PKUN_DB/assets/160683545/7242aaeb-07be-49cd-96b9-c3462f474d38)


- 경기정보를 확인하는 메소드
    ```java
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
    ```

![경기정보](https://github.com/king-dong-gun/PKUN_DB/assets/160683545/75cff711-017d-4240-8a99-cd627e490e33)



- 경기별 최대 티켓 수량을 확인하는 메소드
    - 한 경기, 한 날짜 당 최대티켓 10장
  >   테이블에서 날짜 row 조회후 티켓 수량 확인
    ```java
    // 티켓수량을 데이터베이스에서 가져오는 메서드
    static int getTicketCount(Statement stmt, String date) throws SQLException {
        String sql = "SELECT COUNT(*) AS COUNT FROM VIPTICKET WHERE MATCHDATE = '" + date + "'";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        int count = rs.getInt("count");
        rs.close();
        return count;
    }    
    ```
  > 데이터 베이스에서 가져온 수량으로 최대티켓수량과 비교
    ```java
    // 최대 티켓 수량을 확인하는 메소드
    static boolean checkTicketAvailability(Statement stmt, String date, int maxTickets) throws SQLException {
    int ticketCount = getTicketCount(stmt, date); 
    if (ticketCount >= maxTickets) {
    System.out.println("이 날짜의 티켓 예매 가능 수량을 초과했습니다. 다른 날짜를 선택하세요.");
    return false;
    } 
    return true;
    }
    ```

![티켓수량초과](https://github.com/king-dong-gun/PKUN_DB/assets/160683545/5e487ebc-1065-4c3b-959c-89c17acba3d3)



- 예약정보를 확인하는 메소드
```java
    // 사용자에게 티켓 정보를 제공하는 메서드
    static ResultSet getTicket(Statement stmt, String team) throws SQLException {
        String sql = "SELECT * FROM VIPTICKET WHERE TEAM='" + team + "'";   // 팀 정보 가져오기
        return stmt.executeQuery(sql);
    }
```

```java
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
```


![예약정보](https://github.com/king-dong-gun/PKUN_DB/assets/160683545/6749620a-23d4-4df5-a6fc-b0cff9b27a90)



- 반복을 진행하는지 확인하는 메소드
```java
    // 반복을 진행하는지 묻는 메소드
    static boolean continueBooking(Scanner sc) {
        System.out.print("계속하려면 'Y'를 종료하려면 'N'을 입력하세요 >> ");
        String choice = sc.nextLine();
        return !choice.equalsIgnoreCase("N");  // 대소문자 구분없음
    }
```