--DROP TABLE AUDIT_DATA_T;
--DROP TABLE LOC_NUTS_T;
--DROP TABLE LOC_NUTS_T_SUPP_SUPPLIER_T;
--DROP TABLE LOC_LAU_T;
--DROP TABLE SEC_USERS_T;
--DROP TABLE SEC_ROLES_T;
--DROP TABLE SEC_RIGHTS_T;
--DROP TABLE SEC_USER_ROLES_T;
--DROP TABLE SEC_ROLE_RIGHTS_T;
--DROP TABLE SEQUENCE;
--DROP TABLE BEN_LGE_T;
--DROP TABLE BEN_MAY_T;
--DROP TABLE BEN_REP_T;
--DROP TABLE SEC_TEMPTOKEN_T;
--DROP TABLE SUPP_SUPPLIER_T;
--DROP TABLE SUPP_INSTALLATION_T;
--DROP TABLE SUPP_ACCESSPOINT_T;
--DROP TABLE TIM_TIMELINE_T;
--DROP TABLE HEL_HELPDESK_T;
--DROP TABLE HEL_HELPDESK_COMMENTS_T;
--DROP TABLE CALL_T;
--DROP TABLE SUPP_BENPUBSUP_T;
--DROP TABLE LEF;
--DROP TABLE BAF;
--DROP TABLE CONTACT_DETAILS_LEF;
--DROP TABLE CONTACT_DETAILS_BAF;
--DROP TABLE VALIDATED_LEF;
--DROP TABLE VALIDATED_BC;

CREATE TABLE "WIFI4EU_ABAC"."AUDIT_DATA_T"
( "audit_data_id" NUMBER(11,0) NOT NULL primary key, 
  "request_body" VARCHAR2(255 BYTE),
  "request_endpoint" VARCHAR2(255 BYTE),
  "request_method" VARCHAR2(255 BYTE),
  "response_body" VARCHAR2(255 BYTE),
  "user_id" NUMBER(11,0) not null
);

CREATE SEQUENCE SEQ_LOC_NUTS_T
  START WITH 1
  INCREMENT BY 1
  CACHE 100;

CREATE TABLE "WIFI4EU_ABAC"."LOC_NUTS_T"
( "NUTS_ID" NUMBER(5,0) NOT NULL primary key, 
  "NUTS_CODE" VARCHAR2(255 BYTE) NOT NULL, 
  "NUTS_LABEL" VARCHAR2(255 BYTE) NOT NULL, 
  "NUTS_LEVEL" NUMBER(5,0) NOT NULL, 
  "COUNTRY_CODE" VARCHAR2(255 BYTE) NOT NULL, 
  "ORDER" NUMBER(5,0) NOT NULL, 
  "SORTING" NUMBER(5,0) NOT NULL
);

CREATE OR REPLACE TRIGGER TRIG_LOC_NUTS_T
  BEFORE INSERT ON LOC_NUTS_T
  FOR EACH ROW
BEGIN
  SELECT SEQ_LOC_NUTS_T.nextval
    INTO :new.NUTS_ID
    FROM dual;
END;

CREATE TABLE "WIFI4EU_ABAC"."LOC_NUTS_T_SUPP_SUPPLIER_T"(	
    "NUTS_NUTS_ID" NUMBER(19,0), 
	"SUPPLIERS_SUPPLIER_ID" NUMBER(19,0)
);

CREATE SEQUENCE SEQ_LOC_LAU_T
  START WITH 1
  INCREMENT BY 1
  CACHE 100;
  
    
