/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.knr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;

import rs.ac.uns.ftn.informatika.bibliography.backup.Task;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

import com.gint.util.string.StringUtils;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ImportProceedingsPapersFromKNRTask implements Task {

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	private Pattern authors1 = Pattern.compile("((\\s*,)?(\\s+and\\s+)?(\\s+i\\s+)?\\s*[\\w\\-]+\\s+([\\w]+[\\-\\s]*){1,2})+[,:]*");
	private Pattern authors2 = Pattern.compile("((\\s*,)?(\\s+and\\s+)?(\\s+i\\s+)?\\s*[\\w\\-]+\\s+((\\w){1,2}\\s*\\.+[\\-\\s]*){1,2})+[,:]*");
	private Pattern authors3 = Pattern.compile("((\\s*,)?(\\s+and\\s+)?(\\s+i\\s+)?\\s*([\\w]+[\\-\\s]*){1,2},\\s*([\\w]+[\\-\\s]*){1,2})+[,:]*");
	private Pattern authors4 = Pattern.compile("((\\s*,)?(\\s+and\\s+)?(\\s+i\\s+)?\\s*([\\w]+[\\-\\s]*){1,2},+\\s*((\\w){1,2}\\s*\\.+[\\-\\s]*){1,2})+[,:]*");
	private Pattern authors5 = Pattern.compile("((\\s*,)?(\\s+and\\s+)?(\\s+i\\s+)?\\s*((\\w){1,2}\\s*\\.+[\\-\\s]*){1,2}([\\w]+[\\-\\s]*){1,2})+[,:]*");
	private Pattern authors6 = Pattern.compile("((\\.,)?(\\.\\s,)?(\\s+and\\s+)?(\\s+i\\s+)?\\s*[\\w]+[\\-]*[,\\s]+([\\w]+[\\-\\s]*){1,2})+[\\.,:\\s]*");
	private Pattern authors7 = Pattern.compile("((\\s*,)?(\\s+and\\s+)?(\\s+i\\s+)?\\s*[\\w\\-]+\\s+((\\w){1,2}\\s*\\.+[\\-\\s]*){1,2})+[,:]*");
	
	public ImportProceedingsPapersFromKNRTask(Connection source) {
		this.source = source;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.backup.Task#execute()
	 */
	@Override
	public boolean execute() {
		boolean retVal = true;
		try {
			PreparedStatement pstmt = source
			 .prepareStatement("update PROCEEDINGSPAPER set CONTROLNUMBER=? where RESULTID=?");
			StringBuffer query = new StringBuffer();
			query
					.append("select RESULTID, TITLE, AUTHORS, PROCEEDINGSNAME, STARTPAGE, ENDPAGE, PUBLICATIONYEAR, R from PROCEEDINGSPAPER where CONTROLNUMBER not like '(BISIS)%' and ID > 10999 group by RESULTID order by ID");
			Statement stmt = source.createStatement();
			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				try {
					Integer resultId = rset.getInt(1);
					String title = rset.getString(2);
					String authors = rset.getString(3);
					String proceedingsOrConference = rset.getString(4);
					String startPage = rset.getString(5);
					String endPage = rset.getString(6);
					Integer publicationYear = rset.getInt(7);
					String r = rset.getString(8);
					
					PaperProceedingsDTO paperProceedingsDTO = new PaperProceedingsDTO();
					if ((title == null) || (title.trim().equals(""))){
						throw new Exception("Nema naslova!!!");
					}
					paperProceedingsDTO.getTitle().setContent(title.trim());
					
					paperProceedingsDTO.setProceedings(loadProceedings(proceedingsOrConference, publicationYear));
					
					setMainAndOtherAuthors(paperProceedingsDTO, authors, resultId);
					
					if ((startPage != null) && (! (startPage.trim().equals(""))))
						paperProceedingsDTO.setStartPage(startPage.replace("-", "").trim());
					if ((endPage != null) && (! (endPage.trim().equals(""))))
						paperProceedingsDTO.setEndPage(endPage.trim());
					
					
					if("53".equals(r) || "64".equals(r))
						paperProceedingsDTO.setPaperType("records.paperProceedings.editPanel.paperType.invitedTalkFull");
					else if("71".equals(r))
						paperProceedingsDTO.setPaperType("records.paperProceedings.editPanel.paperType.invitedTalkAbstract");
					else if("54".equals(r) || "65".equals(r))
						paperProceedingsDTO.setPaperType("records.paperProceedings.editPanel.paperType.full");
					else if("72".equals(r) || "73".equals(r))
						paperProceedingsDTO.setPaperType("records.paperProceedings.editPanel.paperType.abstract");
					
					BooleanQuery bq = new BooleanQuery();
					bq.add(QueryUtils.parseQuery("TI: (" +  QueryParser.escape(StringUtils.clearDelimiters(paperProceedingsDTO.getTitle().getContent().trim(), delims))+ ")", new CrisAnalyzer(), Operator.AND), Occur.MUST);
					bq.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.MUST);
					List<Record> list = recordDAO.getDTOs(bq, new AllDocCollector(true));
					boolean alreadyExists = false;
					for (Record record : list) {
						PaperProceedingsDTO dto = (PaperProceedingsDTO) record.getDto();
						if (dto != null){
							if((paperProceedingsDTO.getTitle().equals(dto.getTitle())) && (paperProceedingsDTO.getProceedings().getControlNumber().equals(dto.getProceedings().getControlNumber()) && (paperProceedingsDTO.getAllAuthors().size() == dto.getAllAuthors().size()))){
								alreadyExists = true;
								paperProceedingsDTO.setControlNumber(dto.getControlNumber());
								System.out.println("Vec postoji: " + dto.getStringRepresentation());
							}
						}
					}
					
					if ((alreadyExists == false) && (recordDAO.add(new Record("knr", new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
							paperProceedingsDTO)) == false)){
						retVal = false;
						break;
					} else {
						System.out.println("paperAdded: " + paperProceedingsDTO.getStringRepresentation());
						pstmt.setInt(2, rset.getInt(1));
						pstmt.setString(1, paperProceedingsDTO.getControlNumber());
						pstmt.executeUpdate();
					}
				} catch (Exception e){
					pstmt.setInt(2, rset.getInt(1));
					pstmt.setString(1, e.toString());
					pstmt.executeUpdate();
					System.out.println(e.toString());
					log.info(e);
				}
			}
			recordDAO.optimizeIndexer();
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.fatal("Cannot read multiple papers");
			log.fatal(ex);
			return false;
		}
	}
	
	
	
	private void setMainAndOtherAuthors(PaperProceedingsDTO paperProceedingsDTO,
			String authors, Integer resultId) throws Exception{
		RecordDAO recDAO = new RecordDAO(new PersonDB());
		List<AuthorDTO> authorsList = new ArrayList<AuthorDTO>();
		List<AuthorNameDTO> parsedAuthorsNames= parseAuthors(authors);
		for(int i = 0; i < parsedAuthorsNames.size(); i++){
			authorsList.add(new AuthorDTO());
		}	
		StringBuffer query = new StringBuffer();
		query.append("select a.FIRSTNAME, a.LASTNAME, a.CONTROLNUMBER from PROCEEDINGSPAPER pp, AUTHOR a where pp.AUID = a.AUID and RESULTID = " + resultId);
		Statement stmt = source.createStatement();
		ResultSet rset = stmt.executeQuery(query.toString());
		while (rset.next()) {
			boolean found = false;
			for (int i = 0; i < parsedAuthorsNames.size(); i++) {
				AuthorNameDTO authorNameDTO = parsedAuthorsNames.get(i);
				if(ImportAuthorsFromKNRTask.findAuthor(authorNameDTO, rset.getString(1), rset.getString(2))){
					found = true;
					AuthorDTO addAuthor = (AuthorDTO)recDAO.getDTO(rset.getString(3));
					addAuthor.setName(authorNameDTO);
					authorsList.set(i, addAuthor);
					break;
				}						
			}
			if(found==false){
				authorsList.add((AuthorDTO)recDAO.getDTO(rset.getString(3)));
			}
			
		}
		for (int i = 0; i < parsedAuthorsNames.size(); i++) {
			AuthorNameDTO authorNameDTO = parsedAuthorsNames.get(i);
			if(authorsList.get(i).getControlNumber() != null)
				continue;
			List<AuthorDTO> similarAuthors = new ArrayList<AuthorDTO>();
			BooleanQuery bq = new BooleanQuery();
			bq.add(QueryUtils.parseQuery("AU:(" + QueryParser.escape(StringUtils.clearDelimiters(authorNameDTO.getLastname().trim(), delims)) + ")", new CrisAnalyzer(), Operator.AND), Occur.MUST);
			bq.add(new TermQuery(new Term("TYPE", Types.AUTHOR)), Occur.MUST);
			List<Record> list = recDAO.getDTOs(bq, new AllDocCollector(true));
			for (Record record : list) {
				AuthorDTO dto = (AuthorDTO) record.getDto();
				if (dto != null)
					similarAuthors.add(dto);
			}
			List<AuthorDTO> appropriateAuthors = new ArrayList<AuthorDTO>();
			for (AuthorDTO aut : similarAuthors) {
				if(ImportAuthorsFromKNRTask.findAuthor(authorNameDTO, aut.getAllNames())){
					aut.setName(authorNameDTO);
					appropriateAuthors.add(aut);
				}
			
			}
			if(appropriateAuthors.size() == 1){
				authorsList.set(i, appropriateAuthors.get(0));
			}
			else if (appropriateAuthors.size() == 0){
				AuthorDTO addAuthorDTO = new AuthorDTO();
				addAuthorDTO.setName(authorNameDTO);
				if((!(authorNameDTO.getFirstname().equals(LatCyrUtils.toLatinUnaccented(authorNameDTO.getFirstname())))) || (!(authorNameDTO.getLastname().equals(LatCyrUtils.toLatinUnaccented(authorNameDTO.getLastname()))))){
					List<AuthorNameDTO> otherFormatNames = new ArrayList<AuthorNameDTO>();
					otherFormatNames.add(new AuthorNameDTO(LatCyrUtils.toLatinUnaccented(authorNameDTO.getFirstname()), LatCyrUtils.toLatinUnaccented(authorNameDTO.getLastname()), ""));
					addAuthorDTO.setOtherFormatNames(otherFormatNames);
				}
				if (recDAO.add(new Person("knr", new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.PERSON, 
						addAuthorDTO, null, null, null, null)) == false) {
					throw new Exception("Dodavanje autora neuspesno: " + addAuthorDTO);
				} else {
					System.out.println("Author added: " + addAuthorDTO);
				}
				authorsList.set(i, addAuthorDTO);
			} else {
				throw new Exception("Nije jednoznacno: " + authorNameDTO);
			}
		}
		paperProceedingsDTO.setMainAuthor(authorsList.get(0));
		authorsList.remove(0);
		paperProceedingsDTO.setOtherAuthors(authorsList);
	}
	
	private List<AuthorNameDTO> parseAuthors(String authors) throws Exception{
		authors = authors.replace(""+'\u00D0', ""+'\u0110').replace(""+'\u00D1', ""+'\u0111');
		String latinUnaccentedAuthors = LatCyrUtils.toLatinUnaccented(authors);
		List<AuthorNameDTO> retVal = null;
		System.out.println(latinUnaccentedAuthors);
		if(authors1.matcher(latinUnaccentedAuthors).matches()) {
			retVal = new ArrayList<AuthorNameDTO>();
			String[] authorsList = authors.split("(,)|(\\sand\\s)|(\\si\\s)");
			for (String author : authorsList) {
				if((author == null) || (author.trim().length()==0)) 
					continue;
				author = author.trim();
				String lastName = author.substring(0, author.indexOf(" ")).trim().replace(" ", "-");
				String firstName = author.substring(author.indexOf(" ")+1).trim();
				retVal.add(new AuthorNameDTO(firstName, lastName, ""));
			}
		} else if(authors2.matcher(latinUnaccentedAuthors).matches()) {
			retVal = new ArrayList<AuthorNameDTO>();
			String[] authorsList = authors.split("(,)|(\\sand\\s)|(\\si\\s)");
			for (String author : authorsList) {
				if((author == null) || (author.trim().length()==0)) 
					continue;
				author = author.trim();
				String lastName = author.substring(0, author.indexOf(" ")).trim().replace(" ", "-");
				String firstName = author.substring(author.indexOf(" ")+1).trim();
				retVal.add(new AuthorNameDTO(firstName, lastName, ""));
			}
		}
		else if (authors3.matcher(latinUnaccentedAuthors).matches()) {
			retVal = new ArrayList<AuthorNameDTO>();
			String[] authorsList = authors.split("(,)|(\\sand\\s)|(\\si\\s)");
			int i = 0;
			String lastName = null;
			String firstName = null;
			for (String author : authorsList) {
				if((author == null) || (author.trim().length()==0)) 
					continue;
				author = author.trim();
				if(i%2==0)
					lastName = author.trim().replace(" ", "-");
				else {
					firstName = author.trim();
					retVal.add(new AuthorNameDTO(firstName, lastName, ""));
				}
				i++;
			}
			if(i%2==1)
				throw new Exception("Nemoguce parsiranje autora: " + authors);
		}
		else if (authors4.matcher(latinUnaccentedAuthors).matches()) {
			retVal = new ArrayList<AuthorNameDTO>();
			String[] authorsList = authors.split("(,)|(\\sand\\s)|(\\si\\s)");
			int i = 0;
			String lastName = null;
			String firstName = null;
			for (String author : authorsList) {
				if((author == null) || (author.trim().length()==0)) 
					continue;
				author = author.trim();
				if(i%2==0)
					lastName = author.trim().replace(" ", "-");
				else {
					firstName = author.trim();
					retVal.add(new AuthorNameDTO(firstName, lastName, ""));
				}
				i++;
			}
			if(i%2==1)
				throw new Exception("Nemoguce parsiranje autora: " + authors);
		}
		else if (authors5.matcher(latinUnaccentedAuthors).matches()) {
			retVal = new ArrayList<AuthorNameDTO>();
			String[] authorsList = authors.split("(,)|(\\sand\\s)|(\\si\\s)");
			for (String author : authorsList) {
				if((author == null) || (author.trim().length()==0)) 
					continue;
				author = author.trim();
				String firstName = author.substring(0, author.lastIndexOf(".")).trim();
				String lastName = author.substring(author.lastIndexOf(".")+1).trim().replace(" ", "-");
				retVal.add(new AuthorNameDTO(firstName, lastName, ""));
			}
		}
		else if (authors6.matcher(latinUnaccentedAuthors).matches()) {
			retVal = new ArrayList<AuthorNameDTO>();
			String[] authorsList = authors.split("(\\.,)|(\\.\\s,)|(\\sand\\s)|(\\si\\s)");
			for (String author : authorsList) {
				if((author == null) || (author.replaceAll(":", "").trim().length()==0)) 
					continue;
				author = author.trim();
				System.out.println(author);
				
				String firstName = author.split("[,\\s]+")[1].trim();
				String lastName = author.split("[,\\s]+")[0].trim().replace(" ", "-");
				retVal.add(new AuthorNameDTO(firstName, lastName, ""));
			}
		} else if(authors7.matcher(latinUnaccentedAuthors).matches()) {
			retVal = new ArrayList<AuthorNameDTO>();
			String[] authorsList = authors.split("(,)|(\\sand\\s)|(\\si\\s)");
			for (String author : authorsList) {
				if((author == null) || (author.trim().length()==0)) 
					continue;
				author = author.trim();
				String lastName = author.substring(0, author.indexOf(" ")).trim().replace(" ", "-");
				String firstName = author.substring(author.indexOf(" ")+1).trim();
				retVal.add(new AuthorNameDTO(firstName, lastName, ""));
			}
		}
		if(retVal == null)
			throw new Exception("Nemoguce parsiranje autora: " + authors);
		return retVal;
		
	}
	private ProceedingsDTO loadProceedings(String proceedingsOrConference, Integer publicationYear) throws Exception{
		ProceedingsDTO retVal = null;
		try {
			PreparedStatement pstmt = source
			.prepareStatement("select CONTROLNUMBER from CONFERENCEPROCEEDINGS where PROCEEDINGSORCONFERENCENAME like ? and YEAR = ?");
			pstmt.setString(1, proceedingsOrConference);
			pstmt.setInt(2, publicationYear);
			ResultSet rset = pstmt.executeQuery();
			rset.next();
			retVal = (ProceedingsDTO) recordDAO.getDTO(rset.getString(1));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(proceedingsOrConference);
			throw e;
		}
		if(retVal == null)
			throw new Exception("Nije pronadjen zbornik sa konferencije: " + proceedingsOrConference);
		else
			return retVal;
	}

	private static String delims = ",;:\"()[]{}+/.!-";

	private Connection source;
	
	private static Log log = LogFactory.getLog(ImportJournalPapersFromKNRTask.class.getName());

}
