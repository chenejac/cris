/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ConverterFactory;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class Types {

	public static String TYPE_SCHEMA = "type";
	
	public static final String INSTITUTION = "institution";
	public static final String ORGANIZATION_UNIT = "organizationUnit";
	public static final String AUTHOR = "author";
	public static final String CONFERENCE = "conference";
	public static final String PROCEEDINGS = "proceedings";
	public static final String PAPER_PROCEEDINGS = "paperProceedings";
	public static final String FULL_PAPER_PROCEEDINGS = "fullPP";
	public static final String ABSTRACT_PAPER_PROCEEDINGS = "abstractPP";
	public static final String INVITED_TALK_FULL_PAPER_PROCEEDINGS = "invTalkFullPP";
	public static final String INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS = "invTalkAbstractPP";
	public static final String DISCUSSION_PAPER_PROCEEDINGS = "discussionPP";
	public static final String JOURNAL = "journal";
	public static final String PAPER_JOURNAL = "paperJournal";
	public static final String SCIENTIFIC_CRITICISM_JOURNAL = "scientificCriticismJournal";
	public static final String OTHER_JOURNAL = "otherJournal";
	public static final String MONOGRAPH = "monograph";
	public static final String PAPER_MONOGRAPH = "paperMonograph";
	public static final String STUDY_FINAL_DOCUMENT = "studyFinalDocument";
	public static final String PHD_STUDY_FINAL_DOCUMENT = "phdStudyFinalDocument";
	public static final String PHD_ART_PROJECT = "phdArtProject";
	public static final String MASTER_STUDY_FINAL_DOCUMENT = "masterStudyFinalDocument";
	public static final String OLD_MASTER_STUDY_FINAL_DOCUMENT = "oldMasterStudyFinalDocument";
	public static final String BACHELOR_STUDY_FINAL_DOCUMENT = "bachelorStudyFinalDocument";
	public static final String OLD_BACHELOR_STUDY_FINAL_DOCUMENT = "oldBachelorStudyFinalDocument";
	public static final String SPECIALISTIC_STUDY_FINAL_DOCUMENT = "specialisticStudyFinalDocument";	
	public static final String PATENT = "patent";	
	public static final String PRODUCT = "product";	
	public static final String JOB_AD = "jobAd";
	public static final String RULEBOOK = "ruleBook";
	public static final String RULEBOOK_IMPLEMENTATION = "ruleBookImplementation";
	public static final String COMMISSION = "commission";
	public static final String SPECIALLY_VERIFIED_LIST = "speciallyVerifiedList"; 
	
	
	
	/**
	 * @return the tYPE_SCHEMA
	 */
	public static String getSCHEMA() {
		return TYPE_SCHEMA;
	}
	
	public static List<String> getAllCerifEntityClassification(){
		List<String> retVal = new ArrayList<String>();
		retVal.add(INSTITUTION);
		retVal.add(ORGANIZATION_UNIT);
		retVal.add(AUTHOR);
		retVal.add(CONFERENCE);
		retVal.add(PROCEEDINGS);
		retVal.add(PAPER_PROCEEDINGS);
		retVal.add(FULL_PAPER_PROCEEDINGS);
		retVal.add(ABSTRACT_PAPER_PROCEEDINGS);
		retVal.add(INVITED_TALK_FULL_PAPER_PROCEEDINGS);
		retVal.add(INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS);
		retVal.add(DISCUSSION_PAPER_PROCEEDINGS);
		retVal.add(JOURNAL);
		retVal.add(PAPER_JOURNAL);
		retVal.add(SCIENTIFIC_CRITICISM_JOURNAL);
		retVal.add(OTHER_JOURNAL);
		retVal.add(MONOGRAPH);
		retVal.add(PAPER_MONOGRAPH);
		retVal.add(STUDY_FINAL_DOCUMENT);
		retVal.add(PHD_STUDY_FINAL_DOCUMENT);
		retVal.add(MASTER_STUDY_FINAL_DOCUMENT);
		retVal.add(OLD_MASTER_STUDY_FINAL_DOCUMENT);
		retVal.add(BACHELOR_STUDY_FINAL_DOCUMENT);
		retVal.add(OLD_BACHELOR_STUDY_FINAL_DOCUMENT);
		retVal.add(SPECIALISTIC_STUDY_FINAL_DOCUMENT);
		retVal.add(PATENT);
		retVal.add(PRODUCT);
		retVal.add(JOB_AD);
		retVal.add(RULEBOOK);
		retVal.add(RULEBOOK_IMPLEMENTATION);
		retVal.add(COMMISSION);
		retVal.add(SPECIALLY_VERIFIED_LIST);
		return retVal;
	}
	
	public static String getCerifEntityClassification(RecordDTO dto){
		return ConverterFactory.getConverterType(dto);
	}
}
