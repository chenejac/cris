package rs.ac.uns.ftn.informatika.bibliography.backup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel.Column;
import rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel.ForeignKey;
import rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel.Index;
import rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel.Model;
import rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel.ModelFactory;
import rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel.PrimaryKey;
import rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel.Table;


public class ImportTask implements Task {

	public ImportTask(String fileName, Connection conn) {
		this.fileName = fileName;
		this.conn = conn;
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute() {
		try {
			Model model = makeModel();
			List<Table> tables = model.getHierarchicalTables();
			dropTable(tables);
			createTable(tables);
			createIndex(tables);
			createFK(tables);
			for (Table table : tables) {
				ZipFile zis = new ZipFile(fileName);
				Enumeration entries = zis.entries();
				while (entries.hasMoreElements()) {
					ZipEntry ze = (ZipEntry) entries.nextElement();
					if (!ze.getName().equals(table.getName()+".tbl"))
						continue;
					createSQL(table, ze, zis, conn);
				}
				zis.close();
			}
			log.info("Data has been successfully imported!");
			return true;
		} catch (FileNotFoundException e) {
			log.error(e);
			return false;
		} catch (IOException e) {
			log.error(e);
			return false;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	// metod raspakuje bekap fajl i pravi objektni model baze
	@SuppressWarnings("rawtypes")
	private Model makeModel() {
		Model model = null;
		try {
			ZipFile zis = new ZipFile(fileName);
			Enumeration entries = zis.entries();
			while (entries.hasMoreElements()) {
				ZipEntry ze = (ZipEntry) entries.nextElement();
				if (!ze.getName().equals("database-model.xml"))
					continue;
				model = ModelFactory.createModel(zis.getInputStream(ze));
			}
			zis.close();
			return model;
		} catch (FileNotFoundException e) {
			log.error(e);
			return null;
		} catch (IOException e) {
			log.error(e);
			return null;
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	private void createSQL(Table t, ZipEntry ze, ZipFile zis, Connection conn) {
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(zis
					.getInputStream(ze), "UTF8"));
			String insertColumn = getColumnsName(t);
			String line;
			String insert = "INSERT INTO " + t.getName() + "(" + insertColumn
					+ ") VALUES (" + makeQuestion(t.getColumns().size()) + ")";
			PreparedStatement insertStatement = conn.prepareStatement(insert);
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("<sep>", -1);

				for (int i = 0; i < parts.length - 1; i++) {
					if (parts[i].equals("")) {
						insertStatement.setObject(i + 1, null);
					} else {
						insertStatement.setObject(i + 1, parts[i].trim().replace("<backslashr>", "\r").replace("<backslashn>", "\n"));
					}
				}
				try {
					insertStatement.execute();
					count++;
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(),
							"GRESKA", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
			conn.commit();
			insertStatement.close();
			br.close();
		} catch (Exception e) {
			log.error(e);
		}

	}

	private void dropTable(List<Table> tables) {
		String dropTable = "drop table if exists ";
		Statement stat = getStatement();
		try {
			for (int i = tables.size() - 1; i >= 0; i--) {
				Table t = tables.get(i);
				stat.execute(dropTable + t.getName());
			}
			conn.commit();
			stat.close();
		} catch (Exception e) {
			log.error(e);
		}
	}

	private void createTable(List<Table> tables) {
		String createTable;
		Column column;
		Statement stat = getStatement();
		try {
			for (int i = 0; i < tables.size(); i++) {
				createTable = "CREATE TABLE ";
				Table t = tables.get(i);
				PrimaryKey primarykey = t.getPrimaryKey();
				List<Column> columnsPrim = primarykey.getColumns();
				createTable = createTable + t.getName() + "(";
				List<Column> columns = t.getColumns();
				for (int j = 0; j < columns.size() - 1; j++) {
					column = columns.get(j);
					createTable = createTable + " " + column + ", ";
				}
				createTable = createTable + " "
						+ columns.get(columns.size() - 1) + " ";
				createTable = createTable + ", PRIMARY KEY ( ";
				for (int j = 0; j < columnsPrim.size() - 1; j++) {
					createTable = createTable + columnsPrim.get(j).getName()
							+ " ,";
				}
				createTable = createTable
						+ columnsPrim.get(columnsPrim.size() - 1).getName()
						+ " ) ";
				createTable = createTable + " )";
				stat.execute(createTable);
			}
			conn.commit();
			stat.close();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	private void createIndex(List<Table> tables) {
		String createIndex;
		Statement stat = getStatement();
		try {
			for (int i = 0; i < tables.size(); i++) {
				Table t = tables.get(i);
				List<Index> indexes = t.getIndexes();
				for (int j = 0; j < indexes.size(); j++) {
					createIndex = "CREATE INDEX ";
					List<Column> indexColumns = indexes.get(j).getColumns();
					createIndex = createIndex + indexes.get(j).getName()
							+ " ON " + t.getName() + " ( ";
					for (int k = 0; k < indexColumns.size() - 1; k++) {
						createIndex = createIndex + indexColumns.get(k).getName()
								+ " ,";
					}
					createIndex = createIndex
							+ indexColumns.get(indexColumns.size() - 1).getName()
							+ " )";

					stat.execute(createIndex);
				}
				conn.commit();
			}
			stat.close();
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	private void createFK(List<Table> tables) {
		Statement stat = getStatement();
		try {
			for (int i = 0; i < tables.size(); i++) {
				Table t = tables.get(i);
				List<ForeignKey> foreignkeys = t.getForeignKeys();
				for (int j = 0; j < foreignkeys.size(); j++) {
					String alterTable = "ALTER TABLE ";
					List<Column> fkcolumns = foreignkeys.get(j).getFkColumns();
					List<Column> pkcolumns = foreignkeys.get(j).getPkColumns();
					
					alterTable = alterTable + t.getName() + " ADD CONSTRAINT "
							+ foreignkeys.get(j).getName() + " FOREIGN KEY (";
					for (int k = 0; k < fkcolumns.size() - 1; k++) {
						alterTable = alterTable + fkcolumns.get(k).getName()
								+ " ,";
					}
					alterTable = alterTable
							+ fkcolumns.get(fkcolumns.size() - 1).getName()
							+ " )";
					alterTable = alterTable + " REFERENCES "
							+ foreignkeys.get(j).getPkTable().getName() + " (";
					for (int k = 0; k < pkcolumns.size() - 1; k++) {
						alterTable = alterTable + pkcolumns.get(k).getName()
								+ " ,";
					}
					alterTable = alterTable
							+ pkcolumns.get(pkcolumns.size() - 1).getName()
							+ " )";
					String ondelete = foreignkeys.get(j).getPkTable().getPrimaryKey().getDeleteRule();
					String onupdate = foreignkeys.get(j).getPkTable().getPrimaryKey().getUpdateRule();
					
					if (ondelete != null && onupdate != null) {
						alterTable = alterTable + " ON DELETE " + ondelete
								+ " ON UPDATE " + onupdate;
					} else if (ondelete != null && onupdate == null) {
						alterTable = alterTable + " ON DELETE " + ondelete;
					} else if (ondelete == null && onupdate != null) {
						alterTable = alterTable + " ON UPDATE " + onupdate;
					}
					stat.execute(alterTable);
				}
				conn.commit();
			}
			stat.close();
		} catch (Exception e) {
			log.error(e);
		}
	}


	private static String getColumnsName(Table t) {
		List<Column> columns = t.getColumns();
		String insertColumn = "";
		for (int i = 0; i < columns.size() - 1; i++) {
			insertColumn = insertColumn + columns.get(i).getName() + ",";
		}
		insertColumn = insertColumn + columns.get(columns.size() - 1).getName();
		return insertColumn;
	}

	private static String makeQuestion(int number) {
		String questions = "";
		for (int i = 0; i < number - 1; i++) {
			questions = questions + "?,";
		}
		questions = questions + "?";
		return questions;
	}

	private Statement getStatement() {
		try {
			return conn.createStatement();
		} catch (SQLException e) {
			return null;
		}
	}

	
	private String fileName;
	private int count = 0;
	private Connection conn;
	
	private static Log log = LogFactory.getLog(ImportTask.class.getName());

}
