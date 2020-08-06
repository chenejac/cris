insert into CFCLASS (CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, CFURI)
              values ('resultType', 'M21a', '2008-03-21', '3008-03-21', null);

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
                      values ('resultType', 'M21a', 'srp', 'o', 'Рад у међународном часопису изузетних вредности');

insert into CFCLASS_CLASS (CFCLASSSCHEMEID1, CFCLASSID1, CFCLASSSCHEMEID2, CFCLASSID2, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
                    values ('resultType', 'M21a', 'resultType', 'M20', 'hierarchy', 'belongsTo', '2008-03-21', '3008-03-21');

insert into RESULTTYPEMEASURE (CFCLASSSCHEMEIDSCIENCEGROUP, CFCLASSIDSCIENCEGROUP,CFCLASSSCHEMEIDRESULTTYPE, CFCLASSIDRESULTTYPE, QUANTITATIVEMEASURE)
                    values ('sciencesGroup', 'naturalSciences', 'resultType', 'M21a', 10),
                           ('sciencesGroup', 'technicalSciences', 'resultType', 'M21a', 10),
                           ('sciencesGroup', 'socialSciences', 'resultType', 'M21a', 10);

insert into CFCLASS (CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, CFURI)
              values  ('type', 'topLeadingInternationalJournal', '2008-03-21', '3008-03-21', null);

insert into RESULTMAPPING (CFCLASSSCHEMEIDRULEBOOKRESULLTTYPE, CFCLASSIDRULEBOOKRESULTTYPE, CFCLASSSCHEMEIDPUBLICATIONTYPE, CFCLASSIDPUBLICATIONTYPE)
                    values ('resultType', 'M21a', 'resultType', 'M21a');

insert into RESULTMAPPING (CFCLASSSCHEMEIDRULEBOOKRESULLTTYPE, CFCLASSIDRULEBOOKRESULTTYPE, CFCLASSSCHEMEIDRESEARCHERROLETYPE, CFCLASSIDRESEARCHERROLETYPE, CFCLASSSCHEMEIDSUPERPUBLICATIONTYPE, CFCLASSIDSUPERPUBLICATIONTYPE, CFCLASSSCHEMEIDPUBLICATIONTYPE, CFCLASSIDPUBLICATIONTYPE)
                    values ('resultType', 'M21a', 'authorshipType', 'is author of', 'type', 'topLeadingInternationalJournal', 'type', 'paperJournal'),
                           ('resultType', 'M25', 'authorshipType', 'is author of', 'type', 'topLeadingInternationalJournal', 'type', 'scientificCriticismJournal'),
                           ('resultType', 'M27', 'authorshipType', 'is editor of', null, null, 'type', 'topLeadingInternationalJournal');

INSERT INTO `COMMISSION_COMMISSION` (`COMMISSIONID1`,`COMMISSIONID2`,`CFCLASSSCHEMEID`,`CFCLASSID`,`CFSTARTDATE`,`CFENDDATE`) VALUES
 (701,700,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,610,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,611,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,612,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,613,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,614,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,615,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,616,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,617,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,618,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,619,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,620,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,621,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,622,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,623,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,624,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,625,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,626,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,627,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,628,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,629,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,630,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,631,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00'),
 (701,632,'commissionsRelation','adopt','2011-03-21 00:00:00','3011-03-21 00:00:00');
