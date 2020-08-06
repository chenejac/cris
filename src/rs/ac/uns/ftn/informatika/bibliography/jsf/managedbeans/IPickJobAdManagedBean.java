/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import rs.ac.uns.ftn.informatika.bibliography.dto.JobAdDTO;

/**
 * Interface which must be implemented by managed beans that picking job competitions
 * 
 * @author chenejac@uns.ac.rs
 */
public interface IPickJobAdManagedBean {

	/**
	 * @param job competition
	 *            the job competition to set
	 */
	public void setJobAd(JobAdDTO jobAd);

	/**
	 * Cancel picking job competition
	 */
	public void cancelPickingJobAd();
}
