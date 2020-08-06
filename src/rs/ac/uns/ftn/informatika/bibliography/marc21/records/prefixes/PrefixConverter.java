package rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JobAdDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookImplementationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SpecVerLstDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ConverterFactory;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.DocTextExtractor;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.DocTextExtractorFactory;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.ControlField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Leader;

/**
 * Represents a converter responsible for conversion records to prefixes.
 * 
 * @author mbranko@uns.ac.rs
 */
public class PrefixConverter {

	/**
	 * Converts a mARC21Record to the list of prefixes
	 * 
	 * @param rec
	 *            mARC21Record
	 * @return the list of prefixes
	 */
	public static List<PrefixValue> toPrefixes(Record rec) {
		
		rec.getDto().setLocale(new Locale("sr"));
		
		String type = null;
		
		for (Classification classification : rec.getRecordClasses()) {
			if((Types.TYPE_SCHEMA.equalsIgnoreCase(classification.getCfClassSchemeId())&&(classification.getCommissionId().equals(new Integer(0)))))
				type = classification.getCfClassId();
		}
//		System.out.println("PrefixConverter metoda toPrefixes 0 "+type);
		List<PrefixValue> retVal = new ArrayList<PrefixValue>();
		
		if(rec.getScopusID() != null)
			retVal.add(new PrefixValue("SCOPUSID", rec.getScopusID()));
		
		if(rec.getORCID() != null)
			retVal.add(new PrefixValue("ORCID", rec.getORCID()));
		
		if(type.equals(Types.AUTHOR)){
			if(((Person)rec).getJmbg() != null)
				retVal.add(new PrefixValue("JMBG", ((Person)rec).getJmbg()));
			if(((Person)rec).getDirectPhones() != null)
				retVal.add(new PrefixValue("DP", ((Person)rec).getDirectPhones()));
			if(((Person)rec).getLocalPhones() != null)
				retVal.add(new PrefixValue("LP", ((Person)rec).getLocalPhones()));
			retVal.add(new PrefixValue("REGISTERED", new Boolean(((AuthorDTO)rec.getDto()).isAlreadyRegistered()).toString()));
			retVal.add(new PrefixValue("AUTHORUNSDISSERTATIONS", new Boolean(((AuthorDTO)rec.getDto()).isAuthorUnsDissertations()).toString()));
			retVal.add(new PrefixValue("ADVISORUNSDISSERTATIONS", new Boolean(((AuthorDTO)rec.getDto()).isAdvisorUnsDissertations()).toString()));
			retVal.add(new PrefixValue("COMMISSIONMEMBERUNSDISSERTATIONS", new Boolean(((AuthorDTO)rec.getDto()).isCommissionMemberUnsDissertations()).toString()));
			retVal.add(new PrefixValue("COMMISSIONCHAIRUNSDISSERTATIONS", new Boolean(((AuthorDTO)rec.getDto()).isCommissionChairUnsDissertations()).toString()));
			retVal.add(new PrefixValue("AUTHORPADISSERTATIONS", new Boolean(((AuthorDTO)rec.getDto()).isAuthorPaDissertations()).toString()));
			retVal.add(new PrefixValue("ADVISORPADISSERTATIONS", new Boolean(((AuthorDTO)rec.getDto()).isAdvisorPaDissertations()).toString()));
			retVal.add(new PrefixValue("COMMISSIONMEMBERPADISSERTATIONS", new Boolean(((AuthorDTO)rec.getDto()).isCommissionMemberPaDissertations()).toString()));
			retVal.add(new PrefixValue("COMMISSIONCHAIRPADISSERTATIONS", new Boolean(((AuthorDTO)rec.getDto()).isCommissionChairPaDissertations()).toString()));
			if(((Person)rec).getApvnt() != null)
				retVal.add(new PrefixValue("APVNT", ((Person)rec).getApvnt()));
			if((((AuthorDTO)rec.getDto()).getNames()!=null) && (((AuthorDTO)rec.getDto()).getNames().trim().length()!=0))
				retVal.add(new PrefixValue("NAMES", ((AuthorDTO)rec.getDto()).getNames()));
			if(((AuthorDTO)rec.getDto()).getYearOfBirth()!=null)
				retVal.add(new PrefixValue("YEAR", ((AuthorDTO)rec.getDto()).getYearOfBirth().toString()));
			if((((AuthorDTO)rec.getDto()).getInstitutionName()!=null) && (((AuthorDTO)rec.getDto()).getInstitutionName().trim().length()!=0))
				retVal.add(new PrefixValue("INSTITUTIONNAME", ((AuthorDTO)rec.getDto()).getInstitutionName()));
			if((((AuthorDTO)rec.getDto()).getTitle()!=null) && (((AuthorDTO)rec.getDto()).getTitle().trim().length()!=0))
				retVal.add(new PrefixValue("TITLE", ((AuthorDTO)rec.getDto()).getTitle()));
			if((((AuthorDTO)rec.getDto()).getCurrentPositionName()!=null) && (((AuthorDTO)rec.getDto()).getCurrentPositionName().trim().length()!=0))
				retVal.add(new PrefixValue("CURRENTPOSITIONNAME", ((AuthorDTO)rec.getDto()).getCurrentPositionName()));
			for (JobAdDTO jobAd : ((AuthorDTO)rec.getDto()).getJobApplications()) {
				retVal.add(new PrefixValue("JACN", jobAd.getControlNumber()));
			}
		}
		
		if(type.equals(Types.ORGANIZATION_UNIT)){
			if((((OrganizationUnitDTO)rec.getDto()).getSomeName()!=null) && (((OrganizationUnitDTO)rec.getDto()).getSomeName().trim().length()!=0))
				retVal.add(new PrefixValue("SOMENAME", ((OrganizationUnitDTO)rec.getDto()).getSomeName()));
			if((((OrganizationUnitDTO)rec.getDto()).getPlace()!=null) && (((OrganizationUnitDTO)rec.getDto()).getPlace().trim().length()!=0))
				retVal.add(new PrefixValue("PLACE", ((OrganizationUnitDTO)rec.getDto()).getPlace()));
			if((((OrganizationUnitDTO)rec.getDto()).getInstitution()!=null) && (((OrganizationUnitDTO)rec.getDto()).getInstitution().getControlNumber()!=null))
				retVal.add(new PrefixValue("SUPERINSTITUTIONCN", (((OrganizationUnitDTO)rec.getDto()).getInstitution().getControlNumber())));
			if((((OrganizationUnitDTO)rec.getDto()).getSuperOrganizationUnit()!=null) && (((OrganizationUnitDTO)rec.getDto()).getSuperOrganizationUnit().getControlNumber()!=null))
				retVal.add(new PrefixValue("SUPERORGANIZATIONUNITCN", (((OrganizationUnitDTO)rec.getDto()).getSuperOrganizationUnit().getControlNumber())));
		} else if(type.equals(Types.INSTITUTION)){
				if((((InstitutionDTO)rec.getDto()).getSomeName()!=null) && (((InstitutionDTO)rec.getDto()).getSomeName().trim().length()!=0))
					retVal.add(new PrefixValue("SOMENAME", ((InstitutionDTO)rec.getDto()).getSomeName()));
				if((((InstitutionDTO)rec.getDto()).getPlace()!=null) && (((InstitutionDTO)rec.getDto()).getPlace().trim().length()!=0))
					retVal.add(new PrefixValue("PLACE", ((InstitutionDTO)rec.getDto()).getPlace()));
				if((((InstitutionDTO)rec.getDto()).getSuperInstitution()!=null) && (((InstitutionDTO)rec.getDto()).getSuperInstitution().getControlNumber()!=null))
					retVal.add(new PrefixValue("SUPERINSTITUTIONCN", (((InstitutionDTO)rec.getDto()).getSuperInstitution().getControlNumber())));
		}
		if(ConverterFactory.getDTO(type) instanceof StudyFinalDocumentDTO){
			if((((StudyFinalDocumentDTO)rec.getDto()).getAuthor()!=null) && (((StudyFinalDocumentDTO)rec.getDto()).getAuthor().getName() != null)){
				retVal.add(new PrefixValue("AUTHORNAME", ((StudyFinalDocumentDTO)rec.getDto()).getAuthor().getName().toString()));
			}
			if((((StudyFinalDocumentDTO)rec.getDto()).getInstitution()!=null) && (((StudyFinalDocumentDTO)rec.getDto()).getInstitution().getSomeName() != null))
				retVal.add(new PrefixValue("INSTITUTIONNAME", ((StudyFinalDocumentDTO)rec.getDto()).getInstitution().getSomeName()));
			if((((StudyFinalDocumentDTO)rec.getDto()).getSomeTitle()!=null) && (((StudyFinalDocumentDTO)rec.getDto()).getSomeTitle().trim().length()!=0))
				retVal.add(new PrefixValue("SOMETITLE", ((StudyFinalDocumentDTO)rec.getDto()).getSomeTitle()));
			if((((StudyFinalDocumentDTO)rec.getDto()).getResearchArea()!=null) && (((StudyFinalDocumentDTO)rec.getDto()).getResearchArea().getSomeTerm() != null))
				retVal.add(new PrefixValue("SOMERESEARCHAREA", ((StudyFinalDocumentDTO)rec.getDto()).getResearchArea().getSomeTerm()));
			if((((StudyFinalDocumentDTO)rec.getDto()).getStudyType()!=null) && (((StudyFinalDocumentDTO)rec.getDto()).getStudyType().trim().length()!=0))
				retVal.add(new PrefixValue("STUDYTYPE", ((StudyFinalDocumentDTO)rec.getDto()).getStudyType()));
			if((((StudyFinalDocumentDTO)rec.getDto()).getPublicPeriod()!=null) && (((StudyFinalDocumentDTO)rec.getDto()).getPublicPeriod().trim().length()!=0))
				retVal.add(new PrefixValue("PUBLICPERIOD", ((StudyFinalDocumentDTO)rec.getDto()).getPublicPeriod()));
			if(((StudyFinalDocumentDTO)rec.getDto()).getDateModified() != 0)
				retVal.add(new PrefixValue("DATEMODIFIED", "" + ((StudyFinalDocumentDTO)rec.getDto()).getDateModified()));
			retVal.add(new PrefixValue("RECORDETDMSREPRESENTATION", ((StudyFinalDocumentDTO)rec.getDto()).getETDMS()));
			retVal.add(new PrefixValue("UNSDISSERTATION", new Boolean(((StudyFinalDocumentDTO)rec.getDto()).isUnsDissertation()).toString()));
			retVal.add(new PrefixValue("PADISSERTATION", new Boolean(((StudyFinalDocumentDTO)rec.getDto()).isPaDissertation()).toString()));
			((StudyFinalDocumentDTO)rec.getDto()).loadRegisterEntry(false);
			if(((StudyFinalDocumentDTO)rec.getDto()).getRegisterEntry()!=null){
				retVal.add(new PrefixValue("REGISTERENTRYHTMLREPRESENTATION", ((StudyFinalDocumentDTO)rec.getDto()).getRegisterEntry().getHTMLRepresentation()));
				retVal.add(new PrefixValue("REGISTERENTRYHTMLREPRESENTATIONEN", ((StudyFinalDocumentDTO)rec.getDto()).getRegisterEntry().getHTMLRepresentationEn()));
			}
			if(((StudyFinalDocumentDTO)rec.getDto()).getCommitteeMembers().size() > 0){
				retVal.add(new PrefixValue("CCCN", ((StudyFinalDocumentDTO)rec.getDto()).getCommitteeMembers().get(0).getControlNumber()));
				retVal.add(new PrefixValue("CC", ((StudyFinalDocumentDTO)rec.getDto()).getCommitteeMembers().get(0).getName().toString()));
			}
			if(((StudyFinalDocumentDTO)rec.getDto()).getPublicationYear() != null){
				retVal.add(new PrefixValue("PY", ((StudyFinalDocumentDTO)rec.getDto()).getPublicationYear()));
			}
			if(((StudyFinalDocumentDTO)rec.getDto()).getDefendedOn() != null){
				retVal.add(new PrefixValue("DEFENDED", StudyFinalDocumentDTO.defendedNameString[1]));
			} else {
				retVal.add(new PrefixValue("DEFENDED", StudyFinalDocumentDTO.defendedNameString[2]));
			}
			retVal.add(new PrefixValue("RECORDHARVARDREPRESENTATIONEN", ((StudyFinalDocumentDTO)rec.getDto()).getHarvardRepresentationEn()));
			if((((StudyFinalDocumentDTO)rec.getDto()).getPreliminaryTheses()!=null) && (((StudyFinalDocumentDTO)rec.getDto()).getPreliminaryTheses().getFileName()!=null))
				retVal.add(new PrefixValue("PRELIMINARYTHESESURL", ((StudyFinalDocumentDTO)rec.getDto()).getPreliminaryThesesURL()));
			if((((StudyFinalDocumentDTO)rec.getDto()).getPreliminarySupplement()!=null) && (((StudyFinalDocumentDTO)rec.getDto()).getPreliminarySupplement().getFileName()!=null))
				retVal.add(new PrefixValue("PRELIMINARYSUPPLEMENTURL", ((StudyFinalDocumentDTO)rec.getDto()).getPreliminarySupplementURL()));
			if((((StudyFinalDocumentDTO)rec.getDto()).getReport()!=null) && (((StudyFinalDocumentDTO)rec.getDto()).getReport().getFileName()!=null))
				retVal.add(new PrefixValue("REPORTURL", ((StudyFinalDocumentDTO)rec.getDto()).getReportURL()));
			if((((StudyFinalDocumentDTO)rec.getDto()).getRelatedPublicationsHTMLRepresentation() != null) && (((StudyFinalDocumentDTO)rec.getDto()).getRelatedPublicationsHTMLRepresentation().trim().length()!=0))
				retVal.add(new PrefixValue("RELATEDPUBLICATIONS", ((StudyFinalDocumentDTO)rec.getDto()).getRelatedPublicationsHTMLRepresentation()));
//			if((((StudyFinalDocumentDTO)rec.getDto()).getUri()!=null) && (((StudyFinalDocumentDTO)rec.getDto()).getUri().trim().length()!=0))
//				retVal.add(new PrefixValue("URI", ((StudyFinalDocumentDTO)rec.getDto()).getUri()));
		}
		
		if(ConverterFactory.getDTO(type) instanceof PublicationDTO){
			if(((PublicationDTO)rec.getDto()).getPublicationYear()!=null)
				retVal.add(new PrefixValue("YEAR", ((PublicationDTO)rec.getDto()).getPublicationYear().toString()));
			if(rec.getArchived()!=null)
				retVal.add(new PrefixValue("ARCHIVED", rec.getArchived().toString()));
			if((((PublicationDTO)rec.getDto()).getWordCloudImage()!=null) && (((PublicationDTO)rec.getDto()).getWordCloudImage().getFileName()!=null)){
				retVal.add(new PrefixValue("WCIURL", ((PublicationDTO)rec.getDto()).getWordCloudImageURL()));
			}
			if((((PublicationDTO)rec.getDto()).getFile()!=null) && (((PublicationDTO)rec.getDto()).getFile().getFileName()!=null)){
				retVal.add(new PrefixValue("FILEURL", ((PublicationDTO)rec.getDto()).getFileURL()));
				String ft = extractText(((PublicationDTO)rec.getDto()).getFile());
				if(ft != null){
					retVal.add(new PrefixValue("FT", ft));
					retVal.add(new PrefixValue("FT_SRP", ft));
					retVal.add(new PrefixValue("FT_ENG", ft));
				}
			}
			if(((PublicationDTO)rec.getDto()).getFile()!=null){
				if((((PublicationDTO)rec.getDto()).getFile().getFileName()!=null) && (((PublicationDTO)rec.getDto()).getFile().getFileName().trim().length()!=0)){
					retVal.add(new PrefixValue("FILENAME", ((PublicationDTO)rec.getDto()).getFile().getFileName()));
				}
				if(((PublicationDTO)rec.getDto()).getFile().getId()!=0){
					retVal.add(new PrefixValue("FILEID", "" + ((PublicationDTO)rec.getDto()).getFile().getId()));
				}
				if((((PublicationDTO)rec.getDto()).getFile().getLicense()!=null) && (((PublicationDTO)rec.getDto()).getFile().getLicense().trim().length()!=0)){
					retVal.add(new PrefixValue("FILELICENSE", ((PublicationDTO)rec.getDto()).getFile().getLicense()));
				}
			}
			
			
			for (JobAdDTO jobAd : ((PublicationDTO)rec.getDto()).getJobAds()) {
				retVal.add(new PrefixValue("JACN", jobAd.getControlNumber()));
			}
			
			if((((PublicationDTO)rec.getDto()).getUri()!=null) && (((PublicationDTO)rec.getDto()).getUri().trim().length()!=0))
				retVal.add(new PrefixValue("URI", ((PublicationDTO)rec.getDto()).getUri()));
		}
		
		if(type.equals(Types.CONFERENCE)){
			if((((ConferenceDTO)rec.getDto()).getSomeName()!=null) && (((ConferenceDTO)rec.getDto()).getSomeName().trim().length()!=0))
				retVal.add(new PrefixValue("SOMENAME", ((ConferenceDTO)rec.getDto()).getSomeName()));
			if(((ConferenceDTO)rec.getDto()).getYear()!=null)
				retVal.add(new PrefixValue("YEAR", ((ConferenceDTO)rec.getDto()).getYear().toString()));
			if((((ConferenceDTO)rec.getDto()).getPlace()!=null) && (((ConferenceDTO)rec.getDto()).getPlace().trim().length()!=0))
				retVal.add(new PrefixValue("PLACE", ((ConferenceDTO)rec.getDto()).getPlace()));
			if((((ConferenceDTO)rec.getDto()).getNumber()!=null) && (((ConferenceDTO)rec.getDto()).getNumber().trim().length()!=0))
				retVal.add(new PrefixValue("NUMBER", ((ConferenceDTO)rec.getDto()).getNumber()));
		}
		
		if(type.equals(Types.JOB_AD)){
			if((((JobAdDTO)rec.getDto()).getSomeName()!=null) && (((JobAdDTO)rec.getDto()).getSomeName().trim().length()!=0))
				retVal.add(new PrefixValue("SOMENAME", ((JobAdDTO)rec.getDto()).getSomeName()));
			for (AuthorDTO author : ((JobAdDTO)rec.getDto()).getApplications()) {
				retVal.add(new PrefixValue("APCN", author.getControlNumber()));
			}
		}
		
		if(type.equals(Types.JOURNAL)){
			if((((JournalDTO)rec.getDto()).getSomeName()!=null) && (((JournalDTO)rec.getDto()).getSomeName().trim().length()!=0))
				retVal.add(new PrefixValue("SOMENAME", ((JournalDTO)rec.getDto()).getSomeName()));
			if((((JournalDTO)rec.getDto()).getIssn()!=null) && (((JournalDTO)rec.getDto()).getIssn().trim().length()!=0)){
				retVal.add(new PrefixValue("ISSN", ((JournalDTO)rec.getDto()).getIssn()));
				if(((JournalDTO)rec.getDto()).getIssn().contains(";")){
					String[] issns = ((JournalDTO)rec.getDto()).getIssn().split(";");
					for(String issn:issns){
						retVal.add(new PrefixValue("ISSN", issn.substring(0, issn.indexOf("("))));
					}
				}	
			}
		}
		
		if(type.equals(Types.MONOGRAPH)){
			if((((MonographDTO)rec.getDto()).getSomeTitle()!=null) && (((MonographDTO)rec.getDto()).getSomeTitle().trim().length()!=0))
				retVal.add(new PrefixValue("SOMETITLE", ((MonographDTO)rec.getDto()).getSomeTitle()));
			if((((MonographDTO)rec.getDto()).getIsbn()!=null) && (((MonographDTO)rec.getDto()).getIsbn().trim().length()!=0))
				retVal.add(new PrefixValue("ISBN", ((MonographDTO)rec.getDto()).getIsbn()));
//			if((((MonographDTO)rec.getDto()).getUri()!=null) && (((MonographDTO)rec.getDto()).getUri().trim().length()!=0))
//				retVal.add(new PrefixValue("URI", ((MonographDTO)rec.getDto()).getUri()));
		}
		
//		if(ConverterFactory.getDTO(type) instanceof PaperMonographDTO){
//			if((((PaperMonographDTO)rec.getDto()).getUri()!=null) && (((PaperMonographDTO)rec.getDto()).getUri().trim().length()!=0))
//				retVal.add(new PrefixValue("URI", ((PaperMonographDTO)rec.getDto()).getUri()));
//		}
		
		if(ConverterFactory.getDTO(type) instanceof PaperJournalDTO){
			if((((PaperJournalDTO)rec.getDto()).getPaperType()!=null) && (((PaperJournalDTO)rec.getDto()).getPaperType().trim().length()!=0))
				retVal.add(new PrefixValue("PAPERTYPE", ((PaperJournalDTO)rec.getDto()).getPaperType()));
//			if((((PaperJournalDTO)rec.getDto()).getUri()!=null) && (((PaperJournalDTO)rec.getDto()).getUri().trim().length()!=0))
//				retVal.add(new PrefixValue("URI", ((PaperJournalDTO)rec.getDto()).getUri()));
		}
		
		if(ConverterFactory.getDTO(type) instanceof PaperProceedingsDTO){
			if((((PaperProceedingsDTO)rec.getDto()).getPaperType()!=null) && (((PaperProceedingsDTO)rec.getDto()).getPaperType().trim().length()!=0))
				retVal.add(new PrefixValue("PAPERTYPE", ((PaperProceedingsDTO)rec.getDto()).getPaperType()));
//			if((((PaperProceedingsDTO)rec.getDto()).getUri()!=null) && (((PaperProceedingsDTO)rec.getDto()).getUri().trim().length()!=0))
//				retVal.add(new PrefixValue("URI", ((PaperProceedingsDTO)rec.getDto()).getUri()));
		}
		
		if(type.equals(Types.PROCEEDINGS)){
			if((((ProceedingsDTO)rec.getDto()).getSomeTitle()!=null) && (((ProceedingsDTO)rec.getDto()).getSomeTitle().trim().length()!=0))
				retVal.add(new PrefixValue("SOMETITLE", ((ProceedingsDTO)rec.getDto()).getSomeTitle()));
			if((((ProceedingsDTO)rec.getDto()).getConferenceName()!=null) && (((ProceedingsDTO)rec.getDto()).getConferenceName().trim().length()!=0))
				retVal.add(new PrefixValue("CONFERENCENAME", ((ProceedingsDTO)rec.getDto()).getConferenceName()));
			if((((ProceedingsDTO)rec.getDto()).getIsbn()!=null) && (((ProceedingsDTO)rec.getDto()).getIsbn().trim().length()!=0))
				retVal.add(new PrefixValue("ISBN", ((ProceedingsDTO)rec.getDto()).getIsbn()));
		}
		
		if(type.equals(Types.RULEBOOK_IMPLEMENTATION)){
			if((((RuleBookImplementationDTO)rec.getDto()).getSomeName()!=null) && (((RuleBookImplementationDTO)rec.getDto()).getSomeName().trim().length()!=0))
				retVal.add(new PrefixValue("SOMENAME", ((RuleBookImplementationDTO)rec.getDto()).getSomeName()));
			if((((RuleBookImplementationDTO)rec.getDto()).getSuperRuleBook()!=null) && (((RuleBookImplementationDTO)rec.getDto()).getSuperRuleBook().getControlNumber()!=null))
				retVal.add(new PrefixValue("SUPERRULEBOOKCN", (((RuleBookImplementationDTO)rec.getDto()).getSuperRuleBook().getControlNumber())));
			if((((RuleBookImplementationDTO)rec.getDto()).getSuperRuleBookImplementation()!=null) && (((RuleBookImplementationDTO)rec.getDto()).getSuperRuleBookImplementation().getControlNumber()!=null))
				retVal.add(new PrefixValue("SUPERRULEBOOKIMPLEMENTATIONCN", (((RuleBookImplementationDTO)rec.getDto()).getSuperRuleBookImplementation().getControlNumber())));
		} else if(type.equals(Types.RULEBOOK)){
				if((((RuleBookDTO)rec.getDto()).getSomeName()!=null) && (((RuleBookDTO)rec.getDto()).getSomeName().trim().length()!=0)){
					retVal.add(new PrefixValue("SOMENAME", ((RuleBookDTO)rec.getDto()).getSomeName()));
				}
		}
		
		if(type.equals(Types.SPECIALLY_VERIFIED_LIST)){
			if((((SpecVerLstDTO)rec.getDto()).getSomeName()!=null) && (((SpecVerLstDTO)rec.getDto()).getSomeName().trim().length()!=0))
				retVal.add(new PrefixValue("SOMENAME", ((SpecVerLstDTO)rec.getDto()).getSomeName()));
		}
		
		PrefixConverter.leaderToPrefixes(retVal, rec.getMARC21Record().getLeader(), type);
		for (ControlField cf : rec.getMARC21Record().getControlFields())
			PrefixConverter.controlFieldToPrefixes(retVal, cf, type);
		
		for (DataField df : rec.getMARC21Record().getDataFields())
			PrefixConverter.dataFieldToPrefixes(retVal, df, type);
		
//		StringBuffer recordClasses = new StringBuffer();
		for (Classification rc : rec.getRecordClasses()) {
			PrefixConverter.recordClassToPrefixes(retVal, rc, type);
//			if(recordClasses.length() == 0)
//				recordClasses.append(rc.toString());
//			else
//				recordClasses.append("|;" + rc.toString());
		}
//		if(recordClasses.length() > 0)
//			retVal.add(new PrefixValue("RECORDCLASSES", recordClasses.toString()));
		
//		StringBuffer recordKeywords = new StringBuffer();
		for (MultilingualContentDTO rk : rec.getRecordKeywords()) {
			PrefixConverter.recordKeywordsToPrefixes(retVal, rk, type);
//			if(recordKeywords.length() == 0)
//				recordKeywords.append(rk.toString());
//			else
//				recordKeywords.append("|;" + rk.toString());
		}
		
//		if(recordKeywords.length() > 0)
//			retVal.add(new PrefixValue("RECORDKEYWORDS", recordKeywords.toString()));
		
//		StringBuffer relationsOtherRecordsThisRecord = new StringBuffer();
		for (RecordRecord rr : rec.getRelationsOtherRecordsThisRecord()) {
			PrefixConverter.relationsOtherRecordsThisRecordToPrefixes(retVal, rr, type);
//			if(relationsOtherRecordsThisRecord.length() == 0)
//				relationsOtherRecordsThisRecord.append(rr.toString());
//			else
//				relationsOtherRecordsThisRecord.append("|;" + rr.toString());
		}
//		if(relationsOtherRecordsThisRecord.length() > 0)
//			retVal.add(new PrefixValue("RELATIONSOTHERTHIS", relationsOtherRecordsThisRecord.toString()));
		
//		StringBuffer relationsThisRecordOtherRecords = new StringBuffer();
		for (RecordRecord rr : rec.getRelationsThisRecordOtherRecords()) {
			PrefixConverter.relationsThisRecordOtherRecordsToPrefixes(retVal, rr, type);
//			if(relationsThisRecordOtherRecords.length() == 0)
//				relationsThisRecordOtherRecords.append(rr.toString());
//			else 
//				relationsThisRecordOtherRecords.append("|;" + rr.toString());
		}
	
//		if(relationsThisRecordOtherRecords.length() > 0)
//			retVal.add(new PrefixValue("RELATIONSTHISOTHER", relationsThisRecordOtherRecords.toString()));
		
//		System.out.println("PrefixConverter metoda toPrefixes 99");
//		retVal.add(new PrefixValue("RECORD", rec.getMARC21Record().toString()));
		retVal.add(new PrefixValue("RECORDHTMLREPRESENTATION", rec.getDto().getHTMLRepresentation()));
		retVal.add(new PrefixValue("RECORDHARVARDREPRESENTATION", rec.getDto().getHarvardRepresentation()));
		retVal.add(new PrefixValue("RECORDSTRINGREPRESENTATION", rec.getDto().getStringRepresentation()));
		retVal.add(new PrefixValue("RECORDMARC21REPRESENTATION", rec.getDto().getMARC21()));
		retVal.add(new PrefixValue("RECORDDUBLINCOREREPRESENTATION", rec.getDto().getDublinCore()));
		retVal.add(new PrefixValue("CN", rec.getMARC21Record().getControlNumber()));
		retVal.add(new PrefixValue("TYPE", type));
		
//		System.out.println("PrefixConverter metoda toPrefixes 100");
		return retVal;
	}

