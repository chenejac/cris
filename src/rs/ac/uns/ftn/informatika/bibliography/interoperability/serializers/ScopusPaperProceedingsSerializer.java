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
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.ImportRecordsDTO;

import com.mashape.unirest.http.Unirest;

/**
 * @author Dragan Ivanovic
 *
 */
public class ScopusPaperProceedingsSerializer implements GroupSerializer{
	
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
				ScopusImportUtility.headers.put("X-ELS-APIKey", "2af9ded2f6e1b20cfb33af2333cfffcd");
		        ScopusImportUtility.headers.put("Accept", "application/json");
	
		        Unirest.setProxy(new HttpHost("proxy.uns.ac.rs", 8080));
		        // ScopusImportUtility.authenticate();
		        
		        List<JSONObject> jos = ScopusImportUtility.getDocumentsByAuthor(scopusID, startYear, endYear);
		        for(JSONObject jo:jos){
		        	JSONArray ja = jo.getJSONObject("search-results").getJSONArray("entry");
		        	for(int i = 0; i <ja.length(); i++){
		        		boolean error = ! ja.getJSONObject(i).has("prism:aggregationType");
		        		if(error == true)
		        			continue;
			        	String type = ja.getJSONObject(i).getString("prism:aggregationType");
		        		if(type !=null && type.equalsIgnoreCase("Conference Proceeding")){
							loadPaper(jo.getJSONObject("search-results").getJSONArray("entry").getJSONObject(i), importRecords);
						} 
		        	}
		        }
		        
