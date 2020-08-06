--/*==============================================================*/
--/* DBMS name:      MySQL 5.0                                    */
--/* Created on:     11/18/2009 9:31:15 AM                        */
--/*==============================================================*/


--/* =============================================================*/ 
--/* drop database if exists bibliography;							*/
--/* create database bibliography default character set utf8;			*/
--/* use bibliography; 												*/
--/* ================================================================*/ 

--/*==============================================================*/
--/* Table: CFCLASS                                               */
--/*==============================================================*/
create table CFCLASS
(
   CFCLASSSCHEMEID      varchar(32) not null,
   CFCLASSID            varchar(255) not null,
   CFSTARTDATE          datetime not null,
   CFENDDATE            datetime not null,
   CFURI                varchar(128),
   primary key (CFCLASSSCHEMEID, CFCLASSID)
);

--/*==============================================================*/
--/* Table: CFCLASSDESCRIPTION                                    */
--/*==============================================================*/
create table CFCLASSDESCRIPTION
(
   CFCLASSSCHEMEID      varchar(32) not null,
   CFCLASSID            varchar(255) not null,
   CFLANGCODE           varchar(5) not null,
   CFTRANS              varchar(1) not null,
   CFDESCRIPTION        text not null,
   primary key (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS)
);

--/*==============================================================*/
--/* Table: CFCLASSSCHEME                                         */
--/*==============================================================*/
create table CFCLASSSCHEME
(
   CFCLASSSCHEMEID      varchar(32) not null,
   CFURI                varchar(128),
   primary key (CFCLASSSCHEMEID)
);

--/*==============================================================*/
--/* Table: CFCLASSSCHEMEDESCRIPTION                              */
--/*==============================================================*/
create table CFCLASSSCHEMEDESCRIPTION
(
   CFCLASSSCHEMEID      varchar(32) not null,
   CFLANGCODE           varchar(5) not null,
   CFTRANS              varchar(1) not null,
   CFDESCRIPTION        text not null,
   primary key (CFCLASSSCHEMEID, CFLANGCODE, CFTRANS)
);

--/*==============================================================*/
--/* Table: CFCLASSSCHEME_CLASSSCHEME                             */
--/*==============================================================*/
create table CFCLASSSCHEME_CLASSSCHEME
(
   CFCLASSSCHEMEID1     varchar(32) not null,
   CFCLASSSCHEMEID2     varchar(32) not null,
   CFCLASSSCHEMEID      varchar(32) not null,
   CFCLASSID            varchar(255) not null,
   CFSTARTDATE          datetime not null,
   CFENDDATE            datetime not null,
   primary key (CFCLASSSCHEMEID, CFCLASSSCHEMEID1, CFCLASSSCHEMEID2, CFCLASSID, CFSTARTDATE, CFENDDATE)
);

--/*==============================================================*/
--/* Table: CFCLASSTERM                                           */
--/*==============================================================*/
create table CFCLASSTERM
(
   CFCLASSSCHEMEID      varchar(32) not null,
   CFCLASSID            varchar(255) not null,
   CFLANGCODE           varchar(5) not null,
   CFTRANS              varchar(1) not null,
   CFTERM               varchar(255) not null,
   primary key (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS)
);

--/*==============================================================*/
--/* Table: CFCLASS_CLASS                                         */
--/*==============================================================*/
create table CFCLASS_CLASS
(
   CFCLASSSCHEMEID1     varchar(32) not null,
   CFCLASSID1           varchar(255) not null,
   CFCLASSSCHEMEID2     varchar(32) not null,
   CFCLASSID2           varchar(255) not null,
   CFCLASSSCHEMEID      varchar(32) not null,
   CFCLASSID            varchar(255) not null,
   CFSTARTDATE          datetime not null,
   CFENDDATE            datetime not null,
   primary key (CFCLASSSCHEMEID, CFCLASSSCHEMEID2, CFCLASSSCHEMEID1, CFCLASSID1, CFCLASSID2, CFCLASSID, CFSTARTDATE, CFENDDATE)
);

--/*==============================================================*/
--/* Table: CFCURRENCY                                            */
--/*==============================================================*/
create table CFCURRENCY
(
   CFCURRCODE           varchar(3) not null,
   CFNUMCURRCODE        varchar(3),
   CFURI                varchar(128),
   primary key (CFCURRCODE)
);