	private static void dataFieldToPrefixes(List<PrefixValue> dest,
			DataField dataField, String type) {
		List<String> ds = PrefixConverter.getDataSources(dataField.getName(),
				type);
		for (String element : ds) {
			List<String> prefList = prefixMap.getPrefixes(type, element);
			if (prefList != null) {
				Iterator<String> it = prefList.iterator();
				while (it.hasNext()) {
					String prefName = it.next();
					for (String val : prefixHandler.getDataSubfieldsValues(
							element, dataField ))
						dest.add(new PrefixValue(prefName, val));
				}
			}
		}
	}

	private static void controlFieldToPrefixes(List<PrefixValue> dest,
			ControlField controlField, String type) {
		List<String> cs = PrefixConverter.getDataSources(controlField.getName(),
				type);
		for (String element : cs) {
			List<String> prefList = prefixMap.getPrefixes(type, element);
			if (prefList != null) {
				Iterator<String> it = prefList.iterator();
				while (it.hasNext()) {
					String prefName = it.next();
					dest.add(new PrefixValue(prefName.split("\\|")[0], prefixHandler
							.getControlFieldValue(element, controlField)));
				}
			}
		}
	}

	private static void leaderToPrefixes(List<PrefixValue> dest, Leader leader,
			String type) {
		List<String> ls = PrefixConverter.getDataSources("LDR", type);
		for (String element : ls) {
			List<String> prefList = prefixMap.getPrefixes(type, element);
			if (prefList != null) {
				Iterator<String> it = prefList.iterator();
				while (it.hasNext()) {
					String prefName = it.next();
					dest.add(new PrefixValue(prefName.split("\\|")[0], prefixHandler
							.getLeaderValue(element, leader)));
				}
			}
		}
	}
	
