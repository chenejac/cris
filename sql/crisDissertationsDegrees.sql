insert into CFCLASSSCHEME(CFCLASSSCHEMEID)
										values ('nameOfDegree');

insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
             values ('nameOfDegree', 'pmf1', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'pmf2', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
										('nameOfDegree', 'pmf3', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'pmf4', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'pmf5', '1900-01-01 00:00:00','2099-12-31 00:00:00'),
                    ('nameOfDegree', 'pmf6', '1900-01-01 00:00:00','2099-12-31 00:00:00'),
                    ('nameOfDegree', 'pmf7', '1900-01-01 00:00:00','2099-12-31 00:00:00'),
                    ('nameOfDegree', 'pmf8', '1900-01-01 00:00:00','2099-12-31 00:00:00'),
                    ('nameOfDegree', 'pmf9', '1900-01-01 00:00:00','2099-12-31 00:00:00'),
                    ('nameOfDegree', 'pmf10', '1900-01-01 00:00:00','2099-12-31 00:00:00'),
                    ('nameOfDegree', 'pmf11', '1900-01-01 00:00:00','2099-12-31 00:00:00');

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'pmf1', 'srp', 'o', 'doktor fizičkih nauka'),
                  ('nameOfDegree', 'pmf2', 'srp', 'o', 'doktor hemijskih nauka'),
                  ('nameOfDegree', 'pmf3', 'srp', 'o', 'doktor bioloških nauka'),
                  ('nameOfDegree', 'pmf4', 'srp', 'o', 'doktor geografskih nauka'),
                  ('nameOfDegree', 'pmf5', 'srp', 'o', 'doktor matematičkih nauka'),
                  ('nameOfDegree', 'pmf6', 'srp', 'o', 'doktor informatičkih nauka'),
                  ('nameOfDegree', 'pmf7', 'srp', 'o', 'doktor biohemijskih nauka'),
                  ('nameOfDegree', 'pmf8', 'srp', 'o', 'doktor nauka metodike nastave hemije'),
                  ('nameOfDegree', 'pmf9', 'srp', 'o', 'doktor nauka metodike nastave biologije'),
                  ('nameOfDegree', 'pmf10', 'srp', 'o', 'doktor nauka iz oblasti turizma'),
                  ('nameOfDegree', 'pmf11', 'srp', 'o', 'doktor nauka metodike nastave matematike');


