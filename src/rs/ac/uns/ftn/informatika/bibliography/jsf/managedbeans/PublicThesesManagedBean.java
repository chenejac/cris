package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.faces.event.PhaseEvent;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.EmailMessage;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * Managed bean for theses set available to the public
 * 
 * @author chenejac@uns.ac.rs
 */
public class PublicThesesManagedBean {
	
	protected List<StudyFinalDocumentDTO> list;
	protected List<StudyFinalDocumentDTO> listNotDefended;
	protected List<StudyFinalDocumentDTO> list2014;
	protected List<StudyFinalDocumentDTO> list2015;
	protected List<StudyFinalDocumentDTO> list2016;
	protected List<StudyFinalDocumentDTO> list2017;
	protected List<StudyFinalDocumentDTO> list2018;
	protected List<StudyFinalDocumentDTO> list2019;
	protected List<StudyFinalDocumentDTO> list2020;
	protected List<StudyFinalDocumentDTO> list2021;
	
//	protected Map<String, StudyFinalDocumentDTO> map = new HashMap<String, StudyFinalDocumentDTO>();
//	protected Map<String, StudyFinalDocumentDTO> map2014 = new HashMap<String, StudyFinalDocumentDTO>();

	public static String PUBLIC_THESES_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' and LICENSE like 'Temporary available') AND (RECORDSTRING like '%710_2#__0(BISIS)8011%' OR RECORDSTRING like '%710_2#__0(BISIS)114901%' OR RECORDSTRING like '%710_2#__0(BISIS)593%' OR RECORDSTRING like '%710_2#__0(BISIS)5928%' OR RECORDSTRING like '%710_2#__0(BISIS)82773%' OR RECORDSTRING like '%710_2#__0(BISIS)5941%' OR RECORDSTRING like '%710_2#__0(BISIS)5940%' OR RECORDSTRING like '%710_2#__0(BISIS)5929%')";
//	public static String PUBLIC_THESES_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' and LICENSE like 'Temporary available') AND (RECORDSTRING like '%710_2#__0(BISIS)8011%' OR RECORDSTRING like '%710_2#__0(BISIS)114901%' OR RECORDSTRING like '%710_2#__0(BISIS)5936%' OR RECORDSTRING like '%710_2#__0(BISIS)5934%' OR RECORDSTRING like '%710_2#__0(BISIS)5930%' OR RECORDSTRING like '%710_2#__0(BISIS)5939%' OR RECORDSTRING like '%710_2#__0(BISIS)5928%' OR RECORDSTRING like '%710_2#__0(BISIS)5937%' OR RECORDSTRING like '%710_2#__0(BISIS)82773%' OR RECORDSTRING like '%710_2#__0(BISIS)5941%' OR RECORDSTRING like '%710_2#__0(BISIS)5935%' OR RECORDSTRING like '%710_2#__0(BISIS)5940%' OR RECORDSTRING like '%710_2#__0(BISIS)5932%' OR RECORDSTRING like '%710_2#__0(BISIS)5929%' OR RECORDSTRING like '%710_2#__0(BISIS)5931%' OR RECORDSTRING like '%710_2#__0(BISIS)5933%' OR RECORDSTRING like '%710_2#__0(BISIS)5938%')";
		
	public static String PUBLIC_THESES_NOT_DEFENDED_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' and LICENSE like 'Temporary available - not defended') AND (RECORDSTRING like '%710_2#__0(BISIS)8011%' OR RECORDSTRING like '%710_2#__0(BISIS)114901%' OR RECORDSTRING like '%710_2#__0(BISIS)593%' OR RECORDSTRING like '%710_2#__0(BISIS)5928%' OR RECORDSTRING like '%710_2#__0(BISIS)82773%' OR RECORDSTRING like '%710_2#__0(BISIS)5941%' OR RECORDSTRING like '%710_2#__0(BISIS)5940%' OR RECORDSTRING like '%710_2#__0(BISIS)5929%')";
	
