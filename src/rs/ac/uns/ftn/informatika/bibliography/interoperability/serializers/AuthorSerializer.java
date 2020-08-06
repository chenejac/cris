/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rs.ac.uns.ftn.informatika.bibliography.dao.MyDataSource;
import rs.ac.uns.ftn.informatika.bibliography.dao.PositionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.ImportRecordsDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

/**
 * @author Dragan Ivanovic
 *
 */
public class AuthorSerializer implements GroupSerializer{
	
	@Override
	public OutputStream exportRecords(ImportRecordsDTO records) {
		return null;
	}

	@Override
	public ImportRecordsDTO importRecords(InputStream is) {
		ImportRecordsDTO retVal = new ImportRecordsDTO();
		if(loadInstitutions(retVal))
			if(loadAuthors(retVal))
				return retVal;
		return new ImportRecordsDTO();
	}
	
	private boolean loadInstitutions(ImportRecordsDTO importRecordDTO){
		boolean retVal = false;
		List<RecordDTO> institutions = new ArrayList<RecordDTO>();
		try {
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			List<String> recordsControlNumbers = new ArrayList<String>(); 
			recordsControlNumbers.add("(BISIS)5929"); //PMF
			recordsControlNumbers.add("(BISIS)6782"); //DMI 
			recordsControlNumbers.add("(BISIS)6781"); //DH
			recordsControlNumbers.add("(BISIS)6780"); //DGT
			recordsControlNumbers.add("(BISIS)6779"); //DF
			recordsControlNumbers.add("(BISIS)6778"); //DBE
//			recordsControlNumbers.add("(BISIS)5933"); //TF
			List<Record> records = recordDAO.getRecords(recordsControlNumbers);
			for (Record temp : records) {
				institutions.add(temp.getDto());
			}
			importRecordDTO.setAllRecords(institutions);
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
				XSSFCell cell = row.getCell(1);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				authorDTO.getName().setFirstname(cellContent);
				cell = row.getCell(2);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				authorDTO.getName().setLastname(cellContent);
				cell = row.getCell(3);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				PositionDAO positionDAO = new PositionDAO();
				PositionDTO positionDTO = positionDAO.getPosition(getPositionClassId(cellContent));
				authorDTO.getCurrentPosition().setPosition(positionDTO);
				
				cell = row.getCell(4);
				if(cell == null)
					cellContent = "";
				else {
					DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
					Date date = (Date)formatter.parse(cell.getRichStringCellValue().getString().trim());
					Calendar startDate = new GregorianCalendar();
					startDate.setTime(date);
					authorDTO.getCurrentPosition().setStartDate(startDate);
				}
				
				cell = row.getCell(5);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
				authorDTO.setOrganizationUnit(new OrganizationUnitDTO(cellContent));
				
				authorDTO.setInstitution(new InstitutionDTO("(BISIS)5929"));
				authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
				authors.add(authorDTO);
			}
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

	private String getPositionClassId(String cellContent) {
		if("redovni profesor".equals(cellContent.toLowerCase().trim()))
			return "50";
		if("vanredni profesor".equals(cellContent.toLowerCase().trim()))
			return "40";
		if("asistent".equals(cellContent.toLowerCase().trim()))
			return "12";
		if("istrazivac saradnik".equals(cellContent.toLowerCase().trim()))
			return "70";
		if("docent".equals(cellContent.toLowerCase().trim()))
			return "30";
		if("asistent pripravnik".equals(cellContent.toLowerCase().trim()))
			return "11";
		if("istrazivac pripravnik".equals(cellContent.toLowerCase().trim()))
			return "66";
		if("naucni saradnik".equals(cellContent.toLowerCase().trim()))
			return "80";
		if("predavac".equals(cellContent.toLowerCase().trim()))
			return "24";
		if("visi naucni saradnik".equals(cellContent.toLowerCase().trim()))
			return "90";
		if("visi predavac".equals(cellContent.toLowerCase().trim()))
			return "25";
		System.out.println("Problem!!!!");
		return null;
	}

	private static Log log = LogFactory.getLog(AuthorSerializer.class.getName());
	
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

}
