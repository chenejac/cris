package rs.ac.uns.ftn.informatika.bibliography.backup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;
import rs.ac.uns.ftn.informatika.bibliography.utils.CroSerUtils;

public class RecoveryApp {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out
					.println("import: <backupPath> <luceneIndexPath>");
			return;
		}
		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.backup.connection");
		String hostname = rb.getString("hostname");
		String port = rb.getString("port");
		String schema = rb.getString("schema");
		String connectionParameters = rb.getString("connectionParameters");
		String username = rb.getString("username");
		String password = rb.getString("password");
		new CroSerUtils();
		CroSerUtils.dictionaryPath = rb.getString("dictionaryRoot");
		CroSerUtils.loadStemDictionary();
		
		
		String backupPath = args[0];
		String luceneIndexPath = args[1];
		Retriever.setIndexPath(luceneIndexPath);

		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
		conn.setAutoCommit(false);


		ImportTask importTask = new ImportTask(backupPath, conn);
		if(importTask.execute()){
			System.out.println("Data has been successfully imported!");
//			IndexTask indexTask = new IndexTask(luceneIndexPath, conn, false, false, true);
//			if(indexTask.execute()){
//				System.out.println("Indexing has been successfully finished!");
//				System.out.println("Recovering has been successfully finished!");
//				conn.commit();
//				return;
//			} else {
//				System.out.println("Indexing has NOT been successfully finished!");
//				System.out.println("Recovering has NOT been successfully finished!");
//				return;
//			}
		} else {
			System.out.println("Data has NOT been successfully imported!");
			System.out.println("Recovering has NOT been successfully finished!");
		}
		return;
	}

}

