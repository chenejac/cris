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
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
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
 * PaperProceedingsDTO object.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class PaperProceedingsConverter extends ABibliographicRecordConverter {

	
	/**
	 * Convert the mARC21Record with data about Paper which is published in
	 * Proceedings from MARC21 format to DTO format
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
		PaperProceedingsDTO retVal = (PaperProceedingsDTO)rec.getDto();
		retVal.setControlNumber(mARC21Record.getControlNumber());
		retVal.setScopusID(rec.getScopusID());
		
		String originalLanguage = null;//MultilingualContentDTO.LANGUAGE_SERBIAN;
		try{
			ControlField cf008 = mARC21Record.getControlField("008");
			originalLanguage = cf008.getCharPositions(35, 3);
		} catch (Exception e){
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
		
		DataField df300 = mARC21Record.getDataField("300");
		try {
			Subfield a = df300.getSubfield('a');
			String aField = a.getContent();
			if (a.getContent().indexOf('p') != -1) {
				aField = a.getContent().substring(a.getContent().indexOf('p') + 2);
				if (aField.indexOf('-') != -1) {
					String startPage = Sanitizer.nextPar(aField, '-');
					aField = Sanitizer.remPar(aField, '-');
					String endPage = Sanitizer.nextPar(aField, '(');
					aField = Sanitizer.remPar(aField, '(');
					if (startPage.length() != 0)
						retVal.setStartPage(startPage);
					if (endPage.length() != 0)
						retVal.setEndPage(endPage);
				} else {
					retVal.setStartPage(aField);
				}
			}
			if(aField.indexOf(")") != -1){
				aField =  aField.substring(aField.indexOf("(") + 1).trim();
				retVal.setNumberOfPages(Integer.parseInt(aField.substring(0, aField.indexOf(")"))));
			}
		} catch (Exception e) {
		}

		DataField df100 = mARC21Record.getDataField("100");
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
				if(authorDTO.getAllNames().contains(choosedAuthorNameDTO)){
					authorDTO.setName(choosedAuthorNameDTO);
				}
				retVal.setMainAuthor(authorDTO);
			} catch (Exception e) {
				return false;
			}
		} else
			return false;
		
		List<DataField> dfs700 = mARC21Record.getDataFields("700");
		List<AuthorDTO> coAuthorDTOs = new ArrayList<AuthorDTO>();
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
					if(authorDTO.getAllNames().contains(choosedAuthorNameDTO)){
						authorDTO.setName(choosedAuthorNameDTO);
					}
					coAuthorDTOs.add(authorDTO);
				}
			} catch (Exception e) {
			}
		}
		retVal.setOtherAuthors(coAuthorDTOs);
		
		DataField df773 = mARC21Record.getDataField("773");
		try {
			Subfield w = df773.getSubfield('w');
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			ProceedingsDTO proceedingsDTO = (ProceedingsDTO) recordDAO
					.getDTO(w.getContent());
			retVal.setProceedings(proceedingsDTO);
		} catch (Exception e) {
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
		for (Classification classification : recordClasses) {
			if(classification.getCfClassSchemeId().equalsIgnoreCase(Types.TYPE_SCHEMA)){
				String pubType = classification.getCfClassId();
				if(Types.FULL_PAPER_PROCEEDINGS.equals(pubType))
					retVal.setPaperType("records.paperProceedings.editPanel.paperType.full");
				else if(Types.ABSTRACT_PAPER_PROCEEDINGS.equals(pubType))
					retVal.setPaperType("records.paperProceedings.editPanel.paperType.abstract");
				else if(Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS.equals(pubType))
					retVal.setPaperType("records.paperProceedings.editPanel.paperType.invitedTalkFull"); 
				else if(Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS.equals(pubType))
					retVal.setPaperType("records.paperProceedings.editPanel.paperType.invitedTalkAbstract"); 
				else if(Types.DISCUSSION_PAPER_PROCEEDINGS.equals(pubType))
					retVal.setPaperType("records.paperProceedings.editPanel.paperType.discussion");
				break;
			}	
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
		
		PaperProceedingsDTO paperProceedingsDTO = (PaperProceedingsDTO) dto;

		DataField data245 = new DataField("245", '0', '0');
		Subfield sub6 = new Subfield('6');
		if((paperProceedingsDTO.getTitleTranslations().size() > 0) || (paperProceedingsDTO.getSubtitleTranslations().size() > 0)){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data245.addSubfield(sub6);
		}
		StringBuffer tempContent = new StringBuffer();
		Subfield a = new Subfield('a');
		if ((paperProceedingsDTO.getTitle().getContent() != null)
				&& (!"".equals(paperProceedingsDTO.getTitle().getContent())))
			tempContent.append(paperProceedingsDTO.getTitle().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data245.addSubfield(a);
		}
		tempContent = new StringBuffer();
		Subfield b = new Subfield('b');
		if ((paperProceedingsDTO.getSubtitle().getContent() != null)
				&& (!"".equals(paperProceedingsDTO.getSubtitle().getContent())))
			tempContent.append(paperProceedingsDTO.getSubtitle().getContent());
		if (tempContent.toString().length() > 0) {
			b.setContent(tempContent.toString());
			data245.addSubfield(b);
		}
		if(data245.getSubfieldCount() > 0)
			rec.addDataField(data245);

		
		for (MultilingualContentDTO titleTranslation : paperProceedingsDTO.getTitleTranslations()) {
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
		
		for (MultilingualContentDTO subtitleTranslation : paperProceedingsDTO.getSubtitleTranslations()) {
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

		DataField data300 = new DataField("300", AbstractRecordConverter.BLANK,
				AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((paperProceedingsDTO.getStartPage() != null) || (paperProceedingsDTO.getEndPage() != null))
			tempContent.append("p. ");
		if (paperProceedingsDTO.getStartPage() != null)
			tempContent.append(Sanitizer.sanitize(paperProceedingsDTO.getStartPage()));
		if (paperProceedingsDTO.getEndPage() != null)
			tempContent.append("-"
					+ Sanitizer.sanitize(paperProceedingsDTO.getEndPage()));
		if (paperProceedingsDTO.getNumberOfPages() != null)
			tempContent.append("(" + paperProceedingsDTO.getNumberOfPages().toString() + ")");
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data300.addSubfield(a);
		}
		if(data300.getSubfieldCount() > 0)
			rec.addDataField(data300);

		AuthorDTO mainAuthor = paperProceedingsDTO.getMainAuthor();
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

		for (AuthorDTO author : paperProceedingsDTO.getOtherAuthors()) {
			DataField data700 = new DataField("700", '1',
					AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub0 = new Subfield('0');
			sub0.setContent(author.getControlNumber());
			data700.addSubfield(sub0);
			a = new Subfield('a');
			authorName = author.getName();
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
			u = new Subfield('u');
			authorInstitution = author.getInstitution();
			authorOrganizationUnit = author.getOrganizationUnit();
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
		
		ProceedingsDTO proceedingsDTO = paperProceedingsDTO.getProceedings();
		if (proceedingsDTO != null) {
			DataField data773 = new DataField("773", AbstractRecordConverter.BLANK,
					AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			a = new Subfield('a');
			if ((proceedingsDTO.getConference() != null)
					&& (proceedingsDTO.getConference().getSomeName() != null)
					&& (!"".equals(proceedingsDTO.getConference().getSomeName())))
				tempContent.append(proceedingsDTO.getConference().getSomeName());
			if (tempContent.toString().length() > 0) {
				a.setContent(tempContent.toString());
				data773.addSubfield(a);
			}
			tempContent = new StringBuffer();
			Subfield d = new Subfield('d');
			if (proceedingsDTO.getPublicationYear() != null)
				tempContent.append(proceedingsDTO.getPublicationYear());
			if (tempContent.toString().length() > 0) {
				d.setContent(tempContent.toString());
				data773.addSubfield(d);
			}
			tempContent = new StringBuffer();
			Subfield g = new Subfield('g');
			if ((paperProceedingsDTO.getStartPage() != null) || (paperProceedingsDTO.getEndPage() != null))
				tempContent.append("p. ");
			if (paperProceedingsDTO.getStartPage() != null)
				tempContent.append(Sanitizer.sanitize(paperProceedingsDTO.getStartPage()));
			if (paperProceedingsDTO.getEndPage() != null)
				tempContent.append("-"
						+ Sanitizer.sanitize(paperProceedingsDTO.getEndPage()));
			if (paperProceedingsDTO.getNumberOfPages() != null)
				tempContent.append("(" + paperProceedingsDTO.getNumberOfPages().toString() + ")");
			if (tempContent.toString().length() > 0) {
				g.setContent(tempContent.toString());
				data773.addSubfield(g);
			}
			tempContent = new StringBuffer();
			Subfield t = new Subfield('t');
			if (proceedingsDTO.getSomeTitle() != null)
				tempContent.append(proceedingsDTO.getSomeTitle());
			if (tempContent.toString().length() > 0) {
				t.setContent(tempContent.toString());
				data773.addSubfield(t);
			}
			tempContent = new StringBuffer();
			Subfield w = new Subfield('w');
			if ((proceedingsDTO.getControlNumber() != null)
					&& (!"".equals(proceedingsDTO.getControlNumber())))
				tempContent.append(proceedingsDTO.getControlNumber());
			if (tempContent.toString().length() > 0) {
				w.setContent(tempContent.toString());
				data773.addSubfield(w);
			}
			tempContent = new StringBuffer();
			Subfield z = new Subfield('z');
			if (proceedingsDTO.getIsbn() != null)
				tempContent.append(proceedingsDTO.getIsbn().toString());
			if (tempContent.toString().length() > 0) {
				z.setContent(tempContent.toString());
				data773.addSubfield(z);
			}
			if(data773.getSubfieldCount() > 0)
				rec.addDataField(data773);
		}
		
		DataField data500 = new DataField("500", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(paperProceedingsDTO.getNoteTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data500.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((paperProceedingsDTO.getNote().getContent() != null)
				&& (!"".equals(paperProceedingsDTO.getNote().getContent())))
			tempContent.append(paperProceedingsDTO.getNote().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data500.addSubfield(a);
		}
		if(data500.getSubfieldCount() > 0)
			rec.addDataField(data500);
		
		for (MultilingualContentDTO noteTranslation : paperProceedingsDTO.getNoteTranslations()) {
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
		if(paperProceedingsDTO.getAbstractTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data520.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((paperProceedingsDTO.getAbstracT().getContent() != null)
				&& (!"".equals(paperProceedingsDTO.getAbstracT().getContent())))
			tempContent.append(paperProceedingsDTO.getAbstracT().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data520.addSubfield(a);
		}
		if(data520.getSubfieldCount() > 0)
			rec.addDataField(data520);
		
		for (MultilingualContentDTO abstractTranslation : paperProceedingsDTO.getAbstractTranslations()) {
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

		
		DataField data653 = new DataField("653", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		sub6 = new Subfield('6');
		if(paperProceedingsDTO.getKeywordsTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data653.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((paperProceedingsDTO.getKeywords().getContent() != null)
				&& (!"".equals(paperProceedingsDTO.getKeywords().getContent())))
			tempContent.append(paperProceedingsDTO.getKeywords().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data653.addSubfield(a);
		}
		if(data653.getSubfieldCount() > 0)
			rec.addDataField(data653);
		
		for (MultilingualContentDTO keywordsTranslation : paperProceedingsDTO.getKeywordsTranslations()) {
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
		u = new Subfield('u');
		if ((paperProceedingsDTO.getUri() != null)
				&& (!"".equals(paperProceedingsDTO.getUri())))
			tempContent.append(paperProceedingsDTO.getUri());
		if (tempContent.toString().length() > 0) {
			u.setContent(tempContent.toString());
			data856.addSubfield(u);
		}
		tempContent = new StringBuffer();
		u = new Subfield('u');
		if ((paperProceedingsDTO.getDoi() != null)
				&& (!"".equals(paperProceedingsDTO.getDoi())))
			tempContent.append("doi:" + paperProceedingsDTO.getDoi().trim());
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
		
		Classification type = null;
		for (Classification classification : retVal.getRecordClasses()) {
			if(classification.getCfClassSchemeId().equalsIgnoreCase(Types.TYPE_SCHEMA)){
				type = classification;
				break;
			}	
		}
		
		
		String paperType = ((PaperProceedingsDTO)dto).getPaperType();
		String pubType = null;
		if("records.paperProceedings.editPanel.paperType.full".equals(paperType))
			pubType = Types.FULL_PAPER_PROCEEDINGS;
		else if("records.paperProceedings.editPanel.paperType.abstract".equals(paperType))
			pubType = Types.ABSTRACT_PAPER_PROCEEDINGS;
		else if("records.paperProceedings.editPanel.paperType.invitedTalkFull".equals(paperType))
			pubType = Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS;
		else if("records.paperProceedings.editPanel.paperType.invitedTalkAbstract".equals(paperType))
			pubType = Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS;
		else if("records.paperProceedings.editPanel.paperType.discussion".equals(paperType))
			pubType = Types.DISCUSSION_PAPER_PROCEEDINGS;
		else 
			pubType = Types.PAPER_PROCEEDINGS;
		
		if(type!=null){
			retVal.getRecordClasses().remove(type);
		}
		
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		Classification recClass = new Classification(Types.TYPE_SCHEMA, pubType, startDate, endDate, new Integer(0), null);
		retVal.getRecordClasses().add(recClass);
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsOtherRecordsThisRecord(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsOtherRecordsThisRecord(Record retVal,
			RecordDTO dto) {
		PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO) dto;
		
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
		
		for (AuthorDTO author : paperProceedings.getAllAuthors()) {
			recordRecords.add(new RecordResultPublication(author.getControlNumber(), "authorshipType", "is author of", startDate, endDate, null));
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsThisRecordOtherRecords(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsThisRecordOtherRecords(Record retVal,
			RecordDTO dto) {
		PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO) dto;
		
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		deleteRelationsThisRecordOtherRecords(retVal, "publicationsRelation", "is published in");
		deleteRelationsThisRecordOtherRecords(retVal, "publicationJobAd", "applied to");
		
		List<RecordRecord> recordRecords = retVal.getRelationsThisRecordOtherRecords();
		recordRecords.add(new RecordRecord(paperProceedings.getProceedings().getControlNumber(), "publicationsRelation", "is published in", startDate, endDate));
		for (JobAdDTO jobAd : paperProceedings.getJobAds()) {
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
		PaperProceedingsDTO paperProceedingsDTO = (PaperProceedingsDTO) dto;
		ControlField retVal = null;
		String content = "";
		for(int i=0;i<35;i++)
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		if(paperProceedingsDTO.getTitle().getLanguage() != null){
			content += paperProceedingsDTO.getTitle().getLanguage();
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
			retVal =  new ControlField("008");
			retVal.setContent(content);
		}
		return retVal;
	}

}
