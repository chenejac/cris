package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.jbibtex.ObjectResolutionException;
import org.jbibtex.TokenMgrException;
import org.json.JSONArray;
import org.json.JSONObject;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.ImportRecordsDTO;

import com.mashape.unirest.http.Unirest;

/**
 * @author Dragan Ivanovic
 *
 */
public class ScopusPaperJournalSerializer implements GroupSerializer{
	
	@Override
	public OutputStream exportRecords(ImportRecordsDTO records) {
		return null;
	}

	@Override
	public ImportRecordsDTO importRecords(InputStream is) {
		if(reimport == false)
			return importRecords;
		else {
			importRecords = new ImportRecordsDTO();
		
			try {
				ScopusImportUtility.headers.put("X-ELS-APIKey", "2af9ded2f6e1b20cfb33af2333cfffcd");//"894638366d70903b6aa0b08f59f56220");
		        ScopusImportUtility.headers.put("Accept", "application/json");
	
		        Unirest.setProxy(new HttpHost("proxy.uns.ac.rs", 8080));
		        // ScopusImportUtility.authenticate();
		        
		        List<JSONObject> jos = ScopusImportUtility.getDocumentsByAuthor(scopusID, startYear, endYear);
		        for(JSONObject jo:jos){
//		        	System.out.println(jo.toString());
		        	JSONArray ja = jo.getJSONObject("search-results").getJSONArray("entry");
		        	for(int i = 0; i <ja.length(); i++){
		        		boolean error = ! ja.getJSONObject(i).has("prism:aggregationType");
		        		if(error == true)
		        			continue;
			        	String type = ja.getJSONObject(i).getString("prism:aggregationType");
		        		if(type !=null && type.equalsIgnoreCase("Journal")){
							loadPaper(jo.getJSONObject("search-results").getJSONArray("entry").getJSONObject(i), importRecords);
						} 
		        	}
		        }
		        /*
				BibTeXDatabase database = parser.parseFully(reader);
				Collection<BibTeXEntry> entries = (database.getEntries()).values();
				for(BibTeXEntry entry : entries){
					if(entry.getType().getValue().equalsIgnoreCase("article")){
						loadPaper(entry, retVal);
					}
				} */
			} catch (ObjectResolutionException e) {
			} catch (TokenMgrException e) {
			}finally {
			}
			return importRecords;
		}
	}
	
	private List<RecordDTO> loadAuthors(JSONObject entry){
		List<RecordDTO> authors = new ArrayList<RecordDTO>();
		try {
			JSONArray authorsList = (entry.has("author") && !entry.isNull("author"))?entry.getJSONArray("author"):null;
			if(authorsList == null)
				return null;
			else {
				for (int i=0; i<authorsList.length(); i++) {
					JSONObject author = authorsList.getJSONObject(i);
					AuthorDTO authorDTO = new AuthorDTO();
//					authorDTO.setRelatedRecords(new ArrayList<RecordDTO>());
					authorDTO.getName().setFirstname((author.has("given-name") && !author.isNull("given-name"))?author.getString("given-name"):null);
					authorDTO.getName().setLastname((author.has("surname") && !author.isNull("surname"))?author.getString("surname"):null);
					authorDTO.setScopusID((author.has("authid") && !author.isNull("authid"))?author.getString("authid"):null);
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
					if(! authors.contains((RecordDTO)authorDTO))
						authors.add((RecordDTO)authorDTO);
				}
			}
		} catch (Throwable ex) {
			log.fatal(ex);
			log.fatal("Cannot load authors");
			return null;
		}
		return authors;
	}
	
	private RecordDTO loadJournal(JSONObject entry){
		RecordDTO journal = null;
		try {
			String journalValue = (entry. has("prism:publicationName") && !entry.isNull("prism:publicationName"))?entry.getString("prism:publicationName"):null;
			if(journalValue == null)
				return null;
			else {
				JournalDTO journalDTO = new JournalDTO();
//				journalDTO.setRelatedRecords(new ArrayList<RecordDTO>());
				journalDTO.getName().setContent(journalValue);
				journalDTO.getName().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);
				String issnValue = (entry.has("prism:issn") && !entry.isNull("prism:issn"))?entry.getString("prism:issn"):null;
				if(issnValue != null){
					if(issnValue.length() > 4)
						issnValue = issnValue.substring(0, 4) + "-" + issnValue.substring(4);
					journalDTO.setIssn(issnValue);
					journalDTO.setControlNumber(journalDTO.getIssn());
				} else {
					journalDTO.setControlNumber(journalDTO.getName().getContent());
				}
				journalDTO.setScopusID((entry.has("source-id") && !entry.isNull("source-id"))?entry.getString("source-id"):null);
				
				journal = journalDTO;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.fatal(ex);
			log.fatal("Cannot load journals");
		}
		return journal;
	}
	
