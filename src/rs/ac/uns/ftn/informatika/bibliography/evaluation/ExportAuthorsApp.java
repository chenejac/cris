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
public class ExportAuthorsApp {

	public static void main(String[] args) throws Exception {
		Connection conn = null;
		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.connection");
		String hostname = rb.getString("hostname");
		String port = rb.getString("port");
		String schema = rb.getString("schema");
		String connectionParameters = rb.getString("connectionParameters");
		String username = rb.getString("username");
		String password = rb.getString("password");
		String luceneIndex = rb.getString("luceneIndex");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
			conn.setAutoCommit(false);
		} catch (Exception e) {
		}
		Retriever.setIndexPath(luceneIndex);

		
		ExportAuthorsTask task = new ExportAuthorsTask(conn, new PrintWriter(new File("D:/autoriVrednovanjePMF.csv"), "UTF8"));// new PrintWriter(System.out, true));
		if(task.execute())
			System.out.println("Export of authors done!");
		else {
			System.out.println("Export of authors failed!");
		}
	}

}
