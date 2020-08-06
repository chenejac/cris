/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.ImportRecordsDTO;

/**
 * @author Dragan Ivanovic
 *
 */
public class UNSDissertationsSerializer implements GroupSerializer{
	
	@Override
	public OutputStream exportRecords(ImportRecordsDTO records) {
		return null;
	}

	@Override
	public ImportRecordsDTO importRecords(InputStream is) {
		ImportRecordsDTO retVal = new ImportRecordsDTO();
		if(loadInstitutions(retVal))
			if(loadAuthors(retVal))
				if(loadTheses(retVal))
					return retVal;
		return new ImportRecordsDTO();
	}

	private boolean loadInstitutions(ImportRecordsDTO importRecordDTO){
		boolean retVal = false;
		List<RecordDTO> institutions = new ArrayList<RecordDTO>();
		try {
			InputStream inputStream = null;
			inputStream = new FileInputStream (xlsPath);
			XSSFWorkbook      workBook = new XSSFWorkbook (inputStream);
			XSSFSheet         sheet    = workBook.getSheetAt (0);
			Iterator<Row> rows     = sheet.rowIterator();
			String cellContent = "";
			while (rows.hasNext ()){
				InstitutionDTO institutionDTO = new InstitutionDTO();
				XSSFRow row = (XSSFRow) rows.next();
				XSSFCell cell = row.getCell(2);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if("".equals(cellContent))
					break;
				institutionDTO.getName().setContent(cellContent);
				institutionDTO.getName().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);		
				institutionDTO.setControlNumber(cellContent);
				institutionDTO.setSuperInstitution(new InstitutionDTO());
				if(!institutions.contains(institutionDTO))
					institutions.add(institutionDTO);
			}
			System.out.println(institutions.size());
			importRecordDTO.getAllRecords().addAll(institutions);
			importRecordDTO.setAllRecords(importRecordDTO.getAllRecords());
			retVal = true;
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot load institutions");
			retVal = false;
		}
		return retVal;
	}
		
	private boolean loadAuthors(ImportRecordsDTO importRecordDTO){
		boolean retVal = false;
		List<RecordDTO> authors = new ArrayList<RecordDTO>();
		try {
			InputStream inputStream = null;
			inputStream = new FileInputStream (xlsPath);
			XSSFWorkbook      workBook = new XSSFWorkbook (inputStream);
			XSSFSheet         sheet    = workBook.getSheetAt (0);
			Iterator<Row> rows     = sheet.rowIterator();
			String cellContent = "";
			while (rows.hasNext ()){
				
				AuthorDTO authorDTO = new AuthorDTO();
				XSSFRow row = (XSSFRow) rows.next();
				XSSFCell cell = row.getCell(4);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if("".equals(cellContent))
					break;
				authorDTO.getName().setFirstname(cellContent);
				cell = row.getCell(3);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				authorDTO.getName().setLastname(cellContent);
				cell = row.getCell(5);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				authorDTO.getName().setOtherName(cellContent);
				
				cell = row.getCell(6);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(!("".equals(cellContent))){
					DateFormat formatter ; 
					Date date ; 
					formatter = new SimpleDateFormat("dd.MM.yyyy.");
					date = (Date)formatter.parse(cellContent); 
					Calendar cal=GregorianCalendar.getInstance();
					cal.setTime(date);
					authorDTO.setDateOfBirth(cal);
				}
				
				cell = row.getCell(7);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				authorDTO.setPlaceOfBirth(cellContent);
				
				cell = row.getCell(8);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				authorDTO.setState(cellContent);
				
				cell = row.getCell(12);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				
				authorDTO.setControlNumber(cellContent + authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
				if(!authors.contains(authorDTO))
					authors.add(authorDTO);
				
				authorDTO = new AuthorDTO();
				cell = row.getCell(16);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					String[] data = cellContent.split(";");
					authorDTO.getName().setFirstname(data[2].trim());
					authorDTO.getName().setLastname(data[3].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					if(!authors.contains(authorDTO))
						authors.add(authorDTO);
				}
				
				authorDTO = new AuthorDTO();
				cell = row.getCell(17);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					String[] data = cellContent.split(";");
					authorDTO.getName().setFirstname(data[2].trim());
					authorDTO.getName().setLastname(data[3].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					if(!authors.contains(authorDTO))
						authors.add(authorDTO);
				}
				
				authorDTO = new AuthorDTO();
				cell = row.getCell(18);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					String[] data = cellContent.split(";");
					authorDTO.getName().setFirstname(data[2].trim());
					authorDTO.getName().setLastname(data[3].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					if(!authors.contains(authorDTO))
						authors.add(authorDTO);
				}
				
				authorDTO = new AuthorDTO();
				cell = row.getCell(19);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					String[] data = cellContent.split(";");
					authorDTO.getName().setFirstname(data[2].trim());
					authorDTO.getName().setLastname(data[3].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					if(!authors.contains(authorDTO))
						authors.add(authorDTO);
				}
				
				authorDTO = new AuthorDTO();
				cell = row.getCell(20);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					String[] data = cellContent.split(";");
					authorDTO.getName().setFirstname(data[2].trim());
					authorDTO.getName().setLastname(data[3].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					if(!authors.contains(authorDTO))
						authors.add(authorDTO);
				}
				
				authorDTO = new AuthorDTO();
				cell = row.getCell(21);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					String[] data = cellContent.split(";");
					authorDTO.getName().setFirstname(data[2].trim());
					authorDTO.getName().setLastname(data[3].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					if(!authors.contains(authorDTO))
						authors.add(authorDTO);
				}
				
				authorDTO = new AuthorDTO();
				cell = row.getCell(22);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					String[] data = cellContent.split(";");
					authorDTO.getName().setFirstname(data[2].trim());
					authorDTO.getName().setLastname(data[3].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					if(!authors.contains(authorDTO))
						authors.add(authorDTO);
				}
			}
			System.out.println(authors.size());
			importRecordDTO.getAllRecords().addAll(authors);
			importRecordDTO.setAllRecords(importRecordDTO.getAllRecords());
			retVal = true;
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot load authors");
			retVal = false;
		}
		return retVal;
	}
	
	private boolean loadTheses(ImportRecordsDTO importRecordDTO){
		boolean retVal = false;
		List<RecordDTO> theses = new ArrayList<RecordDTO>();
		try {
			InputStream inputStream = null;
			inputStream = new FileInputStream (xlsPath);
			XSSFWorkbook      workBook = new XSSFWorkbook (inputStream);
			XSSFSheet         sheet    = workBook.getSheetAt (0);
			Iterator<Row> rows     = sheet.rowIterator();
			String cellContent = "";
			while (rows.hasNext ()){
				StudyFinalDocumentDTO thesisDTO = new StudyFinalDocumentDTO();
				thesisDTO.setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
				
				XSSFRow row = (XSSFRow) rows.next();
				XSSFCell cell = row.getCell(12);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if("".equals(cellContent))
					break;
				thesisDTO.getTitle().setContent(cellContent);
				thesisDTO.getTitle().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
				
				cell = row.getCell(0);
				if(cell == null)
					cellContent = "";
				else {
					if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
						cellContent = "" + (int)cell.getNumericCellValue();
					} else {
						XSSFRichTextString richTextString = cell.getRichStringCellValue();
						cellContent = richTextString.getString().trim();
					}
				}
				thesisDTO.getRegisterEntry().setId(cellContent);
				
				cell = row.getCell(9);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				thesisDTO.getRegisterEntry().setPreviouslyGraduated(cellContent);
				
				cell = row.getCell(10);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				thesisDTO.getRegisterEntry().setPreviouslyNameOfAuthorDegreeOld(cellContent);
				
				cell = row.getCell(11);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				thesisDTO.getRegisterEntry().setNameOfAuthorDegree(cellContent);
				
				cell = row.getCell(14); 
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					DateFormat formatter ; 
					Date date ; 
					formatter = new SimpleDateFormat("dd.MM.yyyy.");
					date = (Date)formatter.parse(cellContent); 
					Calendar cal=GregorianCalendar.getInstance();
					cal.setTime(date);
					thesisDTO.getRegisterEntry().setPromotionDate(cal);
				}
				
				cell = row.getCell(13); 
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					DateFormat formatter ; 
					Date date ; 
					formatter = new SimpleDateFormat("dd.MM.yyyy.");
					date = (Date)formatter.parse(cellContent); 
					Calendar cal=GregorianCalendar.getInstance();
					cal.setTime(date);
					thesisDTO.setDefendedOn(cal);
					thesisDTO.setPublicationDate(cal);
				} else {
					cell = row.getCell(1); 
					if(cell == null)
						cellContent = "";
					else {
						if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
							cellContent = "" + (int)cell.getNumericCellValue();
						} else {
							XSSFRichTextString richTextString = cell.getRichStringCellValue();
							cellContent = richTextString.getString().trim();
						}
					}
					if(! ("".equals(cellContent))){
						Calendar cal=GregorianCalendar.getInstance();
						cal.set(Calendar.YEAR, Integer.parseInt(cellContent));
						cal.set(Calendar.DAY_OF_YEAR, 1);
						thesisDTO.setDefendedOn(cal);
						thesisDTO.setPublicationDate(cal);
					}
				}
			    
				cell = row.getCell(15);  
				if(cell == null)
					cellContent = "";
				else {
					if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
						cellContent = "" + (int)cell.getNumericCellValue();
					} else {
						XSSFRichTextString richTextString = cell.getRichStringCellValue();
						cellContent = richTextString.getString().trim();
					}
				}
				thesisDTO.getRegisterEntry().setDiplomaNumber(cellContent);
				
				cell = row.getCell(2);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				
				thesisDTO.getInstitution().getName().setContent(cellContent);
				thesisDTO.getInstitution().getName().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
				thesisDTO.getInstitution().setControlNumber(cellContent);
				
				RecordDTO superInstitution = (RecordDTO)thesisDTO.getInstitution();
				if((superInstitution != null) && (importRecordDTO.getAllRecords().indexOf(superInstitution) != -1))
					importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(superInstitution)).addRelatedRecord(thesisDTO);
				
				cell = row.getCell(3);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				
				thesisDTO.getAuthor().getName().setLastname(cellContent);
				
				cell = row.getCell(4);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				
				thesisDTO.getAuthor().getName().setFirstname(cellContent);
				thesisDTO.getAuthor().setControlNumber(thesisDTO.getTitle().getContent() + thesisDTO.getAuthor().getName().getLastname() + thesisDTO.getAuthor().getName().getFirstname());
				
				
				RecordDTO author = (RecordDTO)thesisDTO.getAuthor();
				if((author != null) && (importRecordDTO.getAllRecords().indexOf(author) != -1))
					importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(author)).addRelatedRecord(thesisDTO);
				
				
				AuthorDTO authorDTO = new AuthorDTO();
				cell = row.getCell(17);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					String[] data = cellContent.split(";");
					if(!("-".equals(data[0].trim())))
						authorDTO.getCurrentPosition().getPosition().getTerm().setContent(data[0].trim());
					if(!("-".equals(data[1].trim())))
						authorDTO.setTitle(data[1].trim());
					authorDTO.getName().setFirstname(data[2].trim());
					authorDTO.getName().setLastname(data[3].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					thesisDTO.getCommitteeMembers().add(authorDTO);
					if((authorDTO != null) && (importRecordDTO.getAllRecords().indexOf(authorDTO) != -1))
						importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(authorDTO)).addRelatedRecord(thesisDTO);
				}
				
				authorDTO = new AuthorDTO();
				cell = row.getCell(18);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					String[] data = cellContent.split(";");
					if(!("-".equals(data[0].trim())))
						authorDTO.getCurrentPosition().getPosition().getTerm().setContent(data[0].trim());
					if(!("-".equals(data[1].trim())))
						authorDTO.setTitle(data[1].trim());
					authorDTO.getName().setFirstname(data[2].trim());
					authorDTO.getName().setLastname(data[3].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					thesisDTO.getCommitteeMembers().add(authorDTO);
					if((authorDTO != null) && (importRecordDTO.getAllRecords().indexOf(authorDTO) != -1))
						importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(authorDTO)).addRelatedRecord(thesisDTO);
				}
				
				authorDTO = new AuthorDTO();
				cell = row.getCell(19);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					String[] data = cellContent.split(";");
					if(!("-".equals(data[0].trim())))
						authorDTO.getCurrentPosition().getPosition().getTerm().setContent(data[0].trim());
					if(!("-".equals(data[1].trim())))
						authorDTO.setTitle(data[1].trim());
					authorDTO.getName().setFirstname(data[2].trim());
					authorDTO.getName().setLastname(data[3].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					thesisDTO.getCommitteeMembers().add(authorDTO);
					if((authorDTO != null) && (importRecordDTO.getAllRecords().indexOf(authorDTO) != -1))
						importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(authorDTO)).addRelatedRecord(thesisDTO);
				}
				
				authorDTO = new AuthorDTO();
				cell = row.getCell(20);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					String[] data = cellContent.split(";");
					if(!("-".equals(data[0].trim())))
						authorDTO.getCurrentPosition().getPosition().getTerm().setContent(data[0].trim());
					if(!("-".equals(data[1].trim())))
						authorDTO.setTitle(data[1].trim());
					authorDTO.getName().setFirstname(data[2].trim());
					authorDTO.getName().setLastname(data[3].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					thesisDTO.getCommitteeMembers().add(authorDTO);
					if((authorDTO != null) && (importRecordDTO.getAllRecords().indexOf(authorDTO) != -1))
						importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(authorDTO)).addRelatedRecord(thesisDTO);
				}
				
				authorDTO = new AuthorDTO();
				cell = row.getCell(21);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					String[] data = cellContent.split(";");
					if(!("-".equals(data[0].trim())))
						authorDTO.getCurrentPosition().getPosition().getTerm().setContent(data[0].trim());
					if(!("-".equals(data[1].trim())))
						authorDTO.setTitle(data[1].trim());
					authorDTO.getName().setFirstname(data[2].trim());
					authorDTO.getName().setLastname(data[3].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					thesisDTO.getCommitteeMembers().add(authorDTO);
					if((authorDTO != null) && (importRecordDTO.getAllRecords().indexOf(authorDTO) != -1))
						importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(authorDTO)).addRelatedRecord(thesisDTO);
				}
				
				authorDTO = new AuthorDTO();
				cell = row.getCell(22);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					String[] data = cellContent.split(";");
					if(!("-".equals(data[0].trim())))
						authorDTO.getCurrentPosition().getPosition().getTerm().setContent(data[0].trim());
					if(!("-".equals(data[1].trim())))
						authorDTO.setTitle(data[1].trim());
					authorDTO.getName().setFirstname(data[2].trim());
					authorDTO.getName().setLastname(data[3].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					thesisDTO.getCommitteeMembers().add(authorDTO);
					if((authorDTO != null) && (importRecordDTO.getAllRecords().indexOf(authorDTO) != -1))
						importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(authorDTO)).addRelatedRecord(thesisDTO);
				}
				
				authorDTO = new AuthorDTO();
				cell = row.getCell(16);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				if(! ("".equals(cellContent))){
					String[] data = cellContent.split(";");
					if(!("-".equals(data[0].trim())))
						authorDTO.getCurrentPosition().getPosition().getTerm().setContent(data[0].trim());
					if(!("-".equals(data[1].trim())))
						authorDTO.setTitle(data[1].trim());
					authorDTO.getName().setFirstname(data[2].trim());
					authorDTO.getName().setLastname(data[3].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					thesisDTO.getAdvisors().add(authorDTO);
					thesisDTO.getCommitteeMembers().add(authorDTO);
					if((authorDTO != null) && (importRecordDTO.getAllRecords().indexOf(authorDTO) != -1))
						importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(authorDTO)).addRelatedRecord(thesisDTO);
				}
				thesisDTO.setControlNumber(thesisDTO.getPublicationYear() + thesisDTO.getTitle().getContent());
				thesisDTO.setStudyType("records.studyFinalDocument.editPanel.studyType.phd");
				if(!theses.contains(thesisDTO))
					theses.add(thesisDTO);
				else 
					System.out.println(thesisDTO);
			}
			System.out.println(theses.size());
			importRecordDTO.getAllRecords().addAll(theses);
			importRecordDTO.setAllRecords(importRecordDTO.getAllRecords());
			retVal = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.fatal(ex);
			log.fatal("Cannot load theses");
		}
		return retVal;
	}

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
		this.xlsPath = xlsPath;
	}

	private static Log log = LogFactory.getLog(UNSDissertationsSerializer.class.getName());

}