insert into MARC21RECORD_CLASS(RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID)
             values ('(BISIS)5929', 'nameOfDegree', 'pmf1', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5929', 'nameOfDegree', 'pmf2', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
										('(BISIS)5929', 'nameOfDegree', 'pmf3', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5929', 'nameOfDegree', 'pmf4', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5929', 'nameOfDegree', 'pmf5', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5929', 'nameOfDegree', 'pmf6', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5929', 'nameOfDegree', 'pmf7', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5929', 'nameOfDegree', 'pmf8', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5929', 'nameOfDegree', 'pmf9', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5929', 'nameOfDegree', 'pmf10', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5929', 'nameOfDegree', 'pmf11', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0);

-- filo --
insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
             values ('nameOfDegree', 'filo1', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'filo2', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
			              ('nameOfDegree', 'filo3', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'filo4', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'filo5', '1900-01-01 00:00:00','2099-12-31 00:00:00'),
                    ('nameOfDegree', 'filo6', '1900-01-01 00:00:00','2099-12-31 00:00:00'),
                    ('nameOfDegree', 'filo7', '1900-01-01 00:00:00','2099-12-31 00:00:00'),
                    ('nameOfDegree', 'filo8', '1900-01-01 00:00:00','2099-12-31 00:00:00'),
                    ('nameOfDegree', 'filo9', '1900-01-01 00:00:00','2099-12-31 00:00:00');

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'filo1', 'srp', 'o', 'doktor filoloških nauka'),
                  ('nameOfDegree', 'filo2', 'srp', 'o', 'doktor filozofskih nauka'),
                  ('nameOfDegree', 'filo3', 'srp', 'o', 'doktor istorijskih nauka'),
                  ('nameOfDegree', 'filo4', 'srp', 'o', 'doktor književnih nauka'),
                  ('nameOfDegree', 'filo5', 'srp', 'o', 'doktor lingvističkih nauka'),
                  ('nameOfDegree', 'filo6', 'srp', 'o', 'doktor literarnih nauka'),
                  ('nameOfDegree', 'filo7', 'srp', 'o', 'doktor pedagoških nauka'),
                  ('nameOfDegree', 'filo8', 'srp', 'o', 'doktor psiholoških nauka'),
                  ('nameOfDegree', 'filo9', 'srp', 'o', 'doktor socioloških nauka');


insert into MARC21RECORD_CLASS(RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID)
             values ('(BISIS)5930', 'nameOfDegree', 'filo1', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5930', 'nameOfDegree', 'filo2', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
			              ('(BISIS)5930', 'nameOfDegree', 'filo3', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5930', 'nameOfDegree', 'filo4', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5930', 'nameOfDegree', 'filo5', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5930', 'nameOfDegree', 'filo6', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5930', 'nameOfDegree', 'filo7', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5930', 'nameOfDegree', 'filo8', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5930', 'nameOfDegree', 'filo9', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0);

-- Medi --
insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
             values ('nameOfDegree', 'medi1', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'medi2', '1900-01-01 00:00:00','2099-12-31 00:00:00' );

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'medi1', 'srp', 'o', 'doktor medicinskih nauka'),
                  ('nameOfDegree', 'medi2', 'srp', 'o', 'doktor stomatoloških nauka');


insert into MARC21RECORD_CLASS(RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID)
             values ('(BISIS)5935', 'nameOfDegree', 'medi1', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5935', 'nameOfDegree', 'medi2', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0);

-- poljo --
insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
             values ('nameOfDegree', 'poljo1', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'poljo2', '1900-01-01 00:00:00','2099-12-31 00:00:00' );

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'poljo1', 'srp', 'o', 'doktor poljoprivrednih nauka'),
                  ('nameOfDegree', 'poljo2', 'srp', 'o', 'doktor veterinarskih nauka');


insert into MARC21RECORD_CLASS(RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID)
             values ('(BISIS)5931', 'nameOfDegree', 'poljo1', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5931', 'nameOfDegree', 'poljo2', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0);

-- tfmp --
insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
             values ('nameOfDegree', 'tfmp1', '1900-01-01 00:00:00','2099-12-31 00:00:00' );

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'tfmp1', 'srp', 'o', 'doktor tehničkih nauka');


insert into MARC21RECORD_CLASS(RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID)
             values ('(BISIS)5938', 'nameOfDegree', 'tfmp1', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0);

-- ths --
insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
             values ('nameOfDegree', 'ths1', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                     ('nameOfDegree', 'ths2', '1900-01-01 00:00:00','2099-12-31 00:00:00' );

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'ths1', 'srp', 'o', 'doktor tehničkih nauka'),
                   ('nameOfDegree', 'ths2', 'srp', 'o', 'doktor tehnoloških nauka');


insert into MARC21RECORD_CLASS(RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID)
             values ('(BISIS)5933', 'nameOfDegree', 'ths1', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                     ('(BISIS)5933', 'nameOfDegree', 'ths2', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0);


-- au --
insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
             values ('nameOfDegree', 'au1', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'au2', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'au3', '1900-01-01 00:00:00','2099-12-31 00:00:00' );

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'au1', 'srp', 'o', 'doktor nauka iz oblasti filmologije'),
                  ('nameOfDegree', 'au2', 'srp', 'o', 'doktor nauka iz oblasti muzikologije'),
                  ('nameOfDegree', 'au3', 'srp', 'o', 'doktor nauka iz oblasti teatrologije');


insert into MARC21RECORD_CLASS(RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID)
             values ('(BISIS)5936', 'nameOfDegree', 'au1', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5936', 'nameOfDegree', 'au2', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)5936', 'nameOfDegree', 'au3', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0);

-- acimsi --
insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
             values ('nameOfDegree', 'acimsi1', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'acimsi2', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi3', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi4', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'acimsi5', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi6', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi7', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'acimsi8', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi9', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi10', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'acimsi11', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi12', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi13', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'acimsi14', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi15', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi16', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'acimsi17', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi18', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi19', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'acimsi20', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi21', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi22', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'acimsi23', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi24', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi25', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'acimsi26', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'acimsi27', '1900-01-01 00:00:00','2099-12-31 00:00:00' );

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'acimsi1', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti bioloških i pedagoških nauka'),
                  ('nameOfDegree', 'acimsi2', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti bioloških i tehničkih nauka'),
                  ('nameOfDegree', 'acimsi3', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti ekonomskih i geografskih nauka'),
		              ('nameOfDegree', 'acimsi4', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti ekonomskih i tehničkih nauka'),
                  ('nameOfDegree', 'acimsi5', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti fizičke kulture i pedagoških nauka'),
                  ('nameOfDegree', 'acimsi6', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti fizičkih nauka i zaštite životne sredine'),
                  ('nameOfDegree', 'acimsi7', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti informatičkih, menadžerskih, ekonomskih i tehničkih nauka'),
                  ('nameOfDegree', 'acimsi8', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti inženjerstva za zaštitu životne sredine'),
                  ('nameOfDegree', 'acimsi9', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti matematičkih i pedagoških nauka'),
		              ('nameOfDegree', 'acimsi10', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti medicinske fizike'),
                  ('nameOfDegree', 'acimsi11', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti menadžmenta i inženjerstva za zaštitu životne sredine'),
                  ('nameOfDegree', 'acimsi12', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti menadžmenta organizacije, sociologije i psihologije'),
		              ('nameOfDegree', 'acimsi13', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti menadžmenta u obrazovanju'),
                  ('nameOfDegree', 'acimsi14', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti menadžmenta u sportu'),
                  ('nameOfDegree', 'acimsi15', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti meteorologije i modeliranja životne sredine'),
		              ('nameOfDegree', 'acimsi16', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti organizacije i upravljanje preduzećima'),
                  ('nameOfDegree', 'acimsi17', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti pedagoško-tehničkih nauka'),
                  ('nameOfDegree', 'acimsi18', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti političkih, pravnih i tehničkih nauka'),
		              ('nameOfDegree', 'acimsi19', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti prava, filozofije i politike '),
                  ('nameOfDegree', 'acimsi20', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti pravnih geografskih i pedagoško-metodoloških nauka'),
                  ('nameOfDegree', 'acimsi21', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti rodnih studija'),
		              ('nameOfDegree', 'acimsi22', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti socioloških i pravnih nauka'),
                  ('nameOfDegree', 'acimsi23', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti tehničkih i ekonomskih nauka'),
                  ('nameOfDegree', 'acimsi24', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti tehničkih i hemijskih nauka'),
		              ('nameOfDegree', 'acimsi25', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti tehnike i menadžmenta u primenjenim umetnostima'),
                  ('nameOfDegree', 'acimsi26', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti telekomunikacija, prava i ekonomije'),
                  ('nameOfDegree', 'acimsi27', 'srp', 'o', 'doktor nauka iz interdisciplinarne oblasti umetnosti i istorijskih nauka');


insert into MARC21RECORD_CLASS(RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID)
             values ('(BISIS)8011', 'nameOfDegree', 'acimsi1', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)8011', 'nameOfDegree', 'acimsi2', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi3', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi4', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)8011', 'nameOfDegree', 'acimsi5', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi6', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi7', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)8011', 'nameOfDegree', 'acimsi8', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi9', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi10', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)8011', 'nameOfDegree', 'acimsi11', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi12', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi13', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)8011', 'nameOfDegree', 'acimsi14', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi15', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi16', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)8011', 'nameOfDegree', 'acimsi17', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi18', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi19', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)8011', 'nameOfDegree', 'acimsi20', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)8011', 'nameOfDegree', 'acimsi21', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi22', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)8011', 'nameOfDegree', 'acimsi23', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi24', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi25', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)8011', 'nameOfDegree', 'acimsi26', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)8011', 'nameOfDegree', 'acimsi27', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0);

-- eko --
insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
             values ('nameOfDegree', 'eko1', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'eko2', '1900-01-01 00:00:00','2099-12-31 00:00:00' );

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'eko1', 'srp', 'o', 'doktor ekonomskih nauka'),
                  ('nameOfDegree', 'eko2', 'srp', 'o', 'doktor informatičkih nauka');


insert into MARC21RECORD_CLASS(RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID)
             values ('(BISIS)5934', 'nameOfDegree', 'eko1', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5934', 'nameOfDegree', 'eko2', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0) ;

-- dif --
insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
             values ('nameOfDegree', 'dif1', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'dif2', '1900-01-01 00:00:00','2099-12-31 00:00:00' );

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'dif1', 'srp', 'o', 'doktor nauka iz fizičke kulture'),
                  ('nameOfDegree', 'dif2', 'srp', 'o', 'doktor nauka iz oblasti sporta i fizičkog vaspitanja');


insert into MARC21RECORD_CLASS(RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID)
             values ('(BISIS)5939', 'nameOfDegree', 'dif1', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5939', 'nameOfDegree', 'dif2', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0) ;

-- ftn --
insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
             values ('nameOfDegree', 'ftn1', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'ftn2', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'ftn3', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'ftn4', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'ftn5', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'ftn6', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'ftn7', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'ftn8', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
		                ('nameOfDegree', 'ftn9', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'ftn10', '1900-01-01 00:00:00','2099-12-31 00:00:00' );

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'ftn1', 'srp', 'o', 'doktor tehničkih nauka'),
                  ('nameOfDegree', 'ftn2', 'srp', 'o', 'doktor tehničkih nauka iz oblasti arhitekture i urbanizma'),
		              ('nameOfDegree', 'ftn3', 'srp', 'o', 'doktor tehničkih nauka iz oblasti elektrotehnike i računarstva'),
                  ('nameOfDegree', 'ftn4', 'srp', 'o', 'doktor tehničkih nauka iz oblasti građevinarstva'),
                  ('nameOfDegree', 'ftn5', 'srp', 'o', 'doktor tehničkih nauka iz oblasti saobraćaja'),
		              ('nameOfDegree', 'ftn6', 'srp', 'o', 'doktor tehničkih nauka iz oblasti grafičkog inženjerstva i dizajna'),
                  ('nameOfDegree', 'ftn7', 'srp', 'o', 'doktor tehničkih nauka iz oblasti mašinstva'),
		              ('nameOfDegree', 'ftn8', 'srp', 'o', 'doktor tehničkih nauka iz oblasti industrijsko inženjerstvo i inženjerski menadžment'),
                  ('nameOfDegree', 'ftn9', 'srp', 'o', 'doktor tehničkih nauka iz oblasti inženjerstva i zaštite životne sredine'),
                  ('nameOfDegree', 'ftn10', 'srp', 'o', 'doktor tehničkih nauka iz oblasti mehatronike');


insert into MARC21RECORD_CLASS(RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID)
             values ('(BISIS)5928', 'nameOfDegree', 'ftn1', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5928', 'nameOfDegree', 'ftn2', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5928', 'nameOfDegree', 'ftn3', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5928', 'nameOfDegree', 'ftn4', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5928', 'nameOfDegree', 'ftn5', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5928', 'nameOfDegree', 'ftn6', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5928', 'nameOfDegree', 'ftn7', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5928', 'nameOfDegree', 'ftn8', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)5928', 'nameOfDegree', 'ftn9', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5928', 'nameOfDegree', 'ftn10', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0);

-- pra --
insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
             values ('nameOfDegree', 'pra1', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'pra2', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('nameOfDegree', 'pra3', '1900-01-01 00:00:00','2099-12-31 00:00:00' );

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'pra1', 'srp', 'o', 'doktor društveno-ekonomskih nauka'),
                  ('nameOfDegree', 'pra2', 'srp', 'o', 'doktor društveno-političkih nauka'),
                  ('nameOfDegree', 'pra3', 'srp', 'o', 'doktor pravnih nauka');


insert into MARC21RECORD_CLASS(RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID)
             values ('(BISIS)5932', 'nameOfDegree', 'pra1', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
                    ('(BISIS)5932', 'nameOfDegree', 'pra2', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0),
		                ('(BISIS)5932', 'nameOfDegree', 'pra3', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0)  ;



-- gra --
insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
             values ('nameOfDegree', 'gra1', '1900-01-01 00:00:00','2099-12-31 00:00:00' );

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'gra1', 'srp', 'o', 'doktor tehničkih nauka');


insert into MARC21RECORD_CLASS(RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID)
             values ('(BISIS)5937', 'nameOfDegree', 'gra1', '1900-01-01 00:00:00','2099-12-31 00:00:00', 0) ;





-- prevodi --

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'pmf1', 'eng', 'h', 'Doctor of Physical Sciences'),
                  ('nameOfDegree', 'pmf2', 'eng', 'h', 'Doctor of Chemical Sciences'),
                  ('nameOfDegree', 'pmf3', 'eng', 'h', 'Doctor of Biological Sciences'),
                  ('nameOfDegree', 'pmf4', 'eng', 'h', 'Doctor of Geographical Sciences'),
                  ('nameOfDegree', 'pmf5', 'eng', 'h', 'Doctor of Mathematical Sciences'),
                  ('nameOfDegree', 'pmf6', 'eng', 'h', 'Doctor of Information Sciences'),
                  ('nameOfDegree', 'pmf7', 'eng', 'h', 'Doctor of Biochemical Sciences'),
                  ('nameOfDegree', 'pmf8', 'eng', 'h', 'Doctor of Sciences in Methodology of Teaching Chemistry'),
                  ('nameOfDegree', 'pmf9', 'eng', 'h', 'Doctor of Sciences in Methodology of Teaching Biology'),
                  ('nameOfDegree', 'pmf10', 'eng', 'h', 'Doctor of Sciences in the field of Tourism'),
                  ('nameOfDegree', 'pmf11', 'eng', 'h', 'Doctor of Sciences in Methodology of Teaching Mathematics');

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'filo1', 'eng', 'h', 'Doctor of Philological Sciences'),
                  ('nameOfDegree', 'filo2', 'eng', 'h', 'Doctor of Philosophical Sciences'),
                  ('nameOfDegree', 'filo3', 'eng', 'h', 'Doctor of Historical Sciences'),
                  ('nameOfDegree', 'filo4', 'eng', 'h', 'Doctor of Literaly Sciences'),
                  ('nameOfDegree', 'filo5', 'eng', 'h', 'Doctor of Linguistic Sciences'),
                  ('nameOfDegree', 'filo6', 'eng', 'h', 'Doctor of Literature Sciences'),
                  ('nameOfDegree', 'filo7', 'eng', 'h', 'Doctor of Pedagogical Sciences'),
                  ('nameOfDegree', 'filo8', 'eng', 'h', 'Doctor of Psycholigical Sciences'),
                  ('nameOfDegree', 'filo9', 'eng', 'h', 'Doctor of Sociological Sciences');

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'medi1', 'eng', 'h', 'Doctor of Medical Sciences'),
                  ('nameOfDegree', 'medi2', 'eng', 'h', 'Doctor of Dental Science');

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'poljo1', 'eng', 'h', 'Doctor of Agricultural Sciences'),
                  ('nameOfDegree', 'poljo2', 'eng', 'h', 'Doctor of Veterinary Sciences');

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'tfmp1', 'eng', 'h', 'Doctor of Technical Sciences');

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'ths1', 'eng', 'h', 'Doctor of Technical Sciences'),
                   ('nameOfDegree', 'ths2', 'eng', 'h', 'Doctor of Technological Sciences');

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'au1', 'eng', 'h', 'Doctor of Science In the field of Filmology'),
                  ('nameOfDegree', 'au2', 'eng', 'h', 'Doctor of Sciences in the field of Musicology'),
                  ('nameOfDegree', 'au3', 'eng', 'h', 'Doctor of Sciences in the field of Theatrology');

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'acimsi1', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Biological and Pedagogical Sciences'),
                  ('nameOfDegree', 'acimsi2', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Biological and Technical Sciences'),
                  ('nameOfDegree', 'acimsi3', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Economic and Geographical Sciences'),
		              ('nameOfDegree', 'acimsi4', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Economic and Technical Sciences'),
                  ('nameOfDegree', 'acimsi5', 'eng', 'h', 'Doctor of Sciiences in the interdisciplinary field of Physical Culture and Pedagogical Sciences'),
                  ('nameOfDegree', 'acimsi6', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Physical Sciences and Environmental Protection'),
                  ('nameOfDegree', 'acimsi7', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Information, Menagerial, Economic and Technical Sciences'),
                  ('nameOfDegree', 'acimsi8', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Enviromental Engineering'),
                  ('nameOfDegree', 'acimsi9', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Mathematical and Pedagogical Sciences'),
		              ('nameOfDegree', 'acimsi10', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Medical Physics'),
                  ('nameOfDegree', 'acimsi11', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Management and Environmental Engineering'),
                  ('nameOfDegree', 'acimsi12', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Organizational Management, Sociology and Psychology'),
		              ('nameOfDegree', 'acimsi13', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Management in Education'),
                  ('nameOfDegree', 'acimsi14', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Sports Management'),
                  ('nameOfDegree', 'acimsi15', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Meteorology and Environment Modeling'),
		              ('nameOfDegree', 'acimsi16', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Organization and Management of the Enterprize'),
                  ('nameOfDegree', 'acimsi17', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Pedagogical and Technical Sciences'),
                  ('nameOfDegree', 'acimsi18', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Political, Legal and Technical Sciences'),
		              ('nameOfDegree', 'acimsi19', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Law, Philosophy and Politics '),
                  ('nameOfDegree', 'acimsi20', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Legal Geographic and Pedagogical and Methodological Sciences'),
                  ('nameOfDegree', 'acimsi21', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Gender Studies'),
		              ('nameOfDegree', 'acimsi22', 'eng', 'h', 'Doctor of Scoences in the interdisciplinary field of Sociological and Legal Sciences'),
                  ('nameOfDegree', 'acimsi23', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Technical and Economic Sciences'),
                  ('nameOfDegree', 'acimsi24', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Technical and Chemical Sciences'),
		              ('nameOfDegree', 'acimsi25', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Technique and Management in Applied Arts'),
                  ('nameOfDegree', 'acimsi26', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Telecomunnications, Law and Economy'),
                  ('nameOfDegree', 'acimsi27', 'eng', 'h', 'Doctor of Sciences in the interdisciplinary field of Art and Historical Sciences');

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'eko1', 'eng', 'h', 'Doctor of Economic Sciences'),
                  ('nameOfDegree', 'eko2', 'eng', 'h', 'Doctor of Information Sciences');

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'dif1', 'eng', 'h', 'Doctor of Sciences in Physical Culture'),
                  ('nameOfDegree', 'dif2', 'eng', 'h', 'Doctor of Sciences in the field of Sport and Physical Education');

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'ftn1', 'eng', 'h', 'Doctor of Technical Sciences'),
                  ('nameOfDegree', 'ftn2', 'eng', 'h', 'Doctor of Technical Sciences in the field of Architecture and Urbanism'),
		              ('nameOfDegree', 'ftn3', 'eng', 'h', 'Doctor of Technical Sciences in the field of Electrical and Computer Engineering '),
                  ('nameOfDegree', 'ftn4', 'eng', 'h', 'Doctor of Technical Sciemces on the field of Civil Engineering'),
                  ('nameOfDegree', 'ftn5', 'eng', 'h', 'Doctor of Technical Sciences in the field of Traffic Engineering'),
		              ('nameOfDegree', 'ftn6', 'eng', 'h', 'Doctor of Technical Sciences in the field of Graphic Engineering and Design'),
                  ('nameOfDegree', 'ftn7', 'eng', 'h', 'Doctor of Technical Science in the field of Mechanical Engineering'),
		              ('nameOfDegree', 'ftn8', 'eng', 'h', 'Doctor of Technical Sciences in the field of Industrial Engineering and Engineering Management'),
                  ('nameOfDegree', 'ftn9', 'eng', 'h', 'Dosctor of Technical Sciences in the field of Engineering and Enviromental Protection'),
                  ('nameOfDegree', 'ftn10', 'eng', 'h', 'Doctor of Technical Sciences in the field of Mechatronics');

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'pra1', 'eng', 'h', 'Doctor of Socio-Economic Sciences'),
                  ('nameOfDegree', 'pra2', 'eng', 'h', 'Doctor of Socio-Political Sciences'),
                  ('nameOfDegree', 'pra3', 'eng', 'h', 'Doctor of Legal Sciences');

insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM)
            values ('nameOfDegree', 'gra1', 'eng', 'h', 'Doctor of Technical Sciences');
