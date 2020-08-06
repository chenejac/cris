package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JobAdDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Subfield;

/**
 * Class for converting normative mARC21Record between MARC21 format and JobAdDTO
 * object.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class JobAdConverter extends ANormativeRecordConverter {

	/**
	 * Convert the mARC21Record with data about Job competition from MARC21 format to DTO
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
		JobAdDTO retVal = (JobAdDTO)rec.getDto();
		retVal.setControlNumber(mARC21Record.getControlNumber());
		retVal.setScopusID(rec.getScopusID());
		
		
		String originalLanguage = MultilingualContentDTO.LANGUAGE_SERBIAN;
		DataField df040 = mARC21Record.getDataField("040");
		try {
			Subfield b = df040.getSubfield('b');
			originalLanguage = b.getContent();
		} catch (Exception e) {
		}
		
		DataField df046 = mARC21Record.getDataField("046");
		try {
			Subfield s = df046.getSubfield('s');
			String dateString = s.getContent();
			if(dateString!=null){
				DateFormat formatter ; 
			    Date date ; 
			    formatter = new SimpleDateFormat("yyyyMMdd");
			    date = (Date)formatter.parse(dateString); 
			    Calendar cal=GregorianCalendar.getInstance();
			    cal.setTime(date);
				retVal.setStartDate(cal);
			}
		} catch (Exception e) {
		}
		
		try {
			Subfield t = df046.getSubfield('t');
			String dateString = t.getContent();
			if(dateString!=null){
				DateFormat formatter ; 
			    Date date ; 
			    formatter = new SimpleDateFormat("yyyyMMdd");
			    date = (Date)formatter.parse(dateString); 
			    Calendar cal=GregorianCalendar.getInstance();
			    cal.setTime(date);
				retVal.setEndDate(cal);
			}
		} catch (Exception e) {
		}
		
		DataField df111 = mARC21Record.getDataField("111");
		Subfield sub6 = df111.getSubfield('6');
		String ordNum = null;
		if(sub6 != null){
			ordNum = sub6.getContent().split("\\-")[1];
		}
		
		if (df111.getInd1() == '2') {
			try {
				Subfield a = df111.getSubfield('a');
				retVal.getName().setLanguage(originalLanguage);
				retVal.getName().setContent(a.getContent());
			} catch (Exception e) {
			}
			try {
				Subfield c = df111.getSubfield('c');
				String place = c.getContent().trim();
				retVal.setPlace(place);
			} catch (Exception e) {
			}
		} else
			return false;
		
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> nameTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("111-"+ordNum)) {
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
		
		DataField df373 = mARC21Record.getDataField("373");
		if(df373 != null){
			if(df373.getSubfield('0')!=null){
				RecordDAO recordDAO = new RecordDAO(new RecordDB());
				InstitutionDTO institutionDTO = (InstitutionDTO) recordDAO
					.getDTO(df373.getSubfield('0').getContent());
				if((institutionDTO!=null)){
					retVal.setInstitution(institutionDTO);
				}
			}
		}
		
		ordNum = null;
		retVal.getConditions().setLanguage(originalLanguage);
		DataField df678 = mARC21Record.getDataField("678");
		if(df678 != null){
			Subfield a = df678.getSubfield('a');
			if ((a!=null) && (a.getContent()!=null)){
				retVal.getConditions().setContent(a.getContent());
			}
			sub6 = df678.getSubfield('6');
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}
		}
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> conditionsTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("678-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						if(a!=null){
							String content = a.getContent().trim();
							String transType = content.substring(0, 1);
							String language = content.substring(2, 5);
							String transContent = content.substring(6);
							MultilingualContentDTO condTranslation = new MultilingualContentDTO(transContent, language, transType);
							conditionsTranslations.add(condTranslation);
						}
					}
				} catch (Exception e) {
				}
			}
			retVal.setConditionsTranslations(conditionsTranslations);
		}
		
		try {
			ordNum = null;
			DataField df680 = mARC21Record.getDataField("680");
			sub6 = df680.getSubfield('6');
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}
			
			Subfield i = df680.getSubfield('i');
			if ((i!=null) && (i.getContent()!=null)){
				retVal.getDescription().setLanguage(originalLanguage);
				retVal.getDescription().setContent(i.getContent());
			}
		} catch (Exception e) {
		}	
		
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> descriptionTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("680-"+ordNum)) {
						Subfield i = dataField.getSubfield('i');
						if(i!=null){
							String content = i.getContent().trim();
							String transType = content.substring(0, 1);
							String language = content.substring(2, 5);
							String transContent = content.substring(6);
							MultilingualContentDTO descriptionTranslation = new MultilingualContentDTO(transContent, language, transType);
							descriptionTranslations.add(descriptionTranslation);
						}
					}
				} catch (Exception e) {
				}
			}
			retVal.setDescriptionTranslations(descriptionTranslations);
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
		
		try{
			List<RecordRecord> recordRecords = rec.getRelationsOtherRecordsThisRecord();
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			for (RecordRecord recordRecord : recordRecords) {
				if(recordRecord.getCfClassSchemeId().equalsIgnoreCase("authorJobAd") && (recordRecord.getCfClassId().equalsIgnoreCase("applied to"))){
					AuthorDTO authorDTO = (AuthorDTO)recordDAO.getDTO(recordRecord.getRecordId());
					if(authorDTO != null){
						retVal.getApplications().add(authorDTO);
					}
				}
			}	
		}catch (Exception e) {
		}
		
//		retVal.setClassifications(rec.getRecordClasses());
		return true;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ANormativeRecordConverter#setDataFields(rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record,
	 *      rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setDataFields(MARC21Record rec, RecordDTO dto) {
		
		Integer ordNum = 0;
		
		JobAdDTO jobAdDTO = (JobAdDTO) dto;

		DataField data040 = new DataField("040", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		Subfield b = new Subfield('b');
		if(jobAdDTO.getName().getLanguage() != null)
			b.setContent(jobAdDTO.getName().getLanguage());
		else 
			b.setContent(MultilingualContentDTO.LANGUAGE_SERBIAN);
		data040.addSubfield(b);
		rec.addDataField(data040);
		
		DataField data046 = new DataField("046", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		StringBuffer tempContent = new StringBuffer();
		Subfield s = new Subfield('s');
		if (jobAdDTO.getStartDate() != null) {
			DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String startDate = formatter.format(jobAdDTO.getStartDate().getTime());
			s.setContent(startDate);
			data046.addSubfield(s);
		}
		Subfield t = new Subfield('t');
		if (jobAdDTO.getEndDate() != null) {
			DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String endDate = formatter.format(jobAdDTO.getEndDate().getTime());
			t.setContent(endDate);
			data046.addSubfield(t);
		}
		if(data046.getSubfieldCount() > 0)
			rec.addDataField(data046);
		
		DataField data111 = new DataField("111", '2', AbstractRecordConverter.BLANK);
		Subfield sub6 = new Subfield('6');
		if(jobAdDTO.getNameTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data111.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		Subfield a = new Subfield('a');
		if ((jobAdDTO.getName().getContent() != null)
				&& (!"".equals(jobAdDTO.getName().getContent())))
			tempContent.append(jobAdDTO.getName().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data111.addSubfield(a);
		}
		tempContent = new StringBuffer();
		Subfield c = new Subfield('c');
		if ((jobAdDTO.getPlace() != null)
				&& (!"".equals(jobAdDTO.getPlace())))
			tempContent.append(jobAdDTO.getPlace());
		if (tempContent.toString().length() > 0) {
			c.setContent(tempContent.toString());
			data111.addSubfield(c);
		}
		if(data111.getSubfieldCount() > 0)
			rec.addDataField(data111);
		
		for (MultilingualContentDTO nameTranslation : jobAdDTO.getNameTranslations()) {
			DataField data880 = new DataField("880", '2', AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("111-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(nameTranslation.getTransType() + "-" + nameTranslation.getLanguage() + ":" + nameTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		DataField data373 = new DataField("373", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		InstitutionDTO institutionDTO = jobAdDTO.getInstitution();
		if(institutionDTO!=null){
			if(institutionDTO.getControlNumber()!=null){
				Subfield sub0 = new Subfield('0');
				if ((institutionDTO.getControlNumber() != null)
						&& (!"".equals(institutionDTO.getControlNumber())))
					tempContent.append(institutionDTO.getControlNumber());
				if (tempContent.toString().length() > 0) {
					sub0.setContent(tempContent.toString());
					data373.addSubfield(sub0);
				}
			}
			tempContent = new StringBuffer();
			a = new Subfield('a');
			if ((institutionDTO.getName().getContent() != null)
					&& (!"".equals(institutionDTO.getName().getContent())))
				tempContent.append(institutionDTO.getName().getContent());
			if (tempContent.toString().length() > 0) {
				a.setContent(tempContent.toString());
				data373.addSubfield(a);
			}
			if(data373.getSubfieldCount() > 0)
				rec.addDataField(data373);
		}
		
		DataField data678 = new DataField("678", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(jobAdDTO.getConditionsTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data678.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((jobAdDTO.getConditions().getContent() != null)
				&& (!"".equals(jobAdDTO.getConditions().getContent())))
			tempContent.append(jobAdDTO.getConditions().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data678.addSubfield(a);
		}
		if(data678.getSubfieldCount() > 0)
			rec.addDataField(data678);
		
		for (MultilingualContentDTO conditionsTranslation : jobAdDTO.getConditionsTranslations()) {
			DataField data880 = new DataField("880", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("678-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(conditionsTranslation.getTransType() + "-" + conditionsTranslation.getLanguage() + ":" + conditionsTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		DataField data680 = new DataField("680", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(jobAdDTO.getDescriptionTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data680.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		Subfield i = new Subfield('i');
		if ((jobAdDTO.getDescription().getContent() != null)
				&& (!"".equals(jobAdDTO.getDescription().getContent())))
			tempContent.append(jobAdDTO.getDescription().getContent());
		if (tempContent.toString().length() > 0) {
			i.setContent(tempContent.toString());
			data680.addSubfield(i);
		}
		if(data680.getSubfieldCount() > 0)
			rec.addDataField(data680);
		
		for (MultilingualContentDTO descriptionTranslation : jobAdDTO.getDescriptionTranslations()) {
			DataField data880 = new DataField("880", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("680-0" + ordNum);
			data880.addSubfield(sub6);
			i = new Subfield('i');
			i.setContent(descriptionTranslation.getTransType() + "-" + descriptionTranslation.getLanguage() + ":" + descriptionTranslation.getContent());
			data880.addSubfield(i);
			rec.addDataField(data880);
		}
		
		DataField data856 = new DataField("856", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		Subfield u = new Subfield('u');
		if ((jobAdDTO.getUri() != null)
				&& (!"".equals(jobAdDTO.getUri())))
			tempContent.append(jobAdDTO.getUri());
		if (tempContent.toString().length() > 0) {
			u.setContent(tempContent.toString());
			data856.addSubfield(u);
		}
		if(data856.getSubfieldCount() > 0)
			rec.addDataField(data856);
		
	}

	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ANormativeRecordConverter#setRecordKeywords(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRecordKeywords(Record retVal, RecordDTO dto) {
		JobAdDTO jobAd = (JobAdDTO)dto;
		retVal.setRecordKeywords(new ArrayList<MultilingualContentDTO>());
		if((jobAd.getKeywords().getContent() != null) && (jobAd.getKeywords().getContent().trim().length() > 0)){
			if(jobAd.getKeywords().getLanguage() == null)
				jobAd.getKeywords().setLanguage(jobAd.getName().getLanguage());
			retVal.getRecordKeywords().add(jobAd.getKeywords());
		}
		for (MultilingualContentDTO keywords : jobAd.getKeywordsTranslations()) {
			retVal.getRecordKeywords().add(keywords);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsOtherRecordsThisRecord(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsOtherRecordsThisRecord(Record retVal,
			RecordDTO dto) {
		JobAdDTO jobAd = (JobAdDTO) dto;
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		deleteRelationsOtherRecordsThisRecord(retVal, "authorJobAd", "applied to");
		
		List<RecordRecord> recordRecords = retVal.getRelationsOtherRecordsThisRecord();
		
		for (AuthorDTO author : jobAd.getApplications()) {
			recordRecords.add(new RecordRecord(author.getControlNumber(), "authorJobAd", "applied to", startDate, endDate));
		}
		
	}

}
