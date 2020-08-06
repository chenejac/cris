/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.dto;

import rs.ac.uns.ftn.informatika.bibliography.reports.knr.ResultsGroupDTO;

/**
 * @author chenejac@uns.ac.rs
 *
 */
public class PublicationSelectionDTO {
	
	private PublicationDTO publication;
	private boolean selected = false;	
	private ResultsGroupDTO resultEvaluation = null;
	
	/**
	 * @param publication
	 * @param selected
	 */
	public PublicationSelectionDTO(PublicationDTO publication, boolean selected, ResultsGroupDTO resultEvaluation) {
		super();
		this.publication = publication;
		this.selected = selected;
		this.resultEvaluation = resultEvaluation;
	}
	/**
	 * @return the publication
	 */
	public PublicationDTO getPublication() {
		return publication;
	}
	/**
	 * @param publication the publication to set
	 */
	public void setPublication(PublicationDTO publication) {
		this.publication = publication;
	}
	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}
	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	/**
	 * @return the resultEvaluation
	 */
	public ResultsGroupDTO getResultEvaluation() {
		return resultEvaluation;
	}
	/**
	 * @param resultEvaluation the resultEvaluation to set
	 */
	public void setResultEvaluation(ResultsGroupDTO resultEvaluation) {
		this.resultEvaluation = resultEvaluation;
	}
	
	

}