CREATE TABLE "WIFI4EU_ABAC"."LOC_LAU_T" (
  "LAU_ID"       NUMBER(5,0) NOT NULL primary key,
  "COUNTRY_CODE" VARCHAR2(255 BYTE) NOT NULL,
  "NUTS3"        VARCHAR2(255 BYTE) NOT NULL,
  "LAU1"         VARCHAR2(255 BYTE) NOT NULL,
  "LAU2"         VARCHAR2(255 BYTE) NOT NULL,
  "CHANGE"     VARCHAR2(255 BYTE) NOT NULL,
  "NAME1"        VARCHAR2(255 BYTE) NOT NULL,
  "NAME2"        VARCHAR2(255 BYTE) NOT NULL,
  "POP"          NUMBER(5,0) NOT NULL,
  "AREA"         NUMBER(5,0) NOT NULL
);
  
CREATE OR REPLACE TRIGGER TRIG_LOC_LAU_T
  BEFORE INSERT ON LOC_LAU_T
  FOR EACH ROW
BEGIN
  SELECT SEQ_LOC_LAU_T.nextval
    INTO :new.LAU_ID
    FROM dual;
END;

CREATE TABLE "WIFI4EU_ABAC"."SEC_USERS_T" (
  "USER_ID"      NUMBER(5,0) NOT NULL primary key,
  "EMAIL"        VARCHAR2(255 BYTE),
  "PASSWORD"     VARCHAR2(255 BYTE),
  "CREATE_DATE"  DATE,
  "ACCESS_DATE"  DATE,
  "USER_TYPE"    NUMBER(5,0) NOT NULL,
  "USER_TYPE_ID" NUMBER(5,0) NOT NULL
);

INSERT INTO "WIFI4EU_ABAC"."SEC_USERS_T" (USER_ID, EMAIL, PASSWORD, CREATE_DATE, ACCESS_DATE, USER_TYPE, USER_TYPE_ID)
VALUES (69, 'd1@example.com', '12345678', '2017-06-02', '2017-06-02', 5, 0);

INSERT INTO "WIFI4EU_ABAC"."SEC_USERS_T" (USER_ID, EMAIL, PASSWORD, CREATE_DATE, ACCESS_DATE, USER_TYPE, USER_TYPE_ID)
VALUES (70, 'test@wifi4eu.eu', 'test', TO_DATE('2017-06-02','YY/MM/DD'), TO_DATE('2017-06-02','YY/MM/DD'), 5, 0);

CREATE TABLE "WIFI4EU_ABAC"."SEC_ROLES_T" (
  "ROLE_ID" NUMBER(19,0) NOT NULL PRIMARY KEY,
  "NAME"    VARCHAR2(255 BYTE)
);

CREATE TABLE "WIFI4EU_ABAC"."SEC_RIGHTS_T" (
  "RIGHT_ID" NUMBER(19,0) NOT NULL PRIMARY KEY,
  "NAME"     VARCHAR2(255 BYTE)
);

CREATE TABLE "WIFI4EU_ABAC"."SEC_USER_ROLES_T" (	
    "ROLE_ID" NUMBER(19,0) NOT NULL, 
    "USER_ID" NUMBER(19,0) NOT NULL
);
   
CREATE TABLE "WIFI4EU_ABAC"."SEC_ROLE_RIGHTS_T" (	
    "RIGHT_ID" NUMBER(19,0) NOT NULL, 
    "ROLE_ID" NUMBER(19,0) NOT NULL
);
   
CREATE TABLE SEQUENCE (
  "SEQ_NAME"  VARCHAR2(255 BYTE) NOT NULL PRIMARY KEY,
  "SEQ_COUNT" NUMBER(38,0)
);
INSERT INTO SEQUENCE (SEQ_NAME, SEQ_COUNT) VALUES ('SEQ_GEN', 50);

CREATE TABLE "WIFI4EU_ABAC"."BEN_LGE_T" (
  "LEGAL_ENTITY_ID"   NUMBER(11,0) NOT NULL PRIMARY KEY,
  "COUNTRY_CODE"      VARCHAR2(255 BYTE),
  "MUNICIPALITY_CODE" VARCHAR2(255 BYTE),
  "ADDRESS"           VARCHAR2(255 BYTE),
  "ADDRESS_NUM"       VARCHAR2(255 BYTE),
  "POSTAL_CODE"       VARCHAR2(255 BYTE),
  "LEGAL_CHECKBOX_1"  VARCHAR2(1),
  "LEGAL_CHECKBOX_2"  VARCHAR2(1),
  "LEGAL_CHECKBOX_3"  VARCHAR2(1),
  "ABAC_STATUS"       VARCHAR2(1)
);