	public static String THESES_2014_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2014-__-__') AND (RECORDSTRING like '%710_2#__0(BISIS)8011%' OR RECORDSTRING like '%710_2#__0(BISIS)114901%' OR RECORDSTRING like '%710_2#__0(BISIS)593%' OR RECORDSTRING like '%710_2#__0(BISIS)5928%' OR RECORDSTRING like '%710_2#__0(BISIS)82773%' OR RECORDSTRING like '%710_2#__0(BISIS)5941%' OR RECORDSTRING like '%710_2#__0(BISIS)5940%' OR RECORDSTRING like '%710_2#__0(BISIS)5929%')";
	
	public static String THESES_2015_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2015-__-__') AND (RECORDSTRING like '%710_2#__0(BISIS)8011%' OR RECORDSTRING like '%710_2#__0(BISIS)114901%' OR RECORDSTRING like '%710_2#__0(BISIS)593%' OR RECORDSTRING like '%710_2#__0(BISIS)5928%' OR RECORDSTRING like '%710_2#__0(BISIS)82773%' OR RECORDSTRING like '%710_2#__0(BISIS)5941%' OR RECORDSTRING like '%710_2#__0(BISIS)5940%' OR RECORDSTRING like '%710_2#__0(BISIS)5929%')";
	
	public static String THESES_2016_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2016-__-__') AND (RECORDSTRING like '%710_2#__0(BISIS)8011%' OR RECORDSTRING like '%710_2#__0(BISIS)114901%' OR RECORDSTRING like '%710_2#__0(BISIS)593%' OR RECORDSTRING like '%710_2#__0(BISIS)5928%' OR RECORDSTRING like '%710_2#__0(BISIS)82773%' OR RECORDSTRING like '%710_2#__0(BISIS)5941%' OR RECORDSTRING like '%710_2#__0(BISIS)5940%' OR RECORDSTRING like '%710_2#__0(BISIS)5929%')";
	
	public static String THESES_2017_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2017-__-__') AND (RECORDSTRING like '%710_2#__0(BISIS)8011%' OR RECORDSTRING like '%710_2#__0(BISIS)114901%' OR RECORDSTRING like '%710_2#__0(BISIS)593%' OR RECORDSTRING like '%710_2#__0(BISIS)5928%' OR RECORDSTRING like '%710_2#__0(BISIS)82773%' OR RECORDSTRING like '%710_2#__0(BISIS)5941%' OR RECORDSTRING like '%710_2#__0(BISIS)5940%' OR RECORDSTRING like '%710_2#__0(BISIS)5929%')";
	
	public static String THESES_2018_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2018-__-__') AND (RECORDSTRING like '%710_2#__0(BISIS)8011%' OR RECORDSTRING like '%710_2#__0(BISIS)114901%' OR RECORDSTRING like '%710_2#__0(BISIS)593%' OR RECORDSTRING like '%710_2#__0(BISIS)5928%' OR RECORDSTRING like '%710_2#__0(BISIS)82773%' OR RECORDSTRING like '%710_2#__0(BISIS)5941%' OR RECORDSTRING like '%710_2#__0(BISIS)5940%' OR RECORDSTRING like '%710_2#__0(BISIS)5929%')";
	
	public static String THESES_2019_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2019-__-__') AND (RECORDSTRING like '%710_2#__0(BISIS)8011%' OR RECORDSTRING like '%710_2#__0(BISIS)114901%' OR RECORDSTRING like '%710_2#__0(BISIS)593%' OR RECORDSTRING like '%710_2#__0(BISIS)5928%' OR RECORDSTRING like '%710_2#__0(BISIS)82773%' OR RECORDSTRING like '%710_2#__0(BISIS)5941%' OR RECORDSTRING like '%710_2#__0(BISIS)5940%' OR RECORDSTRING like '%710_2#__0(BISIS)5929%')";
	
