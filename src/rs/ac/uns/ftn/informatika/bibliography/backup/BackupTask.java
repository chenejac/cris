package rs.ac.uns.ftn.informatika.bibliography.backup;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel.Column;
import rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel.Model;
import rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel.ModelFactory;
import rs.ac.uns.ftn.informatika.bibliography.backup.dbmodel.Table;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class BackupTask implements Task {

	public BackupTask(String fileName, Connection conn) {
		this.fileName = fileName;
		this.conn = conn;
	}

	@Override
	public boolean execute() {
		try {
			Model model = ModelFactory.createModel(conn);
			zip = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(fileName)));
			//sinisa - integer argument from 0 through 9, where 0 indicates no compression and 9 indicates best compression – better compression results in slower performanc
			zip.setLevel(9);
			zip.putNextEntry(new ZipEntry("backup-date"));
			zip.write(sdf.format(new Date()).getBytes("UTF8"));
			zip.closeEntry();
			zip.putNextEntry(new ZipEntry("database-model.xml"));
			ModelFactory.saveModel(model, zip);
			zip.closeEntry();
			for (Table t : model.getHierarchicalTables()) {
				zip.putNextEntry(new ZipEntry(t.getName() + ".tbl"));
				saveTableData(conn, t);
				zip.closeEntry();
			}
			zip.close();
			log.info("Backup has been successfully created");
			return true;
		} catch (Exception ex) {
			log.error(ex);
			return false;
		} catch (Throwable e) {
			log.error(e);
			return false;
		}
	}

	private void saveTableData(Connection conn, Table t) throws SQLException,
			IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		out = new PrintWriter(new OutputStreamWriter(baos, "UTF8"), true);

		Statement stmt = conn.createStatement(
				java.sql.ResultSet.TYPE_FORWARD_ONLY,
				java.sql.ResultSet.CONCUR_READ_ONLY);
		stmt.setFetchSize(Integer.MIN_VALUE);

		ResultSet rset = stmt.executeQuery(getSelect(t));
		while (rset.next()) {
			int index = 1;
			for (Column c : t.getColumns()) {
				switch (c.getType()) {
					case Types.INTEGER:
						write(out, rset, rset.getInt(index++));
						break;
					case Types.CHAR:
						write(out, rset, rset.getString(index++));
						break;
					case Types.VARCHAR:
						write(out, rset, rset.getString(index++));
						break;
					case Types.DECIMAL:
						write(out, rset, rset.getBigDecimal(index++));
						break;
					case Types.FLOAT:
						write(out, rset, rset.getFloat(index++));
						break;
					case Types.DOUBLE:
						write(out, rset, rset.getDouble(index++));
						break;
					case Types.DATE:
						write(out, rset, rset.getDate(index++));
						break;
					case Types.TIME:
						write(out, rset, rset.getTime(index++));
						break;
					case Types.TIMESTAMP:
						write(out, rset, rset.getTimestamp(index++));
						break;
					case Types.BOOLEAN:
						write(out, rset, rset.getBoolean(index++));
						break;
					case Types.LONGVARCHAR:
						write(out, rset, rset.getString(index++));
						break;
					default:
						break;
				}
			}
			out.println();
			baos.writeTo(zip);
			baos.reset();
		}

		rset.close();
		stmt.close();
		baos.close();
	}

	private String getSelect(Table t) {
		StringBuffer retVal = new StringBuffer();
		retVal.append("SELECT ");
		int index = 0;
		for (Column c : t.getColumns()) {
			if (index++ > 0)
				retVal.append(", ");
			retVal.append(c.getName());
		}
		retVal.append(" FROM ");
		retVal.append(t.getName());
		return retVal.toString();
	}

	private void write(PrintWriter out, ResultSet rset, String s)
			throws SQLException {
		if (rset.wasNull())
			out.print("<sep>");
		else
			out.print(s.replace("\r", "<backslashr>").replace("\n", "<backslashn>").trim() + "<sep>");
	}

	private void write(PrintWriter out, ResultSet rset, int i)
			throws SQLException {
		if (rset.wasNull())
			out.print("<sep>");
		else
			out.print(Integer.toString(i) + "<sep>");

	}

	private void write(PrintWriter out, ResultSet rset, BigDecimal bd)
			throws SQLException {
		if (rset.wasNull())
			out.print("<sep>");
		else
			out.print(bd.toString() + "<sep>");

	}

	private void write(PrintWriter out, ResultSet rset, float f)
			throws SQLException {
		if (rset.wasNull())
			out.print("<sep>");
		else
			out.print(Float.toString(f) + "<sep>");

	}

	private void write(PrintWriter out, ResultSet rset, double d)
			throws SQLException {
		if (rset.wasNull())
			out.print("<sep>");
		else
			out.print(Double.toString(d) + "<sep>");

	}

	private void write(PrintWriter out, ResultSet rset, Date d)
			throws SQLException {
		if (rset.wasNull())
			out.print("<sep>");
		else
			out.print(sdf.format(d) + "<sep>");

	}

	private void write(PrintWriter out, ResultSet rset, boolean b)
			throws SQLException {
		if (rset.wasNull())
			out.print("<sep>");
		else
			out.print(Boolean.toString(b) + "<sep>");

	}

	private String fileName;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private PrintWriter out;
	private ZipOutputStream zip;
	private Connection conn;
	
	private static Log log = LogFactory.getLog(BackupTask.class.getName());

}
