package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.eval;

import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookImplementationDTO;

/**
 * Interface which must be implemented by managed beans that picking results type
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public interface IPickRuleBookImplementationManagedBean {

	/**
	 * @param ruleBookImplementation
	 *            the ruleBookImplementation to set
	 */
	public void setRuleBookImplementation(RuleBookImplementationDTO ruleBookImplementation);

	/**
	 * Cancel picking Implementation
	 */
	public void cancelPickingRuleBookImplementation();
}
