package rs.ac.uns.ftn.informatika.bibliography.utils;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.PublicThesesManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;


/**
 * @author chenejac@uns.ac.rs
 */
public class PublicThesesRemover extends TimerTask{
	
	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	@Override
	public void run() {
		
		log.info("Public theses remover started");
		
		long time = new GregorianCalendar().getTimeInMillis();
		String whereClause = PublicThesesManagedBean.PUBLIC_THESES_WHERE_CLAUSE;
		
		List<Record> records = recordDAO.getRecordsIdsFromDatabaseByWhereClause(whereClause);
		if(records != null){
			ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
					"messages.messages-administration", new Locale("sr")); //lokalizacija nije bitna
			String message = "";
			String errorMessage = "";
			for (Record record : records) {
				StudyFinalDocumentDTO theses = (StudyFinalDocumentDTO) record.getDto();
				if((theses.getReport() != null) && (theses.getPreliminaryTheses() != null)){
					if(time - theses.getReport().getDateModified().getTimeInMillis() > (30l*24*60*60*1000)){
						log.info("Public period finished: " + theses);//email Miri Brkovic
						
						
						if(theses.getDefendedOn() == null)
							theses.getReport().setLicense("Temporary available - not defended");
						else {
							theses.getReport().setLicense(null);
							theses.getReport().setNote("Public period finished!");
						}

						if(theses.getDefendedOn() == null)
							theses.getPreliminaryTheses().setLicense("Temporary available - not defended");
						else {
							theses.getPreliminaryTheses().setLicense(null);
							theses.getPreliminaryTheses().setNote("Public period finished!");
						}
						
						if(theses.getPreliminarySupplement() != null){
							if(theses.getDefendedOn() == null)
								theses.getPreliminarySupplement().setLicense("Temporary available - not defended");
							else {
								theses.getPreliminarySupplement().setLicense(null);
								theses.getPreliminarySupplement().setNote("Public period finished!");
							}
						}
//						theses.getReport().setLicense(null);
//						theses.getReport().setNote("Public period finished!");
//
//						theses.getPreliminaryTheses().setLicense(null);
//						theses.getPreliminaryTheses().setNote("Public period finished!");
//						if((theses.getPreliminaryTheses() != null) && (theses.getPreliminaryTheses().getLicense() != null) && (theses.getPreliminaryTheses().getLicense().equals("Temporary available")))
//							theses.getPreliminaryTheses().setLicense(null);

							// Da li ima potrebe da se rad rucno doda u listu arhive ???
						
							
						boolean updateSuccess = false;
						record.setArchived(0);
						try{
							updateSuccess = recordDAO.update(record);
						}catch(Exception e){
						}
						//mira uvek dobija obavestenje
						message += "<br/>" + theses.getAuthor().getName() + " - " + theses.getInstitution().getSomeName() +" - " + theses.getSomeTitle() +
								" (" + theses.getPublicPeriod() + ")";
						if(!updateSuccess){
							//poruka administratorima u slucaju da nesto nije u redu
							errorMessage += "<br/>" + theses.getAuthor().getName() + " - " + theses.getInstitution().getSomeName() +" - " + theses.getSomeTitle() + " (" + theses.getPublicPeriod() + ")";
						}
					}
				}
				theses.setNotLoaded(true);
			}
			if(!message.equals("")){
				PublicThesesManagedBean.PUBLIC_THESES_LAST_MODIFICATION_DATE = new Date();
				message = rbAdministration.getString("administration.notification.publicTheses.expired") + message;
				PublicThesesManagedBean.publicThesesExpiredMessage(message);
			}
			if(!errorMessage.equals("")){
				errorMessage = rbAdministration.getString("administration.notification.publicTheses.error") + errorMessage;
				PublicThesesManagedBean.publicThesesExpiredErrorMessage(errorMessage);
			}
		}
		log.info("Public theses remover finished");
	}
	
	protected static Log log = LogFactory.getLog(PublicThesesRemover.class.getName());
	
}

