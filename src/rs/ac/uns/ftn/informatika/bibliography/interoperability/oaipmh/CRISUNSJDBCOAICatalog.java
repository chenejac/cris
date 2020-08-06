/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.oaipmh;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import ORG.oclc.oai.server.catalog.ExtendedJDBCOAICatalog;

/**
 * @author Dragan Ivanovic
 *
 */
public class CRISUNSJDBCOAICatalog extends ExtendedJDBCOAICatalog {

	private String identifierLabel;
	
	public CRISUNSJDBCOAICatalog(Properties properties) throws IOException {
		super(properties);
		identifierLabel = properties.getProperty("JDBCRecordFactory.identifierLabel");
        if (identifierLabel == null) {
            throw new IllegalArgumentException("JDBCRecordFactory.identifierLabel is missing from the properties file");
        } 
	}

	/**
	 * @see ORG.oclc.oai.server.catalog.ExtendedJDBCOAICatalog#extendItem(java.sql.Connection, java.util.HashMap)
	 */
	@Override
	protected void extendItem(Connection con, HashMap nativeItem) {
		super.extendItem(con, nativeItem);
		RecordDB recordDB = new RecordDB();
		Record record = recordDB.getRecord(con, nativeItem.get(identifierLabel).toString());
		nativeItem.put("CRISUNSRECORD", record);
	}

	/* (non-Javadoc)
	 * @see ORG.oclc.oai.server.catalog.ExtendedJDBCOAICatalog#getNewConnection()
	 */
	@Override
	protected Connection getNewConnection() throws SQLException {
		// TODO Auto-generated method stub
		return RecordDAO.dataSource.getConnection();
	}
	

}
