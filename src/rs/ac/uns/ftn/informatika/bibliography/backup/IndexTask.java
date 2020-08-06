package rs.ac.uns.ftn.informatika.bibliography.backup;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.RegisterEntryDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RegisterEntryDB;
import rs.ac.uns.ftn.informatika.bibliography.db.UserDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.BulkIndexer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Indexer;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

public class IndexTask implements Task {

	public IndexTask(String folderName, Connection conn, boolean firstStep, boolean secondStep, boolean thirdStep) {
		this.folderName = folderName;
		this.conn = conn;
		this.firstStep = firstStep;
		this.secondStep = secondStep;
		this.thirdStep = thirdStep;
	}

	
	@Override
	public boolean execute() {
		File indexFolder = new File(folderName);
		if((indexFolder.exists()) && (indexFolder.isDirectory())){
			if(firstStep){
				for (File file : indexFolder.listFiles()) {
					if(!(file.delete()))
						return false;
				}
				
				RecordDB recordDB = new RecordDB();
				Indexer indexer = new Indexer(folderName, new CrisAnalyzer());
				List<Record> records = null;
				
				// institutions
				String[] typesInstitution = {"institution"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesInstitution);
				List<Record> orderedInstitutions = new ArrayList<Record>();
				while(orderedInstitutions.size() != records.size()){
					for (Record record : records) {
						if(!orderedInstitutions.contains(record)){
							Record institution = new Record();
							for (RecordRecord recordRecord : record.getRelationsThisRecordOtherRecords()) {
								if((recordRecord.getCfClassSchemeId().equals("institutionsRelation")) && (recordRecord.getCfClassId().equals("is part of"))){
									institution.getMARC21Record().setControlNumber(recordRecord.getRecordId());
									break;
								}
							}
							if((institution.getControlNumber() == null) || (orderedInstitutions.contains(institution)))
								orderedInstitutions.add(record);
						}
					}
				}
				for (Record record : orderedInstitutions) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					indexer.add(record);
				}
				records.clear();
				System.out.println("Institutions indexed!!!");
				
				
				//organizationUnits
				String[] typesOrganizationUnit = {"organizationUnit"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesOrganizationUnit);
				List<Record> orderedOrganizationUnits = new ArrayList<Record>();
				while(orderedOrganizationUnits.size() != records.size()){
					for (Record record : records) {
						if(!orderedOrganizationUnits.contains(record)){
							Record organizationUnit = new Record();
							for (RecordRecord recordRecord : record.getRelationsThisRecordOtherRecords()) {
								if((recordRecord.getCfClassSchemeId().equals("institutionsRelation")) && (recordRecord.getCfClassId().equals("is part of"))){
									organizationUnit.getMARC21Record().setControlNumber(recordRecord.getRecordId());
									if(! orderedInstitutions.contains(organizationUnit)){
										break;
									} else {
										organizationUnit = new Record();
									}
								}
							}
							if((organizationUnit.getControlNumber() == null) || (orderedOrganizationUnits.contains(organizationUnit)))
								orderedOrganizationUnits.add(record);
						}
					}
				}
				for (Record record : orderedOrganizationUnits) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					indexer.add(record);
				}
				orderedInstitutions.clear();
				orderedOrganizationUnits.clear();
				records.clear();
				
				indexer.close();
				
				System.out.println("Organization Units indexed!!!");
				
				
				BulkIndexer bulkIndexer = new BulkIndexer(folderName, new CrisAnalyzer());
				
				//authors
				String[] typesAuthor = {"author"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesAuthor);
				UserDB userDB = new UserDB();
				for (Record record : records) {
					try {
						record.loadDTOFromMARC21();
						record.loadMARC21FromDTO();
						if(userDB.getUserByAuthorControlNumber(conn, record.getControlNumber())!=null)
							((AuthorDTO)(record.getDto())).setAlreadyRegistered(true);
						bulkIndexer.add(record);
						record.clear();
					} catch (Throwable e) {
						System.out.println("Puklo indeksiranje za autora sa id: "+record.getControlNumber());
						e.printStackTrace();
						record.clear();
					}
					
					
				}
				bulkIndexer.optimize();
				records.clear();
				
				System.out.println("Authors indexed!!!");
				
				//conferences
				String[] typesConference = {"conference"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesConference);
				for (Record record : records) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					bulkIndexer.add(record);
					record.clear();
				}
				bulkIndexer.optimize();
				records.clear();
				
