package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;

/**
 * DTO class which present author name (default name or other format name).
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class AuthorNameDTO implements Serializable {

	private String firstname;
	private String lastname;
	private String otherName;

	/**
	 * 
	 */
	public AuthorNameDTO() {
		firstname = "";
		lastname = "";
		otherName = "";
	}

	/**
	 * @param firstname
	 *            First name
	 * @param lastname
	 *            Last name
	 */
	public AuthorNameDTO(String firstname, String lastname, String otherName) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.otherName = otherName;
	}

	/**
	 * @return the first name
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname
	 *            the first name to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the last name
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname
	 *            the last name to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the otherName
	 */
	public String getOtherName() {
		return otherName;
	}

	/**
	 * @param otherName the otherName to set
	 */
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return lastname + " "
//				+ (((otherName != null) && (!"".equals(otherName))) ? (otherName + " "): "")
				+ (((firstname != null) && (!"".equals(firstname))) ? (firstname): "");
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean retVal = true;
		AuthorNameDTO an = (AuthorNameDTO) obj;
		try {
			if ((!(an.firstname.equals(this.firstname)))
					|| (!(an.lastname.equals(this.lastname)))
//					|| (!(an.otherName.equals(this.otherName)))
					) {
				retVal = false;
			}
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}

}
