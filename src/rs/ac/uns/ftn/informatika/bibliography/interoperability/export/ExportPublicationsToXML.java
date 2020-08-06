package rs.ac.uns.ftn.informatika.bibliography.interoperability.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.opendope.SmartArt.dataHierarchy.SmartArtDataHierarchy.Texts.IdentifiedText;

import rs.ac.uns.ftn.informatika.bibliography.db.CommissionDB;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorPosition;
import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperMonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.SamovrednovanjeApp;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.SamovrednovanjeUtils;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;


/**
 * Export of publications from institution
 * to XML document
 * 
 * @author bdimic@uns.ac.rs
 *
 */

public class ExportPublicationsToXML {
	
	private static String indent = "\t";
	
	private static Connection conn;
	private static File outputFilePublications;
	private static File outputFileAuthors;
	private static String pubFileNameBase = "CRISpmf-pubsExport";
	private static String autFileNameBase = "CRISpmf-authExport";
	
	
	
	private static List<String> pubIdsProcessed = new ArrayList<String>();
	private static List<String> autorIdsProcessed = new ArrayList<String>();
	
	private static List<CommissionDTO> allCommissionList;
	private static List<RuleBookDTO> ruleBookList;
	private static RuleBookDTO ruleBook; 
	
	private static PrintWriter pubWriter;
	private static PrintWriter autWriter;
	
	private static List<String> authorsIds;
	private static List<String> authorsIdsDates;
	private static List<AuthorDTO> authorsAndCoauthors;
	
