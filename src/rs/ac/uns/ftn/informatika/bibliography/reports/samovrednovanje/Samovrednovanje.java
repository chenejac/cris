package rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Tr;

import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.dao.MyDataSource;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.reports.obrazci.DocxUtils;

import com.gint.util.string.LatCyrUtils;

import edu.emory.mathcs.backport.java.util.Collections;

public class Samovrednovanje {
	
	public static List<String> reportTypesToGenerate = new ArrayList<String>();
    public static String organisation; 
    public static String lastYear = "2019";		
	private static List<String> ids = null;
	public static int numberOfYearsForMentors = 5; 
	public static String generatedReportsDir; 
	
	private static Log log = LogFactory.getLog(Samovrednovanje.class.getName());
	
	
	public static boolean init(){
		log.info("Initialization...");
		
		log.info("Reports dir set to "+generatedReportsDir);
		setDefaultDataSource();			
	    try {
	    	log.info("Obtaining id numbers for researchers, organisation = "+organisation+" conn = "+dataSource.getConnection());
			setIdsForOrganisations(organisation, dataSource.getConnection());			
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}	
		return true;
	}
	
	
	public static void runReportGeneration(){
		if(reportTypesToGenerate.contains("Tabela64")){
			log.info("Generating Tabela64...");
			generateTabela64();
			log.info("Report Tabela64 completed.");
		}
		if(reportTypesToGenerate.contains("Tabela63")){
			log.info("Generating Tabela63...");
			generateTabela63();
			log.info("Report Tabela63 completed.");
		}
		if(reportTypesToGenerate.contains("Tabela67")){
			log.info("Generating Tabela67...");
			generateTabela67();
			log.info("Report Tabela67 completed.");
		}
		if(reportTypesToGenerate.contains("Tabela67saZvanjima")){
			log.info("Generating Tabela67saZvnjima...");
			generateTabela67saZvanjima();
			log.info("Report Tabela67saZvanjima completed.");
		}
		if(reportTypesToGenerate.contains("UNS")){
			log.info("Generating UNS reports...");
			generateReportUNS();
			log.info("Report UNS completed.");
		}
		
		if(reportTypesToGenerate.contains("UNSIns")){
			log.info("Generating UNS with institutions reports ...");
			generateReportUNSsaInstitucijama();
			log.info("Report UNS with institutions completed.");
		}
		
		if(reportTypesToGenerate.contains("UNSCol")){
			log.info("Generating UNS with colors reports ...");
			generateReportUNSsaBojama();
			log.info("Report UNS with colors completed.");
		}
		
		if(reportTypesToGenerate.contains("SamoBol")){
			log.info("Generating Samo with bold authors reports ...");
			generateReportSamosaBold();
			log.info("Report Samo with bold authors completed.");
		}
		
	}
	
	public static void setMyDataSource(String hostname, String port, String schema, String connectionParameters, String username, String password){
		dataSource = new MyDataSource(hostname, port, schema, connectionParameters, username, password);
	}
	
	public static void setDefaultDataSource(){
		dataSource = DataSourceFactory.getDataSource();
	}
	
	public static void setIdsForOrganisations(String organisation, Connection conn1){
		ids = obtainIdsForOrganisation(organisation, lastYear + "-01-01 00:00:00",conn1); 
	}
	
	private static List<String> obtainIdsForOrganisation(String organisation, String startDate, Connection conn1){
		List<String> controlNumber =  getControlNumberForOrganisation(organisation);
		List<String> ids = getIdsForOrganisationControlNumber(controlNumber, startDate, conn1);
		return ids;
	}
	
	private static List<String> getControlNumberForOrganisation(String organisation){
		List<String> retVal = new ArrayList<String>();
		if(organisation.equals("pmf"))
			retVal.add("(BISIS)5929");
		if(organisation.equals("pmf-dg")){
			retVal.add("(BISIS)6782");
			retVal.add("(BISIS)6781");
			retVal.add("(BISIS)6779");
			retVal.add("(BISIS)6778");
		}if(organisation.equals("dmi"))
			retVal.add("(BISIS)6782");
		if(organisation.equals("dh"))
			retVal.add("(BISIS)6781");
		if(organisation.equals("dg"))
			retVal.add("(BISIS)6780");
		if(organisation.equals("df"))
			retVal.add("(BISIS)6779");
		if(organisation.equals("db"))
			retVal.add("(BISIS)6778");		
		if(organisation.equals("tf"))
			retVal.add("(BISIS)5933");
		
		return retVal;		
	}
	
	private static List<String> getIdsForOrganisationControlNumber(List<String> orgControlNumbers, String startDate, Connection conn1){
		PersonDB personDB = new PersonDB();
		List<String> ids = new ArrayList<String>();		
		for (String orgControlNumber : orgControlNumbers) {
			List<Record> listAuthors = personDB.getInstitutionRecords(conn1, orgControlNumber, startDate);	
			for (Record record : listAuthors) {
				try {
					ids.add(record.getControlNumber());
				} catch (Exception e) {			
					e.printStackTrace();				
				}
			}
		}
		return ids;
	}
	
