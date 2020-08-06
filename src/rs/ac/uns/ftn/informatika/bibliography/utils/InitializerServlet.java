package rs.ac.uns.ftn.informatika.bibliography.utils;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.backup.IndexTask;
import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.oaipmh.DOISerbiaClient;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.mediator.MediatorService;
import rs.ac.uns.ftn.informatika.bibliography.reports.ReportsServlet;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.RecordConsumerListener;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;

/**
 * Servlet for initialization. Instance of this Servlet is created during
 * deploying application on web server
 * 
 * @author chenejac@uns.ac.rs
 * 
 */
@SuppressWarnings("serial")
public class InitializerServlet extends HttpServlet {

	/**
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig cfg) {
		Retriever.setIndexPath(cfg.getInitParameter("luceneIndex"));
		ReportsServlet.subreportDir = cfg.getInitParameter("subreportDir");
		MediatorService.contextSetPath = cfg.getInitParameter("contextSetPath");
		FileStorage.storageRoot = cfg.getInitParameter("storageRoot");
		FileStorage.documentsPerSet = Integer.parseInt(cfg.getInitParameter("documentPerSet"));
		GeolocationManager.data = new File(cfg.getInitParameter("geolocationData"));
		FileStorage.createStorageRootDir();
		DataSourceFactory.dataSourceName = cfg.getInitParameter("jndiDataSource");
		try {
			 Context ctx = new InitialContext();
			 Context envCtx = (Context) ctx.lookup("java:comp/env");
			 ConnectionFactory cf = (ConnectionFactory)envCtx.lookup(cfg.getInitParameter("jndiFactory"));
			 Queue queue = (Queue)envCtx.lookup(cfg.getInitParameter("jndiQueueEmailer"));
			 CRUDManagedBean.cf = cf;
			 CRUDManagedBean.mailQueue = queue;
			 connEmailer = cf.createConnection();
			 Session mailJmsSession = connEmailer.createSession(false,
					 Session.AUTO_ACKNOWLEDGE);
			 MessageConsumer consumer =  mailJmsSession.createConsumer(queue);
			 String mailhost = "smtp.uns.ac.rs";
			 Properties props = System.getProperties();

			 props.put("mail.smtp.host", mailhost);
			 javax.mail.Session mailSession = javax.mail.Session.getInstance(props,null);
			 consumer.setMessageListener(new EmailConsumerListener(mailSession));
			 connEmailer.start();
		 } catch (NamingException ex) {
			 log.fatal(ex);
		 } catch (JMSException e) {
			 log.fatal(e);
		}
		 
		 new CroSerUtils();  //initialization
		 
		 try {
			 Context ctx = new InitialContext();
			 Context envCtx = (Context) ctx.lookup("java:comp/env");
			 ConnectionFactory cf = (ConnectionFactory)envCtx.lookup(cfg.getInitParameter("jndiFactory"));
			 Queue queue = (Queue)envCtx.lookup(cfg.getInitParameter("jndiQueueUpdater"));
			 CRUDManagedBean.updateQueue = queue;
			 connUpdater = cf.createConnection();
			 Session updateSession = connUpdater.createSession(false,
					 Session.AUTO_ACKNOWLEDGE);
			 MessageConsumer consumer = updateSession.createConsumer(queue);
			 consumer.setMessageListener(new RecordConsumerListener());
			 connUpdater.start();
		 } catch (NamingException ex) {
			 log.fatal(ex);
		 } catch (JMSException e) {
			 log.fatal(e);
		}
		 
		CroSerUtils.dictionaryPath = cfg.getInitParameter("dictionaryRoot");
		CroSerUtils.loadStemDictionary();
		
		Calendar date = new GregorianCalendar();
		date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) + 1);
		date.set(Calendar.HOUR_OF_DAY, 1);
		
		TimerTask publicThesesRemover = new PublicThesesRemover();
		TimerTask publicThesesPARemover = new PublicThesesPARemover();
		//		TimerTask doiSerbiaClient = new DOISerbiaClient();
		 
//		publicThesesRemover.run();
//		publicThesesPARemover.run();
		
    	Timer timer1 = new Timer();
    	timer1.schedule(publicThesesRemover, date.getTimeInMillis() - new GregorianCalendar().getTimeInMillis(), 24l*3600*1000);
    	Timer timer2 = new Timer();
    	timer2.schedule(publicThesesPARemover, date.getTimeInMillis() - new GregorianCalendar().getTimeInMillis(), 24l*3600*1000);
    	//    	Timer timer3 = new Timer();
    	//    	timer3.schedule(doiSerbiaClient, date.getTimeInMillis() + 500000l - new GregorianCalendar().getTimeInMillis(), 24l*3600*1000);
    	
//    	try {
//			IndexTask indexTask = new IndexTask(Retriever.getIndexPath(), DataSourceFactory.getDataSource().getConnection(), false, false, true);
//			indexTask.execute();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	/**
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	@Override
	public void destroy() {
		 try {
			 connEmailer.close();
		 } catch (JMSException e) {
			 log.fatal(e);
		 }
		 try {
			 connUpdater.close();
		 } catch (JMSException e) {
			 log.fatal(e);
		 }
	}


	



	
	private Connection connEmailer;
	
	private Connection connUpdater;
	
	private static Log log = LogFactory.getLog(InitializerServlet.class.getName());
}
