package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.eval;

import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SciencesGroupDTO;

/**
 * Interface which must be implemented by managed beans that picking sciences group
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public interface IPickSciencesGroupManagedBean {

	/**
	 * @param sciencesGroup
	 *            the sciencesGroup to set
	 */
	public void setSciencesGroup(SciencesGroupDTO sciencesGroup);

	/**
	 * Cancel picking sciences Group
	 */
	public void cancelPickingSciencesGroup();
}
