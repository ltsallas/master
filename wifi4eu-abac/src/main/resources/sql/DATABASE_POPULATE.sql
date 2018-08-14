REM INSERTING into WIF_CONSTANTS
SET DEFINE OFF;
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (21,'LEF_V_LOC_DOC_SCAN_TABLE_ALIAS','SCANNED DOCUMENT');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (1,'ABAC_SYS_CD','WIF');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (2,'LEF_V_LOC_DEST','ABACBUDT');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (3,'LEF_V_LOC_TRANS_AREA_CD','THP');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (4,'LEF_V_LOC_TRANS_TP_CD','THP');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (5,'LEF_V_LOC_TRANS_ACTION_CD','CREATE');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (6,'LEF_V_LOC_TABLE_ALIAS','LEGAL ENTITY');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (7,'LEF_V_LOC_DEBTOR_CAT_ID','3');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (8,'LEF_V_LOC_LEGAL_TYPE_CD','PULB');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (9,'LEF_V_LOC_RESP_ORG_STRUCT_KEY_CD','\\EU\CE\INEA');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (10,'LEF_V_LOC_RESP_ORG_STRUCT_TP_CD','EGY');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (11,'LEF_V_LOC_DOC_TABLE_ALIAS','DOCUMENT REFERENCE');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (12,'LEF_V_LOC_VISA_TABLE_ALIAS','VISA');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (13,'LEF_V_LOC_VISA_ACTION_CD','AC');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (14,'LEF_V_LOC_VISA_SIGNATURE','PANAGIOTOPOULOS Alexander');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (15,'LEF_V_LOC_VISA_DEFAULT_COMMENT','VISA GIVEN BY WIFI4EU');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (16,'LEF_V_LOC_VISA_AGENT_ID','panaale');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (17,'LEF_V_LOC_VISA_WRKFLW_CENTER_CD','WIFI4EU');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (18,'LEF_V_LOC_VISA_WRKFLW_ORG_NAME','INEA');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (19,'LEF_V_LOC_VISA_SIGN_AS_AGENT_TYPE','FIA');
Insert into WIF_CONSTANTS (ID,NAME,VALUE) values (20,'LEF_V_LOC_VISA_SUPPLIED_AGENT_NAME','panaale');


REM INSERTING into WIFI4EU_ABAC.WIF_COUNTRY
SET DEFINE OFF;
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('CY','Cyprus',20000871,'Kypros/Κύπρος/Kıbrıs','CYP');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('EE','Estonia',20000880,'Eesti','EST');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('LT','Lithuania',20000944,'Lietuva','LTU');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('MT','Malta',20000960,'Malta','MLT');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('AT','Austria',20000832,'Österreich','AUT');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('BG','Bulgaria',20000841,'Bulgariya or Bălgarija/България','BGR');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('HR','Croatia',20000911,'Hrvatska','HRV');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('CZ','Czech Republic',20000872,'Ceská republika/Cesko','CZE');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('DK','Denmark',20000875,'Danmark','DNK');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('BE','Belgium',20000839,'België/Belgique/Belgien','BEL');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('DE','Germany',20000873,'Deutschland','DEU');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('SE','Sweden',20001001,'Sverige','SWE');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('IT','Italy',20000922,'Italia','ITA');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('ES','Spain',20000883,'España','ESP');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('FI','Finland',20000885,'Suomi/Finland','FIN');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('HU','Hungary',20000913,'Magyarország','HUN');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('IE','Ireland',20000915,'Éire/Ireland','IRL');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('FR','France',20000890,'France','FRA');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('LV','Latvia',20000946,'Latvija','LVA');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('NL','Netherlands',20000973,'Nederland','NLD');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('PT','Portugal',20000990,'Portugal','PRT');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('RO','Romania',20000994,'România','ROU');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('SI','Slovenia',20001004,'Slovenija','SVN');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('GB','United Kingdom',20000893,'United Kingdom','GBR');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('SK','Slovakia',20001005,'Slovensko','SVK');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('GR','Greece',20000902,'Hellas/Ελλάς or Ellada/Ελλάδα','GRC');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('PL','Poland',20000986,'Polska','POL');
Insert into WIFI4EU_ABAC.WIF_COUNTRY (ISO2_CODE,NAME,CCM2_CODE,NATIVE_DESCRIPTIONS,ISO3_CODE) values ('LU','Luxembourg',20000945,'Lëtzebuerg/Luxemburg/Luxembourg','LUX');