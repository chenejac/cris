create table REGISTERENTRY
(
   REGISTERENTRYID                                  text,
   AUTHORNAME                                       text,
   AUTHORLASTNAME                                   text,
   FATHERNAME                                       text,
   FATHERLASTNAME                                   text,
   MOTHERNAME                                       text,
   MOTHERLASTNAME                                   text,
   GUARDIANSFULLNAME                                text,
   BIRTHDATE                                        datetime,
   BIRTHPLACE                                       text,
   BIRTHCITY                                        text,
   BIRTHCOUNTRY                                     text,
   PREVIOUSLYGRADUATED                              text,
   PREVIOUSLYGRADUATEDPLACE                         text,
   PREVIOUSLYNAMEOFAUTHORDEGREEOLD                  text,
   PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONOLD      text,
   PREVIOUSLYNAMEOFAUTHORDEGREEDATEOLD              datetime,
   PREVIOUSLYNAMEOFAUTHORDEGREEBOLOGNA              text,
   PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONBOLOGNA  text,
   PREVIOUSLYNAMEOFAUTHORDEGREEYEARBOLOGNA          text,
   TITLE                                            text,
   INSTITUTION                                      text,
   INSTITUTIONPLACE                                 text,
   ADVISORS                                         text,
   COMMISSIONMEMBERS                                text,
   DEFENDEDON                                       datetime,
   MARK                                             text,
   NAMEOFAUTHORDEGREE                               text,
   PROMOTIONDATE                                    datetime,
   DIPLOMANUMBER                                    text,
   SUPPLEMENTNUMBER                                 text,
   DIPLOMAPUBLICATIONDATE                           datetime,
   SUPPLEMENTPUBLICATIONDATE                        datetime,
   DISSERTATIONID                                   varchar(50) not null,
   CREATOR                                          varchar(50),
   primary key (DISSERTATIONID)
);

alter table REGISTERENTRY add constraint FK_REFERENCE_300 foreign key (DISSERTATIONID)
      references MARC21RECORD (RECORDID) on delete cascade on update cascade;

alter table REGISTERENTRY add constraint FK_REFERENCE_301 foreign key (CREATOR)
      references USER (EMAIL) on delete cascade on update cascade;
      
ALTER TABLE REGISTERENTRY ADD COLUMN ACADEMICYEAR text AFTER REGISTERENTRYID;
ALTER TABLE REGISTERENTRY ADD COLUMN ACADEMICYEARNUMBER int AFTER ACADEMICYEAR;
ALTER TABLE REGISTERENTRY ADD COLUMN STREETANDNUMBER text AFTER GUARDIANSFULLNAME; 
ALTER TABLE REGISTERENTRY ADD COLUMN PLACE text AFTER STREETANDNUMBER; 
ALTER TABLE REGISTERENTRY ADD COLUMN POSTALCODE text AFTER PLACE; 
ALTER TABLE REGISTERENTRY ADD COLUMN CITY text AFTER POSTALCODE; 
ALTER TABLE REGISTERENTRY ADD COLUMN COUNTRY text AFTER CITY; 
ALTER TABLE REGISTERENTRY ADD COLUMN EMAIL text AFTER COUNTRY; 
ALTER TABLE REGISTERENTRY ADD COLUMN PHONE text AFTER EMAIL; 
