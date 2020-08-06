/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.knr;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ImportStudyFinalDocumentFromKNRApp {

	public static void main(String[] args) throws Exception {
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/PROCESSED?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8", "root", "root");
		
		ImportStudyFinalDocumentFromKNRTask  task = new ImportStudyFinalDocumentFromKNRTask(conn);
		if(task.execute())
			System.out.println("Successfully finished!!!");
	}
}
