package rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel;

import java.sql.Types;

/**
 * DTO class which presents a table column.
 * 
 * @author "chenejac@uns.ac.rs"
 *
 */
public class Column {

	private String name;
	private int type;
	private int size;
	private int decimals;
	private int nullable;
	private int position;
	private String autoIncrement = "";

	/**
	 * Default constructor
	 */
	public Column() {
	}

	/**
	 * @param name
	 */
	public Column(String name) {
		this.name = name;
	}
	
	/**
	 * @param name
	 * @param type
	 * @param size
	 * @param decimals
	 * @param nullable
	 * @param position
	 * @param autoIncrement
	 */
	public Column(String name, int type, int size, int decimals, int nullable,
			int position, String autoIncrement) {
		this.name = name;
		this.type = type;
		this.size = size;
		this.decimals = decimals;
		this.nullable = nullable;
		this.position = position;
		this.autoIncrement = autoIncrement;
	}

	/**
	 * @return
	 */
	public int getDecimals() {
		return decimals;
	}

	/**
	 * @return
	 */
	public String getAutoIncrement() {
		return autoIncrement;
	}

	/**
	 * @param autoIncrement
	 */
	public void setAutoIncrement(String autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	/**
	 * @param decimals
	 */
	public void setDecimals(int decimals) {
		this.decimals = decimals;
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
	public int getNullable() {
		return nullable;
	}

	/**
	 * @param nullable
	 */
	public void setNullable(int nullable) {
		this.nullable = nullable;
	}

	/**
	 * @return
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (autoIncrement.equalsIgnoreCase("YES")) {
			return String.format("  %1$-20s %2$-15s  %3$10s %4$10s", name,
					typeDesc(), nullDesc(), "AUTO_INCREMENT");
		} else {

			return String.format("  %1$-20s %2$-15s  %3$10s", name, typeDesc(),
					nullDesc());
		}
	}

	/**
	 * @return the SQL type
	 */
	public String typeDesc() {
		switch (type) {
			case Types.INTEGER:
				return "INTEGER";
			case Types.CHAR:
				return "CHAR(" + size + ")";
			case Types.VARCHAR:
				return "VARCHAR(" + size + ")";
			case Types.DECIMAL:
				return "DECIMAL(" + size + "," + decimals + ")";
			case Types.FLOAT:
				return "FLOAT";
			case Types.DOUBLE:
				return "DOUBLE";
			case Types.DATE:
				return "DATETIME";
			case Types.TIME:
				return "TIME";
			case Types.TIMESTAMP:
				return "DATETIME";
			case Types.BOOLEAN:
				return "BOOLEAN";
			case Types.VARBINARY:
				return "VARBINARY";
			case Types.LONGNVARCHAR:
				return "LONGVARTEXT";
			case Types.LONGVARBINARY:
				return "LONGVARBINARY";
			case Types.OTHER:
				return "OTHER";
			default:
				return "TEXT";
		}
	}

	/**
	 * @return if column value is not mandatory NULL else NOT NULL
	 */
	public String nullDesc() {
		return (nullable == 0) ? "NOT NULL" : "NULL";
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		Column col = (Column)arg0;
		return this.name.equals(col.name);
	}
	
	
}
