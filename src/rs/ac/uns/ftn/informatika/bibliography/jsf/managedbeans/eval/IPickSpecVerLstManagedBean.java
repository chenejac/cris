package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.eval;

import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SpecVerLstDTO;

/**
 * Interface which must be implemented by managed beans that picking SpeciallyVerifiedList
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public interface IPickSpecVerLstManagedBean {

	/**
	 * @param speciallyVerifiedList
	 *            the speciallyVerifiedList to set
	 */
	public void setSpecVerLst(SpecVerLstDTO specVerLst);

	/**
	 * Cancel picking ruleBook
	 */
	public void cancelPickingSpecVerLst();
}