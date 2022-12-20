package rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;

public class SamovrednovanjeApp {
	
	private static String reportsDirectory;
	private static String subreportDir;
	private static HashMap params = new HashMap<String, String>();
	private static Connection conn = null;
	private static List<String> idsPMF = null;
	private static List<String> idsDMI = null;
	private static List<String> idsDF = null;
	private static List<String> idsDH = null;
	private static List<String> idsDB = null;
	private static List<String> idsDG = null;
	private static List<String> idsTF = null;
	
	private static String endYear = "2011";
	
	
	
	
	public static void main(String[] args) throws JRException, IOException{
		if(args.length!=8){
			System.out.println("samovrednovanje <luceneIndex> <hostname> <schema> <username> <password> <tabela61|tabela64|prilog64|trogodisnjiSCI|tabelaM24|all>" +
					" <pmf|dmi|dh|db|dg|df|all|tf> <jasper>");
			return;
		}
	
		
		
		String hostname = args[1];
		String port = "3306";
		String schema = args[2];
		String connectionParameters = "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
		String username = args[3];
		String password = args[4];
		String luceneIndex = args[0];
		
		
	 SamovrednovanjeUtils.hostname = hostname;
	 SamovrednovanjeUtils.port = port;
	 SamovrednovanjeUtils.schema = schema;
	 SamovrednovanjeUtils.connectionParameters = connectionParameters;
	 SamovrednovanjeUtils.username = username;
	 SamovrednovanjeUtils.password = password;
	 SamovrednovanjeUtils.luceneIndex = luceneIndex;
		
	 

		try {
			System.out.println("Connecting to database...");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
			conn.setAutoCommit(false);
			SamovrednovanjeUtils.setMyDataSource(hostname, port, schema, connectionParameters, username, password);
			System.out.println("Connection created.");		
		Retriever.setIndexPath(luceneIndex);		
		List<String> years = new ArrayList<String>();		
		String reportType = args[5];
		String organisation = args[6];
		
		List data = null;		
	
		reportsDirectory = args[7];
		subreportDir = reportsDirectory;
		System.out.println("Jasper reports directory set to "+reportsDirectory);
		String jasperFile = "";
		params = new HashMap<String, String>();			
		PersonDB personDB = new PersonDB();		
		params.put("SUBREPORT_DIR", subreportDir);		
		System.out.println("Obtaining id numbers for researchers.");
		
		if(organisation.equals("tf")){
			setIdsForOrganisationsTF(conn);
		}else{
			setIdsForOrganisations(conn);
		}
		
		if(organisation.equals("all")){		
			if(reportType.equals("all") || reportType.equals("tabela61")){
				generateTabela61("pmf");			
				generateTabela61("dmi");
				generateTabela61("df");
				generateTabela61("dh");
				generateTabela61("db");
				generateTabela61("dg");			
			}
			if(reportType.equals("all") || reportType.equals("tabela64")){
				generateTabela64("pmf");
				generateTabela64("dmi");
				generateTabela64("df");
				generateTabela64("dh");
				generateTabela64("db");
				generateTabela64("dg");
			}			
			if(reportType.equals("all") || reportType.equals("prilog64")){
				generatePrilog64("pmf");
				generatePrilog64("dmi");
				generatePrilog64("df");
				generatePrilog64("dh");
				generatePrilog64("db");
				generatePrilog64("dg");
			}if(reportType.equals("trogodisnjiSCI")){
				generateTrogodisnjiSCI("pmf");
				generateTrogodisnjiSCI("dmi");
				generateTrogodisnjiSCI("df");
				generateTrogodisnjiSCI("dh");
				generateTrogodisnjiSCI("db");
				generateTrogodisnjiSCI("dg");
			}
		}else{
			if(reportType.equals("all") || reportType.equals("tabela61")){
				generateTabela61(organisation);
			}
			if(reportType.equals("all") || reportType.equals("tabela64")){
				generateTabela64(organisation);
			}
			if(reportType.equals("all") || reportType.equals("prilog64")){
				generatePrilog64(organisation);
			}if(reportType.equals("trogodisnjiSCI")){
				generateTrogodisnjiSCI(organisation);
			}if(reportType.equals("tabelaM24")){
				generateTabelaForM24(organisation);
			}
		}		
		
		
		}catch (Exception e) {
			e.printStackTrace();		
		}
	}
	
	
	
	
	