	private static void relationsThisRecordOtherRecordsToPrefixes(
			List<PrefixValue> dest, RecordRecord rr, String type) {
		List<String> ls = PrefixConverter.getDataSources("THIS_OTHER", type);
		for (String element : ls) {
			List<String> prefList = prefixMap.getPrefixes(type, element);
			if (prefList != null) {
				Iterator<String> it = prefList.iterator();
				while (it.hasNext()) {
					String prefName = it.next();
					dest.add(new PrefixValue(prefName.split("\\|")[0], prefixHandler
							.getRecordRecordValue(element, rr)));
				}
			}
		}
	}

	private static void relationsOtherRecordsThisRecordToPrefixes(
			List<PrefixValue> dest, RecordRecord rr, String type) {
		List<String> ls = PrefixConverter.getDataSources("OTHER_THIS", type);
		for (String element : ls) {
			List<String> prefList = prefixMap.getPrefixes(type, element);
			if (prefList != null) {
				Iterator<String> it = prefList.iterator();
				while (it.hasNext()) {
					String prefName = it.next();
					dest.add(new PrefixValue(prefName.split("\\|")[0], prefixHandler
							.getRecordRecordValue(element, rr)));
				}
			}
		}
	}

	private static void recordKeywordsToPrefixes(List<PrefixValue> dest,
			MultilingualContentDTO rk, String type) {
		List<String> ls = PrefixConverter.getDataSources("KWS", type);
		for (String element : ls) {
			List<String> prefList = prefixMap.getPrefixes(type, element);
			if (prefList != null) {
				Iterator<String> it = prefList.iterator();
				while (it.hasNext()) {
					String prefName = it.next();
					dest.add(new PrefixValue(prefName.split("\\|")[0], prefixHandler
							.getRecordKeywordsValue(element, rk)));
				}
			}
		}
		
	}

