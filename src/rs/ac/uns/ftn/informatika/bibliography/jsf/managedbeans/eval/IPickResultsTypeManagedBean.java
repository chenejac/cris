package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.eval;

import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;

/**
 * Interface which must be implemented by managed beans that picking results type
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public interface IPickResultsTypeManagedBean {

	/**
	 * @param ResultsType
	 *            the ResultsType to set
	 */
	public void setResultsType(ResultsTypeDTO resultsType);

	/**
	 * Cancel picking ResultsType
	 */
	public void cancelPickingResultsType();
}

