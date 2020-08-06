package rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.ControlField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Leader;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Subfield;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * Default prefix handler.
 * 
 * @author mbranko@uns.ac.rs
 */
public class DefaultPrefixHandler implements PrefixHandler {

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixHandler#getControlFieldValue(java.lang.String,
	 *      rs.ac.uns.ftn.informatika.bibliography.marc21.records.ControlField)
	 */
	@Override
	public String getControlFieldValue(String prefix,
			ControlField controlField) {
		String retVal = "";
		try {
			int start = -1, length = -1;
			start = Integer.parseInt(prefix.substring(3, 5));
			length = Integer.parseInt(prefix.substring(5, 6));
			retVal = controlField.getContent().substring(start, length);
		} catch (Exception e) {
		}
		return retVal;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixHandler#getDataSubfieldsValues(java.lang.String,
	 *      rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField)
	 */
	@Override
	public List<String> getDataSubfieldsValues(String prefix,
			DataField dataField) {
		List<String> retVal = new ArrayList<String>();
		String[] temp = prefix.split("\\|");
		String separator = null;
		String lang = null;
		int ordNum = 0; 
		try {
			if(temp.length == 2){
				String condition = temp[1];
				if("before".equalsIgnoreCase(condition.split("=")[0])){
					separator = condition.split("=")[1];
					ordNum = 1;
				} else if ("after".equalsIgnoreCase(condition.split("=")[0])) {
							separator = condition.split("=")[1];
							ordNum = 2;
					} else if ("lang".equalsIgnoreCase(condition.split("=")[0])) {
						lang = condition.split("=")[1];
						}else if(!(dataField.getSubfield(condition.charAt(0)).getContent().startsWith(condition.split("=")[1]))) 
							return retVal;
			}		
			char subfield = prefix.charAt(3);
			for (Subfield sf : dataField.getSubfields(subfield)){
				if((lang == null) || (lang.equals(getLanguage(sf)))){
					String content = sf.getContent();
					if(separator != null){
						String tempContent = content;
						for (int i = 0; i < ordNum; i++) {
							content = Sanitizer.nextPar(tempContent, separator.charAt(0));
							if(i+1 == ordNum)
								break;
							else
								tempContent = Sanitizer.remPar(tempContent, separator.charAt(0));
						}
					} 
					retVal.add(content);
				}
			}
			for (DataField df : getTranslations(dataField)){
				for (Subfield sf : df.getSubfields(subfield)){
					if((lang == null) || (lang.equals(getLanguage(sf)))){
						String content = sf.getContent().substring(6);
						if(separator != null){
							String tempContent = content;
							for (int i = 0; i < ordNum; i++) {
								content = Sanitizer.nextPar(tempContent, separator.charAt(0));
								if(i+1 == ordNum)
									break;
								else 
									tempContent = Sanitizer.remPar(tempContent, separator.charAt(0));
							}
						}
						retVal.add(content);
					}
				}
			}
		} catch (Exception e) {

		}
		return retVal;
	}

	/**
	 * @param dataField
	 * 			we need translations of this dataField subfields
	 * @return the list of data fields  880 which contain translations of provided dataField
	 */
	private List<DataField> getTranslations(DataField dataField) {
		List<DataField> retVal = new ArrayList<DataField>();
		try{
			Subfield sub6 = dataField.getSubfield('6');
			if(sub6 == null)
				return retVal;
			String content = sub6.getContent();
			String ordNum = content.split("\\-")[1];
			for (DataField df : dataField.getOwner().getDataFields("880")) {
				if(df.getSubfield('6').getContent().equals(dataField.getName()+"-"+ordNum))
					retVal.add(df);
			}
		} catch (Exception e) {
		}
		return retVal;
	}
	
	/**
	 * @param dataField
	 * 			we need language of this dataField subfields
	 * @return the language
	 */
	private String getLanguage(Subfield subfield) {
		String retVal = "srp";
		try{
			if(! "880".equals(subfield.getOwner().getName())){
				try{
					retVal = subfield.getOwner().getOwner().getDataField("040").getSubfield('b').getContent();
				} catch (Exception ex){
					retVal = subfield.getOwner().getOwner().getControlField("008").getCharPositions(35, 3);	
				}
			} else {
				String content = subfield.getContent();
				retVal = content.split("\\-")[1].substring(0, 3);
			}
		} catch (Exception e) {
			System.out.println("Language has not been specified: " + subfield.getOwner().getOwner());
		}
		return retVal;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixHandler#getLeaderValue(java.lang.String,
	 *      rs.ac.uns.ftn.informatika.bibliography.marc21.records.Leader)
	 */
	@Override
	public String getLeaderValue(String prefix, Leader leader) {
		String retVal = "";
		try {
			int start = -1, length = -1;
			start = Integer.parseInt(prefix.substring(3, 4));
			length = Integer.parseInt(prefix.substring(5, 5));
			retVal = leader.getContent().substring(start, length);
		} catch (Exception e) {

		}
		return retVal;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixHandler#getRecordClassValue(java.lang.String, rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification)
	 */
	@Override
	public String getRecordClassValue(String prefix, Classification classification) {
		String retVal = "";
		String[] temp = prefix.split("\\|");
		try {
			if(temp.length == 2){
				String condition = temp[1];
				if(condition.equalsIgnoreCase(classification.getCfClassSchemeId()))
					retVal = classification.getCfClassId();
			} else {
				retVal = classification.getCfClassId();
			}
		} catch (Exception e) {

		}
		return retVal;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixHandler#getRecordKeywordsValue(java.lang.String, rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordKeywords)
	 */
	@Override
	public String getRecordKeywordsValue(String prefix,
			MultilingualContentDTO recordKeywords) {
		String retVal = "";
		try {
			retVal = recordKeywords.getContent();
		} catch (Exception e) {

		}
		return retVal;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixHandler#getRecordRecordValue(java.lang.String, rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord)
	 */
	@Override
	public String getRecordRecordValue(String prefix, RecordRecord recordRecord) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