--/*==============================================================*/
--/* Table: CFLANGUAGE                                            */
--/*==============================================================*/
create table CFLANGUAGE
(
   CFLANGCODE           varchar(5) not null,
   CFURI                varchar(128),
   primary key (CFLANGCODE)
);

--/*==============================================================*/
--/* Table: CFMORGUNIT                                            */
--/*==============================================================*/
create table CFMORGUNIT
(
   ORGUNITRECORDID      varchar(50) not null,
   CFMCURRCODE          varchar(3),
   CFMTURN              float,
   CFMHEADCOUNT         int(11),
   primary key (ORGUNITRECORDID)
);

--/*==============================================================*/
--/* Table: COUNTER                                               */
--/*==============================================================*/
create table COUNTER
(
   COUNTERNAME          varchar(255) not null,
   COUNTERVALUE         int(11) not null,
   primary key (COUNTERNAME)
);

/*==============================================================*/
/* Table: FILE_STORAGE                                          */
/*==============================================================*/
create table FILE_STORAGE
(
   ID                   int not null AUTO_INCREMENT,
   RECORDID             varchar(50) not null,
   FILENAME             varchar(100),
   MIME                 varchar(150),
   UPLOADER             varchar(50),
   LENGTH               bigint,
   primary key (ID)
);

/*==============================================================*/
/* Table: REGISTER                                          */
/*==============================================================*/
create table REGISTER
(
   REGISTERID               int(11) not null,
   NAME                     text,
   FATHERNAME               text,
   MOTHERNAME               text,
   FATHERLASTNAME           text,
   MOTHERLASTNAME           text,
   GUARDIANSFULLNAME         text,
   BIRTHDATE                text,
   BIRTHPLACE               text,
   BIRTHCITY                text,
   BIRTHCOUNTRY             text,
   EDUCATIONHISTORY         text,
   SCIENTIFICTITLEHISTORY   text,
   TITLE                    text,
   FACULTY                  text,
   ADVISOR                  text,
   COMMISSION               text,
   DEFENDEDON               text,
   NAMEOFAUTHORDEGREE       text,
   PROMOTIONDATE            text,
   DIPLOMANUMBER            text,
   SUPPLEMENTNUMBER         text,
   DIPLOMAPUBLICATIONDATE   text,
   SUPPLEMENTPUBLICATIONDATE  text,
   RECORDID                 varchar(50) not null,
   CREATOR                  varchar(50),
   primary key (REGISTERID)
);

--/*==============================================================*/
--/* Table: MARC21RECORD                                          */
--/*==============================================================*/
create table MARC21RECORD
(
   RECORDID             varchar(50) not null,
   TYPE                 varchar(100) not null,
   RECORDSTRING         text not null,
   CREATOR              varchar(255),
   MODIFIER             varchar(255),
   DATECREATED          datetime,
   DATEMODIFIED         datetime,
   ARCHIVED             int(11),
   INUSEBY              varchar(255),
   primary key (RECORDID)
);

/*==============================================================*/
/* Table: PERSON                                                */
/*==============================================================*/
create table PERSON
(
   RECORDID             varchar(50) not null,
   JMBG                 varchar(25),
   DIRECTPHONES         varchar(50),
   LOCALPHONES          varchar(50),
   primary key (RECORDID)
);

--/*==============================================================*/
--/* Table: MARC21RECORD_CLASS                                    */
--/*==============================================================*/
create table MARC21RECORD_CLASS
(
   RECORDID             varchar(50) not null,
   CFCLASSSCHEMEID      varchar(32) not null,
   CFCLASSID            varchar(255) not null,
   CFSTARTDATE          datetime not null,
   CFENDDATE            datetime not null,
   primary key (CFCLASSSCHEMEID, RECORDID, CFCLASSID, CFSTARTDATE, CFENDDATE)
);

--/*==============================================================*/
--/* Table: MARC21RECORD_KEYWORDS                                 */
--/*==============================================================*/
create table MARC21RECORD_KEYWORDS
(
   RECORDID             varchar(50) not null,
   CFLANGCODE           varchar(5) not null,
   CFTRANS              varchar(1) not null,
   CFKEYWORDS           varchar(255),
   primary key (RECORDID, CFLANGCODE, CFTRANS)
);

