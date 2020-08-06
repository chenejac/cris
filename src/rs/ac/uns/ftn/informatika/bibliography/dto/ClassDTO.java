package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;


/**
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class ClassDTO implements Serializable {

	protected String schemeId;
	
	protected String classId;
	
	protected MultilingualContentDTO term;
	
	protected List<MultilingualContentDTO> termTranslations; 
	
	protected MultilingualContentDTO description;
	
	protected List<MultilingualContentDTO> descriptionTranslations;
	
	protected Locale locale;
	
	public ClassDTO() {
		super();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		locale = (facesContext==null)?new Locale("sr"):facesContext.getViewRoot().getLocale();	
		termTranslations = new ArrayList<MultilingualContentDTO>();
		term = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		descriptionTranslations = new ArrayList<MultilingualContentDTO>();
		description = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
	}

	

	/**
	 * @param schemeId
	 * @param classId
	 * @param term
	 * @param termTranslations
	 * @param description
	 * @param descriptionTranslations
	 */
	public ClassDTO(String schemeId, String classId, MultilingualContentDTO term, List<MultilingualContentDTO> termTranslations,
			MultilingualContentDTO description, List<MultilingualContentDTO> descriptionTranslations) {
		super();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		locale = (facesContext==null)?new Locale("sr"):facesContext.getViewRoot().getLocale();	
		this.schemeId = schemeId;
		this.classId = classId;
		this.term = term;
		this.termTranslations = termTranslations;
		this.description = description;
		this.descriptionTranslations = descriptionTranslations;
	}

	/**
	 * @return the schemeId
	 */
	public String getSchemeId() {
		return schemeId;
	}


	/**
	 * @param schemeId the schemeId to set
	 */
	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}


	/**
	 * @return the classId
	 */
	public String getClassId() {
		return classId;
	}


	/**
	 * @param classId the classId to set
	 */
	public void setClassId(String classId) {
		this.classId = classId;
	}
	
	
	/**
	 * @return the term
	 */
	public MultilingualContentDTO getTerm() {
		return term;
	}


	/**
	 * @param term the term to set
	 */
	public void setTerm(MultilingualContentDTO term) {
		this.term = term;
	}
	
	/**
	 * @return the originalLanguage
	 */
	public String getOriginalLanguage() {
		return term.getLanguage();
	}

	/**
	 * @param originalLanguage the originalLanguage to set
	 */
	public void setOriginalLanguage(String originalLanguage) {
		this.term.setLanguage(originalLanguage);
		this.description.setLanguage(originalLanguage);
	}



	/**
	 * @return original term or first translated term (if original term is not defined)
	 */
	public String getSomeTerm(){
		
		
		String retVal = null;
		if(term.getLanguage() != null) {
			if (term.getContent() != null && locale.getLanguage().equals(term.getLanguage().substring(0,2)))
				retVal = term.getContent();
			for (MultilingualContentDTO termTranslation : termTranslations) {
				if((retVal == null) && (locale.getLanguage().equals(term.getLanguage().substring(0,2))) && term.getContent()!=null)
					retVal = termTranslation.getContent();
			}
		}
		//ako nije na trazenom jeziku postavi bilo koji
		if (retVal == null){
			if (term.getContent() != null)
				retVal = term.getContent();
			else
				for (MultilingualContentDTO termTranslation : termTranslations) {
					if(termTranslation.getContent()!=null){
						retVal = termTranslation.getContent();	
						break;
					}
				}
		}
//		System.out.println(retVal);
		
		return retVal!=null?retVal:"";
	}


	/**
	 * @return the termTranslations
	 */
	public List<MultilingualContentDTO> getTermTranslations() {
		return termTranslations;
	}


	/**
	 * @param termTranslations the termTranslations to set
	 */
	public void setTermTranslations(List<MultilingualContentDTO> termTranslations) {
		this.termTranslations = termTranslations;
	}
	
	/**
	 * @return the allTerms
	 */
	public List<MultilingualContentDTO> getAllTerms() {
		List<MultilingualContentDTO> retVal = new ArrayList<MultilingualContentDTO>();
		if((term.content != null) && (!("".equals(term.content.trim()))))
			retVal.add(term);
		retVal.addAll(termTranslations);
		return retVal;
	}

	/**
	 * @return the description
	 */
	public MultilingualContentDTO getDescription() {
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
	 * @return the allDescriptions
	 */
	public List<MultilingualContentDTO> getAllDescriptions() {
		List<MultilingualContentDTO> retVal = new ArrayList<MultilingualContentDTO>();
		if((description.content != null) && (!("".equals(description.content.trim()))))
			retVal.add(description);
		retVal.addAll(descriptionTranslations);
		return retVal;
	}


	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}



	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}



	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		boolean retVal = false;
		try{
			ClassDTO c = (ClassDTO) arg0;
			retVal = ((this.classId.equals(c.classId)) && (this.schemeId.equals(c.schemeId)));
			
		} catch (Exception ex){	
		}
		return retVal;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return classId.hashCode()/2 + schemeId.hashCode()/2;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getSomeTerm();
	}

	public String getClassComparableId() {
		return schemeId+"-"+classId;
	}
	
}
