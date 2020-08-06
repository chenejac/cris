 package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;

/**
 * DTO class which presents register entry related to dissertations.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class RegisterEntryDTO implements Serializable{

	private String id;
	private String universityId;
	private String academicYear;
	private int academicYearNumber;
	private AuthorNameDTO authorName;
	private AuthorNameDTO fatherName;
	private AuthorNameDTO motherName;
	private String guardiansName;
	private String streetAndNumber;
	private String place;
	private String postalCode;
	private String city;
	private String country;
	private String email;
	private String phone;
	private Calendar birthDate;
	private String birthPlace;
	private String birthCity;
	private String birthCountry;
	private String previouslyGraduated;
	private String previouslyGraduatedPlace;
	private String previouslyNameOfAuthorDegreeOld;
	private String previouslyNameOfAuthorDegreeAbbreviationOld;
	private Calendar previouslyNameOfAuthorDegreeDateOld;
	private String previouslyNameOfAuthorDegreeBologna;
	private String previouslyNameOfAuthorDegreeAbbreviationBologna;
	private String previouslyNameOfAuthorDegreeYearBologna;
	private String title;
	private String institution;
	private String institutionPlace;
	private String advisors;
	private String commissionMembers;
	private Calendar defendedOn;
	private String mark;
	private String nameOfAuthorDegree;
	private Calendar promotionDate;
	private Date promotionDateDate;
	private String diplomaNumber;
	private String supplementNumber;
	private Calendar diplomaPublicationDate;
	private Date diplomaPublicationDateDate;
	private Calendar supplementPublicationDate;
	
	private StudyFinalDocumentDTO dissertation;
	private boolean authorChanged;
	private AuthorDTO author;
	private String user;
	
	private boolean notLoaded = false;
	private String stringRepresentation="";
	private String HTMLRepresentation="";
	private String HTMLRepresentationEn="";
	
	private String futurePromotionName = null;
	
	/**
	 * 
	 */
	public RegisterEntryDTO(StudyFinalDocumentDTO dissertation, AuthorDTO author) {
		super();
		this.dissertation = dissertation;
		this.author = author;
		authorName = new AuthorNameDTO();
		fatherName = new AuthorNameDTO();
		motherName = new AuthorNameDTO();		
		academicYearNumber = 0;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the academicYear
	 */
	public String getAcademicYear() {
		return academicYear;
	}

	/**
	 * @param academicYear the academicYear to set
	 */
	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	/**
	 * @return the academicYearNumber
	 */
	public int getAcademicYearNumber() {
		return academicYearNumber;
	}

	/**
	 * @param academicYearNumber the academicYearNumber to set
	 */
	public void setAcademicYearNumber(int academicYearNumber) {
		this.academicYearNumber = academicYearNumber;
	}

	/**
	 * @return the authorName
	 */
	public AuthorNameDTO getAuthorName() {
		return authorName;
	}

	/**
	 * @param authorName the authorName to set
	 */
	public void setAuthorName(AuthorNameDTO authorName) {
		this.authorName = authorName;
	}

	/**
	 * @return the fatherName
	 */
	public AuthorNameDTO getFatherName() {
		return fatherName;
	}

	/**
	 * @param fatherName the fatherName to set
	 */
	public void setFatherName(AuthorNameDTO fatherName) {
		this.fatherName = fatherName;
	}

	/**
	 * @return the motherName
	 */
	public AuthorNameDTO getMotherName() {
		return motherName;
	}

	/**
	 * @param motherName the motherName to set
	 */
	public void setMotherName(AuthorNameDTO motherName) {
		this.motherName = motherName;
		
	}

	/**
	 * @return the guardiansName
	 */
	public String getGuardiansName() {
		return guardiansName;
	}

	/**
	 * @param guardiansName the guardiansName to set
	 */
	public void setGuardiansName(String guardiansName) {
		this.guardiansName = guardiansName;
	}

	/**
	 * @return the streetAndNumber
	 */
	public String getStreetAndNumber() {
		return streetAndNumber;
	}

	/**
	 * @param streetAndNumber the streetAndNumber to set
	 */
	public void setStreetAndNumber(String streetAndNumber) {
		this.streetAndNumber = streetAndNumber;
	}

	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the birthDate
	 */
	public Calendar getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Calendar birthDate) {
		this.birthDate = birthDate;
		if((author != null) && ((author.getDateOfBirth() != null) && (birthDate == null)) || ((author.getDateOfBirth() == null) && (birthDate != null)) || ((author.getDateOfBirth() != null) && (birthDate != null) && (! birthDate.equals(author.getDateOfBirth())))){
			authorChanged = true;
			author.setDateOfBirth(birthDate);
		}
	}

	/**
	 * @return the birthPlace
	 */
	public String getBirthPlace() {
		return birthPlace;
	}

	/**
	 * @param birthPlace the birthPlace to set
	 */
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
		if((author != null) && ((author.getPlaceOfBirth() != null) && (birthPlace == null)) || ((author.getPlaceOfBirth() == null) && (birthPlace != null)) || ((author.getPlaceOfBirth() != null) && (birthPlace != null) && (! birthPlace.equals(author.getPlaceOfBirth())))){
			authorChanged = true;
			author.setPlaceOfBirth(birthPlace);
		}
	}

	/**
	 * @return the birthCity
	 */
	public String getBirthCity() {
		return birthCity;
	}

	/**
	 * @param birthCity the birthCity to set
	 */
	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
		if((author != null) && ((author.getCity() != null) && (birthCity == null)) || ((author.getCity() == null) && (birthCity != null)) || ((author.getCity() != null) && (birthCity != null) && (! birthCity.equals(author.getCity())))){
			authorChanged = true;
			author.setCity(birthCity);
		}
	}

	/**
	 * @return the birthCountry
	 */
	public String getBirthCountry() {
		return birthCountry;
	}

	/**
	 * @param birthCountry the birthCountry to set
	 */
	public void setBirthCountry(String birthCountry) {
		this.birthCountry = birthCountry;
		if((author != null) && ((author.getState() != null) && (birthCountry == null)) || ((author.getState() == null) && (birthCountry != null)) || ((author.getState() != null) && (birthCountry != null) && (! birthCountry.equals(author.getState())))){
			authorChanged = true;
			author.setState(birthCountry);
		}
	}

	/**
	 * @return the previouslyGraduated
	 */
	public String getPreviouslyGraduated() {
		return previouslyGraduated;
	}

	/**
	 * @param previouslyGraduated the previouslyGraduated to set
	 */
	public void setPreviouslyGraduated(String previouslyGraduated) {
		this.previouslyGraduated = previouslyGraduated;
	}
	
	/**
	 * @return the previouslyGraduatedPlace
	 */
	public String getPreviouslyGraduatedPlace() {
		return previouslyGraduatedPlace;
	}

	/**
	 * @param previouslyGraduatedPlace the previouslyGraduatedPlace to set
	 */
	public void setPreviouslyGraduatedPlace(String previouslyGraduatedPlace) {
		this.previouslyGraduatedPlace = previouslyGraduatedPlace;
	}

	/**
	 * @return the previouslyNameOfAuthorDegreeOld
	 */
	public String getPreviouslyNameOfAuthorDegreeOld() {
		return previouslyNameOfAuthorDegreeOld;
	}

	/**
	 * @param previouslyNameOfAuthorDegreeOld the previouslyNameOfAuthorDegreeOld to set
	 */
	public void setPreviouslyNameOfAuthorDegreeOld(
			String previouslyNameOfAuthorDegreeOld) {
		this.previouslyNameOfAuthorDegreeOld = previouslyNameOfAuthorDegreeOld; 
	}

	/**
	 * @return the previouslyNameOfAuthorDegreeAbbreviationOld
	 */
	public String getPreviouslyNameOfAuthorDegreeAbbreviationOld() {
		return previouslyNameOfAuthorDegreeAbbreviationOld;
	}

	/**
	 * @param previouslyNameOfAuthorDegreeAbbreviationOld the previouslyNameOfAuthorDegreeAbbreviationOld to set
	 */
	public void setPreviouslyNameOfAuthorDegreeAbbreviationOld(
			String previouslyNameOfAuthorDegreeAbbreviationOld) {
		if(previouslyNameOfAuthorDegreeAbbreviationOld == null){
			this.previouslyNameOfAuthorDegreeAbbreviationOld = previouslyNameOfAuthorDegreeAbbreviationOld;
			this.previouslyNameOfAuthorDegreeOld = null;
		} else if(previouslyNameOfAuthorDegreeAbbreviationOld.equals("мр")){
			this.previouslyNameOfAuthorDegreeOld = "Магистарске студије";
			this.previouslyNameOfAuthorDegreeAbbreviationOld = "мр";
		}
	}

	/**
	 * @return the previouslyNameOfAuthorDegreeDateOld
	 */
	public Calendar getPreviouslyNameOfAuthorDegreeDateOld() {
		return previouslyNameOfAuthorDegreeDateOld;
	}

	/**
	 * @param previouslyNameOfAuthorDegreeDateOld the previouslyNameOfAuthorDegreeDateOld to set
	 */
	public void setPreviouslyNameOfAuthorDegreeDateOld(
			Calendar previouslyNameOfAuthorDegreeDateOld) {
		this.previouslyNameOfAuthorDegreeDateOld = previouslyNameOfAuthorDegreeDateOld;
	}

	/**
	 * @return the previouslyNameOfAuthorDegreeBologna
	 */
	public String getPreviouslyNameOfAuthorDegreeBologna() {
		return previouslyNameOfAuthorDegreeBologna;
	}

	/**
	 * @param previouslyNameOfAuthorDegreeBologna the previouslyNameOfAuthorDegreeBologna to set
	 */
	public void setPreviouslyNameOfAuthorDegreeBologna(
			String previouslyNameOfAuthorDegreeBologna) {
		this.previouslyNameOfAuthorDegreeBologna = previouslyNameOfAuthorDegreeBologna; 
	}

	/**
	 * @return the previouslyNameOfAuthorDegreeAbbreviationBologna
	 */
	public String getPreviouslyNameOfAuthorDegreeAbbreviationBologna() {
		return previouslyNameOfAuthorDegreeAbbreviationBologna;
	}

	/**
	 * @param previouslyNameOfAuthorDegreeAbbreviationBologna the previouslyNameOfAuthorDegreeAbbreviationBologna to set
	 */
	public void setPreviouslyNameOfAuthorDegreeAbbreviationBologna(
			String previouslyNameOfAuthorDegreeAbbreviationBologna) {
		if(previouslyNameOfAuthorDegreeAbbreviationBologna == null){
			this.previouslyNameOfAuthorDegreeAbbreviationBologna = previouslyNameOfAuthorDegreeAbbreviationBologna;
			this.previouslyNameOfAuthorDegreeBologna = null;
		} else if(previouslyNameOfAuthorDegreeAbbreviationBologna.equals("ОАС")){
			this.previouslyNameOfAuthorDegreeBologna = "Основне академске студије";
			this.previouslyNameOfAuthorDegreeAbbreviationBologna = "ОАС";
		} else if(previouslyNameOfAuthorDegreeAbbreviationBologna.equals("МАС")){
			this.previouslyNameOfAuthorDegreeBologna = "Мастер академске студије";
			this.previouslyNameOfAuthorDegreeAbbreviationBologna = "МАС";
		} else if(previouslyNameOfAuthorDegreeAbbreviationBologna.equals("ОАС-МАС")){
			this.previouslyNameOfAuthorDegreeBologna = "Интегрисане академске студије";
			this.previouslyNameOfAuthorDegreeAbbreviationBologna = "ОАС-МАС";
		} else if(previouslyNameOfAuthorDegreeAbbreviationBologna.equals("САС")){
			this.previouslyNameOfAuthorDegreeBologna = "Специјалистичке академске студије";
			this.previouslyNameOfAuthorDegreeAbbreviationBologna = "САС";
		}
	}

	/**
	 * @return the previouslyNameOfAuthorDegreeYearBologna
	 */
	public String getPreviouslyNameOfAuthorDegreeYearBologna() {
		return previouslyNameOfAuthorDegreeYearBologna;
	}

	/**
	 * @param previouslyNameOfAuthorDegreeYearBologna the previouslyNameOfAuthorDegreeYearBologna to set
	 */
	public void setPreviouslyNameOfAuthorDegreeYearBologna(
			String previouslyNameOfAuthorDegreeYearBologna) {
		this.previouslyNameOfAuthorDegreeYearBologna = previouslyNameOfAuthorDegreeYearBologna;
	}

	/**
	 * @return the institutionPlace
	 */
	public String getInstitutionPlace() {
		return institutionPlace;
	}

	/**
	 * @param institutionPlace the institutionPlace to set
	 */
	public void setInstitutionPlace(String institutionPlace) {
		this.institutionPlace = institutionPlace;
	}

	/**
	 * @return the mark
	 */
	public String getMark() {
		return mark;
	}

	/**
	 * @param mark the mark to set
	 */
	public void setMark(String mark) {
		this.mark = mark;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the institution
	 */
	public String getInstitution() {
		return institution;
	}

	/**
	 * @param institution the institution to set
	 */
	public void setInstitution(String institution) {
		this.institution = institution;
	}

	/**
	 * @return the advisors
	 */
	public String getAdvisors() {
		return advisors;
	}

	/**
	 * @param advisors the advisors to set
	 */
	public void setAdvisors(String advisors) {
		this.advisors = advisors;
	}

	/**
	 * @return the commissionMembers
	 */
	public String getCommissionMembers() {
		return commissionMembers;
	}

	/**
	 * @param commissionMembers the commissionMembers to set
	 */
	public void setCommissionMembers(String commissionMembers) {
		this.commissionMembers = commissionMembers;
	}

	/**
	 * @return the defendedOn
	 */
	public Calendar getDefendedOn() {
		return defendedOn;
	}

	/**
	 * @param defendedOn the defendedOn to set
	 */
	public void setDefendedOn(Calendar defendedOn) {
		this.defendedOn = defendedOn;
		if(defendedOn != null)
			dissertation.setDefendedOn(defendedOn);
	}
	
	/**
	 * @return the publication year
	 */
	public String getPublicationYear() {
		return dissertation.getPublicationYear();
	}

	/**
	 * @return the nameOfAuthorDegree
	 */
	public String getNameOfAuthorDegree() {
		return nameOfAuthorDegree;
	}

	/**
	 * @param nameOfAuthorDegree the nameOfAuthorDegree to set
	 */
	public void setNameOfAuthorDegree(String nameOfAuthorDegree) {
		this.nameOfAuthorDegree = nameOfAuthorDegree;
	}

	/**
	 * @return the promotionDate
	 */
	public Calendar getPromotionDate() {
		return promotionDate;
	}

	/**
	 * @param promotionDate the promotionDate to set
	 */
	public void setPromotionDate(Calendar promotionDate) {
		this.promotionDate = promotionDate;
		if(promotionDate!=null){
			this.promotionDateDate = promotionDate.getTime();
		}
	}

	/**
	 * @return the diplomaNumber
	 */
	public String getDiplomaNumber() {
		return diplomaNumber;
	}

	/**
	 * @param diplomaNumber the diplomaNumber to set
	 */
	public void setDiplomaNumber(String diplomaNumber) {
		this.diplomaNumber = diplomaNumber;
	}
	
	/**
	 * @return the supplementNumber
	 */
	public String getSupplementNumber() {
		return supplementNumber;
	}

	/**
	 * @param supplementNumber the supplementNumber to set
	 */
	public void setSupplementNumber(String supplementNumber) {
		this.supplementNumber = supplementNumber;
	}

	/**
	 * @return the diplomaPublicationDate
	 */
	public Calendar getDiplomaPublicationDate() {		
		return diplomaPublicationDate;
	}

	/**
	 * @param diplomaPublicationDate the diplomaPublicationDate to set
	 */
	public void setDiplomaPublicationDate(Calendar diplomaPublicationDate) {		
		this.diplomaPublicationDate = diplomaPublicationDate;
		if(diplomaPublicationDate!=null)
			this.diplomaPublicationDateDate = diplomaPublicationDate.getTime();
		else
			this.diplomaPublicationDateDate = null;
	}

	/**
	 * @return the supplementPublicationDate
	 */
	public Calendar getSupplementPublicationDate() {
		return supplementPublicationDate;
	}

	/**
	 * @param supplementPublicationDate the supplementPublicationDate to set
	 */
	public void setSupplementPublicationDate(Calendar supplementPublicationDate) {
		this.supplementPublicationDate = supplementPublicationDate;
	}

	/**
	 * @return the dissertation
	 */
	public StudyFinalDocumentDTO getDissertation() {
		return dissertation;
	}

	/**
	 * @param dissertation the dissertation to set
	 */
	public void setDissertation(StudyFinalDocumentDTO dissertation) {
		this.dissertation = dissertation;
	}
	
	/**
	 * @return the author
	 */
	public AuthorDTO getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(AuthorDTO author) {
		this.author = author;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the notLoaded
	 */
	public boolean isNotLoaded() {
		return notLoaded;
	}

	/**
	 * @param notLoaded the notLoaded to set
	 */
	public void setNotLoaded(boolean notLoaded) {
		this.notLoaded = notLoaded;
	}

	/**
	 * @return the authorChanged
	 */
	public boolean isAuthorChanged() {
		return authorChanged;
	}

	/**
	 * @param authorChanged the authorChanged to set
	 */
	public void setAuthorChanged(boolean authorChanged) {
		this.authorChanged = authorChanged;
	}
	
	
	
	/**
	 * @return the promotionDateDate
	 */
	public Date getPromotionDateDate() {
		return promotionDateDate;
	}

	/**
	 * @param promotionDateDate the promotionDateDate to set
	 */
	public void setPromotionDateDate(Date promotionDateDate) {
		this.promotionDateDate = promotionDateDate;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(notLoaded){
			return stringRepresentation;
		}
		else {
			StringBuffer retVal = new StringBuffer();
			if ((authorName != null) && (authorName.toString() != null))
				retVal.append(authorName.toString() + ", ");
			else
				retVal.append("mainAuthor, ");
			if ((title != null) && (! title.trim().equals("")))
				retVal.append(title + ", ");
			else 
				retVal.append("Title, ");
			if(institution != null) {
				retVal.append(institution + ", "); 
			}
			if (getPublicationYear() != null) 
				retVal.append(getPublicationYear());
			else
				retVal.append("publicationYear");
			return retVal.toString();
		}
	}

	/**
	 * return stringRepresentation
	 */
	public String getStringRepresentation() {
		return this.toString();
	}

	/**
	 * @param stringRepresentation the stringRepresentation to set
	 */
	public void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

	/**
	 * @return the hTMLRepresentation
	 */
	public String getHTMLRepresentation() {
		if((notLoaded) && dissertation.getLocale().getLanguage().equals("sr"))
			return HTMLRepresentation;
		else {
			if(notLoaded)
				dissertation.loadRegisterEntry(false);
			FacesMessages fm= new FacesMessages("messages.messages-records", dissertation.getLocale()); 
			StringBuffer retVal = new StringBuffer();
			retVal.append(dissertation.getSomeHarvardRepresentation());
			if (authorName != null){
				retVal.append("<br/><b>" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.authorData.header")+ ":</b>");
				if((authorName.getFirstname() != null) && (! authorName.getFirstname().trim().equals(""))){
					retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.authorData.firstname") + ": " + authorName.getFirstname());
				}
				if((authorName.getLastname() != null) && (! authorName.getLastname().trim().equals(""))){
					retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.authorData.lastname") + ": " + authorName.getLastname());
				}
				if((fatherName.getFirstname() != null) && (! fatherName.getFirstname().trim().equals(""))){
					retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.authorData.fatherFirstname") + ": " + fatherName.getFirstname());
				}
				/*if(birthDate != null){
					String dateString = null;
					if(birthDate.get(Calendar.DAY_OF_YEAR) == 1){
						dateString = "" + birthDate.get(Calendar.YEAR);
						retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.authorData.birthYear") + ": " + dateString);
					} else {
						DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
						Date date = birthDate.getTime();
						if(date != null){
							if(birthDate.get(Calendar.DAY_OF_YEAR) == 1)
								dateString = "" + birthDate.get(Calendar.YEAR);
							else 	
								dateString = formatter.format(date);
							dateString = formatter.format(date);
							retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.authorData.birthDate") + ": " + dateString);
						}
					}
				}
				if((birthPlace != null) && (! birthPlace.trim().equals(""))){
					retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.authorData.birthPlace") + ": " + birthPlace);
				}
				if((birthCity != null) && (! birthCity.trim().equals(""))){
					retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.authorData.birthCity") + ": " + birthCity);
				}
				if((birthCountry != null) && (! birthCountry.trim().equals(""))){
					retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.authorData.birthCountry") + ": " + birthCountry);
				}
				*/
				if((previouslyNameOfAuthorDegreeOld != null) && (! previouslyNameOfAuthorDegreeOld.trim().equals(""))){
					retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.authorData.previouslyNameOfAuthorDegreeOld") + ": " + previouslyNameOfAuthorDegreeOld);
					if(previouslyNameOfAuthorDegreeDateOld != null){
						DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
						Date date = previouslyNameOfAuthorDegreeDateOld.getTime();
						String dateString = null;
						if(date != null){
							dateString = formatter.format(date);
							retVal.append(", " + dateString);
						}
					}
				} else if((previouslyNameOfAuthorDegreeBologna != null) && (! previouslyNameOfAuthorDegreeBologna.trim().equals(""))){
					retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.authorData.previouslyNameOfAuthorDegreeOld") + ": " + previouslyNameOfAuthorDegreeBologna);
					if(previouslyNameOfAuthorDegreeYearBologna != null){
						retVal.append(", " + previouslyNameOfAuthorDegreeYearBologna);
					}
				}
				if((previouslyGraduated != null) && (! previouslyGraduated.trim().equals(""))){
					retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.authorData.previouslyGraduated") + ": " + previouslyGraduated + (((previouslyGraduatedPlace != null) && (! previouslyGraduatedPlace.trim().equals("")))?(", " + previouslyGraduatedPlace):("")));
				}
			}
			retVal.append("<br/><b>" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.dissertationData.header")+ ":</b>");
			if((nameOfAuthorDegree != null) && (! nameOfAuthorDegree.trim().equals(""))){
				retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.dissertationData.nameOfAuthorDegree") + ": " + nameOfAuthorDegree);
			}
			if((defendedOn != null) && (defendedOn.get(Calendar.DAY_OF_YEAR) != 1)){
				String dateString = null;
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date date = defendedOn.getTime();
				if(date != null){
					if(defendedOn.get(Calendar.DAY_OF_YEAR) != 1){ 	
						dateString = formatter.format(date);
						retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.dissertationData.defendedOn") + ": " + dateString);
					}
				}
			}
			if((promotionDate != null) && (promotionDate.get(Calendar.DAY_OF_YEAR)!=1)){
				String dateString = null;
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date date = promotionDate.getTime();
				if(date != null){
					dateString = formatter.format(date);
					retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.dissertationData.promotionDate") + ": " + dateString);
				}
			}
			if((advisors != null) && (! advisors.trim().equals(""))){
				int n = fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.dissertationData.advisors").length();
				StringBuffer nbsp = new StringBuffer("");
				for (int i=0; i < (n/2+2); i++) {
					nbsp.append("&emsp;");
				}
				retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.dissertationData.advisors") + ":&nbsp;&nbsp;" + advisors.replace("\n", "<br/>" + nbsp.toString()));
			}
			
			
			
			if((commissionMembers != null) && (! commissionMembers.trim().equals(""))){
				int n = fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.dissertationData.commissionMembers").length();
				StringBuffer nbsp = new StringBuffer("");
				for (int i=0; i < (n/2+2); i++) {
					nbsp.append("&emsp;");
				}
				retVal.append("<br/>&nbsp;&nbsp;&nbsp;" + fm.getMessageFromResourceBundle("records.studyFinalDocument.registerEntry.dissertationData.commissionMembers") + ":&nbsp;&nbsp;" + commissionMembers.replace("\n", "<br/>" + nbsp.toString()));
			}
			return retVal.toString();
		}
	}
	
	
	
	/**
	 * @param hTMLRepresentation the hTMLRepresentation to set
	 */
	public void setHTMLRepresentation(String hTMLRepresentation) {
		HTMLRepresentation = hTMLRepresentation;
	}
	
	public String getHTMLRepresentationEn(){
		if(notLoaded){
			return HTMLRepresentationEn;
		} 
		else {
			Locale oldLocale = dissertation.getLocale();
			Locale enLocale = new Locale("en");
			dissertation.setLocale(enLocale);
			HTMLRepresentationEn = getHTMLRepresentation();
			dissertation.setLocale(oldLocale);
			return HTMLRepresentationEn;
		}
	}
	
	/**
	 * @param hTMLRepresentationEn the hTMLRepresentationEn to set
	 */
	public void setHTMLRepresentationEn(String hTMLRepresentationEn) {
		HTMLRepresentationEn = hTMLRepresentationEn;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getSomeHarvardRepresentation()
	 */
	public String getSomeHTMLRepresentation() {
		if(dissertation.getLocale().getLanguage().equals("en")){
			return getHTMLRepresentationEn();
		} else {
			return getHTMLRepresentation();
		}
	}
	
	public void setSelectedForPromotion(boolean selected){
		if (selected)
			this.id = "-1";
		else 
			this.id= "-2";
	
	}
	
	public void unselectForPromotion(){
		if(this.id!=null && this.id.equals("-1")){
			this.id= "-2";
			futurePromotionName = null;
		}
	
	}
	
	public boolean getSelectedForPromotion(){
		return this.id !=null && this.id.equals("-1");
	}

	/**
	 * @return the futurePromotionName
	 */
	public String getFuturePromotionName() {
		return futurePromotionName;
	}

	/**
	 * @param futurePromotionName the futurePromotionName to set
	 */
	public void setFuturePromotionName(String futurePromotionName) {
		this.futurePromotionName = futurePromotionName;
	}
	
	public boolean isOldProgram(){		
		return previouslyNameOfAuthorDegreeOld!=null && previouslyNameOfAuthorDegreeOld.equals("Магистарске студије");
	}

	public Date getDiplomaPublicationDateDate() {		
		if(diplomaPublicationDate==null)
			diplomaPublicationDateDate = null;
		else
			diplomaPublicationDateDate = diplomaPublicationDate.getTime();		
		return diplomaPublicationDateDate;
	}

	public void setDiplomaPublicationDateDate(Date diplomaPublicationDateDate) {
		if(diplomaPublicationDateDate!=null){			
			Calendar date = new GregorianCalendar();		
			date.setTime(diplomaPublicationDateDate);
			this.setDiplomaPublicationDate(date);
		}else{
			this.setDiplomaPublicationDate(null);
			this.diplomaPublicationDateDate = diplomaPublicationDateDate;
		}
	}

	public String getUniversityId() {
		return universityId;
	}

	public void setUniversityId(String universityId) {
		this.universityId = universityId;
	}
	
	

	
	

}
