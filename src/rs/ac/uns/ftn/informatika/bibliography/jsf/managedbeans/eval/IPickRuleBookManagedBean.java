package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.eval;

import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;

/**
 * Interface which must be implemented by managed beans that picking ruleBook
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public interface IPickRuleBookManagedBean {

	/**
	 * @param ruleBook
	 *            the ruleBook to set
	 */
	public void setRuleBook(RuleBookDTO ruleBook);

	/**
	 * Cancel picking ruleBook
	 */
	public void cancelPickingRuleBook();
}
