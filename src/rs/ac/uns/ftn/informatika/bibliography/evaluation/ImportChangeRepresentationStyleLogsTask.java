package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import rs.ac.uns.ftn.informatika.bibliography.dto.LogChangeRepresentationStyleDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.LogFeedbackDTO;

/**
* This java program is used to read the data from a log file and store them
* to MySQL DBMS.
*/

public class ImportChangeRepresentationStyleLogsTask{
	
	private Connection conn;
	
	private BufferedReader br;
	
	private int counter = 0;
	 
	
	public ImportChangeRepresentationStyleLogsTask (Connection conn, FileInputStream stream){
		this.conn = conn;
		try {
			this.br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean execute(){
		boolean retVal = true;
		try{
			String strLine;
			/* read log line by line */
			while ((strLine = br.readLine()) != null)   {
			  /* parse strLine to obtain what you want */
			  if(!strLine.trim().equals("")){
				  LogChangeRepresentationStyleDTO log = new LogChangeRepresentationStyleDTO();
				  log.setMethod("representationStyle");
				  String dateString = strLine.substring(8, 28);
				  DateFormat formatter ; 
				  Date date ; 
				  formatter = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
				  date = (Date)formatter.parse(dateString);
				  Calendar cal=GregorianCalendar.getInstance();
				  cal.setTime(date);
				  log.setDate(cal);
				  String nextLine = br.readLine();
				  String[] tokens = nextLine.split("\\|");
				  log.setMiliseconds(Long.parseLong(tokens[1].trim().substring(13).trim()));
				  log.setSessionId(tokens[2].trim().substring(13).trim());
				  log.setUserId(tokens[3].trim().substring(7).trim());
				  String ipaddress = tokens[4].trim();
				  if(ipaddress.contains("(")){
					  log.setIpaddress(ipaddress.substring(0, ipaddress.indexOf("(")));
					  log.setProxy(ipaddress.substring(ipaddress.indexOf("(") + 1, ipaddress.indexOf(")")).trim().substring(8));
				  } else {
					  log.setIpaddress(ipaddress);
				  }
				  String[] location = tokens[5].trim().split(",");
				  log.setLocationCity(location[0].trim().substring(15).trim());
				  log.setLocationPostalCode(location[1].trim().substring(12).trim());
				  log.setLocationRegionName(location[2].trim().substring(11).trim());
				  log.setLocationCountryName(location[3].trim().substring(12).trim());
				  log.setLocationLatitude(Double.parseDouble(location[4].trim().substring(9).trim()));
				  log.setLocationLongitude(Double.parseDouble(location[5].trim().substring(10).trim()));
				  log.setUserAgent(tokens[6].trim().substring(20).trim());
				  log.setNewRepresentationStyle(tokens[7].trim().substring(25).trim());
				  
				  
				  store(log);
			  }
			}
			conn.commit();
		}catch (Exception e){
			retVal = false;
			e.printStackTrace();
		}
		return retVal;
	}
	
	public boolean store(LogChangeRepresentationStyleDTO logDTO){
		boolean retVal = true;
		try {
				PreparedStatement stmt = conn.prepareStatement("insert into REPRESENTATIONSTYLEENTRY (SESSIONID, DATEANDTIME, EDAY, EMONTH, EYEAR, EHOURS, EMINUTES, ESECONDS, EDAYOFWEEK, MILISECONDS, USERID, " +
						"LOCATIONCITY, LOCATIONPOSTALCODE, LOCATIONREGIONNAME, LOCATIONCOUNTRYNAME, LOCATIONLATITUDE, LOCATIONLONGITUDE, USERAGENT, NEWREPRESENTATIONSTYLE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				stmt.setString(1, logDTO.getSessionId());
				Calendar creationDate = logDTO.getDate();
				if (creationDate == null){
					stmt.setNull(2, Types.DATE);
				}
				else {
					stmt.setTimestamp(2, new java.sql.Timestamp(creationDate.getTimeInMillis()));
				}
				stmt.setInt(3, logDTO.getDay());
				stmt.setInt(4, logDTO.getMonth());
				stmt.setInt(5, logDTO.getYear());
				stmt.setInt(6, logDTO.getHour());
				stmt.setInt(7, logDTO.getMinute());
				stmt.setInt(8, logDTO.getSeconds());
				stmt.setInt(9, logDTO.getDayOfWeek());
				stmt.setLong(10, logDTO.getMiliseconds());
				stmt.setString(11, logDTO.getUserId());
				stmt.setString(12, logDTO.getLocationCity());
				stmt.setString(13, logDTO.getLocationPostalCode());
				stmt.setString(14, logDTO.getLocationRegionName());
				stmt.setString(15, logDTO.getLocationCountryName());
				stmt.setDouble(16, logDTO.getLocationLatitude());
				stmt.setDouble(17, logDTO.getLocationLongitude());
				stmt.setString(18, logDTO.getUserAgent());
				stmt.setString(19, logDTO.getNewRepresentationStyle());
				stmt.executeUpdate();
				stmt.close();
			
			
			counter++;
			if(counter%1000 == 0)
				conn.commit();
		} catch (SQLException ex) {
			retVal = false;
			ex.printStackTrace();
		}
		return retVal;
	}


}		