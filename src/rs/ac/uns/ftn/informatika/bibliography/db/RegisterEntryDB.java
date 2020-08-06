package rs.ac.uns.ftn.informatika.bibliography.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RegisterEntryDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.maticnaKnjiga.NumberPhDPerInstitution;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

/**
 * Class for persist and retrieve data about register
 * from database.
 * 
 * @author chenejac@uns.ac.rs
 */
public class RegisterEntryDB {

	/**
	 * Retrieves a register entry.
	 * 
	 * @param conn
	 *            Database connection
	 * @param dissertation
	 *            The dissertation
	 * @param user
	 *            The creator
	 * @return The retrieved register entry ; null if not found or an error occured.
	 */
	public RegisterEntryDTO getRegisterEntry(Connection conn, StudyFinalDocumentDTO dissertation, boolean translateToCyrillic) {
		RegisterEntryDTO retVal = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select REGISTERENTRYID, AUTHORNAME, AUTHORLASTNAME, FATHERNAME, FATHERLASTNAME, MOTHERNAME, MOTHERLASTNAME, GUARDIANSFULLNAME, " +
							"STREETANDNUMBER, PLACE, POSTALCODE, CITY, COUNTRY, EMAIL, PHONE, " +
							"BIRTHDATE, BIRTHPLACE, BIRTHCITY, BIRTHCOUNTRY, PREVIOUSLYGRADUATED, PREVIOUSLYGRADUATEDPLACE, PREVIOUSLYNAMEOFAUTHORDEGREEOLD, " +
							"PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONOLD, PREVIOUSLYNAMEOFAUTHORDEGREEDATEOLD, PREVIOUSLYNAMEOFAUTHORDEGREEBOLOGNA, PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONBOLOGNA, " +
							" PREVIOUSLYNAMEOFAUTHORDEGREEYEARBOLOGNA, " +
							"TITLE, INSTITUTION, INSTITUTIONPLACE, ADVISORS, COMMISSIONMEMBERS, DEFENDEDON, MARK, NAMEOFAUTHORDEGREE, " +
							"PROMOTIONDATE, DIPLOMANUMBER, SUPPLEMENTNUMBER, DIPLOMAPUBLICATIONDATE, SUPPLEMENTPUBLICATIONDATE, CREATOR, ACADEMICYEAR, ACADEMICYEARNUMBER, FUTUREPROMOTIONNAME, UNIVERSITYID from REGISTERENTRY where DISSERTATIONID like '"
							+ dissertation.getControlNumber() + "'");
			if (rset.next()) {
				int i = 1;
				String id = rset.getString(i++);
				
				String authorName = rset.getString(i++);
				String authorLastName = rset.getString(i++);
				String fatherName = rset.getString(i++);
				String fatherLastName = rset.getString(i++);
				String motherName = rset.getString(i++);
				String motherLastName = rset.getString(i++);
				String guardiansFullName = rset.getString(i++);
				
				String streetAndNumber = rset.getString(i++);
				String place = rset.getString(i++);
				String postalCode = rset.getString(i++);
				String city = rset.getString(i++);
				String country = rset.getString(i++);
				String email = rset.getString(i++);
				String phone = rset.getString(i++);
				
				Calendar birthDate = null;
				if (rset.getDate(i++) != null) {
					birthDate = new GregorianCalendar();
					birthDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String birthPlace = rset.getString(i++);
				String birthCity = rset.getString(i++);
				String birthCountry = rset.getString(i++);
				
				String previouslyGraduated = rset.getString(i++);
				String previouslyGraduatedPlace = rset.getString(i++);
				String previouslyNameOfAuthorDegreeOld = rset.getString(i++);
				String previouslyNameOfAuthorDegreeAbbreviationOld = rset.getString(i++);
				Calendar previouslyNameOfAuthorDegreeDateOld = null;
				if (rset.getDate(i++) != null) {
					previouslyNameOfAuthorDegreeDateOld = new GregorianCalendar();
					previouslyNameOfAuthorDegreeDateOld.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String previouslyNameOfAuthorDegreeBologna = rset.getString(i++);
				String previouslyNameOfAuthorDegreeAbbreviationBologna = rset.getString(i++);
				String previouslyNameOfAuthorDegreeYearBologna = rset.getString(i++);
				
				String title = rset.getString(i++);
				String institution = rset.getString(i++);
				String institutionPlace = rset.getString(i++);
				String advisors = rset.getString(i++);
				String commission = rset.getString(i++);
				Calendar defendedOn = null;
				if (rset.getDate(i++) != null) {
					defendedOn = new GregorianCalendar();
					defendedOn.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String mark = rset.getString(i++);
				String nameOfAuthorDegree = rset.getString(i++);
				
				Calendar promotionDate = null;
				if (rset.getDate(i++) != null) {
					promotionDate = new GregorianCalendar();
					promotionDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String diplomaNumber = rset.getString(i++);
				String supplementNumber = rset.getString(i++);
				Calendar diplomaPublicationDate = null;
				if (rset.getDate(i++) != null) {
					diplomaPublicationDate = new GregorianCalendar();
					diplomaPublicationDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				Calendar supplementPublicationDate = null;
				if (rset.getDate(i++) != null) {
					supplementPublicationDate = new GregorianCalendar();
					supplementPublicationDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				
				String user = rset.getString(i++);
				String academicYear = rset.getString(i++);
				int academicYearNumber = rset.getInt(i++);
				if(rset.wasNull())
					academicYearNumber = 0;
				
				String futurePromotionName = rset.getString(i++);
				String universityID = rset.getString(i++);
				
				retVal = dissertation.getRegisterEntry(); 
				retVal.getDissertation().getAcceptedOn(); // load object
				retVal.setAuthor((AuthorDTO) new RecordDAO(new PersonDB()).getDTO(dissertation.getAuthor().getControlNumber()));
				retVal.getAuthor().getAddress(); // load object
				retVal.setNotLoaded(false);
				
				retVal.setId(id);
				retVal.setAcademicYear(academicYear);
				retVal.setAcademicYearNumber(academicYearNumber);
				retVal.setFuturePromotionName(futurePromotionName);
				retVal.setUniversityId(universityID);
				
				retVal.getAuthorName().setLastname(authorLastName);
				retVal.getAuthorName().setFirstname(authorName);
				retVal.getFatherName().setLastname(fatherLastName);
				retVal.getFatherName().setFirstname(fatherName);
				retVal.getMotherName().setLastname(motherLastName);
				retVal.getMotherName().setFirstname(motherName);
				retVal.setGuardiansName(guardiansFullName);
				
				retVal.setStreetAndNumber(streetAndNumber);
				retVal.setPlace(place);
				retVal.setPostalCode(postalCode);
				retVal.setCity(city);
				retVal.setCountry(country);
				retVal.setEmail(email);
				retVal.setPhone(phone);
				
				retVal.setBirthDate(birthDate);
				retVal.setBirthPlace(birthPlace);
				retVal.setBirthCity(birthCity);
				retVal.setBirthCountry(birthCountry);
				
				retVal.setPreviouslyGraduated(previouslyGraduated);
				retVal.setPreviouslyGraduatedPlace(previouslyGraduatedPlace);
				retVal.setPreviouslyNameOfAuthorDegreeOld(previouslyNameOfAuthorDegreeOld);
				retVal.setPreviouslyNameOfAuthorDegreeAbbreviationOld(previouslyNameOfAuthorDegreeAbbreviationOld);
				retVal.setPreviouslyNameOfAuthorDegreeDateOld(previouslyNameOfAuthorDegreeDateOld);
				retVal.setPreviouslyNameOfAuthorDegreeBologna(previouslyNameOfAuthorDegreeBologna);
				retVal.setPreviouslyNameOfAuthorDegreeAbbreviationBologna(previouslyNameOfAuthorDegreeAbbreviationBologna);
				retVal.setPreviouslyNameOfAuthorDegreeYearBologna(previouslyNameOfAuthorDegreeYearBologna);
				
				retVal.setTitle(title);
				retVal.setInstitution(institution);
				retVal.setInstitutionPlace(institutionPlace);
				retVal.setAdvisors(advisors);
				retVal.setCommissionMembers(commission);
				retVal.setDefendedOn(defendedOn);
				retVal.setMark(mark);
				retVal.setNameOfAuthorDegree(nameOfAuthorDegree);
				
				retVal.setPromotionDate(promotionDate);
				retVal.setDiplomaNumber(diplomaNumber);
				retVal.setSupplementNumber(supplementNumber);
				retVal.setDiplomaPublicationDate(diplomaPublicationDate);
				retVal.setSupplementPublicationDate(supplementPublicationDate);
				
				retVal.setUser(user);
				
			} else {
				retVal = dissertation.getRegisterEntry();
				retVal.getDissertation().getAcceptedOn(); // load object
				retVal.setAuthor((AuthorDTO) new RecordDAO(new PersonDB()).getDTO(dissertation.getAuthor().getControlNumber()));
				retVal.setEmail(retVal.getAuthor().getEmail()); // load object
				retVal.setNotLoaded(false);
				retVal.getAuthorName().setLastname((translateToCyrillic)?(LatCyrUtils.toCyrillic(retVal.getAuthor().getName().getLastname())):(retVal.getAuthor().getName().getLastname()));
				retVal.getAuthorName().setFirstname((translateToCyrillic)?(LatCyrUtils.toCyrillic(retVal.getAuthor().getName().getFirstname())):(retVal.getAuthor().getName().getFirstname()));
				retVal.getFatherName().setFirstname((translateToCyrillic)?(LatCyrUtils.toCyrillic(retVal.getAuthor().getName().getOtherName())):(retVal.getAuthor().getName().getOtherName()));
				
				retVal.setBirthDate(retVal.getAuthor().getDateOfBirth());
				retVal.setBirthPlace((translateToCyrillic)?(LatCyrUtils.toCyrillic(retVal.getAuthor().getPlaceOfBirth())):(retVal.getAuthor().getPlaceOfBirth()));
				retVal.setBirthCity((translateToCyrillic)?(LatCyrUtils.toCyrillic(retVal.getAuthor().getCity())):(retVal.getAuthor().getCity()));
				retVal.setBirthCountry((translateToCyrillic)?(LatCyrUtils.toCyrillic(retVal.getAuthor().getState())):(retVal.getAuthor().getState()));
				retVal.setTitle((translateToCyrillic)?(LatCyrUtils.toCyrillic(dissertation.getSomeTitle())):(dissertation.getSomeTitle()));
				String institutionTemp = dissertation.getInstitution().getSomeName();
				institutionTemp = institutionTemp.replace(" u Novom Sadu, Novi Sad", "");
				institutionTemp = institutionTemp.replace(" u Novom Sadu", "");
				retVal.setInstitution((translateToCyrillic)?(LatCyrUtils.toCyrillic(institutionTemp)):(institutionTemp));
				retVal.setInstitutionPlace((translateToCyrillic)?(LatCyrUtils.toCyrillic(dissertation.getInstitution().getPlace())):(dissertation.getInstitution().getPlace()));
				StringBuffer advisorsBuffer = new StringBuffer("");
				for (AuthorDTO advisor : dissertation.getAdvisors()) {
					if(advisorsBuffer.length() > 0)
						advisorsBuffer.append("\n");
					if((advisor.getTitle()!=null) && (! advisor.getTitle().trim().equals("")))
						advisorsBuffer.append(advisor.getTitle() + " ");
					advisorsBuffer.append(advisor.getName().getFirstname() + " ");
					advisorsBuffer.append(advisor.getName().getLastname());
					boolean commaAdded = false;
					if(advisor.getCurrentPositionName() != null){
						commaAdded = true;
						if(advisor.getCurrentPositionName().equals("Docent")){
							advisorsBuffer.append(", доцент");
						} else if(advisor.getCurrentPositionName().equals("Vаnredni profesor")){
							advisorsBuffer.append(", ванр. проф.");
						} else if(advisor.getCurrentPositionName().equals("Profesor emeritus")){
							advisorsBuffer.append(", проф. емеритус");
						} else if(advisor.getCurrentPositionName().equals("Naučni - saradnik")){
							advisorsBuffer.append(", науч. сар.");
						} else if(advisor.getCurrentPositionName().equals("Viši naučni - saradnik")){
							advisorsBuffer.append(", виши науч. сар.");
						} else if(advisor.getCurrentPositionName().equals("Naučni savetnik")){
							advisorsBuffer.append(", науч. сав.");
						} else if(advisor.getCurrentPositionName().equals("Akademik")){
							advisorsBuffer.append(", академик.");
						} else {
							advisorsBuffer.append(", ред. проф.");
						} 
					}
					if(advisor.getInstitution().getSomeName()!=null){
						advisorsBuffer.append(((commaAdded)?(" "):(", ")) + advisor.getInstitution().getSomeName());
					}
				}
				String advisorsTemp = advisorsBuffer.toString();
				advisorsTemp = advisorsTemp.replace(" u Novom Sadu, Novi Sad", ", Novi Sad");
				advisorsTemp = advisorsTemp.replace(" u Novom Sadu", ", Novi Sad");
				retVal.setAdvisors((translateToCyrillic)?(LatCyrUtils.toCyrillic(advisorsTemp)):(advisorsTemp));
				StringBuffer commissionBufferStart = new StringBuffer();
				StringBuffer commissionBufferEnd = new StringBuffer();
				StringBuffer commissionBuffer = commissionBufferStart;
				for (AuthorDTO committeeMember : dissertation.getCommitteeMembers()) {
					if(dissertation.getAdvisors().contains(committeeMember)){
						commissionBuffer = commissionBufferEnd;
						commissionBuffer.append(" ");
					} else
						commissionBuffer = commissionBufferStart;
					if(commissionBuffer.length() > 0)
						commissionBuffer.append("\n");
					if((committeeMember.getTitle()!=null) && (! committeeMember.getTitle().trim().equals("")))
						commissionBuffer.append(committeeMember.getTitle() + " ");
					commissionBuffer.append(committeeMember.getName().getFirstname() + " ");
					commissionBuffer.append(committeeMember.getName().getLastname());
					boolean commaAdded = false;
					if(committeeMember.getCurrentPositionName() != null){
						commaAdded = true;
						if(committeeMember.getCurrentPositionName().equals("Docent")){
							commissionBuffer.append(", доцент");
						} else if(committeeMember.getCurrentPositionName().equals("Vаnredni profesor")){
							commissionBuffer.append(", ванр. проф.");
						} else if(committeeMember.getCurrentPositionName().equals("Profesor emeritus")){
							commissionBuffer.append(", проф. емеритус");
						} else if(committeeMember.getCurrentPositionName().equals("Naučni - saradnik")){
							commissionBuffer.append(", науч. сар.");
						} else if(committeeMember.getCurrentPositionName().equals("Viši naučni - saradnik")){
							commissionBuffer.append(", виши науч. сар.");
						} else if(committeeMember.getCurrentPositionName().equals("Naučni savetnik")){
							commissionBuffer.append(", науч. сав.");
						} else if(committeeMember.getCurrentPositionName().equals("Akademik")){
							commissionBuffer.append(", академик");
						} else {
							commissionBuffer.append(", ред. проф.");
						} 
					}
					if(committeeMember.getInstitution().getSomeName()!=null){
						commissionBuffer.append(((commaAdded)?(" "):(", ")) + committeeMember.getInstitution().getSomeName());
					}
					if(dissertation.getAdvisors().contains(committeeMember)){
						commissionBuffer.append(", ментор и члан");
					} else if(dissertation.getCommitteeMembers().indexOf(committeeMember) == 0){
						commissionBuffer.append(", председник");
					}
				}
				
				commissionBuffer = commissionBufferEnd;
				if(dissertation.getAdvisors().size() != 0){
				for (AuthorDTO advisor : dissertation.getAdvisors()) {
					if(dissertation.getCommitteeMembers().contains(advisor))
						continue;
					if(commissionBufferStart.length() > 0)
						commissionBuffer.append("\n");
					if((advisor.getTitle()!=null) && (! advisor.getTitle().trim().equals("")))
						commissionBuffer.append(advisor.getTitle() + " ");
					commissionBuffer.append(advisor.getName().getFirstname() + " ");
					commissionBuffer.append(advisor.getName().getLastname());
					boolean commaAdded = false;
					if(advisor.getCurrentPositionName() != null){
						commaAdded = true;
						if(advisor.getCurrentPositionName().equals("Docent")){
							commissionBuffer.append(", доцент");
						} else if(advisor.getCurrentPositionName().equals("Vаnredni profesor")){
							commissionBuffer.append(", ванр. проф.");
						} else if(advisor.getCurrentPositionName().equals("Profesor emeritus")){
							commissionBuffer.append(", проф. емеритус");
						} else if(advisor.getCurrentPositionName().equals("Naučni - saradnik")){
							commissionBuffer.append(", науч. сар.");
						} else if(advisor.getCurrentPositionName().equals("Viši naučni - saradnik")){
							commissionBuffer.append(", виши науч. сар.");
						} else if(advisor.getCurrentPositionName().equals("Naučni savetnik")){
							commissionBuffer.append(", науч. сав.");
						} else if(advisor.getCurrentPositionName().equals("Akademik")){
							commissionBuffer.append(", академик");
						} else {
							commissionBuffer.append(", ред. проф.");
						} 
					}
					if(advisor.getInstitution().getSomeName()!=null){
						commissionBuffer.append(((commaAdded)?(" "):(", ")) + advisor.getInstitution().getSomeName());
					}
					commissionBuffer.append(", ментор и није члан");
				}
				} else {
					commissionBuffer.append("\nMентор: без ментора");
				}
				
				commissionBufferStart.append(commissionBufferEnd.toString());
				commissionBuffer = commissionBufferStart;
				String commissionTemp = commissionBuffer.toString();
				commissionTemp = commissionTemp.replace(" u Novom Sadu, Novi Sad", ", Novi Sad");
				commissionTemp = commissionTemp.replace(" u Novom Sadu", ", Novi Sad");
				retVal.setCommissionMembers((translateToCyrillic)?(LatCyrUtils.toCyrillic(commissionTemp)):(commissionTemp));
				retVal.setDefendedOn(dissertation.getDefendedOn());
				if(translateToCyrillic){
					retVal.setPreviouslyGraduated(LatCyrUtils.toCyrillic(retVal.getPreviouslyGraduated()));
					retVal.setPreviouslyGraduatedPlace(LatCyrUtils.toCyrillic(retVal.getPreviouslyGraduatedPlace()));
					retVal.setNameOfAuthorDegree(LatCyrUtils.toCyrillic(retVal.getNameOfAuthorDegree()));
				}
			}
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read register entry for dissertation: "
					+ dissertation.getControlNumber());
			log.fatal(ex);
			return null;
		}
	}
	
	
	public RegisterEntryDTO getRegisterEntry(Connection conn, String dissertationControlNumber, boolean translateToCyrillic) {
		RegisterEntryDTO retVal = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select REGISTERENTRYID, AUTHORNAME, AUTHORLASTNAME, FATHERNAME, FATHERLASTNAME, MOTHERNAME, MOTHERLASTNAME, GUARDIANSFULLNAME, " +
							"STREETANDNUMBER, PLACE, POSTALCODE, CITY, COUNTRY, EMAIL, PHONE, " +
							"BIRTHDATE, BIRTHPLACE, BIRTHCITY, BIRTHCOUNTRY, PREVIOUSLYGRADUATED, PREVIOUSLYGRADUATEDPLACE, PREVIOUSLYNAMEOFAUTHORDEGREEOLD, " +
							"PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONOLD, PREVIOUSLYNAMEOFAUTHORDEGREEDATEOLD, PREVIOUSLYNAMEOFAUTHORDEGREEBOLOGNA, PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONBOLOGNA, " +
							" PREVIOUSLYNAMEOFAUTHORDEGREEYEARBOLOGNA, " +
							"TITLE, INSTITUTION, INSTITUTIONPLACE, ADVISORS, COMMISSIONMEMBERS, DEFENDEDON, MARK, NAMEOFAUTHORDEGREE, " +
							"PROMOTIONDATE, DIPLOMANUMBER, SUPPLEMENTNUMBER, DIPLOMAPUBLICATIONDATE, SUPPLEMENTPUBLICATIONDATE, CREATOR, ACADEMICYEAR, ACADEMICYEARNUMBER, FUTUREPROMOTIONNAME, UNIVERSITYID from REGISTERENTRY where DISSERTATIONID like '"
							+ dissertationControlNumber+ "'");
			StudyFinalDocumentDTO dissertation = new StudyFinalDocumentDTO();
			dissertation = (StudyFinalDocumentDTO)(new RecordDAO(new RecordDB()).getRecordFromDatabase(dissertationControlNumber).getDto());
			if (rset.next()) {
				int i = 1;
				String id = rset.getString(i++);
				
				String authorName = rset.getString(i++);
				String authorLastName = rset.getString(i++);
				String fatherName = rset.getString(i++);
				String fatherLastName = rset.getString(i++);
				String motherName = rset.getString(i++);
				String motherLastName = rset.getString(i++);
				String guardiansFullName = rset.getString(i++);
				
				String streetAndNumber = rset.getString(i++);
				String place = rset.getString(i++);
				String postalCode = rset.getString(i++);
				String city = rset.getString(i++);
				String country = rset.getString(i++);
				String email = rset.getString(i++);
				String phone = rset.getString(i++);
				
				Calendar birthDate = null;
				if (rset.getDate(i++) != null) {
					birthDate = new GregorianCalendar();
					birthDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String birthPlace = rset.getString(i++);
				String birthCity = rset.getString(i++);
				String birthCountry = rset.getString(i++);
				
				String previouslyGraduated = rset.getString(i++);
				String previouslyGraduatedPlace = rset.getString(i++);
				String previouslyNameOfAuthorDegreeOld = rset.getString(i++);
				String previouslyNameOfAuthorDegreeAbbreviationOld = rset.getString(i++);
				Calendar previouslyNameOfAuthorDegreeDateOld = null;
				if (rset.getDate(i++) != null) {
					previouslyNameOfAuthorDegreeDateOld = new GregorianCalendar();
					previouslyNameOfAuthorDegreeDateOld.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String previouslyNameOfAuthorDegreeBologna = rset.getString(i++);
				String previouslyNameOfAuthorDegreeAbbreviationBologna = rset.getString(i++);
				String previouslyNameOfAuthorDegreeYearBologna = rset.getString(i++);
				
				String title = rset.getString(i++);
				String institution = rset.getString(i++);
				String institutionPlace = rset.getString(i++);
				String advisors = rset.getString(i++);
				String commission = rset.getString(i++);
				Calendar defendedOn = null;
				if (rset.getDate(i++) != null) {
					defendedOn = new GregorianCalendar();
					defendedOn.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String mark = rset.getString(i++);
				String nameOfAuthorDegree = rset.getString(i++);
				
				Calendar promotionDate = null;
				if (rset.getDate(i++) != null) {
					promotionDate = new GregorianCalendar();
					promotionDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String diplomaNumber = rset.getString(i++);
				String supplementNumber = rset.getString(i++);
				Calendar diplomaPublicationDate = null;
				if (rset.getDate(i++) != null) {
					diplomaPublicationDate = new GregorianCalendar();
					diplomaPublicationDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				Calendar supplementPublicationDate = null;
				if (rset.getDate(i++) != null) {
					supplementPublicationDate = new GregorianCalendar();
					supplementPublicationDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				
				String user = rset.getString(i++);
				String academicYear = rset.getString(i++);
				int academicYearNumber = rset.getInt(i++);
				if(rset.wasNull())
					academicYearNumber = 0;
				
				String futurePromotionName = rset.getString(i++);
				String universityID = rset.getString(i++);
				
				
				retVal = dissertation.getRegisterEntry(); 
				retVal.getDissertation().getAcceptedOn(); // load object
				retVal.setAuthor((AuthorDTO) new RecordDAO(new PersonDB()).getDTO(dissertation.getAuthor().getControlNumber()));
				retVal.getAuthor().getAddress(); // load object
				retVal.setNotLoaded(false);
				
				retVal.setId(id);
				retVal.setAcademicYear(academicYear);
				retVal.setAcademicYearNumber(academicYearNumber);
				retVal.setFuturePromotionName(futurePromotionName);
				retVal.setUniversityId(universityID);
				
				retVal.getAuthorName().setLastname(authorLastName);
				retVal.getAuthorName().setFirstname(authorName);
				retVal.getFatherName().setLastname(fatherLastName);
				retVal.getFatherName().setFirstname(fatherName);
				retVal.getMotherName().setLastname(motherLastName);
				retVal.getMotherName().setFirstname(motherName);
				retVal.setGuardiansName(guardiansFullName);
				
				retVal.setStreetAndNumber(streetAndNumber);
				retVal.setPlace(place);
				retVal.setPostalCode(postalCode);
				retVal.setCity(city);
				retVal.setCountry(country);
				retVal.setEmail(email);
				retVal.setPhone(phone);
				
				retVal.setBirthDate(birthDate);
				retVal.setBirthPlace(birthPlace);
				retVal.setBirthCity(birthCity);
				retVal.setBirthCountry(birthCountry);
				
				retVal.setPreviouslyGraduated(previouslyGraduated);
				retVal.setPreviouslyGraduatedPlace(previouslyGraduatedPlace);
				retVal.setPreviouslyNameOfAuthorDegreeOld(previouslyNameOfAuthorDegreeOld);
				retVal.setPreviouslyNameOfAuthorDegreeAbbreviationOld(previouslyNameOfAuthorDegreeAbbreviationOld);
				retVal.setPreviouslyNameOfAuthorDegreeDateOld(previouslyNameOfAuthorDegreeDateOld);
				retVal.setPreviouslyNameOfAuthorDegreeBologna(previouslyNameOfAuthorDegreeBologna);
				retVal.setPreviouslyNameOfAuthorDegreeAbbreviationBologna(previouslyNameOfAuthorDegreeAbbreviationBologna);
				retVal.setPreviouslyNameOfAuthorDegreeYearBologna(previouslyNameOfAuthorDegreeYearBologna);
				
				retVal.setTitle(title);
				retVal.setInstitution(institution);
				retVal.setInstitutionPlace(institutionPlace);
				retVal.setAdvisors(advisors);
				retVal.setCommissionMembers(commission);
				retVal.setDefendedOn(defendedOn);
				retVal.setMark(mark);
				retVal.setNameOfAuthorDegree(nameOfAuthorDegree);
				
				retVal.setPromotionDate(promotionDate);
				retVal.setDiplomaNumber(diplomaNumber);
				retVal.setSupplementNumber(supplementNumber);
				retVal.setDiplomaPublicationDate(diplomaPublicationDate);
				retVal.setSupplementPublicationDate(supplementPublicationDate);
				
				retVal.setUser(user);
				
			} else {
				retVal = dissertation.getRegisterEntry();
				retVal.getDissertation().getAcceptedOn(); // load object
				retVal.setAuthor((AuthorDTO) new RecordDAO(new PersonDB()).getDTO(dissertation.getAuthor().getControlNumber()));
				retVal.setEmail(retVal.getAuthor().getEmail()); // load object
				retVal.setNotLoaded(false);
				retVal.getAuthorName().setLastname((translateToCyrillic)?(LatCyrUtils.toCyrillic(retVal.getAuthor().getName().getLastname())):(retVal.getAuthor().getName().getLastname()));
				retVal.getAuthorName().setFirstname((translateToCyrillic)?(LatCyrUtils.toCyrillic(retVal.getAuthor().getName().getFirstname())):(retVal.getAuthor().getName().getFirstname()));
				retVal.getFatherName().setFirstname((translateToCyrillic)?(LatCyrUtils.toCyrillic(retVal.getAuthor().getName().getOtherName())):(retVal.getAuthor().getName().getOtherName()));
				
				retVal.setBirthDate(retVal.getAuthor().getDateOfBirth());
				retVal.setBirthPlace((translateToCyrillic)?(LatCyrUtils.toCyrillic(retVal.getAuthor().getPlaceOfBirth())):(retVal.getAuthor().getPlaceOfBirth()));
				retVal.setBirthCity((translateToCyrillic)?(LatCyrUtils.toCyrillic(retVal.getAuthor().getCity())):(retVal.getAuthor().getCity()));
				retVal.setBirthCountry((translateToCyrillic)?(LatCyrUtils.toCyrillic(retVal.getAuthor().getState())):(retVal.getAuthor().getState()));
				retVal.setTitle((translateToCyrillic)?(LatCyrUtils.toCyrillic(dissertation.getSomeTitle())):(dissertation.getSomeTitle()));
				String institutionTemp = dissertation.getInstitution().getSomeName();
				institutionTemp = institutionTemp.replace(" u Novom Sadu, Novi Sad", "");
				institutionTemp = institutionTemp.replace(" u Novom Sadu", "");
				retVal.setInstitution((translateToCyrillic)?(LatCyrUtils.toCyrillic(institutionTemp)):(institutionTemp));
				retVal.setInstitutionPlace((translateToCyrillic)?(LatCyrUtils.toCyrillic(dissertation.getInstitution().getPlace())):(dissertation.getInstitution().getPlace()));
				StringBuffer advisorsBuffer = new StringBuffer("");
				for (AuthorDTO advisor : dissertation.getAdvisors()) {
					if(advisorsBuffer.length() > 0)
						advisorsBuffer.append("\n");
					if((advisor.getTitle()!=null) && (! advisor.getTitle().trim().equals("")))
						advisorsBuffer.append(advisor.getTitle() + " ");
					advisorsBuffer.append(advisor.getName().getFirstname() + " ");
					advisorsBuffer.append(advisor.getName().getLastname());
					boolean commaAdded = false;
					if(advisor.getCurrentPositionName() != null){
						commaAdded = true;
						if(advisor.getCurrentPositionName().equals("Docent")){
							advisorsBuffer.append(", доцент");
						} else if(advisor.getCurrentPositionName().equals("Vаnredni profesor")){
							advisorsBuffer.append(", ванр. проф.");
						} else if(advisor.getCurrentPositionName().equals("Profesor emeritus")){
							advisorsBuffer.append(", проф. емеритус");
						} else if(advisor.getCurrentPositionName().equals("Naučni - saradnik")){
							advisorsBuffer.append(", науч. сар.");
						} else if(advisor.getCurrentPositionName().equals("Viši naučni - saradnik")){
							advisorsBuffer.append(", виши науч. сар.");
						} else if(advisor.getCurrentPositionName().equals("Naučni savetnik")){
							advisorsBuffer.append(", науч. сав.");
						} else if(advisor.getCurrentPositionName().equals("Akademik")){
							advisorsBuffer.append(", академик.");
						} else {
							advisorsBuffer.append(", ред. проф.");
						} 
					}
					if(advisor.getInstitution().getSomeName()!=null){
						advisorsBuffer.append(((commaAdded)?(" "):(", ")) + advisor.getInstitution().getSomeName());
					}
				}
				String advisorsTemp = advisorsBuffer.toString();
				advisorsTemp = advisorsTemp.replace(" u Novom Sadu, Novi Sad", ", Novi Sad");
				advisorsTemp = advisorsTemp.replace(" u Novom Sadu", ", Novi Sad");
				retVal.setAdvisors((translateToCyrillic)?(LatCyrUtils.toCyrillic(advisorsTemp)):(advisorsTemp));
				StringBuffer commissionBufferStart = new StringBuffer();
				StringBuffer commissionBufferEnd = new StringBuffer();
				StringBuffer commissionBuffer = commissionBufferStart;
				for (AuthorDTO committeeMember : dissertation.getCommitteeMembers()) {
					if(dissertation.getAdvisors().contains(committeeMember)){
						commissionBuffer = commissionBufferEnd;
						commissionBuffer.append(" ");
					} else
						commissionBuffer = commissionBufferStart;
					if(commissionBuffer.length() > 0)
						commissionBuffer.append("\n");
					if((committeeMember.getTitle()!=null) && (! committeeMember.getTitle().trim().equals("")))
						commissionBuffer.append(committeeMember.getTitle() + " ");
					commissionBuffer.append(committeeMember.getName().getFirstname() + " ");
					commissionBuffer.append(committeeMember.getName().getLastname());
					boolean commaAdded = false;
					if(committeeMember.getCurrentPositionName() != null){
						commaAdded = true;
						if(committeeMember.getCurrentPositionName().equals("Docent")){
							commissionBuffer.append(", доцент");
						} else if(committeeMember.getCurrentPositionName().equals("Vаnredni profesor")){
							commissionBuffer.append(", ванр. проф.");
						} else if(committeeMember.getCurrentPositionName().equals("Profesor emeritus")){
							commissionBuffer.append(", проф. емеритус");
						} else if(committeeMember.getCurrentPositionName().equals("Naučni - saradnik")){
							commissionBuffer.append(", науч. сар.");
						} else if(committeeMember.getCurrentPositionName().equals("Viši naučni - saradnik")){
							commissionBuffer.append(", виши науч. сар.");
						} else if(committeeMember.getCurrentPositionName().equals("Naučni savetnik")){
							commissionBuffer.append(", науч. сав.");
						} else if(committeeMember.getCurrentPositionName().equals("Akademik")){
							commissionBuffer.append(", академик");
						} else {
							commissionBuffer.append(", ред. проф.");
						} 
					}
					if(committeeMember.getInstitution().getSomeName()!=null){
						commissionBuffer.append(((commaAdded)?(" "):(", ")) + committeeMember.getInstitution().getSomeName());
					}
					if(dissertation.getAdvisors().contains(committeeMember)){
						commissionBuffer.append(", ментор и члан");
					} else if(dissertation.getCommitteeMembers().indexOf(committeeMember) == 0){
						commissionBuffer.append(", председник");
					}
				}
				
				commissionBuffer = commissionBufferEnd;
				if(dissertation.getAdvisors().size() != 0){
				for (AuthorDTO advisor : dissertation.getAdvisors()) {
					if(dissertation.getCommitteeMembers().contains(advisor))
						continue;
					if(commissionBufferStart.length() > 0)
						commissionBuffer.append("\n");
					if((advisor.getTitle()!=null) && (! advisor.getTitle().trim().equals("")))
						commissionBuffer.append(advisor.getTitle() + " ");
					commissionBuffer.append(advisor.getName().getFirstname() + " ");
					commissionBuffer.append(advisor.getName().getLastname());
					boolean commaAdded = false;
					if(advisor.getCurrentPositionName() != null){
						commaAdded = true;
						if(advisor.getCurrentPositionName().equals("Docent")){
							commissionBuffer.append(", доцент");
						} else if(advisor.getCurrentPositionName().equals("Vаnredni profesor")){
							commissionBuffer.append(", ванр. проф.");
						} else if(advisor.getCurrentPositionName().equals("Profesor emeritus")){
							commissionBuffer.append(", проф. емеритус");
						} else if(advisor.getCurrentPositionName().equals("Naučni - saradnik")){
							commissionBuffer.append(", науч. сар.");
						} else if(advisor.getCurrentPositionName().equals("Viši naučni - saradnik")){
							commissionBuffer.append(", виши науч. сар.");
						} else if(advisor.getCurrentPositionName().equals("Naučni savetnik")){
							commissionBuffer.append(", науч. сав.");
						} else if(advisor.getCurrentPositionName().equals("Akademik")){
							commissionBuffer.append(", академик");
						} else {
							commissionBuffer.append(", ред. проф.");
						} 
					}
					if(advisor.getInstitution().getSomeName()!=null){
						commissionBuffer.append(((commaAdded)?(" "):(", ")) + advisor.getInstitution().getSomeName());
					}
					commissionBuffer.append(", ментор и није члан");
				}
				} else {
					commissionBuffer.append("\nMентор: без ментора");
				}
				
				commissionBufferStart.append(commissionBufferEnd.toString());
				commissionBuffer = commissionBufferStart;
				String commissionTemp = commissionBuffer.toString();
				commissionTemp = commissionTemp.replace(" u Novom Sadu, Novi Sad", ", Novi Sad");
				commissionTemp = commissionTemp.replace(" u Novom Sadu", ", Novi Sad");
				retVal.setCommissionMembers((translateToCyrillic)?(LatCyrUtils.toCyrillic(commissionTemp)):(commissionTemp));
				retVal.setDefendedOn(dissertation.getDefendedOn());
				if(translateToCyrillic){
					retVal.setPreviouslyGraduated(LatCyrUtils.toCyrillic(retVal.getPreviouslyGraduated()));
					retVal.setPreviouslyGraduatedPlace(LatCyrUtils.toCyrillic(retVal.getPreviouslyGraduatedPlace()));
					retVal.setNameOfAuthorDegree(LatCyrUtils.toCyrillic(retVal.getNameOfAuthorDegree()));
				}
			}
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read register entry for dissertation: "
					+ dissertationControlNumber);
			log.fatal(ex);
			return null;
		}
	}
	
	/**
	 * Retrieves all register entries.
	 * 
	 * @param conn
	 *            Database connection
	 * @return The retrieved register entries ; null if an error occured.
	 */
	public List<RegisterEntryDTO> getAllRegisterEntries(Connection conn) {
		List<RegisterEntryDTO> retVal = new ArrayList<RegisterEntryDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select REGISTERENTRYID, AUTHORNAME, AUTHORLASTNAME, FATHERNAME, FATHERLASTNAME, MOTHERNAME, MOTHERLASTNAME, GUARDIANSFULLNAME, " +
							"STREETANDNUMBER, PLACE, POSTALCODE, CITY, COUNTRY, EMAIL, PHONE, " +
							"BIRTHDATE, BIRTHPLACE, BIRTHCITY, BIRTHCOUNTRY, PREVIOUSLYGRADUATED, PREVIOUSLYGRADUATEDPLACE, PREVIOUSLYNAMEOFAUTHORDEGREEOLD, " +
							"PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONOLD, PREVIOUSLYNAMEOFAUTHORDEGREEDATEOLD, PREVIOUSLYNAMEOFAUTHORDEGREEBOLOGNA, PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONBOLOGNA, " +
							" PREVIOUSLYNAMEOFAUTHORDEGREEYEARBOLOGNA, " +
							"TITLE, INSTITUTION, INSTITUTIONPLACE, ADVISORS, COMMISSIONMEMBERS, DEFENDEDON, MARK, NAMEOFAUTHORDEGREE, " +
							"PROMOTIONDATE, DIPLOMANUMBER, SUPPLEMENTNUMBER, DIPLOMAPUBLICATIONDATE, SUPPLEMENTPUBLICATIONDATE, CREATOR, ACADEMICYEAR, ACADEMICYEARNUMBER, DISSERTATIONID, FUTUREPROMOTIONNAME, UNIVERSITYID from REGISTERENTRY");
			while (rset.next()) {		
				int i=1;
				
				String id = rset.getString(i++);
				
				String authorName = rset.getString(i++);
				String authorLastName = rset.getString(i++);
				String fatherName = rset.getString(i++);
				String fatherLastName = rset.getString(i++);
				String motherName = rset.getString(i++);
				String motherLastName = rset.getString(i++);
				String guardiansFullName = rset.getString(i++);
				
				String streetAndNumber = rset.getString(i++);
				String place = rset.getString(i++);
				String postalCode = rset.getString(i++);
				String city = rset.getString(i++);
				String country = rset.getString(i++);
				String email = rset.getString(i++);
				String phone = rset.getString(i++);
				
				Calendar birthDate = null;
				if (rset.getDate(i++) != null) {
					birthDate = new GregorianCalendar();
					birthDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String birthPlace = rset.getString(i++);
				String birthCity = rset.getString(i++);
				String birthCountry = rset.getString(i++);
				
				String previouslyGraduated = rset.getString(i++);
				String previouslyGraduatedPlace = rset.getString(i++);
				String previouslyNameOfAuthorDegreeOld = rset.getString(i++);
				String previouslyNameOfAuthorDegreeAbbreviationOld = rset.getString(i++);
				Calendar previouslyNameOfAuthorDegreeDateOld = null;
				if (rset.getDate(i++) != null) {
					previouslyNameOfAuthorDegreeDateOld = new GregorianCalendar();
					previouslyNameOfAuthorDegreeDateOld.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String previouslyNameOfAuthorDegreeBologna = rset.getString(i++);
				String previouslyNameOfAuthorDegreeAbbreviationBologna = rset.getString(i++);
				String previouslyNameOfAuthorDegreeYearBologna = rset.getString(i++);
				
				String title = rset.getString(i++);
				String institution = rset.getString(i++);
				String institutionPlace = rset.getString(i++);
				String advisors = rset.getString(i++);
				String commission = rset.getString(i++);
				Calendar defendedOn = null;
				if (rset.getDate(i++) != null) {
					defendedOn = new GregorianCalendar();
					defendedOn.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String mark = rset.getString(i++);
				String nameOfAuthorDegree = rset.getString(i++);
				
				Calendar promotionDate = null;
				if (rset.getDate(i++) != null) {
					promotionDate = new GregorianCalendar();
					promotionDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String diplomaNumber = rset.getString(i++);
				String supplementNumber = rset.getString(i++);
				Calendar diplomaPublicationDate = null;
				if (rset.getDate(i++) != null) {
					diplomaPublicationDate = new GregorianCalendar();
					diplomaPublicationDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				Calendar supplementPublicationDate = null;
				if (rset.getDate(i++) != null) {
					supplementPublicationDate = new GregorianCalendar();
					supplementPublicationDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				
				String user = rset.getString(i++);
				String academicYear = rset.getString(i++);
				int academicYearNumber = rset.getInt(i++);
				if(rset.wasNull())
					academicYearNumber = 0;
				
				
				StudyFinalDocumentDTO dissertation = new StudyFinalDocumentDTO();
				dissertation = (StudyFinalDocumentDTO)(new RecordDAO(new RecordDB()).getRecordFromDatabase(rset.getString(i++)).getDto());
				String futurePromotionName = rset.getString(i++);
				String universityID = rset.getString(i++);
				if(dissertation != null){
					RegisterEntryDTO re = dissertation.getRegisterEntry(); 
					re.setAuthor((AuthorDTO) new RecordDAO(new PersonDB()).getDTO(dissertation.getAuthor().getControlNumber()));
					re.getDissertation().getAcceptedOn(); // load object
					re.getAuthor().getAddress(); // load object
					re.setNotLoaded(false);
					
					re.setId(id);
					re.setAcademicYear(academicYear);
					re.setAcademicYearNumber(academicYearNumber);
					
					re.getAuthorName().setLastname(authorLastName);
					re.getAuthorName().setFirstname(authorName);
					re.getFatherName().setLastname(fatherLastName);
					re.getFatherName().setFirstname(fatherName);
					re.getMotherName().setLastname(motherLastName);
					re.getMotherName().setFirstname(motherName);
					re.setGuardiansName(guardiansFullName);
					
					re.setStreetAndNumber(streetAndNumber);
					re.setPlace(place);
					re.setPostalCode(postalCode);
					re.setCity(city);
					re.setCountry(country);
					re.setEmail(email);
					re.setPhone(phone);
					
					re.setBirthDate(birthDate);
					re.setBirthPlace(birthPlace);
					re.setBirthCity(birthCity);
					re.setBirthCountry(birthCountry);
					
					re.setPreviouslyGraduated(previouslyGraduated);
					re.setPreviouslyGraduatedPlace(previouslyGraduatedPlace);
					re.setPreviouslyNameOfAuthorDegreeOld(previouslyNameOfAuthorDegreeOld);
					re.setPreviouslyNameOfAuthorDegreeAbbreviationOld(previouslyNameOfAuthorDegreeAbbreviationOld);
					re.setPreviouslyNameOfAuthorDegreeDateOld(previouslyNameOfAuthorDegreeDateOld);
					re.setPreviouslyNameOfAuthorDegreeBologna(previouslyNameOfAuthorDegreeBologna);
					re.setPreviouslyNameOfAuthorDegreeAbbreviationBologna(previouslyNameOfAuthorDegreeAbbreviationBologna);
					re.setPreviouslyNameOfAuthorDegreeYearBologna(previouslyNameOfAuthorDegreeYearBologna);
					
					re.setTitle(title);
					re.setInstitution(institution);
					re.setInstitutionPlace(institutionPlace);
					re.setAdvisors(advisors);
					re.setCommissionMembers(commission);
					re.setDefendedOn(defendedOn);
					re.setMark(mark);
					re.setNameOfAuthorDegree(nameOfAuthorDegree);
					
					re.setPromotionDate(promotionDate);
					re.setDiplomaNumber(diplomaNumber);
					re.setSupplementNumber(supplementNumber);
					re.setDiplomaPublicationDate(diplomaPublicationDate);
					re.setSupplementPublicationDate(supplementPublicationDate);
					re.setFuturePromotionName(futurePromotionName);
					re.setUniversityId(universityID);
					
					re.setUser(user);
					retVal.add(re);
					
					dissertation.setNotLoaded(true);
					re.getAuthor().setNotLoaded(true);
				}	
			} 
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.fatal("Cannot read register entries." );
			log.fatal(ex);		
			return null;
		}
	}


	/**
	 * Retrieves an array of records.
	 * 
	 * @param conn
	 *            Database connection
	 * @param dissertations
	 *            The dissertations
	 * @return The array of register entries
	 */
	public List<RegisterEntryDTO> getRegisterEntries(Connection conn, List<StudyFinalDocumentDTO> dissertations, boolean translateToCyrillic) {
		List<RegisterEntryDTO> retVal = new ArrayList<RegisterEntryDTO>();
		try {
			for (StudyFinalDocumentDTO dissertation : dissertations) {
				RegisterEntryDTO registerEntry = getRegisterEntry(conn, dissertation, translateToCyrillic);
				if(registerEntry != null)
					retVal.add(registerEntry);
			}
			return retVal;
		} catch (Exception ex) {
			String s = "";
			for (StudyFinalDocumentDTO dissertation : dissertations)
				s += dissertation.getControlNumber() + ",";
			log
					.fatal("Cannot read multiple register entries with identifiers: "
							+ s);
			log.fatal(ex);
			return null;
		}
	}

	/**
	 * Updates the register entry in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param registerEntry
	 *            register entry to update
	 * @return true if successful
	 */
	public boolean addOrUpdate(Connection conn, RegisterEntryDTO registerEntry) {
		try{
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select REGISTERENTRYID  from REGISTERENTRY where DISSERTATIONID like '"
							+ registerEntry.getDissertation().getControlNumber() + "'");
			if (rset.next()) {
				return update(conn, registerEntry);
			} else {
				return add(conn, registerEntry);
			}
		} catch (Exception ex) {
			log.fatal("Cannot read register entry for dissertation: "
					+ registerEntry.getDissertation().getControlNumber());
			log.fatal(ex);
			return false;
		}
	}


	/**
	 * Adds a new register entry to the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param registerEntry
	 *            Register entry to add
	 * @return true if successful
	 * @throws SQLException
	 */
	public boolean add(Connection conn, RegisterEntryDTO registerEntry) {
		boolean retVal = false;
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into REGISTERENTRY (REGISTERENTRYID, AUTHORNAME, AUTHORLASTNAME, FATHERNAME, FATHERLASTNAME, MOTHERNAME, MOTHERLASTNAME, GUARDIANSFULLNAME, " +
							"STREETANDNUMBER, PLACE, POSTALCODE, CITY, COUNTRY, EMAIL, PHONE, " +
							"BIRTHDATE, BIRTHPLACE, BIRTHCITY, BIRTHCOUNTRY, PREVIOUSLYGRADUATED, PREVIOUSLYGRADUATEDPLACE, PREVIOUSLYNAMEOFAUTHORDEGREEOLD, " +
							"PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONOLD, PREVIOUSLYNAMEOFAUTHORDEGREEDATEOLD, PREVIOUSLYNAMEOFAUTHORDEGREEBOLOGNA, PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONBOLOGNA, " +
							" PREVIOUSLYNAMEOFAUTHORDEGREEYEARBOLOGNA, " +
							"TITLE, INSTITUTION, INSTITUTIONPLACE, ADVISORS, COMMISSIONMEMBERS, DEFENDEDON, MARK, NAMEOFAUTHORDEGREE, " +
							"PROMOTIONDATE, DIPLOMANUMBER, SUPPLEMENTNUMBER, DIPLOMAPUBLICATIONDATE, SUPPLEMENTPUBLICATIONDATE, CREATOR, ACADEMICYEAR, ACADEMICYEARNUMBER, FUTUREPROMOTIONNAME, UNIVERSITYID, DISSERTATIONID) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			String id = registerEntry.getId();
			int i = 1;
			if (id == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, id);
			
			String authorName = registerEntry.getAuthorName().getFirstname();
			if (authorName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, authorName);
			String authorLastName = registerEntry.getAuthorName().getLastname();
			if (authorLastName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, authorLastName);
			String fatherName = registerEntry.getFatherName().getFirstname();
			if (fatherName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, fatherName);
			String fatherLastName = registerEntry.getFatherName().getLastname();
			if (fatherLastName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, fatherLastName);
			String motherName = registerEntry.getMotherName().getFirstname();
			if (motherName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, motherName);
			String motherLastName = registerEntry.getMotherName().getLastname();
			if (motherLastName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, motherLastName);
			String guardiansFullName = registerEntry.getGuardiansName();
			if (guardiansFullName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, guardiansFullName);
			
			String streetAndNumber = registerEntry.getStreetAndNumber();
			if (streetAndNumber == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, streetAndNumber);
			
			String place = registerEntry.getPlace();
			if (place == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, place);
			
			String postalCode = registerEntry.getPostalCode();
			if (postalCode == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, postalCode);
			
			String city = registerEntry.getCity();
			if (city == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, city);
			
			String country = registerEntry.getCountry();
			if (country == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, country);
			
			String email = registerEntry.getEmail();
			if (email == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, email);
			
			String phone = registerEntry.getPhone();
			if (phone == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, phone);
			
			Calendar birthDate = registerEntry.getBirthDate();
			if (birthDate == null){
				stmt.setNull(i++, Types.DATE);
			}
			else {
				stmt.setDate(i++, new java.sql.Date(birthDate.getTime()
						.getTime()));
			}
			String birthPlace = registerEntry.getBirthPlace();
			if (birthPlace == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, birthPlace);
			String birthCity = registerEntry.getBirthCity();
			if (birthCity == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, birthCity);
			String birthCountry = registerEntry.getBirthCountry();
			if (birthCountry == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, birthCountry);
	
			String previouslyGraduated = registerEntry.getPreviouslyGraduated();
			if (previouslyGraduated == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, previouslyGraduated);
			String previouslyGraduatedPlace = registerEntry.getPreviouslyGraduatedPlace();
			if (previouslyGraduatedPlace == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, previouslyGraduatedPlace);
			String previouslyNameOfAuthorDegreeOld = registerEntry.getPreviouslyNameOfAuthorDegreeOld();
			if (previouslyNameOfAuthorDegreeOld == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, previouslyNameOfAuthorDegreeOld);
			String previouslyNameOfAuthorDegreeAbbreviationOld = registerEntry.getPreviouslyNameOfAuthorDegreeAbbreviationOld();
			if (previouslyNameOfAuthorDegreeAbbreviationOld == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, previouslyNameOfAuthorDegreeAbbreviationOld);
			Calendar previouslyNameOfAuthorDegreeDateOld = registerEntry.getPreviouslyNameOfAuthorDegreeDateOld();
			if (previouslyNameOfAuthorDegreeDateOld == null){
				stmt.setNull(i++, Types.DATE);
			}
			else {
				stmt.setDate(i++, new java.sql.Date(previouslyNameOfAuthorDegreeDateOld.getTime()
						.getTime()));
			}
			String previouslyNameOfAuthorDegreeBologna = registerEntry.getPreviouslyNameOfAuthorDegreeBologna();
			if (previouslyNameOfAuthorDegreeBologna == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, previouslyNameOfAuthorDegreeBologna);
			String previouslyNameOfAuthorDegreeAbbreviationBologna = registerEntry.getPreviouslyNameOfAuthorDegreeAbbreviationBologna();
			if (previouslyNameOfAuthorDegreeAbbreviationBologna == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, previouslyNameOfAuthorDegreeAbbreviationBologna);
			String previouslyNameOfAuthorDegreeYearBologna = registerEntry.getPreviouslyNameOfAuthorDegreeYearBologna();
			if (previouslyNameOfAuthorDegreeYearBologna == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, previouslyNameOfAuthorDegreeYearBologna);
			
			String title = registerEntry.getTitle();
			if (title == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, title);
			String institution = registerEntry.getInstitution();
			if (institution == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, institution);
			String institutionPlace = registerEntry.getInstitutionPlace();
			if (institutionPlace == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, institutionPlace);
			String advisors = registerEntry.getAdvisors();
			if (advisors == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, advisors);
			String commission = registerEntry.getCommissionMembers();
			if (commission == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, commission);
			
			Calendar defendedOn = registerEntry.getDefendedOn();
			if (defendedOn == null){
				stmt.setNull(i++, Types.DATE);
			}
			else {
				stmt.setDate(i++, new java.sql.Date(defendedOn.getTime()
						.getTime()));
			}
			String mark = registerEntry.getMark();
			if (mark == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, mark);
			String nameOfAuthorDegree = registerEntry.getNameOfAuthorDegree();
			if (nameOfAuthorDegree == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, nameOfAuthorDegree);
			Calendar promotionDate = registerEntry.getPromotionDate();
			if (promotionDate == null){
				stmt.setNull(i++, Types.DATE);
			}
			else {
				stmt.setDate(i++, new java.sql.Date(promotionDate.getTime()
						.getTime()));
			}
			String diplomaNumber = registerEntry.getDiplomaNumber();
			if (diplomaNumber == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, diplomaNumber);
			String supplementNumber = registerEntry.getSupplementNumber();
			if (supplementNumber == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, supplementNumber);
			Calendar diplomaPublicationDate = registerEntry.getDiplomaPublicationDate();
			if (diplomaPublicationDate == null){
				stmt.setNull(i++, Types.DATE);
			}
			else {
				stmt.setDate(i++, new java.sql.Date(diplomaPublicationDate.getTime()
						.getTime()));
			}
			Calendar supplementPublicationDate = registerEntry.getSupplementPublicationDate();
			if (supplementPublicationDate == null){
				stmt.setNull(i++, Types.DATE);
			}
			else {
				stmt.setDate(i++, new java.sql.Date(supplementPublicationDate.getTime()
						.getTime()));
			}
			
			String creator = registerEntry.getUser();
			if (creator == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, creator);
			String academicYear = registerEntry.getAcademicYear();
			if (academicYear == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, academicYear);
			int academicYearNumber = registerEntry.getAcademicYearNumber();
			if (academicYearNumber == 0)
				stmt.setNull(i++, Types.INTEGER);
			else
				stmt.setInt(i++, academicYearNumber);
			String futurePromotionName = registerEntry.getFuturePromotionName();
			if(futurePromotionName==null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, futurePromotionName);
			
			String univerityID = registerEntry.getUniversityId();
			if(univerityID==null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, univerityID);
			
			String dissertationId = registerEntry.getDissertation().getControlNumber();
			if (dissertationId == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, dissertationId);
			
			if(stmt.executeUpdate() > 0)
				retVal = true;
			stmt.close();
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add register entry for dissertation: " + registerEntry.getDissertation().getControlNumber());
		}
		return retVal;
	}
	
	/**
	 * Updates the register entry in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param registerEntry
	 *            register entry to update
	 * @return true if successful
	 */
	public boolean update(Connection conn, RegisterEntryDTO registerEntry) {
		boolean retVal = false;
		try {
			PreparedStatement stmt = conn
					.prepareStatement("update REGISTERENTRY set REGISTERENTRYID=?, AUTHORNAME=?, AUTHORLASTNAME=?, FATHERNAME=?, FATHERLASTNAME=?, MOTHERNAME=?, MOTHERLASTNAME=?, GUARDIANSFULLNAME=?, " +
							"STREETANDNUMBER=?, PLACE=?, POSTALCODE=?, CITY=?, COUNTRY=?, EMAIL=?, PHONE=?, " +
							"BIRTHDATE=?, BIRTHPLACE=?, BIRTHCITY=?, BIRTHCOUNTRY=?, PREVIOUSLYGRADUATED=?, PREVIOUSLYGRADUATEDPLACE=?, PREVIOUSLYNAMEOFAUTHORDEGREEOLD=?, " +
							"PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONOLD=?, PREVIOUSLYNAMEOFAUTHORDEGREEDATEOLD=?, PREVIOUSLYNAMEOFAUTHORDEGREEBOLOGNA=?, PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONBOLOGNA=?, " +
							" PREVIOUSLYNAMEOFAUTHORDEGREEYEARBOLOGNA=?, " +
							"TITLE=?, INSTITUTION=?, INSTITUTIONPLACE=?, ADVISORS=?, COMMISSIONMEMBERS=?, DEFENDEDON=?, MARK=?, NAMEOFAUTHORDEGREE=?, " +
							"PROMOTIONDATE=?, DIPLOMANUMBER=?, SUPPLEMENTNUMBER=?, DIPLOMAPUBLICATIONDATE=?, SUPPLEMENTPUBLICATIONDATE=?, CREATOR=?, ACADEMICYEAR=?, ACADEMICYEARNUMBER=?, FUTUREPROMOTIONNAME=?, UNIVERSITYID=? where DISSERTATIONID like ?");
			String id = registerEntry.getId();
			int i = 1;
			if (id == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, id);
			
			String authorName = registerEntry.getAuthorName().getFirstname();
			if (authorName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, authorName);
			String authorLastName = registerEntry.getAuthorName().getLastname();
			if (authorLastName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, authorLastName);
			String fatherName = registerEntry.getFatherName().getFirstname();
			if (fatherName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, fatherName);
			String fatherLastName = registerEntry.getFatherName().getLastname();
			if (fatherLastName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, fatherLastName);
			String motherName = registerEntry.getMotherName().getFirstname();
			if (motherName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, motherName);
			String motherLastName = registerEntry.getMotherName().getLastname();
			if (motherLastName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, motherLastName);
			String guardiansFullName = registerEntry.getGuardiansName();
			if (guardiansFullName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, guardiansFullName);
			
			String streetAndNumber = registerEntry.getStreetAndNumber();
			if (streetAndNumber == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, streetAndNumber);
			
			String place = registerEntry.getPlace();
			if (place == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, place);
			
			String postalCode = registerEntry.getPostalCode();
			if (postalCode == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, postalCode);
			
			String city = registerEntry.getCity();
			if (city == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, city);
			
			String country = registerEntry.getCountry();
			if (country == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, country);
			
			String email = registerEntry.getEmail();
			if (email == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, email);
			
			String phone = registerEntry.getPhone();
			if (phone == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, phone);
			
			Calendar birthDate = registerEntry.getBirthDate();
			if (birthDate == null){
				stmt.setNull(i++, Types.DATE);
			}
			else {
				stmt.setDate(i++, new java.sql.Date(birthDate.getTime()
						.getTime()));
			}
			String birthPlace = registerEntry.getBirthPlace();
			if (birthPlace == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, birthPlace);
			String birthCity = registerEntry.getBirthCity();
			if (birthCity == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, birthCity);
			String birthCountry = registerEntry.getBirthCountry();
			if (birthCountry == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, birthCountry);
	
			String previouslyGraduated = registerEntry.getPreviouslyGraduated();
			if (previouslyGraduated == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, previouslyGraduated);
			String previouslyGraduatedPlace = registerEntry.getPreviouslyGraduatedPlace();
			if (previouslyGraduatedPlace == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, previouslyGraduatedPlace);
			String previouslyNameOfAuthorDegreeOld = registerEntry.getPreviouslyNameOfAuthorDegreeOld();
			if (previouslyNameOfAuthorDegreeOld == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, previouslyNameOfAuthorDegreeOld);
			String previouslyNameOfAuthorDegreeAbbreviationOld = registerEntry.getPreviouslyNameOfAuthorDegreeAbbreviationOld();
			if (previouslyNameOfAuthorDegreeAbbreviationOld == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, previouslyNameOfAuthorDegreeAbbreviationOld);
			Calendar previouslyNameOfAuthorDegreeDateOld = registerEntry.getPreviouslyNameOfAuthorDegreeDateOld();
			if (previouslyNameOfAuthorDegreeDateOld == null){
				stmt.setNull(i++, Types.DATE);
			}
			else {
				stmt.setDate(i++, new java.sql.Date(previouslyNameOfAuthorDegreeDateOld.getTime()
						.getTime()));
			}
			String previouslyNameOfAuthorDegreeBologna = registerEntry.getPreviouslyNameOfAuthorDegreeBologna();
			if (previouslyNameOfAuthorDegreeBologna == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, previouslyNameOfAuthorDegreeBologna);
			String previouslyNameOfAuthorDegreeAbbreviationBologna = registerEntry.getPreviouslyNameOfAuthorDegreeAbbreviationBologna();
			if (previouslyNameOfAuthorDegreeAbbreviationBologna == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, previouslyNameOfAuthorDegreeAbbreviationBologna);
			String previouslyNameOfAuthorDegreeYearBologna = registerEntry.getPreviouslyNameOfAuthorDegreeYearBologna();
			if (previouslyNameOfAuthorDegreeYearBologna == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, previouslyNameOfAuthorDegreeYearBologna);
			
			String title = registerEntry.getTitle();
			if (title == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, title);
			String institution = registerEntry.getInstitution();
			if (institution == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, institution);
			String institutionPlace = registerEntry.getInstitutionPlace();
			if (institutionPlace == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, institutionPlace);
			String advisors = registerEntry.getAdvisors();
			if (advisors == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, advisors);
			String commission = registerEntry.getCommissionMembers();
			if (commission == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, commission);
			
			Calendar defendedOn = registerEntry.getDefendedOn();
			if (defendedOn == null){
				stmt.setNull(i++, Types.DATE);
			}
			else {
				stmt.setDate(i++, new java.sql.Date(defendedOn.getTime()
						.getTime()));
			}
			String mark = registerEntry.getMark();
			if (mark == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, mark);
			String nameOfAuthorDegree = registerEntry.getNameOfAuthorDegree();
			if (nameOfAuthorDegree == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, nameOfAuthorDegree);
			Calendar promotionDate = registerEntry.getPromotionDate();
			if (promotionDate == null){
				stmt.setNull(i++, Types.DATE);
			}
			else {
				stmt.setDate(i++, new java.sql.Date(promotionDate.getTime()
						.getTime()));
			}
			String diplomaNumber = registerEntry.getDiplomaNumber();
			if (diplomaNumber == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, diplomaNumber);
			String supplementNumber = registerEntry.getSupplementNumber();
			if (supplementNumber == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, supplementNumber);
			Calendar diplomaPublicationDate = registerEntry.getDiplomaPublicationDate();
			if (diplomaPublicationDate == null){
				stmt.setNull(i++, Types.DATE);
			}
			else {
				stmt.setDate(i++, new java.sql.Date(diplomaPublicationDate.getTime()
						.getTime()));
			}
			Calendar supplementPublicationDate = registerEntry.getSupplementPublicationDate();
			if (supplementPublicationDate == null){
				stmt.setNull(i++, Types.DATE);
			}
			else {
				stmt.setDate(i++, new java.sql.Date(supplementPublicationDate.getTime()
						.getTime()));
			}
			
			String creator = registerEntry.getUser();
			if (creator == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, creator);
			String academicYear = registerEntry.getAcademicYear();
			if (academicYear == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, academicYear);
			int academicYearNumber = registerEntry.getAcademicYearNumber();
			if (academicYearNumber == 0)
				stmt.setNull(i++, Types.INTEGER);
			else
				stmt.setInt(i++, academicYearNumber);
			String futurePromotionName = registerEntry.getFuturePromotionName();
			if (futurePromotionName == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, futurePromotionName);
			String universityID = registerEntry.getUniversityId();
			if(universityID == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, universityID);			
			
			String dissertationId = registerEntry.getDissertation().getControlNumber();
			if (dissertationId == null)
				stmt.setNull(i++, Types.VARCHAR);
			else
				stmt.setString(i++, dissertationId);
			
			if(stmt.executeUpdate() > 0)
				retVal = true;
			stmt.close();
		} catch (SQLException ex) {
			log.fatal("Cannot update register entry for dissertation: " + registerEntry.getDissertation().getControlNumber());
			log.fatal(ex);
		}
		return retVal;
	}
	
	/**
	 * Deletes a register entry from the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param dissertation
	 *            dissertation register entry to delete
	 * @return true if successful
	 */
	public boolean delete(Connection conn, StudyFinalDocumentDTO dissertation) {
		boolean retVal = false;
			retVal = delete(conn, dissertation.getControlNumber());
		return retVal;
	}

	/**
	 * Deletes a register entry from the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param dissertationId
	 *            dissertation identifier
	 * @return true if successful
	 */
	protected boolean delete(Connection conn, String dissertationId) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from REGISTERENTRY where DISSERTATIONID like '"
							+ dissertationId + "'");
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal("Cannot delete Register entry for dissertation with control number: "
					+ dissertationId);
			log.fatal(ex);
			return false;
		}
	}
	
	public List<RegisterEntryDTO> getRegisterEntriesWithoutId(Connection conn, String universityID, String institutionString){
		List<RegisterEntryDTO> retVal = new ArrayList<RegisterEntryDTO>();
		try {
			PreparedStatement stmt = conn.prepareStatement("select REGISTERENTRYID, AUTHORNAME, AUTHORLASTNAME, FATHERNAME, FATHERLASTNAME, MOTHERNAME, MOTHERLASTNAME, GUARDIANSFULLNAME, " +
					"STREETANDNUMBER, PLACE, POSTALCODE, CITY, COUNTRY, EMAIL, PHONE, " +
					"BIRTHDATE, BIRTHPLACE, BIRTHCITY, BIRTHCOUNTRY, PREVIOUSLYGRADUATED, PREVIOUSLYGRADUATEDPLACE, PREVIOUSLYNAMEOFAUTHORDEGREEOLD, " +
					"PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONOLD, PREVIOUSLYNAMEOFAUTHORDEGREEDATEOLD, PREVIOUSLYNAMEOFAUTHORDEGREEBOLOGNA, PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONBOLOGNA, " +
					" PREVIOUSLYNAMEOFAUTHORDEGREEYEARBOLOGNA, " +
					"TITLE, INSTITUTION, INSTITUTIONPLACE, ADVISORS, COMMISSIONMEMBERS, DEFENDEDON, MARK, NAMEOFAUTHORDEGREE, " +
					"PROMOTIONDATE, DIPLOMANUMBER, SUPPLEMENTNUMBER, DIPLOMAPUBLICATIONDATE, SUPPLEMENTPUBLICATIONDATE, CREATOR, ACADEMICYEAR, ACADEMICYEARNUMBER, FUTUREPROMOTIONNAME, DISSERTATIONID from REGISTERENTRY " +
					"where (REGISTERENTRYID like '-1' or REGISTERENTRYID like '-2') and PROMOTIONDATE is null and UNIVERSITYID like ? and INSTITUTION like ? order by AUTHORLASTNAME");
			stmt.setString(1, universityID);
			if(institutionString == null)
				institutionString = "%";
			stmt.setString(2, institutionString);
			ResultSet rset = stmt.executeQuery();
			
			while (rset.next()) {		
				int i=1;
				
				String id = rset.getString(i++);
				
				String authorName = rset.getString(i++);
				String authorLastName = rset.getString(i++);
				String fatherName = rset.getString(i++);
				String fatherLastName = rset.getString(i++);
				String motherName = rset.getString(i++);
				String motherLastName = rset.getString(i++);
				String guardiansFullName = rset.getString(i++);
				
				String streetAndNumber = rset.getString(i++);
				String place = rset.getString(i++);
				String postalCode = rset.getString(i++);
				String city = rset.getString(i++);
				String country = rset.getString(i++);
				String email = rset.getString(i++);
				String phone = rset.getString(i++);
				
				Calendar birthDate = null;
				if (rset.getDate(i++) != null) {
					birthDate = new GregorianCalendar();
					birthDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String birthPlace = rset.getString(i++);
				String birthCity = rset.getString(i++);
				String birthCountry = rset.getString(i++);
				
				String previouslyGraduated = rset.getString(i++);
				String previouslyGraduatedPlace = rset.getString(i++);
				String previouslyNameOfAuthorDegreeOld = rset.getString(i++);
//	 ako je po starom ucitacemo dodatne podatke zbog ispisa podataka za diplomu
				boolean isMr = previouslyNameOfAuthorDegreeOld!=null && previouslyNameOfAuthorDegreeOld.equals("Магистарске студије");
				String previouslyNameOfAuthorDegreeAbbreviationOld = rset.getString(i++);
				Calendar previouslyNameOfAuthorDegreeDateOld = null;
				if (rset.getDate(i++) != null) {
					previouslyNameOfAuthorDegreeDateOld = new GregorianCalendar();
					previouslyNameOfAuthorDegreeDateOld.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String previouslyNameOfAuthorDegreeBologna = rset.getString(i++);
				String previouslyNameOfAuthorDegreeAbbreviationBologna = rset.getString(i++);
				String previouslyNameOfAuthorDegreeYearBologna = rset.getString(i++);
				
				String title = rset.getString(i++);
				String institution = rset.getString(i++);
				String institutionPlace = rset.getString(i++);
				String advisors = rset.getString(i++);
				String commission = rset.getString(i++);
				Calendar defendedOn = null;
				if (rset.getDate(i++) != null) {
					defendedOn = new GregorianCalendar();
					defendedOn.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String mark = rset.getString(i++);
				String nameOfAuthorDegree = rset.getString(i++);
				
				Calendar promotionDate = null;
				if (rset.getDate(i++) != null) {
					promotionDate = new GregorianCalendar();
					promotionDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String diplomaNumber = rset.getString(i++);
				String supplementNumber = rset.getString(i++);
				Calendar diplomaPublicationDate = null;
				if (rset.getDate(i++) != null) {
					diplomaPublicationDate = new GregorianCalendar();
					diplomaPublicationDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				Calendar supplementPublicationDate = null;
				if (rset.getDate(i++) != null) {
					supplementPublicationDate = new GregorianCalendar();
					supplementPublicationDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				
				String user = rset.getString(i++);
				String academicYear = rset.getString(i++);
				int academicYearNumber = rset.getInt(i++);
				if(rset.wasNull())
					academicYearNumber = 0;
				String futurePromotionName = rset.getString(i++);
				
				
				StudyFinalDocumentDTO dissertation = new StudyFinalDocumentDTO();
				dissertation = (StudyFinalDocumentDTO)(new RecordDAO(new RecordDB()).getRecordFromDatabase(rset.getString(i++)).getDto());
				if(dissertation != null){
					RegisterEntryDTO re = dissertation.getRegisterEntry(); 
					re.setAuthor(null);
//					re.getDissertation().getAcceptedOn(); // load object
//					re.getAuthor().getAddress(); // load object
					/*if(isMr)  
						re.setAuthor((AuthorDTO) new RecordDAO(new PersonDB()).getDTO(dissertation.getAuthor().getControlNumber()));*/
					re.setNotLoaded(false);
					
					re.setId(id);
//					re.setAcademicYear(academicYear);
//					re.setAcademicYearNumber(academicYearNumber);
					re.setFuturePromotionName(futurePromotionName);
					re.getAuthorName().setLastname(authorLastName);
					re.getAuthorName().setFirstname(authorName);
	//				re.getFatherName().setLastname(fatherLastName);
		//   		re.getFatherName().setFirstname(fatherName);
//					re.getMotherName().setLastname(motherLastName);
//					re.getMotherName().setFirstname(motherName);
//					re.setGuardiansName(guardiansFullName);
					
					re.setStreetAndNumber(streetAndNumber);
					re.setPlace(place);
					re.setPostalCode(postalCode);
					re.setCity(city);
					re.setCountry(country);
					re.setEmail(email);
					re.setPhone(phone);
					/*
					if(isMr){
						if(birthDate!=null)
							re.setBirthDate(birthDate);
						if(birthPlace!=null)
							re.setBirthPlace(birthPlace);
						if(birthCity!=null)
							re.setBirthCity(birthCity);
						if(birthCountry!=null)
							re.setBirthCountry(birthCountry);
					}
					*/
//					re.setPreviouslyGraduated(previouslyGraduated);
//					re.setPreviouslyGraduatedPlace(previouslyGraduatedPlace);
						re.setPreviouslyNameOfAuthorDegreeOld(previouslyNameOfAuthorDegreeOld);
//					re.setPreviouslyNameOfAuthorDegreeAbbreviationOld(previouslyNameOfAuthorDegreeAbbreviationOld);
					re.setPreviouslyNameOfAuthorDegreeDateOld(previouslyNameOfAuthorDegreeDateOld);
//					re.setPreviouslyNameOfAuthorDegreeBologna(previouslyNameOfAuthorDegreeBologna);
//					re.setPreviouslyNameOfAuthorDegreeAbbreviationBologna(previouslyNameOfAuthorDegreeAbbreviationBologna);
//					re.setPreviouslyNameOfAuthorDegreeYearBologna(previouslyNameOfAuthorDegreeYearBologna);
					
					re.setTitle(title);
					re.setInstitution(institution);
					re.setInstitutionPlace(institutionPlace);
//					re.setAdvisors(advisors);
//					re.setCommissionMembers(commission);
					re.setDefendedOn(defendedOn);
//					re.setMark(mark);
					re.setNameOfAuthorDegree(nameOfAuthorDegree);
					
//					re.setPromotionDate(promotionDate);
 	 				re.setDiplomaNumber(diplomaNumber);
	 	 			re.setSupplementNumber(supplementNumber);
	 	 			re.setDiplomaPublicationDate(diplomaPublicationDate);
//					re.setSupplementPublicationDate(supplementPublicationDate);
					
//					re.setUser(user);
					retVal.add(re);
					
					dissertation.setNotLoaded(true);
//					re.getAuthor().setNotLoaded(true);
				}	
			} 
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.fatal("Cannot read register entries without id." );
			log.fatal(ex);		
			ex.printStackTrace();
			return null;
		}
		
	}	
	
	
	public boolean saveRegisterEntryForPromotion(Connection conn, RegisterEntryDTO regEntry){
		boolean retVal = false;
		try {
			PreparedStatement stmt;
			if(regEntry.isOldProgram()){
			 stmt = conn
					.prepareStatement("update REGISTERENTRY set REGISTERENTRYID= ?, FUTUREPROMOTIONNAME=?, DIPLOMANUMBER=?, DIPLOMAPUBLICATIONDATE=? where DISSERTATIONID like ?");
			stmt.setString(1, regEntry.getId());
			stmt.setString(2, regEntry.getFuturePromotionName());			
			stmt.setString(3, regEntry.getDiplomaNumber());		
			if (regEntry.getDiplomaPublicationDate() == null){
				stmt.setNull(4, Types.DATE);
			}
			else {
				stmt.setDate(4, new java.sql.Date(regEntry.getDiplomaPublicationDate().getTime()
						.getTime()));
			}						
			stmt.setString(5, regEntry.getDissertation().getControlNumber());
			}else{
				stmt = conn
				.prepareStatement("update REGISTERENTRY set REGISTERENTRYID= ?, FUTUREPROMOTIONNAME=? where DISSERTATIONID like ?");
				stmt.setString(1, regEntry.getId());
				stmt.setString(2, regEntry.getFuturePromotionName());			
				stmt.setString(3, regEntry.getDissertation().getControlNumber());
			}
			if(stmt.executeUpdate() > 0)
				retVal = true;
			stmt.close();
			
		}catch(SQLException ex){
			ex.printStackTrace();
			log.fatal("Cannot save Register entry for for promotion with control number: "
					+ regEntry.getDissertation().getControlNumber());
			log.fatal(ex);			
		}
		return retVal;
	}
	
	public boolean savePromotionDateForRegisterEntry(Connection conn, RegisterEntryDTO regEntry, Date promotionDate){
		boolean retVal = false;
		try {
			PreparedStatement stmt = conn
					.prepareStatement("update REGISTERENTRY set PROMOTIONDATE= ?, FUTUREPROMOTIONNAME=null where DISSERTATIONID like ?");
			stmt.setDate(1, new java.sql.Date(promotionDate.getTime()));	
			stmt.setString(2, regEntry.getDissertation().getControlNumber());
			if(stmt.executeUpdate() > 0)
				retVal = true;
			stmt.close();
			
		}catch(SQLException ex){
			ex.printStackTrace();
			log.fatal("Cannot save promotion date with control number: "
					+ regEntry.getDissertation().getControlNumber());
			log.fatal(ex);			
		}
		return retVal;
	}
	
	public List<NumberPhDPerInstitution> getNumberPhDPerIntitution(Connection conn, Date dateFrom, Date dateTo, String universityId){
		List<NumberPhDPerInstitution> retVal = new ArrayList<NumberPhDPerInstitution>();		
		try{
			PreparedStatement stmt = conn
				.prepareStatement("SELECT r.INSTITUTION, count(r.PREVIOUSLYNAMEOFAUTHORDEGREEOLD), count(*) " +
						"FROM REGISTERENTRY r " +
						"where r.PROMOTIONDATE is not null and r.PROMOTIONDATE >= ? and r.PROMOTIONDATE <= ? and r.UNIVERSITYID like ? group by r.INSTITUTION;");
			stmt.setDate(1, new java.sql.Date(dateFrom.getTime()));
			stmt.setDate(2, new java.sql.Date(dateTo.getTime()));	
			stmt.setString(3, universityId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				String institutionName = rs.getString(1);
			    int countOld = rs.getInt(2);
			    int countAll = rs.getInt(3);
			    retVal.add(new NumberPhDPerInstitution(institutionName, countOld, countAll-countOld));
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			log.fatal("Cannot retrieve number of PhDs per institution");
			log.fatal(ex);			
		}		
		return retVal;
	}
	
	public List<RegisterEntryDTO> getPromotedInPeriod(Connection conn, Date dateFrom, Date dateTo, String universityId, String institutionString){
		List<RegisterEntryDTO> retVal = new ArrayList<RegisterEntryDTO>();
		try {
			PreparedStatement stmt = conn.prepareStatement("select REGISTERENTRYID, AUTHORNAME, AUTHORLASTNAME, FATHERNAME, FATHERLASTNAME, MOTHERNAME, MOTHERLASTNAME, GUARDIANSFULLNAME, " +
					"STREETANDNUMBER, PLACE, POSTALCODE, CITY, COUNTRY, EMAIL, PHONE, " +
					"BIRTHDATE, BIRTHPLACE, BIRTHCITY, BIRTHCOUNTRY, PREVIOUSLYGRADUATED, PREVIOUSLYGRADUATEDPLACE, PREVIOUSLYNAMEOFAUTHORDEGREEOLD, " +
					"PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONOLD, PREVIOUSLYNAMEOFAUTHORDEGREEDATEOLD, PREVIOUSLYNAMEOFAUTHORDEGREEBOLOGNA, PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONBOLOGNA, " +
					" PREVIOUSLYNAMEOFAUTHORDEGREEYEARBOLOGNA, " +
					"TITLE, INSTITUTION, INSTITUTIONPLACE, ADVISORS, COMMISSIONMEMBERS, DEFENDEDON, MARK, NAMEOFAUTHORDEGREE, " +
					"PROMOTIONDATE, DIPLOMANUMBER, SUPPLEMENTNUMBER, DIPLOMAPUBLICATIONDATE, SUPPLEMENTPUBLICATIONDATE, CREATOR, ACADEMICYEAR, ACADEMICYEARNUMBER, DISSERTATIONID from REGISTERENTRY " +
					"WHERE PROMOTIONDATE >= ? AND PROMOTIONDATE <= ? AND UNIVERSITYID like ? AND INSTITUTION like ?");
			stmt.setDate(1, new java.sql.Date(dateFrom.getTime()));
			stmt.setDate(2, new java.sql.Date(dateTo.getTime()));	
			stmt.setString(3, universityId);
			if(institutionString == null)
				institutionString = "%";
			stmt.setString(4, institutionString);
			
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {		
				int i=1;
				
				String id = rset.getString(i++);
				
				String authorName = rset.getString(i++);
				String authorLastName = rset.getString(i++);
				String fatherName = rset.getString(i++);
				String fatherLastName = rset.getString(i++);
				String motherName = rset.getString(i++);
				String motherLastName = rset.getString(i++);
				String guardiansFullName = rset.getString(i++);
				
				String streetAndNumber = rset.getString(i++);
				String place = rset.getString(i++);
				String postalCode = rset.getString(i++);
				String city = rset.getString(i++);
				String country = rset.getString(i++);
				String email = rset.getString(i++);
				String phone = rset.getString(i++);
				
				Calendar birthDate = null;
				if (rset.getDate(i++) != null) {
					birthDate = new GregorianCalendar();
					birthDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String birthPlace = rset.getString(i++);
				String birthCity = rset.getString(i++);
				String birthCountry = rset.getString(i++);
				
				String previouslyGraduated = rset.getString(i++);
				String previouslyGraduatedPlace = rset.getString(i++);
				String previouslyNameOfAuthorDegreeOld = rset.getString(i++);
				String previouslyNameOfAuthorDegreeAbbreviationOld = rset.getString(i++);
				Calendar previouslyNameOfAuthorDegreeDateOld = null;
				if (rset.getDate(i++) != null) {
					previouslyNameOfAuthorDegreeDateOld = new GregorianCalendar();
					previouslyNameOfAuthorDegreeDateOld.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String previouslyNameOfAuthorDegreeBologna = rset.getString(i++);
				String previouslyNameOfAuthorDegreeAbbreviationBologna = rset.getString(i++);
				String previouslyNameOfAuthorDegreeYearBologna = rset.getString(i++);
				
				String title = rset.getString(i++);
				String institution = rset.getString(i++);
				String institutionPlace = rset.getString(i++);
				String advisors = rset.getString(i++);
				String commission = rset.getString(i++);
				Calendar defendedOn = null;
				if (rset.getDate(i++) != null) {
					defendedOn = new GregorianCalendar();
					defendedOn.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String mark = rset.getString(i++);
				String nameOfAuthorDegree = rset.getString(i++);
				
				Calendar promotionDate = null;
				if (rset.getDate(i++) != null) {
					promotionDate = new GregorianCalendar();
					promotionDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String diplomaNumber = rset.getString(i++);
				String supplementNumber = rset.getString(i++);
				Calendar diplomaPublicationDate = null;
				if (rset.getDate(i++) != null) {
					diplomaPublicationDate = new GregorianCalendar();
					diplomaPublicationDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				Calendar supplementPublicationDate = null;
				if (rset.getDate(i++) != null) {
					supplementPublicationDate = new GregorianCalendar();
					supplementPublicationDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				
				String user = rset.getString(i++);
				String academicYear = rset.getString(i++);
				int academicYearNumber = rset.getInt(i++);
				if(rset.wasNull())
					academicYearNumber = 0;
				
				
				
				StudyFinalDocumentDTO dissertation = new StudyFinalDocumentDTO();
				dissertation = (StudyFinalDocumentDTO)(new RecordDAO(new RecordDB()).getRecordFromDatabase(rset.getString(i++)).getDto());
				if(dissertation != null){
					RegisterEntryDTO re = dissertation.getRegisterEntry(); 
					re.setAuthor((AuthorDTO) new RecordDAO(new PersonDB()).getDTO(dissertation.getAuthor().getControlNumber()));
					re.getDissertation().getAcceptedOn(); // load object
					re.getAuthor().getAddress(); // load object
					re.setNotLoaded(false);
					
					re.setId(id);
					re.setAcademicYear(academicYear);
					re.setAcademicYearNumber(academicYearNumber);
					re.setUniversityId(universityId);
					
					re.getAuthorName().setLastname(authorLastName);
					re.getAuthorName().setFirstname(authorName);
					re.getFatherName().setLastname(fatherLastName);
					re.getFatherName().setFirstname(fatherName);
					re.getMotherName().setLastname(motherLastName);
					re.getMotherName().setFirstname(motherName);
					re.setGuardiansName(guardiansFullName);
					
					re.setStreetAndNumber(streetAndNumber);
					re.setPlace(place);
					re.setPostalCode(postalCode);
					re.setCity(city);
					re.setCountry(country);
					re.setEmail(email);
					re.setPhone(phone);
					
					re.setBirthDate(birthDate);
					re.setBirthPlace(birthPlace);
					re.setBirthCity(birthCity);
					re.setBirthCountry(birthCountry);
					
					re.setPreviouslyGraduated(previouslyGraduated);
					re.setPreviouslyGraduatedPlace(previouslyGraduatedPlace);
					re.setPreviouslyNameOfAuthorDegreeOld(previouslyNameOfAuthorDegreeOld);
					re.setPreviouslyNameOfAuthorDegreeAbbreviationOld(previouslyNameOfAuthorDegreeAbbreviationOld);
					re.setPreviouslyNameOfAuthorDegreeDateOld(previouslyNameOfAuthorDegreeDateOld);
					re.setPreviouslyNameOfAuthorDegreeBologna(previouslyNameOfAuthorDegreeBologna);
					re.setPreviouslyNameOfAuthorDegreeAbbreviationBologna(previouslyNameOfAuthorDegreeAbbreviationBologna);
					re.setPreviouslyNameOfAuthorDegreeYearBologna(previouslyNameOfAuthorDegreeYearBologna);
					
					re.setTitle(title);
					re.setInstitution(institution);
					re.setInstitutionPlace(institutionPlace);
					re.setAdvisors(advisors);
					re.setCommissionMembers(commission);
					re.setDefendedOn(defendedOn);
					re.setMark(mark);
					re.setNameOfAuthorDegree(nameOfAuthorDegree);
					
					re.setPromotionDate(promotionDate);
					re.setDiplomaNumber(diplomaNumber);
					re.setSupplementNumber(supplementNumber);
					re.setDiplomaPublicationDate(diplomaPublicationDate);
					re.setSupplementPublicationDate(supplementPublicationDate);
					
					re.setUser(user);
					retVal.add(re);
					
					dissertation.setNotLoaded(true);
					re.getAuthor().setNotLoaded(true);
				}	
			} 
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.fatal("Cannot read register entries." );
			log.fatal(ex);		
			return null;
		}
	}
	
	
	public List<RegisterEntryDTO> getPromotedForInstitution(Connection conn, String institution1){
		List<RegisterEntryDTO> retVal = new ArrayList<RegisterEntryDTO>();
		try {
			PreparedStatement stmt = conn.prepareStatement("select REGISTERENTRYID, AUTHORNAME, AUTHORLASTNAME, FATHERNAME, FATHERLASTNAME, MOTHERNAME, MOTHERLASTNAME, GUARDIANSFULLNAME, " +
					"STREETANDNUMBER, PLACE, POSTALCODE, CITY, COUNTRY, EMAIL, PHONE, " +
					"BIRTHDATE, BIRTHPLACE, BIRTHCITY, BIRTHCOUNTRY, PREVIOUSLYGRADUATED, PREVIOUSLYGRADUATEDPLACE, PREVIOUSLYNAMEOFAUTHORDEGREEOLD, " +
					"PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONOLD, PREVIOUSLYNAMEOFAUTHORDEGREEDATEOLD, PREVIOUSLYNAMEOFAUTHORDEGREEBOLOGNA, PREVIOUSLYNAMEOFAUTHORDEGREEABBREVIATIONBOLOGNA, " +
					" PREVIOUSLYNAMEOFAUTHORDEGREEYEARBOLOGNA, " +
					"TITLE, INSTITUTION, INSTITUTIONPLACE, ADVISORS, COMMISSIONMEMBERS, DEFENDEDON, MARK, NAMEOFAUTHORDEGREE, " +
					"PROMOTIONDATE, DIPLOMANUMBER, SUPPLEMENTNUMBER, DIPLOMAPUBLICATIONDATE, SUPPLEMENTPUBLICATIONDATE, CREATOR, ACADEMICYEAR, ACADEMICYEARNUMBER, DISSERTATIONID from REGISTERENTRY " +
					"WHERE INSTITUTION= ? ");
			stmt.setString(1, institution1);
			
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {		
				int i=1;
				
				String id = rset.getString(i++);
				
				String authorName = rset.getString(i++);
				String authorLastName = rset.getString(i++);
				String fatherName = rset.getString(i++);
				String fatherLastName = rset.getString(i++);
				String motherName = rset.getString(i++);
				String motherLastName = rset.getString(i++);
				String guardiansFullName = rset.getString(i++);
				
				String streetAndNumber = rset.getString(i++);
				String place = rset.getString(i++);
				String postalCode = rset.getString(i++);
				String city = rset.getString(i++);
				String country = rset.getString(i++);
				String email = rset.getString(i++);
				String phone = rset.getString(i++);
				
				Calendar birthDate = null;
				if (rset.getDate(i++) != null) {
					birthDate = new GregorianCalendar();
					birthDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String birthPlace = rset.getString(i++);
				String birthCity = rset.getString(i++);
				String birthCountry = rset.getString(i++);
				
				String previouslyGraduated = rset.getString(i++);
				String previouslyGraduatedPlace = rset.getString(i++);
				String previouslyNameOfAuthorDegreeOld = rset.getString(i++);
				String previouslyNameOfAuthorDegreeAbbreviationOld = rset.getString(i++);
				Calendar previouslyNameOfAuthorDegreeDateOld = null;
				if (rset.getDate(i++) != null) {
					previouslyNameOfAuthorDegreeDateOld = new GregorianCalendar();
					previouslyNameOfAuthorDegreeDateOld.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String previouslyNameOfAuthorDegreeBologna = rset.getString(i++);
				String previouslyNameOfAuthorDegreeAbbreviationBologna = rset.getString(i++);
				String previouslyNameOfAuthorDegreeYearBologna = rset.getString(i++);
				
				String title = rset.getString(i++);
				String institution = rset.getString(i++);
				String institutionPlace = rset.getString(i++);
				String advisors = rset.getString(i++);
				String commission = rset.getString(i++);
				Calendar defendedOn = null;
				if (rset.getDate(i++) != null) {
					defendedOn = new GregorianCalendar();
					defendedOn.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String mark = rset.getString(i++);
				String nameOfAuthorDegree = rset.getString(i++);
				
				Calendar promotionDate = null;
				if (rset.getDate(i++) != null) {
					promotionDate = new GregorianCalendar();
					promotionDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				String diplomaNumber = rset.getString(i++);
				String supplementNumber = rset.getString(i++);
				Calendar diplomaPublicationDate = null;
				if (rset.getDate(i++) != null) {
					diplomaPublicationDate = new GregorianCalendar();
					diplomaPublicationDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				Calendar supplementPublicationDate = null;
				if (rset.getDate(i++) != null) {
					supplementPublicationDate = new GregorianCalendar();
					supplementPublicationDate.setTimeInMillis(rset.getDate(i-1).getTime());
				}
				
				String user = rset.getString(i++);
				String academicYear = rset.getString(i++);
				int academicYearNumber = rset.getInt(i++);
				if(rset.wasNull())
					academicYearNumber = 0;
				
				
				StudyFinalDocumentDTO dissertation = new StudyFinalDocumentDTO();
				dissertation = (StudyFinalDocumentDTO)(new RecordDAO(new RecordDB()).getRecordFromDatabase(rset.getString(i++)).getDto());
				if(dissertation != null){
					RegisterEntryDTO re = dissertation.getRegisterEntry(); 
					re.setAuthor((AuthorDTO) new RecordDAO(new PersonDB()).getDTO(dissertation.getAuthor().getControlNumber()));
					re.getDissertation().getAcceptedOn(); // load object
					re.getAuthor().getAddress(); // load object
					re.setNotLoaded(false);
					
					re.setId(id);
					re.setAcademicYear(academicYear);
					re.setAcademicYearNumber(academicYearNumber);
					
					re.getAuthorName().setLastname(authorLastName);
					re.getAuthorName().setFirstname(authorName);
					re.getFatherName().setLastname(fatherLastName);
					re.getFatherName().setFirstname(fatherName);
					re.getMotherName().setLastname(motherLastName);
					re.getMotherName().setFirstname(motherName);
					re.setGuardiansName(guardiansFullName);
					
					re.setStreetAndNumber(streetAndNumber);
					re.setPlace(place);
					re.setPostalCode(postalCode);
					re.setCity(city);
					re.setCountry(country);
					re.setEmail(email);
					re.setPhone(phone);
					
					re.setBirthDate(birthDate);
					re.setBirthPlace(birthPlace);
					re.setBirthCity(birthCity);
					re.setBirthCountry(birthCountry);
					
					re.setPreviouslyGraduated(previouslyGraduated);
					re.setPreviouslyGraduatedPlace(previouslyGraduatedPlace);
					re.setPreviouslyNameOfAuthorDegreeOld(previouslyNameOfAuthorDegreeOld);
					re.setPreviouslyNameOfAuthorDegreeAbbreviationOld(previouslyNameOfAuthorDegreeAbbreviationOld);
					re.setPreviouslyNameOfAuthorDegreeDateOld(previouslyNameOfAuthorDegreeDateOld);
					re.setPreviouslyNameOfAuthorDegreeBologna(previouslyNameOfAuthorDegreeBologna);
					re.setPreviouslyNameOfAuthorDegreeAbbreviationBologna(previouslyNameOfAuthorDegreeAbbreviationBologna);
					re.setPreviouslyNameOfAuthorDegreeYearBologna(previouslyNameOfAuthorDegreeYearBologna);
					
					re.setTitle(title);
					re.setInstitution(institution);
					re.setInstitutionPlace(institutionPlace);
					re.setAdvisors(advisors);
					re.setCommissionMembers(commission);
					re.setDefendedOn(defendedOn);
					re.setMark(mark);
					re.setNameOfAuthorDegree(nameOfAuthorDegree);
					
					re.setPromotionDate(promotionDate);
					re.setDiplomaNumber(diplomaNumber);
					re.setSupplementNumber(supplementNumber);
					re.setDiplomaPublicationDate(diplomaPublicationDate);
					re.setSupplementPublicationDate(supplementPublicationDate);
					
					re.setUser(user);
					retVal.add(re);
					
					dissertation.setNotLoaded(true);
					re.getAuthor().setNotLoaded(true);
				}	
			} 
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.fatal("Cannot read register entries." );
			log.fatal(ex);		
			return null;
		}
	}
	
	
	public List<String> getDistinctInstitutions(Connection conn, String universityId){
		List<String> retVal = new ArrayList<String>();
		try{
			PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT INSTITUTION FROM REGISTERENTRY WHERE UNIVERSITYID like ?");
			stmt.setString(1, universityId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				retVal.add(rs.getString(1));
			}			
		}catch(Exception ex){
			ex.printStackTrace();
			log.fatal("Cannot read distinct institutions." );
			log.fatal(ex);		
			
		}
		return retVal;			
		
		
	}
	/**
	 * 
	 * @return vraca listu entry-ja koji su na redu za generisanje maticnih brojeva,
	 * to su svi koji imaju najraniji datum promocije bez broja
	 */
	public List<RegisterEntryDTO> getRegisterEntriesForIdGeneration(Connection conn, String universityId){
		List<RegisterEntryDTO> retVal = new ArrayList<RegisterEntryDTO>();
		try{
			PreparedStatement stmt = conn.prepareStatement("SELECT DISSERTATIONID FROM REGISTERENTRY  " +
						"where PROMOTIONDATE is not null and UNIVERSITYID like ? "+ 
						"and  (REGISTERENTRYID like '-1' or REGISTERENTRYID like '-2') "+ 
						"and PROMOTIONDATE is not null and PROMOTIONDATE <= (SELECT MIN(PROMOTIONDATE) FROM REGISTERENTRY " +
						"WHERE UNIVERSITYID like ? and PROMOTIONDATE is not null and  (REGISTERENTRYID like '-1' or REGISTERENTRYID like '-2')) ");
			stmt.setString(1, universityId);
			stmt.setString(2, universityId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				retVal.add(getRegisterEntry(conn, rs.getString(1), false));
			}			
		}catch(Exception ex){
			ex.printStackTrace();
			log.fatal("Cannot read entries for generating id");
			log.fatal(ex);
		}
		return retVal;
	}
	
	public int getNextAvailableId(Connection conn,String universityId){
		int retVal = 0;
		try{
			PreparedStatement stmt = conn.prepareStatement("SELECT max(cast(REGISTERENTRYID as signed)) " +
					"FROM REGISTERENTRY where  UNIVERSITYID like ?");
			stmt.setString(1, universityId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				retVal = rs.getInt(1)+1;
			}
		}catch(Exception ex){
			log.fatal(ex);
		}
		return retVal;
		
	}
	
	public int getNextAvailableAcademicYearNumber(Connection conn,String universityId, String academicYear){
		int retVal = 0;
		try{
			PreparedStatement stmt = conn.prepareStatement("SELECT max(cast(ACADEMICYEARNUMBER as signed)) " +
					"FROM REGISTERENTRY where  UNIVERSITYID like ? and ACADEMICYEAR like ? ");
			stmt.setString(1, universityId);
			stmt.setString(2, academicYear);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				retVal = rs.getInt(1)+1;
			}
		}catch(Exception ex){
			log.fatal(ex);
		}
		return retVal;
		
	}
	


	private static Log log = LogFactory.getLog(RecordDB.class.getName());

}