		       /* for(RecordDTO record:importRecords.getAllRecords()){
		        	System.out.println(record.toString());
		        }*/
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
			ex.printStackTrace();
			log.fatal(ex);
			log.fatal("Cannot load authors");
			return null;
		}
		return authors;
	}
	
	private RecordDTO loadProceedings(JSONObject entry, JSONObject conferenceInfo, ImportRecordsDTO importRecords){
		RecordDTO proceedings = null;
		try {
			String proceedingsValue = (entry. has("prism:publicationName") && !entry.isNull("prism:publicationName"))?entry.getString("prism:publicationName"):null;
			if(proceedingsValue == null)
				return null;
			else {
				ProceedingsDTO proceedingsDTO = new ProceedingsDTO();
//				proceedingsDTO.setRelatedRecords(new ArrayList<RecordDTO>());
				proceedingsDTO.getTitle().setContent(proceedingsValue);
				proceedingsDTO.getTitle().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);
				String isbnValue = (entry.has("prism:isbn") && !entry.isNull("prism:isbn"))?entry.getJSONArray("prism:isbn").getJSONObject(0).getString("$"):null;
				
				//proceedingsDTO.setScopusID((entry.has("source-id") && !entry.isNull("source-id"))?entry.getString("source-id"):null);
				
				ConferenceDTO conferenceDTO = new ConferenceDTO();
//				conferenceDTO.setRelatedRecords(new ArrayList<RecordDTO>());
				
				JSONObject temp = (conferenceInfo.has("abstracts-retrieval-response") && !conferenceInfo.isNull("abstracts-retrieval-response"))?conferenceInfo.getJSONObject("abstracts-retrieval-response"):null;
				temp = (temp.has("item") && !temp.isNull("item"))?temp.getJSONObject("item"):null;
				temp = (temp.has("bibrecord") && !temp.isNull("bibrecord"))?temp.getJSONObject("bibrecord"):null;
				temp = (temp.has("head") && !temp.isNull("head"))?temp.getJSONObject("head"):null;
				temp = (temp.has("source") && !temp.isNull("source"))?temp.getJSONObject("source"):null;
				temp = (temp.has("additional-srcinfo") && !temp.isNull("additional-srcinfo"))?temp.getJSONObject("additional-srcinfo"):null;
				temp = (temp.has("conferenceinfo") && !temp.isNull("conferenceinfo"))?temp.getJSONObject("conferenceinfo"):null;
				JSONObject confevent = (temp.has("confevent") && !temp.isNull("confevent"))?temp.getJSONObject("confevent"):null;
				
				
				//abstracts-retrieval-response.item.bibrecord.head.source.additional-srcinfo.conferenceInfo.confevent
				if(confevent == null)
					return null; 
				String confname = (confevent. has("confname") && !confevent.isNull("confname"))?confevent.getString("confname"):null;
				if(confname != null){
					conferenceDTO.getName().setContent(confname);
					conferenceDTO.getName().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);
				}
				String confnumber = (confevent. has("confnumber") && !confevent.isNull("confnumber"))?confevent.getString("confnumber"):null;
				if(confnumber != null){
					conferenceDTO.setNumber(confnumber);
				}
				
				JSONObject confdate = (confevent.has("confdate") && !confevent.isNull("confdate"))?confevent.getJSONObject("confdate"):null;
				
				JSONObject startdate = (confdate.has("startdate") && !confdate.isNull("startdate"))?confdate.getJSONObject("startdate"):null;
				
				JSONObject enddate = (confdate.has("enddate") && !confdate.isNull("enddate"))?confdate.getJSONObject("enddate"):null;
				
				if(startdate != null){
					String year = (startdate. has("@year") && !startdate.isNull("@year"))?startdate.getString("@year"):null;
					if(year != null){
						conferenceDTO.setYear(Integer.parseInt(year));
					}	
					String month = (startdate. has("@month") && !startdate.isNull("@month"))?startdate.getString("@month"):null;
					String day = (startdate. has("@day") && !startdate.isNull("@day"))?startdate.getString("@day"):null;
					String period = "";
					if(year != null){
						period += year + ((month!=null)?("/" + month):"") + ((day!=null)?("/" + day):"");
					}
					
					if(enddate != null){
						String yearEnd = (enddate. has("@year") && !enddate.isNull("@year"))?enddate.getString("@year"):null;
						String monthEnd = (enddate. has("@month") && !enddate.isNull("@month"))?enddate.getString("@month"):null;
						String dayEnd = (enddate. has("@day") && !enddate.isNull("@day"))?enddate.getString("@day"):null;
						if(yearEnd != null){
							period +=  "-" + yearEnd + ((monthEnd!=null)?("/" + monthEnd):"") + ((dayEnd!=null)?("/" + dayEnd):"");
						}
					}
					
					if(period.trim().length() > 0)
						conferenceDTO.setPeriod(period);
				}
				
				JSONObject conflocation = (confevent.has("conflocation") && !confevent.isNull("conflocation"))?confevent.getJSONObject("conflocation"):null;
				
				if(conflocation != null){
					String country = (conflocation. has("@country") && !conflocation.isNull("@country"))?conflocation.getString("@country"):null;
					String venue = (conflocation. has("venue") && !conflocation.isNull("venue"))?conflocation.getString("venue"):null;
					String city = (conflocation. has("city") && !conflocation.isNull("city"))?conflocation.getString("city"):null;
					
					if(country != null)
						conferenceDTO.setState(country);
					
					String place = "";
					
					place += ((venue!=null)?(venue + ", "):"") + ((city!=null)?(city):"");
					
					if(place.trim().length() > 0) 
						conferenceDTO.setPlace(place);
				}
				
				String confid = (confevent.has("confcode") && !confevent.isNull("confcode"))?confevent.getString("confcode"):null;
				if(confid != null){
					conferenceDTO.setControlNumber("CONF:" + confid);
					conferenceDTO.setScopusID(confid);
				} else {
					conferenceDTO.setControlNumber("CONF:" + conferenceDTO.getName().getContent());
				}
				
				if(isbnValue != null){
					proceedingsDTO.setIsbn(isbnValue);
					proceedingsDTO.setControlNumber(conferenceDTO.getControlNumber() + " PROC: " + proceedingsDTO.getIsbn());
				} else {
					proceedingsDTO.setControlNumber(conferenceDTO.getControlNumber() + " PROC: " + proceedingsDTO.getTitle().getContent());
				}
				
				if(importRecords.getAllRecords().contains(conferenceDTO))
					conferenceDTO = (ConferenceDTO) importRecords.getAllRecords().get(importRecords.getAllRecords().indexOf(conferenceDTO));
				
				
				conferenceDTO.addRelatedRecord(proceedingsDTO);
				
				proceedingsDTO.setConference(conferenceDTO);
				
				proceedings = proceedingsDTO;
			}
			
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot load proceedings");
		}
		return proceedings;
	}
	
	private boolean loadPaper(JSONObject entry, ImportRecordsDTO importRecords){
		boolean retVal = false;
		try {
			PaperProceedingsDTO paperDTO = new PaperProceedingsDTO();
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
			
			String scopusId = (entry.has("dc:identifier") && !entry.isNull("dc:identifier"))?entry.getString("dc:identifier"):null;
			if(scopusId != null){
				if(scopusId.startsWith("SCOPUS_ID:")){
					scopusId = scopusId.replace("SCOPUS_ID:", "");
					paperDTO.setScopusID(scopusId);
				}
			}
			JSONObject conferenceInfoIncluded = ScopusImportUtility.getAbstractData(scopusId);
			
			RecordDTO proceedings = loadProceedings(entry, conferenceInfoIncluded, importRecords);
			if(proceedings == null)
				return false;
			else {
				RecordDTO proceedingsMerged = null;
				if(importRecords.getAllRecords().contains(proceedings))
					proceedingsMerged = importRecords.getAllRecords().get(importRecords.getAllRecords().indexOf(proceedings));
				else
					proceedingsMerged = proceedings;
				proceedingsMerged.addRelatedRecord(paperDTO);
				paperDTO.setProceedings((ProceedingsDTO)proceedingsMerged);
			}
			
			String titleValue = (entry.has("dc:title")&& !entry.isNull("dc:title"))?entry.getString("dc:title"):null;
			if(titleValue == null)
				return false;
			else {
				paperDTO.getTitle().setContent(titleValue);
				paperDTO.getTitle().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);
				
				String yearValue = (entry.has("prism:coverDate")&& !entry.isNull("prism:coverDate"))?entry.getString("prism:coverDate"):null;
				if(yearValue != null){
					paperDTO.getProceedings().setPublicationYear(yearValue.split("-")[0]);
					paperDTO.setPublicationYear(yearValue.split("-")[0]);
					paperDTO.setControlNumber(paperDTO.getPublicationYear() + paperDTO.getTitle().getContent());
				} else {
					paperDTO.setControlNumber(paperDTO.getTitle().getContent());
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
				
				
				
//				String subtype = (entry.has("subtype")&& !entry.isNull("subtype"))?entry.getString("subtype"):null;
//				if("cp".equals(subtype))
					paperDTO.setPaperType("records.paperProceedings.editPanel.paperType.full");
//				else if ("cp".equals(subtype))
//					paperDTO.setPaperType("records.paperProceedings.editPanel.paperType.full");
//				else if ("cp".equals(subtype))
//					paperDTO.setPaperType("records.paperProceedings.editPanel.paperType.full");
//				else if ("cp".equals(subtype))
//					paperDTO.setPaperType("records.paperProceedings.editPanel.paperType.full");
				
				if(!importRecords.getAllRecords().contains(paperDTO)){
					for (RecordDTO recordDTO : authors) {
						if(!importRecords.getAllRecords().contains(recordDTO)){
							importRecords.getAllRecords().add(recordDTO);
						}
					}
					importRecords.setAllRecords(importRecords.getAllRecords());
					
					if(!importRecords.getAllRecords().contains(proceedings)){
						if(!importRecords.getAllRecords().contains(((ProceedingsDTO)proceedings).getConference())){
							importRecords.getAllRecords().add(((ProceedingsDTO)proceedings).getConference());
						}
						importRecords.getAllRecords().add(proceedings);
					}
					importRecords.setAllRecords(importRecords.getAllRecords());
					importRecords.getAllRecords().add(paperDTO);
					importRecords.setAllRecords(importRecords.getAllRecords());	
					retVal = true;
				}			
			}	
		} catch (Throwable ex) {
			ex.printStackTrace();
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
