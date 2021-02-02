package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.PositionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.ResearchAreaDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorPosition;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JobAdDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.TitleInstitution;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Subfield;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * Class for converting normative mARC21Record between MARC21 format and AuthorDTO
 * object.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class AuthorConverter extends ANormativeRecordConverter {

	
	/**
	 * Converts the mARC21Record with data about Author from MARC21 format to DTO
	 * format
	 * 
	 * @param rec
	 *            MARC21Record which should be converted
	 * 
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.AbstractRecordConverter#getDTO(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record)
	 * 
	 * @return true if everything is ok, otherwise false
	 */
	@Override
	public boolean getDTO(Record rec){
		
		MARC21Record mARC21Record = rec.getMARC21Record();
		if(rec.getDto() == null)
			rec.setDto(ConverterFactory.getDTO(rec.getDtoType()));
		AuthorDTO retVal = (AuthorDTO)rec.getDto();
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
		DataField df100 = mARC21Record.getDataField("100");
		if (df100.getInd1() == '1') {
			try {
				Subfield a = df100.getSubfield('a');
				String content = a.getContent().trim();
				String lastName = Sanitizer.nextPar(content, ',');
				content = Sanitizer.remPar(content, ',');
				String firstName = Sanitizer.nextPar(content, ',');
				content = Sanitizer.remPar(content, ',');
				String middleName = Sanitizer.nextPar(content, ',');
				retVal.setName(new AuthorNameDTO(firstName, lastName, middleName));
			} catch (Exception e) {
				return false;
			}
			try {
				Subfield c = df100.getSubfield('c');
				String content = c.getContent().trim();
				retVal.setTitle(Sanitizer.nextPar(content, ','));
//				content = Sanitizer.remPar(content, ',');
//				retVal.setPositionString(Sanitizer.nextPar(content, ','));
			} catch (Exception e) {
			}
			try {
				String dateString = df100.getSubfield('d').getContent();
				if(dateString!=null){
					if(dateString.contains(".")){
						DateFormat formatter ; 
					    Date date ; 
					    formatter = new SimpleDateFormat("dd.MM.yyyy");
					    date = (Date)formatter.parse(dateString); 
					    Calendar cal=GregorianCalendar.getInstance();
					    cal.setTimeInMillis(date.getTime());
						retVal.setDateOfBirth(cal);
//						retVal.setYearOfBirth(cal.get(Calendar.YEAR));
					} else {
						retVal.setYearOfBirth(Integer.parseInt(dateString));
					}
				}
				
			} catch (Exception e) {
			}
		} else {
			return false;
		}
		List<DataField> dfs400 = mARC21Record.getDataFields("400");
		List<AuthorNameDTO> otherFormatNames = new ArrayList<AuthorNameDTO>();
		for (DataField dataField : dfs400) {
			try {
				if (dataField.getInd1() == '1') {
					AuthorNameDTO authorName = null;
					Subfield a = dataField.getSubfield('a');
					String content = a.getContent().trim();
					String lastName = Sanitizer.nextPar(content, ',');
					content = Sanitizer.remPar(content, ',');
					String firstName = Sanitizer.nextPar(content, ',');
					content = Sanitizer.remPar(content, ',');
					String middleName = Sanitizer.nextPar(content, ',');
					authorName = new AuthorNameDTO(firstName, lastName, middleName);
					otherFormatNames.add(authorName);
				}
			} catch (Exception e) {
			}
		}
		retVal.setOtherFormatNames(otherFormatNames);
		
		DataField df370 = mARC21Record.getDataField("370");
		try {
			Subfield a = df370.getSubfield('a');
			if ((a!=null) && (a.getContent()!=null)){
				String content = a.getContent().trim();
				retVal.setPlaceOfBirth(Sanitizer.nextPar(content, ','));
				content = Sanitizer.remPar(content, ',');
				retVal.setState(Sanitizer.nextPar(content, ','));
			}
		} catch (Exception e) {
		}
		DataField df371 = mARC21Record.getDataField("371");
		try {
			Subfield a = df371.getSubfield('a');
			if ((a!=null) && (a.getContent()!=null))
				retVal.setAddress(a.getContent());
			Subfield b = df371.getSubfield('b');
			if ((b!=null) && (b.getContent()!=null))
				retVal.setCity(b.getContent());
			Subfield m = df371.getSubfield('m');
			if ((m!=null) && (m.getContent()!=null))
				retVal.setEmail(m.getContent());
		} catch (Exception e) {
		}
		
//		String ordNum = null;
//		try {
//			DataField df372 = mARC21Record.getDataField("372");
//			Subfield sub6 = df372.getSubfield('6');
//			if(sub6 != null){
//				ordNum = sub6.getContent().split("\\-")[1];
//			}
//			
//			Subfield a = df372.getSubfield('a');
//			if ((a!=null) && (a.getContent()!=null)){
//				retVal.getResearchInterest().setLanguage(originalLanguage);
//				retVal.getResearchInterest().setContent(a.getContent());
//			}
//		} catch (Exception e) {
//		}		
//			
//		if(ordNum != null){
//			List<DataField> dfs880 = mARC21Record.getDataFields("880");
//			List<MultilingualContentDTO> resIntTranslations = new ArrayList<MultilingualContentDTO>();
//			for (DataField dataField : dfs880) {
//				try {
//					if (dataField.getSubfield('6').getContent().equals("372-"+ordNum)) {
//						Subfield a = dataField.getSubfield('a');
//						if(a!=null){
//							String content = a.getContent().trim();
//							String transType = content.substring(0, 1);
//							String language = content.substring(2, 5);
//							String transContent = content.substring(6);
//							MultilingualContentDTO resIntTranslation = new MultilingualContentDTO(transContent, language, transType);
//							resIntTranslations.add(resIntTranslation);
//						}
//					}
//				} catch (Exception e) {
//				}
//			}
//			retVal.setResearchInterestTranslations(resIntTranslations);
//		}
//		
		
		for (DataField df373 : mARC21Record.getDataFields("373")) {
			if(df373 != null){
				if(df373.getSubfield('a')!=null){
					if(df373.getSubfield('a').getContent() != null)
						if(df373.getSubfield('a').getContent().startsWith("Titula")){
							if(df373.getSubfield('0')!=null){
								RecordDAO recordDAO = new RecordDAO(new RecordDB());
								InstitutionDTO institutionDTO = (InstitutionDTO) recordDAO
									.getDTO(df373.getSubfield('0').getContent());
								if((institutionDTO!=null)){
									retVal.getTitleInstitution().setInstitution(institutionDTO);
								}
							} else {
								retVal.getTitleInstitution().getInstitution().setSomeName(df373.getSubfield('a').getContent().substring(df373.getSubfield('a').getContent().indexOf(":") + 1).trim());
//								retVal.getTitleInstitution().getInstitution().getName().setContent(df373.getSubfield('a').getContent().substring(df373.getSubfield('a').getContent().indexOf(":") + 1).trim());
							}
							try{
								if(df373.getSubfield('t') != null){
									Integer year = Integer.parseInt(df373.getSubfield('t').getContent());
									retVal.getTitleInstitution().setYear(year);
								}
							} catch (Exception e){
								
							}
						} else {
							if(df373.getSubfield('0')!=null){
								RecordDAO recordDAO = new RecordDAO(new RecordDB());
								InstitutionDTO institutionDTO = (InstitutionDTO) recordDAO
									.getDTO(df373.getSubfield('0').getContent());
								if((institutionDTO!=null)){
									retVal.setInstitution(institutionDTO);
								}
							} else {
								retVal.getInstitution().setSomeName(df373.getSubfield('a').getContent());
//								retVal.getInstitution().getName().setContent(df373.getSubfield('a').getContent());
							}
						}
				}
			}
		}
				
		DataField df375 = mARC21Record.getDataField("375");
		try {
			Subfield a = df375.getSubfield('a');
			if ((a!=null) && (a.getContent()!=null)){
				retVal.setSex(a.getContent().charAt(0));
			}
		} catch (Exception e) {
		}
		
		String ordNum = null;
		retVal.getBiography().setLanguage(originalLanguage);
		DataField df678 = mARC21Record.getDataField("678");
		if(df678 != null){
			if (df678.getInd1() == '1') {
				Subfield b = df678.getSubfield('b');
				if ((b!=null) && (b.getContent()!=null)){
					retVal.getBiography().setContent(b.getContent());
				}
				Subfield sub6 = df678.getSubfield('6');
				if(sub6 != null){
					ordNum = sub6.getContent().split("\\-")[1];
				}
			}
		}
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> biogTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("678-"+ordNum)) {
						Subfield b = dataField.getSubfield('b');
						if(b!=null){
							String content = b.getContent().trim();
							String transType = content.substring(0, 1);
							String language = content.substring(2, 5);
							String transContent = content.substring(6);
							MultilingualContentDTO biogTranslation = new MultilingualContentDTO(transContent, language, transType);
							biogTranslations.add(biogTranslation);
						}
					}
				} catch (Exception e) {
				}
			}
			retVal.setBiographyTranslations(biogTranslations);
		}
		for (DataField df710 : mARC21Record.getDataFields("710")) {
			if(df710 != null){
				if (df710.getInd1() == '2') {
					RecordDAO recordDAO = new RecordDAO(new RecordDB());
					if(df710.getSubfield('a')!=null){
						if(df710.getSubfield('0')!=null){
							InstitutionDTO institutionDTO = (InstitutionDTO) recordDAO
								.getDTO(df710.getSubfield('0').getContent());
							if((institutionDTO!=null)){
								retVal.setInstitution(institutionDTO);
							}
						} else {
							retVal.getInstitution().setSomeName(df710.getSubfield('a').getContent());
//							retVal.getInstitution().getName().setContent(df710.getSubfield('a').getContent());
						}
					} 
					if(df710.getSubfield('b')!=null){
						OrganizationUnitDTO organizationUnitDTO = (OrganizationUnitDTO) recordDAO
							.getDTO(df710.getSubfield('0').getContent());
						if((organizationUnitDTO!=null)){
							retVal.setOrganizationUnit(organizationUnitDTO);
						}
					}
				}
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
		
//		Calendar currentPositionEndDate = new GregorianCalendar();
//		currentPositionEndDate.set(Calendar.YEAR, 2099);
//		currentPositionEndDate.set(Calendar.MONTH, Calendar.DECEMBER);
//		currentPositionEndDate.set(Calendar.DAY_OF_MONTH, 31);
		
//		PositionDTO currentPosition = null;
//		List<PositionDTO> formerPositions = new ArrayList<PositionDTO>();
//		
//		for (Classification classification : rec.getRecordClasses()) {
//			if(PositionDTO.POSITION_SCHEMA.equals(classification.getCfClassSchemeId()))
//				if(currentPositionEndDate.equals(classification.getCfEndDate())){
//					currentPosition = new PositionDTO(new ClassDTO(classification.getCfClassSchemeId(), classification.getCfClassId(), null, null, null, null), classification.getCfStartDate(), classification.getCfEndDate());
//				} else {
//					PositionDTO temp = new PositionDTO(new ClassDTO(classification.getCfClassSchemeId(), classification.getCfClassId(), null, null, null, null), classification.getCfStartDate(), classification.getCfEndDate());
//					formerPositions.add(temp);
//				}
//		}
//		
//		if(currentPosition != null)
//			retVal.setCurrentPosition(currentPosition);
		
		for (MultilingualContentDTO keywords : rec.getRecordKeywords()) {
			if(keywords.getTransType().equals(MultilingualContentDTO.TRANS_ORIGINAL)){
				retVal.setKeywords(keywords);
			} else {
				retVal.getKeywordsTranslations().add(keywords);
			}	
		}
		
		List<Classification> recordClasses = rec.getRecordClasses();
		ResearchAreaDAO researchAreaDAO = new ResearchAreaDAO();
		PositionDAO positionDAO = new PositionDAO();
		for (Classification classification : recordClasses) {
			if (classification.getCfClassSchemeId().equalsIgnoreCase(ResearchAreaDTO.RESEARCH_AREA_SCHEMA)){
				retVal.getResearchAreas().add(researchAreaDAO.getResearchArea(classification.getCfClassId()));
			}
			if (classification.getCfClassSchemeId().equalsIgnoreCase(PositionDTO.POSITION_SCHEMA)){
				if(classification.getCfEndDate().get(Calendar.YEAR) == 2099){
					PositionDTO position = positionDAO.getPosition(classification.getCfClassId());
					position.setTerm(new MultilingualContentDTO(position.getTerm().getContent(), position.getTerm().getLanguage(), position.getTerm().getTransType()));
					retVal.getCurrentPosition().setPosition(position);
					retVal.getCurrentPosition().setStartDate(classification.getCfStartDate());
					retVal.getCurrentPosition().setEndDate(null);
					retVal.getCurrentPosition().setResearchArea(classification.getResearchArea());
				}else {
					PositionDTO position = positionDAO.getPosition(classification.getCfClassId());
					position.setTerm(new MultilingualContentDTO(position.getTerm().getContent(), position.getTerm().getLanguage(), position.getTerm().getTransType()));
					retVal.getFormerPositions().add(new AuthorPosition(position, classification.getCfStartDate(), classification.getCfEndDate(), classification.getResearchArea()));
				}
					
			}
		}
		
		try{
			List<RecordRecord> recordRecords = rec.getRelationsThisRecordOtherRecords();
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			for (RecordRecord recordRecord : recordRecords) {
				if(recordRecord.getCfClassSchemeId().equalsIgnoreCase("authorJobAd") && (recordRecord.getCfClassId().equalsIgnoreCase("applied to"))){
					JobAdDTO jobAd = (JobAdDTO)recordDAO.getDTO(recordRecord.getRecordId());
					if(jobAd != null)
						retVal.getJobApplications().add(jobAd);
				}
			}	
		}catch (Exception e) {
		}
//		retVal.setClassifications(rec.getRecordClasses());
		Person person = (Person)rec;
		retVal.setJmbg(person.getJmbg());
		retVal.setDirectPhones(person.getDirectPhones());
		retVal.setLocalPhones(person.getLocalPhones());
		retVal.setApvnt(person.getApvnt());
		
		return true;
	}

	/**
	 * Convert mARC21Record from DTO format to MARC21 format
	 * 
	 * @param dto
	 *            MARC21Record which should be converted
	 * 
	 * @return true if everything is ok, otherwise false
	 */
	public boolean getRecord(RecordDTO dto){
		try {
			Person record = (Person)dto.getRecord();

			AuthorDTO author = (AuthorDTO) dto;
			
			record.setJmbg(author.getJmbg());
			record.setDirectPhones(author.getDirectPhones());
			record.setLocalPhones(author.getLocalPhones());
			record.setApvnt(author.getApvnt());
			record.setScopusID(author.getScopusID());
			record.setORCID(author.getORCID());
			
			MARC21Record mARC21Record = new MARC21Record();
			record.setMARC21Record(mARC21Record);
			setDataFields(mARC21Record, dto);
			String controlNumber = dto.getControlNumber();
			setLeader(mARC21Record, dto);
			setControlFields(mARC21Record, controlNumber, dto);
			setRecordClasses(record, dto);
			setRelationsThisRecordOtherRecords(record, dto);
			setRelationsOtherRecordsThisRecord(record, dto);
			setRecordKeywords(record, dto);
			record.getMARC21Record().sortFields();
			return true;
			
		} catch (Throwable e) {
			log.error("getRecord", e);
			return false;
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ANormativeRecordConverter#setDataFields(rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record,
	 *      rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setDataFields(MARC21Record rec, RecordDTO dto) {
		
		Integer ordNum = 0; 
		
		AuthorDTO authorDTO = (AuthorDTO) dto;

		DataField data040 = new DataField("040", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		Subfield b = new Subfield('b');
		
		if(authorDTO.getBiography().getLanguage() != null)
			b.setContent(authorDTO.getBiography().getLanguage());
		else 
			b.setContent(MultilingualContentDTO.LANGUAGE_SERBIAN);
		data040.addSubfield(b);
		rec.addDataField(data040);
		
		DataField data100 = new DataField("100", '1', AbstractRecordConverter.BLANK);
		StringBuffer tempContent = new StringBuffer();
		Subfield a = new Subfield('a');
		if ((authorDTO.getName().getLastname() != null)
				&& (!"".equals(authorDTO.getName().getLastname())))
			tempContent.append(Sanitizer.sanitize(authorDTO.getName().getLastname()));
		if ((authorDTO.getName().getFirstname() != null)
				&& (!"".equals(authorDTO.getName().getFirstname())))
			tempContent.append(", " + Sanitizer.sanitize(authorDTO.getName().getFirstname()));
		if ((authorDTO.getName().getOtherName() != null)
				&& (!"".equals(authorDTO.getName().getOtherName())))
			tempContent.append(", " + Sanitizer.sanitize(authorDTO.getName().getOtherName()));
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data100.addSubfield(a);
		}
		tempContent = new StringBuffer();
		Subfield c = new Subfield('c');
		if ((authorDTO.getTitle() != null)
				&& (!"".equals(authorDTO.getTitle())))
			tempContent.append(Sanitizer.sanitize(authorDTO.getTitle()));
		if (tempContent.toString().length() > 0) {
			c.setContent(tempContent.toString());
			data100.addSubfield(c);
		}
		Subfield d = new Subfield('d');
		if (authorDTO.getDateOfBirth() != null) {
			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			String dateOfBirth = formatter.format(authorDTO.getDateOfBirth().getTime());
			d.setContent(dateOfBirth);
			data100.addSubfield(d);
		}
		if(data100.getSubfieldCount() > 0)
			rec.addDataField(data100);
		
		for (AuthorNameDTO authorName : authorDTO.getOtherFormatNames()) {
			DataField data400 = new DataField("400", '1', AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			a = new Subfield('a');
			if ((authorName.getLastname() != null)
					&& (!"".equals(authorName.getLastname())))
				tempContent.append(Sanitizer.sanitize(authorName.getLastname()));
			if ((authorName.getFirstname() != null)
					&& (!"".equals(authorName.getFirstname())))
				tempContent.append(", " + Sanitizer.sanitize(authorName.getFirstname()));
			if ((authorName.getOtherName() != null)
					&& (!"".equals(authorName.getOtherName())))
				tempContent.append(", " + Sanitizer.sanitize(authorName.getOtherName()));
			if (tempContent.toString().length() > 0) {
				a.setContent(tempContent.toString());
				data400.addSubfield(a);
			}
			if(data400.getSubfieldCount() > 0)
				rec.addDataField(data400);
		}
		
		DataField data370 = new DataField("370", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((authorDTO.getPlaceOfBirth() != null)
				&& (!"".equals(authorDTO.getPlaceOfBirth())))
			tempContent.append(Sanitizer.sanitize(authorDTO.getPlaceOfBirth()));
		if ((authorDTO.getState() != null)
				&& (!"".equals(authorDTO.getState())))
			tempContent.append(", " + Sanitizer.sanitize(authorDTO.getState()));
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data370.addSubfield(a);
		}
		if(data370.getSubfieldCount() > 0)
			rec.addDataField(data370);
		
		
		DataField data371 = new DataField("371", AbstractRecordConverter.BLANK,
				AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if (authorDTO.getAddress() != null)
			tempContent.append(authorDTO.getAddress());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data371.addSubfield(a);
		}
		tempContent = new StringBuffer();
		b = new Subfield('b');
		if (authorDTO.getCity() != null)
			tempContent.append(authorDTO.getCity());
		if (tempContent.toString().length() > 0) {
			b.setContent(tempContent.toString());
			data371.addSubfield(b);
		}
		tempContent = new StringBuffer();
		Subfield m = new Subfield('m');
		if (authorDTO.getEmail() != null)
			tempContent.append(authorDTO.getEmail());
		if (tempContent.toString().length() > 0) {
			m.setContent(tempContent.toString());
			data371.addSubfield(m);
		}
		if(data371.getSubfieldCount() > 0)
			rec.addDataField(data371);
		
		for (ResearchAreaDTO researchArea : authorDTO.getResearchAreas()) {
			DataField data372 = new DataField("372", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
			Subfield sub6 = new Subfield('6');
			if(researchArea.getTermTranslations().size() > 0){
				ordNum++;
				sub6.setContent("880-0" + ordNum);
				data372.addSubfield(sub6);
			}
			a = new Subfield('a');
			tempContent = new StringBuffer();
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
		
		DataField data373 = new DataField("373", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		InstitutionDTO institutionDTO = authorDTO.getInstitution();
		if(institutionDTO!=null){
			if((institutionDTO.getControlNumber()!=null) && (institutionDTO.getControlNumber().contains("(BISIS)"))){
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
			if ((institutionDTO.getSomeName() != null)
					&& (!"".equals(institutionDTO.getSomeName())))
				tempContent.append(institutionDTO.getSomeName());
			if (tempContent.toString().length() > 0) {
				a.setContent(tempContent.toString());
				data373.addSubfield(a);
			}
			tempContent = new StringBuffer();
			c = new Subfield('c');
			if ((institutionDTO.getPlace() != null)
					&& (!"".equals(institutionDTO.getPlace())))
				tempContent.append(institutionDTO.getPlace());
			if (tempContent.toString().length() > 0) {
				c.setContent(tempContent.toString());
				data373.addSubfield(c);
			}
			if(data373.getSubfieldCount() > 0)
				rec.addDataField(data373);
		}
		
		data373 = new DataField("373", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		TitleInstitution titleInstitution = authorDTO.getTitleInstitution();
		if((titleInstitution.getInstitution().getSomeName()!=null) && (!"".equals(titleInstitution.getInstitution().getSomeName()))){
			tempContent = new StringBuffer();
			if(titleInstitution.getInstitution().getControlNumber()!=null){
				Subfield sub0 = new Subfield('0');
				if ((titleInstitution.getInstitution().getControlNumber() != null)
						&& (!"".equals(titleInstitution.getInstitution().getControlNumber())))
					tempContent.append(titleInstitution.getInstitution().getControlNumber());
				if (tempContent.toString().length() > 0) {
					sub0.setContent(tempContent.toString());
					data373.addSubfield(sub0);
				}
				tempContent = new StringBuffer();
			}
			a = new Subfield('a');
			tempContent.append("Titula " + authorDTO.getTitleInstitution().getTitle() + ": ");
			tempContent.append(titleInstitution.getInstitution().getSomeName());
			if (tempContent.toString().length() > 0) {
				a.setContent(tempContent.toString());
				data373.addSubfield(a);
			}
			tempContent = new StringBuffer();
			if(titleInstitution.getYear()!=null){
				Subfield subt = new Subfield('t');
				tempContent.append(titleInstitution.getYear().toString());
				if (tempContent.toString().length() > 0) {
					subt.setContent(tempContent.toString());
					data373.addSubfield(subt);
				}
			}
			if(data373.getSubfieldCount() > 0)
				rec.addDataField(data373);
		}
		
		DataField data375 = new DataField("375", AbstractRecordConverter.BLANK,
				AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((authorDTO.getSex() == 'm') || (authorDTO.getSex() == 'f'))
			tempContent.append(authorDTO.getSex());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data375.addSubfield(a);
		}
		if(data375.getSubfieldCount() > 0)
			rec.addDataField(data375);
		
		DataField data678 = new DataField("678", '1', AbstractRecordConverter.BLANK);
		Subfield sub6 = new Subfield('6');
		if(authorDTO.getBiographyTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data678.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		b = new Subfield('b');
		if ((authorDTO.getBiography().getContent() != null)
				&& (!"".equals(authorDTO.getBiography().getContent())))
			tempContent.append(authorDTO.getBiography().getContent());
		if (tempContent.toString().length() > 0) {
			b.setContent(tempContent.toString());
			data678.addSubfield(b);
		}
		if(data678.getSubfieldCount() > 0)
			rec.addDataField(data678);
		
		for (MultilingualContentDTO biographyTranslation : authorDTO.getBiographyTranslations()) {
			DataField data880 = new DataField("880", '1', AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("678-0" + ordNum);
			data880.addSubfield(sub6);
			b = new Subfield('b');
			b.setContent(biographyTranslation.getTransType() + "-" + biographyTranslation.getLanguage() + ":" + biographyTranslation.getContent());
			data880.addSubfield(b);
			rec.addDataField(data880);
		}
		
//		DataField data710 = new DataField("710", '2', AbstractRecordConverter.BLANK);
//		tempContent = new StringBuffer();
//		institutionDTO = authorDTO.getInstitution();
//		if((institutionDTO!=null) && (institutionDTO.getControlNumber()!=null)){
//			Subfield sub0 = new Subfield('0');
//			if ((institutionDTO.getControlNumber() != null)
//					&& (!"".equals(institutionDTO.getControlNumber())))
//				tempContent.append(institutionDTO.getControlNumber());
//			if (tempContent.toString().length() > 0) {
//				sub0.setContent(tempContent.toString());
//				data710.addSubfield(sub0);
//			}
//			tempContent = new StringBuffer();
//			a = new Subfield('a');
//			if ((institutionDTO.getSomeName() != null)
//					&& (!"".equals(institutionDTO.getSomeName())))
//				tempContent.append(institutionDTO.getSomeName());
//			if (tempContent.toString().length() > 0) {
//				a.setContent(tempContent.toString());
//				data710.addSubfield(a);
//			}
//			tempContent = new StringBuffer();
//			c = new Subfield('c');
//			if ((institutionDTO.getPlace() != null)
//					&& (!"".equals(institutionDTO.getPlace())))
//				tempContent.append(institutionDTO.getPlace());
//			if (tempContent.toString().length() > 0) {
//				c.setContent(tempContent.toString());
//				data710.addSubfield(c);
//			}
//			if(data710.getSubfieldCount() > 0)
//				rec.addDataField(data710);
//		}
		
		DataField data710 = new DataField("710", '2', AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		OrganizationUnitDTO organizationUnitDTO = authorDTO.getOrganizationUnit();
		if((organizationUnitDTO!=null) && (organizationUnitDTO.getControlNumber()!=null)){
			Subfield sub0 = new Subfield('0');
			if ((organizationUnitDTO.getControlNumber() != null)
					&& (!"".equals(organizationUnitDTO.getControlNumber())))
				tempContent.append(organizationUnitDTO.getControlNumber());
			if (tempContent.toString().length() > 0) {
				sub0.setContent(tempContent.toString());
				data710.addSubfield(sub0);
			}
			tempContent = new StringBuffer();
			b = new Subfield('b');
			if ((organizationUnitDTO.getName().getContent() != null)
					&& (!"".equals(organizationUnitDTO.getName().getContent())))
				tempContent.append(organizationUnitDTO.getName().getContent());
			if (tempContent.toString().length() > 0) {
				b.setContent(tempContent.toString());
				data710.addSubfield(b);
			}
			tempContent = new StringBuffer();
			c = new Subfield('c');
			if ((organizationUnitDTO.getPlace() != null)
					&& (!"".equals(organizationUnitDTO.getPlace())))
				tempContent.append(organizationUnitDTO.getPlace());
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
		if ((authorDTO.getUri() != null)
				&& (!"".equals(authorDTO.getUri())))
			tempContent.append(authorDTO.getUri());
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
		AuthorDTO author = (AuthorDTO)dto;
		retVal.setRecordKeywords(new ArrayList<MultilingualContentDTO>());
		if((author.getKeywords().getContent() != null) && (author.getKeywords().getContent().trim().length() > 0)){
			if(author.getKeywords().getLanguage() == null)
				author.getKeywords().setLanguage(author.getBiography().getLanguage());
			retVal.getRecordKeywords().add(author.getKeywords());
		}
		for (MultilingualContentDTO keywords : author.getKeywordsTranslations()) {
			retVal.getRecordKeywords().add(keywords);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsThisRecordOtherRecords(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsThisRecordOtherRecords(Record retVal,
			RecordDTO dto) {
		AuthorDTO author = (AuthorDTO) dto;
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		deleteRelationsThisRecordOtherRecords(retVal, "authorInstitutionRelation", "belongs to");
		deleteRelationsThisRecordOtherRecords(retVal, "authorJobAd", "applied to");
		
		List<RecordRecord> recordRecords = retVal.getRelationsThisRecordOtherRecords();
		if((author.getInstitution() != null) && (author.getInstitution().getControlNumber() != null) && (author.getInstitution().getControlNumber().contains("(BISIS)"))){
			recordRecords.add(new RecordRecord( author.getInstitution().getControlNumber(), "authorInstitutionRelation", "belongs to", startDate, endDate));
		}
		
		if((author.getOrganizationUnit() != null) && (author.getOrganizationUnit().getControlNumber() != null)){
			recordRecords.add(new RecordRecord(author.getOrganizationUnit().getControlNumber(), "authorInstitutionRelation", "belongs to", startDate, endDate));
		}
		
		for (JobAdDTO jobAd : author.getJobApplications()) {
			recordRecords.add(new RecordRecord(jobAd.getControlNumber(), "authorJobAd", "applied to", startDate, endDate));
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
		
		for (ResearchAreaDTO ra : ((AuthorDTO)dto).getResearchAreas()) {
			
			String institutionResearchArea = ra.getClassId();
			
			if(institutionResearchArea != null){
				Classification recClass = new Classification(ResearchAreaDTO.RESEARCH_AREA_SCHEMA, institutionResearchArea, startDate, endDate, new Integer(0), null);
				retVal.getRecordClasses().add(recClass);
			}
		}
		List<Classification> authorPositions = new ArrayList<Classification>();
		for (Classification classification : retVal.getRecordClasses()) {
			if(classification.getCfClassSchemeId().equalsIgnoreCase(PositionDTO.POSITION_SCHEMA)){
				authorPositions.add(classification);
			}	
		}	
		for (Classification authorPosition : authorPositions) {
			retVal.getRecordClasses().remove(authorPosition);
		}
		
		AuthorPosition authorCurrentPosition = ((AuthorDTO)dto).getCurrentPosition();
		if(authorCurrentPosition.getPosition().getClassId() != null){
			Classification recClass = new Classification(PositionDTO.POSITION_SCHEMA, authorCurrentPosition.getPosition().getClassId(), authorCurrentPosition.getStartDate(), endDate, new Integer(0), authorCurrentPosition.getResearchArea());
			retVal.getRecordClasses().add(recClass);
		}
		for (AuthorPosition ap : ((AuthorDTO)dto).getFormerPositions()) {
			
			String authorPosition = ap.getPosition().getClassId();
			
			if(authorPosition != null){
				Classification recClass = new Classification(PositionDTO.POSITION_SCHEMA, authorPosition, ap.getStartDate(), ap.getEndDate(), new Integer(0), ap.getResearchArea());
				retVal.getRecordClasses().add(recClass);
			}
		}
		
	}
	
	private static Log log = LogFactory.getLog(AuthorConverter.class.getName());
}