	private boolean loadPaper(JSONObject entry, ImportRecordsDTO importRecords){
		boolean retVal = false;
		try {
			PaperJournalDTO paperDTO = new PaperJournalDTO();
//			paperDTO.setRelatedRecords(new ArrayList<RecordDTO>());
			List<RecordDTO> authors = loadAuthors(entry);
			if(authors == null)
				return false;
			else { 
				List<RecordDTO> authorsMerged = new ArrayList<RecordDTO>();
				for (RecordDTO recordDTO : authors) {
					if(importRecords.getAllRecords().contains(recordDTO))
						authorsMerged.add(importRecords.getAllRecords().get(importRecords.getAllRecords().indexOf(recordDTO)));
					else
						authorsMerged.add(recordDTO);
				}
				
				boolean mainAuthor = false;
				for (RecordDTO recordDTO : authorsMerged) {
					recordDTO.addRelatedRecord(paperDTO);
					if(mainAuthor == true)
						paperDTO.getOtherAuthors().add((AuthorDTO)recordDTO);
					else {
						paperDTO.setMainAuthor((AuthorDTO)recordDTO);
						mainAuthor = true;
					}
						
				}
			}
			RecordDTO journal = loadJournal(entry);
			if(journal == null)
				return false;
			else {
				RecordDTO journalMerged = null;
				if(importRecords.getAllRecords().contains(journal))
					journalMerged = importRecords.getAllRecords().get(importRecords.getAllRecords().indexOf(journal));
				else
					journalMerged = journal;
				journalMerged.addRelatedRecord(paperDTO);
				paperDTO.setJournal((JournalDTO)journalMerged);
			}
			
			String titleValue = (entry.has("dc:title")&& !entry.isNull("dc:title"))?entry.getString("dc:title"):null;
			if(titleValue == null)
				return false;
			else {
				paperDTO.getTitle().setContent(titleValue);
				paperDTO.getTitle().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);
				
				String yearValue = (entry.has("prism:coverDate")&& !entry.isNull("prism:coverDate"))?entry.getString("prism:coverDate"):null;
				if(yearValue != null){
					paperDTO.setPublicationYear(yearValue.split("-")[0]);
					paperDTO.setControlNumber(paperDTO.getPublicationYear() + paperDTO.getTitle().getContent());
				} else {
					paperDTO.setControlNumber(paperDTO.getTitle().getContent());
				}
			
				String volumeValue = (entry.has("prism:volume")&& !entry.isNull("prism:volume"))?entry.getString("prism:volume"):null;
				if(volumeValue != null){
					paperDTO.setVolume(volumeValue);
				} 
				
				String numberValue = (entry.has("prism:issueIdentifier") && !entry.isNull("prism:issueIdentifier"))?entry.getString("prism:issueIdentifier"):null;
				if(numberValue != null){
					paperDTO.setNumber(numberValue);
				} 
				
				String doiValue = (entry.has("prism:doi")&& !entry.isNull("prism:doi"))?entry.getString("prism:doi"):null;
				if(doiValue != null){
					paperDTO.setDoi(doiValue);
				}
				
				
				JSONArray urlsList = (entry.has("link")&& !entry.isNull("link"))?entry.getJSONArray("link"):null;
				if(urlsList != null){
					for (int i=0; i<urlsList.length(); i++) {
						JSONObject url = urlsList.getJSONObject(i);
						String href = (url.has("@href")&& !url.isNull("@href"))?url.getString("@href"):null;
						if(href != null){
							if(href.contains("inward/record.uri")){
								paperDTO.setUri(href);
								break;
							}
						}
					}
				}
				
				String abstractValue = (entry.has("dc:description")&& !entry.isNull("dc:description"))?entry.getString("dc:description"):null;
				if(abstractValue != null){
					paperDTO.getAbstracT().setContent(abstractValue);
					paperDTO.getAbstracT().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);
				}
				
				String pagesValue = (entry.has("prism:pageRange")&& !entry.isNull("prism:pageRange"))?entry.getString("prism:pageRange"):null;
				if(pagesValue != null){
					String[] pagesListString = pagesValue.split("-");
					if((pagesListString != null) && (pagesListString.length > 0))
						paperDTO.setStartPage(pagesListString[0]);
					if((pagesListString != null) && (pagesListString.length > 1))
						paperDTO.setEndPage(pagesListString[1]);
				}
				
				String scopusId = (entry.has("dc:identifier") && !entry.isNull("dc:identifier"))?entry.getString("dc:identifier"):null;
				if(scopusId != null){
					if(scopusId.startsWith("SCOPUS_ID:"))
						paperDTO.setScopusID(scopusId.replace("SCOPUS_ID:", ""));
				}
				
				
				paperDTO.setPaperType("records.paperJournal.editPanel.paperType.paper");
				
				if(!importRecords.getAllRecords().contains(paperDTO)){
					for (RecordDTO recordDTO : authors) {
						if(!importRecords.getAllRecords().contains(recordDTO)){
							importRecords.getAllRecords().add(recordDTO);
						}
					}
					importRecords.setAllRecords(importRecords.getAllRecords());
					
					if(!importRecords.getAllRecords().contains(journal))
						importRecords.getAllRecords().add(journal);
					importRecords.setAllRecords(importRecords.getAllRecords());
					
					importRecords.getAllRecords().add(paperDTO);
					importRecords.setAllRecords(importRecords.getAllRecords());	
					retVal = true;
				}			
			}	
		} catch (Throwable ex) {
			log.fatal(ex);
			log.fatal("Cannot load paper");
		}
		return retVal;
	}
	
	

	private String scopusID;

	/**
	 * @return the scopusID
	 */
	public String getScopusID() {
		return scopusID;
	}

	/**
	 * @param scopusID the scopusID to set
	 */
	public void setScopusID(String scopusID) {
		if (scopusID == null)
			reimport = false;
		else if(this.scopusID == null)
			reimport = true;
		else if (!scopusID.equalsIgnoreCase(this.scopusID))
			reimport = true;
		else
			reimport = false;
		this.scopusID = scopusID;
	}
	
	private Integer startYear;
	
	

	/**
	 * @return the startYear
	 */
	public Integer getStartYear() {
		return startYear;
	}

	/**
	 * @param startYear the startYear to set
	 */
	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	private Integer endYear;
	
	
	
	/**
	 * @return the endYear
	 */
	public Integer getEndYear() {
		return endYear;
	}

	/**
	 * @param endYear the endYear to set
	 */
	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}
	
	private boolean reimport = false;
	
	private ImportRecordsDTO importRecords = new ImportRecordsDTO();

	private static Log log = LogFactory.getLog(ScopusPaperJournalSerializer.class.getName());

}
