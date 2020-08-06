insert into CFCLASSSCHEME(CFCLASSSCHEMEID)
										values ('authorJobAd'),
												('publicationJobAd');

insert into CFCLASS(CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE)
             values ('authorJobAd', 'applied to', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('publicationJobAd', 'applied to', '1900-01-01 00:00:00','2099-12-31 00:00:00' ),
                    ('type', 'jobAd', '1900-01-01 00:00:00','2099-12-31 00:00:00' );