	public static String THESES_2020_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2020-__-__') AND (RECORDSTRING like '%710_2#__0(BISIS)8011%' OR RECORDSTRING like '%710_2#__0(BISIS)114901%' OR RECORDSTRING like '%710_2#__0(BISIS)593%' OR RECORDSTRING like '%710_2#__0(BISIS)5928%' OR RECORDSTRING like '%710_2#__0(BISIS)82773%' OR RECORDSTRING like '%710_2#__0(BISIS)5941%' OR RECORDSTRING like '%710_2#__0(BISIS)5940%' OR RECORDSTRING like '%710_2#__0(BISIS)5929%')";

	public static String THESES_2021_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2021-__-__') AND (RECORDSTRING like '%710_2#__0(BISIS)8011%' OR RECORDSTRING like '%710_2#__0(BISIS)114901%' OR RECORDSTRING like '%710_2#__0(BISIS)593%' OR RECORDSTRING like '%710_2#__0(BISIS)5928%' OR RECORDSTRING like '%710_2#__0(BISIS)82773%' OR RECORDSTRING like '%710_2#__0(BISIS)5941%' OR RECORDSTRING like '%710_2#__0(BISIS)5940%' OR RECORDSTRING like '%710_2#__0(BISIS)5929%')";

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
		
	public static Date PUBLIC_THESES_LAST_MODIFICATION_DATE = new Date();
	
	/**
	 * 
	 */
	public PublicThesesManagedBean() {
		super();
	}
	
	/**
	 * Prepares public theses page
	 * 
	 */
	public synchronized void loadPublicTheses(PhaseEvent event) {
		list = getThesesByWhereClause(PUBLIC_THESES_WHERE_CLAUSE);
//		optimizeListLoading(list, map);
//		map = new HashMap<String, StudyFinalDocumentDTO>();
//		populateMap(map, list);
//		List<String> orderByList = new ArrayList<String>();
//		List<String> directionsList = new ArrayList<String>();
//		orderByList.add("datemodified");
//		directionsList.add("desc");
//		orderByList.add("controlNumber");
//		directionsList.add("desc");
//		Collections.sort(list, new GenericComparator<StudyFinalDocumentDTO>(
//				orderByList, directionsList));
	}
	
	public synchronized void loadPublicThesesNotDefended(PhaseEvent event) {
		listNotDefended = getThesesByWhereClause(PUBLIC_THESES_NOT_DEFENDED_WHERE_CLAUSE);
//		optimizeListLoading(list, map);
//		map = new HashMap<String, StudyFinalDocumentDTO>();
//		populateMap(map, list);
//		List<String> orderByList = new ArrayList<String>();
//		List<String> directionsList = new ArrayList<String>();
//		orderByList.add("datemodified");
//		directionsList.add("desc");
//		orderByList.add("controlNumber");
//		directionsList.add("desc");
//		Collections.sort(list, new GenericComparator<StudyFinalDocumentDTO>(
//				orderByList, directionsList));
	}
	
	public synchronized void loadTheses2014(PhaseEvent event){
		list2014 = getThesesByWhereClause(THESES_2014_WHERE_CLAUSE);
//		optimizeListLoading(list2014, map2014);
//		map2014 = new HashMap<String, StudyFinalDocumentDTO>();
//		populateMap(map2014, list2014);
//		List<String> orderByList = new ArrayList<String>();
//		List<String> directionsList = new ArrayList<String>();
//		orderByList.add("datemodified"); //ovo bi trebalo da je dovoljno dobro
//		directionsList.add("desc"); //isto tako je i na sajtu UNSa
//		orderByList.add("controlNumber");
//		directionsList.add("desc");
//		Collections.sort(list2014, new GenericComparator<StudyFinalDocumentDTO>(
//				orderByList, directionsList));
	}
	
