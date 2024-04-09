CREATE TABLE news
(
    id       NUMBER PRIMARY KEY,
    title    VARCHAR2(255) NOT NULL,
    content  varchar2(255) NOT NULL,
    category VARCHAR2(50)  NOT NULL
);

CREATE SEQUENCE news_seq
    START WITH 1
    INCREMENT BY 1
    NOMAXVALUE;

SELECT *
FROM news;

SELECT *
FROM news
WHERE category = '정치';
