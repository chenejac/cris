package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.util.List;

import javax.faces.event.PhaseEvent;

import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;

/**
 * Managed bean for theses from FF set available to the public
 * 
 * @author chenejac@uns.ac.rs
 */
public class PublicThesesFFManagedBean extends PublicThesesManagedBean{

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
	
	public static String PUBLIC_THESES_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' and LICENSE like 'Temporary available') AND RECORDSTRING like '%710_2#__0(BISIS)5930%'";
	
	public static String PUBLIC_THESES_NOT_DEFENDED_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' and LICENSE like 'Temporary available - not defended') AND RECORDSTRING like '%710_2#__0(BISIS)5930%'";
	
	public static String THESES_2014_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2014-__-__') AND RECORDSTRING like '%710_2#__0(BISIS)5930%'";
	
	public static String THESES_2015_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2015-__-__') AND RECORDSTRING like '%710_2#__0(BISIS)5930%'";
	
	public static String THESES_2016_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2016-__-__') AND RECORDSTRING like '%710_2#__0(BISIS)5930%'";
	
	public static String THESES_2017_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2017-__-__') AND RECORDSTRING like '%710_2#__0(BISIS)5930%'";
	
	public static String THESES_2018_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2018-__-__') AND RECORDSTRING like '%710_2#__0(BISIS)5930%'";
	
	public static String THESES_2019_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2019-__-__') AND RECORDSTRING like '%710_2#__0(BISIS)5930%'";
	
	public static String THESES_2020_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2020-__-__') AND RECORDSTRING like '%710_2#__0(BISIS)5930%'";

	public static String THESES_2021_WHERE_CLAUSE = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' AND DATEMODIFIED like '2021-__-__') AND RECORDSTRING like '%710_2#__0(BISIS)5930%'";

	/**
	 * 
	 */
	public PublicThesesFFManagedBean() {
		super();
	}
	
	/**
	 * Prepares public theses page
	 * 
	 */
	public synchronized void loadPublicTheses(PhaseEvent event) {
		list = getThesesByWhereClause(PublicThesesFFManagedBean.PUBLIC_THESES_WHERE_CLAUSE);
	}
	
	public synchronized void loadPublicThesesNotDefended(PhaseEvent event) {
		listNotDefended = getThesesByWhereClause(PublicThesesFFManagedBean.PUBLIC_THESES_NOT_DEFENDED_WHERE_CLAUSE);
	}
	
	public synchronized void loadTheses2014(PhaseEvent event){
		list2014 = getThesesByWhereClause(PublicThesesFFManagedBean.THESES_2014_WHERE_CLAUSE);
	}
	
	public synchronized void loadTheses2015(PhaseEvent event){
		list2015 = getThesesByWhereClause(PublicThesesFFManagedBean.THESES_2015_WHERE_CLAUSE);
	}
	
	public synchronized void loadTheses2016(PhaseEvent event){
		list2016 = getThesesByWhereClause(PublicThesesFFManagedBean.THESES_2016_WHERE_CLAUSE);
	}
	
	public synchronized void loadTheses2017(PhaseEvent event){
		list2017 = getThesesByWhereClause(PublicThesesFFManagedBean.THESES_2017_WHERE_CLAUSE);
	}
	
	public synchronized void loadTheses2018(PhaseEvent event){
		list2018 = getThesesByWhereClause(PublicThesesFFManagedBean.THESES_2018_WHERE_CLAUSE);
	}
	
	public synchronized void loadTheses2019(PhaseEvent event){
		list2019 = getThesesByWhereClause(PublicThesesFFManagedBean.THESES_2019_WHERE_CLAUSE);
	}
	
	public synchronized void loadTheses2020(PhaseEvent event){
		list2020 = getThesesByWhereClause(PublicThesesFFManagedBean.THESES_2020_WHERE_CLAUSE);
	}

	public synchronized void loadTheses2021(PhaseEvent event){
		list2021 = getThesesByWhereClause(PublicThesesFFManagedBean.THESES_2021_WHERE_CLAUSE);
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
}
