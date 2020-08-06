package rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO class which presents a table foreign key.
 * 
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ForeignKey {

	private String name;
	private Table pkTable;
	private Table fkTable;
	private List<Column> fkColumns;
	private List<Column> pkColumns;

	/**
	 * Default constructor
	 */
	public ForeignKey() {
		fkColumns = new ArrayList<Column>();
		pkColumns = new ArrayList<Column>();
	}

	/**
	 * @param name
	 */
	public ForeignKey(String name) {
		this();
		this.name = name;
		fkColumns = new ArrayList<Column>();
		pkColumns = new ArrayList<Column>();
	}

	/**
	 * @return  
	 */
	public List<Column> getFkColumns() {
		return fkColumns;
	}

	/**
	 * @param fkColumns
	 */
	public void setFkColumns(List<Column> fkColumns) {
		this.fkColumns = fkColumns;
	}

	/**
	 * @param fkColumn
	 */
	public void addFkColumn(Column fkColumn) {
		this.fkColumns.add(fkColumn);
	}

	/**
	 * @return 
	 */
	public Table getFkTable() {
		return fkTable;
	}

	/**
	 * @param fkTable
	 */
	public void setFkTable(Table fkTable) {
		this.fkTable = fkTable;
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
	public List<Column> getPkColumns() {
		return pkColumns;
	}

	/**
	 * @param pkColumns
	 */
	public void setPkColumns(List<Column> pkColumns) {
		this.pkColumns = pkColumns;
	}

	/**
	 * @param pkColumn
	 */
	public void addPkColumn(Column pkColumn) {
		this.pkColumns.add(pkColumn);
	}

	/**
	 * @return
	 */
	public Table getPkTable() {
		return pkTable;
	}

	/**
	 * @param pkTable
	 */
	public void setPkTable(Table pkTable) {
		this.pkTable = pkTable;
	}
}
