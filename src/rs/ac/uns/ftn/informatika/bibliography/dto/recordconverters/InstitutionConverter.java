package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.ResearchAreaDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Subfield;

/**
 * Class for converting authority mARC21Record between MARC21 format and InstitutionDTO
 * object.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class InstitutionConverter extends ANormativeRecordConverter {

	/**
	 * Convert the mARC21Record with data about Institution from MARC21 format to DTO
	 * format
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
		InstitutionDTO retVal = (InstitutionDTO)rec.getDto();
		retVal.setControlNumber(mARC21Record.getControlNumber());
		retVal.setScopusID(rec.getScopusID());
		retVal.setORCID(rec.getORCID());
		String originalLanguage = MultilingualContentDTO.LANGUAGE_SERBIAN;
		DataField df040 = mARC21Record.getDataField("040");
		try {
			Subfield b = df040.getSubfield('b');
			originalLanguage = b.getContent();
		} catch (Exception e) {
		}
		
		DataField df110 = mARC21Record.getDataField("110");
		Subfield sub6 = df110.getSubfield('6');
		String ordNum = null;
		if(sub6 != null){
			ordNum = sub6.getContent().split("\\-")[1];
		}
		
		if (df110.getInd1() == '2') {
			try {
				Subfield a = df110.getSubfield('a');
				retVal.getName().setLanguage(originalLanguage);
				retVal.getName().setContent(a.getContent());
			} catch (Exception e) {
			}
			try {
				Subfield c = df110.getSubfield('c');
				retVal.setPlace(c.getContent());
			} catch (Exception e) {
			}
		} else
			return false;
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> nameTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("110-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						String content = a.getContent().trim();
						String transType = content.substring(0, 1);
						String language = content.substring(2, 5);
						String transContent = content.substring(6);
						MultilingualContentDTO nameTranslation = new MultilingualContentDTO(transContent, language, transType);
						nameTranslations.add(nameTranslation);
					}
				} catch (Exception e) {
				}
			}
			retVal.setNameTranslations(nameTranslations);
		}
		
//		try {
//			ordNum = null;
//			DataField df372 = mARC21Record.getDataField("372");
//			sub6 = df372.getSubfield('6');
//			if(sub6 != null){
//				ordNum = sub6.getContent().split("\\-")[1];
//			}
//			Subfield a = df372.getSubfield('a');
//			if ((a!=null) && (a.getContent()!=null)){
//				retVal.getResearchActivity().setLanguage(originalLanguage);
//				retVal.getResearchActivity().setContent(a.getContent());
//			}
//		} catch (Exception e) {
//		}
		
//		if(ordNum != null){
//			List<DataField> dfs880 = mARC21Record.getDataFields("880");
//			List<MultilingualContentDTO> resActTranslations = new ArrayList<MultilingualContentDTO>();
//			for (DataField dataField : dfs880) {
//				try {
//					if (dataField.getSubfield('6').getContent().equals("372-"+ordNum)) {
//						Subfield a = dataField.getSubfield('a');
//						if(a!=null){
//							String content = a.getContent().trim();
//							String transType = content.substring(0, 1);
//							String language = content.substring(2, 5);
//							String transContent = content.substring(6);
//							MultilingualContentDTO resActTranslation = new MultilingualContentDTO(transContent, language, transType);
//							resActTranslations.add(resActTranslation);
//						}
//					}
//				} catch (Exception e) {
//				}
//			}
//			retVal.setResearchActivityTranslations(resActTranslations);
//		}

		DataField df410 = mARC21Record.getDataField("410");
		if (df410 != null) {
			if (df410.getInd1() == '2') {
				try {
					Subfield a = df410.getSubfield('a');
					if ((a!=null) && (a.getContent()!=null))
						retVal.setAcro(a.getContent());
				} catch (Exception e) {
				}
			}
		}
		DataField df710 = mARC21Record.getDataField("710");
		if(df710 != null){
			if (df710.getInd1() == '2') {
				RecordDAO recordDAO = new RecordDAO(new RecordDB());
				InstitutionDTO superInstitutionDTO = (InstitutionDTO) recordDAO
						.getDTO(df710.getSubfield('0').getContent());
				if(superInstitutionDTO!=null)
					retVal.setSuperInstitution(superInstitutionDTO);
			}
		}
		
		DataField df856 = mARC21Record.getDataField("856");
		try {
			Subfield u = df856.getSubfield('u');
			if ((u!=null) && (u.getContent()!=null)){
				retVal.setUri(u.getContent());
			}
		} catch (Exception e) {
		}
		
		for (MultilingualContentDTO keywords : rec.getRecordKeywords()) {
			if(keywords.getTransType().equals(MultilingualContentDTO.TRANS_ORIGINAL)){
				retVal.setKeywords(keywords);
			} else {
				retVal.getKeywordsTranslations().add(keywords);
			}	
		}
		List<Classification> recordClasses = rec.getRecordClasses();
		ResearchAreaDAO researchAreaDAO = new ResearchAreaDAO();
		for (Classification classification : recordClasses) {
			if (classification.getCfClassSchemeId().equalsIgnoreCase(ResearchAreaDTO.RESEARCH_AREA_SCHEMA)){
				retVal.getResearchAreas().add(researchAreaDAO.getResearchArea(classification.getCfClassId()));
			}
		}	
		return true;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ANormativeRecordConverter#setDataFields(rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record,
	 *      rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setDataFields(MARC21Record rec, RecordDTO dto) {
		
		Integer ordNum = 0;
		
		InstitutionDTO institutionDTO = (InstitutionDTO) dto;

		DataField data040 = new DataField("040", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		Subfield b = new Subfield('b');
		if(institutionDTO.getName().getLanguage() != null)
			b.setContent(institutionDTO.getName().getLanguage());
		else 
			b.setContent(MultilingualContentDTO.LANGUAGE_SERBIAN);
		data040.addSubfield(b);
		rec.addDataField(data040);
		
		DataField data110 = new DataField("110", '2', AbstractRecordConverter.BLANK);
		Subfield sub6 = new Subfield('6');
		if(institutionDTO.getNameTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data110.addSubfield(sub6);
		}
		StringBuffer tempContent = new StringBuffer();
		Subfield a = new Subfield('a');
		if ((institutionDTO.getName().getContent() != null)
				&& (!"".equals(institutionDTO.getName().getContent())))
			tempContent.append(institutionDTO.getName().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data110.addSubfield(a);
		}
		tempContent = new StringBuffer();
		Subfield c = new Subfield('c');
		if ((institutionDTO.getPlace() != null)
				&& (!"".equals(institutionDTO.getPlace())))
			tempContent.append(institutionDTO.getPlace());
		if (tempContent.toString().length() > 0) {
			c.setContent(tempContent.toString());
			data110.addSubfield(c);
		}
		if(data110.getSubfieldCount() > 0)
			rec.addDataField(data110);
		
		for (MultilingualContentDTO nameTranslation : institutionDTO.getNameTranslations()) {
			DataField data880 = new DataField("880", '2', AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("110-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(nameTranslation.getTransType() + "-" + nameTranslation.getLanguage() + ":" + nameTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		for (ResearchAreaDTO researchArea : institutionDTO.getResearchAreas()) {
			DataField data372 = new DataField("372", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
			sub6 = new Subfield('6');
			if(researchArea.getTermTranslations().size() > 0){
				ordNum++;
				sub6.setContent("880-0" + ordNum);
				data372.addSubfield(sub6);
			}
			tempContent = new StringBuffer();
			a = new Subfield('a');
			if ((researchArea.getTerm().getContent() != null)
					&& (!"".equals(researchArea.getTerm().getContent())))
				tempContent.append(researchArea.getTerm().getContent());
			if (tempContent.toString().length() > 0) {
				a.setContent(tempContent.toString());
				data372.addSubfield(a);
			}
			if(data372.getSubfieldCount() > 0)
				rec.addDataField(data372);
			
			for (MultilingualContentDTO resAreTranslation : researchArea.getTermTranslations()) {
				DataField data880 = new DataField("880", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
				tempContent = new StringBuffer();
				sub6 = new Subfield('6');
				sub6.setContent("372-0" + ordNum);
				data880.addSubfield(sub6);
				a = new Subfield('a');
				a.setContent(resAreTranslation.getTransType() + "-" + resAreTranslation.getLanguage() + ":" + resAreTranslation.getContent());
				data880.addSubfield(a);
				rec.addDataField(data880);
			}
			
		}
		
		DataField data410 = new DataField("410", '2',
				AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((institutionDTO.getAcro() != null)
				&& (!"".equals(institutionDTO.getAcro())))
			tempContent.append(institutionDTO.getAcro());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data410.addSubfield(a);
		}
		if(data410.getSubfieldCount() > 0)
			rec.addDataField(data410);
		
		DataField data710 = new DataField("710", '2', AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		InstitutionDTO superInstitutionDTO = institutionDTO.getSuperInstitution();
		if(superInstitutionDTO!=null){
			Subfield sub0 = new Subfield('0');
			if ((superInstitutionDTO.getControlNumber() != null)
					&& (!"".equals(superInstitutionDTO.getControlNumber())))
				tempContent.append(superInstitutionDTO.getControlNumber());
			if (tempContent.toString().length() > 0) {
				sub0.setContent(tempContent.toString());
				data710.addSubfield(sub0);
			}
			tempContent = new StringBuffer();
			a = new Subfield('a');
			if ((superInstitutionDTO.getName().getContent() != null)
					&& (!"".equals(superInstitutionDTO.getName().getContent())))
				tempContent.append(superInstitutionDTO.getName().getContent());
			if (tempContent.toString().length() > 0) {
				a.setContent(tempContent.toString());
				data710.addSubfield(a);
			}
			tempContent = new StringBuffer();
			c = new Subfield('c');
			if ((superInstitutionDTO.getPlace() != null)
					&& (!"".equals(superInstitutionDTO.getPlace())))
				tempContent.append(superInstitutionDTO.getPlace());
			if (tempContent.toString().length() > 0) {
				c.setContent(tempContent.toString());
				data710.addSubfield(c);
			}
			if(data710.getSubfieldCount() > 0)
				rec.addDataField(data710);
		}
		DataField data856 = new DataField("856", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		Subfield u = new Subfield('u');
		if ((institutionDTO.getUri() != null)
				&& (!"".equals(institutionDTO.getUri())))
			tempContent.append(institutionDTO.getUri());
		if (tempContent.toString().length() > 0) {
			u.setContent(tempContent.toString());
			data856.addSubfield(u);
		}
		if(data856.getSubfieldCount() > 0)
			rec.addDataField(data856);
		
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ANormativeRecordConverter#setRelationsOtherRecordsThisRecord(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsOtherRecordsThisRecord(Record retVal,
			RecordDTO dto) {
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsThisRecordOtherRecords(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsThisRecordOtherRecords(Record retVal,
			RecordDTO dto) {
		InstitutionDTO institution = (InstitutionDTO) dto;
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		deleteRelationsThisRecordOtherRecords(retVal, "institutionsRelation", "is part of");
		
		List<RecordRecord> recordRecords = retVal.getRelationsThisRecordOtherRecords();
		if((institution.getSuperInstitution() != null) && (institution.getSuperInstitution().getControlNumber() != null)){
			recordRecords.add(new RecordRecord(institution.getSuperInstitution().getControlNumber(), "institutionsRelation", "is part of", startDate, endDate));
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ANormativeRecordConverter#setRecordKeywords(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRecordKeywords(Record retVal, RecordDTO dto) {
		InstitutionDTO institution = (InstitutionDTO)dto;
		retVal.setRecordKeywords(new ArrayList<MultilingualContentDTO>());
		if((institution.getKeywords().getContent() != null) && (institution.getKeywords().getContent().trim().length() > 0)){
			if(institution.getKeywords().getLanguage() == null)
				institution.getKeywords().setLanguage(institution.getName().getLanguage());
			retVal.getRecordKeywords().add(institution.getKeywords());
		}
		for (MultilingualContentDTO keywords : institution.getKeywordsTranslations()) {
			retVal.getRecordKeywords().add(keywords);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.AbstractRecordConverter#setRecordClasses(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRecordClasses(Record retVal, RecordDTO dto) {
		super.setRecordClasses(retVal, dto);
		List<Classification> researchAreas = new ArrayList<Classification>();
		for (Classification classification : retVal.getRecordClasses()) {
			if(classification.getCfClassSchemeId().equalsIgnoreCase(ResearchAreaDTO.RESEARCH_AREA_SCHEMA)){
				researchAreas.add(classification);
			}	
		}	
		
		for (Classification researchArea : researchAreas) {
			retVal.getRecordClasses().remove(researchArea);
		}
		
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		for (ResearchAreaDTO ra : ((InstitutionDTO)dto).getResearchAreas()) {
			
			String institutionResearchArea = ra.getClassId();
			
			if(institutionResearchArea != null){
				Classification recClass = new Classification(ResearchAreaDTO.RESEARCH_AREA_SCHEMA, institutionResearchArea, startDate, endDate, new Integer(0), null);
				retVal.getRecordClasses().add(recClass);
			}
		}
	}
	
	

}
