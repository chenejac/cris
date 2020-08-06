package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.File;
import java.io.PrintWriter;

import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;


/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ReevaluateResultsApp {

	public static void main(String[] args) throws Exception {
		String luceneIndexPath = "";
		if (args.length != 1) {
			System.out.println("export conferences <lucene index path>");
			return;
		}
		
		luceneIndexPath = args[0];
		Retriever.setIndexPath(luceneIndexPath);
		
		ReevaluateResultsTask task = new ReevaluateResultsTask();
		if(task.execute())
			System.out.println("Reevaluation of results done!");
		else {
			System.out.println("Reevaluation of results failed!");
		}
	}

}
