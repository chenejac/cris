package rs.ac.uns.ftn.informatika.bibliography.textsrv;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.dao.CommissionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;


/**
 * Class for creating, updating, deleting index in Lucene index files
 * asynchronously.
 * 
 * @author chenejac@uns.ac.rs
 */
public class RecordConsumerListener implements MessageListener {

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	private RecordDAO personDAO = new RecordDAO(new PersonDB());
	
	private CommissionDAO commissionDAO = new CommissionDAO();
	
	
	/**
	 * 
	 */
	public RecordConsumerListener() {
		log.info("RecordConsumerListener created");
	}

	/**
	 * Creates, updates, deletes index
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message msg) {
		try {
			ObjectMessage om = (ObjectMessage) msg;
			String operation = om.getStringProperty("operation");
			Object obj = om.getObject();
			log.info("onRecordMessage: " + operation + " operation, record: " + obj);
			if (obj instanceof RecordDTO) {
				RecordDTO dto = (RecordDTO)obj;
				if ("add".equals(operation)) {
					if(obj instanceof StudyFinalDocumentDTO){
						StudyFinalDocumentDTO sfd = (StudyFinalDocumentDTO) obj;
						sfd.getAuthor().getRecord().loadFromDatabase();
						sfd.getAuthor().getRecord().setModifier("CRISUNSupdate");
						sfd.getAuthor().getRecord().setLastModificationDate(new GregorianCalendar());
						personDAO.update(sfd.getAuthor().getRecord());
						for (AuthorDTO advisor : sfd.getAdvisors()) {
							advisor.getRecord().loadFromDatabase();
							advisor.getRecord().setModifier("CRISUNSupdate");
							advisor.getRecord().setLastModificationDate(new GregorianCalendar());
							personDAO.update(advisor.getRecord());
						}
						for (AuthorDTO commissionMember : sfd.getCommitteeMembers()) {
							commissionMember.getRecord().loadFromDatabase();
							commissionMember.getRecord().setModifier("CRISUNSupdate");
							commissionMember.getRecord().setLastModificationDate(new GregorianCalendar());
							personDAO.update(commissionMember.getRecord());
						}
					} 
					if(obj instanceof PaperProceedingsDTO){
						List<RecordDTO> records = new ArrayList<RecordDTO>();
						records.add((RecordDTO)obj);
						commissionDAO.reevaluateByCommissionFromAuthorsAffiliation(records, null);
					}
					if(obj instanceof PaperJournalDTO){
						List<RecordDTO> records = new ArrayList<RecordDTO>();
						records.add((RecordDTO)obj);
						commissionDAO.reevaluateByCommissionFromAuthorsAffiliation(records, null);
					}
					log.info("MARC21Record related objects updated, Control number: "
							+ dto.getControlNumber());
				} else 
				if ("update".equals(operation)) {
					if(obj instanceof StudyFinalDocumentDTO){
						StudyFinalDocumentDTO sfd = (StudyFinalDocumentDTO) obj;
						sfd.getAuthor().getRecord().loadFromDatabase();
						sfd.getAuthor().getRecord().setModifier("CRISUNSupdate");
						sfd.getAuthor().getRecord().setLastModificationDate(new GregorianCalendar());
						personDAO.update(sfd.getAuthor().getRecord());
						for (AuthorDTO advisor : sfd.getAdvisors()) {
							advisor.getRecord().loadFromDatabase();
							advisor.getRecord().setModifier("CRISUNSupdate");
							advisor.getRecord().setLastModificationDate(new GregorianCalendar());
							personDAO.update(advisor.getRecord());
						}
						for (AuthorDTO commissionMember : sfd.getCommitteeMembers()) {
							commissionMember.getRecord().loadFromDatabase();
							commissionMember.getRecord().setModifier("CRISUNSupdate");
							commissionMember.getRecord().setLastModificationDate(new GregorianCalendar());
							personDAO.update(commissionMember.getRecord());
						}
					}
					if(obj instanceof PaperProceedingsDTO){
						List<RecordDTO> records = new ArrayList<RecordDTO>();
						records.add((RecordDTO)obj);
						commissionDAO.reevaluateByCommissionFromAuthorsAffiliation(records, null);
					}
					if(obj instanceof PaperJournalDTO){
						List<RecordDTO> records = new ArrayList<RecordDTO>();
						records.add((RecordDTO)obj);
						commissionDAO.reevaluateByCommissionFromAuthorsAffiliation(records, null);
					}
					update(dto);
					log.info("MARC21Record related objects updated, Control number: "
							+ dto.getControlNumber());
				}
			} 
//			else if ((obj instanceof String) && "delete".equals(operation)) {
//				String recControlNumber = (String) obj;
//				indexer.delete(recControlNumber);
//				log.info("MARC21Record deleted, Control number: " + recControlNumber);
//			}
		} catch (JMSException e) {
			log.fatal(e);
		}
	}
	
	public boolean update(RecordDTO dto){
		Query query = null;
		if (dto instanceof OrganizationUnitDTO){
			query = new TermQuery(new Term("OUCN", dto.getControlNumber()));
		} else if (dto instanceof InstitutionDTO){
			query = new TermQuery(new Term("INCN", dto.getControlNumber()));
		} else if (dto instanceof AuthorDTO){
			BooleanQuery bq = new BooleanQuery();
			bq.add(new TermQuery(new Term("AUCN", dto.getControlNumber())), Occur.SHOULD);
			bq.add(new TermQuery(new Term("EDCN", dto.getControlNumber())), Occur.SHOULD);
			bq.add(new TermQuery(new Term("ADCN", dto.getControlNumber())), Occur.SHOULD);
			bq.add(new TermQuery(new Term("CMCN", dto.getControlNumber())), Occur.SHOULD);
			query = new BooleanQuery();
			((BooleanQuery)query).add(bq, Occur.MUST);
		} else if (dto instanceof JournalDTO){
			query = new TermQuery(new Term("JOCN", dto.getControlNumber()));
		} else if (dto instanceof ConferenceDTO) {
			query = new TermQuery(new Term("COCN", dto.getControlNumber()));
		} else if (dto instanceof ProceedingsDTO) {
			query = new TermQuery(new Term("PRCN", dto.getControlNumber()));
		} else if (dto instanceof MonographDTO) {
			query = new TermQuery(new Term("MOCN", dto.getControlNumber()));
		}
		
		if(query!=null){
			List<Record> records = recordDAO.getDTOs(query, new AllDocCollector(true));
			for (int i=0; i<records.size(); ) {
				Record record = records.get(i);
				record.loadMARC21FromDatabase();
				String temp1 = record.toString();
				record.loadDTOFromMARC21();
				record.loadMARC21FromDTO();
				String temp2 = record.toString();
				if(! temp1.equals(temp2)){
					boolean updateResult;
					record.setModifier("CRISUNSupdate");
					record.setLastModificationDate(new GregorianCalendar());
					synchronized (RecordConsumerListener.class) {
						updateResult = recordDAO.update(record); 
					}
					if(updateResult)
						update(record.getDto());
					else 
						return false;
				}
				records.remove(i);
			}
		} 
		 
		return true;
	}


	private static Log log = LogFactory.getLog(RecordConsumerListener.class.getName());
}