	public static void generateTabela64(){
		log.info("Generating MS word report Tabela 6.4. for "+organisation+", last year: "+lastYear);
		log.info("Obtaining id numbers for researches...");		
		
		List<String> years = new ArrayList<String>();
		// poslednje tri godine
		years.add(lastYear);
		years.add(String.valueOf(Integer.parseInt(lastYear)-1));
		years.add(String.valueOf(Integer.parseInt(lastYear)-2));
		
		List<ResultForYearDTO> data = SamovrednovanjeUtils
		.getSCIResultsWord(ids, years);
		
		log.info("Creating word document");
		try{
			File f = new File(generatedReportsDir+"Tabela64-"+lastYear+organisation+".docx");			
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
			P naslovP = DocxUtils.createParagraphWithText("Табела 6.4. Списак SCI/ ССЦИ-индексираних радова по годинама за трогодишњи период (" + (Integer.parseInt(lastYear)-2) + "-" + lastYear + ").");
			wordMLPackage.getMainDocumentPart().addObject(naslovP);			
			
			ObjectFactory factory = Context.getWmlObjectFactory();			
			
			Tbl tabela = factory.createTbl();
			Tr tableHeader = factory.createTr();
			DocxUtils.addTableCell(tableHeader,"Редни број", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Радови (на СЦИ/ ССЦИ листи)   у научним часописима са званичне листе ресорног министарства за науку у сладу са захтевима допунских стандарда за дато поље (аутори, назив рада, часопис, година)", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "M", wordMLPackage);
			tabela.getContent().add(tableHeader);
			
			int rBr = 0;
			
			for(ResultForYearDTO resForYear:data){
				for(ResultForTypeDTO resForType:resForYear.getResultsForType()){
					log.info(rBr);
					Tr tableRow = factory.createTr();
					rBr++;
					DocxUtils.addTableCell(tableRow, ""+rBr+".", wordMLPackage);
					DocxUtils.addTableCell(tableRow, resForType.getResult() , wordMLPackage);
					DocxUtils.addTableCell(tableRow, resForType.getResultType() , wordMLPackage);
					tabela.getContent().add(tableRow);
				}
			}
			DocxUtils.addBorders(tabela);
			wordMLPackage.getMainDocumentPart().addObject(tabela);
			wordMLPackage.save(f);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void generateTabela63(){
		log.info("Generating MS word report Tabela 6.3. for "+organisation+", last year: "+lastYear);
		log.info("Obtaining id numbers for researches...");
		
		List<String> years = new ArrayList<String>();
		// poslednje tri godine
		years.add(lastYear);
		HashMap<String, Integer> mMap = new HashMap<String, Integer>();	
		
		
		
		
		List<ResultForYearDTO> data = SamovrednovanjeUtils.getAllResults
			(ids, years);
		
		List<ResultForTypeDTO> resultsForType = data.get(0).getResultsForType();
		
		
		for(ResultForTypeDTO res:resultsForType){
			if(mMap.get(res.getResultType().substring(0, 2))==null){
				mMap.put(res.getResultType().substring(0,2), 1);
			}else{
				mMap.put(res.getResultType().substring(0,2), mMap.get(res.getResultType().substring(0,2))+1);
			}
		}
		
		log.info("Creating word document");
		try{
			File f = new File(generatedReportsDir+"Tabela63-"+lastYear+organisation+".docx");			
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
			P naslovP = DocxUtils.createParagraphWithText("Табела 6.3.  Збирни  преглед  научноистраживачких и уметничких резултата  у установи у календарској години (" + lastYear + ") према критеријумима Министарства");
			wordMLPackage.getMainDocumentPart().addObject(naslovP);			
			
			ObjectFactory factory = Context.getWmlObjectFactory();			
			
			Tbl tabela = factory.createTbl();
			Tr tableHeader = factory.createTr();
			DocxUtils.addTableCell(tableHeader,"Редни број", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Резултат (назив научног/уметничког резултата)", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "*Према Правилнику Министарства (М10, М20, М30, М40, М60, М70, М80, M90)", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Број резултата", wordMLPackage);
			
			tabela.getContent().add(tableHeader);
			
			int rBr = 0;
			
			List<String> list = new ArrayList(mMap.keySet());
			Collections.sort(list);
			for(String mMark: list){
				Tr tableRow = factory.createTr();
				rBr++;
				DocxUtils.addTableCell(tableRow, ""+rBr+".", wordMLPackage);
				DocxUtils.addTableCell(tableRow,  LatCyrUtils.toCyrillic(mNames.getProperty(mMark+"0")), wordMLPackage);
				DocxUtils.addTableCell(tableRow, mMark+"0", wordMLPackage);
				DocxUtils.addTableCell(tableRow, ""+mMap.get(mMark), wordMLPackage);
				tabela.getContent().add(tableRow);
				
			}
			DocxUtils.addBorders(tabela);
			wordMLPackage.getMainDocumentPart().addObject(tabela);
			wordMLPackage.save(f);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public static void generateTabela67(){
		log.info("Generating MS word report Tabela 6.7. (mentors) for "+organisation+", last year: "+lastYear);
		log.info("Obtaining id numbers for researches...");
		
		List<MentorDTO> mentors = SamovrednovanjeUtils
			.getMentors(ids, lastYear, numberOfYearsForMentors, 5);
		log.info("Creating word document");
		try{
			File f = new File(generatedReportsDir+"Tabela67-"+lastYear+organisation+".docx");			
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
			P naslovP = DocxUtils.createParagraphWithText("Табела 6.7. Листа ментора према тренутно важећим стандардима који се односи на испуњеност услова за менторе у оквиру образовно-научног, односно образовно-уметничког поља (период "+ (Integer.parseInt(lastYear) - numberOfYearsForMentors + 1) + "-" + lastYear+ ")");
			wordMLPackage.getMainDocumentPart().addObject(naslovP);			
			
			ObjectFactory factory = Context.getWmlObjectFactory();			
			
			Tbl tabela = factory.createTbl();
			Tr tableHeader = factory.createTr();
			DocxUtils.addTableCell(tableHeader,"Редни број", wordMLPackage);
			DocxUtils.addTableCell(tableHeader,"Матични број", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Име и презиме", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Назив установе у којој је ментор  запослен са пуним радним временом", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Број СЦИ/ ССЦИ индексираних радова", wordMLPackage);
			tabela.getContent().add(tableHeader);
			
			int rBr = 0;
			
			for(MentorDTO mentor:mentors){				
					Tr tableRow = factory.createTr();
					rBr++;
					DocxUtils.addTableCell(tableRow, ""+rBr+".", wordMLPackage);
					DocxUtils.addTableCell(tableRow, mentor.getJmbg() , wordMLPackage);
					DocxUtils.addTableCell(tableRow, LatCyrUtils.toCyrillic(mentor.getFirstName()+" "+mentor.getLastName()), wordMLPackage);
					DocxUtils.addTableCell(tableRow, (organisation.equals("tf"))?"Технолошки факултет у Новом Саду":"Природно математички факултет у Новом Саду", wordMLPackage);
					int total = mentor.getNumM21a()+ mentor.getNumM21()+mentor.getNumM22()+mentor.getNumM23();
					DocxUtils.addTableCell(tableRow, ""+total, wordMLPackage);
					tabela.getContent().add(tableRow);
				
			}
			
			DocxUtils.addBorders(tabela);
			wordMLPackage.getMainDocumentPart().addObject(tabela);
			wordMLPackage.save(f);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void generateTabela67saZvanjima(){
		log.info("Generating MS word report Tabela 6.7. (mentors) sa zvanjima for "+organisation+", last year: "+lastYear);
		log.info("Obtaining id numbers for researches...");
		//numberOfYearsForMentors = 40;
		//System.out.println(numberOfYearsForMentors);
		List<MentorDTO> mentors = SamovrednovanjeUtils
			.getMentors(ids, lastYear, numberOfYearsForMentors, 0);
		log.info("Creating word document");
		try{
			File f = new File(generatedReportsDir+"Tabela67saZvanjima-"+lastYear+organisation+".docx");			
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
			P naslovP = DocxUtils.createParagraphWithText("Продукција истраживача (период "+ (Integer.parseInt(lastYear) - numberOfYearsForMentors + 1) + "-" + lastYear+ ")");
			wordMLPackage.getMainDocumentPart().addObject(naslovP);			
			
			ObjectFactory factory = Context.getWmlObjectFactory();			
			
			Tbl tabela = factory.createTbl();
			Tr tableHeader = factory.createTr();
			DocxUtils.addTableCell(tableHeader,"Редни број", wordMLPackage);
			DocxUtils.addTableCell(tableHeader,"Матични број", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Име и презиме", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Звање", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Број СЦИ/ ССЦИ индексираних радова", wordMLPackage);
			tabela.getContent().add(tableHeader);
			
			int rBr = 0;
			
			for(MentorDTO mentor:mentors){				
					Tr tableRow = factory.createTr();
					rBr++;
					DocxUtils.addTableCell(tableRow, ""+rBr+".", wordMLPackage);
					DocxUtils.addTableCell(tableRow, mentor.getJmbg() , wordMLPackage);
					DocxUtils.addTableCell(tableRow, LatCyrUtils.toCyrillic(mentor.getFirstName()+" "+mentor.getLastName()), wordMLPackage);
					DocxUtils.addTableCell(tableRow, LatCyrUtils.toCyrillic(mentor.getCurrentPositionName()), wordMLPackage);
					int total = mentor.getNumM21a()+mentor.getNumM21()+mentor.getNumM22()+mentor.getNumM23();
					DocxUtils.addTableCell(tableRow, ""+total, wordMLPackage);
					tabela.getContent().add(tableRow);
				
			}
			
			DocxUtils.addBorders(tabela);
			wordMLPackage.getMainDocumentPart().addObject(tabela);
			wordMLPackage.save(f);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String getGeneratedReportsDir(){
		if(generatedReportsDir==null){
			ResourceBundle rbR = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.reports.reports");		
			generatedReportsDir = rbR.getString("generatedReportsDir");
		}
		return generatedReportsDir;
	}
	
	
	public static void generateReportUNS(){		
		Map<String, List<String>> resultsByCategorySCIUNS = new HashMap<String, List<String>>();
		Map<String, List<String>> institutionsByCategorySCIUNS = new HashMap<String, List<String>>();
		Map<String, List<String>> mcategoryByCategorySCIUNS = new HashMap<String, List<String>>();
		Map<String, List<String>> resultsByCategoryZbirni = new TreeMap<String, List<String>>();
		//List<String> izostavljeni = new ArrayList<String>(); 
		try {
		
			
	
		List<String> years = new ArrayList<String>();
		years.add(lastYear);
		
		
		List<ResultForYearDTO> data = SamovrednovanjeUtils.getAllResultsWord(ids, years);
		
		
		resultsByCategorySCIUNS.put("SCI", new ArrayList<String>());
		resultsByCategorySCIUNS.put("M24", new ArrayList<String>());
		institutionsByCategorySCIUNS.put("SCI", new ArrayList<String>());
		institutionsByCategorySCIUNS.put("M24", new ArrayList<String>());
		mcategoryByCategorySCIUNS.put("SCI", new ArrayList<String>());
		mcategoryByCategorySCIUNS.put("M24", new ArrayList<String>());
		for(ResultForTypeDTO res:data.get(0).getResultsForType()){			
			if(resultsByCategoryZbirni.get(res.getResultType())==null){
				resultsByCategoryZbirni.put(res.getResultType(), new ArrayList<String>());
			}
			resultsByCategoryZbirni.get(res.getResultType()).add(res.getResult());
			
			if(res.getResultType().equals("M21") || res.getResultType().equals("M21a") || res.getResultType().equals("M22") || res.getResultType().equals("M23")){
				resultsByCategorySCIUNS.get("SCI").add(res.getResult());
				institutionsByCategorySCIUNS.get("SCI").add(res.getAuthorsInstitutions());
				mcategoryByCategorySCIUNS.get("SCI").add(res.getResultType());
			}else if(res.getResultType().equals("M24")){
				resultsByCategorySCIUNS.get("M24").add(res.getResult());
				institutionsByCategorySCIUNS.get("M24").add(res.getAuthorsInstitutions());
				mcategoryByCategorySCIUNS.get("M24").add(res.getResultType());
			}
			
		}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	// kreiranje word-a
	



	
	log.info("Creating word document");
	try{
	
		
		File f = new File(generatedReportsDir+"TabelaUNS-"+lastYear+organisation+".docx");			
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		
		
	
		P naslov = DocxUtils.createParagraphWithText("1. СПИСАК НАУЧНИХ РАДОВА ОБЈАВЉЕНИХ У ЧАСОПИСИМА СА СЦИ/ССЦИ ЛИСТЕ И У ЧАСОПИСИМА М24 КАТЕГОРИЈЕ У КАЛЕНДАРСКОЈ " + lastYear + ". ГОДИНИ");
		wordMLPackage.getMainDocumentPart().addObject(naslov);
		
		P sci = DocxUtils.createParagraphWithText("СЦИ/ССЦИ");
		wordMLPackage.getMainDocumentPart().addObject(sci);
		
		
		ObjectFactory factory = Context.getWmlObjectFactory();
		Tbl tabelaSCI = factory.createTbl();
		Tr tableHeader = factory.createTr();
		DocxUtils.addTableCell(tableHeader, "Редни број", wordMLPackage);
		DocxUtils.addTableCell(tableHeader, "Институција", wordMLPackage);
		DocxUtils.addTableCell(tableHeader, "Библиографска одредница научног рада", wordMLPackage);
		DocxUtils.addTableCell(tableHeader, "Категорија (М)", wordMLPackage);
		tabelaSCI.getContent().add(tableHeader);
		
		int i = 0;	
		List<String> sciResults = resultsByCategorySCIUNS.get("SCI");
		List<String> sciInstitutions = institutionsByCategorySCIUNS.get("SCI");
		List<String> sciCategories = mcategoryByCategorySCIUNS.get("SCI");
		for(String str:sciResults){
//			log.info("Add result "+str);
			Tr tableRow = factory.createTr();
			i++;
			DocxUtils.addTableCell(tableRow, ""+i+".", wordMLPackage);
			DocxUtils.addTableCell(tableRow,sciInstitutions.get(i-1) , wordMLPackage);
			DocxUtils.addTableCell(tableRow,sciResults.get(i-1) , wordMLPackage);
			DocxUtils.addTableCell(tableRow,sciCategories.get(i-1) , wordMLPackage);
			tabelaSCI.getContent().add(tableRow);
		}
		DocxUtils.addBorders(tabelaSCI);
		wordMLPackage.getMainDocumentPart().addObject(tabelaSCI);
		
		
		P M24 = DocxUtils.createParagraphWithText("M24");
		wordMLPackage.getMainDocumentPart().addObject(M24);
		
		
		
		Tbl tabelaM24 = factory.createTbl();
		Tr tableHeaderM24 = factory.createTr();
		DocxUtils.addTableCell(tableHeaderM24, "Редни број", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderM24, "Институција", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderM24, "Библиографска одредница научног рада", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderM24, "Категорија (М)", wordMLPackage);
		tabelaM24.getContent().add(tableHeaderM24);
		
		i = 0;	
		List<String> m24Results = resultsByCategorySCIUNS.get("M24");
		List<String> m24Institutions = institutionsByCategorySCIUNS.get("M24");
		List<String> m24Categories = mcategoryByCategorySCIUNS.get("M24");
		for(String str:m24Results){
//			log.info("Add result "+str);
			Tr tableRow = factory.createTr();
			i++;
			DocxUtils.addTableCell(tableRow, ""+i+".", wordMLPackage);
			DocxUtils.addTableCell(tableRow,m24Institutions.get(i-1) , wordMLPackage);
			DocxUtils.addTableCell(tableRow,m24Results.get(i-1) , wordMLPackage);
			DocxUtils.addTableCell(tableRow,m24Categories.get(i-1) , wordMLPackage);
			tabelaM24.getContent().add(tableRow);
		}
		DocxUtils.addBorders(tabelaM24);
		wordMLPackage.getMainDocumentPart().addObject(tabelaM24);
		
		P naslov2 = DocxUtils.createParagraphWithText("2.	ЗБИРНИ ПРЕГЛЕД НАУЧНИХ РАДОВА ОБЈАВЉЕНИХ У КАЛЕНДАРСКОЈ " + lastYear + ". ГОДИНИ");
		wordMLPackage.getMainDocumentPart().addObject(naslov2);
	
		Tbl tabelaZbirni = factory.createTbl();
		Tr tableHeaderZbirni = factory.createTr();
		DocxUtils.addTableCell(tableHeaderZbirni, "Редни број", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderZbirni, "Категорија научних радова", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderZbirni, "Број научних радова објављених у М категорији", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderZbirni, "Библиографске одреднице научних радова", wordMLPackage);
		tabelaZbirni.getContent().add(tableHeaderZbirni);
		
		i = 0;	
		
		for(String str:resultsByCategoryZbirni.keySet()){		
		
			if(!str.equals("M99")){
				Tr tableRow = factory.createTr();
				i++;
				DocxUtils.addTableCell(tableRow, ""+i, wordMLPackage);
				DocxUtils.addTableCell(tableRow, str, wordMLPackage);
				DocxUtils.addTableCell(tableRow, ""+resultsByCategoryZbirni.get(str).size(), wordMLPackage);
				int j = 0;
				StringBuilder reference = new StringBuilder();
				List<String> list = resultsByCategoryZbirni.get(str);
				for (j=0; j < list.size(); j++) {
					reference.append(" " + (j+1) + ". " + list.get(j) + "<br/><br/>");
				}
				Tc column = factory.createTc();
				XHTMLImporterImpl importer = new XHTMLImporterImpl(wordMLPackage);
				column.getContent().addAll(importer.convert("<p>" + reference.toString().replace("&nbsp;", " ").replace("&", "&amp;") + "</p>", null));
//				DocxUtils.addTableCell(tableRow, reference.toString(), wordMLPackage);
				tableRow.getContent().add(column);
				tabelaZbirni.getContent().add(tableRow);
			}
		}
		DocxUtils.addBorders(tabelaZbirni);
		wordMLPackage.getMainDocumentPart().addObject(tabelaZbirni); 
		wordMLPackage.save(f);	
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	public static void generateReportUNSsaInstitucijama(){		
		Map<String, List<String>> resultsByCategorySCIUNS = new HashMap<String, List<String>>();
		Map<String, List<String>> institutionsByCategorySCIUNS = new HashMap<String, List<String>>();
		Map<String, List<String>> mcategoryByCategorySCIUNS = new HashMap<String, List<String>>();
		Map<String, List<String>> resultsByCategoryZbirni = new TreeMap<String, List<String>>();
		Map<String, List<String>> institutionsByCategoryZbirni = new TreeMap<String, List<String>>();
		//List<String> izostavljeni = new ArrayList<String>(); 
		try {
		
			
	
		List<String> years = new ArrayList<String>();
		years.add(lastYear);
		
		
		List<ResultForYearDTO> data = SamovrednovanjeUtils.getAllResultsWord(ids, years);
		
		
		resultsByCategorySCIUNS.put("SCI", new ArrayList<String>());
		resultsByCategorySCIUNS.put("M24", new ArrayList<String>());
		institutionsByCategorySCIUNS.put("SCI", new ArrayList<String>());
		institutionsByCategorySCIUNS.put("M24", new ArrayList<String>());
		mcategoryByCategorySCIUNS.put("SCI", new ArrayList<String>());
		mcategoryByCategorySCIUNS.put("M24", new ArrayList<String>());
		for(ResultForTypeDTO res:data.get(0).getResultsForType()){			
			if(resultsByCategoryZbirni.get(res.getResultType())==null){
				resultsByCategoryZbirni.put(res.getResultType(), new ArrayList<String>());
			}
			resultsByCategoryZbirni.get(res.getResultType()).add(res.getResult());
			if(institutionsByCategoryZbirni.get(res.getResultType())==null){
				institutionsByCategoryZbirni.put(res.getResultType(), new ArrayList<String>());
			}
			institutionsByCategoryZbirni.get(res.getResultType()).add(res.getAuthorsInstitutions());
			
			if(res.getResultType().equals("M21") || res.getResultType().equals("M21a") || res.getResultType().equals("M22") || res.getResultType().equals("M23")){
				resultsByCategorySCIUNS.get("SCI").add(res.getResult());
				institutionsByCategorySCIUNS.get("SCI").add(res.getAuthorsInstitutions());
				mcategoryByCategorySCIUNS.get("SCI").add(res.getResultType());
			}else if(res.getResultType().equals("M24")){
				resultsByCategorySCIUNS.get("M24").add(res.getResult());
				institutionsByCategorySCIUNS.get("M24").add(res.getAuthorsInstitutions());
				mcategoryByCategorySCIUNS.get("M24").add(res.getResultType());
			}
			
		}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	// kreiranje word-a
	



	
	log.info("Creating word document");
	try{
	
		
		File f = new File(generatedReportsDir+"TabelaUNSsaInstitucijama-"+lastYear+organisation+".docx");			
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		
		
	
		P naslov = DocxUtils.createParagraphWithText("1. СПИСАК НАУЧНИХ РАДОВА ОБЈАВЉЕНИХ У ЧАСОПИСИМА СА СЦИ/ССЦИ ЛИСТЕ И У ЧАСОПИСИМА М24 КАТЕГОРИЈЕ У КАЛЕНДАРСКОЈ " + lastYear + ". ГОДИНИ");
		wordMLPackage.getMainDocumentPart().addObject(naslov);
		
		P sci = DocxUtils.createParagraphWithText("СЦИ/ССЦИ");
		wordMLPackage.getMainDocumentPart().addObject(sci);
		
		
		ObjectFactory factory = Context.getWmlObjectFactory();
		Tbl tabelaSCI = factory.createTbl();
		Tr tableHeader = factory.createTr();
		DocxUtils.addTableCell(tableHeader, "Редни број", wordMLPackage);
		DocxUtils.addTableCell(tableHeader, "Институција", wordMLPackage);
		DocxUtils.addTableCell(tableHeader, "Библиографска одредница научног рада", wordMLPackage);
		DocxUtils.addTableCell(tableHeader, "Категорија (М)", wordMLPackage);
		tabelaSCI.getContent().add(tableHeader);
		
		int i = 0;	
		List<String> sciResults = resultsByCategorySCIUNS.get("SCI");
		List<String> sciInstitutions = institutionsByCategorySCIUNS.get("SCI");
		List<String> sciCategories = mcategoryByCategorySCIUNS.get("SCI");
		for(String str:sciResults){
//			log.info("Add result "+str);
			Tr tableRow = factory.createTr();
			i++;
			DocxUtils.addTableCell(tableRow, ""+i+".", wordMLPackage);
			DocxUtils.addTableCell(tableRow,sciInstitutions.get(i-1) , wordMLPackage);
			DocxUtils.addTableCell(tableRow,sciResults.get(i-1) , wordMLPackage);
			DocxUtils.addTableCell(tableRow,sciCategories.get(i-1) , wordMLPackage);
			tabelaSCI.getContent().add(tableRow);
		}
		DocxUtils.addBorders(tabelaSCI);
		wordMLPackage.getMainDocumentPart().addObject(tabelaSCI);
		
		
		P M24 = DocxUtils.createParagraphWithText("M24");
		wordMLPackage.getMainDocumentPart().addObject(M24);
		
		
		
		Tbl tabelaM24 = factory.createTbl();
		Tr tableHeaderM24 = factory.createTr();
		DocxUtils.addTableCell(tableHeaderM24, "Редни број", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderM24, "Институција", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderM24, "Библиографска одредница научног рада", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderM24, "Категорија (М)", wordMLPackage);
		tabelaM24.getContent().add(tableHeaderM24);
		
		i = 0;	
		List<String> m24Results = resultsByCategorySCIUNS.get("M24");
		List<String> m24Institutions = institutionsByCategorySCIUNS.get("M24");
		List<String> m24Categories = mcategoryByCategorySCIUNS.get("M24");
		for(String str:m24Results){
//			log.info("Add result "+str);
			Tr tableRow = factory.createTr();
			i++;
			DocxUtils.addTableCell(tableRow, ""+i+".", wordMLPackage);
			DocxUtils.addTableCell(tableRow,m24Institutions.get(i-1) , wordMLPackage);
			DocxUtils.addTableCell(tableRow,m24Results.get(i-1) , wordMLPackage);
			DocxUtils.addTableCell(tableRow,m24Categories.get(i-1) , wordMLPackage);
			tabelaM24.getContent().add(tableRow);
		}
		DocxUtils.addBorders(tabelaM24);
		wordMLPackage.getMainDocumentPart().addObject(tabelaM24);
		
		P naslov2 = DocxUtils.createParagraphWithText("2.	ЗБИРНИ ПРЕГЛЕД НАУЧНИХ РАДОВА ОБЈАВЉЕНИХ У КАЛЕНДАРСКОЈ " + lastYear + ". ГОДИНИ");
		wordMLPackage.getMainDocumentPart().addObject(naslov2);
	
		Tbl tabelaZbirni = factory.createTbl();
		Tr tableHeaderZbirni = factory.createTr();
		DocxUtils.addTableCell(tableHeaderZbirni, "Редни број", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderZbirni, "Категорија научних радова", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderZbirni, "Број научних радова објављених у М категорији", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderZbirni, "Институције", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderZbirni, "Библиографске одреднице научних радова", wordMLPackage);
		tabelaZbirni.getContent().add(tableHeaderZbirni);
		
		i = 0;	
		
		for(String str:resultsByCategoryZbirni.keySet()){		
		
			if(!str.equals("M99")){
				Tr tableRow = factory.createTr();
				i++;
				DocxUtils.addTableCell(tableRow, ""+i, wordMLPackage);
				DocxUtils.addTableCell(tableRow, str, wordMLPackage);
				DocxUtils.addTableCell(tableRow, ""+resultsByCategoryZbirni.get(str).size(), wordMLPackage);
				int j = 0;
				StringBuilder reference = new StringBuilder();
				StringBuilder institucije = new StringBuilder();
				List<String> list = resultsByCategoryZbirni.get(str);
				List<String> listInst = institutionsByCategoryZbirni.get(str);
				for (j=0; j < list.size(); j++) {
					reference.append(" " + (j+1) + ". " + list.get(j) + "<br/><br/>");
					institucije.append(" " + (j+1) + ". " + listInst.get(j) + "<br/><br/>");
				}
				Tc columnIns = factory.createTc();
				XHTMLImporterImpl importerIns = new XHTMLImporterImpl(wordMLPackage);
				columnIns.getContent().addAll(importerIns.convert("<p>" + institucije.toString().replace("&nbsp;", " ").replace("&", "&amp;") + "</p>", null));
//				DocxUtils.addTableCell(tableRow, reference.toString(), wordMLPackage);
				tableRow.getContent().add(columnIns);
				XHTMLImporterImpl importer = new XHTMLImporterImpl(wordMLPackage);
				Tc column = factory.createTc();
				column.getContent().addAll(importer.convert("<p>" + reference.toString().replace("&nbsp;", " ").replace("&", "&amp;") + "</p>", null));
//				DocxUtils.addTableCell(tableRow, reference.toString(), wordMLPackage);
				tableRow.getContent().add(column);
				tabelaZbirni.getContent().add(tableRow);
			}
		}
		DocxUtils.addBorders(tabelaZbirni);
		wordMLPackage.getMainDocumentPart().addObject(tabelaZbirni); 
		wordMLPackage.save(f);	
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	public static void generateReportUNSsaBojama(){		
		Map<String, List<String>> resultsByCategorySCIUNS = new HashMap<String, List<String>>();
		Map<String, List<String>> institutionsByCategorySCIUNS = new HashMap<String, List<String>>();
		Map<String, List<String>> mcategoryByCategorySCIUNS = new HashMap<String, List<String>>();
		Map<String, List<String>> resultsByCategoryZbirni = new TreeMap<String, List<String>>();
		Map<String, List<String>> institutionsByCategoryZbirni = new TreeMap<String, List<String>>();
		//List<String> izostavljeni = new ArrayList<String>(); 
		try {
		
			
	
		List<String> years = new ArrayList<String>();
		years.add(lastYear);
		
		
		List<ResultForYearDTO> data = SamovrednovanjeUtils.getAllResultsWord(ids, years);
		
		
		resultsByCategorySCIUNS.put("SCI", new ArrayList<String>());
		resultsByCategorySCIUNS.put("M24", new ArrayList<String>());
		institutionsByCategorySCIUNS.put("SCI", new ArrayList<String>());
		institutionsByCategorySCIUNS.put("M24", new ArrayList<String>());
		mcategoryByCategorySCIUNS.put("SCI", new ArrayList<String>());
		mcategoryByCategorySCIUNS.put("M24", new ArrayList<String>());
		for(ResultForTypeDTO res:data.get(0).getResultsForType()){			
			if(resultsByCategoryZbirni.get(res.getResultType())==null){
				resultsByCategoryZbirni.put(res.getResultType(), new ArrayList<String>());
			}
			resultsByCategoryZbirni.get(res.getResultType()).add(res.getResult());
			if(institutionsByCategoryZbirni.get(res.getResultType())==null){
				institutionsByCategoryZbirni.put(res.getResultType(), new ArrayList<String>());
			}
			institutionsByCategoryZbirni.get(res.getResultType()).add(res.getAuthorsInstitutions());
			
			if(res.getResultType().equals("M21") || res.getResultType().equals("M21a") || res.getResultType().equals("M22") || res.getResultType().equals("M23")){
				resultsByCategorySCIUNS.get("SCI").add(res.getResult());
				institutionsByCategorySCIUNS.get("SCI").add(res.getAuthorsInstitutions());
				mcategoryByCategorySCIUNS.get("SCI").add(res.getResultType());
			}else if(res.getResultType().equals("M24")){
				resultsByCategorySCIUNS.get("M24").add(res.getResult());
				institutionsByCategorySCIUNS.get("M24").add(res.getAuthorsInstitutions());
				mcategoryByCategorySCIUNS.get("M24").add(res.getResultType());
			}
			
		}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	// kreiranje word-a
	



	
	log.info("Creating word document");
	try{
	
		
		File f = new File(generatedReportsDir+"TabelaUNSsaBojama-"+lastYear+organisation+".docx");			
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		
		
	
		P naslov = DocxUtils.createParagraphWithText("1. СПИСАК НАУЧНИХ РАДОВА ОБЈАВЉЕНИХ У ЧАСОПИСИМА СА СЦИ/ССЦИ ЛИСТЕ И У ЧАСОПИСИМА М24 КАТЕГОРИЈЕ У КАЛЕНДАРСКОЈ " + lastYear + ". ГОДИНИ");
		wordMLPackage.getMainDocumentPart().addObject(naslov);
		
		P sci = DocxUtils.createParagraphWithText("СЦИ/ССЦИ");
		wordMLPackage.getMainDocumentPart().addObject(sci);
		
		
		ObjectFactory factory = Context.getWmlObjectFactory();
		Tbl tabelaSCI = factory.createTbl();
		Tr tableHeader = factory.createTr();
		DocxUtils.addTableCell(tableHeader, "Редни број", wordMLPackage);
//		DocxUtils.addTableCell(tableHeader, "Институција", wordMLPackage);
		DocxUtils.addTableCell(tableHeader, "Библиографска одредница научног рада", wordMLPackage);
		DocxUtils.addTableCell(tableHeader, "Категорија (М)", wordMLPackage);
		tabelaSCI.getContent().add(tableHeader);
		
		int i = 0;	
		List<String> sciResults = resultsByCategorySCIUNS.get("SCI");
		List<String> sciInstitutions = institutionsByCategorySCIUNS.get("SCI");
		List<String> sciCategories = mcategoryByCategorySCIUNS.get("SCI");
		for(String str:sciResults){
//			log.info("Add result "+str);
			Tr tableRow = factory.createTr();
			i++;
			DocxUtils.addTableCell(tableRow, ""+i+".", wordMLPackage);
//			DocxUtils.addTableCell(tableRow,sciInstitutions.get(i-1) , wordMLPackage);
			StringBuilder reference = new StringBuilder();
			String inst = sciInstitutions.get(i-1);
			if((inst != null) && ((inst.contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) && (inst.contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
				reference.append("<span style=\"background-color:red;\"> " + sciResults.get(i-1) + "</span><br/><br/>");
			} else if ((inst != null) && ((inst.equals("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (inst.equals("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
				reference.append("<span> " + sciResults.get(i-1) + "</span><br/><br/>");
			} else {
				reference.append("<span style=\"background-color:yellow;\">  " + sciResults.get(i-1) + "</span><br/><br/>");
			}
			XHTMLImporterImpl importer = new XHTMLImporterImpl(wordMLPackage);
			Tc column = factory.createTc();
			column.getContent().addAll(importer.convert("<p>" + reference.toString().replace("&nbsp;", " ").replace("&", "&amp;") + "</p>", null));
			tableRow.getContent().add(column);
			DocxUtils.addTableCell(tableRow,sciCategories.get(i-1) , wordMLPackage);
			tabelaSCI.getContent().add(tableRow);
		}
		DocxUtils.addBorders(tabelaSCI);
		wordMLPackage.getMainDocumentPart().addObject(tabelaSCI);
		
		
		P M24 = DocxUtils.createParagraphWithText("M24");
		wordMLPackage.getMainDocumentPart().addObject(M24);
		
		
		
		Tbl tabelaM24 = factory.createTbl();
		Tr tableHeaderM24 = factory.createTr();
		DocxUtils.addTableCell(tableHeaderM24, "Редни број", wordMLPackage);
//		DocxUtils.addTableCell(tableHeaderM24, "Институција", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderM24, "Библиографска одредница научног рада", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderM24, "Категорија (М)", wordMLPackage);
		tabelaM24.getContent().add(tableHeaderM24);
		
		i = 0;	
		List<String> m24Results = resultsByCategorySCIUNS.get("M24");
		List<String> m24Institutions = institutionsByCategorySCIUNS.get("M24");
		List<String> m24Categories = mcategoryByCategorySCIUNS.get("M24");
		for(String str:m24Results){
//			log.info("Add result "+str);
			Tr tableRow = factory.createTr();
			i++;
			DocxUtils.addTableCell(tableRow, ""+i+".", wordMLPackage);
//			DocxUtils.addTableCell(tableRow,m24Institutions.get(i-1) , wordMLPackage);
			StringBuilder reference = new StringBuilder();
			String inst = m24Institutions.get(i-1);
			if((inst != null) && ((inst.contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) && (inst.contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
				reference.append("<span style=\"background-color:red;\"> " + m24Results.get(i-1) + "</span><br/><br/>");
			} else if ((inst != null) && ((inst.equals("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (inst.equals("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
				reference.append("<span> " + m24Results.get(i-1) + "</span><br/><br/>");
			} else {
				reference.append("<span style=\"background-color:yellow;\">  " + m24Results.get(i-1) + "</span><br/><br/>");
			}
			XHTMLImporterImpl importer = new XHTMLImporterImpl(wordMLPackage);
			Tc column = factory.createTc();
			column.getContent().addAll(importer.convert("<p>" + reference.toString().replace("&nbsp;", " ").replace("&", "&amp;") + "</p>", null));
			tableRow.getContent().add(column);
			DocxUtils.addTableCell(tableRow,m24Categories.get(i-1) , wordMLPackage);
			tabelaM24.getContent().add(tableRow);
		}
		DocxUtils.addBorders(tabelaM24);
		wordMLPackage.getMainDocumentPart().addObject(tabelaM24);
		
		P naslov2 = DocxUtils.createParagraphWithText("2.	ЗБИРНИ ПРЕГЛЕД НАУЧНИХ РАДОВА ОБЈАВЉЕНИХ У КАЛЕНДАРСКОЈ " + lastYear + ". ГОДИНИ");
		wordMLPackage.getMainDocumentPart().addObject(naslov2);
	
		Tbl tabelaZbirni = factory.createTbl();
		Tr tableHeaderZbirni = factory.createTr();
		DocxUtils.addTableCell(tableHeaderZbirni, "Редни број", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderZbirni, "Категорија научних радова", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderZbirni, "Број научних радова објављених у М категорији", wordMLPackage);
//		DocxUtils.addTableCell(tableHeaderZbirni, "Институције", wordMLPackage);
		DocxUtils.addTableCell(tableHeaderZbirni, "Библиографске одреднице научних радова", wordMLPackage);
		tabelaZbirni.getContent().add(tableHeaderZbirni);
		
		i = 0;	
		
		for(String str:resultsByCategoryZbirni.keySet()){		
		
			if(!str.equals("M99")){
				Tr tableRow = factory.createTr();
				i++;
				DocxUtils.addTableCell(tableRow, ""+i, wordMLPackage);
				DocxUtils.addTableCell(tableRow, str, wordMLPackage);
				DocxUtils.addTableCell(tableRow, ""+resultsByCategoryZbirni.get(str).size(), wordMLPackage);
				int j = 0;
				StringBuilder reference = new StringBuilder();
				List<String> list = resultsByCategoryZbirni.get(str);
				List<String> listInst = institutionsByCategoryZbirni.get(str);
				for (j=0; j < list.size(); j++) {
					String inst = listInst.get(j);
					if((inst != null) && ((inst.contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) && (inst.contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
						reference.append("<span style=\"background-color:red;\"> " + (j+1) + ". " + list.get(j) + "</span><br/><br/>");
					} else if ((inst != null) && ((inst.equals("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (inst.equals("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
						reference.append("<span> " + (j+1) + ". " + list.get(j) + "</span><br/><br/>");
					} else {
						reference.append("<span style=\"background-color:yellow;\">  " + (j+1) + ". " + list.get(j) + "</span><br/><br/>");
					}
				}
				XHTMLImporterImpl importer = new XHTMLImporterImpl(wordMLPackage);
				Tc column = factory.createTc();
				column.getContent().addAll(importer.convert("<p>" + reference.toString().replace("&nbsp;", " ").replace("&", "&amp;") + "</p>", null));
				tableRow.getContent().add(column);
				tabelaZbirni.getContent().add(tableRow);
			}
		}
		DocxUtils.addBorders(tabelaZbirni);
		wordMLPackage.getMainDocumentPart().addObject(tabelaZbirni); 
		wordMLPackage.save(f);	
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	
	public static void generateReportSamosaBold(){		
		Map<String, List<String>> resultsByCategory = new TreeMap<String, List<String>>();
		Map<String, List<String>> institutionsByCategory = new HashMap<String, List<String>>();
		Map<String, List<String>> mcategoryByCategory = new HashMap<String, List<String>>();
		Map<String, List<String>> resultsByCategoryZbirni = new TreeMap<String, List<String>>();
		Map<String, List<String>> institutionsByCategoryZbirni = new TreeMap<String, List<String>>();
		//List<String> izostavljeni = new ArrayList<String>(); 
		try {		
	
		List<String> years = new ArrayList<String>();
		years.add(lastYear);
		years.add(String.valueOf(Integer.parseInt(lastYear)-1));
		years.add(String.valueOf(Integer.parseInt(lastYear)-2));
		years.add(String.valueOf(Integer.parseInt(lastYear)-3));
		
		
		List<ResultForYearDTO> data = SamovrednovanjeUtils.getAllResultsWord(ids, years, "(BISIS)5929");
		
		
		for (ResultForYearDTO resultForYearDTO : data) {
		
			for(ResultForTypeDTO res:resultForYearDTO.getResultsForType()){			
				if(resultsByCategoryZbirni.get(res.getResultType())==null){
					resultsByCategoryZbirni.put(res.getResultType(), new ArrayList<String>());
				}
				resultsByCategoryZbirni.get(res.getResultType()).add(res.getResult());
				
				if(institutionsByCategoryZbirni.get(res.getResultType())==null){
					institutionsByCategoryZbirni.put(res.getResultType(), new ArrayList<String>());
				}
				institutionsByCategoryZbirni.get(res.getResultType()).add(res.getAuthorsInstitutions());
				
				if(resultsByCategory.get(res.getResultType())==null){
					resultsByCategory.put(res.getResultType(), new ArrayList<String>());
				}
				resultsByCategory.get(res.getResultType()).add(res.getResult());
				
				if(institutionsByCategory.get(res.getResultType())==null){
					institutionsByCategory.put(res.getResultType(), new ArrayList<String>());
				}
				institutionsByCategory.get(res.getResultType()).add(res.getAuthorsInstitutions());
				
				if(mcategoryByCategory.get(res.getResultType())==null){
					mcategoryByCategory.put(res.getResultType(), new ArrayList<String>());
				}
				mcategoryByCategory.get(res.getResultType()).add(res.getResultType());						
			}
		}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	// kreiranje word-a
	



	
	log.info("Creating word document");
	try{
	
		
		File f = new File(generatedReportsDir+"TabelaSamosaBold-"+lastYear+organisation+".docx");			
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		
		
	
		P naslov = DocxUtils.createParagraphWithText("1. Списак научних радова за четворогодишњи период (" + (Integer.parseInt(lastYear)-3) + "-" + lastYear + ").");
		wordMLPackage.getMainDocumentPart().addObject(naslov);
		
		P sci = DocxUtils.createParagraphWithText("Природно математичке науке");
		wordMLPackage.getMainDocumentPart().addObject(sci);
		
		
		ObjectFactory factory = Context.getWmlObjectFactory();
		Tbl tabelaSCI = factory.createTbl();
		Tr tableHeader = factory.createTr();
		DocxUtils.addTableCell(tableHeader, "Редни број", wordMLPackage);
		DocxUtils.addTableCell(tableHeader, "Редни број ѕа М категорију", wordMLPackage);
		DocxUtils.addTableCell(tableHeader, "Библиографска одредница научног рада", wordMLPackage);
		DocxUtils.addTableCell(tableHeader, "Категорија (М)", wordMLPackage);
		tabelaSCI.getContent().add(tableHeader);
		
			
		int k = 0;
		for(String category:resultsByCategory.keySet()){
			List<String> results = resultsByCategory.get(category);
			List<String> institutions = institutionsByCategory.get(category);
			List<String> categories = mcategoryByCategory.get(category);
			int i = 0;
			for(String str:results){
	//			log.info("Add result "+str);
				Tr tableRow = factory.createTr();
				i++;
				k++;
				DocxUtils.addTableCell(tableRow, ""+k+".", wordMLPackage);
				DocxUtils.addTableCell(tableRow, ""+i+".", wordMLPackage);
	//			DocxUtils.addTableCell(tableRow,sciInstitutions.get(i-1) , wordMLPackage);
				StringBuilder reference = new StringBuilder();
				String inst = institutions.get(i-1);
//				if((inst != null) && ((inst.contains("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) && (inst.contains("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
//					reference.append("<span style=\"background-color:red;\"> " + results.get(i-1) + "</span><br/><br/>");
//				} else if ((inst != null) && ((inst.equals("Tehnološki fakultet Novi Sad, Univerzitet u Novom Sadu")) || (inst.equals("Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu")))){
					reference.append("<span> " + results.get(i-1) + "</span><br/><br/>");
//				} else {
//					reference.append("<span style=\"background-color:yellow;\">  " + results.get(i-1) + "</span><br/><br/>");
//				}
				XHTMLImporterImpl importer = new XHTMLImporterImpl(wordMLPackage);
				Tc column = factory.createTc();
				column.getContent().addAll(importer.convert("<p>" + reference.toString().replace("&nbsp;", " ").replace("&", "&amp;") + "</p>", null));
				tableRow.getContent().add(column);
				DocxUtils.addTableCell(tableRow,categories.get(i-1) , wordMLPackage);
				tabelaSCI.getContent().add(tableRow);
			}
		}
		DocxUtils.addBorders(tabelaSCI);
		wordMLPackage.getMainDocumentPart().addObject(tabelaSCI);
		
		
		wordMLPackage.save(f);	
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	/*public static void generateReportUNSCompleteList(){		
		Map<String, List<String>> resultsByCategorySCIUNS = new HashMap<String, List<String>>();
		Map<String, List<String>> institutionsByCategorySCIUNS = new HashMap<String, List<String>>();
		Map<String, List<String>> mcategoryByCategorySCIUNS = new HashMap<String, List<String>>();
 
		try {
		
			
	
		List<String> years = new ArrayList<String>();
		years.add(lastYear);
		
		
		List<ResultForYearDTO> data = SamovrednovanjeUtils.getAllResultsWord(ids, years);
		
		
		resultsByCategorySCIUNS.put("SCI", new ArrayList<String>());

		institutionsByCategorySCIUNS.put("SCI", new ArrayList<String>());

		mcategoryByCategorySCIUNS.put("SCI", new ArrayList<String>());

		for(ResultForTypeDTO res:data.get(0).getResultsForType()){			

			if(!res.getResultType().equals("M99")){
				resultsByCategorySCIUNS.get("SCI").add(res.getResult());
				institutionsByCategorySCIUNS.get("SCI").add(res.getAuthorsInstitutions());
				mcategoryByCategorySCIUNS.get("SCI").add(res.getResultType());
			}

			
		}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	



	
	log.info("Creating word document");
	try{
	
		
		File f = new File(generatedReportsDir+"TabelaUNSAll-"+lastYear+organisation+".docx");			
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		
		
	
		P naslov = DocxUtils.createParagraphWithText("1. СПИСАК СВИХ НАУЧНИХ РАДОВА У КАЛЕНДАРСКОЈ " + lastYear + ". ГОДИНИ");
		wordMLPackage.getMainDocumentPart().addObject(naslov);
		
		
		
		ObjectFactory factory = Context.getWmlObjectFactory();
		Tbl tabelaSCI = factory.createTbl();
		Tr tableHeader = factory.createTr();
		DocxUtils.addTableCell(tableHeader, "Редни број", wordMLPackage);
		DocxUtils.addTableCell(tableHeader, "Институција", wordMLPackage);
		DocxUtils.addTableCell(tableHeader, "Библиографска одредница научног рада", wordMLPackage);
		DocxUtils.addTableCell(tableHeader, "Категорија (М)", wordMLPackage);
		tabelaSCI.getContent().add(tableHeader);
		
		int i = 0;	
		List<String> sciResults = resultsByCategorySCIUNS.get("SCI");
		List<String> sciInstitutions = institutionsByCategorySCIUNS.get("SCI");
		List<String> sciCategories = mcategoryByCategorySCIUNS.get("SCI");
		for(String str:sciResults){
//			log.info("Add result "+str);
			Tr tableRow = factory.createTr();
			i++;
			DocxUtils.addTableCell(tableRow, ""+i+".", wordMLPackage);
			DocxUtils.addTableCell(tableRow,sciInstitutions.get(i-1) , wordMLPackage);
			DocxUtils.addTableCell(tableRow,sciResults.get(i-1) , wordMLPackage);
			DocxUtils.addTableCell(tableRow,sciCategories.get(i-1) , wordMLPackage);
			tabelaSCI.getContent().add(tableRow);
		}
		DocxUtils.addBorders(tabelaSCI);
		wordMLPackage.getMainDocumentPart().addObject(tabelaSCI);
		
		
		
		wordMLPackage.save(f);	
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}*/
	
	
	
    private static Properties mNames = new Properties();
	
	static{
		mNames.put("M10", "Monografije, monografske studije, tematski zbornici, leskikografske i kartografske publikacije međunarodnog značaja");
		mNames.put("M20", "Radovi objavljeni u naučnim časopisima međunarodnog značaja");
		mNames.put("M30", "Zbornici međunarodnih naučnih skupova");
		mNames.put("M40", "Nacionalne monografije tematski zbornici, leksikografske i kartografske publikacije nacionalnog značaja; naučni prevodi i kritička izdanja građe, bibliografske publikacije");
		mNames.put("M50", "Časopisi nacionalnog značaja");
		mNames.put("M60", "Zbornici skupova nacionalnog značaja");
		mNames.put("M70", "Magistarske i doktorske teze");
		mNames.put("M80", "Tehnička i razvojna rešenja");
		mNames.put("M90", "Patenti, autorske izložbe, testovi");
		
		ResourceBundle rbR = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.reports.reports");		
		generatedReportsDir = rbR.getString("generatedReportsDir");
		
		
		
	}
	
	private static DataSource dataSource;

}