INSERT INTO BEN_LGE_T (LEGAL_ENTITY_ID, COUNTRY_CODE, MUNICIPALITY_CODE, ADDRESS, ADDRESS_NUM, POSTAL_CODE, LEGAL_CHECKBOX_1, LEGAL_CHECKBOX_2, LEGAL_CHECKBOX_3, ABAC_STATUS)
VALUES ('100', 'ES', 'ES511', 'Diagonal', '605', '08022', '1', '1', '1', '0');

CREATE TABLE "WIFI4EU_ABAC"."BEN_MAY_T" (
  "MAYOR_ID"        NUMBER(11,0) NOT NULL PRIMARY KEY,
  "TREATMENT"       VARCHAR2(255 BYTE),
  "NAME"            VARCHAR2(255 BYTE),
  "SURNAME"         VARCHAR2(255 BYTE),
  "EMAIL"           VARCHAR2(255 BYTE),
  "LEGAL_ENTITY_ID" NUMBER(11,0) NOT NULL
);

INSERT INTO BEN_MAY_T (MAYOR_ID, TREATMENT, NAME, SURNAME, EMAIL, LEGAL_ENTITY_ID)
VALUES (100, 'mr', 'John', 'Doe', 'b1@example.com', 100);

CREATE TABLE "WIFI4EU_ABAC"."BEN_REP_T" (
  "REPRESENTATIVE_ID" NUMBER(11,0) NOT NULL PRIMARY KEY,
  "TREATMENT"         VARCHAR2(255 BYTE),
  "NAME"              VARCHAR2(255 BYTE),
  "SURNAME"           VARCHAR2(255 BYTE),
  "MUNICIPALITY_ROLE" VARCHAR2(255 BYTE),
  "EMAIL"             VARCHAR2(255 BYTE),
  "MAYOR_ID"          NUMBER(11,0)
);

CREATE TABLE "WIFI4EU_ABAC"."SEC_TEMPTOKEN_T" (
  "ID"          NUMBER(11,0) NOT NULL PRIMARY KEY,
  "TOKEN"       VARCHAR2(255 BYTE),
  "EMAIL"       VARCHAR2(255 BYTE),
  "USER_ID"     NUMBER(11,0) NOT NULL,
  "CREATE_DATE" DATE,
  "EXPIRY_DATE" DATE
);

CREATE TABLE "WIFI4EU_ABAC"."SUPP_SUPPLIER_T" (
  "SUPPLIER_ID"          NUMBER(11,0) NOT NULL PRIMARY KEY,
  "NAME"                 VARCHAR2(255 BYTE),
  "ADDRESS"              VARCHAR2(255 BYTE),
  "VAT"                  VARCHAR2(255 BYTE),
  "BIC"                  VARCHAR2(255 BYTE),
  "ACCOUNT_NUMBER"       VARCHAR2(255 BYTE),
  "WEBSITE"              VARCHAR2(255 BYTE),
  "CONTACT_NAME"         VARCHAR2(255 BYTE),
  "CONTACT_SURNAME"      VARCHAR2(255 BYTE),
  "CONTACT_PHONE_PREFIX" VARCHAR2(255 BYTE),
  "CONTACT_PHONE_NUMBER" VARCHAR2(255 BYTE),
  "CONTACT_EMAIL"        VARCHAR2(255 BYTE),
  "LEGAL_CHECK1"         VARCHAR2(1),
  "LEGAL_CHECK2"         VARCHAR2(1),
  "CREATE_DATE"          DATE,
  "NUTS_IDS"             CLOB default EMPTY_CLOB(),
  "LOGO"                 CLOB default EMPTY_CLOB(),
  "ABAC_STATUS"          VARCHAR2(1)
);

