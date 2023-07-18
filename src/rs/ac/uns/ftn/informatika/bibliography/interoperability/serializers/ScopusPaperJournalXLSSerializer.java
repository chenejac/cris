package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
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
public class ScopusPaperJournalXLSSerializer implements GroupSerializer{
	
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
			if(cellIds == null)
				cellIds = null;
			else {
				cellContent = fmt.formatCellValue(cellIds);
			}

			String authorsIdsList = cellContent;
			if(((authorsList == null) || (authorsList.equals("Authors"))))
				return null;
			else {
				String[] authorsListString = authorsList.split("\\.,");
				String[] authorsIdsString = (authorsIdsList==null)?new String[0]:authorsIdsList.split(";");
				for (int i=0; i < authorsListString.length; i++) {
					String author = authorsListString[i];
					String[] firstNameLastName = author.split(",");
					AuthorDTO authorDTO = new AuthorDTO();
//					authorDTO.setRelatedRecords(new ArrayList<RecordDTO>());
					authorDTO.getName().setFirstname((firstNameLastName[1].endsWith("."))?firstNameLastName[1]:firstNameLastName[1]+".");
					authorDTO.getName().setLastname(firstNameLastName[0]);
					authorDTO.setScopusID((authorsIdsString.length > i)?authorsIdsString[i]:null);
					authorDTO.setControlNumber((authorDTO.getScopusID()!=null)?authorDTO.getScopusID():(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname()));
					if(! authors.contains((RecordDTO)authorDTO))
						authors.add((RecordDTO)authorDTO);
				}
			}
		} catch (Throwable ex) {
			log.fatal(cellName);
			log.fatal(cellIds);
			log.fatal(ex);
			log.fatal("Cannot load authors");
			return null;
		}
		return authors;
	}
	
	private RecordDTO loadJournal(XSSFCell cellName, XSSFCell cellISSN){
		RecordDTO journal = null;
		DataFormatter fmt = new DataFormatter();
		try {
			String cellContent = null;
			if(cellName == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cellName);
			}

			String journalValue = cellContent;

			cellContent = null;
			if(cellISSN == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cellISSN);
			}
			String issnValue = cellContent;

			if(journalValue == null)
				return null;
			else {
				JournalDTO journalDTO = new JournalDTO();
//				journalDTO.setRelatedRecords(new ArrayList<RecordDTO>());
				journalDTO.getName().setContent(journalValue);
				journalDTO.getName().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);
				if(issnValue != null){
					if(issnValue.length() > 4){
						if(issnValue.length() < 8)
							issnValue = ("00000000" + issnValue).substring(issnValue.length());
						issnValue = issnValue.substring(0, 4) + "-" + issnValue.substring(4);
					}
					journalDTO.setIssn(issnValue);
					journalDTO.setControlNumber(journalDTO.getIssn());
				} else {
					journalDTO.setControlNumber(journalDTO.getName().getContent());
				}
				journalDTO.setScopusID(null);

				journal = journalDTO;
			}
		} catch (Exception ex) {
			log.fatal(cellName);
			log.fatal(cellISSN);
			log.fatal(ex);
			log.fatal("Cannot load journals");
		}
		return journal;
	}
	
	private boolean loadPaper(XSSFRow row, ImportRecordsDTO importRecords){
		boolean retVal = false;
		DataFormatter fmt = new DataFormatter();
		try {
			PaperJournalDTO paperDTO = new PaperJournalDTO();
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
			RecordDTO journal = loadJournal(row.getCell(4), row.getCell(14));
			if(journal == null)
				return false;
			else {
				RecordDTO journalMerged = null;
				if(importRecords.getAllRecords().contains(journal))
					journalMerged = importRecords.getAllRecords().get(importRecords.getAllRecords().indexOf(journal));
				else
					journalMerged = journal;
				journalMerged.addRelatedRecord(paperDTO);
				paperDTO.setJournal((JournalDTO)journalMerged);
			}

			String cellContent = null;
			XSSFCell cell = row.getCell(2);
			if(cell == null)
				cellContent = null;
			else {
				cellContent = fmt.formatCellValue(cell);
			}

			String titleValue = cellContent;
			if(titleValue == null)
				return false;
			else {
				paperDTO.getTitle().setContent(titleValue);
				paperDTO.getTitle().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);

				cellContent = null;
				cell = row.getCell(3);
				if(cell == null)
					cellContent = null;
				else {
					cellContent = fmt.formatCellValue(cell);
				}
				String yearValue = cellContent;
				if(yearValue != null){
					paperDTO.setPublicationYear(yearValue);
					paperDTO.setControlNumber(paperDTO.getPublicationYear() + paperDTO.getTitle().getContent());
				} else {
					paperDTO.setControlNumber(paperDTO.getTitle().getContent());
				}

				cellContent = null;
				cell = row.getCell(5);
				if(cell == null)
					cellContent = null;
				else {
					cellContent = fmt.formatCellValue(cell);
				}
				String volumeValue = cellContent;
				if(volumeValue != null){
					paperDTO.setVolume(volumeValue);
				}

				cellContent = null;
				cell = row.getCell(6);
				if(cell == null)
					cellContent = null;
				else {
					cellContent = fmt.formatCellValue(cell);
				}
				String numberValue = cellContent;
				if(numberValue != null){
					paperDTO.setNumber(numberValue);
				}

				cellContent = null;
				cell = row.getCell(7);
				if(cell == null)
					cellContent = null;
				else {
					cellContent = fmt.formatCellValue(cell);
				}
				String articleNumberValue = cellContent;
				if(articleNumberValue != null){
					paperDTO.setStartPage(articleNumberValue);
				}

				cellContent = null;
				cell = row.getCell(8);
				if(cell == null)
					cellContent = null;
				else {
					cellContent = fmt.formatCellValue(cell);
				}
				String startPageValue = cellContent;
				if(startPageValue != null){
					paperDTO.setStartPage(startPageValue);
				}

				cellContent = null;
				cell = row.getCell(9);
				if(cell == null)
					cellContent = null;
				else {
					cellContent = fmt.formatCellValue(cell);
				}
				String endPageValue = cellContent;
				if(endPageValue != null){
					paperDTO.setEndPage(endPageValue);
				}

				cellContent = null;
				cell = row.getCell(11);
				if(cell == null)
					cellContent = null;
				else {
					cellContent = fmt.formatCellValue(cell);
				}
				String doiValue = cellContent;
				if(doiValue != null){
					paperDTO.setDoi(doiValue);
				}

				cellContent = null;
				cell = row.getCell(12);
				if(cell == null)
					cellContent = null;
				else {
					cellContent = fmt.formatCellValue(cell);
				}
				String uriValue = cellContent;
				if(uriValue != null){
					paperDTO.setUri(uriValue);
				}

				cellContent = null;
				cell = row.getCell(13);
				if(cell == null)
					cellContent = null;
				else {
					cellContent = fmt.formatCellValue(cell);
				}
				String abstractValue = cellContent;
				if(abstractValue != null){
					paperDTO.getAbstracT().setContent(abstractValue);
					paperDTO.getAbstracT().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);
				}

				cellContent = null;
				cell = row.getCell(17);
				if(cell == null)
					cellContent = null;
				else {
					cellContent = fmt.formatCellValue(cell);
				}
				String eidValue = cellContent;
				if(eidValue != null){
					paperDTO.setScopusID(eidValue.replace("2-s2.0-", "").trim());
				}
				
				
				paperDTO.setPaperType("records.paperJournal.editPanel.paperType.paper");
				
				if(!importRecords.getAllRecords().contains(paperDTO)){
					for (RecordDTO recordDTO : authors) {
						if(!importRecords.getAllRecords().contains(recordDTO)){
							importRecords.getAllRecords().add(recordDTO);
						}
					}
					importRecords.setAllRecords(importRecords.getAllRecords());
					
					if(!importRecords.getAllRecords().contains(journal))
						importRecords.getAllRecords().add(journal);
					importRecords.setAllRecords(importRecords.getAllRecords());
					
					importRecords.getAllRecords().add(paperDTO);
					importRecords.setAllRecords(importRecords.getAllRecords());	
					retVal = true;
				}			
			}	
		} catch (Throwable ex) {
			log.fatal(ex);
			log.fatal("Cannot load paper");
		}
		return retVal;
	}
	
	private boolean reimport = false;
	
	private ImportRecordsDTO importRecords = new ImportRecordsDTO();

	private static Log log = LogFactory.getLog(ScopusPaperJournalXLSSerializer.class.getName());

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
