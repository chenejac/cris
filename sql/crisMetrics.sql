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
   CFCLASSID      varchar(32) not null,
   CFSTARTDATE          datetime not null,
   CFENDDATE            datetime not null,
   primary key (CFCLASSSCHEMEID, CFMETRICSID, CFCLASSID, CFSTARTDATE, CFENDDATE)
);

/*==============================================================*/
/* Table: MARC21RECORD_METRICS                                  */
/*==============================================================*/
create table MARC21RECORD_METRICS
(
   RECORDID             varchar(50) not null,
   CFMETRICSID          varchar(32) not null,
   CFCLASSSCHEMEID      varchar(32) not null,
   CFCLASSID            varchar(32) not null,
   CFYEAR               int not null,
   CFCOUNT              double,
   CFFRACTION           double,
   primary key (RECORDID, CFMETRICSID, CFCLASSSCHEMEID, CFCLASSID, CFYEAR)
);

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

insert into CFCLASSSCHEME(CFCLASSSCHEMEID)
										values
												('value of metric');

insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
													values
															('value of metric', 'value of IF', '1900-01-01 00:00:00', '2099-12-31 00:00:00');