--/*==============================================================*/
--/* Table: MARC21RECORD_MARC21RECORD                             */
--/*==============================================================*/
create table MARC21RECORD_MARC21RECORD
(
   RECORDID1            varchar(50) not null,
   RECORDID2            varchar(50) not null,
   CFCLASSSCHEMEID      varchar(32) not null,
   CFCLASSID            varchar(255) not null,
   CFSTARTDATE          datetime not null,
   CFENDDATE            datetime not null,
   primary key (CFCLASSSCHEMEID, RECORDID1, RECORDID2, CFCLASSID, CFSTARTDATE, CFENDDATE)
);

--/*==============================================================*/
--/* Table: MARC21RECORD_RESPUBL                                  */
--/*==============================================================*/
create table MARC21RECORD_RESPUBL
(
   RECORDID             varchar(50) not null,
   RESPUBLRECORDID      varchar(50) not null,
   CFCLASSSCHEMEID      varchar(32) not null,
   CFCLASSID            varchar(255) not null,
   CFSTARTDATE          datetime not null,
   CFENDDATE            datetime not null,
   CFCOPYRIGHT          varchar(64),
   primary key (CFCLASSSCHEMEID, RECORDID, RESPUBLRECORDID, CFCLASSID, CFSTARTDATE, CFENDDATE)
);

/*==============================================================*/
/* Table: CFMETRICS                                             */
/*==============================================================*/
create table CFMETRICS
(
   CFMETRICSID          varchar(32) not null,
   CFURI                varchar(128),
   primary key (CFMETRICSID)
);

/*==============================================================*/
/* Table: CFMETRICSDESCRIPTION                                  */
/*==============================================================*/
create table CFMETRICSDESCRIPTION
(
   CFLANGCODE           varchar(5) not null,
   CFMETRICSID          varchar(32) not null,
   CFDESCRIPTION        text not null,
   primary key (CFLANGCODE, CFMETRICSID)
);

/*==============================================================*/
/* Table: CFMETRICSNAME                                         */
/*==============================================================*/
create table CFMETRICSNAME
(
   CFLANGCODE           varchar(5) not null,
   CFMETRICSID          varchar(32) not null,
   CFNAME              varchar(255) not null,
   primary key (CFLANGCODE, CFMETRICSID)
);

/*==============================================================*/
/* Table: CFMETRICS_CLASS                                       */
/*==============================================================*/
create table CFMETRICS_CLASS
(
   CFMETRICSID          varchar(32) not null,
   CFCLASSSCHEMEID varchar(32) not null,
   CFCLASSID      varchar(255) not null,
   CFSTARTDATE          datetime not null,
   CFENDDATE            datetime not null,
   primary key (CFCLASSSCHEMEID, CFCLASSID, CFMETRICSID, CFSTARTDATE, CFENDDATE)
);

/*==============================================================*/
/* Table: MARC21RECORD_METRICS                                  */
/*==============================================================*/
create table MARC21RECORD_METRICS
(
   RECORDID             varchar(50) not null,
   CFMETRICSID          varchar(32) not null,
   CFCLASSSCHEMEID      varchar(32) not null,
   CFCLASSID            varchar(255) not null,
   CFYEAR               int not null,
   CFCOUNT              double,
   CFFRACTION           double,
   primary key (RECORDID, CFMETRICSID, CFCLASSSCHEMEID, CFCLASSID, CFYEAR)
);

--/*==============================================================*/
--/* Table: USER                                                  */
--/*==============================================================*/
create table USER
(
   EMAIL                varchar(50) not null,
   RECORDID             varchar(50),
   PASSWORD             varchar(50) not null,
   LANGUAGE             varchar(6) not null,
   TYPE                 varchar(50) not null,
   NOTE                 text,
   primary key (EMAIL)
);

--/*==============================================================*/
--/* Index: INDEX_4                                               */
--/*==============================================================*/
create unique index INDEX_4 on USER
(
   RECORDID
);

alter table CFCLASS add constraint FK_RELATIONSHIP_1 foreign key (CFCLASSSCHEMEID)
      references CFCLASSSCHEME (CFCLASSSCHEMEID) on delete cascade on update cascade;

alter table CFCLASSDESCRIPTION add constraint FK_RELATIONSHIP_10 foreign key (CFCLASSSCHEMEID, CFCLASSID)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table CFCLASSDESCRIPTION add constraint FK_RELATIONSHIP_221 foreign key (CFLANGCODE)
      references CFLANGUAGE (CFLANGCODE) on delete cascade on update cascade;

