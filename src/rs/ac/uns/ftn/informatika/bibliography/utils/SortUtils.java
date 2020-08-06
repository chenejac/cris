package rs.ac.uns.ftn.informatika.bibliography.utils;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixConverter;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixValue;

import com.gint.util.sort.Sorter;
import com.gint.util.string.UnimarcConverter;

/**
 * Contains various utilities for sorting.
 * 
 * @author Branko Milosavljevic, mbranko@uns.ns.ac.yu
 * @version 1.0
 */
public class SortUtils {
	/**
	 * Returns a cyrillic/latin collator with cyrillic alphabet ordering.
	 */
	public static Collator getCyrLatCollator() {
		return cyrLatCollator;
	}

	/**
	 * Returns a cyrillic/latin collator with latin alphabet ordering.
	 */
	public static Collator getLatCyrCollator() {
		return latCyrCollator;
	}

	private static Collator latCyrCollator;
	private static Collator cyrLatCollator;

	static {
		String cyrLatRules = "< a      =A      =\u0430 =\u0410 " + // a
				"< b      =B      =\u0431 =\u0411 " + // b
				"< v      =V      =\u0432 =\u0412 " + // v
				"< g      =G      =\u0433 =\u0413 " + // g
				"< d      =D      =\u0434 =\u0414 " + // d
				"< \u0111 =\u0110 =\u0452 =\u0402 " + // dj
				"< e      =E      =\u0435 =\u0415 " + // e
				"< \u017e =\u017d =\u0436 =\u0416 " + // zz
				"< z      =Z      =\u0437 =\u0417 " + // z
				"< i      =I      =\u0438 =\u0418 " + // i
				"< j      =J      =\u0458 =\u0408 " + // j
				"< k      =K      =\u043a =\u041a " + // k
				"< l      =L      =\u041b =\u041b " + // l
				"< lj     =Lj     =\u0459 =\u0409 " + // lj
				"< m      =M      =\u043c =\u041c " + // m
				"< n      =N      =\u043d =\u041d " + // n
				"< nj     =Nj     =\u045a =\u040a " + // nj
				"< o      =O      =\u043e =\u041e " + // o
				"< p      =P      =\u043f =\u041f " + // p
				"< q      =Q " + "< r      =R      =\u0440 =\u0420 " + // r
				"< s      =S      =\u0441 =\u0421 " + // s
				"< t      =T      =\u0442 =\u0422 " + // t
				"< \u0107 =\u0106 =\u045b =\u040b " + // cc
				"< u      =U      =\u0443 =\u0423 " + // u
				"< f      =F      =\u0444 =\u0424 " + // f
				"< h      =H      =\u0445 =\u0425 " + // h
				"< c      =C      =\u0446 =\u0426 " + // c
				"< \u010d =\u010c =\u0447 =\u0427 " + // ch
				"< d\u017e    =D\u017e     =\u045f =\u040f " + // dz
				"< \u0161 =\u0160 =\u0448 =\u0428 " + // ss
				"< w      =W " + "< x      =X " + "< y      =Y ";

		String latCyrRules = "< a      =A      =\u0430 =\u0410 "
				+ // a
				"< b      =B      =\u0431 =\u0411 "
				+ // b
				"< c      =C      =\u0446 =\u0426 "
				+ // c
				"< \u010d =\u010c =\u0447 =\u0427 "
				+ // ch
				"< \u0107 =\u0106 =\u045b =\u040b "
				+ // tj
				"< d      =D      =\u0434 =\u0414 "
				+ // d
				"< \u0111 =\u0110 =\u0452 =\u0402 "
				+ // dj
				"< d\u017e     =D\u017e     =\u045f =\u040f "
				+ // dz
				"< e      =E      =\u0435 =\u0415 "
				+ // e
				"< f      =F      =\u0444 =\u0424 "
				+ // f
				"< g      =G      =\u0433 =\u0413 "
				+ // g
				"< h      =H      =\u0445 =\u0425 "
				+ // h
				"< i      =I      =\u0438 =\u0418 "
				+ // i
				"< j      =J      =\u0458 =\u0408 "
				+ // j
				"< k      =K      =\u043a =\u041a "
				+ // k
				"< l      =L      =\u041b =\u041b "
				+ // l
				"< lj     =Lj     =\u0459 =\u0409 "
				+ // lj
				"< m      =M      =\u043c =\u041c "
				+ // m
				"< n      =N      =\u043d =\u041d "
				+ // n
				"< nj     =Nj     =\u045a =\u040a "
				+ // nj
				"< o      =O      =\u043e =\u041e "
				+ // o
				"< p      =P      =\u043f =\u041f "
				+ // p
				"< q      =Q " + "< r      =R      =\u0440 =\u0420 "
				+ // r
				"< s      =S      =\u0441 =\u0421 "
				+ // s
				"< \u0161 =\u0160 =\u0448 =\u0428 "
				+ // ss
				"< t      =T      =\u0442 =\u0422 "
				+ // t
				"< u      =U      =\u0443 =\u0423 "
				+ // u
				"< v      =V      =\u0432 =\u0412 "
				+ // v
				"< w      =W " + "< x      =X " + "< y      =Y "
				+ "< z      =Z      =\u0437 =\u0417 " + // z
				"< \u017e =\u017d =\u0436 =\u0416 "; // zz

		try {
			cyrLatCollator = new RuleBasedCollator(cyrLatRules);
			latCyrCollator = new RuleBasedCollator(latCyrRules);
		} catch (Exception ex) {
		}
	}

