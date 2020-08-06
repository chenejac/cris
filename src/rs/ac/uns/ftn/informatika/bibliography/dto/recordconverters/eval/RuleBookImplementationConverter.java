package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.eval;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.ResultsTypeGroupDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeGroupDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookImplementationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.AbstractRecordConverter;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ConverterFactory;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.ControlField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Subfield;

/**
 * Class for converting bibliographic mARC21Record between MARC21 format and
 * RuleBookImplementationDTO object.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 */
@SuppressWarnings("serial")
public class RuleBookImplementationConverter extends
		ABibliographicRecordConverter {

	/**
	 * Converts the mARC21Record with bibliographic data about RuleBook from MARC21 format to DTO format
	 * 
	 * @param rec
	 *            MARC21Record which should be converted
	 * 
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.AbstractRecordConverter#getDTO(rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record)
	 * 
	 * @return true if everything is ok, otherwise false
	 */
	@Override
	public boolean getDTO(Record rec) {
		
//		System.out.println("RuleBookImplementationConverter - getDTO - 1");
		
		MARC21Record mARC21Record = rec.getMARC21Record();
		if(rec.getDto() == null)
			rec.setDto(ConverterFactory.getDTO(rec.getDtoType()));
		
		RuleBookImplementationDTO retVal = (RuleBookImplementationDTO)rec.getDto();
		retVal.setControlNumber(mARC21Record.getControlNumber());
		
//		System.out.println("RuleBookImplementationConverter - getDTO - 2");
		
		retVal.setFiles(rec.getFiles());
		retVal.setDeletedFiles(rec.getDeletedFiles());
		
//		System.out.println("RuleBookImplementationConverter - getDTO - 3");
		
		String originalLanguage = MultilingualContentDTO.LANGUAGE_SERBIAN;
		try{
			ControlField cf008 = mARC21Record.getControlField("008");
			originalLanguage = cf008.getCharPositions(35, 3);
//			System.out.println("RuleBookImplementationConverter - getDTO - 4");
		} catch (Exception e){
			
		}

		DataField df046 = mARC21Record.getDataField("046");
		try {
			Subfield m = df046.getSubfield('m');
			String dateString = m.getContent();
			DateFormat formatter ; 
		    Date date ; 
		    formatter = new SimpleDateFormat("dd.MM.yyyy");
		    date = (Date)formatter.parse(dateString); 
		    Calendar cal=GregorianCalendar.getInstance();
		    cal.setTime(date);
			retVal.setStartDate(cal);
//			System.out.println("RuleBookImplementationConverter - getDTO - 5");
		} catch (Exception e) {
			try {
				Subfield m = df046.getSubfield('m');
				String yearString = m.getContent();
				Calendar cal = new GregorianCalendar();
				cal.set(Calendar.YEAR, Integer.parseInt(yearString));
				cal.set(Calendar.DAY_OF_YEAR, 1);
				retVal.setStartDate(cal);
			} catch (Exception ex) {
			}
		}
		
		try {
			Subfield n = df046.getSubfield('n');
			String dateString = n.getContent();
			DateFormat formatter ; 
		    Date date ; 
		    formatter = new SimpleDateFormat("dd.MM.yyyy");
		    date = (Date)formatter.parse(dateString); 
		    Calendar cal=GregorianCalendar.getInstance();
		    cal.setTime(date);
			retVal.setEndDate(cal);
//			System.out.println("RuleBookImplementationConverter - getDTO - 6");
		} catch (Exception e) {
			try {
				Subfield n = df046.getSubfield('n');
				String yearString = n.getContent();
				Calendar cal = new GregorianCalendar();
				cal.set(Calendar.YEAR, Integer.parseInt(yearString));
				cal.set(Calendar.DAY_OF_YEAR, 1);
				retVal.setStartDate(cal);
			} catch (Exception ex) {
			}
		}
		
		DataField df084 = mARC21Record.getDataField("084");
		try {
			Subfield b = df084.getSubfield('b');
			retVal.setRuleBookUserCode(b.getContent());
//			System.out.println("RuleBookImplementationConverter - getDTO - 7");
		} catch (Exception e) {
		}
		
		DataField df245 = mARC21Record.getDataField("245");
		String ordNum = null;
//		System.out.println("RuleBookImplementationConverter - getDTO - 8");
		
		if(df245!=null){
			Subfield sub6 = df245.getSubfield('6');
			 
			if(sub6 != null){
					ordNum = sub6.getContent().split("\\-")[1];
			}
			
			if ((df245.getInd1() == '0') && (df245.getInd2() == '0')) {
				try {
					Subfield a = df245.getSubfield('a');
					retVal.getName().setLanguage(originalLanguage);
					retVal.getName().setContent(a.getContent());
//					System.out.println("RuleBookImplementationConverter - getDTO - 9");
				} catch (Exception e) {
				}
			} else {
				return false;
			}
		}
		
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> nameTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("245-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						if(a!=null){
							String content = a.getContent().trim();
							String transType = content.substring(0, 1);
							String language = content.substring(2, 5);
							String transContent = content.substring(6);
							MultilingualContentDTO nameTranslation = new MultilingualContentDTO(transContent, language, transType);
							nameTranslations.add(nameTranslation);
//							System.out.println("RuleBookImplementationConverter - getDTO - 10");
						}
					}
				} catch (Exception e) {
				}
			}
			retVal.setNameTranslations(nameTranslations);
		}
		
		try {
			ordNum = null;
			DataField df500 = mARC21Record.getDataField("500");
			Subfield sub6 = df500.getSubfield('6');
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}
		
			Subfield a = df500.getSubfield('a');
			retVal.getDescription().setLanguage(originalLanguage);
			retVal.getDescription().setContent(a.getContent());
