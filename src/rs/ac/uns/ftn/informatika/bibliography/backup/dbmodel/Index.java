package rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel;

import java.util.ArrayList;
import java.util.List;


/**
 * DTO class which presents a table index.
 * 
 * @author "chenejac@uns.ac.rs"
 *
 */
public class Index {

	private String name;
	private Table table;
	private boolean nonUnique;
	private List<Column> columns; 
	
	/**
	 * Default constructor
	 */
	public Index() {
		columns =  new ArrayList<Column>(); 
	}
	
	/**
	 * @param name
	 */
	public Index(String name) {
		this();
		this.name = name;
		columns =  new ArrayList<Column>(); 
	}

	/**
	 * @param name
	 * @param table
	 * @param nonUnique
	 */
	public Index(String name, Table table, boolean nonUnique) {
		this();
		this.name = name;
		this.table = table;
		this.nonUnique = nonUnique;
		columns =  new ArrayList<Column>(); 
	}

	
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param column
	 */
	public void setName(String column) {
		this.name = column;
	}

	
	/**
	 * @return
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * @param table
	 */
	public void setTable(Table table) {
		this.table = table;
	}
	
	/**
	 * @return the nonUnique
	 */
	public boolean isNonUnique() {
		return nonUnique;
	}

	/**
	 * @param nonUnique the nonUnique to set
	 */
	public void setNonUnique(boolean nonUnique) {
		this.nonUnique = nonUnique;
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		Index index = (Index)arg0;
		return this.name.equals(index.name);
	}
}
