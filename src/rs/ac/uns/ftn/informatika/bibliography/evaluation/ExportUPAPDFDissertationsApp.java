package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;

import java.io.File;
import java.io.PrintWriter;


/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ExportUPAPDFDissertationsApp {

	public static void main(String[] args) throws Exception {
		String luceneIndexPath = "";
		if (args.length != 1) {
			System.out.println("export monographs <lucene index path>");
			return;
		}

		luceneIndexPath = args[0];
		Retriever.setIndexPath(luceneIndexPath);

		ExportUPAPDFDissertationsTask task = new ExportUPAPDFDissertationsTask(new File("E:/dissUPA"), setupJsch());// new PrintWriter(System.out, true));
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

	private static String remoteHost = "cris.uns.ac.rs";
	private static String username = "root";
	private static String password = "besnatrikolena";

}
