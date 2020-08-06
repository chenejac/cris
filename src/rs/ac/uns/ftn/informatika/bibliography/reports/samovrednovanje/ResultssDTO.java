package rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje;

import java.util.ArrayList;
import java.util.List;

public class ResultssDTO {
	
	List<ResultForYearDTO> results = new ArrayList<ResultForYearDTO>();

	/**
	 * @return the results
	 */
	public List<ResultForYearDTO> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<ResultForYearDTO> results) {
		this.results = results;
	}

}
