-- 회원 테이블 생성
CREATE TABLE MEMBER (
                        ID   NUMBER(4) PRIMARY KEY,
                        NAME VARCHAR2(50) NOT NULL,
                    CONSTRAINT CHK_ID_LENGTH CHECK (LENGTH(TO_CHAR(ID)) = 4)
);
INSERT INTO MEMBER VALUES (1997, 'RONALDO');
SELECT * FROM VIPTICKET;
-- 티켓 테이블 생성
CREATE TABLE VIPTICKET (
                           TICKET_ID NUMBER PRIMARY KEY,
                           ID NUMBER(4),
                           TEAM VARCHAR2(50),
                           MATCHDATE VARCHAR2(50),
                           LOCATION VARCHAR2(50),
                           CONSTRAINT FK_ID FOREIGN KEY (ID) REFERENCES MEMBER(ID),
                           CONSTRAINT CHK_TEAM CHECK (TEAM IN ('맨체스터 유나이티드', '아스날', '첼시', '리버풀'))
);

COMMIT ;
-- 시퀀스 생성
CREATE SEQUENCE ticket_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

SELECT *
FROM VIPTICKET;

SELECT *
FROM MEMBER;
commit;

DROP TABLE VIPTICKET;



DROP SEQUENCE ticket_seq;

-- 맴버테이블로 넘기는 메소드 작성
-- 예메 테이블 맴버와 티켓테이블 연동