	private static void generateTabela61(String organisation) throws JRException{
		System.out.println("Obtaining results for tabela61 for "+organisation+"...");		
		List<String> years = new ArrayList<String>();	
		
		years.add("2014");
		years.add("2013");
		years.add("2012");
		years.add("2011");
		years.add("2010");

		
		List<ResultForYearDTO> results = SamovrednovanjeUtils.getSCIResults(getIdsForOrganisation(organisation), years);
		ResultssDTO resultss =  new ResultssDTO();
		resultss.setResults(results);
		List<ResultssDTO> data = new ArrayList<ResultssDTO>();			
		data.add(resultss);
		String jasperFile = reportsDirectory + "tabela61"+".jasper";				
		params.put("organisation", getOrganisationLabel(organisation));
		printReport(data, "tabela61"+"-"+organisation, jasperFile);
	}
	
	private static void generateTabela64(String organisation) throws JRException{
		System.out.println("Obtaining results for tabela64 for "+organisation+"...");		
		
		List<String> years = new ArrayList<String>();			
		years = new ArrayList<String>();
//		for(int i=1950;i<2014;i++)
//			years.add(""+i);
		years.add(endYear);	
		List<String> idsTest = new ArrayList<String>();
		idsTest.add(getIdsForOrganisation(organisation).get(0));
		
		List<ResultForYearDTO> data = SamovrednovanjeUtils.getAllResults(getIdsForOrganisation(organisation), years);
		System.out.println("data.size="+data.size());
	
		
		/*List<ResultForTypeDTO> resForType = data.get(0).getResultsForType();
		
		
		/*
		// dodavanje urednika
		
		if(organisation.equals("pmf") || organisation.equals("dmi")){
			
			// Pilipovic 1
			ResultForTypeDTO resultForTypePilipovic1 = new ResultForTypeDTO();
			resultForTypePilipovic1.setResultType("M27"); 
			resultForTypePilipovic1.setResult("<html>Pilipović, S. (2011), Editorial Board, <i>Integral transform and special functions</i> (ISSN: 1065-2469)<html>");
			resForType.add(resultForTypePilipovic1);
			
			// Pilipovic 2
			ResultForTypeDTO resultForTypePilipovic2 = new ResultForTypeDTO();
			resultForTypePilipovic2.setResultType("M27"); 
			resultForTypePilipovic2.setResult("<html>Pilipović, S. (2011), Editorial Board, <i>Applicable Analysis and Discrete Mathematics</i> (ISSN: 1452-8630)<html>");
			resForType.add(resultForTypePilipovic2);
			
			
			// Mirjana Ivanovic
			ResultForTypeDTO resultForType = new ResultForTypeDTO();
			resultForType.setResultType("M28"); 
			resultForType.setResult("<html>Ivanović, M. (2011),  Editor- in-Chief,<i> Computer Science and Information Systems</i> (ISSN: 1820-0214)<html>");
			resForType.add(resultForType);
			
			// Pilipovic 3
			ResultForTypeDTO resultForTypePilipovic3 = new ResultForTypeDTO();
			resultForTypePilipovic3.setResultType("M28"); 
			resultForTypePilipovic3.setResult("<html>Pilipović, S. (2011),Editors,<i>Filomat</i> (ISSN: 0354-5180)<html>");
			resForType.add(resultForTypePilipovic3);
				
			// Pilipovic 4
			ResultForTypeDTO resultForTypePilipovic4 = new ResultForTypeDTO();
			resultForTypePilipovic4.setResultType("M55"); 
			resultForTypePilipovic4.setResult("<html>Pilipović, S. (2011), Editor- in-Chief,<i>Publications de l'Institut Mathématique</i> (ISSN: 0350-1302)<html>");
			resForType.add(resultForTypePilipovic4);
			
			// Kurilic 
			ResultForTypeDTO resultForTypeKurilic = new ResultForTypeDTO();
			resultForTypeKurilic.setResultType("M55"); 
			resultForTypeKurilic.setResult("<html>Pilipović, S. (2011), Editor- in-Chief,<i>Publications de l'Institut Mathématique</i> (ISSN: 0350-1302)<html>");
			resForType.add(resultForTypeKurilic);
			
			// Herceg 
			ResultForTypeDTO resultForTypeHerceg = new ResultForTypeDTO();
			resultForTypeHerceg.setResultType("M55"); 
			resultForTypeHerceg.setResult("<html>Herceg, D. (2011), Editor- in-Chief,<i>Novi Sad Journal of Mathematics</i> (ISSN: 1450-5444)<html>");
			resForType.add(resultForTypeHerceg);
		}
		
		
		if(organisation.equals("pmf") || organisation.equals("dg")){
			
			// Markovic1
			
			ResultForTypeDTO resultForTypeMarkovic = new ResultForTypeDTO();
			resultForTypeMarkovic.setResultType("M27"); 
			resultForTypeMarkovic.setResult("<html>Marković, S. (2011) Editors, <i>Quaternary International</i> (ISSN: 1040-6182) <html>");
			resForType.add(resultForTypeMarkovic);
			
			ResultForTypeDTO resultForTypeMarkovic2 = new ResultForTypeDTO();
			resultForTypeMarkovic2.setResultType("M27"); 
			resultForTypeMarkovic2.setResult("<html>Marković, S. (2011) Edited,<i>Quaternary International</i>, 240 (1–2)(ISSN: 1040-6182) <html>");
			resForType.add(resultForTypeMarkovic2);
			
			ResultForTypeDTO resultForTypeMarkovic3 = new ResultForTypeDTO();
			resultForTypeMarkovic3.setResultType("M28"); 
			resultForTypeMarkovic3.setResult("<html>Marković, S. (2011) Editors,<i>Acta Geographica Slovenica</i>, (ISSN: 1581-6613)<html>");
			resForType.add(resultForTypeMarkovic3);
			
			//Lazic
			ResultForTypeDTO resultForTypeLazic = new ResultForTypeDTO();
			resultForTypeLazic.setResultType("M55"); 
			resultForTypeLazic.setResult("<html>Lazić, L. (2011), Editor- in-Chief, <i>Geographica Pannonica</i> (ISSN: 0354-8724)<html>");
			resForType.add(resultForTypeLazic);
		
		}
		if(organisation.equals("pmf") || organisation.equals("db")){
			//Teodorovic
			ResultForTypeDTO resultForTypeTeodorovic = new ResultForTypeDTO();
			resultForTypeTeodorovic.setResultType("M28"); 
			resultForTypeTeodorovic.setResult("<html>Teodorović, I. (2011), Associate editor, <i>Water Science and Technology: Water Supply</i>(ISSN: 1606-9749) <html>");
			resForType.add(resultForTypeTeodorovic);
			
			//Teodorovic 2
			ResultForTypeDTO resultForTypeTeodorovic2 = new ResultForTypeDTO();
			resultForTypeTeodorovic2.setResultType("M28"); 
			resultForTypeTeodorovic2.setResult("<html>Teodorović, I. (2011), Associate editor, <i>Water Science and Technology</i> (ISSN: 0273-1223)<html>");
			resForType.add(resultForTypeTeodorovic2);
		}
		
		if(organisation.equals("pmf") || organisation.equals("dh")){
			//Tumbas
			ResultForTypeDTO resultForTypeTumbas = new ResultForTypeDTO();
			resultForTypeTumbas.setResultType("M28"); 
			resultForTypeTumbas.setResult("<html>Ivan�?ev-Tumbas, I. (2011), Editors, <i>Water Science and Technology</i>  (ISSN: 0273-1223) <html>");
			resForType.add(resultForTypeTumbas);
		}
		
		if(organisation.equals("pmf") || organisation.equals("df")){
			//bikit
			ResultForTypeDTO resultForTypeBikit = new ResultForTypeDTO();
			resultForTypeBikit.setResultType("M56"); 
			resultForTypeBikit.setResult("<html>Bikit, I. (2011), Editor-in-Chief, <i>Journal of Research in Physics</i> (ISSN: 1450-7404)<html>");
			resForType.add(resultForTypeBikit);			
		}		
		
		SamovrednovanjeUtils.prepareResults(data.get(0));
		*/
		
		params.put("organisation", getOrganisationLabel(organisation));		
		String jasperFile = reportsDirectory + "tabela64"+"_" + organisation + ".jasper";			
		if(jasperFile.startsWith("csv")){
			try {
				PrintWriter out = new PrintWriter(new File("E:/"+ reportsDirectory + "tabela64"+"_" + organisation + ".csv"), "UTF8");
				for (ResultForYearDTO resultForYearDTO : data) {
					out.print(resultForYearDTO.toString());
				}
				out.flush();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else 
			printReport(data, "tabela64"+"-"+organisation, jasperFile);		
	}
	
	private static void generateTabelaForM24(String organisation) throws JRException{
		System.out.println("Obtaining M24 results for "+organisation+"...");	
		List<String> years = new ArrayList<String>();	
		/*
		years.add("2013");
		years.add("2012");
		years.add("2011");
		years.add("2010");
		years.add("2009");
		*/

		
		List<ResultForTypeDTO> data = SamovrednovanjeUtils.getResultsForType("M24", null,null);
	
		
	
		params.put("organisation", getOrganisationLabel(organisation));
		params.put("resultType", "M24");
		String jasperFile = reportsDirectory + "spisakJednaKategorija"+".jasper";			
		printReport(data, "spisakJednaKategorija"+"-"+organisation, jasperFile);		
	}
	
	
	
	private static void generatePrilog64(String organisation) throws JRException{	
		System.out.println("Obtaining results for prilog64 for "+organisation+"...");
		List<MentorDTO> data = new ArrayList<MentorDTO>();
		String startYear = "2010";
		if(organisation.equals("pmf"))	{	
			data = SamovrednovanjeUtils.getMentors(getIdsForOrganisation(organisation), "2015", 10, 0);			
			startYear = "2006";
		}
		else if(organisation.equals("tf")){
			data = SamovrednovanjeUtils.getMentors(getIdsForOrganisation(organisation), "2015", 5, 0);
			startYear = "2010";
		}
		params.put("brojNastavnika", String.valueOf(SamovrednovanjeUtils.brojNastavnika));
		params.put("brojMentora", String.valueOf(SamovrednovanjeUtils.brojMentora));
		params.put("organisation", getOrganisationLabel(organisation));
		params.put("endYear", endYear);
		params.put("startYear", startYear);
		
		String jasperFile = reportsDirectory + "prilog64"+".jasper";			
		printReport(data, "prilog64"+"-"+organisation, jasperFile);

		
	}
	
	private static void generateTrogodisnjiSCI(String organisation) throws JRException{	
		System.out.println("Obtaining results for trogodisnjiSCI for "+organisation+"...");		
		
		List<ThreeYearsSCIResults> data = SamovrednovanjeUtils.getThreeYearsSCIResults(endYear, getIdsForOrganisation(organisation));
		params.put("organisation", getOrganisationLabel(organisation));
		params.put("year3", endYear);
		params.put("year2", String.valueOf(Integer.parseInt(endYear)-1));
		params.put("year1", String.valueOf(Integer.parseInt(endYear)-2));
	
		String jasperFile = reportsDirectory + "threeYearSCIResults"+".jasper";			
		printReport(data, "threeYearSCIResults"+"-"+organisation, jasperFile);
	}
	
	
	private static void printReport(List data, String reportType, String jasperFile) throws JRException{
			JasperPrint jasperPrint = null;	
			if(data.size()!=0) {
				JRDataSource dataSource = new JRBeanCollectionDataSource(data);
				jasperPrint = JasperFillManager.fillReport(jasperFile,params, dataSource);
			}else {
				JREmptyDataSource dataSource = new JREmptyDataSource();
				jasperPrint = JasperFillManager.fillReport(jasperFile,params, dataSource);
			}		 
			File outputFile = new File(reportType+".pdf");	
			System.out.println("Creating file "+outputFile.getAbsolutePath());
			
			JRPdfExporter pdfExporter = new JRPdfExporter();			
			
			
			pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE,outputFile);			
			pdfExporter.exportReport();
		/*
		
	 JRXlsxExporter exporter = new JRXlsxExporter();
  exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
  exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE, outputFile);
 */
		//pdfExporter.exportReport();
	}
	
	
	private static List<String> getIdsForOrganisationControlNumber(String orgControlNumber, String startDate, Connection conn1){
		PersonDB personDB = new PersonDB();
		List<Record> listAuthors = personDB.getInstitutionRecords(conn1, orgControlNumber, startDate);				
		List<String> ids = new ArrayList<String>();		
		for (Record record : listAuthors) {
			try {
				ids.add(record.getControlNumber());
			} catch (Exception e) {			
				e.printStackTrace();				
			}
		}
		return ids;
	}
	
	private static List<String> getIdsForOrganisationControlNumberNoDate(String orgControlNumber,  Connection conn1){
		PersonDB personDB = new PersonDB();
		List<Record> listAuthors = personDB.getInstitutionRecordsNoDate(conn1, orgControlNumber);				
		List<String> ids = new ArrayList<String>();		
		for (Record record : listAuthors) {
			try {
				ids.add(record.getControlNumber());
			} catch (Exception e) {			
				e.printStackTrace();				
			}
		}
		return ids;
	}
	
	/**
	 * 
	 * @param organisation - pmf, dmi, df, dh, dg, db
	 * @return
	 */
	
	private static List<String> obtainIdsForOrganisation(String organisation, String startDate, Connection conn1){
		String controlNumber =  getControlNumberForOrganisation(organisation);
		List<String> ids = getIdsForOrganisationControlNumber(controlNumber, startDate, conn1);
		return ids;
	}
	
	private static List<String> obtainIdsForOrganisationNodate(String organisation, Connection conn1){
		String controlNumber =  getControlNumberForOrganisation(organisation);
		List<String> ids = getIdsForOrganisationControlNumberNoDate(controlNumber, conn1);
		return ids;
	}
	
	
	public static List<String> getIdsForOrganisation(String organisation){
		if(organisation.equals("pmf")) return idsPMF;
		if(organisation.equals("dmi")) return idsDMI;
		if(organisation.equals("df")) return idsDF;
		if(organisation.equals("dh")) return idsDH;
		if(organisation.equals("db")) return idsDB;
		if(organisation.equals("dg")) return idsDG;		
		if(organisation.equals("tf")) return idsTF;
		return null;		
	}
	
	
	public static void setIdsForOrganisations(Connection conn1){
	 idsPMF = obtainIdsForOrganisation("pmf", "2022-01-01 00:00:00",conn1);
	 
	idsDMI = obtainIdsForOrganisation("dmi", "2022-01-01 00:00:00", conn1);
		idsDF = obtainIdsForOrganisation("df", "2022-01-01 00:00:00", conn1);
		idsDH = obtainIdsForOrganisation("dh", "2022-01-01 00:00:00", conn1);
		idsDB = obtainIdsForOrganisation("db", "2022-01-01 00:00:00", conn1);
		idsDG = obtainIdsForOrganisation("dg", "2022-01-01 00:00:00", conn1);
	}
	
	public static void setIdsForOrganisationsNoDate(Connection conn1){
		 idsPMF = obtainIdsForOrganisationNodate("pmf", conn1);
	} 
	
	public static void setIdsForOrganisationsTF(Connection conn1){
	 idsTF = obtainIdsForOrganisation("tf", "2014-01-01 00:00:00", conn1);
	 
	}
	
	
	
	
	/**
	 * 
	 * @param organisation - pmf, dmi, df, dh, dg, db
	 * @return
	 */
	
	private static String getControlNumberForOrganisation(String organisation){
		if(organisation.equals("pmf"))
			return "(BISIS)5929";
		if(organisation.equals("dmi"))
			return "(BISIS)6782";
		if(organisation.equals("dh"))
			return "(BISIS)6781";
		if(organisation.equals("dg"))
			return "(BISIS)6780";
		if(organisation.equals("df"))
			return "(BISIS)6779";
		if(organisation.equals("db"))
			return "(BISIS)6778";		
		if(organisation.equals("tf"))
			return "(BISIS)5933";
		
		return "";		
	}
	
	
	private static String getOrganisationLabel(String organisation){
		if(organisation.equals("pmf")) return "Природно-математичком факултeту";
		if(organisation.equals("dmi")) return "Департману за математику и информатику Природно-математичког факултeта";
		if(organisation.equals("df")) return "Департману за физику Природно-математичког факултeта";
		if(organisation.equals("dg")) return "Департману за географију Природно-математичког факултeта";
		if(organisation.equals("db")) return "Департману за биологију Природно-математичког факултeта";
		if(organisation.equals("dh")) return "Департману за хемију Природно-математичког факултeта";		
		if(organisation.equals("tf")) return "Технолошком факултету";		
		return "";
		
	}
	
	
	
	
	
	
	
	}