INSERT INTO SUPP_SUPPLIER_T (SUPPLIER_ID, NAME, ADDRESS, VAT, BIC, ACCOUNT_NUMBER, WEBSITE, CONTACT_NAME, CONTACT_SURNAME, CONTACT_PHONE_PREFIX, CONTACT_PHONE_NUMBER, CONTACT_EMAIL, LEGAL_CHECK1, LEGAL_CHECK2, CREATE_DATE, NUTS_IDS, LOGO, ABAC_STATUS)
VALUES
  (100, 'Everis', 'Diagonal, 605', '0', '0', '0', 'http://everis.com', 'Everis', 'Diagonal, 605', '+34', '666666666',
   's1@example.com', '1', '1', TO_DATE('2017-05-01','YY/MM/DD'), 'ES,ES511', NULL, '0');
   
CREATE TABLE "WIFI4EU_ABAC"."SUPP_INSTALLATION_T" (
  "INSTALLATION_ID" INTEGER NOT NULL PRIMARY KEY,
  "NIP"             VARCHAR2(255 BYTE)
);

INSERT INTO SUPP_INSTALLATION_T (INSTALLATION_ID, NIP)
VALUES (111, '0');

CREATE TABLE "WIFI4EU_ABAC"."SUPP_ACCESSPOINT_T" (
  "ACCESSPOINT_ID"  NUMBER(11) NOT NULL PRIMARY KEY,
  "NAME"            VARCHAR2(255 BYTE),
  "SERIAL_NUMBER"   VARCHAR2(255 BYTE),
  "PRODUCT_NAME"    VARCHAR2(255 BYTE),
  "MODEL_NUMBER"    VARCHAR2(255 BYTE),
  "INSTALLATION_ID" NUMBER(11) NOT NULL,
  "INDOOR"          NUMBER(3)
);

INSERT INTO SUPP_ACCESSPOINT_T (ACCESSPOINT_ID, NAME, SERIAL_NUMBER, PRODUCT_NAME, MODEL_NUMBER, INSTALLATION_ID, INDOOR)
VALUES (112, 'AP-01', '0', 'Makita 1-1/4 HP Compact Router', 'RT0701C', 111, 1);

CREATE TABLE "WIFI4EU_ABAC"."TIM_TIMELINE_T" (
  "TIMELINE_ID" NUMBER(11,0) NOT NULL PRIMARY KEY,
  "START_DATE"  VARCHAR2(255 BYTE),
  "END_DATE"    VARCHAR2(255 BYTE),
  "EVENT"       VARCHAR2(255 BYTE)
);

INSERT INTO TIM_TIMELINE_T (TIMELINE_ID, START_DATE, END_DATE, EVENT)
VALUES (108, '1494055200000', '1493968800000', 'Opening competition');


CREATE TABLE "WIFI4EU_ABAC"."HEL_HELPDESK_T" (
  "ISSUE_ID"              NUMBER(11,0) NOT NULL,
  "ASSIGNED_TO"           VARCHAR2(255 BYTE),
  "CREATE_DATE"           DATE,
  "DG_CONNECT_COMMENTS"   VARCHAR2(255 BYTE),
  "FROM_EMAIL"            VARCHAR2(255 BYTE),
  "ISSUE_SUMMARY"         VARCHAR2(255 BYTE),
  "MEMBER_STATE"          VARCHAR2(255 BYTE),
  "MEMBER_STATE_COMMENTS" VARCHAR2(255 BYTE),
  "PORTAL"                VARCHAR2(255 BYTE),
  "STATUS"                VARCHAR2(255 BYTE),
  "TOPIC"                 VARCHAR2(255 BYTE)
);

