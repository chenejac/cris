package rs.ac.uns.ftn.informatika.bibliography.backup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class BackupApp {

	public static void main(String[] args) throws Exception {
		String fileName = "";
		if (args.length != 1) {
			System.out.println("backup: <path>");
			return;
		}
		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.backup.connection");
		String hostname = rb.getString("hostname");
		String port = rb.getString("port");
		String schema = rb.getString("schema");
		String connectionParameters = rb.getString("connectionParameters");
		String username = rb.getString("username");
		String password = rb.getString("password");
		
		fileName = args[0];

		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
		conn.setAutoCommit(false);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		String date = sdf.format(new Date());
		fileName = fileName + "backup-" + date + ".zip";
		BackupTask task = new BackupTask(fileName, conn);
		task.execute();
		System.out.println("Backup has been successfully created!");
	}

}
