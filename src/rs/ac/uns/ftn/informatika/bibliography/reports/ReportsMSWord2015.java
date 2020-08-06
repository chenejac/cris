package rs.ac.uns.ftn.informatika.bibliography.reports;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.SortedMap;

import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tr;

import com.gint.util.string.LatCyrUtils;

import rs.ac.uns.ftn.informatika.bibliography.reports.obrazci.DocxUtils;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.MentorDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.ResultForTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.ResultForYearDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.SamovrednovanjeApp;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.SamovrednovanjeUtils;

public class ReportsMSWord2015 {
	
	private static String lastYear = "2015";
	
	public static void generateTabela64(String organisation, Connection conn){
		System.out.println("Generating MS word report Tabela 6.4. for "+organisation+", last year: "+lastYear);
		System.out.println("Obtaining id numbers for researches...");
		if(organisation.equals("pmf"))
			SamovrednovanjeApp.setIdsForOrganisations(conn);
		else if(organisation.equals("tf"))
			SamovrednovanjeApp.setIdsForOrganisationsTF(conn);
		
		List<String> years = new ArrayList<String>();
		// poslednje tri godine
		years.add(lastYear);
		years.add(String.valueOf(Integer.parseInt(lastYear)-1));
		years.add(String.valueOf(Integer.parseInt(lastYear)-2));
		
		List<ResultForYearDTO> data = SamovrednovanjeUtils
		.getSCIResultsWord(SamovrednovanjeApp.getIdsForOrganisation(organisation), years);
		
		System.out.println("Creating word document");
		try{
			File f = new File("Tabela64-"+lastYear+organisation+".docx");			
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(f);
			P naslovP = DocxUtils.createParagraphWithText("Naslov tabele");
			wordMLPackage.getMainDocumentPart().addObject(naslovP);			
			
			ObjectFactory factory = Context.getWmlObjectFactory();			
			
			Tbl tabela = factory.createTbl();
			Tr tableHeader = factory.createTr();
			DocxUtils.addTableCell(tableHeader,"R.B.", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Zaglavlje", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "M", wordMLPackage);
			tabela.getContent().add(tableHeader);
			
			int rBr = 0;
			
			for(ResultForYearDTO resForYear:data){
				for(ResultForTypeDTO resForType:resForYear.getResultsForType()){
					System.out.println(rBr);
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
	
	
	public static void generateM99(String organisation, Connection conn, int numLastYear){
		System.out.println("Generating M99 word report  for "+organisation+", last year: "+lastYear);
		System.out.println("Obtaining id numbers for researches...");
		if(organisation.equals("pmf"))
			SamovrednovanjeApp.setIdsForOrganisations(conn);
		else if(organisation.equals("tf"))
			SamovrednovanjeApp.setIdsForOrganisationsTF(conn);
		
		List<String> years = new ArrayList<String>();
		
		
		for(int i=0;i<numLastYear; i++){
			String year = String.valueOf(Integer.valueOf(lastYear)-i);
			System.out.println(year);
			years.add(year);
		}
		
		List<ResultForYearDTO> data = SamovrednovanjeUtils
		.getM99Word(SamovrednovanjeApp.getIdsForOrganisation(organisation), years);
		
		//System.out.println("Creating word document");
		try{
			/*
			File f = new File("TabelaM99-"+lastYear+organisation+".docx");			
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
			P naslovP = DocxUtils.createParagraphWithText("Naslov tabele");
			wordMLPackage.getMainDocumentPart().addObject(naslovP);			
			
			ObjectFactory factory = Context.getWmlObjectFactory();			
			
			Tbl tabela = factory.createTbl();
			Tr tableHeader = factory.createTr();
			DocxUtils.addTableCell(tableHeader,"R.B.", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Zaglavlje", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "M", wordMLPackage);
			tabela.getContent().add(tableHeader);
			
			*/
			
			int rBr = 0;
			
			for(ResultForYearDTO resForYear:data){
				for(ResultForTypeDTO resForType:resForYear.getResultsForType()){
					System.out.println(rBr);
					/*
					Tr tableRow = factory.createTr();
					rBr++;
					DocxUtils.addTableCell(tableRow, ""+rBr+".", wordMLPackage);
					DocxUtils.addTableCell(tableRow, resForType.getResult() , wordMLPackage);
					*/
					System.out.println(rBr+resForType.getResult()+"----"+resForType.getResultType());
					//DocxUtils.addTableCell(tableRow, resForType.getResultType() , wordMLPackage);
					//tabela.getContent().add(tableRow);
				}
			}
			//DocxUtils.addBorders(tabela);
			//wordMLPackage.getMainDocumentPart().addObject(tabela);
			//wordMLPackage.save(f);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void generateTabela63(String organisation, Connection conn){
		System.out.println("Generating MS word report Tabela 6.3. for "+organisation+", last year: "+lastYear);
		System.out.println("Obtaining id numbers for researches...");
		if(organisation.equals("pmf"))
			SamovrednovanjeApp.setIdsForOrganisations(conn);
		else if(organisation.equals("tf"))
			SamovrednovanjeApp.setIdsForOrganisationsTF(conn);
		
		List<String> years = new ArrayList<String>();
		// poslednje tri godine
		years.add(lastYear);
		HashMap<String, Integer> mMap = new HashMap<String, Integer>();
		
		
		
		
		
		List<ResultForYearDTO> data = SamovrednovanjeUtils.getAllResults
			(SamovrednovanjeApp.getIdsForOrganisation(organisation), years);
		
		List<ResultForTypeDTO> resultsForType = data.get(0).getResultsForType();
		
		
		for(ResultForTypeDTO res:resultsForType){
			if(mMap.get(res.getResultType().substring(0, 2))==null){
				mMap.put(res.getResultType().substring(0,2), 1);
			}else{
				mMap.put(res.getResultType().substring(0,2), mMap.get(res.getResultType().substring(0,2))+1);
			}
		}
		
		System.out.println("Creating word document");
		try{
			File f = new File("Tabela63-"+lastYear+organisation+".docx");			
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
			P naslovP = DocxUtils.createParagraphWithText("Naslov tabele");
			wordMLPackage.getMainDocumentPart().addObject(naslovP);			
			
			ObjectFactory factory = Context.getWmlObjectFactory();			
			
			Tbl tabela = factory.createTbl();
			Tr tableHeader = factory.createTr();
			DocxUtils.addTableCell(tableHeader,"Редни број", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Резултат", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Према...", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Број резултата", wordMLPackage);
			
			tabela.getContent().add(tableHeader);
			
			int rBr = 0;
			
			for(String mMark: mMap.keySet()){
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
	
	
	public static void generateTabela67(String organisation, Connection conn){
		System.out.println("Generating MS word report Tabela 6.7. (mentors) for "+organisation+", last year: "+lastYear);
		System.out.println("Obtaining id numbers for researches...");
		if(organisation.equals("pmf"))
			SamovrednovanjeApp.setIdsForOrganisations(conn);
		else if(organisation.equals("tf"))
			SamovrednovanjeApp.setIdsForOrganisationsTF(conn);
		List<MentorDTO> mentors = SamovrednovanjeUtils
			.getMentors(SamovrednovanjeApp.getIdsForOrganisation(organisation), lastYear, 5, 5);
		System.out.println("Creating word document");
		try{
			File f = new File("Tabela67-"+lastYear+organisation+".docx");			
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
			P naslovP = DocxUtils.createParagraphWithText("Naslov tabele");
			wordMLPackage.getMainDocumentPart().addObject(naslovP);			
			
			ObjectFactory factory = Context.getWmlObjectFactory();			
			
			Tbl tabela = factory.createTbl();
			Tr tableHeader = factory.createTr();
			DocxUtils.addTableCell(tableHeader,"Редни број", wordMLPackage);
			DocxUtils.addTableCell(tableHeader,"Матични број", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Име и презиме", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Назив установе...", wordMLPackage);
			DocxUtils.addTableCell(tableHeader, "Број СЦИ/ССЦИ...", wordMLPackage);
			tabela.getContent().add(tableHeader);
			
			int rBr = 0;
			
			for(MentorDTO mentor:mentors){				
					System.out.println(rBr);
					Tr tableRow = factory.createTr();
					rBr++;
					DocxUtils.addTableCell(tableRow, ""+rBr+".", wordMLPackage);
					DocxUtils.addTableCell(tableRow, mentor.getJmbg() , wordMLPackage);
					DocxUtils.addTableCell(tableRow, LatCyrUtils.toCyrillic(mentor.getFirstName()+" "+mentor.getLastName()), wordMLPackage);
					DocxUtils.addTableCell(tableRow, "Технолошки факултет у Новом Саду", wordMLPackage);
					int total = mentor.getNumM21()+mentor.getNumM22()+mentor.getNumM23();
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
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
		
		
		
	}

}