INSERT INTO HEL_HELPDESK_T (ISSUE_ID, ASSIGNED_TO, CREATE_DATE, DG_CONNECT_COMMENTS, FROM_EMAIL, ISSUE_SUMMARY, MEMBER_STATE, MEMBER_STATE_COMMENTS, PORTAL, STATUS, TOPIC)
VALUES (115, 'Member State', TO_DATE('2017-05-01','YY/MM/DD'), NULL, 'b1@example.com', 'Test', NULL, NULL, NULL, 'Pending', 'Test');

CREATE TABLE "WIFI4EU_ABAC"."HEL_HELPDESK_COMMENTS_T" (
  "COMMENT_ID"    NUMBER(11,0) NOT NULL,
  "TYPE"          VARCHAR2(255 BYTE),
  "COMMENT"       VARCHAR2(255 BYTE),
  "COMMENT_DATE"  DATE,
  "ISSUE_ID"      NUMBER(11,0)
);

CREATE TABLE "WIFI4EU_ABAC"."CALL_T" (
  "CALL_ID"    NUMBER(11,0) NOT NULL PRIMARY KEY,
  "END_DATE"   VARCHAR2(255 BYTE),
  "EVENT"      VARCHAR2(255 BYTE),
  "START_DATE" VARCHAR2(255 BYTE)
);

INSERT INTO CALL_T (CALL_ID, END_DATE, EVENT, START_DATE)
VALUES (201, '1494055200000', 'Wifi for Europeans 2017', '1493968800000');

CREATE TABLE "WIFI4EU_ABAC"."SUPP_BENPUBSUP_T" (
  "BENPUBSUP_ID" NUMBER(11,0) NOT NULL PRIMARY KEY,
  "BENEFICIARY_ID" NUMBER(11,0) NOT NULL,
  "PUBLICATION_ID" NUMBER(11,0) NOT NULL,
  "AWARDED" NUMBER(3,0) NOT NULL,
  "SUPPLIER_ID" NUMBER(11,0) DEFAULT NULL,
  "DATE" DATE,
  "ABAC_STATUS" VARCHAR2(1),
  "LEF_EXPORT" NUMBER(20,0) NOT NULL,
  "LEF_IMPORT" NUMBER(20,0) NOT NULL,
  "LEF_STATUS" NUMBER(11,0) NOT NULL,
  "BC_EXPORT" NUMBER(20,0) NOT NULL,
  "BC_IMPORT" NUMBER(20,0) NOT NULL,
  "BC_STATUS" NUMBER(11,0) NOT NULL,
  "LC_EXPORT" NUMBER(20,0) NOT NULL,
  "LC_IMPORT" NUMBER(20,0) NOT NULL,
  "LC_STATUS" NUMBER(11,0) NOT NULL,
  "LAST_ABAC_MESSAGE" VARCHAR2(255 BYTE)
);

INSERT INTO SUPP_BENPUBSUP_T (BENPUBSUP_ID, BENEFICIARY_ID, PUBLICATION_ID, AWARDED, SUPPLIER_ID, "DATE", ABAC_STATUS, LEF_EXPORT, LEF_IMPORT, LEF_STATUS, BC_EXPORT, BC_IMPORT, BC_STATUS, LC_EXPORT, LC_IMPORT, LC_STATUS, LAST_ABAC_MESSAGE)
VALUES (111, 100, 201, 1, 100, TO_DATE('2017-05-01','YY/MM/DD'),'1',1,1,1,1,1,1,1,1,1,'Hello');

CREATE TABLE "WIFI4EU_ABAC"."LEF" (
  "idLef" NUMBER(16,0) NOT NULL PRIMARY KEY,
  "idContact" NUMBER(11,0) NOT NULL,
  "municipality" VARCHAR2(255 BYTE) NOT NULL,
  "address" VARCHAR2(255 BYTE) NOT NULL,
  "number" NUMBER(11,0) NOT NULL,
  "postalCode" VARCHAR2(255 BYTE) NOT NULL,
  "name" VARCHAR2(255 BYTE) NOT NULL,
  "surname" VARCHAR2(255 BYTE) NOT NULL,
  "emailAddress" VARCHAR2(255 BYTE) NOT NULL,
  "country" VARCHAR2(255 BYTE) NOT NULL
);

