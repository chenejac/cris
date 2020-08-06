/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.MyDataSource;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.ResearchAreaDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.ImportRecordsDTO;

/**
 * @author Dragan Ivanovic
 *
 */
public class AndrejevicSerializer implements GroupSerializer{
	
	@Override
	public OutputStream exportRecords(ImportRecordsDTO records) {
		return null;
	}

	@Override
	public ImportRecordsDTO importRecords(InputStream is) {
		ImportRecordsDTO retVal = new ImportRecordsDTO();
		if(loadInstitutions(retVal))
			if(loadAuthors(retVal))
				if(loadMonograph(retVal))
					return retVal;
		return new ImportRecordsDTO();
	}
	
	private boolean loadInstitutions(ImportRecordsDTO importRecordDTO){
		
		boolean retVal = false;
		List<RecordDTO> institutions = new ArrayList<RecordDTO>();
		try {
			Connection conn = dataSource.getConnection();
			StringBuffer query = new StringBuffer();
			query.append("select f.IDFaksa, f.`Naziv faksa` from andrejevic.Fakulteti f");
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				int idFaksa = rset.getInt(1);
				String naziv = rset.getString(2);
				InstitutionDTO institution = new InstitutionDTO();
				institution.setControlNumber("ins"+idFaksa);
				institution.getName().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
				if(naziv.indexOf(',') != -1){
					String mesto = naziv.split(",")[0];
					String samoNaziv = naziv.split(",")[1];
					if(samoNaziv.contains("Univerzitet"))
						institution.setSuperInstitution((InstitutionDTO)recordDAO.getDTO("(BISIS)6705"));
					else if(mesto.equals("Beograd")){
						institution.setSuperInstitution((InstitutionDTO)recordDAO.getDTO("(BISIS)6762"));
					} else if ((mesto.equals("Novi Sad")) || (mesto.equals("Subotica")) || (mesto.equals("Zrenjanin"))){
						institution.setSuperInstitution((InstitutionDTO)recordDAO.getDTO("(BISIS)5920"));
					} else if (mesto.equals("Niš")){
						institution.setSuperInstitution((InstitutionDTO)recordDAO.getDTO("(BISIS)67420"));
					} else if (mesto.equals("Kragujevac")){
						institution.setSuperInstitution((InstitutionDTO)recordDAO.getDTO("(BISIS)27367"));
					} else {
						institution.setSuperInstitution((InstitutionDTO)recordDAO.getDTO("(BISIS)6705"));
					}
					institution.setPlace(mesto);
					institution.getName().setContent(samoNaziv);
				} else {
					if(naziv.equals("Inostranstvo")){
						institution =(InstitutionDTO)recordDAO.getDTO("(BISIS)6706");
					} else {
						institution.getName().setContent(naziv);
					}
				}
				institutions.add(institution);
			}
			importRecordDTO.setAllRecords(institutions);
			retVal = true;
			rset.close();
			stmt.close();
			conn.close();
			System.out.println("Institution loaded " + importRecordDTO.getAllRecords().size());
			return retVal;
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot load institutions");
		}
		return retVal;
	}
	
	private boolean loadAuthors(ImportRecordsDTO importRecordDTO){
		boolean retVal = false;
		List<RecordDTO> authors = new ArrayList<RecordDTO>();
		try {
			Connection conn = dataSource.getConnection();
			StringBuffer query = new StringBuffer();
			query.append("SELECT a.`ID autora`, m.`Naziv mesta`, t.`Naziv titule`, z.`naziv zaposlenja`, zm.`Naziv mesta`, a.`Ime`, a.`Prezime`, a.`Pol`, a.`Godina rodjenja`, a.`Adresa`, a.`Telefon`, a.`Email`, a.`GodinaZvanja`, a.`IDfaksa` " +
							" FROM andrejevic.Autori a left join andrejevic.Mesta m on a.`ID mesta` = m.`ID mesta` left join andrejevic.Titule t on a.`ID titule` = t.`ID titule` left join andrejevic.Zaposlenje z  on a.`ID zaposlenja` = z.`ID zaposlenja` left join andrejevic.Mesta zm on z.`ID mesta` = zm.`ID mesta`;");
			Statement stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				int idAut = rset.getInt(1);
				String mesto = rset.getString(2);
				String titula = rset.getString(3);
				if(titula == null)
					titula = "";
				if(titula.equalsIgnoreCase("doktor nauka")){
					titula = "dr";
				} else if(titula.equalsIgnoreCase("magistar")){
					titula = "mr";
				} else if(titula.equalsIgnoreCase("master")){
					titula = "MSc";
				} else if(titula.equalsIgnoreCase("akademik")){
					titula = "akademik";
				} else titula = "";
				String institucijaString = rset.getString(4);
				InstitutionDTO institution = new InstitutionDTO();
				institution.getName().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
				institution.getName().setContent(institucijaString);
				institution.setPlace(rset.getString(5));
				String ime = rset.getString(6);
				String prezime = rset.getString(7);
				String polString = rset.getString(8);
				char pol = 'n';
				if(polString.equalsIgnoreCase("zenski"))
					pol = 'f';
				else if (polString.equalsIgnoreCase("muski"))
						pol = 'm'; 
				Integer godinaRodjenja = rset.getInt(9);
				if(! rset.wasNull()){
					if(godinaRodjenja != 0)
						godinaRodjenja += 1900;
				} else 
					godinaRodjenja = null;
				String adresa = rset.getString(10);
				String telefon = rset.getString(11);
				String email = rset.getString(12);
				
				Integer godinaZvanja = rset.getInt(13);
				if(! rset.wasNull()){
					if(godinaZvanja < 15)
						godinaZvanja += 2000;
					else 
						godinaZvanja += 1900;
				} else 
					godinaZvanja = null;
				String idFaksa = null; 
				int idFaksaInt = rset.getInt(14);
				if(! rset.wasNull())
					idFaksa = "ins" + idFaksaInt;
				
				
				AuthorDTO author = new AuthorDTO();
				author.setControlNumber("aut"+idAut);
				author.setCity(mesto);
				author.setTitle(titula);
				author.setInstitution(institution);
				author.setName(new AuthorNameDTO(ime, prezime, ""));
				author.setSex(pol);
				author.setYearOfBirth(godinaRodjenja);
				author.setAddress(adresa);
				author.setDirectPhones(telefon);
				author.setEmail(email);
				author.getTitleInstitution().setYear(godinaZvanja);
				author.getTitleInstitution().getInstitution().setControlNumber(idFaksa);
				
				
				RecordDTO faculty = (RecordDTO)author.getTitleInstitution().getInstitution();
				if((faculty != null) && (importRecordDTO.getAllRecords().indexOf(faculty) != -1)){
					importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(faculty)).addRelatedRecord(author);
				}
				
				authors.add(author);
			}
			rset.close();
			stmt.close();
			
			
			importRecordDTO.getAllRecords().addAll(authors);
			importRecordDTO.setAllRecords(importRecordDTO.getAllRecords());
			retVal = true;
			rset.close();
			stmt.close();
			conn.close();
			System.out.println("authors loaded " + importRecordDTO.getAllRecords().size());
			
			return retVal;
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot load authors");
		}
		return retVal;
	}
	
	private boolean loadMonograph(ImportRecordsDTO importRecordDTO){
		boolean retVal = false;
		List<RecordDTO> monographs = new ArrayList<RecordDTO>();
		ResearchAreaDAO researchAreaDAO = new ResearchAreaDAO();
		try {
			Connection conn = dataSource.getConnection();
			StringBuffer query = new StringBuffer();
			query.append("SELECT k.`ID knjige`, b.`Naziv`, b.`ISSN broj`, f.`Vrednost formata`, j.`Naziv jezika`, k.`ID grane`, k.`Broj knjige`, " +
					"k.`Godina objavljivanja`, k.`Broj strana`, k.`ISBN broj`, k.`Naziv`, k.`Opis`, k.`Pismo` " +
					"FROM andrejevic.Knjige k left join andrejevic.Biblioteke b on k.`ID biblioteke` = b.`ID biblioteke` left join andrejevic.Formati f on k.`ID formata` = f.`ID formata` " +
					"left join andrejevic.Dvojezicna j on k.`ID jezika` = j.`ID jezika`");
			
			

			Statement stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				MonographDTO monograph = new MonographDTO();
				int sifraDok = rset.getInt(1);
				String nazivBiblioteke = rset.getString(2); 
				String issnBiblioteke = rset.getString(3);
				String format = rset.getString(4);
				if(format!=null){
					format = format.split("x")[1];
					format = new Integer((int)(Math.round(Integer.parseInt(format) / 10.0))).toString();
				}
				String jezik = rset.getString(5);
				List<String> jeziciSrp = new ArrayList<String>();
				jeziciSrp.add("srp");
				List<String> ostaliJezici = new ArrayList<String>();
				if(jezik.equals("engleski"))
					ostaliJezici.add("eng");
				else if(jezik.equals("nemački"))
					ostaliJezici.add("ger"); 
				else if(jezik.equals("francuski"))
					ostaliJezici.add("fre"); 
				else if(jezik.equals("ruski"))
					ostaliJezici.add("rus"); 
				else if(jezik.equals("mađarski-engleski")){
					ostaliJezici.add("hun");
					ostaliJezici.add("eng");
				} else if(jezik.equals("makedonski"))
					ostaliJezici.add("mac");
				String naucnaGrana = rset.getString(6);
				ResearchAreaDTO researchArea = new ResearchAreaDTO(); 
				if(naucnaGrana != null)
					researchArea = researchAreaDAO.getResearchArea("andGr" + naucnaGrana);
				String brojKnjige = rset.getString(7);
				if((brojKnjige != null) && (brojKnjige.length()>=4))
					brojKnjige = brojKnjige.substring(brojKnjige.length()-3);
				else 
					brojKnjige = null;
				Integer godina = rset.getInt(8);
				if(rset.wasNull())
					godina = null;
				Integer brojStrana = rset.getInt(9);
				if(rset.wasNull())
					brojStrana = null;
				String isbn = rset.getString(10);
				if(isbn != null)
					isbn = isbn.replace("-", "");
				String naslov = rset.getString(11);
				String opis = rset.getString(12);
				String pismo = rset.getString(13);
				
				monograph.setControlNumber("m"+sifraDok);
				monograph.setEditionTitle(nazivBiblioteke);
				monograph.setEditionISSN(issnBiblioteke);
				monograph.setDimension(format);
				monograph.setLanguages(jeziciSrp);
				monograph.setOriginalLanguages(ostaliJezici);
				monograph.setResearchArea(researchArea);
				monograph.getKeywords().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
				monograph.getKeywords().setContent(researchArea.getFullHierarchy());
				if(brojKnjige!=null)
					monograph.setEditionNumber(Integer.parseInt(brojKnjige));
				monograph.setPublicationYear(godina.toString());
				monograph.setNumberOfPages(brojStrana);
				monograph.setIsbn(isbn);
				monograph.getTitle().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
				monograph.getTitle().setContent(naslov);
				monograph.getAbstracT().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
				monograph.getAbstracT().setContent(opis);
				monograph.setAlphabet(pismo);
				monograph.getPublisher().getOriginalPublisher().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
				monograph.getPublisher().getOriginalPublisher().setName("Zadužbina Andrejević");
				monograph.getPublisher().getOriginalPublisher().setPlace("Beograd");
				monograph.getPublisher().getOriginalPublisher().setState("Srbija");
				if(loadMonographAuthors(monograph, importRecordDTO))
						monographs.add(monograph);
			}
			rset.close();
			stmt.close();
			
			
			importRecordDTO.getAllRecords().addAll(monographs);
			importRecordDTO.setAllRecords(importRecordDTO.getAllRecords());
			retVal = true;
			rset.close();
			stmt.close();
			conn.close();
			System.out.println("Monographs loaded " + importRecordDTO.getAllRecords().size());
			
			return retVal;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.fatal(ex);
			log.fatal("Cannot load monographs");
		}
		return retVal;
	}

	private boolean loadMonographAuthors(MonographDTO monograph, ImportRecordsDTO importRecordDTO) {
		boolean retVal = false;
		try {
			Connection conn = dataSource.getConnection();
			StringBuffer query = new StringBuffer();
			query.append("SELECT k.`ID autora` FROM andrejevic.Knjige_autori k where k.`ID knjige` = " + monograph.getControlNumber().substring(1));
			Statement stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(query.toString());
			boolean firstAuthor = true;
			while (rset.next()) {
				AuthorDTO author = new AuthorDTO();
				author.setControlNumber("aut"+rset.getInt(1));
				if(firstAuthor){
					monograph.setMainAuthor(author);
					firstAuthor = false;
				} else {
					monograph.getOtherAuthors().add(author);
				}
				RecordDTO superAuthor = (RecordDTO)author;
				if((superAuthor != null) && (importRecordDTO.getAllRecords().indexOf(superAuthor) != -1)){
					importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(superAuthor)).addRelatedRecord(monograph);
				}
			}
			rset.close();
			stmt.close();
			conn.close();
			
			retVal = true;
			return retVal;
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot load authors");
		}
		return retVal;
	}

//	private DataSource dataSource = new MyDataSource("localhost", "3306", "andrejevic", "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8", "root", "root");
	private DataSource dataSource = new MyDataSource("localhost", "3306", "andrejevic", "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8", "root", "zoinks");
	
	private static Log log = LogFactory.getLog(AndrejevicSerializer.class.getName());
	
	private RecordDAO recordDAO = new RecordDAO(new RecordDB());

}