	private static List<String> authorsAndCoauthorsIds = new ArrayList<String>();
	
	
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	public static void writePublicationToXML(PublicationDTO pub, ResultMeasureDTO resultMeasure){
		
		try{
		
		String pubType = pub.getClass().getName().replace("rs.ac.uns.ftn.informatika.bibliography.dto.", "").replace("DTO", "").toLowerCase();		
		pubWriter.write("<publication type='"+pubType+"'>\n");
		pubWriter.write(indent+"<id>"+pub.getControlNumber()+"</id>\n");
		pubWriter.write(indent+"<publicationYear>"+pub.getPublicationYear()+"</publicationYear>\n");
		pubWriter.write(indent+"<authors>\n");
		List<AuthorDTO> allAuthors = pub.getAllAuthors();
		for(AuthorDTO aut:allAuthors){
			pubWriter.write(indent+"\t"+"<authorId>"+aut.getControlNumber()+"</authorId>\n");
		}
		pubWriter.write(indent+"</authors>\n");
		if(pub instanceof PaperJournalDTO){
			PaperJournalDTO pubJournal = (PaperJournalDTO)pub;
			if(pubJournal.getTitle().getContent()!=null)
				pubWriter.write(indent+"<title lang='"+pubJournal.getTitle().getLanguage()+"'>"+pubJournal.getTitle()
						.getContent().replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\"", "&quot; ")+"</title>\n");
			pubWriter.write(indent+"<startPage>"+pubJournal.getStartPage()+"</startPage>\n");
			pubWriter.write(indent+"<endPage>"+pubJournal.getEndPage()+"</endPage>\n");
			if(pubJournal.getKeywords()!=null && pubJournal.getKeywords().getContent()!=null)
				pubWriter.write(indent+"<keywords lang='"+pubJournal.getKeywords().getLanguage()+"'>"+pubJournal.getKeywords().getContent()+"</keywords>\n");
			if(pubJournal.getJournal()!=null){
				pubWriter.write(indent+"<journal>\n");		
				pubWriter.write(indent+"\t<journalId>"+pubJournal.getJournal().getControlNumber()+"</journalId>\n");
				if(pubJournal.getJournal().getName().getContent()!=null)
					pubWriter.write(indent+"\t<journalTitle lang='"+pubJournal.getJournal().getName().getLanguage()+"'>"
							+pubJournal.getJournal().getName().getContent().replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\"", "&quot; ")+"</journalTitle>\n");
				if(pubJournal.getJournal().getNameAbbreviation()!=null && pubJournal.getJournal().getNameAbbreviation().getContent()!=null)
					pubWriter.write(indent+"\t<journalAbbTitle lang='"+pubJournal.getJournal().getNameAbbreviation().getLanguage()+"'>"+pubJournal.getJournal().getNameAbbreviation().getContent().trim()
							.replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\"", "&quot; ").trim()
							+"\t</journalAbbTitle>\n");
				if(pubJournal.getJournal().getKeywords()!=null && pubJournal.getJournal().getKeywords().getContent()!=null)
					pubWriter.write(indent+"\t<journalKeywords lang='"+pubJournal.getJournal().getKeywords().getLanguage()+"'>"
							+pubJournal.getJournal().getKeywords().getContent()+"</journalKeywords>\n");			
				pubWriter.write(indent+"</journal>\n");
			}
			
		}if(pub instanceof MonographDTO){
			MonographDTO pubMonograph = (MonographDTO) pub;
			if(pubMonograph.getTitle().getContent()!=null)
				pubWriter.write(indent+"<title lang='"+pubMonograph.getTitle().getLanguage()+"'>"+pubMonograph.getTitle()
						.getContent().replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\"", "&quot; ").trim()+"</title>\n");
			if(pubMonograph.getNumberOfPages()!=null)
				pubWriter.write(indent+"<numberOfPages>"+pubMonograph.getNumberOfPages()+"</numberOfPages>\n");			
			if(pubMonograph.getSubtitle()!=null && pubMonograph.getSubtitle().getContent()!=null && !pubMonograph.getSubtitle().getContent().equals(""))
				pubWriter.write(indent+"<subtitle lang='"+pubMonograph.getSubtitle().getLanguage()+"'>"+pubMonograph.getSubtitle().getContent().trim()+"</subtitle>\n");			
			if(pubMonograph.getKeywords()!=null && pubMonograph.getKeywords().getContent()!=null)
				pubWriter.write(indent+"<keywords lang='"+pubMonograph.getKeywords().getLanguage()+"'>"
						+pubMonograph.getKeywords().getContent().trim()+"</keywords>\n");
			// TODO evaluation
			
			// TODO mozda publisher
			
		}if(pub instanceof PaperMonographDTO){
			PaperMonographDTO pubPaperMonograph = (PaperMonographDTO) pub;
			if(pubPaperMonograph.getTitle().getContent()!=null){
			pubWriter.write(indent+"<title lang='"+pubPaperMonograph.getTitle().getLanguage()+"'>"
					+pubPaperMonograph.getTitle().getContent().replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\"", "&quot; ").trim()+"</title>\n");
			}
			if(pubPaperMonograph.getStartPage()!=null)
				pubWriter.write(indent+"<startPage>"+pubPaperMonograph.getStartPage().trim()+"</startPage>\n");
			if(pubPaperMonograph.getEndPage()!=null)
				pubWriter.write(indent+"<endPage>"+pubPaperMonograph.getEndPage().trim()+"</endPage>\n");
			if(pubPaperMonograph.getKeywords()!=null && pubPaperMonograph.getKeywords().getContent()!=null)
				pubWriter.write(indent+"<keywords lang='"+pubPaperMonograph.getKeywords().getLanguage()+"'>"
						+pubPaperMonograph.getKeywords().getContent()+"</keywords>\n");
			pubWriter.write(indent+"<monograph>\n");
			pubWriter.write(indent+"\t<monographId>"+pubPaperMonograph.getMonograph().getControlNumber()+"</monographId>\n");
			pubWriter.write(indent+"\t<monographTitle lang='"+pubPaperMonograph.getMonograph().getTitle().getLanguage()+"'>"+pubPaperMonograph.getMonograph().getTitle().getContent()+"</monographTitle>\n");
			pubWriter.write(indent+"\t<numberOfPages>"+pubPaperMonograph.getMonograph().getNumberOfPages()+"</numberOfPages>\n");
			if(pubPaperMonograph.getMonograph().getEditors().size()>0){
				pubWriter.write(indent+"\t<editors>");
				for(AuthorDTO aut: pubPaperMonograph.getMonograph().getEditors()){
					pubWriter.write(indent+"\t\t<editorId>"+aut.getControlNumber()+"</editorId>\n");
				}
				pubWriter.write(indent+"\t</editors>");
			}
			if(pubPaperMonograph.getMonograph().getSubtitle()!=null && pubPaperMonograph.getMonograph().getSubtitle().getContent()!=null 
					&& !pubPaperMonograph.getMonograph().getSubtitle().getContent().equals(""))
				pubWriter.write(indent+"\t<monographSubtitle>"+pubPaperMonograph.getMonograph().getSubtitle()+"</monographSubtitle>\n");			
			if(pubPaperMonograph.getMonograph().getKeywords()!=null && pubPaperMonograph.getMonograph().getKeywords().getContent()!=null)
				pubWriter.write(indent+"\t<monographKeywords lang='"+pubPaperMonograph.getMonograph().getKeywords().getLanguage()+"'>"
						+pubPaperMonograph.getMonograph().getKeywords().getContent()+"</monographKeywords>\n");
			
			pubWriter.write(indent+"</monograph>\n");		
			// TODO evaluation
		}if(pub instanceof PaperProceedingsDTO){
			PaperProceedingsDTO pubPaperProceedings = (PaperProceedingsDTO)pub;
			if(pubPaperProceedings.getTitle().getContent()!=null){
			pubWriter.write(indent+"<title lang='"+pubPaperProceedings.getTitle().getLanguage()+"'>"
					+pubPaperProceedings.getTitle()
					.getContent().replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\"", "&quot; ")+"</title>\n");	
			}
			pubWriter.write(indent+"<startPage>"+pubPaperProceedings.getStartPage()+"</startPage>\n");
			pubWriter.write(indent+"<endPage>"+pubPaperProceedings.getEndPage()+"</endPage>\n");
			if(pubPaperProceedings.getKeywords()!=null && pubPaperProceedings.getKeywords().getContent()!=null)
				pubWriter.write(indent+"<keywords lang='"+pubPaperProceedings.getKeywords().getLanguage()+"'>"
						+pubPaperProceedings.getKeywords().getContent()+"</keywords>\n");
			pubWriter.write(indent+"<proceedings>\n");
			pubWriter.write(indent+"\t<proceedingsId>"+pubPaperProceedings.getProceedings().getControlNumber()+"</proceedingsId>\n");
			if(pubPaperProceedings.getProceedings().getTitle().getContent()!=null)
				pubWriter.write(indent+"\t<proceedingsTitle lang='"+pubPaperProceedings.getProceedings().getTitle().getLanguage()+"'>"
						+pubPaperProceedings.getProceedings().getTitle().getContent()+"</proceedingsTitle>\n");
			if(pubPaperProceedings.getProceedings().getNumberOfPages()!=null)
				pubWriter.write(indent+"\t<numberOfPages>"+pubPaperProceedings.getProceedings().getNumberOfPages()+"</numberOfPages>\n");
			if(pubPaperProceedings.getProceedings().getEditors().size()>0){
				pubWriter.write(indent+"\t<editors>\n");
				for(AuthorDTO aut: pubPaperProceedings.getProceedings().getEditors()){
					pubWriter.write(indent+"\t\t<editorId>"+aut.getControlNumber()+"</editorId>\n");
				}
				pubWriter.write(indent+"\t</editors>\n");	
			}
			if(pubPaperProceedings.getProceedings().getKeywords()!=null && pubPaperProceedings.getProceedings().getKeywords().getContent()!=null)
				pubWriter.write(indent+"\t<proceedingsKeywords lang='"+pubPaperProceedings.getProceedings().getKeywords().getLanguage()+"'>"
						+pubPaperProceedings.getProceedings().getKeywords().getContent()+"</proceedingsKeywords>\n");			
			pubWriter.write(indent+"\t<conference>\n");	
			if(pubPaperProceedings.getProceedings().getConference().getName().getContent()!=null)
				pubWriter.write(indent+"\t\t<name lang='"+pubPaperProceedings.getProceedings().getConference().getName().getLanguage()+"'>"
						+pubPaperProceedings.getProceedings().getConference().getName()
					.getContent().replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\"", "&quot; ")+"</name>\n");
			if(pubPaperProceedings.getProceedings().getConference().getPlace()!=null)
				pubWriter.write(indent+"\t\t<place>"+pubPaperProceedings.getProceedings().getConference().getPlace()+"</place>\n");
			if(pubPaperProceedings.getProceedings().getConference().getPeriod()!=null)
				pubWriter.write(indent+"\t\t<period>"+pubPaperProceedings.getProceedings().getConference().getPeriod()+"</period>\n");
			if(pubPaperProceedings.getProceedings().getConference().getYear()!=null)
				pubWriter.write(indent+"\t\t<year>"+pubPaperProceedings.getProceedings().getConference().getYear()+"</year>\n");
			if(pubPaperProceedings.getProceedings().getConference().getState()!=null)
				pubWriter.write(indent+"\t\t<state>"+pubPaperProceedings.getProceedings().getConference().getState()+"</state>\n");
			if(pubPaperProceedings.getProceedings().getConference().getKeywords()!=null && pubPaperProceedings.getProceedings().getConference().getKeywords().getContent()!=null)
				pubWriter.write(indent+"\t\t<conferenceKeywords lang='"+pubPaperProceedings.getProceedings().getConference().getKeywords().getLanguage()+"'>"
						+pubPaperProceedings.getProceedings().getConference().getKeywords().getContent()+"</conferenceKeywords>\n");		
			pubWriter.write(indent+"\t</conference>\n");			
			pubWriter.write(indent+"</proceedings>\n");
			
		
		}				// TODO evaluation
		}catch(Exception e){
			e.printStackTrace();
		}
		pubWriter.write(getResultMeasureXML(resultMeasure));
		pubWriter.write("</publication>\n");
	}
	
	public static void writeAuthorToXML(AuthorDTO author){
		System.out.println("Ispis autora "+author.getControlNumber());
		boolean orgEmployee = authorsIds.contains(author.getControlNumber());
		boolean orgEmployee2014 = authorsIdsDates.contains(author.getControlNumber());
		autWriter.write("<author orgEmployee='"+orgEmployee+"' orgEmployee2014='"+orgEmployee2014+"'>\n");
		autWriter.write(indent+"<controlNumber>"+author.getControlNumber()+"</controlNumber>\n");
		if(author.getName()!=null){
			autWriter.write(indent+"<authorName>\n");
			if(author.getName().getLastname()!=null)
				autWriter.write(indent+"\t<lastName>"+author.getName().getLastname()+"</lastName>\n");
			if(author.getName().getFirstname()!=null)
				autWriter.write(indent+"\t<firstName>"+author.getName().getFirstname()+"</firstName>\n");
			if(author.getName().getOtherName()!=null)
				autWriter.write(indent+"\t<middleName>"+author.getName().getOtherName()+"</middleName>\n");
			autWriter.write(indent+"</authorName>\n");					
		}
		if(author.getOtherFormatNames()!=null && author.getOtherFormatNames().size()>0){
			autWriter.write(indent+"<otherFormatNames>\n");
			for(AuthorNameDTO name:author.getOtherFormatNames()){
				autWriter.write(indent+"\t<name>\n");
				if(name.getLastname()!=null)
					autWriter.write(indent+"\t\t<lastName>"+name.getLastname()+"</lastName>\n");
				if(name.getFirstname()!=null)
					autWriter.write(indent+"\t\t<firstName>"+name.getFirstname()+"</firstName>\n");
				if(name.getOtherName()!=null)
					autWriter.write(indent+"\t\t<middleName>"+name.getOtherName()+"</middleName>\n");
				autWriter.write(indent+"\t</name>\n");					
			}
			autWriter.write(indent+"</otherFormatNames>\n");			
		}
		if(author.getDateOfBirth()!=null){
			autWriter.write(indent+"<dateOfBirth>"+sdf.format(author.getDateOfBirth().getTime())+"</dateOfBirth>\n");
		}
		if(author.getTitleInstitution()!=null){
			autWriter.write(indent+"<titleInstitution>\n");
			if(author.getTitleInstitution().getTitle()!=null)
				autWriter.write(indent+"\t<title>"+author.getTitleInstitution().getTitle()+"</title>\n");
			if(author.getTitleInstitution().getInstitution().getControlNumber()!=null)
				autWriter.write(indent+"\t<institutionControlNumber>"+author.getTitleInstitution().getInstitution().getControlNumber()+"</institutionControlNumber>\n");
			if(author.getTitleInstitution().getInstitution().getName()!=null && author.getTitleInstitution().getInstitution().getName().getContent()!=null){
				autWriter.write(indent+"\t<institutionName>"+author.getTitleInstitution().getInstitution().getName().getContent()+"</institutionName>\n");
			}
			if(author.getTitleInstitution().getYear()!=null)
				autWriter.write(indent+"\t<year>"+author.getTitleInstitution().getYear()+"</year>\n");			
			autWriter.write(indent+"</titleInstitution>\n");
		}
		if(author.getInstitution()!=null){
			autWriter.write(indent+"<institution>\n");
			if(author.getInstitution().getControlNumber()!=null)
				autWriter.write(indent+"\t<institutionControlNumber>"+author.getInstitution().getControlNumber()+"</institutionControlNumber>\n");
			if(author.getInstitution().getName()!=null && author.getInstitution().getName().getContent()!=null){
				autWriter.write(indent+"\t<institutionName>"+author.getInstitution().getName().getContent()
						.replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\"", "&quot; ")+"</institutionName>\n");
			}
			if(author.getInstitution().getPlace()!=null){
				autWriter.write(indent+"\t<institutionPlace>"+author.getInstitution().getPlace()+"</institutionPlace>\n");
			}
			
			autWriter.write(indent+"</institution>\n");
		}
		if(author.getOrganizationUnit()!=null){
			autWriter.write(indent+"<orgUnit>\n");
			if(author.getOrganizationUnit().getControlNumber()!=null)
				autWriter.write(indent+"\t<orgUnitControlNumber>"+author.getOrganizationUnit().getControlNumber()+"</orgUnitControlNumber>\n");
			if(author.getOrganizationUnit().getName()!=null && author.getOrganizationUnit().getName().getContent()!=null){
				autWriter.write(indent+"\t<orgUnitName>"+author.getOrganizationUnit().getName().getContent()+"</orgUnitName>\n");
			}		
			if(author.getOrganizationUnit().getSuperOrganizationUnit()!=null){
				autWriter.write(indent+"\t<superOrgUnit>\n");
				autWriter.write(indent+"\t\t<orgUnitControlNumber>"+author.getOrganizationUnit().getSuperOrganizationUnit().getControlNumber()+"</orgUnitControlNumber>\n");
				if(author.getOrganizationUnit().getSuperOrganizationUnit().getName()!=null && author.getOrganizationUnit().getSuperOrganizationUnit().getName().getContent()!=null){
					autWriter.write(indent+"\t\t<orgUnitName>"+author.getOrganizationUnit().getSuperOrganizationUnit().getName().getContent()+"</orgUnitName>\n");
				}	
				autWriter.write(indent+"\t</superOrgUnit>\n");
			}			
			autWriter.write(indent+"</orgUnit>\n");
		}
		if(author.getCurrentPosition()!=null){
			autWriter.write(indent+"<currentPosition>\n");
			if(author.getCurrentPosition().getStartDate()!=null)
				autWriter.write(indent+"\t<startDate>"+sdf.format(author.getCurrentPosition().getStartDate().getTime())+"</startDate>\n");
			if(author.getCurrentPosition().getEndDate()!=null)
				autWriter.write(indent+"\t<endDate>"+sdf.format(author.getCurrentPosition().getEndDate().getTime())+"</endDate>\n");
			if(author.getCurrentPositionName()!=null)
				autWriter.write(indent+"\t<positionName>"+author.getCurrentPositionName()+"</positionName>\n");
			if(author.getCurrentPosition().getResearchArea()!=null)
				autWriter.write(indent+"\t<researchArea>"+author.getCurrentPosition().getResearchArea()+"</researchArea>\n");		
			autWriter.write(indent+"</currentPosition>\n");
		}
		if(author.getFormerPositions()!=null && author.getFormerPositions().size()>0){
			autWriter.write(indent+"<formerPositions>\n");
			for(AuthorPosition position:author.getFormerPositions()){
				autWriter.write(indent+"\t<position>\n");
				autWriter.write(indent+"\t\t<startDate>"+sdf.format(position.getStartDate().getTime())+"</startDate>\n");
				autWriter.write(indent+"\t\t<endDate>"+sdf.format(position.getEndDate().getTime())+"</endDate>\n");
				autWriter.write(indent+"\t\t<positionName>"+position.getPosition().getSomeTerm()+"</positionName>\n");
				autWriter.write(indent+"\t\t<researchArea>"+position.getResearchArea()+"</researchArea>\n");		
				autWriter.write(indent+"\t</position>\n");
			}			
			autWriter.write(indent+"</formerPositions>\n");
		}
		autWriter.write(indent+"<sex>"+author.getSex()+"</sex>\n");
		if(author.getResearchAreas()!=null && author.getResearchAreas().size()>0){
			autWriter.write(indent+"<researchAreas>\n");
			for(ResearchAreaDTO researchArea:author.getResearchAreas()){
				autWriter.write(indent+"\t<researchArea>"+researchArea.getSomeTerm()+"</researchArea>\n");
			}
			autWriter.write(indent+"</researchAreas>\n");
		}
		autWriter.write("</author>\n");
		
	}	
	
	public static void exportPublicationsFromOrganisation(String organisation) throws Exception{
		
		int counter = 0;
		int authorCounter = 1;
		
		authorsIds = SamovrednovanjeApp.getIdsForOrganisation("pmf");
		//authorsIds = authorsIds.subList(authorsIds.size()-1, authorsIds.size());
		System.out.println("Total authprs: "+authorsIds.size());
		for(String id:authorsIds){
			System.out.println("Processing author for id="+id);
			System.out.println("Author counter = "+authorCounter++);
			List<PublicationDTO> pubs = ExportUtils.getAllResultsForAuthor(id);			
			System.out.println("Tota pubs: "+pubs.size());			
			for(PublicationDTO pub:pubs){
				System.out.println(counter);
				if(pub instanceof StudyFinalDocumentDTO)
					continue;				
				if(counter==0){
					outputFilePublications = new File(pubFileNameBase+"1.xml");
					pubWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				            new FileOutputStream(outputFilePublications), "UTF8")));
					pubWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
					pubWriter.write("<crisPublications>\n");
					pubWriter.write("<organisation>pmf</organisation>\n");
				}
				if(counter%500==0){
					pubWriter.write("</crisPublications>\n");
					pubWriter.flush();
					pubWriter.close();
					outputFilePublications = new File(pubFileNameBase+(counter/500+1)+".xml");
					pubWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				            new FileOutputStream(outputFilePublications), "UTF8")));
					pubWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
					pubWriter.write("<crisPublications>\n");
					pubWriter.write("<organisation>pmf</organisation>\n");					
				}					
				
				if(!pubIdsProcessed.contains(pub.getControlNumber())){
					counter++;
					pubIdsProcessed.add(pub.getControlNumber());
					ResultMeasureDTO resultMeasure = SamovrednovanjeUtils.getResultMeasure(pub.getRecord(), ruleBook, allCommissionList, false);
					writePublicationToXML(pub, resultMeasure);
					pub.setNotLoaded(true);
				}
			}
				
		}
		pubWriter .write("</crisPublications>\n");
		pubWriter.flush();
		
	}
	
	public static void exportAuthorsAndCoauthorsFromOrganisation(String organisation) throws UnsupportedEncodingException, FileNotFoundException{
		
		
		if(authorsAndCoauthors==null){
			authorsAndCoauthors = new ArrayList<AuthorDTO>();
			authorsIds = SamovrednovanjeApp.getIdsForOrganisation("pmf");
			System.out.println("authorsIds.size()"+authorsIds.size());
			System.out.println("Dodavanje autora");
			int counter = 0;
			for(String id:authorsIds){
				List<PublicationDTO> pubs = ExportUtils.getAllResultsForAuthor(id);	
				for(PublicationDTO pub:pubs){
					try{
						for(AuthorDTO author:pub.getAllAuthors()){					
							
							if(!authorsAndCoauthorsIds.contains(author.getControlNumber())){
								System.out.println(counter);
								if(counter==0){
									outputFileAuthors = new File(autFileNameBase+"1.xml");
									autWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
								            new FileOutputStream(outputFileAuthors), "UTF8")));
									autWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
									autWriter.write("<crisAuthors>\n");
									autWriter.write("<organisation>pmf</organisation>\n");
								}
								if(counter%1000==0){
									autWriter.write("</crisAuthors>\n");
									autWriter.flush();
									autWriter.close();
									outputFileAuthors = new File(autFileNameBase+(counter/1000+1)+".xml");
									autWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
								            new FileOutputStream(outputFileAuthors), "UTF8")));
									autWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
									autWriter.write("<crisAuthors>\n");
									autWriter.write("<organisation>pmf</organisation>\n");					
								}	
								counter++;
								authorsAndCoauthorsIds.add(author.getControlNumber());
								writeAuthorToXML(author);	
								
							}
							author.setNotLoaded(true);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					autWriter.flush();					
					pub.setNotLoaded(true);
					
				}
			}
			
		}
		autWriter .write("</crisAuthors>\n");
		autWriter.flush();
		
		
	}
	
	private static void initialize() throws Exception{
		String hostname = "localhost";
		String port = "3306";
		String schema = "bibliography";
		String connectionParameters = "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
		String username = "root";
		String password = "bisis";
		String luceneIndex = "E:/cris/lucene-index";
		try {
			System.out.println("Connecting to database...");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
			conn.setAutoCommit(false);
			
			System.out.println("Connection created.");		
			Retriever.setIndexPath(luceneIndex);		
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
		CommissionDB commissionDB = new CommissionDB();
		EvaluationDB evaluationDB = new EvaluationDB();
				
		ruleBookList = evaluationDB.getRuleBooks(conn);
		ruleBook = null;
		for (RuleBookDTO rb : ruleBookList){
			if("serbianResearchersEvaluation".equals(rb.getClassId()))
				ruleBook = rb;
		}
		allCommissionList = commissionDB.getCommissions(conn);	
		SamovrednovanjeApp.setIdsForOrganisations(conn);
		authorsIdsDates = SamovrednovanjeApp.getIdsForOrganisation("pmf");
		SamovrednovanjeApp.setIdsForOrganisationsNoDate(conn);
		
	}
	
	private static String getResultMeasureXML(ResultMeasureDTO resultMeasure){
		StringBuffer retVal = new StringBuffer();
		if(resultMeasure != null){
			retVal.append(indent+"<evaluation>\n");
			if((resultMeasure != null) && (resultMeasure.getCommissionDTO() != null) && (resultMeasure.getCommissionDTO().getAppointmentBoard() != null)) 
				retVal.append(indent+"\t<commission>" + resultMeasure.getCommissionDTO().getAppointmentBoard() + "</commission>\n");
			if((resultMeasure != null) && (resultMeasure.getResultType() != null))
				retVal.append(indent+"\t<type>" + resultMeasure.getResultType().getClassId() + "</type>\n");
			if((resultMeasure != null) && (resultMeasure.getResultType() != null))
				retVal.append(indent+"\t<quantitativeMeasure>" + resultMeasure.getQuantitativeMeasure() + "</quantitativeMeasure>\n");			
			if((resultMeasure != null) && (resultMeasure.getScienceArea() != null))
				retVal.append(indent+"\t<sciencesGroup>" + resultMeasure.getScienceArea().getSomeTerm() + "</sciencesGroup>\n");
			if((resultMeasure != null) && (resultMeasure.getCommissionDTO() != null) && (resultMeasure.getCommissionDTO().getScientificField() != null))
				retVal.append(indent+"\t<scientificField>" + resultMeasure.getCommissionDTO().getScientificField() + "</scientificField>\n");			
			retVal.append(indent+"</evaluation>\n");
		}
		
		return retVal.toString();
	}
	
	private static void finish() throws IOException{
		
		if(autWriter!=null){		
			
			autWriter.flush();
		}else if(pubWriter!=null){
			pubWriter.close();
		}
		

	}

	/*
	 * dodaje listu autora i koautora u authorsAndCoauthors, ali samo ako ne postoje
	 */
	private static void addDistinctAuthors(List<AuthorDTO> newAuthors){
		for(AuthorDTO newAuthor:newAuthors){
			if(!authorExist(newAuthor))
				authorsAndCoauthors.add(newAuthor);
		}
		
	}
	
	private static boolean authorExist(AuthorDTO newAuthor){
		for(AuthorDTO aut:authorsAndCoauthors){
			if(newAuthor.getControlNumber().equals(aut.getControlNumber()))
				return true;
		}
		
		return false;
		
	}
	
	
	

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws Exception {
	/*	if(args.length!=6){
			System.out.println("exportXML <luceneIndex> <hostname> <schema> <username> <password> <pmf|dmi|dh|db|dg|df|all|tf>");
			return;
		}
		
		luceneIndex = args[0];	
		hostname = args[1];
		port = "3307";
		schema = args[2];
		connectionParameters = "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
		username = args[3];
		password = args[4];
		String organisation = args[5];
		*/
		initialize();
		exportPublicationsFromOrganisation("pmf");
		//exportAuthorsAndCoauthorsFromOrganisation("pmf");
		finish();
		
	 }
	
		
		private static String hostname ;
		private static String port;
		private static String schema;
		private static String connectionParameters = "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
		private static String username;
		private static String password;
		private static String luceneIndex;

}