CREATE TABLE "WIFI4EU_ABAC"."BAF" (
  "idBaf" NUMBER(11,0) NOT NULL PRIMARY KEY,
  "idContact" NUMBER(11,0)NOT NULL,
  "companyName" VARCHAR2(255 BYTE) NOT NULL,
  "officialAddress" VARCHAR2(255 BYTE) NOT NULL,
  "vatNumber" VARCHAR2(255 BYTE) NOT NULL,
  "bic" VARCHAR2(255 BYTE) NOT NULL,
  "IBAN" VARCHAR2(255 BYTE) NOT NULL,
  "companyWebsite" VARCHAR2(255 BYTE) NOT NULL,
  "country" VARCHAR2(255 BYTE) NOT NULL,
  "area" VARCHAR2(255 BYTE) NOT NULL
);

CREATE TABLE "WIFI4EU_ABAC"."CONTACT_DETAILS_LEF" (
  "id" NUMBER(11,0) NOT NULL PRIMARY KEY,
  "name" VARCHAR2(255 BYTE) NOT NULL,
  "surname" VARCHAR2(255 BYTE) NOT NULL,
  "emailAddress" VARCHAR2(255 BYTE) NOT NULL,
  "address" VARCHAR2(255 BYTE) NOT NULL,
  "number" NUMBER(11,0) NOT NULL,
  "postalCode" VARCHAR2(255 BYTE) NOT NULL
);

CREATE TABLE "WIFI4EU_ABAC"."CONTACT_DETAILS_BAF" (
  "id" NUMBER(11,0) NOT NULL PRIMARY KEY,
  "companyName" VARCHAR2(255 BYTE) NOT NULL,
  "name" VARCHAR2(255 BYTE) NOT NULL,
  "surname" VARCHAR2(255 BYTE) NOT NULL,
  "phoneNumber" NUMBER(11,0) NOT NULL,
  "emailAddress" VARCHAR2(255 BYTE) NOT NULL
);

CREATE TABLE "WIFI4EU_ABAC"."VALIDATED_LEF" (
  "idLef" NUMBER(16,0) NOT NULL PRIMARY KEY,
  "STATUS" VARCHAR2(10 BYTE)
);

CREATE TABLE "WIFI4EU_ABAC"."VALIDATED_BC" (
  "idBc" NUMBER(11,0) NOT NULL PRIMARY KEY,
  "STATUS" VARCHAR2(10 BYTE)
);

CREATE TABLE "WIFI4EU_ABAC"."MUNICIPALITY" 
   (           "ID" NUMBER(10,0) NOT NULL PRIMARY KEY, 
                "NAME" VARCHAR2(100 CHAR) NOT NULL, 
                "ADDRESS" VARCHAR2(100 CHAR), 
                "ADDRESS_NUM" VARCHAR2(100 CHAR), 
                "POSTAL_CODE" VARCHAR2(100 CHAR), 
                "COUNTRY" VARCHAR2(100 CHAR), 
                "JAGATE_KEY" VARCHAR2(100 CHAR), 
                "FEL_ID" VARCHAR2(100 CHAR)
   ) ;
   
CREATE SEQUENCE  "WIFI4EU_ABAC"."SEQ_MUNICIPALITY"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 
INCREMENT BY 1 START WITH 10 
NOCACHE  NOORDER  NOCYCLE ;

Insert into WIFI4EU_ABAC.VALIDATED_LEF ("idLef",STATUS) values ('1','(mod/0=0)');

Insert into WIFI4EU_ABAC.VALIDATED_BC ("idBc",STATUS) values ('1','(mod/0=0)');