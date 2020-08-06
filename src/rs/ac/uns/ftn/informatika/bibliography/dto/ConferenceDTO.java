package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;


/**
 * DTO class which presents authority mARC21Record with data about conference.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class ConferenceDTO extends RecordDTO {

	private MultilingualContentDTO name;
	private String someName = "";
	private List<MultilingualContentDTO> nameTranslations;
	private String place="";
	private Integer year;
	private String period;
	private String number="";
	
	private String state;
	private String fee;
	private MultilingualContentDTO description;
	private List<MultilingualContentDTO> descriptionTranslations;
	private MultilingualContentDTO keywords;
	private List<MultilingualContentDTO> keywordsTranslations;
	private String uri;

	public ConferenceDTO() {
		super();
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		descriptionTranslations = new ArrayList<MultilingualContentDTO>();
		description = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
	}

	/**
	 * @param controlNumber
	 *            Control number of the authority mARC21Record
	 */
	public ConferenceDTO(String controlNumber) {
		super(controlNumber);
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		descriptionTranslations = new ArrayList<MultilingualContentDTO>();
		description = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
	}
	


	/**
	 * @param controlNumber
	 * @param name
	 * @param nameTranslations
	 * @param place
	 * @param state
	 * @param year
	 * @param period
	 * @param number
	 * @param fee
	 * @param description
	 * @param descriptionTranslations
	 * @param uri
	 */
	public ConferenceDTO(String controlNumber, MultilingualContentDTO name, 
			List<MultilingualContentDTO> nameTranslations, String place,
			String state, Integer year, String period, String number, String fee,
			MultilingualContentDTO description, List<MultilingualContentDTO> descriptionTranslations, 
			MultilingualContentDTO keywords, List<MultilingualContentDTO> keywordsTranslations, String uri) {
		super(controlNumber);
		this.name = name;
		this.nameTranslations = nameTranslations;
		this.place = place;
		this.state = state;
		this.year = year;
		this.period = period;
		this.number = number;
		this.fee = fee;
		this.description = description;
		this.descriptionTranslations = descriptionTranslations;
		this.keywords = keywords;
		this.keywordsTranslations = keywordsTranslations;
		this.uri = uri;
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
		return place;
	}

	/**
	 * @param place
	 *            the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
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
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the period
	 */
	public String getPeriod() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(String period) {
		this.period = period;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	
	/**
	 * @return the fee
	 */
	public String getFee() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return fee;
	}

	/**
	 * @param fee the fee to set
	 */
	public void setFee(String fee) {
		this.fee = fee;
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
			if ((place != null) && (!"".equals(place)))
				retVal.append(", " + place);
			if ((state != null) && (!"".equals(state)))
				retVal.append(", " + state);
			retVal.append(", " + year);
			if ((period != null) && (!"".equals(period)))
				retVal.append(", " + period);
			if ((number != null) && (!"".equals(number)))
				retVal.append(", No " + number);
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
		if(notLoaded){
			return HTMLRepresentation;
		}
		else { 
			FacesMessages fm= new FacesMessages("messages.messages-records",  new Locale("sr")); 
			//FacesContext.getCurrentInstance().getViewRoot().getLocale());
			StringBuffer retVal = new StringBuffer();
			retVal.append("<u>" + fm.getMessageFromResourceBundle("records.conference.header") + "</u><br/>");
			if (getSomeName() != null)
				retVal.append(fm.getMessageFromResourceBundle("records.conference.editPanel.name") + ": "+ getSomeName() + "<br/>");
			else
				retVal.append("Conference name <br/>");
			
			if (((place != null) && (!"".equals(place.toString().trim()))) || ((state != null) && (!"".equals(state.toString().trim()))))
				retVal.append(fm.getMessageFromResourceBundle("records.conference.editPanel.place") + ": "+ (((place != null) && (!"".equals(place.toString().trim())))?(place + " "):("")) + (((state != null) && (!"".equals(state.toString().trim())))?(state):(""))+ "<br/>");
			
			if (year != null) 
				retVal.append(fm.getMessageFromResourceBundle("records.conference.editPanel.year") + ": "+ year + "<br/>");
			if ((number != null) && (!"".equals(number.toString().trim())))
				retVal.append(fm.getMessageFromResourceBundle("records.conference.editPanel.number") + ": "+ number + "<br/>");
			
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
		retVal.append(indent + "<conference>\n");
		if(controlNumber != null)
			retVal.append(indent + "\t<identifier>" + controlNumber + "</identifier>\n");
		if(getSomeName() != null)
			retVal.append(indent + "\t<name>" + getSomeName() + "</name>\n");
		if((place != null) && (place.trim().length() > 0))
			retVal.append(indent + "\t<place>" + place + "</place>\n");
		if((state != null) && (state.trim().length() > 0))
			retVal.append(indent + "\t<country>" + state + "</country>\n");
		if(year != null)
			retVal.append(indent + "\t<year>" + year + "</year>\n");
		if((number != null) && (number.trim().length() > 0))
			retVal.append(indent + "\t<number>" + number + "</number>\n");
		retVal.append(indent + "</conference>\n");
		return retVal.toString();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		descriptionTranslations = new ArrayList<MultilingualContentDTO>();
		description = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
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
