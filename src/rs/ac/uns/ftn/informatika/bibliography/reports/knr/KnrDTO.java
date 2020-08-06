/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.reports.knr;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;

/**
 * @author chenejac@uns.ac.rs
 *
 */
public class KnrDTO {
	
	private AuthorDTO researcher;
	private List<StudyFinalDocumentDTO> diplomas;
	private List<ResultsGroupDTO> resultsGroups;
	
	
	/**
	 * @return the researcher
	 */
	public AuthorDTO getResearcher() {
		return researcher;
	}

	/**
	 * @param researcher the researcher to set
	 */
	public void setResearcher(AuthorDTO researcher) {
		this.researcher = researcher;
	}

	/**
	 * @return the diplomas
	 */
	public List<StudyFinalDocumentDTO> getDiplomas() {
		return diplomas;
	}

	/**
	 * @param diplomas the diplomas to set
	 */
	public void setDiplomas(List<StudyFinalDocumentDTO> diplomas) {
		this.diplomas = diplomas;
	}

	/**
	 * @return the resultsGroups
	 */
	public List<ResultsGroupDTO> getResultsGroups() {
		return resultsGroups;
	}

	/**
	 * @param resultsGroups the resultsGroups to set
	 */
	public void setResultsGroups(List<ResultsGroupDTO> resultTypes) {
		this.resultsGroups = resultTypes;
	}
	
	/**
	 * @return the results
	 */
	public List<ResultDTO> getResults() {
		List<ResultDTO> retVal = new ArrayList<ResultDTO>();
		for (ResultsGroupDTO resultsGroup : resultsGroups) {
			retVal.addAll(resultsGroup.getResults());
		}
		return retVal;
	}
	
	public int getTotalPapers(){
		int totalPapers = 0;
		for(ResultsGroupDTO group:resultsGroups){
			totalPapers = totalPapers+group.getResults().size();
		}
		return totalPapers;
	}
	
	public double getTotalPoints(){
		double totalPoints = 0.0;
		for(ResultsGroupDTO group:resultsGroups){
			totalPoints = totalPoints+group.getResults().size()*group.getQuantitativeMeasure();
		}		
		return totalPoints;
	}
	

}
