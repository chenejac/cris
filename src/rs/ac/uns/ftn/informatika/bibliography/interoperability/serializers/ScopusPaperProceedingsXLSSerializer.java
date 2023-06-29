package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import com.mashape.unirest.http.Unirest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jbibtex.ObjectResolutionException;
import org.jbibtex.TokenMgrException;
import org.json.JSONArray;
import org.json.JSONObject;
import rs.ac.uns.ftn.informatika.bibliography.dto.*;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.ImportRecordsDTO;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dragan Ivanovic
 *
 */
public class ScopusPaperProceedingsXLSSerializer implements GroupSerializer{
	
	@Override
	public OutputStream exportRecords(ImportRecordsDTO records) {
		return null;
	}

	@Override
	public ImportRecordsDTO importRecords(InputStream is) {
		if(reimport == false)
			return importRecords;
		else {
			importRecords = new ImportRecordsDTO();

			try {
				InputStream inputStream = null;
				inputStream = new FileInputStream(xlsPath);
				XSSFWorkbook workBook = new XSSFWorkbook (inputStream);
				XSSFSheet sheet    = workBook.getSheetAt (0);
				Iterator<Row> rows     = sheet.rowIterator();
				String cellContent = "";
				while (rows.hasNext ()) {

					XSSFRow row = (XSSFRow) rows.next();
					if(row.getCell(0).getStringCellValue().equals("ENDLINE"))
						break;
					else
						loadPaper(row, importRecords);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				log.fatal(ex);
				log.fatal("Cannot load papers");
			}
			return importRecords;
		}
	}
	
	private List<RecordDTO> loadAuthors(XSSFCell cellName, XSSFCell cellIds){
		List<RecordDTO> authors = new ArrayList<RecordDTO>();
		DataFormatter fmt = new DataFormatter();
		try {
			String cellContent = null;
			if(cellName == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cellName);
			}

			String authorsList = cellContent;

			cellContent = null;
			if(cellName == null)
				cellIds = null;
			else {
				cellContent = fmt.formatCellValue(cellIds);
			}

			String authorsIdsList = cellContent;
			if(((authorsList == null) || (authorsList.equals("Authors"))) || (authorsIdsList == null))
				return null;
			else {
				String[] authorsListString = authorsList.split("\\.,");
				String[] authorsIdsString = authorsIdsList.split(";");
				for (int i=0; i < authorsListString.length; i++) {
					String author = authorsListString[i];
					String[] firstNameLastName = author.split(",");
					AuthorDTO authorDTO = new AuthorDTO();
//					authorDTO.setRelatedRecords(new ArrayList<RecordDTO>());
					authorDTO.getName().setFirstname((firstNameLastName[1].endsWith("."))?firstNameLastName[1]:firstNameLastName[1]+".");
					authorDTO.getName().setLastname(firstNameLastName[0]);
					authorDTO.setScopusID((authorsIdsString.length > i)?authorsIdsString[i]:null);
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					if(! authors.contains((RecordDTO)authorDTO))
						authors.add((RecordDTO)authorDTO);
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
			log.fatal(ex);
			log.fatal("Cannot load authors");
			return null;
		}
		return authors;
	}
	
	private RecordDTO loadProceedings(XSSFCell cellTitle, XSSFCell cellYear, XSSFCell cellISBN, XSSFCell cellLanguage, XSSFCell cellAbbreviatedTitle, XSSFCell cellConferenceName, XSSFCell cellConferenceYear, XSSFCell cellConferenceDate, XSSFCell cellConferenceLocation, XSSFCell cellConferenceCode, ImportRecordsDTO importRecords){
		RecordDTO proceedings = null;
		DataFormatter fmt = new DataFormatter();
		try {
			String cellContent = null;
			if(cellTitle == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cellTitle);
			}
			String proceedingsValue = cellContent;

			cellContent = null;
			if(cellYear == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cellYear);
			}
			String yearValue = cellContent;

			cellContent = null;
			if(cellISBN == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cellISBN);
			}
			String isbnValue = cellContent;

			cellContent = null;
			if(cellLanguage == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cellLanguage);
			}
			String languageValue = (cellContent != null && cellContent.trim().equals("Serbian"))?MultilingualContentDTO.LANGUAGE_SERBIAN:MultilingualContentDTO.LANGUAGE_ENGLISH;

