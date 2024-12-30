package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.File;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;

import static rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.SamovrednovanjeUtils.username;


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

		ExportPDFDissertationsTask task = new ExportPDFDissertationsTask(new File("E:/diss2024"), 2024, setupJsch());// new PrintWriter(System.out, true));
		if(task.execute())
			System.out.println("Export of dissertations done!");
		else {
			System.out.println("Export of dissertations failed!");
		}
	}

	private static ChannelSftp setupJsch() throws JSchException {
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");


		JSch jsch = new JSch();
		jsch.setKnownHosts("c:/Users/dragan/.ssh/known_hosts");
		Session jschSession = jsch.getSession(username, remoteHost, 22);
		jschSession.setConfig(config);
		jschSession.setPassword(password);
		jschSession.connect();
		return (ChannelSftp) jschSession.openChannel("sftp");
	}

	private static String remoteHost = "XXXX.uns.ac.rs";
	private static String username = "XXXXX";
	private static String password = "XXXXXX";

}