	public synchronized void loadTheses2015(PhaseEvent event){
		list2015 = getThesesByWhereClause(THESES_2015_WHERE_CLAUSE);
//		optimizeListLoading(list2014, map2014);
//		map2014 = new HashMap<String, StudyFinalDocumentDTO>();
//		populateMap(map2014, list2014);
//		List<String> orderByList = new ArrayList<String>();
//		List<String> directionsList = new ArrayList<String>();
//		orderByList.add("datemodified"); //ovo bi trebalo da je dovoljno dobro
//		directionsList.add("desc"); //isto tako je i na sajtu UNSa
//		orderByList.add("controlNumber");
//		directionsList.add("desc");
//		Collections.sort(list2014, new GenericComparator<StudyFinalDocumentDTO>(
//				orderByList, directionsList));
	}
	
	
	public synchronized void loadTheses2016(PhaseEvent event){
		list2016 = getThesesByWhereClause(THESES_2016_WHERE_CLAUSE);
//		optimizeListLoading(list2014, map2014);
//		map2014 = new HashMap<String, StudyFinalDocumentDTO>();
//		populateMap(map2014, list2014);
//		List<String> orderByList = new ArrayList<String>();
//		List<String> directionsList = new ArrayList<String>();
//		orderByList.add("datemodified"); //ovo bi trebalo da je dovoljno dobro
//		directionsList.add("desc"); //isto tako je i na sajtu UNSa
//		orderByList.add("controlNumber");
//		directionsList.add("desc");
//		Collections.sort(list2014, new GenericComparator<StudyFinalDocumentDTO>(
//				orderByList, directionsList));
	}
	
	public synchronized void loadTheses2017(PhaseEvent event){
		list2017 = getThesesByWhereClause(THESES_2017_WHERE_CLAUSE);
	}
	
	public synchronized void loadTheses2018(PhaseEvent event){
		list2018 = getThesesByWhereClause(THESES_2018_WHERE_CLAUSE);
	}
	
	public synchronized void loadTheses2019(PhaseEvent event){
		list2019 = getThesesByWhereClause(THESES_2019_WHERE_CLAUSE);
	}
	
	public synchronized void loadTheses2020(PhaseEvent event){
		list2020 = getThesesByWhereClause(THESES_2020_WHERE_CLAUSE);
	}

	public synchronized void loadTheses2021(PhaseEvent event){
		list2021 = getThesesByWhereClause(THESES_2021_WHERE_CLAUSE);
	}
	
	/*
	public List<StudyFinalDocumentDTO> getThesesSetAvaialableToThePublic(){
		List<StudyFinalDocumentDTO> retVal = new ArrayList<StudyFinalDocumentDTO>();
		
		String whereClause = PublicThesesManagedBean.PUBLIC_THESES_WHERE_CLAUSE;
		
		List<Record> records = recordDAO.getRecordsFromDatabaseByWhereClause(whereClause);
		if(records != null)
			for (Record record : records) {
				retVal.add((StudyFinalDocumentDTO) record.getDto());
			}
		
		return retVal;
	}*/
	
	public List<StudyFinalDocumentDTO> getThesesByWhereClause(String whereClause){
		List<StudyFinalDocumentDTO> retVal = new ArrayList<StudyFinalDocumentDTO>();
		List<Record> records = recordDAO.getRecordsIdsFromDatabaseByWhereClause(whereClause);
		if(records != null) {
			for (Record record : records) {
				retVal.add((StudyFinalDocumentDTO) record.getDto());
			}
		}
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		orderByList.add("dateModified");
		directionsList.add("desc");
		orderByList.add("controlNumber");
		directionsList.add("desc");
		Collections.sort(retVal, new GenericComparator<StudyFinalDocumentDTO>(
				orderByList, directionsList));
		return retVal;
	}

	/**
	 * @return the list
	 */
	public List<StudyFinalDocumentDTO> getPublicTheses() {
		return list;
	}
	
	public List<StudyFinalDocumentDTO> getPublicThesesNotDefended() {
		return listNotDefended;
	}
	
	public List<StudyFinalDocumentDTO> getTheses2014() {
		return list2014;
	}
	
