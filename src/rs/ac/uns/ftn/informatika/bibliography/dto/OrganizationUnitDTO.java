package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.List;
import java.util.Locale;

import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;


/**
 * DTO class which presents authority mARC21Record with data about organizationUnit.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class OrganizationUnitDTO extends InstitutionDTO {


	private OrganizationUnitDTO superOrganizationUnit;
	/**
	 * 
	 */
	public OrganizationUnitDTO() {
		super();
		
	}

	/**
	 * @param controlNumber
	 *            Control number of the authority mARC21Record
	 */
	public OrganizationUnitDTO(String controlNumber) {
		super(controlNumber);
	}
	
	/**
	 * @param controlNumber
	 * @param name
	 * @param nameTranslations
	 * @param place
	 * @param institution
	 * @param superOrganizationUnit
	 * @param acro
	 * @param researchActivity
	 * @param researchActivityTranslations
	 * @param uri
	 */
	public OrganizationUnitDTO(String controlNumber, MultilingualContentDTO name,
			List<MultilingualContentDTO> nameTranslations, String place, 
			InstitutionDTO institution, OrganizationUnitDTO superOrganizationDTO, String acro,  
			List<ResearchAreaDTO> researchAreas, MultilingualContentDTO keywords, 
			List<MultilingualContentDTO> keywordsTranslations,String uri) {
		super(controlNumber);
		this.superOrganizationUnit = superOrganizationDTO;
	}

	
	public InstitutionDTO getInstitution() {
		return getSuperInstitution();
	}

	/**
	 * @param superInstitution the superInstitution to set
	 */
	public void setInstitution(InstitutionDTO institution) {
		setSuperInstitution(institution);
	}
	
	
	
	/**
	 * @return the superOrganizationUnit
	 */
	public OrganizationUnitDTO getSuperOrganizationUnit() {
		return superOrganizationUnit;
	}

	/**
	 * @param superOrganizationUnit the superOrganizationUnit to set
	 */
	public void setSuperOrganizationUnit(OrganizationUnitDTO superOrganizationUnit) {
		this.superOrganizationUnit = superOrganizationUnit;
	}

	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getLocalizedString(this.locale.getLanguage());
	}

	public String getLocalizedString(String language) {
		if((notLoaded) && (language.equals("sr")))
			return stringRepresentation;
		else {
			if(notLoaded)
				record.loadFromDatabase();
			StringBuffer retVal = new StringBuffer();
			String someName = getSomeName(language);
			if(someName != null)
				retVal.append(someName);

//			if (name.getContent() == null){
//				if(nameTranslations.size()>0)
//					retVal.append("" + nameTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ name.getLanguage())+ ")");
//			} else
//				retVal.append("" + name.getContent());

			if((superOrganizationUnit != null) && (superOrganizationUnit.getControlNumber()!=null))
				retVal.append(", " + superOrganizationUnit.getLocalizedString(language));
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
			retVal.append("<u>" + fm.getMessageFromResourceBundle("records.organizationUnit.header") + "</u><br/>");
			if (getSomeName() != null)
				retVal.append(fm.getMessageFromResourceBundle("records.organizationUnit.editPanel.name") + ": "+ getSomeName() + "<br/>");
			else
				retVal.append("Organization unit name <br/>");
			
			if ((place != null) && (!"".equals(place.toString().trim())))
				retVal.append(fm.getMessageFromResourceBundle("records.organizationUnit.editPanel.place") + ": "+ place + "<br/>");
			
			if((getInstitution() != null) && (getInstitution().getControlNumber()!=null)) 
				retVal.append(fm.getMessageFromResourceBundle("records.organizationUnit.editPanel.institution") + ": "+ getInstitution().toString() + "<br/>");
			if((superOrganizationUnit != null) && (superOrganizationUnit.getControlNumber()!=null)) 
				retVal.append(fm.getMessageFromResourceBundle("records.organizationUnit.editPanel.superOrganizationUnit") + ": "+ superOrganizationUnit.toString() + "<br/>");
			
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
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getControlNumber()
	 */
	@Override
	public String getControlNumber() {
		return super.getControlNumber();
	}	
	
}
