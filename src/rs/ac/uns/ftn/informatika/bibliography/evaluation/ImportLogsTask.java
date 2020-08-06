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

import rs.ac.uns.ftn.informatika.bibliography.dto.LogDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21RecordFactory;

/**
* This java program is used to read the data from a log file and store them
* to MySQL DBMS.
*/

public class ImportLogsTask{
	
	private Connection conn;
	
	private BufferedReader br;
	
	private int counter = 0;
	 
	
	public ImportLogsTask (Connection conn, FileInputStream stream){
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
				  if(strLine.contains("handleDownload")){
					  LogDTO log = new LogDTO();
					  log.setMethod("handleDownload");
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
					  log.setControlNumber(tokens[0].trim().substring(15));
					  log.setFileId(tokens[1].trim());
					  log.setFileName(tokens[2].trim());
					  String source = tokens[3].trim();
					  if(source.contains("(")){
						  log.setSource(source.substring(0, source.indexOf("(")));
						  log.setReferrer(source.substring(source.indexOf("(") + 1, source.indexOf(")")));
					  } else {
						  log.setSource(source);
					  }
					  String ipaddress = tokens[4].trim();
					  if(ipaddress.contains("(")){
						  log.setIpaddress(ipaddress.substring(0, ipaddress.indexOf("(")));
						  log.setProxy(ipaddress.substring(ipaddress.indexOf("(") + 1, ipaddress.indexOf(")")).trim().substring(8));
					  } else {
						  log.setIpaddress(ipaddress);
					  }
					  log.setRecordString(tokens[5].trim().substring(8));
					  store(log);
				  } else if (strLine.contains("loadRecord")){
					  LogDTO log = new LogDTO();
					  log.setMethod("loadRecord");
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
					  log.setControlNumber(tokens[0].trim().substring(18));
					  String source = tokens[1].trim();
					  if(source.contains("(")){
						  log.setSource(source.substring(0, source.indexOf("(")));
						  log.setReferrer(source.substring(source.indexOf("(") + 1, source.indexOf(")")));
					  } else {
						  log.setSource(source);
					  }
					  String ipaddress = tokens[2].trim();
					  if(ipaddress.contains("(")){
						  log.setIpaddress(ipaddress.substring(0, ipaddress.indexOf("(")));
						  log.setProxy(ipaddress.substring(ipaddress.indexOf("(") + 1, ipaddress.indexOf(")")).trim().substring(8));
					  } else {
						  log.setIpaddress(ipaddress);
					  }
					  log.setRecordString(tokens[3].trim().substring(8));
					  store(log);
				  }
			  }
			}
		}catch (Exception e){
			retVal = false;
			e.printStackTrace();
		}
		return retVal;
	}
	
	public boolean store(LogDTO logDTO){
		boolean retVal = true;
		try {
			if(logDTO.getMethod().equals("handleDownload")){
				PreparedStatement stmt = conn.prepareStatement("insert into DOWNLOADENTRY (CONTROLNUMBER, DATEANDTIME, EDAY, EMONTH, EYEAR, EHOURS, EMINUTES, ESECONDS, EDAYOFWEEK, FILEID, FILENAME, SOURCE, REFERRER, IPADDRESS, PROXY, DISSERTATION) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				stmt.setString(1, logDTO.getControlNumber());
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
				stmt.setString(10, logDTO.getFileId());
				stmt.setString(11, logDTO.getFileName());
				stmt.setString(12, logDTO.getSource());
				String referrer = logDTO.getReferrer();
				if (referrer == null)
					stmt.setNull(13, Types.VARCHAR);
				else
					stmt.setString(13, referrer);
				stmt.setString(14, logDTO.getIpaddress());
				String proxy = logDTO.getProxy();
				if (proxy == null)
					stmt.setNull(15, Types.VARCHAR);
				else
					stmt.setString(15, proxy);
				stmt.setString(16, logDTO.getRecordString());
				stmt.executeUpdate();
				stmt.close();
			} else {
				PreparedStatement stmt = conn.prepareStatement("insert into READMETADATAENTRY (CONTROLNUMBER, DATEANDTIME, EDAY, EMONTH, EYEAR, EHOURS, EMINUTES, ESECONDS, EDAYOFWEEK, SOURCE, REFERRER, IPADDRESS, PROXY, DISSERTATION) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				stmt.setString(1, logDTO.getControlNumber());
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
				stmt.setString(10, logDTO.getSource());
				String referrer = logDTO.getReferrer();
				if (referrer == null)
					stmt.setNull(11, Types.VARCHAR);
				else
					stmt.setString(11, referrer);
				stmt.setString(12, logDTO.getIpaddress());
				String proxy = logDTO.getProxy();
				if (proxy == null)
					stmt.setNull(13, Types.VARCHAR);
				else
					stmt.setString(13, referrer);
				stmt.setString(14, logDTO.getRecordString());
				stmt.executeUpdate();
				stmt.close();
			}
				
			
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