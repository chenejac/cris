//package rs.ac.uns.ftn.informatika.bibliography.evaluation;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.GregorianCalendar;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.PropertyResourceBundle;
//import java.util.ResourceBundle;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRichTextString;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.CreationHelper;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//
//import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
//import rs.ac.uns.ftn.informatika.bibliography.db.MetricsDB;
//import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
//import rs.ac.uns.ftn.informatika.bibliography.db.ResearchAreaDB;
//import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
//import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
//import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
//import rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson.KobsonDatabaseOperations;
//import rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson.beans.KobsonCategory;
//import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
//import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
//import rs.ac.uns.ftn.informatika.bibliography.textsrv.RecordConsumerListener;
//import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;
//
///**
// * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
// *
// */
//public class ResolveComparedKobsonCris {
//
//	public static Connection connCris;
//	public static Connection connKobson;
//	
//	public static RecordDB recordDB = new RecordDB();
//	public static RecordDAO recordDAO = new RecordDAO(new RecordDB());
//	public static ResearchAreaDB researchAreaDB = new ResearchAreaDB();
//	public static MetricsDB metricsDB = new MetricsDB();
//	
//	public static Log log = LogFactory.getLog(ExportJournalNameISSNDuplicates.class.getName());
//	
//	public static String importFolder;
//	public static String exportFolder;
//	
//	// Well behaved document
//	public static HSSFWorkbook wb;
//	public static HSSFSheet sheetCompared;
//	public static HSSFSheet sheetKobsonFound;
//	public static HSSFSheet sheetCrisFound;
//	public static HSSFSheet sheetCrisUnmapped;
//	
//	// Well behaved document
//	public static HSSFWorkbook wbOut = new HSSFWorkbook();
//	public static HSSFSheet sheetKobsonToCris = wbOut.createSheet();
//	public static CreationHelper createHelper = wbOut.getCreationHelper();
//	
//	public static HashMap<String, Integer> workbookHeaders = new HashMap<String, Integer>();
//	public static int hederRowNumber = 0;
//	
//	public static List<String> kobsonListIDs =null;
//	
//	public static HashMap<String, ResearchAreaDTO> researchAreas = new HashMap<String, ResearchAreaDTO>();
//	
////	public static boolean clearCategoriesCrissDatabase(Connection connCris)
////	{
////		boolean retVal = false;
////		try {
////			connCris.setAutoCommit(true);
////		} catch (SQLException e) {
////			retVal=false;
////			e.printStackTrace();
////		}
////		Map<String, ResearchAreaDTO> researchAreasAll = researchAreaDB.getAll(connCris);
////		
////		for(int i = 1; i<=500; i++)
////		{
////			if(researchAreasAll.containsKey("wos"+i))
////			{
////				System.out.println("wos"+i);
////				if(researchAreaDB.deleteClass(connCris, researchAreasAll.get("wos"+i))==false)
////				{
////					System.out.println("Greska u brisanju podataka kategorija");
////					System.exit(0);
////				}
////				System.out.println("Brisem obrisao klasu " + "wos"+i + " i semu " + researchAreasAll.get("wos"+i).getSchemeId());
////			}
////		}
////		metricsDB.deleteResultMetricsAll(connCris);
////		retVal=true;
////		
////		try {
////			connCris.setAutoCommit(false);
////		} catch (SQLException e) {
////			retVal=false;
////			e.printStackTrace();
////		}
////		
////		return retVal;
////	}
//	
////	public static boolean setCategoriesCrssDatabase(Connection connCris, Connection connKobson)
////	{
////		boolean retVal = false;
////		try {
////			connCris.setAutoCommit(true);
////		} catch (SQLException e) {
////			retVal = false;
////			e.printStackTrace();
////		}
////		KobsonDatabaseOperations.
////		KobsonDatabaseOperations.loadCategoriesFromDatabase();
////		
////		List<KobsonCategory> listKobsonKategories = 
////		
////		ResearchAreaDTO superResearchAreaDTO = researchAreaDB.getResearchArea(connCris, ResearchAreaDTO.RESEARCH_AREA_SCHEMA, "wos", new HashMap<String, ResearchAreaDTO>());
////		
////		for(Category cat : listKobsonKategories)
////		{
////			ResearchAreaDTO researchAreaDTO = new ResearchAreaDTO();
////			researchAreaDTO.setClassId("wos"+cat.getId());
////			researchAreaDTO.setTerm(new MultilingualContentDTO(cat.getName(), "eng", MultilingualContentDTO.TRANS_ORIGINAL));
////			researchAreaDTO.setSuperResearchArea(superResearchAreaDTO);
////			
////			if(researchAreaDB.addClass(connCris, researchAreaDTO)==false)
////			{
////				System.out.println("Greska u dodavanju kobsonovih naucnih oblasti");
////				System.exit(0);
////			}
////			System.out.println("Ubacio kategoriju klasu " + researchAreaDTO.getClassId());
////		}
////		retVal = true;
////		
////		try {
////			connCris.setAutoCommit(false);
////		} catch (SQLException e) {
////			retVal = false;
////			e.printStackTrace();
////			
////		}
////		
////		return retVal;
////	}
////	public static boolean loadCategoriesCrssDatabase(Connection connCris)
////	{
////		boolean retVal = false;
////		try {
////			connCris.setAutoCommit(true);
////		} catch (SQLException e) {
////			retVal = false;
////			e.printStackTrace();
////		}
////		Map<String, ResearchAreaDTO> researchAreasAll = researchAreaDB.getAll(connCris);
////		
////		for(int i = 1; i<=300; i++)
////		{
////			if(researchAreasAll.containsKey("wos"+i))
////			{
////				researchAreas.put("wos"+i, researchAreasAll.get("wos"+i));
////			}
////		}
////		
////		try {
////			connCris.setAutoCommit(false);
////		} catch (SQLException e) {
////			retVal = false;
////			e.printStackTrace();
////		}
////		
////		return retVal;
////	}
//	
//	public static void importFromExcel (String xlsPath){
//		
//		InputStream inputStream = null;
//		try{
//			inputStream = new FileInputStream (xlsPath);
//		}catch (FileNotFoundException e){
//			System.out.println ("File not found in the specified path.");
//			e.printStackTrace ();
//		}
//		
//		try {
//			wb = new HSSFWorkbook(inputStream);
//			sheetCompared = wb.getSheet("sheetCompared");
//			sheetKobsonFound = wb.getSheet("sheetKobsonFound");
//			sheetCrisFound = wb.getSheet("sheetCrisFound");
//			sheetCrisUnmapped = wb.getSheet("sheetCrisUnmapped");
//			
//		} catch (IOException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//	}
//	
//	public static void importFromExcelComared (boolean ifUradi){
//	
//		try{
//			RecordConsumerListener recordConsumer = new RecordConsumerListener();
//			
//			Iterator<Row> rows = sheetCompared.rowIterator();
//			
//			HSSFRow row;
//			HSSFCell cell;
//			
//			JouranlKobson jouranlKobson = new JouranlKobson();
//			int redUExcelu = -1;
//			//Map of kobson elements
//			int kobsonID = 0;
//			String kobsonNaziv = null;
//			String kobsonSkraceniNaziv = null;
//			String kobsonJezik = null;
//			String kobsonISSN = null;
//			String kobsonURI = null;
//			
//			String crisID = null;
//			String crisNaziv = null;
//			String crisSkraceniNaziv = null;
//			String crisJezik = null;
//			String crisISSN = null;
//			String crisURI = null;
//			
//			String compareNaziv = null;
//			String compareSkraceniNaziv = null;
//			String compareJezik = null;
//			String compareISSN = null;
//			String compareURI = null;
//			
//			while (rows.hasNext ()){
//				
//				//Map of elements
//				int redBr = 0;
//				String ISSNCompared = null;
//				
//				//prvi red RED.BR.
//				row = (HSSFRow) rows.next();
//				redUExcelu++;
//				cell = row.getCell(0); // RED.BR.
//				if(!cell.getStringCellValue().equalsIgnoreCase("RED.BR."))
//				{
//					System.out.println("Ocekuje se u excelu RED.BR. na mestu " + redUExcelu);
//					System.exit(0);
//
//				}
//				
//				redBr = getCellValueAsInt(row, 1);				//REDNI BROJ KOMPARACIJE
//				ISSNCompared = getCellValueAsString(row, 2);	//ISSN KOMPARACIJE
//				
//				System.out.println("Komparacija sa RED.BR. "+ redBr);
//				
//				kobsonNaziv = null;
//				kobsonSkraceniNaziv = null;
//				kobsonJezik = null;
//				kobsonISSN = null;
//				kobsonURI = null;
//				
//				//drugi red KOBSON
//				if(!rows.hasNext ())
//				{
//					System.out.println("Nema reda Ocekuje se u excelu KOBSON na mestu " + (redUExcelu+1));
//					System.exit(0);
//				}
//				row = (HSSFRow) rows.next();
//				redUExcelu++;
//				cell = row.getCell(0); // KOBSON
//				if(!cell.getStringCellValue().equalsIgnoreCase("KOBSON"))
//				{
//					System.out.println("Ocekuje se u excelu KOBSON na mestu " + redUExcelu);
//					System.exit(0);
//				}
//				kobsonID = getCellValueAsInt(row, 1);		//KOBSON ID KOMPARACIJE
//				kobsonNaziv = getCellValueAsString(row, 2);	//KOBSON NAZIV KOMPARACIJE
//				kobsonSkraceniNaziv = getCellValueAsString(row, 3);	//KOBSON SKRACENI NAZIV KOMPARACIJE
//				kobsonJezik = getCellValueAsString(row, 4);	//KOBSON JEZIK KOMPARACIJE
//				kobsonISSN = getCellValueAsString(row, 5);	//KOBSON ISSN KOMPARACIJE
//				kobsonURI = getCellValueAsString(row, 6);	//KOBSON URI KOMPARACIJE
//				
//				kobsonJezik = ResolveComparedKobsonCris.getLanguageFromKobson(kobsonJezik);
//				
//				
//				//Map of cris elements
//				crisID = null;
//				crisNaziv = null;
//				crisSkraceniNaziv = null;
//				crisJezik = null;
//				crisISSN = null;
//				crisURI = null;
//				//treci red CRIS
//				if(!rows.hasNext ())
//				{
//					System.out.println("Nema reda Ocekuje se u excelu CRIS na mestu " + (redUExcelu+1));
//					System.exit(0);
//				}
//				row = (HSSFRow) rows.next();
//				redUExcelu++;
//				cell = row.getCell(0); // CRIS
//				if(!cell.getStringCellValue().equalsIgnoreCase("CRIS"))
//				{
//					System.out.println("Ocekuje se u excelu CRIS na mestu " + redUExcelu);
//					System.exit(0);
//				}
//				crisID = getCellValueAsString(row, 1);		//CRIS ID KOMPARACIJE
//				crisNaziv = getCellValueAsString(row, 2);	//CRIS NAZIV KOMPARACIJE
//				crisSkraceniNaziv = getCellValueAsString(row, 3);	//CRIS SKRACENI NAZIV KOMPARACIJE
//				crisJezik = getCellValueAsString(row, 4);	//CRIS JEZIK KOMPARACIJE
//				crisISSN = getCellValueAsString(row, 5);	//CRIS ISSN KOMPARACIJE
//				crisURI = getCellValueAsString(row, 6);	//CRIS URI KOMPARACIJE
//				
//
//				//Map of compare elements
//				compareNaziv = null;
//				compareSkraceniNaziv = null;
//				compareJezik = null;
//				compareISSN = null;
//				compareURI = null;
//				//treci red compare
//				if(!rows.hasNext ())
//				{
//					System.out.println("Nema reda Ocekuje se u excelu ODLUKA na mestu " + (redUExcelu+1));
//					System.exit(0);
//				}
//				row = (HSSFRow) rows.next();
//				redUExcelu++;
//				cell = row.getCell(0); // compare
//				if(!cell.getStringCellValue().equalsIgnoreCase("ODLUKA K za Kobson, C za Cris"))
//				{
//					System.out.println("Ocekuje se u excelu ODLUKA na mestu " + redUExcelu);
//					System.exit(0);
//				}
//				
//				compareNaziv = getCellValueAsString(row, 2); 			//ODLUKA NAZIV
//				compareSkraceniNaziv = getCellValueAsString(row, 3); 	//ODLUKA SKRACENI NAZIV
//				compareJezik = getCellValueAsString(row, 4);			//ODLUKA JEZIK
//				compareISSN = getCellValueAsString(row, 5);				//ODLUKA ISSN
//				compareURI = getCellValueAsString(row, 6);				//ODLUKA URI
//
////				citanje cris casopisa 
//				
//				JournalDTO journalDTO = (JournalDTO) recordDAO.getDTO(crisID);
//				
//				if(journalDTO==null)
//				{
//					System.out.println("Casopis ne postoji u criss bazi sa id: " + crisID);
//					System.exit(0);
//				}
//				
////				Record journal = recordDB.getRecord(connCris, crisID);
////				//ne postoji casopis za bazu
////				if(journal==null)
////				{
////					System.out.println("Ne postoji casopis u bazi sa "+ crisID);
////					System.exit(0);
////				}
////				journal.loadFromDatabase();
////				
////				if(!(journal.getDto() instanceof JournalDTO))
////				{
////					System.out.println("ID u bazi ne valja "+ crisID + " to je " + journal.getDto().getRecord().getType());
////					continue;
////				}
////				
////				JournalDTO journalDTO = (JournalDTO) journal.getDto();
//				
//				//ocitavanje svih podataka iz cris baze 
//				journalDTO.getName();
//				
//				//setovanje vrednosti
//				if((compareNaziv==null) || (compareNaziv.equalsIgnoreCase("K")))
//				{
//					if(kobsonNaziv!=null)
//					{
//						journalDTO.getName().setContent(kobsonNaziv);
//					}
//					else
//					{
//						System.out.println("Za redni broj RED.BR. " + redBr + " ne valja kobsonNaziv");
//						System.exit(0);
//					}
//				}
//				else if (compareNaziv.equalsIgnoreCase("C"))
//				{
//					if(crisNaziv!=null)
//					{
//						journalDTO.getName().setContent(crisNaziv);
//					}
//					else
//					{
//						System.out.println("Za redni broj RED.BR. " + redBr + " ne valja crisNaziv");
//						System.exit(0);
//					}
//				}
//				else
//				{
//					System.out.println("Za redni broj RED.BR. " + redBr + " ne valja odluka Naslov");
//					System.exit(0);
//				}
//				
//				//setovanje vrednosti
//				if((compareJezik==null) || (compareJezik.equalsIgnoreCase("K")))
//				{
//					if(kobsonJezik!=null)
//					{
//						journalDTO.getName().setLanguage(kobsonJezik);
//					}
//				}
//				else if (compareJezik.equalsIgnoreCase("C"))
//				{
//					if(crisJezik!=null)
//					{
//						journalDTO.getName().setLanguage(crisJezik);
//					}
//					else
//					{
//						System.out.println("Za redni broj RED.BR. " + redBr + " ne valja crisJezik");
//						System.exit(0);
//					}
//				}
//				else
//				{
//					System.out.println("Za redni broj RED.BR. " + redBr + " ne valja odluka Jezik");
//					System.exit(0);
//				}
//				
//				//setovanje vrednosti
//				if((compareSkraceniNaziv==null) || (compareSkraceniNaziv.equalsIgnoreCase("K")))
//				{
//					if(kobsonSkraceniNaziv!=null)
//					{
//						journalDTO.getNameAbbreviation().setContent(kobsonSkraceniNaziv);
//					}
//				}
//				else if (compareSkraceniNaziv.equalsIgnoreCase("C"))
//				{
//					if(crisSkraceniNaziv!=null)
//					{
//						journalDTO.getNameAbbreviation().setContent(crisSkraceniNaziv);
//					}
//					else
//					{
//						System.out.println("Za redni broj RED.BR. " + redBr + " ne valja crisSkraceniNaziv");
//						System.exit(0);
//					}
//				}
//				else
//				{
//					System.out.println("Za redni broj RED.BR. " + redBr + " ne valja odluka SkraceniNaziv");
//					System.exit(0);
//				}
//				
//				//setovanje vrednosti
//				if((compareISSN==null) || (compareISSN.equalsIgnoreCase("K")))
//				{
//					if(kobsonISSN!=null)
//					{
//						journalDTO.setIssn(kobsonISSN);
//					}
//					else
//					{
//						System.out.println("Za redni broj RED.BR. " + redBr + " ne valja kobsonISSN");
//						System.exit(0);
//					}
//				}
//				else if (compareISSN.equalsIgnoreCase("C"))
//				{
//					if(crisISSN!=null)
//					{
//						journalDTO.setIssn(crisISSN);
//					}
//					else
//					{
//						System.out.println("Za redni broj RED.BR. " + redBr + " ne valja crisISSN");
//						System.exit(0);
//					}
//				}
//				else
//				{
//					System.out.println("Za redni broj RED.BR. " + redBr + " ne valja odluka ISSN");
//					System.exit(0);
//				}
//				
//				//setovanje vrednosti
//				if((compareURI==null) || (compareURI.equalsIgnoreCase("K")))
//				{
//					if(kobsonURI!=null)
//					{
//						journalDTO.setUri(kobsonURI);
//					}
//				}
//				else if (compareURI.equalsIgnoreCase("C"))
//				{
//					if(crisURI!=null)
//					{
//						journalDTO.setUri(crisURI);
//					}
//					else
//					{
//						System.out.println("Za redni broj RED.BR. " + redBr + " ne valja crisURI");
//						System.exit(0);
//					}
//				}
//				else
//				{
//					System.out.println("Za redni broj RED.BR. " + redBr + " ne valja odluka URI");
//					System.exit(0);
//				}
//				
//				//SNIMANJE JOURNALDTO IZMENJENOG U BAZU
//				if ((recordDAO.update(new Record(null, null, "sinisa_nikolic@uns.ac.rs", new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
//						journalDTO)) == false) || (recordConsumer.update(journalDTO) == false)) 
//				{
//					System.out.println("Neuspeli update casopisa RED.BR. "+ redBr);
//					System.exit(0);
//				}
//				else
//				{
//					System.out.println("USPEO update casopisa RED.BR. "+ redBr);
//				}
//				
//				//load impact factr and categories for kobson
//				
//				if(JournalYearIF.chackIFForYear2Exist(connKobson, kobsonID) && ifUradi)
//				{
//					jouranlKobson.clear();
//					if(jouranlKobson.loadFromDatabaseAll(connKobson, kobsonID, null)==false)
//					{
//						System.out.println("Casopis ne postoji u kobson bazi sa id: " + kobsonID);
//						System.exit(0);
//					}
//				
//					//ubaci metriku za if
//					// fiveYearsIF
//					// twoYearsIF
//					int godinaIF = 0;
//					double vrednostIF = 0;
//					List<JournalYearIF> IFsTwoYear = jouranlKobson.getIFsTwoYear();
//					List<JournalYearIF> IFsFiveYear = jouranlKobson.getIFsFiveYear();
//					
//					for(JournalYearIF journalYearIF : IFsTwoYear)
//					{
//						godinaIF = journalYearIF.getGodina();
//						vrednostIF = Double.parseDouble(journalYearIF.getVrednost());
//						metricsDB.addResultMetrics(connCris, journalDTO.getControlNumber(), "twoYearsIF", "value of metric", "value of IF", godinaIF, vrednostIF, null);
//					}
//					for(JournalYearIF journalYearIF : IFsFiveYear)
//					{
//						godinaIF = journalYearIF.getGodina();
//						vrednostIF = Double.parseDouble(journalYearIF.getVrednost());
//						metricsDB.addResultMetrics(connCris, journalDTO.getControlNumber(), "fiveYearsIF", "value of metric", "value of IF", godinaIF, vrednostIF, null);
//					}
//					
//					//ubaci metriku za rang
//					// fiveYearsIF
//					// twoYearsIF
//					int godinaRang = 0;
//					String vrednostRang = null;
//					String valueCategory = null;
//					double count = 0;
//					double fraction = 0;
//					List<JournalCategoryRankings> rankCategoryTwoYear = jouranlKobson.getRankCategoryTwoYear();
//					List<JournalCategoryRankings> rankCategoryFiveYear = jouranlKobson.getRankCategoryFiveYear();
//					
//					for(JournalCategoryRankings journalCategoryRankings : rankCategoryTwoYear)
//					{
//						
//						valueCategory = "wos"+journalCategoryRankings.getIdCategory();
//						List <Integer> godineRangiranja = journalCategoryRankings.getGodina();
//						List <String> rang = journalCategoryRankings.getRang();
//						
//						for(int i = 0; i<godineRangiranja.size(); i++)
//						{
//							godinaRang = godineRangiranja.get(i);
//							vrednostRang = rang.get(i);
//							
//							if(vrednostRang==null)
//								continue;
//							if(vrednostRang.trim().equalsIgnoreCase(""))
//								continue;
//							
//							count = Double.parseDouble(vrednostRang.split("/")[0]);
//							fraction = count / Double.parseDouble(vrednostRang.split("/")[1]);
//							
//							metricsDB.addResultMetrics(connCris, journalDTO.getControlNumber(), "twoYearsIF", "researchArea", valueCategory, godinaRang, count, fraction);
//						}
//					}
//					
//					for(JournalCategoryRankings journalCategoryRankings : rankCategoryFiveYear)
//					{
//						
//						valueCategory = "wos"+journalCategoryRankings.getIdCategory();
//						List <Integer> godineRangiranja = journalCategoryRankings.getGodina();
//						List <String> rang = journalCategoryRankings.getRang();
//						
//						for(int i = 0; i<godineRangiranja.size(); i++)
//						{
//							godinaRang = godineRangiranja.get(i);
//							vrednostRang = rang.get(i);
//							
//							if(vrednostRang==null)
//								continue;
//							if(vrednostRang.trim().equalsIgnoreCase(""))
//								continue;
//							
//							count = Double.parseDouble(vrednostRang.split("/")[0]);
//							fraction = count / Double.parseDouble(vrednostRang.split("/")[1]);
//							
//							metricsDB.addResultMetrics(connCris, journalDTO.getControlNumber(), "fiveYearsIF", "researchArea", valueCategory, godinaRang, count, fraction);
//						}
//					}
//					try {
//						connCris.commit();
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						System.out.println("Pukao upis u bazu IF casopisa RED.BR. "+ redBr);
//						System.exit(0);
//					}
//					System.out.print("Odradio IF ");
//				}
//				System.out.println("Odradio Casopisa RED.BR. "+ redBr);
//				//remove kobson ID form kobsonListIDs
//				//removeKovsonID(kobsonID);
//			}
//		}
//		catch (Exception e)
//		{
//			System.out.println("Puko importFromExcelComared");
//			e.printStackTrace();
//			System.exit(0);
//		}
//		
//			
//	}
//	
//	public static void importFromExcelNotCompared (){
//		
//		try{
//			Iterator<Row> rows = sheetKobsonFound.rowIterator();
//			
//			HSSFRow row;
//			HSSFCell cell;
//			
//			JouranlKobson jouranlKobson = new JouranlKobson();
//			
//			//da preskocimo zaglavlje tabele
//			if(sheetKobsonFound.getRow(hederRowNumber)!=null)
//			{
//				importHeaderFromExcel(sheetKobsonFound.getRow(hederRowNumber));
//				for(int i=0;i<=hederRowNumber;i++)
//					rows.next();
//			}
//
//			int rowsCounter = hederRowNumber; //zbog zaglavlja
//			
//			while (rows.hasNext ()){
//				//Map of kobson elements
//				int kobsonID = 0;
//				
//				//prvi red RED.BR.
//				row = (HSSFRow) rows.next();
//				rowsCounter++;
//				
//				cell = row.getCell(workbookHeaders.get("ID").intValue()); // RED.BR.
//				if(cell == null)
//				{
//					System.out.println("Ocekuje se vrednost ID u "+ rowsCounter);
//					System.exit(0);
//				}
//				else 
//				{
//					kobsonID = getCellValueAsInt(row, workbookHeaders.get("ID").intValue());		//KOBSON ID 
//					//uklonimo ga iz liste
//					removeKovsonID(kobsonID);
//				}
//			}
//					
//			int brojac = 0;
//			for (String idString : kobsonListIDs)
//			{
//
//				brojac++;
//				int kobsonID = Integer.parseInt(idString);	
//					
//				jouranlKobson.clear();
//				if(jouranlKobson.loadFromDatabaseAll(connKobson, kobsonID, null)==false)
//				{
//					System.out.println("Casopis ne postoji u kobson bazi sa id: " + kobsonID);
//					System.exit(0);
//				}
//				
//				//kreiranje casopisa u bazi
//				JournalDTO journalDTO = new JournalDTO();
//					
//				journalDTO.getName().setContent(jouranlKobson.getNaslov());
//					
//				if(jouranlKobson.getSkraceniNaslov()!=null)
//					if(!jouranlKobson.getSkraceniNaslov().trim().equalsIgnoreCase(""))
//						journalDTO.getNameAbbreviation().setContent(jouranlKobson.getSkraceniNaslov());
//					
//				if(jouranlKobson.getISSN()!=null)
//					if(!jouranlKobson.getISSN().trim().equalsIgnoreCase(""))
//						journalDTO.setIssn(jouranlKobson.getISSN());
//					
//				if(jouranlKobson.getURL()!=null)
//					if(!jouranlKobson.getURL().trim().equalsIgnoreCase(""))
//						journalDTO.setUri(jouranlKobson.getURL());
//					
//				String kobsonJezik = jouranlKobson.getJezik();
//				kobsonJezik = ResolveComparedKobsonCris.getLanguageFromKobson(kobsonJezik);
//					
//				if(kobsonJezik!=null)
//					if(!kobsonJezik.trim().equalsIgnoreCase(""))
//						journalDTO.getName().setLanguage(kobsonJezik);
//					
//					//SNIMANJE JOURNALDTO IZMENJENOG U BAZU
//				if (recordDAO.add(new Record("sinisa_nikolic@uns.ac.rs", new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
//						journalDTO)) == false) 
//				{
//					System.out.println("Neuspeli add casopisa kobson id "+ kobsonID);
//					System.exit(0);
//				}
//				else
//				{
//					System.out.println("USPEO add casopisa kobson id "+ kobsonID);
//				}
//					
//				//ubaci metriku za if
//				// fiveYearsIF
//				// twoYearsIF
//				int godinaIF = 0;
//				double vrednostIF = 0;
//				List<JournalYearIF> IFsTwoYear = jouranlKobson.getIFsTwoYear();
//				List<JournalYearIF> IFsFiveYear = jouranlKobson.getIFsFiveYear();
//					
//				for(JournalYearIF journalYearIF : IFsTwoYear)
//				{
//					godinaIF = journalYearIF.getGodina();
//					vrednostIF = Double.parseDouble(journalYearIF.getVrednost());
//					metricsDB.addResultMetrics(connCris, journalDTO.getControlNumber(), "twoYearsIF", "value of metric", "value of IF", godinaIF, vrednostIF, null);
//				}
//				for(JournalYearIF journalYearIF : IFsFiveYear)
//				{
//					godinaIF = journalYearIF.getGodina();
//					vrednostIF = Double.parseDouble(journalYearIF.getVrednost());
//					metricsDB.addResultMetrics(connCris, journalDTO.getControlNumber(), "fiveYearsIF", "value of metric", "value of IF", godinaIF, vrednostIF, null);
//				}
//				
//				//ubaci metriku za rang
//				// fiveYearsIF
//				// twoYearsIF
//				int godinaRang = 0;
//				String vrednostRang = null;
//				String valueCategory = null;
//				double count = 0;
//				double fraction = 0;
//				List<JournalCategoryRankings> rankCategoryTwoYear = jouranlKobson.getRankCategoryTwoYear();
//				List<JournalCategoryRankings> rankCategoryFiveYear = jouranlKobson.getRankCategoryFiveYear();
//
//				for(JournalCategoryRankings journalCategoryRankings : rankCategoryTwoYear)
//				{
//						
//					valueCategory = "wos"+journalCategoryRankings.getIdCategory();
//					List <Integer> godineRangiranja = journalCategoryRankings.getGodina();
//					List <String> rang = journalCategoryRankings.getRang();
//						
//					for(int i = 0; i<godineRangiranja.size(); i++)
//					{
//						godinaRang = godineRangiranja.get(i);
//						vrednostRang = rang.get(i);
//							
//						if(vrednostRang==null)
//							continue;
//						if(vrednostRang.trim().equalsIgnoreCase(""))
//							continue;
//							
//						count = Double.parseDouble(vrednostRang.split("/")[0]);
//						fraction = count / Double.parseDouble(vrednostRang.split("/")[1]);
//							
//						metricsDB.addResultMetrics(connCris, journalDTO.getControlNumber(), "twoYearsIF", "researchArea", valueCategory, godinaRang, count, fraction);
//					}
//				}
//
//				for(JournalCategoryRankings journalCategoryRankings : rankCategoryFiveYear)
//				{
//						
//					valueCategory = "wos"+journalCategoryRankings.getIdCategory();
//					List <Integer> godineRangiranja = journalCategoryRankings.getGodina();
//					List <String> rang = journalCategoryRankings.getRang();
//						
//					for(int i = 0; i<godineRangiranja.size(); i++)
//					{
//						godinaRang = godineRangiranja.get(i);
//						vrednostRang = rang.get(i);
//							
//						if(vrednostRang==null)
//							continue;
//						if(vrednostRang.trim().equalsIgnoreCase(""))
//							continue;
//							
//						count = Double.parseDouble(vrednostRang.split("/")[0]);
//						fraction = count / Double.parseDouble(vrednostRang.split("/")[1]);
//							
//						metricsDB.addResultMetrics(connCris, journalDTO.getControlNumber(), "fiveYearsIF", "researchArea", valueCategory, godinaRang, count, fraction);
//					}
//				}
//				try {
//					connCris.commit();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					System.out.println("Pukao upis u bazu IF casopisa kobson id "+ kobsonID);
//					System.exit(0);
//				}
//				System.out.println("Odradio IF casopisa kobson id "+ kobsonID);
//				
//				row =null;
//			    // Create a row and put some cells in it. Rows are 0 based.
//			    row = sheetKobsonToCris.createRow(brojac);
//			    
//			    row.createCell(0).setCellValue(brojac);
//			    row.createCell(1).setCellValue(createHelper.createRichTextString(jouranlKobson.getISSN()));
//			    row.createCell(2).setCellValue(kobsonID);
//			    row.createCell(3).setCellValue(createHelper.createRichTextString(journalDTO.getControlNumber()));		
//			    row.createCell(4).setCellValue(createHelper.createRichTextString(jouranlKobson.getNaslov()));
//			    	
//			}	
//		}
//		catch (Exception e)
//		{
//			System.out.println("Puko importFromExcelComared");
//			e.printStackTrace();
//			System.exit(0);
//		}
//	}
//	public static void importHeaderFromExcel (HSSFRow row){
//		
//		if(row==null)
//			return;
//
//		for (int i = 0; i < row.getLastCellNum(); i++){
//			
//			HSSFCell cell = row.getCell(i);
//			String cellContent="";
//			
//			if(cell == null)
//				continue;
//			else {
//				HSSFRichTextString richTextString = cell.getRichStringCellValue();
//				cellContent = richTextString.getString();
//			}
//			
//			if(cellContent.equalsIgnoreCase("ID")){
//				workbookHeaders.put("ID", new Integer(i));
//			}
//			else if (cellContent.equalsIgnoreCase("Naziv")){
//				workbookHeaders.put("Naziv", new Integer(i));
//			}
//			else if (cellContent.equalsIgnoreCase("Skraceni naziv")){
//				workbookHeaders.put("Skraceni naziv", new Integer(i));
//			}
//			else if (cellContent.equalsIgnoreCase("ISSN")){
//				workbookHeaders.put("ISSN", new Integer(i));
//			}
//			else if (cellContent.equalsIgnoreCase("URL")){
//				workbookHeaders.put("URL", new Integer(i));
//			}
//		}
////				System.out.println("ID " + workbookHeaders.get("ID") + " TITLE " + workbookHeaders.get("TITLE") + " ISSN " + workbookHeaders.get("ISSN"));
//	}
//	
//	public boolean snimiUFajl(String fileName)
//	{
//		try {
//		FileOutputStream fileOut = null;
//		if(fileName==null)
//		{
//			System.out.println("Ime fajla je null");
//			return false;
//		}
//		else
//			fileOut = new FileOutputStream(fileName);
//		
//		wbOut.write(fileOut);
//		fileOut.close();
//		}
//		catch (FileNotFoundException e2) {
//			// TODO Auto-generated catch block
//			System.out.println("Greska pri kreiranju excel fajla");
//			e2.printStackTrace();
//			return false;
//		}
//	    catch (IOException e1) {
//	    	System.out.println("Greska pri snimanju excel fajla");
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			return false;
//		}
//	    return true;
//	}
//	
//	public static int getCellValueAsInt(HSSFRow row, int celNUmber)
//	{
//		if(row.getCell(celNUmber)==null)
//		{
//			return -1;
//		}
//		return (int) row.getCell(celNUmber).getNumericCellValue();
//	}
//	
//	public static String getCellValueAsString(HSSFRow row, int celNUmber)
//	{
//		if(row.getCell(celNUmber)==null)
//		{
//			return null;
//		}
//		HSSFRichTextString richTextString = row.getCell(celNUmber).getRichStringCellValue();
//		
//		if(richTextString.getString().trim().equalsIgnoreCase(""))
//		{
//			return null;
//		}
//		
//		return richTextString.getString().trim();
//	}
//	
//	public static String getLanguageFromKobson(String jezik)
//	{
//		if(jezik==null)
//			return null;
//		
//		if(jezik.trim().equalsIgnoreCase(""))
//			return null;
//		
//		String tokens [] = jezik.split(";");
//
//		for(int i=0; i<tokens.length;i++)
//		{
//			String token = tokens[i].toLowerCase();			
//			String detektovaniJezik = ResolveComparedKobsonCris.getLanguageFromString(token);
//			
//			if(token.contains("text"))
//			{
//				return detektovaniJezik;
//			}
//		}
//		
//		return null;
//	}
//	
//	public static String getLanguageFromString(String jezik)
//	{
//		//detektujemo prvi jezik po nasim prioritetima
//		
//		if(jezik.contains("engleski"))
//			return "eng";
//		if(jezik.contains("english"))
//			return "eng";
//		if(jezik.contains("croatian"))
//			return "srp";
//		else if(jezik.contains("serbo-croatian"))
//			return "srp";
//		if(jezik.contains("serbian"))
//			return "srp";
//		if(jezik.contains("srpski"))
//			return "srp";
//		if(jezik.contains("yugoslavian"))
//			return "srp";
//		if(jezik.contains("french"))
//			return "fre";
//		if(jezik.contains("german"))
//			return "ger";
//		if(jezik.contains("russian"))
//			return "rus";	
//		if(jezik.contains("italian"))
//			return "ita";
//		if(jezik.contains("spanish"))
//			return "spa";
//		if(jezik.contains("hungarian"))
//			return "hun";
//		if(jezik.contains("slovenian"))
//			return "slo";
//
//		return "eng";
//	}
//	
//	public static void removeKovsonID(int kobsonIDXml)
//	{
//		
//		for (String idString : kobsonListIDs)
//		{
//			int kobsonIDDatabase = Integer.parseInt(idString);
//			
//			if(kobsonIDDatabase == kobsonIDXml)
//			{
//				kobsonListIDs.remove(idString);
//				break;
//			}
//		}
//	}
//	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		
//		Connection connCris = null;
//		Connection connKobson = null;
//		
//		String luceneIndexPath = "";
//		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.connection");
//		String hostname = rb.getString("hostname");
//		String port = rb.getString("port");
//		String schema = rb.getString("schema");
//		String connectionParameters = rb.getString("connectionParameters");
//		String username = rb.getString("username");
//		String password = rb.getString("password");
//		
//		importFolder = rb.getString("importFolder");
//		exportFolder = rb.getString("exportFolder");
//		
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			connCris = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
//				+ "/" + schema + connectionParameters, username, password);
//			connCris.setAutoCommit(false);
//		} catch (Exception e) 
//		{		
//			e.printStackTrace();
//		}
//		
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			connKobson = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
//				+ "/" + "kobson" + connectionParameters, username, password);
//			connKobson.setAutoCommit(false);
//		} catch (Exception e) 
//		{		
//			e.printStackTrace();
//		}
//		
//		luceneIndexPath = rb.getString("luceneIndex");
//		Retriever.setIndexPath(luceneIndexPath);
//		
//		System.out.println("Poceo");
//		
//		String  xlsDocument    = "/ISSNCompared.xls";
//		
//		ResolveComparedKobsonCris resolveComparedKobsonCris = new ResolveComparedKobsonCris();
//		resolveComparedKobsonCris.connCris = connCris;
//		resolveComparedKobsonCris.connKobson = connKobson;
//		
//		resolveComparedKobsonCris.clearCategoriesCrissDatabase(connCris);
//		resolveComparedKobsonCris.setCategoriesCrssDatabase(connCris, connKobson);
//		resolveComparedKobsonCris.loadCategoriesCrssDatabase(connCris);
//		
//		resolveComparedKobsonCris.importFromExcel(importFolder+xlsDocument);
//		resolveComparedKobsonCris.importFromExcelComared(true);
//		
//		System.out.println("****************************************************AAAAADDDDD*****************");
//		
////		resolveComparedKobsonCris.importFromExcel(importFolder+xlsDocument);
//		resolveComparedKobsonCris.kobsonListIDs = JouranlKobson.loadJournalsIDFromDatabase(connKobson);
//		resolveComparedKobsonCris.importFromExcelNotCompared();
//		
//		if(!resolveComparedKobsonCris.snimiUFajl(exportFolder+"/AddedFromKobsonToCris.xls"))
//		{
//			System.out.println("Greska pri snimanju dodatih casopisa");
//			System.exit(0);
//		}
//		
//		try {
//			if (connCris != null) {
//				connCris.close();
//			}
//			
//			if (connKobson != null) {
//				connKobson.close();
//			}
//		} catch (Exception e) {
//
//		}
//		
//		System.out.println("Obradio xml fajl");
//		
//	}
//}
