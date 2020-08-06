package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.File;
import java.io.PrintWriter;

import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;


/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ExportConferencesApp {

	public static void main(String[] args) throws Exception {
		String luceneIndexPath = "";
		if (args.length != 1) {
			System.out.println("export conferences <lucene index path>");
			return;
		}
		
		luceneIndexPath = args[0];
		Retriever.setIndexPath(luceneIndexPath);
		
		ExportConferencesTask task = new ExportConferencesTask(new PrintWriter(new File("D:/skupoviVrednovanje.csv"), "UTF8"));// new PrintWriter(System.out, true));
		if(task.execute())
			System.out.println("Export of conferences done!");
		else {
			System.out.println("Export of conferences failed!");
		}
	}

}
