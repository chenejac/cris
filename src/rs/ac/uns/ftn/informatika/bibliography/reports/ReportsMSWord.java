package rs.ac.uns.ftn.informatika.bibliography.reports;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.docx4j.XmlUtils;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import rs.ac.uns.ftn.informatika.bibliography.reports.obrazci.DocxUtils;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.ResultForTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.ResultForYearDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.SamovrednovanjeApp;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.SamovrednovanjeUtils;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;

public class ReportsMSWord {
	

	
	public static void generateReportUNS(String organisation, String year, Connection conn){
		
		System.out.println("Generating MS word report for "+organisation+", year: "+year);
		Map<String, List<String>> resultsByCategorySCIUNS = new HashMap<String, List<String>>();
		Map<String, List<String>> resultsByCategoryZbirni = new HashMap<String, List<String>>();
		List<String> izostavljeni = new ArrayList<String>(); 
		try {
		
			
	
		List<String> years = new ArrayList<String>();
		years.add(year);
		System.out.println("Obtaining id numbers for researches...");
		if(organisation.equals("pmf"))
			SamovrednovanjeApp.setIdsForOrganisations(conn);
		else if(organisation.equals("tf"))
			SamovrednovanjeApp.setIdsForOrganisationsTF(conn);
		
		System.out.println("Obtaining results...");
		
		List<String> idsTest = new ArrayList<String>();
		idsTest.add(SamovrednovanjeApp.getIdsForOrganisation(organisation).get(0));
		
		List<ResultForYearDTO> data = SamovrednovanjeUtils
			.getAllResultsWord(idsTest, years);
		
		
		resultsByCategorySCIUNS.put("SCI", new ArrayList<String>());
		resultsByCategorySCIUNS.put("M24", new ArrayList<String>());
		for(ResultForTypeDTO res:data.get(0).getResultsForType()){
			System.out.println("res.getFirstAuthorId()="+res.getFirstAuthorId());
			if(res.getFirstAuthorId()!=null && SamovrednovanjeApp.getIdsForOrganisation(organisation).contains(res.getFirstAuthorId())){
				if(resultsByCategoryZbirni.get(res.getResultType())==null){
					resultsByCategoryZbirni.put(res.getResultType(), new ArrayList<String>());
				}
				resultsByCategoryZbirni.get(res.getResultType()).add(res.getResult());
				
				if(res.getResultType().equals("M21") || res.getResultType().equals("M22") || res.getResultType().equals("M23")){
					resultsByCategorySCIUNS.get("SCI").add(res.getResult());
				}else if(res.getResultType().equals("M24")){
					resultsByCategorySCIUNS.get("M24").add(res.getResult());
				}
			}else{
				izostavljeni.add(res.getResult());
				
			}
		}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	// kreiranje word-a
	



	
	System.out.println("Creating word document");
	try{
	File f = new File("Tabela"+year+organisation+".docx");
	
	
	WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(f);
	
	P sci = DocxUtils.createParagraphWithText("SCI");
	wordMLPackage.getMainDocumentPart().addObject(sci);
	
	
	ObjectFactory factory = Context.getWmlObjectFactory();
	Tbl tabelaSCI = factory.createTbl();
	Tr tableHeader = factory.createTr();
	DocxUtils.addTableCell(tableHeader, "Redni broj˜", wordMLPackage);
	DocxUtils.addTableCell(tableHeader, "Библиографска одредница научног рада", wordMLPackage);
	tabelaSCI.getContent().add(tableHeader);
	
	int i = 0;	
	List<String> sciResults = resultsByCategorySCIUNS.get("SCI");
	for(String str:sciResults){
		System.out.println("Add result "+str);
		Tr tableRow = factory.createTr();
		i++;
		DocxUtils.addTableCell(tableRow, ""+i+".", wordMLPackage);
		DocxUtils.addTableCell(tableRow,sciResults.get(i-1) , wordMLPackage);
		tabelaSCI.getContent().add(tableRow);
	}
	DocxUtils.addBorders(tabelaSCI);
	wordMLPackage.getMainDocumentPart().addObject(tabelaSCI);
	
	
 P M24 = DocxUtils.createParagraphWithText("M24");
	wordMLPackage.getMainDocumentPart().addObject(M24);
	
	
	
	Tbl tabelaM24 = factory.createTbl();
	Tr tableHeaderM24 = factory.createTr();
	DocxUtils.addTableCell(tableHeaderM24, "Redni broj", wordMLPackage);
	DocxUtils.addTableCell(tableHeaderM24, "Bibliografska odrednica naucnog rada", wordMLPackage);
	tabelaM24.getContent().add(tableHeaderM24);
	
	i = 0;	
	List<String> m24Results = resultsByCategorySCIUNS.get("M24");
	for(String str:m24Results){
		System.out.println("Add result "+str);
		Tr tableRow = factory.createTr();
		i++;
		DocxUtils.addTableCell(tableRow, ""+i+".", wordMLPackage);
		DocxUtils.addTableCell(tableRow,m24Results.get(i-1) , wordMLPackage);
		tabelaM24.getContent().add(tableRow);
	}
	DocxUtils.addBorders(tabelaM24);
	wordMLPackage.getMainDocumentPart().addObject(tabelaM24);
	
	P naslov2 = DocxUtils.createParagraphWithText("naslov2");
	wordMLPackage.getMainDocumentPart().addObject(naslov2);
	
	Tbl tabelaZbirni = factory.createTbl();
	Tr tableHeaderZbirni = factory.createTr();
	DocxUtils.addTableCell(tableHeaderZbirni, "Redni broj", wordMLPackage);
	DocxUtils.addTableCell(tableHeaderZbirni, "Kategorija casopisa", wordMLPackage);
	DocxUtils.addTableCell(tableHeaderZbirni, "Број научних радова објављених у часописима М категорије", wordMLPackage);
	tabelaZbirni.getContent().add(tableHeaderZbirni);
	
	i = 0;	
	
	for(String str:resultsByCategoryZbirni.keySet()){		
	
		Tr tableRow = factory.createTr();
		i++;
		DocxUtils.addTableCell(tableRow, ""+i, wordMLPackage);
		DocxUtils.addTableCell(tableRow, str, wordMLPackage);
		DocxUtils.addTableCell(tableRow, ""+resultsByCategoryZbirni.get(str).size(), wordMLPackage);
		tabelaZbirni.getContent().add(tableRow);
	}
	DocxUtils.addBorders(tabelaZbirni);
	wordMLPackage.getMainDocumentPart().addObject(tabelaZbirni); 
 wordMLPackage.save(new File("Tabela"+year+organisation+".docx"));
 
 // tabela izbaceni
 
 File f1 = new File("Tabela"+year+organisation+"-izbaceni.docx");	
	WordprocessingMLPackage wordMLPackage1 = WordprocessingMLPackage.load(f1);
	
	P izbaceniP = DocxUtils.createParagraphWithText("Izbaceni zbog prvog autora");
	wordMLPackage1.getMainDocumentPart().addObject(izbaceniP);
	
	
	ObjectFactory factory1 = Context.getWmlObjectFactory();
	
	Tbl tabela = factory1.createTbl();	
	i = 0;	
	for(String str:izostavljeni){
		System.out.println("Add result "+str);
		Tr tableRow = factory.createTr();
		i++;
		DocxUtils.addTableCell(tableRow, ""+i+".", wordMLPackage1);
		DocxUtils.addTableCell(tableRow, izostavljeni.get(i-1) , wordMLPackage1);
		tabela.getContent().add(tableRow);
	}
	DocxUtils.addBorders(tabela);
	wordMLPackage1.getMainDocumentPart().addObject(tabela); 
	wordMLPackage1.save(new File("Tabela"+year+organisation+"-izbaceni.docx"));
	
	}catch(Exception ex){
		ex.printStackTrace();
	}
	}
	
	public static void replaceTable(String[] placeholders, List<Map<String, String>> textToAdd,
			WordprocessingMLPackage template, String style) throws Docx4JException, JAXBException {
		List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);
		// 1. find the table
		Tbl tempTable = getTemplateTable(tables, placeholders[0]);
		List<Object> rows = getAllElementFromObject(tempTable, Tr.class);
 
		// first row is header, second row is content
		if (rows.size() == 2) {
			// this is our template row
			Tr templateRow = (Tr) rows.get(1);
 
			for (Map<String, String> replacements : textToAdd) {
				// 2 and 3 are done in this method
				addRowToTable(tempTable, templateRow, replacements, template, style);
			}
 
			// 4. remove the template row
			tempTable.getContent().remove(templateRow);
		}
	}
	
