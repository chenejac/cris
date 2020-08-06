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
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JobAdDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublisherNameDTO;
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
 * ProceedingsDTO object.
 * 
 * @author chenejac@uns.ac.rs
 * 
 */
@SuppressWarnings("serial")
public class ProceedingsConverter extends ABibliographicRecordConverter {

	
	/**
	 * Converts the mARC21Record with data about Proceedings from MARC21 format to DTO
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
		ProceedingsDTO retVal = (ProceedingsDTO)rec.getDto();
		retVal.setControlNumber(mARC21Record.getControlNumber());
		retVal.setScopusID(rec.getScopusID());
		
		String originalLanguage = MultilingualContentDTO.LANGUAGE_SERBIAN;
		try{
			ControlField cf008 = mARC21Record.getControlField("008");
			originalLanguage = cf008.getCharPositions(35, 3);
		} catch (Exception e){
		}
		
		DataField df020 = mARC21Record.getDataField("020");
		try {
			Subfield a = df020.getSubfield('a');
			retVal.setIsbn(a.getContent());
		} catch (Exception e) {
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
		
		DataField df111 = mARC21Record.getDataField("111");
		if (df111.getInd1() == '2') {
			try {
				RecordDAO recordDAO = new RecordDAO(new RecordDB());
				ConferenceDTO con = (ConferenceDTO)(recordDAO.getDTO(df111.getSubfield('0').getContent()));
				if(con==null){
					return false;
				}
				retVal.setConference(con);
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
		
		String ordNum = null;
		try {
			DataField df210 = mARC21Record.getDataField("210");
			Subfield sub6 = df210.getSubfield('6');
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
		
		DataField df245 = mARC21Record.getDataField("245");
		ordNum = null;
		Subfield sub6 = null;
		if(df245!=null){
			 sub6 = df245.getSubfield('6');
		if(sub6 != null){
			ordNum = sub6.getContent().split("\\-")[1];
		}
		
		if ((df245.getInd1() == '0') && (df245.getInd2() == '0')) {
			try {
				Subfield a = df245.getSubfield('a');
				retVal.getTitle().setLanguage(originalLanguage);
				retVal.getTitle().setContent(a.getContent());
			} catch (Exception e) {
			}
			try {
				Subfield b = df245.getSubfield('b');
				retVal.getSubtitle().setLanguage(originalLanguage);
				retVal.getSubtitle().setContent(b.getContent());
			} catch (Exception e) {
			}
		} else {
			return false;
		}
		}
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> titleTranslations = new ArrayList<MultilingualContentDTO>();
			List<MultilingualContentDTO> subtitleTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("245-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						Subfield b = dataField.getSubfield('b');
						if(a != null){
							String content = a.getContent().trim();
							String transType = content.substring(0, 1);
							String language = content.substring(2, 5);
							String transContent = content.substring(6);
							MultilingualContentDTO titleTranslation = new MultilingualContentDTO(transContent, language, transType);
							titleTranslations.add(titleTranslation);
						}
						if(b != null){
							String content = b.getContent().trim();
							String transType = content.substring(0, 1);
							String language = content.substring(2, 5);
							String transContent = content.substring(6);
							MultilingualContentDTO subtitleTranslation = new MultilingualContentDTO(transContent, language, transType);
							subtitleTranslations.add(subtitleTranslation);
						}
					}
				} catch (Exception e) {
				}
			}
			retVal.setTitleTranslations(titleTranslations);
			retVal.setSubtitleTranslations(subtitleTranslations);
		}
		
		DataField df260 = mARC21Record.getDataField("260");
		ordNum = null;
		if (df260!=null){ 
			 sub6 = df260.getSubfield('6');
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}
			try {
				Subfield b = df260.getSubfield('b');
				String content = b.getContent().trim();
				String pubName = Sanitizer.nextPar(content, ',');
				content = Sanitizer.remPar(content, ',');
				String place = Sanitizer.nextPar(content, ',');
				content = Sanitizer.remPar(content, ',');
				String state = Sanitizer.nextPar(content, ',');
				PublisherNameDTO publisherNameDTO = retVal.getPublisher().getOriginalPublisher();
				publisherNameDTO.setName(pubName);
				publisherNameDTO.setPlace(place);
				publisherNameDTO.setState(state);
				publisherNameDTO.setLanguage(originalLanguage);
			} catch (Exception e) {
			}
			try {
				Subfield c = df260.getSubfield('c');
				retVal.setPublicationYear(c.getContent());
			} catch (Exception e) {
			}
		}
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<PublisherNameDTO> publisherTranslations = new ArrayList<PublisherNameDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("260-"+ordNum)) {
						Subfield b = dataField.getSubfield('b');
						String content = b.getContent().trim();
						String transType = content.substring(0, 1);
						String language = content.substring(2, 5);
						String transContent = content.substring(6);
						String pubName = Sanitizer.nextPar(transContent, ',');
						transContent = Sanitizer.remPar(transContent, ',');
						String place = Sanitizer.nextPar(transContent, ',');
						transContent = Sanitizer.remPar(transContent, ',');
						String state = Sanitizer.nextPar(transContent, ',');
						PublisherNameDTO publisherNameDTO = new PublisherNameDTO(pubName, place, state, language, transType);
						publisherTranslations.add(publisherNameDTO);
					}
				} catch (Exception e) {
				}
			}
			retVal.getPublisher().setPublisherTranslations(publisherTranslations);
		}
		
		DataField df300 = mARC21Record.getDataField("300");
		try {
			Subfield a = df300.getSubfield('a');
			retVal.setNumberOfPages(Integer.parseInt(a.getContent()));
		} catch (Exception e) {
		}
		try {
			Subfield c = df300.getSubfield('c');
			retVal.setDimension(c.getContent());
		} catch (Exception e) {
		}
		
		DataField df490 = mARC21Record.getDataField("490");
		if ((df490 != null) && (df490.getInd1() == '0')) {
			try {
				Subfield a = df490.getSubfield('a');
				retVal.setEditionTitle(a.getContent());
			} catch (Exception e) {
			}
			try {
				Subfield v = df490.getSubfield('v');
				retVal.setEditionNumber(Integer.parseInt(v.getContent()));
			} catch (Exception e) {
			}
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
		
//		retVal.setClassifications(rec.getRecordClasses());
		return true;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setDataFields(rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record,
	 *      rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setDataFields(MARC21Record rec, RecordDTO dto) {
		
		Integer ordNum = 0;
		
		ProceedingsDTO proceedingsDTO = (ProceedingsDTO) dto;

		DataField data020 = new DataField("020", AbstractRecordConverter.BLANK,
				AbstractRecordConverter.BLANK);
		StringBuffer tempContent = new StringBuffer();
		Subfield a = new Subfield('a');
		if (proceedingsDTO.getIsbn() != null)
			tempContent.append(proceedingsDTO.getIsbn().toString());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data020.addSubfield(a);
		}
		if(data020.getSubfieldCount() > 0)
			rec.addDataField(data020);
		
		for (String language : proceedingsDTO.getLanguages()) {
			DataField data041 = new DataField("041", '0',
					AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			a = new Subfield('a');
			if ((language != null) && (!"".equals(language)))
				tempContent.append(language);
			if (tempContent.toString().length() > 0) {
				a.setContent(tempContent.toString());
				data041.addSubfield(a);
			}
			if(data041.getSubfieldCount() > 0)
				rec.addDataField(data041);
		}
		
		DataField data111 = new DataField("111", '2', AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		ConferenceDTO conferenceDTO = proceedingsDTO.getConference();
		Subfield sub0 = new Subfield('0');
		if ((conferenceDTO.getControlNumber() != null)
				&& (!"".equals(conferenceDTO.getControlNumber())))
			tempContent.append(conferenceDTO.getControlNumber());
		if (tempContent.toString().length() > 0) {
			sub0.setContent(tempContent.toString());
			data111.addSubfield(sub0);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((conferenceDTO.getSomeName() != null)
				&& (!"".equals(conferenceDTO.getSomeName())))
			tempContent.append(conferenceDTO.getSomeName());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data111.addSubfield(a);
		}
		tempContent = new StringBuffer();
		Subfield c = new Subfield('c');
		if ((conferenceDTO.getPlace() != null)
				&& (!"".equals(conferenceDTO.getPlace())))
			tempContent.append(conferenceDTO.getPlace());
		if (tempContent.toString().length() > 0) {
			c.setContent(tempContent.toString());
			data111.addSubfield(c);
		}
		tempContent = new StringBuffer();
		Subfield d = new Subfield('d');
		if (conferenceDTO.getYear() != null)
			tempContent.append(conferenceDTO.getYear().toString());
		if (tempContent.toString().length() > 0) {
			d.setContent(tempContent.toString());
			data111.addSubfield(d);
		}
		tempContent = new StringBuffer();
		Subfield n = new Subfield('n');
		if (conferenceDTO.getNumber() != null)
			tempContent.append(conferenceDTO.getNumber().toString());
		if (tempContent.toString().length() > 0) {
			n.setContent(tempContent.toString());
			data111.addSubfield(n);
		}
		if(data111.getSubfieldCount() > 0)
			rec.addDataField(data111);
		
		DataField data210 = new DataField("210", '0', AbstractRecordConverter.BLANK);
		Subfield sub6 = new Subfield('6');
		if(proceedingsDTO.getNameAbbreviationTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data210.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((proceedingsDTO.getNameAbbreviation().getContent() != null)
				&& (!"".equals(proceedingsDTO.getNameAbbreviation().getContent())))
			tempContent.append(proceedingsDTO.getNameAbbreviation().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data210.addSubfield(a);
		}
		if(data210.getSubfieldCount() > 0)
			rec.addDataField(data210);
		
		for (MultilingualContentDTO nameAbbrevTranslation : proceedingsDTO.getNameAbbreviationTranslations()) {
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
		
		DataField data245 = new DataField("245", '0', '0');
		sub6 = new Subfield('6');
		if((proceedingsDTO.getTitleTranslations().size() > 0) || (proceedingsDTO.getSubtitleTranslations().size() > 0)){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data245.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((proceedingsDTO.getTitle().getContent() != null)
				&& (!"".equals(proceedingsDTO.getTitle().getContent())))
			tempContent.append(proceedingsDTO.getTitle().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data245.addSubfield(a);
		}
		tempContent = new StringBuffer();
		Subfield b = new Subfield('b');
		if ((proceedingsDTO.getSubtitle().getContent() != null)
				&& (!"".equals(proceedingsDTO.getSubtitle().getContent())))
			tempContent.append(proceedingsDTO.getSubtitle().getContent());
		if (tempContent.toString().length() > 0) {
			b.setContent(tempContent.toString());
			data245.addSubfield(b);
		}
		if(data245.getSubfieldCount() > 0)
			rec.addDataField(data245);
		
		for (MultilingualContentDTO titleTranslation : proceedingsDTO.getTitleTranslations()) {
			DataField data880 = new DataField("880", '0', '0');
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("245-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(titleTranslation.getTransType() + "-" + titleTranslation.getLanguage() + ":" + titleTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		for (MultilingualContentDTO subtitleTranslation : proceedingsDTO.getSubtitleTranslations()) {
			DataField data880 = new DataField("880", '0', '0');
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("245-0" + ordNum);
			data880.addSubfield(sub6);
			b = new Subfield('b');
			b.setContent(subtitleTranslation.getTransType() + "-" + subtitleTranslation.getLanguage() + ":" + subtitleTranslation.getContent());
			data880.addSubfield(b);
			rec.addDataField(data880);
		}
		
		DataField data260 = new DataField("260", AbstractRecordConverter.BLANK,
				AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(proceedingsDTO.getPublisher().getPublisherTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data260.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		b = new Subfield('b');
		PublisherNameDTO publisherNameDTO = proceedingsDTO.getPublisher().getOriginalPublisher();
		if ((publisherNameDTO.getName() != null)
				&& (!"".equals(publisherNameDTO.getName())))
			tempContent.append(Sanitizer.sanitize(publisherNameDTO.getName()));
		if ((publisherNameDTO.getPlace() != null)
				&& (!"".equals(publisherNameDTO.getPlace())))
			tempContent.append(", " + Sanitizer.sanitize(publisherNameDTO.getPlace()));
		if ((publisherNameDTO.getState() != null)
				&& (!"".equals(publisherNameDTO.getState())))
			tempContent.append(", " + Sanitizer.sanitize(publisherNameDTO.getState()));
		if (tempContent.toString().length() > 0) {
			b.setContent(tempContent.toString());
			data260.addSubfield(b);
		}
		tempContent = new StringBuffer();
		c = new Subfield('c');
		if (proceedingsDTO.getPublicationYear() != null)
			tempContent.append(proceedingsDTO.getPublicationYear().toString());
		if (tempContent.toString().length() > 0) {
			c.setContent(tempContent.toString());
			data260.addSubfield(c);
		}
		if(data260.getSubfieldCount() > 0)
			rec.addDataField(data260);
		
		for (PublisherNameDTO publisherTranslation : proceedingsDTO.getPublisher().getPublisherTranslations()) {
			try {
				DataField data880 = new DataField("880", AbstractRecordConverter.BLANK,
						AbstractRecordConverter.BLANK);
				tempContent = new StringBuffer();
				sub6 = new Subfield('6');
				sub6.setContent("260-0" + ordNum);
				data880.addSubfield(sub6);
				b = new Subfield('b');
				tempContent.append(publisherTranslation.getTransType() + "-");
				tempContent.append(publisherTranslation.getLanguage() + ":");
				if ((publisherTranslation.getName() != null)
						&& (!"".equals(publisherTranslation.getName())))
					tempContent.append(Sanitizer.sanitize(publisherTranslation.getName()));
				if ((publisherTranslation.getPlace() != null)
						&& (!"".equals(publisherTranslation.getPlace())))
					tempContent.append(", " + Sanitizer.sanitize(publisherTranslation.getPlace()));
				if ((publisherTranslation.getState() != null)
						&& (!"".equals(publisherTranslation.getState())))
					tempContent.append(", " + Sanitizer.sanitize(publisherTranslation.getState()));
				if (tempContent.toString().length() > 0) {
					b.setContent(tempContent.toString());
					data880.addSubfield(b);
				}
				if(data880.getSubfieldCount() > 0)
					rec.addDataField(data880);
			} catch (Exception e) {
			}
		}

		DataField data300 = new DataField("300", AbstractRecordConverter.BLANK,
				AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if (proceedingsDTO.getNumberOfPages() != null)
			tempContent.append(proceedingsDTO.getNumberOfPages());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data300.addSubfield(a);
		}
		tempContent = new StringBuffer();
		c = new Subfield('c');
		if ((proceedingsDTO.getDimension() != null)
				&& (!"".equals(proceedingsDTO.getDimension())))
			tempContent.append(proceedingsDTO.getDimension());
		if (tempContent.toString().length() > 0) {
			c.setContent(tempContent.toString());
			data300.addSubfield(c);
		}
		if(data300.getSubfieldCount() > 0)
			rec.addDataField(data300);

		DataField data490 = new DataField("490", '0', AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((proceedingsDTO.getEditionTitle() != null)
				&& (!"".equals(proceedingsDTO.getEditionTitle())))
			tempContent.append(proceedingsDTO.getEditionTitle());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data490.addSubfield(a);
		}
		tempContent = new StringBuffer();
		Subfield v = new Subfield('v');
		if (proceedingsDTO.getEditionNumber() != null)
			tempContent.append(proceedingsDTO.getEditionNumber());
		if (tempContent.toString().length() > 0) {
			v.setContent(tempContent.toString());
			data490.addSubfield(v);
		}
		if(data490.getSubfieldCount() > 0)
			rec.addDataField(data490);
		
		DataField data500 = new DataField("500", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(proceedingsDTO.getNoteTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data500.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((proceedingsDTO.getNote().getContent() != null)
				&& (!"".equals(proceedingsDTO.getNote().getContent())))
			tempContent.append(proceedingsDTO.getNote().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data500.addSubfield(a);
		}
		if(data500.getSubfieldCount() > 0)
			rec.addDataField(data500);
		
		for (MultilingualContentDTO noteTranslation : proceedingsDTO.getNoteTranslations()) {
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
		if(proceedingsDTO.getKeywordsTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data653.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((proceedingsDTO.getKeywords().getContent() != null)
				&& (!"".equals(proceedingsDTO.getKeywords().getContent())))
			tempContent.append(proceedingsDTO.getKeywords().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data653.addSubfield(a);
		}
		if(data653.getSubfieldCount() > 0)
			rec.addDataField(data653);
		
		for (MultilingualContentDTO keywordsTranslation : proceedingsDTO.getKeywordsTranslations()) {
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

		for (AuthorDTO author : proceedingsDTO.getEditors()) {
			DataField data700 = new DataField("700", '1',
					AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub0 = new Subfield('0');
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
		if ((proceedingsDTO.getUri() != null)
				&& (!"".equals(proceedingsDTO.getUri())))
			tempContent.append(proceedingsDTO.getUri());
		if (tempContent.toString().length() > 0) {
			u.setContent(tempContent.toString());
			data856.addSubfield(u);
		}
		tempContent = new StringBuffer();
		u = new Subfield('u');
		if ((proceedingsDTO.getDoi() != null)
				&& (!"".equals(proceedingsDTO.getDoi())))
			tempContent.append("doi:" + proceedingsDTO.getDoi().trim());
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
		ProceedingsDTO proceedings = (ProceedingsDTO) dto;
	
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
		
		for (AuthorDTO editor : proceedings.getEditors()) {
			recordRecords.add(new RecordResultPublication(editor.getControlNumber(), "authorshipType", "is editor of", startDate, endDate, null));
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsThisRecordOtherRecords(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsThisRecordOtherRecords(Record retVal,
			RecordDTO dto) {
		ProceedingsDTO proceedings = (ProceedingsDTO) dto;
		
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		deleteRelationsThisRecordOtherRecords(retVal, "publicationEventRelation", "is output from");
		deleteRelationsThisRecordOtherRecords(retVal, "publicationJobAd", "applied to");
		
		List<RecordRecord> recordRecords = retVal.getRelationsThisRecordOtherRecords();
		
		recordRecords.add(new RecordRecord(proceedings.getConference().getControlNumber(), "publicationEventRelation", "is output from", startDate, endDate));
		for (JobAdDTO jobAd : proceedings.getJobAds()) {
			recordRecords.add(new RecordRecord(jobAd.getControlNumber(), "publicationJobAd", "applied to", startDate, endDate));
		}
	}

	/**
	 * 
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.
	 *      ABibliographicRecordConverter
	 *     
	 *     
	 *      #getControlField006(rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected ControlField getControlField006(RecordDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.
	 *      ABibliographicRecordConverter
	 *     
	 *     
	 *      #getControlField007(rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected ControlField getControlField007(RecordDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see yu.ac.ns.ftn.informatika.bibliography.dto.recordconverters.
	 * ABibliographicRecordConverter
	 * #getControlField008(rs.ac.uns.ftn.informatika
	 * .bibliography.dto.ARecordDTO)
	 */
	@Override
	protected ControlField getControlField008(RecordDTO dto) {
		ProceedingsDTO proceedingsDTO = (ProceedingsDTO) dto;
		ControlField retVal = new ControlField("008");
		String content = "";
		for(int i=0;i<35;i++)
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		if(proceedingsDTO.getTitle().getLanguage() != null)
			content += proceedingsDTO.getTitle().getLanguage();
		else 
			content += MultilingualContentDTO.LANGUAGE_SERBIAN;
		content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		retVal.setContent(content);
		return retVal;
	}

}
