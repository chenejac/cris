package rs.ac.uns.ftn.informatika.bibliography.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class for persist and retrieve data about number of records in tables from
 * database.
 * 
 * @author chenejac@uns.ac.rs
 */
public class CounterDB {

	/**
	 * Return max_id+1.
	 * 
	 * @param conn
	 *            Database connection
	 * @param counter
	 *            id name
	 * @return max_id+1 if successful else -1
	 */
	public static int getNewId(Connection conn, String counter) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select COUNTERVALUE from COUNTER where COUNTERNAME like '"
							+ counter + "' FOR UPDATE");
			int recID = 0;
			if (rset.next()) {
				recID = rset.getInt(1) + 1;
				stmt
						.executeUpdate("update COUNTER set COUNTERVALUE=COUNTERVALUE+1 where COUNTERNAME like '"
								+ counter + "'");
			}
			rset.close();
			stmt.close();
			return recID;
		} catch (Exception ex) {
			log.fatal("Cannot create new id, counter name = " + counter);
			log.fatal(ex);
			return -1;
		}
	}

	private static Log log = LogFactory.getLog(CounterDB.class.getName());
}
