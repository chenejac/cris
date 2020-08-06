package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class MetricsDTO implements Serializable {
	
	protected String metricsId;
	
	protected String uri;
	
	protected MultilingualContentDTO name;
	
	protected List<MultilingualContentDTO> nameTranslations; 
	
	protected MultilingualContentDTO description;
	
	protected List<MultilingualContentDTO> descriptionTranslations;
	
	public MetricsDTO() {
		super();
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		descriptionTranslations = new ArrayList<MultilingualContentDTO>();
		description = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
	}

	/**
	 * @param metricsId
	 * @param uri
	 * @param name
	 * @param nameTranslations
	 * @param description
	 * @param descriptionTranslations
	 */
	public MetricsDTO(String metricsId, String uri,
			MultilingualContentDTO name,
			List<MultilingualContentDTO> nameTranslations,
			MultilingualContentDTO description,
			List<MultilingualContentDTO> descriptionTranslations) {
		super();
		this.metricsId = metricsId;
		this.uri = uri;
		this.name = name;
		this.nameTranslations = nameTranslations;
		this.description = description;
		this.descriptionTranslations = descriptionTranslations;
	}
	
	/**
	 * @return the metricsId
	 */
	public String getMetricsId() {
		return metricsId;
	}

	/**
	 * @param metricsId the metricsId to set
	 */
	public void setMetricsId(String metricsId) {
		this.metricsId = metricsId;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the name
	 */
	public MultilingualContentDTO getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(MultilingualContentDTO name) {
		this.name = name;
	}

	/**
	 * @return the nameTranslations
	 */
	public List<MultilingualContentDTO> getNameTranslations() {
		return nameTranslations;
	}

	/**
	 * @param nameTranslations the nameTranslations to set
	 */
	public void setNameTranslations(List<MultilingualContentDTO> nameTranslations) {
		this.nameTranslations = nameTranslations;
	}

	/**
	 * @return the originalLanguage
	 */
	public String getOriginalLanguage() {
		return name.getLanguage();
	}

	/**
	 * @param originalLanguage the originalLanguage to set
	 */
	public void setOriginalLanguage(String originalLanguage) {
		this.name.setLanguage(originalLanguage);
		this.description.setLanguage(originalLanguage);
	}



	/**
	 * @return original name or first translated name (if original name is not defined)
	 */
	public String getSomeTerm(){
		String retVal = "";
		if (name.getContent() != null)
			retVal = name.getContent();
		else if (nameTranslations.size()>0)
				retVal = nameTranslations.get(0).getContent();
		return retVal;
	}
	
	/**
	 * @return the allTerms
	 */
	public List<MultilingualContentDTO> getAllNames() {
		List<MultilingualContentDTO> retVal = new ArrayList<MultilingualContentDTO>();
		if((name.content != null) && (!("".equals(name.content.trim()))))
			retVal.add(name);
		retVal.addAll(nameTranslations);
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		boolean retVal = false;
		try{
			MetricsDTO m = (MetricsDTO) arg0;
			retVal = (this.metricsId.equals(m.metricsId));
		} catch (Exception ex){	
		}
		return retVal;
	}
	
	public String getPreview() {
		return this.toString();
	}
	
}