	private static Tbl getTemplateTable(List<Object> tables, String templateKey) throws Docx4JException, JAXBException {
		for (Iterator<Object> iterator = tables.iterator(); iterator.hasNext();) {
			Object tbl = iterator.next();
			List<?> textElements = getAllElementFromObject(tbl, Text.class);
			for (Object text : textElements) {
				Text textElement = (Text) text;
				if (textElement.getValue() != null && textElement.getValue().equals(templateKey))
					return (Tbl) tbl;
			}
		}
		return null;
	}
	
	
	private static void addRowToTable(Tbl reviewtable, Tr templateRow, Map<String, String> replacements, WordprocessingMLPackage template, String style) throws Docx4JException {
		Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
		List<Object> paragraphs = getAllElementFromObject(workingRow, P.class);
		 
		List<?> textElements = getAllElementFromObject(workingRow, Text.class);
		for (Object object : textElements) {
			Text text = (Text) object;
			String replacementValue = (String) replacements.get(text.getValue());
			if (replacementValue != null){
				P toReplace = null;
				for (Object p : paragraphs) {
					List<Object> texts = getAllElementFromObject(p, Text.class);
					for (Object t : texts) {
						Text content = (Text) t;
						if (content.getValue().equals(text.getValue())) {
							toReplace = (P) p;
							break;
						}
					}
				}
				XHTMLImporterImpl importer = new XHTMLImporterImpl(template);
				replacementValue = "<p" + ((style!=null)?(" style=\"" + style + "\">"):(">")) + replacementValue + "</p>";
				((ContentAccessor)toReplace.getParent()).getContent().addAll(importer.convert(replacementValue, null));
				((ContentAccessor)toReplace.getParent()).getContent().remove(toReplace);
//				text.setValue(replacementValue);
			}
		}
		reviewtable.getContent().add(workingRow);
	}
	private static void replaceParagraph(String placeholder, String textToAdd, WordprocessingMLPackage template, ContentAccessor addTo) {
		// 1. get the paragraph
		List<Object> paragraphs = getAllElementFromObject(template.getMainDocumentPart(), P.class);
 
		P toReplace = null;
		for (Object p : paragraphs) {
			List<Object> texts = getAllElementFromObject(p, Text.class);
			for (Object t : texts) {
				Text content = (Text) t;
				if (content.getValue().equals(placeholder)) {
					toReplace = (P) p;
					break;
				}
			}
		}
 
		// we now have the paragraph that contains our placeholder: toReplace
		// 2. split into seperate lines
		String as[] = StringUtils.splitPreserveAllTokens(textToAdd, '\n');
 
		for (int i = 0; i < as.length; i++) {
			String ptext = as[i];
 
			// 3. copy the found paragraph to keep styling correct
			P copy = (P) XmlUtils.deepCopy(toReplace);
 
			// replace the text elements from the copy
			List<?> texts = getAllElementFromObject(copy, Text.class);
			if (texts.size() > 0) {
				Text textToReplace = (Text) texts.get(0);
				textToReplace.setValue(ptext);
			}
 
			// add the paragraph to the document
			addTo.getContent().add(copy);
		}
 
		// 4. remove the original one
		((ContentAccessor)toReplace.getParent()).getContent().remove(toReplace);
 
	}
	
	private static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
		List<Object> result = new ArrayList<Object>();
		if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();
 
		if (obj.getClass().equals(toSearch))
			result.add(obj);
		else if (obj instanceof ContentAccessor) {
			List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child : children) {
				result.addAll(getAllElementFromObject(child, toSearch));
			}
 
		}
		return result;
	}
	
	
	public static void main(String[] args){
		if(args.length!=7){
			System.out.println("reports-word <luceneIndex> <hostname> <schema> <username> <password> <uns>" +
					" <pmf|dmi|dh|db|dg|df|all|tf> ");
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
			Connection conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
			conn.setAutoCommit(false);
			//SamovrednovanjeUtils.setMyDataSource(hostname, port, schema, connectionParameters, username, password);
			System.out.println("Connection created.");		
		Retriever.setIndexPath(luceneIndex);		
		List<String> years = new ArrayList<String>();		
		String reportType = args[5];
		String organisation = args[6];
		
		//ReportsMSWord2015.generateTabela67("tf", conn);
		//ReportsMSWord2015.generateTabela64("tf", conn);
		//generateReportUNS("pmf", "2014", conn);
		ReportsMSWord2015.generateM99("pmf", conn, 10);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
