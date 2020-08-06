package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JobAdDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordResultPublication;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.ControlField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Subfield;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * Class for converting bibliographic mARC21Record between MARC21 format and
 * JournalDTO object.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class JournalConverter extends ABibliographicRecordConverter {

	/**
	 * Converts the mARC21Record with bibliographic data about Journal from MARC21 format to DTO format
	 * 
	 * @param rec
	 *            MARC21Record which should be converted
	 * 
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.AbstractRecordConverter#getDTO(rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record)
	 * 
	 * @return true if everything is ok, otherwise false
	 */
	@Override
	public boolean getDTO(Record rec){
		
		MARC21Record mARC21Record = rec.getMARC21Record();
		if(rec.getDto() == null)
			rec.setDto(ConverterFactory.getDTO(rec.getDtoType()));
		JournalDTO retVal = (JournalDTO)rec.getDto();
		retVal.setControlNumber(mARC21Record.getControlNumber());
		retVal.setScopusID(rec.getScopusID());
		
		String originalLanguage = MultilingualContentDTO.LANGUAGE_SERBIAN;
		try{
			ControlField cf008 = mARC21Record.getControlField("008");
			originalLanguage = cf008.getCharPositions(35, 3);
		} catch (Exception e){
		}
			
		List<DataField> dfs041 = mARC21Record.getDataFields("041");
		List<String> languageList = new ArrayList<String>();
		for (DataField dataField : dfs041) {
			try {
				if (dataField.getInd1() == '0') {
					languageList.add(dataField.getSubfield('a').getContent());
				}
			} catch (Exception e) {
			}
		}
		retVal.setLanguages(languageList);
		DataField df130 = mARC21Record.getDataField("130");
		Subfield sub6 = df130.getSubfield('6');
		String ordNum = null;
		if(sub6 != null){
			ordNum = sub6.getContent().split("\\-")[1];
		}
		if (df130.getInd2() == '0') {
			try {
				Subfield a = df130.getSubfield('a');
				retVal.getName().setLanguage(originalLanguage);
				retVal.getName().setContent(a.getContent());
			} catch (Exception e) {
			}
		} else
			return false;
		
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> nameTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("130-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						if(a!=null){
							String content = a.getContent().trim();
							String transType = content.substring(0, 1);
							String language = content.substring(2, 5);
							String transContent = content.substring(6);
							MultilingualContentDTO nameTranslation = new MultilingualContentDTO(transContent, language, transType);
							nameTranslations.add(nameTranslation);
						}
					}
				} catch (Exception e) {
				}
			}
			retVal.setNameTranslations(nameTranslations);
		}
		
		DataField df022 = mARC21Record.getDataField("022");
		try {
			Subfield a = df022.getSubfield('a');
			retVal.setIssn(a.getContent());
		} catch (Exception e) {
		}
		
		ordNum = null;
		try {
			DataField df210 = mARC21Record.getDataField("210");
			sub6 = df210.getSubfield('6');
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}
		
			if (df210.getInd1() == '0') {
				Subfield a = df210.getSubfield('a');
				retVal.getNameAbbreviation().setLanguage(originalLanguage);
				retVal.getNameAbbreviation().setContent(a.getContent());
			} 
		} catch (Exception e) {
		}
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> nameAbbrevTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("210-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						String content = a.getContent().trim();
						String transType = content.substring(0, 1);
						String language = content.substring(2, 5);
						String transContent = content.substring(6);
						MultilingualContentDTO nameAbbrevTranslation = new MultilingualContentDTO(transContent, language, transType);
						nameAbbrevTranslations.add(nameAbbrevTranslation);
					}
				} catch (Exception e) {
				}
			}
			retVal.setNameAbbreviationTranslations(nameAbbrevTranslations);
		}
		
		
		
		try {
			ordNum = null;
			DataField df500 = mARC21Record.getDataField("500");
			sub6 = df500.getSubfield('6');
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}
		
			Subfield a = df500.getSubfield('a');
			retVal.getNote().setLanguage(originalLanguage);
			retVal.getNote().setContent(a.getContent());
		} catch (Exception e) {
		}	 
		
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> noteTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("500-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						String content = a.getContent().trim();
						String transType = content.substring(0, 1);
						String language = content.substring(2, 5);
						String transContent = content.substring(6);
						MultilingualContentDTO noteTranslation = new MultilingualContentDTO(transContent, language, transType);
						noteTranslations.add(noteTranslation);
					}
				} catch (Exception e) {
				}
			}
			retVal.setNoteTranslations(noteTranslations);
		}
		
		try {
			ordNum = null;
			DataField df653 = mARC21Record.getDataField("653");
			sub6 = df653.getSubfield('6');
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}
		
			Subfield a = df653.getSubfield('a');
			retVal.getKeywords().setLanguage(originalLanguage);
			retVal.getKeywords().setContent(a.getContent());
		} catch (Exception e) {
		}		 
		
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> keywordsTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("653-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						String content = a.getContent().trim();
						String transType = content.substring(0, 1);
						String language = content.substring(2, 5);
						String transContent = content.substring(6);
						MultilingualContentDTO keywordsTranslation = new MultilingualContentDTO(transContent, language, transType);
						keywordsTranslations.add(keywordsTranslation);
					}
				} catch (Exception e) {
				}
			}
			retVal.setKeywordsTranslations(keywordsTranslations);
		}
		

		List<DataField> dfs700 = mARC21Record.getDataFields("700");
		List<AuthorDTO> editorDTOs = new ArrayList<AuthorDTO>();
		for (DataField dataField : dfs700) {
			try {
				if (dataField.getInd1() == '1') {
					RecordDAO recordDAO = new RecordDAO(new PersonDB());
					AuthorDTO authorDTO = (AuthorDTO) recordDAO
							.getDTO(dataField.getSubfield('0').getContent());
					Subfield a = dataField.getSubfield('a');
					String content = a.getContent().trim();
					String lastName = Sanitizer.nextPar(content, ',');
					content = Sanitizer.remPar(content, ',');
					String firstName = Sanitizer.nextPar(content, ',');
					AuthorNameDTO choosedAuthorNameDTO = new AuthorNameDTO(firstName, lastName, "");
					if(authorDTO.getAllNames().contains(choosedAuthorNameDTO))
						authorDTO.setName(choosedAuthorNameDTO);
					if (authorDTO != null)
						editorDTOs.add(authorDTO);
				}
			} catch (Exception e) {
			}
		}
		retVal.setEditors(editorDTOs);
		DataField df856 = mARC21Record.getDataField("856");
		try {
			List<Subfield> sfsu = df856.getSubfields('u');
			for (Subfield  u: sfsu) {
				if ((u!=null) && (u.getContent()!=null)){
					if(u.getContent().trim().toLowerCase().startsWith("doi:"))
						retVal.setDoi(u.getContent().trim().substring(4).trim());
					else
						retVal.setUri(u.getContent().trim());
				}
			}
		} catch (Exception e) {
		}
		
		try{
			List<RecordRecord> recordRecords = rec.getRelationsThisRecordOtherRecords();
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			for (RecordRecord recordRecord : recordRecords) {
				if(recordRecord.getCfClassSchemeId().equalsIgnoreCase("publicationJobAd") && (recordRecord.getCfClassId().equalsIgnoreCase("applied to"))){
					JobAdDTO jobAd = (JobAdDTO)recordDAO.getDTO(recordRecord.getRecordId());
					if(jobAd != null)
						retVal.getJobAds().add(jobAd);
				}
			}	
		}catch (Exception e) {
		}
		
		return true;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setDataFields(rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record,
	 *      rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setDataFields(MARC21Record rec, RecordDTO dto) {

		Integer ordNum = 0;
		
		JournalDTO journalDTO = (JournalDTO) dto;
		StringBuffer tempContent = new StringBuffer();
		
		
		for (String language : journalDTO.getLanguages()) {
			DataField data041 = new DataField("041", '0',
					AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			Subfield a = new Subfield('a');
			if ((language != null) && (!"".equals(language)))
				tempContent.append(language);
			if (tempContent.toString().length() > 0) {
				a.setContent(tempContent.toString());
				data041.addSubfield(a);
			}
			if(data041.getSubfieldCount() > 0)
				rec.addDataField(data041);
		}

		DataField data130 = new DataField("130", AbstractRecordConverter.BLANK, '0');
		tempContent = new StringBuffer();
		Subfield sub6 = new Subfield('6');
		if(journalDTO.getNameTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data130.addSubfield(sub6);
		}
		Subfield a = new Subfield('a');
		if ((journalDTO.getName().getContent() != null)
				&& (!"".equals(journalDTO.getName().getContent())))
			tempContent.append(journalDTO.getName().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data130.addSubfield(a);
		}
		if(data130.getSubfieldCount() > 0)
			rec.addDataField(data130);
		
		for (MultilingualContentDTO nameTranslation : journalDTO.getNameTranslations()) {
			DataField data880 = new DataField("880", AbstractRecordConverter.BLANK, '0');
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("130-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(nameTranslation.getTransType() + "-" + nameTranslation.getLanguage() + ":" + nameTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		DataField data022 = new DataField("022", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((journalDTO.getIssn() != null)
				&& (!"".equals(journalDTO.getIssn().trim())))
			tempContent.append(journalDTO.getIssn());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data022.addSubfield(a);
		}
		if(data022.getSubfieldCount() > 0)
		rec.addDataField(data022);
		
		DataField data210 = new DataField("210", '0', AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(journalDTO.getNameAbbreviationTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data210.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((journalDTO.getNameAbbreviation().getContent() != null)
				&& (!"".equals(journalDTO.getNameAbbreviation().getContent())))
			tempContent.append(journalDTO.getNameAbbreviation().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data210.addSubfield(a);
		}
		if(data210.getSubfieldCount() > 0)
			rec.addDataField(data210);
		for (MultilingualContentDTO nameAbbrevTranslation : journalDTO.getNameAbbreviationTranslations()) {
			DataField data880 = new DataField("880", '0', AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("210-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(nameAbbrevTranslation.getTransType() + "-" + nameAbbrevTranslation.getLanguage() + ":" + nameAbbrevTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		DataField data500 = new DataField("500", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(journalDTO.getNoteTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data500.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((journalDTO.getNote().getContent() != null)
				&& (!"".equals(journalDTO.getNote().getContent())))
			tempContent.append(journalDTO.getNote().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data500.addSubfield(a);
		}
		if(data500.getSubfieldCount() > 0)
			rec.addDataField(data500);
		
		for (MultilingualContentDTO noteTranslation : journalDTO.getNoteTranslations()) {
			DataField data880 = new DataField("880", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("500-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(noteTranslation.getTransType() + "-" + noteTranslation.getLanguage() + ":" + noteTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		DataField data653 = new DataField("653", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(journalDTO.getKeywordsTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data653.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((journalDTO.getKeywords().getContent() != null)
				&& (!"".equals(journalDTO.getKeywords().getContent())))
			tempContent.append(journalDTO.getKeywords().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data653.addSubfield(a);
		}
		if(data653.getSubfieldCount() > 0)
			rec.addDataField(data653);
		
		for (MultilingualContentDTO keywordsTranslation : journalDTO.getKeywordsTranslations()) {
			DataField data880 = new DataField("880", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("653-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(keywordsTranslation.getTransType() + "-" + keywordsTranslation.getLanguage() + ":" + keywordsTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		for (AuthorDTO author : journalDTO.getEditors()) {
			DataField data700 = new DataField("700", '1',
					AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			Subfield sub0 = new Subfield('0');
			sub0.setContent(author.getControlNumber());
			data700.addSubfield(sub0);
			a = new Subfield('a');
			AuthorNameDTO authorName = author.getName();
			if ((authorName.getLastname() != null)
					&& (!"".equals(authorName.getLastname())))
				tempContent.append(Sanitizer.sanitize(authorName.getLastname()));
			if ((authorName.getFirstname() != null)
					&& (!"".equals(authorName.getFirstname())))
				tempContent.append(", " + Sanitizer.sanitize(authorName.getFirstname()));
			if (tempContent.toString().length() > 0) {
				a.setContent(tempContent.toString());
				data700.addSubfield(a);
			}
			tempContent = new StringBuffer();
			Subfield u = new Subfield('u');
			InstitutionDTO authorInstitution = author.getInstitution();
			OrganizationUnitDTO authorOrganizationUnit = author.getOrganizationUnit();
			if(authorOrganizationUnit.getControlNumber() != null)
				tempContent.append(authorOrganizationUnit.getSomeName());
			else if (authorInstitution.getControlNumber() != null)
				tempContent.append(authorInstitution.getSomeName());
			if (tempContent.toString().length() > 0) {
				u.setContent(tempContent.toString());
				data700.addSubfield(u);
			}
			if(data700.getSubfieldCount() > 0)
				rec.addDataField(data700);
		}
		DataField data856 = new DataField("856", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		Subfield u = new Subfield('u');
		if ((journalDTO.getUri() != null)
				&& (!"".equals(journalDTO.getUri())))
			tempContent.append(journalDTO.getUri());
		if (tempContent.toString().length() > 0) {
			u.setContent(tempContent.toString());
			data856.addSubfield(u);
		}
		tempContent = new StringBuffer();
		u = new Subfield('u');
		if ((journalDTO.getDoi() != null)
				&& (!"".equals(journalDTO.getDoi())))
			tempContent.append("doi:"+journalDTO.getDoi().trim());
		if (tempContent.toString().length() > 0) {
			u.setContent(tempContent.toString());
			data856.addSubfield(u);
		}
		if(data856.getSubfieldCount() > 0)
			rec.addDataField(data856);
		
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsOtherRecordsThisRecord(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsOtherRecordsThisRecord(Record retVal,
			RecordDTO dto) {
		JournalDTO journal = (JournalDTO) dto;
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		deleteRelationsOtherRecordsThisRecord(retVal, "authorshipType", "is editor of");
		
		List<RecordRecord> recordRecords = retVal.getRelationsOtherRecordsThisRecord();
		for (AuthorDTO editor : journal.getEditors()) {
			recordRecords.add(new RecordResultPublication(editor.getControlNumber(), "authorshipType", "is editor of", startDate, endDate, null));
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsThisRecordOtherRecords(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsThisRecordOtherRecords(Record retVal,
			RecordDTO dto) {
		JournalDTO journal = (JournalDTO) dto;
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		deleteRelationsThisRecordOtherRecords(retVal, "publicationJobAd", "applied to");
		
		List<RecordRecord> recordRecords = retVal.getRelationsThisRecordOtherRecords();
		
		for (JobAdDTO jobAd : journal.getJobAds()) {
			recordRecords.add(new RecordRecord(jobAd.getControlNumber(), "publicationJobAd", "applied to", startDate, endDate));
		}
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeyu.ac.ns.ftn.informatika.bibliography.dto.recordconverters.
	 * ABibliographicRecordConverter
	 * #getControlField006(rs.ac.uns.ftn.informatika
	 * .bibliography.dto.ARecordDTO)
	 */
	@Override
	protected ControlField getControlField006(RecordDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeyu.ac.ns.ftn.informatika.bibliography.dto.recordconverters.
	 * ABibliographicRecordConverter
	 * #getControlField007(rs.ac.uns.ftn.informatika
	 * .bibliography.dto.ARecordDTO)
	 */
	@Override
	protected ControlField getControlField007(RecordDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 * 
	 * @see yu.ac.ns.ftn.informatika.bibliography.dto.recordconverters.
	 * ABibliographicRecordConverter
	 * #getControlField008(rs.ac.uns.ftn.informatika
	 * .bibliography.dto.ARecordDTO)
	 */
	@Override
	protected ControlField getControlField008(RecordDTO dto) {
		JournalDTO journalDTO = (JournalDTO) dto;
		ControlField retVal = new ControlField("008");
		String content = "";
		for(int i=0;i<35;i++)
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		if(journalDTO.getName().getLanguage() != null)
			content += journalDTO.getName().getLanguage();
		else 
			content += MultilingualContentDTO.LANGUAGE_SERBIAN;
		content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		retVal.setContent(content);
		return retVal;
	}
}
