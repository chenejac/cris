package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.eval;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dao.eval.ResultsTypeGroupDAO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeGroupDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.AbstractRecordConverter;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ConverterFactory;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.ControlField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Subfield;

/**
 * Class for converting bibliographic mARC21Record between MARC21 format and
 * RuleBookDTO object.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 */
@SuppressWarnings("serial")
public class RuleBookConverter extends ABibliographicRecordConverter {
	
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
		
//		System.out.println("RuleBookConverter - method getDTO - 1");
		
		MARC21Record mARC21Record = rec.getMARC21Record();
		if(rec.getDto() == null)
			rec.setDto(ConverterFactory.getDTO(rec.getDtoType()));
		
		RuleBookDTO retVal = (RuleBookDTO)rec.getDto();
		retVal.setControlNumber(mARC21Record.getControlNumber());
		
//		System.out.println("RuleBookConverter - method getDTO - 2");
		
		retVal.setFiles(rec.getFiles());
		retVal.setDeletedFiles(rec.getDeletedFiles());
		
//		System.out.println("RuleBookConverter - method getDTO - 3");
		
		String originalLanguage = MultilingualContentDTO.LANGUAGE_SERBIAN;
		try{
			ControlField cf008 = mARC21Record.getControlField("008");
			originalLanguage = cf008.getCharPositions(35, 3);
//			System.out.println("RuleBookConverter - method getDTO - 4");
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
//			System.out.println("RuleBookConverter - method getDTO - 5");
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
//			System.out.println("RuleBookConverter - method getDTO - 6");
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
//			System.out.println("RuleBookConverter - method getDTO - 7");
		} catch (Exception e) {
		}
		
		DataField df245 = mARC21Record.getDataField("245");
		String ordNum = null;
//		System.out.println("RuleBookConverter - method getDTO - 8");
		
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
//					System.out.println("RuleBookConverter - method getDTO - 9");
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
//							System.out.println("RuleBookConverter - method getDTO - 10");
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
//			System.out.println("RuleBookConverter - method getDTO - 11");
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
//						System.out.println("RuleBookConverter - method getDTO - 12");
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
//			System.out.println("RuleBookConverter - method getDTO - 13");
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
//						System.out.println("RuleBookConverter - method getDTO - 14");
					}
				} catch (Exception e) {
				}
			}
			retVal.setKeywordsTranslations(keywordsTranslations);
		}
		
//		System.out.println("RuleBookConverter - method getDTO - 15");
		
		List<Classification> recordClasses = rec.getRecordClasses();
		ResultsTypeGroupDAO resultsTypeGroupDAO = new ResultsTypeGroupDAO();
		for (Classification classification : recordClasses) {
			if(classification.getCfClassSchemeId().equalsIgnoreCase(Types.TYPE_SCHEMA)){
				String classID = classification.getCfClassId();
				//ne radi nista
			}	else if (classification.getCfClassSchemeId().equalsIgnoreCase(ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA)){
//				System.out.println("RuleBookConverter - method getDTO - 16");
				ResultsTypeGroupDTO rtg = resultsTypeGroupDAO.getResultsTypeGroup(classification.getCfClassId(), false, false);
				if(rtg!=null)
					retVal.setResultsTypeGroup(rtg);
			}
		}
