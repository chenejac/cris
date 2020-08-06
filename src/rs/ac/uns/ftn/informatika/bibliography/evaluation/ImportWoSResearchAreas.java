package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rs.ac.uns.ftn.informatika.bibliography.db.MetricsDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.db.ResearchAreaDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.MetricsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

/**
* This java program is used to read the data from a Excel file and display them
* on the console output.
*/

public class ImportWoSResearchAreas{
	
	public static Connection conn;
	 
	public static ResearchAreaDB researchAreaDB = new ResearchAreaDB();
	
	public static RecordDB recordDB = new RecordDB();
	
	public static MetricsDB metricsDB = new MetricsDB();
	
	static {
		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.connection");
		String hostname = rb.getString("hostname");
		String port = rb.getString("port");
		String schema = rb.getString("schema");
		String connectionParameters = rb.getString("connectionParameters");
		String username = rb.getString("username");
		String password = rb.getString("password");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
			conn.setAutoCommit(false);
		} catch (Exception e) {
		}
	}
	
	public static void importFromExcel (String xlsPath){
		InputStream inputStream = null;
		try{
			inputStream = new FileInputStream (xlsPath);
		}catch (FileNotFoundException e){
			System.out.println ("File not found in the specified path.");
			e.printStackTrace ();
		}

		try{
			XSSFWorkbook      workBook = new XSSFWorkbook (inputStream);
			XSSFSheet         sheet    = workBook.getSheetAt (0);
			Iterator<Row> rows     = sheet.rowIterator();

			String researchArea = "";
			HashMap<String, ResearchAreaDTO> researchAreas = new HashMap<String, ResearchAreaDTO>();
//			ResearchAreaDTO ra = new ResearchAreaDTO();
//			ra.setSchemeId("researchArea");
//			ra.setClassId("wos4");
//			researchAreas.put("Mathematics, Applied", ra);
//			ra = new ResearchAreaDTO();
//			ra.setSchemeId("researchArea");
//			ra.setClassId("wos10");
//			researchAreas.put("Operations Research & Management Science", ra);
//			ra = new ResearchAreaDTO();
//			ra.setSchemeId("researchArea");
//			ra.setClassId("wos5");
//			researchAreas.put("Mathematics", ra);
//			ra = new ResearchAreaDTO();
//			ra.setSchemeId("researchArea");
//			ra.setClassId("wos39");
//			researchAreas.put("Automation & Control Systems", ra);
//			ra = new ResearchAreaDTO();
//			ra.setSchemeId("researchArea");
//			ra.setClassId("wos1");
//			researchAreas.put("Computer Applications & Cybernetics", ra);
//			ra = new ResearchAreaDTO();
//			ra.setSchemeId("researchArea");
//			ra.setClassId("wos18");
//			researchAreas.put("Computer Science, Artificial Intelligence", ra);
//			ra = new ResearchAreaDTO();
//			ra.setSchemeId("researchArea");
//			ra.setClassId("wos40");
//			researchAreas.put("Robotics", ra);
			
			Record journal = null;
			String metricsId = "";
			String cellContent = "";
			int i=1; 
			while (rows.hasNext ()){
				journal = null;
				XSSFRow row = (XSSFRow) rows.next();
				XSSFCell cell = row.getCell(0);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString();
				}
				if(cellContent.startsWith("(BISIS)")){
					journal = recordDB.getRecord(conn, cellContent);
				}
				if(journal == null)
					continue;
				while (rows.hasNext()){
					row = (XSSFRow) rows.next();
					cell = row.getCell(0);
					if(cell == null)
						cellContent = "";
					else {
						XSSFRichTextString richTextString = cell.getRichStringCellValue();
						cellContent = richTextString.getString();
					}
					
					if(cellContent.equalsIgnoreCase("oblast / impakt faktor")){
						if(sheet.getRow(row.getRowNum()-3).getCell(0).getRichStringCellValue().getString().contains("PETOGODI")){
							metricsId = "fiveYearsIF";
						}
						else {
							metricsId = "twoYearsIF";
						}
						List<ResearchAreaDTO> researchAreasList = new ArrayList<ResearchAreaDTO>();
						
						int l = 0;
						do{
							XSSFRow rowTemp = (XSSFRow) sheet.getRow(row.getRowNum()+ l + 1);
							if(rowTemp == null)
								break;
							cell = rowTemp.getCell(0);
							if(cell == null)
								researchArea = "";
							else {
								XSSFRichTextString richTextString = cell.getRichStringCellValue();
								researchArea = richTextString.getString();
							}
							if(researchArea != null){
								if(! (researchAreas.containsKey(researchArea))){
									ResearchAreaDTO researchAreaDTO = new ResearchAreaDTO();
									researchAreaDTO.setClassId("wos"+i);
									researchAreaDTO.setTerm(new MultilingualContentDTO(researchArea, "eng", MultilingualContentDTO.TRANS_ORIGINAL));
									researchAreaDTO.setSuperResearchArea(new ResearchAreaDTO());
									researchAreaDB.addClass(conn, researchAreaDTO);
									researchAreasList.add(researchAreaDTO);
									researchAreas.put(researchArea, researchAreaDTO);
									i++;
								}
								else 
									researchAreasList.add(researchAreas.get(researchArea));
							}
							l++;
						} while ((! researchArea.equals("")) && (! researchArea.startsWith("Rang")) && (! researchArea.startsWith("(BISIS)")) && (! researchArea.startsWith("Podaci o")));
						researchAreasList.remove(researchAreasList.size()-1);
						for (int j = 2; j < sheet.getRow(row.getRowNum()-1).getLastCellNum(); j++){
							if((sheet.getRow(row.getRowNum()-1).getCell(j) != null) && (0 != (int)sheet.getRow(row.getRowNum()-1).getCell(j).getNumericCellValue())){
								int year = (int)sheet.getRow(row.getRowNum()-1).getCell(j).getNumericCellValue();
								double count = sheet.getRow(row.getRowNum()).getCell(j).getNumericCellValue();
								metricsDB.addResultMetrics(conn, journal.getMARC21Record().getControlNumber(), metricsId, "value of metric", "value of IF", year, count, null);
	//							System.out.println(journal.getMARC21Record().getControlNumber());
	//							System.out.println(year);
	//							System.out.println(count);
								for (int k = 0; k < researchAreasList.size(); k++) {
									if((sheet.getRow(row.getRowNum() + k + 1).getCell(j) != null) && (! "".equals(sheet.getRow(row.getRowNum() + k + 1).getCell(j).getRichStringCellValue().toString().trim()))){
										
										String fractionString = sheet.getRow(row.getRowNum() + k + 1).getCell(j).getRichStringCellValue().toString();
										count = Double.parseDouble(fractionString.split("/")[0]);
										double fraction = count / Double.parseDouble(fractionString.split("/")[1]);
	//									System.out.println(researchAreasList.get(k).getClassId());
	//									System.out.println(count);
	//									System.out.println(fraction);
										
										metricsDB.addResultMetrics(conn, journal.getMARC21Record().getControlNumber(), metricsId, researchAreasList.get(k).getSchemeId(), researchAreasList.get(k).getClassId(), year, count, fraction);
										
									}
								}
							}
						}
						if(metricsId.equals("fiveYearsIF"))
							break;
					}
				}
//					if((isResearchArea == true) && (researchArea.equals("") || researchArea.startsWith("Rang") || researchArea.startsWith("(BISIS)") || researchArea.startsWith("Podaci o")))
//					isResearchArea = false;
//				else if((isResearchArea==false) && (researchArea.equalsIgnoreCase("oblast / impakt faktor")))
//					isResearchArea = true;
//				else if (isResearchArea == true){
//					if(! (researchAreas.contains(researchArea))){
//						researchAreas.add(researchArea);
//						i++;
//						System.out.println(i+ ". " + researchArea);
//					}
				
				
			}	
		}catch (IOException e){
		}
	}


	public static void main (String[] args){
		
		
		
		MetricsDTO metricsDTO = new MetricsDTO();
		metricsDTO.setMetricsId("twoYearsIF");
		metricsDTO.setName(new MultilingualContentDTO("Two years impact factor", "eng", null));
		metricsDB.addMetrics(conn, metricsDTO);
		metricsDTO.setMetricsId("fiveYearsIF");
		metricsDTO.setName(new MultilingualContentDTO("Five years impact factor", "eng", null));
		metricsDB.addMetrics(conn, metricsDTO);
		
		
//		ImportWoSResearchAreas iwra = new ImportWoSResearchAreas ();
		//String  xlsPath    = "data/Prilozi I - IV Pravilnika-22.02.2010.xls";
		String  xlsPath    = "D:/ImpaktCasopisa.xlsx";
		ImportWoSResearchAreas.importFromExcel (xlsPath);
		
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}		