//			System.out.println("RuleBookImplementationConverter - getDTO - 11");
		} catch (Exception e) {
		}	 
		
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> descriptionTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("500-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						String content = a.getContent().trim();
						String transType = content.substring(0, 1);
						String language = content.substring(2, 5);
						String transContent = content.substring(6);
						MultilingualContentDTO noteTranslation = new MultilingualContentDTO(transContent, language, transType);
						descriptionTranslations.add(noteTranslation);
//						System.out.println("RuleBookImplementationConverter - getDTO - 12");
					}
				} catch (Exception e) {
				}
			}
			retVal.setDescriptionTranslations(descriptionTranslations);
		}

		try {
			ordNum = null;
			DataField df653 = mARC21Record.getDataField("653");
			Subfield sub6 = df653.getSubfield('6');
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}
		
			Subfield a = df653.getSubfield('a');
			retVal.getKeywords().setLanguage(originalLanguage);
			retVal.getKeywords().setContent(a.getContent());
//			System.out.println("RuleBookImplementationConverter - getDTO - 13");
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
//						System.out.println("RuleBookImplementationConverter - getDTO - 14");
					}
				} catch (Exception e) {
				}
			}
			retVal.setKeywordsTranslations(keywordsTranslations);
		}
		
//		System.out.println("RuleBookImplementationConverter - getDTO - 15");
		
		List<DataField> df773 = mARC21Record.getDataFields("773");
		for (DataField dataField : df773) {
//			System.out.println("RuleBookImplementationConverter - getDTO - 16");
			try {
				if ((dataField.getInd1() == '0') && ("abs".equalsIgnoreCase(dataField.getSubfield('4').getContent()))){
					Subfield w = dataField.getSubfield('w');
					RecordDAO recordDAO = new RecordDAO(new RecordDB());
					RuleBookDTO superRuleBookDTO = (RuleBookDTO) recordDAO.getDTO(w.getContent());
					if(superRuleBookDTO!=null)
						retVal.setSuperRuleBook(superRuleBookDTO);
				}
				else if((dataField.getInd1() == '0') && ("imp".equalsIgnoreCase(dataField.getSubfield('4').getContent()))){
					Subfield w = dataField.getSubfield('w');
					RecordDAO recordDAO = new RecordDAO(new RecordDB());
					RuleBookImplementationDTO superRuleBookImplementationDTO = (RuleBookImplementationDTO) recordDAO.getDTO(w.getContent());
					if(superRuleBookImplementationDTO!=null)
						retVal.setSuperRuleBookImplementation(superRuleBookImplementationDTO);
				}
			} catch (Exception e) {
//				System.out.println("RuleBookImplementationConverter - getDTO - 16-Error");
			}
		}

		List<Classification> recordClasses = rec.getRecordClasses();
		ResultsTypeGroupDAO resultsTypeGroupDAO = new ResultsTypeGroupDAO();
		for (Classification classification : recordClasses) {
			if(classification.getCfClassSchemeId().equalsIgnoreCase(Types.TYPE_SCHEMA)){
				String classID = classification.getCfClassId();
				//ne radi nista
			}	else if (classification.getCfClassSchemeId().equalsIgnoreCase(ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA)){
//				System.out.println("RuleBookImplementationConverter - getDTO - 17");
				ResultsTypeGroupDTO rtg = resultsTypeGroupDAO.getResultsTypeGroup(classification.getCfClassId(), false, false);
				if(rtg!=null)
					retVal.setResultsTypeGroup(rtg);
			}
		}