alter table CFCLASSSCHEMEDESCRIPTION add constraint FK_RELATIONSHIP_12 foreign key (CFCLASSSCHEMEID)
      references CFCLASSSCHEME (CFCLASSSCHEMEID) on delete cascade on update cascade;

alter table CFCLASSSCHEMEDESCRIPTION add constraint FK_RELATIONSHIP_13 foreign key (CFLANGCODE)
      references CFLANGUAGE (CFLANGCODE) on delete cascade on update cascade;

alter table CFCLASSSCHEME_CLASSSCHEME add constraint FK_RELATIONSHIP_2 foreign key (CFCLASSSCHEMEID1)
      references CFCLASSSCHEME (CFCLASSSCHEMEID) on delete cascade on update cascade;

alter table CFCLASSSCHEME_CLASSSCHEME add constraint FK_RELATIONSHIP_3 foreign key (CFCLASSSCHEMEID2)
      references CFCLASSSCHEME (CFCLASSSCHEMEID) on delete cascade on update cascade;

alter table CFCLASSSCHEME_CLASSSCHEME add constraint FK_RELATIONSHIP_4 foreign key (CFCLASSSCHEMEID, CFCLASSID)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table CFCLASSTERM add constraint FK_RELATIONSHIP_8 foreign key (CFCLASSSCHEMEID, CFCLASSID)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table CFCLASSTERM add constraint FK_RELATIONSHIP_9 foreign key (CFLANGCODE)
      references CFLANGUAGE (CFLANGCODE) on delete cascade on update cascade;

alter table CFCLASS_CLASS add constraint FK_RELATIONSHIP_5 foreign key (CFCLASSSCHEMEID1, CFCLASSID1)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table CFCLASS_CLASS add constraint FK_RELATIONSHIP_6 foreign key (CFCLASSSCHEMEID2, CFCLASSID2)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table CFCLASS_CLASS add constraint FK_RELATIONSHIP_7 foreign key (CFCLASSSCHEMEID, CFCLASSID)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table CFMORGUNIT add constraint FK_INHERITANCE_12 foreign key (ORGUNITRECORDID)
      references MARC21RECORD (RECORDID) on delete cascade on update cascade;

alter table CFMORGUNIT add constraint FK_REFERENCE_225 foreign key (CFMCURRCODE)
      references CFCURRENCY (CFCURRCODE) on delete cascade on update cascade;

alter table MARC21RECORD_CLASS add constraint FK_RELATIONSHIP_22 foreign key (RECORDID)
      references MARC21RECORD (RECORDID) on delete cascade on update cascade;

alter table MARC21RECORD_CLASS add constraint FK_RELATIONSHIP_28 foreign key (CFCLASSSCHEMEID, CFCLASSID)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table MARC21RECORD_KEYWORDS add constraint FK_RELATIONSHIP_222 foreign key (RECORDID)
      references MARC21RECORD (RECORDID) on delete cascade on update cascade;

alter table MARC21RECORD_KEYWORDS add constraint FK_RELATIONSHIP_223 foreign key (CFLANGCODE)
      references CFLANGUAGE (CFLANGCODE) on delete cascade on update cascade;

alter table MARC21RECORD_MARC21RECORD add constraint FK_RELATIONSHIP_29 foreign key (RECORDID1)
      references MARC21RECORD (RECORDID) on delete cascade on update cascade;

alter table MARC21RECORD_MARC21RECORD add constraint FK_RELATIONSHIP_30 foreign key (RECORDID2)
      references MARC21RECORD (RECORDID) on delete cascade on update cascade;

alter table MARC21RECORD_MARC21RECORD add constraint FK_RELATIONSHIP_31 foreign key (CFCLASSSCHEMEID, CFCLASSID)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table MARC21RECORD_RESPUBL add constraint FK_INHERITANCE_4 foreign key (CFCLASSSCHEMEID, RECORDID, RESPUBLRECORDID, CFCLASSID, CFSTARTDATE, CFENDDATE)
      references MARC21RECORD_MARC21RECORD (CFCLASSSCHEMEID, RECORDID1, RECORDID2, CFCLASSID, CFSTARTDATE, CFENDDATE) on delete cascade on update cascade;

