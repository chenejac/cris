/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;

/**
 * @author penca_valentin@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class SearchDTO implements Serializable {
	
	protected int id;
	
	protected String operator = "";
	protected String inputQuery="";
	protected String inputQueryType="";
	
	
	
	
	/**
	 * @param id
	 * @param operator
	 * @param type
	 */
	public SearchDTO(int id, String operator, String inputQueryType) {
		super();
		this.id = id;
		this.operator=operator;
		this.inputQueryType=inputQueryType;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}
	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	/**
	 * @return the value
	 */
	public String getInputQuery() {
		return inputQuery;
	}
	/**
	 * @param value the value to set
	 */
	public void setInputQuery(String inputQuery) {
		this.inputQuery = inputQuery;
	}
	/**
	 * @return the type
	 */
	public String getInputQueryType() {
		return inputQueryType;
	}
	/**
	 * @param type the type to set
	 */
	public void setInputQueryType(String inputQueryType) {
		this.inputQueryType = inputQueryType;
	}
	

}
