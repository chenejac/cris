package rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO class which presents a table primary key.
 * 
 * @author "chenejac@uns.ac.rs"
 *
 */
public class PrimaryKey {

	private String name;
	private List<Column> columns;
	private String updateRule;
	private String deleteRule;

	/**
	 * Default constructor
	 */
	public PrimaryKey() {
		columns = new ArrayList<Column>();
	}

	/**
	 * @param name
	 */
	public PrimaryKey(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public List<Column> getColumns() {
		return columns;
	}

	/**
	 * @param columns
	 */
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	/**
	 * @param column
	 */
	public void addColumn(Column column) {
		this.columns.add(column);
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public String getUpdateRule() {
		return updateRule;
	}

	/**
	 * @param updateRule
	 */
	public void setUpdateRule(String updateRule) {
		this.updateRule = updateRule;
	}

	/**
	 * @return
	 */
	public String getDeleteRule() {
		return deleteRule;
	}

	/**
	 * @param deleteRule
	 */
	public void setDeleteRule(String deleteRule) {
		this.deleteRule = deleteRule;
	}

}
