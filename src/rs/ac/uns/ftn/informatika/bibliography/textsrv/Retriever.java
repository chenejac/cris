package rs.ac.uns.ftn.informatika.bibliography.textsrv;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DefaultSimilarity;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocCollector;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JobAdDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
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
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

/**
 * Implements a mARC21Record retriever.
 * 
 * @author Dragan Ivanovic chenejac@uns.ac.rs
 */
public class Retriever {
	

	/**
	 * Executes a select query.
	 * 
	 * @param query
	 *            The Lucene query
	 * @param hitCollector
	 *            hitCollector for retrieving
	 * @return An array of records; an empty array if an error occurred
	 */
	public static List<Record> select(Query query, HitCollector hitCollector) {
		try {
			BooleanQuery.setMaxClauseCount(20000);// zbog heap-a
			Searcher searcher = new IndexSearcher(Retriever.getIndexPath());
			searcher.setSimilarity(new DefaultSimilarity());
			searcher.search(query, hitCollector);
			log.info("Query: " + query);
			ScoreDoc[] hits = null;
			if (hitCollector instanceof AllDocCollector) {
				AllDocCollector allDocCollector = (AllDocCollector) hitCollector;
				List<ScoreDoc> hitsList = allDocCollector.getHits();
				hits = new ScoreDoc[hitsList.size()];
				hits = hitsList.toArray(hits);
			} else if (hitCollector instanceof TopDocCollector) {
				hits = ((TopDocCollector) hitCollector).topDocs().scoreDocs;
			}
			List<Record> retVal = new ArrayList<Record>();
			for (int i = 0; i < hits.length; i++) {
				int docId = hits[i].doc;
				Document doc = searcher.doc(docId);
				Field fieldRecordHTMLRepresentation = doc.getField("RECORDHTMLREPRESENTATION");
				Field fieldRecordHarvardRepresentation = doc.getField("RECORDHARVARDREPRESENTATION");
				Field fieldRecordStringRepresentation = doc.getField("RECORDSTRINGREPRESENTATION");
				Field fieldRecordMARC21Representation = doc.getField("RECORDMARC21REPRESENTATION");
				Field fieldRecordDublinCoreRepresentation = doc.getField("RECORDDUBLINCOREREPRESENTATION");
				Field archived = doc.getField("ARCHIVED");
				Field fieldRecordControlNumber = doc.getField("CN");
				
				Field fieldScopusID = doc.getField("SCOPUSID");
				Field fieldORCID = doc.getField("ORCID");
				Field fieldType = doc.getField("TYPE");
				if ((fieldRecordControlNumber != null) && (fieldRecordStringRepresentation != null) && (fieldType != null)) {
					Record rec = null;
					if(fieldType.stringValue().equals(Types.AUTHOR)) {
						rec = new Person();
						Field jmbg = doc.getField("JMBG");
						if(jmbg != null){
							((Person)rec).setJmbg(jmbg.stringValue());
						}
						
						Field apvnt = doc.getField("APVNT");
						if(apvnt != null){
							((Person)rec).setApvnt(apvnt.stringValue());
						}
						
						Field dp = doc.getField("DP");
						if(dp != null){
							((Person)rec).setDirectPhones(dp.stringValue());
						}
						Field lp = doc.getField("LP");
						if(lp != null){
							((Person)rec).setLocalPhones(lp.stringValue());
						}
					}
					else 
						rec = new Record();
					rec.setDto(ConverterFactory.getDTO(fieldType.stringValue()));
					rec.getDto().setControlNumber(fieldRecordControlNumber.stringValue());
					if((fieldScopusID != null) && (!"".equals(fieldScopusID))){
						rec.getDto().setScopusID(fieldScopusID.stringValue());
						rec.setScopusID(fieldScopusID.stringValue());
					}
					
					if((fieldORCID != null) && (!"".equals(fieldORCID))){
						rec.getDto().setORCID(fieldORCID.stringValue());
						rec.setORCID(fieldORCID.stringValue());
					}
					rec.getMARC21Record().setControlNumber(fieldRecordControlNumber.stringValue());
					rec.getDto().setNotLoaded(true);
					rec.getDto().setRelevanceScore(hits[i].score);
					if(fieldRecordStringRepresentation!=null)
						rec.getDto().setStringRepresentation(fieldRecordStringRepresentation.stringValue());
					if(fieldRecordHTMLRepresentation!=null)
						rec.getDto().setHTMLRepresentation(fieldRecordHTMLRepresentation.stringValue());
					if(fieldRecordHarvardRepresentation!=null)
						rec.getDto().setHarvardRepresentation(fieldRecordHarvardRepresentation.stringValue());
					if(fieldRecordMARC21Representation!=null)
						rec.getDto().setMARC21(fieldRecordMARC21Representation.stringValue());
					if(fieldRecordDublinCoreRepresentation!=null)
						rec.getDto().setDublinCore(fieldRecordDublinCoreRepresentation.stringValue());
					if(archived!=null)
						rec.setArchived(Integer.parseInt(archived.stringValue()));
					if(rec.getDto() instanceof AuthorDTO){
						if (doc.getField("REGISTERED") != null)
							((AuthorDTO)rec.getDto()).setAlreadyRegistered(new Boolean(doc.getField("REGISTERED").stringValue()));
						if (doc.getField("AUTHORUNSDISSERTATIONS") != null)
							((AuthorDTO)rec.getDto()).setAuthorUnsDissertations(new Boolean(doc.getField("AUTHORUNSDISSERTATIONS").stringValue()));
						if (doc.getField("ADVISORUNSDISSERTATIONS") != null)
							((AuthorDTO)rec.getDto()).setAdvisorUnsDissertations(new Boolean(doc.getField("ADVISORUNSDISSERTATIONS").stringValue()));
						if (doc.getField("COMMISSIONMEMBERUNSDISSERTATIONS") != null)
							((AuthorDTO)rec.getDto()).setCommissionMemberUnsDissertations(new Boolean(doc.getField("COMMISSIONMEMBERUNSDISSERTATIONS").stringValue()));
						if (doc.getField("COMMISSIONCHAIRUNSDISSERTATIONS") != null)
							((AuthorDTO)rec.getDto()).setCommissionChairUnsDissertations(new Boolean(doc.getField("COMMISSIONCHAIRUNSDISSERTATIONS").stringValue()));
						if (doc.getField("AUTHORPADISSERTATIONS") != null)
							((AuthorDTO)rec.getDto()).setAuthorPaDissertations(new Boolean(doc.getField("AUTHORPADISSERTATIONS").stringValue()));
						if (doc.getField("ADVISORPADISSERTATIONS") != null)
							((AuthorDTO)rec.getDto()).setAdvisorPaDissertations(new Boolean(doc.getField("ADVISORPADISSERTATIONS").stringValue()));
						if (doc.getField("COMMISSIONMEMBERPADISSERTATIONS") != null)
							((AuthorDTO)rec.getDto()).setCommissionMemberPaDissertations(new Boolean(doc.getField("COMMISSIONMEMBERPADISSERTATIONS").stringValue()));
						if (doc.getField("COMMISSIONCHAIRPADISSERTATIONS") != null)
							((AuthorDTO)rec.getDto()).setCommissionChairPaDissertations(new Boolean(doc.getField("COMMISSIONCHAIRPADISSERTATIONS").stringValue()));
						if (doc.getField("NAMES") != null)
							((AuthorDTO)rec.getDto()).setNames(doc.getField("NAMES").stringValue());
						if (doc.getField("YEAR") != null){
							try {
								((AuthorDTO)rec.getDto()).setYearOfBirth(Integer.parseInt(doc.getField("YEAR").stringValue()));
							} catch (Exception e) {
								System.out.println("CN: " + rec.getDto().getControlNumber());
								e.printStackTrace();
							}
						}
						if (doc.getField("INSTITUTIONNAME") != null)
							((AuthorDTO)rec.getDto()).setInstitutionName(doc.getField("INSTITUTIONNAME").stringValue());
						if (doc.getField("TITLE") != null)
							((AuthorDTO)rec.getDto()).setTitle(doc.getField("TITLE").stringValue());
						if (doc.getField("CURRENTPOSITIONNAME") != null)
							((AuthorDTO)rec.getDto()).setCurrentPositionName(doc.getField("CURRENTPOSITIONNAME").stringValue());
					}
					if(rec.getDto() instanceof OrganizationUnitDTO){
						if (doc.getField("SOMENAME") != null)
							((OrganizationUnitDTO)rec.getDto()).setSomeName(doc.getField("SOMENAME").stringValue());
						if (doc.getField("PLACE") != null)
							((OrganizationUnitDTO)rec.getDto()).setPlace(doc.getField("PLACE").stringValue());
						if (doc.getField("SUPERINSTITUTIONCN") != null){
							((OrganizationUnitDTO)rec.getDto()).setInstitution(new InstitutionDTO());
							((OrganizationUnitDTO)rec.getDto()).getInstitution().setControlNumber(doc.getField("SUPERINSTITUTIONCN").stringValue());
						}
						if (doc.getField("SUPERORGANIZATIONUNITCN") != null){
							((OrganizationUnitDTO)rec.getDto()).setSuperOrganizationUnit(new OrganizationUnitDTO());
							((OrganizationUnitDTO)rec.getDto()).getSuperOrganizationUnit().setControlNumber(doc.getField("SUPERORGANIZATIONUNITCN").stringValue());
						}
					} else if(rec.getDto() instanceof InstitutionDTO){
								if (doc.getField("SOMENAME") != null)
									((InstitutionDTO)rec.getDto()).setSomeName(doc.getField("SOMENAME").stringValue());
								if (doc.getField("PLACE") != null)
									((InstitutionDTO)rec.getDto()).setPlace(doc.getField("PLACE").stringValue());
								if (doc.getField("SUPERINSTITUTIONCN") != null){
									((InstitutionDTO)rec.getDto()).setSuperInstitution(new InstitutionDTO());
									((InstitutionDTO)rec.getDto()).getSuperInstitution().setControlNumber(doc.getField("SUPERINSTITUTIONCN").stringValue());
								}
							} 
					
					if(rec.getDto() instanceof StudyFinalDocumentDTO) { 
						if (doc.getField("AUTHORNAME") != null){
							((StudyFinalDocumentDTO)rec.getDto()).setSomeAuthorName(doc.getField("AUTHORNAME").stringValue());
						}
						if (doc.getField("INSTITUTIONNAME") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setSomeInstitutionName(doc.getField("INSTITUTIONNAME").stringValue());
						if (doc.getField("SOMETITLE") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setSomeTitle(doc.getField("SOMETITLE").stringValue());
						if (doc.getField("SOMERESEARCHAREA") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setSomeResearchArea(doc.getField("SOMERESEARCHAREA").stringValue());
						if (doc.getField("STUDYTYPE") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setStudyType(doc.getField("STUDYTYPE").stringValue());
						if (doc.getField("PUBLICPERIOD") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setPublicPeriod(doc.getField("PUBLICPERIOD").stringValue());
						if (doc.getField("DATEMODIFIED") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setDateModified(Long.parseLong(doc.getField("DATEMODIFIED").stringValue()));
						if (doc.getField("RECORDETDMSREPRESENTATION") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setETDMS(doc.getField("RECORDETDMSREPRESENTATION").stringValue());
						if (doc.getField("UNSDISSERTATION") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setUnsDissertation(new Boolean(doc.getField("UNSDISSERTATION").stringValue()));
						if (doc.getField("PADISSERTATION") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setPaDissertation(new Boolean(doc.getField("PADISSERTATION").stringValue()));
						((StudyFinalDocumentDTO)rec.getDto()).getRegisterEntry().setNotLoaded(true);
						if (doc.getField("REGISTERENTRYHTMLREPRESENTATION") != null)
							((StudyFinalDocumentDTO)rec.getDto()).getRegisterEntry().setHTMLRepresentation(doc.getField("REGISTERENTRYHTMLREPRESENTATION").stringValue());
						if (doc.getField("REGISTERENTRYHTMLREPRESENTATIONEN") != null)
							((StudyFinalDocumentDTO)rec.getDto()).getRegisterEntry().setHTMLRepresentationEn(doc.getField("REGISTERENTRYHTMLREPRESENTATIONEN").stringValue());
						if (doc.getField("RECORDHARVARDREPRESENTATIONEN") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setHarvardRepresentationEn(doc.getField("RECORDHARVARDREPRESENTATIONEN").stringValue());
						if (doc.getField("PRELIMINARYTHESESURL") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setPreliminaryThesesURL(doc.getField("PRELIMINARYTHESESURL").stringValue());
						if (doc.getField("PRELIMINARYSUPPLEMENTURL") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setPreliminarySupplementURL(doc.getField("PRELIMINARYSUPPLEMENTURL").stringValue());
						if (doc.getField("REPORTURL") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setReportURL(doc.getField("REPORTURL").stringValue());
						if (doc.getField("RELATEDPUBLICATIONS") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setRelatedPublicationsHTMLRepresentation(doc.getField("RELATEDPUBLICATIONS").stringValue());
						if (doc.getField("DEFENDED") != null)
							((StudyFinalDocumentDTO)rec.getDto()).setDefendedStatus(doc.getField("DEFENDED").stringValue());
					}
					if(rec.getDto() instanceof PublicationDTO){ 
						if (doc.getField("YEAR") != null)
							((PublicationDTO)rec.getDto()).setPublicationYear(doc.getField("YEAR").stringValue());
						if (doc.getField("FILEURL") != null)
							((PublicationDTO)rec.getDto()).setFileURL(doc.getField("FILEURL").stringValue());
						if ((doc.getField("FILENAME") != null) && (doc.getField("FILEID") != null)){
							((PublicationDTO)rec.getDto()).setFileName(doc.getField("FILENAME").stringValue());
							((PublicationDTO)rec.getDto()).setFileId(Integer.parseInt(doc.getField("FILEID").stringValue()));
						}
						if(doc.getField("FILELICENSE") != null)
							((PublicationDTO)rec.getDto()).setFileLicense(doc.getField("FILELICENSE").stringValue());
						if (doc.getField("SUPPLEMENTURL") != null)
							((PublicationDTO)rec.getDto()).setSupplementURL(doc.getField("SUPPLEMENTURL").stringValue());
						if (doc.getField("WCIURL") != null){
							((PublicationDTO)rec.getDto()).setWordCloudImageURL(doc.getField("WCIURL").stringValue());
						}
						if (doc.getField("URI") != null)
							((PublicationDTO)rec.getDto()).setUri(doc.getField("URI").stringValue());
						
					}
					
					if(rec.getDto() instanceof ConferenceDTO){ 
						if (doc.getField("SOMENAME") != null)
							((ConferenceDTO)rec.getDto()).setSomeName(doc.getField("SOMENAME").stringValue());
						if (doc.getField("YEAR") != null)
							((ConferenceDTO)rec.getDto()).setYear(Integer.parseInt(doc.getField("YEAR").stringValue()));
						if (doc.getField("PLACE") != null)
							((ConferenceDTO)rec.getDto()).setPlace(doc.getField("PLACE").stringValue());
						if (doc.getField("NUMBER") != null)
							((ConferenceDTO)rec.getDto()).setNumber(doc.getField("NUMBER").stringValue());
					}
					
					if(rec.getDto() instanceof JobAdDTO){ 
						if (doc.getField("SOMENAME") != null)
							((JobAdDTO)rec.getDto()).setSomeName(doc.getField("SOMENAME").stringValue());
					}
					
					if(rec.getDto() instanceof JournalDTO){ 
						if (doc.getField("SOMENAME") != null)
							((JournalDTO)rec.getDto()).setSomeName(doc.getField("SOMENAME").stringValue());
						if (doc.getField("ISSN") != null)
							((JournalDTO)rec.getDto()).setIssn(doc.getField("ISSN").stringValue());
					}
					
					if(rec.getDto() instanceof MonographDTO){ 
						if (doc.getField("SOMETITLE") != null)
							((MonographDTO)rec.getDto()).setSomeTitle(doc.getField("SOMETITLE").stringValue());
						if (doc.getField("ISBN") != null)
							((MonographDTO)rec.getDto()).setIsbn(doc.getField("ISBN").stringValue());
//						if (doc.getField("URI") != null)
//							((MonographDTO)rec.getDto()).setUri(doc.getField("URI").stringValue());
					}
					
//					if(rec.getDto() instanceof PaperMonographDTO){ 
//						if (doc.getField("URI") != null)
//							((PaperMonographDTO)rec.getDto()).setUri(doc.getField("URI").stringValue());
//					}
					
					if(rec.getDto() instanceof PaperJournalDTO){ 
						if (doc.getField("PAPERTYPE") != null)
							((PaperJournalDTO)rec.getDto()).setPaperType(doc.getField("PAPERTYPE").stringValue());
//						if (doc.getField("URI") != null)
//							((PaperJournalDTO)rec.getDto()).setUri(doc.getField("URI").stringValue());
					}
					
					if(rec.getDto() instanceof PaperProceedingsDTO){ 
						if (doc.getField("PAPERTYPE") != null)
							((PaperProceedingsDTO)rec.getDto()).setPaperType(doc.getField("PAPERTYPE").stringValue());
//						if (doc.getField("URI") != null)
//							((PaperProceedingsDTO)rec.getDto()).setUri(doc.getField("URI").stringValue());
					}
					
					if(rec.getDto() instanceof ProceedingsDTO){ 
						if (doc.getField("SOMETITLE") != null)
							((ProceedingsDTO)rec.getDto()).setSomeTitle(doc.getField("SOMETITLE").stringValue());
						if (doc.getField("CONFERENCENAME") != null)
							((ProceedingsDTO)rec.getDto()).setConferenceName(doc.getField("CONFERENCENAME").stringValue());
						if (doc.getField("ISBN") != null)
							((ProceedingsDTO)rec.getDto()).setIsbn(doc.getField("ISBN").stringValue());
					}
					
					if(rec.getDto() instanceof RuleBookImplementationDTO){
						if (doc.getField("SOMENAME") != null)
							((RuleBookImplementationDTO)rec.getDto()).setSomeName(doc.getField("SOMENAME").stringValue());
						if (doc.getField("SUPERRULEBOOKCN") != null){
							((RuleBookImplementationDTO)rec.getDto()).setSuperRuleBook(new RuleBookDTO());
							((RuleBookImplementationDTO)rec.getDto()).getSuperRuleBook().setControlNumber(doc.getField("SUPERRULEBOOKCN").stringValue());
						}
						if (doc.getField("SUPERRULEBOOKIMPLEMENTATIONCN") != null){
							((RuleBookImplementationDTO)rec.getDto()).setSuperRuleBookImplementation(new RuleBookImplementationDTO());
							((RuleBookImplementationDTO)rec.getDto()).getSuperRuleBookImplementation().setControlNumber(doc.getField("SUPERRULEBOOKIMPLEMENTATIONCN").stringValue());
						}
					} else if(rec.getDto() instanceof RuleBookDTO){
								if (doc.getField("SOMENAME") != null)
									((RuleBookDTO)rec.getDto()).setSomeName(doc.getField("SOMENAME").stringValue());
					}
					
					if(rec.getDto() instanceof SpecVerLstDTO){ 
						if (doc.getField("SOMENAME") != null)
							((SpecVerLstDTO)rec.getDto()).setSomeName(doc.getField("SOMENAME").stringValue());
					}
					
//					MARC21Record mARC21Record = MARC21RecordFactory.fromFullFormatString(fieldRecord
//							.stringValue());
//					rec.setMARC21Record(mARC21Record);
//					Field fieldRecordClasses = doc.getField("RECORDCLASSES");
//					if(fieldRecordClasses != null){
//						rec.setRecordClasses(fieldRecordClasses.stringValue());
//					}
//					Field fieldRecordKeywords = doc.getField("RECORDKEYWORDS");
//					if(fieldRecordKeywords != null){
//						rec.setRecordKeywords(fieldRecordKeywords.stringValue());
//					}
//					Field fieldRelationsOtherThis = doc.getField("RELATIONSOTHERTHIS");
//					if(fieldRelationsOtherThis != null){
//						rec.setRelationsOtherRecordsThisRecord(fieldRelationsOtherThis.stringValue());
//					}
//					Field fieldRelationsThisOther = doc.getField("RELATIONSTHISOTHER");
//					if(fieldRelationsThisOther != null){
//						rec.setRelationsThisRecordOtherRecords(fieldRelationsThisOther.stringValue());
//					}
//					rec.loadDTOFromMARC21();
//					if(rec.getConverter() instanceof AuthorConverter){
//						Field alreadyRegistered = doc.getField("REGISTERED");
//						((AuthorDTO)rec.getDto()).setAlreadyRegistered(new Boolean(alreadyRegistered.stringValue()));
//					}
//					rec.loadFromDatabase();
					retVal.add(rec);
				}
			}
			searcher.close();
			
			return retVal;
		} catch (Exception e) {
			log.error(e);
			return new ArrayList<Record>();
		}
	}
	
	public static Document selectDocument(String controlNumber)  {
	    try {
		      Document doc=null;
		      BooleanQuery.setMaxClauseCount(20000);//zbog heap-a
		      Query query = new TermQuery(new Term("CN", controlNumber));
		      Searcher searcher = new IndexSearcher(Retriever.getIndexPath());
		      searcher.setSimilarity(new DefaultSimilarity());
		      TopDocCollector collector = new TopDocCollector(1);
			  searcher.search(query, collector);
			  log.info("Query: " + query);
			  ScoreDoc[] hits = collector.topDocs().scoreDocs;     
		      if (hits.length == 1){
				  doc = searcher.doc(hits[0].doc);
		      }
		      searcher.close();
		      return doc;   
	    } catch (Exception ex) {
	    	log.error("selectDocument: " + ex.getMessage());
	    	return null;
	    }
	  }


	private static String indexPath;

	/**
	 * @return the indexPath
	 */
	public static String getIndexPath() {
		if(indexPath == null){
			ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.backup.connection");
			indexPath = rb.getString("luceneIndex");
		}
		return indexPath;
	}



	/**
	 * @param indexPath the indexPath to set
	 */
	public static void setIndexPath(String indexPath) {
		Retriever.indexPath = indexPath;
	}

	private static Log log = LogFactory.getLog(Retriever.class.getName());
}
