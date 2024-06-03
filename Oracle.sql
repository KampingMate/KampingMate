ALTER SESSION SET "_ORACLE_SCRIPT"=true;
-- 테이블 스페이스 생성
CREATE TABLESPACE myts DATAFILE 'E:\oradb\myts.dbf' SIZE 100M AUTOEXTEND ON NEXT 5M;
-- 스프링 부트 사용자 생성
CREATE USER kamp IDENTIFIED BY kamp
DEFAULT TABLESPACE myts;

-- 스프링 부트 사용자 계정에 권한 부여
GRANT CONNECT, RESOURCE TO kamp;
GRANT CREATE VIEW TO kamp;

--boot_user에 대한 테이블 스페이스 공간 할당
alter user kamp default tablespace myts quota unlimited on myts; 
