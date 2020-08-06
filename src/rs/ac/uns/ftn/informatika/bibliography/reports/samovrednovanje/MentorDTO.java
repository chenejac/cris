package rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;

public class MentorDTO {
	
	private int numM21a = 0;
	private int numM21 = 0;
	private int numM22 = 0;
	private int numM23 = 0;	
	private String firstName;
	private String lastName;
	private String title; 
	private String currentPositionName;
	private String jmbg;
	
	/**
	 * @return the numM21a
	 */
	public int getNumM21a() {
		return numM21a;
	}
	/**
	 * @param numM21a the numM21a to set
	 */
	public void setNumM21a(int numM21a) {
		this.numM21a = numM21a;
	}
	/**
	 * @return the numM21
	 */
	public int getNumM21() {
		return numM21;
	}
	/**
	 * @param numM21 the numM21 to set
	 */
	public void setNumM21(int numM21) {
		this.numM21 = numM21;
	}
	/**
	 * @return the numM22
	 */
	public int getNumM22() {
		return numM22;
	}
	/**
	 * @param numM22 the numM22 to set
	 */
	public void setNumM22(int numM22) {
		this.numM22 = numM22;
	}
	/**
	 * @return the numM23
	 */
	public int getNumM23() {
		return numM23;
	}
	/**
	 * @param numM23 the numM23 to set
	 */
	public void setNumM23(int numM23) {
		this.numM23 = numM23;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the currentPositionName
	 */
	public String getCurrentPositionName() {
		return currentPositionName;
	}
	/**
	 * @param currentPositionName the currentPositionName to set
	 */
	public void setCurrentPositionName(String currentPositionName) {
		this.currentPositionName = currentPositionName;
	}
	
		
	public String getJmbg() {
		return jmbg;
	}
	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return firstName + "," + lastName + "," + title + "," + currentPositionName + "," + numM21a + "," + numM21 + "," + numM22 + "," + numM23;
	}
	
	

}
