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


alter table FILE_STORAGE add constraint FK_REFERENCE_226 foreign key (UPLOADER)
      references USER (EMAIL) on delete cascade on update cascade;

alter table FILE_STORAGE add constraint FK_REFERENCE_227 foreign key (RECORDID)
      references MARC21RECORD (RECORDID) on delete cascade on update cascade;