	public List<StudyFinalDocumentDTO> getTheses2015() {
		return list2015;
	}
	
	public List<StudyFinalDocumentDTO> getTheses2016() {
		return list2016;
	}
	
	public List<StudyFinalDocumentDTO> getTheses2017() {
		return list2017;
	}
	
	public List<StudyFinalDocumentDTO> getTheses2018() {
		return list2018;
	}
	
	public List<StudyFinalDocumentDTO> getTheses2019() {
		return list2019;
	}
	
	public List<StudyFinalDocumentDTO> getTheses2020() {
		return list2020;
	}

	public List<StudyFinalDocumentDTO> getTheses2021() {
		return list2021;
	}
	
	public String getLastModificationDate(){
		String retVal = "";
		DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		Date date = PublicThesesManagedBean.PUBLIC_THESES_LAST_MODIFICATION_DATE;
		if(date != null){
			retVal = formatter.format(date);
		}
		return retVal;
	}
	
//	protected void optimizeListLoading(List<StudyFinalDocumentDTO> list, Map<String, StudyFinalDocumentDTO> map){
//		for (StudyFinalDocumentDTO theses : map.values()) {
//			if(list.contains(theses)){
//				try {
//					Record record = recordDAO.getRecordFromDatabase(theses.getControlNumber());
//					if(theses.getRecord().getLastModificationDate().compareTo(record.getLastModificationDate()) == 0){
//						list.set(list.indexOf(theses), theses);
//					}
//				} catch (Exception e) {
//					PublicThesesManagedBean.SFDMB.error("Compare problem: ", e);
//				}
//			}
//		}
//	}
//	
//	protected void populateMap(Map<String, StudyFinalDocumentDTO> map, List<StudyFinalDocumentDTO> list){
//		for (StudyFinalDocumentDTO theses : list) {
//			map.put(theses.getControlNumber(), theses);
//		}
//	}
	
	/**
	 * 
	 * @param message message without header and footer
	 */
	public static void publicThesesExpiredMessage(String message){
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale("sr"));
		
		String footer = "<br/><br/><br/>"
				+ rbAdministration.getString("administration.notification.footer.kindRegards") + "<br/>"
				+ rbAdministration.getString("administration.notification.footer.sign") + "<br/>"
				+ "<a href=mailto:"+rbAdministration.getString("administration.email.cris") + ">"
				+ rbAdministration.getString("administration.email.cris")+"</a>";
		
		message = rbAdministration.getString("administration.notification.header.dear") + "<br/><br/>" + message + footer;
		EmailMessage emessage = new EmailMessage(rbAdministration.getString("administration.email.cris"),
				rbAdministration.getString("administration.email.librarian"), null, null, rbAdministration.getString("administration.notification.publicTheses.emailSubject"), message);
		PublicThesesManagedBean.SFDMB.sendMessage(emessage);
		PublicThesesManagedBean.SFDMB.log.info("Expired public theses email notification sent");
	}
	
	public static void publicThesesExpiredErrorMessage(String errorMessage){
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale("sr"));
		
		String footer = "<br/><br/><br/>"
				+ rbAdministration.getString("administration.notification.footer.kindRegards") + "<br/>"
				+ rbAdministration.getString("administration.notification.footer.sign") + "<br/>"
				+ "<a href=mailto:"+rbAdministration.getString("administration.email.cris") + ">"
				+ rbAdministration.getString("administration.email.cris")+"</a>";
		
		EmailMessage emessage = new EmailMessage(rbAdministration.getString("administration.email.cris"),
				rbAdministration.getString("administration.email.cris"), null, null, rbAdministration.getString("administration.notification.publicTheses.emailSubject"), errorMessage + footer);
		PublicThesesManagedBean.SFDMB.sendMessage(emessage);
		PublicThesesManagedBean.SFDMB.log.error("Expired public theses email error notification sent");
	}
	
	private static StudyFinalDocumentManagedBean SFDMB = new StudyFinalDocumentManagedBean();

}
