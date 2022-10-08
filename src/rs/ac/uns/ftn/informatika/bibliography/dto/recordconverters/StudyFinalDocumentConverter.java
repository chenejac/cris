package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.ResearchAreaDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JobAdDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublisherNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordResultPublication;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.ControlField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Subfield;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * Class for converting bibliographic mARC21Record between MARC21 format and
 * StudyFinalDocumentDTO object.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class StudyFinalDocumentConverter extends ABibliographicRecordConverter {

	
	/**
	 * Convert the mARC21Record with data about study final document from MARC21 format to DTO format
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
		StudyFinalDocumentDTO retVal = (StudyFinalDocumentDTO)rec.getDto();
		retVal.setControlNumber(mARC21Record.getControlNumber());
		retVal.setScopusID(rec.getScopusID());
		
		retVal.setFiles(rec.getFiles());
		retVal.setDeletedFiles(rec.getDeletedFiles());
		String originalLanguage = null; //=MultilingualContentDTO.LANGUAGE_SERBIAN; 
		try{
			ControlField cf008 = mARC21Record.getControlField("008");
			originalLanguage = cf008.getCharPositions(35, 3);
			retVal.setLanguage(originalLanguage);
		} catch (Exception e){
		}
		
		DataField df020 = mARC21Record.getDataField("020");
		try {
			Subfield a = df020.getSubfield('a');
			retVal.setIsbn(a.getContent());
		} catch (Exception e) {
		}
		
		DataField df041 = mARC21Record.getDataField("041");
		String language = null;
		try {
			if (df041.getInd1() == '0'){ 
				retVal.setLanguage(df041.getSubfield('a').getContent());
			}
		} catch (Exception e) {
		}

		DataField df080 = mARC21Record.getDataField("080");
		try {
			Subfield a = df080.getSubfield('a');
			retVal.setUdc(a.getContent());
		} catch (Exception e) {
		}
		
		DataField df100 = mARC21Record.getDataField("100");
		if (df100.getInd1() == '1') {
			try {
				RecordDAO recordDAO = new RecordDAO(new PersonDB());
				AuthorDTO authorDTO = (AuthorDTO) recordDAO
						.getDTO(df100.getSubfield('0').getContent());
				Subfield a = df100.getSubfield('a');
				if(a != null){
					String content = a.getContent().trim();
					String lastName = Sanitizer.nextPar(content, ',');
					content = Sanitizer.remPar(content, ',');
					String firstName = Sanitizer.nextPar(content, ',');
					AuthorNameDTO choosedAuthorNameDTO = new AuthorNameDTO(firstName, lastName, "");
					if(authorDTO.getAllNames().contains(choosedAuthorNameDTO))
						authorDTO.setName(choosedAuthorNameDTO);
					authorDTO.getRecord().setRelationsThisRecordOtherRecords(new ArrayList<RecordRecord>());
					authorDTO.getRecord().setRelationsOtherRecordsThisRecord(new ArrayList<RecordRecord>());
				}
				retVal.setAuthor(authorDTO);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else
			return false;
		
		Subfield sub6 = null;
		String ordNum = null;
		
		try
		{
			DataField df245 = mARC21Record.getDataField("245");
			sub6 = df245.getSubfield('6');
			ordNum = null;
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
					retVal.getSubtitle().setContent(b.getContent());
					retVal.getSubtitle().setLanguage(originalLanguage);
				} catch (Exception e) {
				}
			} else
				return false;
		} catch (Exception e) {
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
							language = content.substring(2, 5);
							String transContent = content.substring(6);
							MultilingualContentDTO titleTranslation = new MultilingualContentDTO(transContent, language, transType);
							titleTranslations.add(titleTranslation);
						}
						if(b != null){
							String content = b.getContent().trim();
							String transType = content.substring(0, 1);
							language = content.substring(2, 5);
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
		
		try{
			DataField df246 = mARC21Record.getDataField("246");
			sub6 = df246.getSubfield('6');
			ordNum = null;
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}
			if ((df246.getInd1() == '0')) {
					Subfield a = df246.getSubfield('a');
					retVal.getAlternativeTitle().setLanguage(originalLanguage);
					retVal.getAlternativeTitle().setContent(a.getContent());
			} 
		} catch (Exception e) {
		}
		
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> alternativeTitleTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("246-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						if(a != null){
							String content = a.getContent().trim();
							String transType = content.substring(0, 1);
							language = content.substring(2, 5);
							String transContent = content.substring(6);
							MultilingualContentDTO alternativeTitleTranslation = new MultilingualContentDTO(transContent, language, transType);
							alternativeTitleTranslations.add(alternativeTitleTranslation);
						}
					}
				} catch (Exception e) {
				}
			}
			retVal.setAlternativeTitleTranslations(alternativeTitleTranslations);
		}
		
		DataField df260 = mARC21Record.getDataField("260");
		try { 
			sub6 = df260.getSubfield('6');
			ordNum = null;
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}
	
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
			String dateString = c.getContent();
			DateFormat formatter ; 
		    Date date ; 
		    formatter = new SimpleDateFormat("dd.MM.yyyy");
		    date = (Date)formatter.parse(dateString); 
		    Calendar cal=GregorianCalendar.getInstance();
		    cal.setTime(date);
			retVal.setPublicationDate(cal);
		} catch (Exception e) {
			try {
				Subfield c = df260.getSubfield('c');
				String yearString = c.getContent();
				Calendar cal = new GregorianCalendar();
				cal.set(Calendar.YEAR, Integer.parseInt(yearString));
				cal.set(Calendar.DAY_OF_YEAR, 1);
				retVal.setPublicationDate(cal);
			} catch (Exception ex) {
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
						language = content.substring(2, 5);
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
			for (int i=0; i < df300.getSubfieldCount(); i = i + 1) {
				Subfield a = df300.getSubfield(i);
				if(a.getName() != 'a')
					continue;
				if((i+1) >= df300.getSubfieldCount())
					retVal.getPhysicalDescription().setNumberOfPages(Integer.parseInt(a.getContent()));
				Subfield f = df300.getSubfield(i+1);
				if ((f.getName() != 'f'))
					continue;
				else 
					i+=1;
				if(f.getContent().equals("chapters"))
					retVal.getPhysicalDescription().setNumberOfChapters(Integer.parseInt(a.getContent()));
				if(f.getContent().equals("pages"))
					retVal.getPhysicalDescription().setNumberOfPages(Integer.parseInt(a.getContent()));
				if(f.getContent().equals("appendixes"))
					retVal.getPhysicalDescription().setNumberOfAppendixes(Integer.parseInt(a.getContent()));
				if(f.getContent().equals("graphs"))
					retVal.getPhysicalDescription().setNumberOfGraphs(Integer.parseInt(a.getContent()));
				if(f.getContent().equals("pictures"))
					retVal.getPhysicalDescription().setNumberOfPictures(Integer.parseInt(a.getContent()));
				if(f.getContent().equals("references"))
					retVal.getPhysicalDescription().setNumberOfReferences(Integer.parseInt(a.getContent()));
				if(f.getContent().equals("tables"))
					retVal.getPhysicalDescription().setNumberOfTables(Integer.parseInt(a.getContent()));
			}
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
						language = content.substring(2, 5);
						String transContent = content.substring(6);
						MultilingualContentDTO noteTranslation = new MultilingualContentDTO(transContent, language, transType);
						noteTranslations.add(noteTranslation);
					}
				} catch (Exception e) {
				}
			}
			retVal.setNoteTranslations(noteTranslations);
		}
		
		List<DataField> dfs502 = mARC21Record.getDataFields("502");
		for (DataField df502 : dfs502) {
			boolean precondition = false;
			try {
				List<Subfield> lg = df502.getSubfields('g');
				for (Subfield g : lg) {
					if(g.getContent().equals("preconditions")){
						precondition = true;
					}
				}
			} catch (Exception e) {
			}
			if(precondition){
				try {
					Subfield a = df502.getSubfield('a');
					if((a != null) && (a.getContent() != null)){
						if(a.getContent().toLowerCase().startsWith("odbranjena magistarska teza")){
							retVal.getRegisterEntry().setPreviouslyNameOfAuthorDegreeAbbreviationOld("мр");
							String temp = a.getContent();
							try{
								temp = a.getContent().replace("Odbranjena magistarska teza", "").trim();
								if(! temp.trim().endsWith("."))
									temp = temp + ".";
								DateFormat formatter ; 
								Date date ; 
								formatter = new SimpleDateFormat("dd.MM.yyyy.");
								date = (Date)formatter.parse(temp); 
								Calendar cal=GregorianCalendar.getInstance();
								cal.setTime(date);
								retVal.getRegisterEntry().setPreviouslyNameOfAuthorDegreeDateOld(cal);
							}catch(Exception e){
								retVal.getRegisterEntry().setPreviouslyNameOfAuthorDegreeOld(a.getContent());
							}
						} else if(a.getContent().toLowerCase().startsWith("магистарске студије")){
							retVal.getRegisterEntry().setPreviouslyNameOfAuthorDegreeAbbreviationOld("мр");
							if(a.getContent().startsWith("Магистарске студије,")){
								String temp = a.getContent().replace("Магистарске студије,", "").trim();
								DateFormat formatter ; 
								Date date ; 
								formatter = new SimpleDateFormat("dd.MM.yyyy");
								date = (Date)formatter.parse(temp); 
								Calendar cal=GregorianCalendar.getInstance();
								cal.setTime(date);
								retVal.getRegisterEntry().setPreviouslyNameOfAuthorDegreeDateOld(cal);
							}
						} else {
							if(a.getContent().contains(",")){
								retVal.getRegisterEntry().setPreviouslyNameOfAuthorDegreeBologna(a.getContent().substring(0, a.getContent().indexOf(",")));
								retVal.getRegisterEntry().setPreviouslyNameOfAuthorDegreeYearBologna(a.getContent().substring(a.getContent().indexOf(",")+1).trim());
							} else {
								retVal.getRegisterEntry().setPreviouslyNameOfAuthorDegreeBologna(a.getContent());
							}
							
						}
					}
				} catch (Exception e) {
				}
				try {
					Subfield c = df502.getSubfield('c');
					if(c.getContent().contains(",")){
						String temp = c.getContent().substring(0, c.getContent().indexOf(","));
						retVal.getRegisterEntry().setPreviouslyGraduated(temp);
						temp = c.getContent().substring(c.getContent().indexOf(",") + 1).trim();
						retVal.getRegisterEntry().setPreviouslyGraduatedPlace(temp);
					} else {
						retVal.getRegisterEntry().setPreviouslyGraduated(c.getContent());
					}
				} catch (Exception e) {
				}
			} else {
				try {
					Subfield a = df502.getSubfield('a');
					retVal.getRegisterEntry().setNameOfAuthorDegree(a.getContent());
				} catch (Exception e) {
				}
				try {
					Subfield b = df502.getSubfield('b');
					retVal.setLevelOfEducation(b.getContent());
				} catch (Exception e) {
				}
				try {
					Subfield c = df502.getSubfield('c');
					retVal.getInstitution().setSomeName(c.getContent());
//					retVal.getInstitution().getName().setContent(c.getContent());
				} catch (Exception e) {
				}
				try {
					List<Subfield> lg = df502.getSubfields('g');
					for (Subfield g : lg) {
						String content = g.getContent();
						String label = Sanitizer.nextPar(content, '-').trim();
						content = Sanitizer.remPar(content, '-');
						String dateString = Sanitizer.nextPar(content, '-');
						DateFormat formatter ; 
					    Date date ; 
					    formatter = new SimpleDateFormat("dd.MM.yyyy");
					    date = (Date)formatter.parse(dateString); 
					    Calendar cal=GregorianCalendar.getInstance();
					    cal.setTime(date);
						if(label.equals("Accepted theme of thesis or dissertation on"))
							retVal.setAcceptedOn(cal);
						else if (label.equals("Defended thesis or dissertation on"))
							retVal.setDefendedOn(cal);
						else if (label.equals("PhD promoted on"))
							retVal.getRegisterEntry().setPromotionDate(cal);
					}
				} catch (Exception e) {
				}
				try {
					List<Subfield> lo = df502.getSubfields('o');
					for (Subfield o : lo) {
						String content = o.getContent();
						String label = Sanitizer.nextPar(content, '-').trim();
						content = Sanitizer.remPar(content, '-');
						String numberString = Sanitizer.nextPar(content, '-');
						if(label.equals("Identification number"))
							retVal.getRegisterEntry().setId(numberString);
						else if (label.equals("Diploma number"))
							retVal.getRegisterEntry().setDiplomaNumber(numberString);
					}
				} catch (Exception e) {
				}
			}
		}
		try {
			ordNum = null;
			List<DataField> dfs520 = mARC21Record.getDataFields("520");
			for (DataField df520 : dfs520) {
				sub6 = df520.getSubfield('6');
				if(sub6 != null){
					ordNum = sub6.getContent().split("\\-")[1];
				}
				if (df520.getInd1() == '3') {
					Subfield a = df520.getSubfield('a');
					if (a!=null){
						retVal.getAbstracT().setLanguage(originalLanguage);
						retVal.getAbstracT().setContent(a.getContent());
					}
					if(ordNum != null){
						List<DataField> dfs880 = mARC21Record.getDataFields("880");
						List<MultilingualContentDTO> abstractTranslations = new ArrayList<MultilingualContentDTO>();
						for (DataField dataField : dfs880) {
							try {
								if (dataField.getSubfield('6').getContent().equals("520-"+ordNum)) {
									a = dataField.getSubfield('a');
									String content = a.getContent().trim();
									String transType = content.substring(0, 1);
									language = content.substring(2, 5);
									String transContent = content.substring(6);
									MultilingualContentDTO abstractTranslation = new MultilingualContentDTO(transContent, language, transType);
									abstractTranslations.add(abstractTranslation);
								}
							} catch (Exception e) {
							}
						}
						retVal.setAbstractTranslations(abstractTranslations);
					}
				} else if (df520.getInd1() == AbstractRecordConverter.BLANK){
					Subfield a = df520.getSubfield('a');
					if(a != null){
						retVal.getExtendedAbstract().setLanguage(originalLanguage);
						retVal.getExtendedAbstract().setContent(a.getContent());
					}
					if(ordNum != null){
						List<DataField> dfs880 = mARC21Record.getDataFields("880");
						List<MultilingualContentDTO> extendedAbstractTranslations = new ArrayList<MultilingualContentDTO>();
						for (DataField dataField : dfs880) {
							try {
								if (dataField.getSubfield('6').getContent().equals("520-"+ordNum)) {
									a = dataField.getSubfield('a');
									String content = a.getContent().trim();
									String transType = content.substring(0, 1);
									language = content.substring(2, 5);
									String transContent = content.substring(6);
									MultilingualContentDTO extendedAbstractTranslation = new MultilingualContentDTO(transContent, language, transType);
									extendedAbstractTranslations.add(extendedAbstractTranslation);
								}
							} catch (Exception e) {
							}
						}
						retVal.setExtendedAbstractTranslations(extendedAbstractTranslations);
					}
				}
			}
		} catch (Exception e) {
		}
		
		try {
			ordNum = null;
			DataField df540 = mARC21Record.getDataField("540");
			sub6 = df540.getSubfield('6');
			if(sub6 != null){
				ordNum = sub6.getContent().split("\\-")[1];
			}
		
			Subfield a = df540.getSubfield('a');
			retVal.getRights().setLanguage(originalLanguage);
			retVal.getRights().setContent(a.getContent());
		} catch (Exception e) {
		}	
		
			
		if(ordNum != null){
			List<DataField> dfs880 = mARC21Record.getDataFields("880");
			List<MultilingualContentDTO> rightsTranslations = new ArrayList<MultilingualContentDTO>();
			for (DataField dataField : dfs880) {
				try {
					if (dataField.getSubfield('6').getContent().equals("540-"+ordNum)) {
						Subfield a = dataField.getSubfield('a');
						String content = a.getContent().trim();
						String transType = content.substring(0, 1);
						language = content.substring(2, 5);
						String transContent = content.substring(6);
						MultilingualContentDTO keywordsTranslation = new MultilingualContentDTO(transContent, language, transType);
						rightsTranslations.add(keywordsTranslation);
					}
				} catch (Exception e) {
				}
			}
			retVal.setRightsTranslations(rightsTranslations);
		}
		
		try {
			DataField df546 = mARC21Record.getDataField("546");
		
			Subfield b = df546.getSubfield('b');
			retVal.setAlphabet(b.getContent());
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
						language = content.substring(2, 5);
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
		List<AuthorDTO> advisorDTOs = new ArrayList<AuthorDTO>();
		List<AuthorDTO> committeeMemberDTOs = new ArrayList<AuthorDTO>();
		
		Map<String, String> positions = new HashMap<String, String>();
		positions.put("doc.", "Docent");
		positions.put("vanr. prof.", "Vаnredni profesor");
		positions.put("red. prof.", "Redovni profesor");
		positions.put("prof. emeritus", "Profesor emeritus");
		positions.put("docent", "Docent");
		positions.put("vanredni profesor", "Vаnredni profesor");
		positions.put("redovni profesor", "Redovni profesor");
		positions.put("profesor emeritus", "Profesor emeritus");
		positions.put("nauč. sar.", "Naučni - saradnik");
		positions.put("viši nauč. sar.", "Viši naučni - saradnik");
		positions.put("nauč. sav.", "Naučni savetnik");
		positions.put("naučni savetnik", "Naučni savetnik");
		positions.put("naučni - saradnik", "Naučni - saradnik");
		positions.put("viši naučni - saradnik", "Viši naučni - saradnik");
		positions.put("prof. u penziji", "Profesor u penziji");
		positions.put("profesor u penziji", "Profesor u penziji");
		
		for (DataField dataField : dfs700) {
			try {
				if ((dataField.getInd1() == '1') && ("ths".equalsIgnoreCase(dataField.getSubfield('4').getContent()))){
					RecordDAO recordDAO = new RecordDAO(new PersonDB());
					AuthorDTO authorDTO = (AuthorDTO) recordDAO
							.getDTO(dataField.getSubfield('0').getContent());
					Subfield a = dataField.getSubfield('a');
					if(a != null){
						String content = a.getContent().trim();
						String lastName = Sanitizer.nextPar(content, ',');
						content = Sanitizer.remPar(content, ',');
						String firstName = Sanitizer.nextPar(content, ',');
						AuthorNameDTO choosedAuthorNameDTO = new AuthorNameDTO(firstName, lastName, "");
						if(authorDTO.getAllNames().contains(choosedAuthorNameDTO))
							authorDTO.setName(choosedAuthorNameDTO);
					}
					Subfield c = dataField.getSubfield('c');
					if(c!=null){
						String content = c.getContent().trim();
						authorDTO.setTitle(Sanitizer.nextPar(content, ','));
						content = Sanitizer.remPar(content, ',');
						authorDTO.getCurrentPosition().setPosition(new PositionDTO());
						authorDTO.getCurrentPosition().getPosition().getTerm().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
						String position = Sanitizer.nextPar(content, ',');
						if(position != null){
							position = LatCyrUtils.toLatin(position).toLowerCase();
							if(positions.containsKey(position))
								position = positions.get(position);
							authorDTO.getCurrentPosition().getPosition().getTerm().setContent(position);
						}
					}
					Subfield g = dataField.getSubfield('g');
					if(g!=null){
						String content = g.getContent().trim();
						authorDTO.getInstitution().setSomeName(content);
//						authorDTO.getInstitution().getName().setContent(content);
					}
					authorDTO.getInstitution().setNotLoaded(true);
					authorDTO.getRecord().setRelationsThisRecordOtherRecords(new ArrayList<RecordRecord>());
					authorDTO.getRecord().setRelationsOtherRecordsThisRecord(new ArrayList<RecordRecord>());
					advisorDTOs.add(authorDTO);
				}
				if ((dataField.getInd1() == '1') && ("exp".equalsIgnoreCase(dataField.getSubfield('4').getContent()))){
					RecordDAO recordDAO = new RecordDAO(new PersonDB());
					AuthorDTO authorDTO = (AuthorDTO) recordDAO
							.getDTO(dataField.getSubfield('0').getContent());
					Subfield a = dataField.getSubfield('a');
					if(a != null){
						String content = a.getContent().trim();
						String lastName = Sanitizer.nextPar(content, ',');
						content = Sanitizer.remPar(content, ',');
						String firstName = Sanitizer.nextPar(content, ',');
						AuthorNameDTO choosedAuthorNameDTO = new AuthorNameDTO(firstName, lastName, "");
						if(authorDTO.getAllNames().contains(choosedAuthorNameDTO))
							authorDTO.setName(choosedAuthorNameDTO);
					}
					Subfield c = dataField.getSubfield('c');
					if(c!=null){
						String content = c.getContent().trim();
						authorDTO.setTitle(Sanitizer.nextPar(content, ','));
						content = Sanitizer.remPar(content, ',');
						authorDTO.getCurrentPosition().setPosition(new PositionDTO());
						authorDTO.getCurrentPosition().getPosition().getTerm().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
						String position = Sanitizer.nextPar(content, ',');
						if(position != null){
							position = LatCyrUtils.toLatin(position).toLowerCase();
							if(positions.containsKey(position))
								position = positions.get(position);
							authorDTO.getCurrentPosition().getPosition().getTerm().setContent(position);
						}
					}
					Subfield g = dataField.getSubfield('g');
					if(g!=null){
						String content = g.getContent().trim();
						authorDTO.getInstitution().setSomeName(content);
//						authorDTO.getInstitution().getName().setContent(content);
					}
					authorDTO.getInstitution().setNotLoaded(true);
					authorDTO.getRecord().setRelationsThisRecordOtherRecords(new ArrayList<RecordRecord>());
					authorDTO.getRecord().setRelationsOtherRecordsThisRecord(new ArrayList<RecordRecord>());
					committeeMemberDTOs.add(authorDTO);
				}
			} catch (Exception e) {
				log.error("Nema mentora ili clana komisije: " + retVal.getControlNumber());
			}
		}
		retVal.setAdvisors(advisorDTOs);
		retVal.setCommitteeMembers(committeeMemberDTOs);
		
		DataField df710 = mARC21Record.getDataField("710");
		if ((df710 != null) && (df710.getInd1() == '2')) {
			try {
				if(df710.getSubfield('0')!=null){
					RecordDAO recordDAO = new RecordDAO(new RecordDB());
					InstitutionDTO institutionDTO = (InstitutionDTO) recordDAO
						.getDTO(df710.getSubfield('0').getContent());
					if((institutionDTO!=null)){
						retVal.setInstitution(institutionDTO);
					}
				} else {
					retVal.getInstitution().setSomeName(df710.getSubfield('a').getContent());
//					retVal.getInstitution().getName().setContent(df710.getSubfield('a').getContent());
				}
			} catch (Exception e) {
			}
		}
		
		DataField df852 = mARC21Record.getDataField("852");
		try {
			Subfield a = df852.getSubfield('a');
			retVal.setHoldingData(a.getContent());
		} catch (Exception e) {
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
			Subfield q = df856.getSubfield('q');
			retVal.setContentFormat(q.getContent());
		} catch (Exception e) {
		}
		
		List<Classification> recordClasses = rec.getRecordClasses();
		ResearchAreaDAO researchAreaDAO = new ResearchAreaDAO();
		for (Classification classification : recordClasses) {
			if(classification.getCfClassSchemeId().equalsIgnoreCase(Types.TYPE_SCHEMA)){
				String pubType = classification.getCfClassId();
				if(Types.PHD_STUDY_FINAL_DOCUMENT.equals(pubType))
					retVal.setStudyType("records.studyFinalDocument.editPanel.studyType.phd");
				else if(Types.PHD_ART_PROJECT.equals(pubType))
					retVal.setStudyType("records.studyFinalDocument.editPanel.studyType.phdArt");
				else if(Types.OLD_MASTER_STUDY_FINAL_DOCUMENT.equals(pubType))
					retVal.setStudyType("records.studyFinalDocument.editPanel.studyType.oldMaster");
				else if(Types.MASTER_STUDY_FINAL_DOCUMENT.equals(pubType))
					retVal.setStudyType("records.studyFinalDocument.editPanel.studyType.master");
				else if(Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT.equals(pubType))
					retVal.setStudyType("records.studyFinalDocument.editPanel.studyType.oldBachelor");
				else if(Types.BACHELOR_STUDY_FINAL_DOCUMENT.equals(pubType))
					retVal.setStudyType("records.studyFinalDocument.editPanel.studyType.bachelor");
				else if(Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT.equals(pubType))
					retVal.setStudyType("records.studyFinalDocument.editPanel.studyType.specialistic");
			}	else if (classification.getCfClassSchemeId().equalsIgnoreCase(ResearchAreaDTO.RESEARCH_AREA_SCHEMA)){
						retVal.setResearchArea(researchAreaDAO.getResearchArea(classification.getCfClassId()));
					}
		}	
		
		if(retVal.getResearchArea().getClassId() == null){
			DataField df650 = mARC21Record.getDataField("650");
			try {
				Subfield a = df650.getSubfield('a');
				retVal.getResearchArea().getTerm().setContent(a.getContent());
			} catch (Exception e) {
			}
		}

		try{
			List<RecordRecord> recordRecords = rec.getRelationsThisRecordOtherRecords();
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			for (RecordRecord recordRecord : recordRecords) {
				if(recordRecord.getCfClassSchemeId().equalsIgnoreCase("publicationInstitutionRelation") && (recordRecord.getCfClassId().equalsIgnoreCase("is defended at"))){
					retVal.setInstitution((InstitutionDTO)recordDAO.getDTO(recordRecord.getRecordId()));
				} else if(recordRecord.getCfClassSchemeId().equalsIgnoreCase("publicationJobAd") && (recordRecord.getCfClassId().equalsIgnoreCase("applied to"))){
					JobAdDTO jobAd = (JobAdDTO)recordDAO.getDTO(recordRecord.getRecordId());
					if(jobAd != null)
						retVal.getJobAds().add(jobAd);
				}
			}	
		}catch (Exception e) {
		}
		
		try{
			List<RecordRecord> recordRecords = rec.getRelationsOtherRecordsThisRecord();
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			for (RecordRecord recordRecord : recordRecords) {
				if(recordRecord.getCfClassSchemeId().equalsIgnoreCase("publicationsRelation") && (recordRecord.getCfClassId().equalsIgnoreCase("is result of"))){
					PublicationDTO publicationDTO = (PublicationDTO)recordDAO.getDTO(recordRecord.getRecordId());
					if(publicationDTO != null){
						retVal.getRelatedPublications().add(publicationDTO);
					}
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
		
		StudyFinalDocumentDTO studyFinalDocumentDTO = (StudyFinalDocumentDTO) dto;
		if(studyFinalDocumentDTO.getDefendedOn() != null){
			if(studyFinalDocumentDTO.getReport() != null){
				studyFinalDocumentDTO.getReport().setDateModified(studyFinalDocumentDTO.getPublicationDate());
				studyFinalDocumentDTO.getReport().setLicense(null);
				studyFinalDocumentDTO.getReport().setNote("Public period finished!");
			}
			if(studyFinalDocumentDTO.getPreliminaryTheses() != null){
				studyFinalDocumentDTO.getPreliminaryTheses().setDateModified(studyFinalDocumentDTO.getPublicationDate());
				studyFinalDocumentDTO.getPreliminaryTheses().setLicense(null);
				studyFinalDocumentDTO.getPreliminaryTheses().setNote("Public period finished!");
			}
			if(studyFinalDocumentDTO.getPreliminarySupplement() != null){
				studyFinalDocumentDTO.getPreliminarySupplement().setDateModified(studyFinalDocumentDTO.getPublicationDate());
				studyFinalDocumentDTO.getPreliminarySupplement().setLicense(null);
				studyFinalDocumentDTO.getPreliminarySupplement().setNote("Public period finished!");
			}
		}
		
		DataField data020 = new DataField("020", AbstractRecordConverter.BLANK,
				AbstractRecordConverter.BLANK);
		StringBuffer tempContent = new StringBuffer();
		Subfield a = new Subfield('a');
		if (studyFinalDocumentDTO.getIsbn() != null)
			tempContent.append(studyFinalDocumentDTO.getIsbn().toString());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data020.addSubfield(a);
		}
		if(data020.getSubfieldCount() > 0)
			rec.addDataField(data020);
		
		DataField data041 = new DataField("041", '0', AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((studyFinalDocumentDTO.getLanguage() != null) && (!"".equals(studyFinalDocumentDTO.getLanguage())))
			tempContent.append(studyFinalDocumentDTO.getLanguage());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data041.addSubfield(a);
		}
		if(data041.getSubfieldCount() > 0)
				rec.addDataField(data041); 
		
		
		DataField data080 = new DataField("080", AbstractRecordConverter.BLANK,
				AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if (studyFinalDocumentDTO.getUdc() != null)
			tempContent.append(studyFinalDocumentDTO.getUdc().toString());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data080.addSubfield(a);
		}
		if(data080.getSubfieldCount() > 0)
			rec.addDataField(data080);
		
		AuthorDTO author = studyFinalDocumentDTO.getAuthor();
		DataField data100 = new DataField("100", '1', AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		Subfield sub0 = new Subfield('0');
		sub0.setContent(author.getControlNumber());
		data100.addSubfield(sub0);
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
			data100.addSubfield(a);
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
			data100.addSubfield(u);
		}
		if(data100.getSubfieldCount() > 0)
			rec.addDataField(data100);
		
		DataField data245 = new DataField("245", '0', '0');
		Subfield sub6 = new Subfield('6');
		if((studyFinalDocumentDTO.getTitleTranslations().size() > 0) || (studyFinalDocumentDTO.getSubtitleTranslations().size() > 0)){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data245.addSubfield(sub6);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((studyFinalDocumentDTO.getTitle().getContent() != null)
				&& (!"".equals(studyFinalDocumentDTO.getTitle().getContent()))){
			if (studyFinalDocumentDTO.getTitle().getContent().endsWith(", Magistarska teza")) 
				tempContent.append(studyFinalDocumentDTO.getTitle().getContent().substring(0, studyFinalDocumentDTO.getTitle().getContent().indexOf(", Magistarska teza")));
			else if (studyFinalDocumentDTO.getTitle().getContent().endsWith(", Doktorska disertacija")) 
				tempContent.append(studyFinalDocumentDTO.getTitle().getContent().substring(0, studyFinalDocumentDTO.getTitle().getContent().indexOf(", Doktorska disertacija")));
			else
				tempContent.append(studyFinalDocumentDTO.getTitle().getContent());
		}
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data245.addSubfield(a);
		}
		tempContent = new StringBuffer();
		Subfield b = new Subfield('b');
		if ((studyFinalDocumentDTO.getSubtitle().getContent() != null)
				&& (!"".equals(studyFinalDocumentDTO.getSubtitle().getContent())))
			tempContent.append(studyFinalDocumentDTO.getSubtitle().getContent());
		if (tempContent.toString().length() > 0) {
			b.setContent(tempContent.toString());
			data245.addSubfield(b);
		}
		if(data245.getSubfieldCount() > 0)
			rec.addDataField(data245);

		
		for (MultilingualContentDTO titleTranslation : studyFinalDocumentDTO.getTitleTranslations()) {
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
		
		for (MultilingualContentDTO subtitleTranslation : studyFinalDocumentDTO.getSubtitleTranslations()) {
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
		
		DataField data246 = new DataField("246", '0', AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((studyFinalDocumentDTO.getAlternativeTitle().getContent() != null)
				&& (!"".equals(studyFinalDocumentDTO.getAlternativeTitle().getContent())))
			tempContent.append(studyFinalDocumentDTO.getAlternativeTitle().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data246.addSubfield(a);
		}
		sub6 = new Subfield('6');
		if(studyFinalDocumentDTO.getAlternativeTitleTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data246.addSubfield(sub6);
		}
		if(data246.getSubfieldCount() > 0)
			rec.addDataField(data246);

		
		for (MultilingualContentDTO alternativeTitleTranslation : studyFinalDocumentDTO.getAlternativeTitleTranslations()) {
			DataField data880 = new DataField("880", '0', AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("246-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(alternativeTitleTranslation.getTransType() + "-" + alternativeTitleTranslation.getLanguage() + ":" + alternativeTitleTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		DataField data260 = new DataField("260", AbstractRecordConverter.BLANK,
				AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		b = new Subfield('b');
		PublisherNameDTO publisherNameDTO = studyFinalDocumentDTO.getPublisher().getOriginalPublisher();
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
		if (studyFinalDocumentDTO.getPublicationDate() != null){
			try{
				String pubdate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				pubdate = sdf.format(studyFinalDocumentDTO.getPublicationDate().getTime());
				tempContent.append(pubdate);
			} catch (Exception e) {
			}
		}
		if (tempContent.toString().length() > 0) {
			c.setContent(tempContent.toString());
			data260.addSubfield(c);
		}
		sub6 = new Subfield('6');
		if(studyFinalDocumentDTO.getPublisher().getPublisherTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data260.addSubfield(sub6);
		}	
		if(data260.getSubfieldCount() > 0)
			rec.addDataField(data260);
		
		for (PublisherNameDTO publisherTranslation : studyFinalDocumentDTO.getPublisher().getPublisherTranslations()) {
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
		
		DataField data300 = new DataField("300", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		Subfield f = new Subfield('f');
		if ((studyFinalDocumentDTO.getPhysicalDescription().getNumberOfChapters() != null))
			tempContent.append(studyFinalDocumentDTO.getPhysicalDescription().getNumberOfChapters());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data300.addSubfield(a);
			f.setContent("chapters");
			data300.addSubfield(f);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		f = new Subfield('f');
		if ((studyFinalDocumentDTO.getPhysicalDescription().getNumberOfPages() != null))
			tempContent.append(studyFinalDocumentDTO.getPhysicalDescription().getNumberOfPages());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data300.addSubfield(a);
			f.setContent("pages");
			data300.addSubfield(f);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		f = new Subfield('f');
		if ((studyFinalDocumentDTO.getPhysicalDescription().getNumberOfAppendixes() != null))
			tempContent.append(studyFinalDocumentDTO.getPhysicalDescription().getNumberOfAppendixes());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data300.addSubfield(a);
			f.setContent("appendixes");
			data300.addSubfield(f);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		f = new Subfield('f');
		if ((studyFinalDocumentDTO.getPhysicalDescription().getNumberOfGraphs() != null))
			tempContent.append(studyFinalDocumentDTO.getPhysicalDescription().getNumberOfGraphs());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data300.addSubfield(a);
			f.setContent("graphs");
			data300.addSubfield(f);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		f = new Subfield('f');
		if ((studyFinalDocumentDTO.getPhysicalDescription().getNumberOfPictures() != null))
			tempContent.append(studyFinalDocumentDTO.getPhysicalDescription().getNumberOfPictures());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data300.addSubfield(a);
			f.setContent("pictures");
			data300.addSubfield(f);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		f = new Subfield('f');
		if ((studyFinalDocumentDTO.getPhysicalDescription().getNumberOfReferences() != null))
			tempContent.append(studyFinalDocumentDTO.getPhysicalDescription().getNumberOfReferences());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data300.addSubfield(a);
			f.setContent("references");
			data300.addSubfield(f);
		}
		tempContent = new StringBuffer();
		a = new Subfield('a');
		f = new Subfield('f');
		if ((studyFinalDocumentDTO.getPhysicalDescription().getNumberOfTables() != null))
			tempContent.append(studyFinalDocumentDTO.getPhysicalDescription().getNumberOfTables());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data300.addSubfield(a);
			f.setContent("tables");
			data300.addSubfield(f);
		}
		if(data300.getSubfieldCount() > 0)
			rec.addDataField(data300);
	
		DataField data500 = new DataField("500", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((studyFinalDocumentDTO.getNote().getContent() != null)
				&& (!"".equals(studyFinalDocumentDTO.getNote().getContent())))
			tempContent.append(studyFinalDocumentDTO.getNote().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data500.addSubfield(a);
		}
		sub6 = new Subfield('6');
		if(studyFinalDocumentDTO.getNoteTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data500.addSubfield(sub6);
		}
		if(data500.getSubfieldCount() > 0)
			rec.addDataField(data500);
		
		for (MultilingualContentDTO noteTranslation : studyFinalDocumentDTO.getNoteTranslations()) {
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
		
		DataField data502 = new DataField("502", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((studyFinalDocumentDTO.getRegisterEntry().getNameOfAuthorDegree() != null)
				&& (!"".equals(studyFinalDocumentDTO.getRegisterEntry().getNameOfAuthorDegree())))
			tempContent.append(studyFinalDocumentDTO.getRegisterEntry().getNameOfAuthorDegree());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data502.addSubfield(a);
		}
		tempContent = new StringBuffer();
		b = new Subfield('b');
		if ((studyFinalDocumentDTO.getLevelOfEducation() != null)
				&& (!"".equals(studyFinalDocumentDTO.getLevelOfEducation())))
			tempContent.append(studyFinalDocumentDTO.getLevelOfEducation());
		if (tempContent.toString().length() > 0) {
			b.setContent(tempContent.toString());
			data502.addSubfield(b);
		}
		tempContent = new StringBuffer();
		c = new Subfield('c');
		if ((studyFinalDocumentDTO.getInstitution() != null)
				&& (!"".equals(studyFinalDocumentDTO.getInstitution().toString())))
			tempContent.append(studyFinalDocumentDTO.getInstitution().toString());
		if (tempContent.toString().length() > 0) {
			c.setContent(tempContent.toString());
			data502.addSubfield(c);
		}
		tempContent = new StringBuffer();
		Subfield g = new Subfield('g');
		if ((studyFinalDocumentDTO.getAcceptedOn() != null)){
			try{
				String strdate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				strdate = sdf.format(studyFinalDocumentDTO.getAcceptedOn().getTime());
				tempContent.append("Accepted theme of thesis or dissertation on - " + strdate);
			} catch (Exception e) {
			}
		}
		if (tempContent.toString().length() > 0) {
			g.setContent(tempContent.toString());
			data502.addSubfield(g);
		}
		tempContent = new StringBuffer();
		g = new Subfield('g');
		if ((studyFinalDocumentDTO.getDefendedOn() != null)){
			try{
				String strdate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				strdate = sdf.format(studyFinalDocumentDTO.getDefendedOn().getTime());
				tempContent.append("Defended thesis or dissertation on - " + strdate);
			} catch (Exception e) {
			}
		}
		if (tempContent.toString().length() > 0) {
			g.setContent(tempContent.toString());
			data502.addSubfield(g);
		}
		tempContent = new StringBuffer();
		g = new Subfield('g');
		if ((studyFinalDocumentDTO.getRegisterEntry().getPromotionDate() != null)){
			try{
				String strdate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				strdate = sdf.format(studyFinalDocumentDTO.getRegisterEntry().getPromotionDate().getTime());
				tempContent.append("PhD promoted on - " + strdate);
			} catch (Exception e) {
			}
		}
		if (tempContent.toString().length() > 0) {
			g.setContent(tempContent.toString());
			data502.addSubfield(g);
		}
		tempContent = new StringBuffer();
		Subfield o = new Subfield('o');
		if ((studyFinalDocumentDTO.getRegisterEntry().getId() != null)
				&& (!"".equals(studyFinalDocumentDTO.getRegisterEntry().getId())))
			tempContent.append("Identification number - " + studyFinalDocumentDTO.getRegisterEntry().getId());
		if (tempContent.toString().length() > 0) {
			o.setContent(tempContent.toString());
			data502.addSubfield(o);
		}
		tempContent = new StringBuffer();
		o = new Subfield('o');
		if ((studyFinalDocumentDTO.getRegisterEntry().getDiplomaNumber() != null)
				&& (!"".equals(studyFinalDocumentDTO.getRegisterEntry().getDiplomaNumber())))
			tempContent.append("Diploma number - " + studyFinalDocumentDTO.getRegisterEntry().getDiplomaNumber());
		if (tempContent.toString().length() > 0) {
			o.setContent(tempContent.toString());
			data502.addSubfield(o);
		}
		if(data502.getSubfieldCount() > 0)
			rec.addDataField(data502);
		
		data502 = new DataField("502", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((studyFinalDocumentDTO.getRegisterEntry().getPreviouslyNameOfAuthorDegreeOld() != null)
				&& (!"".equals(studyFinalDocumentDTO.getRegisterEntry().getPreviouslyNameOfAuthorDegreeOld()))){
			tempContent.append(studyFinalDocumentDTO.getRegisterEntry().getPreviouslyNameOfAuthorDegreeOld());
			if(studyFinalDocumentDTO.getRegisterEntry().getPreviouslyNameOfAuthorDegreeDateOld() != null){
				try{
					String strdate = null;
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
					strdate = sdf.format(studyFinalDocumentDTO.getRegisterEntry().getPreviouslyNameOfAuthorDegreeDateOld().getTime());
					tempContent.append(", " + strdate);
				} catch (Exception e) {
				}
			}
		}
		else if ((studyFinalDocumentDTO.getRegisterEntry().getPreviouslyNameOfAuthorDegreeBologna() != null)
				&& (!"".equals(studyFinalDocumentDTO.getRegisterEntry().getPreviouslyNameOfAuthorDegreeBologna()))){
				tempContent.append(studyFinalDocumentDTO.getRegisterEntry().getPreviouslyNameOfAuthorDegreeBologna());
			if(((studyFinalDocumentDTO.getRegisterEntry().getPreviouslyNameOfAuthorDegreeYearBologna() != null)
				&& (!"".equals(studyFinalDocumentDTO.getRegisterEntry().getPreviouslyNameOfAuthorDegreeYearBologna())))){
				tempContent.append(studyFinalDocumentDTO.getRegisterEntry().getPreviouslyNameOfAuthorDegreeYearBologna());
			}
		}
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data502.addSubfield(a);
		}
		tempContent = new StringBuffer();
		c = new Subfield('c');
		if ((studyFinalDocumentDTO.getRegisterEntry().getPreviouslyGraduated() != null)
				&& (!"".equals(studyFinalDocumentDTO.getRegisterEntry().getPreviouslyGraduated())))
			tempContent.append(studyFinalDocumentDTO.getRegisterEntry().getPreviouslyGraduated());
		if (tempContent.toString().length() > 0) {
			c.setContent(tempContent.toString());
			data502.addSubfield(c);
		}
		if(data502.getSubfieldCount() > 0){
			g = new Subfield('g');
			g.setContent("preconditions");
			data502.addSubfield(g);
			rec.addDataField(data502);
		}
		
		DataField data520 = new DataField("520", '3', AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((studyFinalDocumentDTO.getAbstracT().getContent() != null)
				&& (!"".equals(studyFinalDocumentDTO.getAbstracT().getContent())))
			tempContent.append(studyFinalDocumentDTO.getAbstracT().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data520.addSubfield(a);
		}
		sub6 = new Subfield('6');
		if(studyFinalDocumentDTO.getAbstractTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data520.addSubfield(sub6);
		}
		if(data520.getSubfieldCount() > 0)
			rec.addDataField(data520);
		
		for (MultilingualContentDTO abstractTranslation : studyFinalDocumentDTO.getAbstractTranslations()) {
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
		
		data520 = new DataField("520", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((studyFinalDocumentDTO.getExtendedAbstract().getContent() != null)
				&& (!"".equals(studyFinalDocumentDTO.getExtendedAbstract().getContent())))
			tempContent.append(studyFinalDocumentDTO.getExtendedAbstract().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data520.addSubfield(a);
		}
		sub6 = new Subfield('6');
		if(studyFinalDocumentDTO.getExtendedAbstractTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data520.addSubfield(sub6);
		}
		if(data520.getSubfieldCount() > 0)
			rec.addDataField(data520);
		
		for (MultilingualContentDTO extendedAbstractTranslation : studyFinalDocumentDTO.getExtendedAbstractTranslations()) {
			DataField data880 = new DataField("880", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("520-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(extendedAbstractTranslation.getTransType() + "-" + extendedAbstractTranslation.getLanguage() + ":" + extendedAbstractTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		DataField data540 = new DataField("540", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((studyFinalDocumentDTO.getRights().getContent() != null)
				&& (!"".equals(studyFinalDocumentDTO.getRights().getContent())))
			tempContent.append(studyFinalDocumentDTO.getRights().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data540.addSubfield(a);
		}
		sub6 = new Subfield('6');
		if(studyFinalDocumentDTO.getExtendedAbstractTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data540.addSubfield(sub6);
		}
		if(data540.getSubfieldCount() > 0)
			rec.addDataField(data540);
		
		for (MultilingualContentDTO rightsTranslation : studyFinalDocumentDTO.getRightsTranslations()) {
			DataField data880 = new DataField("880", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub6 = new Subfield('6');
			sub6.setContent("540-0" + ordNum);
			data880.addSubfield(sub6);
			a = new Subfield('a');
			a.setContent(rightsTranslation.getTransType() + "-" + rightsTranslation.getLanguage() + ":" + rightsTranslation.getContent());
			data880.addSubfield(a);
			rec.addDataField(data880);
		}
		
		DataField data546 = new DataField("546", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		b = new Subfield('b');
		if ((studyFinalDocumentDTO.getAlphabet() != null)
				&& (!"".equals(studyFinalDocumentDTO.getAlphabet())))
			tempContent.append(studyFinalDocumentDTO.getAlphabet());
		if (tempContent.toString().length() > 0) {
			b.setContent(tempContent.toString());
			data546.addSubfield(b);
		}
		if(data546.getSubfieldCount() > 0)
			rec.addDataField(data546);
		
		DataField data650 = new DataField("650", AbstractRecordConverter.BLANK, '4');
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((studyFinalDocumentDTO.getResearchArea().getFullHierarchy() != null)
				&& (!"".equals(studyFinalDocumentDTO.getResearchArea().getFullHierarchy())))
			tempContent.append(studyFinalDocumentDTO.getResearchArea().getFullHierarchy());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data650.addSubfield(a);
		}
		if(data650.getSubfieldCount() > 0)
			rec.addDataField(data650);

		
		DataField data653 = new DataField("653", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((studyFinalDocumentDTO.getKeywords().getContent() != null)
				&& (!"".equals(studyFinalDocumentDTO.getKeywords().getContent())))
			tempContent.append(studyFinalDocumentDTO.getKeywords().getContent());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data653.addSubfield(a);
		}
		sub6 = new Subfield('6');
		if(studyFinalDocumentDTO.getKeywordsTranslations().size() > 0){
			ordNum++;
			sub6.setContent("880-0" + ordNum);
			data653.addSubfield(sub6);
		}
		if(data653.getSubfieldCount() > 0)
			rec.addDataField(data653);
		
		for (MultilingualContentDTO keywordsTranslation : studyFinalDocumentDTO.getKeywordsTranslations()) {
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
		
		for (AuthorDTO advisor : studyFinalDocumentDTO.getAdvisors()) {
			DataField data700 = new DataField("700", '1',
					AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub0 = new Subfield('0');
			sub0.setContent(advisor.getControlNumber());
			data700.addSubfield(sub0);
			Subfield sub4 = new Subfield('4');
			sub4.setContent("ths");
			data700.addSubfield(sub4);
			a = new Subfield('a');
			AuthorNameDTO advisorName = advisor.getName();
			if ((advisorName.getLastname() != null)
					&& (!"".equals(advisorName.getLastname())))
				tempContent.append(Sanitizer.sanitize(advisorName.getLastname()));
			if ((advisorName.getFirstname() != null)
					&& (!"".equals(advisorName.getFirstname())))
				tempContent.append(", " + Sanitizer.sanitize(advisorName.getFirstname()));
			if (tempContent.toString().length() > 0) {
				a.setContent(tempContent.toString());
				data700.addSubfield(a);
			}
			tempContent = new StringBuffer();
			c = new Subfield('c');
			if ((advisor.getTitle() != null)
					&& (!"".equals(advisor.getTitle())))
				tempContent.append(Sanitizer.sanitize(advisor.getTitle()));
			if ((advisor.getCurrentPosition().getPosition().getTerm().getContent() != null)
					&& (!"".equals(advisor.getCurrentPosition().getPosition().getTerm().getContent())))
				tempContent.append(", " + Sanitizer.sanitize(advisor.getCurrentPosition().getPosition().getTerm().getContent()));
			if (tempContent.toString().length() > 0) {
				c.setContent(tempContent.toString());
				data700.addSubfield(c);
			}
			tempContent = new StringBuffer();
			g = new Subfield('g');
			if ((advisor.getInstitution().getSomeName() != null)
					&& (!"".equals(advisor.getInstitution().getSomeName())))
				tempContent.append(advisor.getInstitution().getSomeName().trim());
			if (tempContent.toString().length() > 0) {
				g.setContent(tempContent.toString());
				data700.addSubfield(g);
			}
			if(data700.getSubfieldCount() > 0)
				rec.addDataField(data700);
		}
		
		for (AuthorDTO committeeMember : studyFinalDocumentDTO.getCommitteeMembers()) {
			DataField data700 = new DataField("700", '1',
					AbstractRecordConverter.BLANK);
			tempContent = new StringBuffer();
			sub0 = new Subfield('0');
			sub0.setContent(committeeMember.getControlNumber());
			data700.addSubfield(sub0);
			Subfield sub4 = new Subfield('4');
			sub4.setContent("exp");
			data700.addSubfield(sub4);
			a = new Subfield('a');
			AuthorNameDTO committeeMemberName = committeeMember.getName();
			if ((committeeMemberName.getLastname() != null)
					&& (!"".equals(committeeMemberName.getLastname())))
				tempContent.append(Sanitizer.sanitize(committeeMemberName.getLastname()));
			if ((committeeMemberName.getFirstname() != null)
					&& (!"".equals(committeeMemberName.getFirstname())))
				tempContent.append(", " + Sanitizer.sanitize(committeeMemberName.getFirstname()));
			if (tempContent.toString().length() > 0) {
				a.setContent(tempContent.toString());
				data700.addSubfield(a);
			}
			tempContent = new StringBuffer();
			c = new Subfield('c');
			if ((committeeMember.getTitle() != null)
					&& (!"".equals(committeeMember.getTitle())))
				tempContent.append(Sanitizer.sanitize(committeeMember.getTitle()));
			if ((committeeMember.getCurrentPosition().getPosition().getTerm().getContent() != null)
					&& (!"".equals(committeeMember.getCurrentPosition().getPosition().getTerm().getContent())))
				tempContent.append(", " + Sanitizer.sanitize(committeeMember.getCurrentPosition().getPosition().getTerm().getContent()));
			if (tempContent.toString().length() > 0) {
				c.setContent(tempContent.toString());
				data700.addSubfield(c);
			}
			tempContent = new StringBuffer();
			g = new Subfield('g');
			if ((committeeMember.getInstitution().getSomeName() != null)
					&& (!"".equals(committeeMember.getInstitution().getSomeName())))
				tempContent.append(committeeMember.getInstitution().getSomeName().trim());
			if (tempContent.toString().length() > 0) {
				g.setContent(tempContent.toString());
				data700.addSubfield(g);
			}
			if(data700.getSubfieldCount() > 0)
				rec.addDataField(data700);
		}
		
		DataField data710 = new DataField("710", '2', AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		InstitutionDTO institutionDTO = studyFinalDocumentDTO.getInstitution();
		if((institutionDTO!=null) && (institutionDTO.getControlNumber()!=null)){
			sub0 = new Subfield('0');
			if ((institutionDTO.getControlNumber() != null)
					&& (!"".equals(institutionDTO.getControlNumber())))
				tempContent.append(institutionDTO.getControlNumber());
			if (tempContent.toString().length() > 0) {
				sub0.setContent(tempContent.toString());
				data710.addSubfield(sub0);
			}
			tempContent = new StringBuffer();
			a = new Subfield('a');
			if ((institutionDTO.getSomeName() != null)
					&& (!"".equals(institutionDTO.getSomeName())))
				tempContent.append(institutionDTO.getSomeName());
			if (tempContent.toString().length() > 0) {
				a.setContent(tempContent.toString());
				data710.addSubfield(a);
			}
			tempContent = new StringBuffer();
			c = new Subfield('c');
			if ((institutionDTO.getPlace() != null)
					&& (!"".equals(institutionDTO.getPlace())))
				tempContent.append(institutionDTO.getPlace());
			if (tempContent.toString().length() > 0) {
				c.setContent(tempContent.toString());
				data710.addSubfield(c);
			}
			if(data710.getSubfieldCount() > 0)
				rec.addDataField(data710);
		}
		
		DataField data852 = new DataField("852", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		a = new Subfield('a');
		if ((studyFinalDocumentDTO.getHoldingData() != null)
				&& (!"".equals(studyFinalDocumentDTO.getHoldingData())))
			tempContent.append(studyFinalDocumentDTO.getHoldingData());
		if (tempContent.toString().length() > 0) {
			a.setContent(tempContent.toString());
			data852.addSubfield(a);
		}
		if(data852.getSubfieldCount() > 0)
			rec.addDataField(data852);
		
		DataField data856 = new DataField("856", AbstractRecordConverter.BLANK, AbstractRecordConverter.BLANK);
		tempContent = new StringBuffer();
		u = new Subfield('u');
		if ((studyFinalDocumentDTO.getUri() != null)
				&& (!"".equals(studyFinalDocumentDTO.getUri())))
			tempContent.append(studyFinalDocumentDTO.getUri());
		if (tempContent.toString().length() > 0) {
			u.setContent(tempContent.toString());
			data856.addSubfield(u);
		}
		tempContent = new StringBuffer();
		u = new Subfield('u');
		if ((studyFinalDocumentDTO.getDoi() != null)
				&& (!"".equals(studyFinalDocumentDTO.getDoi())))
			tempContent.append("doi:" + studyFinalDocumentDTO.getDoi().trim());
		if (tempContent.toString().length() > 0) {
			u.setContent(tempContent.toString());
			data856.addSubfield(u);
		}
		tempContent = new StringBuffer();
		Subfield q = new Subfield('q');
		if ((studyFinalDocumentDTO.getContentFormat() != null)
				&& (!"".equals(studyFinalDocumentDTO.getContentFormat())))
			tempContent.append(studyFinalDocumentDTO.getContentFormat());
		if (tempContent.toString().length() > 0) {
			q.setContent(tempContent.toString());
			data856.addSubfield(q);
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
			if(classification.getCfClassSchemeId().equalsIgnoreCase(Types.TYPE_SCHEMA) && (classification.getCommissionId().equals(new Integer(0)))){
				type = classification;
				break;
			}	
		}
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		String studyType = ((StudyFinalDocumentDTO)dto).getStudyType();
		String stType = null;
		if("records.studyFinalDocument.editPanel.studyType.phd".equals(studyType))
			stType = Types.PHD_STUDY_FINAL_DOCUMENT;
		else if("records.studyFinalDocument.editPanel.studyType.phdArt".equals(studyType))
			stType = Types.PHD_ART_PROJECT;
		else if("records.studyFinalDocument.editPanel.studyType.oldMaster".equals(studyType))
			stType = Types.OLD_MASTER_STUDY_FINAL_DOCUMENT;
		else if("records.studyFinalDocument.editPanel.studyType.master".equals(studyType))
			stType = Types.MASTER_STUDY_FINAL_DOCUMENT;
		else if("records.studyFinalDocument.editPanel.studyType.oldBachelor".equals(studyType))
			stType = Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT;
		else if("records.studyFinalDocument.editPanel.studyType.bachelor".equals(studyType))
			stType = Types.BACHELOR_STUDY_FINAL_DOCUMENT;
		else if("records.studyFinalDocument.editPanel.studyType.specialistic".equals(studyType))
			stType = Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT;
		else 
			stType = Types.STUDY_FINAL_DOCUMENT;
		if((type == null) || ((type!=null) && (! (type.getCfClassId().equals(stType))))){
			if(((type!=null) && (! (type.getCfClassId().equals(stType))))){
				retVal.getRecordClasses().remove(type);
			}
			Classification recClass = new Classification(Types.TYPE_SCHEMA, stType, startDate, endDate, new Integer(0), null);
			retVal.getRecordClasses().add(recClass);
		}
		
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
		
		String studyFinalResearchArea = ((StudyFinalDocumentDTO)dto).getResearchArea().getClassId();
		
		if(studyFinalResearchArea != null){
			Classification recClass = new Classification(ResearchAreaDTO.RESEARCH_AREA_SCHEMA, studyFinalResearchArea, startDate, endDate, new Integer(0), null);
			retVal.getRecordClasses().add(recClass);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsOtherRecordsThisRecord(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsOtherRecordsThisRecord(Record retVal,
			RecordDTO dto) {
		StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO) dto;
		
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
		
		recordRecords.add(new RecordResultPublication(studyFinalDocument.getAuthor().getControlNumber(), "authorshipType",  "is author of", startDate, endDate, null));
		
		deleteRelationsOtherRecordsThisRecord(retVal, "authorshipType", "is advisor of");
		
		for (AuthorDTO advisor : studyFinalDocument.getAdvisors()) {
			recordRecords.add(new RecordResultPublication(advisor.getControlNumber(), "authorshipType", "is advisor of", startDate, endDate, null));
		}
		
		deleteRelationsOtherRecordsThisRecord(retVal, "authorshipType", "is committee member of");
		
		for (AuthorDTO committeeMember : studyFinalDocument.getCommitteeMembers()) {
			recordRecords.add(new RecordResultPublication(committeeMember.getControlNumber(), "authorshipType", "is committee member of", startDate, endDate, null));
		}
		
		deleteRelationsOtherRecordsThisRecord(retVal, "publicationsRelation", "is result of");
		
		for (PublicationDTO publication : studyFinalDocument.getRelatedPublications()) {
			recordRecords.add(new RecordResultPublication(publication.getControlNumber(), "publicationsRelation", "is result of", startDate, endDate, null));
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#setRelationsThisRecordOtherRecords(rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected void setRelationsThisRecordOtherRecords(Record retVal,
			RecordDTO dto) {
		StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO) dto;
		
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		deleteRelationsThisRecordOtherRecords(retVal, "publicationInstitutionRelation", "is defended at");
		deleteRelationsThisRecordOtherRecords(retVal, "publicationJobAd", "applied to");
		
		List<RecordRecord> recordRecords = retVal.getRelationsThisRecordOtherRecords();
		
		if(studyFinalDocument.getInstitution().getControlNumber() != null)
			recordRecords.add(new RecordRecord(studyFinalDocument.getInstitution().getControlNumber(), "publicationInstitutionRelation", "is defended at", startDate, endDate));
		for (JobAdDTO jobAd : studyFinalDocument.getJobAds()) {
			recordRecords.add(new RecordRecord(jobAd.getControlNumber(), "publicationJobAd", "applied to", startDate, endDate));
		}
	}
	
	/**
	 *
	 * 
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#getControlField006(rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected ControlField getControlField006(RecordDTO dto) {
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
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ABibliographicRecordConverter#getControlField008(rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected ControlField getControlField008(RecordDTO dto) {
		StudyFinalDocumentDTO studyFinalDocumentDTO = (StudyFinalDocumentDTO) dto;
		ControlField retVal = null; 
		String content = "";
		for(int i=0;i<35;i++)
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
		if(studyFinalDocumentDTO.getLanguage() != null){
			content += studyFinalDocumentDTO.getLanguage();
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
			content += AbstractRecordConverter.NO_ATTEMPT_TO_CODE;
			retVal = new ControlField("008");
			retVal.setContent(content);
		}
		return retVal;
	}
	
	@Override
	protected void setLeader(MARC21Record rec, RecordDTO dto) {
		super.setLeader(rec, dto);
		StudyFinalDocumentDTO sfd = (StudyFinalDocumentDTO)dto;
		if(sfd.getRecordType() == '\u0000')
			rec.getLeader().setRecordType(AbstractRecordConverter.BLANK);
		else
			rec.getLeader().setRecordType(sfd.getRecordType());
	}
	
	protected static Log log = LogFactory.getLog(StudyFinalDocumentConverter.class.getName());

}
