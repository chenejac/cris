package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.eval;

import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeGroupDTO;

/**
 * Interface which must be implemented by managed beans that picking results type group
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public interface IPickResultsTypeGroupManagedBean {

	/**
	 * @param ResultsTypeGroup
	 *            the ResultsTypeGroup to set
	 */
	public void setResultsTypeGroup(ResultsTypeGroupDTO resultsTypeGroup);

	/**
	 * Cancel picking ResultsTypeGroup
	 */
	public void cancelPickingResultsTypeGroup();
}
