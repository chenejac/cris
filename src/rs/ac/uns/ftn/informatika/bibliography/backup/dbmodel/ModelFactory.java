package rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.DecapitalizeNameMapper;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;

/**
 * Factory for creating model from file and for creating file from model.
 * 
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ModelFactory {

	/**
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static Model createModel(InputStream in) throws Exception {

		BeanReader reader = new BeanReader();
		reader.getXMLIntrospector().getConfiguration()
				.setAttributesForPrimitives(true);

		reader.getXMLIntrospector().getConfiguration().setAttributeNameMapper(
				new HyphenatedNameMapper());
		reader.getXMLIntrospector().getConfiguration().setElementNameMapper(
				new DecapitalizeNameMapper());
		reader.getBindingConfiguration().setMapIDs(true);
		reader.registerBeanClass(Model.class);
		Model m = (Model) reader.parse(in);
		return m;

	}

	/**
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static Model createModel(Connection conn) throws SQLException {
		Model model = new Model();
		DatabaseMetaData dbmd = conn.getMetaData();

		Map<String, Table> tableMap = new HashMap<String, Table>();
		ResultSet rset = dbmd.getTables(null, null, null, null);
		while (rset.next()) {
			String tableName = rset.getString("TABLE_NAME").toUpperCase();
			String tableType = rset.getString("TABLE_TYPE").toUpperCase();
			if (tableType.indexOf("VIEW") != -1)
				continue;
			if (tableType.indexOf("SYSTEM") != -1)
				continue;
			tableMap.put(tableName, new Table(tableName));
		}
		rset.close();
		rset = dbmd.getColumns(null, null, null, null);
		while (rset.next()) {
			String tableName = rset.getString("TABLE_NAME").toUpperCase();
			String columnName = rset.getString("COLUMN_NAME").toUpperCase();
			int dataType = rset.getInt("DATA_TYPE");
			int columnSize = rset.getInt("COLUMN_SIZE");
			int decimalDigits = rset.getInt("DECIMAL_DIGITS");
			int nullable = rset.getInt("NULLABLE");
			int position = rset.getInt("ORDINAL_POSITION");

			String autoincrement = rset. getString("IS_AUTOINCREMENT").toUpperCase();
			Table table = tableMap.get(tableName);
			if (table != null) {
				Column column = new Column(columnName, dataType, columnSize,
						decimalDigits, nullable, position, autoincrement);

				table.getColumns().add(column);
			}
		}
		rset.close();

		for (Table t : tableMap.values()) {
			Collections.sort(t.getColumns(), new ColumnOrderComparator());
		}

		for (Table t : tableMap.values()) {
			rset = dbmd.getPrimaryKeys(null, null, t.getName());
			while (rset.next()) {
				String columnName = rset.getString("COLUMN_NAME").toUpperCase();
				String pkName = rset.getString("PK_NAME").toUpperCase();
				t.getPrimaryKey().setName(pkName);
				Column c = t.getColumn(columnName);
				if (c != null)
					t.getPrimaryKey().getColumns().add(c);
			}
			rset.close();
			Collections.sort(t.getPrimaryKey().getColumns(),
					new PrimaryKeyOrderComparator());
		}

		for (Table t : tableMap.values()) {
			rset = dbmd.getExportedKeys(null, null, t.getName());
			while (rset.next()) {
				String pkTableName = rset.getString("PKTABLE_NAME").toUpperCase();
				String fkTableName = rset.getString("FKTABLE_NAME").toUpperCase();
				String pkColumnName = rset.getString("PKCOLUMN_NAME").toUpperCase();
				String fkColumnName = rset.getString("FKCOLUMN_NAME").toUpperCase();
				String fkName = rset.getString("FK_NAME").toUpperCase();
				String updateRule = rset.getString("UPDATE_RULE").toUpperCase();
				String deleteRule = rset.getString("DELETE_RULE").toUpperCase();
				Table pkTable = tableMap.get(pkTableName);
				pkTable.getPrimaryKey().setUpdateRule(
						getRestriction(updateRule));
				pkTable.getPrimaryKey().setDeleteRule(
						getRestriction(deleteRule));

				Table fkTable = tableMap.get(fkTableName);
				ForeignKey fk = fkTable.getForeignKey(fkName);
				if (fk == null) {
					fk = new ForeignKey(fkName);
					fkTable.getForeignKeys().add(fk);
				}
				if (pkTable != null && fkTable != null) {
					fk.setFkTable(fkTable);
					fk.setPkTable(pkTable);
					Column fkColumn = fkTable.getColumn(fkColumnName);
					Column pkColumn = pkTable.getColumn(pkColumnName);
					if (fkColumn != null && pkColumn != null) {
						fk.getFkColumns().add(fkColumn);
						Collections.sort(fk.getFkColumns(), new ColumnOrderComparator());
						fk.getPkColumns().add(pkColumn);
						Collections.sort(fk.getPkColumns(), new ColumnOrderComparator());
					}
				}
			}
			rset = dbmd.getIndexInfo(null, null, t.getName(), false, false);
			while (rset.next()) {
				String indexName = rset.getString("INDEX_NAME").toUpperCase();
				if(!(indexName.toLowerCase().equals("primary"))){
					Index index = null;
					if(t.getIndexes().contains(new Index(indexName))){
						index = t.getIndexes().get(t.getIndexes().indexOf(new Index(indexName)));
					} else { 
						boolean nonUnique = rset.getBoolean("NON_UNIQUE");
						index = new Index(indexName, t, nonUnique);
						t.addIndex(index);
					}
					String columnName = rset.getString("COLUMN_NAME").toUpperCase();
					Column column = new Column(columnName);
					index.addColumn(column);
				}
			}
			rset.close();
		}

		// create a hierarchical list of tables
		List<Table> temp = new ArrayList<Table>();
		temp.addAll(tableMap.values());
		do {
			for (Iterator<Table> i = temp.iterator(); i.hasNext();) {
				Table t = i.next();

				boolean hasParents = false;
				for (ForeignKey fk : t.getForeignKeys()) {
					if (temp.contains(fk.getPkTable()))
						hasParents = true;
				}
				if (!hasParents) {
					model.getHierarchicalTables().add(t); //
					i.remove();
				}

			}
		} while (temp.size() > 0);

		model.getAlphabeticalTables().addAll(model.getHierarchicalTables());
		Collections.sort(model.getAlphabeticalTables(),
				new TableAlphabeticalComparator());
		return model;
	}

	/**
	 * @param rest
	 * @return
	 */
	private static String getRestriction(String rest) {
		switch (Integer.parseInt(rest)) {
			case 0:
				return "CASCADE";

			case 1:
				return "RESTRICT";

			case 2:
				return "SET NULL";

			default:
				return "NO ACTION";

		}
	}

	/**
	 * @author "chenejac@uns.ac.rs"
	 *
	 */
	public static class ColumnOrderComparator implements Comparator<Column> {
		
		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Column c1, Column c2) {
			return c1.getPosition() - c2.getPosition();
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object o) {
			return (o instanceof ColumnOrderComparator);
		}
	}

	/**
	 * @author "chenejac@uns.ac.rs"
	 *
	 */
	public static class PrimaryKeyOrderComparator implements Comparator<Column> {
		
		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Column c1, Column c2) {
			return c1.getPosition() - c2.getPosition();
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object o) {
			return (o instanceof PrimaryKeyOrderComparator);
		}
	}

	/**
	 * @author "chenejac@uns.ac.rs"
	 *
	 */
	public static class TableAlphabeticalComparator implements
			Comparator<Table> {
		
		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Table t1, Table t2) {
			return t1.getName().compareTo(t2.getName());
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object o) {
			return (o instanceof TableAlphabeticalComparator);
		}
	}

	/**
	 * @param model
	 * @param out
	 * @throws Exception
	 */
	public static void saveModel(Model model, OutputStream out)
			throws Exception {
		BeanWriter writer = new BeanWriter(out);
		writer.getXMLIntrospector().getConfiguration()
				.setAttributesForPrimitives(true);
		writer.enablePrettyPrint();
		writer.getBindingConfiguration().setMapIDs(true);
		writer.getXMLIntrospector().getConfiguration().setAttributeNameMapper(
				new HyphenatedNameMapper());
		writer.getXMLIntrospector().getConfiguration().setElementNameMapper(
				new DecapitalizeNameMapper());

		writer.write(model);
	}

}
