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
import rs.ac.uns.ftn.informatika.bibliography.dto.LogFeedbackDTO;

/**
* This java program is used to read the data from a log file and store them
* to MySQL DBMS.
*/

public class ImportFeedbackLogsTask{
	
	private Connection conn;
	
	private BufferedReader br;
	
	private int counter = 0;
	 
	
	public ImportFeedbackLogsTask (Connection conn, FileInputStream stream){
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
				  LogFeedbackDTO log = new LogFeedbackDTO();
				  log.setMethod("feedback");
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
				  log.setUserId(tokens[2].trim().substring(7).trim());
				  log.setControlNumber(tokens[3].trim().substring(15).trim());
				  log.setSearchingMode(tokens[4].trim().substring(15).trim());
				  log.setPosition(Integer.parseInt(tokens[5].trim().substring(9).trim()));
				  log.setRelevanceScore(Double.parseDouble(tokens[6].trim().substring(16).trim()));
				  log.setRecommendationScore(Double.parseDouble(tokens[7].trim().substring(21).trim()));
				  log.setMixedScore(Double.parseDouble(tokens[8].trim().substring(12).trim()));
				  log.setFeedbackType(tokens[9].trim().substring(14).trim());
				  log.setEvaluation(tokens[10].trim().substring(11).trim());
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
	
	public boolean store(LogFeedbackDTO logDTO){
		boolean retVal = true;
		try {
				PreparedStatement stmt = conn.prepareStatement("insert into FEEDBACKENTRY (CONTROLNUMBER, DATEANDTIME, EDAY, EMONTH, EYEAR, EHOURS, EMINUTES, ESECONDS, EDAYOFWEEK, MILISECONDS, USERID, SEARCHINGMODE, POSITION, RELEVANCESCORE, RECOMMENDATIONSCORE, MIXEDSCORE, FEEDBACKTYPE, EVALUATION) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
				stmt.setLong(10, logDTO.getMiliseconds());
				stmt.setString(11, logDTO.getUserId());
				stmt.setString(12, logDTO.getSearchingMode());
				stmt.setInt(13, logDTO.getPosition());
				stmt.setDouble(14, logDTO.getRelevanceScore());
				stmt.setDouble(15, logDTO.getRecommendationScore());
				stmt.setDouble(16, logDTO.getMixedScore());
				stmt.setString(17, logDTO.getFeedbackType());
				stmt.setString(18, logDTO.getEvaluation());
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