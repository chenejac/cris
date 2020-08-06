package rs.ac.uns.ftn.informatika.bibliography.utils;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.EmailMessage;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.RecordConsumerListener;

import com.sun.mail.smtp.SMTPTransport;

/**
 * Class for sending e mails
 * 
 * @author chenejac@uns.ac.rs
 */
public class EmailConsumerListener implements MessageListener {

	
	/**
	 * @param session
	 *            session for sending e mail
	 */
	public EmailConsumerListener(Session session) {
		this.session = session;
		log.info("EmailConsumerListener created");
	}

	/**
	 * Sends e mail
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message msg) {
		try {
			ObjectMessage om = (ObjectMessage) msg;
			EmailMessage mess = (EmailMessage)om.getObject();
			log.info("onMessage: sending mail");
			
			javax.mail.Message emailMessage = getMessage(mess);
			
			String prot = "smtp";
			
					    
		    SMTPTransport t = null;
			
		    try{
		    	t = (SMTPTransport)session.getTransport(prot);
		    	t.connect();
		    	t.sendMessage(emailMessage, emailMessage.getAllRecipients());
		    } catch (NoSuchProviderException e) {
		    	log.fatal(e);
			} catch (MessagingException e) {
				log.fatal(e);
			} finally {
		    	try {
					if(t!=null)
						t.close();
				} catch (MessagingException e) {
					log.fatal(e);
				}
		    }

		} catch (JMSException e) {
			log.fatal(e);
		}
	}
	
	private javax.mail.Message getMessage(EmailMessage mess){
		String from = mess.getFrom();
		String to = mess.getTo();
		String cc = mess.getCC();
		String bcc = mess.getBCC();
		String subject = mess.getSubject();
		String text = mess.getText();
		
		String mailer = "smtpsend";
		
		MimeMessage msg = new MimeMessage(session);
	    try {
			msg.setFrom(new InternetAddress(from));
			

			msg.setRecipients(javax.mail.Message.RecipientType.TO,
						InternetAddress.parse(to, false));
			if(cc!=null)
				msg.setRecipients(javax.mail.Message.RecipientType.CC,
						InternetAddress.parse(cc, false));
			if(bcc!=null)
				msg.setRecipients(javax.mail.Message.RecipientType.BCC,
						InternetAddress.parse(bcc, false));
			
			msg.setSubject(subject, "UTF-8");
			msg.setText(text, "UTF-8");
			msg.setHeader("Content-Type", "text/html; charset=UTF-8");
			msg.setHeader("X-Mailer", mailer);
			msg.setSentDate(new Date());
		} catch (AddressException e) {
			log.error(e);
		} catch (MessagingException e) {
			log.error(e);
		}
	    
	    return msg;
	}


	private Session session;

	private static Log log = LogFactory.getLog(RecordConsumerListener.class.getName());
}
