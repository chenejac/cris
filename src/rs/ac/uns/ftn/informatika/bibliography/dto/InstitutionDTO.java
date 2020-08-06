package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;


/**
 * DTO class which presents authority mARC21Record with data about institution.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class InstitutionDTO extends RecordDTO {

	protected MultilingualContentDTO name;
	protected String someName = "";
	protected List<MultilingualContentDTO> nameTranslations;
	protected String place = "";
	private InstitutionDTO superInstitution;
	
	protected String acro;
	protected MultilingualContentDTO keywords;
	protected List<MultilingualContentDTO> keywordsTranslations;
	protected String uri;

	protected List<ResearchAreaDTO> researchAreas;
	
	protected boolean enabledElement = false;
	
	public InstitutionDTO() {
		super();
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL);
		researchAreas = new ArrayList<ResearchAreaDTO>();
	}

	/**
	 * @param controlNumber
	 *            Control number of the authority mARC21Record
	 */
	public InstitutionDTO(String controlNumber) {
		super(controlNumber);
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL);
		researchAreas = new ArrayList<ResearchAreaDTO>();
	}

	
	/**
	 * @param controlNumber
	 * @param name
	 * @param nameTranslations
	 * @param place
	 * @param superInstitution
	 * @param acro
	 * @param researchActivity
	 * @param researchActivityTranslations
	 * @param uri
	 * @param enabledElement
	 */
	public InstitutionDTO(String controlNumber, MultilingualContentDTO name,
			List<MultilingualContentDTO> nameTranslations, String place, 
			InstitutionDTO superInstitution, String acro,  
			List<ResearchAreaDTO> researchAreas, MultilingualContentDTO keywords, 
			List<MultilingualContentDTO> keywordsTranslations,String uri, boolean enabledElement) {
		super(controlNumber);
		this.name = name;
		this.nameTranslations = nameTranslations;
		this.place = place;
		this.superInstitution = superInstitution;
		this.acro = acro;
		this.keywords = keywords;
		this.keywordsTranslations = keywordsTranslations;
		this.uri = uri;
		this.researchAreas = researchAreas;
		this.enabledElement = enabledElement;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale locale) {
		if(superInstitution != null)
			superInstitution.setLocale(locale);
		super.setLocale(locale);
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
		if((notLoaded) && (locale.getLanguage().equals("sr"))){
			return someName;
		}else {
			if(notLoaded && controlNumber !=null){
				record.loadFromDatabase();
			}
			String retVal = "";
			if (name.getContent() != null && (name.getLanguage() == null || locale.getLanguage().equals(name.getLanguage().substring(0,2)))){
				retVal = name.getContent();
			}
			for (MultilingualContentDTO nameTranslation : nameTranslations) {
				if((retVal == null) || (locale.getLanguage().equals(nameTranslation.getLanguage().substring(0,2))))
					retVal = nameTranslation.getContent();	
			}	
			if(retVal == null && name.getContent() != null){
				retVal = name.getContent();
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
	 * @return the superInstitution
	 */
	public InstitutionDTO getSuperInstitution() {
		return superInstitution;
	}

	/**
	 * @param superInstitution the superInstitution to set
	 */
	public void setSuperInstitution(InstitutionDTO superInstitution) {
		this.superInstitution = superInstitution;
	}

	/**
	 * @return the acro
	 */
	public String getAcro() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return acro;
	}

	/**
	 * @param acro the acro to set
	 */
	public void setAcro(String acro) {
		this.acro = acro;
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
	 * @return the enabledElement
	 */
	public boolean isEnabledElement() {
		List<String> tempMoguci = new ArrayList<String>();
		//tempMoguci.add("(BISIS)6705"); //Republika Srbija (BISIS)6705
		//tempMoguci.add("(BISIS)5920"); //Univerzitet u Novom Sadu (BISIS)5920
		tempMoguci.add("(BISIS)5929"); //Prirodno-matematiÄ�ki fakultet u Novom Sadu, Univerzitet u Novom Sadu (BISIS)5929
		tempMoguci.add("(BISIS)6782"); //Departman za matematiku i informatiku (BISIS)6782
		tempMoguci.add("(BISIS)6781"); //Departman za hemiju (BISIS)6781
		tempMoguci.add("(BISIS)6780"); //Departman za geografiju, turizam i hotelijerstvo (BISIS)6780
		tempMoguci.add("(BISIS)6779"); //Departman za fiziku (BISIS)6779
		tempMoguci.add("(BISIS)6778"); //Departman za biologiju i ekologiju (BISIS)6778
		
		//Katedre na Informatici PMF Novi Sad
		
		tempMoguci.add("(BISIS)6887"); //Katedra za opÅ¡tu algebru i teorijsko raÄ�unarstvo, Departman za matematiku i informatiku (BISIS)6887
		tempMoguci.add("(BISIS)6888"); //Katedra za analizu, verovatnoÄ‡u i diferencijalne jednaÄ�ine, Departman za matematiku i informatiku (BISIS)6888
		tempMoguci.add("(BISIS)6889"); //Katedra za numeriÄ�ku matematiku, Departman za matematiku i informatiku (BISIS)6889
		tempMoguci.add("(BISIS)6890"); //Katedra za primenjenu algebru, Departman za matematiku i informatiku (BISIS)6890
		tempMoguci.add("(BISIS)6891"); //Katedra za funkcionalnu analizu, geometriju i topologiju, Departman za matematiku i informatiku (BISIS)6891
		tempMoguci.add("(BISIS)6892"); //Katedra za raÄ�unarske nauke, Departman za matematiku i informatiku (BISIS)6892
		tempMoguci.add("(BISIS)6893"); //Katedra za matematiÄ�ku logiku i diskretnu matematiku, Departman za matematiku i informatiku (BISIS)6893
		tempMoguci.add("(BISIS)6894"); //Katedra za primenjenu analizu, Departman za matematiku i informatiku (BISIS)6894
		tempMoguci.add("(BISIS)6895"); //Katedra za informacione sisteme, Departman za matematiku i informatiku (BISIS)6895
		
		
		for(String tempNumber:tempMoguci){
			if(tempNumber.equalsIgnoreCase(controlNumber))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return the enabledElement
	 */
	public boolean isEnabledElementForAuthorSearch() {
		List<String> tempMoguci = new ArrayList<String>();
		//tempMoguci.add("(BISIS)6705"); //Republika Srbija (BISIS)6705
		//tempMoguci.add("(BISIS)5920"); //Univerzitet u Novom Sadu (BISIS)5920
		//tempMoguci.add("(BISIS)5929"); //Prirodno-matematiÄ�ki fakultet u Novom Sadu, Univerzitet u Novom Sadu (BISIS)5929
		tempMoguci.add("(BISIS)6782"); //Departman za matematiku i informatiku (BISIS)6782
		//tempMoguci.add("(BISIS)6781"); //Departman za hemiju (BISIS)6781
		//tempMoguci.add("(BISIS)6780"); //Departman za geografiju, turizam i hotelijerstvo (BISIS)6780
		//tempMoguci.add("(BISIS)6779"); //Departman za fiziku (BISIS)6779
		//tempMoguci.add("(BISIS)6778"); //Departman za biologiju i ekologiju (BISIS)6778
		
		
		tempMoguci.add("(BISIS)6887"); //Katedra za opÅ¡tu algebru i teorijsko raÄ�unarstvo, Departman za matematiku i informatiku (BISIS)6887
		tempMoguci.add("(BISIS)6888"); //Katedra za analizu, verovatnoÄ‡u i diferencijalne jednaÄ�ine, Departman za matematiku i informatiku (BISIS)6888
		tempMoguci.add("(BISIS)6889"); //Katedra za numeriÄ�ku matematiku, Departman za matematiku i informatiku (BISIS)6889
		tempMoguci.add("(BISIS)6890"); //Katedra za primenjenu algebru, Departman za matematiku i informatiku (BISIS)6890
		tempMoguci.add("(BISIS)6891"); //Katedra za funkcionalnu analizu, geometriju i topologiju, Departman za matematiku i informatiku (BISIS)6891
		tempMoguci.add("(BISIS)6892"); //Katedra za raÄ�unarske nauke, Departman za matematiku i informatiku (BISIS)6892
		tempMoguci.add("(BISIS)6893"); //Katedra za matematiÄ�ku logiku i diskretnu matematiku, Departman za matematiku i informatiku (BISIS)6893
		tempMoguci.add("(BISIS)6894"); //Katedra za primenjenu analizu, Departman za matematiku i informatiku (BISIS)6894
		tempMoguci.add("(BISIS)6895"); //Katedra za informacione sisteme, Departman za matematiku i informatiku (BISIS)6895
		
		
		for(String tempNumber:tempMoguci){
			if(tempNumber.equalsIgnoreCase(controlNumber))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param enabledElement the enabledElement to set
	 */
	public void setEnabledElement(boolean enabledElement) {
		this.enabledElement = enabledElement;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if((notLoaded) && (locale.getLanguage().equals("sr"))){
			return stringRepresentation;
		} else {
			if(notLoaded && controlNumber!=null)
				record.loadFromDatabase();
			StringBuffer retVal = new StringBuffer();
			String someName = getSomeName();
			if(someName != null)
				retVal.append(getSomeName());
			if((superInstitution != null) && (superInstitution.getControlNumber()!=null) && (superInstitution.getSuperInstitution() != null) && (superInstitution.getSuperInstitution().getControlNumber()!=null)) 
				retVal.append(", " + superInstitution.toString());
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
		if((notLoaded) && (locale.getLanguage().equals("sr")))
			return HTMLRepresentation;
		else { 
			if(notLoaded)
				record.loadFromDatabase();
			FacesMessages fm= new FacesMessages("messages.messages-records",  locale); 
			//FacesContext.getCurrentInstance().getViewRoot().getLocale());
			StringBuffer retVal = new StringBuffer();
			retVal.append("<u>" + fm.getMessageFromResourceBundle("records.institution.header") + "</u><br/>");
			if (getSomeName() != null)
				retVal.append(fm.getMessageFromResourceBundle("records.institution.editPanel.name") + ": "+ getSomeName() + "<br/>");
			else
				retVal.append("Institution name <br/>");
			
			if ((place != null) && (!"".equals(place.toString().trim())))
				retVal.append(fm.getMessageFromResourceBundle("records.institution.editPanel.place") + ": "+ place + "<br/>");
			
			if((superInstitution != null) && (superInstitution.getControlNumber()!=null) && (superInstitution.getSuperInstitution() != null) && (superInstitution.getSuperInstitution().getControlNumber()!=null)) 
				retVal.append(fm.getMessageFromResourceBundle("records.institution.editPanel.superInstitution") + ": "+ superInstitution.toString() + "<br/>");
			
			return retVal.toString();
		}
	}
	
	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean retVal = true;
		RecordDTO dto = (RecordDTO) obj;
		try {
			if((controlNumber == null) && (dto.getControlNumber() == null))
				retVal = true;
			else 
				retVal = super.equals(obj);
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
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
	
	/* (non-Javadoc)
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#toString(java.lang.String)
	 */
	@Override
	public String toString(String id) {
		// TODO Auto-generated method stub
		return toString();
	}

}
