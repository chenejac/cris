package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.ResearchAreaDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JobAdDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublisherNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
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
 * MonographDTO object.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class MonographConverter extends ABibliographicRecordConverter {

	/**
	 * Convert the mARC21Record with data about Monograph from MARC21 format to DTO format
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
		MonographDTO retVal = (MonographDTO)rec.getDto();
		retVal.setControlNumber(mARC21Record.getControlNumber());
		retVal.setScopusID(rec.getScopusID());
		
		String originalLanguage = null;//MultilingualContentDTO.LANGUAGE_SERBIAN;
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
		
		DataField df041 = mARC21Record.getDataField("041");
		List<String> languageList = new ArrayList<String>();
		List<String> originalLanguageList = new ArrayList<String>();
		try {
			if (df041.getInd1() == '0'){ 
				for (Subfield a : df041.getSubfields('a')) {
					languageList.add(a.getContent());
				}
			}
			if (df041.getInd1() == '1') {
				for (Subfield a : df041.getSubfields('a')) {
					languageList.add(a.getContent());
				}
				for (Subfield h : df041.getSubfields('h')) {
					originalLanguageList.add(h.getContent());
				}
			}
		} catch (Exception e) {
		}
		retVal.setLanguages(languageList);
		retVal.setOriginalLanguages(originalLanguageList);
		
		DataField df245 = mARC21Record.getDataField("245");
		Subfield sub6 = df245.getSubfield('6');
		String ordNum = null;
		if(sub6 != null){
			ordNum =sub6.getContent().split("\\-")[1];
		}
		
		if (((df245.getInd1() == '0') || (df245.getInd1() == '1')) && (df245.getInd2() == '0')) {
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
			try {
				Subfield n = df245.getSubfield('n');
				retVal.setVolumeCode(n.getContent());
			} catch (Exception e) {
			}
			try {
				Subfield p = df245.getSubfield('p');
				retVal.setVolumeTitle(p.getContent());
			} catch (Exception e) {
			}
		} else
			return false;
		
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
		
		DataField df250 = mARC21Record.getDataField("250");
		try {
			Subfield a = df250.getSubfield('a');
			retVal.setCount(Integer.parseInt(a.getContent()));
		} catch (Exception e) {
		}
		
		DataField df260 = mARC21Record.getDataField("260");
		sub6 = df260.getSubfield('6');
		ordNum = null;
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
			try {
				Subfield x = df490.getSubfield('x');
				retVal.setEditionISSN(x.getContent());
			} catch (Exception e) {
			}
		}
		
		DataField df100 = mARC21Record.getDataField("100");
		if(df100!=null){
			if (df100.getInd1() == '1') {
				try {
					RecordDAO recordDAO = new RecordDAO(new PersonDB());
					AuthorDTO authorDTO = (AuthorDTO) recordDAO
							.getDTO(df100.getSubfield('0').getContent());
					Subfield a = df100.getSubfield('a');
					String content = a.getContent().trim();
					String lastName = Sanitizer.nextPar(content, ',');
					content = Sanitizer.remPar(content, ',');
					String firstName = Sanitizer.nextPar(content, ',');
					AuthorNameDTO choosedAuthorNameDTO = new AuthorNameDTO(firstName, lastName, "");
					if(authorDTO.getAllNames().contains(choosedAuthorNameDTO))
						authorDTO.setName(choosedAuthorNameDTO);
					retVal.setMainAuthor(authorDTO);
				} catch (Exception e) {
					return false;
				}
			} else
				return false;
		}
		
		List<DataField> dfs700 = mARC21Record.getDataFields("700");
		List<AuthorDTO> coAuthorDTOs = new ArrayList<AuthorDTO>();
		for (DataField dataField : dfs700) {
			try {
				if ((dataField.getInd1() == '1') && ("aut".equalsIgnoreCase(dataField.getSubfield('4').getContent()))){
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
					coAuthorDTOs.add(authorDTO);
				}
			} catch (Exception e) {
			}
		}
		retVal.setOtherAuthors(coAuthorDTOs);
		
		List<AuthorDTO> editorDTOs = new ArrayList<AuthorDTO>();
		for (DataField dataField : dfs700) {
			try {
				if ((dataField.getInd1() == '1') && ("edt".equalsIgnoreCase(dataField.getSubfield('4').getContent()))){
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
					editorDTOs.add(authorDTO);
				}
			} catch (Exception e) {
			}
		}
		retVal.setEditors(editorDTOs);
		
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
			DataField df520 = mARC21Record.getDataField("520");
			sub6 = df520.getSubfield('6');
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}
		
			if (df520.getInd1() == '3') {
				Subfield a = df520.getSubfield('a');
				retVal.getAbstracT().setLanguage(originalLanguage);
				retVal.getAbstracT().setContent(a.getContent());
			} 
		} catch (Exception e) {
		}
		
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> abstractTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("520-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						String content = a.getContent().trim();
						String transType = content.substring(0, 1);
						String language = content.substring(2, 5);
						String transContent = content.substring(6);
						MultilingualContentDTO abstractTranslation = new MultilingualContentDTO(transContent, language, transType);
						abstractTranslations.add(abstractTranslation);
					}
				} catch (Exception e) {
				}
			}
			retVal.setAbstractTranslations(abstractTranslations);
		}
		
		DataField df546 = mARC21Record.getDataField("546");
		try {
			Subfield b = df546.getSubfield('b');
			if ((b!=null) && (b.getContent()!=null)){
				retVal.setAlphabet(b.getContent());
			}
		} catch (Exception e) {
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
		
		List<Classification> recordClasses = rec.getRecordClasses();
		ResearchAreaDAO researchAreaDAO = new ResearchAreaDAO();
		for (Classification classification : recordClasses) {
			if(classification.getCfClassSchemeId().equalsIgnoreCase(Types.TYPE_SCHEMA)){
				if (classification.getCfClassSchemeId().equalsIgnoreCase(ResearchAreaDTO.RESEARCH_AREA_SCHEMA)){
					retVal.setResearchArea(researchAreaDAO.getResearchArea(classification.getCfClassId()));
				}
			}
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
		
		MonographDTO monographDTO = (MonographDTO) dto;
		
		DataField data020 = new DataField("020", AbstractRecordConverter.BLANK,
				AbstractRecordConverter.BLANK);
		StringBuffer tempContent = new StringBuffer();
		Subfield a = new Subfield('a');
		if (monographDTO.getIsbn() != null)
			tempContent.append(monographDTO.getIsbn().toString());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data020.addSubfield(a);
		}
		if(data020.getSubfieldCount() > 0)
			rec.addDataField(data020);
		
		if(monographDTO.getOriginalLanguages().size() == 0){
			DataField data041 = new DataField("041", '0',
				AbstractRecordConverter.BLANK);
			for (String language : monographDTO.getLanguages()) {
				tempContent = new StringBuffer();
				a = new Subfield('a');
				if ((language != null) && (!"".equals(language)))
					tempContent.append(language);
				if (tempContent.toString().length() > 0) {
					a.setContent(tempContent.toString());
					data041.addSubfield(a);
				}
			}
			if(data041.getSubfieldCount() > 0)
				rec.addDataField(data041);
		} else if(monographDTO.getOriginalLanguages().size() > 0){
					DataField data041 = new DataField("041", '1',
						AbstractRecordConverter.BLANK);
					for (String language : monographDTO.getLanguages()) {
						tempContent = new StringBuffer();
						a = new Subfield('a');
						if ((language != null) && (!"".equals(language)))
							tempContent.append(language);
						if (tempContent.toString().length() > 0) {
							a.setContent(tempContent.toString());
							data041.addSubfield(a);
						}
					}
					for (String language : monographDTO.getOriginalLanguages()) {
						tempContent = new StringBuffer();
						Subfield h = new Subfield('h');
						if ((language != null) && (!"".equals(language)))
							tempContent.append(language);
						if (tempContent.toString().length() > 0) {
							h.setContent(tempContent.toString());
							data041.addSubfield(h);
						}
					}
					if(data041.getSubfieldCount() > 0)
						rec.addDataField(data041);
				}
		
		DataField data245 = (monographDTO.getMainAuthor().getControlNumber() == null)?(new DataField("245", '1', '0')):(new DataField("245", '0', '0'));
		Subfield sub6 = new Subfield('6');
		if((monographDTO.getTitleTranslations().size() > 0) || (monographDTO.getSubtitleTranslations().size() > 0)){
			ordNum++;
			sub6.setContent("880-0"+ordNum);
			data245.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((monographDTO.getTitle().getContent() != null)
				&& (!"".equals(monographDTO.getTitle().getContent())))
			tempContent.append(monographDTO.getTitle().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data245.addSubfield(a);
		}
		tempContent = new StringBuffer();
		Subfield b = new Subfield('b');
		if ((monographDTO.getSubtitle().getContent() != null)
				&& (!"".equals(monographDTO.getSubtitle().getContent())))
			tempContent.append(monographDTO.getSubtitle().getContent());
		if (tempContent.toString().length() > 0) {
			b.setContent(tempContent.toString());
			data245.addSubfield(b);
		}
		tempContent = new StringBuffer();
		Subfield n = new Subfield('n');
		if ((monographDTO.getVolumeCode() != null)
				&& (!"".equals(monographDTO.getVolumeCode())))
			tempContent.append(monographDTO.getVolumeCode());
		if (tempContent.toString().length() > 0) {
			n.setContent(tempContent.toString());
			data245.addSubfield(n);
		}
		tempContent = new StringBuffer();
		Subfield p = new Subfield('p');
		if ((monographDTO.getVolumeTitle() != null)
				&& (!"".equals(monographDTO.getVolumeTitle())))
			tempContent.append(monographDTO.getVolumeTitle());
		if (tempContent.toString().length() > 0) {
			p.setContent(tempContent.toString());
			data245.addSubfield(p);
		}
		if(data245.getSubfieldCount() > 0)
			rec.addDataField(data245);

		for (MultilingualContentDTO titleTranslation : monographDTO.getTitleTranslations()) {
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
		
		for (MultilingualContentDTO subtitleTranslation : monographDTO.getSubtitleTranslations()) {
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
	
		DataField data250 = new DataField("250", AbstractRecordConverter.BLANK,
				AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if (monographDTO.getCount() != null)
			tempContent.append(monographDTO.getCount());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data250.addSubfield(a);
		}
		if(data250.getSubfieldCount() > 0)
			rec.addDataField(data250);
		
		DataField data260 = new DataField("260", AbstractRecordConverter.BLANK,
				AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(monographDTO.getPublisher().getPublisherTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data260.addSubfield(sub6);
		}	
		PublisherNameDTO publisherNameDTO = monographDTO.getPublisher().getOriginalPublisher();
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((publisherNameDTO.getPlace() != null)
				&& (!"".equals(publisherNameDTO.getPlace())))
			tempContent.append(Sanitizer.sanitize(publisherNameDTO.getPlace()));
		if ((publisherNameDTO.getState() != null)
				&& (!"".equals(publisherNameDTO.getState())))
			tempContent.append(((tempContent.toString().length() > 0)?(", "):("")) + Sanitizer.sanitize(publisherNameDTO.getState()));
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data260.addSubfield(a);
		}
		tempContent = new StringBuffer();
		b = new Subfield('b');
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
		Subfield c = new Subfield('c');
		if (monographDTO.getPublicationYear() != null)
			tempContent.append(monographDTO.getPublicationYear().toString());
		if (tempContent.toString().length() > 0) {
			c.setContent(tempContent.toString());
			data260.addSubfield(c);
		}
		if(data260.getSubfieldCount() > 0)
			rec.addDataField(data260);
		
		for (PublisherNameDTO publisherTranslation : monographDTO.getPublisher().getPublisherTranslations()) {
			try {
				DataField data880 = new DataField("880", AbstractRecordConverter.BLANK,
						AbstractRecordConverter.BLANK);
				tempContent = new StringBuffer();
				sub6 = new Subfield('6');
				sub6.setContent("260-0" + ordNum);
				data880.addSubfield(sub6);
				tempContent.append(publisherTranslation.getTransType() + "-");
				tempContent.append(publisherTranslation.getLanguage() + ":");
				a = new Subfield('a');
				if ((publisherNameDTO.getPlace() != null)
						&& (!"".equals(publisherNameDTO.getPlace())))
					tempContent.append(Sanitizer.sanitize(publisherNameDTO.getPlace()));
				if ((publisherNameDTO.getState() != null)
						&& (!"".equals(publisherNameDTO.getState())))
					tempContent.append(", " + Sanitizer.sanitize(publisherNameDTO.getState()));
				if (tempContent.toString().length() > 0) {
					a.setContent(tempContent.toString());
					data260.addSubfield(a);
				}
				tempContent = new StringBuffer();
				tempContent.append(publisherTranslation.getTransType() + "-");
				tempContent.append(publisherTranslation.getLanguage() + ":");
				b = new Subfield('b');
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
		if (monographDTO.getNumberOfPages() != null)
			tempContent.append(monographDTO.getNumberOfPages());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data300.addSubfield(a);
		}
		tempContent = new StringBuffer();
		c = new Subfield('c');
		if ((monographDTO.getDimension() != null)
				&& (!"".equals(monographDTO.getDimension())))
			tempContent.append(monographDTO.getDimension());
		if (tempContent.toString().length() > 0) {
			c.setContent(tempContent.toString());
			data300.addSubfield(c);
		}
		if(data300.getSubfieldCount() > 0)
			rec.addDataField(data300);
		
		DataField data490 = new DataField("490", '0', AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((monographDTO.getEditionTitle() != null)
				&& (!"".equals(monographDTO.getEditionTitle())))
			tempContent.append(monographDTO.getEditionTitle());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data490.addSubfield(a);
		}
		tempContent = new StringBuffer();
		Subfield v = new Subfield('v');
		if (monographDTO.getEditionNumber() != null)
			tempContent.append(monographDTO.getEditionNumber());
		if (tempContent.toString().length() > 0) {
			v.setContent(tempContent.toString());
			data490.addSubfield(v);
		}
		tempContent = new StringBuffer();
		Subfield x = new Subfield('x');
		if (monographDTO.getEditionISSN() != null)
			tempContent.append(monographDTO.getEditionISSN());
		if (tempContent.toString().length() > 0) {
			x.setContent(tempContent.toString());
			data490.addSubfield(x);
		}
		if(data490.getSubfieldCount() > 0)
			rec.addDataField(data490);
		
		if(monographDTO.getMainAuthor().getControlNumber() != null){
			AuthorDTO mainAuthor = monographDTO.getMainAuthor();
			DataField data100 = new DataField("100", '1', AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			Subfield sub0 = new Subfield('0');
			sub0.setContent(mainAuthor.getControlNumber());
			data100.addSubfield(sub0);
			a = new Subfield('a');
			AuthorNameDTO authorName = mainAuthor.getName();
			if ((authorName.getLastname() != null)
					&& (!"".equals(authorName.getLastname())))
				tempContent.append(Sanitizer.sanitize(authorName.getLastname()));
			if ((authorName.getFirstname() != null)
					&& (!"".equals(authorName.getFirstname())))
				tempContent.append(", " + Sanitizer.sanitize(authorName.getFirstname()));
			if (tempContent.toString().length() > 0) {
				a.setContent(tempContent.toString());
				data100.addSubfield(a);
			}
			tempContent = new StringBuffer();
			Subfield u = new Subfield('u');
			InstitutionDTO authorInstitution = mainAuthor.getInstitution();
			OrganizationUnitDTO authorOrganizationUnit = mainAuthor.getOrganizationUnit();
			if(authorOrganizationUnit.getControlNumber() != null)
				tempContent.append(authorOrganizationUnit.getSomeName());
			else if (authorInstitution.getControlNumber() != null)
				tempContent.append(authorInstitution.getSomeName());
			if (tempContent.toString().length() > 0) {
				u.setContent(tempContent.toString());
				data100.addSubfield(u);
			}
			if(data100.getSubfieldCount() > 0)
				rec.addDataField(data100);
		}
			
		for (AuthorDTO author : monographDTO.getOtherAuthors()) {
			DataField data700 = new DataField("700", '1',
					AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			Subfield sub0 = new Subfield('0');
			sub0.setContent(author.getControlNumber());
			data700.addSubfield(sub0);
			Subfield sub4 = new Subfield('4');
			sub4.setContent("aut");
			data700.addSubfield(sub4);
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
		
		for (AuthorDTO editor : monographDTO.getEditors()) {
			DataField data700 = new DataField("700", '1',
					AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			Subfield sub0 = new Subfield('0');
			sub0.setContent(editor.getControlNumber());
			data700.addSubfield(sub0);
			Subfield sub4 = new Subfield('4');
			sub4.setContent("edt");
			data700.addSubfield(sub4);
			a = new Subfield('a');
			AuthorNameDTO authorName = editor.getName();
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
			if(data700.getSubfieldCount() > 0)
				rec.addDataField(data700);
		}
		
		DataField data500 = new DataField("500", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(monographDTO.getNoteTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data500.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((monographDTO.getNote().getContent() != null)
				&& (!"".equals(monographDTO.getNote().getContent())))
			tempContent.append(monographDTO.getNote().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data500.addSubfield(a);
		}
		if(data500.getSubfieldCount() > 0)
			rec.addDataField(data500);
		
		for (MultilingualContentDTO noteTranslation : monographDTO.getNoteTranslations()) {
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
		
		DataField data520 = new DataField("520", '3', AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(monographDTO.getAbstractTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data520.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((monographDTO.getAbstracT().getContent() != null)
				&& (!"".equals(monographDTO.getAbstracT().getContent())))
			tempContent.append(monographDTO.getAbstracT().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data520.addSubfield(a);
		}
		if(data520.getSubfieldCount() > 0)
			rec.addDataField(data520);
		
		for (MultilingualContentDTO abstractTranslation : monographDTO.getAbstractTranslations()) {
			DataField data880 = new DataField("880", '3', AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("520-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(abstractTranslation.getTransType() + "-" + abstractTranslation.getLanguage() + ":" + abstractTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		DataField data546 = new DataField("546", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		b = new Subfield('b');
		if ((monographDTO.getAlphabet() != null)
				&& (!"".equals(monographDTO.getAlphabet())))
			tempContent.append(monographDTO.getAlphabet());
		if (tempContent.toString().length() > 0) {
			b.setContent(tempContent.toString());
			data546.addSubfield(b);
		}
		if(data546.getSubfieldCount() > 0)
			rec.addDataField(data546);

		
		DataField data653 = new DataField("653", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(monographDTO.getKeywordsTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data653.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((monographDTO.getKeywords().getContent() != null)
				&& (!"".equals(monographDTO.getKeywords().getContent())))
			tempContent.append(monographDTO.getKeywords().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data653.addSubfield(a);
		}
		if(data653.getSubfieldCount() > 0)
			rec.addDataField(data653);
		
		for (MultilingualContentDTO keywordsTranslation : monographDTO.getKeywordsTranslations()) {
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
		
		DataField data856 = new DataField("856", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		Subfield u = new Subfield('u');
		if ((monographDTO.getUri() != null)
				&& (!"".equals(monographDTO.getUri())))
			tempContent.append(monographDTO.getUri());
		if (tempContent.toString().length() > 0) {
			u.setContent(tempContent.toString());
			data856.addSubfield(u);
		}
		tempContent = new StringBuffer();
		u = new Subfield('u');
		if ((monographDTO.getDoi() != null)
				&& (!"".equals(monographDTO.getDoi())))
			tempContent.append("doi:" + monographDTO.getDoi().trim());
		if (tempContent.toString().length() > 0) {
			u.setContent(tempContent.toString());
			data856.addSubfield(u);
		}
		if(data856.getSubfieldCount() > 0)
			rec.addDataField(data856);
		
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRecordClasses(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRecordClasses(Record retVal, RecordDTO dto) {
		
		super.setRecordClasses(retVal, dto);
		
		Classification researchArea = null;
		for (Classification classification : retVal.getRecordClasses()) {
			if(classification.getCfClassSchemeId().equalsIgnoreCase(ResearchAreaDTO.RESEARCH_AREA_SCHEMA)){
				researchArea = classification;
				break;
			}	
		}	
		
		if(researchArea!=null){
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
		
		String monographResearchArea = ((MonographDTO)dto).getResearchArea().getClassId();
		
		if(monographResearchArea != null){
			Classification recClass = new Classification(ResearchAreaDTO.RESEARCH_AREA_SCHEMA, monographResearchArea, startDate, endDate, new Integer(0), null);
			retVal.getRecordClasses().add(recClass);
		}
	}
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsOtherRecordsThisRecord(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsOtherRecordsThisRecord(Record retVal,
			RecordDTO dto) {
		MonographDTO monograph = (MonographDTO) dto;
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		deleteRelationsOtherRecordsThisRecord(retVal, "authorshipType", "is editor of");
		deleteRelationsOtherRecordsThisRecord(retVal, "authorshipType", "is author of");
		
		List<RecordRecord> recordRecords = retVal.getRelationsOtherRecordsThisRecord();
		for (AuthorDTO editor : monograph.getEditors()) {
			recordRecords.add(new RecordResultPublication(editor.getControlNumber(), "authorshipType", "is editor of", startDate, endDate, null));
		}
		
		for (AuthorDTO author : monograph.getAllAuthors()) {
			recordRecords.add(new RecordResultPublication(author.getControlNumber(), "authorshipType", "is author of", startDate, endDate, null));
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsThisRecordOtherRecords(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsThisRecordOtherRecords(Record retVal,
			RecordDTO dto) {
		MonographDTO monograph = (MonographDTO) dto;
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
		
		for (JobAdDTO jobAd : monograph.getJobAds()) {
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
	 * @see yu.ac.ns.ftn.informatika.bibliography.dto.recordconverters.
	 * ABibliographicRecordConverter
	 * #getControlField008(rs.ac.uns.ftn.informatika
	 * .bibliography.dto.ARecordDTO)
	 */
	@Override
	protected ControlField getControlField008(RecordDTO dto) {
		MonographDTO monographDTO = (MonographDTO) dto;
		ControlField retVal = null;
		String content = "";
		for(int i=0;i<7;i++)
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		content += monographDTO.getPublicationYear();
		for(int i=11;i<15;i++)
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		content += "rb" + AbstractRecordConverter.BLANK;
		for(int i=18;i<35;i++)
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		if(monographDTO.getTitle().getLanguage() != null){
			content += monographDTO.getTitle().getLanguage();
		} else {
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		}
		content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		retVal = new ControlField("008");
		retVal.setContent(content);
		return retVal;
	}

}