alter table USER add constraint FK_RELATIONSHIP_226 foreign key (RECORDID)
      references MARC21RECORD (RECORDID) on delete cascade on update cascade;

alter table PERSON add constraint FK_INHERITANCE_14 foreign key (RECORDID)
      references MARC21RECORD (RECORDID) on delete cascade on update cascade;

alter table FILE_STORAGE add constraint FK_REFERENCE_226 foreign key (UPLOADER)
      references USER (EMAIL) on delete cascade on update cascade;

alter table FILE_STORAGE add constraint FK_REFERENCE_227 foreign key (RECORDID)
      references MARC21RECORD (RECORDID) on delete cascade on update cascade;
      
alter table REGISTER add constraint FK_REFERENCE_300 foreign key (RECORDID)
      references MARC21RECORD (RECORDID) on delete cascade on update cascade;
      
alter table REGISTER add constraint FK_REFERENCE_301 foreign key (CREATOR)
      references USER (EMAIL) on delete cascade on update cascade;

/*==============================================================*/
/* Table: COMMISSION                                            */
/*==============================================================*/
create table COMMISSION
(
   COMMISSIONID         int(11) not null,
   APPOINTMENTBOARD     text null,
   APPOINTMENTDATE      datetime null,
   MEMBERS              text null,
   CFCLASSSCHEMEIDSCIENCEAREA varchar(32) not null,
   CFCLASSIDSCIENCEAREA      varchar(255) not null,
   primary key (COMMISSIONID)
);