			cellContent = null;
			if(cellAbbreviatedTitle == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cellAbbreviatedTitle);
			}
			String proceedingsAbbreviatedValue = cellContent;

			cellContent = null;
			if(cellConferenceName == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cellConferenceName);
			}
			String conferenceNameValue = cellContent;

			cellContent = null;
			if(cellConferenceYear == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cellConferenceYear);
			}
			String conferenceYearValue = cellContent;

			cellContent = null;
			if(cellConferenceDate == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cellConferenceDate);
			}
			String conferenceDateValue = cellContent;

			cellContent = null;
			if(cellConferenceLocation == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cellConferenceLocation);
			}
			String conferenceLocationValue = cellContent;

			cellContent = null;
			if(cellConferenceCode == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cellConferenceCode);
			}
			String conferenceCodeValue = cellContent;

			if(proceedingsValue == null || conferenceNameValue == null)
				return null;
			else {
				ProceedingsDTO proceedingsDTO = new ProceedingsDTO();
//				proceedingsDTO.setRelatedRecords(new ArrayList<RecordDTO>());
				proceedingsDTO.getTitle().setContent(proceedingsValue);
				proceedingsDTO.getTitle().setLanguage(languageValue);

				if(yearValue != null){
					proceedingsDTO.setPublicationYear(yearValue);
				}

				if(isbnValue != null){
					proceedingsDTO.setIsbn(isbnValue);
				}

				if(proceedingsAbbreviatedValue != null){
					proceedingsDTO.getNameAbbreviation().setContent(proceedingsAbbreviatedValue);
					proceedingsDTO.getNameAbbreviation().setLanguage(languageValue);
				}

				ConferenceDTO conferenceDTO = new ConferenceDTO();
//				conferenceDTO.setRelatedRecords(new ArrayList<RecordDTO>());

				conferenceDTO.getName().setContent(conferenceNameValue);
				conferenceDTO.getName().setLanguage(languageValue);

				if(conferenceYearValue != null){
					conferenceDTO.setYear(Integer.valueOf(conferenceYearValue));
				}

				if(conferenceDateValue != null){
					conferenceDTO.setPeriod(conferenceDateValue);
				}

				if(conferenceLocationValue != null){
					conferenceDTO.setPlace(conferenceLocationValue);
				}

				if(conferenceCodeValue != null){
					conferenceDTO.setScopusID(conferenceCodeValue);
					conferenceDTO.setControlNumber(conferenceCodeValue);
				} else {
					conferenceDTO.setControlNumber(conferenceNameValue);
				}

				if(isbnValue != null){
					proceedingsDTO.setControlNumber(conferenceDTO.getControlNumber() + " PROC: " + proceedingsDTO.getIsbn());
				} else {
					proceedingsDTO.setControlNumber(conferenceDTO.getControlNumber() + " PROC: " + proceedingsDTO.getTitle().getContent());
				}

				proceedingsDTO.setScopusID(null);
				
				if(importRecords.getAllRecords().contains(conferenceDTO))
					conferenceDTO = (ConferenceDTO) importRecords.getAllRecords().get(importRecords.getAllRecords().indexOf(conferenceDTO));
				
				
				conferenceDTO.addRelatedRecord(proceedingsDTO);
				
				proceedingsDTO.setConference(conferenceDTO);
				
				proceedings = proceedingsDTO;
			}
			
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot load proceedings");
		}
		return proceedings;
	}
	
	private boolean loadPaper(XSSFRow row, ImportRecordsDTO importRecords){
		boolean retVal = false;
		DataFormatter fmt = new DataFormatter();
		try {
			PaperProceedingsDTO paperDTO = new PaperProceedingsDTO();
//			paperDTO.setRelatedRecords(new ArrayList<RecordDTO>());
			List<RecordDTO> authors = loadAuthors(row.getCell(0), row.getCell(1));
			if(authors == null)
				return false;
			else {
				List<RecordDTO> authorsMerged = new ArrayList<RecordDTO>();
				for (RecordDTO recordDTO : authors) {
					if(importRecords.getAllRecords().contains(recordDTO))
						authorsMerged.add(importRecords.getAllRecords().get(importRecords.getAllRecords().indexOf(recordDTO)));
					else
						authorsMerged.add(recordDTO);
				}

				boolean mainAuthor = false;
				for (RecordDTO recordDTO : authorsMerged) {
					recordDTO.addRelatedRecord(paperDTO);
					if(mainAuthor == true)
						paperDTO.getOtherAuthors().add((AuthorDTO)recordDTO);
					else {
						paperDTO.setMainAuthor((AuthorDTO)recordDTO);
						mainAuthor = true;
					}

				}
			}
			RecordDTO proceedings = loadProceedings(row.getCell(4), row.getCell(3), row.getCell(23), row.getCell(25), row.getCell(26), row.getCell(17), row.getCell(18), row.getCell(19), row.getCell(20), row.getCell(21),importRecords);
			if(proceedings == null)
				return false;
			else {
				RecordDTO proceedingsMerged = null;
				if(importRecords.getAllRecords().contains(proceedings))
					proceedingsMerged = importRecords.getAllRecords().get(importRecords.getAllRecords().indexOf(proceedings));
				else
					proceedingsMerged = proceedings;
				proceedingsMerged.addRelatedRecord(paperDTO);
				paperDTO.setProceedings((ProceedingsDTO)proceedingsMerged);
			}

			String cellContent = null;
			XSSFCell cell = row.getCell(2);
			if(cell == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cell);
			}
			String titleValue = cellContent;

			cellContent = null;
			cell = row.getCell(7);
			if(cell == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cell);
			}
			String articleNumberValue = cellContent;

			cellContent = null;
			cell = row.getCell(8);
			if(cell == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cell);
			}
			String startPageValue = cellContent;

			cellContent = null;
			cell = row.getCell(9);
			if(cell == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cell);
			}
			String endPageValue = cellContent;

			cellContent = null;
			cell = row.getCell(12);
			if(cell == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cell);
			}
			String doiValue = cellContent;

			cellContent = null;
			cell = row.getCell(13);
			if(cell == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cell);
			}
			String urlValue = cellContent;

			cellContent = null;
			cell = row.getCell(14);
			if(cell == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cell);
			}
			String abstractValue = cellContent;

			cellContent = null;
			cell = row.getCell(15);
			if(cell == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cell);
			}
			String keywordsValue = cellContent;

			cellContent = null;
			cell = row.getCell(25);
			if(cell == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cell);
			}
			String languageValue = (cellContent != null && cellContent.trim().equals("Serbian"))?MultilingualContentDTO.LANGUAGE_SERBIAN:MultilingualContentDTO.LANGUAGE_ENGLISH;

			cellContent = null;
			cell = row.getCell(31);
			if(cell == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cell);
			}
			String eidValue = cellContent;

			if(titleValue == null)
				return false;
			else {
				paperDTO.getTitle().setContent(titleValue);
				paperDTO.getTitle().setLanguage(languageValue);

				if(eidValue != null){
					paperDTO.setScopusID(eidValue);
					paperDTO.setControlNumber(eidValue + ", title: " + titleValue);
				} else {
					paperDTO.setControlNumber(titleValue);
				}

				if(startPageValue != null){
					paperDTO.setStartPage(startPageValue);
				}

				if(endPageValue != null){
					paperDTO.setEndPage(endPageValue);
				}

				if(articleNumberValue != null){
					paperDTO.setStartPage(articleNumberValue);
				}

				if(doiValue != null){
					paperDTO.setDoi(doiValue);
				}

				if(urlValue != null){
					paperDTO.setUri(urlValue);
				}

				if(abstractValue != null){
					paperDTO.getAbstracT().setContent(abstractValue);
					paperDTO.getAbstracT().setLanguage(languageValue);
				}

				if(keywordsValue != null){
					paperDTO.getKeywords().setContent(keywordsValue);
					paperDTO.getKeywords().setLanguage(languageValue);
				}

//				String subtype = (entry.has("subtype")&& !entry.isNull("subtype"))?entry.getString("subtype"):null;
//				if("cp".equals(subtype))
					paperDTO.setPaperType("records.paperProceedings.editPanel.paperType.full");
//				else if ("cp".equals(subtype))
//					paperDTO.setPaperType("records.paperProceedings.editPanel.paperType.full");
//				else if ("cp".equals(subtype))
//					paperDTO.setPaperType("records.paperProceedings.editPanel.paperType.full");
//				else if ("cp".equals(subtype))
//					paperDTO.setPaperType("records.paperProceedings.editPanel.paperType.full");
				
				if(!importRecords.getAllRecords().contains(paperDTO)){
					for (RecordDTO recordDTO : authors) {
						if(!importRecords.getAllRecords().contains(recordDTO)){
							importRecords.getAllRecords().add(recordDTO);
						}
					}
					importRecords.setAllRecords(importRecords.getAllRecords());
					
					if(!importRecords.getAllRecords().contains(proceedings)){
						if(!importRecords.getAllRecords().contains(((ProceedingsDTO)proceedings).getConference())){
							importRecords.getAllRecords().add(((ProceedingsDTO)proceedings).getConference());
						}
						importRecords.getAllRecords().add(proceedings);
					}
					importRecords.setAllRecords(importRecords.getAllRecords());
					importRecords.getAllRecords().add(paperDTO);
					importRecords.setAllRecords(importRecords.getAllRecords());	
					retVal = true;
				}			
			}	
		} catch (Throwable ex) {
			ex.printStackTrace();
			log.fatal(ex);
			log.fatal("Cannot load paper");
		}
		return retVal;
	}
	

	private boolean reimport = false;
	
	private ImportRecordsDTO importRecords = new ImportRecordsDTO();

	private static Log log = LogFactory.getLog(ScopusPaperJournalSerializer.class.getName());

	private String xlsPath;

	/**
	 * @return the xlsPath
	 */
	public String getXlsPath() {
		return xlsPath;
	}

	/**
	 * @param xlsPath the xlsPath to set
	 */
	public void setXlsPath(String xlsPath) {
		if (xlsPath == null)
			reimport = false;
		else if(this.xlsPath == null)
			reimport = true;
		else if (!xlsPath.equalsIgnoreCase(this.xlsPath))
			reimport = true;
		else
			reimport = false;
		this.xlsPath = xlsPath;
	}

}