	private static void recordClassToPrefixes(List<PrefixValue> dest,
			Classification rc, String type) {
		List<String> ls = PrefixConverter.getDataSources("CLS", type);
		for (String element : ls) {
			List<String> prefList = prefixMap.getPrefixes(type, element);
			if (prefList != null) {
				Iterator<String> it = prefList.iterator();
				while (it.hasNext()) {
					String prefName = it.next();
					dest.add(new PrefixValue(prefName, prefixHandler
							.getRecordClassValue(element, rc)));
				}
			}
		}
	}
	
	private static List<String> getDataSources(String start, String type) {
		Set<String> all = prefixMap.getPrefixMap(type).keySet();
		List<String> retVal = new ArrayList<String>();
		for (String dataSource : all) {
			if (dataSource.startsWith(start))
				retVal.add(dataSource);
		}
		return retVal;
	}

	private static String extractText(FileDTO docFile) {
		String retVal = "";
		DocTextExtractor docTextExtractor = DocTextExtractorFactory.getDocTextExtractor(
	        docFile.getMime());
	    if (docTextExtractor != null){
	    	File file = new File(FileStorage.getFullPath(docFile));
		    if (!file.isFile()) {
		      log.fatal("File " + file.getAbsolutePath() + " does not exist.");
		      return null;
		    }
		    try {
		      retVal = docTextExtractor.extractText(file);
		    } catch (Exception ex) {
		    	log.fatal(ex);
		    }
	    }
	    return retVal;
	  }
	
	static private PrefixConfig prefixConfig;

	static private PrefixMap prefixMap;

	static private PrefixHandler prefixHandler;

	static {
		try {
			prefixConfig = PrefixConfigFactory.getPrefixConfig();
			prefixMap = prefixConfig.getPrefixMap();
			prefixHandler = prefixConfig.getPrefixHandler();
		} catch (Exception ex) {
		}
	}
	
	private static Log log = LogFactory.getLog(PrefixConverter.class.getName());
}