/*==============================================================*/
/* Table: COMMISSION_COMMISSION                             */
/*==============================================================*/
create table COMMISSION_COMMISSION
(
   COMMISSIONID1            int(11) not null,
   COMMISSIONID2            int(11) not null,
   CFCLASSSCHEMEID      varchar(32) not null,
   CFCLASSID            varchar(255) not null,
   CFSTARTDATE          datetime not null,
   CFENDDATE            datetime not null,
   primary key (COMMISSIONID1, COMMISSIONID2, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
);


/*==============================================================*/
/* Table: RESULTMAPPING                                         */
/*==============================================================*/
create table RESULTMAPPING
(
   RESULTMAPPINGID      bigint not null,
   CFCLASSSCHEMEIDRULEBOOKRESULLTTYPE varchar(32) not null,
   CFCLASSIDRULEBOOKRESULTTYPE varchar(255) not null,
   CFCLASSSCHEMEIDPRIZETYPE varchar(32),
   CFCLASSIDPRIZETYPE   varchar(255),
   CFCLASSSCHEMEIDRESEARCHERROLETYPE varchar(32),
   CFCLASSIDRESEARCHERROLETYPE varchar(255),
   CFCLASSSCHEMEIDEVENTTYPE varchar(32),
   CFCLASSIDEVENTTYPE   varchar(255),
   CFCLASSSCHEMEIDPUBLICATIONTYPE varchar(32),
   CFCLASSIDPUBLICATIONTYPE varchar(255),
   CFCLASSSCHEMEIDSUPERPUBLICATIONTYPE varchar(32),
   CFCLASSIDSUPERPUBLICATIONTYPE varchar(255),
   CFCLASSSCHEMEIDPRODUCTTYPE varchar(32),
   CFCLASSIDPRODUCTTYPE varchar(255),
   CFCLASSSCHEMEIDPATENTTYPE varchar(32),
   CFCLASSIDPATENTTYPE  varchar(255),
   primary key (RESULTMAPPINGID)
);

/*==============================================================*/
/* Table: RESULTTYPEMEASURE                                     */
/*==============================================================*/
create table RESULTTYPEMEASURE
(
   CFCLASSSCHEMEIDSCIENCEGROUP varchar(32) not null,
   CFCLASSIDSCIENCEGROUP varchar(255) not null,
   CFCLASSSCHEMEIDRESULTTYPE varchar(32) not null,
   CFCLASSIDRESULTTYPE  varchar(255) not null,
   QUANTITATIVEMEASURE  decimal(6,2) not null,
   primary key (CFCLASSSCHEMEIDRESULTTYPE, CFCLASSSCHEMEIDSCIENCEGROUP, CFCLASSIDSCIENCEGROUP, CFCLASSIDRESULTTYPE)
);

alter table COMMISSION add constraint FK_RELATIONSHIP_238 foreign key (CFCLASSSCHEMEIDSCIENCEAREA, CFCLASSIDSCIENCEAREA)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table MARC21RECORD_CLASS ADD COLUMN COMMISSIONID  int(11) not null default 0;

alter table MARC21RECORD_CLASS add constraint FK_RELATIONSHIP_227 foreign key (COMMISSIONID)
      references COMMISSION (COMMISSIONID) on delete cascade on update cascade;

alter table MARC21RECORD_CLASS drop primary key, add primary key(RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID);

alter table RESULTTYPEMEASURE add constraint FK_RELATIONSHIP_228 foreign key (CFCLASSSCHEMEIDSCIENCEGROUP, CFCLASSIDSCIENCEGROUP)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table RESULTTYPEMEASURE add constraint FK_RELATIONSHIP_229 foreign key (CFCLASSSCHEMEIDRESULTTYPE, CFCLASSIDRESULTTYPE)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table RESULTMAPPING add constraint FK_RELATIONSHIP_230 foreign key (CFCLASSSCHEMEIDSUPERPUBLICATIONTYPE, CFCLASSIDSUPERPUBLICATIONTYPE)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table RESULTMAPPING add constraint FK_RELATIONSHIP_231 foreign key (CFCLASSSCHEMEIDRULEBOOKRESULLTTYPE, CFCLASSIDRULEBOOKRESULTTYPE)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table RESULTMAPPING add constraint FK_RELATIONSHIP_232 foreign key (CFCLASSSCHEMEIDPUBLICATIONTYPE, CFCLASSIDPUBLICATIONTYPE)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table RESULTMAPPING add constraint FK_RELATIONSHIP_233 foreign key (CFCLASSSCHEMEIDPRODUCTTYPE, CFCLASSIDPRODUCTTYPE)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table RESULTMAPPING add constraint FK_RELATIONSHIP_234 foreign key (CFCLASSSCHEMEIDPRIZETYPE, CFCLASSIDPRIZETYPE)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table RESULTMAPPING add constraint FK_RELATIONSHIP_235 foreign key (CFCLASSSCHEMEIDRESEARCHERROLETYPE, CFCLASSIDRESEARCHERROLETYPE)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table RESULTMAPPING add constraint FK_RELATIONSHIP_236 foreign key (CFCLASSSCHEMEIDPATENTTYPE, CFCLASSIDPATENTTYPE)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table RESULTMAPPING add constraint FK_RELATIONSHIP_237 foreign key (CFCLASSSCHEMEIDEVENTTYPE, CFCLASSIDEVENTTYPE)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table CFMETRICSDESCRIPTION add constraint FK_RELATIONSHIP_163 foreign key (CFLANGCODE)
      references CFLANGUAGE (CFLANGCODE) on delete cascade on update cascade;

alter table CFMETRICSDESCRIPTION add constraint FK_RELATIONSHIP_165 foreign key (CFMETRICSID)
      references CFMETRICS (CFMETRICSID) on delete cascade on update cascade;

alter table CFMETRICSNAME add constraint FK_RELATIONSHIP_162 foreign key (CFLANGCODE)
      references CFLANGUAGE (CFLANGCODE) on delete cascade on update cascade;

alter table CFMETRICSNAME add constraint FK_RELATIONSHIP_164 foreign key (CFMETRICSID)
      references CFMETRICS (CFMETRICSID) on delete cascade on update cascade;

alter table CFMETRICS_CLASS add constraint FK_RELATIONSHIP_166 foreign key (CFMETRICSID)
      references CFMETRICS (CFMETRICSID) on delete cascade on update cascade;

alter table CFMETRICS_CLASS add constraint FK_RELATIONSHIP_167 foreign key (CFCLASSSCHEMEID, CFCLASSID)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table MARC21RECORD_METRICS add constraint FK_REFERENCE_228 foreign key (RECORDID)
      references MARC21RECORD (RECORDID) on delete cascade on update cascade;

alter table MARC21RECORD_METRICS add constraint FK_REFERENCE_229 foreign key (CFCLASSSCHEMEID, CFCLASSID)
      references CFCLASS (CFCLASSSCHEMEID, CFCLASSID) on delete cascade on update cascade;

alter table MARC21RECORD_METRICS add constraint FK_REFERENCE_230 foreign key (CFMETRICSID)
      references CFMETRICS (CFMETRICSID) on delete cascade on update cascade;
