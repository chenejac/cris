package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;

/**
 * DTO class for monograph evaluation data.
 * 
 * @author bdimic@uns.ac.rs
 */

public class MonographEvaluationDataDTO implements Serializable {


	private String hasReviewInInternationalJournal;
	private int numberOfReviewers;
	private MonographDTO monograph; 
	private List<FileDTO> attachedFiles;
	private List<AutocitationDTO> autocitations;
	
	
	
	
	/**
	 * @param monograph
	 */
	public MonographEvaluationDataDTO(MonographDTO monograph) {
		super();
		this.monograph = monograph;
		autocitations = new ArrayList<AutocitationDTO>();
		attachedFiles = new ArrayList<FileDTO>();
	}
	/**
	 * 
	 */
	public MonographEvaluationDataDTO() {
		super();
		autocitations = new ArrayList<AutocitationDTO>();		
		attachedFiles = new ArrayList<FileDTO>();
	}
	
	/**
	 * @return the hasReviewInInternationalJournal
	 */
	public String getHasReviewInInternationalJournal() {
		return hasReviewInInternationalJournal;
	}
	/**
	 * @param hasReviewInInternationalJournal the hasReviewInInternationalJournal to set
	 */
	public void setHasReviewInInternationalJournal(
			String hasReviewInInternationalJournal) {
		this.hasReviewInInternationalJournal = hasReviewInInternationalJournal;
	}
	/**
	 * @return the numberOfReviewers
	 */
	public int getNumberOfReviewers() {
		return numberOfReviewers;
	}
	/**
	 * @param numberOfReviewers the numberOfReviewers to set
	 */
	public void setNumberOfReviewers(int numberOfReviewers) {
		this.numberOfReviewers = numberOfReviewers;
	}
	/**
	 * @return the monograph
	 */
	public MonographDTO getMonograph() {
		return monograph;
	}
	/**
	 * @param monograph the monograph to set
	 */
	public void setMonograph(MonographDTO monograph) {
		this.monograph = monograph;
	}
	/**
	 * @return the attachedFiles
	 */
	public List<FileDTO> getAttachedFiles() {
		return attachedFiles;
	}
	/**
	 * @param attachedFiles the attachedFiles to set
	 */
	public void setAttachedFiles(List<FileDTO> attachedFiles) {
		this.attachedFiles = attachedFiles;
	}
	/**
	 * @return the autocitations
	 */
	public List<AutocitationDTO> getAutocitations() {
		return autocitations;
	}
	/**
	 * @param autocitations the autocitations to set
	 */
	public void setAutocitations(List<AutocitationDTO> autocitations) {
		this.autocitations = autocitations;
	}
	
	public AutocitationDTO getAutocitationForAuthor(AuthorDTO author){
		for(AutocitationDTO autocitaion:autocitations){
			if(autocitaion.getForResearcher().equals(author))
				return autocitaion;			
		}
		return null;		
	}
	
	
	
	
		
}
