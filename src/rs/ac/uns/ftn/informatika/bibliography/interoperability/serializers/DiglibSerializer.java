/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.MyDataSource;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.ImportRecordsDTO;

/**
 * @author Dragan Ivanovic
 *
 */
public class DiglibSerializer implements GroupSerializer{
	
	@Override
	public OutputStream exportRecords(ImportRecordsDTO records) {
		return null;
	}

	@Override
	public ImportRecordsDTO importRecords(InputStream is) {
		ImportRecordsDTO retVal = new ImportRecordsDTO();
		if(loadInstitutions(retVal))
			if(loadAuthors(retVal))
				if(loadTheses(retVal))
					return retVal;
		return new ImportRecordsDTO();
	}
	
	private boolean loadInstitutions(ImportRecordsDTO importRecordDTO){
		boolean retVal = false;
		List<RecordDTO> institutions = new ArrayList<RecordDTO>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			StringBuffer query = new StringBuffer();
			query
					.append("select SIFRA_UNIV, EMAIL, TELEFON, ULICA_I_BROJ, MESTO from UNIVERZITET");
			Statement stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				int sifraUniv = rset.getInt(1);
				String email = rset.getString(2);
				String telefon = rset.getString(3);
				String ulicaIBroj = rset.getString(4);
				String mesto = rset.getString(5);
				InstitutionDTO institution = new InstitutionDTO();
				institution.setControlNumber(""+sifraUniv);
				institution.setPlace(mesto);
				institution.setSuperInstitution(new InstitutionDTO());
				if(! loadUniversityNames(institution))
					return retVal;
				institutions.add(institution);
			}
			rset.close();
			stmt.close();
			
			query = new StringBuffer();
			query
					.append("select SIFRA_UNIV, EMAIL, TELEFON, ULICA_I_BROJ, MESTO, SIFRA_FAK from FAKULTET");
			stmt = conn.createStatement();

			rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				int sifraUniv = rset.getInt(1);
				String email = rset.getString(2);
				String telefon = rset.getString(3);
				String ulicaIBroj = rset.getString(4);
				String mesto = rset.getString(5);
				int sifraFak = rset.getInt(6);
				InstitutionDTO institution = new InstitutionDTO();
				institution.setControlNumber(""+sifraFak);
				institution.setPlace(mesto);
				institution.setSuperInstitution(new InstitutionDTO(""+sifraUniv));
				if(! loadFacultyNames(institution))
					return retVal;
				RecordDTO superInstitution = institution.getSuperInstitution();
				if((superInstitution != null) && (institutions.indexOf(superInstitution) != -1))
					institutions.get(institutions.indexOf(superInstitution)).addRelatedRecord(institution);
				institutions.add(institution);
			}
			importRecordDTO.setAllRecords(institutions);
			retVal = true;
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot load institutions");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException throwables) {
				}
			}
		}
		return retVal;
	}
	
	private boolean loadUniversityNames(InstitutionDTO institution) {
		boolean retVal = false;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			StringBuffer query = new StringBuffer();
			query
					.append("select NAZIV_UNIVERZITETA, SIFRA_JEZIKA from NAZIV_UNIV where SIFRA_UNIV = " + institution.getControlNumber());
			Statement stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				int sifraJezika = rset.getInt(2);
				String naziv = rset.getString(1);
				if(sifraJezika == 1){
					institution.setName(new MultilingualContentDTO(naziv, "srp", MultilingualContentDTO.TRANS_ORIGINAL));
				} else {
					List<MultilingualContentDTO> mcList = new ArrayList<MultilingualContentDTO>();
					mcList.add(new MultilingualContentDTO(naziv, "eng", MultilingualContentDTO.TRANS_HUMAN));
					institution.setNameTranslations(mcList);
				}
			}
			retVal = true;
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot load university names");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException throwables) {

				}
			}
		}
		return retVal;
	}
	
	private boolean loadFacultyNames(InstitutionDTO institution) {
		boolean retVal = false;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			StringBuffer query = new StringBuffer();
			query
					.append("select NAZIV_FAKULTETA, SIFRA_JEZIKA from NAZIV_FAK where SIFRA_FAK = " + institution.getControlNumber());
			Statement stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				int sifraJezika = rset.getInt(2);
				String naziv = rset.getString(1);
				if(sifraJezika == 1){
					institution.setName(new MultilingualContentDTO(naziv, "srp", MultilingualContentDTO.TRANS_ORIGINAL));
				} else {
					List<MultilingualContentDTO> mcList = new ArrayList<MultilingualContentDTO>();
					mcList.add(new MultilingualContentDTO(naziv, "eng", MultilingualContentDTO.TRANS_HUMAN));
					institution.setNameTranslations(mcList);
				}
			}
			retVal = true;
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot load institution names");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException throwables) {

				}
			}
		}
		return retVal;
	}
	
	private boolean loadAuthors(ImportRecordsDTO importRecordDTO){
		boolean retVal = false;
		List<RecordDTO> authors = new ArrayList<RecordDTO>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			StringBuffer query = new StringBuffer();
			query
					.append("select distinct PREZIME_MENTORA, IME_MENTORA from DOKUMENT where STATUS_DOKUMENTA = 0 order by PREZIME_MENTORA");
			Statement stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				String prezime = rset.getString(1);
				String ime = rset.getString(2);
				AuthorDTO author = new AuthorDTO();
				author.setName(new AuthorNameDTO(ime, prezime, ""));
				author.setControlNumber(prezime+ime);
				authors.add(author);
			}
			rset.close();
			stmt.close();
			
			query = new StringBuffer();
			query
				.append("select distinct PREZIME, IME from CLAN_KOMISIJE c, DOKUMENT d where c.SIFRA_DOK = d.SIFRA_DOK and d.STATUS_DOKUMENTA order by PREZIME");
			stmt = conn.createStatement();

			rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				String prezime = rset.getString(1);
				String ime = rset.getString(2);
				AuthorDTO author = new AuthorDTO();
				author.setName(new AuthorNameDTO(ime, prezime, ""));
				author.setControlNumber(prezime+ime);
				if(authors.indexOf(author)==-1)
					authors.add(author);
			}
			rset.close();
			stmt.close();
	
			query = new StringBuffer();
			query
				.append("select a.SIFRA_AUTORA, a.PREZIME, a.IME, a.ZVANJE, a.TELEFON from AUTOR a, DOKUMENT d where a.SIFRA_AUTORA = d.SIFRA_AUTORA and d.STATUS_DOKUMENTA = 0 group by a.SIFRA_AUTORA, a.PREZIME, a.IME, a.ZVANJE, a.TELEFON order by a.PREZIME");
			stmt = conn.createStatement();

			rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				int sifraAutora = rset.getInt(1);
				String prezime = rset.getString(2);
				String ime = rset.getString(3);
				String zvanje = rset.getString(3);
				String telefon = rset.getString(4);
				AuthorDTO author = new AuthorDTO();
				author.setCurrentPositionName(zvanje);
				author.setDirectPhones(telefon);
				author.setName(new AuthorNameDTO(ime, prezime, ""));
				author.setControlNumber("a" + sifraAutora);
				authors.add(author);
			}
			importRecordDTO.getAllRecords().addAll(authors);
			importRecordDTO.setAllRecords(importRecordDTO.getAllRecords());
			retVal = true;
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot load authors");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException throwables) {

				}
			}
		}
		return retVal;
	}
	
	private boolean loadTheses(ImportRecordsDTO importRecordDTO){
		boolean retVal = false;
		List<RecordDTO> theses = new ArrayList<RecordDTO>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			StringBuffer query = new StringBuffer();
			query
					.append("select d.SIFRA_DOK, d.PREZIME_MENTORA, d.IME_MENTORA, nt.NAZIV_TITULE, nz.NAZIV_ZVANJA, b.SIFRA_FAK, " +
							"       d.BROJ_PRILOGA, d.BROJ_GRAFIKONA, d.BROJ_SLIKA, d.BROJ_TABELA, d.BROJ_REFERENCI, d.BROJ_STRANA, d.BROJ_POGLAVLJA, " +
							"		d.DATUM_PRIHVATANJA, d.DATUM_ODBRANE, d.UDK_BROJ, d.SIFRA_JEZIKA, d.SIFRA_AUTORA, nuno.NAZIV_UN_OBL, nno.NAZ_N_OBL, b.SIFRA_FAK, " +
							"		nb.NAZIV_BIBLIOTEKE, d.SIFRA_VR_RADA, " +
							"		ndsr.TEKST_NASLOVA, nden.TEKST_NASLOVA, krsr.KLJUCNE_RECI, kren.KLJUCNE_RECI, " +
							"		nasr.TEKST_NAPOMENE, naen.TEKST_NAPOMENE, izsr.TEKST_IZVODA, izen.TEKST_IZVODA " +
							"	from DOKUMENT d left join NAZIV_TITULE nt on (d.SIFRA_TITULE = nt.SIFRA_TITULE and nt.SIFRA_JEZIKA = 1) " +
							"			left join NAZIV_ZVANJA nz on (d.SIFRA_ZVANJA = nz.SIFRA_ZVANJA and nz.SIFRA_JEZIKA = 1) " +
							"			left join BIBLIOTEKA b on (d.SIFRA_BIB = b.SIFRA_BIB) " +
							"			left join NAZIV_BIB nb on (d.SIFRA_BIB = nb.SIFRA_BIB and nb.SIFRA_JEZIKA = 1) " +
							"			left join NAZIV_N_OBL nno on (d.SIFRA_OBLASTI = nno.SIFRA_OBLASTI and nno.SIFRA_JEZIKA = 1) " +
							"			left join NAZIV_UN_OBL nuno on (d.SIFRA_UZE_OBL = nuno.SIFRA_UZE_OBL and nuno.SIFRA_JEZIKA = 1) " +
							"			left join NAS_DOK ndsr on (d.SIFRA_DOK = ndsr.SIFRA_DOK and ndsr.SIFRA_JEZIKA = 1) " +
							"			left join NAS_DOK nden on (d.SIFRA_DOK = nden.SIFRA_DOK and nden.SIFRA_JEZIKA = 2) " +
							"			left join KLJ_REC krsr on (d.SIFRA_DOK = krsr.SIFRA_DOK and krsr.SIFRA_JEZIKA = 1) " +
							"			left join KLJ_REC kren on (d.SIFRA_DOK = kren.SIFRA_DOK and kren.SIFRA_JEZIKA = 2) " +
							"			left join NAPOMENE nasr on (d.SIFRA_DOK = nasr.SIFRA_DOK and nasr.SIFRA_JEZIKA = 1) " +
							"			left join NAPOMENE naen on (d.SIFRA_DOK = naen.SIFRA_DOK and naen.SIFRA_JEZIKA = 2) " +
							"			left join IZV_DOK izsr on (d.SIFRA_DOK = izsr.SIFRA_DOK and izsr.SIFRA_JEZIKA = 1) " +
							"			left join IZV_DOK izen on (d.SIFRA_DOK = izen.SIFRA_DOK and izen.SIFRA_JEZIKA = 2) " +
							"		where d.STATUS_DOKUMENTA = 0 and ((ndsr.TEKST_NASLOVA is not null) or (nden.TEKST_NASLOVA is not null))");
			
			

			Statement stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				StudyFinalDocumentDTO thesis = new StudyFinalDocumentDTO();
				int sifraDok = rset.getInt(1);
				String prezimeMentora = rset.getString(2);
				String imeMentora = rset.getString(3);
				AuthorDTO mentor = new AuthorDTO();
				mentor.setName(new AuthorNameDTO(imeMentora, prezimeMentora, ""));
				mentor.setControlNumber(prezimeMentora+imeMentora);
				mentor.setTitle(rset.getString(4));
				mentor.getCurrentPosition().getPosition().getTerm().setContent(rset.getString(5));
				mentor.getInstitution().setControlNumber(""+rset.getInt(6));
				RecordDTO superInstitution = (RecordDTO)mentor.getInstitution();
				if((superInstitution != null) && (importRecordDTO.getAllRecords().indexOf(superInstitution) != -1))
					importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(superInstitution)).addRelatedRecord(mentor);
				RecordDTO superAuthor = (RecordDTO)mentor;
				if((superAuthor != null) && (importRecordDTO.getAllRecords().indexOf(superAuthor) != -1)){
					importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(superAuthor)).addRelatedRecord(thesis);
				}
				
				
				int brojPriloga = rset.getInt(7);
				int brojGrafikona = rset.getInt(8);
				int brojSlika = rset.getInt(9);
				int brojTabela = rset.getInt(10);
				int brojReferenci = rset.getInt(11);
				int brojStrana = rset.getInt(12);
				int brojPoglavlja = rset.getInt(13);
				Calendar datumPrihvatanja = null;
				if (rset.getDate(14) != null) {
					datumPrihvatanja = new GregorianCalendar();
					datumPrihvatanja.setTimeInMillis(rset.getDate(14).getTime());
				}
				Calendar datumOdbrane = null;
				if (rset.getDate(15) != null) {
					datumOdbrane = new GregorianCalendar();
					datumOdbrane.setTimeInMillis(rset.getDate(15).getTime());
				}
				String udkBroj = rset.getString(16);
				String jezik = null;
				switch (rset.getInt(17)){
					case 1: jezik = "srp";
							break;
					case 2: jezik = "eng";
							break;
				}
				AuthorDTO autor = new AuthorDTO("a"+rset.getInt(18));
				superAuthor = (RecordDTO)autor;
				if((superAuthor != null) && (importRecordDTO.getAllRecords().indexOf(superAuthor) != -1)){
					importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(superAuthor)).addRelatedRecord(thesis);
				}
				ResearchAreaDTO naucnaOblast = new ResearchAreaDTO();
				String naucnaOblastString = (rset.getString(19)!=null)?rset.getString(19):"";
				if(rset.getString(20) != null){
					if(naucnaOblastString.trim().length() != 0)
						naucnaOblastString += ", ";
					naucnaOblastString += rset.getString(20);
				}
				naucnaOblast.getTerm().setContent(naucnaOblastString);
				InstitutionDTO fakultet = new InstitutionDTO(""+rset.getInt(21));
				superInstitution = (RecordDTO)fakultet;
				if((superInstitution != null) && (importRecordDTO.getAllRecords().indexOf(superInstitution) != -1))
					importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(superInstitution)).addRelatedRecord(thesis);
				String nazivBiblioteke = rset.getString(22);
				String vrstaDokumenta = null;
				switch(rset.getInt(23)){
					case 1: vrstaDokumenta = "records.studyFinalDocument.editPanel.studyType.bachelor";
							break;
					case 2: vrstaDokumenta = "records.studyFinalDocument.editPanel.studyType.oldMaster";
							break;
					case 3: vrstaDokumenta = "records.studyFinalDocument.editPanel.studyType.phd";
							break;
					case 4: vrstaDokumenta = "records.studyFinalDocument.editPanel.studyType.master";
							break;
					case 5: vrstaDokumenta = "records.studyFinalDocument.editPanel.studyType.specialistic";
							break;
				}
				
				MultilingualContentDTO naslovSr = new MultilingualContentDTO(rset.getString(24), MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL);
				MultilingualContentDTO naslovEn = new MultilingualContentDTO(rset.getString(25), "eng", MultilingualContentDTO.TRANS_HUMAN);
				MultilingualContentDTO kljucneReciSr = new MultilingualContentDTO(rset.getString(26), MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL);
				MultilingualContentDTO kljucneReciEn = new MultilingualContentDTO(rset.getString(27), "eng", MultilingualContentDTO.TRANS_HUMAN);
				MultilingualContentDTO napomenaSr = new MultilingualContentDTO(rset.getString(28), MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL);
				MultilingualContentDTO napomenaEn = new MultilingualContentDTO(rset.getString(29), "eng", MultilingualContentDTO.TRANS_HUMAN);
				MultilingualContentDTO izvodSr = new MultilingualContentDTO(rset.getString(30), MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL);
				MultilingualContentDTO izvodEn = new MultilingualContentDTO(rset.getString(31), "eng", MultilingualContentDTO.TRANS_HUMAN);
				
				
				
				thesis.setControlNumber(""+sifraDok);
				thesis.getAdvisors().add(mentor);
				thesis.getPhysicalDescription().setNumberOfAppendixes(brojPriloga);
				thesis.getPhysicalDescription().setNumberOfGraphs(brojGrafikona);
				thesis.getPhysicalDescription().setNumberOfPictures(brojSlika);
				thesis.getPhysicalDescription().setNumberOfTables(brojTabela);
				thesis.getPhysicalDescription().setNumberOfReferences(brojReferenci);
				thesis.getPhysicalDescription().setNumberOfPages(brojStrana);
				thesis.getPhysicalDescription().setNumberOfChapters(brojPoglavlja);
				thesis.setAcceptedOn(datumPrihvatanja);
				thesis.setDefendedOn(datumOdbrane);
				if(datumOdbrane != null)
					thesis.setPublicationDate(datumOdbrane);
				thesis.setUdc(udkBroj);
				thesis.setLanguage(jezik);
				thesis.setAuthor(autor);
				thesis.setResearchArea(naucnaOblast);
				thesis.setInstitution(fakultet);
				thesis.setHoldingData(nazivBiblioteke);
				thesis.setStudyType(vrstaDokumenta);
				thesis.setTitle(naslovSr);
				if(naslovEn.getContent() != null)
					thesis.getTitleTranslations().add(naslovEn);
				thesis.setKeywords(kljucneReciSr);
				if(kljucneReciEn.getContent() != null)
					thesis.getKeywordsTranslations().add(kljucneReciEn);
				thesis.setNote(napomenaSr);
				if(napomenaEn.getContent() != null)
					thesis.getNoteTranslations().add(napomenaEn);
				thesis.setAbstracT(izvodSr);
				if(izvodEn.getContent() != null)
					thesis.getAbstractTranslations().add(izvodEn);
				if(loadCommitteeMembers(thesis, importRecordDTO))
					if (loadFile(thesis))
						theses.add(thesis);
			}
			rset.close();
			stmt.close();
			
			
			importRecordDTO.getAllRecords().addAll(theses);
			importRecordDTO.setAllRecords(importRecordDTO.getAllRecords());
			retVal = true;
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.fatal(ex);
			log.fatal("Cannot load theses");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException throwables) {

				}
			}
		}
		return retVal;
	}

	private boolean loadCommitteeMembers(StudyFinalDocumentDTO thesis, ImportRecordsDTO importRecordDTO) {
		boolean retVal = false;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			StringBuffer query = new StringBuffer();
			query
					.append("select PREZIME, IME, SIFRA_TITULE, SIFRA_ORG, SIFRA_ZVANJA from CLAN_KOMISIJE where SIFRA_DOK = " + thesis.getControlNumber() + " order by PREDSEDNIK desc");
			Statement stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				String prezime = rset.getString(1);
				String ime = rset.getString(2);
				AuthorDTO author = new AuthorDTO();
				author.setName(new AuthorNameDTO(ime, prezime, ""));
				author.setControlNumber(prezime+ime);
				switch (rset.getInt(3)){
					case 1: author.setTitle("dr");
							break;
					case 2: author.setTitle("mr");
							break;
				}
				switch (rset.getInt(4)){
					case 1: author.setCurrentPositionName("Redovni profesor");
							break;
					case 2: author.setCurrentPositionName("Asistent");
							break;
					case 3: author.setCurrentPositionName("Docent");
							break;
					case 4: author.setCurrentPositionName("Vanredni profesor");
							break;
					case 5: author.setCurrentPositionName("Viši naučni saradnik");
							break;
					case 6: author.setCurrentPositionName("Profesor emeritus");
							break;
				}
				switch (rset.getInt(5)){
					case 1: author.setInstitutionName("Fakultet tehničkih nauka, Novi Sad");
							break;
					case 2: author.setInstitutionName("Prirodno-matematički fakultet, Novi Sad");
							break;
					case 3: author.setInstitutionName("Fakultet organizacionih nauka, Beograd");
							break;
					case 4: author.setInstitutionName("Tehnički fakultet \"Mihajlo Pupin\", Zrenjanin");
							break;
					case 5: author.setInstitutionName("Institut \"Mihajlo Pupin\", Beograd");
							break;
					case 6: author.setInstitutionName("Elektrotehnički fakultet, Beograd");
							break;
					case 7: author.setInstitutionName("Prirodno-matematički fakultet, Kragujevac");
							break;
					case 8: author.setInstitutionName("Tehnološki fakultet, Novi Sad");
							break;
					case 9: author.setInstitutionName("Fakultet civilne odbrane, Beograd");
							break;
					case 10: author.setInstitutionName("Fakultet političkih nauka, Beograd");
							break;
					case 11: author.setInstitutionName("Filozofski fakultet, Beograd");
							break;
					case 12: author.setInstitutionName("Mašinski fakultet, Beograd");
							break;
					case 13: author.setInstitutionName("Tehnološko-metalurški fakultet, Beograd");
							break;
					case 14: author.setInstitutionName("Građevinsko-arhitektonski fakultet, Niš");
							break;
					case 15: author.setInstitutionName("Mašinski fakultet, Kragujevac");
							break;
					case 16: author.setInstitutionName("Prirodno-matematički fakultet, Niš");
							break;
					case 17: author.setInstitutionName("Tehnološki fakultet, Leskovac");
							break;
					case 18: author.setInstitutionName("Mašinski fakultet, Niš");
							break;
					case 19: author.setInstitutionName("Tehnički fakultet, Čačak");
							break;
					case 20: author.setInstitutionName("Prirodno-matematički fakultet, Beograd");
							break;
					case 21: author.setInstitutionName("Viša tehnička škola, Subotica");
							break;
					case 22: author.setInstitutionName("Pedagoškog zavoda Vojvodine - Novi Sad");
							break;
					case 23: author.setInstitutionName("Elektronski fakultet, Niš");
							break;
					case 24: author.setInstitutionName("Univerzitet za tehnologiju, Drezden");
							break;
					case 25: author.setInstitutionName("Univerzitet primenjenjih nauka, Merseburg");
							break;
					case 27: author.setInstitutionName("Politehnički fakultet u Budimpešti");
							break;
					case 28: author.setInstitutionName("Fakultet fizičke kulture, Novi Sad");
							break;
					case 29: author.setInstitutionName("Ekonomski fakultet, Subotica");
							break;
					case 30: author.setInstitutionName("Filozofski fakultet, Novi Sad");
							break;
					case 31: author.setInstitutionName("Pravni fakultet, Novi Sad");
							break;
					case 32: author.setInstitutionName("Medicinski fakultet, Novi Sad");
							break;
					case 33: author.setInstitutionName("Institut za nuklearne nauke Vinča");
							break;
					case 34: author.setInstitutionName("Naučni institut za ratarstvo i povrtarstvo, Novi Sad");
							break;
					case 35: author.setInstitutionName("Medicinski fakultet, Beograd");
							break;
					case 36: author.setInstitutionName("Poljoprivredni fakultet, Novi Sad");
							break;
					case 37: author.setInstitutionName("Gradjevinski fakultet, Subotica");
							break;
					case 38: author.setInstitutionName("Institut za biologiju mora, Kotor");
							break;
					case 39: author.setInstitutionName("Poljoprivredni fakultet, Zemun");
							break;
				}
				thesis.getCommitteeMembers().add(author);
				RecordDTO superAuthor = (RecordDTO)author;
				if((superAuthor != null) && (importRecordDTO.getAllRecords().indexOf(superAuthor) != -1)){
					importRecordDTO.getAllRecords().get(importRecordDTO.getAllRecords().indexOf(superAuthor)).addRelatedRecord(thesis);
				}
			}
			rset.close();
			stmt.close();

			retVal = true;
			return retVal;
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot load committee members");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException throwables) {

				}
			}
		}
		return retVal;
	}
	
	private boolean loadFile(StudyFinalDocumentDTO thesis) {
		boolean retVal = false;
		if ((thesis.getStudyType() != null) && ((thesis.getStudyType().equals("records.studyFinalDocument.editPanel.studyType.oldMaster")) || (thesis.getStudyType().equals("records.studyFinalDocument.editPanel.studyType.master")) || (thesis.getStudyType().equals("records.studyFinalDocument.editPanel.studyType.phd")))){
			FileDTO fileDTO = new FileDTO();
			fileDTO.setControlNumber(thesis.getControlNumber());
			File folder = new File("E:/files/set" + ((Integer.parseInt(thesis.getControlNumber())-1)/200 + 1) + "/ndltd" + thesis.getControlNumber());
			if((folder == null) || (!folder.isDirectory()))
				return retVal;
			File file = null;
			for (File temp : folder.listFiles()) {
				file = temp;
			}
			if(file != null){
				fileDTO.setFileNameClient(file.getName());
				fileDTO.setLength(file.length());
				fileDTO.setUploader("chenejac@uns.ac.rs");
				try {
					fileDTO.setData(IOUtils.toByteArray(new BufferedInputStream(
					  new FileInputStream(file))));
					thesis.setFile(fileDTO);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}
		retVal = true; 
		return retVal;
	}

	private DataSource dataSource = new MyDataSource("localhost", "3306", "ndltd", "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8", "root", "root");
	
	private static Log log = LogFactory.getLog(DiglibSerializer.class.getName());

}
