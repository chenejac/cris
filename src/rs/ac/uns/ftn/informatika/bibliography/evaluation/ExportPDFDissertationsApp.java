package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.File;

import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;


/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ExportPDFDissertationsApp {

	public static void main(String[] args) throws Exception {
		String luceneIndexPath = "";
		if (args.length != 1) {
			System.out.println("export monographs <lucene index path>");
			return;
		}
		
		luceneIndexPath = args[0];
		Retriever.setIndexPath(luceneIndexPath);
		
		ExportPDFDissertationsTask task = new ExportPDFDissertationsTask(new File("E:/diss2019"), 2019);// new PrintWriter(System.out, true));
		if(task.execute())
			System.out.println("Export of dissertations done!");
		else {
			System.out.println("Export of dissertations failed!");
		}
	}

}
