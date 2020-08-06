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
public class QueryDTO implements Serializable {
	
	protected int id;
	
	protected String operator = "";
	protected String value="";
	protected String fieldName="";
	protected String type="";
	protected float boost = 1f;
	
	public static String ALL = "ALL";
	public static String EXACT_PHRASE = "EXACT_PHRASE";
	public static String AT_LEAST_ONE = "AT_LEAST_ONE";
	public static String NONE = "NONE";
	public static String SIMILAR = "SIMILAR";
	public static String MORE_EXACT_PHRASES = "MORE_EXACT_PHRASES";
	
	public static String AND = "AND";
	public static String OR = "OR";
	public static String AND_NOT = "AND NOT";
	
	/**
	 * @param id
	 * @param operator
	 * @param type
	 */
	public QueryDTO(int id, String operator, String fieldName, String type) {
		super();
		this.id = id;
		this.operator=operator;
		this.type=type;
		this.fieldName=fieldName;
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
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String inputQuery) {
		this.value = inputQuery;
	}
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String inputQueryField) {
		this.fieldName = inputQueryField;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String inputQueryType) {
		this.type = inputQueryType;
	}

	/**
	 * @return the boost
	 */
	public float getBoost() {
		return boost;
	}
	/**
	 * @param boost the boost to set
	 */
	public void setBoost(float boost) {
		this.boost = boost;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QueryDTO [id=" + id + ", operator=" + operator + ", value="
				+ value + ", fieldName=" + fieldName + ", type=" + type + ", boost=" + boost + "]";
	}

}
