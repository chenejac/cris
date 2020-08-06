package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;


/**
 * DTO class which presents authority mARC21Record with data about job competition.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class JobAdDTO extends RecordDTO {

	private MultilingualContentDTO name;
	private String someName = "";
	private List<MultilingualContentDTO> nameTranslations;
	private String place;
	private InstitutionDTO institution;
	private Calendar startDate;
	private Calendar endDate;
	
	private MultilingualContentDTO description;
	private List<MultilingualContentDTO> descriptionTranslations;
	private MultilingualContentDTO keywords;
	private List<MultilingualContentDTO> keywordsTranslations;
	private MultilingualContentDTO conditions;
	private List<MultilingualContentDTO> conditionsTranslations;
	private String uri;
	
	private CommissionDTO commission;
	private List<AuthorDTO> applications;

	public JobAdDTO() {
		super();
		institution = new InstitutionDTO();
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		descriptionTranslations = new ArrayList<MultilingualContentDTO>();
		description = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		conditionsTranslations = new ArrayList<MultilingualContentDTO>();
		conditions = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		applications = new ArrayList<AuthorDTO>();
	}

	/**
	 * @param controlNumber
	 *            Control number of the authority mARC21Record
	 */
	public JobAdDTO(String controlNumber) {
		super(controlNumber);
		institution = new InstitutionDTO();
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		descriptionTranslations = new ArrayList<MultilingualContentDTO>();
		description = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		conditionsTranslations = new ArrayList<MultilingualContentDTO>();
		conditions = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		applications = new ArrayList<AuthorDTO>();
	}
	
	/**
	 * @param name
	 * @param someName
	 * @param nameTranslations
	 * @param place
	 * @param institution
	 * @param startDate
	 * @param endDate
	 * @param description
	 * @param descriptionTranslations
	 * @param keywords
	 * @param keywordsTranslations
	 * @param conditions
	 * @param conditionsTranslations
	 * @param uri
	 */
	public JobAdDTO(MultilingualContentDTO name, String someName,
			List<MultilingualContentDTO> nameTranslations, String place,
			InstitutionDTO institution, Calendar startDate, Calendar endDate,
			MultilingualContentDTO description,
			List<MultilingualContentDTO> descriptionTranslations,
			MultilingualContentDTO keywords,
			List<MultilingualContentDTO> keywordsTranslations,
			MultilingualContentDTO conditions,
			List<MultilingualContentDTO> conditionsTranslations, String uri,
			List<AuthorDTO> applications) {
		super();
		this.name = name;
		this.someName = someName;
		this.nameTranslations = nameTranslations;
		this.place = place;
		this.institution = institution;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.descriptionTranslations = descriptionTranslations;
		this.keywords = keywords;
		this.keywordsTranslations = keywordsTranslations;
		this.conditions = conditions;
		this.conditionsTranslations = conditionsTranslations;
		this.uri = uri;
		this.applications = applications;
	}

	/**
	 * @return the name
	 */
	public MultilingualContentDTO getName() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(MultilingualContentDTO name) {
		this.name = name;
	}
	
	/**
	 * @return original name or first translated name (if original name is not defined)
	 */
	public String getSomeName(){
		if(notLoaded)
			return someName;
		else {
			String retVal = null;
			if (name.getContent() != null)
				retVal = name.getContent();
			else if (nameTranslations.size()>0){
						retVal = nameTranslations.get(0).getContent();
					}
			return retVal;
		}
	}

	/**
	 * @param someName the someName to set
	 */
	public void setSomeName(String someName) {
		this.someName = someName;
	}

	/**
	 * @return the nameTranslations
	 */
	public List<MultilingualContentDTO> getNameTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return nameTranslations;
	}

	/**
	 * @param nameTranslations the nameTranslations to set
	 */
	public void setNameTranslations(List<MultilingualContentDTO> nameTranslations) {
		this.nameTranslations = nameTranslations;
	}

	/**
	 * @return the place
	 */
	public String getPlace() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * @return the institution
	 */
	public InstitutionDTO getInstitution() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return institution;
	}

	/**
	 * @param institution the institution to set
	 */
	public void setInstitution(InstitutionDTO institution) {
		this.institution = institution;
	}

	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the description
	 */
	public MultilingualContentDTO getDescription() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(MultilingualContentDTO description) {
		this.description = description;
	}

	/**
	 * @return the descriptionTranslations
	 */
	public List<MultilingualContentDTO> getDescriptionTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return descriptionTranslations;
	}

	/**
	 * @param descriptionTranslations the descriptionTranslations to set
	 */
	public void setDescriptionTranslations(
			List<MultilingualContentDTO> descriptionTranslations) {
		this.descriptionTranslations = descriptionTranslations;
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
	 * @return the conditions
	 */
	public MultilingualContentDTO getConditions() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return conditions;
	}

	/**
	 * @param conditions the conditions to set
	 */
	public void setConditions(MultilingualContentDTO conditions) {
		this.conditions = conditions;
	}

	/**
	 * @return the conditionsTranslations
	 */
	public List<MultilingualContentDTO> getConditionsTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return conditionsTranslations;
	}

	/**
	 * @param conditionsTranslations the conditionsTranslations to set
	 */
	public void setConditionsTranslations(
			List<MultilingualContentDTO> conditionsTranslations) {
		this.conditionsTranslations = conditionsTranslations;
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
	 * @return the commission
	 */
	public CommissionDTO getCommission() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return commission;
	}

	/**
	 * @param commission the commission to set
	 */
	public void setCommission(CommissionDTO commission) {
		this.commission = commission;
	}

	/**
	 * @return the applications
	 */
	public List<AuthorDTO> getApplications() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return applications;
	}

	/**
	 * @param applications the applications to set
	 */
	public void setApplications(List<AuthorDTO> applications) {
		this.applications = applications;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(notLoaded) {
			return stringRepresentation;
		} else {
			StringBuffer retVal = new StringBuffer();
			if (name.getContent() == null){
				if(nameTranslations.size()>0)
					retVal.append("" + nameTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ name.getLanguage())+ ")");
			} else
				retVal.append("" + name.getContent());
			if ((institution != null) && (institution.getControlNumber() != null))
				retVal.append(", " + institution.toString());
			if ((startDate != null) && (endDate != null)){
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				String startDateString = null;
				String endDateString = null;
				startDateString = formatter.format(startDate.getTime());
				endDateString = formatter.format(endDate.getTime());
				retVal.append(", " + startDateString + " - " + endDateString);
			}
			return retVal.toString();
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getStringRepresentation()
	 */
	@Override
	public String getStringRepresentation() {
		return this.toString();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getHTMLRepresentation()
	 */
	@Override
	public String getHTMLRepresentation() {
		if(notLoaded)
			return HTMLRepresentation;
		else { 
			FacesMessages fm= new FacesMessages("messages.messages-records",  new Locale("sr")); 
			//FacesContext.getCurrentInstance().getViewRoot().getLocale());
			StringBuffer retVal = new StringBuffer();
			retVal.append("<u>" + fm.getMessageFromResourceBundle("records.jobAd.header") + "</u><br/>");
			if(commission != null){
				if (getSomeName() != null)
					retVal.append(fm.getMessageFromResourceBundle("records.jobAd.editPanel.name") + ": "+ getSomeName() + "<br/>");
				else
					retVal.append("Job ad name <br/>");
				
				if ((startDate != null) && (endDate != null)){
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					String startDateString = null;
					String endDateString = null;
					startDateString = formatter.format(startDate.getTime());
					endDateString = formatter.format(endDate.getTime());
					retVal.append(fm.getMessageFromResourceBundle("records.jobAd.editPanel.startDate") + "-" + fm.getMessageFromResourceBundle("records.jobAd.editPanel.endDate") + ": " + startDateString + " - " + endDateString + "<br/>");
				}
				
				if (uri != null)
					retVal.append(fm.getMessageFromResourceBundle("records.jobAd.editPanel.uri") + ": <a href=\""+ uri + "\">link</a><br/>");
				
				
				if ((getDescription().getContent() != null) && (!"".equals(description.getContent().toString().trim()))){
					retVal.append("<u>" + fm.getMessageFromResourceBundle("records.jobAd.editPanel.description") + "</u><br/>" + description.getContent() + "<br/>");
				} else if(descriptionTranslations.size()>0)
					retVal.append("<u>" + fm.getMessageFromResourceBundle("records.jobAd.editPanel.description") + "</u><br/>" + descriptionTranslations.get(0).getContent() + "<br/>");
				
				if ((getConditions().getContent() != null) && (!"".equals(conditions.getContent().toString().trim())))
					retVal.append("<u>" + fm.getMessageFromResourceBundle("records.jobAd.editPanel.conditions") + "</u><br/>" + conditions.getContent() + "<br/>");
				else if(conditionsTranslations.size()>0)
					retVal.append("<u>" + fm.getMessageFromResourceBundle("records.jobAd.editPanel.conditions") + "</u><br/>" + conditionsTranslations.get(0).getContent() );
			} else {
				if (getPlace() != null)
					retVal.append(fm.getMessageFromResourceBundle("records.jobAd.editPanel.place") + ": "+ getPlace() + "<br/>");
				
				if ((startDate != null) && (endDate != null)){
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					String startDateString = null;
					String endDateString = null;
					startDateString = formatter.format(startDate.getTime());
					endDateString = formatter.format(endDate.getTime());
					retVal.append(fm.getMessageFromResourceBundle("records.jobAd.editPanel.startDate") + "-" + fm.getMessageFromResourceBundle("records.jobAd.editPanel.endDate") + ": " + startDateString + " - " + endDateString + "<br/>");
				}
				if (getSomeName() != null)
					retVal.append(fm.getMessageFromResourceBundle("records.jobAd.simpleEditPanel.name") + ": "+ getSomeName() + "<br/>");
				
			}
			return retVal.toString();
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		institution = null;
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		descriptionTranslations = new ArrayList<MultilingualContentDTO>();
		description = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		conditionsTranslations = new ArrayList<MultilingualContentDTO>();
		conditions = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		relatedRecords = null;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getControlNumber()
	 */
	@Override
	public String getControlNumber() {
		return super.getControlNumber();
	}	
	
	/* (non-Javadoc)
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#toString(java.lang.String)
	 */
	@Override
	public String toString(String id) {
		// TODO Auto-generated method stub
		return toString();
	}
	
}