//		System.out.println("RuleBookImplementationConverter - getDTO - 18");
		
		//visak od kako se prebacilo u polje 773
//		try{
//			List<RecordRecord> recordRecords = rec.getRelationsThisRecordOtherRecords();
//			RecordDAO recordDAO = new RecordDAO(new RecordDB());
//			for (RecordRecord recordRecord : recordRecords) {
//				if(recordRecord.getCfClassSchemeId().equalsIgnoreCase("ruleBoooksRelation") && (recordRecord.getCfClassId().equalsIgnoreCase("is version of"))){
//					if(recordDAO.getDTO(recordRecord.getRecordId()) instanceof RuleBookImplementationDTO)
//						retVal.setSuperRuleBookImplementation((RuleBookImplementationDTO)recordDAO.getDTO(recordRecord.getRecordId()));
//					else if(recordDAO.getDTO(recordRecord.getRecordId()) instanceof RuleBookDTO)
//						retVal.setSuperRuleBook((RuleBookDTO)recordDAO.getDTO(recordRecord.getRecordId()));
//				}
//			}	
//		}catch (Exception e) {
//		}
		
		return true;	
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setDataFields(rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record,
	 *      rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setDataFields(MARC21Record rec, RecordDTO dto) {
		
//		System.out.println("RuleBookImplementationConverter - setDataFields - 1");
		
		Integer ordNum = 0;
		RuleBookImplementationDTO ruleBookImplementationDTO = (RuleBookImplementationDTO) dto;
		
		DataField df046 = new DataField("046", AbstractRecordConverter.BLANK,	AbstractRecordConverter.BLANK);
		StringBuffer tempContent = new StringBuffer();
		Subfield m = new Subfield('m');
		if (ruleBookImplementationDTO.getStartDate() != null){
			try{
				String startDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				startDate = sdf.format(ruleBookImplementationDTO.getStartDate().getTime());
				tempContent.append(startDate);
//				System.out.println("RuleBookImplementationConverter - setDataFields - 2");
			} catch (Exception e) {
			}
		}
		if (tempContent.toString().length() > 0) {
			m.setContent(tempContent.toString());
			df046.addSubfield(m);
		}
		tempContent = new StringBuffer();
		Subfield n = new Subfield('n');
		if (ruleBookImplementationDTO.getEndDate() != null){
			try{
				String endDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				endDate = sdf.format(ruleBookImplementationDTO.getEndDate().getTime());
				tempContent.append(endDate);
//				System.out.println("RuleBookImplementationConverter - setDataFields - 3");
			} catch (Exception e) {
			}
		}
		if (tempContent.toString().length() > 0) {
			n.setContent(tempContent.toString());
			df046.addSubfield(n);
//			System.out.println("RuleBookImplementationConverter - setDataFields - 4");
		}
		
		if(df046.getSubfieldCount() > 0)
			rec.addDataField(df046);
		
//		System.out.println("RuleBookImplementationConverter - setDataFields - 5");
		
		DataField data084 = new DataField("084", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		Subfield b = new Subfield('b');
		if ((ruleBookImplementationDTO.getRuleBookUserCode() != null)
				&& (!"".equals(ruleBookImplementationDTO.getRuleBookUserCode().trim()))){
			tempContent.append(ruleBookImplementationDTO.getRuleBookUserCode());
//			System.out.println("RuleBookImplementationConverter - setDataFields - 6");
		}

		if (tempContent.toString().length() > 0) {
			b.setContent(tempContent.toString());
			data084.addSubfield(b);
//			System.out.println("RuleBookImplementationConverter - setDataFields - 7");
		}
		if(data084.getSubfieldCount() > 0)
		rec.addDataField(data084);
		
		DataField data245 = new DataField("245", '0', '0');
		tempContent = new StringBuffer();
		Subfield a = new Subfield('a');
		if ((ruleBookImplementationDTO.getName().getContent() != null)
				&& (!"".equals(ruleBookImplementationDTO.getName().getContent()))){
			tempContent.append(ruleBookImplementationDTO.getName().getContent());
//			System.out.println("RuleBookImplementationConverter - setDataFields - 8");
		}
		Subfield sub6 = new Subfield('6');
		if(ruleBookImplementationDTO.getNameTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data245.addSubfield(sub6);
//			System.out.println("RuleBookImplementationConverter - setDataFields - 9");
		}
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data245.addSubfield(a);
//			System.out.println("RuleBookImplementationConverter - setDataFields - 10");
		}
		if(data245.getSubfieldCount() > 0)
			rec.addDataField(data245);
		
		for (MultilingualContentDTO nameTranslation : ruleBookImplementationDTO.getNameTranslations()) {
			DataField data880 = new DataField("880", '0', '0');
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("245-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(nameTranslation.getTransType() + "-" + nameTranslation.getLanguage() + ":" + nameTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		DataField data500 = new DataField("500", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((ruleBookImplementationDTO.getDescription().getContent() != null)
				&& (!"".equals(ruleBookImplementationDTO.getDescription().getContent()))){
			tempContent.append(ruleBookImplementationDTO.getDescription().getContent());
//			System.out.println("RuleBookImplementationConverter - setDataFields - 11");
		}
		sub6 = new Subfield('6');
		if(ruleBookImplementationDTO.getDescriptionTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data500.addSubfield(sub6);
//			System.out.println("RuleBookImplementationConverter - setDataFields - 12");
		}
		
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data500.addSubfield(a);
		}
		
		if(data500.getSubfieldCount() > 0)
			rec.addDataField(data500);
		
		for (MultilingualContentDTO descriptionTranslation : ruleBookImplementationDTO.getDescriptionTranslations()) {
			DataField data880 = new DataField("880", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("500-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(descriptionTranslation.getTransType() + "-" + descriptionTranslation.getLanguage() + ":" + descriptionTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		DataField data653 = new DataField("653", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((ruleBookImplementationDTO.getKeywords().getContent() != null)
				&& (!"".equals(ruleBookImplementationDTO.getKeywords().getContent()))){
			tempContent.append(ruleBookImplementationDTO.getKeywords().getContent());
//			System.out.println("RuleBookImplementationConverter - setDataFields - 13");
		}
		sub6 = new Subfield('6');
		if(ruleBookImplementationDTO.getKeywordsTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data653.addSubfield(sub6);
//			System.out.println("RuleBookImplementationConverter - setDataFields - 12");
		}
		
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data653.addSubfield(a);
		}
		
		if(data653.getSubfieldCount() > 0)
			rec.addDataField(data653);
		
		for (MultilingualContentDTO keywordsTranslation : ruleBookImplementationDTO.getKeywordsTranslations()) {
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
		
		RuleBookDTO superRulebookDTO = ruleBookImplementationDTO.getSuperRuleBook();
		if (superRulebookDTO != null) {
			DataField data773 = new DataField("773", '0',
					AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			Subfield t = new Subfield('t');
			if (superRulebookDTO.getSomeName() != null)
				tempContent.append(superRulebookDTO.getSomeName());
			if (tempContent.toString().length() > 0) {
				t.setContent(tempContent.toString());
				data773.addSubfield(t);
			}
			tempContent = new StringBuffer();
			Subfield w = new Subfield('w');
			if ((superRulebookDTO.getControlNumber() != null)
					&& (!"".equals(superRulebookDTO.getControlNumber())))
				tempContent.append(superRulebookDTO.getControlNumber());
			if (tempContent.toString().length() > 0) {
				w.setContent(tempContent.toString());
				data773.addSubfield(w);
			}

			Subfield sub4 = new Subfield('4');
			sub4.setContent("abs");
			data773.addSubfield(sub4);
			
			if(data773.getSubfieldCount() > 0)
				rec.addDataField(data773);
		}
		
		RuleBookImplementationDTO superRulebookImplementationDTO = ruleBookImplementationDTO.getSuperRuleBookImplementation();
		if (superRulebookImplementationDTO != null) {
			DataField data773 = new DataField("773", '0',
					AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			Subfield t = new Subfield('t');
			if (superRulebookImplementationDTO.getSomeName() != null)
				tempContent.append(superRulebookImplementationDTO.getSomeName());
			if (tempContent.toString().length() > 0) {
				t.setContent(tempContent.toString());
				data773.addSubfield(t);
			}
			tempContent = new StringBuffer();
			Subfield w = new Subfield('w');
			if ((superRulebookImplementationDTO.getControlNumber() != null)
					&& (!"".equals(superRulebookImplementationDTO.getControlNumber())))
				tempContent.append(superRulebookImplementationDTO.getControlNumber());
			if (tempContent.toString().length() > 0) {
				w.setContent(tempContent.toString());
				data773.addSubfield(w);
			}

			Subfield sub4 = new Subfield('4');
			sub4.setContent("imp");
			data773.addSubfield(sub4);
			
			if(data773.getSubfieldCount() > 0)
				rec.addDataField(data773);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsOtherRecordsThisRecord(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsOtherRecordsThisRecord(Record retVal,
			RecordDTO dto) {
		// TODO Auto-generated method stub
		super.setRelationsOtherRecordsThisRecord(retVal, dto);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsThisRecordOtherRecords(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsThisRecordOtherRecords(Record retVal,
			RecordDTO dto) {
		RuleBookImplementationDTO ruleBookImplementation = (RuleBookImplementationDTO) dto;
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		deleteRelationsThisRecordOtherRecords(retVal, "ruleBoooksRelation", "is version of");
		
		List<RecordRecord> recordRecords = retVal.getRelationsThisRecordOtherRecords();
		if((ruleBookImplementation.getSuperRuleBook() != null) && (ruleBookImplementation.getSuperRuleBook().getControlNumber() != null)){
			recordRecords.add(new RecordRecord(ruleBookImplementation.getSuperRuleBook().getControlNumber(), "ruleBoooksRelation", "is version of", startDate, endDate));
		}
		
		if((ruleBookImplementation.getSuperRuleBookImplementation() != null) && (ruleBookImplementation.getSuperRuleBookImplementation().getControlNumber() != null)){
			recordRecords.add(new RecordRecord(ruleBookImplementation.getSuperRuleBookImplementation().getControlNumber(), "ruleBoooksRelation", "is version of", startDate, endDate));
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#getControlField006(rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected ControlField getControlField006(RecordDTO dto) {
	
		return null;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#getControlField007(rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected ControlField getControlField007(RecordDTO dto) {
		
		return null;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#getControlField008(rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected ControlField getControlField008(RecordDTO dto) {
		RuleBookDTO ruleBookDTO = (RuleBookDTO) dto;
		ControlField retVal = new ControlField("008");
		String content = "";
		for(int i=0;i<35;i++)
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		if(ruleBookDTO.getName().getLanguage() != null)
			content += ruleBookDTO.getName().getLanguage();
		else 
			content += MultilingualContentDTO.LANGUAGE_SERBIAN;
		content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		retVal.setContent(content);
		return retVal;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.AbstractRecordConverter#setRecordClasses(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRecordClasses(Record retVal, RecordDTO dto) {
		
		super.setRecordClasses(retVal, dto);;
		
		Classification classResultsTypeGroup = null;
		for (Classification classification : retVal.getRecordClasses()) {
			if(classification.getCfClassSchemeId().equalsIgnoreCase(ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA)){
//				System.out.println("RuleBookImplementationConverter - setRecordClasses - 1");
				classResultsTypeGroup = classification;
				break;
			}	
		}	
		
		if(classResultsTypeGroup!=null){
			retVal.getRecordClasses().remove(classResultsTypeGroup);
//			System.out.println("RuleBookImplementationConverter - setRecordClasses - 2");
		}
		
		ResultsTypeGroupDTO rtg = ((RuleBookImplementationDTO)dto).getResultsTypeGroup();
		Calendar startDate = rtg.getStartDate();
		if(startDate==null){
			startDate = new GregorianCalendar();
			startDate.set(Calendar.YEAR, 1900);
			startDate.set(Calendar.MONTH, Calendar.JANUARY);
			startDate.set(Calendar.DAY_OF_MONTH, 1);
		}
		Calendar endDate = rtg.getEndDate();
		if(endDate==null){
			endDate = new GregorianCalendar();
			endDate.set(Calendar.YEAR, 1900);
			endDate.set(Calendar.MONTH, Calendar.JANUARY);
			endDate.set(Calendar.DAY_OF_MONTH, 1);
		}
		
		String classResultsTypeGroupNew = rtg.getClassId();
//		System.out.println("RuleBookImplementationConverter - setRecordClasses - 3");
		if(classResultsTypeGroupNew != null){
			Classification recClass = new Classification(ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroupNew, startDate, endDate, new Integer(0), null);
			retVal.getRecordClasses().add(recClass);
//			System.out.println("RuleBookImplementationConverter - setRecordClasses - 4");
		}
	}

}
