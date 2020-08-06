package rs.ac.uns.ftn.informatika.bibliography.db;

/**
 * Exception which is thrown if needed mARC21Record from database is in use by
 * somebody else
 * 
 * @author mbranko@uns.ns.ac.yu
 */
@SuppressWarnings("serial")
public class LockException extends Exception {

	private String inUseBy;

	/**
	 * @param inUseBy
	 *            e-mail of person who is using the mARC21Record
	 */
	public LockException(String inUseBy) {
		this.inUseBy = inUseBy;
	}

	/**
	 * @return e-mail of person who is using the mARC21Record
	 */
	public String getInUseBy() {
		return inUseBy;
	}

	/**
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return "The mARC21Record is locked by " + inUseBy;
	}

}