				System.out.println("Conferences indexed!!!");
				
				//proceedings
				String[] typesProceedings = {"proceedings"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesProceedings);
				for (Record record : records) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					bulkIndexer.add(record);
					record.clear();
				}
				bulkIndexer.optimize();
				records.clear();
				
				System.out.println("Proceedings indexed!!!");
				
				//paperProceedings
				String[] typesPaperProceedings = {"paperProceedings", "invTalkFullPP", "invTalkAbstractPP", "fullPP", "abstractPP", "discussionPP"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesPaperProceedings);
				for (Record record : records) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					bulkIndexer.add(record);
					record.clear();
				}
				bulkIndexer.optimize();
				records.clear();
				
				System.out.println("Paper Proceedings indexed!!!");
				
				//journal
				String[] typesJournal = {"journal"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesJournal);
				for (Record record : records) {
					try{
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					bulkIndexer.add(record);
					record.clear();
					} catch (Throwable e) {
						System.out.println("Puklo indeksiranje za journal sa id: "+record.getControlNumber());
						record.clear();
					}
				}
				bulkIndexer.optimize();
				records.clear();
				
				System.out.println("Journal indexed!!!");
				
				//paperJournal
				String[] typesPaperJournal = {"paperJournal", "scientificCriticismJournal"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesPaperJournal);
				for (Record record : records) {
					try{
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					bulkIndexer.add(record);
					record.clear();
					} catch (Throwable e) {
						System.out.println("Puklo indeksiranje za paperJournal sa id: "+record.getControlNumber());
						record.clear();
					}
				}
				bulkIndexer.optimize();
				records.clear();
				
				System.out.println("Paper Journal indexed!!!");
				
				//monograph
				String[] typesMonograph = {"monograph"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesMonograph);
				for (Record record : records) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					bulkIndexer.add(record);
					record.clear();
				}
				bulkIndexer.optimize();
				records.clear();
				
				System.out.println("Monograph indexed!!!");
				
				//paperMonograph
				String[] typesPaperMonograph = {"paperMonograph"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesPaperMonograph);
				for (Record record : records) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					bulkIndexer.add(record);
					record.clear();
				}
				bulkIndexer.optimize();
				records.clear();
				
				System.out.println("Paper monograph indexed!!!");
				
				//patent
				String[] typesPatent = {"patent"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesPatent);
				for (Record record : records) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					bulkIndexer.add(record);
				}
				bulkIndexer.optimize();
				records.clear();
				
				System.out.println("Patents indexed!!!");
				
				//product
				String[] typesProduct = {"product"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesProduct);
				for (Record record : records) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					bulkIndexer.add(record);
				}
				bulkIndexer.optimize();
				records.clear();
				
				System.out.println("Product indexed!!!");
				
				//studyFinalDocument
				String[] typesStudyFinalDocument = {"studyFinalDocument", "bachelorStudyFinalDocument", "masterStudyFinalDocument", "oldBachelorStudyFinalDocument", "oldMasterStudyFinalDocument", "phdStudyFinalDocument", "phdArtProject", "specialisticStudyFinalDocument"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesStudyFinalDocument);
				for (Record record : records) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					bulkIndexer.add(record);
				}
				bulkIndexer.optimize();
				records.clear();
				
				System.out.println("Study Final Documents indexed!!!");
				
				bulkIndexer.optimize();
				bulkIndexer.close();
				System.out.println("First step finished!!!");	
			}
			
			if(secondStep){
				RecordDB recordDB = new RecordDB();
				List<Record> records = null;
				RecordDAO recordDAO = new RecordDAO(new RecordDB());
				
				Indexer indexer = new Indexer(folderName, new CrisAnalyzer());
				// institutions
				String[] typesInstitution = {"institution"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesInstitution);
				List<Record> orderedInstitutions = new ArrayList<Record>();
				while(orderedInstitutions.size() != records.size()){
					for (Record record : records) {
						if(!orderedInstitutions.contains(record)){
							Record institution = new Record();
							for (RecordRecord recordRecord : record.getRelationsThisRecordOtherRecords()) {
								if((recordRecord.getCfClassSchemeId().equals("institutionsRelation")) && (recordRecord.getCfClassId().equals("is part of"))){
									institution.getMARC21Record().setControlNumber(recordRecord.getRecordId());
									break;
								}
							}
							if((institution.getControlNumber() == null) || (orderedInstitutions.contains(institution)))
								orderedInstitutions.add(record);
						}
					}
				}
				for (Record record : orderedInstitutions) {
					RecordDTO temp = (RecordDTO)recordDAO.getDTO(record.getControlNumber());
					if(temp==null){
						record.loadDTOFromMARC21();
						record.loadMARC21FromDTO();
						indexer.add(record);
						System.out.println(record);
					}
				}
				records.clear();
				System.out.println("Institutions indexed!!!");
				
				//organizationUnits
				String[] typesOrganizationUnit = {"organizationUnit"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesOrganizationUnit);
				List<Record> orderedOrganizationUnits = new ArrayList<Record>();
				while(orderedOrganizationUnits.size() != records.size()){
					for (Record record : records) {
						if(!orderedOrganizationUnits.contains(record)){
							Record organizationUnit = new Record();
							for (RecordRecord recordRecord : record.getRelationsThisRecordOtherRecords()) {
								if((recordRecord.getCfClassSchemeId().equals("institutionsRelation")) && (recordRecord.getCfClassId().equals("is part of"))){
									organizationUnit.getMARC21Record().setControlNumber(recordRecord.getRecordId());
									if(! orderedInstitutions.contains(organizationUnit)){
										break;
									} else {
										organizationUnit = new Record();
									}
								}
							}
							if((organizationUnit.getControlNumber() == null) || (orderedOrganizationUnits.contains(organizationUnit)))
								orderedOrganizationUnits.add(record);
						}
					}
				}
				for (Record record : orderedOrganizationUnits) {
					RecordDTO temp = (RecordDTO)recordDAO.getDTO(record.getControlNumber());
					if(temp==null){
						record.loadDTOFromMARC21();
						record.loadMARC21FromDTO();
						indexer.add(record);
						System.out.println(record);
					}
					record.clear();
				}
				orderedInstitutions.clear();
				orderedOrganizationUnits.clear();
				records.clear();

				indexer.close();
				
				
				System.out.println("Organization Units indexed!!!");
				//authors
				String[] typesAuthor = {"author"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesAuthor);
				UserDB userDB = new UserDB();
				BulkIndexer bulkIndexer = new BulkIndexer(folderName, new CrisAnalyzer());
				RecordDAO recordDAOAuthor = new RecordDAO(new PersonDB());
				for (Record record : records) {
					try{
					AuthorDTO temp = (AuthorDTO)recordDAOAuthor.getDTO(record.getControlNumber());
					if(temp==null){
						record.loadDTOFromMARC21();
						record.loadMARC21FromDTO();
						if(userDB.getUserByAuthorControlNumber(conn, record.getControlNumber())!=null)
							((AuthorDTO)(record.getDto())).setAlreadyRegistered(true);
						bulkIndexer.add(record);
						System.out.println(record);
					}
					record.clear();
					} catch (Throwable e) {
						System.out.println("Puklo Reindeksiranje za autora sa id: "+record.getControlNumber());
						record.clear();
					}
				}
				records.clear();
				
				System.out.println("Authors indexed!!!"); 
				
				//conferences
				String[] typesConference = {"conference"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesConference);
				for (Record record : records) {
					RecordDTO temp = (RecordDTO)recordDAO.getDTO(record.getControlNumber());
					if(temp==null){
						record.loadDTOFromMARC21();
						record.loadMARC21FromDTO();
						bulkIndexer.add(record);
						System.out.println(record);
					}
					record.clear();
				}
				records.clear();
				
				System.out.println("Conferences indexed!!!");
				
				//proceedings
				String[] typesProceedings = {"proceedings"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesProceedings);
				for (Record record : records) {
					RecordDTO temp = (RecordDTO)recordDAO.getDTO(record.getControlNumber());
					if(temp==null){
						record.loadDTOFromMARC21();
						record.loadMARC21FromDTO();
						bulkIndexer.add(record);
						System.out.println(record);
					}
					record.clear();
				}
				records.clear();
				
				System.out.println("Proceedings indexed!!!");
				//paperProceedings
				String[] typesPaperProceedings = {"paperProceedings", "invTalkFullPP", "invTalkAbstractPP", "fullPP", "abstractPP", "discussionPP"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesPaperProceedings);
				for (Record record : records) {
					try{
						RecordDTO temp = (RecordDTO)recordDAO.getDTO(record.getControlNumber());
						if(temp==null){
							record.loadDTOFromMARC21();
							record.loadMARC21FromDTO();
							bulkIndexer.add(record);
							System.out.println(record);
						}
						record.clear();
					} catch (Throwable e) {
						System.out.println("Puklo Reindeksiranje za Paper Proceedings sa id: "+record.getControlNumber());
						record.clear();
					}
				}
				records.clear();
				
				System.out.println("Paper Proceedings indexed!!!"); 
				
				//journal
				String[] typesJournal = {"journal"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesJournal);
				for (Record record : records) {
					try {
						RecordDTO temp = (RecordDTO)recordDAO.getDTO(record.getControlNumber());
						if(temp==null){
							record.loadDTOFromMARC21();
							record.loadMARC21FromDTO();
							bulkIndexer.add(record);
							System.out.println(record);
						}
						record.clear();
					} catch (Throwable e) {
						System.out.println("Puklo Reindeksiranje za journal sa id: "+record.getControlNumber());
						record.clear();
					}
				}
				records.clear();
				
				System.out.println("Journal indexed!!!");
				//paperJournal
				String[] typesPaperJournal = {"paperJournal", "scientificCriticismJournal"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesPaperJournal);
				for (Record record : records) {
					try{
						RecordDTO temp = (RecordDTO)recordDAO.getDTO(record.getControlNumber());
						if(temp==null){
							record.loadDTOFromMARC21();
							record.loadMARC21FromDTO();
							bulkIndexer.add(record);
							System.out.println(record);
						}
						record.clear();
					} catch (Throwable e) {
						System.out.println("Puklo Reindeksiranje za paperJournal sa id: "+record.getControlNumber());
						record.clear();
					}
				}
				records.clear();
				
				System.out.println("Paper Journal indexed!!!");
				//monograph
				String[] typesMonograph = {"monograph"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesMonograph);
				for (Record record : records) {
					RecordDTO temp = (RecordDTO)recordDAO.getDTO(record.getControlNumber());
					if(temp==null){
						record.loadDTOFromMARC21();
						record.loadMARC21FromDTO();
						bulkIndexer.add(record);
						System.out.println(record);
					}
					record.clear();
				}
				records.clear();
				
				System.out.println("Monograph indexed!!!");
				//paperMonograph
				String[] typesPaperMonograph = {"paperMonograph"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesPaperMonograph);
				for (Record record : records) {
					RecordDTO temp = (RecordDTO)recordDAO.getDTO(record.getControlNumber());
					if(temp==null){
						record.loadDTOFromMARC21();
						record.loadMARC21FromDTO();
						bulkIndexer.add(record);
						System.out.println(record);
					}
					record.clear();
				}
				records.clear();
				
				System.out.println("Paper monograph indexed!!!");
				
				//patent
				String[] typesPatent = {"patent"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesPatent);
				for (Record record : records) {
					RecordDTO temp = (RecordDTO)recordDAO.getDTO(record.getControlNumber());
					if(temp==null){
						record.loadDTOFromMARC21();
						record.loadMARC21FromDTO();
						bulkIndexer.add(record);
						System.out.println(record);
					}
					record.clear();
				}
				records.clear();
				
				System.out.println("Patents indexed!!!");
				//product
				String[] typesProduct = {"product"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesProduct);
				for (Record record : records) {
					RecordDTO temp = (RecordDTO)recordDAO.getDTO(record.getControlNumber());
					if(temp==null){
						record.loadDTOFromMARC21();
						record.loadMARC21FromDTO();
						bulkIndexer.add(record);
						System.out.println(record);
					}
					record.clear();
				}
				records.clear();
				
				System.out.println("Product indexed!!!");
				
				//studyFinalDocument
				String[] typesStudyFinalDocument = {"studyFinalDocument", "bachelorStudyFinalDocument", "masterStudyFinalDocument", "oldBachelorStudyFinalDocument", "oldMasterStudyFinalDocument", "phdStudyFinalDocument", "phdArtProject", "specialisticStudyFinalDocument"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesStudyFinalDocument);
				for (Record record : records) {
					RecordDTO temp = (RecordDTO)recordDAO.getDTO(record.getControlNumber());
					if(temp==null){
						record.loadDTOFromMARC21();
						record.loadMARC21FromDTO();
						bulkIndexer.add(record);
						System.out.println(record);
					}
					record.clear();
				}
				records.clear();
				
				System.out.println("Study Final Documents indexed!!!");
				bulkIndexer.optimize();
				bulkIndexer.close();
				System.out.println("Second step finished!!!");	
			}
			if(thirdStep){
				RecordDB recordDB = new RecordDB();
				List<Record> records = null;
				RecordDAO recordDAO = new RecordDAO(new RecordDB());
				RegisterEntryDAO registerEntryDAO = new RegisterEntryDAO(new RegisterEntryDB());
				
				
				// institutions
				/*String[] typesInstitution = {"institution"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesInstitution);
				List<Record> orderedInstitutions = new ArrayList<Record>();
				while(orderedInstitutions.size() != records.size()){
					for (Record record : records) {
						if(!orderedInstitutions.contains(record)){
							Record institution = new Record();
							for (RecordRecord recordRecord : record.getRelationsThisRecordOtherRecords()) {
								if((recordRecord.getCfClassSchemeId().equals("institutionsRelation")) && (recordRecord.getCfClassId().equals("is part of"))){
									institution.getMARC21Record().setControlNumber(recordRecord.getRecordId());
									break;
								}
							}
							if((institution.getControlNumber() == null) || (orderedInstitutions.contains(institution)))
								orderedInstitutions.add(record);
						}
					}
				}
				for (Record record : orderedInstitutions) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					recordDAO.update(record);
				}
				records.clear();
				System.out.println("Institutions indexed!!!");
				
				//organizationUnits
				String[] typesOrganizationUnit = {"organizationUnit"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesOrganizationUnit);
				List<Record> orderedOrganizationUnits = new ArrayList<Record>();
				while(orderedOrganizationUnits.size() != records.size()){
					for (Record record : records) {
						if(!orderedOrganizationUnits.contains(record)){
							Record organizationUnit = new Record();
							for (RecordRecord recordRecord : record.getRelationsThisRecordOtherRecords()) {
								if((recordRecord.getCfClassSchemeId().equals("institutionsRelation")) && (recordRecord.getCfClassId().equals("is part of"))){
									organizationUnit.getMARC21Record().setControlNumber(recordRecord.getRecordId());
									if(! orderedInstitutions.contains(organizationUnit)){
										break;
									} else {
										organizationUnit = new Record();
									}
								}
							}
							if((organizationUnit.getControlNumber() == null) || (orderedOrganizationUnits.contains(organizationUnit)))
								orderedOrganizationUnits.add(record);
						}
					}
				}
				for (Record record : orderedOrganizationUnits) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					recordDAO.update(record);
				}
				orderedInstitutions.clear();
				orderedOrganizationUnits.clear();
				records.clear();
				
				System.out.println("Organization Units indexed!!!");  
				
				//authors
				String[] typesAuthor = {"author"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesAuthor);
				UserDB userDB = new UserDB();
				RecordDAO recordDAOAuthor = new RecordDAO(new PersonDB());
				System.out.println(records.size());
				int t=0;
				for (Record record : records) {
					try{
						record.loadDTOFromMARC21();
						record.loadMARC21FromDTO();
						if(userDB.getUserByAuthorControlNumber(conn, record.getControlNumber())!=null)
							((AuthorDTO)(record.getDto())).setAlreadyRegistered(true);
						recordDAOAuthor.update(record);
						record.clear();
						System.out.println(t++);
					} catch (Throwable e) {
						System.out.println("Puklo Reindeksiranje za autora sa id: "+record.getControlNumber());
						record.clear();
					}
				}
				records.clear();
				
				System.out.println("Authors indexed!!!");
				*/
				/*
				//conferences
				String[] typesConference = {"conference"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesConference);
				for (Record record : records) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					recordDAO.update(record);
					record.clear();
				}
				records.clear();
				
				System.out.println("Conferences indexed!!!");
				
				//proceedings
				String[] typesProceedings = {"proceedings"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesProceedings);
				for (Record record : records) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					recordDAO.update(record);
					record.clear();
				}
				records.clear();
				
				System.out.println("Proceedings indexed!!!");
				
				//paperProceedings
				String[] typesPaperProceedings = {"paperProceedings", "invTalkFullPP", "invTalkAbstractPP", "fullPP", "abstractPP", "discussionPP"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesPaperProceedings);
				for (Record record : records) {
					try{
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					recordDAO.update(record);
					record.clear();
					} catch (Throwable e) {
						System.out.println("Puklo Reindeksiranje za Paper Proceedings sa id: "+record.getControlNumber());
						record.clear();
					}
				}
				records.clear();
				
				System.out.println("Paper Proceedings indexed!!!"); 
				
				//journal
				String[] typesJournal = {"journal"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesJournal);
				for (Record record : records) {
					try {
						record.loadDTOFromMARC21();
						record.loadMARC21FromDTO();
						recordDAO.update(record);
						record.clear();
					} catch (Throwable e) {
						System.out.println("Puklo Reindeksiranje za journal sa id: "+record.getControlNumber());
						record.clear();
					}
				}
				records.clear();
				
				System.out.println("Journal indexed!!!");
				
				
				//paperJournal
				String[] typesPaperJournal = {"paperJournal", "scientificCriticismJournal"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesPaperJournal);
				for (Record record : records) {
					try{
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					recordDAO.update(record);
					record.clear();
					} catch (Throwable e) {
						System.out.println("Puklo Reindeksiranje za paperJournal sa id: "+record.getControlNumber());
						record.clear();
					}
				}
				records.clear();
				
				System.out.println("Paper Journal indexed!!!");
				//monograph
				String[] typesMonograph = {"monograph"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesMonograph);
				for (Record record : records) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					recordDAO.update(record);
					record.clear();
				}
				records.clear();
				
				System.out.println("Monograph indexed!!!");
				//paperMonograph
				String[] typesPaperMonograph = {"paperMonograph"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesPaperMonograph);
				for (Record record : records) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					recordDAO.update(record);
					record.clear();
				}
				records.clear();
				
				System.out.println("Paper monograph indexed!!!");
				
				//patent
				String[] typesPatent = {"patent"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesPatent);
				for (Record record : records) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					recordDAO.update(record);
					record.clear();
				}
				records.clear();
				
				System.out.println("Patents indexed!!!");
				//product
				String[] typesProduct = {"product"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesProduct);
				for (Record record : records) {
					record.loadDTOFromMARC21();
					record.loadMARC21FromDTO();
					recordDAO.update(record);
					record.clear();
				}
				records.clear();
				
				System.out.println("Product indexed!!!");
				*/
				
				//studyFinalDocument
				int counter = 1;
				String[] typesStudyFinalDocument = {"studyFinalDocument", "bachelorStudyFinalDocument", "masterStudyFinalDocument", "oldBachelorStudyFinalDocument", "oldMasterStudyFinalDocument", "phdStudyFinalDocument", "phdArtProject", "specialisticStudyFinalDocument"};
				records = recordDB.getAllRecordsOfCertainType(conn, typesStudyFinalDocument);
				for (int i=0; i < records.size(); i++) {
					Record record = records.get(i);
//					if((record.getFiles().size() > 0)){
						try {
							record.loadDTOFromMARC21();
							record.loadMARC21FromDTO();
							StudyFinalDocumentDTO dto = (StudyFinalDocumentDTO) record.getDto();
//							if((dto.getWordCloudImage() != null) && (dto.getWordCloudImage().getId() == 0))
//							if(dto.getDefendedOn() == null)	
//								if(dto.getPublicationDate() != null)
//									if(dto.getPublicationDate().get(Calendar.DAY_OF_YEAR) == 1){
//										Calendar cal=GregorianCalendar.getInstance();
//										cal.setTimeInMillis(dto.getPublicationDate().getTimeInMillis());
//										dto.setDefendedOn(cal);
//									}
							boolean changed = false;
							dto.loadRegisterEntry(true);
							if(dto.getDefendedOn() == null){
								if(dto.getRegisterEntry().getDefendedOn() != null){
									dto.setDefendedOn(dto.getRegisterEntry().getDefendedOn());
									changed = true;
								}
							} else {
								if(dto.getRegisterEntry().getDefendedOn() == null){
									dto.getRegisterEntry().setDefendedOn(dto.getDefendedOn());
									changed = true;
								}
							}
							if(dto.getRegisterEntry().getAdvisors() == null){
								StringBuffer advisorsBuffer = new StringBuffer("");
								for (AuthorDTO advisor : dto.getAdvisors()) {
									if(advisorsBuffer.length() > 0)
										advisorsBuffer.append("\n");
									if((advisor.getTitle()!=null) && (! advisor.getTitle().trim().equals("")))
										advisorsBuffer.append(advisor.getTitle() + " ");
									advisorsBuffer.append(advisor.getName().getFirstname() + " ");
									advisorsBuffer.append(advisor.getName().getLastname());
									boolean commaAdded = false;
									if(advisor.getCurrentPositionName() != null){
										commaAdded = true;
										if(advisor.getCurrentPositionName().equals("Docent")){
											advisorsBuffer.append(", доцент");
										} else if(advisor.getCurrentPositionName().equals("Vаnredni profesor")){
											advisorsBuffer.append(", ванр. проф.");
										} else if(advisor.getCurrentPositionName().equals("Profesor emeritus")){
											advisorsBuffer.append(", проф. емеритус");
										} else if(advisor.getCurrentPositionName().equals("Naučni - saradnik")){
											advisorsBuffer.append(", науч. сар.");
										} else if(advisor.getCurrentPositionName().equals("Viši naučni - saradnik")){
											advisorsBuffer.append(", виши науч. сар.");
										} else if(advisor.getCurrentPositionName().equals("Naučni savetnik")){
											advisorsBuffer.append(", науч. сав.");
										} else if(advisor.getCurrentPositionName().equals("Akademik")){
											advisorsBuffer.append(", академик.");
										} else {
											advisorsBuffer.append(", ред. проф.");
										} 
									}
									if(advisor.getInstitution().getSomeName()!=null){
										advisorsBuffer.append(((commaAdded)?(" "):(", ")) + advisor.getInstitution().getSomeName());
									}
								}
								String advisorsTemp = advisorsBuffer.toString();
								advisorsTemp = advisorsTemp.replace(" u Novom Sadu, Novi Sad", ", Novi Sad");
								advisorsTemp = advisorsTemp.replace(" u Novom Sadu", ", Novi Sad");
								dto.getRegisterEntry().setAdvisors(LatCyrUtils.toCyrillic(advisorsTemp));
								changed = true;
							}
							if (dto.getRegisterEntry().getCommissionMembers() == null){
								StringBuffer commissionBufferStart = new StringBuffer();
								StringBuffer commissionBufferEnd = new StringBuffer();
								StringBuffer commissionBuffer = commissionBufferStart;
								for (AuthorDTO committeeMember : dto.getCommitteeMembers()) {
									if(dto.getAdvisors().contains(committeeMember)){
										commissionBuffer = commissionBufferEnd;
										commissionBuffer.append(" ");
									} else
										commissionBuffer = commissionBufferStart;
									if(commissionBuffer.length() > 0)
										commissionBuffer.append("\n");
									if((committeeMember.getTitle()!=null) && (! committeeMember.getTitle().trim().equals("")))
										commissionBuffer.append(committeeMember.getTitle() + " ");
									commissionBuffer.append(committeeMember.getName().getFirstname() + " ");
									commissionBuffer.append(committeeMember.getName().getLastname());
									boolean commaAdded = false;
									if(committeeMember.getCurrentPositionName() != null){
										commaAdded = true;
										if(committeeMember.getCurrentPositionName().equals("Docent")){
											commissionBuffer.append(", доцент");
										} else if(committeeMember.getCurrentPositionName().equals("Vаnredni profesor")){
											commissionBuffer.append(", ванр. проф.");
										} else if(committeeMember.getCurrentPositionName().equals("Profesor emeritus")){
											commissionBuffer.append(", проф. емеритус");
										} else if(committeeMember.getCurrentPositionName().equals("Naučni - saradnik")){
											commissionBuffer.append(", науч. сар.");
										} else if(committeeMember.getCurrentPositionName().equals("Viši naučni - saradnik")){
											commissionBuffer.append(", виши науч. сар.");
										} else if(committeeMember.getCurrentPositionName().equals("Naučni savetnik")){
											commissionBuffer.append(", науч. сав.");
										} else if(committeeMember.getCurrentPositionName().equals("Akademik")){
											commissionBuffer.append(", академик");
										} else {
											commissionBuffer.append(", ред. проф.");
										} 
									}
									if(committeeMember.getInstitution().getSomeName()!=null){
										commissionBuffer.append(((commaAdded)?(" "):(", ")) + committeeMember.getInstitution().getSomeName());
									}
									if(dto.getAdvisors().contains(committeeMember)){
										commissionBuffer.append(", ментор и члан");
									} else if(dto.getCommitteeMembers().indexOf(committeeMember) == 0){
										commissionBuffer.append(", председник");
									}
								}
								
								commissionBuffer = commissionBufferEnd;
								if(dto.getAdvisors().size() != 0){
								for (AuthorDTO advisor : dto.getAdvisors()) {
									if(dto.getCommitteeMembers().contains(advisor))
										continue;
									if(commissionBufferStart.length() > 0)
										commissionBuffer.append("\n");
									if((advisor.getTitle()!=null) && (! advisor.getTitle().trim().equals("")))
										commissionBuffer.append(advisor.getTitle() + " ");
									commissionBuffer.append(advisor.getName().getFirstname() + " ");
									commissionBuffer.append(advisor.getName().getLastname());
									boolean commaAdded = false;
									if(advisor.getCurrentPositionName() != null){
										commaAdded = true;
										if(advisor.getCurrentPositionName().equals("Docent")){
											commissionBuffer.append(", доцент");
										} else if(advisor.getCurrentPositionName().equals("Vаnredni profesor")){
											commissionBuffer.append(", ванр. проф.");
										} else if(advisor.getCurrentPositionName().equals("Profesor emeritus")){
											commissionBuffer.append(", проф. емеритус");
										} else if(advisor.getCurrentPositionName().equals("Naučni - saradnik")){
											commissionBuffer.append(", науч. сар.");
										} else if(advisor.getCurrentPositionName().equals("Viši naučni - saradnik")){
											commissionBuffer.append(", виши науч. сар.");
										} else if(advisor.getCurrentPositionName().equals("Naučni savetnik")){
											commissionBuffer.append(", науч. сав.");
										} else if(advisor.getCurrentPositionName().equals("Akademik")){
											commissionBuffer.append(", академик");
										} else {
											commissionBuffer.append(", ред. проф.");
										} 
									}
									if(advisor.getInstitution().getSomeName()!=null){
										commissionBuffer.append(((commaAdded)?(" "):(", ")) + advisor.getInstitution().getSomeName());
									}
									commissionBuffer.append(", ментор и није члан");
								}
								} else {
									commissionBuffer.append("\nMентор: без ментора");
								}
								
								commissionBufferStart.append(commissionBufferEnd.toString());
								commissionBuffer = commissionBufferStart;
								String commissionTemp = commissionBuffer.toString();
								commissionTemp = commissionTemp.replace(" u Novom Sadu, Novi Sad", ", Novi Sad");
								commissionTemp = commissionTemp.replace(" u Novom Sadu", ", Novi Sad");
								dto.getRegisterEntry().setCommissionMembers(LatCyrUtils.toCyrillic(commissionTemp));
								changed = true;
							}
							if(changed){
								registerEntryDAO.update(dto.getRegisterEntry());
								recordDAO.update(record);
								System.out.println(dto.getRegisterEntry().getHTMLRepresentation());
							}
							
						} catch (Exception e) {
							System.out.println(record.getControlNumber());
							e.printStackTrace();
						}
//					}
					record.clear();
					records.set(i, null);
				}
				records.clear();
				
				System.out.println("Study Final Documents indexed!!!"); 
				
				
				
				
				recordDAO.optimizeIndexer(); 
				System.out.println("Third step finished!!!");	
			}
			
			log.info("Indexing has been successfully finished!");
			return true;
		} else {
			log.error("Indexing has not been finished!");
			return false;
		}
	}
	
	private String folderName;
	private Connection conn;
	private boolean firstStep;
	private boolean secondStep;
	private boolean thirdStep;
	
	private static Log log = LogFactory.getLog(IndexTask.class.getName());

}
