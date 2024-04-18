-- VIPTICKET 테이블 생성
CREATE TABLE VIPTICKET
(
    ID          NUMBER PRIMARY KEY,
    TEAM        VARCHAR2(50),
    MATCH_DATE  VARCHAR2(50),
    MAX_TICKETS NUMBER DEFAULT 10 -- 최대 티켓 수량을 추적하는 열만 남깁니다.
);
--------------------------------------------------------------------------------
-- USERINFO 테이블 생성
CREATE TABLE USERINFO
(
    UserID      VARCHAR2(20) PRIMARY KEY,
    Password    VARCHAR2(20) UNIQUE NOT NULL ,
    Name        VARCHAR2(50) NOT NULL ,
    Email       VARCHAR2(100),
    PhoneNumber VARCHAR2(20)
);
--------------------------------------------------------------------------------
-- VIPTICKET, USERINFO 조인
SELECT USERINFO.UserID,
       USERINFO.Name,
       USERINFO.PhoneNumber,
       USERINFO.Email,
       VIPTICKET.TEAM,
       VIPTICKET.MATCH_DATE
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
VALUES ('맨체스터 유나이티드 vs 아스날', '올드 트레포드', '2024/05/13');
INSERT INTO HOMETEAM
VALUES ('맨체스터 유나이티드 vs 뉴캐슬', '올드 트레포드', '2024/05/16');
--------------------------------------------------------------------------------
INSERT INTO HOMETEAM
VALUES ('아스날 vs 첼시', '에미레이츠 스타디움', '2024/04/24');
INSERT INTO HOMETEAM
VALUES ('아스날 vs 본머스', '에미레이츠 스타디움', '2024/05/04');
INSERT INTO HOMETEAM
VALUES ('아스날 vs 에버튼', '에미레이츠 스타디움', '2024/05/20');
--------------------------------------------------------------------------------
INSERT INTO HOMETEAM
VALUES ('첼시 vs 토트넘 핫스퍼', '스탬포드 브릿지', '2024/05/03');
INSERT INTO HOMETEAM
VALUES ('첼시 vs 웨스트햄', '스탬포드 브릿지', '2024/05/05');
INSERT INTO HOMETEAM
VALUES ('첼시 vs 본머스', '스탬포드 브릿지', '2024/05/16');
--------------------------------------------------------------------------------
INSERT INTO HOMETEAM
VALUES ('리버풀 vs 토트넘', '안필드', '2024/05/06');
INSERT INTO HOMETEAM
VALUES ('리버풀 vs 울버햄튼', '안필드', '2024/05/20');
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
SELECT LAST_NUMBER
FROM ALL_SEQUENCES
WHERE SEQUENCE_NAME = 'TICKET_SEQ';
--------------------------------------------------------------------------------
-- VIPTICKET 테이블 데이터 삭제
TRUNCATE TABLE VIPTICKET;

---

drop TABLE USERINFO;








SELECT ID, TEAM, MATCH_DATE
FROM VIPTICKET
WHERE (TEAM, MATCH_DATE) IN (SELECT TEAM, MATCH_DATE FROM VIPTICKET);


