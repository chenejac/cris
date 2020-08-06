insert into marc21record_class(recordid, cfclassschemeid, cfclassid, cfstartdate, cfenddate, commissionid)
select recordid, 'type', 'conference', '1900-01-01 00:00:00', '2099-12-31 00:00:00', 0 from marc21record where recordid not in (select recordid from marc21record_class where commissionid=0)

update marc21record_class set cfclassid='nationalJournal' where cfclassid like 'scienceJournal' and recordid in (select distinct(recordid) from (select * from marc21record_class) as X where cfclassid in ('leadingInternationalJournal', 'outstandingInternationalJournal', 'internationalJournal'));

update marc21record m SET recordstring=(
REPLACE (recordstring,'BISIS008    |||||||||||||||||||||||||||||||||||srp||','BISIS'))
where datecreated >= '2011-06-22 00:00:00' and recordid in (select recordid from marc21record_class where cfclassschemeid like 'type' and cfclassid in ('abstractPP', 'fullPP', 'discussionPP', 'invTalkAbstractPP', 'invTalkFullPP', 'monograph', 'paperJournal', 'scientificCriticismJournal', 'paperMonograph', 'paperProceedings', 'oldMasterStudyFinalDocument', 'phdStudyFinalDocument', 'phdArtProject'));