ALTER TABLE USER ADD COLUMN INSTITUTIONID VARCHAR(50) AFTER RECORDID;
ALTER TABLE USER ADD COLUMN ORGANIZATIONUNITID VARCHAR(50) AFTER INSTITUTIONID;

alter table USER add constraint FK_RELATIONSHIP_INSTITUTION foreign key (INSTITUTIONID)
      references MARC21RECORD (RECORDID) on delete restrict on update cascade;

alter table USER add constraint FK_RELATIONSHIP_ORGANIZATIONUNIT foreign key (ORGANIZATIONUNITID)
      references MARC21RECORD (RECORDID) on delete restrict on update cascade;

alter table USER drop FOREIGN KEY FK_RELATIONSHIP_226;

alter table USER add constraint FK_RELATIONSHIP_RESEARCHER foreign key (RECORDID)
      references MARC21RECORD (RECORDID) on delete restrict on update cascade;