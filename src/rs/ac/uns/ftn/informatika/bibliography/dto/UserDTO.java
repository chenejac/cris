package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;

/**
 * DTO class which present user of the system.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class UserDTO implements Serializable {

	private String email;
	private String password;
	private String note;
	private String language = "sr";
	private String activationCode = "ACTIVATED";

	private AuthorDTO author;
	private InstitutionDTO institution;
	private OrganizationUnitDTO organizationUnit;

	private String type;

	public UserDTO() {
		author = new AuthorDTO();
		institution = new InstitutionDTO();
		organizationUnit = new OrganizationUnitDTO();
	}

	/**
	 * @param username
	 *            Username
	 * @param password
	 *            Password
	 * @param email
	 *            E-mail
	 * @param note
	 *            Note
	 * @param author
	 *            Author
	 * @param type
	 *            Type
	 * @param activationCode
	 *            activation code
	 */
	public UserDTO(String email, String password, String note,
			String language, AuthorDTO author, InstitutionDTO institution, OrganizationUnitDTO organizationUnit, String type, String activationCode) {
		this.password = password;
		this.note = note;
		this.language = language;
		this.author = author;
		this.institution = institution;
		this.organizationUnit = organizationUnit;
		this.type = type;
		this.activationCode = activationCode;
		setEmail(email);
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
		if(author.getControlNumber() != null){
			author.setEmail(email);
		}
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
		if(author.getControlNumber() != null){
			author.getBiography().setLanguage(language);
			author.getKeywords().setLanguage(language);
		}
	}

	/**
	 * @return the author
	 */
	public AuthorDTO getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(AuthorDTO author) {
		this.author = author;
	}
	

	/**
	 * @return the institution
	 */
	public InstitutionDTO getInstitution() {
		return institution;
	}

	/**
	 * @param institution the institution to set
	 */
	public void setInstitution(InstitutionDTO institution) {
		this.institution = institution;
	}

	/**
	 * @return the organizationUnit
	 */
	public OrganizationUnitDTO getOrganizationUnit() {
		return organizationUnit;
	}

	/**
	 * @param organizationUnit the organizationUnit to set
	 */
	public void setOrganizationUnit(OrganizationUnitDTO organizationUnit) {
		this.organizationUnit = organizationUnit;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the activationCode
	 */
	public String getActivationCode() {
		return activationCode;
	}

	/**
	 * @param activationCode the activationCode to set
	 */
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	/**
	 * @return the type in appropriate language
	 */
	public String getTypeInAppropriateLanguage() {
		return new FacesMessages("messages.messages-administration",
				FacesContext.getCurrentInstance().getViewRoot().getLocale())
				.getMessageFromResourceBundle("administration.user.editPanel.type."
						+ this.type);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		if(author!=null)
			retVal.append(author.getStringRepresentation() + ", ");
		if(email!=null)
			retVal.append(email);

		return retVal.toString();
	}

}