	/**
	 * Sorts an array of records by given prefixes
	 * 
	 * @param records
	 *            an array of records
	 * @param prefSort
	 *            a vector of prefixes which will be used for sorting
	 * @return - an array of sorted records
	 */

	public static MARC21Record[] sortRecord(Record[] records,
			Vector<String> prefSort) {
		UnimarcConverter conv = new UnimarcConverter();

		SortableBiblio[] niz = new SortableBiblio[records.length];
		for (int i = 0; i < records.length; i++) {
			Vector<String> sortableVector = new Vector<String>();
			HashMap<String, List<String>> prefs = prepareMap(records[i]);
			for (int j = 0; j < prefSort.size(); j++) {
				List<String> valueList = prefs.get(prefSort.elementAt(j));
				String str = "";
				if (valueList != null && valueList.size() != 0) {
					String tmp = conv.Unimarc2Unicode(valueList.get(0));
					str += tmp;
					for (int k = 1; k < valueList.size(); k++) {
						tmp = valueList.get(k);
						str += "; " + conv.Unimarc2Unicode(tmp);
					}
				}
				sortableVector.add(str);
			}
			niz[i] = new SortableBiblio(records[i].getMARC21Record(),
					sortableVector);
		}
		Sorter.qsort(niz);
		MARC21Record[] hitsSort = new MARC21Record[niz.length];
		for (int m = 0; m < niz.length; m++) {
			hitsSort[m] = niz[m].getRecord();
		}
		return hitsSort;
	}

	/**
	 * 
	 * @param rec
	 *            mARC21Record
	 * @return the HashMap of mARC21Record field values lists mapped to the prefixes
	 */
	private static HashMap<String, List<String>> prepareMap(Record rec) {
		Iterator<PrefixValue> prefixes = PrefixConverter.toPrefixes(rec)
				.iterator();
		HashMap<String, List<String>> hm = new HashMap<String, List<String>>();
		while (prefixes.hasNext()) {
			PrefixValue pref = prefixes.next();
			String value = LatCyrUtils.toLatin(pref.getValue());
			List<String> l = hm.get(pref.getPrefName());
			if (l == null) {
				l = new ArrayList<String>();
			}
			l.add(value);
			hm.put(pref.getPrefName(), l);
		}
		return hm;
	}
}