//		System.out.println("RuleBookConverter - method getDTO - 17");
		return true;	
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setDataFields(rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record,
	 *      rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setDataFields(MARC21Record rec, RecordDTO dto) {
		
//		System.out.println("RuleBookConverter - method setDataFields - 1");
		
		Integer ordNum = 0;
		RuleBookDTO ruleBookDTO = (RuleBookDTO) dto;
		
		DataField df046 = new DataField("046", AbstractRecordConverter.BLANK,	AbstractRecordConverter.BLANK);
		StringBuffer tempContent = new StringBuffer();
		Subfield m = new Subfield('m');
		if (ruleBookDTO.getStartDate() != null){
			try{
				String startDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				startDate = sdf.format(ruleBookDTO.getStartDate().getTime());
				tempContent.append(startDate);
//				System.out.println("RuleBookConverter - method setDataFields - 2");
			} catch (Exception e) {
			}
		}
		if (tempContent.toString().length() > 0) {
			m.setContent(tempContent.toString());
			df046.addSubfield(m);
		}
		tempContent = new StringBuffer();
		Subfield n = new Subfield('n');
		if (ruleBookDTO.getEndDate() != null){
			try{
				String endDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				endDate = sdf.format(ruleBookDTO.getEndDate().getTime());
				tempContent.append(endDate);
//				System.out.println("RuleBookConverter - method setDataFields - 3");
			} catch (Exception e) {
			}
		}
		if (tempContent.toString().length() > 0) {
			n.setContent(tempContent.toString());
			df046.addSubfield(n);
//			System.out.println("RuleBookConverter - method setDataFields - 4");
		}
		
		if(df046.getSubfieldCount() > 0)
			rec.addDataField(df046);
		
//		System.out.println("RuleBookConverter - method setDataFields - 5");
		
		DataField data084 = new DataField("084", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		Subfield b = new Subfield('b');
		if ((ruleBookDTO.getRuleBookUserCode() != null)
				&& (!"".equals(ruleBookDTO.getRuleBookUserCode().trim()))){
			tempContent.append(ruleBookDTO.getRuleBookUserCode());
//			System.out.println("RuleBookConverter - method setDataFields - 6");
		}

		if (tempContent.toString().length() > 0) {
			b.setContent(tempContent.toString());
			data084.addSubfield(b);
//			System.out.println("RuleBookConverter - method setDataFields - 7");
		}
		if(data084.getSubfieldCount() > 0)
		rec.addDataField(data084);
		
		DataField data245 = new DataField("245", '0', '0');
		tempContent = new StringBuffer();
		Subfield a = new Subfield('a');
		if ((ruleBookDTO.getName().getContent() != null)
				&& (!"".equals(ruleBookDTO.getName().getContent()))){
			tempContent.append(ruleBookDTO.getName().getContent());
//			System.out.println("RuleBookConverter - method setDataFields - 8");
		}
		Subfield sub6 = new Subfield('6');
		if(ruleBookDTO.getNameTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data245.addSubfield(sub6);
//			System.out.println("RuleBookConverter - method setDataFields - 9");
		}
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data245.addSubfield(a);
//			System.out.println("RuleBookConverter - method setDataFields - 10");
		}
		if(data245.getSubfieldCount() > 0)
			rec.addDataField(data245);
		
		for (MultilingualContentDTO nameTranslation : ruleBookDTO.getNameTranslations()) {
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
		if ((ruleBookDTO.getDescription().getContent() != null)
				&& (!"".equals(ruleBookDTO.getDescription().getContent()))){
			tempContent.append(ruleBookDTO.getDescription().getContent());
//			System.out.println("RuleBookConverter - method setDataFields - 11");
		}
		sub6 = new Subfield('6');
		if(ruleBookDTO.getDescriptionTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data500.addSubfield(sub6);
//			System.out.println("RuleBookConverter - method setDataFields - 12");
		}
		
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data500.addSubfield(a);
		}
		
		if(data500.getSubfieldCount() > 0)
			rec.addDataField(data500);
		
		for (MultilingualContentDTO descriptionTranslation : ruleBookDTO.getDescriptionTranslations()) {
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
		if ((ruleBookDTO.getKeywords().getContent() != null)
				&& (!"".equals(ruleBookDTO.getKeywords().getContent()))){
			tempContent.append(ruleBookDTO.getKeywords().getContent());
//			System.out.println("RuleBookConverter - method setDataFields - 14");
		}
		sub6 = new Subfield('6');
		if(ruleBookDTO.getKeywordsTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data653.addSubfield(sub6);
//			System.out.println("RuleBookConverter - method setDataFields - 15");
		}
		
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data653.addSubfield(a);
		}
		
		if(data653.getSubfieldCount() > 0)
			rec.addDataField(data653);
		
		for (MultilingualContentDTO keywordsTranslation : ruleBookDTO.getKeywordsTranslations()) {
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
//		System.out.println("**********************STAMPAJ 1**********************");
//		System.out.println(rec.toString());
//		System.out.println("**********************STAMPAJ 2**********************");
//		System.out.println(rec.getPrintableString());
//		System.out.println("**********************STAMPAJ 3**********************");
//		System.out.println("RuleBookConverter - method setDataFields - 16");
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
		// TODO Auto-generated method stub
		super.setRelationsThisRecordOtherRecords(retVal, dto);
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
		
		super.setRecordClasses(retVal, dto);
		
		Classification classResultsTypeGroup = null;
		for (Classification classification : retVal.getRecordClasses()) {
			if(classification.getCfClassSchemeId().equalsIgnoreCase(ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA)){
//				System.out.println("RuleBookConverter - method setRecordClasses - 1");
				classResultsTypeGroup = classification;
				break;
			}	
		}	
		
		if(classResultsTypeGroup!=null){
			retVal.getRecordClasses().remove(classResultsTypeGroup);
//			System.out.println("RuleBookConverter - method setRecordClasses - 2");
		}
		
		ResultsTypeGroupDTO rtg = ((RuleBookDTO)dto).getResultsTypeGroup();
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
//		System.out.println("RuleBookConverter - method setRecordClasses - 3");
		if(classResultsTypeGroupNew != null){
			Classification recClass = new Classification(ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroupNew, startDate, endDate, new Integer(0), null);
			retVal.getRecordClasses().add(recClass);
//			System.out.println("RuleBookConverter - method setRecordClasses - 4");
		}
	}
}