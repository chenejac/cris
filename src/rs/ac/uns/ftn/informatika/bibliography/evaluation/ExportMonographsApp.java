package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.File;
import java.io.PrintWriter;

import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;


/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ExportMonographsApp {

	public static void main(String[] args) throws Exception {
		String luceneIndexPath = "";
		if (args.length != 1) {
			System.out.println("export monographs <lucene index path>");
			return;
		}
		
		luceneIndexPath = args[0];
		Retriever.setIndexPath(luceneIndexPath);
		
		ExportMonographsTask task = new ExportMonographsTask(new PrintWriter(new File("D:/monografijeVrednovanje.csv"), "UTF8"));// new PrintWriter(System.out, true));
		if(task.execute())
			System.out.println("Export of monographs done!");
		else {
			System.out.println("Export of monographs failed!");
		}
	}

}
