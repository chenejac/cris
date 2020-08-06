package rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;

/**
 * Represents a default prefix map.
 * 
 * @author mbranko@uns.ac.rs
 */
public class DefaultPrefixMap implements PrefixMap {

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixMap#getPrefixes(java.lang.String)
	 */
	@Override
	public List<String> getPrefixes(String type, String datasource) {
		List<String> list = prefixMap.get(type).get(datasource);
		if (list == null)
			return new ArrayList<String>();
		else
			return list;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixMap#getSubfields(java.lang.String)
	 */
	@Override
	public List<String> getDataSources(String type, String prefix) {
		List<String> list = dataSourceMap.get(type).get(prefix);
		if (list == null)
			return new ArrayList<String>();
		else
			return list;
	}

	/**
	 * Default constructor creates prefix maps and subfield maps for ALL mARC21Record
	 * types.
	 */
	public DefaultPrefixMap() {
		prefixMap = new HashMap<String, Map<String, List<String>>>();
		dataSourceMap = new HashMap<String, Map<String, List<String>>>();

		prefixMap.put(Types.INSTITUTION,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.INSTITUTION,
				new HashMap<String, List<String>>());

		addToMap(Types.INSTITUTION, "NA", "110a", null);
		addToMap(Types.INSTITUTION, "ALL", "110a", null);
		addToMap(Types.INSTITUTION, "PL", "110c", null);
		addToMap(Types.INSTITUTION, "ALL", "110c", null);
		addToMap(Types.INSTITUTION, "ALL", "372a", null);
		addToMap(Types.INSTITUTION, "ALL", "KWS", null);
		addToMap(Types.INSTITUTION, "INCN", "7100", null);
		addToMap(Types.INSTITUTION, "RA", "372a", null);
		addToMap(Types.INSTITUTION, "KW", "KWS", null);
		addToMap(Types.INSTITUTION, "RA", "CLS", ResearchAreaDTO.RESEARCH_AREA_SCHEMA);
		
		
		prefixMap.put(Types.ORGANIZATION_UNIT,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.ORGANIZATION_UNIT,
				new HashMap<String, List<String>>());

		addToMap(Types.ORGANIZATION_UNIT, "NA", "110a", null);
		addToMap(Types.ORGANIZATION_UNIT, "ALL", "110a", null);
		addToMap(Types.ORGANIZATION_UNIT, "PL", "110c", null);
		addToMap(Types.ORGANIZATION_UNIT, "ALL", "110c", null);
		addToMap(Types.ORGANIZATION_UNIT, "ALL", "372a", null);
		addToMap(Types.ORGANIZATION_UNIT, "ALL", "KWS", null);
		addToMap(Types.ORGANIZATION_UNIT, "INCN", "7100", null);
		addToMap(Types.ORGANIZATION_UNIT, "OUCN", "7100", null);
		addToMap(Types.ORGANIZATION_UNIT, "RA", "372a", null);
		addToMap(Types.ORGANIZATION_UNIT, "KW", "KWS", null);
		addToMap(Types.ORGANIZATION_UNIT, "RA", "CLS", ResearchAreaDTO.RESEARCH_AREA_SCHEMA);
		
		prefixMap.put(Types.AUTHOR,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.AUTHOR,
				new HashMap<String, List<String>>());

		addToMap(Types.AUTHOR, "AU", "100a", null);
		addToMap(Types.AUTHOR, "LN", "100a", "before=,");
		addToMap(Types.AUTHOR, "FN", "100a", "after=,");
		addToMap(Types.AUTHOR, "PO", "100c", null);
		addToMap(Types.AUTHOR, "YE", "100d", null);
		addToMap(Types.AUTHOR, "PL", "371b", null);
		addToMap(Types.AUTHOR, "AU", "400a", null);
		addToMap(Types.AUTHOR, "LN", "400a", "before=,");
		addToMap(Types.AUTHOR, "FN", "400a", "after=,");
		addToMap(Types.AUTHOR, "INCN", "7100", null);
		addToMap(Types.AUTHOR, "INCN", "3730", null);
		addToMap(Types.AUTHOR, "OUCN", "7100", null);
		addToMap(Types.AUTHOR, "ALL", "100a", null);
		addToMap(Types.AUTHOR, "ALL", "100c", null);
		addToMap(Types.AUTHOR, "ALL", "100d", null);
		addToMap(Types.AUTHOR, "ALL", "100g", null); // treba obrisati
		addToMap(Types.AUTHOR, "ALL", "400a", null);
		addToMap(Types.AUTHOR, "ALL", "372a", null);
		addToMap(Types.AUTHOR, "ALL", "KWS", null);
		addToMap(Types.AUTHOR, "RI", "372a", null);
		addToMap(Types.AUTHOR, "KW", "KWS", null);
		addToMap(Types.AUTHOR, "RA", "CLS", ResearchAreaDTO.RESEARCH_AREA_SCHEMA);

		prefixMap.put(Types.CONFERENCE,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.CONFERENCE,
				new HashMap<String, List<String>>());

		addToMap(Types.CONFERENCE, "NA", "111a", null);
		addToMap(Types.CONFERENCE, "ALL", "111a", null);
		addToMap(Types.CONFERENCE, "PL", "111c", null);
		addToMap(Types.CONFERENCE, "ALL", "111c", null);
		addToMap(Types.CONFERENCE, "YE", "111d", null);
		addToMap(Types.CONFERENCE, "ALL", "111d", null);
		addToMap(Types.CONFERENCE, "NO", "111n", null);
		addToMap(Types.CONFERENCE, "ALL", "111n", null);
		addToMap(Types.CONFERENCE, "ALL", "680i", null);
		addToMap(Types.CONFERENCE, "ALL", "KWS", null);
		addToMap(Types.CONFERENCE, "DS", "680i", null);
		addToMap(Types.CONFERENCE, "KW", "KWS", null);
		

		prefixMap.put(Types.PROCEEDINGS,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.PROCEEDINGS,
				new HashMap<String, List<String>>());

		addToMap(Types.PROCEEDINGS, "TI", "245a", null);
		addToMap(Types.PROCEEDINGS, "ALL", "245a", null);
		addToMap(Types.PROCEEDINGS, "SB", "020a", null);
		addToMap(Types.PROCEEDINGS, "ALL", "020a", null);
		addToMap(Types.PROCEEDINGS, "ALL", "300a", null);
		addToMap(Types.PROCEEDINGS, "ALL", "300c", null);
		addToMap(Types.PROCEEDINGS, "ALL", "490a", null);
		addToMap(Types.PROCEEDINGS, "ALL", "490v", null);
		addToMap(Types.PROCEEDINGS, "NA", "111a", null);
		addToMap(Types.PROCEEDINGS, "ALL", "111a", null);
		addToMap(Types.PROCEEDINGS, "YE", "111d", null);
		addToMap(Types.PROCEEDINGS, "ALL", "111d", null);
		addToMap(Types.PROCEEDINGS, "ALL", "111c", null);
		addToMap(Types.PROCEEDINGS, "ALL", "111n", null);
		addToMap(Types.PROCEEDINGS, "ALL", "260b", null);
		addToMap(Types.PROCEEDINGS, "ALL", "260c", null);
		addToMap(Types.PROCEEDINGS, "ALL", "210a", null);
		addToMap(Types.PROCEEDINGS, "ALL", "500a", null);
		addToMap(Types.PROCEEDINGS, "ALL", "653a", null);
		addToMap(Types.PROCEEDINGS, "ALL", "041a", null);
		addToMap(Types.PROCEEDINGS, "EDCN", "7000", null);
		addToMap(Types.PROCEEDINGS, "COCN", "1110", null);
		addToMap(Types.PROCEEDINGS, "AT", "210a", null);
		addToMap(Types.PROCEEDINGS, "NOT", "500a", null);
		addToMap(Types.PROCEEDINGS, "KW", "653a", null);
		addToMap(Types.PROCEEDINGS, "ED", "700a", null);
		addToMap(Types.PROCEEDINGS, "CONT", "700a", null);

		prefixMap.put(Types.PAPER_PROCEEDINGS,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.PAPER_PROCEEDINGS,
				new HashMap<String, List<String>>());

		addToMap(Types.PAPER_PROCEEDINGS, "LA", "008353", null);
		addToMap(Types.PAPER_PROCEEDINGS, "TI", "245a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "TI", "245b", null);
		addToMap(Types.PAPER_PROCEEDINGS, "ALL", "245a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "ALL", "245b", null);
		addToMap(Types.PAPER_PROCEEDINGS, "ALL", "100a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "ALL", "700a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "ALL", "773a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "ALL", "520a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "ALL", "500a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "ALL", "653a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "PY", "773d", null);
		addToMap(Types.PAPER_PROCEEDINGS, "PR", "773t", null);
		addToMap(Types.PAPER_PROCEEDINGS, "CO", "773a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "PRCN", "773w", null);
		addToMap(Types.PAPER_PROCEEDINGS, "AU", "700a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "AU", "100a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "FA", "100a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "FACN", "1000", null);
		addToMap(Types.PAPER_PROCEEDINGS, "INS", "700u", null);
		addToMap(Types.PAPER_PROCEEDINGS, "INS", "100u", null);
		addToMap(Types.PAPER_PROCEEDINGS, "AUCN", "7000", null);
		addToMap(Types.PAPER_PROCEEDINGS, "AUCN", "1000", null);
		addToMap(Types.PAPER_PROCEEDINGS, "AB", "520a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "NOT", "500a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "KW", "653a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "ID", "856u", null);
		addToMap(Types.PAPER_PROCEEDINGS, "CONT", "700u", null);
		addToMap(Types.PAPER_PROCEEDINGS, "CONT", "100u", null);
		addToMap(Types.PAPER_PROCEEDINGS, "SOUR", "773a", null);
		addToMap(Types.PAPER_PROCEEDINGS, "SOUR", "773t", null);
		
		prefixMap.put(Types.FULL_PAPER_PROCEEDINGS,
				prefixMap.get(Types.PAPER_PROCEEDINGS));
		dataSourceMap.put(Types.FULL_PAPER_PROCEEDINGS,
				dataSourceMap.get(Types.PAPER_PROCEEDINGS));
		
		prefixMap.put(Types.ABSTRACT_PAPER_PROCEEDINGS,
				prefixMap.get(Types.PAPER_PROCEEDINGS));
		dataSourceMap.put(Types.ABSTRACT_PAPER_PROCEEDINGS,
				dataSourceMap.get(Types.PAPER_PROCEEDINGS));
		
		prefixMap.put(Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS,
				prefixMap.get(Types.PAPER_PROCEEDINGS));
		dataSourceMap.put(Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS,
				dataSourceMap.get(Types.PAPER_PROCEEDINGS));
		
		prefixMap.put(Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS,
				prefixMap.get(Types.PAPER_PROCEEDINGS));
		dataSourceMap.put(Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS,
				dataSourceMap.get(Types.PAPER_PROCEEDINGS));
		
		prefixMap.put(Types.DISCUSSION_PAPER_PROCEEDINGS,
				prefixMap.get(Types.PAPER_PROCEEDINGS));
		dataSourceMap.put(Types.DISCUSSION_PAPER_PROCEEDINGS,
				dataSourceMap.get(Types.PAPER_PROCEEDINGS));
		
		prefixMap.put(Types.JOURNAL,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.JOURNAL,
				new HashMap<String, List<String>>());

		addToMap(Types.JOURNAL, "ALL", "130a", null);
		addToMap(Types.JOURNAL, "ALL", "022a", null); 
		addToMap(Types.JOURNAL, "ALL", "700a", null);
		addToMap(Types.JOURNAL, "ALL", "041a", null);
		addToMap(Types.JOURNAL, "ALL", "210a", null);
		addToMap(Types.JOURNAL, "ALL", "500a", null);
		addToMap(Types.JOURNAL, "ALL", "653a", null);
		addToMap(Types.JOURNAL, "NA", "130a", null);
		addToMap(Types.JOURNAL, "AT", "210a", null);
		addToMap(Types.JOURNAL, "ALL", "022a", null); 
		addToMap(Types.JOURNAL, "NOT", "500a", null);
		addToMap(Types.JOURNAL, "KW", "653a", null);
		addToMap(Types.JOURNAL, "EDCN", "7000", null);
		addToMap(Types.JOURNAL, "ED", "700a", null);
		addToMap(Types.JOURNAL, "CONT", "700a", null);
		
		prefixMap.put(Types.PAPER_JOURNAL,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.PAPER_JOURNAL,
				new HashMap<String, List<String>>());

		addToMap(Types.PAPER_JOURNAL, "LA", "008353", null);
		addToMap(Types.PAPER_JOURNAL, "ALL", "245a", null);
		addToMap(Types.PAPER_JOURNAL, "ALL", "245b", null);
		addToMap(Types.PAPER_JOURNAL, "ALL", "773x", null);
		addToMap(Types.PAPER_JOURNAL, "ALL", "773t", null);
		addToMap(Types.PAPER_JOURNAL, "ALL", "773g", null);
		addToMap(Types.PAPER_JOURNAL, "ALL", "100a", null);
		addToMap(Types.PAPER_JOURNAL, "ALL", "700a", null);
		addToMap(Types.PAPER_JOURNAL, "ALL", "520a", null);
		addToMap(Types.PAPER_JOURNAL, "ALL", "500a", null);
		addToMap(Types.PAPER_JOURNAL, "ALL", "653a", null);
		addToMap(Types.PAPER_JOURNAL, "PY", "773d", null);
		addToMap(Types.PAPER_JOURNAL, "TI", "245a", null);
		addToMap(Types.PAPER_JOURNAL, "TI", "245b", null);
		addToMap(Types.PAPER_JOURNAL, "JO", "773t", null);
		addToMap(Types.PAPER_JOURNAL, "JOCN", "773w", null);
		addToMap(Types.PAPER_JOURNAL, "AUCN", "1000", null);
		addToMap(Types.PAPER_JOURNAL, "AUCN", "7000", null);
		addToMap(Types.PAPER_JOURNAL, "AU", "100a", null);
		addToMap(Types.PAPER_JOURNAL, "AU", "700a", null);
		addToMap(Types.PAPER_JOURNAL, "FA", "100a", null);
		addToMap(Types.PAPER_JOURNAL, "FACN", "1000", null);
		addToMap(Types.PAPER_JOURNAL, "INS", "100u", null);
		addToMap(Types.PAPER_JOURNAL, "INS", "700u", null);
		addToMap(Types.PAPER_JOURNAL, "AB", "520a", null);
		addToMap(Types.PAPER_JOURNAL, "NOT", "500a", null);
		addToMap(Types.PAPER_JOURNAL, "KW", "653a", null);
		addToMap(Types.PAPER_JOURNAL, "ID", "856u", null);
		addToMap(Types.PAPER_JOURNAL, "CONT", "100u", null);
		addToMap(Types.PAPER_JOURNAL, "CONT", "700u", null);
		addToMap(Types.PAPER_JOURNAL, "SOUR", "773t", null);
		
		prefixMap.put(Types.SCIENTIFIC_CRITICISM_JOURNAL,
				prefixMap.get(Types.PAPER_JOURNAL));
		dataSourceMap.put(Types.SCIENTIFIC_CRITICISM_JOURNAL,
				dataSourceMap.get(Types.PAPER_JOURNAL));
		
		prefixMap.put(Types.OTHER_JOURNAL,
				prefixMap.get(Types.PAPER_JOURNAL));
		dataSourceMap.put(Types.OTHER_JOURNAL,
				dataSourceMap.get(Types.PAPER_JOURNAL));
		
		prefixMap.put(Types.MONOGRAPH,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.MONOGRAPH,
				new HashMap<String, List<String>>());

		addToMap(Types.MONOGRAPH, "LA", "008353", null);
		addToMap(Types.MONOGRAPH, "TI", "245a", null);
		addToMap(Types.MONOGRAPH, "PU", "260b", null);
		addToMap(Types.MONOGRAPH, "INS", "260b", null);
		addToMap(Types.MONOGRAPH, "ALL", "245a", null);
		addToMap(Types.MONOGRAPH, "TI", "245b", null);
		addToMap(Types.MONOGRAPH, "ALL", "245b", null);
		addToMap(Types.MONOGRAPH, "SB", "020a", null);
		addToMap(Types.MONOGRAPH, "ALL", "020a", null);
		addToMap(Types.MONOGRAPH, "ALL", "100a", null);
		addToMap(Types.MONOGRAPH, "ALL", "700a", null);
		addToMap(Types.MONOGRAPH, "ALL", "520a", null);
		addToMap(Types.MONOGRAPH, "ALL", "500a", null);
		addToMap(Types.MONOGRAPH, "ALL", "653a", null);
//		addToMap(Types.MONOGRAPH, "ALL", "300a", null);
//		addToMap(Types.MONOGRAPH, "ALL", "300c", null);
//		addToMap(Types.MONOGRAPH, "ALL", "490a", null);
//		addToMap(Types.MONOGRAPH, "ALL", "260b", null);
//		addToMap(Types.MONOGRAPH, "ALL", "260c", null);
		addToMap(Types.MONOGRAPH, "ALL", "041a", null);
		addToMap(Types.MONOGRAPH, "AUCN", "1000", null);
		addToMap(Types.MONOGRAPH, "AUCN", "7000", "4=aut");
		addToMap(Types.MONOGRAPH, "EDCN", "7000", "4=edt");
		addToMap(Types.MONOGRAPH, "AU", "100a", null);
		addToMap(Types.MONOGRAPH, "FA", "100a", null);
		addToMap(Types.MONOGRAPH, "FACN", "1000", null);
		addToMap(Types.MONOGRAPH, "INS", "100u", null);
		addToMap(Types.MONOGRAPH, "AU", "700a", "4=aut");
		addToMap(Types.MONOGRAPH, "INS", "700u", "4=aut");
		addToMap(Types.MONOGRAPH, "ED", "700a", "4=edt");
		addToMap(Types.MONOGRAPH, "INS", "700u", "4=edt");
		addToMap(Types.MONOGRAPH, "AB", "520a", null);
		addToMap(Types.MONOGRAPH, "NOT", "500a", null);
		addToMap(Types.MONOGRAPH, "KW", "653a", null);
		addToMap(Types.MONOGRAPH, "PY", "260c", null);
		addToMap(Types.MONOGRAPH, "ID", "856u", null);
		addToMap(Types.MONOGRAPH, "ETI", "490a", null);
		addToMap(Types.MONOGRAPH, "ENO", "490v", null);
		addToMap(Types.MONOGRAPH, "ESN", "490x", null);
		addToMap(Types.MONOGRAPH, "CONT", "700a", "4=edt");
		addToMap(Types.MONOGRAPH, "CONT", "700u", null);
		addToMap(Types.MONOGRAPH, "CONT", "260b", null);
		addToMap(Types.MONOGRAPH, "CONT", "100u", null);
		//Sledeca dva mapiranja su veza sourca sa ETI in ENO (ime edicije i njen broj)
		//addToMap(Types.MONOGRAPH, "SOUR", "490a", null);
		//addToMap(Types.MONOGRAPH, "SOUR", "490x", null);
		addToMap(Types.MONOGRAPH, "RA", "CLS", ResearchAreaDTO.RESEARCH_AREA_SCHEMA);

		prefixMap.put(Types.PAPER_MONOGRAPH,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.PAPER_MONOGRAPH,
				new HashMap<String, List<String>>());

		addToMap(Types.PAPER_MONOGRAPH, "LA", "008353", null);
		addToMap(Types.PAPER_MONOGRAPH, "ALL", "245a", null);
		addToMap(Types.PAPER_MONOGRAPH, "ALL", "245b", null);
		addToMap(Types.PAPER_MONOGRAPH, "ALL", "773z", null);
		addToMap(Types.PAPER_MONOGRAPH, "ALL", "773t", null);
		addToMap(Types.PAPER_MONOGRAPH, "ALL", "100a", null);
		addToMap(Types.PAPER_MONOGRAPH, "ALL", "700a", null);
		addToMap(Types.PAPER_MONOGRAPH, "ALL", "520a", null);
		addToMap(Types.PAPER_MONOGRAPH, "ALL", "500a", null);
		addToMap(Types.PAPER_MONOGRAPH, "ALL", "653a", null);
		addToMap(Types.PAPER_MONOGRAPH, "TI", "245a", null);
		addToMap(Types.PAPER_MONOGRAPH, "TI", "245b", null);
		addToMap(Types.PAPER_MONOGRAPH, "PY", "773d", null);
		addToMap(Types.PAPER_MONOGRAPH, "MO", "773t", null);
		addToMap(Types.PAPER_MONOGRAPH, "MOCN", "773w", null);
		addToMap(Types.PAPER_MONOGRAPH, "AUCN", "1000", null);
		addToMap(Types.PAPER_MONOGRAPH, "AUCN", "7000", null);
		addToMap(Types.PAPER_MONOGRAPH, "AU", "100a", null);
		addToMap(Types.PAPER_MONOGRAPH, "AU", "700a", null);
		addToMap(Types.PAPER_MONOGRAPH, "INS", "100u", null);
		addToMap(Types.PAPER_MONOGRAPH, "INS", "700u", null);
		addToMap(Types.PAPER_MONOGRAPH, "AB", "520a", null);
		addToMap(Types.PAPER_MONOGRAPH, "NOT", "500a", null);
		addToMap(Types.PAPER_MONOGRAPH, "KW", "653a", null);
		addToMap(Types.PAPER_MONOGRAPH, "ID", "856u", null);
		addToMap(Types.PAPER_MONOGRAPH, "CONT", "100u", null);
		addToMap(Types.PAPER_MONOGRAPH, "CONT", "700u", null);
		addToMap(Types.PAPER_MONOGRAPH, "SOUR", "773t", null);
		
		
		prefixMap.put(Types.STUDY_FINAL_DOCUMENT,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.STUDY_FINAL_DOCUMENT,
				new HashMap<String, List<String>>());

		addToMap(Types.STUDY_FINAL_DOCUMENT, "LA", "008353", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TI", "245a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TI_SRP", "245a", "lang=srp");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TI_ENG", "245a", "lang=eng");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TI", "245b", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TI_SRP", "245b", "lang=srp");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TI_ENG", "245b", "lang=eng");
//		addToMap(Types.STUDY_FINAL_DOCUMENT, "PY", "260c", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "ALL", "245a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "ALL", "100a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "ALL", "710a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "ALL", "520a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "ALL", "500a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "ALL", "653a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "INCN", "7100", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "INS", "710a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "AUCN", "1000", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "AU", "100a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "INS", "100u", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "AB", "520a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "AB_SRP", "520a", "lang=srp");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "AB_ENG", "520a", "lang=eng");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "NOT", "500a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "KW", "653a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "KW_SRP", "653a", "lang=srp");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "KW_ENG", "653a", "lang=eng");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "RA", "CLS", ResearchAreaDTO.RESEARCH_AREA_SCHEMA);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "ID", "856u", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "CONT", "710a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "CONT", "100u", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "ADCN", "7000", "4=ths");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "CMCN", "7000", "4=exp");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "AD", "700a", "4=ths");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "CM", "700a", "4=exp");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "DT", "502a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TAK", "245a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TAK_SRP", "245a", "lang=srp");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TAK_ENG", "245a", "lang=eng");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TAK", "245b", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TAK_SRP", "245b", "lang=srp");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TAK_ENG", "245b", "lang=eng");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TAK", "520a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TAK_SRP", "520a", "lang=srp");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TAK_ENG", "520a", "lang=eng");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TAK", "653a", null);
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TAK_SRP", "653a", "lang=srp");
		addToMap(Types.STUDY_FINAL_DOCUMENT, "TAK_ENG", "653a", "lang=eng");
		
		prefixMap.put(Types.PHD_STUDY_FINAL_DOCUMENT,
				prefixMap.get(Types.STUDY_FINAL_DOCUMENT));
		dataSourceMap.put(Types.PHD_STUDY_FINAL_DOCUMENT,
				dataSourceMap.get(Types.STUDY_FINAL_DOCUMENT));
		
		prefixMap.put(Types.PHD_ART_PROJECT,
				prefixMap.get(Types.STUDY_FINAL_DOCUMENT));
		dataSourceMap.put(Types.PHD_ART_PROJECT,
				dataSourceMap.get(Types.STUDY_FINAL_DOCUMENT));
		
		prefixMap.put(Types.OLD_MASTER_STUDY_FINAL_DOCUMENT,
				prefixMap.get(Types.STUDY_FINAL_DOCUMENT));
		dataSourceMap.put(Types.OLD_MASTER_STUDY_FINAL_DOCUMENT,
				dataSourceMap.get(Types.STUDY_FINAL_DOCUMENT));
		
		prefixMap.put(Types.MASTER_STUDY_FINAL_DOCUMENT,
				prefixMap.get(Types.STUDY_FINAL_DOCUMENT));
		dataSourceMap.put(Types.MASTER_STUDY_FINAL_DOCUMENT,
				dataSourceMap.get(Types.STUDY_FINAL_DOCUMENT));
		
		prefixMap.put(Types.BACHELOR_STUDY_FINAL_DOCUMENT,
				prefixMap.get(Types.STUDY_FINAL_DOCUMENT));
		dataSourceMap.put(Types.BACHELOR_STUDY_FINAL_DOCUMENT,
				dataSourceMap.get(Types.STUDY_FINAL_DOCUMENT));
		
		prefixMap.put(Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT,
				prefixMap.get(Types.STUDY_FINAL_DOCUMENT));
		dataSourceMap.put(Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT,
				dataSourceMap.get(Types.STUDY_FINAL_DOCUMENT));
		
		prefixMap.put(Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT,
				prefixMap.get(Types.STUDY_FINAL_DOCUMENT));
		dataSourceMap.put(Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT,
				dataSourceMap.get(Types.STUDY_FINAL_DOCUMENT));
		
		prefixMap.put(Types.PATENT,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.PATENT,
				new HashMap<String, List<String>>());

		addToMap(Types.PATENT, "TI", "245a", null);
		addToMap(Types.PATENT, "PU", "260b", null);
		addToMap(Types.PATENT, "PY", "260c", null);
		addToMap(Types.PATENT, "INS", "260b", null);
		addToMap(Types.PATENT, "ALL", "245a", null);
		addToMap(Types.PATENT, "ALL", "100a", null);
		addToMap(Types.PATENT, "ALL", "700a", null);
		addToMap(Types.PATENT, "ALL", "260b", null);
		addToMap(Types.PATENT, "ALL", "520a", null);
		addToMap(Types.PATENT, "ALL", "653a", null);
//		addToMap(Types.PATENT, "INCN", "7100", null);
		addToMap(Types.PATENT, "INS", "100u", null);
		addToMap(Types.PATENT, "INS", "700u", null);
		addToMap(Types.PATENT, "AU", "100a", null);
		addToMap(Types.PATENT, "AU", "700a", null);
		addToMap(Types.PATENT, "AUCN", "1000", null);
		addToMap(Types.PATENT, "AUCN", "7000", null);
		addToMap(Types.PATENT, "AB", "520a", null);
		addToMap(Types.PATENT, "KW", "653a", null);
		addToMap(Types.PATENT, "ID", "856u", null);
		addToMap(Types.PATENT, "CONT", "260b", null);
		addToMap(Types.PATENT, "CONT", "100u", null);
		addToMap(Types.PATENT, "CONT", "700u", null);
		
		prefixMap.put(Types.PRODUCT,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.PRODUCT,
				new HashMap<String, List<String>>());

		addToMap(Types.PRODUCT, "TI", "245a", null);
		addToMap(Types.PRODUCT, "PU", "260b", null);
		addToMap(Types.PRODUCT, "PY", "260c", null);
		addToMap(Types.PRODUCT, "INS", "260b", null);
		addToMap(Types.PRODUCT, "ALL", "245a", null);
		addToMap(Types.PRODUCT, "ALL", "100a", null);
		addToMap(Types.PRODUCT, "ALL", "700a", null);
		addToMap(Types.PRODUCT, "ALL", "260b", null);
		addToMap(Types.PRODUCT, "ALL", "500a", null);
		addToMap(Types.PRODUCT, "ALL", "653a", null);
		addToMap(Types.PRODUCT, "INS", "100u", null);
		addToMap(Types.PRODUCT, "INS", "700u", null);
		addToMap(Types.PRODUCT, "AU", "100a", null);
		addToMap(Types.PRODUCT, "AU", "700a", null);
		addToMap(Types.PRODUCT, "AUCN", "1000", null);
		addToMap(Types.PRODUCT, "AUCN", "7000", null);
		addToMap(Types.PRODUCT, "DS", "500a", null);
		addToMap(Types.PRODUCT, "KW", "653a", null);
		addToMap(Types.PRODUCT, "ID", "856u", null);
		addToMap(Types.PRODUCT, "CONT", "260b", null);
		addToMap(Types.PRODUCT, "CONT", "100u", null);
		addToMap(Types.PRODUCT, "CONT", "700u", null);
		
		prefixMap.put(Types.JOB_AD,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.JOB_AD,
				new HashMap<String, List<String>>());

		addToMap(Types.JOB_AD, "NA", "111a", null);
		addToMap(Types.JOB_AD, "PL", "111C", null);
		addToMap(Types.JOB_AD, "INCN", "3730", null);
		addToMap(Types.JOB_AD, "SD", "046s", null);
		addToMap(Types.JOB_AD, "ED", "046t", null);
		addToMap(Types.JOB_AD, "ALL", "111a", null);
		addToMap(Types.JOB_AD, "ALL", "111c", null);
		addToMap(Types.JOB_AD, "ALL", "680i", null);
		addToMap(Types.JOB_AD, "ALL", "678a", null);
		addToMap(Types.JOB_AD, "ALL", "KWS", null);
		addToMap(Types.JOB_AD, "DS", "680i", null);
		addToMap(Types.JOB_AD, "CON", "678a", null);
		addToMap(Types.JOB_AD, "KW", "KWS", null);
		
		
		prefixMap.put(Types.RULEBOOK,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.RULEBOOK,
				new HashMap<String, List<String>>());
		
		addToMap(Types.RULEBOOK, "ALL", "046m", null);
		addToMap(Types.RULEBOOK, "ALL", "046n", null);
		addToMap(Types.RULEBOOK, "ALL", "084b", null);
		addToMap(Types.RULEBOOK, "ALL", "245a", null);
		addToMap(Types.RULEBOOK, "ALL", "500a", null);
		addToMap(Types.RULEBOOK, "ALL", "653a", null);
		addToMap(Types.RULEBOOK, "PY", "046m", null);
		addToMap(Types.RULEBOOK, "P2", "046n", null);
		addToMap(Types.RULEBOOK, "LN", "084b", null);
		addToMap(Types.RULEBOOK, "NA", "245a", null);
		addToMap(Types.RULEBOOK, "NOT", "500a", null);
		addToMap(Types.RULEBOOK, "KW", "653a", null);
		
		prefixMap.put(Types.RULEBOOK_IMPLEMENTATION,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.RULEBOOK_IMPLEMENTATION,
				new HashMap<String, List<String>>());
		
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "ALL", "046m", null);
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "ALL", "046n", null);
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "ALL", "084b", null);
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "ALL", "245a", null);
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "ALL", "500a", null);
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "ALL", "653a", null);
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "PY", "046m", null);
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "P2", "046n", null);
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "LN", "084b", null);
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "NA", "245a", null);
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "NOT", "500a", null);
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "KW", "653a", null);
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "RBCN", "773w", "4=abs");
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "RB", "773t", "4=abs");
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "RBICN", "773w", "4=imp");
		addToMap(Types.RULEBOOK_IMPLEMENTATION, "RBI", "773t", "4=imp");
		
		prefixMap.put(Types.SPECIALLY_VERIFIED_LIST,
				new HashMap<String, List<String>>());
		dataSourceMap.put(Types.SPECIALLY_VERIFIED_LIST,
				new HashMap<String, List<String>>());
		
		addToMap(Types.SPECIALLY_VERIFIED_LIST, "ALL", "046m", null);
		addToMap(Types.SPECIALLY_VERIFIED_LIST, "ALL", "046n", null);
		addToMap(Types.SPECIALLY_VERIFIED_LIST, "ALL", "084b", null);
		addToMap(Types.SPECIALLY_VERIFIED_LIST, "ALL", "245a", null);
		addToMap(Types.SPECIALLY_VERIFIED_LIST, "ALL", "653a", null);
		addToMap(Types.SPECIALLY_VERIFIED_LIST, "PY", "046m", null);
		addToMap(Types.SPECIALLY_VERIFIED_LIST, "P2", "046n", null);
		addToMap(Types.SPECIALLY_VERIFIED_LIST, "LN", "084b", null);
		addToMap(Types.SPECIALLY_VERIFIED_LIST, "NA", "245a", null);
		addToMap(Types.SPECIALLY_VERIFIED_LIST, "KW", "653a", null);
		
	}

	private void addToMap(String type, String prefix, String dataSource, String condition) {
		List<String> list = prefixMap.get(type).get(dataSource+ ((condition==null)?(""):("|" + condition)));
		if (list == null) {
			list = new ArrayList<String>();
			prefixMap.get(type).put(dataSource+ ((condition==null)?(""):("|" + condition)), list);
		}
		list.add(prefix);

		List<String> sfList = dataSourceMap.get(type).get(prefix);
		if (sfList == null) {
			sfList = new ArrayList<String>();
			dataSourceMap.get(type).put(prefix, sfList);
		}
		sfList.add(dataSource + ((condition==null)?(""):("|" + condition)));

	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixMap#getPrefixMap(java.lang.String)
	 */
	@Override
	public Map<String, List<String>> getPrefixMap(String type) {
		return prefixMap.get(type);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixMap#getSubfieldMap(java.lang.String)
	 */
	@Override
	public Map<String, List<String>> getDataSourceMap(String type) {
		return dataSourceMap.get(type);
	}

	private Map<String, Map<String, List<String>>> prefixMap;
	private Map<String, Map<String, List<String>>> dataSourceMap;
}
