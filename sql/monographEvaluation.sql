/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     23.1.2013 11:18:32                           */
/*==============================================================*/


drop table if exists AUTOCITATION;

drop table if exists MONOGRAPHEVALUATIONDATA;

/*==============================================================*/
/* Table: AUTOCITATION                                          */
/*==============================================================*/
create table AUTOCITATION
(
   AUTOCITATIONID       	int not null AUTO_INCREMENT,
   EVALUATEDRECORDID    	varchar(50) not null,
   RESEARCHERRECORDID   	varchar(50) not null,
   AUTOCITATIONM20NUM      int,
   AUTOCITATIONM50NUM      int,
   primary key (AUTOCITATIONID)
);

/*==============================================================*/
/* Table: MONOGRAPHEVALUATIONDATA                               */
/*==============================================================*/
create table MONOGRAPHEVALUATIONDATA(
   
   HASREVIEWININTERNATIONALJOURNAL text,
   NUMBEROFREVIEWERS    int,
   MONOGRAPHRECORDID     varchar(50) not null,
   primary key (MONOGRAPHRECORDID)
);

alter table AUTOCITATION add constraint FK_REFERENCE_302 foreign key (EVALUATEDRECORDID)
      references MARC21RECORD (RECORDID)  on delete cascade on update cascade;
      
alter table AUTOCITATION add constraint FK_REFERENCE_303 foreign key (RESEARCHERRECORDID)
      references MARC21RECORD (RECORDID) on delete cascade on update cascade;      

alter table MONOGRAPHEVALUATIONDATA add constraint FK_REFERENCE_304 foreign key (MONOGRAPHRECORDID)
      references MARC21RECORD (RECORDID) on delete cascade on update cascade;      
