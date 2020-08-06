package rs.ac.uns.ftn.informatika.bibliography.backup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ImportApp {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("import: <backupPath>");
			return;
		}
		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.backup.connection");
		String hostname = rb.getString("hostname");
		String port = rb.getString("port");
		String schema = rb.getString("schema");
		String connectionParameters = rb.getString("connectionParameters");
		String username = rb.getString("username");
		String password = rb.getString("password");
		
		String backupPath = args[0];
		

		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema  + connectionParameters, username, password);
		conn.setAutoCommit(false);

		ImportTask task = new ImportTask(backupPath, conn);
		task.execute();
	}

}

