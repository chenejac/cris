package rs.ac.uns.ftn.informatika.bibliography.interoperability.oaipmh;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;

import ORG.oclc.oai.server.catalog.JDBCRecordFactory;

public class CRISUNSJDBCRecordFactory extends JDBCRecordFactory{

	protected String keepDeletedRecord;
	protected String deletedLabel;
	protected String typeLabel;
	protected String granularity;
	
	public CRISUNSJDBCRecordFactory(Properties properties)
			throws IllegalArgumentException {
		super(properties);
		keepDeletedRecord = properties.getProperty("Identify.deletedRecord");
		if (keepDeletedRecord == null) {
		    throw new IllegalArgumentException("Identify.deletedRecord is missing from the properties file");
		}
		deletedLabel = properties.getProperty("CRISUNSJDBCRecordFactory.deletedLabel");
		if (deletedLabel == null) {
		    throw new IllegalArgumentException("CRISUNSJDBCRecordFactory.deletedLabel is missing from the properties file");
		}
		typeLabel = properties.getProperty("CRISUNSJDBCRecordFactory.typeLabel");
		granularity = properties.getProperty("AbstractCatalog.granularity");
	}

	/* (non-Javadoc)
	 * @see ORG.oclc.oai.server.catalog.ExtendedJDBCRecordFactory#isDeleted(java.lang.Object)
	 */
	@Override
	public boolean isDeleted(Object nativeItem) throws IllegalArgumentException {
		if(keepDeletedRecord.equals("persistent")){
			HashMap table = (HashMap)nativeItem;
		    Object deleted = table.get(deletedLabel);
	        if (deleted instanceof Integer) { 
	        	if(((Integer) deleted).intValue() == 100){
	        		return true;
	        	} 
	        }
		}
		return false;
	}
	
	@Override
	public String getOAIIdentifier(Object nativeItem)
	throws IllegalArgumentException {
		StringBuffer sb = new StringBuffer();
		sb.append("oai:");
		sb.append(repositoryIdentifier);
		sb.append(":");
		if(typeLabel != null){
			HashMap table = (HashMap)nativeItem;
			Object type = table.get(typeLabel);
		
		    String typeStr = type.toString();
			if("resPat".equals(typeStr))
				sb.append("Patents/");
			else if("resProd".equals(typeStr))
				sb.append("Products/");
			else if("resPubl".equals(typeStr))
				sb.append("Publications/");
			else if("event".equals(typeStr))
				sb.append("Events/");
			else if("pers".equals(typeStr))
				sb.append("Persons/");
			else if("orgUnit".equals(typeStr))
				sb.append("OrgUnits/");
		}
		sb.append(getLocalIdentifier(nativeItem));
		return sb.toString();
    }
	
	@Override
	public String fromOAIIdentifier(String oaiIdentifier) {
		StringTokenizer tokenizer = new StringTokenizer(oaiIdentifier, (typeLabel != null)?"/":":");
		try {
		    tokenizer.nextToken();
		    if(typeLabel == null){
		    	tokenizer.nextToken();
		    }
		    return tokenizer.nextToken();
		} catch (java.util.NoSuchElementException e) {
		    return null;
		}
	    }

	@Override
	public String getDatestamp(Object nativeItem) {
//	 	throws IllegalArgumentException  {
//	 	try {
		    HashMap table = (HashMap)nativeItem;
		    Object datestamp = table.get(datestampLabel);
	        StringBuffer sb = new StringBuffer();
            SimpleDateFormat formatter = (granularity == null || "YYYY-MM-DD".equals(granularity))?new SimpleDateFormat("yyyy-MM-dd"):new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            TimeZone tz = TimeZone.getTimeZone("UTC");
            formatter.setTimeZone(tz);
	        if (datestamp instanceof Date) {
	            sb.append(formatter.format(datestamp));
	            return sb.toString();
//	            return ((Date)datestamp).toString();
	        } else if (datestamp instanceof Timestamp) { 
	            sb.append(formatter.format(datestamp));
	            return sb.toString();
//	            return ((Timestamp)datestamp).toString();
	        } else {
	            throw new IllegalArgumentException("Unrecognized datestamp format: " + datestamp.getClass().getName());
	        }
	        
//	 	} catch (SQLException e) {
//	 	    e.printStackTrace();
//	 	    throw new IllegalArgumentException(e.getMessage());
//	 	}
	    }
	
}
