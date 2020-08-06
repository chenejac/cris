package rs.ac.uns.ftn.informatika.bibliography.wordcloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Georgia Kapitsaki
 *
 * Apr 12, 2017
 */

public class FileUtil {

	public static void write(File file, String sb) throws Exception {
		FileWriter writer = new FileWriter(file.getAbsoluteFile());
		writer.append(sb);
	}

	public static String read(File file) throws Exception {
		BufferedReader br = null;
		FileReader fr = null;
		StringBuilder builder = new StringBuilder();
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				builder.append(sCurrentLine + '\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return builder.toString();
	}
	
}
