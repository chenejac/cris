package rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO class which presents a table.
 * 
 * @author "chenejac@uns.ac.rs"
 *
 */
public class Table {

	private String name;
	private List<Column> columns;
	private PrimaryKey primaryKey;
	private List<ForeignKey> foreignKeys;
	private List<Index> indexes;

	/**
	 * Default constructor
	 */
	public Table() {
		columns = new ArrayList<Column>();
		primaryKey = new PrimaryKey();
		foreignKeys = new ArrayList<ForeignKey>();
		indexes = new ArrayList<Index>();
	}

	/**
	 * @param name
	 */
	public Table(String name) {
		this();
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
	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * @param primaryKey
	 */
	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * @return
	 */
	public List<ForeignKey> getForeignKeys() {
		return foreignKeys;
	}

	/**
	 * @param foreignKeys
	 */
	public void setForeignKeys(List<ForeignKey> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}

	/**
	 * @param foreignKey
	 */
	public void addForeignKey(ForeignKey foreignKey) {
		this.foreignKeys.add(foreignKey);
	}
	
	/**
	 * @return
	 */
	public List<Index> getIndexes() {
		return indexes;
	}

	/**
	 * @param indexes
	 */
	public void setIndexes(List<Index> indexes) {
		this.indexes = indexes;
	}

	/**
	 * @param index
	 */
	public void addIndex(Index index) {
		this.indexes.add(index);
	}

	/**
	 * @param name
	 * @return
	 */
	public Column getColumn(String name) {
		for (Column c : columns) {
			if (c.getName().equals(name))
				return c;
		}
		return null;
	}

	/**
	 * @param name
	 * @return
	 */
	public ForeignKey getForeignKey(String name) {
		for (ForeignKey k : foreignKeys) {
			if (k.getName().equals(name))
				return k;
		}
		return null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		retVal.append("TABLE: ");
		retVal.append(name);
		retVal.append("\n");
		for (Column c : columns) {
			retVal.append(c.toString());
			if (primaryKey.getColumns().contains(c))
				retVal.append(" <pk>");
			for (ForeignKey fk : foreignKeys) {
				if (fk.getFkColumns().contains(c))
					retVal.append(" <fk>");
			}
			for (Index index : indexes) {
				if (index.getName().equals(c))
					retVal.append(" <index>");
			}
			retVal.append("\n");
		}
		return retVal.toString();
	}
}
