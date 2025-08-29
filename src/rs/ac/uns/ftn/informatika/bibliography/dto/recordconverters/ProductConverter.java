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
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProductDTO;
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
 * ProductDTO object.
 *
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class ProductConverter extends ABibliographicRecordConverter {


	/**
	 * Convert the mARC21Record with data about product from MARC21 format to DTO format
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
		ProductDTO retVal = (ProductDTO)rec.getDto();
		retVal.setControlNumber(mARC21Record.getControlNumber());
		retVal.setScopusID(rec.getScopusID());

		String originalLanguage = MultilingualContentDTO.LANGUAGE_SERBIAN;
		try{
			ControlField cf008 = mARC21Record.getControlField("008");
			originalLanguage = cf008.getCharPositions(35, 3);
		} catch (Exception e){
		}

		DataField df084 = mARC21Record.getDataField("084");
		try {
			Subfield a = df084.getSubfield('a');
			retVal.setInternalNumber(a.getContent());
		} catch (Exception e) {
		}

		DataField df245 = mARC21Record.getDataField("245");
		Subfield sub6 = df245.getSubfield('6');
		String ordNum = null;
		if(sub6 != null){
			ordNum = sub6.getContent().split("\\-")[1];
		}
		if ((df245.getInd1() == '0') && (df245.getInd2() == '0')) {
			try {
				Subfield a = df245.getSubfield('a');
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
					if (dataField.getSubfield('6').getContent().equals("245-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						if(a != null){
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
				if (dataField.getInd1() == '1'){
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

		try {
			ordNum = null;
			DataField df500 = mARC21Record.getDataField("500");
			sub6 = df500.getSubfield('6');
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}

			Subfield a = df500.getSubfield('a');
			retVal.getDescription().setLanguage(originalLanguage);
			retVal.getDescription().setContent(a.getContent());
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
						MultilingualContentDTO descriptionTranslation = new MultilingualContentDTO(transContent, language, transType);
						descriptionTranslations.add(descriptionTranslation);
					}
				} catch (Exception e) {
				}
			}
			retVal.setDescriptionTranslations(descriptionTranslations);
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

//		try{
//			List<RecordRecord> recordRecords = rec.getRelationsThisRecordOtherRecords();
//			RecordDAO recordDAO = new RecordDAO(new RecordDB());
//			for (RecordRecord recordRecord : recordRecords) {
//				if(recordRecord.getCfClassSchemeId().equalsIgnoreCase("publicationJobAd") && (recordRecord.getCfClassId().equalsIgnoreCase("applied to"))){
//					JobAdDTO jobAd = (JobAdDTO)recordDAO.getDTO(recordRecord.getRecordId());
//					if(jobAd != null)
//						retVal.getJobAds().add(jobAd);
//				}
//			}
//		}catch (Exception e) {
//		}

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

		ProductDTO productDTO = (ProductDTO) dto;

		DataField data084 = new DataField("084", AbstractRecordConverter.BLANK,
				AbstractRecordConverter.BLANK);
		StringBuffer tempContent = new StringBuffer();
		Subfield a = new Subfield('a');
		if (productDTO.getInternalNumber() != null)
			tempContent.append(productDTO.getInternalNumber().toString());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data084.addSubfield(a);
		}
		if(data084.getSubfieldCount() > 0)
			rec.addDataField(data084);

		DataField data245 = new DataField("245", '0', '0');
		Subfield sub6 = new Subfield('6');
		if(productDTO.getNameTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data245.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((productDTO.getName().getContent() != null)
				&& (!"".equals(productDTO.getName().getContent())))
			tempContent.append(productDTO.getName().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data245.addSubfield(a);
		}
		if(data245.getSubfieldCount() > 0)
			rec.addDataField(data245);


		for (MultilingualContentDTO nameTranslation : productDTO.getNameTranslations()) {
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

		DataField data260 = new DataField("260", AbstractRecordConverter.BLANK,
				AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(productDTO.getPublisher().getPublisherTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data260.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		Subfield b = new Subfield('b');
		PublisherNameDTO publisherNameDTO = productDTO.getPublisher().getOriginalPublisher();
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
		if (productDTO.getPublicationYear() != null)
			tempContent.append(productDTO.getPublicationYear().toString());
		if (tempContent.toString().length() > 0) {
			c.setContent(tempContent.toString());
			data260.addSubfield(c);
		}
		if(data260.getSubfieldCount() > 0)
			rec.addDataField(data260);

		for (PublisherNameDTO publisherTranslation : productDTO.getPublisher().getPublisherTranslations()) {
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

		if(productDTO.getMainAuthor().getControlNumber() != null){
			AuthorDTO mainAuthor = productDTO.getMainAuthor();
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

		for (AuthorDTO author : productDTO.getOtherAuthors()) {
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

		DataField data520 = new DataField("500", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(productDTO.getDescriptionTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data520.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((productDTO.getDescription().getContent() != null)
				&& (!"".equals(productDTO.getDescription().getContent())))
			tempContent.append(productDTO.getDescription().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data520.addSubfield(a);
		}
		if(data520.getSubfieldCount() > 0)
			rec.addDataField(data520);

		for (MultilingualContentDTO nameTranslation : productDTO.getNameTranslations()) {
			DataField data880 = new DataField("880", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("500-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(nameTranslation.getTransType() + "-" + nameTranslation.getLanguage() + ":" + nameTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}


		DataField data653 = new DataField("653", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(productDTO.getKeywordsTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data653.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((productDTO.getKeywords().getContent() != null)
				&& (!"".equals(productDTO.getKeywords().getContent())))
			tempContent.append(productDTO.getKeywords().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data653.addSubfield(a);
		}
		if(data653.getSubfieldCount() > 0)
			rec.addDataField(data653);

		for (MultilingualContentDTO keywordsTranslation : productDTO.getKeywordsTranslations()) {
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
		if ((productDTO.getUri() != null)
				&& (!"".equals(productDTO.getUri())))
			tempContent.append(productDTO.getUri());
		if (tempContent.toString().length() > 0) {
			u.setContent(tempContent.toString());
			data856.addSubfield(u);
		}
		tempContent = new StringBuffer();
		u = new Subfield('u');
		if ((productDTO.getDoi() != null)
				&& (!"".equals(productDTO.getDoi())))
			tempContent.append("doi:" + productDTO.getDoi().trim());
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
		ProductDTO patent = (ProductDTO) dto;

		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);

		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);

		deleteRelationsOtherRecordsThisRecord(retVal, "authorshipType", "is author of");

		List<RecordRecord> recordRecords = retVal.getRelationsOtherRecordsThisRecord();

		for (AuthorDTO author : patent.getAllAuthors()) {
			recordRecords.add(new RecordResultPublication(author.getControlNumber(), "authorshipType",  "is author of", startDate, endDate, null));
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsThisRecordOtherRecords(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsThisRecordOtherRecords(Record retVal,
			RecordDTO dto) {
		ProductDTO product = (ProductDTO) dto;
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

		for (JobAdDTO jobAd : product.getJobAds()) {
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
		ProductDTO productDTO = (ProductDTO) dto;
		ControlField retVal = new ControlField("008");
		String content = "";
		for(int i=0;i<35;i++)
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		if(productDTO.getName().getLanguage() != null)
			content += productDTO.getName().getLanguage();
		else
			content += MultilingualContentDTO.LANGUAGE_SERBIAN;
		content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		retVal.setContent(content);
		return retVal;
	}

}
