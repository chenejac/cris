/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.BibTeXString;
import org.jbibtex.Key;
import org.jbibtex.ObjectResolutionException;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;
import org.jbibtex.Value;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.ImportRecordsDTO;

/**
 * @author Dragan Ivanovic
 *
 */
public class BibtexPaperJournalSerializer implements GroupSerializer{
	
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
		
			Reader reader = null;  
	//		 Reader reader = new StringReader(bibtexText);
			try {
				reader = new StringReader(bibtexText); 
	//				new FileReader(new File("E:/scopus.bib"));
				BibTeXParser parser = new BibTeXParser(){
	
					@Override
					public void checkStringResolution(Key key, BibTeXString string){
	
						if(string == null){
							System.err.println("Unresolved string: \"" + key.getValue() + "\"");
						}
					}
	
					@Override
					public void checkCrossReferenceResolution(Key key, BibTeXEntry entry){
	
						if(entry == null){
							System.err.println("Unresolved cross-reference: \"" + key.getValue() + "\"");
						}
					}
				};
	
				BibTeXDatabase database = parser.parseFully(reader);
				Collection<BibTeXEntry> entries = (database.getEntries()).values();
				for(BibTeXEntry entry : entries){
					if(entry.getType().getValue().equalsIgnoreCase("article")){
						loadPaper(entry, importRecords);
					}
				}
			} catch (ObjectResolutionException e) {
			} catch (TokenMgrException e) {
			} catch (ParseException e) {
			} finally {
				try {
					if(reader != null)
						reader.close();
				} catch (IOException e) {
				}
			}
			return importRecords;
		}
	}
	
	private List<RecordDTO> loadAuthors(BibTeXEntry entry){
		List<RecordDTO> authors = new ArrayList<RecordDTO>();
		try {
			Value authorsList = entry.getField(BibTeXEntry.KEY_AUTHOR);
			if(authorsList == null)
				return null;
			else {
				String authorsString = authorsList.toUserString();
				String[] authorsListString = authorsString.split(" and ");
				for (String string : authorsListString) {
					String[] firstLastName = string.split(",");
					AuthorDTO authorDTO = new AuthorDTO();
					authorDTO.getName().setFirstname(firstLastName[1].trim());
					authorDTO.getName().setLastname(firstLastName[0].trim());
					authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
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
	
	private RecordDTO loadJournal(BibTeXEntry entry){
		RecordDTO journal = null;
		try {
			Value journalValue = entry.getField(BibTeXEntry.KEY_JOURNAL);
			if(journalValue == null)
				return null;
			else {
			
				JournalDTO journalDTO = new JournalDTO();
				journalDTO.getName().setContent(journalValue.toUserString());
				Value issnValue = entry.getFields().get(new Key("ISSN"));
				if(issnValue != null){
					journalDTO.setIssn(issnValue.toUserString());
					journalDTO.setControlNumber(journalDTO.getIssn());
				} else {
					journalDTO.setControlNumber(journalDTO.getName().getContent());
				}
				journal = journalDTO;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.fatal(ex);
			log.fatal("Cannot load journals");
		}
		return journal;
	}
	
	private boolean loadPaper(BibTeXEntry entry, ImportRecordsDTO importRecords){
		boolean retVal = false;
		try {
			PaperJournalDTO paperDTO = new PaperJournalDTO();
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
			Value titleValue = entry.getField(BibTeXEntry.KEY_TITLE);
			if(titleValue == null)
				return false;
			else {
				paperDTO.getTitle().setContent(titleValue.toUserString());
				paperDTO.getTitle().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);
				
				Value yearValue = entry.getField(BibTeXEntry.KEY_YEAR);
				if(yearValue != null){
					paperDTO.setPublicationYear(yearValue.toUserString());
					paperDTO.setControlNumber(paperDTO.getPublicationYear() + paperDTO.getTitle().getContent());
				} else {
					paperDTO.setControlNumber(paperDTO.getTitle().getContent());
				}
			
				Value volumeValue = entry.getField(BibTeXEntry.KEY_VOLUME);
				if(volumeValue != null){
					paperDTO.setVolume(volumeValue.toUserString());
				} 
				
				Value numberValue = entry.getField(BibTeXEntry.KEY_NUMBER);
				if(numberValue != null){
					paperDTO.setNumber(numberValue.toUserString());
				} 
				
				Value doiValue = entry.getField(BibTeXEntry.KEY_DOI);
				if(doiValue != null){
					paperDTO.setDoi(doiValue.toUserString());
				}
				
				Value urlValue = entry.getField(BibTeXEntry.KEY_URL);
				if(urlValue != null){
					paperDTO.setUri(urlValue.toUserString());
				}
				
				Value noteValue = entry.getField(BibTeXEntry.KEY_NOTE);
				if(noteValue != null){
					paperDTO.getNote().setContent(noteValue.toUserString());
					paperDTO.getNote().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);
				}
				
				Value pagesValue = entry.getField(BibTeXEntry.KEY_PAGES);
				if(pagesValue != null){
					String pagesString = pagesValue.toUserString();
					String[] pagesListString = pagesString.split("-");
					if((pagesListString != null) && (pagesListString.length > 0))
						paperDTO.setStartPage(pagesListString[0]);
					if((pagesListString != null) && (pagesListString.length > 1))
						paperDTO.setEndPage(pagesListString[1]);
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
			ex.printStackTrace();
			log.fatal(ex);
			log.fatal("Cannot load paper");
		}
		return retVal;
	}
		
	
	private String bibtexText;

	/**
	 * @return the bibtexText
	 */
	public String getBibtexText() {
		return bibtexText;
	}

	/**
	 * @param bibtexText the bibtexText to set
	 */
	public void setBibtexText(String bibtexText) {
		if (bibtexText == null)
			reimport = false;
		else if(this.bibtexText == null)
			reimport = true;
		else if (!bibtexText.equalsIgnoreCase(this.bibtexText))
			reimport = true;
		else
			reimport = false;
		this.bibtexText = bibtexText;
	}
	
	private boolean reimport = false;
	
	private ImportRecordsDTO importRecords = new ImportRecordsDTO();

	private static Log log = LogFactory.getLog(BibtexPaperJournalSerializer.class.getName());

}
