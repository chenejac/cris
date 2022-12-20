package rs.ac.uns.ftn.informatika.bibliography.reports.referalnicentar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.ResultForYearDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.SamovrednovanjeUtils;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

import com.gint.util.string.LatCyrUtils;

public class ResultsCollector {
	
	private static Connection conn;
	
	private static String hostname = "localhost";
	private static String port = "3306";
	private static String schema = "bibliography";
	private static String connectionParameters = "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
	private static String username = "root";
	private static String password = "bisis";
	private static String luceneIndex = "E:/CRIS/lucene-index";
	private static String folder = "E:/CRIS/pmf/";
		

	
	static{
		try {
			System.out.println("Connecting to database...");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
			conn.setAutoCommit(false);
			SamovrednovanjeUtils.setMyDataSource(hostname, port, schema, connectionParameters, username, password);
			System.out.println("Connection created.");		
		//Retriever.setIndexPath(luceneIndex);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static List<Record> getAllResults(String id){
		try{
		List<ResultForYearDTO> retVal = null;
		TermQuery termQuery = null;
		BooleanQuery query = new BooleanQuery();
		if(id != null){
			BooleanQuery idsQuery = new BooleanQuery();			
			termQuery = new TermQuery(new Term("AUCN", id));
			idsQuery.add(termQuery, Occur.SHOULD);
			/*
			termQuery = new TermQuery(new Term("EDCN", id));
			idsQuery.add(termQuery, Occur.SHOULD);
			*/			
			query.add(idsQuery, Occur.MUST);
		}		
		List<Record> records = Retriever.select(query, new AllDocCollector(false));			
		return records;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<PublicationDTO> sortPublications(List<Record> recs){
		List<PublicationDTO> retVal = new ArrayList<PublicationDTO>();
		for(Record rec:recs){
			retVal.add((PublicationDTO)rec.getDto());
		}
	
		List<String> sortAttributes = new ArrayList<String>();
		sortAttributes.add("publicationYear");
		
		List<String> sortDirections = new ArrayList<String>();
		sortDirections.add("asc");
	
		
		Collections.sort(retVal, new GenericComparator<PublicationDTO>(
				sortAttributes, sortDirections));
		
		return retVal;
	}
	
	public static Person getPersonForId(String id){
		PersonDB personDB = new PersonDB();		
		Record rec = new RecordDAO(personDB).getRecord(id);
		Person pers = (Person)personDB.getRecord(conn, id, rec);
		return pers;
	}
	
	public static void writeToFile(Person pers, List<PublicationDTO> pubs) throws IOException{
		AuthorDTO autor = (AuthorDTO)pers.getDto();
	/*
		XWPFDocument doc = new XWPFDocument();
		
		
		XWPFParagraph p1 = doc.createParagraph();
		
		XWPFRun r1 = p1.createRun();
		
		
		
		r1.setText("Autor: "+autor.getName().getLastname()+" "+autor.getName().getFirstname()+"<br><br>"+		
				"APVNT: "+autor.getApvnt()+"<br><br>");
		int redniBroj = 1;
		for(PublicationDTO pub:pubs){
			XWPFParagraph p = doc.createParagraph();
			p.setSpacingBefore(12);
			XWPFRun r = p.createRun();		
			r.setText(redniBroj+++". "+pub.getHTMLRepresentation()+"<br><br><br>");
		}
		
		FileOutputStream out = new FileOutputStream(autor.getName().getLastname()+autor.getName().getFirstname()+".docx");
		
		doc.write(out);
		out.close();
		*/
		
		File validRecFile = new File(folder+autor.getName().getLastname()+" "+autor.getName().getFirstname()+".doc");
		FileOutputStream streamValid = new FileOutputStream(validRecFile);			
		OutputStreamWriter writer = new OutputStreamWriter(streamValid,"UTF-8");	
  //napraviti fajl sa svim istraÅ¾ivaÄ�ima
		
		
		writer.append("<html>");
		writer.append("<head> <meta charset=\"UTF-8\"> </head><body>");
		 writer.append("IstraÅ¾ivaÄ�: "+autor.getName().getLastname()+" "+autor.getName().getFirstname()+"<br>" +
		 		"Institucija: Prirodno-matematiÄ�ki fakultet<br><br>"+		
				"APVNT: "+autor.getApvnt()+"<br><br>");
		 
			int redniBroj = 1;
			for(PublicationDTO pub:pubs){								
				writer.append(redniBroj+++"* "+LatCyrUtils.toLatin(pub.getHarvardRepresentation())+"<br><br>");
			}
		 
		 writer.append("</body></html>");
		 writer.close();
	
		
	}
	
	private static List<String> obtainIdsForOrganisation(String organisation, String startDate){
		String controlNumber =  getControlNumberForOrganisation(organisation);
		List<String> ids = getIdsForOrganisationControlNumber(controlNumber, startDate);
		return ids;
	}
	
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
	
	private static List<String> getIdsForOrganisationControlNumber(String orgControlNumber, String startDate){
		PersonDB personDB = new PersonDB();
		List<Record> listAuthors = personDB.getInstitutionRecords(conn, orgControlNumber, startDate);				
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
	
	public static void main(String[] args) throws IOException{
		
		File f = new File(folder);
		if(f.exists())
			f.delete();
		f.createNewFile();
		
		List<String> ids = obtainIdsForOrganisation("pmf", "2022-01-01 00:00:00");
		List<String> toExclude = new ArrayList<String>();
		toExclude.add("9999");
		toExclude.add("2879");
		toExclude.add("1250");
		toExclude.add("1258");
		toExclude.add("1245");		
		File validRecFile = new File("E:/CRIS/spisakPMF.doc");
		if(validRecFile.exists())
			validRecFile.delete();
		validRecFile.createNewFile();
		
		FileOutputStream streamValid = new FileOutputStream(validRecFile);			
		OutputStreamWriter writer = new OutputStreamWriter(streamValid,"UTF-8");
		writer.append("<html>");
		writer.append("<head> <meta charset=\"UTF-8\"> </head><body>");
		for(String id:ids){
			List<Record> retVal = getAllResults(id);
			List<PublicationDTO> retValPub = sortPublications(retVal);
			Person pers = getPersonForId(id);			
			System.out.println(pers.getApvnt());
			if(pers.getApvnt()!=null && !toExclude.contains(pers.getApvnt())){
				writeToFile(pers, retValPub);				
				AuthorDTO autor = (AuthorDTO) pers.getDto();
				writer.append(autor.getName().getLastname()+" "+autor.getName().getFirstname()+", APVNT: "+pers.getApvnt()+"<br>");				
			}			
		}
		writer.append("</body></html>");
	 writer.close();
		writer.close();
	}
	
	
	
	
	


}
