package rs.ac.uns.ftn.informatika.bibliography.textsrv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* This java program is used to read the data from a Excel file and display them
* on the console output.
*/

public class ImportRules{
	
	
	
	public static void importFromTxt (String rulesPath){
		InputStream inputStream = null;
		BufferedReader br = null;
		try{
			InputStreamReader ir=new InputStreamReader(new FileInputStream(rulesPath),"UTF-8");
			br=new BufferedReader(ir);
		}catch (FileNotFoundException e){
			System.out.println ("File not found in the specified path.");
			e.printStackTrace ();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String temp;
		try {
			temp = br.readLine();
		
		Set<String> all = new HashSet<String>();
		HashMap<String, String> withoutAccents = new HashMap<String, String>();
		while(temp != null){
			if(! temp.startsWith("#")){
				temp = temp.substring(2);
				temp = temp.replace("š", "{sx}").replace("č", "{cx}").replace("ć", "{cy}").replace("đ", "{dx}").replace("ž", "{zx}");
				String[] param = temp.split(" ");
				List<String> prefixList  = new ArrayList<String>();
				if(param[0].trim().length() != 0){
					String[] prefixParam = param[0].split("[\\(\\)]");
					String suffix = "";
					if(prefixParam[0].length() == 0)
						prefixParam[0] = prefixParam[1]; 
					if ((prefixParam.length > 2) && (prefixParam[2].trim().length() > 0))
						suffix = prefixParam[2].trim();
					for (String prefix : prefixParam[0].split("\\|")) {
						prefixList.add(prefix + suffix);
					}
				} else {
					prefixList.add("");
				}
				List<String> suffixList  = new ArrayList<String>();
				if(param[1].trim().length() != 0){
					for (String suffix : param[1].split("\\|")) {
						suffixList.add(suffix);
					}
				}
				
				
				for (String prefix : prefixList) {
					boolean addAdjacent = false;
					for (String suffix : suffixList) {
						if(! all.contains("'" + prefix + suffix + "'")){
							if(withoutAccents.containsKey("'" + prefix + suffix + "'")){
								withoutAccents.remove("'" + prefix + suffix + "'");
							}
							System.out.println("'" + prefix + suffix + "'");
							all.add("'" + prefix + suffix + "'");
							addAdjacent = true;
							if(("'" + prefix + suffix + "'").contains("{")){
								String prefixWithoutAccents =  ("'" + prefix + suffix + "'").replace("{sx}", "s").replace("{cx}", "c").replace("{cy}", "c").replace("{dx}", "d").replace("{zx}", "z");
								if((! all.contains(prefixWithoutAccents)) && (! withoutAccents.containsKey(prefixWithoutAccents))){
									withoutAccents.put(prefixWithoutAccents, prefixWithoutAccents + "(R2 <-'" + prefix + "')");
								}
							}
						} 
					}
					if(addAdjacent)
						System.out.println("(<-'" + prefix + "')");
				}
				
			}
			temp = br.readLine();
		}
		
		System.out.println("###################################");
		for (String string : withoutAccents.values()) {
			System.out.println(string);
		}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void main (String[] args){
		
		String  rulesPath    = "D:/rules.txt";
		ImportRules.importFromTxt(rulesPath);

	}
}		