package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;
import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;

/**
 * DTO class which presents authority mARC21Record with data about author.
 *  
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class AuthorDTO extends RecordDTO {

	private AuthorNameDTO name; 
	private String names = "";
	private List<AuthorNameDTO> otherFormatNames; 
	private Calendar dateOfBirth; 
	private String placeOfBirth; 
	private String state; 
	private String address; 
	private String city; 
	private TitleInstitution titleInstitution;
	private InstitutionDTO institution; 
	private String institutionName = "";
	private OrganizationUnitDTO organizationUnit;
	private AuthorPosition currentPosition; 
	private String currentPositionName = ""; 
	private List<AuthorPosition> formerPositions; 
	
	
	private char sex = 'n';  
	private MultilingualContentDTO biography; 
	private List<MultilingualContentDTO> biographyTranslations; 
	private MultilingualContentDTO keywords; 
	private List<MultilingualContentDTO> keywordsTranslations; 
	private String uri; 
	
	private String email; 
	private String jmbg;
	private String directPhones;
	private String localPhones;
	private String apvnt;
	private boolean alreadyRegistered;
	private boolean authorUnsDissertations;
	private boolean advisorUnsDissertations;
	private boolean commissionMemberUnsDissertations;
	private boolean commissionChairUnsDissertations;
	
	private boolean authorPaDissertations;
	private boolean advisorPaDissertations;
	private boolean commissionMemberPaDissertations;
	private boolean commissionChairPaDissertations;
	
	private List<ResearchAreaDTO> researchAreas;
	
	private List<JobAdDTO> jobApplications;
	
	public AuthorDTO() {
		super();
		otherFormatNames = new ArrayList<AuthorNameDTO>();
		name = new AuthorNameDTO();
		institution = new InstitutionDTO();
		organizationUnit = new OrganizationUnitDTO();
		currentPosition = new AuthorPosition();
		currentPosition.setStartDate(new GregorianCalendar());
		formerPositions = new ArrayList<AuthorPosition>();
		biographyTranslations = new ArrayList<MultilingualContentDTO>();
		biography = new MultilingualContentDTO(null, MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		researchAreas = new ArrayList<ResearchAreaDTO>();
		record = new Person();
		titleInstitution = new TitleInstitution();
		jobApplications = new ArrayList<JobAdDTO>();
	}

	/**
	 * @param controlNumber
	 *            Control number of the authority mARC21Record
	 */
	public AuthorDTO(String controlNumber) {
		super(controlNumber);
		otherFormatNames = new ArrayList<AuthorNameDTO>();
		name = new AuthorNameDTO();
		institution = new InstitutionDTO();
		organizationUnit = new OrganizationUnitDTO();
		currentPosition = new AuthorPosition();
		currentPosition.setStartDate(new GregorianCalendar());
		formerPositions = new ArrayList<AuthorPosition>();
		biographyTranslations = new ArrayList<MultilingualContentDTO>();
		biography = new MultilingualContentDTO(null, MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		researchAreas = new ArrayList<ResearchAreaDTO>();
		record = new Person();
		titleInstitution = new TitleInstitution();
		jobApplications = new ArrayList<JobAdDTO>();
	}
	
	
	/**
	 * @param controlNumber
	 * @param name
	 * @param otherFormatNames
	 * @param dateOfBirth
	 * @param institution
	 * @param organizationUnit
	 * @param title
	 * @param titleTranslations
	 * @param position
	 * @param positionTranslations
	 * @param alreadyRegistered
	 * @param authorUnsDissertations
	 * @param advisorUnsDissertations
	 * @param commissionMemberUnsDissertations
	 * @param commissionChairUnsDissertations
	 * @param authorPaDissertations
	 * @param advisorPaDissertations
	 * @param commissionMemberPaDissertations
	 * @param commissionChairPaDissertations
	 * @param sex
	 * @param resInt
	 * @param resIntTranslations
	 * @param keywords
	 * @param keywordsTranslations
	 * @param uri
	 * @param jmbg
	 * @param placeOfBirth
	 * @param state
	 * @param email
	 * @param localPhones
	 * @param directPhones
	 * @param scienceArea
	 */
	public AuthorDTO(String controlNumber, AuthorNameDTO name,
			List<AuthorNameDTO> otherFormatNames, Calendar dateOfBirth,
			InstitutionDTO institution, OrganizationUnitDTO organizationUnit, String title, AuthorPosition currentPosition, List<AuthorPosition> formerPositions, 
			boolean alreadyRegistered, boolean authorUnsDissertations, boolean advisorUnsDissertations, boolean commissionMemberUnsDissertations, boolean commissionChairUnsDissertations, 
			boolean authorPaDissertations, boolean advisorPaDissertations, boolean commissionMemberPaDissertations, boolean commissionChairPaDissertations,
			char sex, List<ResearchAreaDTO> researchAreas, List<JobAdDTO> jobApplications, MultilingualContentDTO biography, List<MultilingualContentDTO> biographyTranslations,
			MultilingualContentDTO keywords, List<MultilingualContentDTO> keywordsTranslations, String uri, String jmbg, String placeOfBirth,
			String state, String address, String city, TitleInstitution titleInstitution, String email, String localPhones, String directPhones, String apvnt) {
		super(controlNumber);
		this.name = name;
		this.otherFormatNames = otherFormatNames;
		this.dateOfBirth = dateOfBirth;
		this.titleInstitution = titleInstitution;
		this.institution = institution;
		this.organizationUnit = organizationUnit;
		this.currentPosition = currentPosition;
		this.formerPositions = formerPositions;
		this.alreadyRegistered = alreadyRegistered;
		this.authorUnsDissertations = authorUnsDissertations;
		this.advisorUnsDissertations = advisorUnsDissertations;
		this.commissionMemberUnsDissertations = commissionMemberUnsDissertations;
		this.commissionChairUnsDissertations = commissionChairUnsDissertations;
		this.authorPaDissertations = authorPaDissertations;
		this.advisorPaDissertations = advisorPaDissertations;
		this.commissionMemberPaDissertations = commissionMemberPaDissertations;
		this.commissionChairPaDissertations = commissionChairPaDissertations;
		this.sex = sex;
		this.researchAreas = researchAreas;
		this.jobApplications = jobApplications;
		this.biography = biography;
		this.biographyTranslations = biographyTranslations;
		this.keywords = keywords;
		this.keywordsTranslations = keywordsTranslations;
		this.uri = uri;
		this.jmbg = jmbg;
		this.placeOfBirth = placeOfBirth;
		this.state = state;
		this.address = address;
		this.city = city;
		this.email = email;
		this.localPhones = localPhones;
		this.directPhones = directPhones;
		this.apvnt= apvnt;
		this.record = new Person();
	}

	/**
	 * @return the name
	 */
	public AuthorNameDTO getName() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(AuthorNameDTO name) {
		name = (name == null) ? new AuthorNameDTO() : name;
		AuthorNameDTO removeDTO = null;
		for (AuthorNameDTO temp : otherFormatNames) {
			if (temp.equals(name)) {
				removeDTO = temp;
				break;
			}
		}
		if (removeDTO != null) {
			int index = otherFormatNames.indexOf(removeDTO);
			if (!("".equals(this.name.getLastname()))) {
				otherFormatNames.set(index, this.name);
			} else {
				otherFormatNames.remove(index);
			}
		}
		if ((this.name != null) && (this.name.getOtherName() != null) && (! "".equals(this.name.getOtherName().trim()))){
			name.setOtherName(this.name.getOtherName());
		} 
		this.name = name;
	}

	/**
	 * @return  string of the author names 
	 */
	public String getNames(){
		if(notLoaded)
			return names;
		else {	
			StringBuffer retVal = new StringBuffer(name.toString());
			for (AuthorNameDTO formatName : otherFormatNames) {
				retVal.append("; " + formatName.toString());
			}
			return retVal.toString();
		}
	}
	
	/**
	 * @param names the names to set
	 */
	public void setNames(String names) {
		this.names = names;
	}

	/**
	 * @return list of the author other format names
	 */
	public List<AuthorNameDTO> getOtherFormatNames() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return otherFormatNames;
	}

	/**
	 * @param otherFormatNames
	 *            list of the author other format names to set
	 */
	public void setOtherFormatNames(List<AuthorNameDTO> otherFormatNames) {
		this.otherFormatNames = otherFormatNames;
	}

	/**
	 * @return the author allNames - name + otherFormatNames
	 */
	public List<AuthorNameDTO> getAllNames() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		List<AuthorNameDTO> retVal = new ArrayList<AuthorNameDTO>();
		if ((!("".equals(name.getLastname()))))
			retVal.add(name);
		retVal.addAll(otherFormatNames);
		return retVal;
	}
	

	/**
	 * @return list of the author ALL names as select items
	 */
	public List<SelectItem> getAllNamesSelectItems() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		List<SelectItem> retVal = new ArrayList<SelectItem>();
		List<AuthorNameDTO> allAuthorNames = getAllNames();
		if (("".equals(name.getLastname())))
			retVal.add(new SelectItem(
							new AuthorNameDTO(),
							new FacesMessages("messages.messages-records",
									FacesContext.getCurrentInstance()
											.getViewRoot().getLocale())
									.getMessageFromResourceBundle("records.author.name.pleaseSelect")));
		for (AuthorNameDTO authorNameDTO : allAuthorNames) {
			SelectItem si = new SelectItem(authorNameDTO, authorNameDTO
					.toString());
			retVal.add(si);
		}
		return retVal;
	}

	/**
	 * @return date of the author birth
	 */
	public Calendar getDateOfBirth() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            date of the author birth to set
	 */
	public void setDateOfBirth(Calendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	/**
	 * @return year of the author birth
	 */
	public Integer getYearOfBirth() {
		if(dateOfBirth != null)
			return dateOfBirth.get(Calendar.YEAR);
		else
			return null;
	}

	/**
	 * @param yearOfBirth
	 *            year of the author birth to set
	 */
	public void setYearOfBirth(Integer yearOfBirth) {
		if(yearOfBirth!=null){
			if(dateOfBirth == null){
				dateOfBirth = new GregorianCalendar();
			}
			dateOfBirth.set(Calendar.YEAR, yearOfBirth);
			dateOfBirth.set(Calendar.DAY_OF_YEAR, 1);
		}
	}

	/**
	 * @return the institution where author works
	 */
	public InstitutionDTO getInstitution() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return institution;
	}

	/**
	 * @param institution
	 *            the institution where author work to set
	 */
	public void setInstitution(InstitutionDTO institution) {
		this.institution = institution;
	}

	/**
	 * @return the institutionName
	 */
	public String getInstitutionName() {
		if(notLoaded)
			return institutionName;
		else 
			return institution.getStringRepresentation();
	}

	/**
	 * @param institutionName the institutionName to set
	 */
	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	/**
	 * @return the organizationUnit
	 */
	public OrganizationUnitDTO getOrganizationUnit() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return organizationUnit;
	}

	/**
	 * @param organizationUnit the organizationUnit to set
	 */
	public void setOrganizationUnit(OrganizationUnitDTO organizationUnit) {
		this.organizationUnit = organizationUnit;
	}

	/**
	 * @return the currentPosition
	 */
	public AuthorPosition getCurrentPosition() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return currentPosition;
	}

	/**
	 * @param currentPosition the currentPosition to set
	 */
	public void setCurrentPosition(AuthorPosition currentPosition) {
		this.currentPosition = currentPosition;
	}

	/**
	 * @return the currentPositionName
	 */
	public String getCurrentPositionName() {
		if(notLoaded)
			return currentPositionName;
		else
			if ((getCurrentPosition()!=null) && (getCurrentPosition().getPosition()!=null))
				return getCurrentPosition().getPosition().getSomeTerm();
			else return "";
	}

	/**
	 * @param currentPositionName the currentPositionName to set
	 */
	public void setCurrentPositionName(String currentPositionName) {
		this.currentPositionName = currentPositionName;
	}

	/**
	 * @return the formerPositions
	 */
	public List<AuthorPosition> getFormerPositions() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return formerPositions;
	}

	/**
	 * @param formerPositions the formerPositions to set
	 */
	public void setFormerPositions(List<AuthorPosition> formerPositions) {
		this.formerPositions = formerPositions;
	}

	/**
	 * @return the author allPositions = currentPosition + formerPositions
	 */
	public List<AuthorPosition> getAllPositions() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		List<AuthorPosition> retVal = new ArrayList<AuthorPosition>();
		retVal.addAll(formerPositions);
		if ((currentPosition.getPosition().getClassId()!=null) && (! retVal.contains(currentPosition)))
			retVal.add(currentPosition);
		return retVal;
	}
	

	/**
	 * @return is author already registered
	 */
	public boolean isAlreadyRegistered() {
		return alreadyRegistered;
	}

	/**
	 * @param alreadyRegistered the already Registered to set
	 */
	public void setAlreadyRegistered(boolean alreadyRegistered) {
		this.alreadyRegistered = alreadyRegistered;
	}

	/**
	 * @return the authorUnsDissertations
	 */
	public boolean isAuthorUnsDissertations() {
		return authorUnsDissertations;
	}

	/**
	 * @param authorUnsDissertations the authorUnsDissertations to set
	 */
	public void setAuthorUnsDissertations(boolean authorUnsDissertations) {
		this.authorUnsDissertations = authorUnsDissertations;
	}

	/**
	 * @return the advisorUnsDissertations
	 */
	public boolean isAdvisorUnsDissertations() {
		return advisorUnsDissertations;
	}

	/**
	 * @param advisorUnsDissertations the advisorUnsDissertations to set
	 */
	public void setAdvisorUnsDissertations(boolean advisorUnsDissertations) {
		this.advisorUnsDissertations = advisorUnsDissertations;
	}

	/**
	 * @return the commissionMemberUnsDissertations
	 */
	public boolean isCommissionMemberUnsDissertations() {
		return commissionMemberUnsDissertations;
	}

	/**
	 * @param commissionMemberUnsDissertations the commissionMemberUnsDissertations to set
	 */
	public void setCommissionMemberUnsDissertations(
			boolean commissionMemberUnsDissertations) {
		this.commissionMemberUnsDissertations = commissionMemberUnsDissertations;
	}

	/**
	 * @return the commissionChairUnsDissertations
	 */
	public boolean isCommissionChairUnsDissertations() {
		return commissionChairUnsDissertations;
	}

	/**
	 * @param commissionChairUnsDissertations the commissionChairUnsDissertations to set
	 */
	public void setCommissionChairUnsDissertations(
			boolean commissionChairUnsDissertations) {
		this.commissionChairUnsDissertations = commissionChairUnsDissertations;
	}

	/**
	 * @return the authorPaDissertations
	 */
	public boolean isAuthorPaDissertations() {
		return authorPaDissertations;
	}

	/**
	 * @param authorPaDissertations the authorPaDissertations to set
	 */
	public void setAuthorPaDissertations(boolean authorPaDissertations) {
		this.authorPaDissertations = authorPaDissertations;
	}

	/**
	 * @return the advisorPaDissertations
	 */
	public boolean isAdvisorPaDissertations() {
		return advisorPaDissertations;
	}

	/**
	 * @param advisorPaDissertations the advisorPaDissertations to set
	 */
	public void setAdvisorPaDissertations(boolean advisorPaDissertations) {
		this.advisorPaDissertations = advisorPaDissertations;
	}

	/**
	 * @return the commissionMemberPaDissertations
	 */
	public boolean isCommissionMemberPaDissertations() {
		return commissionMemberPaDissertations;
	}

	/**
	 * @param commissionMemberPaDissertations the commissionMemberPaDissertations to set
	 */
	public void setCommissionMemberPaDissertations(
			boolean commissionMemberPaDissertations) {
		this.commissionMemberPaDissertations = commissionMemberPaDissertations;
	}

	/**
	 * @return the commissionChairPaDissertations
	 */
	public boolean isCommissionChairPaDissertations() {
		return commissionChairPaDissertations;
	}

	/**
	 * @param commissionChairPaDissertations the commissionChairPaDissertations to set
	 */
	public void setCommissionChairPaDissertations(
			boolean commissionChairPaDissertations) {
		this.commissionChairPaDissertations = commissionChairPaDissertations;
	}

	/**
	 * @return the sex
	 */
	public char getSex() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(char sex) {
		this.sex = sex;
	}
	
	/**
	 * @return the biography
	 */
	public MultilingualContentDTO getBiography() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return biography;
	}

	/**
	 * @param biography the biography to set
	 */
	public void setBiography(MultilingualContentDTO biography) {
		this.biography = biography;
	}

	/**
	 * @return the biographyTranslations
	 */
	public List<MultilingualContentDTO> getBiographyTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return biographyTranslations;
	}

	/**
	 * @param biographyTranslations the biographyTranslations to set
	 */
	public void setBiographyTranslations(
			List<MultilingualContentDTO> biographyTranslations) {
		this.biographyTranslations = biographyTranslations;
	}
	
	/**
	 * @return original biography or first translated biography (if original biography is not defined)
	 */
	public String getSomeBiography(){
		if(notLoaded){
			record.loadFromDatabase();
		}
		String retVal = "Not defined";
		if (biography.getContent() != null)
			retVal = biography.getContent();
		else if (biographyTranslations.size()>0)
				retVal = biographyTranslations.get(0).getContent();
		return retVal;
	}

	/**
	 * @return the keywords
	 */
	public MultilingualContentDTO getKeywords() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(MultilingualContentDTO keywords) {
		this.keywords = keywords;
	}
	
	/**
	 * @return original keywords or first translated keywords (if original keywords are not defined)
	 */
	public String getSomeKeywords(){
		if(notLoaded){
			record.loadFromDatabase();
		}
		String retVal = "Not defined";
		if (keywords.getContent() != null)
			retVal = keywords.getContent();
		else if (keywordsTranslations.size()>0)
				retVal = keywordsTranslations.get(0).getContent();
		return retVal;
	}

	/**
	 * @return the keywordsTranslations
	 */
	public List<MultilingualContentDTO> getKeywordsTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return keywordsTranslations;
	}

	/**
	 * @param keywordsTranslations the keywordsTranslations to set
	 */
	public void setKeywordsTranslations(
			List<MultilingualContentDTO> keywordsTranslations) {
		this.keywordsTranslations = keywordsTranslations;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the jmbg
	 */
	public String getJmbg() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return jmbg;
	}

	/**
	 * @param jmbg the jmbg to set
	 */
	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}
	
	/**
	 * @return the apvnt
	 */
	public String getApvnt() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return apvnt;
	}

	/**
	 * @param apvnt the apvnt to set
	 */
	public void setApvnt(String apvnt) {
		this.apvnt = apvnt;
	}

	/**
	 * @return the placeOfBirth
	 */
	public String getPlaceOfBirth() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return placeOfBirth;
	}

	/**
	 * @param placeOfBirth the placeOfBirth to set
	 */
	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * @return the titleInstitution
	 */
	public TitleInstitution getTitleInstitution() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return titleInstitution;
	}

	/**
	 * @param titleInstitution the titleInstitution to set
	 */
	public void setTitleInstitution(TitleInstitution titleInstitution) {
		this.titleInstitution = titleInstitution;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the directPhones
	 */
	public String getDirectPhones() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return directPhones;
	}

	/**
	 * @param directPhones the directPhones to set
	 */
	public void setDirectPhones(String directPhones) {
		this.directPhones = directPhones;
	}

	/**
	 * @return the localPhones
	 */
	public String getLocalPhones() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return localPhones;
	}

	/**
	 * @param localPhones the localPhones to set
	 */
	public void setLocalPhones(String localPhones) {
		this.localPhones = localPhones;
	}

	/**
	 * @return the researchAreas
	 */
	public List<ResearchAreaDTO> getResearchAreas() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return researchAreas;
	}

	/**
	 * @param researchAreas the researchAreas to set
	 */
	public void setResearchAreas(List<ResearchAreaDTO> researchAreas) {
		this.researchAreas = researchAreas;
	}

	/**
	 * @return the jobApplications
	 */
	public List<JobAdDTO> getJobApplications() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return jobApplications;
	}

	/**
	 * @param jobApplications the jobApplications to set
	 */
	public void setJobApplications(List<JobAdDTO> jobApplications) {
		this.jobApplications = jobApplications;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(notLoaded)
			return stringRepresentation;
		else {
			StringBuffer retVal = new StringBuffer();
			retVal.append(name.getLastname() + " "
					+ (((name.getOtherName() != null) && (!"".equals(name.getOtherName()))) ? ("(" + name.getOtherName() + ") "): "")
					+ (((name.getFirstname() != null) && (!"".equals(name.getFirstname()))) ? (name.getFirstname()): ""));
			if (dateOfBirth != null){
				retVal.append(", " + dateOfBirth.get(Calendar.YEAR));
			}
			if ((organizationUnit != null) && (!("".equals(organizationUnit.toString()))))
				retVal.append(", " + organizationUnit);
			if ((institution != null) && (!("".equals(institution.toString()))))
				retVal.append(", " + institution);
			if ((titleInstitution.getTitle() != null) && (!"".equals(titleInstitution.getTitle())))
				retVal.append(", " + titleInstitution.getTitle());

			return retVal.toString();
		}
	}
	
	@Override
	public String toString(String institutionId) {
		if(notLoaded)
			record.loadFromDatabase();

		StringBuffer retVal = new StringBuffer();
		if(institution.getControlNumber().equalsIgnoreCase(institutionId)){
			retVal.append("<b>");
		}
		retVal.append(name.getLastname() + " "
				+ (((name.getOtherName() != null) && (!"".equals(name.getOtherName()))) ? ("(" + name.getOtherName() + ") "): "")
				+ (((name.getFirstname() != null) && (!"".equals(name.getFirstname()))) ? (name.getFirstname()): ""));
		if(institution.getControlNumber().equalsIgnoreCase(institutionId)){
			retVal.append("</b>");
		}
		if (dateOfBirth != null){
			retVal.append(", " + dateOfBirth.get(Calendar.YEAR));
		}
		if ((organizationUnit != null) && (!("".equals(organizationUnit.toString()))))
			retVal.append(", " + organizationUnit);
		if ((institution != null) && (!("".equals(institution.toString()))))
			retVal.append(", " + institution);
		if ((titleInstitution.getTitle() != null) && (!"".equals(titleInstitution.getTitle())))
			retVal.append(", " + titleInstitution.getTitle());

		return retVal.toString();
	}
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getStringRepresentation()
	 */
	@Override
	public String getStringRepresentation() {
		return this.toString();
	}
	
	/**
	 *
	 * @return last one added name preview
	 */
	public String getLastOneNamePreview() {
		List<AuthorNameDTO> allNames = getAllNames();
		StringBuffer retVal = new StringBuffer();
		if(allNames.size()!=0)
			retVal.append(allNames.get(allNames.size()-1).toString());
		return retVal.toString();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		RecordDTO temp = (RecordDTO) arg0;
		if ((this.controlNumber != null)
				&& (this.controlNumber.equals(temp.controlNumber)))
			return true;
		else
			return false;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return titleInstitution.getTitle();
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String titleString) {
		this.titleInstitution.setTitle(titleString);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getHTMLRepresentation()
	 */
	@Override
	public String getHTMLRepresentation() {
		if((notLoaded) && (locale.getLanguage().equals("sr"))){
			return HTMLRepresentation;
		} else { 
			if(notLoaded)
				record.loadFromDatabase();
			FacesMessages fm= new FacesMessages("messages.messages-records",  locale); 
			//FacesContext.getCurrentInstance().getViewRoot().getLocale());
			StringBuffer retVal = new StringBuffer();
			retVal.append("<u>" + fm.getMessageFromResourceBundle("records.author.header") + "</u><br/>");
			if ((name != null) && (name.getFirstname() != null))
				retVal.append(fm.getMessageFromResourceBundle("records.author.editPanel.firstname") + ": "+ name.getFirstname() + "<br/>");
			else
				retVal.append("Author first name <br/>");
			
			if ((name != null) && (name.getLastname() != null))
				retVal.append(fm.getMessageFromResourceBundle("records.author.editPanel.lastname") + ": "+ name.getLastname() + "<br/>");
			else
				retVal.append("Author last name <br/>");
			
			if ((currentPosition != null) && (!"".equals(currentPosition.toString().trim())))
				retVal.append(fm.getMessageFromResourceBundle("records.author.editPanel.position") + ": "+ currentPosition + "<br/>");
			
			if ((organizationUnit != null) && (!"".equals(organizationUnit.toString().trim())))
				retVal.append(fm.getMessageFromResourceBundle("records.author.editPanel.organizationUnit") + ": "+ organizationUnit.toString() + "<br/>");
			if ((institution != null) && (!"".equals(institution.toString().trim())))
				retVal.append(fm.getMessageFromResourceBundle("records.author.editPanel.institution") + ": "+ institution.toString() + "<br/>");
			
			if (( titleInstitution != null) && (titleInstitution.getTitle() != null) && ( !"".equals(titleInstitution.getTitle().trim())))
				retVal.append("Author title: "+ titleInstitution.getTitle() + "<br/>");
			
			
			if ((uri!= null) && !("".equals(uri.trim()))){
				retVal.append("<a href=\"");
				retVal.append(uri);
				retVal.append("\" target=\"_blank\">Author web page</a> <br/>");	
			}
			
			return retVal.toString();
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getKnrXML(java.lang.String, rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO)
	 */
	@Override
	public String getKnrXML(String indent, ResultMeasureDTO resultMeasure) {
		if(notLoaded)
			record.loadFromDatabase();
		StringBuffer retVal = new StringBuffer();
		retVal.append(indent + "<researcher>\n");
		if(controlNumber != null)
			retVal.append(indent + "\t<identifier>" + controlNumber + "</identifier>\n");
		if((apvnt != null) && (!apvnt.equals("9999")))
			retVal.append(indent + "\t<apvnt>" + apvnt + "</apvnt>\n");
		if((name != null) && (name.getLastname() != null))
			retVal.append(indent + "\t<lastname>" + name.getLastname() + "</lastname>\n");
		if((name != null) && (name.getOtherName() != null) && (name.getOtherName().trim().length() > 0))
			retVal.append(indent + "\t<middlename>" + name.getOtherName() + "</middlename>\n");
		if((name != null) && (name.getFirstname() != null))
			retVal.append(indent + "\t<firstname>" + name.getFirstname() + "</firstname>\n");
		if((currentPositionName != null) && (currentPositionName.trim().length() > 0))
			retVal.append(indent + "\t<position>" + currentPositionName + "</position>\n");
		if((institution != null) && (institution.getSomeName() != null))
			retVal.append(indent + "\t<institution>" + institution.getSomeName()+ "</institution>\n");
		retVal.append(indent + "</researcher>\n");		
		return retVal.toString();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		otherFormatNames = new ArrayList<AuthorNameDTO>();
		name = new AuthorNameDTO();
//		dateOfBirth = null;
		institution = new InstitutionDTO();
		organizationUnit = new OrganizationUnitDTO();
		currentPosition = new AuthorPosition();
		currentPosition.setStartDate(new GregorianCalendar());
		formerPositions = new ArrayList<AuthorPosition>();
		biographyTranslations = new ArrayList<MultilingualContentDTO>();
		biography = new MultilingualContentDTO(null, MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		researchAreas = new ArrayList<ResearchAreaDTO>();
		relatedRecords = null;
	} 

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getControlNumber()
	 */
	@Override
	public String getControlNumber() {
		return super.getControlNumber();
	}	
	
}
