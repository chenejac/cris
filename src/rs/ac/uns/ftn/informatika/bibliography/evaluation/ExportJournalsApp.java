package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;


/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ExportJournalsApp {

	public static void main(String[] args) throws Exception {
		Connection conn = null;
		String luceneIndexPath = "";
		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.connection");
		String hostname = rb.getString("hostname");
		String port = rb.getString("port");
		String schema = rb.getString("schema");
		String connectionParameters = rb.getString("connectionParameters");
		String username = rb.getString("username");
		String password = rb.getString("password");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
			conn.setAutoCommit(false);
		} catch (Exception e) {
		}
	
		
		luceneIndexPath = rb.getString("luceneIndex");
		Retriever.setIndexPath(luceneIndexPath);

		ExportJournalsTask task = new ExportJournalsTask(conn, new PrintWriter(new File("/home/sinisa/JavaApp/exportFolder/casopisiVrednovanje2.csv"), "UTF8"));// new PrintWriter(System.out, true));
		if(task.execute())
			System.out.println("Export of journals done!");
		else {
			System.out.println("Export of journals failed!");
		}
	}

}
