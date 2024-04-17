-- VIPTICKET 테이블 생성
CREATE TABLE VIPTICKET
(
    ID          NUMBER PRIMARY KEY,
    TEAM        VARCHAR2(50),
    LOCATION    VARCHAR2(50),
    MATCH_DATE  VARCHAR2(50),
    MAX_TICKETS NUMBER DEFAULT 10 -- 최대 티켓 수량을 추적하는 열만 남깁니다.
);
--------------------------------------------------------------------------------
-- USERINFO 테이블 생성
CREATE TABLE USERINFO
(
    UserID      VARCHAR(20) PRIMARY KEY,
    Password    VARCHAR2(20),
    Name        VARCHAR(50),
    Email       VARCHAR(100),
    PhoneNumber VARCHAR(20)
);
--------------------------------------------------------------------------------
-- VIPTICKET, USERINFO 조인
SELECT USERINFO.UserID,
       USERINFO.Name,
       USERINFO.PhoneNumber,
       USERINFO.Email,
       VIPTICKET.TEAM,
       VIPTICKET.MATCH_DATE,
       VIPTICKET.LOCATION
FROM VIPTICKET
         INNER JOIN USERINFO ON VIPTICKET.ID = USERINFO.UserID
WHERE USERINFO.UserID = '사용자 아이디';
--------------------------------------------------------------------------------
-- 시퀀스 생성
CREATE SEQUENCE ticket_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
--------------------------------------------------------------------------------
-- TEAM 테이블 생성
CREATE TABLE HOMETEAM
(
    TEAMID      INT PRIMARY KEY,
    TEAMNAME    VARCHAR2(255),
    HOMESTADIUM VARCHAR2(255),
    MATCH_DATE VARCHAR2(255)
);
--------------------------------------------------------------------------------
INSERT INTO HOMETEAM
VALUES ('맨체스터 유나이티드 vs 셰필드 유나이티드', '올드 트레포드', '2024/04/25');
INSERT INTO HOMETEAM
VALUES ('맨체스터 유나이티드 vs 번리', '올드 트레포드', '2024/04/27');
INSERT INTO HOMETEAM
VALUES ('크리스탈 펠리스 vs 맨체스터 유나이티드', '셀허스트파크', '2024/05/07');
INSERT INTO HOMETEAM
VALUES ('맨체스터 유나이티드 vs 아스날', '올드 트레포드', '2024/05/13');
INSERT INTO HOMETEAM
VALUES ('맨체스터 유나이티드 vs 뉴캐슬', '올드 트레포드', '2024/05/16');
INSERT INTO HOMETEAM
VALUES ('브라이턴 vs 맨체스터 유나이티드', '아메리칸 익스프레스', '2024/05/20');
--------------------------------------------------------------------------------
INSERT INTO HOMETEAM
VALUES ('울버햄튼 vs 아스날', '몰리뉴 스타디움', '2024/04/21');
INSERT INTO HOMETEAM
VALUES ('아스날 vs 첼시', '에미레이츠 스타디움', '2024/04/24');
INSERT INTO HOMETEAM
VALUES ('토트넘 핫스퍼 vs 아스날', '웸블리 스타디움', '2024/04/28');
INSERT INTO HOMETEAM
VALUES ('아스날 vs 본머스', '에메레이츠 스타디움', '2024/05/04');
INSERT INTO HOMETEAM
VALUES ('맨체스터 유나이티드 vs 아스날', '올드 트레포드', '2024/05/13');
INSERT INTO HOMETEAM
VALUES ('아스날 vs 에버튼', '에메레이츠 스타디움', '2024/05/20');
--------------------------------------------------------------------------------
INSERT INTO HOMETEAM
VALUES ('애스턴 빌라 vs 첼시', '빌라 파크', '2024/04/28');
INSERT INTO HOMETEAM
VALUES ('첼시 vs 토트넘 핫스퍼', '스탬포드 브릿지', '2024/05/03');
INSERT INTO HOMETEAM
VALUES ('첼시 vs 웨스트햄', '스탬포드 브릿지', '2024/05/05');
INSERT INTO HOMETEAM
VALUES ('노팅엄 포레스트 vs 첼시', '시티 그라운드', '2024/05/12');
INSERT INTO HOMETEAM
VALUES ('브라이튼 vs 첼시', '아메리칸 익스프레스', '2024/05/16');
INSERT INTO HOMETEAM
VALUES ('첼시 vs 본머스', '스탬포드 브릿지', '2024/05/16');
--------------------------------------------------------------------------------
INSERT INTO HOMETEAM
VALUES ('풀럼 vs 리버풀', '크레이븐 코티지', '2024/04/22');
INSERT INTO HOMETEAM
VALUES ('에버튼 vs 리버풀', '구디슨 파크', '2024/04/25');
INSERT INTO HOMETEAM
VALUES ('웨스트햄 vs 리버풀', '런던 스타디움', '2024/04/27');
INSERT INTO HOMETEAM
VALUES ('리버풀 vs 토트넘', '안필드', '2024/05/06');
INSERT INTO HOMETEAM
VALUES ('애스턴 빌라 vs 토트넘', '빌라 파크', '2024/05/14');
INSERT INTO HOMETEAM
VALUES ('리버풀 vs 울버햄튼', '안필드', '2024/05/20');
--------------------------------------------------------------------------------
-- 어웨이팀 테이블 생성
CREATE TABLE AWAYTEAM
(
    STADIUMID   INT PRIMARY KEY,
    STADIUMNAME VARCHAR(255),
    TEAMID      INT,
    FOREIGN KEY (TEAMID) REFERENCES HOMETEAM (TEAMID)
);
--------------------------------------------------------------------------------
-- 저장
COMMIT;

--------------------------------------------------------------------------------
SELECT *
FROM VIPTICKET;
--------------------------------------------------------------------------------
SELECT *
FROM HOMETEAM;
--------------------------------------------------------------------------------
SELECT *
FROM USERINFO;
--------------------------------------------------------------------------------
SELECT *
FROM ALL_SEQUENCES
WHERE SEQUENCE_NAME = 'TICKET_SEQ';

--------------------------------------------------------------------------------
SELECT LAST_NUMBER
FROM ALL_SEQUENCES
WHERE SEQUENCE_NAME = 'TICKET_SEQ';
--------------------------------------------------------------------------------
-- VIPTICKET 테이블 데이터 삭제
TRUNCATE TABLE VIPTICKET;

---
DROP TABLE team;
SELECT ID, TEAM, MATCH_DATE
FROM VIPTICKET
WHERE (TEAM, MATCH_DATE) IN (SELECT TEAM, MATCH_DATE FROM VIPTICKET);
