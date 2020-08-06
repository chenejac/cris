package rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO class which presents a model of database, actually a list of tables.
 * 
 * @author "chenejac@uns.ac.rs"
 *
 */
public class Model {

	private List<Table> alphabeticalTables;
	private List<Table> hierarchicalTables;

	/**
	 * 
	 */
	public Model() {
		alphabeticalTables = new ArrayList<Table>();
		hierarchicalTables = new ArrayList<Table>();
	}

	/**
	 * @return
	 */
	public List<Table> getAlphabeticalTables() {
		return alphabeticalTables;
	}

	/**
	 * @param tables
	 */
	public void setAlphabeticalTables(List<Table> tables) {
		this.alphabeticalTables = tables;
	}

	/**
	 * @param table
	 */
	public void addAlphabeticalTable(Table table) {
		this.alphabeticalTables.add(table);
	}

	/**
	 * @return
	 */
	public List<Table> getHierarchicalTables() {
		return hierarchicalTables;
	}

	/**
	 * @param tables
	 */
	public void setHierarchicalTables(List<Table> tables) {
		this.hierarchicalTables = tables;
	}

	/**
	 * @param table
	 */
	public void addHierarchicalTable(Table table) {
		this.hierarchicalTables.add(table);
	}

	/**
	 * @param tableName
	 * @return
	 */
	public Table getTable(String tableName) {
		for (Table t : alphabeticalTables) {
			if (t.getName().equals(tableName))
				return t;
		}
		return null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		retVal.append("=== database model ===\n");
		for (Table t : hierarchicalTables)
			retVal.append(t.toString());
		return retVal.toString();
	}
}
